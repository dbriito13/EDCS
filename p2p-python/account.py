from node import Node
import requests

class Account(Node):

    def __init__(self, host, port, name):
        super(Account, self).__init__(host, port)
        self.balance = 0.0
        self.name = name
        self.post_request(name, host, port)

    def post_request(self, username, ip, port):
        url = "https://evening-headland-54924.herokuapp.com/add?username="+username+"&ip="+ip+"&port="+str(port)
        print(url)
        r = requests.post(url = url)
        if r == "Err":
            raise Exception("The user already exists in the system")

    def get_request(self,username):
        url = "https://evening-headland-54924.herokuapp.com/username?username="+username
        r = requests.get(url = url)
        if r is None:
            raise Exception("The user you are trying to send money to doesn't exist")

        data = r.text
        ip = data.split(':')[0]
        port = data.split(':')[1]
        return ip, port

    def transferMoney(self, username, amount):
        ip, port = self.get_request(username)
        self.send_to_node(amount, ip, int(port))
        self.lock.acquire()
        self.balance = self.balance - float(amount)
        self.lock.release()

    def node_message(self, data):
        '''
        node_message: This function is called when a node receives a text message,
            in it we will raise a lock, and update the balance of the user's Account
            data: String of the data received by the node.
        '''
        amount = float(data)
        self.lock.acquire()
        print("Lock acquired!")
        self.balance += amount
        self.lock.release()
        print("Lock released!")
        print("Your resulting balance after the operation is: " + str(self.balance))
        
    
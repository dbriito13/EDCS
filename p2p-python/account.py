from node import Node
import requests
import os.path

class Account(Node):

    def __init__(self, host, port, name):
        super(Account, self).__init__(host, port)
        self.filename = name+".txt"
        if os.path.isfile(self.filename):
            #If the file already exists then we read the file to find the amount
            f = open(self.filename, "r")
            self.balance = float(f.read())
        else:
            #If the file doesn't exist we create it and write the initial balance of 0.0
            self.balance = 0.0
            f = open(self.filename, "w")
            f.write("0.0")
        print("Hola")
        f.close()
        self.name = name
        self.post_request(name, host, port)

    def post_request(self, username, ip, port):
        url = "https://evening-headland-54924.herokuapp.com/dsfsdfdf?username="+username+"&ip="+ip+"&port="+str(port)
        print(url)
        r = requests.post(url = url)
        print(r.status_code)
        if r.status_code > 299:
            print("Changing to secondary server...")
            urlAuxSecondary = "https://fast-atoll-78029.herokuapp.com/add?username="+username+"&ip="+ip+"&port="+str(port)
            resp = requests.post(url = urlAuxSecondary)
        if r == "Err":
            raise Exception("The user already exists in the system")

    def get_request(self,username):
        url = "https://evening-headland-54924.herokuapp.com/badssdasndsad?username="+username
        r = requests.get(url = url)
        if r is None:
            raise Exception("The user you are trying to send money to doesn't exist")
        if r.status_code > 299:
            print("Changing to secondary server in GET...")
            urlAuxSecondary = "https://fast-atoll-78029.herokuapp.com/username?username="+username
            resp = requests.get(url = urlAuxSecondary)
            data = resp.text
            ip = data.split(':')[0]
            port = data.split(':')[1]
            return ip, port

        data = r.text
        ip = data.split(':')[0]
        port = data.split(':')[1]
        return ip, port

    def transferMoney(self, username, amount):
        ip, port = self.get_request(username)
        if float(amount) > self.balance:
            raise Exception("You do not have sufficient funds to make this transfer")
        elif float(amount) <= 0:
            raise Exception("You can not transfer a negative or null amount of money")
        else:
            self.send_to_node(amount, ip, int(port))
            self.lock.acquire()
            self.balance = self.balance - float(amount)
            f = open(self.filename, "w")
            f.write(self.balance)
            f.close()
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
        f = open(self.filename, "w")
        f.write(self.balance)
        f.close()
        self.lock.release()
        print("Lock released!")
        print("Your resulting balance after the operation is: " + str(self.balance))
        
    
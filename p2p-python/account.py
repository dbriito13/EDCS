from node import Node

class Account(Node):

    def __init__(self, host, port, name):
        super(Account, self).__init__(host, port)
        self.balance = 0.0
        self.name = name

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
        
    
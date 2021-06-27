import sys
import time
sys.path.insert(0, '..') # Import the files where the modules are located

from account import Account
from node import Node

def threaded_function1(arg):
	node_1.transferMoney("Receiver", 100)

    

node_1 = Account('127.0.0.1', 8080, "Daniel")
node_2 = Account('127.0.0.1', 18080, "Lucas")
node_dest = Account('127.0.0.1', 19800, "Receiver")

time.sleep(1)

node_dest.start()

time.sleep(1)

node_1.transferMoney("Receiver")

node_dest.stop()
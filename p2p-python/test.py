import sys
import time
sys.path.insert(0, '..') # Import the files where the modules are located

from account import Account
from node import Node

node_1 = Account('127.0.0.1', 8080, "Daniel")
node_2 = Account('127.0.0.1', 18080, "Lucas")

time.sleep(1)

node_1.start()
node_2.start()

time.sleep(1)

node_2.send_to_node("400", '127.0.0.1', 8080)

node_1.stop()
node_2.stop()
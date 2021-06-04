import sys

sys.path.insert(0, '..') 
from p2pnetwork.account import Account

def create_Account(ip, port, name):
	new_account = Account(ip, port, name)
	print("Account created correctly ready to connect to others and start sending money!")
	return new_account

def disconnect_toAccount(Account, ip, port):
	Account.disconnect_with_node(ip, port)
	print("Account removed from your connected accounts!")

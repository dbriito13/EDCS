import sys

sys.path.insert(0, '..') 
from p2pnetwork.account import Account

def connect_toAccount(Account, ip, port):
	res = Account.connect_with_node(ip, port)
	if(res == -1):
		print("You can't connect to yourself!")
		return 0

	if(res == 0):
		print("You have already added this account to your connected accounts!")
		return 0

	#Else the connection worked so we will show the user that the connection was made
	print("The connection was succesful!")
	return Account.outbound_nodes

def create_Account(ip, port, name):
	new_account = Account(ip, port, name)
	print("Account created correctly ready to connect to others and start sending money!")
	return new_account

def disconnect_toAccount(Account, ip, port):
	Account.disconnect_with_node(ip, port)
	print("Account removed from your connected accounts!")

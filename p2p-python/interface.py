import tkinter as tk
import socket
import time
from account import *
from tkinter import messagebox

def on_closing():
    """if messagebox.askokcancel("Quit", "Do you want to quit?"):
        if current_Account:
            current_Account.stop()"""
    window.destroy()

def trButtonPressed():
    global current_Account;
    current_Account = Account(entry1.get(), int(entry2.get()), entry3.get())
    global IP;
    global PORT;
    global name;
    IP = entry1.get()
    PORT = entry2.get()
    name = entry3.get()
    current_Account.start()

    time.sleep(2)
    frame_login.destroy()
    frame_transfer.pack()
    label_ff.config(text = name + ", on " + IP + ":" + PORT)
    return current_Account

def dpButtonPressed():
    frame_login.destroy()
    frame_deposit.pack()

def okButtonPressed():
    print("Adios")


def sendMoneyPressed():
    print("pressed send money")
    current_Account.send_to_node(entryM_M.get(), entry1_M.get(), int(entry2_M.get()))

window = tk.Tk()

window.protocol("WM_DELETE_WINDOW", on_closing)

frame_transfer = tk.Frame(window)
label_ff = tk.Label(frame_transfer,text="Money Sending App")
label_amount = tk.Label(frame_transfer, text = "0.0")

#Now the other row displaying the fields to enter money 
subtitle_2 = tk.Label(frame_transfer, text="Choose the IP and port of the user you want to send money to")

label1_M = tk.Label(frame_transfer,text="IP of receiver")
entry1_M = tk.Entry(frame_transfer)

label2_M = tk.Label(frame_transfer,text="port of receiver")
entry2_M = tk.Entry(frame_transfer)

labelM_M = tk.Label(frame_transfer,text="amount")
entryM_M = tk.Entry(frame_transfer)

button_sendMoney = tk.Button(frame_transfer, text ="Send Money", command = sendMoneyPressed)

subtitle_2.grid(row = 1, column = 0,padx=10, pady=10)
label1_M.grid(row = 2, column = 0,padx=10)
entry1_M.grid(row = 3, column = 0,padx=10, pady=10)
label2_M.grid(row = 4, column = 0,padx=10)
entry2_M.grid(row = 5, column = 0,padx=10, pady=10)
labelM_M.grid(row = 6, column = 0,padx=10, pady=10)
entryM_M.grid(row = 7, column = 0, padx=10, pady=10)
button_sendMoney.grid(row = 8, column = 0, padx=10)


frame_deposit = tk.Frame(window)
label_deposit = tk.Label(frame_deposit, text="Money Sending App")

label1_d = tk.Label(frame_deposit, text="Amount")
entry1_d = tk.Label(frame_deposit)
OK_button = tk.Button(frame_deposit, text="Accept", command = okButtonPressed)

label1_d.grid(row = 1, column = 0,padx=10, pady=10)
entry1_d.grid(row = 2, column = 0,padx=10)
OK_button.grid(row=3, column=0, padx=0)


#frame where a user will be prompted to create an account and select an operation
frame_login = tk.Frame(window)
label = tk.Label(frame_login,text="Money Sending App")

label1 = tk.Label(frame_login,text="IP")
entry1 = tk.Entry(frame_login)

label2 = tk.Label(frame_login,text="port")
entry2 = tk.Entry(frame_login)

label3 = tk.Label(frame_login,text="name")
entry3 = tk.Entry(frame_login)

trButton = tk.Button(frame_login, text ="Transfer", command = trButtonPressed)
dpButton = tk.Button(frame_login, text="Deposit", command = dpButtonPressed)

label1.grid(row = 1, column = 1,padx=10, pady=10)
entry1.grid(row = 2, column = 1,padx=10)
label2.grid(row = 3, column = 1,padx=10, pady=10)
entry2.grid(row = 4, column = 1,padx=10)
label3.grid(row = 5, column = 1,padx=10, pady=10)
entry3.grid(row = 6, column = 1,padx=10, pady=10)
trButton.grid(row = 7, column = 0, padx=10)
dpButton.grid(row = 7, column = 2, padx=10)
frame_login.pack()


window.mainloop()
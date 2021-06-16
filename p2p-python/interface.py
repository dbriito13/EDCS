import tkinter as tk
import socket
import time
from account import *
from tkinter import messagebox

def on_closing():
    if messagebox.askokcancel("Quit", "Do you want to quit?"):
        if current_Account:
            current_Account.stop()
        window.destroy()

def buttonPressed():
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
    frame_login.pack_forget()
    frame_functionality.pack()
    label_ff.config(text = name + ", on " + IP + ":" + PORT)
    return current_Account


def sendMoneyPressed():
    print("pressed send money")
    current_Account.send_to_node(entryM_M.get(), entry1_M.get(), int(entry2_M.get()))

window = tk.Tk()

window.protocol("WM_DELETE_WINDOW", on_closing)

frame_functionality = tk.Frame(window)
label_ff = tk.Label(frame_functionality,text="Money Sending App")
label_amount = tk.Label(frame_functionality, text = "0.0")

#Now the other row displaying the fields to enter money 
subtitle_2 = tk.Label(frame_functionality, text="Choose the IP and port of the user you want to send money to")

label1_M = tk.Label(frame_functionality,text="IP of receiver")
entry1_M = tk.Entry(frame_functionality)

label2_M = tk.Label(frame_functionality,text="port of receiver")
entry2_M = tk.Entry(frame_functionality)

labelM_M = tk.Label(frame_functionality,text="amount")
entryM_M = tk.Entry(frame_functionality)

button_sendMoney = tk.Button(frame_functionality, text ="Send Money", command = sendMoneyPressed)

subtitle_2.grid(row = 1, column = 0,padx=10, pady=10)
label1_M.grid(row = 2, column = 0,padx=10)
entry1_M.grid(row = 3, column = 0,padx=10, pady=10)
label2_M.grid(row = 4, column = 0,padx=10)
entry2_M.grid(row = 5, column = 0,padx=10, pady=10)
labelM_M.grid(row = 6, column = 0,padx=10, pady=10)
entryM_M.grid(row = 7, column = 0, padx=10, pady=10)
button_sendMoney.grid(row = 8, column = 0, padx=10)


#frame where a user will be prompted to create an account
frame_login = tk.Frame(window)

label = tk.Label(frame_login,text="Money Sending App")
label.pack()

label1 = tk.Label(frame_login,text="IP")
entry1 = tk.Entry(frame_login)

label2 = tk.Label(frame_login,text="port")
entry2 = tk.Entry(frame_login)

label3 = tk.Label(frame_login,text="name")
entry3 = tk.Entry(frame_login)

button = tk.Button(frame_login, text ="Create Account", command = buttonPressed)

label1.pack()
entry1.pack()
label2.pack()
entry2.pack()
label3.pack()
entry3.pack()
button.pack()

frame_login.pack()

window.mainloop()
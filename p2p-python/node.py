import socket
import sys
import time
import threading
import random
import hashlib

class Node(threading.Thread):

    def __init__(self, host, port):
        super(Node, self).__init__()

        # When this flag is set, the node will stop and close
        self.terminate_flag = threading.Event()

        # Server details, host (or ip) to bind to and the port
        self.host = host
        self.port = port

        # Start the TCP/IP server
        self.sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.init_server()

        # Debugging on or off!
        self.debug = True

        print("Node created correctly!")

    def init_server(self):
        """Initialization of the TCP/IP server to receive connections. It binds to the given host and port."""
        self.sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        self.sock.bind((self.host, self.port))
        self.sock.settimeout(10.0)
        self.sock.listen(1)

    def send_to_node(self, data, host, port):
        if host == self.host and port == self.port:
            return -1;

        try:
            #Create socket and bind it to our host and port
            sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM);
            print("connecting to %s port %s" % (host, port))
            sock.connect((host,port))

            #Now we send the data through the created socket
            sock.sendall(data.encode('utf-8'))
            print("Sent message to node!")
            sock.close()

        except Exception as e:
            print("TcpServer.connect_with_node: Could not connect with node. (" + str(e) + ")")

    def stop(self):
        self.terminate_flag.set()

    def run(self):
        while not self.terminate_flag.is_set():  # Check whether the thread needs to be closed
            try:
                print("Node: Wait for incoming connection")
                connection, client_address = self.sock.accept()

                message = ""

                while True:
                    data = connection.recv(1);
                    if not data:
                        break
                    data = data.decode('utf-8')
                    message += data

                #Convert the message to a Double that we can add to the amount
                amount = float(message)
                print(amount)

                # Now that we have the message received we will do sth with it
                print("received message: " + message + "message length: " + str(len(message)))
                #We receive the amount sent by another user and call the function add Money which will be implemented in the Account class
            except socket.timeout:
                print('Node: Connection timeout!')

            except Exception as e:
                raise e

            time.sleep(0.01)

        print("Node stopping...")

        time.sleep(1)

        self.sock.settimeout(None)  
        self.sock.close()
        print("Node stopped")
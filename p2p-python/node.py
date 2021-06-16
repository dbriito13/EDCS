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
        self.sock = self.init_server()

        self.lock = threading.Lock()

        print("Node created correctly!")

    def init_server(self):
        """
        Initializes the socket and sets its options, then returns the socket
        """
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM);
        s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        s.bind((self.host, self.port))
        s.settimeout(10.0)
        s.listen(1)
        return s;

    def send_to_node(self, data, host, port):
        '''
        send_to_node connects to the host and port combination given and sends a message containing the data parameter

        data: String of the data we want to transmit as message
        host: IP address of the node we want to connect to
        port: Listening port of the node we want to connect to
        return: 0 if the execution and sending of the message went smoothly
            errors: -1, if we are trying to send a message to ourselves
                    -2, if there is an error with the socket connection

        '''
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

            #Space to receive ACK?

            return 0;

        except Exception as e:
            print("TcpServer.connect_with_node: Could not connect with node. (" + str(e) + ")")
            return -2;

    def stop(self):
        '''
        stops the node from listening to requests
        '''
        self.terminate_flag.set()

    def run(self):
        '''
        run: Loop that listens for incoming requests, accepts them and reads the message they sent.
             It later calls on the node_message function which takes care of higher level functionality
        '''
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

                #Now that we have the message received we will do sth with it
                print("received message: " + message + ", message length: " + str(len(message)))
                #We receive the amount sent by another user and call the function add Money which will be implemented in the Account class
                self.node_message(str(message))
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
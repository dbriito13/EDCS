import socket
import sys
import time
import threading
import random
import hashlib

from p2pnetwork.nodeconnection import NodeConnection

class Node(threading.Thread):

    def __init__(self, host, port):
        super(Node, self).__init__()

        # When this flag is set, the node will stop and close
        self.terminate_flag = threading.Event()

        # Server details, host (or ip) to bind to and the port
        self.host = host
        self.port = port

        # Nodes that have established a connection with this node
        self.nodes_inbound = []  # Nodes that are connect with us N->(US)->N

        # Nodes that this nodes is connected to
        self.nodes_outbound = []  # Nodes that we are connected to (US)->N

        # Create a unique ID for each node.
        # TODO: A fixed unique ID is required for each node, node some random is created, need to think of it.
        id = hashlib.sha512()
        t = self.host + str(self.port) + str(random.randint(1, 99999999))
        id.update(t.encode('ascii'))
        self.id = id.hexdigest()



        # Start the TCP/IP server
        self.sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.init_server()

        # Message counters to make sure everyone is able to track the total messages
        self.message_count_send = 0
        self.message_count_recv = 0
        self.message_count_rerr = 0

        # Debugging on or off!
        self.debug = True

    def debug_print(self, message):
        """When the debug flag is set to True, all debug messages are printed in the console."""
        if self.debug:
            print("DEBUG: " + message)

    def init_server(self):
        """Initialization of the TCP/IP server to receive connections. It binds to the given host and port."""
        print("Initialisation of the Node on port: " + str(self.port) + " on node (" + self.id + ")")
        self.sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        self.sock.bind((self.host, self.port))
        self.sock.settimeout(10.0)
        self.sock.listen(1)

    def send_to_node(self, data, host, port):
        for node in self.nodes_outbound:
            if node.host == host and node.port == port:
                try:
                    node.send(data)
                    #Now we wait for the node to send ACK back
                except Exception as e:
                    self.debug_print("Node send_to_node: Error while sending data to the node (" + str(e) + ")")

    def connect_with_node(self, host, port):
        if host == self.host and port == self.port:
            return -1

        # Check if node is already connected with this node!
        for node in self.nodes_outbound:
            if node.host == host and node.port == port:
                return 0

        try:
            sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            self.debug_print("connecting to %s port %s" % (host, port))
            sock.connect((host, port))

            # Basic information exchange (not secure) of the id's of the nodes!
            sock.send(self.id.encode('utf-8')) # Send my id to the connected node
            connected_node_id = sock.recv(1024).decode('utf-8') # When a node is connected, it sends it id!

            thread_client = NodeConnection(self, sock, connected_node_id, host, port)
            thread_client.start()

            self.nodes_outbound.append(thread_client)
            self.outbound_node_connected(thread_client)

        except Exception as e:
            self.debug_print("TcpServer.connect_with_node: Could not connect with node. (" + str(e) + ")")

    def stop(self):
        self.node_request_to_stop()
        self.terminate_flag.set()

    def run(self):
        while not self.terminate_flag.is_set():  # Check whether the thread needs to be closed
            try:
                self.debug_print("Node: Wait for incoming connection")
                connection, client_address = self.sock.accept()
                
                # Basic information exchange (not secure) of the id's of the nodes!
                connected_node_id = connection.recv(1024).decode('utf-8') # When a node is connected, it sends it id!
                connection.send(self.id.encode('utf-8')) # Send my id to the connected node

                thread_client = NodeConnection(self, connection, connected_node_id, client_address[0], client_address[1])
                thread_client.start()

                self.nodes_inbound.append(thread_client)

                self.inbound_node_connected(thread_client)
                
            except socket.timeout:
                self.debug_print('Node: Connection timeout!')

            except Exception as e:
                raise e

            time.sleep(0.01)

        print("Node stopping...")
        for t in self.nodes_inbound:
            t.stop()

        for t in self.nodes_outbound:
            t.stop()

        time.sleep(1)

        for t in self.nodes_inbound:
            t.join()

        for t in self.nodes_outbound:
            t.join()

        self.sock.settimeout(None)  
        self.sock.close()
        print("Node stopped")

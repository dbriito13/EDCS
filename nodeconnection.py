import socket
import sys
import time
import threading
import random
import hashlib
import json

class NodeConnection(threading.Thread):

    def __init__(self, main_node, sock, id, host, port):
        super(NodeConnection, self).__init__()

        self.host = host
        self.port = port
        self.main_node = main_node
        self.sock = sock
        self.terminate_flag = threading.Event()

        # The id of the connected node
        self.id = id

        # End of transmission character for the network streaming messages.
        self.EOT_CHAR = 0x04.to_bytes(1, 'big')

        # Datastore to store additional information concerning the node.
        self.info = {}

        self.main_node.debug_print("NodeConnection.send: Started with client (" + self.id + ") '" + self.host + ":" + str(self.port) + "'")

    def send(self, data, encoding_type='utf-8'):
        """Send the data to the connected node. The data can be pure text (str), dict object (send as json) and bytes object.
           When sending bytes object, it will be using standard socket communication. A end of transmission character 0x04 
           utf-8/ascii will be used to decode the packets ate the other node."""
        if isinstance(data, str):
            self.sock.sendall( data.encode(encoding_type) + self.EOT_CHAR )

        elif isinstance(data, dict):
            try:
                json_data = json.dumps(data)
                json_data = json_data.encode(encoding_type) + self.EOT_CHAR
                self.sock.sendall(json_data)

            except TypeError as type_error:
                self.main_node.debug_print('This dict is invalid')
                self.main_node.debug_print(type_error)

            except Exception as e:
                print('Unexpected Error in send message')
                print(e)

        elif isinstance(data, bytes):
            bin_data = data + self.EOT_CHAR
            self.sock.sendall(bin_data)

        else:
            self.main_node.debug_print('datatype used is not valid plese use str, dict (will be send as json) or bytes')

    def send_ACK(self):
        self.sock.sendall("ACK".encode('utf-8'))

    def stop(self):
        """Terminates the connection and the thread is stopped."""
        self.terminate_flag.set()

    def parse_packet(self, packet):
        """Parse the packet and determines wheter it has been send in str, json or byte format. It returns
           the according data."""
        try:
            packet_decoded = packet.decode('utf-8')

            try:
                return json.loads(packet_decoded)

            except json.decoder.JSONDecodeError:
                return packet_decoded

        except UnicodeDecodeError:
            return packet

    def ACK_received():
        print("We received ACK message")

    # Required to implement the Thread. This is the main loop of the node client.
    def run(self):
        self.sock.settimeout(10.0)          
        buffer = b'' # Hold the stream that comes in!

        while not self.terminate_flag.is_set():
            chunk = b''

            try:
                chunk = self.sock.recv(1) 

            except socket.timeout:
                self.main_node.debug_print("NodeConnection: timeout")

            except Exception as e:
                self.terminate_flag.set()
                self.main_node.debug_print('Unexpected error')
                self.main_node.debug_print(e)

            if chunk != b'':
                buffer += chunk
                eot_pos = buffer.find(self.EOT_CHAR)

                while eot_pos > 0:
                    packet = buffer[:eot_pos]
                    buffer = buffer[eot_pos + 1:]

                    self.main_node.message_count_recv += 1
                    if(eot_pos == 3): ##The message is ACK
                        ACK_received()
                    else:
                        self.main_node.node_message( self, self.parse_packet(packet))

                    eot_pos = buffer.find(self.EOT_CHAR)

            time.sleep(0.01)

        self.sock.settimeout(None)
        self.sock.close()
        self.main_node.debug_print("NodeConnection: Stopped")

    def set_info(self, key, value):
        self.info[key] = value

    def get_info(self, key):
        return self.info[key]

    def __str__(self):
        return 'NodeConnection: {}:{} <-> {}:{} ({})'.format(self.main_node.host, self.main_node.port, self.host, self.port, self.id)

    def __repr__(self):
        return '<NodeConnection: Node {}:{} <-> Connection {}:{}>'.format(self.main_node.host, self.main_node.port, self.host, self.port)

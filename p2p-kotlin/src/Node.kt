import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.*
import kotlin.IllegalArgumentException
import kotlin.NoSuchElementException
import kotlin.concurrent.thread

open class Node(_NodeAddress : NodeAddress) {
    val address: NodeAddress = _NodeAddress
    val sock : ServerSocket
    private var running : Boolean = true;
    private var debug : Boolean = true;

    //Constructor
    init {
        //Here we will initialize the listening socket
        val endPointAddress = InetSocketAddress(InetAddress.getByName(_NodeAddress.host), _NodeAddress.port)
        sock = ServerSocket()
        //Bind the socket to the specified listening socket address (host + port)
        sock.bind(endPointAddress)
        //Set the socket timeout
        sock.soTimeout = 2000
        debugPrint("Node Created correctly! At IP: ${_NodeAddress.host} and port: ${_NodeAddress.port}\n");
    }

    fun listenConnections(): Int{
        startListening()
        thread(start = true){
            while(running){
                debugPrint("Listening for incoming connections...\n")
                //While our p2p node is running we will accept incoming connections, with each one going into a different thread
                try {
                    val ClientSocket = sock.accept();
                    debugPrint("Connection Accepted from IP: ${ClientSocket.inetAddress.hostAddress} and port: ${ClientSocket.port}\n");
                    //If the connection is accepted we will create a new thread to deal with the incomming message
                    thread(start = true){
                        readConnection(ClientSocket)
                    }
                }catch (e: SocketTimeoutException){
                    debugPrint("Socket Timed out, restarting listening process...\n");
                }
            }
        }
        return 0;
    }

    private fun readConnection(ClientSocket : Socket): String? {
        //read_connection deals with incoming connections
        try {
            val inputReader = BufferedReader(InputStreamReader(ClientSocket.getInputStream()));
            val message = inputReader.readLine();
            //Now, for the moment we will simply print the message
            receiveFunds(message);
            return message
        }catch (e: NoSuchElementException){
            debugPrint("Error when reading the incoming message! No line was found when reading\n");
            e.printStackTrace();
            return  "err";
        }catch (e: NotDoubleAmount){
            debugPrint("Sent amount is not a number!");
            e.printStackTrace();
            return "err";
        }
    }

    open fun receiveFunds(amount : String): Int{
        debugPrint("Function to be overriden by child class");
        return -1;
    }

    fun debugPrint(text : String){
        //Simple function which prints information about the state of a Node if the variable debug is set to true
        if(debug){
            print("Debug:$text");
        }
    }

    fun sendMessage(_NodeAddress: NodeAddress, message: String): Int {
        if (_NodeAddress == this.address){
            debugPrint("Error! Trying to send a message to yourself! Check IP and port combination\n");
            val errMsg = "Can't send a message to yourself\n"
            throw SameInetAddressException(errMsg);
        }
        try {
            val outGoingSocket = Socket()
            val endPointAddress = InetSocketAddress(InetAddress.getByName(_NodeAddress.host), _NodeAddress.port)
            outGoingSocket.connect(endPointAddress);
            //Sending the message
            val output = PrintWriter(outGoingSocket.getOutputStream(), true)
            output.write(message);
            output.flush();
            output.close();
            debugPrint("Message Sent correctly!\n")
            return 0;
        }catch (e: IllegalArgumentException){
            debugPrint("The port value ${_NodeAddress.port} is outside of the permitted values\n")
            e.printStackTrace();
            return -1;
        }catch (e: IOException){
            debugPrint("There was an error when connecting to IP: ${_NodeAddress.host} and port: ${_NodeAddress.port}\n")
            e.printStackTrace();
            return -1;
        }
    }

    fun stopListening(){
        debugPrint("Stopping Node from listening to messages...")
        this.running = false;
    }

    fun startListening(){
        this.running = true
    }

    fun shutdown(){
        debugPrint("Shutting down Node...")
        stopListening()
        this.sock.close();
    }


}
//import com.sun.security.ntlm.Server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Node implements Runnable {
    private String host;
    private int port;
    private ServerSocket ServerSocket;
    private int terminate_flag;

    public Node(String host, int port) throws IOException{
        this.host = host;
        this.port = port;
        this.terminate_flag = 0;
        InetAddress addr = InetAddress.getByName(host);
        this.ServerSocket = new ServerSocket(port,50, addr);
        this.ServerSocket.setSoTimeout(10*1000);
        System.out.println("Created Node at IP: " + this.host + ", port: " + this.port);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        //Here we will try the functionality associated with the Node class
        //Creation of Node
        Node node = new Node("127.0.0.1", 18080);
        Thread t = new Thread(node);
        t.start();

        //Node node2 = new Node("127.0.0.1", 18080);
        //node2.send_message("HOLA", "127.0.0.1",8080);

        TimeUnit.SECONDS.sleep(1);

        node.stop_node();
        //node2.stop_node();
    }

    @Override
    public void run() {
        /**
         * run: Loop that waits for incoming socket connections and deals with them accordingly
         *
         */
        while(terminate_flag == 0){
            System.out.println("Node: Waiting for incoming connections...");
            try {
                Socket clientSocket = this.ServerSocket.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                //Now we receive the UTF-8 String sent by the client
                String str = (String)in.readLine();
                System.out.println("message= " + str);
                this.addAmount(str);

            }catch (java.net.SocketTimeoutException e){
                System.out.println("Socket timed out, restarting listening process...");
            }
            catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void addAmount(String amount){
        System.out.println("Placeholder method. Should be implemented in child class");
    }

    public void send_message(String message, String host, int port){
        Socket ConnectionSocket;
        try {
            ConnectionSocket = new Socket(host,port);
            //After we are connected to the socket we will send the message
            PrintWriter writer = new PrintWriter(ConnectionSocket.getOutputStream(), true);
            //We write the message we want to send in the Data Output stream
            writer.write(message);
            writer.flush();
            writer.close();
            System.out.println("Sent message:" + message + " to receiver at, IP:" + host + ", port: " + port);
            ConnectionSocket.close();
        } catch (IOException e) {
            System.out.println("Error when connecting to IP:" + host + ", port: " + port);
            e.printStackTrace();
        }

    }

    public void stop_node(){
        System.out.println("Stopping node execution");
        this.terminate_flag = 1;
    }

    public void restart_node(){
        System.out.println("Restarting node!...");
        System.out.println("Listening for requests again! IP:" + this.host + "port: " + this.port);
        this.terminate_flag = 0;
    }

    //GETTERS AND SETTERS

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public java.net.ServerSocket getServerSocket() {
        return ServerSocket;
    }

    public void setServerSocket(java.net.ServerSocket serverSocket) {
        ServerSocket = serverSocket;
    }

    public int getTerminate_flag() {
        return terminate_flag;
    }

    public void setTerminate_flag(int terminate_flag) {
        this.terminate_flag = terminate_flag;
    }
}


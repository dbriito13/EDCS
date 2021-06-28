//import com.sun.xml.internal.ws.api.message.ExceptionHasMessage;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Account extends Node{
    private Double amount;
    private String name;
    private ReentrantLock mutex;
    private String filename;

    public Account(String host, int port, String name) throws Exception {
        super(host, port);
        //We will set the starting amount as 0
        this.amount = 0.0;
        this.name = name;
        this.filename = name+".txt";
        File file = new File(this.filename);
        Boolean iscreated = file.createNewFile();
        if(iscreated){
            FileWriter myWriter = new FileWriter(this.filename);
            myWriter.write("0.0");
            myWriter.close();
        }else {
            //We extract the amount from the txt file and set it to the balance
            Scanner myReader = new Scanner(file);
            this.amount = Double.valueOf(myReader.nextLine());
        }
        this.mutex = new ReentrantLock();
        String response = this.post_request(name, host, Integer.toString(port));
        System.out.println(response);
    }

    public int moneyTransfer(String username, Double amount) throws Exception {
        String res = get_request(username);
        if(res == null){
            System.out.println("Error! Receiving user not found!");
            return -1;
        }
        String __host = res.split(":")[0];
        String __port = res.split(":")[1];
        return this.send_money(__host, Integer.parseInt(__port), amount);
    }

    public String get_request(String username) throws Exception {
        URL url = new URL("https://evening-headland-54924.herokuapp.com/username?username=" + username);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        int status = con.getResponseCode();
        if(status>299){
            System.out.println("Changing server!...");
            URL urlSec = new URL("https://fast-atoll-78029.herokuapp.com/username?username=" + username);
            HttpURLConnection conSec = (HttpURLConnection) urlSec.openConnection();
            conSec.setRequestMethod("GET");

            BufferedReader inSec = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLineSec;
            StringBuffer contentSec = new StringBuffer();
            while ((inputLineSec = inSec.readLine()) != null) {
                contentSec.append(inputLineSec);
            }
            inSec.close();
            if(contentSec.toString() == null){
                throw new Exception("User couldn't be found out");
            }
            return contentSec.toString();
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        if(content.toString() == null){
            throw new Exception("User couldn't be found out");
        }
        return content.toString();
    }

    public String post_request(String username, String ip, String port) throws Exception {
        URL url = new URL("https://evening-headland-54924.herokuapp.com/add?username=" + username + "&ip=" +ip+ "&port="+port);
        System.out.println(url);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");

        int status = con.getResponseCode();
        if(status > 299){
            BufferedReader inSec = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLineSec;
            StringBuffer contentSec = new StringBuffer();
            while ((inputLineSec = inSec.readLine()) != null) {
                contentSec.append(inputLineSec);
            }
            inSec.close();
            if(contentSec.toString().equals("Err")){
                //User already exists
                throw new Exception("User already exists");
            }
            return contentSec.toString();
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        if(content.toString().equals("Err")){
            //User already exists
            throw new Exception("User already exists");
        }
        return content.toString();
    }

    public int send_money(String account_ip, int account_port, Double amount){
        String message;
        //First we check that we have enough money to send this amount
        if(amount < 0.0){
            System.out.println("Error! You can't deposit a negative sum into an account.");
            return -1;
        }
        if(this.amount < amount){
            System.out.println("Money transfer to Account: " + account_ip + ":" + account_port + "failed!: Insufficient Funds!");
            return -1;
        }
        message = Double.toString(amount);
        try{
            this.mutex.lock();
            this.amount = this.amount - amount;
            FileWriter myWriter = new FileWriter(this.filename);
            myWriter.write(this.amount.toString());
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.mutex.unlock();
        }
        this.send_message(message, account_ip, account_port);
        return 0;
    }

    @Override
    public void addAmount(String amount){
        //Convert the amount to a Double value
        Double amount_d = Double.parseDouble(amount);
        if(amount_d < 0){
            System.out.println("Error! You can't add a negative sum of money into an account.");
            return;
        }
        System.out.println("Adding " + amount_d.toString() + " € to your account!");
        //Here we will implement the mutex lock
        try{
            this.mutex.lock();
            //Here the update of the balance occurs
            this.amount += amount_d;
            FileWriter myWriter = new FileWriter(this.filename);
            myWriter.write(this.amount.toString());
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.mutex.unlock();
        }
    }

    public void removeAmount(Double amount){
        if(amount < 0){
            System.out.println("Error! You can't remove a negative sum of money into an account.");
            return;
        }
        System.out.println("Taking out " + amount.toString() + " € from your account!");
        //Here we will implement the mutex lock
        try{
            this.mutex.lock();
            this.amount = this.amount - amount;
            FileWriter myWriter = new FileWriter(this.filename);
            myWriter.write(this.amount.toString());
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.mutex.unlock();
        }
    }

    public void depositAmount(Double amount){
        if(amount < 0){
            System.out.println("Error! You can't deposit a negative sum into an account.");
            return;
        }
        System.out.println("Initiating sample deposit of money into account...");
        this.addAmount(amount.toString());
    }

    public static boolean confirmation(String s) throws UnknownHostException {
        String prove = "";
        String aux = InetAddress.getLocalHost() + "";
        System.out.println(aux);
        prove += aux.substring(aux.indexOf("/")+1);
        if (s.equals(prove)){
            System.out.println("Valid IP direction");
            return true;
        }
        System.out.println("Invalid IP direction");
        return false;
    }

    public static void main(String[] args) {
        Account accountDest = null;
        try {
            accountDest = new Account("127.0.0.1", 19011, "destinationUser");
        } catch (Exception e) {
            e.printStackTrace();
        }


        Account account1 = null;
        try {
            account1 = new Account("127.0.0.1", 18082, "usuarioprueba1");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Account account2 = null;
        try {
            account2 = new Account("127.0.0.1", 18083, "usuarioprueba2");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Thread t1 = new Thread(accountDest);
        t1.start();

        final Account finalAccount1 = account1;
        Thread tsend1 = new Thread(new Runnable() {
            @Override
            public void run() {
                assert finalAccount1 != null;
                try {
                    finalAccount1.moneyTransfer("destinationUser", 10.0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        final Account finalAccount2 = account2;
        Thread tsend2 = new Thread(new Runnable() {
            @Override
            public void run() {
                assert finalAccount2 != null;
                try {
                    finalAccount2.moneyTransfer("destinationUser", 10.0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        tsend1.start();
        tsend2.start();

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        t1.interrupt();
        accountDest.stop_node();


    }

    private Double getAmount() {
        return this.amount;
    }
}

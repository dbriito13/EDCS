import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.DoubleBuffer;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Account extends Node{

    private Double amount;
    private String name;
    private ReentrantLock mutex;

    public Account(String host, int port, String name) throws IOException {
        super(host, port);
        //We will set the starting amount as 0
        this.amount = 0.0;
        this.name = name;
        this.mutex = new ReentrantLock();
    }

    public Double getAmount(){
        return this.amount;
    }

    public int send_money(String account_ip, int account_port, Double amount){
        String message;
        if(amount < 0.0){
            System.out.println("Error! You can't deposit a negative sum into an account.");
            return -1;
        }
        if(this.amount < amount){
            System.out.println("Money transfer to Account: " + account_ip + ":" + account_port + "failed!: Insufficient Funds!");
            System.out.println("Your current amount is " + this.amount.toString());
            return -1;
        }
        message = Double.toString(amount);
        try{
            this.mutex.lock();
            this.amount = this.amount - amount;
        }finally {
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
        } finally {
            this.mutex.unlock();
        }

        System.out.println("Your current amount is " + this.amount.toString());
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
        }finally {
            this.mutex.unlock();
        }

        System.out.println("Your current amount is " + this.amount.toString());
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

    public static void main(String[] args) throws IOException, InterruptedException {
        Account account1 = new Account("127.0.0.1", 19011, "Daniel");
        Account account2 = new Account("127.0.0.1", 18082, "Lucas");

        Thread t1 = new Thread(account1);
        t1.start();


        Thread t2 = new Thread(account2);
        t2.start();

        account1.depositAmount(200.0);

        //Sending of the messages between the accounts1
        account1.send_money("127.0.0.1", 18082, 100.0);
        TimeUnit.SECONDS.sleep(35);

        t1.interrupt();
        t2.interrupt();
        account1.stop_node();
        account2.stop_node();

    }
}

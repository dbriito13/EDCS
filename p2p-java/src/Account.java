import java.io.IOException;
import java.nio.DoubleBuffer;

public class Account extends Node{
    private Double amount;
    private String name;

    public Account(String host, int port, String name) throws IOException {
        super(host, port);
        //We will set the starting amount as 0
        this.amount = 0.0;
        this.name = name;
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
        this.send_message(message, account_ip, account_port);
        return 0;
    }

    public void addAmount(Double amount){
        if(amount < 0){
            System.out.println("Error! You can't add a negative sum of money into an account.");
            return;
        }
        System.out.println("Adding " + amount.toString() + " € to your account!");
        //Here we will implement the mutex lock
        this.amount += amount;
    }

    public void removeAmount(Double amount){
        if(amount < 0){
            System.out.println("Error! You can't remove a negative sum of money into an account.");
            return;
        }
        System.out.println("Taking out " + amount.toString() + " € from your account!");
        //Here we will implement the mutex lock
        this.amount = this.amount - amount;
    }

    public void depositAmount(Double amount){
        if(amount < 0){
            System.out.println("Error! You can't deposit a negative sum into an account.");
            return;
        }
        System.out.println("Initiating sample deposit of money into account...");
        this.addAmount(amount);
    }

    public static void main(String[] args) throws IOException {
        Account account1 = new Account("127.0.0.1", 8080, "Daniel");
        //Account account2 = new Account("127.0.0.1", 18080, "Lucas");

        Thread t1 = new Thread(account1);
        t1.start();

        //Thread t2 = new Thread(account2);
        //t2.start();

        account1.depositAmount(200.0);

        //Sending of the messages between the accounts1
        account1.send_money("127.0.0.1", 18000, 100.0);

        t1.interrupt();
        //t2.interrupt();
        account1.stop_node();
        //account2.stop_node();

    }
}
import com.sun.corba.se.impl.orbutil.concurrent.Mutex
import java.util.concurrent.locks.ReentrantLock

class Account(_NodeAddress: NodeAddress, _username: String) : Node(_NodeAddress){
    val username = _username;
    var mutex = ReentrantLock();
    var balance = 0.0;

    fun insertAmount(amount : Double){
        this.balance += amount;
    }

    private fun removeAmount(amount: Double){
        this.balance -= amount;
    }

    override fun receiveFunds(amount: String): Int {
        //First we will deal with the incoming String and try to convert it to a Double
        print("Receiving funds...")
        val amountDeposit = amount.toDoubleOrNull();
        if(amountDeposit == null){
            super.debugPrint("The sent message contains some error and can't be parsed to a Double value");
            return -1;
        }
        if (amountDeposit < 0.0){
            super.debugPrint("Can't receive an incoming transaction for a negative amount of money")
            return -1;
        }
        super.debugPrint("The received amount via message is ${amountDeposit}")
        //If it can be correctly parsed to a Double then we add it to the user's balance.
        try {
            mutex.lock();
            insertAmount(amountDeposit);
        }finally {
            mutex.unlock();
        }
        return 0;
    }

    fun transferMoney(_NodeAddress: NodeAddress, amount: Double): Int{
        //Money transfer function, first we will check that the users amount is enough to perform the transaction
        if (this.balance < amount){
            super.debugPrint("Failed transaction! Current funds: $balance not enough to send ${amount}.");
            return -1;
        }
        //If we have enough funds we initiate the transfer
        super.sendMessage(_NodeAddress, amount.toString())
        //If we receive the desired ACK we will subtract money from our end
        try {
            mutex.lock();
            removeAmount(amount);
        }finally {
            mutex.unlock()
        }
        return 0;
    }

    fun checkBalance(): Double{
        print("Balance of $username is: $balance €\n");
        return balance;
    }
}
import java.lang.Exception
import java.util.concurrent.locks.ReentrantLock


class Account(_NodeAddress: NodeAddress, _username: String) : Node(_NodeAddress){
    val username = _username;
    var mutex = ReentrantLock();
    var balance = 0.0;

    init {
        //When we create an account we will have to do a POST request to the /add endpoint.
        val requestHandler : Request = Request();

        val response = requestHandler.postRequest(_username,_NodeAddress.host,_NodeAddress.port);
        if(response == "OK"){
            debugPrint("User added correctly to our system!\n");
        }else{
            throw UserAlreadyExists("The user is already registered in our database, choose a different username!")
        }
    }

    fun insertAmount(amount : Double){
        this.balance += amount;
    }

    private fun removeAmount(amount: Double){
        this.balance -= amount;
    }

    override fun receiveFunds(amount: String): Int {
        //First we will deal with the incoming String and try to convert it to a Double
        print("Receiving funds...\n")
        val amountDeposit = amount.toDoubleOrNull()
        if(amountDeposit == null){
            super.debugPrint("The sent message contains some error and can't be parsed to a Double value\n");
            return -1;
        }
        if (amountDeposit < 0.0){
            super.debugPrint("Can't receive an incoming transaction for a negative amount of money\n")
            return -2;
        }
        super.debugPrint("The received amount via message is ${amountDeposit}\n")
        //If it can be correctly parsed to a Double then we add it to the user's balance.
        try {
            mutex.lock();
            insertAmount(amountDeposit)
        }finally {
            mutex.unlock();
            return 0;
        }
    }

    fun transferMoney(_NodeAddress: NodeAddress, amount: Double): Int{
        //Money transfer function, first we will check that the users amount is enough to perform the transaction
        if (this.balance < amount){
            super.debugPrint("Failed transaction! Current funds: $balance not enough to send ${amount}.");
            throw NotEnoughFunds("Exception. Account's funds aren't enough to perform operation")
        }
        //If we have enough funds we initiate the transfer
        val res = super.sendMessage(_NodeAddress, amount.toString())
        //If we receive the desired ACK we will subtract money from our end
        if (res == -1){ throw Exception("error when sending money") }
        try {
            mutex.lock();
            removeAmount(amount);
        }finally {
            mutex.unlock()
        }
        return res;
    }

    fun transferMoneyToUser(_username: String, amount: Double): Int{
        //first we do a get request to get this users IP and port
        val requestHandler : Request = Request();
        val response = requestHandler.getRequest(_username);
        //Now the response will hold the data of the GET request.
        val array: List<String> = response.split(':');
        val _host = array[0];
        val _port = array[1].toInt();
        print("The received user has the following info: IP:$_host, and port:$_port\n");

        //Now we build the NodeAddress of the destination user with this information
        //Now we build the NodeAddress of the destination user with this information
        val address = NodeAddress(_host,_port)
        //Finally we call the underlying function that handles the sending of the money to an address
        return transferMoney(address,amount);
    }

    fun checkBalance(): Double{
        print("Balance of $username is: $balance €\n");
        return balance;
    }
}
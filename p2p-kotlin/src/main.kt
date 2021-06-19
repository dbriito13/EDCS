fun main() {
    //creation of listening address
    val nodeAddress1 = NodeAddress("127.0.0.1", 18800);
    val username1 = "Daniel"
    val account1 = Account(nodeAddress1, username1);

    val nodeAddress2 = NodeAddress("127.0.0.1", 19800);

    Thread.sleep(1000);
    account1.insertAmount(1000.0);
    account1.transferMoney(nodeAddress2, 200.0)
    //Now that account1 is listening we will sleep for a bit to give us time to try out send
    Thread.sleep(10*1000)
}
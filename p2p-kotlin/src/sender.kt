fun main(){
    val nodeAddress2 = NodeAddress("127.0.0.1", 19800);
    val username2 = "Lucas"
    //Creation of Account
    val account2 = Account(nodeAddress2, username2);

    account2.listenConnections();
}
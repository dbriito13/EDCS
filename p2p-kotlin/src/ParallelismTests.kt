import kotlin.concurrent.thread

fun main() {
    /*RECEIVER OF THE MONEY*/
    val destUsername = "danibrito13"
    var nodeAddressDest = NodeAddress("127.0.0.1", 15500);
    var accountDest = Account(nodeAddressDest, destUsername)
    accountDest.listenConnections()

    /*SENDER OF MONEY*/
    var nodeAddress1 = NodeAddress("127.0.0.1", 33880);
    var username1 = "testSubjectP1$"
    var account1 = Account(nodeAddress1, username1)

    /*SENDER OF MONEY*/
    var nodeAddress2 = NodeAddress("127.0.0.1", 33881);
    var username2 = "testSubjectP2$"
    var account2 = Account(nodeAddress2, username2)

    thread(start = true) {
        account1.transferMoneyToUser(destUsername, 2.0)
    }
    thread(start = true) {
        account2.transferMoneyToUser(destUsername, 5.0)
    }
    Thread.sleep(1000)
    accountDest.shutdown();
}
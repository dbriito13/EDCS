import org.junit.Test
import java.net.InetAddress
import kotlin.concurrent.thread
import kotlin.test.assertFailsWith

class AccountTests {
    @Test
    fun testCreation(){
        //Tests the correct creation of Account
        val nodeAddress = NodeAddress("127.0.0.1", 31113);
        val username = "tt"
        //Creation of Account
        val account = Account(nodeAddress, username);
        //Check the account was correctly created and has a balance of 0.0€
        assert(account.username == username);
        assert(account.balance == 0.0);
        assert(account.sock.inetAddress == InetAddress.getByName(nodeAddress.host))
        assert(account.sock.localPort == nodeAddress.port)
    }

    @Test
    fun testNotEnoughFunds(){
        val nodeAddress1 = NodeAddress("127.0.0.1", 18080);
        val username1 = "Daniel"
        //Creation of Account
        val account1 = Account(nodeAddress1, username1);

        val nodeAddress2 = NodeAddress("127.0.0.1", 19876);
        val username2 = "Lucas"
        //Creation of Account
        val account2 = Account(nodeAddress2, username2);

        //account1 has 0.0 so it shouldn't be able to perform any money transfer
        assertFailsWith<NotEnoughFunds> { account2.transferMoney(nodeAddress1, 100.0); }
    }

    @Test
    fun testSendNotDoubleAmount(){
        val nodeAddress1 = NodeAddress("127.0.0.1", 33333);
        val username1 = "errorNotDouble"
        //Creation of Account
        val account1 = Account(nodeAddress1, username1);
        assertFailsWith<NotParseableToDouble> {
            account1.listenConnections();

            val nodeAddress2 = NodeAddress("127.0.0.1", 19800);
            //Creation of Account
            val account2 = Node(nodeAddress2);

            Thread.sleep(1000);

            account2.sendMessage(nodeAddress1, "This String is clearly not a Double!")

            Thread.sleep(1000)

            account1.checkState()
        }
    }

    @Test
    fun testSendMoney(){
        val nodeAddress1 = NodeAddress("127.0.0.1", 38080);
        val username1 = "sender"
        //Creation of Account
        val account1 = Account(nodeAddress1, username1);

        val nodeAddress2 = NodeAddress("127.0.0.1", 39800);
        val username2 = "receiver"
        //Creation of Account
        val account2 = Account(nodeAddress2, username2);

        account2.insertAmount(1000.0);
        account1.listenConnections();

        Thread.sleep(1000);

        thread(start = true) {
            account2.transferMoney(nodeAddress1, 100.0);
        }
        Thread.sleep(5000);

        account1.checkBalance()
        account2.checkBalance()
    }

    @Test
    fun testSendMoneyToUsername(){
        val nodeAddress1 = NodeAddress("127.0.0.1", 48000);
        val username1 = "senderTest"
        //Creation of Account
        val account1 = Account(nodeAddress1, username1);

        val nodeAddress2 = NodeAddress("127.0.0.1", 49800);
        val username2 = "receiverTest"
        //Creation of Account
        val account2 = Account(nodeAddress2, username2);

        account2.insertAmount(1000.0);
        account1.listenConnections();

        Thread.sleep(1000);

        account2.transferMoneyToUser(username1, 100.0);
        Thread.sleep(7000);
    }

    @Test
    fun testAccountRegisteredToDiscoveryServer(){
        val nodeAddress = NodeAddress("127.0.0.1", 23880);
        val username = "testSubject"
        //Creation of Account
        val account = Account(nodeAddress, username);
        //Check the account was correctly created and has a balance of 0.0€
        val requestHandler = Request()
        val response = requestHandler.getRequest("testSubject");
        val aux = response.split(":")
        val ipReq = aux[0];
        val portReq = aux[1];
        assert(ipReq == nodeAddress.host);
        assert(portReq.toInt() == nodeAddress.port)
    }

    @Test
    fun testParallelism(){
        val destUsername = "danibrito13"
        for(i in (0..50)) {
            thread(start = true) {
                var nodeAddress = NodeAddress("127.0.0.1", 23880 + i);
                var username = "testSubject$i"
                //Creation of Account
                var account = Account(nodeAddress, username);
                Thread.sleep(100)
                account.insertAmount(10.0)
                account.transferMoneyToUser(destUsername, 1.0)
            }
        }
        Thread.sleep(10000);
    }
}
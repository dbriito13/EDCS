import org.junit.Test
import java.io.IOException
import java.net.InetAddress
import kotlin.test.assertFailsWith

class AccountTests {
    @Test
    fun testCreation(){
        //Tests the correct creation of Account
        val nodeAddress = NodeAddress("127.0.0.1", 18080);
        val username = "Daniel"
        //Creation of Account
        val account = Account(nodeAddress, username);
        //Check the account was correctly created and has a balance of 0.0€
        assert(account.username == username);
        assert(account.balance == 0.0);
        assert(account.sock.inetAddress == InetAddress.getByName(nodeAddress.host))
        assert(account.sock.localPort == nodeAddress.port)
    }

    @Test
    fun testSendMoney(){
        val nodeAddress1 = NodeAddress("127.0.0.1", 18080);
        val username1 = "Daniel"
        //Creation of Account
        val account1 = Account(nodeAddress1, username1);

        val nodeAddress2 = NodeAddress("127.0.0.1", 19800);
        val username2 = "Lucas"
        //Creation of Account
        val account2 = Account(nodeAddress2, username2);

        account2.insertAmount(1000.0);
        account1.listenConnections();

        Thread.sleep(1000);

        account2.transferMoney(nodeAddress1, 100.0);

        Thread.sleep(1000);
        assert(account1.checkBalance() == 100.0)
        assert(account2.checkBalance() == 900.0)
    }
}
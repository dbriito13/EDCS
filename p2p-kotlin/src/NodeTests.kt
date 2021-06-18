import org.junit.Test
import java.io.IOException
import java.net.InetAddress
import kotlin.test.assertFailsWith

class NodeTests {
    @Test
    fun testCreation(){
        //This test will validate that a Node is correctly constructed
        //input parameters
        val host = "127.0.0.1"
        val port = 19000
        //Creation of the corresponding Node
        val node = Node(NodeAddress(host, port));
        //Assertions
        assert(node.address.host == host);
        assert(node.address.port == port);
        assert(node.sock.inetAddress == InetAddress.getByName(host));
        assert(node.sock.localPort == port);
    }

    @Test
    fun testErroneousCreation(){
        //Now we will test the case in which the Node is created on a remote IP for which we don't have permission
        val host = "1.2.3.4"
        val port = 8080
        //We will check that it fails with the desired Exception
        assertFailsWith<IOException> {
            val node = Node(NodeAddress(host, port))
        }
    }

    @Test
    fun testSendMessage(){
        //NODE 1: LISTENING NODE
        val host1 = "127.0.0.1"
        val port1 = 19000
        //Creation of the corresponding Node
        val address1 = NodeAddress(host1, port1)
        val nodeListener = Node(address1);

        //NODE 2: SENDER NODE
        val host2 = "127.0.0.1"
        val port2 = 19800
        //Creation of the Sender Node
        val address2 = NodeAddress(host2, port2)
        val nodeSender = Node(address2)

        //Sequence of events
        nodeListener.listenConnections()
        Thread.sleep(1000);
        nodeSender.sendMessage(address1, "Esto es un mensaje de prueba")
        //No errors occur
    }
}
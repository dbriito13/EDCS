import java.awt.BorderLayout
import java.awt.Container
import java.awt.event.ActionListener
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.net.BindException
import javax.swing.*


class Main (_title: String) : JFrame(){
    val container: Container = contentPane
    val userLabel = JLabel("USERNAME")
    val ipLabel = JLabel("IP")
    val portLabel = JLabel("PORT")
    val userTextField = JTextField()
    val ipTextField = JTextField()
    val portField = JTextField()
    val loginButton = JButton("LOGIN")
    val loginPanel = JPanel()
    //vals for the logged in account
    var account: Account? = null;

    init {
        //Calling methods inside constructor.
        loginPanel.setLayout(null);
        setLocationAndSize()
        addComponentsToContainer()
        loginButton.addActionListener(ActionListener {
            //your actions
            try {
                val _NodeAddress = NodeAddress(ipTextField.text, portField.text.toInt())
                account = Account(_NodeAddress, userTextField.text)
                account!!.listenConnections()
                JOptionPane.showMessageDialog(null, "Account created successfully!");
                changePanel(mainPanel())
                this.addWindowListener(object : WindowAdapter() {
                    override fun windowClosing(windowEvent: WindowEvent) {
                        if (JOptionPane.showConfirmDialog(container,
                                        "Are you sure you want to close this window?", "Close Window?",
                                        JOptionPane.YES_NO_OPTION,
                                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                            //Shutdown account
                            account!!.shutdown();
                            System.exit(0)
                        }
                    }
                })
            }catch (e: BindException){
                JOptionPane.showMessageDialog(null, "Error! The port is already being used");
            }catch (e: UserAlreadyExists){
                JOptionPane.showMessageDialog(null, "Error! The user is already registered");
            }
            catch (e: Exception){
                JOptionPane.showMessageDialog(null, "Error creating the Account, try again!");
                e.printStackTrace()
            }
        })

    }

    fun mainPanel() : JPanel{
        val panel = JPanel(BorderLayout());
        val titleLabel = JLabel("${account?.username} at ${account?.address?.host}:${account?.address?.port}")
        val balance = JLabel("BALANCE: ${account?.balance}")
        val userLabel = JLabel("USERNAME")
        val amountLabel = JLabel("Amount")
        val depositLabel = JLabel("DEPOSIT")
        val userTextField = JTextField()
        val amountTextField = JTextField()
        val depositField = JTextField()
        val transferButton = JButton("Send Money")
        val depositButton = JButton("Deposit Money")
        val refreshButton = JButton("Refresh")

        depositButton.addActionListener(ActionListener {
            try {
                account?.insertAmount(depositField.text.toDouble())
                JOptionPane.showMessageDialog(panel, "Correctly deposited ${depositField.text}€");
            }catch (e: Exception){
                JOptionPane.showMessageDialog(panel, "Error when trying to deposit amount!");
            }
        })

        refreshButton.addActionListener(ActionListener{
           changePanel(mainPanel())
        })

        transferButton.addActionListener {
            try{
                val res = account?.transferMoneyToUser(userTextField.text, amountTextField.text.toDouble());
                if(res == 0){
                    JOptionPane.showMessageDialog(null, "Transaction was successfull")
                    balance.text = account?.checkBalance().toString()
                }
            } catch (e: Exception){
                JOptionPane.showMessageDialog(null, "Error when transferring money!")
            }
        }



        titleLabel.setBounds(50,10,250,30)
        balance.setBounds(50, 50, 300, 30)
        refreshButton.setBounds(150, 50,100,30)
        userLabel.setBounds(50, 150, 100, 30)
        amountLabel.setBounds(50, 220, 100, 30)
        userTextField.setBounds(150, 150, 150, 30)
        amountTextField.setBounds(150, 220, 150, 30)
        transferButton.setBounds(50, 360, 270, 30)
        depositLabel.setBounds(50,430,100,30)
        depositField.setBounds(150, 430, 100, 30)
        depositButton.setBounds(50, 500, 270, 30)

        panel.add(userLabel)
        panel.add(titleLabel)
        panel.add(amountLabel)
        panel.add(userTextField)
        panel.add(amountTextField)
        panel.add(transferButton)
        panel.add(depositLabel)
        panel.add(depositField)
        panel.add(depositButton)
        panel.add(balance)
        panel.add(refreshButton)
        return panel;
    }

    private fun changePanel(panel: JPanel) {
        container.removeAll()
        container.add(panel, BorderLayout.CENTER)
        container.doLayout()
        update(graphics)
    }

    private fun setLocationAndSize() {
        //Setting location and Size of each components using setBounds() method.
        userLabel.setBounds(50, 150, 100, 30)
        ipLabel.setBounds(50, 220, 100, 30)
        portLabel.setBounds(50,290,100,30)
        userTextField.setBounds(150, 150, 150, 30)
        ipTextField.setBounds(150, 220, 150, 30)
        portField.setBounds(150,290,150,30)
        loginButton.setBounds(50, 360, 270, 30)
    }

    private fun addComponentsToContainer() {
        //Adding each components to the Container
        loginPanel.add(userLabel)
        loginPanel.add(ipLabel)
        loginPanel.add(portField)
        loginPanel.add(portLabel)
        loginPanel.add(userTextField)
        loginPanel.add(ipTextField)
        loginPanel.add(loginButton)
        container.add(loginPanel)
    }
}


fun main(){
    val frame = Main("title")
    frame.isVisible = true
    frame.title = "Login Form"
    frame.setBounds(10, 10, 370, 600)
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.isResizable = false
}

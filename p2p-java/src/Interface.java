import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.*;

//import jdk.javadoc.internal.doclets.formats.html.SourceToHTMLConverter;

public class Interface extends javax.swing.JFrame{

    public Interface(String t) throws NumberFormatException, IOException{
        super("Bank Graphical Interface");
        setLocation(100,50);
        setSize(550,315);
        setMinimumSize(new Dimension(200,100));
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initiation();
    }

    public void initiation(){
        JPanel initialPanel = new javax.swing.JPanel();
        JLabel initialIP = new javax.swing.JLabel("IP");
        JLabel initialPort = new javax.swing.JLabel("Port");
        JLabel name = new javax.swing.JLabel("Name");
        JTextField initialIPField = new javax.swing.JTextField();
        JTextField initialPortField = new javax.swing.JTextField();
        JTextField nameField = new javax.swing.JTextField();
        JButton depositButton = new javax.swing.JButton("Deposit");
        JButton transferButton = new javax.swing.JButton("Transfer");

        depositButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                try {
                    if(Account.confirmation(initialIPField.getText())){
                        initialPanel.setVisible(false);
                        deposit(new Account(initialIPField.getText(), Integer.parseInt(initialPortField.getText()), nameField.getText()));
                    }
                } catch (NumberFormatException | IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        transferButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                try {
                    if(Account.confirmation(initialIPField.getText())){
                        initialPanel.setVisible(false);
                        transfer(new Account(initialIPField.getText(), Integer.parseInt(initialPortField.getText()), nameField.getText()));
                    }
                } catch (NumberFormatException | IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout initialPanelLayout = new javax.swing.GroupLayout(initialPanel);
        initialPanel.setLayout(initialPanelLayout);
        initialPanelLayout.setHorizontalGroup(
            initialPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(initialPanelLayout.createSequentialGroup()
                .addGroup(initialPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(initialPanelLayout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addGroup(initialPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(name)
                            .addComponent(initialPort)
                            .addComponent(initialIP))
                        .addGap(51, 51, 51)
                        .addGroup(initialPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(initialIPField)
                            .addComponent(initialPortField)
                            .addComponent(nameField, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)))
                    .addGroup(initialPanelLayout.createSequentialGroup()
                        .addGap(95, 95, 95)
                        .addComponent(depositButton)
                        .addGap(53, 53, 53)
                        .addComponent(transferButton)))
                .addContainerGap(90, Short.MAX_VALUE))
        );
        initialPanelLayout.setVerticalGroup(
            initialPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(initialPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(initialPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(initialIP)
                    .addComponent(initialIPField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(initialPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(initialPort)
                    .addComponent(initialPortField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(initialPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(name)
                    .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addGroup(initialPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(depositButton)
                    .addComponent(transferButton))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(initialPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(initialPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pack();
    }

    public void transfer(Account tr){
        JPanel transferPanel = new javax.swing.JPanel();
        JLabel tIP = new javax.swing.JLabel("IP");
        JLabel tPort = new javax.swing.JLabel("Port");
        JLabel tAmount = new javax.swing.JLabel("Amount");
        JTextField transferIP = new javax.swing.JTextField();
        JTextField transferPort = new javax.swing.JTextField();
        JTextField transferAmount = new javax.swing.JTextField();
        JButton sendMoney = new javax.swing.JButton("SEND");
        sendMoney.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                tr.send_money(tIP.getText(), Integer.parseInt(tPort.getText()), Double.parseDouble(tAmount.getText()));
                transferPanel.setVisible(false);
                aux(tr);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout transferPanelLayout = new javax.swing.GroupLayout(transferPanel);
        transferPanel.setLayout(transferPanelLayout);
        transferPanelLayout.setHorizontalGroup(
            transferPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(transferPanelLayout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addGroup(transferPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tIP)
                    .addComponent(tPort)
                    .addComponent(tAmount))
                .addGap(50, 50, 50)
                .addGroup(transferPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(transferIP)
                    .addComponent(transferPort)
                    .addComponent(transferAmount, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addComponent(sendMoney)
                .addContainerGap())
        );
        transferPanelLayout.setVerticalGroup(
            transferPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(transferPanelLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(transferPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tIP)
                    .addComponent(transferIP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(transferPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tPort)
                    .addComponent(transferPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sendMoney))
                .addGap(18, 18, 18)
                .addGroup(transferPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tAmount)
                    .addComponent(transferAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(33, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(transferPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(transferPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }

    public void deposit(Account dp){
        JPanel depositPanel = new javax.swing.JPanel();
        JLabel depositLabel = new javax.swing.JLabel("Amount");
        JTextField depositAmount = new javax.swing.JTextField();
        JButton dButton = new javax.swing.JButton("ACCEPT");
        dButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                dp.depositAmount(Double.parseDouble(depositAmount.getText()));
                depositPanel.setVisible(false);
                aux(dp);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout depositPanelLayout = new javax.swing.GroupLayout(depositPanel);
        depositPanel.setLayout(depositPanelLayout);
        depositPanelLayout.setHorizontalGroup(
            depositPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(depositPanelLayout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(depositLabel)
                .addGap(51, 51, 51)
                .addComponent(depositAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(117, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, depositPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(dButton)
                .addGap(142, 142, 142))
        );
        depositPanelLayout.setVerticalGroup(
            depositPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, depositPanelLayout.createSequentialGroup()
                .addContainerGap(45, Short.MAX_VALUE)
                .addGroup(depositPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(depositLabel)
                    .addComponent(depositAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addComponent(dButton))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(depositPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(depositPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(47, Short.MAX_VALUE))
        );

        pack();
    }

    public void aux(Account acc){
        JPanel auxiliarPanel = new javax.swing.JPanel();
        JLabel opLabel = new javax.swing.JLabel("Select operation");
        JButton dpOpt = new javax.swing.JButton("DEPOSIT");
        JButton trOpt = new javax.swing.JButton("TRANSFER");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        dpOpt.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                auxiliarPanel.setVisible(false);
                deposit(acc);
            }
        });

        trOpt.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                auxiliarPanel.setVisible(false);
                transfer(acc);
            }
        });

        javax.swing.GroupLayout auxiliarPanelLayout = new javax.swing.GroupLayout(auxiliarPanel);
        auxiliarPanel.setLayout(auxiliarPanelLayout);
        auxiliarPanelLayout.setHorizontalGroup(
            auxiliarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(auxiliarPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(dpOpt)
                .addGroup(auxiliarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(auxiliarPanelLayout.createSequentialGroup()
                        .addGap(66, 66, 66)
                        .addComponent(opLabel)
                        .addContainerGap(151, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, auxiliarPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(trOpt)
                        .addContainerGap())))
        );
        auxiliarPanelLayout.setVerticalGroup(
            auxiliarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(auxiliarPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(opLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addGroup(auxiliarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dpOpt)
                    .addComponent(trOpt))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(auxiliarPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(auxiliarPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }

    public static void main (String[] args) throws NumberFormatException, IOException{
        try{
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch(Exception e){
            
        }
        new Interface("");
    }
}
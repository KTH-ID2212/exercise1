package se.kth.id2212.ex1.threads.swingclient;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Gui for the reverse client. Used to submit string for reversal.
 */
public class SimpleClient extends JPanel
{
    private JButton connectButton;
    private JButton reverseButton;
    private JLabel resultLabel = new JLabel();
    private ServerConnection connection;

    /**
     * Creates a new instance and builds the gui.
     */
    SimpleClient()
    {
        buildGui();
    }

    /**
     * The main method of the client. Starts the gui.
     *
     * @param args No command line parameters are used.
     */
    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Reverse Client");
        frame.setContentPane(new SimpleClient());
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void buildGui()
    {
        setLayout(new GridLayout(3, 1));
        add(createConnectPanel());
        add(createReversePanel());
        add(resultLabel);
    }

    private Component createConnectPanel()
    {
        JPanel connectPanel = new JPanel();
        connectPanel.setBorder(
                new TitledBorder(new EtchedBorder(), "Connection"));

        connectPanel.add(new JLabel("Host:"));
        final JTextField hostField = new JTextField("localhost");
        connectPanel.add(hostField);

        connectPanel.add(new JLabel("Port:"));
        final JTextField portField = new JTextField("4444");
        connectPanel.add(portField);

        connectButton = new JButton("Connect");
        connectPanel.add(connectButton);
        connectButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                String host = hostField.getText();
                int port = Integer.parseInt(portField.getText());
                connectButton.setEnabled(false);
                connection =
                        new ServerConnection(SimpleClient.this, host, port);
                new Thread(connection).start(); //multithreaded
                //connection.connect(); //single threaded
            }
        });
        return connectPanel;
    }

    private Component createReversePanel()
    {
        JPanel reversePanel = new JPanel();
        reversePanel.setBorder(new TitledBorder(new EtchedBorder(),
                "Reverse"));

        reversePanel.add(new JLabel("String to Reverse:"));
        final JTextField reverseField = new JTextField(10);
        reversePanel.add(reverseField);

        reverseButton = new JButton("Reverse");
        reverseButton.setEnabled(false);
        reversePanel.add(reverseButton);
        reverseButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                reverseButton.setEnabled(false);
                connectButton.setEnabled(true);
                connection.reverse(reverseField.getText());
                //connection.callServer(); //single threaded
            }
        });
        return reversePanel;
    }

    /**
     * Callback method for the network layer. Should be invoked when
     * successfully connected to the server.
     */
    void connected()
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                reverseButton.setEnabled(true);
            }
        });
    }

    /**
     * Callback method for the network layer. Used to show the result
     * of the call to the reverse server.
     *
     * @param result The result of the reversal attempt. Should contain
     *               either the reversed string or an error message.
     */
    void showResult(final String result)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                resultLabel.setText(result);
            }
        });
    }
}


package se.kth.id2212.ex1.threads.swingclient;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Handles all communication with the reverse server.
 */
public class ServerConnection implements Runnable
{
    private final String host;
    private final int port;
    private final SimpleClient gui;
    private final LinkedBlockingQueue<String> strings =
            new LinkedBlockingQueue<>();
    private BufferedInputStream in;
    private BufferedOutputStream out;

    /**
     * Creates a new instance. Does not connect to the server.
     *
     * @param gui  The client gui object.
     * @param host The reverse server host name.
     * @param port The reverse server port number.
     */
    public ServerConnection(SimpleClient gui, String host, int port)
    {
        this.host = host;
        this.port = port;
        this.gui = gui;
    }

    /**
     * The run method of the communication thread. First connects to
     * the server using the host name and port number specified in the
     * constructor. Second waits to receive a string from the gui and sends
     * that to the reverse server. This is done once, then the thread dies.
     */
    @Override
    public void run()
    {
        connect();
        callServer();
    }

    /**
     * Connects to the server using the host name and port number
     * specified in the constructor.
     */
    void connect()
    {
        try
        {
            Socket clientSocket = new Socket(host, port);
            in = new BufferedInputStream(clientSocket.getInputStream());
            out = new BufferedOutputStream(clientSocket.getOutputStream());
            gui.connected();
        } catch (UnknownHostException e)
        {
            System.err.println("Don't know about host: " + host + ".");
            System.exit(1);
        } catch (IOException e)
        {
            System.err.println("Couldn't get I/O for the connection to: "
                    + host + ".");
            System.exit(1);
        }
    }

    /**
     * Used to submit a string for reversal.
     *
     * @param text The string to reverse.
     */
    void reverse(String text)
    {
        strings.add(text);
    }

    /**
     * Waits to receive a string from the gui and sends that to the
     * reverse server.
     */
    void callServer()
    {
        String result;
        try
        {
            byte[] toServer = strings.take().getBytes();
            out.write(toServer, 0, toServer.length);
            out.flush();
            byte[] fromServer = new byte[toServer.length];
            int n = in.read(fromServer, 0, fromServer.length);
            if (n != fromServer.length)
            {
                result = "Failed to reverse, some data was lost.";
            } else
            {
                result = new String(fromServer);
            }
        } catch (InterruptedException | IOException e)
        {
            result = "Failed to reverse, " + e.getMessage();
        }
        gui.showResult(result);
    }
}

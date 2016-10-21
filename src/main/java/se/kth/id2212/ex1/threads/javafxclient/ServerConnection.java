package se.kth.id2212.ex1.threads.javafxclient;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Handles all communication with the reverse server.
 */
public class ServerConnection {
    private final LinkedBlockingQueue<String> strings
            = new LinkedBlockingQueue<>();
    private BufferedInputStream in;
    private BufferedOutputStream out;

    /**
     * Connects to a server using the specified host name and port number.
     *
     * @param host Server host name.
     * @param port Server port number.
     */
    public ServerConnection(String host, int port) {
        try {
            Socket clientSocket = new Socket(host, port);
            in = new BufferedInputStream(clientSocket.getInputStream());
            out = new BufferedOutputStream(clientSocket.getOutputStream());
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + host + ".");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: "
                               + host + ".");
            System.exit(1);
        }
    }

    /**
     * Sends the specified string to the server.
     *
     * @param toServer The string that will be sent to the server
     * @return The server's answer.
     */
    String callServer(String toServer) throws IOException {
        byte[] msg = toServer.getBytes();
        out.write(msg, 0, msg.length);
        out.flush();
        byte[] fromServer = new byte[msg.length];
        int n = in.read(fromServer, 0, fromServer.length);
        if (n != fromServer.length) {
            throw new IOException("Failed to reverse, some data was lost.");
        }
        return new String(fromServer);
    }
}

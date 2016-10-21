package se.kth.id2212.ex1.simple;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class SimpleClient
{

    public static void main(String[] args) throws IOException
    {
        Socket clientSocket = null;

        try
        {
            clientSocket = new Socket(args[0], 4444);
        } catch (UnknownHostException e)
        {
            System.err.println("Don't know about host: " + args[0] + ".");
            System.exit(1);
        } catch (IOException e)
        {
            System.err.println("Couldn't get I/O for " +
                    "the connection to: " + args[0] + "");
            System.exit(1);
        }

        BufferedInputStream in = null;
        BufferedOutputStream out = null;

        try
        {
            in = new BufferedInputStream(clientSocket.getInputStream());
            out = new BufferedOutputStream(clientSocket.getOutputStream());
        } catch (IOException e)
        {
            System.out.println(e.toString());
            System.exit(1);
        }

        byte[] toServer = args[1].getBytes();
        out.write(toServer, 0, toServer.length);
        out.flush();

        byte[] fromServer = new byte[toServer.length];
        int n = in.read(fromServer, 0, fromServer.length);
        if (n != fromServer.length)
        {
            System.out.println("Some data are lost ...");
        }
        System.out.println(new String(fromServer));

        out.close();
        in.close();
        clientSocket.close();
    }
}

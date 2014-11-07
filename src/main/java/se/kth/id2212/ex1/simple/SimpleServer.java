package se.kth.id2212.ex1.simple;

/*
1.Open a socket. 
2.Open an input stream and output stream to the socket. 
3.Read from and write to the stream according to the server's protocol. 
4.Close the streams. 
5.Close the socket.
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServer
{

    public static void main(String[] args) throws IOException
    {
        boolean listening = true;
        ServerSocket serverSocket = null;

        try
        {
            serverSocket = new ServerSocket(4444);
        } catch (IOException e)
        {
            System.err.println("Could not listen on port: 4444.");
            System.exit(1);
        }

        while (listening)
        {
            Socket clientSocket = serverSocket.accept();
            (new SimpleConnectionHandler(clientSocket)).start();
        }

        serverSocket.close();
    }
}


package se.kth.id2212.ex1.compute;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class ComputeServer
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
            (new ComputeHandler(clientSocket)).start();
        }

        serverSocket.close();
    }
}


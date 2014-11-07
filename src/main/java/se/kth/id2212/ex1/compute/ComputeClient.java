package se.kth.id2212.ex1.compute;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ComputeClient
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

        Spy spy = new Spy();

        ObjectOutputStream out =
                new ObjectOutputStream(clientSocket.getOutputStream());
        out.writeObject(spy);
        out.flush();

        Object taskObj;
        ObjectInputStream in =
                new ObjectInputStream(clientSocket.getInputStream());

        try
        {
            taskObj = in.readObject();
        } catch (ClassNotFoundException cnfe)
        {
            System.out.println(cnfe.toString());
            return;
        } catch (OptionalDataException ode)
        {
            System.out.println(ode.toString());
            return;
        }

        if (taskObj instanceof Spy)
        {
            spy = (Spy) taskObj;
            for (String fileName : spy.getFileNames())
            {
                System.out.println(fileName);
            }
        }

        out.close();
        in.close();
        clientSocket.close();
    }
}

package se.kth.id2212.ex1.compute;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.net.Socket;

public class ComputeHandler extends Thread {
    private Socket clientSocket;

    public ComputeHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run() {
        Object taskObj;
        ObjectInputStream in;

        try {
            in = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            System.out.println(e.toString());
            return;
        }

        try {
            taskObj = in.readObject();
        } catch (ClassNotFoundException cnfe) {
            System.out.println(cnfe.toString());
            return;
        } catch (OptionalDataException ode) {
            System.out.println(ode.toString());
            return;
        } catch (IOException ioe) {
            System.out.println(ioe.toString());
            return;
        }

        if (taskObj instanceof Task) {
            ((Task) taskObj).execute();
        }

        ObjectOutputStream out = null;

        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.writeObject(taskObj);
            out.flush();
        } catch (IOException e) {
            System.out.println(e.toString());
        }

        try {
            out.close();
            in.close();
            clientSocket.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}

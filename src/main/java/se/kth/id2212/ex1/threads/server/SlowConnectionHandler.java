package se.kth.id2212.ex1.threads.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.net.Socket;

/**
 * A connection handler for the reverse server. Sleeps the specified time before
 * returning the reversed string.
 */
public class SlowConnectionHandler implements Runnable
{
    private static final int DELAY_SECS = 10;
    private static final int SECS_TO_MILLIS = 1000;
    private Socket clientSocket;

    /**
     * Creates anew instance.
     *
     * @param clientSocket This socket should be connected to a reverse client.
     */
    SlowConnectionHandler(Socket clientSocket)
    {
        this.clientSocket = clientSocket;
    }

    /**
     * Reads a string from the client, sleeps the specified time and
     * returns the reversed string.
     */
    @Override
    public void run()
    {
        try (BufferedInputStream in = new BufferedInputStream(clientSocket.getInputStream());
             BufferedOutputStream out = new BufferedOutputStream(clientSocket.getOutputStream()))
        {

            byte[] msg = new byte[4096];
            int bytesRead = 0;
            int n;
            while ((n = in.read(msg, bytesRead, 256)) != -1)
            {
                bytesRead += n;
                if (bytesRead == 4096)
                {
                    break;
                }
                if (in.available() == 0)
                {
                    break;
                }
            }

            Thread.sleep(DELAY_SECS * SECS_TO_MILLIS);

            for (int i = bytesRead; i > 0; i--)
            {
                out.write(msg[i - 1]);
            }
            out.flush();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

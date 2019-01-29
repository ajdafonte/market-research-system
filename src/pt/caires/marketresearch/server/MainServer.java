package pt.caires.marketresearch.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * Main class that starts the Server.
 *
 * @author acaires
 */
public class MainServer
{

    /**
     * @param args the command line arguments
     */
    public static void main(final String[] args)
    {

        System.out.println(">>>>>>> Caravelo - Market Survey Server <<<<<<<<");
        System.out.println("------------------------------------------------");
        System.out.println(">> Server Listening....");
        try (final ServerSocket servSock = new ServerSocket(Integer.parseInt(args[0])))
        {
            while (true)
            {
                final Socket sock = servSock.accept();
                System.out.println(">> Connection established with a client.");
                // new thread for a client
                final JsonServerThread st = new JsonServerThread(sock);
                st.start();
            }
        }
        catch (final IOException ex)
        {
            System.err.println(">> Error manipulating streams: " + ex);
        }

    }

}

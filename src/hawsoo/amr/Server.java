package hawsoo.amr;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
	private Socket socket = null;
	private ServerSocket server = null;
	private DataInputStream streamIn = null;
	
	public Server(int port)
	{
		try
		{
			System.out.println("Binding to port " + port + ", please wait  ...");
			server = new ServerSocket(port);
			System.out.println("Server started: " + server);
			System.out.println("Waiting for a client ...");
			socket = server.accept();
			System.out.println("Client accepted: " + socket + "\n");
			open();
			
			// Game Loop
			boolean done = false,
					restart = false;
			while (!done && !restart)
			{
				try
				{
					String line = streamIn.readUTF();
					System.out.println(line);
					
					if (line.equals(Utils.SEE_YA_MESSAGE) ||
						line.equals(Utils.QUIT_MESSAGE))
					{
						// Check if should restart/quit
						restart = line.equals(Utils.SEE_YA_MESSAGE);
						done = line.equals(Utils.QUIT_MESSAGE);
						break;
					}
					else if (line.equals(Utils.TERMINATE_AUDIO_MESSAGE))
					{
						// Terminate currently playing audio
						Utils.destroySongPlayer();
					}
					else
					{
						// Attempt to play song (for reference)
						Utils.playSongFromURL(line);
					}
				} catch (IOException ioe)
				{
					restart = true;
				}
			}
			
			// Close down server
			close();
			
			// Restart
			if (restart)
			{
				System.out.print("Restarting...\t");
				try
				{
					Thread.sleep(1500);
				} catch (InterruptedException e) { }
				
				System.out.println("done\n------------------------------------");
				main(new String[0]);
			}
		} catch (IOException ioe)
		{
			System.out.println(ioe);
		}
	}
	
	public void open() throws IOException
	{
		streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
	}
	
	public void close() throws IOException
	{
		if (socket != null) socket.close();
		if (streamIn != null) streamIn.close();
		
		if (server != null) server.close();
	}
	
	public static void main(String args[])
	{
		if (args.length >= 1)
			new Server(Integer.parseInt(args[0]));
		else
			new Server(Utils.DEFAULT_SERVER_PORT);
	}
}
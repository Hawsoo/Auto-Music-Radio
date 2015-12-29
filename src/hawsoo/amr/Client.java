package hawsoo.amr;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client
{
	private Socket socket = null;
	private BufferedReader console = null;
	private DataOutputStream streamOut = null;
	
	private boolean accepted;
	
	public Client(String serverName, int serverPort)
	{
		accepted = false;
		System.out.println("Establishing connection. Please wait ...");
		try
		{
			socket = new Socket(serverName, serverPort);
			System.out.println("Connected: " + socket);
			start();
		} catch (UnknownHostException uhe)
		{
			System.out.println("Host unknown: " + uhe.getMessage());
		} catch (IOException ioe)
		{
			System.out.println("Unexpected exception: " + ioe.getMessage());
		}
		
		// Start connection if worked
		if (accepted)
		{
			System.out.println("\nConnection succeeded!");
			
			String line = "";
			while (true)
			{
				try
				{
					line = console.readLine();
					streamOut.writeUTF(line);
					streamOut.flush();
					
					if (line.equals(Utils.SEE_YA_MESSAGE) ||
						line.equals(Utils.QUIT_MESSAGE))
					{
						// Break out
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
					System.out.println("Sending error: " + ioe.getMessage());
				}
			}
		}
	}
	
	public void start() throws IOException
	{
		console = new BufferedReader(new InputStreamReader(System.in));
		streamOut = new DataOutputStream(socket.getOutputStream());
		accepted = true;
	}
	
	public void stop()
	{
		try
		{
			if (console != null) console.close();
			if (streamOut != null) streamOut.close();
			if (socket != null) socket.close();
		} catch (IOException ioe)
		{
			System.out.println("Error closing ...");
		}
	}
	
	public static void main(String args[])
	{
		if (args.length != 1)
			System.out.println("Usage: java Client host port (arg: Computer/Server Name [ex: Tiffany-PC], w/ optional 2nd arg for specifying port number)");
		else if (args.length >= 2)
			new Client(args[0], Integer.parseInt(args[1]));
		else
			new Client(args[0], Utils.DEFAULT_SERVER_PORT);
	}
}
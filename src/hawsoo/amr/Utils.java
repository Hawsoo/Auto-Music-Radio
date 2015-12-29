package hawsoo.amr;

import java.io.BufferedInputStream;
import java.net.URL;

import javazoom.jl.player.Player;

public class Utils
{
	public static final int DEFAULT_SERVER_PORT = 47152;
	public static final String SEE_YA_MESSAGE = ".bye4now";
	public static final String QUIT_MESSAGE = ".quit";
	public static final String TERMINATE_AUDIO_MESSAGE = ".terminate";
	
	private static Player mp3player = null;
	
	public static void destroySongPlayer()
	{
		// Stop any previous sounds
		if (mp3player != null)
		{
			mp3player.close();
		}
	}
	
	public static void playSongFromURL(final String url)
	{
		destroySongPlayer();
		
		// Start thread
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				// Play Music
				String song = url;
				BufferedInputStream in = null;
				try
				{
					// Play once
					in = new BufferedInputStream(new URL(song).openStream());
					mp3player = new Player(in);
					mp3player.play();
					
				} catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}).start();
		
		System.out.println("::Music should be playing now if no error ;)");
	}
}

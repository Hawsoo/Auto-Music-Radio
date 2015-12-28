package hawsoo.amr;

import hawsoo.amr.client.Client;
import hawsoo.amr.server.Server;

public class MainClass
{
	public static final boolean IS_SERVER = true;
	
	public static void main(String[] args)
	{
		// Start correct version
		if (IS_SERVER)
		{
			new Server().start();
		}
		else
		{
			new Client().start();
		}
	}
	
}

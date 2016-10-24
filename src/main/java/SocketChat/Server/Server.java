package SocketChat.Server;

public class Server 
{
	
    public static void main( String[] args )
    {
        new Thread(new SocketCatcherThread()).start();
        
    }
    

}

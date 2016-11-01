package SocketChat.Server;

public class Server 
{
	
    public static void main( String[] args )
    {	//1. Запускаем сокет кетчер, не важно
        new Thread(new SocketCatcherThread()).start();
        
    }
    

}

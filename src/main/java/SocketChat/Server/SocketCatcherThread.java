package SocketChat.Server;

import java.awt.List;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class SocketCatcherThread implements Runnable {

	private ServerSocket ss;
	private ArrayList<Socket> socketList;

	public void run() {
		//2. В этой части кода не сложно. Мы стартуем сервер (сокет кетчер)
		//Для этого используем Сервер Сокет
		System.out.println("This is Server");
		try {
			ss = new ServerSocket(26789);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (ss == null) {
			System.err.println("Some problems occured, while binding a port");
			System.exit(0);
		}
		//Еррей лист соккетов, которые мы приняли
		socketList = new ArrayList<Socket>();
		System.out.println("Server started at localhost:" + ss.getLocalPort());
		while (true) {
			//до бесконечности принимаем сокеты. А вообще нужно в отдельном потоке читать с консоли сервера
			//И в случае команды stop или end - остановить сервер. Но это добавить нужно позже
			Socket clientSocket = getSocket();
			if (clientSocket != null) {
				//Каждый сокет пишем в общий еррейлист
				socketList.add(clientSocket);
				System.out.println("Client connected " + clientSocket.getInetAddress());
				//И для КАЖДОГО СОККЕТА - мы создаем отдельный поток, который будет работать с ЭТИМ сокетом
				//и ОБЩИМ еррей листом соккетов
				new Thread(new ClientSocketWorkerThread(clientSocket, socketList)).start();
			}else {
				System.err.println("SEVERE: NULL POINTER EXCEPTION");
			}
		}
	}
	
    private Socket getSocket(){
		try {
			return this.ss.accept();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
    }

}

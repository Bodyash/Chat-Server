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
		socketList = new ArrayList<Socket>();
		System.out.println("Server started at localhost:" + ss.getLocalPort());
		while (true) {
			Socket clientSocket = getSocket();
			if (clientSocket != null) {
				socketList.add(clientSocket);
				System.out.println("Client connected " + clientSocket.getInetAddress());
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

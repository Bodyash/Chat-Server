package SocketChat.Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Exchanger;

public class ClientSocketWorkerThread implements Runnable {
	private Socket s;
	private ClientCommandsWorker ccw;
	private String nickname; // Написать реализацию.
	private ArrayList<Socket> socketList;

	public ClientSocketWorkerThread(Socket s, ArrayList<Socket> socketList) {
		this.s = s;
		this.ccw = new ClientCommandsWorker();
		this.socketList = socketList;
	}

	public void run() {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		/*
		 * BufferedWriter bw = new BufferedWriter(new
		 * OutputStreamWriter(s.getOutputStream()))
		 */) {
			String str = null;
			while ((str = br.readLine()) != null) {
				System.out.println(ccw.printTime() + " " + str);
				printMessageToOtherSockets(str);

				// this shit prints back u sended to server
				/*
				 * bw.write(str + "\n"); bw.flush();
				 */
			}
		} catch (IOException e) {
			System.out.println("Client " + this.s.getInetAddress().getHostAddress() + " disconnected");
			this.socketList.remove(this.s);
		}
	}

	private void printMessageToOtherSockets(String msg) throws IOException {
		int i = 0;
		for (Socket client : socketList) {
			if (s.isClosed()) {
				this.socketList.remove(i);
			} else {
				if (!s.equals(client)) {
					BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
					try {
						bw.write(msg + "\n");
						bw.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			++i;
		}
	}
}

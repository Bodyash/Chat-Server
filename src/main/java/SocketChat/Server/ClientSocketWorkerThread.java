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
		try (BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));) {
			String str = null;
			//С бафферед ридера читаем строки
			while ((str = br.readLine()) != null) {
				//в Консоль сервера печатаем месседж с временем
				System.out.println(ccw.printTime() + " " + str);
				//и Этим методом отправляем строку остальным сокетам (кроме своего)
				printMessageToOtherSockets(str);
			}
		} catch (IOException e) {
			//Если клиент отключился или другая ошибка - мы пишем что клиент ВСЕ и удаляем его из общего листа
			System.out.println("Client " + this.s.getInetAddress().getHostAddress() + " disconnected");
			this.socketList.remove(this.s);
		}
	}

	private void printMessageToOtherSockets(String msg) throws IOException {
		int i = 0;//Счетчик итерации для удаления мертвых сокетов
		for (Socket client : socketList) {
			if (s.isClosed()) {//Если сокет мертв - то отправить ничего мы туда не сможем
				//Поэтому просто удаляем его и не паримся.
				this.socketList.remove(i);
			} else {
				//А если сокет - не является сокетом, который лежит в этом потоке - то пишем туда туда текст
				//А если мы обратились к нашему сокету - то ++i и поехали следующий цикл
				if (!s.equals(client)) {
					BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
					try {
						//Дефолтно, отправка данных через стрим - такая же как в Клиенте
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

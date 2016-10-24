package SocketChat.Server;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientCommandsWorker {
	SimpleDateFormat sdf = new SimpleDateFormat("[dd.MM.YYYY HH:mm:ss]");
	public String help() {
		return sdf.format(new Date()) + " Type /help to view this message";
	}
	
	public String printTime() {
		return sdf.format(new Date());
	}

}

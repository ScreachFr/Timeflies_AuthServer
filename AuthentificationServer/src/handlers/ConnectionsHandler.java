package handlers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.naming.AuthenticationException;

import checkers.LoginChecker;
import settings.SettingsManager;
import utils.ConsoleDisplay;

public class ConnectionsHandler implements Runnable {
	private ServerSocket serverSocket;
	
	public ConnectionsHandler() throws Exception {
		ConsoleDisplay.display_startNotice("connection handler");
		try {
			int port = SettingsManager.getNetworkSettings().getPort();
		
			serverSocket = new ServerSocket(port);
		} catch(Exception e) {
			ConsoleDisplay.display_startNoticeFail();
			throw e;
		}
		ConsoleDisplay.display_startNoticeSuccess();
	}
	

	@Override
	public void run() {
		Thread thread;
		while (true) { //TODO define an end of loop condition.
			try {
				thread = new Thread(new AuthHandler(serverSocket.accept()));
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
	}
}

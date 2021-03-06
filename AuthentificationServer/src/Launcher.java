import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import handler.args.ArgsHandler;
import handler.args.handlers.TestModeHandler;
import handlers.CommandHandler;
import handlers.ConnectionsHandler;
import settings.SettingsManager;
import storage.TokenBank;
import utils.ConsoleDisplay;
import utils.ConsoleInput;
import utils.DBMapper;
import utils.LibChecker;
import utils.ThreadManager;

public class Launcher {

	public static void main(String[] args) {


		ConsoleDisplay.display_splash();

		ConnectionsHandler connectionHandler = null;
		CommandHandler cmdHandler = null;
		try {
			ThreadManager.init();
			
			ArgsHandler.init(args);
			LibChecker.check();
			SettingsManager.initSettings();
			TokenBank.init();
			connectionHandler = new ConnectionsHandler();
			DBMapper.init();
			ConsoleInput.init();
		} catch (Exception e) {
			ConsoleDisplay.display_errorNotice("Failed to initialize. Stopping the program.");
			if (ConsoleDisplay.debug)
				e.printStackTrace();
			return;
		}

		//Test mode
		if (TestModeHandler.isTestMode()) {
			ConsoleDisplay.display_notice("Test mode enable.");
			ConsoleDisplay.display_notice("Test mode is not available anymore.");
			
		//Normal mode
		} else {
			Thread connectionHandlerThread = new Thread(connectionHandler);
			ThreadManager.getCurrentInstance().addThread(connectionHandlerThread);
			connectionHandlerThread.start();
			ConsoleDisplay.display_notice("Waiting for connections...");
			cmdHandler = new CommandHandler(connectionHandler);
			Thread t = new Thread(cmdHandler);
			t.start();
			ThreadManager.getCurrentInstance().addThread(t);
			
		}
		
	}


	public static String sha1(String message){
		String digest = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			byte[] hash = md.digest(message.getBytes("UTF-8"));

			//converting byte array to Hexadecimal String
			StringBuilder sb = new StringBuilder(2*hash.length);
			for(byte b : hash){
				sb.append(String.format("%02x", b&0xff));
			}

			digest = sb.toString();

		} catch (UnsupportedEncodingException ex) {
		} catch (NoSuchAlgorithmException ex) {
		}
		return digest;
	}

	



}

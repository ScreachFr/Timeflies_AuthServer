package checkers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import exceptions.AuthentificationException;
import messages.login.LoginRequest;
import messages.server_login.ServerLoginRequest;
import utils.ConsoleDisplay;
import utils.DBMapper;

public class LoginChecker {
	private final static String LOGIN_CHECK_QUERY = "SELECT * FROM utilisateur WHERE pseudo = ? AND mdp = ?;";
	private final static String ADD_UUID_QUERY = "INSERT INTO session VALUES(?, ?, ?);";
	
	private static boolean isLoginCorrect(String login, String pwd) {
		ResultSet res;
		try {
			res = DBMapper.executeQuery(LOGIN_CHECK_QUERY, login, pwd);
			return res.next();
		} catch (SQLException e) {
			if (ConsoleDisplay.debug)
				e.printStackTrace();
		}
		
		return false;
	}
	
	public static boolean checkLogin(LoginRequest request) throws AuthentificationException {
		if(isLoginCorrect(request.getLogin(), request.getPassWord())) {
			return true;
		} else {
			throw new AuthentificationException();
		}
	}
	
	public static boolean checkServerLogin(ServerLoginRequest request) {
		//TODO verify that the user is a server.
		return isLoginCorrect(request.getLogin(), request.getPwd());
	}
	
	public static void addUUIDToDB(UUID token, String ip) {
		try {
			DBMapper.executeQuery(ADD_UUID_QUERY, ip, token.toString(), DBMapper.getTime());
		} catch (SQLException e) {
			ConsoleDisplay.display_errorNotice("Failed to inster UUID into database.");
			ConsoleDisplay.printStack(e);
		}
	}
	
}

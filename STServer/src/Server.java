import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

	public static int tcpport = 10000;
	public static int sslport = 20000;
	ServerSocket server = null;
	Socket client = null;
	public static DBConnection DB;
	public static ExecutorService pool = null;
	public static int clientCount = 0;
	public static HashMap<String, String> data = new HashMap<String, String>();

	 BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
	int run = 1;

	public static void main(String[] args) throws IOException, SQLException {
		Server server = new Server();
		server.startServer();
	}

	public Server() {
		//Constructor
	}

	public void startServer() throws IOException, SQLException {
		DB = DBConnection.getInstance();
		DB.init();
		DB.connectDB();
		pool = Executors.newFixedThreadPool(10);
		
		System.out.println("Server Booted");
		// System.out.println("Any client can stop the server by sending -1");
		System.out.println("-------------------------------------------------------------");

		TCPConnectionThread TCPThread = new TCPConnectionThread();
		SSLConnectionThread SSLThread = new SSLConnectionThread();

		pool.execute(TCPThread);
		pool.execute(SSLThread);

		while (run == 1) {
			 String command = br.readLine();
			 if(command.equalsIgnoreCase("quit")) {
			 System.exit(0);
			 }
		}

	}

	public static boolean signal(int connectionType, int status) {
		System.out.println("SERVER: A " + ((connectionType == 0) ? "TCP" : "SSL") + " connection has been "
				+ ((status == 0) ? "closed" : "established"));
		return true;
	}
}

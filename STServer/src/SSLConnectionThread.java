import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

public class SSLConnectionThread implements Runnable {

	// Variables
	ServerSocket tcpServerSocket = null;
	Socket clientSocket = null;
	ServerSocketFactory sslServerSocketFactory;
	 ServerSocket sslServerSocket; 
	int run = 1;

	public SSLConnectionThread() throws IOException {
		
		Start();

	}

	public void Start() {
		System.out.println("SSL CONNECTION CONTROLLER: Thread has been started.");
		//run();
	}

	@Override
	public void run() {
		int x = 1;
		System.setProperty("javax.net.ssl.keyStore","myKeyStore.jks");
		System.setProperty("javax.net.ssl.keyStorePassword", "123456");
		sslServerSocketFactory = SSLServerSocketFactory.getDefault();
	 	
		
		while (run == 1) {
			try {
				
				sslServerSocket= sslServerSocketFactory.createServerSocket(Server.sslport);
				Server.sslport++;
				clientSocket = sslServerSocket.accept();
				
				
				SSLServerThread sslServer = new SSLServerThread(clientSocket, Server.sslport-10001);
				sslServer.Start();
				Server.pool.execute(sslServer);
				Server.signal(1, 1);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
}
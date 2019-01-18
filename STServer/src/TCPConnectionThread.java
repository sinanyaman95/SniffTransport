import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;


public class TCPConnectionThread implements Runnable {

	// Variables
	ServerSocket tcpServerSocket = null;
	Socket clientSocket = null;
	int run = 1;

	public TCPConnectionThread() throws IOException {
		Start();

	}

	public void Start() {
		System.out.println("TCP CONNECTION CONTROLLER: Thread has been started.");
		//run();
	}

	@Override
	public void run() {
		int x = 1;

		while (run == 1) {
			try {
				tcpServerSocket = new ServerSocket(Server.tcpport);
				
				Server.tcpport++;
				clientSocket = tcpServerSocket.accept();

				TCPServerThread tcpServer = new TCPServerThread(clientSocket, Server.tcpport-10001);
				tcpServer.Start();
				Server.pool.execute(tcpServer);
				Server.signal(0, 1);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
}
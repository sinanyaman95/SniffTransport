import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class TCPServerThread implements Runnable {


	Socket tcpSocket = null;
	BufferedReader cin;
	PrintStream cout;
	Scanner sc = new Scanner(System.in);
	int id;

	public TCPServerThread(Socket tcpSocket,int id) throws IOException {

		this.tcpSocket = tcpSocket;
		this.id = id;
		System.out.println("TCP SERVER THREAD:Connection established with client. port:" + tcpSocket.getPort());

		cin = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
		cout = new PrintStream(tcpSocket.getOutputStream());

	}

	public void Start() {
		System.out.println("TCP SERVER THREAD:Thread has been started.");
		run();
	}

	@Override
	public void run() {
		int x = 1;
		try {
			while (true) {
				String message = cin.readLine();

				System.out.println("TCP SERVER THREAD:Client(" + id + ") :" + message);
		
				if (message.contains("submit")) {

					String[] keyValuePair = message.substring(7, message.length()).split(",");
					Server.data.put(keyValuePair[0], keyValuePair[1]);
					cout.println("OK");
					
					//DB INSERT
					Server.DB.insert(keyValuePair[0],keyValuePair[1]);
					
					// cout.println(Arrays.asList(data));
					// cout.println("received");

				} else if (message.contains("get")) {
					String key = message.substring(4, message.length());
					if (Server.data.containsKey(key)) {
						String value = Server.data.get(key);
						cout.println(value);
					} else {
						cout.println("TCP SERVER THREAD:No stored value for " + key);
					}

				} else if (message.equalsIgnoreCase("bye")) {
					cout.println("BYE");
					x = 0;
					System.out.println("TCP SERVER THREAD:Connection ended by server");
					break;
				} else {
					cout.println("TCP SERVER THREAD:Incorrect command/usage. Try \"submit <key,value>\" or \"get <key>\"");
				}

			}

			cin.close();
			tcpSocket.close();
			cout.close();
			if (x == 0) {
				System.out.println("TCP SERVER THREAD:Server cleaning up.");
				System.exit(0);
			}
		} catch (IOException ex) {
			System.out.println("TCP SERVER THREAD:Error : " + ex);
		}

	}
}

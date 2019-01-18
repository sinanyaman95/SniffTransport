import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class Client {

	String host;
	int tcpport=10000;
	int sslport=20000;
	SSLSocketFactory sslSocketFactory;
	SSLSocket sslSocket;
	DataOutputStream os;
	DataInputStream is;
	BufferedReader br;

	Socket tcpSocket;
	BufferedReader sin;
	PrintStream sout;

	int run = 1;

	public Client(String host) throws IOException {
		this.host = host;
		br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Client has started.");

		connect();
	}

	public void connect() throws IOException {
		System.out.println("CLIENT:Please select the connection type (TCP/SSL):");
		String conType = br.readLine();

		if (conType.equalsIgnoreCase("tcp")) {
			System.out.println("CLIENT: TCP Connection is being established...");
			createTCPConnection();
		} else {

			System.out.println("CLIENT: S Connection is being established...");
			createSSLConnection();
		}

	}

	public void createTCPConnection() throws UnknownHostException, IOException {
		tcpSocket = new Socket(host, tcpport);
		sin = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
		sout = new PrintStream(tcpSocket.getOutputStream());
		
		String message;
		while(run==1) {
			System.out.print("CLIENT: ");
			message=br.readLine();
			sout.println(message);
			if ( message.equalsIgnoreCase("BYE") )
			{
				System.out.println("CLIENT:Connection ended by client");
				break;
			}
			message=sin.readLine();
			System.out.print("CLIENT :Server : "+message+"\n");

		}
	}

	public void createSSLConnection() throws UnknownHostException, IOException {
		System.setProperty("javax.net.ssl.trustStore", "myTrustedStore.jts");
		System.setProperty("javax.net.ssl.trustStorePassword", "12345678");
		sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
		sslSocket = (SSLSocket) sslSocketFactory.createSocket(host, sslport);
		//os = new DataOutputStream(sslSocket.getOutputStream());
		//is = new DataInputStream(sslSocket.getInputStream());
		//sin = new BufferedReader(new InputStreamReader(is));
		//sout = new PrintStream(os);
		
		BufferedReader cin = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
		PrintStream cout = new PrintStream(sslSocket.getOutputStream(),true);
		
		String message;
		while(run==1) {
			System.out.print("CLIENT: ");
			message=br.readLine();
			//os.writeChars(message);
			//sout.println(message);
			cout.println(message);
			if ( message.equalsIgnoreCase("BYE") )
			{
				System.out.println("CLIENT:Connection ended by client");
				break;
			}
			message=cin.readLine();
			System.out.print("CLIENT :Server : "+message+"\n");

		}
	}

}

package server_side;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class MySerialServer implements Server {
	
	private int port;
	private boolean stop;

	public MySerialServer(int port) {
		this.port = port;
		this.stop = false;
	}
	
	
	@Override
	public void start(ClientHandler c) {
		new Thread(() -> this.listen(c)).start();
	}
	
	public void listen(ClientHandler c){
		try (ServerSocket serverSocket=new ServerSocket(port)) {
			serverSocket.setSoTimeout(1000);
			while(!stop) {
				try {
					Socket aClient=serverSocket.accept();
					try {
						c.handleClient(aClient.getInputStream(), aClient.getOutputStream());
						aClient.close();
					} catch (IOException exception) {System.out.println("IO Exception while trying to handle a client");}
					
				} catch (SocketTimeoutException e) {}
			}
		} catch (IOException e) {System.out.println("IO Exception while trying to open a socket");}
		
	}

	@Override
	public void stop() {this.stop=true;}

}
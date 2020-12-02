package interpreter.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class MyClient implements Client,Observer {

    private Socket socket;
    private boolean connected;
    private PrintWriter pw;
    
    private static class Holder{
    	public static final MyClient instance=new MyClient();
    }
    
    private MyClient() {}

    @Override
    public void connect(String ip, int port) {
    	connected=false;
    	while(!connected) {
    		try {
                socket = new Socket(ip, port);
                pw = new PrintWriter(socket.getOutputStream());
                connected=true;
                System.out.println("MyClient: Client connected");
            }
            catch(Exception e) {
            	connected=false;
            	try {
					Thread.sleep(1000);
					System.out.println("MyClient: Trying again");
				} catch (InterruptedException e1) {
					Thread.currentThread().interrupt();
				}
            }
    	}
    }

    @Override
    public void set(String path, Double value) {
            pw.println("set " + path + " " + value);
            pw.flush();
    }

    @Override
    public void close() {
        try {
        	//pw.println("bye");
        	//pw.flush();
            pw.close();
            socket.close();
            connected=false;
            System.out.println("Client disconnected");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static MyClient getInstance() {
    	return Holder.instance;
    }

	@Override
	public void update(Observable arg0, Object arg1) {
		if(arg1!=null) {
			List<String> arguments=(List<String>)arg1;
			set(arguments.get(0),Double.parseDouble(arguments.get(1)));
		}
	}
}
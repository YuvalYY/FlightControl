package interpreter.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import interpreter.parser.SymbolTable;

public class MyServer implements Server {

	private volatile boolean stop;
	
	private static class Holder{
    	public static final MyServer instance=new MyServer();
    }
	
	private MyServer() {
		stop=false;
	}
	
	public void startServer(int port,int timesPerSec) {
		stop=false;
		new Thread(()->start(port,timesPerSec)).start();
	}
	
	public void start(int port,int timesPerSec) {
		while(!stop) {
			try {
				ServerSocket servSock=new ServerSocket(port);
				servSock.setSoTimeout(1000);
				while(!stop) {
					try {
					Socket client=servSock.accept();
					System.out.println("Server connected");
					BufferedReader in=new BufferedReader(new InputStreamReader(client.getInputStream()));
					String line=null;
					while((line=in.readLine())!=null) {
						try {
						String[] sims=line.split(",");
						/*
						SymbolTable.getInstance().addToBindMap("simX", sims[0]);
						SymbolTable.getInstance().addToBindMap("simY", sims[1]);
						SymbolTable.getInstance().addToBindMap("simZ", sims[2]);
						*/
						SymbolTable.getInstance().addToBindMap("/controls/flight/speedbrake",Double.parseDouble(sims[0]));
						SymbolTable.getInstance().addToBindMap("/controls/engines/engine/throttle",Double.parseDouble(sims[1]));
						SymbolTable.getInstance().addToBindMap("/instrumentation/heading-indicator/indicated-heading-deg",Double.parseDouble(sims[2]));
						SymbolTable.getInstance().addToBindMap("/instrumentation/airspeed-indicator/indicated-speed-kt",Double.parseDouble(sims[3]));
						SymbolTable.getInstance().addToBindMap("/instrumentation/attitude-indicator/indicated-roll-deg",Double.parseDouble(sims[4]));
						SymbolTable.getInstance().addToBindMap("/instrumentation/attitude-indicator/internal-pitch-deg",Double.parseDouble(sims[5]));
						SymbolTable.getInstance().addToBindMap("/controls/flight/rudder",Double.parseDouble(sims[6]));
						SymbolTable.getInstance().addToBindMap("/controls/flight/aileron",Double.parseDouble(sims[7]));
						SymbolTable.getInstance().addToBindMap("/controls/flight/elevator",Double.parseDouble(sims[8]));
						SymbolTable.getInstance().addToBindMap("/instrumentation/altimeter/indicated-altitude-ft",Double.parseDouble(sims[9]));
						
						}catch(NumberFormatException e) {}
						catch(ArrayIndexOutOfBoundsException e) {}
					}
					in.close();
					client.close();
					Thread.sleep(1000/timesPerSec);
					}catch(SocketTimeoutException e) {}
					catch(InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
				servSock.close();
				System.out.println("Server disconnected");
			} catch (IOException e) {try {
				Thread.sleep(1000/timesPerSec);
			} catch (InterruptedException e1) {
				Thread.currentThread().interrupt();
			};}
		}
	}

	@Override
	public void close() {
		stop=true;
	}
	
	public static MyServer getInstance(){
		return Holder.instance;
	}

	@Override
	public void start() {
		MyServer.getInstance().startServer(5400, 10);//default values
	}
}
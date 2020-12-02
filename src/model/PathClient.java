package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class PathClient {
	public static String calculatePath(String ip, int port, double[][] matrix, String start, String end) {
		Socket s=null;
		PrintWriter out=null;
		BufferedReader in=null;
		String sol=null;
		try {
			//Open a client
			s=new Socket(ip,port);
			s.setSoTimeout(3000);
			out=new PrintWriter(s.getOutputStream());
			in=new BufferedReader(new InputStreamReader(s.getInputStream()));
			
			//Send Problem
			int i,j;
			for(i=0;i<matrix.length;i++){
				for(j=0;j<matrix[i].length-1;j++){
					out.print((int)matrix[i][j]+",");
				}
				out.println(matrix[i][j]);
			}
			out.println("end");
			out.println(start);
			out.println(end);
			out.flush();
			
			//Receive solution
			sol=in.readLine();
		}catch(SocketTimeoutException e){
		}catch(IOException e){
		}finally{
			try {
				in.close();
				out.close();
				s.close();

			} catch (IOException e) {}
		}
		System.out.println(sol);
		return sol;
	}
}

package server_side;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class MyTestClientHandler implements ClientHandler {
	
	Solver<Searchable, String> solver;
	CacheManager<String,String> cm;
	
	
	
	public MyTestClientHandler() {
		this.solver = new SearchSolver();
		this.cm = new FileCacheManager();
	}

	@Override
	public void handleClient(InputStream input, OutputStream output) {
		BufferedReader br=new BufferedReader(new InputStreamReader(input));
		PrintWriter clientOut = new PrintWriter(output);
		try {
			String line;
			String start="";
			String end="";
			List<String> userArrInput=new ArrayList<String>();
			int rows=0;
			while((line=br.readLine())!=null)
			{
				if(line.equals("end"))
				{
					start=br.readLine();
					end=br.readLine();
					break;
				}
				userArrInput.add(line);
				rows++;
			}
			List<String[]> spList= new ArrayList<String[]>();
			for (String linee : userArrInput) {
				spList.add(linee.split(","));
			}
			double[][] matrix=new double[rows][spList.get(0).length];
			int i=-1;
			int j=-1;
			for(String[] all : spList)
			{
				j=-1;
				i++;
				for(String num : all)
				{
					j++;
					matrix[i][j]=Integer.parseInt(num);
				}
			}
			((SearchSolver)solver).SetRoute(start, end);
			String solution=solver.solve(new SearchableMatrix(matrix));
			
			/*if((solution=cm.FindSolution(line))==null){
				solution=solver.solve(line);
				cm.SaveSolution(line, solution);
			}*/
			
			clientOut.println(solution);
			clientOut.flush();
			clientOut.close();
			br.close();
			return;

		} catch (IOException e) {System.out.println("IO Exception while trying to answer a client");}
		try {
			br.close();
		} catch (IOException e) {}
		clientOut.close();
	}

}

package server_side;

import java.util.ArrayList;
import java.util.List;

public class SearchableMatrix implements Searchable {
	private double[][] costMatrix;
	private State[][] stateMatrix;
	
	public SearchableMatrix(double[][] m)
	{
		this.costMatrix=m;
		this.stateMatrix=new State[m.length][m[0].length];
		for (int i = 0; i < m.length; i++) 
		{
			for (int j = 0; j < m[0].length; j++)
			{
				stateMatrix[i][j]=new State(i+","+j);
			}
		}
	}
	
	@Override
	public List<String> Successors(String s)
	{
		int row=Integer.parseInt(s.split(",")[0]);
		int column=Integer.parseInt(s.split(",")[1]);
		List<String> sucList = new ArrayList<>();
		if(row!=0)
		{
			sucList.add((row-1)+","+column);
		}
		if(row!=this.stateMatrix.length-1)
		{
			sucList.add((row+1)+","+column);
		}
		if(column!=0)
		{
			sucList.add(row+","+(column-1));
		}
		if(column!=this.stateMatrix[0].length-1)
		{
			sucList.add(row+","+(column+1));
		}
		return sucList;
	}

	@Override
	public int HowMany() {
		return costMatrix[0].length*costMatrix.length;
	}

	@Override
	public double PriceTo(String s) {
		int row=Integer.parseInt(s.split(",")[0]);
		int column=Integer.parseInt(s.split(",")[1]);
		return(costMatrix[row][column]);
	}

}

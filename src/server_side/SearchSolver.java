package server_side;

public class SearchSolver implements Solver<Searchable, String> {
	private Searcher searcher;
	private String start;
	private String end;
	
	
	
	public SearchSolver()
	{
		searcher=new BestFirstSearch();
		
	}

	@Override
	public String solve(Searchable p)
	{
		return searcher.FindRoute(p,start,end);
	}
	
	public void SetRoute(String start, String end)
	{
		this.end=end;
		this.start=start;
	}
}

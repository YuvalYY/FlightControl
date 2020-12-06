package server_side;

public interface CacheManager<Problem, Solution> {
	public Solution FindSolution(Problem P);
	public void SaveSolution(Problem P, Solution S);
}

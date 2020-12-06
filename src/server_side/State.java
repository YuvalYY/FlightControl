package server_side;

public class State
{
	private String state;
	private double cost;
	private State cameFrom;
	
	public State(String state)
	{
		this.state=state;
		this.cost=0;
	 	this.cameFrom=null;;
	}
	public State(String state,double cost)
	{
		this.state=state;
		this.cost=cost;
	 	this.cameFrom=null;;
	}
	
	public String GetState() {
		return state;
	}


	public void SetState(String state) {
		this.state = state;
	}


	public double GetCost() {
		return cost;
	}


	public void SetCost(double cost) {
		this.cost = cost;
	}


	public State GetCameFrom() {
		return cameFrom;
	}


	public void SetCameFrom(State cameFrom) {
		this.cameFrom = cameFrom;
	}


	@Override
	public boolean equals(Object obj) {
		return this.state.equals(((State)obj).GetState());
	}
}

package server_side;

import java.util.List;
import java.util.ArrayList;

public interface Searchable {
	public List<String> Successors(String s);
	public int HowMany();
	public double PriceTo(String s);
}

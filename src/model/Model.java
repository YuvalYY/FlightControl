package model;

import java.util.Observable;

import interpreter.lexer.Lexer;
import interpreter.parser.Parser;

public class Model extends Observable{
	
	private String mCurrentPath;

	public Model() {
		
	}
	
	public void interpret(String[] lines){
		Lexer lexer=new Lexer();
		Parser parser=new Parser();
		String[][] tokens=lexer.lexer(lines);
		parser.parse(tokens);
	}
	
	public void calculatePath(String ip, int port, double[][] matrix, String start, String end) {
		notifyAllObservers("Path", PathClient.calculatePath(ip, port, matrix, start, end));
	}
	
	public void notifyAllObservers(String id, Object arg) {
		setChanged();
		notifyObservers(new Update(id, arg));
	}
	
}

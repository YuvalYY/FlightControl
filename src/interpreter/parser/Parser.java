package interpreter.parser;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import interpreter.commands.CommandFactory;
import interpreter.parser.SymbolTable;

public class Parser {
	
	private CommandFactory commandFactory;
	
	public Parser() {
		commandFactory=new CommandFactory();
	}
	
	public void parse(String[][] lines){
		for(String[] line : lines){
			parseLine(line);
		}
	}
	
	private void parseLine(String[] lineArr){
    	List<String> line=Arrays.asList(lineArr);
    	switch(line.get(0))
    	{
    	case "var":
    	{
    		if(line.contains("=")){
    			if(line.get(3).equals("bind")) {//equals with bind
    				List<String> arguments=new LinkedList<>();
    				arguments.add(line.get(1));
    				arguments.add(line.get(4));
    				commandFactory.getCommand("bind").doCommand(arguments);
    			}
    			else {//equals without bind
    				List<String> arguments= new LinkedList<>();
    				arguments.add(line.get(1));
    				arguments.addAll(variablesToVals(line.subList(3, line.size())));
    				commandFactory.getCommand("=").doCommand(arguments);
    			}
    			
    		}
    		else {//only declaration
    			commandFactory.getCommand("var").doCommand(line.subList(1, 2));
			}
    		break;
    	}
    	case "return":
    	{
    		commandFactory.getCommand("return")
    		.doCommand(variablesToVals(line.subList(1, line.size())));
    		break;
    	}
    	
    	case "connect":
    	{
    		List<String> arguments=new LinkedList<>();
    		for(int i=1;i<line.size();i++) {
    			arguments.add(line.get(i));
    		}
    		commandFactory.getCommand("connect").doCommand(arguments);
    		break;
    	}
    	
    	case "disconnect":
    	{
    		commandFactory.getCommand("disconnect").doCommand(null);
    		break;
    	}
    	
    	case "openDataServer":
    	{
    		List<String> arguments=new LinkedList<>();
    		for(int i=1;i<line.size();i++) {
    			arguments.add(line.get(i));
    		}
    		commandFactory.getCommand("openDataServer").doCommand(arguments);
    		break;
    	}
    	
    	case "while":
    	{
    		commandFactory.getCommand("while").doCommand(line.subList(1, line.size()-2));
    		break;
    	}
    	
    	case "print":
    	{
    		if(!line.get(1).startsWith("\"")) {
    			commandFactory.getCommand("print").doCommand(variablesToVals(line.subList(1, line.size())));
    		}
    		else
    			commandFactory.getCommand("print").doCommand(line.subList(1, line.size()));
    		break;
    	}
    	
    	case "sleep":
    	{
    		commandFactory.getCommand("sleep").doCommand(variablesToVals(line.subList(1, line.size())));
    		break;
    	}
    	
    	default: //meaning it's a variable with = command
    	{
			if(line.get(2).equals("bind")) { //with bind
				List<String> arguments= new LinkedList<>();
				arguments.add(line.get(0));
				arguments.add(line.get(3));
				commandFactory.getCommand("bind").doCommand(arguments);
			}
			else { //without bind
				List<String> arguments= new LinkedList<>();
				arguments.add(line.get(0));
				arguments.addAll(variablesToVals(line.subList(2, line.size())));
				commandFactory.getCommand("=").doCommand(arguments);
			}
    		break;
    	}
    	}
    }
	
	public static List<String> variablesToVals(List<String> expression) {
		boolean afterFactor=false;
    	SymbolTable symbolTable=SymbolTable.getInstance();
    	List<String> returnList = new LinkedList<String>();
    	for (String token : expression) {
    		if(!token.matches("[)(/+*-]"))
    		{
    			returnList.add(String.valueOf(symbolTable.getVal(token)));
    			afterFactor=true;
    		}
    		else {
    			if(token.matches("[-]") && !afterFactor) {
    				returnList.add("0.0");
    				afterFactor=false;
    			}	
    			else {
    				if(token.matches("[)]"))
    					afterFactor=true;
    				else
    					afterFactor=false;
    			}
    		    returnList.add(token);
    			
    		}
		}
    	return returnList;
    }
}
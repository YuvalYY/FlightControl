package interpreter.commands;

import java.util.LinkedList;
import java.util.List;
import interpreter.parser.Parser;
import interpreter.expressions.ShuntingYard;

//might fail in case of nested loop

public class WhileCommand implements Command{
	
	private Parser parser;
	
	public WhileCommand() {
		parser=new Parser();
	}

	@Override
	public void doCommand(List<String> argumentsTemp) {
		List<String> arguments=new LinkedList<String>(argumentsTemp);
		if(!argumentsTemp.get(argumentsTemp.size()-1).equals("\n")) {
			arguments.add("\n");
		}
		List<String> condition=new LinkedList<>();
		List<String[]> tempList=new LinkedList<String[]>();
		List<String> currentLine=new LinkedList<String>();
		boolean inCondition=true;
		for(String str:arguments){
			if(inCondition) {
				if(str.equals("{")) {}
				else if(str.equals("\n")) {
					condition=new LinkedList<String>(currentLine);
					currentLine.clear();
					inCondition=false;
				}
				else {
					currentLine.add(str);
				}
			}
			else {
				if(str.equals("\n")) {
					String[] curr = new String[currentLine.size()];
					curr = currentLine.toArray(curr);
					tempList.add(curr);
					currentLine.clear();
				}
				else {
					currentLine.add(str);
				}
			}
		}
		String[][] toParse = new String[tempList.size()][];
		toParse = tempList.toArray(toParse);
		while(conditionCase(condition)){
			parser.parse(toParse);
		}
	}
	
	private boolean conditionCase(List<String> arguments)
	{
		int conIndex=conditionIndex(arguments);
		double left=ShuntingYard.calculateExpression(Parser.variablesToVals(arguments.subList(0, conIndex)));
		double right=ShuntingYard.calculateExpression(Parser.variablesToVals(arguments.subList(conIndex+1, arguments.size())));
		switch(arguments.get(conIndex))
		{
		case "==":
		{
			return left==right;
		}
		case "!=":
		{
			return left!=right;
		}
		case ">=":
		{
			return left>=right;
		}
		case "<=":
		{
			return left<=right;
		}
		case "<":
		{
			return left<right;
		}
		case ">":
		{
			return left>right;
		}
		default:
		{
			//never suppose to get here
			return false;
		}
		}
	}
	
	private int conditionIndex(List<String> arguments){
		for(int i=0;i<arguments.size();i++) {
			if(arguments.get(i).matches("==|<=|>=|!=|>|<"))
				return i;
		}
		return -1;
	}
}
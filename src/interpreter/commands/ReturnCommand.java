package interpreter.commands;

import java.util.List;
import interpreter.expressions.ShuntingYard;
import interpreter.parser.SymbolTable;

public class ReturnCommand implements Command{

	public ReturnCommand() {}
	
	@Override
	public void doCommand(List<String> arguments) {
		SymbolTable.getInstance().setVal("returnValue", ShuntingYard.calculateExpression(arguments));
	}

}

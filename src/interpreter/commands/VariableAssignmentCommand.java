package interpreter.commands;

import java.util.List;
import interpreter.expressions.ShuntingYard;
import interpreter.parser.SymbolTable;

public class VariableAssignmentCommand implements Command {

	public VariableAssignmentCommand() {}
	
	@Override
	public void doCommand(List<String> arguments) {
		SymbolTable.getInstance().setVal(arguments.get(0),
				ShuntingYard.calculateExpression(arguments.subList(1, arguments.size())));
	}

}
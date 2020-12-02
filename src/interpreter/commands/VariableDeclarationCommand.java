package interpreter.commands;

import java.util.List;

import interpreter.parser.SymbolTable;

public class VariableDeclarationCommand implements Command {
	
	public VariableDeclarationCommand() {}

	@Override
	public void doCommand(List<String> arguments) {
		SymbolTable.getInstance().setVal(arguments.get(0), 0.0);
	}

}
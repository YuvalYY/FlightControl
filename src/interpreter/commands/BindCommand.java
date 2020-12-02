package interpreter.commands;

import java.util.List;

import interpreter.parser.SymbolTable;

public class BindCommand implements Command{
	public BindCommand() {}

	@Override
	public void doCommand(List<String> arguments) {
		SymbolTable.getInstance().bind(arguments.get(0), arguments.get(1).substring(1, arguments.get(1).length()-1));
	}

}
package interpreter.commands;

import java.util.List;

import interpreter.expressions.ShuntingYard;

public class PrintCommand implements Command {

	public PrintCommand() {}
	
	@Override
	public void doCommand(List<String> arguments) {
		if(!arguments.get(0).startsWith("\""))
			System.out.println(ShuntingYard.calculateExpression(arguments));
		else
		{
			for (String string : arguments) {
				System.out.print(string.replaceAll("\"", "")+" ");
			}
			System.out.println();
		}
	}

}

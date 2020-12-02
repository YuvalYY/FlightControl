package interpreter.commands;

import java.util.List;

import interpreter.expressions.ShuntingYard;

public class SleepCommand implements Command{
	public SleepCommand() {}

	@Override
	public void doCommand(List<String> arguments) { //TODO Find out why you need to surround it with try/catch and check what it means
		try {
			Thread.sleep(ShuntingYard.calculateExpression(arguments).longValue());
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
	
	
}

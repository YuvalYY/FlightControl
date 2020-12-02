package interpreter.commands;

import java.util.List;

public interface Command {
	public void doCommand(List<String> arguments);
}

package interpreter.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class CommandFactory {
	
	private Map<String, Supplier<Command>> commandsSupplierMap;
	
	public CommandFactory()
	{
		this.commandsSupplierMap = new HashMap<>();
		commandsSupplierMap.put("bind", () -> new BindCommand());
		commandsSupplierMap.put("connect", () -> new ConnectCommand());
		commandsSupplierMap.put("disconnect", () -> new DisconnectCommand());
		commandsSupplierMap.put("openDataServer", () -> new OpenDataServerCommand());
		commandsSupplierMap.put("print", () -> new PrintCommand());
		commandsSupplierMap.put("return", () -> new ReturnCommand());
		commandsSupplierMap.put("sleep", () -> new SleepCommand());
		commandsSupplierMap.put("=", () -> new VariableAssignmentCommand());
        commandsSupplierMap.put("var", () -> new VariableDeclarationCommand());
        commandsSupplierMap.put("while", () -> new WhileCommand());
	}
	
	public Command getCommand(String commandKey) {

        return commandsSupplierMap.get(commandKey).get();
    }
}
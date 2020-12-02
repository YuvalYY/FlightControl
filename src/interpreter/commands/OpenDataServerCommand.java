package interpreter.commands;

import java.util.List;

import interpreter.server.MyServer;

public class OpenDataServerCommand implements Command{
	public OpenDataServerCommand() { }

	@Override
	public void doCommand(List<String> arguments) {
		MyServer.getInstance().startServer(Integer.parseInt(arguments.get(0)), Integer.parseInt(arguments.get(1)));
	}
}
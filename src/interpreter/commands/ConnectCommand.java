package interpreter.commands;

import java.util.List;

import interpreter.client.MyClient;

public class ConnectCommand implements Command{
	public ConnectCommand() {}

	@Override
	public void doCommand(List<String> arguments) {
		MyClient client=MyClient.getInstance();
		client.connect(arguments.get(0), Integer.parseInt(arguments.get(1)));
	}
	
}
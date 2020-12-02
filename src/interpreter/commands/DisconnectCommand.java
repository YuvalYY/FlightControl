package interpreter.commands;

import java.util.List;

import interpreter.client.MyClient;

public class DisconnectCommand implements Command{
	public DisconnectCommand() {}

	@Override
	public void doCommand(List<String> arguments) {
		MyClient client=MyClient.getInstance();
		client.close();
	}
}
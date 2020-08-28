package net.sahet.lsp.chatapp.server;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

import net.sahet.lsp.chatapp.client.Client;
import net.sahet.lsp.chatapp.client.Message;

public class ServerImpl implements Server {

	private final List<Message> messages = new CopyOnWriteArrayList<>();
	private final List<Client> clients = new CopyOnWriteArrayList<>();

	@Override
	public CompletableFuture<List<Message>> fetchMessages() {
		return CompletableFuture.completedFuture(messages);
	}

	@Override
	public void postMessage(Message message) {
		messages.add(message);
		clients.forEach(cli -> cli.didPostMessage(message));
	}

	/**
	 * Adds client which is being communicated and returns Runnable which will be
	 * executed to disconnect the client
	 * 
	 * @param client
	 * @return
	 */
	public Runnable addClient(Client client) {
		clients.add(client);
		Runnable runnable = () -> clients.remove(client);
		return runnable;
	}

}

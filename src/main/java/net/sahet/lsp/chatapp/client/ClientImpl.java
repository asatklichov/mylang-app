package net.sahet.lsp.chatapp.client;

public class ClientImpl implements Client {

	@Override
	public void didPostMessage(Message message) {
		System.out.println("Message from "+ message.getUser() + ": " + message.getContent());
	}

}

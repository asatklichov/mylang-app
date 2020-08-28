package net.sahet.lsp.chatapp.client;

import org.eclipse.lsp4j.jsonrpc.services.JsonNotification;
import org.eclipse.lsp4j.jsonrpc.services.JsonSegment;

import net.sahet.lsp.chatapp.server.Server;

@JsonSegment("client")
public interface Client {

	/**
	 * This method is used by server to notify all clients in a response to {@link Server#postMessage()}.
	 * 
	 * @param message
	 */
	@JsonNotification
	void didPostMessage(Message message);
}

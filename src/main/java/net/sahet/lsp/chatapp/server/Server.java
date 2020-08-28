package net.sahet.lsp.chatapp.server;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.eclipse.lsp4j.jsonrpc.services.JsonNotification;
import org.eclipse.lsp4j.jsonrpc.services.JsonRequest;
import org.eclipse.lsp4j.jsonrpc.services.JsonSegment;

import net.sahet.lsp.chatapp.client.Message;

/**
 * Endpoint is a interface to handle JSON-RPC messages with generic JSON
 * elements. Here is a simple version of this Endpoint.
 * 
 * A service interface has methods annotated with @JsonRequest
 * or @JsonNotification annotations.
 * 
 * 
 * @JsonRequest - Annotation to mark a request method on an interface or class.
 * 
 * @JsonNotification - Annotation to mark a notification method on an interface
 *                   or class.
 * 
 *
 */
@JsonSegment("server")
public interface Server {

	/**
	 * This request method is used by client to fetch posted messages.
	 * Request method  - {{@link requestmethod https://github.com/eclipse/lsp4j/blob/master/org.eclipse.lsp4j.jsonrpc/src/main/java/org/eclipse/lsp4j/jsonrpc/services/JsonRequest.java}
	 * 
	 */
	@JsonRequest
	CompletableFuture<List<Message>> fetchMessages();

	/**
	 * This method is used by client to post a new message to server. It is servers
	 * responsibility to store messages posted by clients and broadcast them to
	 * clients.
	 * 
	 * Notify method  - {{@link notifymethod https://github.com/eclipse/lsp4j/blob/master/org.eclipse.lsp4j.jsonrpc/src/main/java/org/eclipse/lsp4j/jsonrpc/services/JsonNotification.java}
	 * 
	 * 
	 * @param message
	 */
	@JsonNotification
	void postMessage(Message message);

}

package net.sahet.lsp.mylang.client;

import org.eclipse.lsp4j.jsonrpc.services.JsonNotification;
import org.eclipse.lsp4j.services.LanguageClient;

import net.sahet.lsp.chatapp.client.Message;

public interface OtherLangClient extends LanguageClient {
	  @JsonNotification("poc/toClient")
	  void sendFoobar(Message msg);
	}
package net.sahet.lsp.mylang.server;

import java.util.concurrent.CompletableFuture;

import org.eclipse.lsp4j.CompletionOptions;
import org.eclipse.lsp4j.InitializeParams;
import org.eclipse.lsp4j.InitializeResult;
import org.eclipse.lsp4j.ServerCapabilities;
import org.eclipse.lsp4j.TextDocumentSyncKind;
import org.eclipse.lsp4j.services.LanguageClient;
import org.eclipse.lsp4j.services.LanguageClientAware;
import org.eclipse.lsp4j.services.LanguageServer;
import org.eclipse.lsp4j.services.TextDocumentService;
import org.eclipse.lsp4j.services.WorkspaceService;

import net.sahet.lsp.mylang.service.HelloWorldTextDocumentService;
import net.sahet.lsp.mylang.service.HelloWorldWorkspaceService;

public class HelloWorldLanguageServer implements LanguageServer, LanguageClientAware {
	private TextDocumentService textDocumentService;
	private WorkspaceService workspaceService;
	private LanguageClient client;
	private int errorCode = 1;

	public HelloWorldLanguageServer() {
		this.textDocumentService = new HelloWorldTextDocumentService();
		this.workspaceService = new HelloWorldWorkspaceService();
	}

	@Override
	public CompletableFuture<InitializeResult> initialize(InitializeParams initializeParams) {
		 // Initialize the InitializeResult for this LS.
		final InitializeResult initializeResult = new InitializeResult(new ServerCapabilities());

		// Set the capabilities of the LS to inform the client.
		initializeResult.getCapabilities().setTextDocumentSync(TextDocumentSyncKind.Full);
		CompletionOptions completionOptions = new CompletionOptions();
		/**
		 * Add the completion provider capability to the initialize result so that the
		 * client knows the server is able to handle completion requests:
		 */
		initializeResult.getCapabilities().setCompletionProvider(completionOptions);
		return CompletableFuture.supplyAsync(() -> initializeResult);
	}

	@Override
	public CompletableFuture<Object> shutdown() {
		// If shutdown request comes from client, set the error code to 0.
		errorCode = 0;
		return null;
	}

	@Override
	public void exit() {
		// Kill the LS on exit request from client.
		System.exit(errorCode);
	}

	@Override
	public TextDocumentService getTextDocumentService() {
		 // Return the endpoint for language features.
		return this.textDocumentService;
	}

	@Override
	public WorkspaceService getWorkspaceService() {
		 // Return the endpoint for workspace functionality.
		return this.workspaceService;
	}

	@Override
	public void connect(LanguageClient languageClient) {
		 // Get the client which started this LS.
		this.client = languageClient;
	}
}

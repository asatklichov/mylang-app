package net.sahet.lsp.mylang.server;

import static java.lang.Boolean.TRUE;

import java.util.concurrent.CompletableFuture;

import org.eclipse.lsp4j.CompletionOptions;
import org.eclipse.lsp4j.InitializeParams;
import org.eclipse.lsp4j.InitializeResult;
import org.eclipse.lsp4j.ServerCapabilities;
import org.eclipse.lsp4j.TextDocumentSyncKind;
import org.eclipse.lsp4j.services.LanguageClient;
import org.eclipse.lsp4j.services.LanguageServer;
import org.eclipse.lsp4j.services.TextDocumentService;
import org.eclipse.lsp4j.services.WorkspaceService;

import lombok.Getter;
import net.sahet.lsp.mylang.service.MyTextDocumentService;
import net.sahet.lsp.mylang.service.MyWorkspaceService;

/**
 * LanguageServer interface should be implemented to provide a language server,
 * similar to {@docRoot ServerImpl}
 * 
 * 
 * LanguageServer - Interface for implementations of
 * https://github.com/Microsoft/vscode-languageserver-protocol
 *
 */
@Getter
public class MyLanguageServerImpl implements LanguageServer {

	private TextDocumentService textService;
	private WorkspaceService workspaceService;
	private LanguageClient client;

	public MyLanguageServerImpl() {
		textService = new MyTextDocumentService(this);
		workspaceService = new MyWorkspaceService();
	}

	/**
	 * “initialize” is the entry method which LS and the LS Client initialize the
	 * connection and let the client know what capabilities(such as auto-completion,
	 * formatting, find all references) that the LS supports.
	 */
	public CompletableFuture<InitializeResult> initialize(InitializeParams params) {
		final InitializeResult res = new InitializeResult(new ServerCapabilities());
		res.getCapabilities().setCodeActionProvider(TRUE);
		res.getCapabilities().setCompletionProvider(new CompletionOptions());
		res.getCapabilities().setDefinitionProvider(TRUE);
		res.getCapabilities().setHoverProvider(TRUE);
		res.getCapabilities().setReferencesProvider(TRUE);
		res.getCapabilities().setTextDocumentSync(TextDocumentSyncKind.Full);
		res.getCapabilities().setDocumentSymbolProvider(TRUE);

//		final CompletionOptions completionOptions = new CompletionOptions();
//        completionOptions.setTriggerCharacters(List.of(":", ".", ">", "@"));
//		res.getCapabilities().setCompletionProvider(completionOptions);
//        res.getCapabilities().setTextDocumentSync(TextDocumentSyncKind.Full);
//        res.getCapabilities().setSignatureHelpProvider(signatureHelpOptions);
//        res.getCapabilities().setHoverProvider(true);
//        res.getCapabilities().setDocumentSymbolProvider(false);
//        res.getCapabilities().setDefinitionProvider(true);
//        res.getCapabilities().setReferencesProvider(false);
//        res.getCapabilities().setCodeActionProvider(true);
//        res.getCapabilities().setExecuteCommandProvider(executeCommandOptions);
//        res.getCapabilities().setDocumentFormattingProvider(true);
//        res.getCapabilities().setRenameProvider(false);
//        res.getCapabilities().setWorkspaceSymbolProvider(false);
//        res.getCapabilities().setImplementationProvider(false);
//        res.getCapabilities().setCodeLensProvider(new CodeLensOptions());

		return CompletableFuture.supplyAsync(() -> res);
	}

	public CompletableFuture<Object> shutdown() {
		return CompletableFuture.supplyAsync(() -> TRUE);
	}

	public void exit() {
		System.out.println("--- Terminating Language Server --- ");
	}

	public TextDocumentService getTextDocumentService() {
		return this.textService;
	}

	/**
	 * overridden “getTextDocumentService()” returns instances of
	 * HelloTextDocumentService class
	 */
	public WorkspaceService getWorkspaceService() {
		return this.workspaceService;
	}

	/**
	 * overridden “getWorkspaceService()” returns instances of HelloWorkspaceService
	 * class.
	 */
	public void setRemoteProxy(LanguageClient remoteProxy) {
		this.client = remoteProxy;
	}

}

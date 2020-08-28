package net.sahet.lsp.mylang.app;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.eclipse.lsp4j.launch.LSPLauncher;
import org.eclipse.lsp4j.services.LanguageClient;

import net.sahet.lsp.mylang.server.HelloWorldLanguageServer;

public class HelloWorldStdioLauncher {
	public static void main(String[] args) throws ExecutionException, InterruptedException {
		startServer(System.in, System.out);
	}

	private static void startServer(InputStream in, OutputStream out) throws ExecutionException, InterruptedException {
		// Initialize the HelloLanguageServer
		HelloWorldLanguageServer helloLanguageServer = new HelloWorldLanguageServer();
		// Create JSON RPC launcher for HelloLanguageServer instance.
		Launcher<LanguageClient> launcher = LSPLauncher.createServerLauncher(helloLanguageServer, in, out);

		// Get the client that request to launch the LS.
		LanguageClient client = launcher.getRemoteProxy();

		// Set the client to language server
		helloLanguageServer.connect(client);

		// Start the listener for JsonRPC
		Future<?> startListening = launcher.startListening();

		// Get the computed result from LS.
		Object x = startListening.get();
		System.out.println(x);
	}
}

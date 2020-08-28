package net.sahet.lsp.mylang.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.annotation.Nonnull;

import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.eclipse.lsp4j.launch.LSPLauncher;
import org.eclipse.lsp4j.services.LanguageClient;
import org.eclipse.lsp4j.services.LanguageServer;

import lombok.experimental.UtilityClass;
import net.sahet.lsp.mylang.client.OtherLangClient;
import net.sahet.lsp.mylang.server.MyLanguageServerImpl;

@UtilityClass
public class MyLSPLauncher {

	public static void launchServer(InputStream in, OutputStream out)
			throws IOException, InterruptedException, ExecutionException {
		MyLanguageServerImpl server = new MyLanguageServerImpl();
		Launcher<LanguageClient> l = MyLSPLauncher.createServerLauncher(server, in, out);
		Future<?> startListening = l.startListening();
		server.setRemoteProxy(l.getRemoteProxy());
		startListening.get();
	}

	public static void launchServerWithSocket(InputStream in, OutputStream out)
			throws InterruptedException, ExecutionException, IOException {
		MyLanguageServerImpl server = new MyLanguageServerImpl();
		Launcher<LanguageClient> l = MyLSPLauncher.createServerLauncherWithSocket(server);
		Future<?> startListening = l.startListening();
		server.setRemoteProxy(l.getRemoteProxy());
		startListening.get();
	}

	public  Launcher<LanguageClient> createServerLauncherWithOtherClient(LanguageServer server, InputStream in, OutputStream out) { 
		return new LSPLauncher.Builder<LanguageClient>()
		        .setLocalService(server)
		        .setRemoteInterface(OtherLangClient.class)
		        .setInput(in)
		        .setOutput(out)
		        .create();
	}

	/**
	 * Create a new Launcher for a language server
	 * 
	 * @param server
	 * @return
	 * @throws IOException
	 */
	private Launcher<LanguageClient> createServerLauncherWithSocket(@Nonnull LanguageServer server) throws IOException {
		try (ServerSocket serverSocket = new ServerSocket(MySocketLauncher.LSP_PORT)) {
			Socket socket = serverSocket.accept();
			return createServerLauncher(server, socket.getInputStream(), socket.getOutputStream());
		}
	}

	/**
	 * Create a new Launcher for a language server and an input and output stream.
	 * 
	 * @param server
	 * @param in
	 * @param out
	 * @return
	 */
	private Launcher<LanguageClient> createServerLauncher(@Nonnull LanguageServer server, @Nonnull InputStream in,
			@Nonnull OutputStream out) {
		return LSPLauncher.createServerLauncher(server, in, out);
	}
	
	

}

package net.sahet.lsp.mylang.service;

import java.net.Socket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
//import java.util.concurrent.Future;

import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.eclipse.lsp4j.jsonrpc.RemoteEndpoint;

public class MySocketLauncher<T> implements Launcher<T> {

	public static final Integer LSP_PORT = 1044;
	public static String HOST = "localhost";

	private final Launcher<T> launcher;

	public MySocketLauncher(Object localService, Class<T> remoteInterface, Socket socket) {
		try {
			this.launcher = Launcher.createLauncher(localService, remoteInterface, socket.getInputStream(),
					socket.getOutputStream());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public CompletableFuture<Void> startListening() {
		return CompletableFuture.runAsync(() -> {
			try {
				this.launcher.startListening().get();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}, Executors.newSingleThreadExecutor());
	}

	public T getRemoteProxy() {
		return this.launcher.getRemoteProxy();
	}

	@Override
	public RemoteEndpoint getRemoteEndpoint() {
		return null;
	}

}

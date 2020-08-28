package net.sahet.lsp.chatapp.client;

import java.net.Socket;
import java.util.Scanner;

import net.sahet.lsp.chatapp.server.Server;
import net.sahet.lsp.mylang.service.MySocketLauncher;

public class ChatClientApp {

	/**
	 * Firstly Start the ChatServerApplication
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Client chatClient = new ClientImpl();

		System.out.println("Chat client launched to listen on port " + MySocketLauncher.LSP_PORT);
		try (Socket socket = new Socket("localhost", MySocketLauncher.LSP_PORT);
				Scanner scanner = new Scanner(System.in)) {
			MySocketLauncher<Server> launcher = new MySocketLauncher<>(chatClient, Server.class, socket);
			//launcher.startListening().get();
			launcher.startListening().thenRun(() -> System.exit(0));

			Server langServer = launcher.getRemoteProxy();
			System.out.println("Enter User ID: ");

			String user = scanner.nextLine();
			//print existing messages on server
			langServer.fetchMessages().get().forEach(msg -> chatClient.didPostMessage(msg));			

			//send every entered message to server 
			while (true) {
				String content = scanner.nextLine();
				langServer.postMessage(new Message(user, content));
			}

		}
	}
}

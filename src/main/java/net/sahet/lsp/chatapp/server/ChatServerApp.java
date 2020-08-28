package net.sahet.lsp.chatapp.server;


import net.sahet.lsp.chatapp.client.Client;
import net.sahet.lsp.mylang.service.MySocketLauncher;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServerApp {

    public static void main(String[] args) throws Exception { 
        ServerImpl chatServer = new ServerImpl();
        ExecutorService threadPool = Executors.newCachedThreadPool();         
        
        try (ServerSocket serverSocket = new ServerSocket(MySocketLauncher.LSP_PORT)) {
            System.out.println("The chat server is running on port " + MySocketLauncher.LSP_PORT);
            threadPool.submit(() -> {
                while (true) {
                    Socket socket = serverSocket.accept();
                    //JSON-RPC connection 
                    MySocketLauncher<Client> launcher = new MySocketLauncher<>(chatServer, Client.class, socket);
                    //connect remote client proxy (JSON-RPC interface) to server
                    Runnable removeClient = chatServer.addClient(launcher.getRemoteProxy());
                    //launcher.startListening().get();
                    launcher.startListening().thenRun(removeClient);
                }
            });
            System.out.println("Enter any character to stop");
            System.in.read();
            System.exit(0);
        }
    }

}

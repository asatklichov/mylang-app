package net.sahet.lsp.mylang.app;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import lombok.Getter;
import net.sahet.lsp.mylang.service.MyLSPLauncher;

@Getter
public class MyLangServerApp {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		try {
			MyLSPLauncher.launchServer(System.in, System.out);
		} catch (IOException e) {
			System.err.printf("Unable to start server using pipe communication, %s", e);
		} catch (InterruptedException e) {
			System.err.printf("InterruptedException exception due to, %s", e);
		} catch (ExecutionException e) {
			System.err.printf("An error occurred while starting a language server, %s", e);
		}
	}
}

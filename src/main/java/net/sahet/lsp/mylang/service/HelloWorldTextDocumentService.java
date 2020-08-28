package net.sahet.lsp.mylang.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.eclipse.lsp4j.CodeAction;
import org.eclipse.lsp4j.CodeActionParams;
import org.eclipse.lsp4j.CodeLens;
import org.eclipse.lsp4j.CodeLensParams;
import org.eclipse.lsp4j.Command;
import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.CompletionItemKind;
import org.eclipse.lsp4j.CompletionList;
import org.eclipse.lsp4j.CompletionParams;
import org.eclipse.lsp4j.DidChangeTextDocumentParams;
import org.eclipse.lsp4j.DidCloseTextDocumentParams;
import org.eclipse.lsp4j.DidOpenTextDocumentParams;
import org.eclipse.lsp4j.DidSaveTextDocumentParams;
import org.eclipse.lsp4j.DocumentFormattingParams;
import org.eclipse.lsp4j.DocumentHighlight;
import org.eclipse.lsp4j.DocumentOnTypeFormattingParams;
import org.eclipse.lsp4j.DocumentRangeFormattingParams;
import org.eclipse.lsp4j.DocumentSymbol;
import org.eclipse.lsp4j.DocumentSymbolParams;
import org.eclipse.lsp4j.Hover;
import org.eclipse.lsp4j.Location;
import org.eclipse.lsp4j.ReferenceParams;
import org.eclipse.lsp4j.RenameParams;
import org.eclipse.lsp4j.SignatureHelp;
import org.eclipse.lsp4j.SymbolInformation;
import org.eclipse.lsp4j.TextDocumentPositionParams;
import org.eclipse.lsp4j.TextEdit;
import org.eclipse.lsp4j.WorkspaceEdit;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.eclipse.lsp4j.services.TextDocumentService;

public class HelloWorldTextDocumentService implements TextDocumentService {

	/**
	 * CompletionItem and filling it with what type of completion item is this and
	 * what is the text to be inserted and description and label of the completion
	 * item.
	 * 
	 * If you have a language AST or Source processor(string processor), this is the
	 * place where you can put your logic to filter out what to be provided as
	 * completion based on the line, column and the file which are provided as a
	 * parameter to the completion() method as CompletionParams.
	 *
	 * @param unresolved
	 * @return
	 */
	@Override
	public CompletableFuture<Either<List<CompletionItem>, CompletionList>> completion(
			CompletionParams completionParams) {
		// Provide completion item.
		return CompletableFuture.supplyAsync(() -> {
			List<CompletionItem> completionItems = new ArrayList<>();
			try {
				// Sample Completion item for sayHello
				CompletionItem completionItem = new CompletionItem();
				// Define the text to be inserted in to the file if the completion item is
				// selected.
				completionItem.setInsertText("sayHello() {\n    print(\"hello\")\n}");
				// Set the label that shows when the completion drop down appears in the Editor.
				completionItem.setLabel("sayHello()");
				// Set the completion kind. This is a snippet.
				// That means it replace character which trigger the completion and
				// replace it with what defined in inserted text.
				completionItem.setKind(CompletionItemKind.Snippet);
				// This will set the details for the snippet code which will help user to
				// understand what this completion item is.
				completionItem.setDetail("sayHello()\n this will say hello to the people");

				// Add the sample completion item to the list.
				completionItems.add(completionItem);
			} catch (Exception e) {
				// TODO: Handle the exception.
			}

			// Return the list of completion items.
			return Either.forLeft(completionItems);
		});
	}

	@Override
	public CompletableFuture<CompletionItem> resolveCompletionItem(CompletionItem completionItem) {
		return null;
	}

	@Override
	public CompletableFuture<Hover> hover(TextDocumentPositionParams textDocumentPositionParams) {
		return null;
	}

	@Override
	public CompletableFuture<SignatureHelp> signatureHelp(TextDocumentPositionParams textDocumentPositionParams) {
		return null;
	}

	@Override
	public CompletableFuture<List<? extends Location>> definition(
			TextDocumentPositionParams textDocumentPositionParams) {
		return null;
	}

	@Override
	public CompletableFuture<List<? extends Location>> references(ReferenceParams referenceParams) {
		return null;
	}

	@Override
	public CompletableFuture<List<? extends DocumentHighlight>> documentHighlight(
			TextDocumentPositionParams textDocumentPositionParams) {
		return null;
	}

	@Override
	public CompletableFuture<List<Either<SymbolInformation, DocumentSymbol>>> documentSymbol(
			DocumentSymbolParams documentSymbolParams) {
		return null;
	}

	@Override
	public CompletableFuture<List<Either<Command, CodeAction>>> codeAction(CodeActionParams codeActionParams) {
		return null;
	}

	@Override
	public CompletableFuture<List<? extends CodeLens>> codeLens(CodeLensParams codeLensParams) {
		return null;
	}

	@Override
	public CompletableFuture<CodeLens> resolveCodeLens(CodeLens codeLens) {
		return null;
	}

	@Override
	public CompletableFuture<List<? extends TextEdit>> formatting(DocumentFormattingParams documentFormattingParams) {
		return null;
	}

	@Override
	public CompletableFuture<List<? extends TextEdit>> rangeFormatting(
			DocumentRangeFormattingParams documentRangeFormattingParams) {
		return null;
	}

	@Override
	public CompletableFuture<List<? extends TextEdit>> onTypeFormatting(
			DocumentOnTypeFormattingParams documentOnTypeFormattingParams) {
		return null;
	}

	@Override
	public CompletableFuture<WorkspaceEdit> rename(RenameParams renameParams) {
		return null;
	}

	@Override
	public void didOpen(DidOpenTextDocumentParams didOpenTextDocumentParams) {

	}

	@Override
	public void didChange(DidChangeTextDocumentParams didChangeTextDocumentParams) {

	}

	@Override
	public void didClose(DidCloseTextDocumentParams didCloseTextDocumentParams) {

	}

	@Override
	public void didSave(DidSaveTextDocumentParams didSaveTextDocumentParams) {

	}
}

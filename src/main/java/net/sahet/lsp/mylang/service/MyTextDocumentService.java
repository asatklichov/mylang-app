package net.sahet.lsp.mylang.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.eclipse.lsp4j.CodeAction;
import org.eclipse.lsp4j.CodeActionParams;
import org.eclipse.lsp4j.CodeLens;
import org.eclipse.lsp4j.CodeLensParams;
import org.eclipse.lsp4j.Command;
import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.CompletionList;
import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.DiagnosticSeverity;
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
import org.eclipse.lsp4j.MarkedString;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.PublishDiagnosticsParams;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.ReferenceParams;
import org.eclipse.lsp4j.RenameParams;
import org.eclipse.lsp4j.SignatureHelp;
import org.eclipse.lsp4j.SymbolInformation;
import org.eclipse.lsp4j.TextDocumentPositionParams;
import org.eclipse.lsp4j.TextEdit;
import org.eclipse.lsp4j.WorkspaceEdit;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.eclipse.lsp4j.services.TextDocumentService;

import net.sahet.lsp.mylang.server.MyLanguageServerImpl;
import net.sahet.lsp.mylang.service.DocumentModel.Route;

public class MyTextDocumentService implements TextDocumentService {

	private final Map<String, DocumentModel> docs = Collections.synchronizedMap(new HashMap<>());
	private MyLanguageServerImpl languageServer;

	public MyTextDocumentService(MyLanguageServerImpl myLanguageServer) {
		this.languageServer = languageServer;
	}

	/**
	 * We will be working in the completion function. This function takes the
	 * position that the request for completion was made within the target document,
	 * and returns a list of CompletionItems or a CompletionList.
	 * 
	 * 
	 * @param position
	 * @return
	 */
	public CompletableFuture<Either<List<CompletionItem>, CompletionList>> completion(
			TextDocumentPositionParams position) {
		List<CompletionItem> completionItems = new ArrayList<>();
		completionItems.add(new CompletionItem("Hello World"));

		completionItems.add(new CompletionItem("First Suggestion"));
		completionItems.add(new CompletionItem("Second Suggestion"));
		completionItems.add(new CompletionItem("Third Suggestion"));

		/**
		 * Here we return a completed future as we do not need to wait for the
		 * completion suggestions to be generated. With any responses that require some
		 * time to compile, futures are used to asynchronously create the results. That
		 * way the client does not have to freeze up waiting for responses from the
		 * server.
		 */
		// return CompletableFuture.completedFuture(Either.forLeft(completionItems));

		return CompletableFuture.supplyAsync(() -> Either.forLeft(ConnectionMap.INSTANCE.all.stream().map(word -> {
			CompletionItem item = new CompletionItem();
			item.setLabel(word);
			item.setInsertText(word);
			return item;
		}).collect(Collectors.toList())));
	}

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
	public CompletableFuture<CompletionItem> resolveCompletionItem(CompletionItem unresolved) {
		return null;
	}

	/**
	 * Creating Location Dependent Hover
	 * 
	 * In the hover function, we will be generating a response that changes based on
	 * where it is within the document and what information is there.
	 * 
	 * The code below when put in the hover function takes the document model for
	 * the current text document, filters through the session present in the
	 * document and finds the one on the same line as the hover request. Hover
	 * content is then created dependent on this session, containing the session
	 * difficulty, and returned:
	 * 
	 * 
	 */
	@Override
	public CompletableFuture<Hover> hover(TextDocumentPositionParams position) {
		return CompletableFuture.supplyAsync(() -> {
			DocumentModel doc = docs.get(position.getTextDocument().getUri());
			Hover res = new Hover();
			res.setContents(doc.getResolvedRoutes().stream()
					.filter(route -> route.line == position.getPosition().getLine()).map(route -> route.name)
					.map(ConnectionMap.INSTANCE.type::get).map(this::getHoverContent).collect(Collectors.toList()));
			return res;
		});
	}

	@Override
	public CompletableFuture<SignatureHelp> signatureHelp(TextDocumentPositionParams position) {
		return null;
	}

	@Override
	public CompletableFuture<List<? extends Location>> definition(TextDocumentPositionParams position) {
		return CompletableFuture.supplyAsync(() -> {
			DocumentModel doc = docs.get(position.getTextDocument().getUri());
			String variable = doc.getVariable(position.getPosition().getLine(), position.getPosition().getCharacter());
			if (variable != null) {
				int variableLine = doc.getDefintionLine(variable);
				if (variableLine == -1) {
					return Collections.emptyList();
				}
				Location location = new Location(position.getTextDocument().getUri(),
						new Range(new Position(variableLine, 0), new Position(variableLine, variable.length())));
				return Collections.singletonList(location);
			}
			return null;
		});
	}

	@Override
	public CompletableFuture<List<? extends Location>> references(ReferenceParams params) {
		return CompletableFuture.supplyAsync(() -> {
			DocumentModel doc = docs.get(params.getTextDocument().getUri());
			String variable = doc.getVariable(params.getPosition().getLine(), params.getPosition().getCharacter());
			if (variable != null) {
				return doc.getResolvedRoutes().stream().filter(
						route -> route.text.contains("${" + variable + "}") || route.text.startsWith(variable + "="))
						.map(route -> new Location(params.getTextDocument().getUri(),
								new Range(new Position(route.line, route.text.indexOf(variable)),
										new Position(route.line, route.text.indexOf(variable) + variable.length()))))
						.collect(Collectors.toList());
			}
			String routeName = doc.getResolvedRoutes().stream()
					.filter(route -> route.line == params.getPosition().getLine()).collect(Collectors.toList())
					.get(0).name;
			return doc.getResolvedRoutes().stream().filter(route -> route.name.equals(routeName))
					.map(route -> new Location(params.getTextDocument().getUri(),
							new Range(new Position(route.line, 0), new Position(route.line, route.text.length()))))
					.collect(Collectors.toList());
		});
	}

	@Override
	public CompletableFuture<List<? extends DocumentHighlight>> documentHighlight(TextDocumentPositionParams position) {
		return null;
	}

	@Override
	public CompletableFuture<List<Either<SymbolInformation, DocumentSymbol>>> documentSymbol(
			DocumentSymbolParams params) {
		DocumentModel model = docs.get(params.getTextDocument().getUri());
		// if(model == null)
		return null;
//		return CompletableFuture.supplyAsync(() ->
//			docs.get(params.getTextDocument().getUri()).getResolvedLines().stream().map(line -> {
//				SymbolInformation symbol = new SymbolInformation();
//				symbol.setLocation(new Location(params.getTextDocument().getUri(), new Range(
//						new Position(line.line, line.charOffset),
//						new Position(line.line, line.charOffset + line.text.length()))));
//				if (line instanceof VariableDefinition) {
//					symbol.setKind(SymbolKind.Variable);
//					symbol.setName(((VariableDefinition) line).variableName);
//				} else if (line instanceof Route) {
//					symbol.setKind(SymbolKind.String);
//					symbol.setName(((Route) line).name);
//				}
//				return symbol;
//			}).collect(Collectors.toList())
//		);
	}

	@Override
	public CompletableFuture<List<Either<Command, CodeAction>>> codeAction(CodeActionParams params) {
		return null;
	}

	@Override
	public CompletableFuture<List<? extends CodeLens>> codeLens(CodeLensParams params) {
		return null;
	}

	@Override
	public CompletableFuture<CodeLens> resolveCodeLens(CodeLens unresolved) {
		return null;
	}

	@Override
	public CompletableFuture<List<? extends TextEdit>> formatting(DocumentFormattingParams params) {
		return null;
	}

	@Override
	public CompletableFuture<List<? extends TextEdit>> rangeFormatting(DocumentRangeFormattingParams params) {
		return null;
	}

	@Override
	public CompletableFuture<List<? extends TextEdit>> onTypeFormatting(DocumentOnTypeFormattingParams params) {
		return null;
	}

	@Override
	public CompletableFuture<WorkspaceEdit> rename(RenameParams params) {
		return null;
	}

	/////// Consuming the Document ////////
	/**
	 * Learning goal: Learn how to use the client's document to form responses from
	 * endpoints. The endpoint we will be developing in this exercises will be the
	 * hover endpoint and we will be using the didOpen and didChange notifications
	 * to keep an updated model of the document on the server.
	 * 
	 */

	//////// Sending Notifications
	/**
	 * We are beginning this sequence of calls when the server receives a didChange
	 * or didOpen notification, but using this technique, you can send the client
	 * notifications at any point.
	 */

	/**
	 * When a file is opened or edited didOpen and didChange notifications are sent
	 * to the server respectfully. These are different from the other endpoints,
	 * such as the completion endpoint used in the second exercise, as they do not
	 * send back a response, instead the notification is used to update the
	 * responses that the other endpoints will send.
	 */
	@Override
	public void didOpen(DidOpenTextDocumentParams params) {
		/**
		 * Consume the Document
		 * 
		 * In the didOpen function, We will be using the text from the text document
		 * given to us to create a model. We create a model instead of just saving the
		 * text for simplicity and convenience when accessing the data in other
		 * functions. We then save the model in a map of all the server's documents with
		 * the document URI as the key.
		 * 
		 * One Language Server is used for multiple documents so it is important to be
		 * able to access the right document model when requested:
		 */
		DocumentModel model = new DocumentModel(params.getTextDocument().getText());
		this.docs.put(params.getTextDocument().getUri(), model);

		/**
		 * Send the Notification
		 * 
		 * In both didOpen and didChange functions, we will take the model generated and
		 * send it to the validate function. This will return any necessary Diagnostics
		 * as a List which will be used to create a new PublishDiagnosticsParams which
		 * is then published to the client:
		 * 
		 * We are beginning this sequence of calls when the server receives a didChange
		 * or didOpen notification, but using this technique, you can send the client
		 * notifications at any point.
		 */
		CompletableFuture.runAsync(() -> languageServer.getClient()
				.publishDiagnostics(new PublishDiagnosticsParams(params.getTextDocument().getUri(), validate(model))));
	}

	@Override
	public void didChange(DidChangeTextDocumentParams params) {
		/**
		 * Similarly, In the didChange function, create a new model from the document
		 * text and update the document map with the new model:
		 */
		DocumentModel model = new DocumentModel(params.getContentChanges().get(0).getText());
		this.docs.put(params.getTextDocument().getUri(), model);
		// send notification
		CompletableFuture.runAsync(() -> languageServer.getClient()
				.publishDiagnostics(new PublishDiagnosticsParams(params.getTextDocument().getUri(), validate(model))));
	}

	@Override
	public void didClose(DidCloseTextDocumentParams params) {
		this.docs.remove(params.getTextDocument().getUri());
	}

	@Override
	public void didSave(DidSaveTextDocumentParams params) {
	}

	/**
	 * To show to yourself that this works, within the getHoverContent function,
	 * return text whose color reflects their difficulty:
	 * 
	 * <pre>
	 * if ("Beginner".equals(difficulty)) {
	 * 	return Either.forLeft("<font color='green'>Beginner</font>");
	 * } else if ("Intermediate".equals(difficulty)) {
	 * 	return Either.forLeft("<font color='blue'>Intermediate</font>");
	 * } else if ("Advanced".equals(difficulty)) {
	 * 	return Either.forLeft("<font color='red'>Advanced</font>");
	 * }
	 * return Either.forLeft(difficulty);
	 * </pre>
	 * 
	 * @param type
	 * @return
	 */
	private Either<String, MarkedString> getHoverContent(String type) {
		return Either.forLeft(type);
	}

	private List<Diagnostic> validate(DocumentModel model) {
		List<Diagnostic> res = new ArrayList<>();
		Route previousRoute = null;
		for (Route route : model.getResolvedRoutes()) {
			if (!ConnectionMap.INSTANCE.all.contains(route.name)) {
				Diagnostic diagnostic = new Diagnostic();
				diagnostic.setSeverity(DiagnosticSeverity.Error);
				diagnostic.setMessage("This is not a Session");
				diagnostic.setRange(new Range(new Position(route.line, route.charOffset),
						new Position(route.line, route.charOffset + route.text.length())));
				res.add(diagnostic);
			} else if (previousRoute != null && !ConnectionMap.INSTANCE.startsFrom(route.name, previousRoute.name)) {
				Diagnostic diagnostic = new Diagnostic();
				diagnostic.setSeverity(DiagnosticSeverity.Warning);
				diagnostic.setMessage("'" + route.name + "' does not follow '" + previousRoute.name + "'");
				diagnostic.setRange(new Range(new Position(route.line, route.charOffset),
						new Position(route.line, route.charOffset + route.text.length())));
				res.add(diagnostic);
			}
			previousRoute = route;
		}
		return res;
	}

}

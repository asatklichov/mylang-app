package net.sahet.cobol.parser.apps;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import net.sahet.cobol.parser.ExprLexer;
import net.sahet.cobol.parser.ExprParser;

public class ExpApp {
	public static void main(String[] args) throws Exception {
//		ANTLRStringStream in = new ANTLRStringStream("12*(5-6)");
//		ExpLexer lexer = new ExpLexer(in);
//		CommonTokenStream tokens = new CommonTokenStream(lexer);
//		ExpParser parser = new ExpParser(tokens);
//		parser.eval();

		System.out.println("*** Expression Eval Listener ***");
		InputStreamReader r = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(r);
		System.out.println("Enter your expression");
		String expr = br.readLine() + "\n";
		// get lexer
		ExprLexer lexer = new ExprLexer(new ANTLRInputStream(expr)); 
		// get list of matched tokens
		CommonTokenStream tokens = new CommonTokenStream(lexer); 
		// parse token
		ExprParser parser = new ExprParser(tokens);
		// walk parse tree and attach our listener
		ParseTreeWalker pw = new ParseTreeWalker();
		EvalListener evalListener = new EvalListener();
		pw.walk(evalListener, parser.prog());

	}
}

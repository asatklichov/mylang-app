package net.sahet.cobol.parser.apps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import net.sahet.cobol.parser.SalamLexer;
import net.sahet.cobol.parser.SalamParser;

public class Salam2 {

	public static void main(String[] args) throws IOException {
		InputStreamReader r = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(r);
		System.out.println("Enter your expression");
		String txt = br.readLine() + "\n";
		
		ANTLRInputStream input = new ANTLRInputStream(txt);

		SalamLexer lexer = new SalamLexer(input);

		CommonTokenStream tokens = new CommonTokenStream(lexer);

		SalamParser parser = new SalamParser(tokens);

		ParseTree tree = parser.rec(); // begin parsing at rule 'r'
		System.out.println(tree.toStringTree(parser)); // print LISP-style tree

		System.out.println();
	}

}

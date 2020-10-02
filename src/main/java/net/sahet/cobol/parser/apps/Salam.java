
package net.sahet.cobol.parser.apps;

import java.io.IOException;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import net.sahet.cobol.parser.SalamBaseListener;
import net.sahet.cobol.parser.SalamLexer;
import net.sahet.cobol.parser.SalamParser;

public class Salam {

	public static void main(String[] args) throws IOException {
		ANTLRFileStream antlrFileStream = new ANTLRFileStream(
				"C:\\workspace-eclipse\\mylang-app\\src\\main\\resources\\mytext.txt");
		SalamLexer lexer = new SalamLexer(antlrFileStream);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		SalamParser parser = new SalamParser(tokens);
		ParseTree tree = parser.rec();
		ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk(new SalamBaseListener(), tree);

		System.out.println();
	}
}

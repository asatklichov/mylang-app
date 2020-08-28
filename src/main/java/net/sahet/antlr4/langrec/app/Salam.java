package net.sahet.antlr4.langrec.app;


import java.io.IOException;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import net.sahet.antlr4.langrec.listener.SalamListener;
import net.sahet.langrec.SalamLexer;
import net.sahet.langrec.SalamParser;

public class Salam {

    public static void main(String[] args) throws IOException {

        ANTLRFileStream antlrFileStream = new ANTLRFileStream("C:\\workspace-eclipse\\mylang-app\\src\\main\\resources\\mytext.txt");//args[0]
        SalamLexer lexer = new SalamLexer(antlrFileStream);
        CommonTokenStream  tokens = new CommonTokenStream(lexer);
        SalamParser parser = new SalamParser(tokens);
        ParseTree tree = parser.rec();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(new SalamListener(), tree);

        System.out.println();
    }
}

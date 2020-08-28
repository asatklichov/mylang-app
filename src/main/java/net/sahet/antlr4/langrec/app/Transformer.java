package net.sahet.antlr4.langrec.app;

import net.sahet.langrec.Cobol85Lexer;
import net.sahet.langrec.Cobol85Parser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.TokenStream;

import java.io.IOException;
import java.io.InputStream;


public class Transformer {
    public static void main(String[] args) {

        //https://github.com/teverett/antlr4example


        System.out.println("Antlr4 Example");
        try {
            /*
             * get the input file as an InputStream
             */
            InputStream inputStream = Transformer.class.getResourceAsStream("/prog.txt");
            /*
             * make Lexer
             */
            Lexer lexer = new Cobol85Lexer(CharStreams.fromStream(inputStream));
            /*
             * get a TokenStream on the Lexer
             */
            TokenStream tokenStream = new CommonTokenStream(lexer);
            /*
             * make a Parser on the token stream
             */
            Cobol85Parser parser = new Cobol85Parser(tokenStream);
            /*
             * get the top node of the AST. This corresponds to the topmost rule of equation.q4, "equation"
             */
            @SuppressWarnings("unused")
            Cobol85Parser.StartRuleContext startRuleContext = parser.startRule();
            System.out.println(parser.fileName());
            System.out.println("-- successfull --");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

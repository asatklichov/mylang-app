package net.sahet.antlr4.langrec.listener;


import net.sahet.langrec.*;

public class SalamListener extends SalamBaseListener {
    @Override
    public void enterRec(SalamParser.RecContext ctx) {
        System.out.println("Entering rec: "+ ctx.ID().getText());
    }

    @Override
    public void exitRec(SalamParser.RecContext ctx) {
        System.out.println("Exiting rec");
    }
}

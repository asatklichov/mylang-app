package net.sahet.cobol.parser.apps;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.antlr.v4.runtime.tree.TerminalNode;

import net.sahet.cobol.parser.ExprBaseListener;
import net.sahet.cobol.parser.ExprParser;

public class EvalListener extends ExprBaseListener {

	// vars for assignment
	Map<String, Integer> vars = new HashMap<String, Integer>();

	// stack for expr tree evaluation
	Stack<Integer> evalstack = new Stack<Integer>();

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation does nothing.
	 * </p>
	 */
	@Override
	public void exitProg(ExprParser.ProgContext ctx) {

		System.out.println("Exitig prog, ..." + ctx.getText());
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation does nothing.
	 * </p>
	 */
	@Override
	public void exitExpr(ExprParser.ExprContext ctx) {
		System.out.println("Exitig expr, ..." + ctx.getText());
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation does nothing.
	 * </p>
	 */
	@Override
	public void visitTerminal(TerminalNode node) {
		System.out.println("Terminal: " + node.getText());
		// Integer i = new Integer(node.getText());
		// evalstack.push(i);
	}

}

package prorunvis.trace;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.ModifierVisitor;

/**
 * This Visitor extends the standard ModifierVisitor and overwrites the visit methods for each codetype being traced in order to instrument them with the needed trace call.
 */
public class TraceVisitor extends ModifierVisitor<Void> {

    /**
     * Add a trace call to every do loop body. Trace call is added as the first line of the loop body.
     * @param stmt the statement to be instrumented
     * @param arg as this visitor is stateless, this argument is not used
     * @return the modified statement
     */
    @Override
    public DoStmt visit(final DoStmt stmt, final Void arg) {
    NodeList<Statement> statements = stmt.getBody().asBlockStmt().getStatements();
        statements.addFirst(traceEntryCreator(stmt));
        BlockStmt block = new BlockStmt().setStatements(statements);
        stmt.setBody(block);
        super.visit(stmt, arg);
        return stmt;
    }

    /**
     * Add a trace call to every for loop body. Trace call is added as the first line of the loop body.
     * @param stmt the statement to be instrumented
     * @param arg as this visitor is stateless, this argument is not used
     * @return the modified statement
     */
    @Override
    public ForStmt visit(final ForStmt stmt, final Void arg) {
        NodeList<Statement> statements = stmt.getBody().asBlockStmt().getStatements();
        statements.addFirst(traceEntryCreator(stmt));
        BlockStmt block = new BlockStmt().setStatements(statements);
        stmt.setBody(block);
        super.visit(stmt, arg);
        return stmt;
    }

    /**
     * Add a trace call to every then and else block of an if-statement. Trace call is added as the first line of the if body and else body if present.
     * @param stmt the statement to be instrumented
     * @param arg as this visitor is stateless, this argument is not used
     * @return the modified statement
     */
    @Override
    public IfStmt visit(final IfStmt stmt, final Void arg) {
        //add a methodCall to proRunVisTrace to the then-block of stmt
        NodeList<Statement> statements = stmt.getThenStmt().asBlockStmt().getStatements();
        Statement trace = traceEntryCreator(stmt);
        statements.addFirst(trace);
        BlockStmt block = new BlockStmt().setStatements(statements);
        stmt.setThenStmt(block);

        //check if stmt has an else-block
        if (stmt.getElseStmt().isPresent()) {

            Statement elseStmt = stmt.getElseStmt().get();
            //check if is a block statement, not an else-if statement
            if (elseStmt.isBlockStmt()) {
                //add a methodCall to proRunVisTrace to the else-block of stmt
                statements = stmt.getElseStmt().get().asBlockStmt().getStatements();
                statements.addFirst(trace);
                block = new BlockStmt().setStatements(statements);
                stmt.setElseStmt(block);
            }
        }

        super.visit(stmt, arg);
        return stmt;
    }

    /**
     * Add a trace call to every method declaration body (if not a forward declaration). Trace call is added as the first line of the method body.
     * @param decl the declaration to be instrumented
     * @param arg as this visitor is stateless, this argument is not used
     * @return the modified method
     */
    @Override
    public MethodDeclaration visit(final MethodDeclaration decl, final Void arg) {
        NodeList<Statement> statements;
        if (decl.getBody().isPresent()) {
            statements = decl.getBody().get().getStatements();
            statements.addFirst(traceEntryCreator(decl));
            BlockStmt block = new BlockStmt().setStatements(statements);
            decl.setBody(block);
        }
        super.visit(decl, arg);
        return decl;
    }

    /**
     * Add a trace call to every return statement. Trace call is added as a line right above the return statement.
     * @param stmt the statement to be instrumented
     * @param arg as this visitor is stateless, this argument is not used
     * @return the modified statement
     */
    @Override
    public ReturnStmt visit(final ReturnStmt stmt, final Void arg) {

        //check if the Optional parentNode is present
        if (stmt.getParentNode().isPresent()) {

            Statement trace = traceEntryCreator(stmt);

            if (stmt.getParentNode().get() instanceof SwitchEntry switchEntry) {
                switchEntry.addStatement(switchEntry.getStatements().indexOf(stmt), trace);
            } else {
                Statement parent = (Statement) stmt.getParentNode().get();

                //get all statements within parent and add the trace to the current index of stmt
                parent.asBlockStmt().getStatements().add(parent.asBlockStmt().getStatements().indexOf(stmt), trace);
            }
        }
        //increase size of arg for the next trace-call

        super.visit(stmt, arg);
        return stmt;
    }

    /**
     * Add a trace call to every case in a switch statement. Trace call is added as the first line of the body of every case in the switch statement.
     * @param stmt the statement to be instrumented
     * @param arg as this visitor is stateless, this argument is not used
     * @return the modified statement
     */
    @Override
    public SwitchStmt visit(final SwitchStmt stmt, final Void arg) {

        for (SwitchEntry entry : stmt.getEntries()) {
            entry.addStatement(0, traceEntryCreator(entry));
        }

        super.visit(stmt, arg);
        return stmt;
    }

    /**
     * Add a trace call to every while loop body. Trace call is added as the first line of the loop body.
     * @param stmt the statement to be instrumented
     * @param arg as this visitor is stateless, this argument is not used
     * @return the modified statement
     */
    @Override
    public WhileStmt visit(final WhileStmt stmt, final Void arg) {

        NodeList<Statement> statements = stmt.getBody().asBlockStmt().getStatements();
        statements.addFirst(traceEntryCreator(stmt));
        BlockStmt block = new BlockStmt().setStatements(statements);
        stmt.setBody(block);
        super.visit(stmt, arg);
        return stmt;
    }

    /**
     * A private method which collects the characteristics of the given statement into a new statement containing the trace call, which can then be added to the original code.
     * @param node
     * @return a statement containing the call to the trace methode with the characteristics of the given statement
     */
    private Statement traceEntryCreator(final Node node) {
        return StaticJavaParser.parseStatement("proRunVisTrace(\""
                + node.findCompilationUnit().get().getStorage().get().getFileName() + ", "
                + node.getClass().getSimpleName() + ", "
                + node.getRange().get().begin.line + ", "
                + node.getRange().get().end.line + "\");");
    }
}

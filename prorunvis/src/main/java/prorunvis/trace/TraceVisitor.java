package prorunvis.trace;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import java.util.Map;


/**
 * This Visitor extends the standard ModifierVisitor and overwrites the visit methods for each codetype being traced in order to instrument them with the needed trace call.
 */
public class TraceVisitor extends ModifierVisitor<Map<Integer, Node>> {

    /**
     * Add a trace call to every try statement. Trace call is added as the first line of the try statement body and to the first line of the body of every corresponding catch statement.
     * @param stmt the statement to be instrumented
     * @param map maps the current ID to this node
     * @return the modified statement
     */
    public TryStmt visit(final TryStmt stmt, final Map<Integer, Node> map) {

        int id = map.size();
        stmt.getTryBlock().addStatement(0, traceEntryCreator(id));
        map.put(id, stmt.clone());

        for (CatchClause clause : stmt.getCatchClauses()) {
            id = map.size();
            clause.getBody().addStatement(0, traceEntryCreator(id));
            map.put(id, clause.clone());
        }

        super.visit(stmt, map);
        return stmt;
    }

    /**
     * Add a trace call to every do loop body. Trace call is added as the first line of the loop body.
     * @param stmt the statement to be instrumented
     * @param map maps the current ID to this node
     * @return the modified statement
     */
    @Override
    public DoStmt visit(final DoStmt stmt, final Map<Integer, Node> map) {

        int id = map.size();
        map.put(id, stmt.clone());
        stmt.getBody().asBlockStmt().addStatement(0, traceEntryCreator(id));
        super.visit(stmt, map);
        return stmt;
    }

    /**
     * Add a trace call to every for loop body. Trace call is added as the first line of the loop body.
     * @param stmt the statement to be instrumented
     * @param map maps the current ID to this node
     * @return the modified statement
     */
    @Override
    public ForStmt visit(final ForStmt stmt, final Map<Integer, Node> map) {

        int id = map.size();
        map.put(id, stmt.clone());
        stmt.getBody().asBlockStmt().addStatement(0, traceEntryCreator(id));
        super.visit(stmt, map);
        return stmt;
    }

    /**
     * Add a trace call to every then and else block of an if-statement. Trace call is added as the first line of the if body and else body if present.
     * @param stmt the statement to be instrumented
     * @param map maps the current ID to this node
     * @return the modified statement
     */
    @Override
    public IfStmt visit(final IfStmt stmt, final Map<Integer, Node> map) {
        //add a methodCall to proRunVisTrace to the then-block of stmt

        int id = map.size();
        map.put(id, stmt.getThenStmt().clone());
        stmt.getThenStmt().asBlockStmt().addStatement(0, traceEntryCreator(id));

        //check if stmt has an else-block
        if (stmt.getElseStmt().isPresent()) {
            id = map.size();
            map.put(id, stmt.getElseStmt().get().clone());
            stmt.getElseStmt().get().asBlockStmt().addStatement(0, traceEntryCreator(id));
        }

        super.visit(stmt, map);
        return stmt;
    }

    /**
     * Add a trace call to every method declaration body (if not a forward declaration). Trace call is added as the first line of the method body.
     * @param decl the declaration to be instrumented
     * @param map maps the current ID to this node
     * @return the modified method
     */
    @Override
    public MethodDeclaration visit(final MethodDeclaration decl, final Map<Integer, Node> map) {

        if (decl.getBody().isPresent()) {
            int id = map.size();
            map.put(id, decl.clone());
            decl.getBody().get().addStatement(0, traceEntryCreator(id));
        }
        super.visit(decl, map);
        return decl;
    }

    /**
     * Add a trace call to every case in a switch statement. Trace call is added as the first line of the body of every case in the switch statement.
     * @param stmt the statement to be instrumented
     * @param map maps the current ID to this node
     * @return the modified statement
     */
    @Override
    public SwitchStmt visit(final SwitchStmt stmt, final Map<Integer, Node> map) {

        for (SwitchEntry entry : stmt.getEntries()) {
            int id = map.size();
            map.put(id, entry.clone());
            entry.addStatement(0, traceEntryCreator(id));
        }

        super.visit(stmt, map);
        return stmt;
    }

    /**
     * Add a trace call to every while loop body. Trace call is added as the first line of the loop body.
     * @param stmt the statement to be instrumented
     * @param map maps the current ID to this node
     * @return the modified statement
     */
    @Override
    public WhileStmt visit(final WhileStmt stmt, final Map<Integer, Node> map) {

        int id = map.size();
        map.put(id, stmt.clone());
        stmt.getBody().asBlockStmt().addStatement(0, traceEntryCreator(id));

        super.visit(stmt, map);
        return stmt;
    }

    /**
     * A private method which collects the characteristics of the given statement into a new statement containing the trace call, which can then be added to the original code.
     * @param id the current id to be printed
     * @return a statement containing the call to the trace methode with the characteristics of the given statement
     */
    private Statement traceEntryCreator(final int id) {
        return StaticJavaParser.parseStatement("proRunVisTrace(\"" + id + "\");");
    }

    /**
     * takes in a node and attempts to write a trace call before it. If that is not possible (because the parent node is not a statement type to which you can add another statement),
     * the same is attempted with its parent, until trace is successfully written.
     * @param map maps the current ID to this node
     * @param node the node to be instrumented
     */
    private void backTrack(final Map<Integer, Node> map, final Node node) {

        Node parent = node.getParentNode().get();
        Node current = node;
        int id = map.size();

        while (true) {
            if (parent instanceof BlockStmt block) {
                block.addStatement(block.getStatements().indexOf(current), traceEntryCreator(id));
                map.put(id, node.clone());
                break;
            } else if (parent instanceof SwitchEntry entry) {
                entry.addStatement(entry.getStatements().indexOf(current), traceEntryCreator(id));
                map.put(id, node.clone());
                break;
            } else {
                current = parent;
                parent = parent.getParentNode().get();
            }
        }
    }
}

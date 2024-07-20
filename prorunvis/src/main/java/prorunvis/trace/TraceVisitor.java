package prorunvis.trace;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.ModifierVisitor;

import java.util.Map;


/**
 * This Visitor extends the standard ModifierVisitor and overwrites the visit methods for each codetype being traced
 * in order to instrument them with the needed trace call.
 */
public class TraceVisitor extends ModifierVisitor<Map<Integer, Node>> {

    /**
     * Add a trace call to every try statement. Trace call is added as the first line of the try statement body and to
     * the first line of the body of every corresponding catch statement.
     *
     * @param stmt the statement to be instrumented
     * @param map  maps the current ID to this node
     * @return the modified statement
     */
    public TryStmt visit(final TryStmt stmt, final Map<Integer, Node> map) {

        int id = map.size();
        createMapEntry(id, map, stmt);
        stmt.getTryBlock().addStatement(0, traceEntryCreator(id));

        for (CatchClause clause : stmt.getCatchClauses()) {
            id = map.size();
            createMapEntry(id, map, clause);
            clause.getBody().addStatement(0, traceEntryCreator(id));
        }

        if (stmt.getFinallyBlock().isPresent()) {
            id = map.size();
            createMapEntry(id, map, stmt.getFinallyBlock().get());
            stmt.getFinallyBlock().get().addStatement(0, traceEntryCreator(id));
        }

        super.visit(stmt, map);
        return stmt;
    }

    /**
     * Add a trace call to every do loop body. Trace call is added as the first line of the loop body.
     *
     * @param stmt the statement to be instrumented
     * @param map  maps the current ID to this node
     * @return the modified statement
     */
    @Override
    public DoStmt visit(final DoStmt stmt, final Map<Integer, Node> map) {

        int id = map.size();
        createMapEntry(id, map, stmt);
        stmt.getBody().asBlockStmt().addStatement(0, traceEntryCreator(id));
        super.visit(stmt, map);
        return stmt;
    }

    /**
     * Add a trace call to every for loop body. Trace call is added as the first line of the loop body.
     *
     * @param stmt the statement to be instrumented
     * @param map  maps the current ID to this node
     * @return the modified statement
     */
    @Override
    public ForStmt visit(final ForStmt stmt, final Map<Integer, Node> map) {

        int id = map.size();
        createMapEntry(id, map, stmt);
        stmt.getBody().asBlockStmt().addStatement(0, traceEntryCreator(id));
        super.visit(stmt, map);
        return stmt;
    }

    /**
     * Add a trace call to every then and else block of an if-statement. Trace call is added as the first line of
     * the if-body and else-body if present.
     *
     * @param stmt the statement to be instrumented
     * @param map  maps the current ID to this node
     * @return the modified statement
     */
    @Override
    public IfStmt visit(final IfStmt stmt, final Map<Integer, Node> map) {
        //add a methodCall to proRunVisTrace to the then-block of stmt

        int id = map.size();
        createMapEntry(id, map, stmt.getThenStmt());
        stmt.getThenStmt().asBlockStmt().addStatement(0, traceEntryCreator(id));

        //check if stmt has an else-block
        if (stmt.getElseStmt().isPresent()) {
            id = map.size();
            //exclude cases with else-if statements since these will be visited by the
            //if-stmt visitor separately
            if (!stmt.getElseStmt().get().isIfStmt()) {
                createMapEntry(id, map, stmt.getElseStmt().get());
                stmt.getElseStmt().get().asBlockStmt().addStatement(0, traceEntryCreator(id));
            }
        }

        super.visit(stmt, map);
        return stmt;
    }

    /**
     * Add a trace call to every method declaration body (if not a forward declaration).
     * Trace call is added as the first line of the method body.
     *
     * @param decl the declaration to be instrumented
     * @param map  maps the current ID to this node
     * @return the modified method
     */
    @Override
    public MethodDeclaration visit(final MethodDeclaration decl, final Map<Integer, Node> map) {

        if (decl.getBody().isPresent()) {
            int id = map.size();
            createMapEntry(id, map, decl);
            decl.getBody().get().addStatement(0, traceEntryCreator(id));
        }

        super.visit(decl, map);
        return decl;
    }

    /**
     * Add a trace call to every case in a switch statement.
     * Trace call is added as the first line of the body of every case in the switch statement.
     *
     * @param stmt the statement to be instrumented
     * @param map  maps the current ID to this node
     * @return the modified statement
     */
    @Override
    public SwitchStmt visit(final SwitchStmt stmt, final Map<Integer, Node> map) {

        for (SwitchEntry entry : stmt.getEntries()) {
            int id = map.size();
            createMapEntry(id, map, entry);
            entry.addStatement(0, traceEntryCreator(id));
        }

        super.visit(stmt, map);
        return stmt;
    }

    /**
     * Add a trace call to every while loop body. Trace call is added as the first line of the loop body.
     *
     * @param stmt the statement to be instrumented
     * @param map  maps the current ID to this node
     * @return the modified statement
     */
    @Override
    public WhileStmt visit(final WhileStmt stmt, final Map<Integer, Node> map) {

        int id = map.size();
        createMapEntry(id, map, stmt);
        stmt.getBody().asBlockStmt().addStatement(0, traceEntryCreator(id));

        super.visit(stmt, map);
        return stmt;
    }

    /**
     * A private method which collects the characteristics of the given statement into a new statement
     * containing the trace call, which can then be added to the original code.
     *
     * @param id the current id to be printed
     * @return a statement containing the call to the trace methode with the characteristics of the given statement
     */
    private Statement traceEntryCreator(final int id) {
        return StaticJavaParser.parseStatement("prorunvis.Trace.next_elem(" + id + ");");
    }

    /**
     * Creates a map entry with a clone of the given node while preserving its range.
     *
     * @param id   the key to map
     * @param map  the map in which to put the entry
     * @param node the value to map
     */
    private void createMapEntry(final int id, final Map<Integer, Node> map, final Node node) {
        Node entry = node.clone().setRange(node.getRange().get());
        entry.setParentNode(node.findCompilationUnit().get().clone());
        map.put(id, entry);
    }
}

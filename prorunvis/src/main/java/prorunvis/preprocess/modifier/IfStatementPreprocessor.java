package prorunvis.preprocess.modifier;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.visitor.ModifierVisitor;


/**
 * A Visitor used by {@link prorunvis.preprocess.Preprocessor}. It can be used to
 * modify if-statements to follow common java conventions using curly braces.
 */
public class IfStatementPreprocessor extends ModifierVisitor<Void> {

    /**
     * Modify if-statements, if the then and/or else statement is a single statement
     * by replacing it with an equivalent block statement.
     */
    @Override
    public IfStmt visit(final IfStmt stmt, final Void arg) {
        super.visit(stmt, arg);

        //check and process then statement
        if (!stmt.getThenStmt().isBlockStmt()) {
            BlockStmt block = new BlockStmt(new NodeList<>(stmt.getThenStmt()));
            block.setRange(stmt.getThenStmt().getRange().get());
            stmt.setThenStmt(block);
            block.getStatement(0).setParentNode(block);
        }

        //check and process else statement if one is present
        if (stmt.getElseStmt().isPresent()) {
            if (!stmt.getElseStmt().get().isBlockStmt()
                && !stmt.getElseStmt().get().isIfStmt()) {
                BlockStmt block = new BlockStmt(new NodeList<>(stmt.getElseStmt().get()));
                block.setRange(stmt.getElseStmt().get().getRange().get());
                stmt.setElseStmt(block);
                block.getStatement(0).setParentNode(block);
            }
        }
        return stmt;
    }
}

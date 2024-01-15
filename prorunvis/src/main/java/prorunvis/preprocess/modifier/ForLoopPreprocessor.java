package prorunvis.preprocess.modifier;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.visitor.ModifierVisitor;

/**
 * A Visitor used by {@link prorunvis.preprocess.Preprocessor}. It can be used to
 * modify for loops to follow common java conventions using curly braces.
 */
public class ForLoopPreprocessor extends ModifierVisitor<Void> {

    /**
     * Modify for loop-statements, if the body statement is a single statement
     * by replacing it with an equivalent block statement.
     */
    @Override
    public ForStmt visit(final ForStmt stmt, final Void arg) {
        super.visit(stmt, arg);

        if (!stmt.getBody().isBlockStmt()) {
            BlockStmt block = new BlockStmt(new NodeList<>(stmt.getBody()));
            block.setRange(stmt.getBody().getRange().get());
            stmt.setBody(block);
            block.getStatement(0).setParentNode(block);
        }
        return stmt;
    }
}

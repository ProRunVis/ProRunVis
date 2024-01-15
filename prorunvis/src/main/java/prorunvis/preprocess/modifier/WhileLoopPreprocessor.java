package prorunvis.preprocess.modifier;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.ModifierVisitor;

/**
 * A Visitor used by {@link prorunvis.preprocess.Preprocessor}. It can be used to
 * modify while loops to follow common java conventions using curly braces.
 */
public class WhileLoopPreprocessor extends ModifierVisitor<Void> {

    /**
     * Modify while-statements, if the body statement is a single statement
     * by replacing it with an equivalent block statement.
     */
    @Override
    public WhileStmt visit(final WhileStmt stmt, final Void arg) {
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

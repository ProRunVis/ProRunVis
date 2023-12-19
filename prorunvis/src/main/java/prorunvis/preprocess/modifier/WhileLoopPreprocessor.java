package prorunvis.preprocess.modifier;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.ModifierVisitor;

public class WhileLoopPreprocessor extends ModifierVisitor<Void> {

    /**
     * Modify while-statements, if the body statement is a single statement
     * by replacing it with an equivalent block statement
     */
    @Override
    public WhileStmt visit(WhileStmt stmt, Void arg){
        super.visit(stmt, arg);

        NodeList<Statement> statements;
        if(!stmt.getBody().isBlockStmt()) {
            statements = new NodeList<Statement>(stmt.getBody());
            BlockStmt block = new BlockStmt().setStatements(statements);
            stmt.setBody(block);

        }
        return stmt;
    }
}

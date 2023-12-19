package prorunvis.preprocess.modifier;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.ModifierVisitor;

public class DoLoopPreprocessor extends ModifierVisitor<Void> {

    /**
     * Modify dowhile-statements, if the body statement is a single statement
     * by replacing it with an equivalent block statement
     */
    @Override
    public DoStmt visit(DoStmt stmt, Void arg){
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

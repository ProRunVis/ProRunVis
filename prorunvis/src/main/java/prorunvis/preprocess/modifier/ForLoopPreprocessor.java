package prorunvis.preprocess.modifier;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.ModifierVisitor;

public class ForLoopPreprocessor extends ModifierVisitor<Void> {

    /**
     * Modify forloop-statements, if the body statement is a single statement
     * by replacing it with an equivalent block statement
     */
    @Override
    public ForStmt visit(ForStmt stmt, Void arg){
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

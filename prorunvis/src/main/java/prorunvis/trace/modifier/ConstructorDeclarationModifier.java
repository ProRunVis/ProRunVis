package prorunvis.trace.modifier;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.ModifierVisitor;

import java.util.List;

public class ConstructorDeclarationModifier extends ModifierVisitor<List<Integer>> {

    /**
     * Add a trace call to every constructor declaration body
     */
    @Override
    public ConstructorDeclaration visit(ConstructorDeclaration stmt, List<Integer> arg){
        String methodCall = "proRunVisTrace(\"$ID\");";
        NodeList<Statement> statements;
        statements = stmt.getBody().getStatements();
        statements.addFirst(StaticJavaParser.parseStatement(methodCall.replace("$ID", String.valueOf(arg.size()))));
        arg.add(arg.size());
        BlockStmt block = new BlockStmt().setStatements(statements);
        stmt.setBody(block);
        super.visit(stmt, arg);
        return stmt;
    }
}

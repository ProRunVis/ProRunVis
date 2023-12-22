package prorunvis.trace.modifier;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.ModifierVisitor;

import java.util.List;

public class ReturnStatementModifier extends ModifierVisitor<List<Integer>> {

    /**
     * Add a trace call to every return statement
     */
    @Override
    public BlockStmt visit(ReturnStmt stmt, List<Integer> arg){
        String methodCall = "proRunVisTrace(\"$ID\");";
        NodeList<Statement> statements = new NodeList<>();
        statements.addFirst(stmt);
        statements.addFirst(StaticJavaParser.parseStatement(methodCall.replace("$ID", String.valueOf(arg.size()))));
        arg.add(arg.size());
        BlockStmt block = new BlockStmt(statements);
        super.visit(stmt, arg);
        return block;
    }
}

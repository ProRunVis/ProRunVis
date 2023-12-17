package prorunvis.trace.modifier;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.ModifierVisitor;

import java.util.List;

public class DoLoopModifier extends ModifierVisitor<List<Integer>>{
    @Override
    public DoStmt visit(DoStmt stmt, List<Integer> arg){
        String methodCall = "proRunVisTrace(\"$ID\");";
        NodeList<Statement> statements = stmt.getBody().asBlockStmt().getStatements();
        statements.addFirst(StaticJavaParser.parseStatement(methodCall.replace("$ID", String.valueOf(arg.size()))));
        arg.add(arg.size());
        BlockStmt block = new BlockStmt().setStatements(statements);
        stmt.setBody(block);
        super.visit(stmt, arg);
        return stmt;
    }
}

package prorunvis.trace.modifier;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.nodeTypes.NodeWithBlockStmt;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.ModifierVisitor;

import javax.swing.plaf.nimbus.State;
import java.util.List;

public class ForLoopModifier extends ModifierVisitor<List<Integer>> {
    @Override
    public ForStmt visit(ForStmt stmt, List<Integer> arg){
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

package prorunvis.trace.modifier;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.ModifierVisitor;

import java.util.List;

public class SwitchModifier extends ModifierVisitor<List<Integer>>{
    @Override
    public SwitchStmt visit(SwitchStmt stmt, List<Integer> arg) {
        String methodCall = "proRunVisTrace(\"$ID\");";
        NodeList<SwitchEntry> entries = stmt.getEntries();

        for (SwitchEntry entry : entries) {
            NodeList<Statement> statements = entry.getStatements();
            statements.addFirst(StaticJavaParser.parseStatement(methodCall.replace("$ID", String.valueOf(arg.size()))));
            arg.add(arg.size());
            entry.setStatements(statements);
        }
        super.visit(stmt, arg);
        return stmt;
    }
}

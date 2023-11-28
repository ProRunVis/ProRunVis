package prorunvis.trace.modifier;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.ModifierVisitor;

import java.util.List;

public class IfStatementModifier extends ModifierVisitor<List<Integer>> {

    /**
     * Add a trace call to every then and else block of an if-statement
     */
    @Override
    public IfStmt visit(IfStmt stmt, List<Integer> arg){
        String methodCall = "proRunVisTrace(\"$ID\");";

        //add a methodCall to proRunVisTrace to the then-block of stmt
        NodeList<Statement> statements = stmt.getThenStmt().asBlockStmt().getStatements();
        statements.addFirst(StaticJavaParser.parseStatement(methodCall.replace("$ID", String.valueOf(arg.size()))));
        arg.add(arg.size());
        BlockStmt block = new BlockStmt().setStatements(statements);
        stmt.setThenStmt(block);

        //check if stmt has an else-block
        if(stmt.getElseStmt().isPresent()) {

            Statement elseStmt = stmt.getElseStmt().get();
            //check if is a block statement, not an else-if statement
            if(elseStmt.isBlockStmt()) {
                //add a methodCall to proRunVisTrace to the else-block of stmt
                statements = stmt.getElseStmt().get().asBlockStmt().getStatements();
                statements.addFirst(StaticJavaParser.parseStatement(methodCall.replace("$ID", String.valueOf(arg.size()))));
                arg.add(arg.size());
                block = new BlockStmt().setStatements(statements);
                stmt.setElseStmt(block);
            }
        }

        super.visit(stmt, arg);
        return stmt;
    }
}

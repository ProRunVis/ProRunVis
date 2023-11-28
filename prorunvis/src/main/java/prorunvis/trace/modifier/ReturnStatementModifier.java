package prorunvis.trace.modifier;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.ModifierVisitor;

import java.util.List;

public class ReturnStatementModifier extends ModifierVisitor<List<Integer>> {

    /**
     * Add a trace call to every return statement
     */
    @Override
    public ReturnStmt visit(ReturnStmt stmt, List<Integer> arg){
        String methodCall = "proRunVisTrace(\"$ID\");";

        Statement parent;
        //check if the Optional parentNode is present
        if(stmt.getParentNode().isPresent()){
            parent = (Statement) stmt.getParentNode().get();

            //get all statements within parent and add the trace to the current index of stmt
            parent.asBlockStmt().getStatements()
                    .add(parent.asBlockStmt()
                                    .getStatements().indexOf(stmt),
                            StaticJavaParser.parseStatement(methodCall.replace("$ID", String.valueOf(arg.size()))));
        }

        //increase size of arg for the next trace-call
        arg.add(arg.size());

        super.visit(stmt, arg);
        return stmt;
    }
}

package prorunvis.trace.modifier;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.ModifierVisitor;

import java.util.List;

public class MethodDeclarationModifier extends ModifierVisitor<List<Integer>>{

    /**
     * Add a trace call to every method declaration body (if not a forward declaration)
     */
        @Override
        public MethodDeclaration visit(MethodDeclaration stmt, List<Integer> arg){
            String methodCall = "proRunVisTrace(\"$ID\");";
            NodeList<Statement> statements;
            if(stmt.getBody().isPresent()) {
                statements = stmt.getBody().get().getStatements();
                statements.addFirst(StaticJavaParser.parseStatement(methodCall.replace("$ID", String.valueOf(arg.size()))));
                arg.add(arg.size());
                BlockStmt block = new BlockStmt().setStatements(statements);
                stmt.setBody(block);
            }
            super.visit(stmt, arg);
            return stmt;
        }
    }

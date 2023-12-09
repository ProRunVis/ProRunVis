package prorunvis.trace.modifier;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.ModifierVisitor;

import java.util.List;

public class MethodDeclarationModifier extends ModifierVisitor<List<Integer>>{
        @Override
        public MethodDeclaration visit(MethodDeclaration decl, List<Integer> arg){
            if(decl.getNameAsString().equals("proRunVisTrace")) return decl;
            String methodCall = "proRunVisTrace(\"$ID\");";
            NodeList<Statement> statements;
            if(decl.getBody().isPresent()) {
                statements = decl.getBody().get().getStatements();
                statements.addFirst(StaticJavaParser.parseStatement(methodCall.replace("$ID", String.valueOf(arg.size()))));
                arg.add(arg.size());
                BlockStmt block = new BlockStmt().setStatements(statements);
                decl.setBody(block);
            }
            super.visit(decl, arg);
            return decl;
        }
    }

package prorunvis.preprocess.modifier;

import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.visitor.ModifierVisitor;

public class IfStatementPreprocessor extends ModifierVisitor<Void> {

    /**
     * Modify if-statements, if the then and/or else statement is a single statement
     * by replacing it with an equivalent block statement
     */
    @Override
    public IfStmt visit(IfStmt stmt, Void arg){
        super.visit(stmt, arg);

        //check and process then statement
        if(!stmt.getThenStmt().isBlockStmt()){
            BlockStmt block = new BlockStmt();
            block.addStatement(stmt.getThenStmt());
            stmt.setThenStmt(block);
        }

        //check and process else statement if one is present
        if(stmt.getElseStmt().isPresent()){
            if(!stmt.getElseStmt().get().isBlockStmt()){
                BlockStmt block = new BlockStmt();
                block.addStatement(stmt.getElseStmt().get());
                stmt.setElseStmt(block);
            }
        }
        return stmt;
    }
}

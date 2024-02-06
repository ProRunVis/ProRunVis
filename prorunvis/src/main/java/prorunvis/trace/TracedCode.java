package prorunvis.trace;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.*;

public enum TracedCode {

    TRY_STMT(TryStmt.class),
    CATCH_CLAUSE(CatchClause.class),
    FOR_STMT(ForStmt.class),
    WHILE_STMT(WhileStmt.class),
    DO_STMT(DoStmt.class),
    SWITCH_CASE(SwitchStmt.class),
    IF_STMT(IfStmt.class);

    public final Class<? extends Node> type;

    TracedCode(Class<? extends Node> type) {
        this.type = type;
    }
}

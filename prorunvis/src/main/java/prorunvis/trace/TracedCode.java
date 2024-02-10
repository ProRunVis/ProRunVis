package prorunvis.trace;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.*;

/**
 * This enum notates all code types which are currently being traced.
 */
public enum TracedCode {

    /**
     * Entry for try-statements.
     */
    TRY_STMT(TryStmt.class),

    /**
     * Entry for catch-statements.
     */
    CATCH_CLAUSE(CatchClause.class),

    /**
     * Entry for for-statements.
     */
    FOR_STMT(ForStmt.class),

    /**
     * Entry for while-statements.
     */
    WHILE_STMT(WhileStmt.class),

    /**
     * Entry for do-statements.
     */
    DO_STMT(DoStmt.class),

    /**
     * Entry for switch-statements.
     */
    SWITCH_CASE(SwitchStmt.class),

    /**
     * Entry for if-statements.
     */
    IF_STMT(IfStmt.class);

    /**
     * The corresponding AST-Node of this codetype.
     */
    private final Class<? extends Node> type;

    /**
     * Returns the type for this entry.
     * @return the type
     */
    public Class<? extends Node> getType() {
        return type;
    }

    TracedCode(final Class<? extends Node> type) {
        this.type = type;
    }
}

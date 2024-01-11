package prorunvis.preprocess;

import com.github.javaparser.ast.CompilationUnit;
import prorunvis.preprocess.modifier.DoLoopPreprocessor;
import prorunvis.preprocess.modifier.ForLoopPreprocessor;
import prorunvis.preprocess.modifier.IfStatementPreprocessor;
import prorunvis.preprocess.modifier.WhileLoopPreprocessor;

/**
 * A preprocessor preparing a {@link CompilationUnit} for tracing.
 * The compilation unit is modified to follow common java conventions,
 * making it easier for the tracing to
 */
public final class Preprocessor {

    /**
     * This constructor should not be called.
     */
    private Preprocessor() {
        throw new IllegalStateException();
    }

    /**
     * Preprocess a compilation unit to get a more uniform code following
     * common java conventions for the tracing.
     * @param cu The compilation unit to process.
     */
    public static void run(final CompilationUnit cu) {
        new IfStatementPreprocessor().visit(cu, null);
        new ForLoopPreprocessor().visit(cu, null);
        new WhileLoopPreprocessor().visit(cu, null);
        new DoLoopPreprocessor().visit(cu, null);
    }
}

package prorunvis.preprocess;

import com.github.javaparser.ast.CompilationUnit;
import prorunvis.preprocess.modifier.IfStatementPreprocessor;

public class Preprocessor {


    /**
     * Preprocess a compilation unit to get a more uniform code for the tracing
     * @param cu The compilation unit to process
     */
    public static void run(CompilationUnit cu){
        new IfStatementPreprocessor().visit(cu, null);
    }
}

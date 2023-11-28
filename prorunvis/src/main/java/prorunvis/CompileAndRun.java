package prorunvis;

import com.github.javaparser.ast.CompilationUnit;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CompileAndRun {

    static JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

    /**
     * Writes the given {@link CompilationUnit} into a .java file, then compiles and runs that file
     * @param unit - <code>CompilationUnit</code> to be compiled
     * @throws IOException if something goes wrong while the file is created
     * @throws InterruptedException if the execution is interrupted
     */
    public static void compile (CompilationUnit unit) throws IOException, InterruptedException {

        String fileName = unit.getStorage().get().getFileName();

        BufferedWriter writer = new BufferedWriter(new FileWriter("out/" + fileName, false));
        writer.write(unit.toString());
        writer.close();
        compiler.run(null, null, null, "out/" + fileName, "-d", "out");
        Runtime.getRuntime().exec("java -cp out " + fileName.replace(".java", "")).waitFor();
    }
}

package prorunvis.instrument;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.utils.ProjectRoot;
import prorunvis.trace.TraceVisitor;

import java.io.*;
import java.nio.file.Paths;
import java.util.Map;

/**
 * This Instrumenter is used to modify (instrument) code.
 * For that, the code is changed so that is automatically writes a trace
 * while being executed.
 */
public final class Instrumenter {

    /**
     * The trace file to which this Instrumenter writes all tracing information.
     */
    private static File traceFile;

    private Instrumenter() {
        throw new IllegalStateException("Class can not be instantiated");
    }

    /**
     * Sets up the trace file. If the given file does not exist, it and all it's parent
     * directories are created. If the file already exists it is deleted and newly created,
     * to avoid problems at runtime.
     * @param file The file to which the code trace is written.
     */
    public static void setupTrace(final File file) {
        traceFile = file;
        try {
            traceFile.mkdirs();
            if (traceFile.exists()) {
                traceFile.delete();
            }
            traceFile.createNewFile();

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Saves the given project to the given path. And adds a package containing the ProRunVis class used for tracking.
     * @param pr Project root of the project to be saved
     * @param instrumentedOutPath relative path where project is to be saved to
     */
    public static void saveInstrumented(final ProjectRoot pr, final String instrumentedOutPath) {
        File instrumented = new File(instrumentedOutPath);
        instrumented.mkdir();
        pr.getSourceRoots().forEach(sr -> sr.saveAll(Paths.get(instrumentedOutPath)));

        File proRunVisClass = new File(instrumentedOutPath + "/prorunvis/Trace.java");
        proRunVisClass.mkdirs();
        if (proRunVisClass.exists()) {
            proRunVisClass.delete();
        }
        try {
            proRunVisClass.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        BufferedWriter bf;
        try {
            bf = new BufferedWriter(new FileWriter(proRunVisClass, true));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // This replaces \'s in the path with /'s if on windows as the former
        // causes problems with escaping characters in the string on that OS
        String path = (File.separator.equals("\\"))
                ? traceFile.getAbsolutePath().replace("\\", "/")
                : traceFile.getAbsolutePath();

        String proRunVisClassContent = """
                package prorunvis;
                import java.io.BufferedWriter;
                import java.io.FileWriter;
                import java.io.IOException;
                public final class Trace {
                    public static void next_elem(int num) {
                        try {
                            BufferedWriter writer = new BufferedWriter(new FileWriter(
                            """
                            + "\"" + path + "\"" + "\n"
                            + """
                            , true));
                            writer.write("" + num + System.lineSeparator());
                            writer.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e.getMessage());
                        }
                    }
                }
                """;
        try {
            bf.write(proRunVisClassContent);
            bf.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Runs this Instrumenter. The {@link TraceVisitor} iterates over the {@link CompilationUnit}s
     * and modifies them accordingly.
     * @param cu The compilation unit which will be modified by this Instrumenter.
     * @param map the map where the Ids are mapped to the AST-Nodes
     */
    public static void run(final CompilationUnit cu, final Map<Integer, Node> map) {
        new TraceVisitor().visit(cu, map);
    }
}






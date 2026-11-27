package prorunvis.trace.process;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.symbolsolver.utils.SymbolSolverCollectionStrategy;
import com.github.javaparser.utils.ProjectRoot;
import org.junit.jupiter.api.Test;
import prorunvis.Tester;
import prorunvis.instrument.Instrumenter;
import prorunvis.preprocess.Preprocessor;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class AbstractRetracerTest extends Tester {

    private final String testDir = "src/test/testfiles/abstractretracer/";

    @Test
    void demoRetracerOutputOnly() throws IOException {
        Path rootDir = Paths.get(testDir + "demo/resources/in");
        ProjectRoot projectRoot = new SymbolSolverCollectionStrategy()
                .collect(rootDir.toAbsolutePath());
        List<CompilationUnit> cus = createCompilationUnits(projectRoot);

        File traceFile = new File(testDir + "demo/resources/traceFile.tr");
        Instrumenter.setupTrace(traceFile);

        Map<Integer, Node> map = new HashMap<>();
        cus.forEach(cu -> {
            Preprocessor.run(cu);
            Instrumenter.run(cu, map);
        });

        AbstractRetracer retracer = new AbstractRetracer(
                cus, map, testDir + "demo/trace.txt", rootDir);
        Stack<Integer> retracedTokens = retracer.retrace();

        System.out.println("=== AbstractRetracer output (block IDs, top-of-stack first) ===");
        List<Integer> ids = new ArrayList<>(retracedTokens);
        Collections.reverse(ids);
        System.out.println(ids);

        System.out.println("\n=== Block ID → AST node mapping ===");
        for (int id : ids) {
            Node node = map.get(id);
            System.out.println("  Block " + id + " → " + node.getClass().getSimpleName()
                    + " at " + node.getRange().orElse(null));
        }
    }

    @Test
    void demoTest() throws IOException {
        process(testDir + "demo/resources",
                testDir + "demo/trace.txt",
                testDir + "demosolution/expectedTraceNodes.tr");
    }

    @Test
    void demoBinaryTraceProducesSameResult() throws IOException {
        process(testDir + "demo/resources",
                testDir + "demo/trace.trace",
                testDir + "demosolution/expectedTraceNodes.tr");
    }

    @Test
    void ifElseTest() throws IOException {
        process(testDir + "ifelse/resources",
                testDir + "ifelse/trace.txt",
                testDir + "ifelsesolution/expectedTraceNodes.tr");
    }

    /**
     * Runs the AbstractRetracer with a pre-recorded src-tracer trace
     * and compares the resulting TraceNode tree to an expected output.
     */
    private void process(final String resourcePath,
                         final String traceFilePath,
                         final String solutionPath) throws IOException {

        Path rootDir = Paths.get(resourcePath + "/in");
        ProjectRoot projectRoot = new SymbolSolverCollectionStrategy()
                .collect(rootDir.toAbsolutePath());
        List<CompilationUnit> cus = createCompilationUnits(projectRoot);

        File traceFile = new File(resourcePath + "/traceFile.tr");
        Instrumenter.setupTrace(traceFile);

        Map<Integer, Node> map = new HashMap<>();
        cus.forEach(cu -> {
            Preprocessor.run(cu);
            Instrumenter.run(cu, map);
        });

        // Abstract retrace: convert src-tracer trace → block IDs
        AbstractRetracer retracer = new AbstractRetracer(
                cus, map, traceFilePath, rootDir);
        Stack<Integer> retracedTokens = retracer.retrace();

        // Feed into TraceProcessor to build TraceNode tree
        TraceProcessor processor = new TraceProcessor(map, retracedTokens, rootDir);
        processor.start();

        // Compare output to expected
        BufferedReader solutionReader = new BufferedReader(new FileReader(solutionPath));
        List<String> expectedResult = solutionReader.lines().toList();
        solutionReader.close();

        List<String> actualResult = Arrays.asList(processor.toString().split("\n"));

        assertIterableEquals(expectedResult, actualResult);
    }
}

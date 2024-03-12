package prorunvis.trace.process;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.symbolsolver.utils.SymbolSolverCollectionStrategy;
import com.github.javaparser.utils.ProjectRoot;
import org.junit.jupiter.api.Test;
import prorunvis.CompileAndRun;
import prorunvis.Tester;
import prorunvis.instrument.Instrumenter;
import prorunvis.preprocess.Preprocessor;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TraceProcessorTest extends Tester {

    /**
     * Testdirectory for TraceProcessor tests.
     */
    private String testDir = "src/test/testfiles/traceprocessor/";

    /**
     * Tests to test the functionality of traced returns.
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    void returnTest() throws IOException, InterruptedException {
        process(testDir + "returntest/resources",
                testDir + "returntestsolution/expectedTraceNodes.tr");
    }

    /**
     * Tests to test the functionality of trace breaks and continues.
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    void breakContinueTest() throws IOException, InterruptedException {
        process(testDir + "breakcontinuetest/resources",
                testDir + "breakcontinuetestsolution/expectedTracenodes.tr");
    }

    @Test
    void throwTest() throws IOException, InterruptedException {
        process(testDir + "throwtest/resources",
                testDir + "throwtestsolution/expectedTracenodes.tr");
    }

    /**
     * Runs the program normally with the given input and compares the output to a
     * given expected result.
     *
     * @param resourcePath the path in the testdirectory to the test-inputs to use
     * @param solutionPath the path in the testdirectory to the expected solution
     * @throws IOException
     * @throws InterruptedException
     */
    private void process(final String resourcePath, final String solutionPath)
            throws IOException, InterruptedException {

        Path rootDir = Paths.get(resourcePath + "/in");
        ProjectRoot projectRoot = new SymbolSolverCollectionStrategy().
                collect(rootDir.toAbsolutePath());
        List<CompilationUnit> cus = createCompilationUnits(projectRoot);
        File traceFile = new File(resourcePath + "/traceFile.tr");

        Instrumenter.setupTrace(traceFile);

        Map<Integer, Node> map = new HashMap<>();
        cus.forEach(cu -> {
            Preprocessor.run(cu);
            Instrumenter.run(cu, map);
        });

        Instrumenter.saveInstrumented(projectRoot, resourcePath + "/out/instrumented");
        try {
            CompileAndRun.run(cus, resourcePath + "/out/instrumented", resourcePath + "/out/compiled");
        } catch (InterruptedException ignored) {
        }

        TraceProcessor processor = new TraceProcessor(map, traceFile.getPath(), rootDir);
        processor.start();

        BufferedReader solutionReader = new BufferedReader(new FileReader(solutionPath));

        List<String> expectedResult = solutionReader.lines().toList();
        List<String> actualResult = Arrays.asList(processor.toString().split("\n"));

        assertIterableEquals(expectedResult, actualResult);
    }
}

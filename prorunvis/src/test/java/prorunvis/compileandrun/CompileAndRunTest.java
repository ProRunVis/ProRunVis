package prorunvis.compileandrun;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.symbolsolver.utils.SymbolSolverCollectionStrategy;
import com.github.javaparser.utils.ProjectRoot;
import org.junit.jupiter.api.Test;
import prorunvis.CompileAndRun;
import prorunvis.instrument.Instrumenter;

import java.io.*;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import prorunvis.Tester;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

/**
 * This class is for testing the {@link CompileAndRun} class.
 */
class CompileAndRunTest extends Tester {

    /**
     * A String to store the relative path to the directory where all the tests are stored,
     * to use in the test functions.
     */
    private String testDir = "src/test/testfiles/compileandrun/";

    /**
     * Test 1.
     */
    @Test
    void test1() {
        compileAndRun(testDir + "test1",
                testDir + "test1result",
                testDir + "test1solution");
    }

    /**
     * Test 2.
     */
    @Test
    void test2() {
        compileAndRun(testDir + "test2",
                testDir + "test2result",
                testDir + "test2solution");
    }

    /**
     * Take a directory with a preprocessed(by the {@link Preprocessor}) java project.
     * Instrument it with the {@link Instrumenter}.
     * Compile and run it with {@link CompileAndRun} and safe the result of the trace-file.
     * Compare it to a given solution traceFile and assert any differences between the two.
     * @param preprocessedInPath the relative path of the directory where the preprocessed test-project is located.
     * Note: it shouldn't be instrumented yet so the absolute path can be set correctly.
     * @param compiledOutPath the relative path of the directory where the instrumented test-project,
     *                        the compiled test-project and the result trace file will be stored.
     * @param solutionPath the relative path of the directory where the solution trace-file is located.
     */
    void compileAndRun(final String preprocessedInPath, final String compiledOutPath, final String solutionPath) {
        //Setup CompilationUnits
        ProjectRoot testProjectRoot =
                new SymbolSolverCollectionStrategy().collect(Paths.get(preprocessedInPath).toAbsolutePath());
        List<CompilationUnit> cusResult = createCompilationUnits(testProjectRoot);

        //Run Instrumenter
        Map<Integer, Node> map = new HashMap<>();
        File resultTrace = new File(compiledOutPath + "/TraceFile.tr");
        Instrumenter.setupTrace(resultTrace);
        cusResult.forEach(cu -> Instrumenter.run(cu, map));
        Instrumenter.saveInstrumented(testProjectRoot, compiledOutPath);
        File solutionTrace = new File(solutionPath + "/TraceFile.tr");


        if (resultTrace.exists()) {
            resultTrace.delete();
        }
        try {
            resultTrace.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            CompileAndRun.run(cusResult, compiledOutPath, compiledOutPath);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        //Setup readers for trace-files and read
        FileReader rSolution;
        FileReader rResult;
        try {
            rSolution = new FileReader(solutionTrace);
            rResult = new FileReader(resultTrace);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        BufferedReader brSolution = new BufferedReader(rSolution);
        BufferedReader brResult = new BufferedReader(rResult);
        List<String> result = brResult.lines().toList();
        List<String> solution = brSolution.lines().toList();

        //Evaluate result
        assertIterableEquals(solution, result);
        try {
            rSolution.close();
            rResult.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

package prorunvis.instrument;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.symbolsolver.utils.SymbolSolverCollectionStrategy;
import com.github.javaparser.utils.ProjectRoot;
import org.junit.jupiter.api.Test;
import prorunvis.Tester;
import prorunvis.preprocess.Preprocessor;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class is for testing the {@link Instrumenter}.
 */
class InstrumenterTest extends Tester {
    /**
     * A String to store the relative path to the directory where all the tests are stored, to use in the test functions.
     * !!! Can't run tests right now until ProRunVisTrace method in project is outsourced into own file
     */
    String testDir = "src/test/testfiles/instrument/";

    /**
     * Test 1
     */
    @Test
    void test1(){
        //instrument(testDir + "test1", testDir + "test1result", testDir + "test1solution");
    }

    /**
     * Test 2
     */
    @Test
    void test2(){
        //instrument(testDir + "test2", testDir + "test2result", testDir + "test2solution");
    }

    /**
     * Take a directory with a preprocessed(by the {@link Preprocessor}) java project.
     * Instrument it with the {@link Instrumenter} and safe the result.
     * Compare it to a given solution and assert any differences between the two.
     * @param preprocessedInPath the relative path of the directory where the already preprocessed test-project is located.
     * @param instrumentedOutPath the relative path of the directory where the instrumented test-project will be stored.
     * @param solutionPath the relative path of the directory where the solution-project is located.
     */
    private void instrument(String preprocessedInPath, String instrumentedOutPath, String solutionPath){
        //setup CompilationUnits
        ProjectRoot testProjectRoot = new SymbolSolverCollectionStrategy().collect(Paths.get(preprocessedInPath).toAbsolutePath());
        ProjectRoot solutionProjectRoot = new SymbolSolverCollectionStrategy().collect(Paths.get(solutionPath).toAbsolutePath());
        List<CompilationUnit> cusResult = createCompilationUnits(testProjectRoot);
        List<CompilationUnit> cusSolution = createCompilationUnits(solutionProjectRoot);

        //run Instrumenter
        Map<Integer, Node> map = new HashMap<>();
        File traceFile = new File(instrumentedOutPath + "/TraceFile.tr");
        Instrumenter.setupTrace(traceFile);
        cusResult.forEach(cu -> Instrumenter.run(cu, map));

        //safe result
        Instrumenter.safeInstrumented(testProjectRoot, instrumentedOutPath);

        //Evaluate result
        assertIterableEquals(cusSolution, cusResult);
    }
}
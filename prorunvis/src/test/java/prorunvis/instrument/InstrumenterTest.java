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
     * !!! Can't run right now tests until ProRunVisTrace method in project is outsourced into own file
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
     * Instrument it with the {@link Instrumenter}, safe the result, compare it to a given solution and assert any differences between the two.
     * @param preprocessedInPath the relative Path of the directory where the already preprocessed Test-project is located
     * @param instrumentedOutPath the relative Path of the directory where the instrumented Test-project will be stored
     * @param solutionPath the relative Path of the directory where the solution(how the instrumented Test-project should look like) for the Test-project is located
     */
    private void instrument(String preprocessedInPath, String instrumentedOutPath, String solutionPath){
        ProjectRoot testProjectRoot = new SymbolSolverCollectionStrategy().collect(Paths.get(preprocessedInPath).toAbsolutePath());
        ProjectRoot solutionProjectRoot = new SymbolSolverCollectionStrategy().collect(Paths.get(solutionPath).toAbsolutePath());

        List<CompilationUnit> cusResult = createCompilationUnits(testProjectRoot);
        List<CompilationUnit> cusSolution = createCompilationUnits(solutionProjectRoot);

        Map<Integer, Node> map = new HashMap<>();
        File traceFile = new File(instrumentedOutPath + "/TraceFile.tr");
        Instrumenter.setupTrace(traceFile);

        cusResult.forEach(cu -> Instrumenter.run(cu, map));

        Instrumenter.safeInstrumented(testProjectRoot, instrumentedOutPath);

        assertEquals(cusSolution.size(), cusResult.size());

        for (int i = 0; i<cusResult.size(); i++)
            assertEquals(cusSolution.get(i).toString(), cusResult.get(i).toString());

    }

}
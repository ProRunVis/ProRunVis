package prorunvis.preprocess;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.utils.SymbolSolverCollectionStrategy;
import com.github.javaparser.utils.ProjectRoot;
import org.junit.jupiter.api.Test;
import prorunvis.Tester;

import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class is for testing the {@link Preprocessor}.
 */
class PreprocessorTest extends Tester {

    /**
     * A String to store the relative path to the directory where all the tests are stored,
     * to use in the test functions.
     */
    private String testdir = "src/test/testfiles/preprocess/";

    /**
     * Test 1.
     */
    @Test
    void test1() {
        preprocess(testdir + "test1",
                testdir + "test1result",
                testdir + "test1solution");
    }

    /**
     * Test 2.
     */
    @Test
    void test2() {
        preprocess(testdir + "test2",
                testdir + "test2result",
                testdir + "test2solution");
    }

    /**
     * Take a directory with a java project.
     * Preprocess it with the {@link Preprocessor} and safe the result.
     * Compare it to a given solution and assert any differences between the two.
     * @param testInPath the relative path of the directory where the test-project is located.
     * @param preprocessedOutPath the relative path of the directory where the preprocessed test-project will be stored.
     * @param solutionPath the relative path of the directory where the solution-project is located.
     */
    private void preprocess(final String testInPath, final String preprocessedOutPath, final String solutionPath) {
        //Setup CompilationUnits
        ProjectRoot testProjectRoot =
                new SymbolSolverCollectionStrategy().collect(Paths.get(testInPath).toAbsolutePath());
        ProjectRoot solutionProjectRoot =
                new SymbolSolverCollectionStrategy().collect(Paths.get(solutionPath).toAbsolutePath());
        List<CompilationUnit> cusResult = createCompilationUnits(testProjectRoot);
        List<CompilationUnit> cusSolution = createCompilationUnits(solutionProjectRoot);

        //Run Preprocessor
        cusResult.forEach(Preprocessor::run);
        testProjectRoot.getSourceRoots().forEach(sr -> sr.saveAll(Paths.get(preprocessedOutPath)));

        //Evaluate result
        assertIterableEquals(cusSolution, cusResult);
    }
}

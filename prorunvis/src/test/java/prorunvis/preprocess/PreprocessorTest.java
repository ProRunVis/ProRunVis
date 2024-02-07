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
     * A String to store the relative path to the directory where all the tests are stored, to use in the test functions.
     */
    String testdir = "src/test/testfiles/preprocess/";

    /**
     * Test 1
     */
    @Test
    void test1(){
        preprocess(testdir + "test1", testdir + "test1result", testdir + "test1solution");
    }

    /**
     * Test 2
     */
    @Test
    void test2(){
        preprocess(testdir + "test2", testdir + "test2result", testdir + "test2solution");
    }

    /**
     * Take a directory with a java project.
     * Preprocess it with the {@link Preprocessor}, safe the result, compare it to a given solution and assert any differences between the two.
     * @param testInPath the relative Path of the directory where the Test-project is located
     * @param preprocessedOutPath the relative Path of the directory where the preprocessed Test-project will be stored
     * @param solutionPath the relative Path of the directory where the solution(how the preprocessed Test-project should look like) for the Test-project is located
     */
    private void preprocess(String testInPath, String preprocessedOutPath, String solutionPath){
        ProjectRoot testProjectRoot = new SymbolSolverCollectionStrategy().collect(Paths.get(testInPath).toAbsolutePath());
        ProjectRoot solutionProjectRoot = new SymbolSolverCollectionStrategy().collect(Paths.get(solutionPath).toAbsolutePath());

        List<CompilationUnit> cusResult = createCompilationUnits(testProjectRoot);
        List<CompilationUnit> cusSolution = createCompilationUnits(solutionProjectRoot);

        cusResult.forEach(Preprocessor::run);
        testProjectRoot.getSourceRoots().forEach(sr -> sr.saveAll(Paths.get(preprocessedOutPath)));
        assertEquals(cusSolution.size(), cusResult.size());

        for (int i = 0; i<cusResult.size(); i++)
            assertEquals(cusSolution.get(i).toString(), cusResult.get(i).toString());
    }
}
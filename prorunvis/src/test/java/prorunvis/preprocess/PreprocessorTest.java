package prorunvis.preprocess;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.utils.SymbolSolverCollectionStrategy;
import com.github.javaparser.utils.ProjectRoot;
import org.junit.jupiter.api.Test;
import prorunvis.Tester;

import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PreprocessorTest extends Tester {

    String testdir = "src/test/testfiles/preprocess/";

    @Test
    void test1(){
        preprocess(testdir + "test1", testdir + "test1result", testdir + "test1solution");
    }

    @Test
    void test2(){
        preprocess(testdir + "test2", testdir + "test2result", testdir + "test2solution");
    }

    private void preprocess(String testPath, String resultPath, String solutionPath){
        ProjectRoot testProjectRoot = new SymbolSolverCollectionStrategy().collect(Paths.get(testPath).toAbsolutePath());
        ProjectRoot solutionProjectRoot = new SymbolSolverCollectionStrategy().collect(Paths.get(solutionPath).toAbsolutePath());

        List<CompilationUnit> cusResult = createCompilationUnits(testProjectRoot);
        List<CompilationUnit> cusSolution = createCompilationUnits(solutionProjectRoot);

        cusResult.forEach(Preprocessor::run);
        testProjectRoot.getSourceRoots().forEach(sr -> sr.saveAll(Paths.get(resultPath)));
        assertEquals(cusSolution.size(), cusResult.size());

        for (int i = 0; i<cusResult.size(); i++)
            assertEquals(cusSolution.get(i).toString(), cusResult.get(i).toString());
    }
}
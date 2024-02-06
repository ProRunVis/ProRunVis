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

class InstrumenterTest extends Tester {
    String testDir = "src/test/testfiles/instrument/";

    @Test
    void test1(){
        instrument(testDir + "test1", testDir + "test1result", testDir + "test1solution");
    }

    @Test
    void test2(){
        instrument(testDir + "test2", testDir + "test2result", testDir + "test2solution");
    }

    private void instrument(String testPath, String resultPath, String solutionPath){
        ProjectRoot testProjectRoot = new SymbolSolverCollectionStrategy().collect(Paths.get(testPath).toAbsolutePath());
        ProjectRoot solutionProjectRoot = new SymbolSolverCollectionStrategy().collect(Paths.get(solutionPath).toAbsolutePath());

        List<CompilationUnit> cusResult = createCompilationUnits(testProjectRoot);
        List<CompilationUnit> cusSolution = createCompilationUnits(solutionProjectRoot);

        Map<Integer, Node> map = new HashMap<>();
        File traceFile = new File(resultPath + "/TraceFile.tr");
        Instrumenter.setupTrace(traceFile);

        cusResult.forEach(cu -> {
            Preprocessor.run(cu);
            Instrumenter.run(cu, map);
        });

        testProjectRoot.getSourceRoots().forEach(sr -> sr.saveAll(Paths.get(resultPath)));

        assertEquals(cusSolution.size(), cusResult.size());

        for (int i = 0; i<cusResult.size(); i++)
            assertEquals(cusSolution.get(i).toString(), cusResult.get(i).toString());
    }

}
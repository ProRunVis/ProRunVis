package prorunvis.instrument;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.utils.SymbolSolverCollectionStrategy;
import com.github.javaparser.utils.ProjectRoot;
import org.junit.jupiter.api.Test;
import prorunvis.preprocess.Preprocessor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InstrumenterTest {
    String testdir = "src/test/testfiles/instrument/";

    @Test
    void test1(){
        instrumentFile(testdir + "test1", testdir + "test1result", testdir + "test1solution");
    }

    @Test
    void test2(){
        instrumentFile(testdir + "test2", testdir + "test2result", testdir + "test2solution");
    }

    private void instrumentFile(String testPath, String resultPath, String solutionPath){
        StaticJavaParser.getParserConfiguration().setSymbolResolver(new JavaSymbolSolver(new CombinedTypeSolver()));

        ProjectRoot testProjectRoot = new SymbolSolverCollectionStrategy().collect(Paths.get(testPath).toAbsolutePath());

        ProjectRoot solutionProjectRoot = new SymbolSolverCollectionStrategy().collect(Paths.get(solutionPath).toAbsolutePath());

        List<CompilationUnit> cusResult = new ArrayList<>();
        List<CompilationUnit> cusSolution = new ArrayList<>();

        solutionProjectRoot.getSourceRoots().forEach(sr -> {
            try {
                sr.tryToParse().forEach(cu -> cusSolution.add(cu.getResult().get()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        testProjectRoot.getSourceRoots().forEach(sr -> {
            try {
                sr.tryToParse().forEach(cu -> cusResult.add(cu.getResult().get()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

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
package prorunvis.compileandrun;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.symbolsolver.utils.SymbolSolverCollectionStrategy;
import com.github.javaparser.utils.ProjectRoot;
import org.junit.jupiter.api.Test;
import prorunvis.CompileAndRun;
import prorunvis.instrument.Instrumenter;
import prorunvis.preprocess.Preprocessor;

import java.io.*;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import prorunvis.Tester;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CompileAndRunTest extends Tester {
    String testDir = "src/test/testfiles/compileandrun/";
    @Test
    void test1(){
        compileAndRun(testDir + "test1", testDir + "test1result", testDir + "test1result", testDir + "test1solution");
    }
    @Test
    void test2(){
        compileAndRun(testDir + "test2", testDir + "test2result", testDir + "test2result", testDir + "test2solution");
    }
    void compileAndRun(String testPath, String instrumentedOutPath, String compiledOutPath, String solutionPath){
        ProjectRoot testProjectRoot = new SymbolSolverCollectionStrategy().collect(Paths.get(testPath).toAbsolutePath());

        List<CompilationUnit> cusResult = createCompilationUnits(testProjectRoot);

        Map<Integer, Node> map = new HashMap<>();
        File resultTrace = new File(instrumentedOutPath + "/TraceFile.tr");
        Instrumenter.setupTrace(resultTrace);

        cusResult.forEach(cu -> {
            Preprocessor.run(cu);
            Instrumenter.run(cu, map);
        });

        try {
            CompileAndRun.run(testProjectRoot, cusResult, instrumentedOutPath, compiledOutPath);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        File solutionTrace = new File(solutionPath + "/TraceFile.tr");
        FileReader frSolution = null;
        FileReader frResult = null;
        try {
            frSolution = new FileReader(solutionTrace);
            frResult = new FileReader(resultTrace);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        BufferedReader rSolution = new BufferedReader(frSolution);
        BufferedReader rResult = new BufferedReader(frResult);

        String lineResult = null;
        String lineSolution = null;

        do {
            try {
                lineResult = rResult.readLine();
                lineSolution = rSolution.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            assertEquals(lineSolution, lineResult);
        }while(lineResult != null && lineSolution != null);

        try {
            frSolution.close();
            frResult.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
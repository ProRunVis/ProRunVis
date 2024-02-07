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

/**
 * This class is for testing the {@link CompileAndRun} class.
 */
class CompileAndRunTest extends Tester {

    /**
     * A String to store the relative path to the directory where all the tests are stored, to use in the test functions.
     */
    String testDir = "src/test/testfiles/compileandrun/";

    /**
     * Test 1
     */
    @Test
    void test1(){
        compileAndRun(testDir + "test1", testDir + "test1result", testDir + "test1solution");
    }

    /**
     * Test 2
     */
    @Test
    void test2(){
        compileAndRun(testDir + "test2", testDir + "test2result", testDir + "test2solution");
    }

    /**
     * Take a directory with a preprocessed(by the {@link Preprocessor}) and instrumented(by the {@link Instrumenter}) java project with a corresponding traceFile.
     * Compile and run it with {@link CompileAndRun}, safe the result of the traceFile, compare it to a given solution and assert any differences between the two.
     * @param instrumentedInPath the relative Path of the directory where the already preprocessed and instrumented Test-project and the traceFile is located
     * @param compiledOutPath the relative Path of the directory where the compiled Test-project will be stored
     * @param solutionPath the relative Path of the directory where the solution(how the traceFile should look like) for the Test-project is located
     */
    void compileAndRun(String instrumentedInPath, String compiledOutPath, String solutionPath){
        ProjectRoot testProjectRoot = new SymbolSolverCollectionStrategy().collect(Paths.get(instrumentedInPath).toAbsolutePath());

        List<CompilationUnit> cusResult = createCompilationUnits(testProjectRoot);


        Map<Integer, Node> map = new HashMap<>();
        File traceFile = new File("resources/TraceFile.tr");
        Instrumenter.setupTrace(traceFile);
        cusResult.forEach(cu -> {
            Preprocessor.run(cu);
            Instrumenter.run(cu, map);
        });

        File solutionTrace = traceFile;

        File resultTrace = new File(instrumentedInPath + "/TraceFile.tr");

        if (resultTrace.exists()) {
            resultTrace.delete();
        }
        try {
            resultTrace.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            CompileAndRun.run(testProjectRoot, cusResult, instrumentedInPath, compiledOutPath);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        FileReader frSolution, frResult;
        try {
            frSolution = new FileReader(solutionTrace);
            frResult = new FileReader(resultTrace);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        BufferedReader rSolution = new BufferedReader(frSolution);
        BufferedReader rResult = new BufferedReader(frResult);

        String lineResult, lineSolution;

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
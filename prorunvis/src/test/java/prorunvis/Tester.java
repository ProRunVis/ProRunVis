package prorunvis;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.utils.ProjectRoot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A class that extends all the other classes that are responsible for running tests.
 * It is responsible for creating a List of {@link CompilationUnit}s out of a {@link ProjectRoot} which is needed for every testcase.
 */
public class Tester {
    /**
     * Take in a {@link ProjectRoot} and create a List of its {@link CompilationUnit}s.
     * @param projectRoot the {@link ProjectRoot} to be processed.
     * @return the created List of {@link CompilationUnit}s.
     */
    protected List<CompilationUnit> createCompilationUnits(ProjectRoot projectRoot){
        StaticJavaParser.getParserConfiguration().setSymbolResolver(new JavaSymbolSolver(new CombinedTypeSolver()));
        List<CompilationUnit> cus = new ArrayList<>();
        projectRoot.getSourceRoots().forEach(sr -> {
            try {
                sr.tryToParse().forEach(cu -> cus.add(cu.getResult().get()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return cus;
    }
}
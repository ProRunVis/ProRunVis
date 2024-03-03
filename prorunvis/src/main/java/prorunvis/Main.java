package prorunvis;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.utils.SymbolSolverCollectionStrategy;
import com.github.javaparser.utils.ProjectRoot;
import prorunvis.instrument.Instrumenter;
import prorunvis.preprocess.Preprocessor;
import prorunvis.trace.process.TraceProcessor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Main {
    /**
    * Private constructor for main is never called.
    */
    private Main() { }

    /**
     * Entry point. Gets called if program is stared via the terminal.
     * @param args parameters (path of directory with project
     *             and optional -i flag if project should only be instrumented,
     *             in that case the project does not need a main method as an entry point since it is not compiled).
     * @throws IOException
     * @throws InterruptedException
     */
    public static void main(final String[]args) throws IOException, InterruptedException {

        boolean instrumentOnly = false;

        //check if an argument of sufficient length has been provided
        if (args.length == 0) {
            System.out.println("Missing input");
            return;
        }
        if (args.length == 2 && args[1].equals("-i")) {
            instrumentOnly = true;
        }
        if (!Files.isDirectory(Paths.get(args[0]))) {
            System.out.println("Folder not found");
            return;
        }

        StaticJavaParser.getParserConfiguration().setSymbolResolver(new JavaSymbolSolver(new CombinedTypeSolver()));
        ProjectRoot projectRoot =
                new SymbolSolverCollectionStrategy().collect(Paths.get(args[0]).toAbsolutePath());
        File traceFile = new File("resources/TraceFile.tr");

        List<CompilationUnit> cus = new ArrayList<>();
        projectRoot.getSourceRoots().forEach(sr -> {
            try {
                sr.tryToParse().forEach(cu -> cus.add(cu.getResult().get()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        Map<Integer, Node> map = new HashMap<>();
        Instrumenter.setupTrace(traceFile);
        cus.forEach(cu -> {
            Preprocessor.run(cu);
            Instrumenter.run(cu, map);
        });
        Instrumenter.safeInstrumented(projectRoot, "resources/out/instrumented");

        if (!instrumentOnly) {
            CompileAndRun.run(cus, "resources/out/instrumented", "resources/out/compiled");
            TraceProcessor processor = new TraceProcessor(map, traceFile.getPath(), Paths.get(args[0]));
            processor.start();
            System.out.println(processor);
        }

    }
}

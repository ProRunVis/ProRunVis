package api.functionality.process;

import api.upload.storage.StorageProperties;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.utils.SymbolSolverCollectionStrategy;
import com.github.javaparser.utils.ProjectRoot;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;
import prorunvis.CompileAndRun;
import prorunvis.instrument.Instrumenter;
import prorunvis.preprocess.Preprocessor;
import prorunvis.trace.TraceNode;
import prorunvis.trace.process.TraceProcessor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public final class SingleRunProcessingService implements ProcessingService {

    /**
     * The location where input data is stored.
     */
    private final Path inLocation;

    /**
     * The location for storing temporary output data.
     */
    private final Path outLocation;

    /**
     * A List of {@link CompilationUnit}s.
     */
    private List<CompilationUnit> cus;

    /**
     * A File containing the trace of a program run.
     */
    private File traceFile;

    /**
     * A HashMap containing {@link Node} objects mapped to
     * their trace IDs.
     */
    private HashMap<Integer, Node> traceMap;

    /**
     * A List of {@link TraceNode} objects as provided by the
     * {@link TraceProcessor} of the ProRunVis library.
     */
    private List<TraceNode> nodes;

    /**
     * Constructs a ProcessingService for processing a single program
     * run.
     *
     * @param properties The StorageProperties for input and output
     *                   locations.
     */
    public SingleRunProcessingService(final StorageProperties properties) {
        if (properties.getLocation().trim().isEmpty()) {
            throw new ProcessingException("Cannot process empty directory.");
        }

        inLocation = Paths.get(properties.getLocation());
        outLocation = Paths.get(properties.getOutLocation());
    }
    @Override
    public boolean isReady() {
        return inLocation.toFile().exists()
                && inLocation.toFile().isDirectory()
                && Objects.requireNonNull(inLocation.toFile().listFiles()).length > 0;
    }

    @Override
    public void instrument() {
        //setup parser and trace file
        StaticJavaParser.getParserConfiguration().setSymbolResolver(new JavaSymbolSolver(new CombinedTypeSolver()));
        ProjectRoot projectRoot =
                new SymbolSolverCollectionStrategy().collect(inLocation);

        traceFile = new File(outLocation.toString() + "/Trace.tr");
        Instrumenter.setupTrace(traceFile);

        //run parser and collect compilation units
        cus = new ArrayList<>();
        projectRoot.getSourceRoots().forEach(sr -> {
            try {
                sr.tryToParse().forEach(cu -> cus.add(cu.getResult().orElseThrow()));
            } catch (IOException | NoSuchElementException e) {
                throw new ProcessingException("Cannot parse provided input.", e);
            }
        });

        traceMap = new HashMap<>();
        cus.forEach(cu -> {
            Preprocessor.run(cu);
            Instrumenter.run(cu, traceMap);
        });
        Instrumenter.saveInstrumented(projectRoot, outLocation.toString() + "/instrumented");
    }

    @Override
    public void trace() {
        try {
            CompileAndRun.run(cus, outLocation.toString() + "/instrumented",
                    outLocation.toString() + "/compiled");
        } catch (IOException | InterruptedException | ArrayIndexOutOfBoundsException e) {
            throw new ProcessingException(e.getMessage());
        }
    }

    @Override
    public void process() {
        TraceProcessor processor = new TraceProcessor(traceMap, traceFile.getPath(), inLocation);
        try {
            processor.start();
        } catch (IOException e) {
            throw new ProcessingException("An error occurred during processing of the trace.", e);
        }
        nodes = processor.getNodeList();
    }

    @Override
    public String toJSON() {
        Gson gson = new Gson();
        String response = gson.toJson(nodes);
        //replace \\ from windows paths with / for webkit directory
        return response.replace("\\\\", "/");
    }
}

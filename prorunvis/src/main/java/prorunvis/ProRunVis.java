package prorunvis;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.utils.SymbolSolverCollectionStrategy;
import com.github.javaparser.utils.ProjectRoot;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.cli.*;
import prorunvis.instrument.Instrumenter;
import prorunvis.preprocess.Preprocessor;
import prorunvis.trace.process.TraceProcessor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ProRunVis {
    /**
     * Private constructor for main is never called.
     */
    private ProRunVis() {
    }

    /**
     * Entry point. Gets called if program is stared via the terminal.
     *
     * @param args parameters (path of directory with project
     *             and optional -i flag if project should only be instrumented,
     *             in that case the project does not need a main method as an entry point since it is not compiled).
     * @throws IOException
     * @throws InterruptedException
     */
    public static void main(final String[] args) {

        boolean instrumentOnly = false;
        String inputPath;
        String outputPath = "resources/out";

        Options options = new Options();
        options.addOption(Option.builder("h")
                .longOpt("help")
                .desc("Prints this help message")
                .build());
        options.addOption(Option.builder("i")
                .longOpt("instrument")
                .desc("If the input should only be instrumented")
                .build());
        options.addOption(Option.builder("o")
                .longOpt("output")
                .hasArg()
                .argName("output_directory")
                .desc("Output file path")
                .build());

        CommandLineParser commandLineParser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;


        try {
            cmd = commandLineParser.parse(options, args);

            //check if required input has been provided
            String[] positionalArgs = cmd.getArgs();
            if (positionalArgs.length < 1) {
                throw new ParseException("Input file is required.");
            }

            inputPath = positionalArgs[0];
            outputPath = "resources/out";

            if (cmd.hasOption("o")) {
                outputPath = cmd.getOptionValue("o");
            }
            if (cmd.hasOption("i")) {
                instrumentOnly = true;
            }
            if (!Paths.get(inputPath).toFile().exists()
                    || !Paths.get(inputPath).toFile().isDirectory()) {
                throw new ParseException(inputPath + " is not an existing directory.");
            }
            if (!Paths.get(outputPath).toFile().exists()) {
                if (!Paths.get(outputPath).toFile().mkdirs()) {
                    throw new ParseException(outputPath + " is not an existing directory and could not be created.");
                }
            }
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            formatter.printHelp("java -jar <prorunvis.jar> <input_path> [options] \n\nWith options: \n", options);
            System.exit(1);
        }


        StaticJavaParser.getParserConfiguration().setSymbolResolver(new JavaSymbolSolver(new CombinedTypeSolver()));
        ProjectRoot projectRoot = new SymbolSolverCollectionStrategy()
                .collect(Paths.get(args[0]).toAbsolutePath());

        File traceFile = new File(outputPath + "/Trace.tr");

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
        Instrumenter.saveInstrumented(projectRoot, outputPath + "/instrumented");

        //run and process the tracing if not specified otherwise by the -i flag
        if (!instrumentOnly) {
            try {
                CompileAndRun.run(cus, outputPath + "/instrumented", outputPath + "/compiled");
                TraceProcessor processor = new TraceProcessor(map, traceFile.getPath(), Paths.get(args[0]));
                processor.start();

                //save json trace to file
                File jsonTrace = new File(outputPath + "/Trace.json");
                BufferedWriter writer = new BufferedWriter(new FileWriter(jsonTrace));
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                writer.write(gson.toJson(processor.getNodeList()));
                writer.flush();
                writer.close();
            } catch (IOException | InterruptedException e) {
                System.err.println(e.getMessage());
            }
        }

    }
}

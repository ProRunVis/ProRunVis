package prorunvis;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.utils.SymbolSolverCollectionStrategy;
import com.github.javaparser.utils.ProjectRoot;
import org.apache.commons.cli.*;
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


        try{
            cmd = commandLineParser.parse(options, args);

            //check if required input has been provided
            String[] positionalArgs = cmd.getArgs();
            if(positionalArgs.length < 1){
                throw new ParseException("Input file is required.");
            }
            
            String inputPath = positionalArgs[0];
            String outputPath = "resources/out";
            if(cmd.hasOption("o")){
                outputPath = cmd.getOptionValue("o");
            }
            if(!Paths.get(inputPath).toFile().exists() ||
               !Paths.get(inputPath).toFile().isDirectory()){
                throw new ParseException(inputPath + " is not an existing directory.");
            }
            if(!Paths.get(outputPath).toFile().exists()){
                if(!Paths.get(outputPath).toFile().mkdirs()){
                    throw new ParseException(outputPath + " is not an existing directory and could not be created.");
                }
            }
        } catch (ParseException e){
            System.err.println(e.getMessage());
            formatter.printHelp("java -jar <prorunvis.jar> <input_path> [options] \n\nWith options: \n", options);
        }

        /*
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

         */

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
        Instrumenter.saveInstrumented(projectRoot, "resources/out/instrumented");

        if (!instrumentOnly) {
            CompileAndRun.run(cus, "resources/out/instrumented", "resources/out/compiled");
            TraceProcessor processor = new TraceProcessor(map, traceFile.getPath(), Paths.get(args[0]));
            processor.start();
            System.out.println(processor);
        }

    }
}

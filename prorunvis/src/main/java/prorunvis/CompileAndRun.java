package prorunvis;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This is a utility class which is responsible for compiling a package of {@link CompilationUnit}s.
 */
public final class CompileAndRun {

    /**
     * Should not be called.
     */
    private CompileAndRun() {
        throw new IllegalStateException();
    }

    /**
     * Take in a List of its {@link CompilationUnit}s.
     * The code is compiled into a new folder. The compiled program is then executed.
     * @param cus an easily accessible List of the <code>projectRoot</code>s {@link CompilationUnit}s
     * @param instrumentedInPath the relative Path of the directory where the instrumented project lies
     * @param compiledOutPath the relative Path of the directory where the compiled project will be saved
     * @throws IOException when a problem occurs while the <code>projectRoot</code> is written out
     * @throws InterruptedException when something interrupts the compilation or execution process
     *
     */
    public static void run(final List<CompilationUnit> cus,
                           final String instrumentedInPath, final String compiledOutPath)
            throws IOException, InterruptedException {
        Path savePath = Paths.get(instrumentedInPath);

        File compiled = new File(compiledOutPath);
        compiled.mkdir();

        CompilationUnit mainUnit = cus.stream().filter(cu -> cu.findFirst(MethodDeclaration.class,
                m -> m.getNameAsString().equals("main")).isPresent()).toList().get(0);

        String fileName = mainUnit.getStorage().get().getFileName();
        Path path = mainUnit.getStorage().get().getDirectory();

        Runtime currentRuntime = Runtime.getRuntime();
        Process compileProc = currentRuntime.exec("javac -sourcepath " + savePath + " -d "
                + compiledOutPath + " " + path.toString() + "/" + fileName);
        compileProc.waitFor();

        String compileError = new BufferedReader(new InputStreamReader(compileProc.getErrorStream()))
                .lines().collect(Collectors.joining("\n"));
        if (!compileError.isEmpty()) {
            throw new InterruptedException("An error occurred during compilation.\n" + compileError);
        }

        String rootPath = new StringBuilder(String.valueOf(path)).delete(
                0, savePath.toAbsolutePath().toString().length() + 1).toString();
        Process runProc = currentRuntime.exec("java -cp " + compiledOutPath + " "
                + ((rootPath.isEmpty()) ? "" : rootPath + ".") + fileName.replace(".java", ""));
        runProc.waitFor();

        String runError = new BufferedReader(new InputStreamReader(runProc.getErrorStream())).lines().
                collect(Collectors.joining("\n"));
        if (!runError.isEmpty()) {
            System.out.println("There was an error running the input code.\n" + runError);
        }
    }
}

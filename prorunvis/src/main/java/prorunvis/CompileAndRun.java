package prorunvis;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.utils.ProjectRoot;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This is a utility class which is responsible for converting a {@link ProjectRoot} back to a directory of java files and compiling said package.
 */
public final class CompileAndRun {

    /**
     * Should not be called
     */
    private CompileAndRun() {
        throw new IllegalStateException();
    }

    /**
     * Take in a {@link ProjectRoot} and a List of its {@link CompilationUnit}s. First the <code>projectRoot</code> is written out into a new folder
     * and then compiled into another new folder. The compiled program is then executed.
     * @param projectRoot the {@link ProjectRoot} to be processed
     * @param cus an easily accessible List of the <code>projectRoot</code>s {@link CompilationUnit}s
     * @throws IOException when a problem occurs while the <code>projectRoot</code> is written out
     * @throws InterruptedException when something interrupts the compilation or execution process
     */
    public static void run(final ProjectRoot projectRoot, final List<CompilationUnit> cus, String instrumentedOutPath, String compiledOutPath) throws IOException, InterruptedException {
        Path savePath = Paths.get(instrumentedOutPath);

        File instrumented = new File(savePath.toString());
        instrumented.mkdir();

        File compiled = new File(compiledOutPath);
        compiled.mkdir();

        projectRoot.getSourceRoots().forEach(sr -> sr.saveAll(savePath));
        CompilationUnit mainUnit = cus.stream().filter(cu -> cu.findFirst(MethodDeclaration.class, m -> m.getNameAsString().equals("main")).isPresent()).toList().get(0);

        String fileName = mainUnit.getStorage().get().getFileName();
        Path path = mainUnit.getStorage().get().getDirectory();

        Runtime currentRuntime = Runtime.getRuntime();
        Process compileProc = currentRuntime.exec("javac -sourcepath " + savePath + " -d " + compiledOutPath + " " + path.toString() + "/" + fileName);
        compileProc.waitFor();

        String compileError = new BufferedReader(new InputStreamReader(compileProc.getErrorStream())).lines().collect(Collectors.joining("\n"));
        if (!compileError.isEmpty()) {
            System.out.println("An error occurred during compilation:\n" + compileError);
            return;
        }

        String rootPath = new StringBuilder(String.valueOf(path)).delete(0, savePath.toAbsolutePath().toString().length() + 1).toString();
        Process runProc = currentRuntime.exec("java -cp " + compiledOutPath +" " + ((rootPath.isEmpty()) ? "" : rootPath + ".") + fileName.replace(".java", ""));
        runProc.waitFor();

        String runError = new BufferedReader(new InputStreamReader(runProc.getErrorStream())).lines().collect(Collectors.joining("\n"));
        if (!runError.isEmpty()) {
            System.out.println("There was an error running the input code:\n" + runError);
        }
    }
}

package prorunvis;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.utils.ProjectRoot;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class CompileAndRun {

    /**
     * Writes the given {@link CompilationUnit}s into .java files, then compiles and runs the project
     * @throws IOException if something goes wrong while the files are created
     * @throws InterruptedException if the compilation or execution is interrupted
     */
    public static void run (ProjectRoot projectRoot, List<CompilationUnit> cus) throws IOException, InterruptedException {
        Path savePath = Paths.get("resources/out/instrumented");

        File instrumented = new File(savePath.toString());
        instrumented.mkdir();

        File compiled = new File("resources/out/compiled");
        compiled.mkdir();

        projectRoot.getSourceRoots().forEach(sr -> sr.saveAll(savePath));
        CompilationUnit mainUnit = cus.stream().filter(cu -> cu.findFirst(MethodDeclaration.class, m -> m.getNameAsString().equals("main")).isPresent()).toList().get(0);

        String fileName = mainUnit.getStorage().get().getFileName();
        Path path = mainUnit.getStorage().get().getDirectory();

        Runtime currentRuntime = Runtime.getRuntime();
        Process compileProc = currentRuntime.exec("javac -sourcepath " + savePath + " -d resources/out/compiled " + path.toString() + "/" + fileName);
        compileProc.waitFor();

        String compileError = new BufferedReader(new InputStreamReader(compileProc.getErrorStream())).lines().collect(Collectors.joining("\n"));
        if (!compileError.isEmpty()) {
            System.out.println("An error occurred during compilation:\n" + compileError);
            return;
        }

        String rootPath = new StringBuilder(String.valueOf(path)).delete(0, savePath.toAbsolutePath().toString().length() + 1).toString();
        Process runProc = currentRuntime.exec("java -cp resources/out/compiled " + ((rootPath.isEmpty()) ? "" : rootPath + ".") + fileName.replace(".java", ""));
        runProc.waitFor();

        String runError = new BufferedReader(new InputStreamReader(runProc.getErrorStream())).lines().collect(Collectors.joining("\n"));
        String output = new BufferedReader(new InputStreamReader(runProc.getInputStream())).lines().collect(Collectors.joining("\n"));
        if (!runError.isEmpty()) System.out.println("There was an error running the input code:\n" + runError);
        else System.out.println(output);
    }
}

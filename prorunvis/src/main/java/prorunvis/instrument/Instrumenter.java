package prorunvis.instrument;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.modules.ModuleDeclaration;
import com.github.javaparser.ast.type.VoidType;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.utils.ProjectRoot;
import prorunvis.trace.TraceVisitor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * This Instrumenter is used to modify (instrument) code.
 * For that, the code is changed so that is automatically writes a trace
 * while being executed.
 */
public final class Instrumenter {

    /**
     * The trace file to which this Instrumenter writes all tracing information.
     */
    private static File traceFile;

    private Instrumenter() {
        throw new IllegalStateException("Class can not be instantiated");
    }

    /**
     * Sets up the trace file. If the given file does not exist, it and all it's parent
     * directories are created. If the file already exists it is deleted and newly created,
     * to avoid problems at runtime.
     * @param file The file to which the code trace is written.
     */
    public static CompilationUnit setupTrace(File file, String rootPackage) {
        traceFile = file;
        try {
            traceFile.mkdirs();
            if (traceFile.exists()) {
                traceFile.delete();
            }
            traceFile.createNewFile();

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return new InstrumenterSetupVisitor().setupVisitorCu(rootPackage, traceFile.getAbsolutePath());
    }

    /**
     * Saves the given project to the given path.
     * @param pr Project root of the project to be saved
     * @param instrumentedOutPath relative path where project is to be saved to
     */
    public static void safeInstrumented(ProjectRoot pr, String instrumentedOutPath) {
        File instrumented = new File(instrumentedOutPath);
        instrumented.mkdir();
        pr.getSourceRoots().forEach(sr -> sr.saveAll(Paths.get(instrumentedOutPath)));
    }

    /**
     * Runs this Instrumenter. The given {@link CompilationUnit} is modified by
     * adding necessary imports before the {@link TraceVisitor} and {@link InstrumenterSetupVisitor}
     * are executed.
     * @param cu The compilation unit which will be modified by this Instrumenter.
     */
    public static void run(final CompilationUnit cu, Map<Integer, Node> map) {
        cu.addImport(BufferedWriter.class).addImport(FileWriter.class).addImport(IOException.class);
        new TraceVisitor().visit(cu, map);
    }
}

/**
 * An inner class used by an {@link Instrumenter}.
 */
class InstrumenterSetupVisitor extends ModifierVisitor<String> {
    public CompilationUnit setupVisitorCu(final String rootPackage, final String path){
        PackageDeclaration packageDeclaration = new PackageDeclaration(new Name(rootPackage));
        NodeList<ImportDeclaration> imports = new NodeList<ImportDeclaration>();
        NodeList<TypeDeclaration<?>> types = new NodeList<TypeDeclaration<?>>();
        ModuleDeclaration module = new ModuleDeclaration();

        MethodDeclaration method = new MethodDeclaration(new NodeList<>(Modifier.privateModifier(), Modifier.staticModifier()), "proRunVisTrace", new VoidType(), new NodeList<>(new Parameter(StaticJavaParser.parseClassOrInterfaceType("String"), "trace")));

        method.setBody(StaticJavaParser.parseBlock("{try {"
                + "BufferedWriter writer = new BufferedWriter(new FileWriter(\""
                + path.replace('\\', '/')
                + "\", true));" + "writer.write("
                + method.getParameter(0).getNameAsString()
                + " + System.lineSeparator());"
                + "writer.close();"
                + "} catch (IOException e) {throw new RuntimeException(e.getMessage());}}")
        );

        ClassOrInterfaceDeclaration cfd = new ClassOrInterfaceDeclaration(new NodeList<Modifier>(), false, "ProRunVisTrace");
        cfd.addMember(method);
        types.add(cfd);

        return new CompilationUnit(packageDeclaration, imports, types, module);
    }

    /**
     * Adds a method to every class or interface which can be called to write
     * a given String into the trace file.
     * @param cfd The class or interface declaration which is modified.
     * @param path The absolute path to the trace file for this Instrumenter.
     * @return The modified <code>cfd</code>.
     */
    @Override
    public ClassOrInterfaceDeclaration visit(final ClassOrInterfaceDeclaration cfd, final String path) {
        MethodDeclaration method = new MethodDeclaration(new NodeList<>(Modifier.privateModifier(), Modifier.staticModifier()), "proRunVisTrace", new VoidType(), new NodeList<>(new Parameter(StaticJavaParser.parseClassOrInterfaceType("String"), "trace")));

        method.setBody(StaticJavaParser.parseBlock("{try {"
                + "BufferedWriter writer = new BufferedWriter(new FileWriter(\""
                + path.replace('\\', '/')
                + "\", true));" + "writer.write("
                + method.getParameter(0).getNameAsString()
                + " + System.lineSeparator());"
                + "writer.close();"
                + "} catch (IOException e) {throw new RuntimeException(e.getMessage());}}")
        );

        cfd.addMember(method);
        return cfd;
    }
}




package prorunvis.Instrumentalize;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.type.VoidType;
import com.github.javaparser.ast.visitor.ModifierVisitor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Instrumentalizer {

    public static File traceFile = new File("resources/TraceFile.tr");
    private static final List<ModifierVisitor<List<Integer>>> visitor = new ArrayList<>();
    public static List<Integer> traces = new ArrayList<>();

    public static void addVisitor(ModifierVisitor<List<Integer>> vis) {visitor.add(vis);}

    /**
     * Creates a tracefile, adds necessary imports to the {@link CompilationUnit} and adds a trace method to every class to be called by tracers for different Statements.
     */
    public static void setupTrace(){
        try {
            traceFile.mkdirs();
            if(traceFile.exists()) traceFile.delete();
            traceFile.createNewFile();
        }
        catch (IOException e) {throw new RuntimeException(e.getMessage());}
    }

    /**
     * runs every {@link ModifierVisitor} over the {@link CompilationUnit}.
     */
    public static void run(CompilationUnit cu) {
        cu.addImport(BufferedWriter.class).addImport(FileWriter.class).addImport(IOException.class);
        for(ModifierVisitor<List<Integer>> visitor : visitor) {
            visitor.visit(cu, traces);
        }
        new InstrumentalizerSetupVisitor().visit(cu, traceFile.getAbsolutePath());
    }
}

class InstrumentalizerSetupVisitor extends ModifierVisitor<String> {

    /**
     * Adds a method to every Class or Interface which can be called to write a given String into the trace file.
     */
    @Override
    public ClassOrInterfaceDeclaration visit(ClassOrInterfaceDeclaration cfd, String path) {

        MethodDeclaration method = new MethodDeclaration(new NodeList<>(Modifier.privateModifier(), Modifier.staticModifier()), "proRunVisTrace", new VoidType(), new NodeList<>(new Parameter(StaticJavaParser.parseClassOrInterfaceType("String"), "trace")));

        method.setBody(StaticJavaParser.parseBlock("{try {" +
                "BufferedWriter writer = new BufferedWriter(new FileWriter(\"" +
                path.replace('\\','/') +
                "\", true));" + "writer.write(" + method.getParameter(0).getNameAsString() + " + System.lineSeparator());" +
                "writer.close();" +
                "} catch (IOException e) {throw new RuntimeException(e.getMessage());}}")
        );

        cfd.addMember(method);
        return cfd;
    }
}




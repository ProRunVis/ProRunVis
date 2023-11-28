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
    public CompilationUnit cu;
    private final InstrumentalizerSetupVisitor isv;
    private final List<ModifierVisitor<?>> visitor = new ArrayList<>();

    /**
     * Enables instrumentalization over the given {@link CompilationUnit} with the given {@link ModifierVisitor}
     * @param cu The {@link CompilationUnit} to be instrumentalized
     * @param visitor {@link ModifierVisitor} implementing tracing for different statements. If none are passed, only setup is performed.
     */
    public Instrumentalizer(CompilationUnit cu, List<ModifierVisitor<?>> visitor) {
        isv = new InstrumentalizerSetupVisitor();
        this.visitor.addAll(visitor);
        this.cu = cu;
    }

    /**
     * Creates a tracefile, adds necessary imports to the {@link CompilationUnit} and adds a trace method to every class to be called by tracers for different Statements.
     */
    private void setupTrace(){
        File file = new File("out/Trace.tr");
        try {file.createNewFile();}
        catch (IOException e) {e.printStackTrace();}
        cu.addImport(BufferedWriter.class).addImport(FileWriter.class).addImport(IOException.class);
        isv.visit(cu, file.getAbsolutePath());
    }

    /**
     * runs every {@link ModifierVisitor} over the {@link CompilationUnit}.
     */
    public void run() {
        setupTrace();
        for(ModifierVisitor<?> visitor : visitor) {
            visitor.visit(cu, null);
        }
    }
}

class InstrumentalizerSetupVisitor extends ModifierVisitor<String> {

    /**
     * Adds a method to every Class or Interface which can be called to write a given String into the tracefile.
     */
    @Override
    public ClassOrInterfaceDeclaration visit(ClassOrInterfaceDeclaration cfd, String path) {

        MethodDeclaration method = new MethodDeclaration(new NodeList<>(Modifier.privateModifier(), Modifier.staticModifier()), "proRunVisTrace", new VoidType(), new NodeList<>(new Parameter(StaticJavaParser.parseClassOrInterfaceType("String"), "trace")));

        method.setBody(StaticJavaParser.parseBlock("{try {" +
                "BufferedWriter writer = new BufferedWriter(new FileWriter(\"" +
                path.replace('\\', '.') +
                "\", true));" + "writer.write(" + method.getParameter(0).getNameAsString() + ");" +
                "writer.close();" +
                "} catch (IOException e) {throw new RuntimeException(e.getMessage());}}")
        );

        cfd.addMember(method);
        return cfd;
    }
}


package prorunvis;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import prorunvis.Instrumentalize.Instrumentalizer;
import prorunvis.trace.modifier.IfStatementModifier;
import prorunvis.trace.modifier.ReturnStatementModifier;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[]args){

        //check if an argument of sufficient length has been provided
        if(args.length == 0
                || args[0].length() < 6){
            System.out.println("Invalid input");
            return;
        }

        //check if the argument specifies a .java file
        if(!args[0].endsWith(".java")) {
            System.out.println("Input is not a .java file");
            return;
        }

        //generate a path and file object
        Path path = Paths.get(args[0]);
        File file = path.toFile().getAbsoluteFile();

        //check if the file exists
        if(!file.exists()) {
            System.out.println("Specified file does not exist");
            return;
        }

        //parse the file and save the resulting ast
        CompilationUnit cu;
        try {

            //create compilation unit and modifier
            cu = StaticJavaParser.parse(file);
            IfStatementModifier ifMod = new IfStatementModifier();
            ReturnStatementModifier retMod = new ReturnStatementModifier();

            //add modifier to list
            List<ModifierVisitor<List<Integer>>> modifier = new ArrayList<>();
            modifier.add(ifMod);
            modifier.add(retMod);

            //instrumentalize the compilation unit
            Instrumentalizer instrumentalizer = new Instrumentalizer(cu, modifier);
            instrumentalizer.run();

            //compile and execute the program
            CompileAndRun.compile(cu);
        }catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}

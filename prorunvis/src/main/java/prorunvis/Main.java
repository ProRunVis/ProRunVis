package prorunvis;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

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
            System.out.println("No .java file found");
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
            cu = StaticJavaParser.parse(file);
        }catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}

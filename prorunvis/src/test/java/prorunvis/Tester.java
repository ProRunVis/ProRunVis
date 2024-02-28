package prorunvis;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.utils.ProjectRoot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * A class that extends all the other classes that are responsible for running tests.
 * It is responsible for creating a List of {@link CompilationUnit}s
 * out of a {@link ProjectRoot} which is needed for every testcase.
 */
public class Tester {
    /**
     * Take in a {@link ProjectRoot} and create a List of its {@link CompilationUnit}s.
     *
     * @param projectRoot the {@link ProjectRoot} to be processed.
     * @return the created List of {@link CompilationUnit}s.
     */
    protected List<CompilationUnit> createCompilationUnits(final ProjectRoot projectRoot) {
        StaticJavaParser.getParserConfiguration().setSymbolResolver(new JavaSymbolSolver(new CombinedTypeSolver()));
        List<CompilationUnit> cus = new ArrayList<>();
        projectRoot.getSourceRoots().forEach(sr -> {
            try {
                sr.tryToParse().forEach(cu -> cus.add(cu.getResult().get()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        /*
        sort the compilation units to prevent different tracing order due to
        different file systems
        */
        cus.sort(new CompilationUnitComparator(projectRoot.getRoot()));
        return cus;
    }

    /**
     * This comparator can be used to compare {@link CompilationUnit} objects.
     * The ordering is determined by the lexical order of the corresponding file name, resulting
     * in an absolute ordering.
     */
    static class CompilationUnitComparator implements Comparator<CompilationUnit> {

        /**
         * The path to the root directory in which the files to the
         * CompilationUnits are located.
         */
        private final Path root;

        /**
         * Constructs an object of CompilationUnitComparator based on a root location.
         *
         * @param root The path to the root directory in which the files for the compilation
         *             are contained.
         */
        CompilationUnitComparator(final Path root) {
            this.root = root;
        }

        /**
         * Compares to compilation units based on lexical order of their
         * file names.
         *
         * @param o1 the first object to be compared.
         * @param o2 the second object to be compared.
         * @return 1 if o1 comes lexically before o2, -1 otherwise.
         * If the paths are exactly identical the return value is 0, however
         * this should never happen for any given combination of o1 and o2, where
         * o1 and o2 aren't the exact same object.
         */
        @Override
        public int compare(final CompilationUnit o1, final CompilationUnit o2) {
            String name1 = o1.getStorage().get().getFileName();
            String name2 = o2.getStorage().get().getFileName();

            if (name1.equals(name2)) {
                List<Path> paths = iterateOverDir(root.toFile(), name1);
                for (Path p : paths) {
                    try {
                        CompilationUnit cu = StaticJavaParser.parse(p.toFile());
                        if (cu.equals(o1)) {
                            name1 = p.toString();
                        } else if (cu.equals(o2)) {
                            name2 = p.toString();
                        }
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            //Return 0 for equivalent paths
            if (name1.equals(name2)) {
                return 0;
            }

            int length = Math.min(name1.length(), name2.length());
            for (int i = 0; i < length; i++) {
                if (name1.charAt(i) < name2.charAt(i)) {
                    return 1;
                }
            }
            return -1;
        }

        private List<Path> iterateOverDir(final File file, final String searchedName) {
            List<Path> paths = new ArrayList<>();

            if (file.isDirectory()) {
                for (File f : Objects.requireNonNull(file.listFiles())) {
                    if (f.isDirectory()) {
                        paths.addAll(iterateOverDir(f, searchedName));
                    } else {
                        if (f.getName().equals(searchedName)) {
                            paths.add(Paths.get(f.getPath()));
                        }
                    }
                }
            }

            return paths;
        }
    }
}

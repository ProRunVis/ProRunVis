package prorunvis.trace.process;

import com.github.javaparser.Range;

import java.nio.file.Path;

public class JumpLink extends Range {

    private final Path filepath;
    public JumpLink(final Range range, final Path filepath) {
        super(range.begin, range.end);
        this.filepath = filepath;
    }
}

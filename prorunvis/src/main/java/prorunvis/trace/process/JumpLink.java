package prorunvis.trace.process;

import com.github.javaparser.Range;


/**
 * This class is used for links in {@link prorunvis.trace.TraceNode} objects, providing
 * the range and target file of the link.
 */
public class JumpLink extends Range {

    /**
     * A string representation of the relative path
     * to the file this link object points to.
     */
    private final String filepath;

    /**
     * Construct a JumpLink object with a range and target.
     *
     * @param range    The range of code to use as link.
     * @param filepath The relative path to the target file as String.
     */
    public JumpLink(final Range range, final String filepath) {
        super(range.begin, range.end);
        this.filepath = filepath;
    }

    /**
     * Creates a string representation of this JumpLink.
     *
     * @return A String representing start, end and target of this JumpLink.
     */
    public String toString() {
        return "(" + super.toString() + "," + this.filepath + ")";
    }
}

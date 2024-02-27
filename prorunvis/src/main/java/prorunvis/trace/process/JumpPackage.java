package prorunvis.trace.process;

import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;

import java.util.List;

/**
 * Objects of this class signify that a link is created from one {@link prorunvis.trace.TraceNode}
 * to another, carrying the information necessary over multiple recursive layers.
 */
public final class JumpPackage {

    /**
     * List of NodeTypes this jump closes, used to determine to which
     * {@link prorunvis.trace.TraceNode} the jump should return to.
     */
    private final List<Class<? extends Node>> jumpTo;

    /**
     * {@link Range} of the keyword which triggered the jump, so it can be set as the {@link Range} of the outLink.
     */
    private final Range jumpFrom;

    /**
     * index of the {@link prorunvis.trace.TraceNode} from which a jump originated.
     */
    private final int start;

    /**
     * Creates a new {@link JumpPackage}, signaling that a jump has started.
     * @param targets list of {@link Node}Types this jump closes.
     * @param link keyword which triggered the jump.
     * @param start {@link Range} of the keyword which triggered the jump.
     */
    public JumpPackage(final List<Class<? extends Node>> targets, final Range link, final int start) {
        jumpTo = targets;
        jumpFrom = link;
        this.start = start;
    }

    /**
     * Checks whether the current {@link Node} closes the current jump.
     * @param node {@link Node} to test.
     * @return true if {@link Node} closes the jump, false otherwise.
     */
    public boolean isTarget(final Node node) {
        return jumpTo.contains(node.getClass());
    }

    /**
     * @return the range of the keyword which triggered the jump.
     */
    public Range getJumpFrom() {
        return jumpFrom;
    }

    /**
     * @return the index of the {@link prorunvis.trace.TraceNode} from which the jump originated.
     */
    public int getStart() {
        return start;
    }
}

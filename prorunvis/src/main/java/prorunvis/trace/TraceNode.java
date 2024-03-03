package prorunvis.trace;

import com.github.javaparser.Range;
import prorunvis.trace.process.JumpLink;

import java.util.ArrayList;
import java.util.List;

/**
 * This class serves as Node for a type of tree, with each node containing information about which lines of code have
 * been executed, from where the code block has been called and which code blocks, represented by other TraceNodes,
 * have been executed within this node.
 */
public class TraceNode {

    /**
     * A List containing the ranges of executed source code
     * within this node.
     */
    private List<Range> ranges;

    /**
     * A List containing TraceNodes for code blocks inside
     * of this node.
     */
    private List<Integer> childrenIndices;

    /**
     * The node within which this node is located.
     */
    private final Integer parentIndex;

    /**
     * The Range of code which serves as link to access this node.
     */
    private JumpLink link;

    /**
     * The Ranges of code which serve as links to access the out node.
     */
    private List<JumpLink> outLinks;

    /**
     * The TraceNode to select upon following the link in outLink.
     */
    private int outIndex;

    /**
     * The index of the current iteration if TraceNode is of the loop type.
     */
    private Integer iteration;

    /**
     * The ID that maps the Node to an ASTNode.
     */
    private String traceId;

    /**
     * Constructs a {@link TraceNode} object.
     * @param parentIndex The index of the node within which this node is located.
     * @param traceId The Id that maps this node to the AST
     */
    public TraceNode(final Integer parentIndex, final String traceId) {
            this.ranges = new ArrayList<>();
            this.childrenIndices = new ArrayList<>();
            this.outLinks = new ArrayList<>();
            this.parentIndex = parentIndex;
            this.traceId = traceId;
            this.iteration = null;
    }

    /**
     *
     * Add a new {@link Range} object to the list of ranges of this node.
     * @param range The range of the executed code.
     */
    public void addRange(final Range range) {
        this.ranges.add(range);
    }

    /**
     * @return The List of Ranges of this node.
     */
    public List<Range> getRanges() {
        return this.ranges;
    }

    /**
     * Set the ranges for the code executed within this node.
     * @param newRanges A list of {@link Range} objects representing
     *                 executed code.
     */
    public void setRanges(final List<Range> newRanges) {
        this.ranges = newRanges;
    }

    /**
     * Add a new index for a {@link TraceNode} object to the list of children of this node.
     * @param childIndex An index of a node within this TraceNode.
     */
    public void addChildIndex(final int childIndex) {
        this.childrenIndices.add(childIndex);
    }

    /**
     * @return The list of indices of children of this node.
     */
    public List<Integer> getChildrenIndices() {
        return this.childrenIndices;
    }

    /**
     * Set the code blocks located within this node.
     * @param childrenIndices A list of indices of {@link TraceNode} objects representing
     *                        code blocks.
     */
    public void setChildrenIndices(final List<Integer> childrenIndices) {
        this.childrenIndices = childrenIndices;
    }

    /**
     * Gets the parent index of this node.
     * @return The index of the node within which this node is located.
     */
    public Integer getParentIndex() {
        return this.parentIndex;
    }

    /**
     * Gets the link for this node.
     * @return The Range of code which serves as link for
     *          this node.
     */
    public Range getLink() {
        return this.link;
    }

    /**
     * Sets the link for this node.
     * @param newLink The Range of code to be used as link for
     *                this node.
     */
    public void setLink(final JumpLink newLink) {
        this.link = newLink;
    }

    /**
     * Gets the outLink of this node.
     * @return The range of code which serves as outLink
     *         of this node.
     */
    public List<JumpLink> getOutLinks() {
        return this.outLinks;
    }

    /**
     * Sets the outLink of this node.
     * @param newOutLink The Range of code to be used as outLink
     *                   for this node.
     */
    public void addOutLink(final JumpLink newOutLink) {
        this.outLinks.add(newOutLink);
    }

    /**
     * Gets the index of the out node of this node.
     * @return The index of the node to be highlighted after using the
     *         {@link #outLinks} of this node.
     */
    public int getOutIndex() {
        return this.outIndex;
    }

    /**
     * Sets the index of the out node of this node.
     * @param outIndex The index of the node to be highlighted after using the
     *        {@link #outLinks} of this node.
     */
    public void setOut(final int outIndex) {
        this.outIndex = outIndex;
    }

    /**
     * Gets the Trace ID of this node.
     * @return TraceId of the TraceNode
     */
    public String getTraceID() {
        return this.traceId;
    }

    /**
     * Sets the iteration value of this node.
     * @param iteration The current iteration of the loop, so the number of times this TraceNode repeated itself so far
     */
    public void setIteration(final Integer iteration) {
        this.iteration = iteration;
    }

    /**
     * Gets the iteration value of this node.
     * @return The iteration of the TraceNode, if TraceNode is not a loop returns null.
     */
    public Integer getIteration() {
        return iteration;
    }
}

package prorunvis.trace;

import com.github.javaparser.Range;

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
    private final int parentIndex;

    /**
     * The Range of code which serves as link to access this node.
     */
    private Range link;

    /**
     * The Range of code which serves as link to access the out node.
     */
    private Range outLink;

    /**
     * The TraceNode to select upon following the link in outLink.
     */
    private int outIndex;

    private String name;

    /**
     * Constructs a {@link TraceNode} object.
     * @param parentIndex The index of the node within which this node
     *                    is located.
     */
    public TraceNode(final int parentIndex, String name) {
            this.ranges = new ArrayList<>();
            this.childrenIndices = new ArrayList<>();
            this.parentIndex = parentIndex;
        this.name = name;
    }


    /**
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
    public int getParentIndex() {
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
    public void setLink(final Range newLink) {
        this.link = newLink;
    }

    /**
     * Gets the outLink of this node.
     * @return The range of code which serves as outLink
     *         of this node.
     */
    public Range getOutLink() {
        return this.outLink;
    }

    /**
     * Sets the outLink of this node.
     * @param newOutLink The Range of code to be used as outLink
     *                   for this node.
     */
    public void setOutLink(final Range newOutLink) {
        this.outLink = newOutLink;
    }

    /**
     * Gets the index of the out node of this node.
     * @return The index of the node to be highlighted after using the
     *         {@link #outLink} of this node.
     */
    public int getOutIndex() {
        return this.outIndex;
    }

    /**
     * Sets the index of the out node of this node.
     * @param outIndex The index of the node to be highlighted after using the
     *        {@link #outLink} of this node.
     */
    public void setOut(final int outIndex) {
        this.outIndex = outIndex;
    }

    public String getName(){
        return this.name;
    }
}

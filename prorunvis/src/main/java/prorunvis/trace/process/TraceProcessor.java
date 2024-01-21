package prorunvis.trace.process;

import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;
import prorunvis.trace.TraceNode;

import java.io.IOException;
import java.util.*;

public class TraceProcessor {

    private final List<TraceNode> nodeList;
    private final Map<Integer, Node> traceMap;
    private TraceNode current;
    private Node nodeOfCurrent;
    private final Scanner scanner;
    private Stack<Integer> tokens;

    public TraceProcessor(Map<Integer, Node> trace, String traceFilePath){
        this.nodeList = new LinkedList<>();
        this.traceMap = trace;
        this.scanner = new Scanner(traceFilePath);
    }

    public void start() throws IOException{

        //read tokens to stack
        try {
            tokens = scanner.readFile();
        } catch(IOException e){
            throw new IOException("Could not read trace file.", e);
        }

        createRoot();
    }


    private void createRoot(){
        System.out.println("MAKE::ROOT");
        TraceNode root = new TraceNode(null, "root");
        nodeList.add(root);
        current = root;

        //add the first node as child to root
        createNewTraceNode();
    }

    private boolean processChild(){
        Node node = traceMap.get(tokens.peek());
        Optional<Range> range = node.getRange();
        Optional<Range> currentRange = nodeOfCurrent.getRange();

        if(range.isPresent() && currentRange.isPresent()){
            System.out.println("RANGECHECK::TRUE"+range.get()+";"+currentRange.get());
            if(currentRange.get().strictlyContains(range.get())){
                createNewTraceNode();
                return true;
            }
        }

        return false;
    }

    private void createNewTraceNode() {
        System.out.println("MAKE::CHILD");
        //create a new node
        int tokenValue = tokens.pop();
        String name = String.valueOf(tokenValue);
        int parentIndex = nodeList.indexOf(current);
        TraceNode traceNode = new TraceNode(parentIndex, name);

        //add the node to the list
        nodeList.add(traceNode);
        current.addChildIndex(nodeList.indexOf(traceNode));

        boolean isFinished = false;

        while(!(tokens.empty() || isFinished)) {
            //save state
            current = traceNode;
            Node tempNodeOfCurrent = nodeOfCurrent;
            nodeOfCurrent = traceMap.get(tokenValue);

            //process the children of this node recursively
            isFinished = !processChild();

            //restore state
            current = nodeList.get(traceNode.getParentIndex());
            nodeOfCurrent = tempNodeOfCurrent;
        }
    }

    public List<TraceNode> getNodeList(){
        return this.nodeList;
    }
}

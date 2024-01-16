package prorunvis.trace.process;

import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;
import prorunvis.trace.TraceNode;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class TraceProcessor {

    private final List<TraceNode> nodeList;
    private final Map<Integer, Node> traceMap;
    private TraceNode current;
    private Node nodeOfCurrent;
    private final Scanner scanner;
    private Stack<Integer> tokens;
    public TraceNode root;

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

        //create root node
        root = new TraceNode(null, "root");
        nodeList.add(root);
        this.current = root;

        //add children to root if there are tokens left on the stack
        while(!tokens.empty()){
            int i = tokens.pop();
            Node node = traceMap.get(i);
            TraceNode child = new TraceNode(root, String.valueOf(i));
            System.out.println(i);
            root.addChild(child);

            current = child;
            Node tempNodeOfCurrent = nodeOfCurrent;
            nodeOfCurrent = node;

            //call recursive method parseChildren to expand the tree for the child node
            processChildren();

            current = child.getParent();
            nodeOfCurrent = tempNodeOfCurrent;
        }
    }

    public void processChildren(){

        while(!tokens.empty()){
            int i = tokens.peek();
            Node node = traceMap.get(i);

            if(node.getRange().isPresent() && nodeOfCurrent.getRange().isPresent()){
                Range range = node.getRange().get();
                if (nodeOfCurrent.getRange().get().strictlyContains(range)){
                    tokens.pop();
                    System.out.println("TRUE");
                    TraceNode child = new TraceNode(current, String.valueOf(i));
                    System.out.println(i);
                    current.addChild(child);
                    current = child;
                    Node tempNodeOfCurrent = nodeOfCurrent;
                    nodeOfCurrent = node;

                    processChildren();

                    current = child.getParent();
                    nodeOfCurrent = tempNodeOfCurrent;
                } else{
                    System.out.println("FALSE");
                    break;
                }
            }
        }
    }
}

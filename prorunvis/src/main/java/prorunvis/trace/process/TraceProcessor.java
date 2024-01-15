package prorunvis.trace.process;

import com.github.javaparser.ast.Node;
import prorunvis.trace.TraceNode;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class TraceProcessor {

    private final List<TraceNode> nodeList;
    private final Map<Integer, Node> traceMap;
    private TraceNode current;
    private final Scanner scanner;
    private Stack<Integer> tokens;

    public TraceProcessor(Map<Integer, Node> trace, String traceFilePath){
        this.nodeList = new LinkedList<>();
        this.traceMap = trace;
        this.scanner = new Scanner(traceFilePath);
    }

    public void start(){
        TraceNode root = new TraceNode(null);
        nodeList.add(root);
        this.current = root;
    }

    
}

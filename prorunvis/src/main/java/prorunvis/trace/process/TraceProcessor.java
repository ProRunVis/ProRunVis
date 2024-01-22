package prorunvis.trace.process;

import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.nodeTypes.NodeWithBody;
import com.github.javaparser.ast.nodeTypes.NodeWithOptionalBlockStmt;
import com.github.javaparser.ast.stmt.*;
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
    private List<Range> methodCallRanges;

    public TraceProcessor(Map<Integer, Node> trace, String traceFilePath){
        this.nodeList = new LinkedList<>();
        this.traceMap = trace;
        this.scanner = new Scanner(traceFilePath);
        this.methodCallRanges = new ArrayList<>();
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

        if(node instanceof MethodDeclaration){
            return !createMethodCallTraceNode();
        }else{

            Optional<Range> range = node.getRange();
            Optional<Range> currentRange = nodeOfCurrent.getRange();

            if(range.isPresent() && currentRange.isPresent()){
                if(currentRange.get().strictlyContains(range.get())){
                    System.out.println("RANGECHECK::TRUE"+currentRange.get()+";"+range.get());
                    createNewTraceNode();
                    return false;
                }
            }
        }

        return true;
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
        //save state
        current = traceNode;
        Node tempNodeOfCurrent = nodeOfCurrent;
        nodeOfCurrent = traceMap.get(tokenValue);
        List<Range> tempRanges = methodCallRanges;
        methodCallRanges = new ArrayList<>();

        while(!tokens.empty() && !isFinished) {

            //process the children of this node recursively
            isFinished = processChild();
        }

        //restore state
        current = nodeList.get(traceNode.getParentIndex());
        nodeOfCurrent = tempNodeOfCurrent;
        methodCallRanges = tempRanges;
    }


    private boolean createMethodCallTraceNode(){
        MethodDeclaration node = (MethodDeclaration)traceMap.get(tokens.peek());
        SimpleName nameOfDeclaration = node.getName();
        SimpleName nameOfCall = null;
        MethodCallExpr callExpr = null;
        BlockStmt block = null;

        //check if call is within a method
        if(nodeOfCurrent instanceof NodeWithOptionalBlockStmt<?> x){
            if(x.getBody().isPresent()) {
                block = x.getBody().get();

            }
        }

        //check if call is in a statement, i.e. a then -or else clause
        if(nodeOfCurrent instanceof Statement y){
            if(y instanceof BlockStmt b){
                block = b;
            }
        }

        //check if call is in a loop
        if(nodeOfCurrent instanceof NodeWithBody<?> loop){
            System.out.println("LOOP::TRUE");
            Statement body = loop.getBody();
            if(body instanceof BlockStmt z){
                block = z;
            }
        }

        //search a found body for expression statements with callExpressions
        if(block != null){
            for(Statement statement: block.getStatements()){

                if(statement instanceof ExpressionStmt expressionStmt){
                    Expression expression = expressionStmt.getExpression();

                    //store the name of the found expression
                    if(expression instanceof MethodCallExpr call){
                        if(!methodCallRanges.contains(call.getRange().get())) {
                            callExpr = call;
                            nameOfCall = call.getName();
                            break;
                        }
                    }
                }
            }
        }

        //if a name hast been found, check if it fits the declaration
        if(nameOfCall != null
        && nameOfCall.equals(nameOfDeclaration)){
            methodCallRanges.add(callExpr.getRange().get());
            System.out.println(callExpr.getRange()+", "+nameOfCall);
            createNewTraceNode();

            //set link, outlink and index of out
            int lastAddedIndex = current.getChildrenIndices().get(current.getChildrenIndices().size()-1);
            TraceNode lastAdded = nodeList.get(lastAddedIndex);
            lastAdded.setLink(nameOfCall.getRange().get());
            lastAdded.setOutLink(nameOfDeclaration.getRange().get());
            lastAdded.setOut(lastAdded.getParentIndex());

            return true;
        }

        return false;
    }

    public List<TraceNode> getNodeList(){
        return this.nodeList;
    }
}

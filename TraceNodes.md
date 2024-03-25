## TraceNodes
In order to simplify understanding and possibly enhancing the tool, here is an explanation on how the input code is stored in the backend. 

To quickly summarize the first steps: the input code is parsed into an AST and altered in order to log information about itself while being executed. The process works mostly the same for different kinds of code and is easily applicable to most not-yet-traced codeblocks. This information is then used to create the central building blocks of the process: TraceNodes.

A TraceNode is designed to represent a certain block of code (an if, a loop, a method etc.) by being mapped to its corresponding AST-node through its traceId. It also stores metadata about it’s position in the program run as well as its relation to other tracenodes. The final output of the backend is simply a json representation of a list of tracenodes, beginning with the "root" tracenode which has no fields set except one child which is the first codeblock in the input program to be executed (typically the main method). A TraceNode consists of the following fields: 


__String _**traceId**___ <br>
A String used for multiple purposes. For one, each codeblock that is set up to be traced is assigned an individual ID consisting if an incrementing number which it logs when being executed. That ID is then adopted by the tracenode created for that codeblock and can be used to connect the tracenode to its corresponding AST-node in the tree. 

__List\<Range\> _**ranges**___ <br>
A List of Javaparser.Range objects. These describe the range between two points, each consisting of row and column. Here they are used to denote which lines of code in the AST-node this tracenode represents are not traced. These are always assumed to have been exectued. If a tracenode doesn't contain any untraced code, this list is empty.

__List\<Integer\> _**childrenIndices**___ <br>
A List of Integers which are used as indices in the final list of tracenodes to point to the tracenodes which are contained in this tracenode. It is important to note that only the indices of direct children are saved. The childs children are saved in that childs childrenIndices-list. parentIndex: The index of the direct parent in the list of tracenodes. For a tracenode without children, this list is empty.

__JumpLink _**link**___ <br>
A link is used to handle cases where code is not executed from top to bottom, but jumps to another part in the file, maybe even another file. link is of type JumpLink, a self-created class which extends Range in order to include a path representation of the file that the jump lands in and the range itself to encapsulate the exact keyword which initiated the jump. A tracenode includes the link which caused the jump to it, so for example a methode-tracenode carries in its link the file it is written in and the range of the methodcall that led to it. Another use of the link is to allow the keyword of a loop (i.e. „for“, „while“ etc) to be used as a jump to it’s individual iterations which are all their own tracenode. For tracenodes without a link, link is set to null.

__List\<JumpLinks\> _**outLinks**___ <br>
A list of JumpLinks, outlinks include the ranges of the keywords that trigger an interuption and subsequent jump back to the caller, as well as the file of the caller. In a method that would be the „return“ keyword and, in order to allow the user to jump back without a return being present, the methodname itself. Throws are also outlinks of their respective parent node, and point to the file containing the fitting catch-statement (throws are not made into an outlink if they terminate the program). For tracenodes without outlinks, the list is empty.

__int _**outIndex**___ <br>
An Integer representing the index of the tracenode that the outlink of a tracenode jumps to. The outIndex is initialized as 0. 

__Integer _**iterations**___ <br>
An integer specific to tracenodes representing loops. As each iteration is its own tracenode, the matching traceId's and the iteration counter are used to keep track which iteration of a loop the current tracenode represents. For every other type of tracenode, iteration is set to null.
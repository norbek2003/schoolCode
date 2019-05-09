import java.util.*;
public class FibonacciHeap<E>{
    private Tree<E> head;
    private Tree<E> min;
    private Comparator<E> comp;
    private int size = 0;
    private HashMap<E, Tree<E>> locations = new HashMap<E, Tree<E>>();
    public FibonacciHeap(){
	this(null);
    }
    public FibonacciHeap(Comparator<E> comp){
	head = new Tree<E>(null, null, null);
	head.setNext(head);
	head.setPrevious(head);
	min = head;
	if(comp != null){
	    this.comp = comp;
	}else{
	    class MyComparator implements Comparator<E> {
		public int compare(E a, E b) {
		    return ((Comparable) a).compareTo(b);
		}
	    };
	    this.comp = new MyComparator();
	}
    }
    public int size(){
	return size;
    }
    public E min(){
	return min.root();
    }
    public void insert(Tree<E> node){
	node.setNext(head.getNext());
	node.setPrevious(head);
	head.getNext().setPrevious(node);
	head.setNext(node);
	if(comp.compare(node.root(), min()) < 0){
	    min  = node;
	}
	size++;
    }
    public void insert(E val){
	if(size == 0){
	    head.setRoot(val);
	    locations.put(val, head);
	    size++;
	}else{
	    Tree<E> t = new Tree<E>(val);
	    locations.put(val, t);
	    insert(t);
	}
    }
    public void add(E val){
	insert(val);
	size++;
    }
    public Tree<E> merge(Tree<E> a, Tree<E> b){ //returns the new parent
	if(compareNodes(a, b) > 0){
	    b.addChild(remove(a));
	    return b;
	}
	else{
	    a.addChild(remove(b));
	    return a;
	}
    }
    public Tree<E> remove(Tree<E> node){
	Tree<E> next = node.getNext();
	Tree<E> last = node.getPrevious();
	next.setPrevious(last);
	last.setNext(next);
	if(node == head) head = node.getPrevious();
	return node;
    }
    public E deleteMin(){
	//Move min's children to list
	for(Tree<E> child : min.children()){
	    insert(child);
	}
	//Remove min
	Tree<E> m = min;
	remove(min);
	///Update min
	Tree<E> node = head.getNext();
	if(compareNodes(head, min) < 0) min = head;
	while(node != head){
	    if(compareNodes(node, min) < 0) min = node;
	    node = node.getNext();
	}
	//Consolidate trees
	HashMap<Integer, Tree<E>> ranks = new HashMap<Integer, Tree<E>>();
	node = head.getNext();
	ranks.put(head.treeRank(), head);
	while(node != head){
	    Tree<E> nextNode = node.getNext();
	    Tree<E> compNode = ranks.get(node.treeRank());
	    //System.out.println("\n\n\n\n");
	    //System.out.println("head, node, next : " + head + " " + node + " " + nextNode);
	    //System.out.println(ranks);
	    //print();
			
			
	    if(compNode != null && compNode != node){
		ranks.remove(compNode.treeRank());
		Tree<E> newNode = merge(compNode, node);
		if(newNode == compNode){
		    remove(node);
		}else{
		    remove(compNode);
		}
		if(compNode == head || node == head) head = newNode;
		//node = nextNode;//newNode;
		if(ranks.get(newNode.treeRank()) == null){
		    ranks.put(newNode.treeRank(),newNode);
		    node = nextNode;
		}else{
		    //node = newNode;
		}
	    }else{
		ranks.put(node.treeRank(), node);
		node = nextNode;
	    }
	    //print();
	    //System.out.println("head, node, next : " + head + " " + node + " " + nextNode);

			
			
	}
	size--;
	return m.root();
    }
    public int compareNodes(Tree<E> a, Tree<E> b){
	return comp.compare(a.root(), b.root());
    }
    public void decreaseKey(Tree<E> tree, E val){
	locations.remove(tree.root());
	tree.setRoot(val);
	if(tree.parent() == null){
	    Tree<E> node = head.getNext();
	    if(compareNodes(head, min) < 0) min = head;
	    while(node != head){
		if(compareNodes(node, min) < 0) min = node;
		node = node.getNext();
	    }
	}else{
	    if(comp.compare(val, tree.parent().root()) < 0){
		cut(tree);
	    }
	}
	locations.put(val, tree);
    }
    public void decreaseKey(E val, E newVal){
	decreaseKey(locations.get(val), newVal);
    }
    public void cut(Tree<E> tree){
	Tree<E> parent = tree.parent();
	parent.children().remove(tree);
	tree.unmark();
	insert(tree);
	tree.setParent(null);
	if(!parent.marked() && parent.parent() != null){
	    parent.mark();
	}else{
	    cut(parent);
	}
    }
    public void print(){
	System.out.println("--------------");
	Tree<E> node = head.getNext();
	System.out.println(head);
	while(node != head){
	    System.out.println(node);
	    node = node.getNext();
	}
	System.out.println("--------------");
    }
    public void display(){
	String[] graph = new String[head.treeRank()];
	for(int i = 0; i < graph.length; i++) graph[i] = "";
	displayHelper(head, graph, 0, 0);
	for(String s : graph){
	    System.out.println(s);
	}	
    }
    private int displayHelper(Tree<E> tree, String[] graph, int depth, int parentPos){
	int pos = 0;
	if(tree.treeRank() > 1){
	    for(Tree<E> child : tree.children()){
		pos = displayHelper(child, graph, depth + 1, 0);
	    }
	}
	//	if(graph[depth].length < parentPos){
	//  for(int i = graph[depth].length; i < parentPos; i++){
	//		graph[depth] += "  ";
	//  }
	//}
	int startLen = graph[depth].length;
	graph[depth] += tree.root() + "-";
	return startLen;
	
    }
    public static void main(String[] args){
	FibonacciHeap<Integer> fib = new FibonacciHeap<Integer>();
	for(int i = 0; i < 10; i ++){
	    fib.add(i);
	}
	//fib.print();
	//System.out.println(fib.min());
	System.out.println(fib.deleteMin());
	fib.print();
	fib.display();
    }
}
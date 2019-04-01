import java.util.NoSuchElementException;
public class ArrayQueue<E> implements MyQueue<E>{
    private int size = 0;
    private final int CAPACITY;
    private E[] queue;
    private int head = 0;
    private int tail = 0;
    public ArrayQueue(int size){
	CAPACITY = size;
	queue = (E[]) new Object[size + 1];
    }
    public int size(){
	return size;
    }
    public boolean isEmpty(){
	return (queue[head] == null);
    }
    public boolean offer(E val){
	if(size == CAPACITY) return false;
	queue[tail] = val;
	tail++;
	tail %= CAPACITY;
	size++;
	return true;
    }
    public boolean add(E val) throws IllegalStateException{
	if(offer(val)){
	    return true;
	}else{
	    throw new IllegalStateException();
	}
    }
    public E peek(){
	return queue[head];
    }
    public E poll(){
	E val = queue[head];
	queue[head] = null;
	head++;
	head %= CAPACITY;
	size--;
	return val;
    }
    public E remove() throws NoSuchElementException{
	if(isEmpty()) throw new NoSuchElementException();
	return poll();
    }
    public String toString(){
	String s = "[ ";
	for(int i = head; i < size; i++){
	    s += queue[i % CAPACITY] + ", ";
	}
	s += "]";
	return s.replace(", ]", " ]");
    }
    public static void main(String[] args){
	ArrayQueue<String> q = new ArrayQueue<String>(5);
	q.offer("A");
	System.out.println(q);
	q.offer("B");
	System.out.println(q);
	q.offer("C");
	System.out.println(q);
	q.offer("D");
	System.out.println(q);
	q.offer("E");
	System.out.println(q);
	q.offer("F");
	System.out.println(q);
	System.out.println(q.poll());
	System.out.println(q);
    }
}

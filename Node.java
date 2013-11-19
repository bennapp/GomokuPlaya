import java.util.Comparator;
import java.util.PriorityQueue;
public class Node{
	public int n;
	public int m;
	public char[][] board;
	public Node parent;
	public int value;
	public int[] move;
	public PriorityQueue<Node> children = new PriorityQueue<Node>(19*19, new NodeValueComparator());

	//list of children?

	public Node(int n, int m, char[][] board, Node parent){
		this.n = n;
		this.m = m;
		this.board = board;
		this.parent = parent;
		this.value = -1;
	}
	public Node(int n, int m, String board, Node parent){
		this.n = n;
		this.m = m;
		this.parent = parent;		
	}


		public static void main(String[] args){ //blow this away before comp
	}
}
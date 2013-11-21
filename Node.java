import java.util.Comparator;
import java.util.PriorityQueue;
public class Node{
	public int n;
	public int m;
	public char[][] board;
	public int value;
	public int[] move;
	public PriorityQueue<int[]> children = null;

	public Node(int n, int m, char[][] board, boolean max){
		this.n = n;
		this.m = m;
		this.board = board;
		this.children = max ? new PriorityQueue<int[]>(19*19, new IntValueMaxComparator()) : new PriorityQueue<int[]>(19*19, new IntValueMinComparator());
	}
}
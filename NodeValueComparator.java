import java.util.Comparator;
public class NodeValueComparator implements Comparator<Node>{
    @Override
    public int compare(Node x, Node y){
    	return x[0] < y[0] ? 1 : x[0] > y[0] ? -1 : 0; //this is backwards because I want the biggest first
    }
}
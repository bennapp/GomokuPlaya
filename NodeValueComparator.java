import java.util.Comparator;
public class NodeValueComparator implements Comparator<Node>{
    @Override
    public int compare(Node x, Node y){
    	int xValue = x.value;
    	int yValue = y.value;
    	return xValue < yValue ? 1 : xValue > yValue ? -1 : 0; //this is backwards because I want the biggest first
    }
}
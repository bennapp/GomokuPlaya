import java.util.Comparator;
public class IntValueComparator implements Comparator<int[]>{
    @Override
    public int compare(int[] x, int[] y){
    	return x[0] < y[0] ? -1 : x[0] > y[0] ? 1 : 0; //this is backwards because I want the biggest first
    }
}
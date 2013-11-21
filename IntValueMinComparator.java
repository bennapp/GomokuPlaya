import java.util.Comparator;
public class IntValueMinComparator implements Comparator<int[]>{
    @Override
    public int compare(int[] x, int[] y){
    	return x[0] < y[0] ? -1 : x[0] > y[0] ? 1 : 0;
    }
}
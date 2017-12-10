package maksim.lisau.rabobankattempt2.graphs;
import java.util.ArrayList;
import java.util.Arrays;
/**
 * Created by Maks on 07-Oct-17.
 */
//Used as an input for graphs. Data is fed through these into Processing.
public class GraphStream{
    String name;
    public ArrayList<Node> stream = new ArrayList();
    int maxindex = 0;
    public GraphStream(String originname,Node[] data){
        name = originname;
        stream = new ArrayList(Arrays.asList(data));
    }
    public Node get(int index){
        if(stream.isEmpty()){
            return null;

        }
        int low=0,high = stream.size()-1,mid = (low+high)/2;
        //println(low,high,mid);
        while(stream.get(mid).index!=index){
            //println(low,mid,high);
            if(low==high||high<0){
                return null;
            }
            if(stream.get(mid).index>index){
                high = mid-1;
            }else{
                low = mid+1;
            }
            mid = (low+high)/2;

        }
        //println(low,high,mid);
        return stream.get(mid);
    }
    public void insert(Node n){
        if(stream.isEmpty()){
            stream.add(n);
        }else{
            for(int i = stream.size()-1;i>=0;i--){
                if(stream.get(i).index<n.index){
                    stream.add(i+1,n);
                    break;
                }
            }
        }
        if(n.index>maxindex){
            maxindex = n.index;
        }

    }
}

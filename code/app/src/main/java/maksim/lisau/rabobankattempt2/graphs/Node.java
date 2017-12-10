package maksim.lisau.rabobankattempt2.graphs;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Maks on 07-Oct-17.
 */
public class Node<T extends Number> implements Comparable{
    ArrayList<T> value = new ArrayList();
    int index;
    public Node(T[] value, int index){
        this.value = new ArrayList(Arrays.asList(value));
        this.index = index;
    }

    public Node(T value, int index){
        this.value.add(value);
        this.index = index;
    }
    public Node(T value,T othervalue, int index){
        this.value.add(value);
        this.value.add(othervalue);
        this.index = index;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public int compareTo(Object o){
        Node f = (Node)o;
        return Integer.compare(f.index,index);
    }

    public String toString(){
        String s = "Node "+index+":[";
        for(T t:value){
            s+=t+",";
        }
        s=s.substring(0,s.length()-1);
        s+="]";
        return s;
    }

}
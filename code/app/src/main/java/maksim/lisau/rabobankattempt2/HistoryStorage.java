package maksim.lisau.rabobankattempt2;

import java.util.ArrayList;

/**
 * Created by Maks on 08-Oct-17.
 */
//This class is used to store recently viewed items in any section.
public class HistoryStorage {
    public static ArrayList<Item> homeRecent=new ArrayList();
    public static Item getRecent(int position) {
        return homeRecent.get(position);
    }
    //This adds to the beginning of the ArrayList
    public static void addRecent(Item item) {
        Item[] items= new Item[homeRecent.size()+1];
        for (int i=0; i<homeRecent.size(); i++) {
            items[i+1]=homeRecent.get(i);
        }
        items[0]=item;
        homeRecent.clear();
        for (int i=0; i<items.length; i++) {
            homeRecent.add(items[i]);
        }
    }
    public static void removeRecent(int position) {
        homeRecent.remove(position);
    }


}

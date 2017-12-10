package maksim.lisau.rabobankattempt2.database;

/**
 * Created by Maks on 07-Oct-17.
 */

import java.util.ArrayList;

/**
 *
 * @author Maksi
 */
public class Branch {
    public String name;
    public Address address;
    public ArrayList <Long> transactionIDs=new ArrayList<>();
    public String returnName() {
        return name;
    }
}
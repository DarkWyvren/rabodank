package maksim.lisau.rabobankattempt2.database;

import java.util.ArrayList;

/**
 * Created by Maks on 07-Oct-17.
 */
public class Supplier {
    public Address address;
    public String name;
    public String contact;
    public int tempStorage=0;
    public ArrayList<Long> transactionIDs=new ArrayList<>();

    public Address getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getContact() {
        return contact;
    }
}

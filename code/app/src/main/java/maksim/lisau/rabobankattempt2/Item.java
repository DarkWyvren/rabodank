package maksim.lisau.rabobankattempt2;

/**
 * Created by Maks on 08-Oct-17.
 */

public class Item {
    //0=Branch
    //1=Supplier
    //2=Invoice
    public int type;
    //Used as key for branch/supplier
    String name;
    //Used as key for invoice
    long invoiceKey;

    public Item(int type, String name) {
        this.type = type;
        this.name = name;
    }
    public Item( long invoiceKey) {
        this.type = 2;
        this.invoiceKey = invoiceKey;
    }

    public String getName() {
        return name;
    }

    public long getInvoiceKey() {
        return invoiceKey;
    }
}

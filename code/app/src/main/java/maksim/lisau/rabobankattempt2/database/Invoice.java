package maksim.lisau.rabobankattempt2.database;

/**
 * Created by Maks on 07-Oct-17.
 */


import java.util.Date;

/**
 *
 * @author Maksi
 */
public class Invoice {
    long transactionID;
    float cashAmount;
    Date date;
    String suppName;
    String branchName;
    //Do not use unless you manually set everything.
    public Invoice() {

    }
    public Invoice(int cashAmount, Date date, String suppName, String branchName) {
        this.transactionID=(long)(Math.random()*Long.MAX_VALUE);
        this.cashAmount = cashAmount;
        this.date = date;
        this.suppName = suppName;
        this.branchName = branchName;
    }

    public float getCashAmount() {
        return cashAmount;
    }

    public Date getDate() {
        return date;
    }

    public String getSuppName() {
        return suppName;
    }

    public String getBranchName() {
        return branchName;
    }

    public long getTransactionID() {
        return transactionID;
    }
}

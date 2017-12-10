package maksim.lisau.rabobankattempt2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;

import maksim.lisau.rabobankattempt2.HistoryStorage;
import maksim.lisau.rabobankattempt2.Item;
import maksim.lisau.rabobankattempt2.R;
import maksim.lisau.rabobankattempt2.database.Branch;
import maksim.lisau.rabobankattempt2.database.DatabaseHandler;
import maksim.lisau.rabobankattempt2.database.Invoice;
import maksim.lisau.rabobankattempt2.database.Supplier;
import maksim.lisau.rabobankattempt2.graphs.GraphStream;
import maksim.lisau.rabobankattempt2.graphs.GraphType;
import maksim.lisau.rabobankattempt2.graphs.Grapher;
import maksim.lisau.rabobankattempt2.graphs.Node;
import processing.android.PFragment;

import static maksim.lisau.rabobankattempt2.database.DatabaseHandler.invoiceHashMap;

public class ViewSupplier extends AppCompatActivity {

    public String supplierName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_supplier);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle b= getIntent().getExtras();
        supplierName=b.getString("Supplier key");
        TextView name=(TextView) findViewById(R.id.supplierName);
        name.setText(supplierName);
        TextView address=(TextView) findViewById(R.id.supplierAddress);
        address.setText(DatabaseHandler.supplierHashMap.get(supplierName).getAddress().getFormattedAddressFull());
        TextView telephone=(TextView) findViewById(R.id.telephoneNumber);
        if (DatabaseHandler.supplierHashMap.get(supplierName).getContact()!=null) {
            telephone.setText("Contact: " + DatabaseHandler.supplierHashMap.get(supplierName).getContact());
        } else {
            telephone.setText("Contact: (none)");
        }
        TextView spending=(TextView) findViewById(R.id.supplierSpending);
        for (int i=0; i<DatabaseHandler.supplierHashMap.get(supplierName).transactionIDs.size(); i++) {
            //If within the current year
            //if (DatabaseHandler.invoiceHashMap.get(DatabaseHandler.supplierHashMap.get(supplierName).transactionIDs.get(i)).getDate().getYear()> Calendar.getInstance().get(Calendar.YEAR)) {
            DatabaseHandler.supplierHashMap.get(supplierName).tempStorage+=DatabaseHandler.invoiceHashMap.get(DatabaseHandler.supplierHashMap.get(supplierName).transactionIDs.get(i)).getCashAmount();
            //}
        }
        spending.setText("Yearly Spending: "+DatabaseHandler.supplierHashMap.get(supplierName).tempStorage);
        DatabaseHandler.supplierHashMap.get(supplierName).tempStorage=0;
        HistoryStorage.addRecent(new Item(1,supplierName));

        Supplier sup = DatabaseHandler.supplierHashMap.get(supplierName);
        GraphStream graphStream = new GraphStream("transactions",new Node[]{});
        GraphStream graphStream2 = new GraphStream("freq",new Node[]{});
        String ext[] ={"Transaction magnitude","$","year"};
        String ext2[] ={"Transaction frequency","","year"};
        ArrayList<String> externals = new ArrayList(Arrays.asList(ext));
        ArrayList<String> externals2 = new ArrayList(Arrays.asList(ext2));
        ArrayList<Invoice> invoices = new ArrayList<>();
        System.out.println("supplier:"+sup.getName()+" supplier invoice:"+sup.transactionIDs);
        System.out.println("extbefore:"+externals);
        for(long id:sup.transactionIDs){
            invoices.add(invoiceHashMap.get(id));

        }

        invoices.sort(new Comparator<Invoice>() {
            @Override
            public int compare(Invoice invoice, Invoice t1) {
                return (int)(invoice.getDate().getTime()-t1.getDate().getTime());
            }
        });

        int index = 0;
        int freqindex=0;
        int yearupto=0;
        int highyear= 2016;
        int lhighyear= 2016;
        int casham = 0;
        int am =0;
        for(Invoice i:invoices){
            Calendar cal = Calendar.getInstance();
            cal.setTime(i.getDate());
            int year = cal.get(Calendar.YEAR);

            if(year>yearupto){
                if(yearupto!=0) {
                    graphStream.insert(new Node(casham, index));
                    for(int ic = highyear;ic<year;ic++) {
                        graphStream2.insert(new Node(0, freqindex));
                        freqindex++;
                    }
                    graphStream2.insert(new Node(am, freqindex));
                    am = 0;
                    externals.add(yearupto+"");
                    index++;
                    freqindex++;
                    casham=0;

                }
                yearupto=year;
                lhighyear=highyear;
                highyear=year;

            }
            casham+=i.getCashAmount();
            am++;

        }
        System.out.println("extbefore:"+externals);

        graphStream.insert(new Node(casham, index));
        for(int ic = lhighyear;ic<highyear;ic++) {
            graphStream2.insert(new Node(0, freqindex));
            freqindex++;
        }
        graphStream2.insert(new Node(am, freqindex));
        freqindex++;
        graphStream2.insert(new Node(0, freqindex));

        externals.add(yearupto+"");




        System.out.println("extafter2:"+externals2);
        System.out.println("nodesafter2:"+graphStream2.stream);


        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        DisplayMetrics m = getResources().getDisplayMetrics();
        PFragment p = new PFragment(new Grapher(m.density* 160f,m.widthPixels,m.heightPixels/4,
                new GraphStream[]{graphStream}, GraphType.BAR,externals));

        PFragment p2 = new PFragment(new Grapher(m.density* 160f,m.widthPixels,m.heightPixels/4,
                new GraphStream[]{graphStream2}, GraphType.LINE,externals2));
        fragmentTransaction.add(R.id.hullo, p);
        fragmentTransaction.add(R.id.hullo2, p2);
        fragmentTransaction.commit();

    }
    public void gotoHome(View view) {
        Intent intent=new Intent(ViewSupplier.this,Home.class);
        startActivity(intent);
    }
}
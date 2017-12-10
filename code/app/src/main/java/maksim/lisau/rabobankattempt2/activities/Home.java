package maksim.lisau.rabobankattempt2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import maksim.lisau.rabobankattempt2.HistoryStorage;
import maksim.lisau.rabobankattempt2.R;
import maksim.lisau.rabobankattempt2.database.Branch;
import maksim.lisau.rabobankattempt2.database.DatabaseHandler;
import maksim.lisau.rabobankattempt2.database.Supplier;

import static maksim.lisau.rabobankattempt2.database.DatabaseHandler.branchHashMap;
import static maksim.lisau.rabobankattempt2.database.DatabaseHandler.invoiceHashMap;
import static maksim.lisau.rabobankattempt2.database.DatabaseHandler.supplierHashMap;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DatabaseHandler.context=this.getBaseContext();
        try {
            DatabaseHandler.retreiveFromCSV();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ListView listView=(ListView)findViewById(R.id.recentlyViewed);
        ArrayList<String> listItems = new ArrayList<>();
        for (int i=0 ;i< HistoryStorage.homeRecent.size(); i++) {
            if ( HistoryStorage.homeRecent.get(i)!=null) {
                // create the ArrayList to store the titles of nodes
                switch(HistoryStorage.homeRecent.get(i).type) {
                    case 0:
                        //Branch: _________
                        //Address: _______________
                        listItems.add("Branch: "+HistoryStorage.homeRecent.get(i).getName() + "\n"+
                                "Address: "+DatabaseHandler.branchHashMap.get(HistoryStorage.homeRecent.get(i).getName()).address.getFormattedAddressFull());
                        break;
                    case 1:
                        //Supplier: _______
                        //Address: _____________
                        listItems.add("Supplier: "+HistoryStorage.homeRecent.get(i).getName() + "\n"+
                                "Address: "+DatabaseHandler.supplierHashMap.get(HistoryStorage.homeRecent.get(i).getName()).getAddress().getFormattedAddressFull());
                        break;
                    case 2:
                        //Invoice amount: _____
                        //Supplier: _____
                        //Branch: ______
                        //Date: ______
                        listItems.add("Invoice amount: "+ invoiceHashMap.get(HistoryStorage.homeRecent.get(i).getInvoiceKey()).getCashAmount() + "\n"+
                                "Supplier: "+ invoiceHashMap.get(HistoryStorage.homeRecent.get(i).getInvoiceKey()).getSuppName()+"\n"+
                                "Branch: "+ invoiceHashMap.get(HistoryStorage.homeRecent.get(i).getInvoiceKey()).getBranchName()+"\n"+
                                "Date:" + invoiceHashMap.get(HistoryStorage.homeRecent.get(i).getInvoiceKey()).getDate());
                        break;
                    default:
                        System.out.println("Why");
                        break;
                }
            }
        }
        ArrayAdapter ad = new ArrayAdapter(this.getBaseContext(),android.R.layout.simple_list_item_1, listItems);

        // give adapter to ListView UI element to render
        listView.setAdapter(ad);
        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle=new Bundle();
                TextView textView=(TextView) view;
                switch(textView.getText().charAt(0)) {
                    case 'B':
                        Branch[] branches = new Branch[branchHashMap.size()];
                        branchHashMap.values().toArray(branches);
                        StringTokenizer stringTokenizer=new StringTokenizer(((TextView) view).getText().toString());
                        stringTokenizer.nextToken(": ");
                        String key= stringTokenizer.nextToken("\n");
                        key=key.substring(2,key.length());
                        System.out.println("Branch key: "+key);
                        bundle.putString("Branch key", key);
                        Intent intent = new Intent(Home.this, ViewBranch.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                    case 'S':
                        Supplier[] suppliers= new Supplier[supplierHashMap.size()];
                        supplierHashMap.values().toArray(suppliers);
                        stringTokenizer=new StringTokenizer(((TextView) view).getText().toString());
                        stringTokenizer.nextToken(": ");
                        String key2=stringTokenizer.nextToken("\n");
                        key2=key2.substring(2);
                        bundle.putString("Supplier key", key2);
                        Intent intent2 = new Intent(Home.this, ViewSupplier.class);
                        intent2.putExtras(bundle);
                        startActivity(intent2);
                        break;
                    /*case 'I':
                        Invoice[] invoices= new Invoice[invoiceHashMap.size()];
                        invoiceHashMap.values().toArray(invoices);
                        bundle.putLong("Invoice key", DatabaseHandler.invoiceHashMap.get((TextView) view).getText().toString());
                        Intent intent3 = new Intent(Home.this, ViewInvoice.class);
                        intent3.putExtras(bundle);
                        startActivity(intent3);
                        break;*/
                }
            }
        });

        System.out.println("History: "+ HistoryStorage.homeRecent.size());
    }
    public void gotoViewData(View view)
    {
        Bundle b=new Bundle();
        b.putString("Type of Search","View");
        Intent intent = new Intent(Home.this, ViewData.class);
        intent.putExtras(b);
        startActivity(intent);
    }
    public void gotoInputData(View view) {
        Bundle b=new Bundle();
        b.putString("Type of Search","Edit");
        Intent intent = new Intent(Home.this, InputData.class);
        intent.putExtras(b);
        startActivity(intent);
    }




}
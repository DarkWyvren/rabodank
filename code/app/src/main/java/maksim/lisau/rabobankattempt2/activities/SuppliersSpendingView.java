package maksim.lisau.rabobankattempt2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.StringTokenizer;

import maksim.lisau.rabobankattempt2.R;
import maksim.lisau.rabobankattempt2.database.DatabaseHandler;
import maksim.lisau.rabobankattempt2.database.Supplier;

import static maksim.lisau.rabobankattempt2.database.DatabaseHandler.loading;



public class SuppliersSpendingView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suppliers_spending_view);
        //Not sorted by country by default.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ListView listView = (ListView) findViewById(R.id.suppliersBySpending);
        Supplier[] suppliers = new Supplier[DatabaseHandler.supplierHashMap.size()];
        DatabaseHandler.supplierHashMap.values().toArray(suppliers);
        Comparator<Supplier> c = new Comparator<Supplier>() {
            @Override
            public int compare(Supplier supplier1, Supplier supplier2) {
                return supplier2.tempStorage - supplier1.tempStorage;
            }
        };
        for (int i = 0; i < suppliers.length; i++) {
            for (int j = 0; j < suppliers[i].transactionIDs.size(); j++) {
                //Sums up all the cash transactions
                suppliers[i].tempStorage += DatabaseHandler.invoiceHashMap.get(suppliers[i].transactionIDs.get(j)).getCashAmount();
            }
        }
        Arrays.sort(suppliers, c);
        ArrayList<String> listItems = new ArrayList<String>();
        for (int i = 0; i < suppliers.length; i++) {
            listItems.add(suppliers[i].name + ", Country: " + suppliers[i].getAddress().country + ", Spending: " + suppliers[i].tempStorage);
            suppliers[i].tempStorage = 0;
        }
        ArrayAdapter ad = new ArrayAdapter(this.getBaseContext(), android.R.layout.simple_list_item_1, listItems);
        listView.setAdapter(ad);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String supplier;
                TextView textView1 = (TextView) view;
                supplier = textView1.getText().toString();
                StringTokenizer st = new StringTokenizer(supplier);
                supplier = st.nextToken(",");
                Intent intent = new Intent(SuppliersSpendingView.this, ViewSupplier.class);
                Bundle b = new Bundle();
                b.putString("Supplier key", supplier);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        Spinner spinner = (Spinner) findViewById(R.id.selectCountry);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    refresh("");
                } else if (i==1){
                    refresh("Australia");
                } else {
                    refresh("New Zealand");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });
    }
    public void gotoHome(View view) {
        Intent intent=new Intent(SuppliersSpendingView.this,Home.class);
        startActivity(intent);
    }
    public void refresh(String s) {
        TextView textView=(TextView)findViewById(R.id.textView10);
        textView.setText("Select country" + (loading ? " (still loading):" : ":"));
        ListView listView = (ListView) findViewById(R.id.suppliersBySpending);
        Supplier[] suppliers = new Supplier[DatabaseHandler.supplierHashMap.size()];
        DatabaseHandler.supplierHashMap.values().toArray(suppliers);
        Comparator<Supplier> c = new Comparator<Supplier>() {
            @Override
            public int compare(Supplier supplier1, Supplier supplier2) {
                return supplier2.tempStorage - supplier1.tempStorage;
            }
        };
        for (int i = 0; i < suppliers.length; i++) {
            for (int j = 0; j < suppliers[i].transactionIDs.size(); j++) {
                //Sums up all the cash transactions
                if (DatabaseHandler.invoiceHashMap.get(suppliers[i].transactionIDs.get(j)).getDate().after(new Date(1,1,new Date().getYear()))) {
                    suppliers[i].tempStorage += DatabaseHandler.invoiceHashMap.get(suppliers[i].transactionIDs.get(j)).getCashAmount();
                }
            }
        }
        Arrays.sort(suppliers, c);
        ArrayList<String> listItems = new ArrayList<String>();
        for (int i = 0; i < suppliers.length; i++) {
            if (suppliers[i].getAddress().country!=null&&suppliers[i].getAddress().country.contains(s)) {
                listItems.add(suppliers[i].name + ", Country: " + suppliers[i].getAddress().country + ", Spending: " + suppliers[i].tempStorage);
            }
            suppliers[i].tempStorage = 0;
        }
        ArrayAdapter ad = new ArrayAdapter(this.getBaseContext(), android.R.layout.simple_list_item_1, listItems);
        listView.setAdapter(ad);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String supplier;
                TextView textView1 = (TextView) view;
                supplier = textView1.getText().toString();
                StringTokenizer st = new StringTokenizer(supplier);
                supplier = st.nextToken(",");
                Intent intent = new Intent(SuppliersSpendingView.this, ViewSupplier.class);
                Bundle b = new Bundle();
                b.putString("Supplier key", supplier);
                intent.putExtras(b);
                startActivity(intent);
            }
        });

    }
}

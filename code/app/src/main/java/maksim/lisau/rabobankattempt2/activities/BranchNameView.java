package maksim.lisau.rabobankattempt2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import maksim.lisau.rabobankattempt2.R;
import maksim.lisau.rabobankattempt2.database.Branch;
import maksim.lisau.rabobankattempt2.database.Supplier;

import static maksim.lisau.rabobankattempt2.database.DatabaseHandler.branchHashMap;
import static maksim.lisau.rabobankattempt2.database.DatabaseHandler.supplierHashMap;

public class BranchNameView extends AppCompatActivity {
    int linkType=-1;
        int searchType=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_name_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        searchType=getIntent().getExtras().getInt("Search type");
        linkType=1;
    }
    public void searchBranch(View view) {
        if (searchType==1) {
            //DatabaseHandler.retreiveFromCSV("");
            ListView listView = (ListView) findViewById(R.id.searchResults);
            Branch[] branches = new Branch[branchHashMap.size()];
            branchHashMap.values().toArray(branches);
            ArrayList<String> listItems = new ArrayList<>();
            for (int i = 0; i < branches.length; i++) {
                EditText content = (EditText) findViewById(R.id.branchSelection);
                String s = content.getText().toString();
                System.out.println(branches.length);
                if (branches[i] != null && branches[i].returnName().contains(s)) {
                    // create the ArrayList to store the titles of nodes
                    listItems.add(branches[i].returnName());
                }
            }
            ArrayAdapter ad = new ArrayAdapter(this.getBaseContext(), android.R.layout.simple_list_item_1, listItems);

            // give adapter to ListView UI element to render
            listView.setAdapter(ad);
            listView.setOnItemClickListener(new ListView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Bundle bundle = new Bundle();
                    TextView textView = (TextView) view;
                    bundle.putString("Branch key", "" + textView.getText());
                    bundle.putInt("View type", linkType);
                    Intent intent = new Intent(BranchNameView.this, ViewBranch.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        } else {
            //Search for supplier
            ListView listView = (ListView) findViewById(R.id.searchResults);
            Supplier[] branches = new Supplier[supplierHashMap.size()];
            supplierHashMap.values().toArray(branches);
            ArrayList<String> listItems = new ArrayList<>();
            for (int i = 0; i < branches.length; i++) {
                EditText content = (EditText) findViewById(R.id.branchSelection);
                String s = content.getText().toString();
                System.out.println(branches.length);
                if (branches[i] != null && branches[i].getName().contains(s)) {
                    // create the ArrayList to store the titles of nodes
                    listItems.add(branches[i].getName());
                }
            }
            ArrayAdapter ad = new ArrayAdapter(this.getBaseContext(), android.R.layout.simple_list_item_1, listItems);

            // give adapter to ListView UI element to render
            listView.setAdapter(ad);
            listView.setOnItemClickListener(new ListView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Bundle bundle = new Bundle();
                    TextView textView = (TextView) view;
                    bundle.putString("Supplier key", "" + textView.getText());
                    bundle.putInt("View type", linkType);
                    Intent intent = new Intent(BranchNameView.this, ViewSupplier.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }
    }
}
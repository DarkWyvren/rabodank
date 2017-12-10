package maksim.lisau.rabobankattempt2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import maksim.lisau.rabobankattempt2.R;

public class ViewData extends AppCompatActivity {
    //View or edit? View=1, Edit=0.
    int typeOfSearch=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView=toolbar.findViewById(R.id.title);

        if (getIntent().getExtras()!=null) {
            System.out.println("Yes");
            if (getIntent().getExtras().getString("Type of Search") == "View") {
                typeOfSearch = 1;
                textView.setText("View Data");
            } else {
                typeOfSearch = 0;
                textView.setText("Edit Data");
            }
        } else{
            System.out.println("No 😞");
            typeOfSearch = 1;
            textView.setText("View Data");
        }
    }
    public void gotoHome(View view)
    {
        Intent intent = new Intent(ViewData.this, Home.class);
        startActivity(intent);
    }
    public void gotoBranchNameView(View view) {
        Bundle b=new Bundle();
        b.putInt("View type",typeOfSearch);
        b.putInt("Search type",1);
        Intent intent = new Intent(ViewData.this, BranchNameView.class);
        intent.putExtras(b);
        startActivity(intent);
    }
    public void gotoSupplierNameView(View view) {
        Bundle b=new Bundle();
        b.putInt("View type",typeOfSearch);
        b.putInt("Search type",2);
        Intent intent=new Intent(ViewData.this, BranchNameView.class);
        intent.putExtras(b);
        startActivity(intent);
    }
    public void gotoSpending(View view) {
        Intent intent=new Intent(ViewData.this, SuppliersSpendingView.class);
        startActivity(intent);
    }
    public void gotoMapView(View view) {
        Intent intent= new Intent(ViewData.this, Maps_Search.class);
        startActivity(intent);
    }

}
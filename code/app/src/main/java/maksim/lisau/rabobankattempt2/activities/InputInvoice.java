package maksim.lisau.rabobankattempt2.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import maksim.lisau.rabobankattempt2.R;
import maksim.lisau.rabobankattempt2.database.Branch;
import maksim.lisau.rabobankattempt2.database.DatabaseHandler;
import maksim.lisau.rabobankattempt2.database.Invoice;
import maksim.lisau.rabobankattempt2.database.Supplier;

public class InputInvoice extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_invoice);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Spinner spinner1=(Spinner)findViewById(R.id.spinner);
        Spinner spinner2=(Spinner)findViewById(R.id.spinner2);
        String branchArray[]=new String[DatabaseHandler.branchHashMap.values().toArray().length];
        for (int i=0; i<DatabaseHandler.branchHashMap.values().toArray().length; i++) {
            branchArray[i]=((Branch)DatabaseHandler.branchHashMap.values().toArray()[i]).name;
        }

        ArrayAdapter<String> branchAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,branchArray);
        spinner1.setAdapter(branchAdapter);
        //Repeat for suppliers
        String supplierArray[]=new String[DatabaseHandler.supplierHashMap.values().toArray().length];
        for (int i=0; i<DatabaseHandler.supplierHashMap.values().toArray().length; i++) {
            supplierArray[i]=((Supplier)DatabaseHandler.supplierHashMap.values().toArray()[i]).getName();
        }

        ArrayAdapter<String> supplierAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,supplierArray);
        spinner1.setAdapter(supplierAdapter);
    }
    public void addInvoice(View view) {

        Spinner spinner1=(Spinner)findViewById(R.id.spinner);
        Spinner spinner2=(Spinner)findViewById(R.id.spinner2);
        EditText editTextInvoiceAmount=(EditText)findViewById(R.id.invoiceAmount);
        EditText editTextInvoiceDate=(EditText)findViewById(R.id.invoiceDate);
        TextView selected1=(TextView) spinner1.getSelectedView();
        TextView selected2=(TextView) spinner2.getSelectedView();
        int invoiceAmount=Integer.parseInt(String.valueOf(editTextInvoiceAmount.getText()));
        String date=editTextInvoiceDate.getText().toString();
        DateFormat df=new SimpleDateFormat("DD-mm-yyyy");
        String suppName=selected1.getText().toString();
        String branchName=selected2.getText().toString();
        Date startDate=null;
        try {
            startDate = df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Invoice invoice=new Invoice(invoiceAmount,startDate,suppName,branchName);
        DatabaseHandler.addInvoice(invoice,suppName,branchName);
        try {
            DatabaseHandler.storeData();
            System.out.println("Save success!");
        } catch (IOException e) {
            System.out.println("Save failed.");
            e.printStackTrace();
        }
    }
}

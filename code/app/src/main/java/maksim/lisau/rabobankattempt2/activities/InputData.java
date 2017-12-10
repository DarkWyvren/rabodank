package maksim.lisau.rabobankattempt2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import maksim.lisau.rabobankattempt2.R;

public class InputData extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }
    public void gotoHome(View view) {
        Intent intent = new Intent(InputData.this, Home.class);
        startActivity(intent);
    }
    public void gotoInputInvoice(View view) {
        Intent intent=new Intent(InputData.this, InputInvoice.class);
        startActivity(intent);
    }

}

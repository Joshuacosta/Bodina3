package com.example.it00046.bodina3;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;

import java.util.Calendar;


public class celebracions_client_alta extends ActionBarActivity {

    public String g_DataCelebracio = new String();
    public Context jo = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ImageButton l_IMB_Data;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.celebracions_client_alta);
        // Recuperem elements de la finestra
        l_IMB_Data = (ImageButton) findViewById(R.id.CelebracionsClientAltaIMBData);
        l_IMB_Data.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                System.out.println("the selected " + mDay);
                DatePickerDialog dialog = new DatePickerDialog(jo,
                        new mDateSetListener(), mYear, mMonth, mDay);
                dialog.show();
            }
        });
        // Control de enrera/cancelacio
        ActionBar l_actionBar = getSupportActionBar();
        l_actionBar.setDisplayHomeAsUpEnabled(true);
        l_actionBar.setHomeAsUpIndicator(R.drawable.ic_close_white_48dp);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.celebracions_client_alta, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class mDateSetListener implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            // getCalender();
            int mYear = year;
            int mMonth = monthOfYear;
            int mDay = dayOfMonth;
            g_DataCelebracio = (mMonth + 1) + "/" + mDay + "/" + mYear;
        }
    }
}

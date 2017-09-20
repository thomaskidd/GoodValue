//Author: Thomas Kidd
//v: 1.0.0
package com.example.thomaskidd.goodvalue;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create Spinners


        //Create currency spinner
        final Spinner currencySpinner = (Spinner) findViewById(R.id.currencySpinner);
        //Array Adapter
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.currency_array, android.R.layout.simple_spinner_item);
        //Specify layout
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_item);
        //Apply the adapter to the spinner
        currencySpinner.setAdapter(adapter1);

        //Create unit spinner
        final Spinner unitSpinner = (Spinner) findViewById(R.id.unitSpinner);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.unit_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_item);
        unitSpinner.setAdapter(adapter2);

        //Create target currency spinner
        final Spinner targetCurrSpinner = (Spinner) findViewById(R.id.targetCurrSpinner);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.currency_array, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_item);
        targetCurrSpinner.setAdapter(adapter3);

        //Create target unit spinner
        final Spinner targetUnitSpinner = (Spinner) findViewById(R.id.targetUnitSpinner);
        final ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this, R.array.unit_array, android.R.layout.simple_spinner_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_item);
        targetUnitSpinner.setAdapter(adapter4);

        //Do not allow users to convert from volume to mass and vice versa
        final ArrayAdapter<CharSequence> volumeAdapter = ArrayAdapter.createFromResource(this, R.array.volume_array, android.R.layout.simple_spinner_item);
        volumeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        final ArrayAdapter<CharSequence> massAdapter = ArrayAdapter.createFromResource(this, R.array.mass_array, android.R.layout.simple_spinner_item);
        massAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        unitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
                String unit = unitSpinner.getSelectedItem().toString();
                if(Forex.volume(unit)) {
                    targetUnitSpinner.setAdapter(volumeAdapter);
                }
                else if(Forex.mass(unit)) {
                    targetUnitSpinner.setAdapter(massAdapter);
                }
                else {
                    targetUnitSpinner.setAdapter(adapter4);
                }

            }
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Create input fields


        //Create price editText
        final EditText currencyQuant = (EditText) findViewById(R.id.currencyQuant);

        //Create unit quantity editText
        final EditText unitQuant = (EditText) findViewById(R.id.unitQuant);

        //Create and load smart banner ad
        final AdView smartBanner = (AdView) findViewById(R.id.smartBanner);
        AdRequest adRequest = new AdRequest.Builder().build();
        smartBanner.loadAd(adRequest);

        //Create convert button
        final Button convert = (Button) findViewById(R.id.convert);
        convert.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Wrapper params = new Wrapper();
                params.currencyName = currencySpinner.getSelectedItem().toString();
                params.unitName = unitSpinner.getSelectedItem().toString();
                params.targetCurrencyName = targetCurrSpinner.getSelectedItem().toString();
                params.targetUnitName = targetUnitSpinner.getSelectedItem().toString();
                params.price = Double.parseDouble(currencyQuant.getText().toString());
                params.quantity = Double.parseDouble(unitQuant.getText().toString());
                try {
                    updatePriceView(new Convert().execute(params).get());
                    AdRequest newAd = new AdRequest.Builder().build();
                    smartBanner.loadAd(newAd);
                }
                catch (Exception e) {
                    updatePriceView("Error.");
                }
            }
        });
    }

    //Update text to display current price
    public void updatePriceView(String toThis) {
        TextView priceView = (TextView) findViewById(R.id.priceView);
        priceView.setText(toThis);
    }

    //Create Action Buttons on startup
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action, menu);
        return true;
    }

    //Respond to Action Button input
    public boolean onOptionsSelectedItem(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.about:
                
        }
    }

    //Private wrapper class to pass variables to Convert AsyncTask
    private class Wrapper {
        public String currencyName;
        public double price;
        public String unitName;
        public double quantity;
        public String targetCurrencyName;
        public String targetUnitName;
    }

    //AsyncTask thread to fetch json currency prices
    private class Convert extends AsyncTask<Wrapper, Void, String> {

        @Override
        protected String doInBackground(Wrapper... params) {
            //Create temp variables
            double currency, unit, targetCurrency, targetUnit;
            double price = params[0].price;
            double quantity = params[0].quantity;

            //Create an exchange
            Exchange exchange = Forex.create("http://api.fixer.io/latest?base=USD");

            //Get value of 1.00 USD in currencies
            currency = Forex.getValue(exchange, params[0].currencyName);
            targetCurrency = Forex.getValue(exchange, params[0].targetCurrencyName);

            //Get value of 1.00 litres/grams in units
            unit = Forex.getUnit(params[0].unitName);
            targetUnit = Forex.getUnit(params[0].targetUnitName);

            //Step One - determine the value of x currency in y currency
            double tempPrice;
            tempPrice = price*Forex.equivalence(currency, targetCurrency); //price in new currency per old unit

            //Step Two - determine the number of x units in y units
            double tempQuantity;
            tempQuantity = quantity*Forex.equivalence(unit, targetUnit);

            //Step Three - determine the price in y currency per y unit
            double newPrice;
            newPrice = tempPrice/tempQuantity;

            //Format results
            DecimalFormat DecFor = new DecimalFormat("0.00");
            return Forex.getCurrencySymbol(params[0].targetCurrencyName)+ DecFor.format(newPrice)+"/"+Forex.getUnitSymbol(params[0].targetUnitName);
        }
    }
}

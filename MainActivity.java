package com.example.zakatcalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Toolbar myToolbar;
    Button btnCalculate;
    EditText etGram,etCurrent;
    TextView textView, textView2;
    Spinner spType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("ZakatGoldCalculator");

        btnCalculate = findViewById(R.id.btnCalculate);
        etGram = findViewById(R.id.etGram);
        etCurrent= findViewById(R.id.etCurrent);
        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
        spType = findViewById(R.id.spType);

        // Set up the spinner with options "Keep" and "Wear"
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.type_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spType.setAdapter(adapter);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculateZakat();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_share) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Please use my application - http;s//t.co/app");
            startActivity(Intent.createChooser(shareIntent, null));
            return false;
        } else if (item.getItemId() == R.id.item_about) {
            Intent aboutIntent = new Intent(this, AboutActivity.class);
            startActivity(aboutIntent);
        }
        return false;
    }

    private void calculateZakat() {
        try {
            // Check if the gold weight is entered
            if (etGram.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "Please fill in the gold weight.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if the current value is entered
            if (etCurrent.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "Please fill in the current value of gold.", Toast.LENGTH_SHORT).show();
                return;
            }

            double weight = Double.parseDouble(etGram.getText().toString());
            double currentValue = Double.parseDouble(etCurrent.getText().toString());

            String type = spType.getSelectedItem().toString();
            double xGram = (type.equals("Keep")) ? 85.0 : 200.0;

            double totalValue = weight * currentValue;
            double zakatPayableValue = (weight - xGram) * currentValue;
            double zakat = 0.025 * zakatPayableValue;

            if (zakatPayableValue < 0) {
                String message = "Your gold weight is less than uruf value." +
                        "\nYou will not pay for zakat gold.";
                textView.setText(message);
            } else {
                String result = "Total Value of Gold: " + totalValue +
                        "\nZakat Payable Value: " + zakatPayableValue +
                        "\nTotal Zakat: " + zakat;
                textView.setText(result);
            }

        } catch (NumberFormatException nfe) {
            Toast.makeText(this, "Please enter valid numeric values.", Toast.LENGTH_SHORT).show();
        }
    }

}

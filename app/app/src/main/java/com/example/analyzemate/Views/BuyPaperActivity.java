package com.example.analyzemate.Views;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.analyzemate.Controllers.Adapters.SpinnerObject;
import com.example.analyzemate.Controllers.Interfaces.PortfolioHandler;
import com.example.analyzemate.Models.Portfolio;
import com.example.analyzemate.R;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BuyPaperActivity extends AppCompatActivity {
    TextView exitBt;
    EditText countEt, costEt;
    Button buyBt;
    Spinner portfolioSp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_buy_paper);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        exitBt = findViewById(R.id.exit);
        exitBt.setOnClickListener(view -> {
            finish();
        });

        portfolioSp = findViewById(R.id.spinner_briefcase);
        PortfolioHandler.getUsersPortfolios(this, portfolioList -> {
            List<SpinnerObject> spinnerObjects = new ArrayList<>();
            for (int i = 0; i < portfolioList.toArray().length; i++) {
                Portfolio portfolio = portfolioList.get(i);
                int portfolioID = portfolio.id;
                String portfolioName = "Портфель" + (i + 1);
                spinnerObjects.add(new SpinnerObject(portfolioName, portfolioID));
            }
            ArrayAdapter<SpinnerObject> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, spinnerObjects);
            runOnUiThread(() -> {
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                portfolioSp.setAdapter(adapter);
            });
        });

        costEt = findViewById(R.id.buy_cost);
        countEt = findViewById(R.id.buy_count);
        buyBt = findViewById(R.id.buy_bt);
        buyBt.setOnClickListener(view -> {
            SpinnerObject spinnerObject = (SpinnerObject) portfolioSp.getSelectedItem();
            String security = Objects.requireNonNull(getIntent().getExtras()).getString("uid");
            try {
                String countString = countEt.getText().toString();
                if (TextUtils.isEmpty(countString)) {
                    Toast.makeText(this, "Введите количество", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (spinnerObject == null) {
                    Toast.makeText(this, "Выберите портфель", Toast.LENGTH_SHORT).show();
                    return;
                }
                PortfolioHandler.MakeTransaction(BuyPaperActivity.this, "Buy",
                        Integer.valueOf(countString), security, spinnerObject.getValue(), () -> {
                    runOnUiThread(() ->
                            Toast.makeText(this, "Бумага куплена", Toast.LENGTH_SHORT).show()
                    );
                });
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            finish();
        });
    }
}
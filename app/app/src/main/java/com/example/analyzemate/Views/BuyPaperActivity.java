package com.example.analyzemate.Views;

import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.analyzemate.R;

public class BuyPaperActivity extends AppCompatActivity {
    TextView exitBt;
    Button buyBt;
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

        buyBt = findViewById(R.id.buy_bt);
        buyBt.setOnClickListener(view -> {
            CreateDialogView();
        });
    }

    private void CreateDialogView() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.CenterTheme));
        builder.setTitle("Купить ценную бумагу?");

        builder.setPositiveButton("Принять", (dialogInterface, i) -> {
            // AAAAAAAAAAAAAAAAAAAAAAAAAAA
            dialogInterface.cancel();
        });
        builder.setNegativeButton("Отмена", (dialogInterface, i) -> dialogInterface.cancel());
        builder.show();
    }
}
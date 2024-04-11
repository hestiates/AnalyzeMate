package com.example.analyzemate.Views.Autorization;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.analyzemate.R;

public class SuccessRegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_success_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        TextView textView = findViewById(R.id.img_text);

        textView.animate().alpha(0f).setDuration(1200).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animator) {}

            @Override
            public void onAnimationEnd(@NonNull Animator animator) {
                Intent intent = new Intent(SuccessRegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }

            @Override
            public void onAnimationCancel(@NonNull Animator animator) {}

            @Override
            public void onAnimationRepeat(@NonNull Animator animator) {}
        });
    }
}
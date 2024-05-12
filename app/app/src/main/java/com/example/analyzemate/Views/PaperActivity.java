package com.example.analyzemate.Views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.analyzemate.R;
import com.example.analyzemate.Views.PaperFragments.GraphFragment;
import com.example.analyzemate.Views.PaperFragments.OverviewFragment;
import com.example.analyzemate.Views.PaperFragments.TrackingFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class PaperActivity extends AppCompatActivity {
    private static final int NUM_PAGES = 3;
    ImageButton btExit;
    Button btBuy, btSell;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_paper);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Определение кнопок
        btExit = findViewById(R.id.exit_button);
        btBuy = findViewById(R.id.bt_buy);
        btSell = findViewById(R.id.bt_sell);

        // Установка listener на кнопку выхода
        btExit.setOnClickListener(view -> finish());

        // Переход на экран покупки
        btBuy.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), BuyPaperActivity.class));
            overridePendingTransition(R.anim.slide_right, R.anim.slide_left);
            finish();
        });

        // Переход на экран продажи
        btSell.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), SellPaperActivity.class));
            overridePendingTransition(R.anim.slide_right, R.anim.slide_left);
            finish();
        });

        // Установление адаптера pagerAdapter для ViewPager2
        ViewPager2 viewPager2 = findViewById(R.id.viewPager);
        FragmentStateAdapter pagerAdapter = new ScreenSlidePagerAdapter(this);
        viewPager2.setAdapter(pagerAdapter);
        viewPager2.setUserInputEnabled(false);

        // Связывание TabLayout и ViewPager2 для синхронизации переходов
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        new TabLayoutMediator(tabLayout, viewPager2,
                (tab, i) -> {
                    if (i == 0)
                        tab.setText("Обзор");
                    else if (i == 1)
                        tab.setText("График");
                    else if (i == 2)
                        tab.setText("Отслеживание");
                }).attach();
    }

    /**
     * Адаптер горизонтальной прокрутки
     */
    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        Bundle bundle = new Bundle();
        Bundle extra = getIntent().getExtras();
        Fragment frag;
        public ScreenSlidePagerAdapter(FragmentActivity fragAct) {
            super(fragAct);
        }
        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (position == 0) {
                bundle.putString("ticker", extra.getString("uid"));
                frag = new OverviewFragment();
                frag.setArguments(bundle);
                return frag;
            }
            else if (position == 1)
                return new GraphFragment();
            else
                return new TrackingFragment();
        }
        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }
}
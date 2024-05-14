package com.example.analyzemate.Views.PaperFragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.analyzemate.Controllers.Interfaces.TrackHandler;
import com.example.analyzemate.R;

public class TrackingFragment extends Fragment {
    ImageView imageView;
    CardView cardView;
    LinearLayout hiddenView;
    Button trackBt;
    String ticker;
    Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = getContext();

        View view = inflater.inflate(R.layout.fragment_tracking, container, false);
        cardView = view.findViewById(R.id.card);
        hiddenView = view.findViewById(R.id.hidden_layout);
        imageView = view.findViewById(R.id.image_bt);
        trackBt = view.findViewById(R.id.trac_bt);
        trackBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickButtonTrack();
            }
        });

        assert getArguments() != null;
        ticker = getArguments().getString("ticker");


        imageView.setOnClickListener(view1 -> {
            if (hiddenView.getVisibility() == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                hiddenView.setVisibility(View.GONE);
                imageView.setImageResource(R.drawable.baseline_expand_more_24);
            }
            else {
                TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                hiddenView.setVisibility(View.VISIBLE);
                imageView.setImageResource(R.drawable.baseline_expand_less_24);
            }
        });
        return view;
    }

    private void onClickButtonTrack() {
        if (trackBt.getText() == "Отслеживать") {
            TrackHandler.SetTrackStockPaper(mContext, ticker);
            trackBt.setText("Закончить отслеживание");
        }
        else {
            TrackHandler.UnSetTrackStockPaper(mContext, ticker);
            trackBt.setText("Отслеживать");
        }
    }
}
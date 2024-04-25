package com.example.analyzemate.Views.PaperFragments;

import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.analyzemate.R;

public class TrackingFragment extends Fragment {
    ImageView imageView;
    CardView cardView;
    LinearLayout hiddenView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tracking, container, false);
        cardView = view.findViewById(R.id.card);
        hiddenView = view.findViewById(R.id.hidden_layout);
        imageView = view.findViewById(R.id.image_bt);

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
}
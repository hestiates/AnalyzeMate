package com.example.analyzemate.Views.PaperFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.analyzemate.R;

public class OverviewFragment extends Fragment {
    TextView description, cost, open, close, rentable;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        description = view.findViewById(R.id.description);
        assert getArguments() != null;
        String description_text = getArguments().getString("description");
        description.setText(description_text);
        return view;
    }
}
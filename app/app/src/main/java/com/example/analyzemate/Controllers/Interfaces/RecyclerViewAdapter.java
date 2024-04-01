package com.example.analyzemate.Controllers.Interfaces;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.analyzemate.Models.State;
import com.example.analyzemate.R;

import java.util.List;

/**
 * Кастомный адаптер RecyclerView
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private final List<State> states;

    /**
     * Создание нового объекта типа RecyclerViewAdapter
     * @param context - контекст
     * @param states - состояния(предметы списка)
     */
    public RecyclerViewAdapter(Context context, List<State> states) {
        this.states = states;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = inflater.inflate(R.layout.paper_view_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
        State state = states.get(position);
        holder.bankView.setImageResource(state.getBankResource());
        holder.nameView.setText(state.getName());
        holder.costView.setText(state.getCost());
        holder.trendView.setText(state.getTrend());
    }

    /**
     * Возвращает размер датасета
     * @return размер датасета
     */
    @Override
    public int getItemCount() {
        return states.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView bankView;
        final TextView nameView, costView, trendView;

        ViewHolder(View view) {
            super(view);
            bankView = view.findViewById(R.id.paperImageView);
            nameView = view.findViewById(R.id.paperNameView);
            costView = view.findViewById(R.id.paperCost);
            trendView = view.findViewById(R.id.paperChange);
        }
    }
}

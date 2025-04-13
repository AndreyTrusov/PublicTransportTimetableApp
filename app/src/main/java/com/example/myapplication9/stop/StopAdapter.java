package com.example.myapplication9.stop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication9.R;
import com.example.myapplication9.model.Stop;

import java.util.List;

public class StopAdapter extends RecyclerView.Adapter<StopAdapter.StopViewHolder> {

    private Context context;
    private List<Stop> stopList;
    private StopItemClickListener listener;

    public interface StopItemClickListener {
        void onStopClick(int position);
        void onStopDeleteClick(int position);
    }

    public StopAdapter(Context context, List<Stop> stopList, StopItemClickListener listener) {
        this.context = context;
        this.stopList = stopList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_stop, parent, false);
        return new StopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StopViewHolder holder, int position) {
        Stop stop = stopList.get(position);

        holder.textStopName.setText(stop.getName());
        holder.textStopCoordinates.setText(String.format("%.4f, %.4f", stop.getLatitude(), stop.getLongitude()));

        if (stop.isAccessibility()) {
            holder.iconAccessibility.setVisibility(View.VISIBLE);
        } else {
            holder.iconAccessibility.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return stopList.size();
    }

    class StopViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView textStopName;
        TextView textStopCoordinates;
        ImageView iconAccessibility;
        ImageButton buttonDelete;

        StopViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.card_view);
            textStopName = itemView.findViewById(R.id.text_stop_name);
            textStopCoordinates = itemView.findViewById(R.id.text_stop_coordinates);
            iconAccessibility = itemView.findViewById(R.id.icon_accessibility);
            buttonDelete = itemView.findViewById(R.id.button_delete);

            cardView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onStopClick(position);
                }
            });

            buttonDelete.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onStopDeleteClick(position);
                }
            });
        }
    }
}

package com.example.myapplication9.route;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication9.R;
import com.example.myapplication9.model.Route;

import java.util.List;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.RouteViewHolder> {

    private Context context;
    private List<Route> routeList;
    private RouteItemClickListener listener;

    public interface RouteItemClickListener {
        void onRouteClick(int position);
        void onRouteDeleteClick(int position);
    }

    public RouteAdapter(Context context, List<Route> routeList, RouteItemClickListener listener) {
        this.context = context;
        this.routeList = routeList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RouteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_route, parent, false);
        return new RouteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteViewHolder holder, int position) {
        Route route = routeList.get(position);

        holder.textLineName.setText(route.getLineName());
        holder.textStopName.setText(route.getStopName());
        holder.textSequence.setText(String.format("Sequence: %d", route.getStopSequence()));
        holder.textDistance.setText(String.format("%.1f km", route.getDistanceFromStart()));
    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }

    class RouteViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView textLineName;
        TextView textStopName;
        TextView textSequence;
        TextView textDistance;
        ImageButton buttonDelete;

        RouteViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.card_view);
            textLineName = itemView.findViewById(R.id.text_line_name);
            textStopName = itemView.findViewById(R.id.text_stop_name);
            textSequence = itemView.findViewById(R.id.text_sequence);
            textDistance = itemView.findViewById(R.id.text_distance);
            buttonDelete = itemView.findViewById(R.id.button_delete);

            cardView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onRouteClick(position);
                }
            });

            buttonDelete.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onRouteDeleteClick(position);
                }
            });
        }
    }
}


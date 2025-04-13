package com.example.myapplication9.line;

import android.content.Context;
import android.graphics.Color;
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
import com.example.myapplication9.model.Line;

import java.util.List;

public class LineAdapter extends RecyclerView.Adapter<LineAdapter.LineViewHolder> {

    private Context context;
    private List<Line> lineList;
    private LineItemClickListener listener;

    public interface LineItemClickListener {
        void onLineClick(int position);
        void onLineDeleteClick(int position);
    }

    public LineAdapter(Context context, List<Line> lineList, LineItemClickListener listener) {
        this.context = context;
        this.lineList = lineList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_line, parent, false);
        return new LineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LineViewHolder holder, int position) {
        Line line = lineList.get(position);

        holder.textLineName.setText(line.getName());
        holder.textLineType.setText(line.getType());

        try {
            holder.colorIndicator.setBackgroundColor(Color.parseColor(line.getColor()));
        } catch (IllegalArgumentException e) {
            holder.colorIndicator.setBackgroundColor(context.getResources().getColor(R.color.primary_color));
        }

        switch (line.getType().toLowerCase()) {
            case "bus":
                holder.iconTransport.setImageResource(R.drawable.ic_bus);
                break;
            case "train":
                holder.iconTransport.setImageResource(R.drawable.ic_train);
                break;
            case "subway":
                holder.iconTransport.setImageResource(R.drawable.ic_subway);
                break;
            case "tram":
                holder.iconTransport.setImageResource(R.drawable.ic_tram);
                break;
            default:
                holder.iconTransport.setImageResource(R.drawable.ic_transport);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return lineList.size();
    }

    class LineViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        View colorIndicator;
        TextView textLineName;
        TextView textLineType;
        ImageView iconTransport;
        ImageButton buttonDelete;

        LineViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.card_view);
            colorIndicator = itemView.findViewById(R.id.color_indicator);
            textLineName = itemView.findViewById(R.id.text_line_name);
            textLineType = itemView.findViewById(R.id.text_line_type);
            iconTransport = itemView.findViewById(R.id.icon_transport);
            buttonDelete = itemView.findViewById(R.id.button_delete);

            cardView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onLineClick(position);
                }
            });

            buttonDelete.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onLineDeleteClick(position);
                }
            });
        }
    }
}
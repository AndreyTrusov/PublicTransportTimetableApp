package com.example.myapplication9.schedule;

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
import com.example.myapplication9.model.Schedule;

import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {

    private Context context;
    private List<Schedule> scheduleList;
    private ScheduleItemClickListener listener;

    public interface ScheduleItemClickListener {
        void onScheduleClick(int position);
        void onScheduleDeleteClick(int position);
    }

    public ScheduleAdapter(Context context, List<Schedule> scheduleList, ScheduleItemClickListener listener) {
        this.context = context;
        this.scheduleList = scheduleList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_schedule, parent, false);
        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        Schedule schedule = scheduleList.get(position);

        // Set text fields
        holder.textLineName.setText(schedule.getLineName());
        holder.textStopName.setText(schedule.getStopName());
        holder.textTime.setText(schedule.getDepartureTime());

        // Set day indicators
        if (schedule.isWeekday()) {
            holder.iconWeekday.setVisibility(View.VISIBLE);
        } else {
            holder.iconWeekday.setVisibility(View.GONE);
        }

        if (schedule.isWeekend()) {
            holder.iconWeekend.setVisibility(View.VISIBLE);
        } else {
            holder.iconWeekend.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    class ScheduleViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView textLineName;
        TextView textStopName;
        TextView textTime;
        ImageView iconWeekday;
        ImageView iconWeekend;
        ImageButton buttonDelete;

        ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.card_view);
            textLineName = itemView.findViewById(R.id.text_line_name);
            textStopName = itemView.findViewById(R.id.text_stop_name);
            textTime = itemView.findViewById(R.id.text_time);
            iconWeekday = itemView.findViewById(R.id.icon_weekday);
            iconWeekend = itemView.findViewById(R.id.icon_weekend);
            buttonDelete = itemView.findViewById(R.id.button_delete);

            // Set click listeners
            cardView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onScheduleClick(position);
                }
            });

            buttonDelete.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onScheduleDeleteClick(position);
                }
            });
        }
    }
}

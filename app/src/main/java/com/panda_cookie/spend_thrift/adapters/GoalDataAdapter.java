package com.panda_cookie.spend_thrift.adapters;

// GoalDataAdapter.java
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.panda_cookie.spend_thrift.R;

import java.util.List;

public class GoalDataAdapter extends RecyclerView.Adapter<GoalDataAdapter.ViewHolder> {
    private List<GoalData> goalDataList;
    private LayoutInflater layoutInflater;

    public GoalDataAdapter(Context context, List<GoalData> goalDataList) {
        this.goalDataList = goalDataList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_stats, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GoalData goalData = goalDataList.get(position);
        holder.bucketType.setText(goalData.getDisplayBucketType());
        holder.goalsMet.setText(String.valueOf(goalData.getGoals_met()));
        holder.goalsMissed.setText(String.valueOf(goalData.getGoals_missed()));
        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.row_even));
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.row_odd));
        }
    }

    @Override
    public int getItemCount() {
        return goalDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView bucketType, goalsMet, goalsMissed;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bucketType = itemView.findViewById(R.id.bucket_type);
            goalsMet = itemView.findViewById(R.id.goal_met_count);
            goalsMissed = itemView.findViewById(R.id.goals_missed);
        }
    }

    public static class GoalData {
        private String bucket_type;
        private int goals_met;
        private int goals_missed;

        public GoalData(String bucket_type, int goals_met, int goals_missed) {
            this.bucket_type = bucket_type;
            this.goals_met = goals_met;
            this.goals_missed = goals_missed;
        }

        public String getBucket_type() {
            return bucket_type;
        }

        public void setBucket_type(String bucket_type) {
            this.bucket_type = bucket_type;
        }

        public int getGoals_met() {
            return goals_met;
        }

        public void setGoals_met(int goals_met) {
            this.goals_met = goals_met;
        }

        public int getGoals_missed() {
            return goals_missed;
        }

        public void setGoals_missed(int goals_missed) {
            this.goals_missed = goals_missed;
        }

        public String getDisplayBucketType() {
            switch (bucket_type) {
                case "small":
                    return "Small";
                case "medium":
                    return "Medium";
                case "large":
                    return "Large";
                case "extra_large":
                    return "Extra Large";
                default:
                    return bucket_type;
            }
        }
    }

}

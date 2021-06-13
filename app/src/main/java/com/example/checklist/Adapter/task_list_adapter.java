package com.example.checklist.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.checklist.R;
import com.example.checklist.models.item;

import java.util.ArrayList;

public class task_list_adapter extends RecyclerView.Adapter<task_list_adapter.taskListViewHolder>{
    private ArrayList<item> task_view;
    private Context context;
    private OnItemClickListener mOnClickListener;

    public task_list_adapter(Context ct,  ArrayList<item> task, OnItemClickListener OnClickListener)
    {
        context = ct;
        task_view = task;
        mOnClickListener = OnClickListener;
    }

    @Override
    public taskListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
          LayoutInflater inflater = LayoutInflater.from(context);
          View view = inflater.inflate(R.layout.task_item_layout, parent, false);
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item,parent,false);
          return new taskListViewHolder(view, mOnClickListener);
    }

    @Override
    public void onBindViewHolder(final taskListViewHolder holder, int position) {
        holder.taskName.setText(task_view.get(position).getItem_name());
        if(task_view.get(position).isCompleted()){
            holder.taskName.setPaintFlags(holder.taskName.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        }

    }

    @Override
    public int getItemCount() {
        return task_view.size();
    }

    public class taskListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView taskName;
        public OnItemClickListener onItemClickListener;

        public taskListViewHolder(View itemView, OnItemClickListener itemClickListener) {
            super(itemView);
            onItemClickListener = itemClickListener;
            taskName = (TextView) itemView.findViewById(R.id.task_item);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onItemClickListener.onItemClick(getAbsoluteAdapterPosition());
        }
    }
    public interface OnItemClickListener{
        public void onItemClick(int position);
    }
}

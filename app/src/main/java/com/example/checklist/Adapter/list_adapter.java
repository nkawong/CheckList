package com.example.checklist.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.checklist.models.Directory_List;
import com.example.checklist.R;

import java.util.ArrayList;

public class list_adapter extends RecyclerView.Adapter<list_adapter.listViewHolder>{
    private Context context;
    private ArrayList<Directory_List> titleNames;
    private OnItemClickListener mOnItemClickListener;


    public list_adapter(Context ct, ArrayList<Directory_List> titles,OnItemClickListener onItemClickListener){
        context = ct;
        titleNames = titles;
        mOnItemClickListener = onItemClickListener;
    }
    @NonNull
    @Override
    public listViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View mView = inflater.inflate(R.layout.list_view_card_layout,parent, false);
        return new listViewHolder(mView, mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull listViewHolder holder, int position) {
        holder.titleText.setText(titleNames.get(position).getTitle_name());
    }

    @Override
    public int getItemCount() {
        return titleNames.size();
    }

    public class listViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView titleText;
        OnItemClickListener onItemClickListener;


        public listViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            this.onItemClickListener = onItemClickListener;
            titleText = itemView.findViewById(R.id.list_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClickListener.OnItemClick(getAbsoluteAdapterPosition(),titleNames.get(getAbsoluteAdapterPosition()).getId().toString());
        }
    }
    public interface OnItemClickListener{
        public void OnItemClick(int position, String id);
    }
}

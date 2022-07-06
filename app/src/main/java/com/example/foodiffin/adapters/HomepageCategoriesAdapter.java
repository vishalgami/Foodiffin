package com.example.foodiffin.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;
import com.example.foodiffin.R;
import com.example.foodiffin.models.HomepageCategoriesModel;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class HomepageCategoriesAdapter extends RecyclerView.Adapter<HomepageCategoriesAdapter.ViewHolder> {

    Context context;
    List<HomepageCategoriesModel> categoryNameList;

    public HomepageCategoriesAdapter(Context context, List<HomepageCategoriesModel> categoryNameList) {
        this.context = context;
        this.categoryNameList = categoryNameList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.homepage_categories,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.categoryNameTextView.setText(categoryNameList.get(position).getCategoryName());
    }

    @Override
    public int getItemCount() {
        return categoryNameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView categoryNameTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryNameTextView = itemView.findViewById(R.id.categoryTextView);
        }
    }
}

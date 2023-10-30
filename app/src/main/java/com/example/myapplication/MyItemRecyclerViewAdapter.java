package com.example.myapplication;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.myapplication.placeholder.PackageContent.PackageItem;
import com.example.myapplication.databinding.FragmentBrowseEventsBinding;

import java.util.ArrayList;
import java.util.List;

import kotlin.contracts.Returns;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PackageItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> implements Filterable{

    private List<PackageItem> mValues;

    public MyItemRecyclerViewAdapter(List<PackageItem> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    return new ViewHolder(FragmentBrowseEventsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).name);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();

                if (constraint == null || constraint.length() == 0) {
                    filterResults.count = mValues.size();
                    filterResults.values = mValues;
                } else {
                    String searchStr = constraint.toString().toLowerCase();
                    List<PackageItem> resultData = new ArrayList<>();

                    for (PackageItem packageItem:mValues){
                        if (packageItem.name.contains(searchStr)){
                            resultData.add(packageItem);
                    }
                    filterResults.count = resultData.size();
                    filterResults.values = resultData;
                }

                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mValues = (List<PackageItem>) results.values;
                notifyDataSetChanged();
            }
        };
        return null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public PackageItem mItem;

    public ViewHolder(FragmentBrowseEventsBinding binding) {
      super(binding.getRoot());
      mIdView = binding.itemNumber;
      mContentView = binding.content;
    }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
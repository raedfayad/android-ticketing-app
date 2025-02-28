package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.data.model.PackageContent;
import com.example.myapplication.data.model.PackageList;
import com.example.myapplication.placeholder.OldPackageContent;

/**
 * A fragment representing a list of Items.
 */
public class PackageFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PackageFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PackageFragment newInstance(int columnCount) {
        PackageFragment fragment = new PackageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_packages_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView recyclerView) {
            Context context = view.getContext();
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            try {
                recyclerView.setAdapter(new MyPackageRecyclerViewAdapter(false));
            } catch (Exception e) {
                System.out.println("Error: " + e);
                Toast.makeText(getContext().getApplicationContext(), e + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        return view;
    }
}
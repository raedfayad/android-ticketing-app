package com.example.myapplication;

import static com.example.myapplication.data.FullResponseBuilder.getFullResponse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.data.Result;
import com.example.myapplication.data.model.LoggedInUser;
import com.example.myapplication.data.model.PackageContent;
import com.example.myapplication.data.model.Package;

import com.example.myapplication.data.model.PackageList;
import com.example.myapplication.data.model.UserToken;
import com.example.myapplication.databinding.FragmentPackageBinding;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PackageContent}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyPackageRecyclerViewAdapter extends RecyclerView.Adapter<MyPackageRecyclerViewAdapter.ViewHolder> {

    private final List<Package> mValues;

    public MyPackageRecyclerViewAdapter(boolean forUser) throws Exception {
        mValues = getPackages(forUser);
    }

    public List<Package> getPackages(boolean forUser) throws Exception {
        List<Package> packages = new ArrayList<>();
        final HttpURLConnection[] con = new HttpURLConnection[1];
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://10.0.2.2:5000/api/packages");

                    con[0] = (HttpURLConnection) url.openConnection();
                    con[0].setRequestMethod("GET");

                    con[0].setRequestProperty("Content-Type", "application/json");
                    con[0].setConnectTimeout(5000);
                    con[0].setReadTimeout(5000);
                    con[0].connect();


                    // send the request
                    String response = getFullResponse(con[0]);
                    System.out.println(response);
                    con[0].disconnect();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        thread.join();
        if (con[0].getResponseCode() == 200){
            InputStream in = con[0].getInputStream();
            String text = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            System.out.println("message response:" + text);

            Gson gson = new Gson();
            PackageList packageList = gson.fromJson(text, PackageList.class);
            return packageList.packages;

//
//            LoggedInUser user =
//                    new LoggedInUser(
//                            usertoken.token,
//                            username);
//            System.out.println("username:"+username+" token:"+usertoken.token);
//            return new Result.Success<>(user);
//            packages.add(new Package("sample", "sample description", 1, true, 100.00));

        } else {
            throw new Exception("Received a non-2xx response. Response code: " + String.valueOf(con[0].getResponseCode()) + " Message: " + con[0].getResponseMessage());
        }


//        return packages;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentPackageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(String.valueOf(mValues.get(position).id));
        holder.mContentView.setText(mValues.get(position).name);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public Package mItem;

        public ViewHolder(FragmentPackageBinding binding) {
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
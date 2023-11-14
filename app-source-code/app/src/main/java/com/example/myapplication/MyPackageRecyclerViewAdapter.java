package com.example.myapplication;

import static com.example.myapplication.data.FullResponseBuilder.getFullResponse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.data.LoginDataSource;
import com.example.myapplication.data.LoginRepository;
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
        packages.add(new Package("No packages found", "Please purchase a package from our website in order to view it here.", 1, 1, 0.0));
        final HttpURLConnection[] con = new HttpURLConnection[1];
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url;
                    if (forUser){

                        url = new URL("https://10.0.2.11/api/account/purchases");
                    } else {
                        url = new URL("https://10.0.2.11/api/packages");
                    }


                    con[0] = (HttpURLConnection) url.openConnection();
                    con[0].setRequestMethod("GET");
                    con[0].setRequestProperty("Content-Type", "application/json");
                    con[0].setConnectTimeout(5000);
                    con[0].setReadTimeout(5000);

                    if (forUser){
                        LoginRepository loginDetails = LoginRepository.getInstance(new LoginDataSource());
                        if (loginDetails.isLoggedIn()) {
                            con[0].setRequestProperty("AuthToken", loginDetails.user.getUserToken());
                        } else {
                            throw new Exception("Trying to access data that requires login");
                        }
                    }

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
            if (packageList.packages == null) {
                return packages;
            } else {
                return packageList.packages;
            }

        } else {
            throw new Exception("Received a non-2xx response. Response code: " + con[0].getResponseCode() + " Message: " + con[0].getResponseMessage());
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
        holder.itemNumber.setText(String.valueOf(position+1));
        holder.name.setText(mValues.get(position).name);
        holder.description.setText(mValues.get(position).description);
        holder.cost.setText("Cost: $"+mValues.get(position).price);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView itemNumber;
        public final TextView cost;
        public final TextView description;
        public final TextView name;
        public Package mItem;

        public ViewHolder(FragmentPackageBinding binding) {
            super(binding.getRoot());
            itemNumber = binding.itemNumber;
            name = binding.packageName;
            description = binding.packageDescription;
            cost = binding.packageCost;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + name.getText() + "'";
        }
    }
}
package com.example.myapplication;

import static com.example.myapplication.data.FullResponseBuilder.getFullResponse;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.data.LoginDataSource;
import com.example.myapplication.data.LoginRepository;
import com.example.myapplication.data.model.Package;
import com.example.myapplication.data.model.PackageContent;
import com.example.myapplication.data.model.PurchaseList;
import com.example.myapplication.databinding.FragmentUserEventsBinding;
import com.google.gson.Gson;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PackageContent}.
 * TODO: Replace the implementation with code for your data type.
 */
public class UserEventsRecyclerViewAdapter extends RecyclerView.Adapter<UserEventsRecyclerViewAdapter.ViewHolder> {

    private final List<PurchaseList.Purchase> mValues;
    Context thiscontext;


    public UserEventsRecyclerViewAdapter() throws Exception {
        mValues = getPackages();
    }

    public List<PurchaseList.Purchase> getPackages() throws Exception {
        List<PurchaseList.Purchase> purchases = new ArrayList<>();
        purchases.add(new PurchaseList.Purchase("No packages found", "Please purchase a package from our website in order to view it here.", 1));
        final HttpURLConnection[] con = new HttpURLConnection[1];
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url;
                    url = new URL("http://10.0.2.2:5000/api/account/purchases");

                    con[0] = (HttpURLConnection) url.openConnection();
                    con[0].setRequestMethod("GET");
                    con[0].setRequestProperty("Content-Type", "application/json");
                    con[0].setConnectTimeout(5000);
                    con[0].setReadTimeout(5000);


                    LoginRepository loginDetails = LoginRepository.getInstance(new LoginDataSource());
                    if (loginDetails.isLoggedIn()) {
                        con[0].setRequestProperty("AuthToken", loginDetails.user.getUserToken());
                    } else {
                        throw new Exception("Trying to access data that requires login");
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
            PurchaseList purchaseList = gson.fromJson(text, PurchaseList.class);
            if (purchaseList.purchases == null) {
                return purchases;
            } else {
                return purchaseList.purchases;
            }

        } else {
            throw new Exception("Received a non-2xx response. Response code: " + con[0].getResponseCode() + " Message: " + con[0].getResponseMessage());
        }


//        return packages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        thiscontext = parent.getContext();

        return new ViewHolder(FragmentUserEventsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.name.setText("Package: " + mValues.get(position).name);

        if (Objects.equals(mValues.get(position).name, "No packages found")) {
            holder.qr_code_button.setVisibility(View.INVISIBLE);
            holder.itemNumber.setVisibility(View.GONE);
        } else {
            holder.itemNumber.setText(String.valueOf(position+1));

            String string_format=mValues.get(position).date;
            Date date_format= null;
            try {
                date_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS").parse(string_format);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            System.out.println(" Date in String Format: "+string_format+" \n Converted Date: "+date_format);

            holder.time.setText("Purchase Date: " + date_format.toString());

            String str = mValues.get(position).date + mValues.get(position).name + mValues.get(position).hashCode();
            String qrCodeValue = Base64.getEncoder().encodeToString(str.getBytes());
            System.out.println("QRcode value before base64 encoding: " + str);
            System.out.println("QRcode value after base64 encoding: " + qrCodeValue);
            holder.qr_code_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(v.getContext(), QRCodeActivity.class);
                    intent.putExtra("key", qrCodeValue);
                    v.getContext().startActivity(intent);


//                    Navigation.findNavController((Activity) v.getContext(), R.id.nav_host_fragment_content_main).navigate(R.id.);

                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView itemNumber;
        public final TextView time;
        public final TextView name;
        public final Button qr_code_button;
        public PurchaseList.Purchase mItem;

        public ViewHolder(FragmentUserEventsBinding binding) {
            super(binding.getRoot());
            itemNumber = binding.itemNumber;
            name = binding.packageName;
            time = binding.purchaseTime;
            qr_code_button = binding.qrCodeButton;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + name.getText() + "'";
        }
    }
}
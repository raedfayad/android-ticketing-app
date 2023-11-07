package com.example.myapplication.data;

import static com.example.myapplication.data.FullResponseBuilder.getFullResponse;

import com.example.myapplication.R;
import com.example.myapplication.data.model.LoggedInUser;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.example.myapplication.data.model.UserToken;
import com.google.gson.Gson;


/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {

        try {
            // TODO: handle loggedInUser authentication

            final HttpURLConnection[] con = new HttpURLConnection[1];
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = new URL("http://10.0.2.2:5000/api/account/login");

                        con[0] = (HttpURLConnection) url.openConnection();
                        con[0].setRequestMethod("POST");

                        con[0].setRequestProperty("Content-Type", "application/json");
                        con[0].setConnectTimeout(5000);
                        con[0].setReadTimeout(5000);

//                        Map<String, String> parameters = new HashMap<>();
//                        parameters.put("username", username);
//                        parameters.put("password", password);
//                        con.setDoOutput(true);
//                        DataOutputStream out = new DataOutputStream(con.getOutputStream());
//                        out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
//                        out.flush();
//                        out.close();

                        OutputStream os = con[0].getOutputStream();
                        OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
                        osw.write(String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password));
                        osw.flush();
                        osw.close();
                        os.close();  //don't forget to close the OutputStream
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
                Gson gson = new Gson();
                UserToken usertoken = gson.fromJson(con[0].getResponseMessage(), UserToken.class);

                LoggedInUser user =
                        new LoggedInUser(
                                usertoken.token,
                                username);
                System.out.println("username:"+username+" token:"+usertoken.token);
                return new Result.Success<>(user);
            } else {
                return new Result.Error(new Exception("Failed to Login"));
            }



        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
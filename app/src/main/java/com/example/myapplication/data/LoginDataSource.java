package com.example.myapplication.data;

import static com.example.myapplication.data.FullResponseBuilder.getFullResponse;

import com.example.myapplication.R;
import com.example.myapplication.data.model.LoggedInUser;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.example.myapplication.data.model.UserToken;
import com.google.gson.Gson;


/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password, boolean registerUser) {

        try {
            final HttpURLConnection[] con = new HttpURLConnection[1];
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url;
                        String method;

                        if (registerUser){
                            url = new URL("http://10.0.2.2:5000/api/accounts");
                            method = "PUT";
                        } else {
                            url = new URL("http://10.0.2.2:5000/api/account/login");
                            method = "POST";
                        }

                        con[0] = (HttpURLConnection) url.openConnection();
                        con[0] = loginRequest(con[0], username, password, url, method);
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

                if (registerUser){
                    return login(username, password, false);
                } else {
                    Gson gson = new Gson();
                    UserToken usertoken = gson.fromJson(text, UserToken.class);

                    LoggedInUser user =
                            new LoggedInUser(
                                    usertoken.token,
                                    username);
                    System.out.println("username:"+username+" token:"+usertoken.token);
                    return new Result.Success<>(user);
                }

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

    public HttpURLConnection loginRequest(HttpURLConnection con, String username, String password, URL url, String method) throws IOException {
        con.setRequestMethod(method);

        con.setRequestProperty("Content-Type", "application/json");
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);

        OutputStream os = con.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
        osw.write(String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password));
        osw.flush();
        osw.close();
        os.close();  //don't forget to close the OutputStream
        con.connect();


        // send the request
        String response = getFullResponse(con);
        System.out.println(response);
        return con;
    }
}
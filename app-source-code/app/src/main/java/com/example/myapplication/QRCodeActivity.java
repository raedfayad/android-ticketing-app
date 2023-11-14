package com.example.myapplication;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.databinding.ActivityQrcodeBinding;

import java.util.Base64;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QRCodeActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityQrcodeBinding binding;

    private String sauce;

    private String ADMIN_TOKEN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityQrcodeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Intent intent = getIntent();
        String value = intent.getStringExtra("key"); //if it's a string you stored.
        sauce = intent.getStringExtra("sauce"); //if it's a string you stored.

        ADMIN_TOKEN = getToken();

        // Inflate the layout for this fragment
        ImageView qrCodeIV = binding.idIVQrcode;

        // setting this dimensions inside our qr code
        // encoder to generate our qr code.
        QRGEncoder qrgEncoder = new QRGEncoder(value, null, QRGContents.Type.TEXT, 150);
        try {
            // Getting QR-Code as Bitmap
            Bitmap bitmap = qrgEncoder.getBitmap(0);
            // Setting Bitmap to ImageView
            qrCodeIV.setImageBitmap(bitmap);
        } catch (Exception e) {
            // this method is called for
            // exception handling.
            Log.e("Tag", e.toString());
        }
    }
    public String getToken() {
        assert sauce != null;
        byte[] obfuscatedBytes = sauce.getBytes();
        for (int round = 2; round >= 0; round--) {
            for (int i = obfuscatedBytes.length - 1; i >= 0; i--) {
                obfuscatedBytes[i] ^= (byte) (i + round);
            }
        }
        return new String(obfuscatedBytes) ;
    }



}
package com.example.lab3;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class StorageActivity extends AppCompatActivity {

    private static final String STORAGE_FILE = "history.txt";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);

        TextView tv = findViewById(R.id.storageText);

        String content = readFileSafe();
        if (content.trim().isEmpty()) {
            tv.setText(R.string.storage_is_empty);
        } else {
            tv.setText(content);
        }
    }

    private String readFileSafe() {
        try (FileInputStream fis = openFileInput(STORAGE_FILE);
             InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
             BufferedReader br = new BufferedReader(isr)) {

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append('\n');
            }
            return sb.toString();

        } catch (Exception e) {
            return "";
        }
    }
}
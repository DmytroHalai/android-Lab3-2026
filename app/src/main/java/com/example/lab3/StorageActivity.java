package com.example.lab3;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class StorageActivity extends AppCompatActivity {

    private static final String STORAGE_FILE = "history.txt";
    private static String content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);

        TextView textView = findViewById(R.id.storageText);

        Button btnClear = findViewById(R.id.btnClear);

        btnClear.setOnClickListener(v -> clearFile(textView));

        content = readFileSafe();
        if (content.trim().isEmpty()) {
            textView.setText(R.string.storage_is_empty);
        } else {
            textView.setText(content);
        }
    }

    private void clearFile(TextView textView) {
        if (content.trim().isEmpty()) {
            Toast.makeText(this, "Storage is empty", Toast.LENGTH_LONG).show();
            return;
        }
        try (FileOutputStream fos = openFileOutput(STORAGE_FILE, MODE_PRIVATE)) {
            fos.write("".getBytes(StandardCharsets.UTF_8));
            fos.flush();
            textView.setText(R.string.storage_is_empty);
            content = "";
        } catch (Exception e){
            System.err.println("Error clearing file:" + e.getMessage());
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
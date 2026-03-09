package com.example.lab3;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity
        implements InputFragment.OnInputSubmitListener, ResultFragment.OnResultCancelListener {

    private static final String TAG_INPUT = "TAG_INPUT";
    private static final String TAG_RESULT = "TAG_RESULT";

    private static final String STORAGE_FILE = "history.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.inputContainer, new InputFragment(), TAG_INPUT)
                    .commit();
        }
    }

    @Override
    public void onInputSubmitted(int selectedShapeId, double value,
                                 boolean areaChecked, boolean perimeterChecked) {
        Shape shape = Shape.fromRadioId(selectedShapeId);
        if (shape == null) return;
        String result = buildResult(shape, value, areaChecked, perimeterChecked);

        boolean saved = appendToFile(result);
        Toast.makeText(
                this,
                saved ? "Sucessfully written in the file" : "Error during writing occurred",
                Toast.LENGTH_LONG
        ).show();

        showResultFragment(result);
    }

    private boolean appendToFile(String text) {
        String entry = text + "\n----------------\n";

        try (FileOutputStream fos = openFileOutput(STORAGE_FILE, MODE_APPEND)) {
            fos.write(entry.getBytes(StandardCharsets.UTF_8));
            fos.flush();
            return true;
        } catch (IOException e) {
            System.err.println("Error writing to file:" + e.getMessage());
            return false;
        }
    }

    private String buildResult(@NonNull Shape shape, double value,
                               boolean areaChecked, boolean perimeterChecked) {
        ArrayList<String> lines = new ArrayList<>();
        lines.add("Selected: " + shape.title);
        lines.add(shape.valueLabel + " = " + value);

        if (areaChecked) lines.add("Area: " + shape.area(value));
        if (perimeterChecked) lines.add("Perimeter: " + shape.perimeter(value));

        return TextUtils.join("\n", lines);
    }

    private void showResultFragment(String resultText) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.resultContainer, ResultFragment.newInstance(resultText), TAG_RESULT)
                .commit();
    }

    @Override
    public void onCancelResult() {
        Fragment resultFragment = getSupportFragmentManager().findFragmentByTag(TAG_RESULT);
        assert resultFragment != null;
        getSupportFragmentManager()
                .beginTransaction()
                .remove(resultFragment)
                .commit();

        ((InputFragment) Objects.requireNonNull(getSupportFragmentManager().
                findFragmentByTag(TAG_INPUT))).clearForm();
    }
}
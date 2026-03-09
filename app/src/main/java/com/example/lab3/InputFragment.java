package com.example.lab3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class InputFragment extends Fragment {

    private RadioGroup shapeGroup;
    private CheckBox checkArea;
    private CheckBox checkPerimeter;
    private EditText inputValue;

    private OnInputSubmitListener listener;

    private static final String KEY_SELECTED_SHAPE = "selected_shape";
    private static final String KEY_AREA_CHECKED = "area_checked";
    private static final String KEY_PERIMETER_CHECKED = "perimeter_checked";
    private static final String KEY_INPUT_VALUE = "input_value";

    public interface OnInputSubmitListener {
        void onInputSubmitted(int selectedShapeId, double value,
                              boolean areaChecked, boolean perimeterChecked);
    }

    public InputFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnInputSubmitListener) listener = (OnInputSubmitListener) context;
        else throw new RuntimeException(context + " must implement OnInputSubmitListener");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_input, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        shapeGroup = view.findViewById(R.id.shapeGroup);
        checkArea = view.findViewById(R.id.checkArea);
        checkPerimeter = view.findViewById(R.id.checkPerimeter);
        inputValue = view.findViewById(R.id.inputValue);
        Button btnOk = view.findViewById(R.id.btnOk);
        Button btnOpen = view.findViewById(R.id.btnOpen);

        inputValue.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        btnOk.setOnClickListener(v -> handleOk());

        btnOpen.setOnClickListener(v -> {
            if (getActivity() != null) {
                Intent i = new Intent(getActivity(), StorageActivity.class);
                startActivity(i);
            }
        });

        if (savedInstanceState != null) {
            int checkedId = savedInstanceState.getInt(KEY_SELECTED_SHAPE, -1);
            if (checkedId != -1) shapeGroup.check(checkedId);

            checkArea.setChecked(savedInstanceState.getBoolean(KEY_AREA_CHECKED, false));
            checkPerimeter.setChecked(savedInstanceState.getBoolean(KEY_PERIMETER_CHECKED, false));
            inputValue.setText(savedInstanceState.getString(KEY_INPUT_VALUE, ""));
        }
    }

    private void handleOk() {
        int selectedId = shapeGroup.getCheckedRadioButtonId();

        if (selectedId == -1) {
            showToast("Please select a figure!");
            return;
        }

        boolean areaChecked = checkArea.isChecked();
        boolean perimeterChecked = checkPerimeter.isChecked();

        if (!(areaChecked || perimeterChecked)) {
            showToast("Please select Area or Perimeter!");
            return;
        }

        String s = inputValue.getText().toString().trim();
        if (TextUtils.isEmpty(s)) {
            showToast("Value is empty!");
            return;
        }

        double value;
        try {
            value = Double.parseDouble(s);
        } catch (NumberFormatException e) {
            showToast("Invalid number!");
            return;
        }

        if (value <= 0) {
            showToast("Value must be > 0");
            return;
        }

        if (listener != null)
            listener.onInputSubmitted(selectedId, value, areaChecked, perimeterChecked);
    }

    public void clearForm() {
        if (shapeGroup != null) shapeGroup.clearCheck();
        if (checkArea != null) checkArea.setChecked(false);
        if (checkPerimeter != null) checkPerimeter.setChecked(false);
        if (inputValue != null) inputValue.setText("");
    }

    private void showToast(String msg) {
        if (getContext() != null) Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (shapeGroup != null)
            outState.putInt(KEY_SELECTED_SHAPE, shapeGroup.getCheckedRadioButtonId());
        if (checkArea != null)
            outState.putBoolean(KEY_AREA_CHECKED, checkArea.isChecked());
        if (checkPerimeter != null)
            outState.putBoolean(KEY_PERIMETER_CHECKED, checkPerimeter.isChecked());
        if (inputValue != null)
            outState.putString(KEY_INPUT_VALUE, inputValue.getText().toString());
    }
}
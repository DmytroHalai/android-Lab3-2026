package com.example.lab3;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ResultFragment extends Fragment {

    private static final String ARG_RESULT_TEXT = "arg_result_text";

    private OnResultCancelListener listener;

    public interface OnResultCancelListener {
        void onCancelResult();
    }

    public ResultFragment() {
    }

    public static ResultFragment newInstance(String resultText) {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_RESULT_TEXT, resultText);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnResultCancelListener) {
            listener = (OnResultCancelListener) context;
        } else {
            throw new RuntimeException(context + " must implement OnResultCancelListener");
        }
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
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView resultView = view.findViewById(R.id.resultView);
        Button btnCancel = view.findViewById(R.id.btnCancel);

        String resultText = "";
        if (getArguments() != null)
            resultText = getArguments().getString(ARG_RESULT_TEXT, "");


        resultView.setText(resultText);

        btnCancel.setOnClickListener(v -> {
            if (listener != null) listener.onCancelResult();
        });
    }
}
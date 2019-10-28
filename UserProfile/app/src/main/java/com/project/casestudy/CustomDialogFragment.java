package com.project.casestudy;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CustomDialogFragment extends DialogFragment {
    private static final String MESSAGE = "MESSAGE";
    private static final String TEXT = "TEXT";
    private TextView warningText;
    private TextView accept;
    private TextView cancel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog, container, false);
        warningText = (TextView) view.findViewById(R.id.warning_text);
        accept = (TextView) view.findViewById(R.id.accept);
        cancel = (TextView) view.findViewById(R.id.cancel);
        Bundle bundle = getArguments();
        warningText.setText(bundle.getString(MESSAGE, ""));
        final String text=bundle.getString(TEXT, "");
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
                DecideDialogFragment deleteMember = (DecideDialogFragment) getActivity();
                deleteMember.onDecideDialogFragment(text);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        return view;
    }

    public interface DecideDialogFragment{
        void onDecideDialogFragment(String text);
    }
}
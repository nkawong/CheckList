package com.example.checklist;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.zip.Inflater;

public class addListDialog extends AppCompatDialogFragment{
    private EditText editTitleName;
    private Button addButton;
    private Button cancelButton;

    public OnInputSelected mOnInputSelected;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View mView = inflater.inflate(R.layout.add_list_dialog, null);

        builder.setView(mView);
        addButton = (Button) mView.findViewById(R.id.add_button);
        cancelButton = (Button) mView.findViewById(R.id.cancel_button);

        editTitleName = (EditText) mView.findViewById(R.id.txt_input);
        final AlertDialog aDialog = builder.create();



        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String input = editTitleName.getText().toString().trim();
                if(!input.equals("")) {
                    mOnInputSelected.sendInput(input);
                    aDialog.dismiss();
                }

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aDialog.dismiss();
            }
        });
        return aDialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnInputSelected = (OnInputSelected) getActivity();
        }catch (ClassCastException e){
            Log.e("addListDialog", "onAttach: ClassClassException: " + e.getMessage());
        }
    }

    public interface OnInputSelected{
        void sendInput(String input);
    }
}

package com.example.checklist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class addItemDialog extends AppCompatDialogFragment {
    private EditText mEditTextName;
    private Button addButton;
    private Button cancelButton;

    private OnEditTextSelected mOnEditTextSelected;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View mView = inflater.inflate(R.layout.add_item_dialog, null);

        builder.setView(mView);
        addButton = (Button) mView.findViewById(R.id.add_item_button);
        cancelButton = (Button) mView.findViewById(R.id.cancel_item_button);

        mEditTextName = (EditText) mView.findViewById(R.id.item_txt_input);

        final AlertDialog aDialog = builder.create();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = mEditTextName.getText().toString().trim();
                if(!input.equals("")) {
                    mOnEditTextSelected.sendEditText(input);
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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            mOnEditTextSelected = (OnEditTextSelected) getActivity();
        }catch(ClassCastException e){
            Log.e("addListDialog", "onAttach: ClassClassException: " + e.getMessage());
        }
    }

    public interface OnEditTextSelected{
        void sendEditText(String editTextAdd);
    }
}

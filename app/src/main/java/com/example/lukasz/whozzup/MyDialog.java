package com.example.lukasz.whozzup;

import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

/**
 * Created by Kashal Silva on 2/12/2016.
 */
public class MyDialog extends DialogFragment implements View.OnClickListener
{
    private EditText mEditText;
    private Button acceptButton;
    private Button rejectButton;
    private Button cancelButton;

    public static interface Callback
    {
        public void accept();
        public void decline();
        public void cancel();
    }

    public MyDialog()
    {
        // Empty constructor required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.dialogfragment, container);
        acceptButton = (Button) view.findViewById(R.id.dialogfragment_acceptbtn);
        rejectButton = (Button) view.findViewById(R.id.dialogfragment_rejectbtn);
        cancelButton = (Button) view.findViewById(R.id.dialogfragment_cancelbtn);
        acceptButton.setOnClickListener(this);
        rejectButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        getDialog().setTitle("dialog_title");
        return view;
    }

    @Override
    public void onClick(View v)
    {
        Callback callback = null;
        try
        {
            callback = (Callback) getTargetFragment();
        }
        catch (ClassCastException e)
        {
            Log.e(this.getClass().getSimpleName(), "Callback of this class must be implemented by target fragment!", e);
            throw e;
        }

        if (callback != null)
        {
            if (v == acceptButton)
            {
                callback.accept();
                this.dismiss();
            }
            else if (v == rejectButton)
            {
                callback.decline();
                this.dismiss();
            }
            else if (v == cancelButton)
            {
                callback.cancel();
                this.dismiss();
            }
        }
    }
}

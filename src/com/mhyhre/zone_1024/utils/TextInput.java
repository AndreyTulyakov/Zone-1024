package com.mhyhre.zone_1024.utils;

import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.ui.activity.BaseGameActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.view.Gravity;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class TextInput {
    
    private static TextInputListener listener = null;

    private static String mValue;

    
    private static boolean cancelPressed;
    private static boolean okPressed;
    private static String resultText;


    protected static CharSequence title;
    protected static CharSequence message;
    
    
    private TextInput() {
    }

    public static void setListener(TextInputListener listener) {
        TextInput.listener = listener;
    }

    public static void showTextInput(final String inTitle, final String inMessage, final BaseGameActivity context) {
        
        title = inTitle;
        message = inMessage;
        
        cancelPressed = false;
        okPressed = false;
        resultText = "";
        
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final AlertDialog.Builder alert = new AlertDialog.Builder(context);

                alert.setTitle(TextInput.title);
                alert.setMessage(TextInput.message);

                final EditText editText = new EditText(context);
                editText.setTextSize(20f);
                editText.setText(TextInput.mValue);
                editText.setGravity(Gravity.CENTER_HORIZONTAL);
                alert.setView(editText);

                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        resultText = editText.getText().toString();
                        okPressed = true;
                        
                        if(listener != null) {
                            listener.textChanged(resultText);
                        }
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        cancelPressed = true;
                        resultText = "";
                        
                        if(listener != null) {
                            listener.textChanged(resultText);
                        }
                    }
                });

                final AlertDialog dialog = alert.create();
                dialog.setOnShowListener(new OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        editText.requestFocus();
                        final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
                dialog.show();
            }
        });
    }

    public static boolean isCancelPressed() {
        return cancelPressed;
    }

    public static boolean isOkPressed() {
        return okPressed;
    }

    public static String getResultText() {
        return resultText;
    }

}

package com.assignment.e_wallet_project;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class Utils {

    public static void showDialog(Context context, String transactionAmount){
        AlertDialog.Builder resultBuilder = new AlertDialog.Builder(context);
        resultBuilder
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setCancelable(true)
                .setTitle("Transaction Complete")
                .setMessage("You paid " + transactionAmount + " dollar(s)");
        AlertDialog resultDialog = resultBuilder.create();
        resultDialog.show();
    }
}

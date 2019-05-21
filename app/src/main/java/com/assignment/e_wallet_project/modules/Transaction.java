package com.assignment.e_wallet_project.modules;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.assignment.e_wallet_project.HomeActivity;
import com.assignment.e_wallet_project.R;
import com.assignment.e_wallet_project.repository.dataRepository;
import com.assignment.e_wallet_project.signIn.SignIn;
import com.assignment.e_wallet_project.model.Cards;
import com.assignment.e_wallet_project.databinding.ActivityTransactionBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.assignment.e_wallet_project.adpater.ListAdapter;

import org.json.JSONException;
import org.json.JSONObject;


public class Transaction extends AppCompatActivity implements View.OnClickListener{
    private int[] listFrame = {R.drawable.pay_and_receive, R.drawable.scan, R.drawable.cards};
    private int[] listIcon = {R.drawable.pay_receive_icon, R.drawable.scan_icon, R.drawable.cards_icon};
    private IntentIntegrator qrScan;
    final Context context = this;
    private TextView balance;
    private String ownPhone, transactionAmount;
    private ActivityTransactionBinding binding;
    private ProgressDialog progressbar;
    private FirebaseAuth firebaseAuth;
    private dataRepository repository;





    private static final String TAG = "track Phone";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transaction);



        //initiate the firebase
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, SignIn.class));
        }

        if(firebaseAuth.getCurrentUser() != null){
            progressbar = new ProgressDialog(this);
            progressbar.setMessage("Loading ...");
            progressbar.show();
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();
        String email = user.getEmail();
        String[] parts = email.split("a");
        ownPhone = parts[0];

        repository = new dataRepository();

        binding.list.setAdapter(new ListAdapter(listFrame, listIcon, context));
        binding.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                if(position == 0){
                    Intent goToPayReceive = new Intent(view.getContext(), payAndReceiveConfirmation.class);
                    startActivity(goToPayReceive);
                }
                else if(position == 1){
                    qrScan.initiateScan();
                }
                else{
                    Intent goToCards = new Intent(view.getContext(), Cards.class);
                    startActivity(goToCards);
                }
            }
        });


        /* font */

        Typeface balanceFont = Typeface.createFromAsset(getAssets(), "roboto_medium.ttf");
        binding.balance.setTypeface(balanceFont);


        Typeface moneyFont = Typeface.createFromAsset(getAssets(), "roboto_medium.ttf");
        binding.money.setTypeface(moneyFont);


        //tranUserNameText.setTxt(tvfirstName);
        Typeface tranUserNameFont = Typeface.createFromAsset(getAssets(), "roboto_medium.ttf");
        binding.tranUserName.setTypeface(tranUserNameFont);


        Typeface greetingFont = Typeface.createFromAsset(getAssets(), "roboto_medium.ttf");
        binding.greeting.setTypeface(greetingFont);


        Typeface signOutButtonFont = Typeface.createFromAsset(getAssets(), "blackjack.otf");
        binding.signOutButton.setTypeface(signOutButtonFont);

        /* listeners */

        binding.signOutButton.setOnClickListener(this);

        qrScan = new IntentIntegrator(this);


        //retrieve the Name of User from database in realtime
        repository.getUserNameFromFireBase(binding.tranUserName, ownPhone);

        //retrieve the balance from database in realtime
        repository.getBalancefromFireBase(binding.balance, ownPhone);
        progressbar.dismiss();





    }

    @Override
    public void onBackPressed() {
        // do not allow android backward button
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.signOutButton) {
            firebaseAuth.signOut();    //sign out
            finish();
            Intent goBackLogIn = new Intent(this, HomeActivity.class);
            startActivity(goBackLogIn);
        }
        else{

        }
    }


    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Transaction Incomplete", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());


                } catch (JSONException e) {
                    e.printStackTrace();
                    String[] separatedMessage = result.getContents().split(" ");
                    final String senderPhone = separatedMessage[0];
                    final String tranAmount = separatedMessage[1];
                    transactionAmount = tranAmount;

                    if(tranAmount.contains(".")){
                        String[] separatedAmount = tranAmount.split("\\.");
                        String inte = separatedAmount[0];
                        String deci = separatedAmount[1];
                        int integ = Integer.parseInt(inte);
                        int decimal = Integer.parseInt(deci);
                        if(deci.length() == 1){decimal = decimal * 10;}
                        int total = integ*100 + decimal;
                        Log.i("The amount is", total+"");
                        repository.transactBalance(ownPhone, senderPhone, total, this, transactionAmount);
                    }
                    else{
                        int total = Integer.parseInt(tranAmount) * 100;
                        repository.transactBalance(ownPhone, senderPhone, total, context, transactionAmount);
                    }
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}

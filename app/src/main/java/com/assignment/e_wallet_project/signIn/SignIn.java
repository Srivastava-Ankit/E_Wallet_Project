package com.assignment.e_wallet_project.signIn;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.assignment.e_wallet_project.HomeActivity;
import com.assignment.e_wallet_project.modules.Transaction;
import com.assignment.e_wallet_project.R;
import com.assignment.e_wallet_project.databinding.ActivitySignInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class SignIn extends AppCompatActivity implements View.OnClickListener{
    private String userNameInput, passwordInput;
    private ProgressDialog signIn;
    final Context context = this;
    private FirebaseAuth firebaseAuth;
    String userEmail = "a@bar.com";
    private ActivitySignInBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(SignIn.this, R.layout.activity_sign_in);

        signIn = new ProgressDialog(this);


        TextView signInWelcomText = (TextView)findViewById(R.id.signInWelcom);
        Typeface signInWelcomTextFont = Typeface.createFromAsset(getAssets(), "roboto_regular.ttf");
        signInWelcomText.setTypeface(signInWelcomTextFont);

        TextView signInUserNameText = (TextView)findViewById(R.id.signInUserName);
        Typeface signInUserNameTextFont = Typeface.createFromAsset(getAssets(), "roboto_regular.ttf");
        signInUserNameText.setTypeface(signInUserNameTextFont);

        TextView signInPasswordText = (TextView)findViewById(R.id.signInPassword);
        Typeface signInPasswordTextFont = Typeface.createFromAsset(getAssets(), "roboto_regular.ttf");
        signInPasswordText.setTypeface(signInPasswordTextFont);

        binding.regEditUserName.setOnClickListener(this);

        Button signInBackwardButton = (Button)findViewById(R.id.signInBackwardButton);
        signInBackwardButton.setOnClickListener(this);

        Button signInForwardButton = (Button)findViewById(R.id.signInForwardButton);
        signInForwardButton.setOnClickListener(this);

        TextView signInRegLinkView = (TextView)findViewById(R.id.signInRegLink);
        signInRegLinkView.setOnClickListener(this);

        //initiate firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.signInBackwardButton) {
            Intent goBackLogInAndReg = new Intent(this, HomeActivity.class);
            startActivity(goBackLogInAndReg);
        }
        else if(view.getId() == R.id.signInForwardButton){
            userNameInput = binding.regEditUserName.getText().toString();
            passwordInput = binding.editPassword.getText().toString();
            Log.i("The userName is ", userNameInput);
            Log.i("The password is ", passwordInput);
            loginUser(userNameInput,passwordInput);
        }
        else if(view.getId() == R.id.signInRegLink){
            Intent goToReg = new Intent(this, Registration.class);
            startActivity(goToReg);
        }
        else if(view.getId() == R.id.regEditUserName){
            binding.regEditUserName.setCursorVisible(true);
        }
    }

    private void loginUser(String userNameInput, String passwordInput){
        String email = "91"+userNameInput.concat(userEmail);   //concat the Phone with a@foo.com

        signIn.setMessage("Signing In ...");
        signIn.show();

        firebaseAuth.signInWithEmailAndPassword(email,passwordInput)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){    // if sign in success
                            finish();
                            Intent goToTransaction = new Intent(getApplicationContext(), Transaction.class);
                            startActivity(goToTransaction);
                        }else{
                            AlertDialog.Builder wrongUserPasswordBuilder = new AlertDialog.Builder(context);
                            wrongUserPasswordBuilder
                                    .setMessage("Your user name and/or password are/is incorrect")
                                    .setTitle("Incorrect Input(s)")
                                    .setCancelable(true)
                                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                            AlertDialog wrongUserPasswordDialog = wrongUserPasswordBuilder.create();
                            wrongUserPasswordDialog.show();

                        }
                        signIn.dismiss();
                    }
                });

    }

    @Override
    public void onBackPressed() {
        // do not allow android backward button
    }


}


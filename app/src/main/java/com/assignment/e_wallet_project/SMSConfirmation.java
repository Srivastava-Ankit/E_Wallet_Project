package com.assignment.e_wallet_project;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.assignment.e_wallet_project.modules.Transaction;
import com.assignment.e_wallet_project.repository.dataRepository;
import com.assignment.e_wallet_project.signIn.Registration;
import com.assignment.e_wallet_project.user.UserInfo;
import com.assignment.e_wallet_project.databinding.ActivitySmsconfirmationBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;


public class SMSConfirmation extends AppCompatActivity implements View.OnClickListener{
    private String firstNameReceived, lastNameReceived, passwordReceived, conPasswordReceived, phoneReceived;
    final Context context = this;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private FirebaseAuth firebaseAuth;
    private String code;
    private ProgressDialog registration;
    public String ext = "a@bar.com";
    private ActivitySmsconfirmationBinding binding;
    private dataRepository respository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_smsconfirmation);

        TextView enterCodeText = (TextView) findViewById(R.id.enterCodeText);
        Typeface enterCodeTextFont = Typeface.createFromAsset(getAssets(), "roboto_medium.ttf");
        enterCodeText.setTypeface(enterCodeTextFont);

        /* get values from Registration */
        firstNameReceived = getIntent().getStringExtra("firstName");
        lastNameReceived = getIntent().getStringExtra("lastName");
        passwordReceived = getIntent().getStringExtra("password");
        conPasswordReceived = getIntent().getStringExtra("conPassword");
        phoneReceived = getIntent().getStringExtra("phone");

        respository = new dataRepository();


        binding.SMSBackwardButton.setOnClickListener(this);


        binding.SMSForwardButton.setOnClickListener(this);


        binding.editSMS.setOnClickListener(this);


        binding.resendLink.setOnClickListener(this);



        if(isSmsSent()) {
            AlertDialog.Builder SMSReceiveBuilder = new AlertDialog.Builder(context);
            SMSReceiveBuilder
                    .setMessage("SMS verification code has been sent to you. Please enter the code.")
                    .setTitle("SMS Confirmation")
                    .setCancelable(true)
                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog SMSReceiveBuilderDialog = SMSReceiveBuilder.create();
            SMSReceiveBuilderDialog.show();
        }

        //initiate the firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        registration = new ProgressDialog(this);

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.SMSBackwardButton) {
            Intent goBackReg = new Intent(this, Registration.class);
            goBackReg.putExtra("firstName", firstNameReceived);
            goBackReg.putExtra("lastName", lastNameReceived);
            goBackReg.putExtra("password", passwordReceived);
            goBackReg.putExtra("conPassword", conPasswordReceived);
            goBackReg.putExtra("phone", phoneReceived);
            startActivity(goBackReg);
        }
        else if(view.getId() == R.id.SMSForwardButton){
            if(binding.editSMS.getText().toString().equals(code)){
                code = "";   //invalidate the code
                registerUser();
            }
            else{
                AlertDialog.Builder wrongCodeBuilder = new AlertDialog.Builder(context);
                wrongCodeBuilder
                        .setMessage("Wrong Code")
                        .setTitle("The code you entered is incorrect. Please enter again!")
                        .setCancelable(true)
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog wrongUserPasswordDialog = wrongCodeBuilder.create();
                wrongUserPasswordDialog.show();
            }
        }
        else if(view.getId() == R.id.editSMS){
            binding.editSMS.setCursorVisible(true);
        }
        else if(view.getId() == R.id.resendLink){
            smsResend();
            Toast.makeText(SMSConfirmation.this, "SMS is sent again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        // do not allow android backward button
    }

    private boolean isSmsSent(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_DENIED) {
                Log.d("permission", "permission denied to SEND_SMS - requesting it");
                String[] permissions = {Manifest.permission.SEND_SMS};
                requestPermissions(permissions, PERMISSION_REQUEST_CODE);
            }
            Random rand = new Random();
            int ran = rand.nextInt(10000);
            code = ran + "";
            Log.i("The SMS is", code);
            SmsManager SMSVerification = SmsManager.getDefault();
            SMSVerification.sendTextMessage(phoneReceived, null, code, null, null);
            return true;
        }
        else{
            Random rand = new Random();
            int ran = rand.nextInt(10000);
            code = ran + "";
            Log.i("The SMS is", code);
            SmsManager SMSVerification = SmsManager.getDefault();
            SMSVerification.sendTextMessage(phoneReceived, null, code , null, null);
            return true;
        }
    }

    public void registerUser() {
        String email = phoneReceived.concat(ext);
        String password = passwordReceived;
        //Log.i(TAG, email);
        //Log.i(TAG, password);
        // Check if email and password is empty
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Toast.makeText(this,"Empty input", Toast.LENGTH_SHORT).show();
            return;
        }

        registration.setMessage("Registering ...");
        registration.show();

        // pass email and password to the Auth
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){     // if registration successful

                            //Saving name, phone and balance in UserInfo Object
                            UserInfo userInfo = new UserInfo(firstNameReceived,lastNameReceived);
                            // upload the User Info Object to the database
                            // root -> User -> Phone -> { User Info }

                            respository.insertDataintoFireBase(userInfo,phoneReceived);
                            Toast.makeText(SMSConfirmation.this, "Registration Success!", Toast.LENGTH_SHORT).show();

                            // finish the activity
                            finish();
                            startActivity(new Intent(getApplicationContext(), Transaction.class));
                        }else{
                            Toast.makeText(SMSConfirmation.this, "Something goes wrong, please resend the SMS", Toast.LENGTH_SHORT).show();
                        }
                        registration.dismiss();
                    }
                });

    }

    private void smsResend(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_DENIED) {
                Log.d("permission", "permission denied to SEND_SMS - requesting it");
                String[] permissions = {Manifest.permission.SEND_SMS};
                requestPermissions(permissions, PERMISSION_REQUEST_CODE);
            }
            Random rand = new Random();
            int ran = rand.nextInt(10000);
            code = ran + "";
            Log.i("The SMS is", code);
            SmsManager SMSVerification = SmsManager.getDefault();
            SMSVerification.sendTextMessage(phoneReceived, null, code, null, null);
        }
        else{
            Random rand = new Random();
            int ran = rand.nextInt(10000);
            code = ran + "";
            Log.i("The SMS is", code);
            SmsManager SMSVerification = SmsManager.getDefault();
            SMSVerification.sendTextMessage(phoneReceived, null, code , null, null);
        }
    }
}

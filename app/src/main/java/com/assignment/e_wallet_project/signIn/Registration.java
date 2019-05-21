package com.assignment.e_wallet_project.signIn;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.assignment.e_wallet_project.HomeActivity;
import com.assignment.e_wallet_project.R;
import com.assignment.e_wallet_project.SMSConfirmation;
import com.assignment.e_wallet_project.databinding.ActivityRegistrationBinding;

import android.databinding.DataBindingUtil;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Registration extends AppCompatActivity implements View.OnClickListener{
    private String[]items = {"91", "0"};
    private String firstNameInput, lastNameInput, passwordInput, conPasswordInput, phoneInput;
    final Context context = this;
    private Pattern onlyChars, passwordCheck, PhoneCheck;
    private ActivityRegistrationBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(Registration.this, R.layout.activity_registration);

        /* restore inputs */
        if(this.getIntent().getExtras() != null){
            binding.regEditUserName.setText(this.getIntent().getStringExtra("firstName"));
            binding.regEditLastName.setText(this.getIntent().getStringExtra("lastName"));
            binding.regEditPassword.setText(this.getIntent().getStringExtra("password"));
            binding.regEditConPassword.setText(this.getIntent().getStringExtra("conPassword"));
            binding.editPhone.setText(this.getIntent().getStringExtra("phone"));

        }

        /* regular express */
        onlyChars = Pattern.compile("^[A-Z]*[a-z]*$");
        passwordCheck = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{4,20}");
        PhoneCheck = Pattern.compile("(0|91)?[7-9][0-9]{9}");


        /* font */
        TextView regSignInButtonText = (TextView)findViewById(R.id.regSignInButton);
        Typeface regSignInButtonTextFont = Typeface.createFromAsset(getAssets(), "roboto_medium.ttf");
        regSignInButtonText.setTypeface(regSignInButtonTextFont);

        /* listeners */
        binding.regEditUserName.setOnClickListener(this);
        binding.regEditLastName.setOnClickListener(this);


        Button regSignInButton = (Button)findViewById(R.id.regSignInButton);
        regSignInButton.setOnClickListener(this);

        Button regBackwardButton = (Button)findViewById(R.id.regBackwardButton);
        regBackwardButton.setOnClickListener(this);

        Button regForwardButton = (Button)findViewById(R.id.regForwardButton);
        regForwardButton.setOnClickListener(this);



    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.regBackwardButton) {
            Intent goBackLogInAndReg = new Intent(this, HomeActivity.class);
            startActivity(goBackLogInAndReg);
        }
        else if(view.getId() == R.id.regSignInButton){
            Intent goToSignIn = new Intent(this, SignIn.class);
            startActivity(goToSignIn);
        }
        else if(view.getId() == R.id.regForwardButton){
            firstNameInput = binding.regEditUserName.getText().toString();
            lastNameInput = binding.regEditLastName.getText().toString();
            passwordInput = binding.regEditPassword.getText().toString();
            conPasswordInput = binding.regEditConPassword.getText().toString();
            phoneInput = "91" + binding.editPhone.getText().toString();
            if(canGoSMSCon(firstNameInput, lastNameInput, passwordInput, conPasswordInput, phoneInput)) {
                Intent goToSMSCon = new Intent(this, SMSConfirmation.class);
                goToSMSCon.putExtra("firstName", firstNameInput);
                goToSMSCon.putExtra("lastName", lastNameInput);
                goToSMSCon.putExtra("password", passwordInput);
                goToSMSCon.putExtra("conPassword", conPasswordInput);
                goToSMSCon.putExtra("phone", phoneInput);
                startActivity(goToSMSCon);
            }
            else{
                AlertDialog.Builder regValidityBuilder = new AlertDialog.Builder(context);
                regValidityBuilder
                        .setMessage("One/Some of your inputs is/are invalid")
                        .setTitle("Proceed rejected")
                        .setCancelable(true)
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog regValidityBuilderDialog = regValidityBuilder.create();
                regValidityBuilderDialog.show();

            }
        }
        else if(view.getId() == R.id.regEditUserName){
            binding.regEditUserName.setCursorVisible(true);
        }
        else if(view.getId() == R.id.regEditLastName){
            binding.regEditLastName.setCursorVisible(true);
        }
    }

    private boolean isFirsttNameValid(String firstNameInput){
        Matcher onlyCharsMatcher = onlyChars.matcher(firstNameInput);
        if(!TextUtils.isEmpty(firstNameInput) && onlyCharsMatcher.find()) {
            return true;
        }
        else {
            binding.regEditUserName.setError("Your first name must start with capital letter and should not be empty");
            return false;
        }
    }

    private boolean isLastNameValid(String lastNameInput){
        Matcher onlyCharsMatcher = onlyChars.matcher(lastNameInput);
        if(!TextUtils.isEmpty(lastNameInput) && onlyCharsMatcher.find()) {
            return true;
        }
        else {
            binding.regEditLastName.setError("Your last name must start with capital letter and should not be empty");
            return false;
        }

    }

    private boolean isPasswordValid(String passwordInput){
        Matcher passwordMatcher = passwordCheck.matcher(passwordInput);
        if(!TextUtils.isEmpty(passwordInput) && passwordMatcher.find()) {
            return true;
        }
        else {
            binding.regEditPassword.setError("Your password should contain digit(s), capital and small letter(s), and should have at least a length of 4");
            return false;
        }

    }

    private boolean isConPasswordValid(String passwordInput, String conPasswordInput){
        Matcher conPasswordMatcher = passwordCheck.matcher(conPasswordInput);
        if(!TextUtils.isEmpty(conPasswordInput) && conPasswordMatcher.find() && conPasswordInput.equals(passwordInput)) {
            return true;
        }
        else {
            binding.regEditConPassword.setError("Your confirm password should be the same as password and should not be empty");
            return false;
        }

    }

    private boolean isPhoneValid(String phoneInput){

            Matcher conPhoneMatcher = PhoneCheck.matcher(phoneInput);
            if(conPhoneMatcher.find()) {
                return true;
            } //&& myPhoneNumber.equals(phoneInput)
            else{
                binding.editPhone.setError("Your phone number is invalid or empty");
                return false;
            }



    }

    private boolean canGoSMSCon(String firstNameInput, String lastNameInput, String passwordInput, String conPasswordInput, String phoneInput){
        if(isFirsttNameValid(firstNameInput) && isLastNameValid(lastNameInput) && isPasswordValid(passwordInput) && isConPasswordValid(passwordInput, conPasswordInput) && isPhoneValid(phoneInput)){return true;}
        else{return false;}
    }

    @Override
    public void onBackPressed() {
        // do not allow android backward button
    }
}
package com.assignment.e_wallet_project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.assignment.e_wallet_project.modules.Transaction;
import com.assignment.e_wallet_project.signIn.Registration;
import com.assignment.e_wallet_project.signIn.SignIn;
import com.assignment.e_wallet_project.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener{
    private ActivityMainBinding binding;
    private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(HomeActivity.this, R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(getApplicationContext(), Transaction.class));
        }



        Typeface signInFont = Typeface.createFromAsset(getAssets(), "blackjack.otf");
        binding.signInButton.setTypeface(signInFont);


        Typeface regButtonFont = Typeface.createFromAsset(getAssets(), "blackjack.otf");
        binding.regButton.setTypeface(regButtonFont);


        Typeface ORTextFont = Typeface.createFromAsset(getAssets(), "roboto_regular.ttf");
        binding.OR.setTypeface(ORTextFont);


        Typeface welcomeTextFont = Typeface.createFromAsset(getAssets(), "roboto_medium.ttf");
        binding.welcome.setTypeface(welcomeTextFont);

        /* set bit flags to draw strike-through */

        binding.leftOR.setPaintFlags(binding.leftOR.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        binding.rightOR.setPaintFlags(binding.rightOR.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        /* onClickListener of the buttons*/

        binding.signInButton.setOnClickListener(this);


        binding.regButton.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.signInButton) {
            Intent goToSignIn = new Intent(this, SignIn.class);
            startActivity(goToSignIn);
        }
        else{
            Intent goToReg = new Intent(this, Registration.class);
            startActivity(goToReg);
        }
    }
}

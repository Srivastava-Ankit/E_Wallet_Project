package com.assignment.e_wallet_project.repository;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.assignment.e_wallet_project.Utils;
import com.assignment.e_wallet_project.modules.Transaction;
import com.assignment.e_wallet_project.user.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class dataRepository {
    private DatabaseReference mRootRef, userRef,phoneRef;
    private DatabaseReference mCurrentUser,mFirstName, mBalance;


    public dataRepository(){
        this.mRootRef = FirebaseDatabase.getInstance().getReference();
        this.userRef = mRootRef.child("User");
        this.phoneRef = userRef.child("Phone Number");
    }



  public void insertDataintoFireBase(UserInfo userInfo, String phoneReceived){

      phoneRef.child(phoneReceived).setValue(userInfo);
      phoneRef.child(phoneReceived).child("card_1").setValue("");
      phoneRef.child(phoneReceived).child("card_2").setValue("");
      phoneRef.child(phoneReceived).child("card_3").setValue("");
      phoneRef.child(phoneReceived).child("card_1_number").setValue("");
      phoneRef.child(phoneReceived).child("card_2_number").setValue("");
      phoneRef.child(phoneReceived).child("card_3_number").setValue("");

  }



  public void getUserNameFromFireBase(final TextView textView, String phoneNumber){
      mCurrentUser = phoneRef.child(phoneNumber);
      mFirstName = mCurrentUser.child("firstName");

      mFirstName.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot dataSnapshot) {
              String firstName = dataSnapshot.getValue(String.class);
              textView.setText(firstName);
          }

          @Override
          public void onCancelled(DatabaseError databaseError) {

          }
      });

  }


  public void getBalancefromFireBase(final TextView textView, String phoneNumber){
      mCurrentUser = phoneRef.child(phoneNumber);
      mBalance = mCurrentUser.child("balanceAmount");

      mBalance.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot dataSnapshot) {
              String tvBalance = dataSnapshot.getValue(String.class);
              int oldBalance = Integer.parseInt(tvBalance);
              int inte = oldBalance/100;
              int deci = oldBalance%100;
              if(deci < 10){
                  String balance = inte + "." + "0" + deci;
                  textView.setText(balance);

              }
              else{
                  String balance = inte + "." + deci;
                  textView.setText(balance);

              }

          }

          @Override
          public void onCancelled(DatabaseError databaseError) {
              //Toast.makeText(Transaction.this, "Cannot find your balance",Toast.LENGTH_SHORT).show();
          }
      });


  }


  public void transactBalance(String phoneToPay, String phoneToReceive, final int Amount,
                                             final Context context, final String transactionAmount){


        //get the database ref from  fromPhone
        DatabaseReference mFromUser = phoneRef.child(phoneToPay);
        final DatabaseReference mFromBalance = mFromUser.child("balanceAmount");
        //get the database ref  from toPhone
        DatabaseReference mToUser = phoneRef.child(phoneToReceive);
        final DatabaseReference mToBalance = mToUser.child("balanceAmount");
        final String sender = phoneToPay;
        final String receiver = phoneToReceive;

        mFromBalance.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //retrieve the balance for fromPhone from database
                int fromBalance = Integer.parseInt(dataSnapshot.getValue(String.class));
                if(sender.equals(receiver)){
                    Toast.makeText(context, "If you do it again, I'll call 999", Toast.LENGTH_SHORT).show(); return;}
                if(fromBalance >= Amount) {   // if enough money, deduct the value
                    fromBalance = fromBalance - Amount;
                }else{
                    Toast.makeText(context, "Not enough money", Toast.LENGTH_SHORT).show();
                    return;
                }
                final int fromBalanceCopy = fromBalance;
                //retrieve the balance for toPhone from database
                mToBalance.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int toBalance = Integer.parseInt(dataSnapshot.getValue(String.class));
                        toBalance = toBalance + Amount;   //add value
                        mFromBalance.setValue(fromBalanceCopy+"");   //update the balance for both account to database
                        mToBalance.setValue(toBalance + "");
                        //inform user transaction has been done
                        Utils.showDialog(context, transactionAmount);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }




}

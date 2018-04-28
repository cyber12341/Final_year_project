package bemo.bemo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import bemo.bemo.Model.Users;

public class Register extends AppCompatActivity implements View.OnClickListener{

    CardView btnSignUp;
    EditText edtname, edtemail, edtpassword, edtphone;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");

        btnSignUp = (CardView)findViewById(R.id.btn_SignUp);

        edtname = (EditText)findViewById(R.id.edtName);
        edtemail = (EditText)findViewById(R.id.edtEmail);
        edtpassword = (EditText)findViewById(R.id.edtPassword);
        edtphone = (EditText)findViewById(R.id.edtPhone);

        btnSignUp.setOnClickListener(this);

    }

    @Override
    public void onClick(final View v) {
        switch (v.getId())
        {
            case R.id.btn_SignUp:
                if(TextUtils.isEmpty(edtname.getText().toString()))
                {
                    Snackbar.make(v, "Please enter your name", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(edtemail.getText().toString()))
                {
                    Snackbar.make(v, "Please enter your Email", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(edtpassword.getText().toString()))
                {
                    Snackbar.make(v, "Please enter your Password", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(edtphone.getText().toString()))
                {
                    Snackbar.make(v, "Please enter your Phone", Snackbar.LENGTH_SHORT).show();
                    return;
                }


                auth.createUserWithEmailAndPassword(edtemail.getText().toString(),edtpassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Users user = new Users();
                        user.setEmail(edtemail.getText().toString());
                        user.setName(edtname.getText().toString());
                        user.setPassword(edtpassword.getText().toString());
                        user.setPhone(edtphone.getText().toString());

                        users.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Snackbar.make(v, "Register Success fully", Snackbar.LENGTH_SHORT).show();
                                Intent register = new Intent(Register.this, MainActivity.class);
                                startActivity(register);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Snackbar.make(v, "Failed "+e.getMessage(), Snackbar.LENGTH_SHORT).show();
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(v, "Failed "+e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    }
                });
                break;


        }
    }
}

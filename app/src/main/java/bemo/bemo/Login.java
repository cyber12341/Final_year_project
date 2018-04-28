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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity implements View.OnClickListener {

    CardView btnSignIn;
    CardView btnSignUp;
    EditText edtemail,edtpassword;

    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");

        btnSignIn = (CardView)findViewById(R.id.btn_SignIn);
        btnSignUp = (CardView)findViewById(R.id.btn_SignUp);

        edtemail = (EditText)findViewById(R.id.edtEmail);
        edtpassword = (EditText)findViewById(R.id.edtPassword);

        btnSignUp.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId())
        {
            case R.id.btn_SignUp:
                Intent register = new Intent(Login.this, Register.class);
                startActivity(register);
                break;

            case R.id.btn_SignIn:

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

                auth.signInWithEmailAndPassword(edtemail.getText().toString(), edtpassword.getText().toString()).
                addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Intent login = new Intent(Login.this, MainActivity.class);
                        startActivity(login);
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
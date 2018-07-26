package bemo.bemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import bemo.bemo.Common.Common;
import bemo.bemo.Model.Users;
import io.paperdb.Paper;

public class Login extends AppCompatActivity implements View.OnClickListener {

    CardView btnSignIn;
    CardView btnSignUp;
    EditText edtemail,edtpassword;

    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pDialog = new ProgressDialog(Login.this);
        pDialog.setMessage("Logging in ...");
        pDialog.setCancelable(false);
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");
        Common.currentuser = new Users();
        btnSignIn = (CardView)findViewById(R.id.btn_SignIn);
        btnSignUp = (CardView)findViewById(R.id.btn_SignUp);

        edtemail = (EditText)findViewById(R.id.edtEmail);
        edtpassword = (EditText)findViewById(R.id.edtPassword);

        btnSignUp.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);

        //Init PaperDB
        Paper.init(this);
        String user = Paper.book().read(Common.user_field);
        String pwd = Paper.book().read(Common.pwd_field);

        if (user != null && pwd !=null)
        {
            if (!TextUtils.isEmpty(user)&&!TextUtils.isEmpty(pwd))
            {
                pDialog.show();
                autoLogin(user,pwd);
            }
        }
    }

    private void autoLogin(String user, String pwd) {
        auth.signInWithEmailAndPassword(user, pwd).
                addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        FirebaseDatabase.getInstance().getReference(Common.user_driver_tbl).child(FirebaseAuth.getInstance().getUid())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Common.currentuser = dataSnapshot.getValue(Users.class);
                                        Intent login = new Intent(Login.this, MainActivity.class);
                                        pDialog.dismiss();
                                        startActivity(login);
                                        finish();
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(findViewById(android.R.id.content), "Failed "+e.getMessage(), Snackbar.LENGTH_SHORT).show();
                pDialog.dismiss();
            }
        });
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
                if (!TextUtils.isEmpty(edtemail.getText().toString()) && !TextUtils.isEmpty(edtpassword.getText().toString()))
                {
                    pDialog.show();
                }
                auth.signInWithEmailAndPassword(edtemail.getText().toString(), edtpassword.getText().toString()).
                addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        FirebaseDatabase.getInstance().getReference(Common.user_driver_tbl).child(FirebaseAuth.getInstance().getUid())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        Paper.book().write(Common.user_field, edtemail.getText().toString());
                                        Paper.book().write(Common.pwd_field, edtpassword.getText().toString());

                                        Common.currentuser = dataSnapshot.getValue(Users.class);
                                        Intent login = new Intent(Login.this, MainActivity.class);
                                        pDialog.dismiss();
                                        startActivity(login);
                                        finish();
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(v, "Failed "+e.getMessage(), Snackbar.LENGTH_SHORT).show();
                        pDialog.dismiss();
                    }
                });

                break;
        }
    }
}
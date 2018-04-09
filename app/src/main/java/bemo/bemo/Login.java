package bemo.bemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Login extends AppCompatActivity implements View.OnClickListener {

    CardView btnSignIn;
    TextView btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnSignIn = (CardView)findViewById(R.id.btn_SignIn);
        btnSignUp = (TextView)findViewById(R.id.btn_SignUp);

        btnSignUp.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_SignUp:
                Intent register = new Intent(Login.this, Register.class);
                startActivity(register);
                break;

            case R.id.btn_SignIn:
                Intent login = new Intent(Login.this, MainActivity.class);
                startActivity(login);
                break;
        }
    }
}

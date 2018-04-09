package bemo.bemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

public class Register extends AppCompatActivity implements View.OnClickListener{
    TextView btnSignIn;
    CardView btnSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnSignUp = (CardView)findViewById(R.id.btn_SignUp);
        btnSignIn = (TextView)findViewById(R.id.btn_SignIn);

        btnSignUp.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_SignUp:
                Intent register = new Intent(Register.this, MainActivity.class);
                startActivity(register);
                break;

            case R.id.btn_SignIn:
                Intent login = new Intent(Register.this, Login.class);
                startActivity(login);
                break;
        }
    }
}

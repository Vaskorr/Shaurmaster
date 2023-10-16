package com.vaskorr.shaurmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button login = findViewById(R.id.login_button);
        TextView registerRedirect = findViewById(R.id.register_redirect);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        registerRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        mAuth = FirebaseAuth.getInstance();
    }

    private void login(){
        String email = ((EditText) findViewById(R.id.email)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();
        if (validate(email, password)){
            // If email and pass are correct, register new account
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("Login", "signInWithEmailAndPassword:success");
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                setContentView(R.layout.activity_main);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("Login", "signInWithEmailAndPassword:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Не получилось. Попробуйте позже",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private boolean validate(String email, String password){
        TextView errorText = findViewById(R.id.error_text);
        if (password.equals("")){
            errorText.setText("Пароль не может быть пустым");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            errorText.setText("Некорректный E-mail адрес");
            return false;
        }
        errorText.setText("");
        return true;
    }
}
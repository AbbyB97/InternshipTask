package youthvine.internshiptask;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wang.avi.AVLoadingIndicatorView;

public class email_login extends AppCompatActivity {

    String email_st, password_st;
    EditText email_ET_obj, password_ET_obj;
    FirebaseAuth mAuth;
    AVLoadingIndicatorView av;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);
        email_ET_obj = findViewById(R.id.login_email);
        password_ET_obj = findViewById(R.id.login_password);
        av = findViewById(R.id.progressD2);
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.loginBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // av.show();
                password_st = password_ET_obj.getText().toString();
                email_st = email_ET_obj.getText().toString();

                //email and password login code

                final boolean isemailvalid = android.util.Patterns.EMAIL_ADDRESS.matcher(email_st).matches();

                if (isemailvalid == true) {
                    mAuth.signInWithEmailAndPassword(email_st, password_st)
                            .addOnCompleteListener(email_login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("", "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        av.hide();
                                        //delete this if else condition if we dont want email verification
                                        if(user.isEmailVerified()) {

                                            Toast.makeText(email_login.this, "email verified login success " + user.getEmail(), Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(email_login.this, welcome.class));

                                        }
                                        else {
                                            Toast.makeText(email_login.this, "email is not verified", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {

                                        // If sign in fails, display a message to the user.
                                        Log.w("", "signInWithEmail:failure", task.getException());
                                        av.hide();

                                        Toast.makeText(email_login.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        //updateUI(null);

                                    }

                                    // ...
                                }
                            });
                }else {
                    Toast.makeText(email_login.this, "Enter valid email", Toast.LENGTH_SHORT).show();
                    av.hide();
                }


            }
        });


    }

}

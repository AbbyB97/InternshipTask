package youthvine.internshiptask;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

public class twiitter_login extends AppCompatActivity {

    FirebaseAuth mAuth;

    private TwitterLoginButton mLoginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twiitter_login);

        Twitter.initialize(this);
        mAuth = FirebaseAuth.getInstance();

    }

}

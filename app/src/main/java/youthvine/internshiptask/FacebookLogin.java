package youthvine.internshiptask;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.Objects;

import static android.provider.ContactsContract.Intents.Insert.EMAIL;

public class FacebookLogin extends AppCompatActivity {
    private TextView tvname;
    private FirebaseAuth mAuth;
    private CallbackManager callbackManager;
    private String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facebbok_login);

        FacebookSdk.sdkInitialize(getApplicationContext());
        mAuth = FirebaseAuth.getInstance();

        tvname = findViewById(R.id.nameTV);
        LoginButton fbbtn = findViewById(R.id.login_button);

        callbackManager = CallbackManager.Factory.create();
        fbbtn.setReadPermissions(Arrays.asList(EMAIL));
        fbbtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //successful access token generation
                handleaccessToken(loginResult);
            }

            @Override
            public void onCancel() {
                Toast.makeText(FacebookLogin.this, "FB Login Canceled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(FacebookLogin.this, "Fb Login Error " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onError: msg:--" + error.getMessage());
            }
        });

    }

    public void handleaccessToken(LoginResult lr) {
        AuthCredential authCredential = FacebookAuthProvider.getCredential(lr.getAccessToken().toString());
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //sign in with fb is successful
                if (task.isSuccessful()) {

                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    assert firebaseUser != null;
                    updateUI(firebaseUser);
                } else {
                    Toast.makeText(FacebookLogin.this, "Facebook Login Failed" + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateUI(FirebaseUser firebaseUser) {
        tvname.setText(firebaseUser.getEmail());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}

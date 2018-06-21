package youthvine.internshiptask;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.wang.avi.AVLoadingIndicatorView;


public class Sign_In extends AppCompatActivity {
    SignInButton button;
    Button fbbtn;
    private final static int RC_SIGN_IN = 2;
    FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
    EditText reg_email, reg_password;
    String user_name_str, user_email_str, user_password_str;
    AVLoadingIndicatorView av;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign__in);

        mAuth = FirebaseAuth.getInstance();

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        fbbtn = findViewById(R.id.button3);
        fbbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FacebbokLogin.class));
            }
        });
        button = findViewById(R.id.googleBtn);
        reg_email = findViewById(R.id.reg_email);
        reg_password = findViewById(R.id.reg_password);
        av = findViewById(R.id.progressD);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                av.show();
                signIn();
            }
        });
        findViewById(R.id.reg_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                av.show();
                user_email_str = reg_email.getText().toString();
                user_password_str = reg_password.getText().toString();

                //user registration using email and password
                //email and password registration code without email verification
                /*
                boolean isemailvalid = android.util.Patterns.EMAIL_ADDRESS.matcher(user_email_str).matches();
                if (isemailvalid == true) {
                    if (user_password_str.length() >= 6) {
                        mAuth = FirebaseAuth.getInstance();
                        mAuth.createUserWithEmailAndPassword(user_email_str, user_password_str)
                                .addOnCompleteListener(Sign_In.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d("", "createUserWithEmail:success");
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            Toast.makeText(Sign_In.this, "User Registered", Toast.LENGTH_SHORT).show();
                                            av.hide();
                                            startActivity(new Intent(Sign_In.this, welcome.class));

                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.e("em pw auth fail", "createUserWithEmail:failure", task.getException());
                                            Toast.makeText(Sign_In.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                            av.hide();
                                        }
                                        // ...
                                    }
                                });
                    } else {
                        Toast.makeText(Sign_In.this, "at least 6 digit password", Toast.LENGTH_SHORT).show();
                        av.hide();
                    }
                } else {
                    Toast.makeText(Sign_In.this, "Please enter valid email address", Toast.LENGTH_SHORT).show();
                    av.hide();
                }*/


                //user registration using email and link


                mAuth.createUserWithEmailAndPassword(user_email_str, user_password_str)
                        .addOnCompleteListener(Sign_In.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("usercreated", "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(Sign_In.this, "user created " + user.getEmail(), Toast.LENGTH_SHORT).show();
                                    user.sendEmailVerification()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d("verification", "Email sent.");
                                                        Toast.makeText(Sign_In.this, "verification email sent", Toast.LENGTH_SHORT).show();
                                                        FirebaseAuth.getInstance().signOut();
                                                    }
                                                }
                                            });
                                    av.hide();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("no usercreated", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(Sign_In.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    av.hide();
                                }
                                // ...
                            }
                        });
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        //if the user is already signed in
        //we will close this activity
        //and take the user to profile activity

        //uncomment this if we want to use just email and password verification
        if (mAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(this, welcome.class));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("Reason ", "Google sign in failed", e);
                Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("begin auth", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Auth Success LOG :", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(Sign_In.this, "user is now signed in", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Sign_In.this, welcome.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Auth fail LOG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(Sign_In.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void gototwitter(View v) {

        startActivity(new Intent(this, twiitter_login.class));
    }

    public void gotologin(View v) {
        Intent next = new Intent(this, email_login.class);
        startActivity(next);
    }


}

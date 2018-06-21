package youthvine.internshiptask;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wang.avi.AVLoadingIndicatorView;

public class welcome extends AppCompatActivity {

    TextView textEmail;
    FirebaseAuth mAuth;

    GoogleSignInClient mGoogleSignInClient;
    ImageView imageView;
    AVLoadingIndicatorView av;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mAuth = FirebaseAuth.getInstance();
        av= findViewById(R.id.progressD3);

        final FirebaseUser user = mAuth.getCurrentUser();

        imageView = findViewById(R.id.imageView);

        String usname;

        if (user.getDisplayName() == null || user.getDisplayName() == "" ) {
            usname="";
        } else {

            usname="\nuser name:"+user.getDisplayName();
        }
        textEmail = findViewById(R.id.textViewEmail);

        textEmail.append(" " + user.getEmail()+usname);

        Glide.with(this)
                .load(user.getPhotoUrl())
                .into(imageView);


        findViewById(R.id.button_signout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                av.show();
                mAuth.getInstance().signOut();
                Toast.makeText(welcome.this,  " logged out", Toast.LENGTH_SHORT).show();
                av.hide();
                startActivity(new Intent(welcome.this, Sign_In.class));

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(welcome.this, Sign_In.class));

        }
    }
}

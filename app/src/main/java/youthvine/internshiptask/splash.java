package youthvine.internshiptask;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class splash extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        /* New Handler to start the Sign_In activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent goto_Sign_in = new Intent(splash.this, Sign_In.class);
                splash.this.startActivity(goto_Sign_in);
                splash.this.finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        }, SPLASH_DISPLAY_LENGTH);


    }

}

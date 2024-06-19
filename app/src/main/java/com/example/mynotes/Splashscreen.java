package com.example.mynotes;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.airbnb.lottie.LottieAnimationView;
import com.example.mynotes.R;

public class Splashscreen extends AppCompatActivity {

    private LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        lottieAnimationView = findViewById(R.id.lottieAnimationView);

        // Start the animation
        lottieAnimationView.playAnimation();

        // Handler to stop animation after 2 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Stop the animation
                lottieAnimationView.cancelAnimation();

                // Start the MainActivity
                Intent intent = new Intent(Splashscreen.this, MainActivity.class);
                startActivity(intent);

                // Finish the Splashscreen activity
                finish();
            }
        }, 3000); // 3 seconds
    }
}

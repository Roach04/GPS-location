package com.project.add;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class Trial extends AppCompatActivity {

    TextView welcome;

    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trial);

        //get the text view to apply the font.
        welcome = (TextView) findViewById(R.id.textView);

        //create a type face have a variable to it and pass in the asset manager and its path.
        Typeface customFont = Typeface.createFromAsset(this.getAssets(),"fonts/DreamsBold.ttf");

        //pass in the font to the appropriate text view.
        welcome.setTypeface(customFont);

        //load the animation and specify the activity and the file to animate this activity
        animation = AnimationUtils.loadAnimation(Trial.this, R.anim.splash);

        //set the animation.
        welcome.setAnimation(animation);

        //set the animation listener
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                //kill the activity
                finish();

                //once it ends, proceed to another activity.
                startActivity(new Intent(getApplicationContext(), Prologue.class));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

}

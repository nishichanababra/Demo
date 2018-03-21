package com.pg.sharedemo;

import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    Button btnClick ;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initObjects();

        initUiControls();
        
        registerForListener();
    }

    private void initObjects() {
    }
    private void initUiControls() {
        btnClick = (Button) findViewById(R.id.btnClick);
        imageView = (ImageView) findViewById(R.id.smallerImageView);
    }

    private void registerForListener() {
        btnClick.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnClick :

                Intent intent = new Intent(this, SecondActivity.class);

                // Get the transition name from the string
                String transitionName = getString(R.string.transition_string);

                // Define the view that the animation will start from

                ActivityOptionsCompat options =

                        ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                                imageView,   // Starting view
                                transitionName    // The String
                        );
                //Start the Intent
                ActivityCompat.startActivity(this, intent, options.toBundle());
                break;
        }
    }
}

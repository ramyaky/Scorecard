package com.example.ramyaky.scorecard;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class GameContinuity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_continuity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("" + bundle.getString("gameName") + " Scorecard");

        if (savedInstanceState == null) {

            DetailsFragment fragment = new DetailsFragment();
            fragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame1, fragment)
                    .commit();
        }

        Button resumeButton = (Button) findViewById(R.id.resumeButton);
        resumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CurrentGameScorecard.class);
                intent.putExtra("GameObject", getIntent().getExtras().getParcelable("GameObject"));
                intent.putExtra("isResume", 1);
                startActivity(intent);
            }
        });

        Button cloneButton = (Button) findViewById(R.id.cloneButton);
        cloneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StartFreshGame.class);
                intent.putExtra("GameObject", getIntent().getExtras().getParcelable("GameObject"));
                intent.putExtra("isClone", 1);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game_continuity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if (id == R.id.action_home) {
            Intent intent = new Intent(getApplicationContext(), StartupScreen.class);
            startActivity(intent);
        }else if (id == R.id.action_history) {
            Intent intent = new Intent(getApplicationContext(), GameHistory.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}

package com.example.ramyaky.scorecard;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;


public class NewGame extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        try {
            //FileOutputStream fo = openFileOutput("testFile.txt",MODE_APPEND);
            File file = getBaseContext().getFileStreamPath("previousGame.txt");
            File filePath = getBaseContext().getFilesDir();
            if(file.exists()){
                Toast message = Toast.makeText(getApplicationContext(), "File exist", Toast.LENGTH_LONG);
                message.show();
            }
            else {
                //Toast.makeText(getApplicationContext(), "File does not exists", Toast.LENGTH_LONG).show();
                //System.out.println(filePath);

            }

        } catch (Exception e) {
            e.printStackTrace();

        }

        Button startFreshButton = (Button) findViewById(R.id.button3);
        startFreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getApplicationContext(), StartFreshGame.class);
                intent2.putExtra("isClone", 0);
                startActivity(intent2);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }
}

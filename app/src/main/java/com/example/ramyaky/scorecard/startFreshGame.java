package com.example.ramyaky.scorecard;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class startFreshGame extends ActionBarActivity {

    HashMap<String,Integer> names = new HashMap<String, Integer>();
    boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_fresh_game);

        final EditText etGameName = (EditText) findViewById(R.id.etGameName);
        final TextView tv = (TextView) findViewById(R.id.tvWinMode);
        RadioGroup winModeGroup = (RadioGroup) findViewById(R.id.radioGroup);
        int checkedMode = winModeGroup.getCheckedRadioButtonId();
        RadioButton winMode = (RadioButton) findViewById(checkedMode);
        final EditText etPlayerName = (EditText) findViewById(R.id.etPlayerName);
        final Button addPlayerNameButton = (Button) findViewById(R.id.addPlayerButton);
        final LinearLayout mainLayout = (LinearLayout) findViewById(R.id.displayPlayers);


        addPlayerNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = etPlayerName.getText().toString();

                if(names.get(name) != null){
                    flag = false;
                }
                else {
                    if(name.length() != 0) {
                        names.put(name, 1);
                        flag = true;
                    }
                    else {
                        etPlayerName.setError("Name should be unique");
                        flag = false;
                    }
                }
                if(flag) {
                    etPlayerName.setError(null);
                    final View displayPlayerNames = getLayoutInflater().inflate(R.layout.player_name, null);
                    TextView tvPlayerName = (TextView) displayPlayerNames.findViewById(R.id.playerName);
                    Button deleteButton = (Button) displayPlayerNames.findViewById(R.id.remove);
                    tvPlayerName.setText(etPlayerName.getText().toString());
                    etPlayerName.setText("");
                    mainLayout.addView(displayPlayerNames);

                    deleteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            names.remove(name);
                            mainLayout.removeView(displayPlayerNames);
                        }
                    });

                }
                else{
                        etPlayerName.setError("Player Name Required");
                }
            }
        });

        Button doneButton = (Button) findViewById(R.id.done);
        doneButton.setOnClickListener(initiateScorecard(names, winMode,etGameName));

    }

    public View.OnClickListener initiateScorecard(final HashMap<String, Integer> namesMap, final RadioButton modeType, final EditText gameName) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gameName.getText().toString().length() == 0) {
                    gameName.setError("Please enter Game Name");

                }else if(namesMap.keySet().size() <= 1){
                    Toast.makeText(getApplicationContext(), "At least 2 players required", Toast.LENGTH_LONG).show();
                }
                else {
                    try {
                        ArrayList<String> namesList = new ArrayList<String>(namesMap.keySet());
                        gameName.setError(null);
                        //JSONArray jsonNamesList = new JSONArray(namesMap.keySet());

                        Timestamp ts = new Timestamp(new Date().getTime());
                        JSONObject scoresJsonObject = new JSONObject();
                        ArrayList<String> scoresList = new ArrayList<String>();
                        ArrayList<String> totalScores = new ArrayList<String>();

                        for (String player : namesList) {
                            scoresJsonObject.put(player, 0);
                        }
                        scoresList.add(scoresJsonObject.toString());
                        totalScores = scoresList;

                        gameObject myGameObject = new gameObject(gameName.getText().toString(), ts.toString(), modeType.getText().toString(), false, namesList, scoresList, totalScores);

                        SQLiteDatabaseHandler dbObj = new SQLiteDatabaseHandler(getApplicationContext());
                        String dateString = myGameObject.getGameStartTime();
                        dbObj.addRecord(dateString, myGameObject, "none");


                        Intent intent = new Intent(getApplicationContext(), currentGameScorecard.class);
                        intent.putExtra("GameObject", myGameObject);
                        startActivity(intent);

                    }catch(Exception e){
                        System.out.println(" Exception raised : " + e);
                        e.printStackTrace();
                    }

                }
            }

        };
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start_fresh_game, menu);
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
        }

        return super.onOptionsItemSelected(item);
    }
}

package com.example.ramyaky.scorecard;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class StartFreshGame extends ActionBarActivity {

    HashMap<String,Integer> names = new HashMap<String, Integer>();
    boolean flag = true;
    RadioButton winMode;
    RadioGroup winModeGroup;
    int gameLimitScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_fresh_game);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final EditText etGameName = (EditText) findViewById(R.id.etGameName);
        //final TextView tv = (TextView) findViewById(R.id.tvWinMode);

        final EditText etPlayerName = (EditText) findViewById(R.id.etPlayerName);
        final Button addPlayerNameButton = (Button) findViewById(R.id.addPlayerButton);
        final LinearLayout mainLayout = (LinearLayout) findViewById(R.id.displayPlayers);
        winModeGroup = (RadioGroup) findViewById(R.id.radioGroup);



        Bundle b = getIntent().getExtras();
        if(b.getInt("isClone") != 1) {
            winModeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    winMode = (RadioButton) findViewById(winModeGroup.getCheckedRadioButtonId());
                    if (winMode.getText().equals("Min")) {
                        final Dialog dialog = new Dialog(StartFreshGame.this);
                        dialog.setContentView(R.layout.custom_limit_dialog);
                        dialog.setTitle("Set Game Maximum Limit");
                        final EditText limitValueEditText = (EditText) dialog.findViewById(R.id.limitValue);
                        Button limitValueButton = (Button) dialog.findViewById(R.id.limitButton);
                        limitValueButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                gameLimitScore = Integer.parseInt(limitValueEditText.getText().toString());
                                dialog.dismiss();
                                Toast.makeText(StartFreshGame.this, "Limit is : " + gameLimitScore, Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialog.show();
                    }
                }

            });
        }
        else {
            GameObject cloneObject = b.getParcelable("GameObject");
            etGameName.setText(cloneObject.getGameName());
            RadioButton maxButton = (RadioButton) findViewById(R.id.Max);
            RadioButton minButton = (RadioButton) findViewById(R.id.Min);
            if(cloneObject.getGameType().equals(maxButton.getText().toString())) {
                maxButton.setChecked(true);
            }else if(cloneObject.getGameType().equals(minButton.getText().toString())) {
                minButton.setChecked(true);
            }
            gameLimitScore = cloneObject.getGameMaxLimit();
            final ArrayList<String> playersList = new ArrayList<String>(cloneObject.getGamePlayers());
            /*View subView = getLayoutInflater().inflate(R.layout.player_name, null);
            TextView subViewPlayerName = (TextView) subView.findViewById(R.id.playerName);
            ImageView subViewDeletePlayer = (ImageView) subView.findViewById(R.id.remove);*/
            mainLayout.removeAllViews();

            for(int i=0; i<playersList.size(); i++) {
                final View subView = getLayoutInflater().inflate(R.layout.player_name, null);
                TextView subViewPlayerName = (TextView) subView.findViewById(R.id.playerName);
                ImageView subViewDeletePlayer = (ImageView) subView.findViewById(R.id.remove);
                subViewPlayerName.setText(playersList.get(i));
                names.put(playersList.get(i), 1);
                final String name = playersList.get(i);
                subViewDeletePlayer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        names.remove(name);
                        mainLayout.removeView(subView);
                    }
                });
                mainLayout.addView(subView);
            }
        }

        addPlayerNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = etPlayerName.getText().toString();

                if (names.get(name) != null) {
                    flag = false;
                } else {
                    if (name.length() != 0) {
                        names.put(name, 1);
                        flag = true;
                    } else {
                        etPlayerName.setError("Name should be unique");
                        flag = false;
                    }
                }
                if (flag) {
                    etPlayerName.setError(null);
                    final View displayPlayerNames = getLayoutInflater().inflate(R.layout.player_name, null);
                    TextView tvPlayerName = (TextView) displayPlayerNames.findViewById(R.id.playerName);
                    ImageView deleteImage = (ImageView) displayPlayerNames.findViewById(R.id.remove);
                    tvPlayerName.setText(etPlayerName.getText().toString());
                    etPlayerName.setText("");
                    mainLayout.addView(displayPlayerNames);

                    deleteImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            names.remove(name);
                            mainLayout.removeView(displayPlayerNames);
                        }
                    });
                } else {
                    etPlayerName.setError("Player Name Required");
                }
            }


        });


        Button doneButton = (Button) findViewById(R.id.done);
        doneButton.setOnClickListener(initiateScorecard(names, etGameName));
    }

    public View.OnClickListener initiateScorecard(final HashMap<String, Integer> namesMap, final EditText gameName) {
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

                        int checkedMode = winModeGroup.getCheckedRadioButtonId();
                        winMode = (RadioButton) findViewById(checkedMode);

                        ArrayList<String> namesList = new ArrayList<String>(namesMap.keySet());
                        gameName.setError(null);
                        Timestamp ts = new Timestamp(new Date().getTime());
                        JSONObject scoresJsonObject = new JSONObject();
                        ArrayList<String> scoresList = new ArrayList<String>();
                        ArrayList<String> totalScores = new ArrayList<String>();

                        for (String player : namesList) {
                            scoresJsonObject.put(player, 0);
                        }
                        scoresList.add(scoresJsonObject.toString());
                        totalScores = scoresList;
                        System.out.println("Printing my mode string : " + winMode.getText().toString());

                        GameObject myGameObject = new GameObject(gameName.getText().toString(), ts.toString(), winMode.getText().toString(), gameLimitScore, false, namesList, scoresList, totalScores);

                        SQLiteDatabaseHandler dbObj = new SQLiteDatabaseHandler(getApplicationContext());
                        String dateString = myGameObject.getGameStartTime();
                        dbObj.addRecord(dateString, myGameObject, "none");

                        Intent intent = new Intent(getApplicationContext(), CurrentGameScorecard.class);
                        intent.putExtra("GameObject", myGameObject);
                        intent.putExtra("isResume", 0);
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

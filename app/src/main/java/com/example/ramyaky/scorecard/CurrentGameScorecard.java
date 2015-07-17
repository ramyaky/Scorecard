package com.example.ramyaky.scorecard;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import java.util.ArrayList;

public class CurrentGameScorecard extends ActionBarActivity {

    public TextView[] playersList;
    public EditText[] currentValueList;
    public Button[] playersTotalValues;
    public TextView[] playersPreviousValues;
    public ImageView[] winnerImages;
    public GameObject gameParcelableObject;
    int gameLimitValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_game_scorecard);

        //TextView tvHeading = (TextView) findViewById(R.id.heading);
        TextView tvRound = (TextView) findViewById(R.id.round);
        TextView tvLimit = (TextView) findViewById(R.id.showLimit);
        TableLayout scoreTable = (TableLayout) findViewById(R.id.scoreTable);
        final Button doneEntering = (Button) findViewById(R.id.doneEntering);
        final Button viewDetails = (Button) findViewById(R.id.viewDetails);
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        Bundle b = getIntent().getExtras();
        int noOfPlayers = 0;

        gameParcelableObject = b.getParcelable("GameObject");

        ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("" + gameParcelableObject.getGameName() + " Scorecard");


        try {
            gameLimitValue = gameParcelableObject.getGameMaxLimit();
            System.out.println("My limit value after resuming immediate is : " + gameLimitValue);
            String gameName = gameParcelableObject.getGameName();
            ArrayList<String> namesList = gameParcelableObject.getGamePlayers();
            final ArrayList<String> scoresList = gameParcelableObject.getGameScores();
            ArrayList<String> totalScoresList = gameParcelableObject.getGameTotalScores();

            noOfPlayers = namesList.size();

            playersList = new TextView[noOfPlayers];
            currentValueList = new EditText[noOfPlayers];
            playersTotalValues = new Button[noOfPlayers];
            playersPreviousValues = new TextView[noOfPlayers];
            winnerImages = new ImageView[noOfPlayers];


            //tvHeading.setText("Scorecard for " + gameName.toUpperCase());
            tvRound.setText("Round " + scoresList.size());
            tvRound.setTextAppearance(getApplicationContext(),R.style.boldText);
            tvLimit.setText("( Max Score : " + gameLimitValue + " )");
            tvLimit.setTextAppearance(getApplicationContext(),R.style.boldText);

            for(int i = 0; i< noOfPlayers; i++){

                TableRow tr = new TableRow(getApplicationContext());

                playersList[i] = new TextView(getApplicationContext());
                currentValueList[i] = new EditText(getApplicationContext());
                playersTotalValues[i] = new Button(getApplicationContext());
                playersPreviousValues[i] = new TextView(getApplicationContext());
                winnerImages[i] = new ImageView(getApplicationContext());

                playersList[i].setText(namesList.get(i));
                playersList[i].setTextSize(20);
                playersList[i].setTextColor(Color.BLACK);
                playersList[i].setId(i);

                currentValueList[i].setInputType(InputType.TYPE_CLASS_NUMBER);
                currentValueList[i].setTextColor(Color.BLACK);
                currentValueList[i].setSelectAllOnFocus(true);
                currentValueList[i].setGravity(Gravity.LEFT);

                currentValueList[i].setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            EditText tmp = (EditText) v;
                            tmp.setSelection(0, tmp.getText().length());
                        }
                    }
                });

                JSONObject scoreObj = new JSONObject(scoresList.get((scoresList.size() -1)));
                playersPreviousValues[i].setText("" + scoreObj.get(namesList.get(i)));
                playersPreviousValues[i].setTextSize(20);
                playersPreviousValues[i].setTextColor(Color.BLACK);
                playersPreviousValues[i].setGravity(Gravity.CENTER);

                JSONObject totalObj = new JSONObject(totalScoresList.get(0));
                playersTotalValues[i].setText("" + totalObj.get(namesList.get(i)));
                playersTotalValues[i].setTextSize(20);
                playersTotalValues[i].setTextColor(Color.BLACK);
                playersTotalValues[i].setGravity(Gravity.CENTER);

                playersTotalValues[i].setBackgroundColor(android.R.drawable.btn_default);

                tr.addView(winnerImages[i]);
                tr.addView(playersList[i]);
                tr.addView(currentValueList[i]);
                tr.addView(playersPreviousValues[i]);
                tr.addView(playersTotalValues[i]);
                tr.setBackgroundResource(android.R.color.transparent);
                tr.setVerticalGravity(Gravity.CENTER_VERTICAL);
                scoreTable.addView(tr);
            }

            // condition to check for game resume or game start. As we are reusing the same activity for
            // both game resume and for starting a new game scorecard.

            if(b.getInt("isResume") == 1) {
                for( int i=0; i< namesList.size(); i++) {
                    if(Integer.parseInt(playersTotalValues[i].getText().toString()) >= gameLimitValue) {
                        currentValueList[i].setFocusable(false);
                        currentValueList[i].setBackgroundColor(Color.rgb(255, 230, 230));
                        playersPreviousValues[i].setTextColor(Color.RED);
                        playersTotalValues[i].setTextColor(Color.RED);
                        playersList[i].setTextColor(Color.RED);
                        currentValueList[i].setText("0");
                    }
                }
            }

            doneEntering.setOnClickListener(updateScores(noOfPlayers, tvRound, tvLimit, namesList, gameParcelableObject.getGameType(), playersList, currentValueList, playersPreviousValues, playersTotalValues, winnerImages));
            viewDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), GameDetails.class);
                    intent.putExtra("gameName", gameParcelableObject.getGameName());
                    intent.putExtra("startTime", gameParcelableObject.getGameStartTime());
                    intent.putExtra("round", scoresList.size());
                    startActivity(intent);
                }
            });

        }catch( Exception e){
            System.out.println(e);
            e.printStackTrace();
        }
    }



    public View.OnClickListener updateScores(final int noOfPlayers, final TextView tvRound, final TextView tvLimit, final ArrayList<String> players, final String mode, final TextView[] playersList, final EditText[] currentValues, final TextView[] previousValues, final TextView[] totalValues, final ImageView[] images) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean allGood = true;

                for(int i=0; i<noOfPlayers; i++) {
                    System.out.println("My limit value is : " + gameLimitValue);
                    if(currentValues[i].getText().toString().length() == 0) {
                        if(Integer.parseInt(totalValues[i].getText().toString()) < gameLimitValue) {
                            currentValues[i].setError("Value required");
                            allGood = false;
                        }else {  allGood = true;   }
                    }
                    else { allGood = true;  }
                }

                if(allGood) {
                    try {

                        ArrayList<String> tmpScores = gameParcelableObject.getGameScores();
                        ArrayList<String> totalScores = new ArrayList<String>();
                        JSONObject tmpObj = new JSONObject();
                        JSONObject tmpObjTotal = new JSONObject();

                        for (int i = 0; i < noOfPlayers; i++) {

                            int currentValue = Integer.parseInt(currentValues[i].getText().toString());
                            tmpObj.put(playersList[i].getText().toString(), currentValue);
                            previousValues[i].setText("" + currentValue);
                            int currentTotalValue = currentValue + Integer.parseInt(totalValues[i].getText().toString());
                            totalValues[i].setText("" + currentTotalValue);
                            tmpObjTotal.put(playersList[i].getText().toString(), totalValues[i].getText().toString());
                            currentValues[i].setText("");
                        }

                        for(int i=0; i<players.size(); i++) {
                            images[i].setImageBitmap(null);
                        }

                        int winnerValue = findHighest(mode,tmpObjTotal, players);
                        ArrayList<String> winners = new ArrayList<String>();

                        for(String p : players){
                            if( (Integer.parseInt(tmpObjTotal.get(p).toString()) == winnerValue) ) {
                                winners.add(p);
                            }else if( Integer.parseInt(tmpObjTotal.get(p).toString()) >= gameLimitValue) {
                                winners.add(Integer.toString(gameLimitValue));
                            }else {
                                winners.add("0");
                            }
                        }

                        for ( int i=0; i<winners.size(); i++) {

                            if((! winners.get(i).equals("0")) && (! winners.get(i).equals(Integer.toString(gameLimitValue)))) {
                                images[i].setImageResource(R.drawable.crown4);
                            }else if(winners.get(i).equals(Integer.toString(gameLimitValue))) {
                                currentValues[i].setFocusable(false);
                                currentValues[i].setBackgroundColor(Color.rgb(255, 230, 230));
                                previousValues[i].setTextColor(Color.RED);
                                totalValues[i].setTextColor(Color.RED);
                                playersList[i].setTextColor(Color.RED);
                                currentValues[i].setText("0");
                            }
                        }

                        tmpScores.add(tmpObj.toString());
                        totalScores.add(tmpObjTotal.toString());
                        gameParcelableObject.setGameScores(tmpScores);
                        gameParcelableObject.setGameTotalScores(totalScores);
                        tvRound.setText("Round " + gameParcelableObject.getGameScores().size());

                        updateDatabase(gameParcelableObject);

                    }catch(Exception e){
                        e.printStackTrace();
                    }

                }

            }
        };
    }

    public int findHighest(String type, JSONObject total, ArrayList<String> players) {
        int value = 0;
        try {
        value = Integer.parseInt(total.get(players.get(0)).toString());


            if(type.equals("Max")) {
                for (int i = 1; i < players.size(); i++) {

                    if ( value < Integer.parseInt(total.get(players.get(i)).toString())) {
                        value = Integer.parseInt(total.get(players.get(i)).toString());
                    }
                }
            }else {
                for (int i = 1; i < players.size(); i++) {
                    if (value > Integer.parseInt(total.get(players.get(i)).toString())) {
                        value = Integer.parseInt(total.get(players.get(i)).toString());
                    }
                }
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
        return value;

    }

    public void updateDatabase(GameObject myObj){
        try{
            SQLiteDatabaseHandler dbObj = new SQLiteDatabaseHandler(getApplicationContext());
            String dateString = myObj.getGameStartTime();
            if(dbObj.checkRecordExists(dateString)) {
                dbObj.updateGameScoreRecord(dateString, myObj, "none");
            }else {
                dbObj.addRecord(dateString, myObj, "none");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_current_game_scorecard, menu);
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

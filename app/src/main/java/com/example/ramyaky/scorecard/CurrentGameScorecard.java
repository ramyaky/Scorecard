package com.example.ramyaky.scorecard;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
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

import org.json.JSONObject;
import java.util.ArrayList;

public class CurrentGameScorecard extends ActionBarActivity {

    public TextView[] playersList;
    public EditText[] currentValueList;
    public Button[] playersTotalValues;
    public TextView[] playersPreviousValues;
    public ImageView[] winnerImages;
    public GameObject gameParcelableObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_game_scorecard);

        TextView tvHeading = (TextView) findViewById(R.id.heading);
        TextView tvRound = (TextView) findViewById(R.id.round);
        TableLayout scoreTable = (TableLayout) findViewById(R.id.scoreTable);
        final Button doneEntering = (Button) findViewById(R.id.doneEntering);
        final Button viewDetails = (Button) findViewById(R.id.viewDetails);
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        Bundle b = getIntent().getExtras();
        int noOfPlayers = 0;

        gameParcelableObject = b.getParcelable("GameObject");

        try {

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

            tvHeading.setText("Scorecard for " + gameName.toUpperCase());
            tvRound.setText("Round " + scoresList.size());

            for(int i = 0; i< noOfPlayers; i++){

                TableRow tr = new TableRow(getApplicationContext());

                //Log.d("MyValuesForChecking", "i value is" + i);
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
                //currentValueList[i].setText("0");
                currentValueList[i].setTextColor(Color.BLACK);
                currentValueList[i].setSelectAllOnFocus(true);

                currentValueList[i].setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(hasFocus) {
                            EditText tmp = (EditText) v;
                            tmp.setSelection(0, tmp.getText().length());
                        }
                    }
                });

                JSONObject scoreObj = new JSONObject(scoresList.get((scoresList.size() -1)));
                playersPreviousValues[i].setText("" + scoreObj.get(namesList.get(i)));
                playersPreviousValues[i].setTextSize(20);
                playersPreviousValues[i].setTextColor(Color.BLACK);

                JSONObject totalObj = new JSONObject(totalScoresList.get(0));
                playersTotalValues[i].setText("" + totalObj.get(namesList.get(i)));
                playersTotalValues[i].setTextSize(20);
                playersTotalValues[i].setTextColor(Color.BLACK);

                playersTotalValues[i].setBackgroundColor(android.R.drawable.btn_default);

                tr.addView(winnerImages[i]);
                tr.addView(playersList[i]);
                tr.addView(currentValueList[i]);
                tr.addView(playersPreviousValues[i]);
                tr.addView(playersTotalValues[i]);

                scoreTable.addView(tr);

            }

            doneEntering.setOnClickListener(updateScores(noOfPlayers, tvRound, namesList, gameParcelableObject.getGameType(), playersList, currentValueList, playersPreviousValues, playersTotalValues, winnerImages));
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

            /*viewDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), CurrentGameScorecard.class);
                    intent.putExtra("GameObject", gameParcelableObject);
                    startActivity(intent);
                }
            });*/

        }catch( Exception e){
            System.out.println(e);
            e.printStackTrace();
        }
    }



    public View.OnClickListener updateScores(final int noOfPlayers, final TextView tvRound, final ArrayList<String> players, final String mode, final TextView[] playersList, final EditText[] currentValues, final TextView[] previousValues, final TextView[] totalValues, final ImageView[] images) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean allGood = true;

                for(int i=0; i<noOfPlayers; i++) {
                    if(currentValues[i].getText().toString().length() == 0) {
                        currentValues[i].setError("Value required");
                        allGood = false;
                        break;
                    }
                    else{
                        allGood = true;
                    }
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

                        int winnerValue = findHighest(mode,tmpObjTotal, players);
                        ArrayList<String> winners = new ArrayList<String>();

                        for(String p : players){
                            if( Integer.parseInt(tmpObjTotal.get(p).toString()) == winnerValue ) {
                                winners.add(p);
                            }else {
                                winners.add("0");
                            }
                        }

                        for(int i=0; i<players.size(); i++) {
                            images[i].setImageBitmap(null);
                        }

                        for ( int i=0; i<winners.size(); i++) {

                            if(! winners.get(i).equals("0")) {
                                images[i].setImageResource(R.drawable.crown);
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

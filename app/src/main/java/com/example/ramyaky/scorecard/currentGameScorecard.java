package com.example.ramyaky.scorecard;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class currentGameScorecard extends ActionBarActivity {

    public TextView[] playersList;
    public EditText[] currentValueList;
    public Button[] playersTotalValues;
    public TextView[] playersPreviousValues;
    //public JSONObject gameJsonObject;
    public gameObject gameParcelableObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_game_scorecard);

        TextView tvHeading = (TextView) findViewById(R.id.heading);
        TextView tvRound = (TextView) findViewById(R.id.round);
        TableLayout scoreTable = (TableLayout) findViewById(R.id.scoreTable);
        final Button doneEntering = (Button) findViewById(R.id.doneEntering);
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        Bundle b = getIntent().getExtras();
        int noOfPlayers = 0;

        gameParcelableObject = b.getParcelable("GameObject");
        /*gameObject gameParcelableObject = b.getParcelable("GameObject");
        System.out.println("----------------- Printing my values -----------------");
        System.out.println(gameParcelableObject.getGameName());
        System.out.println(gameParcelableObject.getGamePlayers());
        System.out.println(gameParcelableObject.getGameScores());

        for(String value : gameParcelableObject.getGameScores()){
            try {
                JSONObject j = new JSONObject(value);
                System.out.println("Printing my actual json : " + j);
            }catch(Exception e){
                e.printStackTrace();
            }
        } */


        try {
            //gameObject gameParcelableObject = b.getParcelable("GameObject");
            //gameJsonObject = new JSONObject(b.getString("GameJsonObject"));

            /*String gameName = gameJsonObject.getString("GameName");
            JSONArray namesList = gameJsonObject.getJSONArray("Players");
            System.out.println("Printing namesList : " + namesList);
            JSONArray scoresList = gameJsonObject.getJSONArray("Scores");
            noOfPlayers = namesList.length();
            System.out.println("Printing size of players list : " + noOfPlayers); */

            String gameName = gameParcelableObject.getGameName();
            ArrayList<String> namesList = gameParcelableObject.getGamePlayers();
            ArrayList<String> scoresList = gameParcelableObject.getGameScores();
            noOfPlayers = namesList.size();

            playersList = new TextView[noOfPlayers];
            currentValueList = new EditText[noOfPlayers];
            playersTotalValues = new Button[noOfPlayers];
            playersPreviousValues = new TextView[noOfPlayers];

            tvHeading.setText("Scorecard for " + gameName.toUpperCase());
            tvRound.setText("Round " + scoresList.size());

            for(int i = 0; i< noOfPlayers; i++){

                TableRow tr = new TableRow(getApplicationContext());

                //Log.d("MyValuesForChecking", "i value is" + i);
                playersList[i] = new TextView(getApplicationContext());
                currentValueList[i] = new EditText(getApplicationContext());
                playersTotalValues[i] = new Button(getApplicationContext());
                playersPreviousValues[i] = new TextView(getApplicationContext());

                playersList[i].setText(namesList.get(i));
                playersList[i].setTextSize(20);
                playersList[i].setTextColor(Color.BLACK);
                playersList[i].setId(i);

                currentValueList[i].setInputType(InputType.TYPE_CLASS_NUMBER);
                currentValueList[i].setText("0");
                currentValueList[i].setTextColor(Color.BLACK);
                currentValueList[i].setSelectAllOnFocus(true);

                final int j = i;
                currentValueList[i].setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(hasFocus) {
                            EditText tmp = (EditText) v;
                            tmp.setSelection(0, tmp.getText().length());
                        }
                    }
                });

                playersPreviousValues[i].setText("0");
                playersPreviousValues[i].setTextSize(20);
                playersPreviousValues[i].setTextColor(Color.BLACK);

                playersTotalValues[i].setText("0");
                playersTotalValues[i].setTextSize(20);
                playersTotalValues[i].setTextColor(Color.BLACK);

                playersTotalValues[i].setBackgroundColor(android.R.drawable.btn_default);

                tr.addView(playersList[i]);
                tr.addView(currentValueList[i]);
                tr.addView(playersPreviousValues[i]);
                tr.addView(playersTotalValues[i]);

                scoreTable.addView(tr);
                //i++;
            }

            doneEntering.setOnClickListener(updateScores(noOfPlayers, tvRound, playersList, currentValueList, playersPreviousValues, playersTotalValues));

        }catch( Exception e){
            System.out.println(e);
            e.printStackTrace();
        }
    }


    public View.OnClickListener updateScores(final int noOfPlayers, final TextView tvRound, final TextView[] playersList, final EditText[] currentValues, final TextView[] previousValues, final TextView[] totalValues ) {
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

                        //JSONArray tmpScores = gameJsonObject.getJSONArray("Scores");
                        ArrayList<String> tmpScores = gameParcelableObject.getGameScores();
                        JSONObject totalScores = new JSONObject();
                        JSONObject tmpObj = new JSONObject();

                        for (int i = 0; i < noOfPlayers; i++) {

                            int currentValue = Integer.parseInt(currentValues[i].getText().toString());
                            tmpObj.put(playersList[i].getText().toString(), currentValue);
                            previousValues[i].setText("" + currentValue);
                            int currentTotalValue = currentValue + Integer.parseInt(totalValues[i].getText().toString());
                            totalValues[i].setText("" + currentTotalValue);
                            totalScores.put(playersList[i].getText().toString(), totalValues[i].getText().toString());
                            currentValues[i].setText("0");
                        }

                        /*tmpScores.put(tmpObj);
                        gameJsonObject.put("Scores", tmpScores);
                        gameJsonObject.put("TotalScores", totalScores);

                        tvRound.setText("Round " + gameJsonObject.getJSONArray("Scores").length());
                        System.out.println("My JSON : " + gameJsonObject);
                        updateDatabase(gameJsonObject); */

                        tmpScores.add(tmpObj.toString());
                        gameParcelableObject.setGameScores(tmpScores);
                        tvRound.setText("Round " + gameParcelableObject.getGameScores().size());
                        updateDatabase(gameParcelableObject);

                    }catch(Exception e){
                        e.printStackTrace();
                    }

                }

            }
        };
    }

    public void updateDatabase(gameObject myObj){
        try{
            System.out.println("Creating database handler");
            SQLiteDatabaseHandler dbObj = new SQLiteDatabaseHandler(getApplicationContext());

            String dateString = myObj.getGameStartTime();
            int y=0;
            if(dbObj.checkRecordExists(dateString)) {
                y = 2;
                System.out.println("Updating the existing Record");
                dbObj.updateGameScoreRecord(dateString, myObj, "none");
            }else {
                y=3;
                dbObj.addRecord(dateString, myObj, "none");
            }
            System.out.println("Value of Y is : " +y);
            dbObj.getAllRecords();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /*public void updateDatabase(JSONObject gameJsonObject){

        try {
            System.out.println("Creating database handler");
            SQLiteDatabaseHandler dbObj = new SQLiteDatabaseHandler(getApplicationContext());

            String dateString = gameJsonObject.getString("StartTime");
            int y = 0;
            if(dbObj.checkRecordExists(dateString)) {
                y=2;
                System.out.println("Updating the existing Record");
                dbObj.updateRecord(dateString,gameJsonObject,"none");
            }else {
                y=3;
                dbObj.addRecord(dateString, gameJsonObject, "none");
            }
            System.out.println("Value of Y is : " +y);
            dbObj.getAllRecords();


        }catch(Exception e) {
            e.printStackTrace();
            System.out.println("Error Message : " + e);
        }
    } */

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

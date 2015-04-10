package com.example.ramyaky.scorecard;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class gameDetails extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_details);
        if (savedInstanceState == null) {

            PlaceholderFragment fragment = new PlaceholderFragment();
            fragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game_details, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_game_deatils, container, false);
            //TextView tv = (TextView) rootView.findViewById(R.id.mystring);

            //tv.setText(getArguments().getString("gameName"));
            SQLiteDatabaseHandler dbObj = new SQLiteDatabaseHandler(getActivity());
            gameObject detailsObject = dbObj.getGameRecord(getArguments().getString("startTime"), getArguments().getString("gameName"));
            TextView tv = (TextView) rootView.findViewById(R.id.mystring);
            tv.setText(detailsObject.getGamePlayers().get(1));

            TableLayout.LayoutParams tableRowParams=
                    new TableLayout.LayoutParams
                            (TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);

            int leftMargin=10;
            int topMargin=2;
            int rightMargin=10;
            int bottomMargin=2;

            tableRowParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);

            TableLayout detailsTable = (TableLayout) rootView.findViewById(R.id.detailsTable);
            TableRow headerRow = new TableRow(getActivity());
            headerRow.setLayoutParams(tableRowParams);
            TextView tRound = new TextView(getActivity());
            tRound.setText("Round");
            tRound.setPadding(20,20,20,20);
            headerRow.addView(tRound);


            try {

                ArrayList<String> playersList = detailsObject.getGamePlayers();
                ArrayList<String> scoresList = detailsObject.getGameScores();
                //JSONArray scoresList = new JSONArray(tmpScoresList.get(0));
                System.out.println("Value of my ScoreList : " +scoresList);

                for(int i =0; i< playersList.size(); i++) {
                    TextView t = new TextView(getActivity());
                    t.setText(playersList.get(i));
                    t.setPadding(20,20,20,20);
                    headerRow.addView(t);
                }
                detailsTable.addView(headerRow);

                TextView[] roundTextViews = new TextView[getArguments().getInt("round")];
                TextView[] scoreTextViews = new TextView[playersList.size()];

                System.out.println("Printing my round number : " + getArguments().getInt("round"));
                for (int i=0; i<getArguments().getInt("round"); i++) {
                    System.out.println("Inside Round");
                    TableRow tr = new TableRow(getActivity());
                    tr.setLayoutParams(tableRowParams);
                    roundTextViews[i] = new TextView(getActivity());
                    roundTextViews[i].setText("" + (i+1));
                    roundTextViews[i].setPadding(20,20,20,20);
                    tr.addView(roundTextViews[i]);
                    System.out.println("Printing ScoresList value : " + scoresList.get(i+1));
                    JSONObject scoreObject = new JSONObject(scoresList.get(i+1));

                    for(int j=0; j<playersList.size(); j++) {
                        scoreTextViews[j] = new TextView(getActivity());
                        scoreTextViews[j].setText("" + scoreObject.getString(playersList.get(j)));
                        scoreTextViews[j].setPadding(20,20,20,20);
                        tr.addView(scoreTextViews[j]);
                    }
                    detailsTable.addView(tr);
                }


            }catch (Exception e) {
                e.printStackTrace();
            }
            return rootView;
        }
    }
}

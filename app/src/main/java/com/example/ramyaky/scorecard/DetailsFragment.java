package com.example.ramyaky.scorecard;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ramyaky on 4/10/15.
 */
public class DetailsFragment extends Fragment {

    public DetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_game_deatils, container, false);

        SQLiteDatabaseHandler dbObj = new SQLiteDatabaseHandler(getActivity());
        GameObject detailsObject = dbObj.getGameRecord(getArguments().getString("startTime"), getArguments().getString("gameName"));
        TextView tv = (TextView) rootView.findViewById(R.id.gameHeading);

        String winnerString = "";
        for(int i=0; i<detailsObject.getGameWinners().size(); i++){
            winnerString += detailsObject.getGameWinners().get(i) + ",";
        }
        tv.setText("" + winnerString.substring(0,winnerString.length()-1));
        tv.setGravity(Gravity.CENTER_HORIZONTAL);
        TableLayout.LayoutParams tableRowParams=
                new TableLayout.LayoutParams
                        (TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);

        int leftMargin=2;
        int topMargin=2;
        int rightMargin=2;
        int bottomMargin=2;
        tableRowParams.gravity = Gravity.CENTER_HORIZONTAL;

        tableRowParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);

        TableLayout detailsTable = (TableLayout) rootView.findViewById(R.id.detailsTable);
        detailsTable.setBackgroundColor(Color.rgb(224,224,224));
        TableRow headerRow = new TableRow(getActivity());
        headerRow.setLayoutParams(tableRowParams);
        headerRow.setBackgroundColor(Color.rgb(95, 174, 217));
        TextView tRound = new TextView(getActivity());
        tRound.setText("Round");
        //tRound.setPadding(0, 20, 20, 20);

        headerRow.addView(tRound);

        try {

            ArrayList<String> playersList = detailsObject.getGamePlayers();
            ArrayList<String> scoresList = detailsObject.getGameScores();

            for(int i =0; i< playersList.size(); i++) {
                TextView t = new TextView(getActivity());
                t.setText(playersList.get(i));
                t.setPadding(20,20,20,20);
                headerRow.addView(t);
            }
            detailsTable.addView(headerRow);

            TextView[] roundTextViews = new TextView[getArguments().getInt("round")];
            TextView[] scoreTextViews = new TextView[playersList.size()];
            boolean isListings = false;

            for (int i=0; i<getArguments().getInt("round") - 1; i++) {
                isListings = true;
                TableRow tr = new TableRow(getActivity());

                if(i%2 == 0) {
                    tr.setBackgroundColor(Color.rgb(240,240,240));
                }
                tr.setLayoutParams(tableRowParams);
                roundTextViews[i] = new TextView(getActivity());
                roundTextViews[i].setText("#" + (i+1));
                //roundTextViews[i].setPadding(0,20,20,20);
                roundTextViews[i].setTypeface(null, Typeface.BOLD_ITALIC);
                roundTextViews[i].setGravity(Gravity.CENTER_HORIZONTAL);
                tr.addView(roundTextViews[i]);

                JSONObject scoreObject = new JSONObject(scoresList.get(i+1));

                for(int j=0; j<playersList.size(); j++) {
                    scoreTextViews[j] = new TextView(getActivity());
                    scoreTextViews[j].setText("" + scoreObject.getString(playersList.get(j)));
                    scoreTextViews[j].setPadding(20,20,20,20);
                    scoreTextViews[j].setGravity(Gravity.CENTER_HORIZONTAL);
                    scoreTextViews[j].setTextAppearance(getActivity(), R.style.playerScoreText);
                    tr.addView(scoreTextViews[j]);
                }
                detailsTable.addView(tr);
            }

            if(isListings){
                ArrayList<String> totalScoresList = detailsObject.getGameTotalScores();
                //System.out.println("printing Total Scores List : " + totalScoresList);
                JSONObject totalObject = new JSONObject(totalScoresList.get(0));
                TableRow trEmpty = new TableRow(getActivity());
                detailsTable.addView(trEmpty);
                TableRow tr = new TableRow(getActivity());
                if((getArguments().getInt("round")%2 != 0)) {
                    tr.setBackgroundColor(Color.rgb(240,240,240));
                }
                TextView tTotal = new TextView(getActivity());
                tTotal.setText("Total");
                tTotal.setGravity(Gravity.CENTER_HORIZONTAL);
                tTotal.setTextAppearance(getActivity(), R.style.boldText);
                tr.addView(tTotal);
                for(int i=0; i<playersList.size(); i++){
                    TextView textView = new TextView(getActivity());
                    textView.setGravity(Gravity.CENTER_HORIZONTAL);
                    textView.setText("" + totalObject.get(playersList.get(i)));
                    textView.setTextAppearance(getActivity(), R.style.boldText);
                    textView.setPadding(20,20,20,20);
                    tr.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
                    tr.addView(textView);

                }
                detailsTable.addView(tr);
            }


        }catch (Exception e) {
            e.printStackTrace();
        }
        return rootView;
    }
}
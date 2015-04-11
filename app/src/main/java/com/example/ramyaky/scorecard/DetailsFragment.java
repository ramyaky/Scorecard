package com.example.ramyaky.scorecard;

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

        tv.setText("Full Scorecard for " + detailsObject.getGameName());

        TableLayout.LayoutParams tableRowParams=
                new TableLayout.LayoutParams
                        (TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);

        int leftMargin=10;
        int topMargin=2;
        int rightMargin=10;
        int bottomMargin=2;
        tableRowParams.gravity = Gravity.CENTER_HORIZONTAL;

        tableRowParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);

        TableLayout detailsTable = (TableLayout) rootView.findViewById(R.id.detailsTable);
        TableRow headerRow = new TableRow(getActivity());
        headerRow.setLayoutParams(tableRowParams);
        TextView tRound = new TextView(getActivity());
        tRound.setText("Round");
        tRound.setPadding(20, 20, 20, 20);

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

            if(isListings){
                ArrayList<String> totalScoresList = detailsObject.getGameTotalScores();
                System.out.println("printing Total Scores List : " + totalScoresList);
                JSONObject totalObject = new JSONObject(totalScoresList.get(0));
                TableRow trEmpty = new TableRow(getActivity());
                detailsTable.addView(trEmpty);
                TableRow tr = new TableRow(getActivity());
                TextView tTotal = new TextView(getActivity());
                tTotal.setText("Total");
                tTotal.setTextAppearance(getActivity(), R.style.boldText);
                tr.addView(tTotal);
                for(int i=0; i<playersList.size(); i++){
                    TextView textView = new TextView(getActivity());
                    System.out.println("Printing individual value : " + totalObject.get(playersList.get(i)));
                    textView.setText("" + totalObject.get(playersList.get(i)));
                    textView.setTextAppearance(getActivity(), R.style.boldText);
                    textView.setPadding(20,20,20,20);
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
package com.example.ramyaky.scorecard;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;


public class GameDeatilsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        System.out.println("Inside fragment class");
        View view = inflater.inflate(R.layout.fragment_game_deatils, container, false);
        //TableLayout gameDetailsTable = (TableLayout) view.findViewById(R.id.gameDetailsTable);
        //SQLiteDatabaseHandler dbObj = new SQLiteDatabaseHandler(getActivity());
        //gameObject gameRecord = dbObj.getGameRecord(getArguments().getString("startTime"), getArguments().getString("gameName"));
        //System.out.println("Result from DB, game name : " + gameRecord.getGameName());



        // Inflate the layout for this fragment
        return view;
    }

}

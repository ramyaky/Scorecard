package com.example.ramyaky.scorecard;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class gameHistory extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_history);

        scoreCardAdapter gameRecordListAdapter = new scoreCardAdapter();
        ListView gameHistoryListView = (ListView) findViewById(R.id.listView);
        gameHistoryListView.setAdapter(gameRecordListAdapter);

    }

    public class scoreCardAdapter extends BaseAdapter{

        ArrayList<JSONObject> gameRecordslist = getDataforListViews();

        @Override
        public int getCount() {
            return gameRecordslist.size();
        }

        @Override
        public Object getItem(int position) {
            return gameRecordslist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            try {

                if (convertView == null) {
                    LayoutInflater layoutInflater = (LayoutInflater) gameHistory.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = layoutInflater.inflate(R.layout.game_record, parent, false);
                }

                TextView name = (TextView)convertView.findViewById(R.id.gameName);
                TextView time = (TextView)convertView.findViewById(R.id.gameStartTime);

                JSONObject gameRecord = gameRecordslist.get(position);

                //String timeString = gameRecord.getString("time");
                String simpleTimeString = getSimpleTimeString(gameRecord.getString("time"));

                time.setText(simpleTimeString);
                name.setText(gameRecord.getString("name"));

            }catch(Exception e){
                e.printStackTrace();
            }

            return convertView;

        }
    }

    public String getSimpleTimeString(String tsString){
        String timeString = "";

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            Date parsedDate = dateFormat.parse(tsString);
            Timestamp ts = new Timestamp(parsedDate.getTime());
            long gameStartTimeInseconds = ts.getTime() / 1000;
            long currentTimeInSeconds = (new Timestamp(new Date().getTime())).getTime() / 1000;
            long difference = ( currentTimeInSeconds - gameStartTimeInseconds );

            if(difference < 3600) {
                timeString = difference / 60 + " min(s) ago";
            }else if(difference > 3600 && difference < 86400) {

                timeString = difference / (60 * 60) + " hour(s) " + (difference % (60 * 60)) / 60 + " mins ago";
            }else if ( difference > 86400 && difference < 2592000) {
                timeString = difference / ( 60 * 60 * 24) + " days(s) ago";
            }else if( difference > 2592000 && difference < 31104000) {
                timeString = difference / (60 * 60 * 24 * 30) + " months(s) ago";
            }else if( difference > 31104000 ) {
                timeString = difference / (60 * 60 * 24 * 30 * 12) + " years(s) ago";
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return timeString;
    }

    public ArrayList<JSONObject> getDataforListViews(){

        SQLiteDatabaseHandler dbobj = new SQLiteDatabaseHandler(getApplicationContext());
        ArrayList<JSONObject> items = dbobj.getAllRecords();
        System.out.println("Rows : " + items);
        return items;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game_history, menu);
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

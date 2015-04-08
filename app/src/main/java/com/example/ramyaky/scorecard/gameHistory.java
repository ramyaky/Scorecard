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

import java.util.ArrayList;


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

                time.setText(gameRecord.getString("time"));
                name.setText(gameRecord.getString("name"));

            }catch(Exception e){
                e.printStackTrace();
            }

            return convertView;

        }
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

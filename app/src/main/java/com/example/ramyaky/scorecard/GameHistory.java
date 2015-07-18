package com.example.ramyaky.scorecard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLOutput;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class GameHistory extends ActionBarActivity {

    ListView gameHistoryListView;
    scoreCardAdapter gameRecordListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_history);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        gameRecordListAdapter = new scoreCardAdapter();
        gameHistoryListView = (ListView) findViewById(R.id.listView);
        gameHistoryListView.setAdapter(gameRecordListAdapter);
        gameHistoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                GameObject object = gameRecordListAdapter.getGameHistoryObject(position);
                Intent intent = new Intent(getApplicationContext(), GameContinuity.class);
                intent.putExtra("gameName", object.getGameName());
                intent.putExtra("startTime", object.getGameStartTime());
                intent.putExtra("round", object.getGameScores().size());
                intent.putExtra("GameObject", object);
                startActivity(intent);

            }
        });

        gameHistoryListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                gameHistoryListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
                gameHistoryListView.setItemChecked(position, true);
                return true;
            }
        });



        gameHistoryListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                gameRecordListAdapter.updateSelectionArray(position, checked);
                int checkedItemsCount = gameRecordListAdapter.getSelectedIdsCount();

                switch (checkedItemsCount) {
                    case 1:
                        mode.setSubtitle("1 item selected");
                        break;
                    default:
                        mode.setSubtitle("" + checkedItemsCount + " items selected");
                        break;
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.delete_multiple_games, menu);
                mode.setTitle("Select items");

                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return true;
            }

            @Override
            public boolean onActionItemClicked(final ActionMode mode, final MenuItem item) {
                Menu menu = mode.getMenu();
                switch (item.getItemId()) {
                    case R.id.selectAll:
                        /*gameRecordListAdapter.removeSelection();
                        for(int k=0; k<gameRecordListAdapter.getCount(); k++) {
                            gameHistoryListView.setItemChecked(k, true);
                            gameRecordListAdapter.updateSelectionArray(k, true);
                        }*/
                        markSelectionAllItems();
                        mode.setSubtitle("" + gameRecordListAdapter.getSelectedIdsCount() + " items selected");

                        menu.findItem(R.id.selectAll).setVisible(false);
                        menu.findItem(R.id.deselectAll).setVisible(true);

                        return true;

                    case R.id.deselectAll:
                        gameRecordListAdapter.removeSelection();
                        menu.findItem(R.id.deselectAll).setVisible(false);
                        menu.findItem(R.id.selectAll).setVisible(true);
                        mode.setSubtitle("" + gameRecordListAdapter.getSelectedIdsCount() + " items selected");
                        return true;

                    case R.id.delete:
                        AlertDialog.Builder builder = new AlertDialog.Builder(GameHistory.this);
                        builder.setMessage("Do you want to delete selected items ?");

                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteActionMode();
                                mode.finish();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        AlertDialog alertDialog = builder.create();
                        alertDialog.setTitle("Confirmation");
                        alertDialog.show();

                        return true;
                    default:
                        return false;
                }

            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                gameRecordListAdapter.removeSelection();
            }
        });

    }

    public class scoreCardAdapter extends BaseAdapter{

        ArrayList<GameObject> gameRecordslist = getDataforListViews();
        private SparseBooleanArray selectedItemsArray = new SparseBooleanArray();

        public GameObject getGameHistoryObject(int position){
            return gameRecordslist.get(position);
        }

        @Override
        public int getCount() {
            return gameRecordslist.size();
        }

        @Override
        public GameObject getItem(int position) {
            return gameRecordslist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        public void updateSelectionArray(int position, boolean value) {
            if(value) {
                selectedItemsArray.put(position,value);

            }else {
                gameHistoryListView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                selectedItemsArray.delete(position);
            }

            notifyDataSetChanged();

        }

        public void remove(GameObject object) {
            SQLiteDatabaseHandler dbobj = new SQLiteDatabaseHandler(getApplicationContext());
            dbobj.deleteRecord(object.getGameStartTime(), object.getGameName());
        }

        public SparseBooleanArray getSelectedIds() {
            return selectedItemsArray;
        }

        public void refreshDataSet() {
            gameRecordslist = getDataforListViews();
            notifyDataSetChanged();
        }

        public int getSelectedIdsCount() {
            return selectedItemsArray.size();
        }

        // Remove selection after unchecked
        public void  removeSelection() {
            selectedItemsArray = new  SparseBooleanArray();
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            try {

                if (convertView == null) {
                    LayoutInflater layoutInflater = (LayoutInflater) GameHistory.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = layoutInflater.inflate(R.layout.game_record, parent, false);
                }
                // setting the background color trasnparent.
                // This will be helpful when user deselect the item incase of selecting multiple items

                convertView.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                // highlights item(s) upon selection
                if(selectedItemsArray.get(position)){
                    convertView.setBackgroundColor(getResources().getColor(R.color.light_blue));
                }
                TextView name = (TextView)convertView.findViewById(R.id.gameName);
                TextView time = (TextView)convertView.findViewById(R.id.gameStartTime);

                GameObject gameRecord = gameRecordslist.get(position);

                String simpleTimeString = getSimpleTimeString(gameRecord.getGameStartTime());

                time.setText(simpleTimeString);
                name.setText(gameRecord.getGameName());

            }catch(Exception e){
                e.printStackTrace();
            }

            return convertView;

        }
    }

    public void markSelectionAllItems() {
        gameRecordListAdapter.removeSelection();
        for(int k=0; k<gameRecordListAdapter.getCount(); k++) {
            gameHistoryListView.setItemChecked(k, true);
            gameRecordListAdapter.updateSelectionArray(k, true);
        }

    }

    public void deleteActionMode() {

        SparseBooleanArray itemsToDelete = gameRecordListAdapter.getSelectedIds();
        for(int k=0; k< itemsToDelete.size(); k++) {
            if(itemsToDelete.valueAt(k)) {
                gameRecordListAdapter.remove(gameRecordListAdapter.getItem(itemsToDelete.keyAt(k)));
            }
        }
        Toast.makeText(getApplicationContext(), "Successfully deleted selected items", Toast.LENGTH_SHORT).show();
        gameRecordListAdapter.refreshDataSet();

        itemsToDelete.clear();

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

    public ArrayList<GameObject> getDataforListViews(){

        SQLiteDatabaseHandler dbobj = new SQLiteDatabaseHandler(getApplicationContext());
        ArrayList<GameObject> items = dbobj.getAllRecords();
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

        switch (item.getItemId()) {
            case R.id.refreshHistory:
                gameRecordListAdapter.refreshDataSet();
                Toast.makeText(getApplicationContext(), "Done reloading history.", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}

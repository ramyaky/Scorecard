package com.aredevelopments.scorecard;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by ramyaky on 4/1/15.
 */

public class SQLiteDatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "scoreCardManager";
    private static final String TABLE_NAME = "scoreCard";

    String KEY_GAME_START_TIME = "gameStartTime";
    String KEY_GAME_NAME = "gameName";
    String KEY_GAME_WINNER = "gameWinner";
    String KEY_GAME_TYPE = "gameType";
    String KEY_GAME_END_TIME = "gameEndTime";
    String KEY_GAME_PLAYERS = "gamePlayers";
    String KEY_GAME_SCORES = "gameScores";
    String KEY_GAME_IS_END = "gameIsEnd";
    String KEY_GAME_TOTAL_SCORES = "gameTotalScores";
    String KEY_GAME_MAX_LIMIT = "gameMaxLimit";
    String separator = " @&@ ";

    public SQLiteDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        String CREATE_SCORECARD_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_GAME_START_TIME + " TEXT PRIMARY KEY," + KEY_GAME_NAME + " TEXT,"
                + KEY_GAME_WINNER + " TEXT, " + KEY_GAME_TYPE + " TEXT, " + KEY_GAME_END_TIME + " TEXT, "
                + KEY_GAME_PLAYERS + " TEXT, " + KEY_GAME_SCORES + " TEXT, " + KEY_GAME_IS_END + " TEXT, " + KEY_GAME_TOTAL_SCORES + " TEXT, " + KEY_GAME_MAX_LIMIT + " TEXT" + ")";
        db.execSQL(CREATE_SCORECARD_TABLE);
        System.out.println("Successfully created SCORECARD table");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    public boolean checkRecordExists(String date){
        String QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_GAME_START_TIME + " = \"" + date + "\"";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(QUERY,null);
        int count = cursor.getCount();
        cursor.close();

        if(count > 0){
            return true;
        }else {
            return false;
        }

    }

    public String arrayToString(ArrayList<String> st){
        String arrayString = "";

        for(String t : st){
            arrayString = arrayString + t + separator;

        }
        return arrayString;
    }

    public ArrayList<String> stringToArray(String st) {
        ArrayList<String> stringToArray = new ArrayList<String>();
        String[] splitString = st.split(separator);
        for(String t : splitString){
            stringToArray.add(t);
        }
        return stringToArray;
    }

    public void deleteRecord(String date, String gn) {

        SQLiteDatabase db = this.getWritableDatabase();
        System.out.println("Calling delete on : " + gn);
        db.delete(TABLE_NAME, KEY_GAME_START_TIME + "=\"" + date +"\" AND " + KEY_GAME_NAME + "=\"" + gn + "\"",null);
        db.close();

    }

    public void addRecord(String date, GameObject details){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_GAME_START_TIME, date);
        values.put(KEY_GAME_NAME, details.getGameName());
        values.put(KEY_GAME_END_TIME, details.getGameEndTime());
        values.put(KEY_GAME_PLAYERS, arrayToString(details.getGamePlayers()));
        values.put(KEY_GAME_SCORES, arrayToString(details.getGameScores()));
        values.put(KEY_GAME_TYPE, details.getGameType());
        values.put(KEY_GAME_IS_END, details.isGameEnd());
        values.put(KEY_GAME_WINNER, arrayToString(details.getGameWinners()));
        values.put(KEY_GAME_TOTAL_SCORES, arrayToString(details.getGameTotalScores()));
        values.put(KEY_GAME_MAX_LIMIT, Integer.toString(details.getGameMaxLimit()));

        db.insert(TABLE_NAME, null, values);
        db.close();
    }


    public void updateGameScoreRecord(String date, GameObject details){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_GAME_SCORES, arrayToString(details.getGameScores()));
        values.put(KEY_GAME_WINNER, arrayToString(details.getGameWinners()));
        values.put(KEY_GAME_TOTAL_SCORES, arrayToString(details.getGameTotalScores()));

        db.update(TABLE_NAME,values, KEY_GAME_START_TIME + " = ?", new String[] {date} );
    }

    public void getRecord(String date){

    }

    public ArrayList<GameObject> getAllRecords(){
        SQLiteDatabase db = this.getWritableDatabase();

        ArrayList<GameObject> listOfObjects = new ArrayList<GameObject>();

        String QUERY = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(QUERY,null);

        try{
            if(cursor.moveToFirst()) {
                while(!cursor.isAfterLast()) {

                    listOfObjects.add(setObjectAttributes(cursor));
                    cursor.moveToNext();
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        return listOfObjects;
    }

    public GameObject getGameRecord(String gStartTime, String gName){
        SQLiteDatabase db = this.getWritableDatabase();
        GameObject gameRecordDetails = new GameObject();

        String QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_GAME_START_TIME + " = \"" + gStartTime + "\"" + " AND " + KEY_GAME_NAME + " = \"" + gName + "\"";
        Cursor cursor = db.rawQuery(QUERY,null);

        try{
            if(cursor.moveToFirst())
            {
                while(!cursor.isAfterLast()){

                    gameRecordDetails = setObjectAttributes(cursor);
                    cursor.moveToNext();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return gameRecordDetails;

    }

    public GameObject setObjectAttributes(Cursor cur){

        GameObject object = new GameObject();
        try{
            object.setGameStartTime(cur.getString(0));
            object.setGameName(cur.getString(1));
            object.setGameWinners(stringToArray(cur.getString(2)));
            object.setGameType(cur.getString(3));
            object.setGameEndTime(cur.getString(4));
            object.setGamePlayers(stringToArray(cur.getString(5)));
            object.setGameScores(stringToArray(cur.getString(6)));
            object.setGameIsEnd(Boolean.valueOf(cur.getString(7)));
            object.setGameTotalScores(stringToArray(cur.getString(8)));
            object.setGameMaxLimit(Integer.parseInt(cur.getString(9)));
        }catch (Exception e){
            e.printStackTrace();
        }
        return object;
    }
}

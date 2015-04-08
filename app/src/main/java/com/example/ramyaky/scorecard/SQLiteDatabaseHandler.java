package com.example.ramyaky.scorecard;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Currency;

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

    public SQLiteDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        String CREATE_SCORECARD_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_GAME_START_TIME + " TEXT PRIMARY KEY," + KEY_GAME_NAME + " TEXT,"
                + KEY_GAME_WINNER + " TEXT, " + KEY_GAME_TYPE + " TEXT, " + KEY_GAME_END_TIME + " TEXT, "
                + KEY_GAME_PLAYERS + " TEXT, " + KEY_GAME_SCORES + " TEXT, " + KEY_GAME_IS_END + " TEXT, " + KEY_GAME_TOTAL_SCORES + " TEXT" + ")";
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

    public void addRecord(String date, gameObject details, String winner){
        System.out.println("Adding record");
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_GAME_START_TIME, date);
        values.put(KEY_GAME_NAME, details.getGameName());
        values.put(KEY_GAME_END_TIME, details.getGameEndTime());
        values.put(KEY_GAME_PLAYERS, details.getGamePlayers().toString());
        values.put(KEY_GAME_SCORES, details.getGameScores().toString());
        values.put(KEY_GAME_TYPE, details.getGameType());
        values.put(KEY_GAME_IS_END, details.isGameEnd());
        values.put(KEY_GAME_WINNER, winner);
        values.put(KEY_GAME_TOTAL_SCORES, details.getGameTotalScores());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void deleteRecord(String date, String details){

    }

    public void updateGameScoreRecord(String date, gameObject details, String winner){
        System.out.println("Updating Record");
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_GAME_SCORES, details.getGameScores().toString());
        values.put(KEY_GAME_WINNER, winner);
        values.put(KEY_GAME_TOTAL_SCORES, details.getGameTotalScores());

        db.update(TABLE_NAME,values, KEY_GAME_START_TIME + " = ?", new String[] {date} );
    }

    public void getRecord(String date){

    }

    public ArrayList<JSONObject> getAllRecords(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<JSONObject> listOfJsonObjects = new ArrayList<JSONObject>();

        String QUERY = "SELECT " + KEY_GAME_START_TIME + "," + KEY_GAME_NAME + "," + KEY_GAME_WINNER + "," + KEY_GAME_TOTAL_SCORES + "," + KEY_GAME_SCORES + " FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(QUERY,null);

        System.out.println("Total no.of rows in DB : " +cursor.getCount());

        try {
            if (cursor.moveToFirst()) {
                //System.out.println("Has elements");
                while (!cursor.isAfterLast()) {
                    //System.out.println("Creating object");
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("time", cursor.getString(0));
                    jsonObject.put("name", cursor.getString(1));
                    jsonObject.put("winner", cursor.getString(2));
                    jsonObject.put("total",cursor.getString(3));
                    jsonObject.put("scores", cursor.getString(4));
                    listOfJsonObjects.add(jsonObject);
                    cursor.moveToNext();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        cursor.close();
        System.out.println("Sending ietms : " + listOfJsonObjects);
        return listOfJsonObjects;
    }
}

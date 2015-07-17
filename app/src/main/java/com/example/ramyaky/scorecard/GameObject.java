package com.example.ramyaky.scorecard;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by ramyaky on 4/7/15.
 */

public class GameObject implements Parcelable{

    // private variables
    String _gameName;
    String _gameStartTime;
    boolean _gameIsEnd;
    String _gameEndTime = "";
    String _gamePlayerGroups = "";
    String _gameType;
    ArrayList<String> _gamePlayers;
    ArrayList<String> _gameScores;
    ArrayList<String> _gameTotalScores;
    ArrayList<String> _gameWinners;
    int _gameMaxLimit;


    public GameObject() {

    }

    public GameObject(String gName, String gStartTime, String gType, int gMaxLimit, boolean gIsEnd, ArrayList<String> gPlayers, ArrayList<String> gScores, ArrayList<String> gTotalScores) {
        this._gameName = gName;
        this._gameStartTime = gStartTime;
        this._gameType = gType;
        this._gameIsEnd = gIsEnd;
       // this._gameEndTime = "";
        //this._gamePlayerGroups = "";
        this._gamePlayers = gPlayers;
        this._gameScores = gScores;
        this._gameTotalScores = gTotalScores;
        this._gameMaxLimit = gMaxLimit;

    }

    /* All get methods */

    public String getGameName() {
        return this._gameName;
    }

    public String getGameStartTime() {
        return this._gameStartTime;
    }

    public boolean isGameEnd() {
        return this._gameIsEnd;
    }

    public String getGameEndTime() {
        return this._gameEndTime;
    }

    public String getGameType() {
        return this._gameType;
    }

    public String getGamePlayerGroups() {
        return this._gamePlayerGroups;
    }

    public ArrayList<String> getGameTotalScores() { return this._gameTotalScores; }

    public ArrayList<String> getGamePlayers() {
        return this._gamePlayers;
    }

    public ArrayList<String> getGameScores() {
        return this._gameScores;
    }

    public int getGameMaxLimit() { return this._gameMaxLimit; }

    /* All set methods */

    public void setGameEndTime(String gEndTime) {
        this._gameEndTime = gEndTime;
    }

    public void setGamePlayerGroups(String gPlayerGroups) {
        this._gamePlayerGroups = gPlayerGroups;
    }

    public void setGameType(String gType) {
        this._gameType = gType;
    }

    public void setGameStartTime(String gStartTime) { this._gameStartTime = gStartTime; }

    public void setGameName(String gName) {
        this._gameName = gName;
    }

    public void setGameIsEnd(boolean gIsEnd) {
        this._gameIsEnd = gIsEnd;
    }

    public void setGameTotalScores(ArrayList<String> gTotalScores) { this._gameTotalScores = gTotalScores; }

    public void setGamePlayers(ArrayList<String> gPlayers) {
        this._gamePlayers = gPlayers;
    }

    public void setGameScores(ArrayList<String> gScores) {
        this._gameScores = gScores;
    }

    public void setGameMaxLimit(int gMaxLimit) { this._gameMaxLimit = gMaxLimit; }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle b = new Bundle();
        b.putString("GameName", _gameName);
        b.putString("GameStartTime", _gameStartTime);
        b.putString("GameType", _gameType);
        b.putInt("GameMaxLimit", _gameMaxLimit);
        b.putBoolean("IsGameEnd", _gameIsEnd);
        b.putStringArrayList("GamePlayers", _gamePlayers);
        b.putStringArrayList("GameScores", _gameScores);
        b.putStringArrayList("GameTotalScores", _gameTotalScores);


        dest.writeBundle(b);
    }

    public static final Parcelable.Creator<GameObject> CREATOR = new Creator<GameObject>() {
        @Override
        public GameObject createFromParcel(Parcel source) {
            Bundle b = new Bundle();
            b = source.readBundle();

            return new GameObject(b.getString("GameName"), b.getString("GameStartTime"), b.getString("GameType"), b.getInt("GameMaxLimit"), b.getBoolean("IsGameEnd"), b.getStringArrayList("GamePlayers"), b.getStringArrayList("GameScores"), b.getStringArrayList("GameTotalScores"));

        }

        @Override
        public GameObject[] newArray(int size) {
            return new GameObject[0];
        }
    };
}

package com.example.theappthatwillnotbenamed;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ListView;

public class WelcomeActivity extends AppCompatActivity {
    Cursor todoCursor;
    ListView noteList;
    NoteAdapter adapter;
    Button start;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NoteTakingDatabase handler = new NoteTakingDatabase(getApplicationContext());
        // Get the writable database
        SQLiteDatabase db = handler.getWritableDatabase();
        //Get all notes from the database
        todoCursor = db.rawQuery("SELECT * FROM notes", null);
       start = (Button) findViewById(R.id.start_note);
        try {
            if (todoCursor.moveToFirst()) {
                setContentView(R.layout.activity_main);
            }
           else setContentView(R.layout.welcome);

        }
        catch (java.lang.NullPointerException e){
           setContentView(R.layout.welcome);
        }
    }
    public void loadNotesFromDatabase() {
        setContentView(R.layout.activity_main);
        try {
            if (todoCursor.moveToFirst()) {
                setContentView(R.layout.activity_main);
            }
            // Create a new instance of the NoteTakingDatabase
            NoteTakingDatabase handler = new NoteTakingDatabase(getApplicationContext());
            // Get the writable database
            SQLiteDatabase db = handler.getWritableDatabase();
            //Get all notes from the database
            todoCursor = db.rawQuery("SELECT * FROM notes", null);

            // Create an instance of the NoteAdapter with our cursor
            adapter = new NoteAdapter(this, todoCursor, 0);

            // Set the NoteAdapter to the ListView (display all notes from DB)
            noteList.setAdapter(adapter);
            setContentView(R.layout.activity_main);

        }
        catch (java.lang.NullPointerException e){
            setContentView(R.layout.welcome);
        }
        catch (java.lang.RuntimeException e){
            setContentView(R.layout.welcome);
        }
    }
    @Override
    public void onResume(){
        super.onResume();
        loadNotesFromDatabase();
    }

}

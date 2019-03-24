package com.example.theappthatwillnotbenamed;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    Cursor todoCursor;
    ListView noteList;
    NoteAdapter adapter;
    SwipeRefreshLayout swipe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            if (userHasPermission()) {
                loadNotesFromDatabase();
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        0);
            }
            noteList = (ListView) findViewById(R.id.note_list);
            noteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent openNote = new Intent(MainActivity.this, NoteActivity.class);
                    openNote.putExtra("noteId", id);
                    startActivity(openNote);
                }
            });
            swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
            swipe.setOnRefreshListener(this);
            if (todoCursor.moveToFirst())setContentView(R.layout.activity_main);
            else setContentView(R.layout.welcome);
        }
        catch(java.lang.NullPointerException e){
            setContentView(R.layout.welcome);
        }
    }
    public void loadNotesFromDatabase() {
        try {
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
    protected void onDestroy() {
        super.onDestroy();
        // Close database cursor
        if (todoCursor != null) {
            todoCursor.close();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add_note:
                // TODO something
                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean userHasPermission() {
        return ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                    loadNotesFromDatabase();
                } else {
                    // TODO tell the user we need permission for our app to work
                }
                break;
        }
    }
    @Override
    public void onResume(){
        super.onResume();
        loadNotesFromDatabase();
    }

    @Override
    public void onRefresh() {
        loadNotesFromDatabase();
    }
}

package com.example.theappthatwillnotbenamed;

import android.support.v7.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        noteList = (ListView) findViewById(R.id.welcome);

        Button start = (Button) findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openNote = new Intent(WelcomeActivity.this, NoteActivity.class);
                openNote.putExtra("noteId", id);
                startActivity(openNote);
            }
        });
    }
}

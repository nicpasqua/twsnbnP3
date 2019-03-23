package com.example.theappthatwillnotbenamed;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
String imagePath = "";
Button saveNote;
TextView noteTitle;
ImageView noteImage;
SQLiteDatabase db;
boolean isUpdate = false;
int noteId;

public class NoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        // Create a new instance of the NoteTakingDatabase
        NoteTakingDatabase handler = new NoteTakingDatabase(getApplicationContext());
// Get the writable database
        db = handler.getReadableDatabase();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            isUpdate = true;
            noteId = (int) extras.getLong("noteId");
            setNote(noteId);
        }
        saveNote = (Button) findViewById(R.id.create_note);
        noteTitle = (TextView) findViewById(R.id.note_title);
        noteImage = (ImageView) findViewById(R.id.note_image);
        saveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isUpdate) {
                    storeNote(imagePath, noteTitle.getText().toString(), "Description", "Category");
                } else {
                    updateNote(noteId, imagePath, noteTitle.getText().toString(), "Description", "Category");
                }
                finish();
            }
        });
        noteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Use the EasyImage library to open up a chooser to pick an image.
                EasyImage.openChooserWithGallery(NoteActivity.this, "Upload an Image", 0);
            }
        });
    }
    private void updateNote(int noteId, String imagePath, String title, String description, String category) {
        // Create a new instance of the NoteTakingDatabase
        NoteTakingDatabase handler = new NoteTakingDatabase(getApplicationContext());
        // Get the writable database
        SQLiteDatabase db = handler.getWritableDatabase();
        // Store the note in the database
        handler.updateNote(db, noteId, imagePath, title, description, category);
    }

    private void setNote(Integer noteId) {
        // Get note by id
        Cursor cursor = db.rawQuery("SELECT * FROM notes WHERE _id = " + noteId, null);
        cursor.moveToFirst();

        // Set note details to view
        String path = cursor.getString(cursor.getColumnIndexOrThrow("noteImage"));
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        noteImage.setImageBitmap(bitmap);

        // Get the note text from the database as a String
        String noteText = cursor.getString(cursor.getColumnIndexOrThrow("noteText"));
        noteTitle.setText(noteText);

        String noteDescription = cursor.getString(cursor.getColumnIndexOrThrow("noteDescription"));
        String noteCategory = cursor.getString(cursor.getColumnIndexOrThrow("noteCategory"));

        cursor.close();
    }

    public void storeNote(String path, String title, String description, String category) {
        // Create a new instance of the NoteTakingDatabase
        NoteTakingDatabase handler = new NoteTakingDatabase(getApplicationContext());
        // Get the writable database
        SQLiteDatabase db = handler.getWritableDatabase();
        // Store the note in the database
        handler.storeNote(db, path, title, description, category);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                // TODO error stuff
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                imagePath = imageFile.getAbsolutePath();
                Bitmap imageBitmap = BitmapFactory.decodeFile(imagePath);
                noteImage.setImageBitmap(imageBitmap);
            }
        });
    }
}

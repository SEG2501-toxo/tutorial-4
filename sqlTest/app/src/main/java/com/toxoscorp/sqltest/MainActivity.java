package com.toxoscorp.sqltest;
import com.toxoscorp.sqltest.FeedReaderContract.FeedEntry;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(this);
    ArrayAdapter<Long> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(v -> {
            // Gets the data repository in write mode
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            String title = "My Title";
            String subtitle = "My Subtitle";

// Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(FeedEntry.COLUMN_NAME_TITLE, title);
            values.put(FeedEntry.COLUMN_NAME_SUBTITLE, subtitle);

// Insert the new row, returning the primary key value of the new row
            long newRowId = db.insert(FeedEntry.TABLE_NAME, null, values);
        });

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(v -> {
            SQLiteDatabase db = dbHelper.getReadableDatabase();

// Define a projection that specifies which columns from the database
// you will actually use after this query.
            String[] projection = {
                    BaseColumns._ID,
                    FeedEntry.COLUMN_NAME_TITLE,
                    FeedEntry.COLUMN_NAME_SUBTITLE
            };

// Filter results WHERE "title" = 'My Title'
            String selection = FeedEntry.COLUMN_NAME_TITLE + " = ?";
            String[] selectionArgs = { "My Title" };

// How you want the results sorted in the resulting Cursor
            String sortOrder =
                    FeedEntry.COLUMN_NAME_SUBTITLE + " DESC";

            Cursor cursor = db.query(
                    FeedEntry.TABLE_NAME,   // The table to query
                    projection,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    sortOrder               // The sort order
            );

            while(cursor.moveToNext()) {
                long itemId = cursor.getLong(
                        cursor.getColumnIndexOrThrow(FeedEntry._ID));
                adapter.add(itemId);
                adapter.notifyDataSetChanged();
            }
            cursor.close();
        });

        Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(v -> {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            // Define 'where' part of query.
            String selection = FeedEntry.COLUMN_NAME_TITLE + " LIKE ?";
// Specify arguments in placeholder order.
            String[] selectionArgs = { "MyTitle" };
// Issue SQL statement.
            int deletedRows = db.delete(FeedEntry.TABLE_NAME, selection, selectionArgs);
        });

        Button button4 = findViewById(R.id.button4);
        button4.setOnClickListener(v -> {
            SQLiteDatabase db = dbHelper.getWritableDatabase();

// New value for one column
            String title = "MyNewTitle";
            ContentValues values = new ContentValues();
            values.put(FeedEntry.COLUMN_NAME_TITLE, title);

// Which row to update, based on the title
            String selection = FeedEntry.COLUMN_NAME_TITLE + " LIKE ?";
            String[] selectionArgs = { "MyOldTitle" };

            int count = db.update(
                    FeedEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);
        });
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}
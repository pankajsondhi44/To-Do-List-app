package com.example.pankajsondhi44.whatnext;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> tasks;
    private ListView LV;
    private ArrayAdapter<String> tasksAdapter;
    static int theme = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Retrieving saved theme state in theme_boolean
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean theme_boolean = sharedPref.getBoolean("THEME", true);
        if(theme_boolean)  {
            ThemeUtils.onActivityCreateSetTheme(this, ThemeUtils.LIGHT);
        } else {
            ThemeUtils.onActivityCreateSetTheme(this, ThemeUtils.DARK);
        }

        setContentView(R.layout.activity_main);
        tasks = new ArrayList<>();
        readItems();
        LV = (ListView) findViewById(R.id.ListOfTasks);
        tasksAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tasks);
        LV.setAdapter(tasksAdapter);
        setupListViewListener();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.theme_light:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                theme = 0;
                ThemeUtils.changeToTheme(this, ThemeUtils.LIGHT);
                return true;
            case R.id.theme_dark:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                theme = 1;
                ThemeUtils.changeToTheme(this, ThemeUtils.DARK);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void setupListViewListener() {
        LV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                tasks.remove(position);
                tasksAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });
    }

    public void addTask(View view) {
        EditText editText = (EditText) findViewById(R.id.editText);
        String taskString = editText.getText().toString();
        if(!taskString.equals(""))
        tasksAdapter.add(taskString);
        editText.setText("");
        writeItems();
    }

    private void readItems() {
        String fileName = "todo.txt";
        File fileDir = getFilesDir();
        File todoFile = new File(fileDir, fileName);
        try {
            tasks = new ArrayList<>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            tasks = new ArrayList<>();
        }
    }

    private void writeItems() {
        File fileDir = getFilesDir();
        File todoFile = new File(fileDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, tasks);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    //Saving the Theme state in sharedprefrences
    @Override
    protected void onPause() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        if(theme == 0)
            editor.putBoolean("THEME", true);
        else
            editor.putBoolean("THEME", false);
        editor.commit();
        super.onPause();
    }
}

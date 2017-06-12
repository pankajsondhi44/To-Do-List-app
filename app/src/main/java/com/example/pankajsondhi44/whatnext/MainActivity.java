package com.example.pankajsondhi44.whatnext;

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
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> tasks;
    private ListView LV;
    private ArrayAdapter<String> tasksAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
            tasks = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            tasks = new ArrayList<String>();
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

}

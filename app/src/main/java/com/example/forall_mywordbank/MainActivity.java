package com.example.forall_mywordbank;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper myDb;


    EditText editWord, editMeaming, id_forDelete;
    Button SubmitInDB;

    ListView listView;
    ArrayList<String> listItems;
    ArrayAdapter adapter;
    List<String> userSelection =  new ArrayList<>();

    //end hear
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDb = new DatabaseHelper(this);

        listItems = new ArrayList<>();

        editWord =  findViewById(R.id.inputWord);
        editMeaming =  findViewById(R.id.inputMeaning);
        SubmitInDB =  findViewById(R.id.submitButton);
        listView = findViewById(R.id.viewTheList);
        //textView = (TextView) findViewById(R.id.clickButton);


        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(multiChoiceModeListener);

        AddData();
        viewData();
        //wordList();

    }

    @Override
    public void onBackPressed() {

        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_exit);
        dialog.setCancelable(false);
        dialog.show();


        Button yes_button, no_button;

        yes_button = (Button) dialog.findViewById(R.id.yes_button);
        no_button = (Button)dialog.findViewById(R.id.no_button);

        yes_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.super.onBackPressed();
            }
        });

        no_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //this dialog for the exit build in from android studio

//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage("Sure You Want to Exit iT?" )
//                .setCancelable(false)
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        MainActivity.super.onBackPressed();
//                    }
//                })
//
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//
//        AlertDialog alertDialog = builder.create();
//        alertDialog.show();
    }

    private void wordList(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text = listView.getItemAtPosition(position).toString();
                Toast.makeText(MainActivity.this,""+text, Toast.LENGTH_LONG).show();

            }
        });

    }

    private void viewData() {

        Cursor cursor = myDb.getAllData();
//        if (cursor.getCount() == 0)
//            Toast.makeText(MainActivity.this,"No Data to Show",Toast.LENGTH_SHORT).show();
        if(cursor.getCount() > 0){

            while (cursor.moveToNext()){
                listItems.add(cursor.getString(0)+"\n"+cursor.getString(1));
            }
            Collections.sort(listItems);
           // adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,listItems);
            adapter = new ArrayAdapter<>(this,R.layout.list_items_view,listItems);
            listView.setAdapter(adapter);

        }
    }

    public  void  AddData(){
        SubmitInDB.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(editWord.getText().toString()) && !TextUtils.isEmpty(editMeaming.getText().toString())){

                            if( checkMatch()){
                                boolean isInserted = myDb.insertData(editWord.getText().toString(),
                                        editMeaming.getText().toString());

                                if (isInserted){
                                    Toast.makeText(MainActivity.this, "Data inserted", Toast.LENGTH_SHORT).show();
                                    editWord.setText("");
                                    editMeaming.setText("");
                                    listItems.clear();
                                    viewData();
                                }
                                else
                                    Toast.makeText(MainActivity.this,"Data Not inserted",Toast.LENGTH_SHORT).show();
                            }

                            else

                                Toast.makeText(MainActivity.this,"Data Is Matched",Toast.LENGTH_SHORT).show();
                        }
                        else

                            Toast.makeText(MainActivity.this,"Data is Empty",Toast.LENGTH_SHORT).show();

                    }
                });
    }


    public boolean checkMatch(){

        Cursor cursor = myDb.getAllData();

        while (cursor.moveToNext()){
            if(cursor.getString(0).toLowerCase().equals(editWord.getText().toString().toLowerCase()) && cursor.getString(0).toLowerCase().equals(editWord.getText().toString().toLowerCase())  )
            {
                    String addStringring = cursor.getString(1) +", "+editMeaming.getText().toString();

                    boolean isUpdate = myDb.updateData(editWord.getText().toString(),addStringring);

                    if (isUpdate){
                        Toast.makeText(MainActivity.this, "Data Update", Toast.LENGTH_SHORT).show();
                        editWord.setText("");
                        editMeaming.setText("");
                        listItems.clear();
                        viewData();
                    }
                    else
                        Toast.makeText(MainActivity.this,"Data Not Update!!",Toast.LENGTH_SHORT).show();

                    return  false;

            }
        }




        return true;
    }




    public void removeItems(List<String> items){
        for (String item : items){
            Integer deletedRows = myDb.deleteData(FindWord(item));
            if(deletedRows > 0) {
                Toast.makeText(MainActivity.this, "Deleted Data ", Toast.LENGTH_SHORT).show();
                listItems.clear();
                viewData();

            }
            else
                Toast.makeText(MainActivity.this,"Deleted Not Data ",Toast.LENGTH_SHORT).show();



        }

    }

    String FindWord(String items){

        String[] arrOfItem = items.split("\n",2);
        return arrOfItem[0];
    }


    AbsListView.MultiChoiceModeListener multiChoiceModeListener = new AbsListView.MultiChoiceModeListener() {
        @Override
        public void onItemCheckedStateChanged(android.view.ActionMode mode, int position, long id, boolean checked) {


            if (userSelection.contains(listItems.get(position)))
                userSelection.remove(listItems.get(position));
            else
                userSelection.add(listItems.get(position));


            mode.setTitle(userSelection.size()+ " Selected..");

        }

        @Override
        public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) {
            MenuInflater menuInflater = mode.getMenuInflater();
            menuInflater.inflate(R.menu.context_menu,menu);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(android.view.ActionMode mode, MenuItem item) {
            switch (item.getItemId()){
                case R.id.delete_button:
                    Toast.makeText(MainActivity.this,"Deleted",Toast.LENGTH_SHORT).show();
                    removeItems(userSelection);
                    mode.finish();
                    return true;

                default:
                    return false;


            }
        }

        @Override
        public void onDestroyActionMode(android.view.ActionMode mode) {
            userSelection.clear();
        }
    };




}

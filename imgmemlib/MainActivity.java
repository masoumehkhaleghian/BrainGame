package com.khaleghian.masi.imgmem;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String level_imgMem="Easy";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //spinner
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                MainActivity.this, R.array.level_imgMem, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                level_imgMem = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                level_imgMem="Easy";
            }
        });
        //spinner
        GridView gridView=findViewById(R.id.gridview);
        final List<ItemObject> allItems=getAllItemObject();
        CustomAdapter customAdapter=new CustomAdapter(this.getLayoutInflater(),allItems,MainActivity.this);
        gridView.setAdapter(customAdapter);


        //select items
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (level_imgMem)
                {
                    case "Easy":
                        Intent intent1=new Intent(MainActivity.this, GameActivityEasy.class);
                        intent1.putExtra("itemNumber",position);
                        startActivity(intent1);
                        break;
                    case "Medium":
                        Intent intent2=new Intent(MainActivity.this, GameActivityMedium.class);
                        intent2.putExtra("itemNumber",position);
                        startActivity(intent2);
                        break;
                    case "Hard":
                        Intent intent3=new Intent(MainActivity.this, GameActivityHard.class);
                        intent3.putExtra("itemNumber",position);
                        startActivity(intent3);
                        break;
                }
            }
        });

    }

    private List<ItemObject> getAllItemObject() {
        List<ItemObject> items=new ArrayList<>();
        items.add(new ItemObject("Flower","flower"));
        items.add(new ItemObject("Cartoon","cartoon"));
        items.add(new ItemObject("car","car"));
        items.add(new ItemObject("Alphabet","alphabet"));
        items.add(new ItemObject("Flag","flag"));
        items.add(new ItemObject("Number","number"));
        return items;
    }

    //dialog exit
    private void showExitDialog() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_layout_exit);
        dialog.setCancelable(false);

        dialog.show();

        Button exit = dialog.findViewById(R.id.yes_button);
        final Button dismiss = dialog.findViewById(R.id.no_button);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        showExitDialog();
    }
}

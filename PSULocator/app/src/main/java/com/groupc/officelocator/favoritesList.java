package com.groupc.officelocator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class favoritesList extends AppCompatActivity {
    private ListView allFavorites;
    private String choice, fpname, floorNumber;
    private int floorCode;
    SharedPreferences favoritesList;
    Set<String> defaultrooms, favRooms;
    ArrayAdapter<String> arrayAdapter;
    public mapdata data;
    public int choiceFloors; //Number of floors in chosen building
    Bundle dataContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        Intent priorInt = getIntent();
        dataContainer = priorInt.getExtras();
        data = new mapdata();
        data = dataContainer.getParcelable("parse");

        TextView text = (TextView) findViewById(R.id.favoritestitle);
        Typeface myCustomfont = Typeface.createFromAsset(getAssets(), "fonts/AdobeGaramondProRegular.ttf");
        text.setTypeface(myCustomfont);

        allFavorites = (ListView) findViewById(R.id.favoritesList);
        allFavorites.setEmptyView(findViewById(R.id.empty));

        ArrayList<String> listofFavorites = new ArrayList<String>();
        favoritesList = getSharedPreferences("PSUFavorites", Context.MODE_PRIVATE);
        defaultrooms = new HashSet();
        favRooms = favoritesList.getStringSet("favRooms", defaultrooms);
        for(String room : favRooms) {
            if (room.matches(".*\\d+.*")){
                String[] parts = room.split("\\d+",2);
                String part1 = parts[0].trim();
                String part2 = room.substring(part1.length() + 1).trim();
                String toAdd = part1 + " Floor " + part2;
                listofFavorites.add(toAdd);
                continue;
            }
            listofFavorites.add(room);
        }

        arrayAdapter = new ArrayAdapter<String>(this, R.layout.listtextview, listofFavorites);
        allFavorites.setAdapter(arrayAdapter);

        //The first time the user utilizes the app a popup dialog will appear giving them instructions on
        //how to use the favorites activity page. After the first time, it will not appear again. To get the
        //instruction to appear again you must uninstall and reinstall the app
        if(firstTime()){
            Toast favoritesInstruction = Toast.makeText(getApplicationContext(), "Click to display the location. " +
                    "\nHold to remove from favorites", Toast.LENGTH_LONG);
            favoritesInstruction.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
            favoritesInstruction.show();
        }

        //What to do when the user clicks on a result from the favorites list
        allFavorites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                floorplan.fromSearch = 1;
                //Gets object at the position and parses
                choice = (String) allFavorites.getAdapter().getItem(position);
                String[] parts = choice.split("\\d+",2);
                String part1 = parts[0].trim();
                String part2 = choice.substring(part1.length() + 1).trim();
                choice = part1.replace(" Floor","");

                for(int i = 0; i < data.numberofBuildings; ++i) {
                    //If the user clicks a section header
                    //If string reads ...Floor (number) [this will not work for buildings
                    //with over 100 floors, but that shouldn't be an issue for now. If we
                    //sell the app to a New York City developer, change this line!]
                    if (choice.contains(data.buildings.get(i).buildingName) &&
                            (part2.length() == 1 || part2.length() == 2)) {
                        fpname = data.buildings.get(i).buildingName;
                        floorCode = i;
                        choiceFloors = data.buildings.get(i).numberofFloors;
                        floorplan.floorNumber = "0";
                        floorplan.rmName = "";
                        //1st floor default
                        floorplan.imageName = fpname.toLowerCase().replaceAll("\\s","") + "1";
                        floorplan.chosenRoomFromSearch = "";
                        break;
                    }
                    //If a room is selected
                    for(int j = 0; j < data.buildings.get(i).numberofFloors; ++j) {
                        for(int k = 0; k < data.buildings.get(i).floors.get(j).rooms.size(); ++k) {
                            if(part2.contains(data.buildings.get(i).floors.get(j).rooms.get(k).roomName)) {
                                fpname = data.buildings.get(i).buildingName;
                                floorCode = i;
                                floorNumber = Integer.toString(data.buildings.get(i).floors.get(j).level);
                                choiceFloors = data.buildings.get(i).numberofFloors;
                                floorplan.buildingselected = floorCode + 1; //Used in floorplan class
                                floorplan.setRoomfromSearch = 1;
                                floorplan.floorNumber = floorNumber;
                                floorplan.imageName =
                                        fpname.toLowerCase().replaceAll("\\s","") + floorNumber;
                                floorplan.rmName = floorplan.chosenRoomFromSearch =
                                        data.buildings.get(i).floors.get(j).rooms.get(k).roomName;
                                break;
                            }
                        }
                    }
                }

                Intent goToFloorPlan = new Intent(favoritesList.this, floorplan.class);

                goToFloorPlan.putExtras(dataContainer);
                floorplan.fpname = fpname;
                floorplan.spinnerNumber = floorCode;
                floorplan.numberOfFloors = choiceFloors;

                startActivity(goToFloorPlan);
            }

        });

        allFavorites.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View view, int position, long id) {
                choice = (String) allFavorites.getAdapter().getItem(position);
                for (String room : favRooms) {
                    if(choice.contains("Floor")) {
                        choice = choice.replace("Floor ", "");
                    }
                    if (room.trim().matches(choice)) {
                        favRooms.remove(room);
                        SharedPreferences.Editor editor = favoritesList.edit();
                        editor.clear();
                        editor.putStringSet("favRooms", favRooms);
                        editor.commit();

                        Toast.makeText(favoritesList.this, choice + " was removed from favorites", Toast.LENGTH_SHORT).show();
                        arrayAdapter.clear();

                        ArrayList<String> listofFavorites = new ArrayList<String>();
                        favRooms = favoritesList.getStringSet("favRooms", defaultrooms);
                        for(String rooms : favRooms) {
                            if (rooms.matches(".*\\d+.*")){
                                String[] parts = rooms.split("\\d+",2);
                                String part1 = parts[0].trim();
                                String part2 = rooms.substring(part1.length() + 1).trim();
                                String toAdd = part1 + " Floor " + part2;
                                listofFavorites.add(toAdd);
                                continue;
                            }
                            listofFavorites.add(rooms);
                        }
                        arrayAdapter = new ArrayAdapter<String>(favoritesList.this, R.layout.listtextview, listofFavorites);
                        allFavorites.invalidateViews();
                        allFavorites.setAdapter(arrayAdapter);
                        break;
                    }
                }
                return true;
            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().getItem(2).setChecked(true);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent theintent = new Intent(favoritesList.this, campus.class);
                    theintent.putExtras(dataContainer);
                    startActivity(theintent);
                    return true;

                case R.id.navigation_search:
                    Intent theintent2 = new Intent(favoritesList.this, masterSearchWithHeaders.class);
                    theintent2.putExtras(dataContainer);
                    startActivity(theintent2);
                    return true;

                case R.id.navigation_favorites:
                    return true;
            }
            return false;
        }
    };

    private boolean firstTime(){
        SharedPreferences firstTime = getSharedPreferences("FirstTime", Context.MODE_PRIVATE);
        boolean isFirstTime = firstTime.getBoolean("isFirstTime", false);
        if(!isFirstTime){
            SharedPreferences.Editor editor = firstTime.edit();
            editor.putBoolean("isFirstTime",true);
            editor.commit();
        }
        return !isFirstTime;
    }
}

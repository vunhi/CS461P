package com.groupc.officelocator;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.widget.Toast;

public class floorplan extends AppCompatActivity{

    RelativeLayout relativeLayout;
    public static int buildingselected = 0; //Tracks which building
    public static int setRoomfromSearch = 0; //Determines if a room was chosen in Search
    public static int floorselected = 0; //Determines if a floor number was chosen in Search or through Spinner
    public static int fromSearch = 0; //Determines if the previous page was Search before coming to the floorplan page
    public static int fromCampus = 0; //Determines if the previous page was the campus page

    ImageView buildingLocation, spinner2drop, selectedRoom;
    private Spinner chooseFloor, chooseRoom;
    ImageButton favorite, maplocationbut;

    Dialog dialog, favoriteDialog;
    TextView cancel, floorplanname, roomName, favoriteyes, favoriteno, favoritecancel, roomspinnerprompt;
    public String addtofavorite;
    public static String fpname, imageName, floorNumber, chosenRoomFromSearch, rmName = "";
    public static int spinnerNumber, numberOfFloors;

    public mapdata data;
    Bundle dataContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Sets appropriate floorplan layout and spinners.
        setup();

        //Floor spinner listener setup
        floorSet();
    }

    private void select(){
        chooseRoom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selection = ((TextView) view).getText().toString();
                if (selection.equals("Choose a room")) {
                    rmName ="";
                    roomName.setVisibility(View.INVISIBLE);
                    return;
                }
                rmName = selection;

                roomName.setVisibility(View.VISIBLE);
                roomName.setText(selection);

                //Clears existing room markers
                for (int j = 0; j < data.numberofBuildings; ++j) {
                    if (data.buildings.get(j).buildingName.equals(fpname)) {
                        for (int k = 0; k < data.buildings.get(j).floors.size(); ++k) {
                            if (data.buildings.get(j).floors.get(k).level == Integer.parseInt(floorNumber)) {
                                for (int m = 0; m < data.buildings.get(j).floors.get(k).rooms.size(); ++m) {
                                    String tempName = data.buildings.get(j).floors.get(k).rooms.get(m).roomName;
                                    tempName = tempName.toLowerCase().replaceAll("\\s", "");
                                    int roomID =
                                            getResources().getIdentifier(tempName, "id", getPackageName());
                                    selectedRoom = (ImageView) findViewById(roomID);
                                    selectedRoom.setVisibility(View.INVISIBLE);
                                }
                            }
                        }
                    }
                }

                //Sets new marker
                selection = selection.toLowerCase().replaceAll("\\s", "");
                int roomID =
                        getResources().getIdentifier(selection, "id", getPackageName());
                selectedRoom = (ImageView) findViewById(roomID);
                selectedRoom.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    private void floorSet() {
        //What to do when the user clicks on a choice for the first spinner for choosing floors
        chooseFloor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                roomName.setVisibility(View.INVISIBLE);

                //set floor choice
                floorNumber = ((TextView) view).getText().toString();

                //Clearing room variable
                rmName = "";

                if (floorNumber.equals("Choose a floor")) {
                    //Disable Room spinner
                    chooseRoom.setSelection(0,true);
                    chooseRoom.setEnabled(false);
                    chooseRoom.setClickable(false);
                    return;
                }

                chooseRoom.setEnabled(true);
                chooseRoom.setClickable(true);

                floorselected = Integer.parseInt(floorNumber);

                //Flag reset for setup function
                fromSearch = 0;
                setRoomfromSearch = 0;
                //Sets new floorplan name
                imageName = fpname.toLowerCase().replaceAll("\\s", "") + floorNumber;
                //Creates new layout based on new floor #
                setup();

                chooseFloor.setSelection(floorselected,true);
                chooseFloor.setSelected(true);

                spinner2drop.setVisibility(View.VISIBLE);
                chooseRoom.setVisibility(View.VISIBLE);
                roomspinnerprompt.setVisibility(View.VISIBLE);

                List<String> spinnerArray = new ArrayList<String>();
                spinnerArray.add("Choose a room");
                for(int j = 0; j < data.numberofBuildings; ++j) {
                    for(int k = 0; k < data.buildings.get(j).floors.size(); ++k) {
                        if(buildingselected == (j + 1) && data.buildings.get(j).floors.get(k).level == floorselected) {
                            for(int m = 0; m < data.buildings.get(j).floors.get(k).rooms.size(); ++m) {
                                spinnerArray.add(data.buildings.get(j).floors.get(k).rooms.get(m).roomName);
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(floorplan.this,  R.layout.spinner_layout, spinnerArray);
                            adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
                            chooseRoom.setAdapter(adapter);
                            break;
                        }
                    }
                }
                chooseRoom.setSelected(false);
                chooseRoom.setSelection(0,true);

                select();

                //Resets floor spinner listener
                floorSet();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    private void setup() {
        setContentView(getResources().getIdentifier(imageName, "layout", this.getPackageName()));
        if(fromCampus == 1)
            rmName = "";
        fromCampus = 0;

        ZoomLayout myZoomView = new ZoomLayout(floorplan.this);
        relativeLayout = (RelativeLayout) findViewById(R.id.zoom);
        relativeLayout.addView(myZoomView);

        //Pulling map data on entry into the activity
        //if(fromSearch == 1) {
            data = new mapdata();
            Intent goToFloorPlan = getIntent();
            dataContainer = goToFloorPlan.getExtras();
            data = dataContainer.getParcelable("parse");
        //}

        spinner2drop = (ImageView)findViewById(R.id.imageView10); //Dropdown arrow for 2nd spinner
        roomspinnerprompt = (TextView)findViewById(R.id.roomSpinnerTitle);

        //Room title on the floorplan page
        roomName = (TextView) findViewById(R.id.roomName);
        Typeface myCustomfont = Typeface.createFromAsset(getAssets(), "fonts/AdobeGaramondProRegular.ttf");
        roomName.setTypeface(myCustomfont);

        //If a room was chosen through search, must cause the room title to appear since by default it doesn't until you choose the
        //room through the second spinner
        if(setRoomfromSearch == 1){
            roomName.setText(rmName);
            roomName.setVisibility(View.VISIBLE);
        }

        //Setting floor plan title name + font style
        floorplanname = (TextView) findViewById(R.id.floorPlanName);
        floorplanname.setTypeface(myCustomfont);

        if(fromSearch == 1) {
            String tempName = rmName.toLowerCase().replaceAll("\\s","");
            int roomID =
                    getResources().getIdentifier(tempName,"id",getPackageName());
            selectedRoom = (ImageView)findViewById(roomID);
            selectedRoom.setVisibility(View.VISIBLE);
        }

        //If the floor plan title has a floor number, we add that to the title
        //if(Integer.parseInt(floorNumber) == 0) //For those without a floor number ("Mia Hamm"/"Tiger Woods"
            //the default is the first floor
            //floorNumber = "1";
        if(floorNumber.equals("0"))
            floorplanname.setText(fpname + " Basement");
        else
            floorplanname.setText(fpname + " Floor " + floorNumber);

        buildingselected = spinnerNumber + 1;

        //Creating the two spinner drop down menus that choose the floor and rooms
        //Choosing a floor in the first spinner causes the second spinner to be visible
        //The choice of the floor also determines the choices of rooms for the second spinner
        chooseFloor = (Spinner)findViewById(R.id.floorSelector);
        chooseRoom = (Spinner) findViewById(R.id.roomSelector);

        //First spinner - Choosing which floor
        List<String> list = new ArrayList<String>();
        list.add("Choose a floor");
        for (int i = 1; i <= numberOfFloors; i++){
            list.add(String.valueOf(i));
        }
        ArrayAdapter<String> numberAdapter = new ArrayAdapter<String>(this, R.layout.spinner_dropdown_layout, list);
        chooseFloor.setAdapter(numberAdapter);
        chooseFloor.setSelected(false);
        chooseFloor.setSelection(0,true);

        //When coming from the search menu, the floor number is already chosen and the room can be chosen through the search
        //results. If they are we must set the spinners to reflect these predetermined choices
        if(fromSearch == 1){
            //All search results have floor values set into them (if there is not one explicitly set, it gets sent
            //to the first floor
            floorselected = Integer.parseInt(floorNumber);
            chooseFloor.setSelection(floorselected,true);
            chooseFloor.setSelected(true);

            spinner2drop.setVisibility(View.VISIBLE);
            chooseRoom.setVisibility(View.VISIBLE);
            roomspinnerprompt.setVisibility(View.VISIBLE);

            List<String> spinnerArray = new ArrayList<String>();
            spinnerArray.add("Choose a room");
            for(int i = 0; i < data.numberofBuildings; ++i) {
                for(int j = 0; j < data.buildings.get(i).floors.size(); ++j) {
                    if(buildingselected == (i + 1) && data.buildings.get(i).floors.get(j).level == floorselected) {
                        for(int k = 0; k < data.buildings.get(i).floors.get(j).rooms.size(); ++k) {
                            spinnerArray.add(data.buildings.get(i).floors.get(j).rooms.get(k).roomName);
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(floorplan.this,  R.layout.spinner_layout, spinnerArray);
                        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
                        chooseRoom.setAdapter(adapter);
                        break;
                    }
                }
            }
            chooseRoom.setSelected(false);
            chooseRoom.setSelection(0,true);

            //If the room was also chosen through the search result chosen, we choose that value to
            //appear in the second spinner for the rooms
            if(setRoomfromSearch==1){
                chooseRoom.setSelected(true);
                int selection = 0;
                if(chosenRoomFromSearch != null) {
                    for(int i = 0; i < data.numberofBuildings; ++i) {
                        for (int j = 0; j < data.buildings.get(i).floors.size(); ++j) {
                            for (int k = 0; k < data.buildings.get(i).floors.get(j).rooms.size(); ++k) {
                                if (chosenRoomFromSearch.matches(data.buildings.get(i).floors.get(j).rooms.get(k).roomName))
                                    selection = k;
                            }
                        }
                    }
                }
                //+1 to skip past 'choose a room'
                chooseRoom.setSelection(selection+1,true);
                //Flag reset
                setRoomfromSearch = 0;
            }
            fromSearch = 0;
            select();
        }
        //Pop up dialog for building location
        createDialog();
        maplocationbut = (ImageButton) findViewById(R.id.buildinglocator);
        maplocationbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

        //If cancel button in dialog popup is clicked
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelected(false);

        createFavoriteDialog();
        favorite = (ImageButton) findViewById(R.id.favorite);
        favorite.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(floorNumber.equals("Choose a floor")) {
                    Toast.makeText(floorplan.this, "Select a floor", Toast.LENGTH_SHORT).show();
                    favoriteDialog.dismiss();
                }
                else
                    favoriteDialog.show();
            }
        });

        //If cancel button in dialog popup is clicked then exit the dialog
        favoritecancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favoriteDialog.dismiss();
            }
        });

        //If the no button in the dialog is clicked then exit the dialog
        favoriteno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favoriteDialog.dismiss();
            }
        });

        //If the yes button in the favorite dialog is clicked then save the string to the favorites
        favoriteyes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                SharedPreferences favoritesList = getSharedPreferences("PSUFavorites", Context.MODE_PRIVATE);
                Set<String> favRooms = new HashSet<>(favoritesList.getStringSet("favRooms", new HashSet<String>()));

                String display = null;
                if(floorNumber.equals("0")) {
                    addtofavorite = fpname + " Basement";
                    display = fpname + " Basement";
                }
                else if(rmName.equals("")) {
                    addtofavorite = fpname + " " + Integer.toString(floorselected);
                    display = fpname + " Floor " + Integer.toString(floorselected);
                }
                else {
                    addtofavorite = fpname + " " + Integer.toString(floorselected) + " " + rmName;
                    display = fpname + " Floor " + Integer.toString(floorselected) + " " + rmName;
                }
                for(String room : favRooms) {
                    if (room.matches(addtofavorite)) {
                        Toast.makeText(floorplan.this, display + " was already added to favorites", Toast.LENGTH_SHORT).show();
                        favoriteDialog.dismiss();
                        return;
                    }
                }
                favRooms.add(addtofavorite);
                SharedPreferences.Editor editor = favoritesList.edit();
                editor.putStringSet("favRooms", favRooms);
                editor.commit();

                Toast.makeText(floorplan.this, display + " was added to favorites", Toast.LENGTH_SHORT).show();
                favoriteDialog.dismiss();
            }
        });
    }

    //Sets up favorite pop up dialog
    private void createFavoriteDialog(){
        favoriteDialog = new Dialog(floorplan.this);
        favoriteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        favoriteDialog.setContentView(R.layout.favoritedialog);
        favoritecancel = (TextView) favoriteDialog.findViewById(R.id.cancel);
        favoriteyes = (TextView) favoriteDialog.findViewById(R.id.yes);
        favoriteno = (TextView) favoriteDialog.findViewById(R.id.no);
    }

    //Come back to fix this perhaps after room class stores images
    //Sets up pop up dialog
    private void createDialog()
    {
        dialog = new Dialog(floorplan.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.imagedialog);
        buildingLocation = (ImageView)dialog.findViewById(R.id.buildingLocation);

        String dialogImage = fpname.toLowerCase().replaceAll("\\s", "") + "highlighted";
        int imgid = getResources().getIdentifier(dialogImage, "drawable", getPackageName());
        buildingLocation.setImageResource(imgid);

        cancel = (TextView) dialog.findViewById(R.id.cancel);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Intent theintent = null;

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    theintent = new Intent(floorplan.this, campus.class);
                    theintent.putExtras(dataContainer);
                    break;

                case R.id.navigation_search:
                    theintent = new Intent(floorplan.this, masterSearchWithHeaders.class);
                    theintent.putExtras(dataContainer);
                    break;

                case R.id.navigation_favorites:
                    theintent = new Intent(floorplan.this, favoritesList.class);
                    theintent.putExtras(dataContainer);
                    break;
            }
            startActivity(theintent);
            return true;
        }

    };

    //Ensures proper reset on back button navigation
    @Override
    public void onBackPressed(){
        fromSearch = 1;
        super.onBackPressed();
    }
}

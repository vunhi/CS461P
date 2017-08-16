package com.groupc.officelocator;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.TextView;
import android.support.design.widget.BottomNavigationView;
import android.support.annotation.NonNull;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Locale;

public class masterSearchWithHeaders extends AppCompatActivity {
    private ListView allSearchResults;
    private EditText searchBar;
    private String choice, fpname, floorNumber;
    private int floorCode;
    public mapdata data;
    public int choiceFloors; //Number of floors in chosen building
    Bundle dataContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mastersearch);
        floorplan.fromSearch = 1;

        searchBar = (EditText) findViewById(R.id.searchBar);
        Typeface myCustomfont = Typeface.createFromAsset(getAssets(), "fonts/AdobeGaramondProRegular.ttf");
        searchBar.setTypeface(myCustomfont);
        allSearchResults = (ListView) findViewById(R.id.searchList);
        allSearchResults.setEmptyView(findViewById(R.id.empty));

        Intent priorInt = getIntent();
        dataContainer = priorInt.getExtras();
        data = new mapdata();
        data = dataContainer.getParcelable("parse");

        ArrayList<SearchItem> campusList = new ArrayList<masterSearchWithHeaders.SearchItem>();
        for (int i = 0; i < data.numberofBuildings; ++i){
            campusList.add(new BuildingName(data.buildings.get(i).buildingName));
            for(int j = 0; j < data.buildings.get(i).floors.size(); ++j) {
                for(int k = 0; k < data.buildings.get(i).floors.get(j).rooms.size(); ++k)
                    campusList.add(new RoomName(data.buildings.get(i).floors.get(j).rooms.get(k).roomName));
            }
        }

        // set adapter
        final CampusAdapter adapter = new CampusAdapter(this, campusList);
        allSearchResults.setAdapter(adapter);
        allSearchResults.setTextFilterEnabled(true);

        //What to do when the user clicks on a search result
        allSearchResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                //Gets object at the position
                SearchItem object = (SearchItem) allSearchResults.getItemAtPosition(position);
                choice = object.getName(); //Gets the name of the object at the position
                for(int i = 0; i < data.numberofBuildings; ++i){
                    //Section header choice
                    if(choice.contains(data.buildings.get(i).buildingName)){
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
                    for(int j = 0; j < data.buildings.get(i).numberofFloors; ++j) {
                        //Room choice
                        for(int k = 0; k < data.buildings.get(i).floors.get(j).rooms.size(); ++k) {
                            if(choice.contains(data.buildings.get(i).floors.get(j).rooms.get(k).roomName)) {
                                fpname = data.buildings.get(i).buildingName;
                                floorCode = i;
                                floorNumber = Integer.toString(data.buildings.get(i).floors.get(j).level);
                                choiceFloors = data.buildings.get(i).numberofFloors;
                                floorplan.buildingselected = floorCode + 1; //Used in floorplan class
                                floorplan.setRoomfromSearch = 1;
                                floorplan.floorNumber = floorNumber;
                                floorplan.imageName =
                                        fpname.toLowerCase().replaceAll("\\s","") + floorNumber;
                                floorplan.rmName = choice;
                                floorplan.chosenRoomFromSearch = choice;
                                break;
                            }
                        }
                    }
                }

                Intent goToFloorPlan = new Intent(masterSearchWithHeaders.this, floorplan.class);

                goToFloorPlan.putExtras(dataContainer);
                floorplan.fpname = fpname;
                floorplan.spinnerNumber = floorCode;
                floorplan.numberOfFloors = choiceFloors;

                startActivity(goToFloorPlan);
            }

        });

        //What to do when the user enters input into the search bar
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //If the user enters in text, filter the result
                if(adapter != null)
                    adapter.getFilter().filter(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().getItem(1).setChecked(true);
    }

    //WHERE WE DEFINE THE SEARCH ITEMS THAT WILL POPULATE THE SEARCH RESULT LISTVIEW
    //Search result items can be either a section header or a regular search result
    public interface SearchItem {
        public boolean isSection();
        public String getName();
    }

    //Listview Section Headers - Buildings
    public class BuildingName implements SearchItem {
        private final String name;
        public BuildingName(String name) {this.name = name;}
        public String getName() {return name;}
        @Override
        public boolean isSection() {return true;}
    }

    //Listview Nonsection-Header results - Rooms
    public class RoomName implements SearchItem {
        public final String name;
        public RoomName(String name) {this.name = name;}
        public String getName() {return name;}
        @Override
        public boolean isSection() {return false;}
    }

    //Custom "CampusAdapter"
    public class CampusAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<SearchItem> searchItem;
        private ArrayList<SearchItem> originalSearchItem;
        public CampusAdapter(Context context, ArrayList<SearchItem> searchItem) {
            this.context = context;
            this.searchItem = searchItem;
        }

        //Supporting methods
        @Override
        public int getCount() {return searchItem.size();}
        @Override
        public Object getItem(int position) {return searchItem.get(position);}
        @Override
        public long getItemId(int position) {return position;}

        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (searchItem.get(position).isSection()) {
                //If the item in the listview search result is a section header...
                convertView = inflater.inflate(R.layout.list_header, parent, false);
                TextView headerName = (TextView) convertView.findViewById(R.id.searchHeader);
                headerName.setText(((BuildingName) searchItem.get(position)).getName());
                Typeface myCustomfont = Typeface.createFromAsset(getAssets(), "fonts/AdobeGaramondProRegular.ttf");
                headerName.setTypeface(myCustomfont);
            }
            else
            {
                //If the item in the listview search result is not a section header...
                convertView = inflater.inflate(R.layout.list_items, parent, false);
                final TextView searchResult = (TextView) convertView.findViewById(R.id.searchItems);
                searchResult.setText(((RoomName) searchItem.get(position)).getName());
            }
            return convertView;
        }


        //Filtering the search results
        public Filter getFilter()
        {
            Filter filter = new Filter() {
                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence userEnteredString, FilterResults results) {
                    searchItem = (ArrayList<SearchItem>) results.values;
                    notifyDataSetChanged();
                }

                @SuppressWarnings("null")
                @Override
                protected FilterResults performFiltering(CharSequence userEnteredString) {
                    FilterResults results = new FilterResults();
                    ArrayList<SearchItem> filteredArrayList = new ArrayList<SearchItem>();

                    if(originalSearchItem == null || originalSearchItem.size() == 0)
                        originalSearchItem = new ArrayList<SearchItem>(searchItem);

                    //if userEnteredString is null then we return the original value, else return the filtered value
                    if(userEnteredString == null || userEnteredString.length() == 0){
                        results.count = originalSearchItem.size();
                        results.values = originalSearchItem;
                    }
                    else {
                        userEnteredString = userEnteredString.toString().toLowerCase(Locale.ENGLISH);
                        for (int i = 0; i < originalSearchItem.size(); i++)
                        {
                            String name = originalSearchItem.get(i).getName().toLowerCase(Locale.ENGLISH);
                            if(name.contains(userEnteredString.toString()))
                                filteredArrayList.add(originalSearchItem.get(i));
                        }
                        results.count = filteredArrayList.size();
                        results.values = filteredArrayList;
                    }
                    return results;
                }
            };
            return filter;
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Intent theintent = null;

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    theintent = new Intent(masterSearchWithHeaders.this, campus.class);
                    theintent.putExtras(dataContainer);
                    break;

                case R.id.navigation_search:
                    return true;

                case R.id.navigation_favorites:
                    theintent = new Intent(masterSearchWithHeaders.this, favoritesList.class);
                    theintent.putExtras(dataContainer);
                    break;
            }
            startActivity(theintent);
            return true;
        }

    };

}

package com.groupc.officelocator;

import android.graphics.Typeface;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

public class campus extends AppCompatActivity {
        RelativeLayout relativeLayout;

        //Buttons for just 2 buildings
        public Button cramer, neuberger;
        Button[] arrayButtons = {cramer, neuberger};
        int[] buttonNames = {R.id.cramer, R.id.neuberger};
        public mapdata data;
        Bundle dataContainer;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_campus);

            ZoomLayout myZoomView = new ZoomLayout(campus.this);
            relativeLayout = (RelativeLayout) findViewById(R.id.zoom);
            relativeLayout.addView(myZoomView);

            TextView text = (TextView) findViewById(R.id.campus);
            Typeface myCustomfont = Typeface.createFromAsset(getAssets(), "fonts/AdobeGaramondProRegular.ttf");
            text.setTypeface(myCustomfont);

            Intent priorInt = getIntent();
            dataContainer = priorInt.getExtras();
            data = new mapdata();
            data = dataContainer.getParcelable("parse");

            if(data == null)
                android.os.Process.killProcess(android.os.Process.myPid());

            for (int i = 0; i < data.buildings.size(); i++) {
                final int j = i;
                arrayButtons[i] = (Button) findViewById(buttonNames[i]);
                arrayButtons[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent goToFloorPlan = new Intent(campus.this, floorplan.class);

                        goToFloorPlan.putExtras(dataContainer);
                        floorplan.fromSearch = 1;
                        floorplan.fromCampus = 1;
                        floorplan.floorNumber = "0";
                        floorplan.fpname = data.buildings.get(j).buildingName;
                        floorplan.imageName = data.buildings.get(j).buildingName.replaceAll("\\s","").toLowerCase() + 1;
                        floorplan.spinnerNumber = j;
                        floorplan.numberOfFloors = data.buildings.get(j).numberofFloors;
                        floorplan.buildingselected = j + 1;

                        startActivity(goToFloorPlan);
                    }
                });
            }


            BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
            navigation.getMenu().getItem(0).setChecked(true);
        }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Intent theintent = null;

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;

                case R.id.navigation_search:
                    theintent = new Intent(campus.this, masterSearchWithHeaders.class);
                    theintent.putExtras(dataContainer);
                    break;

                case R.id.navigation_favorites:
                    theintent = new Intent(campus.this, favoritesList.class);
                    theintent.putExtras(dataContainer);
                    break;
            }
            startActivity(theintent);
            return true;
        }

    };
}

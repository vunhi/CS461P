package vunhi.psulocator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

public class building extends AppCompatActivity {

    ImageView buildingImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building);
        Intent goToBuilding = getIntent();

        //Displaying the correct building name and floorplan image
        //Setting floor plan name
       // String fpname = goToBuilding.getStringExtra("fpname");
        //TextView floorplanname = (TextView) findViewById(R.id.building);
        //floorplanname.setText(fpname);

        //Setting floor plan image
        String building = goToBuilding.getStringExtra("building");
        int res = getResources().getIdentifier(building, "drawable", this.getPackageName());
        buildingImage = (ImageView) findViewById(R.id.buildingImage);
        buildingImage.setImageResource(res);

    }
}

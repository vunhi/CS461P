package vunhi.psulocator;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class building extends AppCompatActivity {

    ImageView buildingImage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent goToHome = new Intent(building.this, MainActivity.class);
                    startActivity(goToHome);
                    return true;

                case R.id.navigation_search:
                    Intent goToSearch = new Intent(building.this, MainActivity.class);
                    startActivity(goToSearch);
                    MainActivity.index = 1;
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building);
        Intent goToBuilding = getIntent();

        //Setting floor plan image
        String building = goToBuilding.getStringExtra("building");
        int res = getResources().getIdentifier(building, "drawable", this.getPackageName());
        buildingImage = (ImageView) findViewById(R.id.buildingImage);
        buildingImage.setImageResource(res);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
}

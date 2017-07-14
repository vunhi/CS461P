package vunhi.psulocator;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class room extends AppCompatActivity {
    ImageView chosenRoom;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent goToHome = new Intent(room.this, MainActivity.class);
                    startActivity(goToHome);
                    return true;

                case R.id.navigation_search:
                    Intent goToSearch = new Intent(room.this, MainActivity.class);
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
        Intent goToRoom = getIntent();

        //Setting chosen room highlighted image
        String room = goToRoom.getStringExtra("room");
        room.replaceAll("\\s+", "");
        room = room.toLowerCase();
        int res = getResources().getIdentifier(room, "drawable", this.getPackageName());
        chosenRoom = (ImageView) findViewById(R.id.chosenRoom);
        chosenRoom.setImageResource(res);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
}

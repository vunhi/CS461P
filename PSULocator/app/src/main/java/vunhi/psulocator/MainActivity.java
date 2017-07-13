package vunhi.psulocator;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private ImageView image;
    private Button cramer;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    image.setVisibility(View.VISIBLE);
                    cramer.setEnabled(true);
                    cramer.setVisibility(View.VISIBLE);
                    return true;


                case R.id.navigation_search:
                    mTextMessage.setText(R.string.title_search);
                    image.setVisibility(View.INVISIBLE);
                    cramer.setVisibility(View.INVISIBLE);
                    cramer.setEnabled(false);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        image = (ImageView)findViewById(R.id.imageView);
        cramer = (Button)findViewById(R.id.cramer);
        cramer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent goToBuilding = new Intent(MainActivity.this, building.class);
                goToBuilding.putExtra("building", "cramer");
                startActivity(goToBuilding);
            }
        });
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}

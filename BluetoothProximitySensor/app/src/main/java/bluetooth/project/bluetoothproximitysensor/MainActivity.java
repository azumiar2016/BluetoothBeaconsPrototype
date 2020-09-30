package bluetooth.project.bluetoothproximitysensor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends DebugActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button houseLocation_button = (Button)findViewById(R.id.HouseLocation_btn);
        Button roomLocation_button = (Button)findViewById(R.id.RoomLocation_btn);
        roomLocation_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, RoomLocation.class);
                startActivity(i);
            }
        });
        houseLocation_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, HouseLocation.class);
                startActivity(i);
            }
        });
    }
}

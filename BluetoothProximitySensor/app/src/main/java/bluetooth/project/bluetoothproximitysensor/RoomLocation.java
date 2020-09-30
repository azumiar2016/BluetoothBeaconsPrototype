package bluetooth.project.bluetoothproximitysensor;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;




public class RoomLocation extends DebugActivity implements BeaconConsumer  {
    private BeaconManager beaconManager;
    String x, y;
    double distance = 0;
    String ip = "192.168.1.38";
    ArrayList<Beacon> myBeacons;
    TextView textView,locationTextView;
    ArrayList<Beacon> BeaconList;
    ListView listView;
    CustomListAdapter adapter;
    PositionCalculating positionCalculating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_location);

        //Määritellään huoneen koko:, voidaan myös siirtää palvelimelle
        double roomx = 3;
        double roomy = 3;

        positionCalculating = new PositionCalculating(roomx,roomy);

        BeaconList = new ArrayList<Beacon>();
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.setBackgroundBetweenScanPeriod(200);
        beaconManager.setBackgroundScanPeriod(600);


        beaconManager.bind(this);
        Button sendData_button = (Button) findViewById(R.id.sendData_btn);
        textView = findViewById(R.id.textView);;
        locationTextView = findViewById(R.id.locationTextView);

        sendData_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast toast = Toast.makeText(getApplicationContext(),
                        "Distance in string is "+String.format ("%f", distance),
                        Toast.LENGTH_SHORT);

                toast.show();

                x=positionCalculating.GetXPositionString();
                y=positionCalculating.GetYPositionString();

                String url = "http://" + ip + "/putdatatest.php";
                RequestQueue queue = Volley.newRequestQueue(RoomLocation.this);

                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // response
                                Log.d("Response", response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                Log.d("Error.Response", error.toString());
                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("x", x);
                        params.put("y", y);

                        return params;
                    }
                };
                queue.add(postRequest);

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.removeAllRangeNotifiers();
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                listView =(ListView) findViewById(R.id.listView);
                if (beacons.size() > 0) {



                    Beacon myBeacon = beacons.iterator().next();
                    Log.i(TAG, "The first beacon I see is about "+beacons.iterator().next().getDistance()+" meters away.");
                    Log.i(TAG, "Name Test "+beacons.iterator().next().getBluetoothName()+" Name");
                    distance = beacons.iterator().next().getDistance();
                    UpdateBeaconsList(beacons.iterator().next());
                    UpdateCurrentLocation();
                    textView.setText("Last Beacon Distance: " + String.valueOf(distance));
                    positionCalculating.calculatePosition();
                    locationTextView.setText(positionCalculating.toString());

                }
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {    }
    }


    public void UpdateBeaconsList(Beacon b){
        if(BeaconList.contains(b)){
            BeaconList.set(BeaconList.indexOf(b),b);
        }else{
            BeaconList.add(b);
        }
        reOrganizeBeaconList();

        adapter = new CustomListAdapter(RoomLocation.this,BeaconList);
        listView.setAdapter(adapter);

    }
    //varmistetaan, että kajapro-sensori on 1. sensorina, eli sitä voidaan käyttää laskemisessa sitten
    public void reOrganizeBeaconList(){

        myBeacons = new ArrayList<Beacon>();
        if(BeaconList.size()>1){
            Beacon temp = BeaconList.get(0);
            int i = 0;
            while(i < BeaconList.size()){
                if(BeaconList.get(i).getBluetoothName()!=null && BeaconList.get(i).getBluetoothName().contains("KajaPro00643")){
                    myBeacons.add(i,BeaconList.get(0));
                    myBeacons.set(0,BeaconList.get(i));

                    i++;
                }else{
                    myBeacons.add(BeaconList.get(i));
                    i++;
                }

            }

            BeaconList = myBeacons;
        }




    }
// Testausversio, koska tarkoituksena debuggaa yksittäisten sensorien toimintaa, myöhemmin voi vaan ilmoittaa että ei onnistu+sensorin nimet mitkä onnistuu
    public void UpdateCurrentLocation(){
        try{

            double beacon1 = BeaconList.get(0).getDistance();

            positionCalculating.UpdateBeacon1(beacon1);



        }catch (Exception e){

            // set text to the user "cant locate position"
            //this may happen if beacon is out of range
            //Or if there is a problem with beacon
        }

        try{

            double beacon2 = BeaconList.get(1).getDistance();

            positionCalculating.UpdateBeacon2(beacon2);



        }catch (Exception e){

            // set text to the user "cant locate position"
            //this may happen if beacon is out of range
            //Or if there is a problem with beacon
        }

        try{

            double beacon3 = BeaconList.get(2).getDistance();
            positionCalculating.UpdateBeacon3(beacon3);



        }catch (Exception e){

            // set text to the user "cant locate position"
            //this may happen if beacon is out of range
            //Or if there is a problem with beacon
        }



    }
}

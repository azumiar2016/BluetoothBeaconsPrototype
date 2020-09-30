package bluetooth.project.bluetoothproximitysensor;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




public class HouseLocation extends DebugActivity implements BeaconConsumer {
    private BeaconManager beaconManager;
    String x, y;
    double distance = 0;
  //  String ip = "192.168.1.38";
    ArrayList<Beacon> myBeacons;
    TextView RoomName;
    ArrayList<Beacon> BeaconList;
    ListView listView;



    String ToimistoId = "jinou";
    String OlohuoneId = "kaja";
    String MakuuhuoneId = "io";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_location);
        myBeacons = new ArrayList<Beacon>();

        BeaconList = new ArrayList<Beacon>();
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().
               setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.setBackgroundBetweenScanPeriod(200);
        beaconManager.setBackgroundScanPeriod(600);
        beaconManager.bind(this);
        Button sendData_button = (Button) findViewById(R.id.sendData_btn);
        RoomName = (TextView)findViewById(R.id.room_name);


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
            reOrganizeBeaconList();
        }else{
            BeaconList.add(b);
            reOrganizeBeaconList();
        }

    }

    public void reOrganizeBeaconList(){//

        myBeacons = new ArrayList<Beacon>();

        if(BeaconList.size()>1){
            Beacon temp = BeaconList.get(0);
            int i = 0;
            while(i < BeaconList.size()){
                if(i>0 && BeaconList.get(i).getBluetoothName()!=null && BeaconList.get(i).getDistance()<= BeaconList.get(0).getDistance()){
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

    public void UpdateCurrentLocation(){
        String roomNameId = "";

        try{

            roomNameId = BeaconList.get(0).getBluetoothName();
            ArrayAdapter arrayAdapter = new ArrayAdapter<>(HouseLocation.this,R.layout.activity_listview,getRoomItems(roomNameId));
            listView.setAdapter(arrayAdapter);


        }catch (Exception e){
            // set text to the user "cant locate position"
            //this may happen if beacon is out of range
            //Or if there is a problem with beacon
        }



    }
//Kovakoodattu protyyppinä, nämä tiedot voisi hakea myös tietokannasta
    //Jinou = Toimisto
    //io = makuuhuone
    //kaja = olohuone

    public ArrayList<String> getRoomItems(String roomnameId){

        ArrayList<String> roomItems = new ArrayList<>();
        if(roomnameId.toLowerCase().contains(ToimistoId)){
            roomItems.clear();
            roomItems.add("Tulostin(varaa klikkaamalla)");
            roomItems.add("Tietokone (varaa klikkaamalla)");
            roomItems.add("Kannettava tietokone (varaa klikkaamalla)");
            roomItems.add("3D-tulostin (varaa klikkaamalla)");
            RoomName.setText("Toimisto");

        }
        if(roomnameId.toLowerCase().contains(OlohuoneId)){
            roomItems.clear();
            roomItems.add("Televisio, varausta ei tarvita");
            RoomName.setText("Olohuone");

        }
        if(roomnameId.toLowerCase().contains(MakuuhuoneId)){
            roomItems.clear();
            roomItems.add("Sänky (varaa klikkaamalla)");
            RoomName.setText("Makuuhuone");
        }
        if(roomnameId.equals("")){
            RoomName.setText("SensoriOngelma");
        }


        return roomItems;
    }


}

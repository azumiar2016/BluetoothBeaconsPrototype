package bluetooth.project.bluetoothproximitysensor;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import org.altbeacon.beacon.Beacon;

import java.util.List;

public class CustomListAdapter extends ArrayAdapter<Beacon> {
    private Context context;
    private List<Beacon> beaconList;
    public CustomListAdapter(Context context, List<Beacon> beaconlist) {
        super(context, R.layout.beacon_list_item , beaconlist);
        this.context = context;
        this.beaconList = beaconlist;
    }
    private class ViewHolder {
        TextView beaconName;
        TextView beaconDistance;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.beacon_list_item, null);
            holder = new ViewHolder();
            holder.beaconName = (TextView) convertView
                    .findViewById(R.id.Beacon_Name);
            holder.beaconDistance = (TextView) convertView
                    .findViewById(R.id.Beacon_Distance);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // asetetaan arvot
        holder.beaconName.setText(beaconList.get(position).getBluetoothName());
        holder.beaconDistance.setText( String.valueOf(beaconList.get(position).getDistance()));



        return convertView;
    }
}

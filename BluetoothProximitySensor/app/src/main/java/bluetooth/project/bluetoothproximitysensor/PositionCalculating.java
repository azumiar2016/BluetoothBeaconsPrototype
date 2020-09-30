package bluetooth.project.bluetoothproximitysensor;
import android.app.Activity;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
public class PositionCalculating {

    private BeaconManager beaconManager;
    String x, y;
    double distance = 0;
    double roomx=3,roomy=3;
    double Xposition, Yposition;
    double nurkkaSensori1,nurkkaSensori2;
    String ip = "192.168.11.9";
    double beacon1,beacon2,beacon3;
    private Activity m_activity;

    public PositionCalculating(double roomXLength,double roomYLength){
        this.roomx = roomXLength;
        this.roomy = roomYLength;
    }
    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public void calculatePosition(){
         nurkkaSensori1 = round(this.beacon1,1);// nurkkasensori kohdassa x=roomx(maximum x),y=0
         nurkkaSensori2 = round(this.beacon2,1); // nurkkasensori2 kohdassa x=0,y=0

        //Jos ollaan lähellä huoneen keskiosaa, kompensoidaan virheitä



            if(nurkkaSensori1>nurkkaSensori2 && nurkkaSensori1+nurkkaSensori2<roomy ){
                nurkkaSensori2 = nurkkaSensori2+0.1;
            }
            if(nurkkaSensori2>nurkkaSensori1 && nurkkaSensori1+nurkkaSensori2<roomy){
                nurkkaSensori1 = nurkkaSensori1+0.1;
            }

    //    }
        //Jos ollaan toisen sensorin vieressä, toisen sensorin pakko olla roomy, koska sen verran sensoreilla on etäisyyttä

            if(nurkkaSensori1<0.2) {
                nurkkaSensori2=roomy;
                nurkkaSensori1 = 0;
            }
            if (nurkkaSensori2<0.2){
                nurkkaSensori1 = roomy;
                nurkkaSensori2 = 0;
            }



        //Laskennallisen osuuden avaamista, kun roomx = 3 ja roomy = 3
        //(i^2==nurkkasensori1*nurkkasensori1)
        //(j^2 == nurkkasensori2*nurkkasensori2)
        //x^2+y^2 = j^2
        //y^2=j^2-x^2
        //roomx=3, eli (3-x)....
        //(3-x)^2+y^2=i^2
        //(3-x)^2+j^2-x^2=i^2
        //9-6x+x^2+j^2-x^2=i^2
        //9-6x+j^2=i^2
        //9-6x=i^2-j^2
        //-6x = i^2-j^2-9
        //x = (i^2-j^2-9)/-6
        //y^2=j^2-x^2

         double y = (nurkkaSensori1*nurkkaSensori1-nurkkaSensori2*nurkkaSensori2-roomx*roomx)/(-2*roomx);
         double x = Math.sqrt((nurkkaSensori2*nurkkaSensori2)-(y*y));

         if(y>=3){
            y=3;
        }
         if(x>=3){
             x=3;
         }

        this.Xposition = x;
        this.Yposition = y;



    }

    public PositionCalculating(){
        this.beacon1 = 0;
        this.beacon2 = 0;
        this.beacon3 = 0;

    }
    public void UpdateBeacon1(double b){
        this.beacon1 = b;
    }
    public void UpdateBeacon2(double b){
        this.beacon2 = b;
    }
    public void UpdateBeacon3(double b){
        this.beacon3 = b;
    }
    public void Update(double beacon1,double beacon2, double beacon3){
        this.beacon1 = beacon1;
        this.beacon2 = beacon2;
        this.beacon3 = beacon3;

    }
    public double GetXPosition(){

        return Xposition;
    }

    public double GetYPosition(){

        return Yposition;
    }

    public String GetXPositionString(){
        double xpositiontemp = this.Xposition;
        xpositiontemp = xpositiontemp*100; // käytetään senttimetrejä
        DecimalFormat df = new DecimalFormat("###");
        String xpostionString = df.format(xpositiontemp);
        return xpostionString;
    }

    public String GetYPositionString(){
        double ypositiontemp = this.Yposition;
        ypositiontemp = ypositiontemp*100; // käytetään senttimetrejä
        DecimalFormat df = new DecimalFormat("###");
        String ypostionString = df.format(ypositiontemp);
        return ypostionString;
    }
    @Override
    public String toString(){
        DecimalFormat df = new DecimalFormat("###");
        String xpostion = df.format(this.Xposition);
        String ypostion = df.format(this.Yposition);
        return "kaja: "+String.valueOf(this.nurkkaSensori1) + " iot: " + String.valueOf(this.nurkkaSensori2) +"\n"
                + "x-arvo: " + GetXPositionString() + " y-arvo: " + GetYPositionString();
    }
}

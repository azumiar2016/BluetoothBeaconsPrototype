package bluetooth.project.bluetoothproximitysensor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class DebugActivity extends AppCompatActivity {

    // TAG Logcat
    protected static final String TAG = "CursoAndroid";
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        Log.i(TAG,getClassName()+".onCreate() ."+ b);
    }
    protected void onStart(){
        super.onStart();
        Log.i(TAG,getClassName()+".onStart() .");
    }
    protected void onRestart(){
        super.onRestart();
        Log.i(TAG,getClassName()+".onRestart() .");
    }

    protected void onResume(){
        super.onResume();
        Log.i(TAG,getClassName()+".onResume() .");
    }

    protected void onPause(){
        super.onPause();
        Log.i(TAG,getClassName()+".onPause() .");
    }

    protected void onStop(){
        super.onStop();
        Log.i(TAG,getClassName()+".onStop() .");
    }
    protected void onDestroy(){
        super.onDestroy();
        Log.i(TAG,getClassName()+".onDestroy() .");
    }
    private String getClassName(){
        String s = getClass().getName();
        return s.substring(s.lastIndexOf("."));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        Log.i(TAG,getClassName()+".onSaveInstanceState() .");
    }
}

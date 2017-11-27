/**
 * https://stackoverflow.com/questions/4300291/example-communication-between-activity-and-service-using-messaging
 * https://gist.github.com/joshdholtz/4522551
 */

package comblanchy.httpsgithub.alphafitness;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.RemoteConnection;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class RecordWorkout extends AppCompatActivity implements OnMapReadyCallback {

    MyIntentService remoteService;
    RemoteConnection remoteConnection = null;
    private boolean isConnected = false;
    private GoogleMap workoutMap;

    private TextView distancedata;
    private TextView timedata;

    private TextView avgdata;
    private TextView maxdata;
    private TextView mindata;

    private Handler stepTimeHandler = new Handler();

    @Override
    public void onMapReady(GoogleMap googleMap) {
        workoutMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        workoutMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        workoutMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    class RemoteConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            remoteService = MyIntentService.Stub.asInterface((IBinder) service);
            Toast.makeText(RecordWorkout.this,
                    "Remote Service connected.", Toast.LENGTH_LONG).show();
            stepTimeHandler.postDelayed(updatePortrait, 20);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            remoteService = null;
            Toast.makeText(RecordWorkout.this,
                    "Remote Service disconnected.", Toast.LENGTH_LONG).show();

            stepTimeHandler.removeCallbacks(updatePortrait);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_workout);
        //startRemote();
        //remoteConnection = new RemoteConnection();
        //TODO: retrieve database for service

        Configuration config = getResources().getConfiguration();
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (findViewById(R.id.chart) != null) {
                LineChart chart = (LineChart) findViewById(R.id.chart);

                int[] sample = {2,4,6};
                int[] sample2 = {1,3,7};

                List<Entry> entries = new ArrayList<Entry>();

                entries.add(new Entry(sample[0], sample2[0]));
                entries.add(new Entry(sample[1], sample2[1]));
                entries.add(new Entry(sample[2], sample2[2]));

                LineDataSet dataSet = new LineDataSet(entries, "Label");

                LineData lineData = new LineData(dataSet);
                chart.setData(lineData);
                chart.invalidate(); // refresh
            }
        } else {
            distancedata = (TextView) findViewById(R.id.distancedata);
            timedata = (TextView) findViewById(R.id.timedata);
        }
    }

    private Runnable updatePortrait = new Runnable() {
        @Override
        public void run() {
            try {
                updateStepTime();
                stepTimeHandler.postDelayed(this, 1000);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    public void toProfile(View view) {
        Intent intent = new Intent(this, ProfileScreen.class);
        startActivity(intent);
    }

    public void toggleWorkout(View view) {
        Button b = (Button) findViewById(R.id.workoutbutton);
        if (b.getText().equals("Start Workout")) {
            b.setText("Stop Workout");
            //TODO: start workout
            if (remoteConnection == null) {
                startRemote();
            }
            Toast.makeText(RecordWorkout.this,
                    "Using service", Toast.LENGTH_LONG).show();
        }
        else {
            b.setText("Start Workout");
            //TODO: stop workout
            stopRemote();
            Toast.makeText(RecordWorkout.this,
                    "Destroy service.", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //TODO: destroy database for service
    }

    public void startRemote() {
        remoteConnection = new RemoteConnection();
        Intent intent = new Intent();
        intent.setClassName("comblanchy.httpsgithub.alphafitness",
                comblanchy.httpsgithub.alphafitness.PedometerService.class.getName());
        if (!bindService(intent, remoteConnection, BIND_AUTO_CREATE)) {
            Toast.makeText(RecordWorkout.this,
                    "Fail to bind the remote service.", Toast.LENGTH_LONG).show();
        }
        else {
            bindService(intent, remoteConnection, 0);
            isConnected = true;
        }
    }

    public void stopRemote() {
        if (remoteConnection != null) {
            unbindService(remoteConnection);
            Log.d("unbinding", "unbindService()");
            remoteConnection = null;
            Log.d("unbinding", "set to null");
        }
        isConnected = false;

        //TODO: access database, save
        
    }

    public void test(View view) throws RemoteException {
        Button tester = (Button) findViewById(R.id.button2);
        if (remoteConnection != null) {
            updateStepTime();
        }
        else {
            tester.setText("not connected");
        }
    }

    public void updateStepTime() throws RemoteException {
        if (remoteConnection != null) {
            int seconds = remoteService.countSec();
            int minutes = seconds/60;
            seconds = seconds % 60;
            timedata.setText(minutes + ":" + seconds);
            distancedata.setText((remoteService.countSteps() *  0.0005) + "");
        }
    }

    public void updateLandscape() throws RemoteException {

    }

    public void addToChart() {

    }

    /**
     * https://stackoverflow.com/questions/9159896/android-update-ui-from-handler-every-second
     * https://stackoverflow.com/questions/18671067/how-to-stop-handler-runnable
     */

}

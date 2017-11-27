/**
 * https://stackoverflow.com/questions/4300291/example-communication-between-activity-and-service-using-messaging
 * https://gist.github.com/joshdholtz/4522551
 */

package comblanchy.httpsgithub.alphafitness;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.Color;
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

    private LineChart lineChart;
    private LineDataSet dataSetCalories;
    private LineDataSet dataSetDistance;
    private LineData lineData;

    static final String STATE_SECONDS = "seconds";
    static final String STATE_DISTANCE = "distance";
    static final String STATE_STEPS = "steps";

    private int weight = 1;
    private int steps = 0;
    private int seconds = 0;

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
            /*
            if (findViewById(R.id.chart) != null) {
                stepTimeHandler.postDelayed(updateDetails, 20);
            }
            else if (findViewById(R.id.timedata) != null) {
                stepTimeHandler.postDelayed(updatePortrait, 20);
            }
            */
            //registerReceiver(landscapeReceiver, new IntentFilter("workoutDetails"));
            //registerReceiver(portraitReceiver,new IntentFilter("workoutSession"));
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            remoteService = null;
            Toast.makeText(RecordWorkout.this,
                    "Remote Service disconnected.", Toast.LENGTH_LONG).show();
            /*
            stepTimeHandler.removeCallbacks(updatePortrait);
            stepTimeHandler.removeCallbacks(updateDetails);
            */
        }
    }

    private BroadcastReceiver portraitReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Toast.makeText(RecordWorkout.this,
            //        "broadcast recieved.", Toast.LENGTH_LONG).show();
            if (intent.hasExtra("steps")) {
                try {
                    steps = intent.getIntExtra("steps", 0);
                    seconds = intent.getIntExtra("seconds", 0);
                    updateStepTime(seconds, steps);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private BroadcastReceiver landscapeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(RecordWorkout.this,
                    "broadcast recieved.", Toast.LENGTH_SHORT).show();
            if (intent.hasExtra("steps")) {
                try {
                    steps = intent.getIntExtra("steps", 0);
                    seconds = intent.getIntExtra("seconds", 0);
                    updateLandscape(intent.getIntExtra("steps", 0), intent.getIntExtra("seconds", 0));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    };

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
                lineChart = (LineChart) findViewById(R.id.chart);

                List<Entry> calorieEntries = new ArrayList<Entry>();
                List<Entry> distanceEntries = new ArrayList<Entry>();

                Entry sample = new Entry(0, (float) 0);

                calorieEntries.add(sample);
                distanceEntries.add(sample);

                dataSetCalories = new LineDataSet(calorieEntries, "CALORIES");
                dataSetDistance = new LineDataSet(distanceEntries, "DISTANCE");

                dataSetCalories.setColor(Color.MAGENTA);
                dataSetDistance.setColor(Color.GREEN);

                lineData = new LineData(dataSetCalories, dataSetDistance);
                lineChart.setData(lineData);
                lineChart.invalidate(); // refresh

            }
            if (isConnected) {
                startRemote();

                //registerReceiver(portraitReceiver,new IntentFilter("workoutSession"));
            }
            registerReceiver(landscapeReceiver, new IntentFilter("workoutDetails"));
        } else {
            distancedata = (TextView) findViewById(R.id.distancedata);
            timedata = (TextView) findViewById(R.id.timedata);

            if (isConnected) {
                startRemote();
                //registerReceiver(landscapeReceiver, new IntentFilter("workoutDetails"));
                registerReceiver(portraitReceiver,new IntentFilter("workoutSession"));
            }
        }



    }
/*
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
    */

    /*
    private Runnable updateDetails = new Runnable() {
        @Override
        public void run() {
            try {

            } catch (RemoteException e) {
                e.printStackTrace();
            }
            stepTimeHandler.postDelayed(this, 10000);
        }
    };
    */

    public void toProfile(View view) {
        Intent intent = new Intent(this, ProfileScreen.class);
        intent.putExtra(STATE_STEPS, steps);
        intent.putExtra(STATE_SECONDS, seconds);
        intent.putExtra("Connection", true);

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
        registerReceiver(portraitReceiver,new IntentFilter("workoutSession"));
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

    public void updateStepTime(int seconds, int steps) throws RemoteException {
        if (remoteConnection != null) {
            int sec = seconds;
            int minutes = seconds/60;
            sec = sec % 60;
            timedata.setText(minutes + ":" + seconds);
            distancedata.setText((steps *  0.0005) + "");
        }
    }

    private int prevSteps = 0;

    public void updateLandscape(int steps, int sec) throws RemoteException {

        addToChart(steps, sec);

        TextView avgdata = (TextView) findViewById(R.id.avgdata);
        TextView maxdata = (TextView) findViewById(R.id.maxdata);
        TextView mindata = (TextView) findViewById(R.id.mindata);

        double max = Double.parseDouble((String) maxdata.getText());
        double min = Double.parseDouble((String) mindata.getText());

        double avg = steps/sec;

        if (avg > max) {
            maxdata.setText(avg + "");
        }
        else if (avg < min) {
            mindata.setText(avg + "");
        }

        avgdata.setText(avg +"");
        prevSteps = steps;
    }

    public void addToChart(int steps, int sec) {
        double datadistance = steps/2000;
        double datacalories = 0.5 * weight / 20;

        dataSetCalories.addEntry(new Entry(sec, (float) datacalories));
        dataSetDistance.addEntry(new Entry(sec, (float) datadistance));
        lineData.notifyDataChanged(); // let the data know a dataSet changed
        lineChart.notifyDataSetChanged(); // let the chart know it's data changed
        lineChart.invalidate();
    }

    /**
     * https://stackoverflow.com/questions/9159896/android-update-ui-from-handler-every-second
     * https://stackoverflow.com/questions/18671067/how-to-stop-handler-runnable
     */


}

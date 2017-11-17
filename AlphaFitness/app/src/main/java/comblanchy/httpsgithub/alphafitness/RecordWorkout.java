package comblanchy.httpsgithub.alphafitness;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.RemoteConnection;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class RecordWorkout extends AppCompatActivity {

    MyIntentService remoteService;
    RemoteConnection remCon = null;

    class RemoteConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            remoteService = MyIntentService.Stub.asInterface((IBinder) service);
            Toast.makeText(RecordWorkout.this, "Remote Service connected", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            remoteService = null;
            Toast.makeText(RecordWorkout.this, "Remote Service disconnected", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_workout);

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
    }

    public void toProfile(View view) {
        Intent intent = new Intent(this, ProfileScreen.class);
        startActivity(intent);
    }

    public void toggleWorkout() {
        Button b = (Button) findViewById(R.id.workoutbutton);
        if (b.getText().equals("Start Workout")) {
            b.setText("Stop Workout");
            //TODO: start workout
        }
        else {
            b.setText("Start Workout");
            //TODO: stop workout
        }
    }
}

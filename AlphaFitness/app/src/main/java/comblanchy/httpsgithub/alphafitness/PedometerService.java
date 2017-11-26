package comblanchy.httpsgithub.alphafitness;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.Timer;
import java.util.TimerTask;

public class PedometerService extends Service implements SensorEventListener {

    MyIntentService.Stub mBinder;
    private int i;
    private int steps;
    private int seconds;

    private SensorManager sensormanager;
    private Sensor sensordetector;

    public PedometerService() {
        
    }

    @Override
    public void onCreate() {

        sensormanager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensordetector = sensormanager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        sensormanager.registerListener(this, sensordetector, SensorManager.SENSOR_DELAY_NORMAL);

        seconds = 0;
        steps = 0;
        super.onCreate();
        i = 0;
        Timer t = new Timer();
        t.schedule(new TimerTask()
        {
            public void run()
            {
                seconds++;
            }
        }, 0, 1000);
        mBinder = new MyIntentService.Stub() {
            @Override
            public int countSteps() throws RemoteException {
                return steps;
            }

            @Override
            public int countSec() throws RemoteException {
                return seconds;
            }

            @Override
            public int calcAverage() throws RemoteException {
                return i/5;
            }

            @Override
            public int calcMax() throws RemoteException {
                return i*6;
            }

            @Override
            public int calcMin() throws RemoteException {
                return seconds;
            }
        };
    }

    public void someMethod() {
        i++;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensordetector.getType() == Sensor.TYPE_STEP_DETECTOR) {
            steps++;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}

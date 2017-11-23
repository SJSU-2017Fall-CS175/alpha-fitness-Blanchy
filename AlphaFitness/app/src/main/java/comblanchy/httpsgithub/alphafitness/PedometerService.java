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
    private Sensor sensorcounter;

    public PedometerService() {
    }

    @Override
    public void onCreate() {

        sensormanager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorcounter = sensormanager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        sensormanager.registerListener(this, sensorcounter, SensorManager.SENSOR_DELAY_NORMAL);

        steps = 0;
        super.onCreate();
        i = 0;
        Timer t = new Timer();
        t.schedule(new TimerTask()
        {
            public void run()
            {
                someMethod();
            }
        }, 0, 10000);
        mBinder = new MyIntentService.Stub() {
            public int countSteps() throws RemoteException {
                return i;
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
                return i+3;
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
        if (sensorcounter.getType() == Sensor.TYPE_STEP_COUNTER) {
            steps = (int) sensorEvent.values[0];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}

package comblanchy.httpsgithub.alphafitness;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.Timer;
import java.util.TimerTask;

public class PedometerService extends Service {

    MyIntentService.Stub mBinder;
    private int i;

    public PedometerService() {
    }

    @Override
    public void onCreate() {
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
                return 1;
            }

            @Override
            public int calcMax() throws RemoteException {
                return 2;
            }

            @Override
            public int calcMin() throws RemoteException {
                return 3;
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
}

package comblanchy.httpsgithub.alphafitness;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RecordWorkout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_workout);
    }

    public void toProfile(Intent intent) {
        intent = new Intent(this, ProfileScreen.class);
        startActivity(intent);
    }
}

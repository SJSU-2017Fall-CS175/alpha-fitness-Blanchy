package comblanchy.httpsgithub.alphafitness;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileScreen extends AppCompatActivity {

    private EditText weight;
    private EditText gender;

    private TextView avdist;
    private TextView avtime;
    private TextView avworkouts;
    private TextView avcalories;

    private TextView ttdist;
    private TextView tttime;
    private TextView ttworkouts;
    private TextView ttcalories;

    private int DBsteps = 0;
    private int DBtime = 0;

    private int tempSteps = 0;
    private int tempTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent.hasExtra("steps")) {
            tempSteps = intent.getIntExtra("steps", 0);
        }
        if (intent.hasExtra("seconds")) {
            tempTime = intent.getIntExtra("seconds", 0);
        }

        setContentView(R.layout.activity_profile_screen);

        weight = (EditText) findViewById(R.id.weightinput);
        gender = (EditText) findViewById(R.id.genderinput);

        avdist = (TextView) findViewById(R.id.avdistdata); // 0.0005 mi * 1 step / 7 days
        avtime = (TextView) findViewById(R.id.avtimedata);
        avworkouts = (TextView) findViewById(R.id.avworkoutdata); // number of workouts / 7 days
        avcalories = (TextView) findViewById(R.id.avgcaldata);

        ttdist = (TextView) findViewById(R.id.ttdistdata); // 0.0005 mi * 1 step
        tttime = (TextView) findViewById(R.id.tttimedata);
        ttworkouts = (TextView) findViewById(R.id.ttworkoutdata);
        ttcalories = (TextView) findViewById(R.id.ttcaldata); // 0.5 * weight / 20 minutes

        retrieveRecord();

        populate();

        weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                populate();
            }
        });
    }

    public void populate() {
        int mWeight = 1;
        if (weight.getText().length() > 0) {
            mWeight = Integer.parseInt(String.valueOf(weight.getText()));
        }
        else  {
            weight.setText("1");
        }

        double steps = DBsteps + DBtime;
        double distance = 0.0005*(steps);
        double calories = 0.5 * mWeight / 20;
        double time = DBtime + tempTime;

        avdist.setText((distance/7) + "");
        avtime.setText((time/7) + "");
        avworkouts.setText("");
        avcalories.setText((calories/7) + "");

        ttdist.setText(distance + "");
        tttime.setText(time + " seconds");
        ttworkouts.setText("");
        ttcalories.setText(calories + "");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //TODO: store in database
    }

    public void retrieveRecord() {
        String URL = "content://com.wearable.myprovider/user";
        Uri devices = Uri.parse(URL);
        Cursor c = managedQuery(devices, null, null, null, "name");

        if (c.moveToFirst()) {
            do {
                Toast.makeText(ProfileScreen.this,
                        c.getString(c.getColumnIndex(MyContentProvider._ID))
                                + ", "
                                +  c.getString(
                                c.getColumnIndex( MyContentProvider.NAME))
                                + ", "
                                + c.getString(
                                c.getColumnIndex( MyContentProvider.GENDER))
                                + ", "
                                + c.getString(
                                c.getColumnIndex( MyContentProvider.WEIGHT))
                                + ", "
                                + c.getString(
                                c.getColumnIndex( MyContentProvider.STEPS))
                                + ", "
                                + c.getString(
                                c.getColumnIndex( MyContentProvider.TIME))
                        ,
                        Toast.LENGTH_SHORT).show();
            } while (c.moveToNext());
        }
    }
}


package comblanchy.httpsgithub.alphafitness;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileScreen extends AppCompatActivity {

    private EditText name;
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

    WorkoutDBHelper db = new WorkoutDBHelper(this);
    private int DBsteps = 0;
    private int DBtime = 0;
    private WorkoutEntry we;

    private int tempSteps = 0;
    private int tempTime = 0;

    static final String PROVIDER = "com.wearable.myprovider";
    static final String URL = "content://" + PROVIDER + "/user";

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

        name = (EditText) findViewById(R.id.nameinput);
        weight = (EditText) findViewById(R.id.weightinput);
        gender = (EditText) findViewById(R.id.genderinput);

        avdist = (TextView) findViewById(R.id.avdistdata); // 0.0005 mi * 1 step / 7 days
        avtime = (TextView) findViewById(R.id.avtimedata);
        avworkouts = (TextView) findViewById(R.id.avworkoutdata); // number of workouts / 7 days
        avcalories = (TextView) findViewById(R.id.avgcaldata);

        ttdist = (TextView) findViewById(R.id.ttdistdata); // 0.0005 mi * 1 step
        tttime = (TextView) findViewById(R.id.tttimedata);
        ttworkouts = (TextView) findViewById(R.id.ttworkoutdata);
        ttcalories = (TextView) findViewById(R.id.ttcaldata); // 0.5 * weight  / 20 steps

        //DBsteps = db.getSumInt(db.STEPS);
        //DBtime = db.getSumInt(db.TIME);

        we = db.getLatestProfile();
        Log.d("Retrieved from DB",we.getName());

        populate();
        Log.d("Profile screen","populated");

        weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                we.setWeight(Integer.parseInt(weight.getText().toString()));
                we.setName(name.getText().toString());
                we.setGender(gender.getText().toString());


                double steps = DBsteps + tempSteps;
                double calories = (0.5 * we.getWeight() / 20) * steps;
                avcalories.setText((calories/7) + " calories");
                ttcalories.setText(calories + " calories");

                //db.editEntry(we);
            }
        });
    }

    public void populate() {

        name.setText(we.getName());
        gender.setText(we.getGender());
        weight.setText(we.getWeight() + "");

        DBsteps = db.getSumSteps();
        Log.d("Database query", "steps = " + DBsteps);
        DBtime = db.getSumSeconds();
        Log.d("Database query", "seconds = " + DBtime);

        int mWeight = 1;
        if (weight.getText().length() > 0) {
            mWeight = Integer.parseInt(String.valueOf(weight.getText()));
        }
        else  {
            weight.setText("1");
        }

        double steps = DBsteps + tempSteps;
        double distance = 0.0005*(steps);
        double calories = (0.5 * mWeight / 20) * steps;
        double time = DBtime + tempTime;

        int workouts = db.getNumEntries();

        avdist.setText((distance/7) + " mi");
        avtime.setText((time/7) + " seconds");
        avworkouts.setText((workouts/7) +" times");
        avcalories.setText((calories/7) + " calories");

        ttdist.setText(distance + " mi");
        tttime.setText(time + " seconds");
        ttworkouts.setText(workouts + " times");
        ttcalories.setText(calories + " calories");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        saveToDB();
    }

    public void saveToDB() {
        db.addEntry(new WorkoutEntry(tempTime, tempSteps,
                name.getText().toString(), Integer.parseInt(weight.getText().toString()), gender.getText().toString()));
    }

}


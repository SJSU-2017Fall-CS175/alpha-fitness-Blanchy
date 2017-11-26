package comblanchy.httpsgithub.alphafitness;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

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

    private int DBsteps;
    private int DBtime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        //TODO: get DB stats and fill name, gender, DBsteps, DBtime, weight

        populate();
    }

    public void populate() {
        int mWeight = Integer.parseInt(String.valueOf(weight.getText()));

        double distance = 0.0005*(DBsteps);
        double calories = 0.5 * mWeight / 20;



        avdist.setText((distance/7) + "");
        //avtime
        //avworkouts
        avcalories.setText((calories/7) + "");

        ttdist.setText(distance + "");
        //tttime
        //ttworkouts
        ttcalories.setText(calories + "");
    }

    public void update() {

    }
}

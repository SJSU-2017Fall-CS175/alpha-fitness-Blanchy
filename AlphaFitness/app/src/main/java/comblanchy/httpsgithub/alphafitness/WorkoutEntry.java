package comblanchy.httpsgithub.alphafitness;

/**
 * Created by blanchypolangcos on 12/3/17.
 */

public class WorkoutEntry {

    private int _id;
    private int seconds;
    private int steps;

    private String name;
    private String gender;
    private int weight;

    public WorkoutEntry() {
        this.seconds = 0;
        this.steps = 0;
        this.name = "";
        this.gender = "";
        this.weight = 0;
    }

    public WorkoutEntry(int id, int seconds, int steps, String name, int weight, String gender) {
        _id = id;
        this.seconds = seconds;
        this.steps = steps;
        this.name = name;
        this.weight = weight;
        this.gender = gender;
    }

    public WorkoutEntry(int seconds, int steps, String name, int weight, String gender) {
        this.seconds = seconds;
        this.steps = steps;
        this.name = name;
        this.weight = weight;
        this.gender = gender;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int id) {
        _id = id;
    }

    public int getSeconds() {
        return seconds;
    }

    public double getSteps() {
        return steps;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void saveToDB() {

    }
}

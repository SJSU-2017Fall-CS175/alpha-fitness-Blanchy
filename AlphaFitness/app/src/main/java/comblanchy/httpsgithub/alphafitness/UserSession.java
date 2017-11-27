package comblanchy.httpsgithub.alphafitness;

/**
 * Created by blanchypolangcos on 11/26/17.
 */

class UserSession {
    private static final UserSession ourInstance = new UserSession();

    static UserSession getInstance() {
        return ourInstance;
    }

    private UserSession() {
    }
}

package pt.testbench.olt.model;

public class StatusSingleton {
    
    private static Status single_instance = null;

    public static Status getInstance() {
        if(single_instance == null) {
            single_instance = new Status();
        }
        return single_instance;
    }
}

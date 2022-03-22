package src;

import java.io.Serializable;

public abstract class Driver implements Serializable {
    private String driverName;
    private String driverLocation;
    private String constructorTeam;

    public Driver(String driverName, String driverLocation, String constructorTeam) {
        this.driverName = driverName;
        this.driverLocation = driverLocation;
        this.constructorTeam = constructorTeam;
    }

//    setter to change the constructor team
    public void changeConstructor(String newConstructor){
        constructorTeam = newConstructor;
    }

//    few simple getters.

    public String getDriverName() {
        return driverName;
    }

    public String getDriverLocation() {
        return driverLocation;
    }

    public String getConstructorTeam() {
        return constructorTeam;
    }
}

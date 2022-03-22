package src;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Race implements Serializable {
    private final Date raceDate;
    private ArrayList<Formula1Driver> driverPositions = new ArrayList<>();

//    constructor to make sure the Race was created with a date.
    public Race(Date raceDate) {
        this.raceDate = raceDate;
    }

//    few simple getters.

    public int getNumOfDrivers() {
        return driverPositions.size();
    }

    public Date getRaceDate() {
        return raceDate;
    }

    public ArrayList<Formula1Driver> getDriverPositions() {
        return driverPositions;
    }

    public int getDriverPosition(Formula1Driver fDriverRef) {
        int positionCount = 0;
        for (Formula1Driver f1Driver: driverPositions) {
            positionCount++;
            if (f1Driver.getDriverName().equalsIgnoreCase(fDriverRef.getDriverName())) {
                return positionCount;
            }
        }
        return 0;
    }

//    Add another position in the race.
    public void addDriverPosition(Formula1Driver fDriver) {
        driverPositions.add(fDriver);
        if (!fDriver.getDriverName().equalsIgnoreCase("DELETED_DRIVER")) {
            updateDriverStats(fDriver);
        }
    }

//    simple method to check whether the given driver is a participant of the race.
    public boolean isParticipant(Formula1Driver fDriverRef) {
        for (Formula1Driver f1Driver: driverPositions) {
            if (f1Driver.getDriverName().equalsIgnoreCase(fDriverRef.getDriverName())) return true;
        }
        return false;
    }

//    to delete a driver from the race when that driver deleted from championship.
    public void driverDeletedTrigger(Formula1Driver fDriverRef) {
        for (int i=0; i<driverPositions.size(); i++) {
            if (driverPositions.get(i).getDriverName() == fDriverRef.getDriverName()) {
                driverPositions.set(i, new Formula1Driver("DELETED_DRIVER","DELETED_DRIVER_LOCATION", "DELETED_DRIVER_CONSTRUCTOR"));
            }
        }
    }

//    to update the stats of the driver when he/she got a position of the race.
    private void updateDriverStats(Formula1Driver fDriver) {
        int driverPosition = driverPositions.size();

        fDriver.addRace();
        fDriver.addPoints(driverPosition);

        switch (driverPosition) {
            case 1 -> fDriver.addFirstPosition();
            case 2 -> fDriver.addSecondPosition();
            case 3 -> fDriver.addThirdPosition();
        }
    }
}

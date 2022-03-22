package src;

import Enums.*;
import java.util.ArrayList;
import java.util.Date;

public interface ChampionshipManager {
    void loadData();
    void saveChanges();
    String[][] addNewRace(Date raceDate, ArrayList<Integer> driverPositionIds);
    String[][] getPointsTable(SortOp sortOp);
    void showDriverStats(int driverIndex);
    void changeDriverOfConstructor(int driverIndex, String newDriverName, String newDriverLocation);
    void deleteDriver(int driverIndex);
    void createDriver(String driverName, String driverLocation, String constructorName);

    String[][] addRandomRace(RaceAddOp raceAddOp);
    String[][] getRaceTable();
    String[][] getSearchResults(String searchFieldText);
    String[][] getDriverRacesTable(int driverIndex);
}

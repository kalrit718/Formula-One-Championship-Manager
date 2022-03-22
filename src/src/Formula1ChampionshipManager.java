package src;

import Comparators.*;
import Enums.*;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Formula1ChampionshipManager implements ChampionshipManager {

    private static ArrayList<Formula1Driver> driverLst = new ArrayList<>();

    private static ArrayList<Race> raceList = new ArrayList<>();

//    loading the data from .ser file.
    @Override
    public void loadData() {
        try {
//            loading the data of the driverList arraylist.
            File driverListFile = new File("DriverListFile.ser");
            FileInputStream driverListFileInputStream = new FileInputStream(driverListFile);
            ObjectInputStream driverListObjectInputStream = new ObjectInputStream(driverListFileInputStream);

            driverLst = (ArrayList<Formula1Driver>) driverListObjectInputStream.readObject();

            System.out.println("DriverListFile.ser file loaded successfully!");
            driverListObjectInputStream.close();
            driverListFileInputStream.close();

//            loading the data of the raceList arraylist.
            File raceListFile = new File("RaceListFile.ser");
            FileInputStream raceListFileInputStream = new FileInputStream(raceListFile);
            ObjectInputStream raceListObjectInputStream = new ObjectInputStream(raceListFileInputStream);

            raceList = (ArrayList<Race>) raceListObjectInputStream.readObject();

            System.out.println("RaceListFile.ser file loaded successfully!");
            raceListObjectInputStream.close();
            raceListFileInputStream.close();
        } catch (IOException e) {
            throw new InputMismatchException("An error occurred when loading the files!");
        } catch (ClassNotFoundException e) {
            throw new IllegalCallerException("Class type of the object not found!");
        }
    }

//    saving the data to .ser file.
    @Override
    public void saveChanges() {
        try {
//            loading the data of the driverList arraylist.
            FileOutputStream driverListFileOutputStream = new FileOutputStream("DriverListFile.ser");
            ObjectOutputStream driverListObjectOutputStream = new ObjectOutputStream(driverListFileOutputStream);

            driverListObjectOutputStream.writeObject(driverLst);

            System.out.println("DriverListFile.ser file saved successfully!");
            driverListObjectOutputStream.close();
            driverListFileOutputStream.close();

//            loading the data of the raceList arraylist.
            FileOutputStream raceListFileOutputStream = new FileOutputStream("RaceListFile.ser");
            ObjectOutputStream raceListObjectOutputStream = new ObjectOutputStream(raceListFileOutputStream);

            raceListObjectOutputStream.writeObject(raceList);

            System.out.println("RaceListFile.ser file saved successfully!");
            raceListObjectOutputStream.close();
            raceListFileOutputStream.close();
        } catch (IOException e) {
            throw new InputMismatchException("An error occurred when saving the files!");
        }
    }

//    adding a new race to the raceList arraylist and returning the positions of drivers.
    @Override
    public String[][] addNewRace(Date raceDate, ArrayList<Integer> driverPositionIds) {
//        checking for a invalid index.
        for (int driverIndex: driverPositionIds) {
            if (!isValidIndex(driverIndex)) {
                throw new IllegalArgumentException("Invalid index number!");
            }
        }

//        adding the race.
        Race fRace = new Race(raceDate);
        raceList.add(fRace);

        String[][] addedDriverPositions = new String[driverPositionIds.size()][2];

        for (int i=0; i<driverPositionIds.size(); i++) {
            fRace.addDriverPosition(driverLst.get(driverPositionIds.get(i)));
            addedDriverPositions[i][0] = driverLst.get(driverPositionIds.get(i)).getDriverName();
            addedDriverPositions[i][1] = Integer.toString(fRace.getNumOfDrivers());
        }
        return addedDriverPositions;
    }

//    to return the points table as a 2d array with the required sort.
    @Override
    public String[][] getPointsTable(SortOp sortOp) {

//        sorting the table accordingly.
        if (sortOp.equals(SortOp.POINTS_DSC)) {
            Collections.sort(driverLst, new FirstPositionsComparator().reversed());
            Collections.sort(driverLst, new PointsComparator().reversed());
        } else if (sortOp.equals(SortOp.POINTS_ASC)) {
            Collections.sort(driverLst, new PointsComparator());
        } else if (sortOp.equals(SortOp.FIRSTPOS_DSC)) {
            Collections.sort(driverLst, new FirstPositionsComparator().reversed());
        }

//        populating the points table data to the 2d array.
        String[][] pointsTableContent = new String[driverLst.size()][8];
        for (int i=0; i<driverLst.size(); i++) {
            pointsTableContent[i] = new String[] {
                    Integer.toString(i+1),
                    driverLst.get(i).getDriverName(),
                    driverLst.get(i).getConstructorTeam(),
                    Integer.toString(driverLst.get(i).getCurrentPoints()),
                    Integer.toString(driverLst.get(i).getNumOfFirstPositions()),
                    Integer.toString(driverLst.get(i).getNumOfSecondPositions()),
                    Integer.toString(driverLst.get(i).getNumOfThirdPositions()),
                    Integer.toString(driverLst.get(i).getNumberOfRaces())
            };
        }
        return pointsTableContent;
    }

//    to get the statistics of the required driver.
    @Override
    public void showDriverStats(int driverIndex) {
        if (!isValidIndex(driverIndex)) {
            throw new IllegalArgumentException("Not a valid index number!");
        }
        System.out.println(driverLst.get(driverIndex).toString());
    }

//    to change the driver for a constructor.
    @Override
    public void changeDriverOfConstructor(int driverIndex, String newDriverName, String newDriverLocation) {
        String driverConstructor;

        if (!isValidIndex(driverIndex)) {
            throw new IllegalArgumentException("Not a valid index number!");
        }
        driverConstructor = driverLst.get(driverIndex).getConstructorTeam();

//        deleting the existing driver and adding the new driver with same constructor team.
        deleteDriver(driverIndex);
        createDriver(newDriverName, newDriverLocation, driverConstructor);

        System.out.println("\nDriver for constructor " + driverConstructor + " changed Successfully!");
    }

//    to delete a specific driver.
    @Override
    public void deleteDriver(int driverIndex) {
        String driverName;

        if (!isValidIndex(driverIndex)) {
            throw new IllegalArgumentException("Not a valid index number!");
        }

        driverName = driverLst.get(driverIndex).getDriverName();

//        to remove the driver from previous race data.
        for (Race fRace:raceList) {
            fRace.driverDeletedTrigger(driverLst.get(driverIndex));
        }

//        removing the driver from driverList arraylist.
        driverLst.remove(driverIndex);
        System.out.println("\nDriver " + driverName + " removed Successfully!");
    }

//    to create a new driver.
    @Override
    public void createDriver(String driverName, String driverLocation, String constructorName) {

//        checking the prerequisites.
        if (driverName.isBlank() || driverLocation.isBlank() || constructorName.isBlank()) {
            throw new  IllegalArgumentException("Any of the fields can't be blank");
        }
        if (isDriverNameExist(driverName)) {
            throw new IllegalArgumentException("Driver name already exists!");
        }
        if (isConstructorExist(constructorName)) {
            throw new IllegalArgumentException("Constructor team already exists!");
        }

//        adding the new driver to the driverList arraylist.
        driverLst.add(new Formula1Driver(driverName, driverLocation, constructorName));
        System.out.println("\nDriver " + driverName + " Added Successfully!");
    }

//    adding an auto generated random race.
    @Override
    public String[][] addRandomRace(RaceAddOp raceAddOp) {
        ArrayList<Integer> driverPositionIds = new ArrayList<>();

        if (driverLst.size() < 10) {
            throw new IllegalArgumentException("You need at least 10 drivers to add a new race.");
        }

//        generating the positions randomly.
        for (int i=0; i<driverLst.size(); i++) {
            driverPositionIds.add(i);
        }
        Collections.shuffle(driverPositionIds);

//        checking whether the first position must include considering probability.
        if (raceAddOp == RaceAddOp.WITH_PROB) { //if true first shuffled driverPositionIds will be taken as starting positions of the race.
            int randomProb = new Random().nextInt(100);
            int firstPositionId;

//            selecting the first position accordingly.
            if (randomProb<40) {firstPositionId = 0;}
            else if (randomProb<70) {firstPositionId = 1;}
            else if (randomProb<80) {firstPositionId = 2;}
            else if (randomProb<90) {firstPositionId = 3;}
            else if (randomProb<92) {firstPositionId = 4;}
            else if (randomProb<94) {firstPositionId = 5;}
            else if (randomProb<96) {firstPositionId = 6;}
            else if (randomProb<98) {firstPositionId = 7;}
            else {firstPositionId = 8;}

//            getting the selected driver to the top of the positions.
            int selectedId = driverPositionIds.get(firstPositionId);
            driverPositionIds.remove(firstPositionId);
            Collections.shuffle(driverPositionIds);
            driverPositionIds.add(0, selectedId);
        }
        return addNewRace(randomDateGen(), driverPositionIds);
    }

//    get the table of past races
    @Override
    public String[][] getRaceTable() {
        String[][] raceTableContent = new String[raceList.size()][4];
        SimpleDateFormat simpDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
//            sorting the race by ascending order of dates.
            Collections.sort(raceList, new RaceDatesComparator());

//            getting the contents of each row.
            for (int i=0; i<raceList.size(); i++) {
                raceTableContent[i][0] = simpDateFormat.format(raceList.get(i).getRaceDate());
                raceTableContent[i][1] = raceList.get(i).getDriverPositions().get(0).getDriverName();
                raceTableContent[i][2] = raceList.get(i).getDriverPositions().get(1).getDriverName();
                raceTableContent[i][3] = raceList.get(i).getDriverPositions().get(2).getDriverName();
            }
        } catch (IndexOutOfBoundsException ex) {

        }
        return raceTableContent;
    }

//    get the race history of a specific driver.
    @Override
    public String[][] getDriverRacesTable(int driverIndex) {

        ArrayList<String[]> driverRacesTableContent = new ArrayList<>();
        SimpleDateFormat simpDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        for (int i=0; i<raceList.size(); i++) {
            if (raceList.get(i).isParticipant(driverLst.get(driverIndex))) {
                driverRacesTableContent.add(new String[] {
                        String.valueOf(simpDateFormat.format(raceList.get(i).getRaceDate())),
                        driverLst.get(driverIndex).getDriverName(),
                        Integer.toString(raceList.get(i).getDriverPosition(driverLst.get(driverIndex)))
                });
            }
        }
        String[][] finalContentArray = new String[driverRacesTableContent.size()][3];
        for (int i=0; i<driverRacesTableContent.size(); i++) {
            finalContentArray[i] = driverRacesTableContent.get(i);
        }

        return finalContentArray;
    }

//    get the search result as a 2d array
    @Override
    public String[][] getSearchResults(String searchFieldText) {
        String[][] filteredPointsTableContent = new String[driverLst.size()][8];
        int i=0; //to generate the indexes of the search results array.
        int j=0; //to keep the original index numbers of the drivers without messing it up.
        for (Formula1Driver f1Driver: driverLst){
            if (f1Driver.getDriverName().toLowerCase().contains(searchFieldText.toLowerCase())) {
                filteredPointsTableContent[i] = new String[] {
                        Integer.toString(j+1),
                        f1Driver.getDriverName(),
                        f1Driver.getConstructorTeam(),
                        Integer.toString(f1Driver.getCurrentPoints()),
                        Integer.toString(f1Driver.getNumOfFirstPositions()),
                        Integer.toString(f1Driver.getNumOfSecondPositions()),
                        Integer.toString(f1Driver.getNumOfThirdPositions()),
                        Integer.toString(f1Driver.getNumberOfRaces())
                };
                i++;
            }
            j++;
        }
        return filteredPointsTableContent;
    }

//    simple private method to check whether the given integer is valid or not.
    private boolean isValidIndex(int index) {
        return (index >= 0 && index<driverLst.size());
    }

//    simple private method to check the given constructor name's existence.
    private boolean isConstructorExist(String newConstructor) {
        for (Formula1Driver formula1Driver : driverLst) {
            if (formula1Driver.getConstructorTeam().equalsIgnoreCase(newConstructor)) return true;
        }
        return false;
    }

//    simple private method to check the given constructor name's existence.
    private boolean isDriverNameExist(String newDriverName) {
        for (Formula1Driver formula1Driver : driverLst) {
            if (formula1Driver.getDriverName().equalsIgnoreCase(newDriverName)) return true;
        }
        return false;
    }

//    simple private method to generate a random date within the year.
    private Date randomDateGen() {
        try {
            Date raceDate = new Date();
            int raceYear = (raceDate.getYear())+1900; //this year as the race year.
            int raceMonth = 1 + (int) Math.round(Math.random() * (12 - 1)); //random month from 1 to 12.
            int raceDay=0;

            switch (raceMonth){
                case 2: //in case if the race month is february.
                    if (raceYear%4 == 0){ //in case if the race month is february and also a leap year.
                        raceDay = 1 + (int) Math.round(Math.random() * (29 - 1));
                    } else {
                        raceDay = 1 + (int) Math.round(Math.random() * (28 - 1));
                    }
                    break;
                case 1: case 3: case 5: case 7: case 8: case 10: case 12: //in case if the race month has 31 days.
                    raceDay = 1 + (int) Math.round(Math.random() * (31 - 1));
                    break;
                case 4: case 6: case 9: case 11: //in case if the race month has 30 days.
                    raceDay = 1 + (int) Math.round(Math.random() * (30 - 1));
                    break;
            }

//            generating the date as a string.
            String raceDateStr = String.valueOf(raceDay).concat("/").concat(String.valueOf(raceMonth)).concat("/").concat(String.valueOf(raceYear));
            raceDate = new SimpleDateFormat("dd/MM/yyyy").parse(raceDateStr); //parsing the string date into Date type.
            return raceDate;
        } catch (ParseException e) {
            throw new IllegalArgumentException("An error occurred while generating the date!");
        }
    }
}

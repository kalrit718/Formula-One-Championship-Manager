package MainPackage;

import SwingFrames.PointsTableFrame;
import src.ChampionshipManager;
import src.Formula1ChampionshipManager;
import Enums.SortOp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        loadData(); //calling the method  to load the saved data if there is any.
        while (true) getInput(); //infinite loop to show the menu in the end of every method.
    }

//    creating the new instance of the championship manager. this instance will be used in every operation happens in program.
    public static ChampionshipManager f1cManager = new Formula1ChampionshipManager();

//    method to show the menu and get the input from user to run the operation.
    public static void getInput() {
        Scanner userInput = new Scanner(System.in);

        System.out.println("\n1) Create a new driver." +
                "\n2) Delete a driver and the team that the driver belongs to." +
                "\n3) Change the driver for an existing constructor team." +
                "\n4) Display the statistics for a existing driver." +
                "\n5) Display the Formula 1 Driver Table." +
                "\n6) Add a race completed." +
                "\n7) Save changes.");

        try {
//            calling the below methods according to the user input.
            System.out.println("\nEnter the index number of the task you want to do: ");
            switch (userInput.nextInt()) {
                case (1) -> createDriver();
                case (2) -> deleteDriver();
                case (3) -> changeConstructor();
                case (4) -> showDriverStats();
                case (5) -> showPointsTable();
                case (6) -> addNewRace();
                case (7) -> saveChanges();
            }
        } catch (InputMismatchException e) {
            System.out.println("Not a valid index number!");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred!");
        }
    }

//    a method to call load data method in the interface.
    public static void loadData() {
        try {
            f1cManager.loadData();
        } catch (InputMismatchException e) {
            System.out.println(e.getMessage());
        } catch (IllegalCallerException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred!");
        }
    }

//    a method to call save changes method in the interface.
    public static void saveChanges() {
        try {
            f1cManager.saveChanges();
        } catch (InputMismatchException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred!");
        }
    }

//    a method to get user inputs to add a new race
    public static void addNewRace() {
        Scanner userInput = new Scanner(System.in);
        ArrayList<Integer> driverPositionIds = new ArrayList<>();
        Date raceDate;

        try {
//            get the race date.
            System.out.println("\nEnter the date of the race(DD/MM/YYYY): ");
            String raceDateStr = userInput.nextLine();
            raceDate = new SimpleDateFormat("dd/MM/yyyy").parse(raceDateStr);

//            getting the driver positions accordingly.
            do {
                System.out.println("\nEnter the index number of the driver who achieved no." + (int) (driverPositionIds.size()+1) + " place: ");
                int driverIndex = userInput.nextInt()-1;
                userInput.nextLine();

                driverPositionIds.add(driverIndex);

                System.out.println("\nDo you want to enter the no." + (int) (driverPositionIds.size()+1) + " position(y/n): ");
            } while (userInput.nextLine().equalsIgnoreCase("y"));

            String[][] addedDriverPositions = f1cManager.addNewRace(raceDate, driverPositionIds);

//            printing the names and positions of added drivers.
            System.out.println();
            for (String[] addedDriverPosition: addedDriverPositions) {
                System.out.println("\nDriver " + addedDriverPosition[0] + " added successfully to the no." + addedDriverPosition[1] + " position.");
            }
        } catch (ParseException e) {
            System.out.println("Wrong date format!");
            addNewRace();
        } catch (InputMismatchException e) {
            System.out.println("Not a valid index number!");
            addNewRace();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            addNewRace();
        } catch (Exception e) {
            System.out.println("An unexpected error occurred!");
        }
    }

//    method to get the data of the points table and print.
    public static void showPointsTable() {
        try {
//            printing the table titles with some formatting.
            System.out.printf("\n%-9s | %-20s | %-20s | %-13s | %-16s | %-16s | %-16s | %-12s\n",
                    "Index No.",
                    "Driver Name",
                    "Constructor Team",
                    "No. of Points",
                    "No. of 1st Paces",
                    "No. of 2nd Paces",
                    "No. of 3rd Paces",
                    "No. of Races");

//            getting the table contents by calling the method in interface,
            String[][] tableContent = f1cManager.getPointsTable(SortOp.POINTS_DSC);

//            printing the table rows with some formatting
            for (String[] tableRecord: tableContent) {
                System.out.printf("%-9s | %-20s | %-20s | %-13s | %-16s | %-16s | %-16s | %-12s\n",
                        tableRecord[0], tableRecord[1], tableRecord[2], tableRecord[3],
                        tableRecord[4], tableRecord[5], tableRecord[6], tableRecord[7]);
            }

//            calling a new swing PointsTableFrame with the table content.
            new PointsTableFrame(tableContent, f1cManager);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An unexpected error occurred!");
        }
    }

//    method to get and print driver stats of a specific driver.
    public static void showDriverStats() {
        Scanner userInput = new Scanner(System.in);
        int driverIndex;

        try {
//            getting the index of driver from the user.
            System.out.println("\nEnter the index number of the driver: ");
            driverIndex = userInput.nextInt()-1;
            userInput.nextLine();

//            calling the method in interface to print the driver stats.
            f1cManager.showDriverStats(driverIndex);
        } catch (InputMismatchException e) {
            System.out.println("Not a valid index number!");
            showDriverStats();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            showDriverStats();
        } catch (Exception e) {
            System.out.println("An unexpected error occurred!");
        }
    }

//    method to change the driver of a constructor team.
    public static void changeConstructor() {
        Scanner userInput = new Scanner(System.in);
        String newDriverName;
        String newDriverLocation;
        int driverIndex = 0;

        try {
//            getting the index of the driver
            System.out.println("\nEnter the index number of the driver: ");
            driverIndex = userInput.nextInt()-1;
            userInput.nextLine();

//            getting the details of the new driver
            System.out.println("\nEnter the name of the new driver: ");
            newDriverName = userInput.nextLine();

            System.out.println("Enter the location of the new driver: ");
            newDriverLocation = userInput.nextLine();

//            calling the method in interface to change driver for a constructor.
            f1cManager.changeDriverOfConstructor(driverIndex, newDriverName, newDriverLocation);
        } catch (InputMismatchException e) {
            System.out.println("Not a valid index number!");
            changeConstructor();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            changeConstructor();
        } catch (Exception e) {
            System.out.println("An unexpected error occurred!");
        }
    }

//    method  to delete a specific driver.
    public static void deleteDriver() {
        Scanner userInput = new Scanner(System.in);
        int driverIndex;

        try {
//            getting the index of the driver
            System.out.println("\nEnter the index number of the driver: ");
            driverIndex = userInput.nextInt()-1;
            userInput.nextLine();

//            calling the method in interface to delete the driver.
            f1cManager.deleteDriver(driverIndex);
        } catch (InputMismatchException e) {
            System.out.println("Not a valid index number!");
            deleteDriver();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            deleteDriver();
        } catch (Exception e) {
            System.out.println("An unexpected error occurred!");
        }
    }

//    method to create a new driver.
    public static void createDriver() {
        Scanner userInput = new Scanner(System.in);
        String driverName;
        String driverLocation;
        String constructorName;

        try {
//            getting the driver details from the user
            System.out.println("\nEnter the name of the driver: ");
            driverName = userInput.nextLine();

            System.out.println("Enter the location of the driver: ");
            driverLocation = userInput.nextLine();

            System.out.println("Enter the constructor team: ");
            constructorName = userInput.nextLine();

//            calling the method in interface to create a new driver.
            f1cManager.createDriver(driverName, driverLocation, constructorName);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            createDriver();
        } catch (Exception e) {
            System.out.println("An unexpected error occurred!");
        }
    }
}

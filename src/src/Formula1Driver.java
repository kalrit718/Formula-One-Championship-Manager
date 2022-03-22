package src;

import java.io.Serializable;

public class Formula1Driver extends Driver implements Serializable {
    private int numOfFirstPositions;
    private int numOfSecondPositions;
    private int numOfThirdPositions;
    private int currentNumberOfPoints;
    private int numberOfRaces;

//    constructor to create a Formula1Driver with the name, location and team.
    public Formula1Driver(String driverName, String driverLocation, String constructorTeam) {
        super(driverName, driverLocation, constructorTeam);
    }

//    few simple getters.

    public int getNumOfFirstPositions() {
        return numOfFirstPositions;
    }

    public int getNumOfSecondPositions() {
        return numOfSecondPositions;
    }

    public int getNumOfThirdPositions() {
        return numOfThirdPositions;
    }

    public int getNumberOfRaces() {
        return numberOfRaces;
    }

    public int getCurrentPoints() {
        return currentNumberOfPoints;
    }

//    method to increase points according to the position.
    public void addPoints(int racePosition) {
        switch (racePosition) {
            case 1 -> currentNumberOfPoints += 25;
            case 2 -> currentNumberOfPoints += 18;
            case 3 -> currentNumberOfPoints += 15;
            case 4 -> currentNumberOfPoints += 12;
            case 5 -> currentNumberOfPoints += 10;
            case 6 -> currentNumberOfPoints += 8;
            case 7 -> currentNumberOfPoints += 6;
            case 8 -> currentNumberOfPoints += 4;
            case 9 -> currentNumberOfPoints += 2;
            case 10 -> currentNumberOfPoints += 1;
        }
    }

//    methods to increase first, second, third positions by 1 accordingly.
    public void addFirstPosition() { numOfFirstPositions++; }

    public void addSecondPosition() {
        numOfSecondPositions++;
    }

    public void addThirdPosition() { numOfThirdPositions++; }

//    method to increase num of races by 1.
    public void addRace() {
        numberOfRaces += 1;
    }

    @Override
    public String toString() {
        return ("\nName of Driver: " + super.getDriverName() +
                "\nLocation of Driver: " + super.getDriverLocation() +
                "\nConstructor of Driver: " + super.getConstructorTeam() +
                "\nNumber Of Races: " + numberOfRaces +
                "\nCurrent Number Of Points: " + currentNumberOfPoints +
                "\nNumber Of First Positions: " + numOfFirstPositions +
                "\nNumber Of Second Positions: " + numOfSecondPositions +
                "\nNumber Of Third Positions: " + numOfThirdPositions);
    }
}

package Comparators;

import src.*;

import java.util.Comparator;

public class FirstPositionsComparator implements Comparator<Formula1Driver> {
    @Override
    public int compare(Formula1Driver fDriver1, Formula1Driver fDriver2) {
        return (fDriver1.getNumOfFirstPositions() > fDriver2.getNumOfFirstPositions()) ? 1 : (fDriver1.getNumOfFirstPositions() < fDriver2.getNumOfFirstPositions()) ? -1 : 0;
    }
}

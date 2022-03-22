package Comparators;

import src.*;

import java.util.Comparator;

public class PointsComparator implements Comparator<Formula1Driver> {
    @Override
    public int compare(Formula1Driver fDriver1, Formula1Driver fDriver2) {
        return (fDriver1.getCurrentPoints() > fDriver2.getCurrentPoints()) ? 1 : (fDriver1.getCurrentPoints() < fDriver2.getCurrentPoints()) ? -1 : 0;
    }
}

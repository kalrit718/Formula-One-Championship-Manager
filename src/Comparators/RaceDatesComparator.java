package Comparators;

import src.Race;

import java.util.Comparator;

public class RaceDatesComparator implements Comparator<Race> {

    @Override
    public int compare(Race race1, Race race2) {
        return race1.getRaceDate().compareTo(race2.getRaceDate());
    }
}

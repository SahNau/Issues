package domain;

import java.util.Comparator;

public class SortByOldest implements Comparator<Issue> {

    @Override
    public int compare (Issue first, Issue second){
        return first.getId() - second.getId();
    }
}
package domain;

import java.util.Comparator;

public class SortByNewest implements Comparator<Issue> {

    @Override
    public int compare(Issue first,Issue second){
        return second.getId() - first.getId();
    }
}
package domain;

import java.util.HashSet;
import java.util.Set;

public class Issue {
    private final int id;
    private final String name;
    private boolean open;
    private final String author;
    private final String assignee;
    private final Set<Label> label = new HashSet<>();

    public Issue(int id, String name, boolean open, String author, String assignee, Label label) {
        this.id = id;
        this.name = name;
        this.open = open;
        this.author = author;
        this.assignee = assignee;
        this.label.add(label);
    }


    public int getId() {
        return id;
    }


    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public String getAuthor() {
        return author;
    }


    public String getAssignee() {
        return assignee;
    }


    public Set<Label> getLabel() {
        return label;
    }
}

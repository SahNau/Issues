package manager;

import domain.Issue;
import domain.Label;
import repository.IssueRepository;

import java.util.*;
import java.util.function.Predicate;

public class IssueManager {
    private final IssueRepository repository;

    public IssueManager(IssueRepository repository) {
        this.repository = repository;
    }

    public void add(Issue issue) {
        repository.save(issue);
    }

    public void addAll(Collection<Issue> issues) {
        repository.saveAll(issues);
    }

    public Collection<Issue> getAll() {
        return repository.findAll();

    }

    public void openIssue(int id) {
        repository.openIssue(id);
    }

    public void closeIssue(int id) {
        repository.closeIssue(id);
    }

    public Collection<Issue> openedIssues() {
        Collection<Issue> opened = new ArrayList<>();
        for (Issue issue : repository.findAll()) {
            if (issue.isOpen()) {
                opened.add(issue);
            }
        }
        if (opened.isEmpty()) return new ArrayList<>();
        return opened;
    }

    public Collection<Issue> closedIssues() {
        Collection<Issue> closed = new ArrayList<>();
        for (Issue issue : repository.findAll()) {
            if (!issue.isOpen()) {
                closed.add(issue);
            }
        }
        if (closed.isEmpty()) return new ArrayList<>();
        return closed;
    }

    public Collection<Issue> sortByIdNewest(Comparator<Issue> newest) {
        ArrayList<Issue> result = new ArrayList<>(repository.findAll());
        result.sort(newest);
        return result;
    }

    public Collection<Issue> sortByIdOldest(Comparator<Issue> oldest) {
        ArrayList<Issue> result = new ArrayList<>(repository.findAll());
        result.sort(oldest);
        return result;
    }

    public Collection<Issue> filterByAuthor(Predicate<String> predicate) {
        Collection<Issue> result = new ArrayList<>();
        for (Issue issue : repository.findAll()) {
            if (predicate.test(issue.getAuthor())) {
                result.add(issue);
            }
        }
        return result;
    }

    public Collection<Issue> filteredByLabel(Label label) {
        Collection<Issue> result = new ArrayList<>();
        for (Issue issue : repository.findAll()) {
            if (issue.getLabel().contains(label)) {
                result.add(issue);
            }
        }
        return result;
    }

    public Collection<Issue> filteredByAssignee(Predicate<String> predicate) {
        Collection<Issue> result = new ArrayList<>();
        for (Issue issue : repository.findAll()) {
            if (predicate.test(issue.getAssignee())) {
                result.add(issue);
            }
        }
        return result;
    }
}

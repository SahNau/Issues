package manager;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import repository.IssueRepository;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class IssueManagerTest {
    IssueRepository repository = new IssueRepository();
    IssueManager manager = new IssueManager(repository);
    SortByNewest comparatorNewest = new SortByNewest();
    SortByOldest comparatorOldest = new SortByOldest();

    private final Issue firstIssue = new Issue(1, "First", false, "author1", "assignee1", Label.label1);
    private final Issue secondIssue = new Issue(2, "Second", true, "author2", "assignee2", Label.label2);
    private final Issue thirdIssue = new Issue(3, "Third", false, "author3", "assignee3", Label.label3);
    private final Issue fourthIssue = new Issue(4, "Fourth", false, "author1", "assignee2", Label.label4);
    private final Issue fifthIssue = new Issue(5, "Fifth", true, "author3", "assignee3", Label.label5);

    @BeforeEach
    void setUp() {
        manager.addAll(List.of(firstIssue, secondIssue, thirdIssue, fourthIssue, fifthIssue));
    }

    @Test
    @DisplayName("Показывает все issues")
    public void shouldShowAllIssues() {
        Collection<Issue> expected = manager.getAll();
        Collection<Issue> actual = List.of(firstIssue, secondIssue, thirdIssue, fourthIssue, fifthIssue);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Добавляет новый issue")
    public void shouldAddNewIssue() {
        Issue sixthIssue = new Issue(6, "Sixth", true, "author1", "assignee2", Label.label5);

        manager.add(sixthIssue);
        Collection<Issue> expected = manager.getAll();
        Collection<Issue> actual = List.of(firstIssue, secondIssue, thirdIssue, fourthIssue, fifthIssue, sixthIssue);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Фильтрует issues по author")
    public void shouldSearchByAuthor() {
        String author = "author3";
        Predicate<String> equalAuthor = t -> t.equalsIgnoreCase(author);
        Collection<Issue> expected = List.of(thirdIssue, fifthIssue);
        Collection<Issue> actual = manager.filterByAuthor(equalAuthor);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Фильтрует issues по assignee")
    public void shouldSearchByAssignee() {
        String assignee = "assignee2";
        Predicate<String> equalAssignee = t -> t.equalsIgnoreCase(assignee);
        Collection<Issue> expected = List.of(secondIssue, fourthIssue);
        Collection<Issue> actual = manager.filteredByAssignee(equalAssignee);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Фильтрует issues по label")
    public void shouldSearchByLabel() {
        Collection<Issue> expected = List.of(firstIssue);
        Collection<Issue> actual = manager.filteredByLabel(Label.label1);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Показывает список всех открытых issues")
    public void shouldShowAllOpenedIssues() {
        Collection<Issue> expected = List.of(secondIssue, fifthIssue);
        Collection<Issue> actual = manager.openedIssues();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Показывает список всех закрытых issues")
    public void shouldShowAllClosedIssues() {
        Collection<Issue> expected = List.of(firstIssue, thirdIssue, fourthIssue);
        Collection<Issue> actual = manager.closedIssues();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Должен открыть ранее закрытую issue")
    public void shouldOpenIssues() {
        manager.openIssue(3);
        Collection<Issue> expected = List.of( secondIssue, thirdIssue, fifthIssue);
        Collection<Issue> actual = manager.openedIssues();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Должен закрыть ранее открытую issue")
    public void shouldCloseIssues() {
        manager.closeIssue(2);
        manager.closeIssue(5);
        Collection<Issue> expected = List.of(firstIssue, secondIssue, thirdIssue, fourthIssue, fifthIssue);
        Collection<Issue> actual = manager.closedIssues();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Сортирует по принципу,сначала новые")
    public void shouldSortByNewest() {
        Collection<Issue> expected = List.of(fifthIssue, fourthIssue, thirdIssue, secondIssue, firstIssue);
        Collection<Issue> actual = manager.sortByIdNewest(comparatorNewest);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Сортирует по принципу,сначала старые")
    public void shouldSortByOldest() {
        Collection<Issue> expected = List.of(firstIssue, secondIssue, thirdIssue, fourthIssue, fifthIssue);
        Collection<Issue> actual = manager.sortByIdOldest(comparatorOldest);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Пройдет exception по причине открытия несуществующего Issue")
    public void shouldShowNotFoundWithNoOpen() {
        assertThrows(NotFoundException.class, () -> manager.openIssue(1111), "Данное issue можно открыть");

    }

    @Test
    @DisplayName("Пройдет exception по причине закрытия несуществующего Issue")
    public void shouldShowNotFoundWithNoClose() {
        assertThrows(NotFoundException.class, () -> manager.closeIssue(1111), "Данное issue есть возможность закрыть");

    }

    @Test
    @DisplayName("Должен показать пустую коллекцию,если нет закрытых")
    public void shouldShowEmptyIfAllOpened() {
        manager.openIssue(1);
        manager.openIssue(3);
        manager.openIssue(4);
        Collection<Issue> expected = List.of();
        Collection<Issue> actual = manager.closedIssues();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Должен показать пустую коллекцию,если нет открытых")
    public void shouldShowEmptyIfAllClosed() {
        manager.closeIssue(2);
        manager.closeIssue(5);
        Collection<Issue> expected = List.of();
        Collection<Issue> actual = manager.openedIssues();
        assertEquals(expected, actual);
    }
}

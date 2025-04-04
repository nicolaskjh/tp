package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.DuplicatePhoneException;
import seedu.address.model.person.exceptions.PersonNotFoundException;

/**
 * A list of persons that enforces uniqueness between its elements and does not allow nulls. A person is considered
 * unique by comparing using {@code Person#isSamePerson(Person)} and {@code Person#{isSamePhone(Person}}. As such,
 * adding and updating of persons uses Person#isSamePerson(Person) and Person#isSamePhone(Person) for equality to ensure
 * that the person being added or updated is unique in terms of identity in the UniquePersonList. However, the removal
 * of a person uses Person#equals(Object) to ensure that the person with exactly the same fields will be removed.
 *
 * Supports a minimal set of list operations.
 *
 * @see Person#isSamePerson(Person)
 * @see Person#isSamePhone(Person)
 */
public class UniquePersonList implements Iterable<Person> {

    private final ObservableList<Person> internalList = FXCollections.observableArrayList();
    private final ObservableList<Person> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent person as the given argument.
     */
    public boolean contains(Person toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSamePerson);
    }

    /**
     * Returns true if the list contains an equivalent phone number as the given argument.
     */
    public boolean containsPhone(Person toCheck) {
        requireNonNull(toCheck.getPhone());
        return internalList.stream().anyMatch(toCheck::isSamePhone);
    }

    /**
     * Adds a person to the list.
     * The person must not already exist in the list.
     * The phone number must not already exist in the list.
     */
    public void add(Person toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicatePersonException();
        }
        if (containsPhone(toAdd)) {
            throw new DuplicatePhoneException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the list.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the list.
     */
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new PersonNotFoundException();
        }

        if (!target.isSamePerson(editedPerson) && contains(editedPerson)) {
            throw new DuplicatePersonException();
        }

        if (!target.isSamePhone(editedPerson) && containsPhone(editedPerson)) {
            throw new DuplicatePhoneException();
        }

        internalList.set(index, editedPerson);
    }

    /**
     * Removes the equivalent person from the list.
     * The person must exist in the list.
     */
    public void remove(Person toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new PersonNotFoundException();
        }
    }

    public void setPersons(UniquePersonList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Person> persons) {
        requireAllNonNull(persons);
        if (!personsAreUnique(persons)) {
            throw new DuplicatePersonException();
        }

        if (!phoneNumbersAreUnique(persons)) {
            throw new DuplicatePhoneException();
        }

        internalList.setAll(persons);
    }

    /**
     * Sorts the list by NextLesson date.
     */
    public void sortByNextLesson() {
        requireAllNonNull(internalList);
        Comparator<Person> byDate = Comparator.comparing(
            person -> {
                NextLesson nextLesson = person.getNextLesson();
                if (nextLesson.isEmpty()) {
                    return null;
                }
                return nextLesson.getDate();
            },
            Comparator.nullsLast(Comparator.naturalOrder())
        );

        Comparator<Person> byTime = Comparator.comparing(
            person -> {
                NextLesson nextLesson = person.getNextLesson();
                return nextLesson.getStartTime();
            },
            Comparator.nullsLast(Comparator.naturalOrder())
        );

        List<Person> sortedList = internalList.stream()
                .sorted(byDate.thenComparing(byTime))
                .collect(java.util.stream.Collectors.toList());

        internalList.setAll(sortedList);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Person> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<Person> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UniquePersonList)) {
            return false;
        }

        UniquePersonList otherUniquePersonList = (UniquePersonList) other;
        return internalList.equals(otherUniquePersonList.internalList);
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    @Override
    public String toString() {
        return internalList.toString();
    }

    /**
     * Returns true if {@code persons} contains only unique persons.
     */
    private boolean personsAreUnique(List<Person> persons) {
        for (int i = 0; i < persons.size() - 1; i++) {
            for (int j = i + 1; j < persons.size(); j++) {
                if (persons.get(i).isSamePerson(persons.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Returns true if {@code persons} contains only unique phone numbers.
     */
    private boolean phoneNumbersAreUnique(List<Person> persons) {
        for (int i = 0; i < persons.size() - 1; i++) {
            for (int j = i + 1; j < persons.size(); j++) {
                if (persons.get(i).isSamePhone(persons.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}

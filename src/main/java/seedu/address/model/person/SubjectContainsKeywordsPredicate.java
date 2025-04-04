package seedu.address.model.person;

import java.util.List;
import java.util.function.Predicate;

/**
 * Tests that a {@code Person}'s {@code Subject} matches any of the keywords given.
 */
public class SubjectContainsKeywordsPredicate implements Predicate<Person> {
    private final List<String> keywords;

    public SubjectContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Person person) {
        return keywords.stream()
                .anyMatch(keyword -> person.getSubjects().stream()
                        .anyMatch(subject -> subject.subjectName.equalsIgnoreCase(keyword)));
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof SubjectContainsKeywordsPredicate
                && keywords.equals(((SubjectContainsKeywordsPredicate) other).keywords));
    }
}

package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

public class NextLessonTest {

    @Test
    void constructor_validInput_parsesCorrectly() {
        String input = "15/4/2023 1430-1600";
        NextLesson lesson = new NextLesson(input);

        assertEquals(LocalDate.of(2023, 4, 15), lesson.getDate());
        assertEquals(LocalTime.of(14, 30), lesson.getStartTime());
        assertEquals(LocalTime.of(16, 0), lesson.getEndTime());
    }

    @Test
    void constructor_missingDash_throwsException() {
        String input = "15/4/2023 14301600"; // Missing '-'
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new NextLesson(input));
        assertTrue(exception.getMessage().contains("Invalid format"));
    }

    @Test
    void constructor_invalidDateFormat_throwsException() {
        String input = "15-04-2023 1430-1600"; // Incorrect date separator
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new NextLesson(input));
        assertTrue(exception.getMessage().contains("Invalid format"));
    }

    @Test
    void constructor_invalidTimeFormat_throwsException() {
        String input = "15/4/2023 14:30-16:00"; // Incorrect time format
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new NextLesson(input));
        assertTrue(exception.getMessage().contains("Invalid format"));
    }

    @Test
    void constructor_missingSpaceBeforeTime_throwsException() {
        String input = "15/4/20231430-1600"; // Missing space before time
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new NextLesson(input));
        assertTrue(exception.getMessage().contains("Invalid format"));
    }

    @Test
    public void equals() {
        NextLesson nextLesson = new NextLesson("14/4/2025 1400-1600");

        // same object -> returns true
        assertTrue(nextLesson.equals(nextLesson));

        // same values -> returns true
        NextLesson nextLessonCopy = new NextLesson(nextLesson.getValue());
        assertTrue(nextLesson.equals(nextLessonCopy));

        // different types -> returns false
        assertFalse(nextLesson.equals(1));

        // null -> returns false
        assertFalse(nextLesson.equals(null));

        // different nextLesson -> returns false
        NextLesson differentNextLesson = new NextLesson("15/4/2025 1900-2100");
        assertFalse(nextLesson.equals(differentNextLesson));
    }

    @Test
    public void hashCodeMethod() {
        NextLesson lesson1 = new NextLesson("14/4/2025 1400-1600");
        NextLesson lesson2 = new NextLesson("14/4/2025 1400-1600");
        NextLesson lesson3 = new NextLesson("15/4/2025 1900-2100");

        // Same value -> Same hash code
        assertEquals(lesson1.hashCode(), lesson2.hashCode());

        // Different values -> Different hash codes
        assertNotEquals(lesson1.hashCode(), lesson3.hashCode());
    }
}

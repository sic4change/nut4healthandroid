package org.sic4change.nut4health;

import org.junit.Test;
import org.sic4change.nut4health.utils.validators.NotEmptyValidator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NotEmptyValidatorTest {

    @Test
    public void notEmptyValidator_CorrectText_ReturnsTrue() {
        assertTrue(NotEmptyValidator.isValid("name"));
    }

    @Test
    public void notEmptyValidator_EmptyText_ReturnsFalse() {
        assertFalse(NotEmptyValidator.isValid(""));
    }

    @Test
    public void notEmptyValidator_NullText_ReturnsFalse() {
        assertFalse(NotEmptyValidator.isValid(null));
    }

    @Test
    public void notEmptyValidator_OnlyTextWithSpaces_ReturnsFalse() {
        assertFalse(NotEmptyValidator.isValid("                 "));
    }
}

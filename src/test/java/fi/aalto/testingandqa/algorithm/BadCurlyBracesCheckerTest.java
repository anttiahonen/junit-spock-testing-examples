package fi.aalto.testingandqa.algorithm;

import static fi.aalto.testingandqa.algorithm.CurlyBracesChecker.hasValidCurlyBraces;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BadCurlyBracesCheckerTest {

    @Test
    public void testCurlyBraces() {
        String code = "this.code = variable;";
        assertTrue(hasValidCurlyBraces(code));
        code = "public class JavaClass {}";
        assertTrue(hasValidCurlyBraces(code));
        code = "public class JavaClass {";
        assertFalse(hasValidCurlyBraces(code));
        code = "public class JavaClass }";
        assertFalse(hasValidCurlyBraces(code));
        code = "public class JavaClass {" +
                "   JavaClass() { }" +
                "}";
        assertTrue(hasValidCurlyBraces(code));

        try {
            hasValidCurlyBraces(null);
        } catch (AlgorithmException e) {
            assertEquals("Provided code was null!", e.getMessage());
        }
    }

}

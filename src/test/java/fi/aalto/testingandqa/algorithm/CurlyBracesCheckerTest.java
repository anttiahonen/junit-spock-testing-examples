package fi.aalto.testingandqa.algorithm;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static fi.aalto.testingandqa.algorithm.CurlyBracesChecker.hasValidCurlyBraces;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CurlyBracesCheckerTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private String nullCode = null;

    @Test
    public void testHasValidCurlyBraces_withCodeThatHasNoBraces_returnsTrue() {
        String codeWithoutAnyBraces = "this.code = variable;";

        assertTrue(hasValidCurlyBraces(codeWithoutAnyBraces));
    }

    @Test
    public void testHasValidCurlyBraces_withValidCode_thatHasOpeningAndClosingBraces_onTheSameline_returnsTrue() {
        String codeWithValidSingleBraces = "public class JavaClass {}";

        assertTrue(hasValidCurlyBraces(codeWithValidSingleBraces));
    }

    @Test
    public void testHasValidCurlyBraces_withInvalidCode_thatHasOneOpeningAndNoClosingBrace_returnsFalse() {
        String codeWithInvalidOnlyOpeningBrace = "public class JavaClass {";

        assertFalse(hasValidCurlyBraces(codeWithInvalidOnlyOpeningBrace));
    }

    @Test
    public void testHasValidCurlyBraces_withInvalidCode_thatHasOnlyOneClosingBrace_returnsFalse() {
        String codeWithInvalidOnlyClosingBrace = "public class JavaClass }";

        assertFalse(hasValidCurlyBraces(codeWithInvalidOnlyClosingBrace));
    }

    @Test
    public void testHasValidCurlyBraces_withValidCode_thatHasMultipleOpeningAndClosingBraces_onMultipleLines_returnsTrue() {
        String codeWithValidMultiLineMultiBraces =
                "public class JavaClass {" +
                "   JavaClass() { }" +
                "}";

        assertTrue(hasValidCurlyBraces(codeWithValidMultiLineMultiBraces));
    }

    @Test(expected = AlgorithmException.class)
    public void testHasValidCurlyBraces_withNullCode_throwsException_way1() {
        hasValidCurlyBraces(nullCode);
    }

    @Test
    public void testHasValidCurlyBraces_withNullCode_throwsException_way2() {
        thrown.expect(AlgorithmException.class);
        thrown.expectMessage("Provided code was null!");

        hasValidCurlyBraces(nullCode);
    }

}

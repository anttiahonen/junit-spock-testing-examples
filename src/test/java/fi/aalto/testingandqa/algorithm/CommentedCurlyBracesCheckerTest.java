package fi.aalto.testingandqa.algorithm;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static fi.aalto.testingandqa.algorithm.CurlyBracesChecker.hasValidCurlyBraces;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

//JUnit unit test class (TestCase) can be named how ever wanted, usually the pattern is ClassUnderTest | Test
// e.g class: CurlyBracesChecker | Test
public class CommentedCurlyBracesCheckerTest {

    //Rules are JUnit way of providing reusability for tests
    //Usually they are reserved for testing libraries etc..
    //This rule is used for doing assertions on the exceptions
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    //shared test context inited for test case
    private String nullCode = null;

    //annotating with @Test makes a method into a test method
    @Test
    //Naming:  test | method under test | context              | expected outcome
    public void testHasValidCurlyBraces_withCodeThatHasNoBraces_returnsTrue() {
        //use arrange - act - assert pattern (AAA), first the context creating: arrange
        String codeWithoutAnyBraces = "this.code = variable;";

        //then a line or two of empty vertical space between the context and
        //the action: act
        boolean isValidCode = hasValidCurlyBraces(codeWithoutAnyBraces);

        //after action, once again a line or two of vert. space and then assertions
        assertTrue(isValidCode);
    }

    @Test
    //Naming:   test| method under test | context     | more context                | m. context  | expected outcome
    public void testHasValidCurlyBraces_withValidCode_thatHasOpeningAndClosingBraces_onTheSameline_returnsTrue() {
        String codeWithValidSingleBraces = "public class JavaClass {}";

        //in this example the assertion and the action are combined together
        //this can be feasible with simple assertions & actions
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

    //simple way of testing an expected exception, when we are not interested about the exception message
    @Test(expected = AlgorithmException.class)
    public void testHasValidCurlyBraces_withNullCode_throwsException_way1() {
        hasValidCurlyBraces(nullCode);
    }

    //checking exceptions with JUnit ExpectedException-rule
    @Test
    public void testHasValidCurlyBraces_withNullCode_throwsException_way2() {
        //define the exception expections before calling the action
        thrown.expect(AlgorithmException.class);
        thrown.expectMessage("Provided code was null!");

        hasValidCurlyBraces(nullCode);
    }

}

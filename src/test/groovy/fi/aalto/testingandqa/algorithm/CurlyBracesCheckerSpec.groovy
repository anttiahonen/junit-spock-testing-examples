package fi.aalto.testingandqa.algorithm

import spock.lang.Unroll

import static fi.aalto.testingandqa.algorithm.CurlyBracesChecker.hasValidCurlyBraces;

import spock.lang.Specification

class CurlyBracesCheckerSpec extends Specification {

    static String codeWithoutAnyBraces = "this.code = variable;"
    static String codeWithValidSingleBraces = "public class JavaClass {}"
    static String codeWithInvalidOnlyOpeningBrace = "public class JavaClass {"
    static String codeWithInvalidOnlyClosingBrace = "public class JavaClass }"
    static String codeWithValidMultiLineMultiBraces = """\
            public class JavaClass {
                JavaClass() { }
            }
            """

    @Unroll
    def "hasValidCurlyBraces() with #contextDesc returns #outcome"() {
        expect: "#contextDesc has #validity curly braces"
            hasValidCurlyBraces(codeToCheck) == outcome

        where:
        codeToCheck                         | contextDesc                           || outcome  | _
        codeWithoutAnyBraces                | "code without any braces"             || true     | _
        codeWithValidSingleBraces           | "code with single braces"             || true     | _
        codeWithValidMultiLineMultiBraces   | "code with braces on multiple lines"  || true     | _
        codeWithInvalidOnlyOpeningBrace     | "code with only single opening brace" || false    | _
        codeWithInvalidOnlyClosingBrace     | "code with only single closing brace" || false    | _

        validity << ["valid", "valid", "valid", "invalid", "invalid"]
    }

    def "hasValidCurlyBraces() with null code throws exception"() {
        given: "code as null"
            String nullCode = null
        when: "testing valid curly braces with null code"
            hasValidCurlyBraces(nullCode)
        then: "exception is thrown"
            def ex = thrown RuntimeException
            ex.message == "Provided code was null!"
    }

}
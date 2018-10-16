package fi.aalto.testingandqa.algorithm

import spock.lang.Specification
import spock.lang.Unroll

import static fi.aalto.testingandqa.algorithm.CurlyBracesChecker.hasValidCurlyBraces

class CommentedCurlyBracesCheckerSpec extends Specification {

    //these variables need to be static for them to be usable inside the where-block
    //the same can be achieved with @Shared-annotation, which is used in spock to create single instantiated objects
    static String codeWithoutAnyBraces = "this.code = variable;"
    static String codeWithValidSingleBraces = "public class JavaClass {}"
    static String codeWithInvalidOnlyOpeningBrace = "public class JavaClass {"
    static String codeWithInvalidOnlyClosingBrace = "public class JavaClass }"
    static String codeWithValidMultiLineMultiBraces = """\
            public class JavaClass {
                JavaClass() { }
            }
            """

    //Unroll is used for individual runs of each of the row in data-driven where-block
    @Unroll
    //test output is injected to feature method with contextDesc and outcome params
    def "hasValidCurlyBraces() with #contextDesc returns #outcome"() {
        //having params injected to the labels can produce bdd-reports
        expect: "#contextDesc has #validity curly braces" //here we use expect-block to combine simple when & then-blocks
            hasValidCurlyBraces(codeToCheck) == outcome //codeToCheck and outcome are both params from the where-block

        where: //data driven params and tests with them
        //column is a parameter, first is the param name and then comes the values
        //here we have three params in the table format and one outside of the table              we can split long param tables with | _
        codeToCheck                         | contextDesc                           || outcome  | _
        //variable/method used outside from where-block needs to be static of @Shared
        codeWithoutAnyBraces                | "code without any braces"             || true     | _ //each row is a separate test
        codeWithValidSingleBraces           | "code with single braces"             || true     | _
        codeWithValidMultiLineMultiBraces   | "code with braces on multiple lines"  || true     | _
        codeWithInvalidOnlyOpeningBrace     | "code with only single opening brace" || false    | _
                                                                                    //expected outcomes from action should be separated with || instead of |
        codeWithInvalidOnlyClosingBrace     | "code with only single closing brace" || false    | _

        //here we have one param and its values for the 5 tests defined in the table
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
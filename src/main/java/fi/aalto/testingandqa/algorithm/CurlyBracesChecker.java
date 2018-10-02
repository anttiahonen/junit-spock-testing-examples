package fi.aalto.testingandqa.algorithm;

public class CurlyBracesChecker {

    public static boolean hasValidCurlyBraces(String code) {
        if (code == null)
            throw new RuntimeException("Provided code was null!");

        return code.replaceAll("\\{", "").length() == code.replaceAll("}", "").length();
    }

}

package io.github.xbeeant.eoffice.service.render.office;

import java.util.Arrays;
import java.util.List;

/**
 * @author xiaobiao
 * @version 2021/7/12
 */
public class DocumentType {
    private static final List<String> CELL = Arrays.asList("csv", "fods", "ods", "ots", "xls", "xlsm", "xlsx",
            "xlt", "xltm", "xltx");

    private static final List<String> SLIDE = Arrays.asList("fodp", "odp", "otp", "pot", "potm", "potx", "pps",
            "ppsm", "ppsx", "ppt", "pptm", "pptx");

    private DocumentType() {
        // do nothing
    }

    public static String getDocumentType(String extension) {
        if (CELL.contains(extension)) {
            return "cell";
        }

        if (SLIDE.contains(extension)) {
            return "slide";
        }

        return "word";
    }
}

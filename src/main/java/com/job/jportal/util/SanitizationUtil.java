package com.job.jportal.util;

public final class SanitizationUtil {

    private SanitizationUtil() { /* utility */ }

    /**
     * Sanitize input to plain text: remove tags, remove scripts/styles, escape special chars.
     * Returns null if input is null.
     */
    public static String sanitizePlain(String input) {
        if (input == null) return null;
        String cleaned = removeScriptAndStyle(input);
        // convert some line-break tags to newlines so text keeps basic separation
        cleaned = convertBreaksToNewlines(cleaned);
        // strip any remaining tags
        cleaned = stripTags(cleaned);
        // escape HTML special characters
        return escapeHtml(cleaned).trim();
    }

    /**
     * Sanitize input but preserve line breaks (recommended for descriptions).
     * It strips tags but converts <br> and paragraphs to newline before escaping.
     */
    public static String sanitizeWithLineBreaks(String input) {
        return sanitizePlain(input); // same behavior for now (keeps newlines)
    }

    /**
     * Sanitize input but keep very basic formatting by removing attributes.
     * NOTE: This is minimal — for real "allow some tags" behavior, use Jsoup/OWASP.
     */
    public static String sanitizeBasic(String input) {
        // for now behave like sanitizePlain. If later you want limited tags,
        // implement an allowlist replacement here.
        return sanitizePlain(input);
    }

    // ---- Internal helpers ----

    private static String removeScriptAndStyle(String s) {
        // remove <script ...>...</script> and <style ...>...</style> (case-insensitive, DOTALL)
        s = s.replaceAll("(?is)<script[^>]*>.*?</script>", "");
        s = s.replaceAll("(?is)<style[^>]*>.*?</style>", "");
        return s;
    }

    private static String convertBreaksToNewlines(String s) {
        // Replace <br>, <br/>, <br /> with newline
        s = s.replaceAll("(?i)<br\\s*/?>", "\n");
        // Replace closing paragraph tags with newline, remove opening p tags
        s = s.replaceAll("(?i)</p\\s*>", "\n");
        s = s.replaceAll("(?i)<p[^>]*>", "");
        return s;
    }

    private static String stripTags(String s) {
        // Remove any remaining tags like <...>
        return s.replaceAll("(?is)<[^>]*>", "");
    }

    private static String escapeHtml(String s) {
        if (s == null) return null;
        StringBuilder sb = new StringBuilder(s.length() * 2);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '&' -> sb.append("&amp;");
                case '<' -> sb.append("&lt;");
                case '>' -> sb.append("&gt;");
                case '"' -> sb.append("&quot;");
                case '\'' -> sb.append("&#x27;"); // HTML entity for apostrophe
                case '/' -> sb.append("&#x2F;");  // optional: escape slash
                default -> sb.append(c);
            }
        }
        return sb.toString();
    }
}

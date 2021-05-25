package com.satish.utils;

import com.satish.datamodels.filter.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Filter parsing utilities
 *
 * @author satish.thulva
 **/
@Slf4j
public class FilterParser {

    private static ThreadLocal<DateFormat> dateFormat = ThreadLocal.withInitial(
            () -> new SimpleDateFormat("yyyy-MM-dd"));

    private static final char NESTED_FILTER_START = '(';
    private static final char NESTED_FILTER_END = ')';
    private static final char SPACE = ' ';

    private static final String AND = "AND";
    private static final String OR = "OR";

    public static Filter parse(String filter) {
        if (StringUtils.isBlank(filter)) {
            return null;
        }

        filter = StringUtils.normalizeSpace(filter);
        List<String> tokens = tokenize(filter);

        Stack<Object> tokenStack = new Stack<>();

        for (String token : tokens) {
            while (!token.equals("" + NESTED_FILTER_END)) {
                tokenStack.push(token);
            }

            // Need to remove 4 tokens now
            // 0      1         2         3
            // (    field   condition   value
            Object value = tokenStack.pop();
            Object condition = tokenStack.pop();
            Object field = tokenStack.pop();
            tokenStack.pop(); // Removing the corresponding left parenthesis

            if (value instanceof Filter) { // If value is already a filter --> condition must be either AND or OR
                Filter rightFilter = (Filter) value;
                Filter leftFilter = (Filter) field;
                List<Filter> filterList = new ArrayList<>();
                filterList.add(leftFilter);
                filterList.add(rightFilter);

                Filter parentFilter = null;
                if (((String) condition).toUpperCase().equals(AND)) {
                    parentFilter = new AndFilter(filterList);
                } else if (((String) condition).toUpperCase().equals(OR)) {
                    parentFilter = new OrFilter(filterList);
                }

                tokenStack.push(parentFilter);
            } else {
                Filter leaf = new LeafFilter(FilterField.valueOf(((String) field).toUpperCase()),
                        FilterCondition.valueOf(((String) condition).toUpperCase()),
                        toValue((String) value));
                tokenStack.push(leaf);
            }
        }

        return (Filter) tokenStack.pop();
    }

    private static List<String> tokenize(String filter) {
        List<String> tokens = new ArrayList<>();

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < filter.length(); i += 1) {
            char ch = filter.charAt(i);
            if (!isSpecialChar(ch)) {
                stringBuilder.append(ch);
            } else {
                if (stringBuilder.length() > 0) {
                    tokens.add(stringBuilder.toString());
                    stringBuilder = new StringBuilder();
                }
                if (ch != SPACE) {
                    tokens.add(ch + "");
                }
            }
        }

        return tokens;
    }

    private static boolean isSpecialChar(char ch) {
        return ch == NESTED_FILTER_START || ch == NESTED_FILTER_END || ch == SPACE;
    }

    private static Object toValue(String value) {
        try {
            if (value.startsWith("'") && value.endsWith("'")) {
                return dateFormat.get().parse(value.substring(1, value.length() - 1));
            }

            return Double.parseDouble(value);
        } catch (NumberFormatException | ParseException pe) {
            log.error("Error parsing filter condition value {}. Ignoring filter", value, pe);
            return null;
        }
    }

}

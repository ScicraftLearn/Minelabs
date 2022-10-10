package be.uantwerpen.minelabs.util;

import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;
import java.util.regex.*;

public class ChemicalFormulaParser {
    public static Map<String, Integer> parseFormula(String formula) {
        // Remove all underscores
        formula = formula.replace("_", "");
        Matcher matcher = Pattern.compile("([A-Z][a-z]*)(\\d*)|(\\()|(\\))(\\d*)").matcher(formula);
        Stack<Map<String, Integer>> stack = new Stack<>();
        stack.push(new TreeMap<>());
        while (matcher.find()) {
            String group = matcher.group();
            if (group.equals("(")) {
                stack.push(new TreeMap<>());
            } else if (group.startsWith(")")) {
                Map<String, Integer> map = stack.pop();
                int multiplier = group.length() > 1 ? Integer.parseInt(group.substring(1)) : 1;
                for (Map.Entry<String, Integer> entry : map.entrySet()) {
                    stack.peek().merge(entry.getKey(), entry.getValue() * multiplier, Integer::sum);
                }
            } else {
                String[] split = group.split("\\d+");
                int count = group.length() > split[0].length() ? Integer.parseInt(group.replace(split[0], "")) : 1;
                stack.peek().merge(split[0], count, Integer::sum);
            }
        }
        return stack.pop();
    }
}

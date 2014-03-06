package org.jbehave.core.steps;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Have found the ByLevenshteinDistance to be too slow when there are many candidates, so need a faster solution.
 * <p/>
 * StepCollector uses first matching candidate, so should prioritise more specific steps first.
 * <p/>
 * Can simply assume longer patterns are more specific
 * Also, remove wildcards from candidate steps
 * <p/>
 * NOTE: Patterns with wildcards like this {price|cost} are mapped to separate candidates for all possibilities
 * <p/>
 * Here are some example patterns which overlap:
 * "WHEN the user clicks on edit cluster relationship link for participating account $participatingAccountName"
 * "WHEN the user clicks $buttonId"
 * <p/>
 */
public class OrderByPatternLength implements StepFinder.OrderingStrategy {
    private static final Pattern PARAM_PATTERN = Pattern.compile("\\$\\w+");

    private static String removePatterns(String candidate) {
        Matcher matcher = PARAM_PATTERN.matcher(candidate);
        return matcher.replaceAll("");
    }

    private static final Comparator<StepCandidate> mostSpecificCandidateFirst = new Comparator<StepCandidate>() {
        public int compare(StepCandidate o1, StepCandidate o2) {
            int result = Integer.valueOf(patternLength(o2)).compareTo(Integer.valueOf(patternLength(o1)));
            if(result == 0) {
                result = o2.getPriority().compareTo(o1.getPriority());
            }

            return result;
        }
    };

    public static int patternLength(StepCandidate o2) {
        String patternAsString = o2.getPatternAsString();
        if(patternAsString != null) {
            return removePatterns(patternAsString).length();
        } else {
            return 0;
        }
    }

    public List<StepCandidate> order(List<StepCandidate> candidates) {
        StepCandidate[] result = candidates.toArray(new StepCandidate[]{});
        Arrays.sort(result, mostSpecificCandidateFirst);
        return Arrays.asList(result);
    }
}

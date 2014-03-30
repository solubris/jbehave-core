package org.jbehave.core.steps;


import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class FilteringByPatternLength implements StepFinder.PrioritisingStrategy {

    public List<StepCandidate> prioritise(String stepAsText, List<StepCandidate> candidates) {
        if (candidates.size() == 0) {
            return candidates;
        }

        if (candidates.get(0).ignore(stepAsText)) {
            // dont filter if this step is ignorable
            return candidates;
        }

        List<StepCandidate> result = new ArrayList<StepCandidate>(candidates.size() / 2);
        int stepAsTextLength = trimTrailingLines(trimStartingWord(stepAsText)).length();
        for (StepCandidate candidate : candidates) {
            if (OrderByPatternLength.patternLength(candidate) <= stepAsTextLength) {
                result.add(candidate);
            }
        }

        return result;
    }

    private String trimStartingWord(String stepAsString) {
        return StringUtils.substringAfter(stepAsString, " ");
    }

    private String trimTrailingLines(String stepAsString) {
        return stepAsString.split("[\\n\\r]", 2)[0];
    }
}

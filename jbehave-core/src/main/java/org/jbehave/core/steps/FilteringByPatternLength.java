package org.jbehave.core.steps;


import org.apache.commons.lang.StringUtils;

import java.util.List;

import static com.google.common.collect.Lists.newArrayListWithExpectedSize;

public class FilteringByPatternLength implements StepFinder.PrioritisingStrategy {

    public List<StepCandidate> prioritise(String stepAsText, List<StepCandidate> candidates) {
        if (candidates.size() == 0) {
            return candidates;
        }

        if (candidates.get(0).ignore(stepAsText)) {
            // dont filter if this step is ignorable
            return candidates;
        }

        List<StepCandidate> result = newArrayListWithExpectedSize(candidates.size() / 2);
        int stepAsTextLength = trimTrailingLines(trimStartingWord(stepAsText)).length();
        for (StepCandidate candidate : candidates) {
            if (OrderByPatternLength.patternLength(candidate) <= stepAsTextLength) {
                result.add(candidate);
            }
        }

        return result;
    }

/*
    @Override
    public List<StepCandidate> prioritise(String stepAsText, List<StepCandidate> candidates) {
        int pos=0;
        int stepAsTextLength = trimStartingWord(stepAsText).length();
        for (StepCandidate candidate : candidates) {
            if (ByPatternLength.removePatterns(candidate.getPatternAsString()).length() <= stepAsTextLength) {
                break;
            }
            pos++;
        }

        return candidates.subList(pos, candidates.size());
    }
*/

    private String trimStartingWord(String stepAsString) {
        return StringUtils.substringAfter(stepAsString, " ");
    }

    private String trimTrailingLines(String stepAsString) {
        return stepAsString.split("[\\n\\r]")[0];
    }
}

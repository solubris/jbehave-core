package org.jbehave.core.steps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Cache candidate steps to improve performance of step collection
 *
 * Steps are typically defined in java classes which are static, so should be fine to cache them
 */
public class CachingStepFinder extends StepFinder {

    /**
     * Creates a StepFinder with a {@link ByPriorityField} strategy
     */
    public CachingStepFinder() {
        super();
    }

    /**
     * Creates a StepFinder with a custom strategy
     *
     * @param prioritisingStrategy
     *            the PrioritisingStrategy
     */
    public CachingStepFinder(PrioritisingStrategy prioritisingStrategy) {
        super(prioritisingStrategy);
    }

    /**
     * Creates a StepFinder with a custom strategy
     *
     * @param prioritisingStrategy
     *            the PrioritisingStrategy
     */
    public CachingStepFinder(PrioritisingStrategy prioritisingStrategy, OrderingStrategy orderingStrategy) {
        super(prioritisingStrategy, orderingStrategy);
    }

    private final Map<CandidateSteps, List<StepCandidate>> cacheOfCandidateSteps = new WeakHashMap<CandidateSteps, List<StepCandidate>>();

    @Override
    public List<StepCandidate> collectCandidates(List<CandidateSteps> candidateSteps) {
        long time = System.currentTimeMillis();
        List<StepCandidate> result = new ArrayList<StepCandidate>();
        for (CandidateSteps steps : candidateSteps) {
            if (cacheOfCandidateSteps.containsKey(steps)) {
                result.addAll(cacheOfCandidateSteps.get(steps));
            } else {
                List<StepCandidate> v = steps.listCandidates();
                cacheOfCandidateSteps.put(steps, v);
                result.addAll(v);
            }
        }

//        logger.info("collectCandidates finished in {}ms (steps {})", System.currentTimeMillis() - time, result.size());
//        time = System.currentTimeMillis();
        result = orderingStrategy.order(result);
//        logger.info("order Candidates finished in {}ms (steps {})", System.currentTimeMillis() - time, result.size());
        return result;
    }
}

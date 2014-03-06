package org.jbehave.examples.performance.steps;

import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Then;

import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;

public class TimingSteps {
    long startTime;

    @BeforeScenario
    public void beforeEachScenarioStartTimer() {
        startTime = System.currentTimeMillis();
    }

    @Then("this step is executed within $seconds second{s|}")
    public void timingStep(long seconds) {
        long duration = System.currentTimeMillis() - startTime;
        assertThat(duration, lessThan(seconds *1000L));
    }
}

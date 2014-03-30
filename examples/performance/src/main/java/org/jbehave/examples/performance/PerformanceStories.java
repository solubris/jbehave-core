package org.jbehave.examples.performance;

import org.jbehave.core.Embeddable;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;
import org.jbehave.examples.performance.steps.ManySteps;
import org.jbehave.examples.performance.steps.PerformanceSteps;
import org.jbehave.examples.performance.steps.TimingSteps;

import java.util.List;

import static org.jbehave.core.io.CodeLocations.codeLocationFromClass;
import static org.jbehave.core.reporters.Format.CONSOLE;
import static org.jbehave.core.reporters.Format.HTML;

public class PerformanceStories extends JUnitStories {

    public PerformanceStories() {
        Embedder embedder = configuredEmbedder();
        embedder.embedderControls().doGenerateViewAfterStories(true).doIgnoreFailureInStories(true)
                .doIgnoreFailureInView(true);
    }

    @Override
    public Configuration configuration() {
        Class<? extends Embeddable> embeddableClass = this.getClass();
        return new MostUsefulConfiguration().useStoryLoader(new LoadFromClasspath(embeddableClass))
                // old slow step collection:
                //.useStepCollector(new MarkUnmatchedStepsAsPending(new StepFinder(new StepFinder.ByLevenshteinDistance(), null)))
//                .useStepCollector(new MarkUnmatchedStepsAsPending(new CachingStepFinder()))
                .useStoryReporterBuilder(
                        new StoryReporterBuilder()
                                .withCodeLocation(CodeLocations.codeLocationFromClass(embeddableClass))
                                .withDefaultFormats().withFormats(CONSOLE, HTML));
    }

    @Override
    public InjectableStepsFactory stepsFactory() {
        return new InstanceStepsFactory(configuration(), new PerformanceSteps(), new TimingSteps(), new ManySteps());
    }

    @Override
    protected List<String> storyPaths() {
        if(System.getProperty("generate") != null) {
            return new StoryFinder().findPaths(codeLocationFromClass(this.getClass()), "**/generate.story", "");
        } else {
            return new StoryFinder().findPaths(codeLocationFromClass(this.getClass()), "**/*.story", "**/generate.story");
        }
    }
}

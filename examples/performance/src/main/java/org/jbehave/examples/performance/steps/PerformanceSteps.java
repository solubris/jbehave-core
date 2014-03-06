package org.jbehave.examples.performance.steps;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PerformanceSteps {
    private final static String loremIpsum = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam non diam ut urna pellentesque viverra eget vel mi. Sed facilisis tempus odio, aliquam dictum augue commodo eget. Cras quam arcu, tempus id mattis in, ultrices vitae enim. Nullam aliquam ipsum vel est euismod cursus. Donec a nisl non ipsum sagittis gravida non at dui. Morbi faucibus id tellus adipiscing iaculis. Nunc dignissim magna elementum vestibulum pulvinar. Ut suscipit sem at nibh varius molestie. Nulla facilisi. Proin molestie, arcu ac porttitor tempor, ipsum diam varius libero, a tristique mi ipsum non magna. Etiam nec ante ultrices, molestie nisi scelerisque, vestibulum quam. Duis rutrum, dui at venenatis gravida, tortor metus sollicitudin erat, id faucibus metus mauris ac tortor.";
    private final static String []loremIpsumArray = loremIpsum.split(" ");

    private List<String> generatedSteps = new ArrayList<String>();

    @Given("a step with a long tabular argument: $table")
    public void givenALongTable(ExamplesTable table){
        
    }
    
    @When("a scenario is generated to $path with a tabular argument of $tabularLines lines and an examples table of $examplesLines lines")
    public void aScenarioWithVeryLongTables(String path, int tabularLines, int examplesLines) {
        StringBuilder builder = new StringBuilder();        
        builder.append("Scenario: A scenario with long tables\n");
        builder.append("Given a step with a long tabular argument:\n")  
               .append(aTableWith(tabularLines));        
        builder.append("Examples:\n")       
               .append(aTableWith(examplesLines));
        try {
            FileWriter writer = new FileWriter(new File(path));
            writer.write(builder.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @When("a story called $path is generated having $scenarios scenarios with $step steps from the candidates previously generated")
    public void aStoryWithGeneratedSteps(String path, int scenarios, int steps) {
        StringBuilder builder = new StringBuilder();
        for(int scenarioCnt = 0; scenarioCnt < scenarios; scenarioCnt++) {
            builder.append("Scenario: scenario " + scenarioCnt + " with " + steps + " steps from ManySteps class\n");
            builder.append("Then this step is executed within 1 second\n");
            for (int i = 0; i < steps; i++) {
                int stepIndex = (int)Math.round(Math.random()*(generatedSteps.size()-1));
                builder.append("When ");
                builder.append(generatedSteps.get(stepIndex));
                builder.append(" - stepNum: " + stepIndex);
                builder.append("\n");
            }
            builder.append("\n");
        }

        try {
            FileWriter writer = new FileWriter(new File(path));
            writer.write(builder.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @When("a class called $className is generated having $numSteps steps, each having a maximum of $maxNumWords words")
    public void aClassWithManySteps(String className, int numSteps, int maxNumWords) {
        StringBuilder builder = new StringBuilder();
        builder.append("package org.jbehave.examples.performance.steps;\n");
        builder.append("\n");
        builder.append("import org.jbehave.core.annotations.Given;\n");
        builder.append("import org.jbehave.core.annotations.When;\n");
        builder.append("\n");
        builder.append("import static org.hamcrest.Matchers.is;\n");
        builder.append("import static org.junit.Assert.assertThat;\n");
        builder.append("\n");
        builder.append("public class " + className + " extends TimingSteps {\n");
        builder.append(aStoryClassWith("\t", numSteps, maxNumWords));
        builder.append("}\n");
        try {
            FileWriter writer = new FileWriter(new File(className + ".java"));
            writer.write(builder.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String aStoryClassWith(String prefix, int numberOfLines, int maxNumWords) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < numberOfLines; i++) {
            builder.append(prefix + "@When(\"");
            int numberOfWords = (int)Math.round(Math.random()*(maxNumWords-1))+1;
            String stepAsString = null;
            while(true) {
                stepAsString = aStepWithNumWords(numberOfWords);
                if(!generatedSteps.contains(stepAsString)) {
                    break;
                }
            }
            builder.append(stepAsString);
            generatedSteps.add(stepAsString);
            builder.append(" - stepNum: $stepNum\")\n");
            builder.append(prefix + "public void dummyStep" + i + "With" + numberOfWords + "Words(int stepNum) {\n");
            builder.append(prefix + prefix + "assertThat(stepNum, is(" + i + "));\n");
            builder.append(prefix + "}\n");
        }
        return builder.toString();
    }

    private String aStepWithNumWords(int numberOfWords) {
        StringBuilder builder = new StringBuilder();
        for (int j = 0; j < numberOfWords; j++) {
            int wordIndex = (int)Math.round(Math.random()*(loremIpsumArray.length-1));
            if(j > 0) {
                builder.append(" ");
            }
            builder.append(loremIpsumArray[wordIndex]);
        }

        return builder.toString();
    }

    private String aTableWith(int numberOfLines) {
        StringBuilder builder = new StringBuilder();        
        builder.append("|h0|h1|h2|h3|h4|h5|h6|h7|h8|h9|\n");
        for (int i = 0; i < numberOfLines; i++) {
            builder.append("|c"+i+"0|c"+i+"1|c"+i+"2|c"+i+"3|c"+i+"4|c"+i+"5|c"+i+"6|c"+i+"7|c"+i+"8|c"+i+"9|\n");
        }
        return builder.toString();
    }
}

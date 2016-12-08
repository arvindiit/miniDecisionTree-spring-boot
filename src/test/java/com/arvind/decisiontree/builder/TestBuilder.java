package com.arvind.decisiontree.builder;

import com.arvind.decisiontree.core.model.DecisionTree;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;

/**
 * Created by arvind on 08/12/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class TestBuilder {

    private DecisionTree decisionTree;

    @Before
    public void setup(){
        decisionTree = new DecisionTreeBuilder("file.xml", "Start").addBuilder().addAlternative("Increase temp").addNode("Increase")
                .addAlternative("Increase by 1 degree")
                .addAlternative("Increase by 5 degree")
                .getOriginal()
                .addBuilder().addAlternative("Decrease temp").addNode("Decrease")
                .addAlternative("Decrease by 1 degree")
                .addAlternative("Decrease by 1 degree")
                .getOriginal()
                .addAlternative("Unknown")
                .addNode("Unknown")
                .addDecisionTreeBuilder("PROCESSUNKNOWN.xml", "Unknown")
                .addAlternative("Map to hot")
                .addAlternative("Map to cold")
                .addDecisionTree()
                .getOriginal()
                .build();
    }

    @Test
    public void testBuilder() throws JAXBException, IOException {
        DecisionTreeMarshaller decisionTreeMarshaller = new DecisionTreeMarshaller(decisionTree);
        decisionTreeMarshaller.marshall();
        ClassLoader classLoader = getClass().getClassLoader();

        File originalFile = new File(classLoader.getResource("file.xml").getFile());
        File generatedFile = new File("file.xml");
        File originalFile2 = new File(classLoader.getResource("PROCESSUNKNOWN.xml").getFile());
        File generatedFile2 = new File("PROCESSUNKNOWN.xml");
        Assert.assertTrue(FileUtils.contentEquals(originalFile, generatedFile));
        Assert.assertTrue(FileUtils.contentEquals(originalFile2, generatedFile2));
        generatedFile.delete();
        generatedFile2.delete();
    }
}

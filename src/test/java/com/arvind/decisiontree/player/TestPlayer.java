package com.arvind.decisiontree.player;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.util.List;

/**
 * Created by arvind on 08/12/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class TestPlayer {

    @InjectMocks
    private PlayerService playerService;

    @Test
    public void testPlayerForUnknownAlternatives() throws JAXBException{
        List<String> list = getResult("Unknown");
        Assert.assertEquals(2, list.size());
        Assert.assertTrue(list.contains("Map to hot"));
        Assert.assertTrue(list.contains("Map to cold"));
    }

    @Test
    public void testPlayerForKNownAlternatives() throws JAXBException{
        List<String> list = getResult("Decrease");
        Assert.assertEquals(2, list.size());
        Assert.assertTrue(list.contains("Decrease by 1 degree"));
        Assert.assertTrue(list.contains("Decrease by 5 degree"));
    }

    private List<String> getResult(String consultation) throws JAXBException{
        ClassLoader classLoader = getClass().getClassLoader();

        playerService.setConsultation(consultation);
        playerService.setMainFile(new File(classLoader.getResource("file.xml").getFile()));
        playerService.setSupplementaryFile(new File(classLoader.getResource("PROCESSUNKNOWN.xml").getFile()));
        return playerService.consult();
    }

}

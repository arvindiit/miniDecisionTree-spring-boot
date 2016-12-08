package com.arvind.decisiontree.player;

import com.arvind.decisiontree.core.model.Alternative;
import com.arvind.decisiontree.core.model.DecisionTree;
import com.arvind.decisiontree.core.model.Node;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.util.List;

/**
 * Created by arvind on 08/12/16.
 * Main player class which can be used to
 * consult for suggestion by passing consultation
 */
@Service
public class PlayerService {

    private File mainFile;
    private File supplementaryFile;
    private String consultation;

    public File getMainFile() {
        return mainFile;
    }

    public void setMainFile(File mainFile) {
        this.mainFile = mainFile;
    }

    public File getSupplementaryFile() {
        return supplementaryFile;
    }

    public void setSupplementaryFile(File supplementaryFile) {
        this.supplementaryFile = supplementaryFile;
    }

    public String getConsultation() {
        return consultation;
    }

    public void setConsultation(String consultation) {
        this.consultation = consultation;
    }

    //start consultation
    public List<String> consult() throws JAXBException {
        DecisionTreeUnmarshaller decisionTreeUnMarshaller = new DecisionTreeUnmarshaller(mainFile, supplementaryFile);
        DecisionTree tree = decisionTreeUnMarshaller.unmarshall();
        return iterate(tree.getRootNode());
    }

    private List<String> iterate(Node node){
        //if name matches with consultation
        if(node.getName().equalsIgnoreCase(consultation)){
            return getAlternatives(node);
        }
        //else iterate for each child node
        else{
              for (Alternative alternative : node.getAlternativeList()){
                  if(alternative.getNode() != null) {
                      List<String> list = iterate(alternative.getNode());
                      if (list != null) {
                          return list;
                      }
                  }
              }
        }

        return null;
    }

    private List<String> getAlternatives(Node node){
        //if alternative is with node
        if(node.getAlternativeList() != null && !node.getAlternativeList().isEmpty()){
            return node.getAlterativeStrings();
        }
        //or alternatives are part of separate decision tree
        else{
            return node.getDecisionTree().getRootNode().getAlterativeStrings();
        }
    }


}

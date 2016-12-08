package com.arvind.decisiontree.player;

import com.arvind.decisiontree.core.model.Alternative;
import com.arvind.decisiontree.core.model.DecisionTree;
import com.arvind.decisiontree.core.model.Node;
import com.arvind.decisiontree.core.util.Utils;
import com.arvind.decisiontree.core.xml.XmlAlternative;
import com.arvind.decisiontree.core.xml.XmlNode;
import com.sun.istack.internal.Nullable;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by arvind on 08/12/16.
 * This is un-marshaller class to Unmarshall
 * xml file to decision tree object
 */
public class DecisionTreeUnmarshaller {

    private File mainFile;
    private File supplementFile;

    public DecisionTreeUnmarshaller(File mainFile, File supplementFile) {
        this.mainFile = mainFile;
        this.supplementFile = supplementFile;
    }

    public DecisionTree unmarshall() throws JAXBException {
        return unmarshall(null);
    }

    public DecisionTree unmarshall(@Nullable String nodeName) throws JAXBException {
        DecisionTree decisionTree = new DecisionTree(mainFile.getName());

        //creating un-marshaller
        JAXBContext jaxbContext = JAXBContext.newInstance(XmlNode.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        XmlNode xmlNode = (XmlNode)jaxbUnmarshaller.unmarshal(mainFile);

        if(nodeName != null){
            xmlNode = xmlNode.getChildNode(nodeName);
        }
        if(xmlNode == null){
            //TODO: custom exception should be thrown
            throw new JAXBException("Specified node is not found in file: "+mainFile.getName());
        }
        decisionTree.setRootNode(buildNode(xmlNode));
        return decisionTree;
    }

    private Node buildNode(XmlNode xmlNode) throws JAXBException {

        Node node = new Node(xmlNode.getName());
        //set decision tree
        node.setDecisionTree(getDecisionTreeWithTargetNode(xmlNode));

        //set alternatives
        List<Alternative> list = getAlternatives(xmlNode);
        if(list != null){
            node.getAlternativeList().addAll(list);
        }

        return node;
    }

    private DecisionTree getDecisionTreeWithTargetNode(XmlNode xmlNode) throws JAXBException {
        if(xmlNode.getElement() != null){
            DecisionTree decisionTree = new DecisionTreeUnmarshaller(supplementFile, null)
                    .unmarshall();
            decisionTree.setRootNode(Utils.getTargetNode(decisionTree.getRootNode(), xmlNode.getElement().getNodeName()));
            return decisionTree;
        }

        return null;
    }

    private List<Alternative> getAlternatives(XmlNode xmlNode) throws JAXBException {
        List<Alternative> alternatives = null;
        if(xmlNode.getAlternativeList() != null && !xmlNode.getAlternativeList().isEmpty()){
            alternatives = new ArrayList<>();
            for (XmlAlternative xmlAlternative : xmlNode.getAlternativeList()){
                alternatives.add(new Alternative(xmlAlternative.getName()));
            }
        }

        if(xmlNode.getMappingList() != null && !xmlNode.getMappingList().isEmpty()){
            alternatives = alternatives == null ? new ArrayList<>() : alternatives;
            for (XmlAlternative xmlAlternative : xmlNode.getMappingList()){
                if(xmlAlternative.getMapsTo() == null || xmlAlternative.getMapsTo().trim().equalsIgnoreCase("")){
                    throw new JAXBException("Property mapTo is not found for alternative: "+xmlAlternative.getName());
                }
                ///building child node for all child node: iteratively
                Alternative alternative =  new Alternative(xmlAlternative.getName());
                alternative.setNode(buildNode(xmlNode.getChildNode(xmlAlternative.getMapsTo())));
                alternatives.add(alternative);
            }
        }

        return alternatives;
    }
}

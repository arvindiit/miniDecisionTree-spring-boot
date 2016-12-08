package com.arvind.decisiontree.core.util;

import com.arvind.decisiontree.core.model.Alternative;
import com.arvind.decisiontree.core.model.Node;

/**
 * Created by arvind on 08/12/16.
 * Utility class for different purpose
 */
public class Utils {

    //find the child node by a given name
    public static Node getTargetNode(Node node, String name){
        if(node.getName().equalsIgnoreCase(name)){
            return node;
        }else {
            for (Alternative alternative : node.getAlternativeList()) {
                return getTargetNode(alternative.getNode(), name);
            }
        }

        return null;
    }
}

package utils.data;

import java.util.*;

public class XmlObjectDefination {
    private ArrayList<String> involvedNames;
    private HashMap<String, XmlObjectDefination> involvedObject;
    
    public XmlObjectDefination() {
        involvedNames = new ArrayList<String>();
        involvedObject = new HashMap<String, XmlObjectDefination>();
    }
    
    public void addNames(String ... names) {
        for (String n : names) {
            addName(n);
        }
    }
    
    public void addName(String n) {
        involvedNames.add(n);
    }
    
    public void addNameWithDefination(String n, XmlObjectDefination def) {
        addName(n);
        involvedObject.put(n, def);
    }
    
    public XmlObjectDefination getChildDefination(String n) {
        return involvedObject.get(n);
    }
    
    public ArrayList<String> getInvolvedNames() {
        return involvedNames;
    }
}

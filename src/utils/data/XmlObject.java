package utils.data;

import java.util.*;

public class XmlObject {
    private String name, value;
    private HashMap<String, ArrayList<XmlObject>> xmlObjects;
    
    public XmlObject(String n) {
        this(n, null);
    }
    
    public XmlObject(String n, String v) {
        setName(n);
        setValue(v);
        xmlObjects = new HashMap<String, ArrayList<XmlObject>>();
    }
    
    public void addXmlObject(XmlObject xObj) {
        if (xObj == null) {
            return;
        } else {
            if (xmlObjects.containsKey(xObj.getName())) {
                xmlObjects.get(xObj.getName()).add(xObj);
            } else {
                xmlObjects.put(xObj.getName(), wrapXmlObject(xObj));
            }
        }
    }
    
    public ArrayList<XmlObject> getChilds(String name) {
        if (xmlObjects.containsKey(name)) {
            return xmlObjects.get(name);
        } else {
            return null;
        }
    }
    
    public int countChilds(String name) {
        if (xmlObjects.containsKey(name)) {
            return xmlObjects.get(name).size();
        } else {
            return 0;
        }
    }
    
    public XmlObject getFirstChild(String name) {
        if (xmlObjects.containsKey(name)) {
            ArrayList<XmlObject> l = xmlObjects.get(name);
            if (l.size() > 0) {
                return l.get(0);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
    
    public XmlObject getLastChild(String name) {
        if (xmlObjects.containsKey(name)) {
            ArrayList<XmlObject> l = xmlObjects.get(name);
            if (l.size() > 0) {
                return l.get(l.size() - 1);
            } else {
                return null;
            }
        } else {
            return null;
        }
    } 
    
    private ArrayList<XmlObject> wrapXmlObject(XmlObject xObj) {
        ArrayList<XmlObject> l = new ArrayList<XmlObject>();
        if (xObj != null) {
            l.add(xObj);
        }
        return l;
    }
    
    public void setName(String n) {
        name = n;
    }
    
    public void setValue(String v) {
        value = v;
    }
    
    public String getName() {
        return name;
    }
    
    public String getValue() {
        return value;
    }
    
    public ArrayList<String> getChildNames() {
        ArrayList<String> keyList = new ArrayList<String>();
        keyList.addAll(xmlObjects.keySet());
        return keyList;
    }
    
    public boolean isEmpty() {
        return xmlObjects.isEmpty();
    }
    
    public boolean containsChild(String name) {
        return xmlObjects.containsKey(name);
    }
}

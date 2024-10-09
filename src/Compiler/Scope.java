package Compiler;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Scope {
    private LinkedHashMap<String, SymbolTableItem> hashtable = new LinkedHashMap<>();
    private String name;
    private int line;

    private ArrayList<Scope> childern = new ArrayList<>();

    private Type type;

    private Scope parent;

    public Scope getParent() {
        return parent;
    }

    public void setParent(Scope parent) {
        this.parent = parent;
    }

    public Scope(String name, int line, Type type) {
        this.name = name;
        this.line = line;
        this.type = type;
    }

    public void insert(String idefName, SymbolTableItem attributes) {
        hashtable.put(idefName, attributes);
    }

    public SymbolTableItem lookup(String idefName) {
        return hashtable.get(idefName);
    }

    public boolean Contain(String idefName) {
        Scope scope = this;
        while (scope != null) {
            if (scope.type != Type.PROGRAM) {
                for (Scope scope1 : scope.getChildern()) {
                    if (scope1.lookup(idefName) != null) {
                        return true;
                    }
                }
            }
            if (scope.lookup(idefName) != null) {
                return true;
            }
            scope = scope.parent;
        }
        return false;
    }

    public String getFieldType(String idefName) {
        Scope scope = this;
        while (scope != null) {
            if (scope.type != Type.PROGRAM) {
                for (Scope scope1 : scope.getChildern()) {
                    if (scope1.lookup(idefName) != null) {
                        return scope1.lookup(idefName).type;
                    }
                }
            }
            if (scope.lookup(idefName) != null) {
                return scope.lookup(idefName).type;
            }
            scope = scope.parent;
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public Type getFieldType() {
        return type;
    }


    public int getLine() {
        return line;
    }

    public ArrayList<Scope> getChildern() {
        return childern;
    }

    @Override
    public String toString() {
        return "------------- " + name + " : " + line + " -------------\n" + printItems() + "-----------------------------------------\n";
    }

    public String printItems() {
        String itemsStr = "";
        for (Map.Entry<String, SymbolTableItem> entry : hashtable.entrySet()) {
            itemsStr += "Key = " + entry.getKey() + " | Value = " + entry.getValue() + "\n";
        }
        return itemsStr;
    }

    public LinkedHashMap<String, SymbolTableItem> getHashtable() {
        return hashtable;
    }
}
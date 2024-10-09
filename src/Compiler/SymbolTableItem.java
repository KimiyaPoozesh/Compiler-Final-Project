package Compiler;

public class SymbolTableItem {
    int line;
    String name;

    Scope parent;

    String type;

    public SymbolTableItem(int line, String name, Scope parent , String type) {
        this.line = line;
        this.name = name;
        this.parent = parent;
        this.type = type;
    }

    @Override
    public String toString() {
        return name;
    }
}

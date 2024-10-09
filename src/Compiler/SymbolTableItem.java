package Compiler;

public class SymbolTableItem {
    int line;
    String name;

    public SymbolTableItem(int line, String name) {
        this.line = line;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

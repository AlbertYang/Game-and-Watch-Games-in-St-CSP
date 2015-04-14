import java.util.*;

public class Edge {
    private Vector<String> varList;
    private Node src;
    private Node dst;
    private int symbols[];
    private int gameLength;
    
    public Edge(Vector<String> varList, Node src, Node dst, String symbols) {
        this.varList = varList;
        this.src = src;
        this.dst = dst;
        String[] symbolList = symbols.split(", ");
        this.symbols = new int[symbolList.length];
        for (int i = 0; i < this.symbols.length; i++) {
            this.symbols[i] = Integer.parseInt(symbolList[i]);
        }
    }
    
    //just for digiInvader, to modify
    public Edge(Vector<String> varList, Node src, Node dst, String symbols, int gL) {
        this.varList = varList;
        this.src = src;
        this.dst = dst;
        gameLength = gL;
        String[] symbolList = symbols.split(", ");
        this.symbols = new int[symbolList.length];
        for (int i = 0; i < this.symbols.length; i++) {
            this.symbols[i] = Integer.parseInt(symbolList[i]);
        }
    }
    
    public boolean accept(int aim) {
        return (symbols[0] == aim);
    }
    
    //just for digiInvader, to be modified
    public boolean accept(int aim, int last) {
        return ( (symbols[0] == aim)&&(symbols[gameLength]==last) );
    }

    public Node getSrc() {
        return src;
    }

    public Node getDst() {
        return dst;
    }

    public int getSymbol(int i) {
        return symbols[i];
    }

    public String toString() {
        StringBuffer str = new StringBuffer();
        for (int i = 0; i < symbols.length; i++) {
            str.append(" ");
            str.append(varList.get(i));
            str.append("=");
            str.append(symbols[i]);
        }
        return str.toString();
    }
}

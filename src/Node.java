import java.util.*;

public class Node {
    private Vector<String> varList;
    private Vector<String> signList;
    private Integer key;
    private Vector<Edge> edges;
    private int[] symbols;
    private int[] Misses;
    private int gameLength;
    private boolean miss;

    public Node(Vector<String> varList, Vector<String> signList, Integer key, int gL) {
        this.varList = varList;
        this.signList = signList;
        this.key = key;
        edges = new Vector<Edge>();
        symbols = null;
        gameLength = gL;
    }

    public void setSignature(String symbols) {
    	if(symbols.contains(":")){
    		String[] symbolList = symbols.split(": ");
    		String theSymbols = symbolList[1];
    		if (!theSymbols.equals("S")) {
                symbolList = theSymbols.split(", ");
                this.symbols = new int[symbolList.length];
                for (int i = 0; i < this.symbols.length; i++) {
                    this.symbols[i] = Integer.parseInt(symbolList[i]);
                }
            }
    	}else{
    		if (!symbols.equals("R")) {
                String[] symbolList = symbols.split(", ");
                this.symbols = new int[symbolList.length];
                for (int i = 0; i < this.symbols.length; i++) {
                    this.symbols[i] = Integer.parseInt(symbolList[i]);
                }
            }
    	}
        
    }

    public Integer getKey() {
        return key;
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }
    
    public Node step(int input) {
    	int aim = input;
        //Node node = null;
        Vector<Edge> dstList = new Vector<Edge>();
        //System.out.println(aim+" "+lastDigit);
        for (Edge edge : edges) {
            if (edge.accept(aim) && hasAll(edge.getDst())) {
            	dstList.add(edge);
            }
        }
        if(dstList.size()==0){
        	return this;
        }
        int rand = (int) Math.round(Math.random() * (dstList.size()-1));
        Node nextNode = dstList.get(rand).getDst();
        boolean tmiss = (dstList.get(rand).getSymbol(22)==1);
        int[] tMisses = new int[4];
        for(int i=0;i<tMisses.length;i++){
        	tMisses[i] = dstList.get(rand).getSymbol(17+i);
//        	System.out.println(tMisses[i]);
        }
//        System.out.println(tmiss);
        nextNode.setMiss(tmiss);
        nextNode.setMissLoc(tMisses);
        return nextNode;
    }
    
    //just for digiinvader, to be modified
    public Node step(int aim,int lastDigit) {
        //Node node = null;
        Vector<Node> dstList = new Vector<Node>();
        //System.out.println(aim+" "+lastDigit);
        for (Edge edge : edges) {
            if (edge.accept(aim,lastDigit)) {
            	//System.out.println("sssss");
                //node = edge.getDst();
            	dstList.add(edge.getDst());
            	//System.out.println("ssdddsss");

            }
        }
        int rand = (int) Math.round(Math.random() * (dstList.size()-1));
        //System.out.println(rand+" "+dstList.size());
        return dstList.get(rand);
    }

    public int getDisplay(int i) {
        return edges.get(0).getSymbol(i + 1);
    }

    public Node walk(int id) {
        return edges.get(id).getDst();
    }

    public void print() {
        System.out.printf("Node %d:", key);
        if (symbols == null) {
            System.out.printf(" Root");
        } else {
            for (int i = 0; i < symbols.length; i++) {
                System.out.printf(" %s=%d", signList.get(i), symbols[i]);
            }
        }
        System.out.printf("\n");
        System.out.printf("%5s", "#");
        for (String var : varList) {
            String str = var;
            if (str.length() > 5) {
                str = str.substring(0, 4);
            }
            System.out.printf(" %5s", str);
        }
        System.out.printf("\n");
        for (int i = 0; i < edges.size(); i++) {
            System.out.printf("%5d", edges.get(i).getDst().getKey());
            for (int j = 0; j < varList.size(); j++) {
                System.out.printf(" %5d", edges.get(i).getSymbol(j));
            }
            System.out.printf("\n");
        }
    }
    
    public int getScore(){
    	return edges.get(0).getSymbol(17);//index of SCORE
    }
    
    public boolean hasMonster(){
    	Edge tmp = edges.get(0);
    	for(int i=1;i<=gameLength;i++){
    		if(tmp.getSymbol(i)==10)
    			return true;
    	}
    	return false; 	
    }
    
    public boolean isMiss(int aim){
    	for(int i=1;i<=gameLength;i++){
    		if(aim==edges.get(0).getSymbol(i))
    			return false;
    	}
    	return true;
    }
    
    public void setMiss(boolean m){
    	this.miss = m;
    }
    
    public boolean getMiss(){
    	return miss;
    }
    
    public void setMissLoc(int[] m){
    	this.Misses = m;
    }
    public int getMissLoc(){
    	for(int i=0;i<Misses.length;i++){
    		if(Misses[i]==1)
    			return i;
    	}
    	return -1;
    }
    
    private boolean hasAll(Node node){
    	int n = 0;
    	for (int i=0;i<4;i++){
    		for (Edge edge : node.edges) {
                if(edge.getSymbol(0)==i){
                	n++;
                	break;
                }
            }
    	}
    	if(n<4)
    		return false;
    	else
    		return true;
    }
}

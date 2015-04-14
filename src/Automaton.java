import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Automaton {
    private Vector<String> varList;
    private Vector<String> signList;
    private Hashtable<Integer, Node> nodeTable;
    private Node current;
    private int gameLength;
    private int lastDigit;
    private Integer nodeNum = null;
    
    public Automaton(InputStream in, boolean oldVersion) {
        varList = new Vector<String>();
        signList = new Vector<String>();
        nodeTable = new Hashtable<Integer, Node>();
        current = null;
        lastDigit = 0;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            
            //number of nodes
            if(!oldVersion){
            	String line = reader.readLine();
            	String[] tmplist = line.split(" ");
            	nodeNum = Integer.parseInt(tmplist[tmplist.length-1]);
            }
            
            // var list
            String line = reader.readLine();
            Scanner scan = new Scanner(line);
            scan.next();
            while (scan.hasNext()) {
                varList.add(scan.next());
            }

            // sign list
            line = reader.readLine();
            scan = new Scanner(line);
            scan.next();
            while (scan.hasNext()) {
                signList.add(scan.next());
            }

            Pattern nodePattern = Pattern.compile("([0-9]+) \\[label=\"(.*)\"\\];");
            Pattern edgePattern = Pattern.compile("([0-9]+) -> ([0-9]+) \\[label=\"(.*)\"\\];");
            line = reader.readLine();
            while (line != null) {
                Matcher matcher = nodePattern.matcher(line);
                if (matcher.matches()) {
                    Node node = getNode(Integer.valueOf(matcher.group(1)));
                    node.setSignature(matcher.group(2));
                }
                matcher = edgePattern.matcher(line);
                if (matcher.matches()) {
                    Node src = getNode(Integer.valueOf(matcher.group(1)));
                    Node dst = getNode(Integer.valueOf(matcher.group(2)));
                    src.addEdge(new Edge(varList, src, dst, matcher.group(3)));
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException ioe) {
            System.err.println("Cannot read automaton from input stream.");
        }
    }
    
    //just for digiInvader, to modify
    public Automaton(InputStream in, int gl) {
    	gameLength = gl;
        varList = new Vector<String>();
        signList = new Vector<String>();
        nodeTable = new Hashtable<Integer, Node>();
        current = null;
        lastDigit = 0;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            // var list
            String line = reader.readLine();
            Scanner scan = new Scanner(line);
            scan.next();
            while (scan.hasNext()) {
                varList.add(scan.next());
            }

            // sign list
            line = reader.readLine();
            scan = new Scanner(line);
            scan.next();
            while (scan.hasNext()) {
                signList.add(scan.next());
            }

            Pattern nodePattern = Pattern.compile("([0-9]+) \\[label=\"(.*)\"\\];");
            Pattern edgePattern = Pattern.compile("([0-9]+) -> ([0-9]+) \\[label=\"(.*)\"\\];");
            line = reader.readLine();
            while (line != null) {
                Matcher matcher = nodePattern.matcher(line);
                if (matcher.matches()) {
                    Node node = getNode(Integer.valueOf(matcher.group(1)));
                    node.setSignature(matcher.group(2));
                }
                matcher = edgePattern.matcher(line);
                if (matcher.matches()) {
                    Node src = getNode(Integer.valueOf(matcher.group(1)));
                    Node dst = getNode(Integer.valueOf(matcher.group(2)));
                    src.addEdge(new Edge(varList, src, dst, matcher.group(3), gameLength));
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException ioe) {
            System.err.println("Cannot read automaton from input stream.");
        }
    }
    
    private Node getNode(Integer key) {
        Node node = nodeTable.get(key);
        if (node == null) {
            node = new Node(varList, signList, key, gameLength);
            nodeTable.put(key, node);
            if (current == null) {
                current = node;
            }
        }
        return node;
    }
    
    public void reStart(){
    	current = getNode(0);
    }

    //to modify it to be standard
    public boolean step(int aim, int game) {
    	switch(game){
    	case 0://digiinvader
    		if( (aim!=-2) && (current.isMiss(aim)) )		
    			return false;
//        	System.out.println("hahaha: "+aim+" "+lastDigit);
            current = current.step(aim,lastDigit);
            lastDigit = current.getDisplay(gameLength-1);
            //System.out.println("gl: "+gameLength+" current: "+current.getKey()+" last: "+lastDigit+" "+current.getDisplay(gameLength));
            return true;
    		//break;
    	case 1://manhole
    		current = current.step(aim);
    		if(current == null)
    			return false;
    		else
    			return true;
    	}
    	return false;
    }

    public int getDisplay(int i) {
        return current.getDisplay(i);
    }
    
    public int getScore(){
    	return current.getScore();
    }

    public void walk(int id) {
        current = current.walk(id);
    }

    public void printCurrent() {
        current.print();
    }
    
    public void setLast(int l){
    	lastDigit = l;
    }
    
    public boolean hasMonster(){
    	return current.hasMonster();
    }
    
    public boolean isMiss(){
    	return current.getMiss();
    }
    
    public int getMissLoc(){
    	return current.getMissLoc();
    }

}

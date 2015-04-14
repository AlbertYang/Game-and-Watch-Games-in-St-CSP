import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;

public class Manhole extends JFrame {
//    private JButton btnAim;
    private JButton btnStart;
    private JButton btnStop;
    private JTextField speed;
    
//    private ImageIcon imgDigits[];
//    private ImageIcon imgMonster;
//    private ImageIcon imgSeparator;
    private ImageIcon imgBlank;
    private ImageIcon imgBackgrounds[];
    private ImageIcon imgLocations[];
    private ImageIcon imgSupports[];
    private ImageIcon imgFalls[];
    
    private JLabel lblDisplay[];
    private JLabel bgDisplay[];
    private JLabel supportsDisplay[];
    private JLabel fallsDisplay[];
    
    private JLabel missLabel;
    private JLabel scoreLabel;
    private JLabel speedLabel;
    
    private Automaton automaton;
    
    private int gameLength;
    private int MISS;
    private double rate;
    private int SCORE;
    private int tmpScore;
    private boolean START;
//    private String title;
    private Timer timer = null;
    private int currentSupport;
    private int TickTime;
    private int[] falls;
    private int[] misses;
    private boolean[] supportJudge;
    private int currentHole;
    private int oneTick;
    private int iniTick;
    private int SPEED;

    public void init() {
    	iniTick = 1500;
    	TickTime = iniTick;//ms
    	oneTick = 300;
    	gameLength = 8;
        MISS = 0;
        rate = 0.5;
        SCORE = 0;
        SPEED = 1;
        START = false;
    	currentSupport = 0;
    	currentHole = -1;
    	tmpScore=0;
    	falls = new int[4];
    	for(int i=0;i<falls.length;i++){
    		falls[i]=0;
    	}
    	supportJudge = new boolean[4];
    	for(int i=0;i<supportJudge.length;i++){
    		supportJudge[i] = false;
    	}
    	supportJudge[0] = true;
        loadImages();
        initLayout();
        initKeyListener();
        loadGame("/res/automatons/manhole2-3.dot");
    }

    private void loadImages() {
    	imgBackgrounds = new ImageIcon[2];
    	imgBackgrounds[0] = new ImageIcon(getClass().getResource("/res/manhole_res/background/bg01.png"));
    	imgBackgrounds[0] = zoomImageIcon(imgBackgrounds[0],rate);
    	//System.out.println(imgBackgrounds[0].getIconWidth()+" "+imgBackgrounds[0].getIconHeight());
    	imgBackgrounds[1] = new ImageIcon(getClass().getResource("/res/manhole_res/background/bg02.png"));
    	imgBackgrounds[1] = zoomImageIcon(imgBackgrounds[1],rate);
    	
    	imgLocations = new ImageIcon[16];
    	for (int i = 0; i < imgLocations.length; i++) {
    		imgLocations[i] = new ImageIcon(getClass().getResource("/res/manhole_res/location/"+i+".png"));
    		imgLocations[i] = zoomImageIcon(imgLocations[i],rate);
    		//System.out.println(imgLocations[i].getIconWidth()+" "+imgLocations[i].getIconHeight());
    	}
    	
    	imgBlank = new ImageIcon(getClass().getResource("/res/manhole_res/location/blank.png"));
    	imgBlank = zoomImageIcon(imgBlank,rate);
    	
    	imgSupports = new ImageIcon[4];
    	for(int i=0;i<imgSupports.length;i++){
    		imgSupports[i] = new ImageIcon(getClass().getResource("/res/manhole_res/support/support"+i+".png"));
    		imgSupports[i] = zoomImageIcon(imgSupports[i],rate);
    	}
    	
    	imgFalls = new ImageIcon[12];
    	for(int i=0;i<4;i++){
    		for(int j=0;j<3;j++){
    			imgFalls[3*i+j] = new ImageIcon(getClass().getResource("res/manhole_res/fall/fall"+i+"-"+j+".png"));
    			imgFalls[3*i+j] = zoomImageIcon(imgFalls[3*i+j],rate);
    		}
    	}
    	
    }

    private void initLayout() {
    	bgDisplay = new JLabel[2];
    	bgDisplay[0] = new JLabel(imgBackgrounds[0]);
    	bgDisplay[1] = new JLabel(imgBackgrounds[1]);
    	
        lblDisplay = new JLabel[16];
        for (int i = 0; i < lblDisplay.length; i++) {
            lblDisplay[i] = new JLabel(imgBlank);
        }
        
        supportsDisplay = new JLabel[4];
        for (int i = 0; i < supportsDisplay.length; i++) {
        	supportsDisplay[i] = new JLabel(currentSupport == i ? imgSupports[i] : imgBlank);
        }
        
        fallsDisplay = new JLabel[4];
        for (int i = 0; i < supportsDisplay.length; i++) {
        	fallsDisplay[i] = new JLabel(imgBlank);
        }
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(0xFF, 0xFF, 0xFF));
        GridBagConstraints c = new GridBagConstraints();
        
        //set locations
        for(int i=0;i<lblDisplay.length;i++){
        	c.fill = GridBagConstraints.HORIZONTAL;
        	c.gridwidth = 1;
        	if(i<8){
        		c.gridx = i;
        		c.gridy = 1;
        		panel.add(lblDisplay[i], c);
        	}else{
        		c.gridx = lblDisplay.length-1-i;
                c.gridy = 3;
                panel.add(lblDisplay[i], c);
        	}
        }
          
        //set supports
        for(int i=0;i<supportsDisplay.length;i++){
        	c.fill = GridBagConstraints.HORIZONTAL;
        	c.gridwidth = 3;
        	switch(i){
        	case 0:
        		c.gridx = 1;
        		c.gridy = 2;
        		break;
        	case 1:
        		c.gridx = 5;
        		c.gridy = 2;
        		break;
        	case 2:
        		c.gridx = 5;
        		c.gridy = 4;
        		break;
        	case 3:
        		c.gridx = 0;
        		c.gridy = 4;
        		break;
        	default:
        			break;
        	}
        	panel.add(supportsDisplay[i],c);	
        }
        
        //set falls
        for(int i=0;i<fallsDisplay.length;i++){
        	c.gridwidth = 1;
        	switch(i){
        	case 0:
        		c.gridx = 2;
        		c.gridy = 2;
        		break;
        	case 1:
        		c.gridx = 5;
        		c.gridy = 2;
        		break;
        	case 2:
        		c.gridx = 5;
        		c.gridy = 4;
        		break;
        	case 3:
        		c.gridx = 2;
        		c.gridy = 4;
        		break;
        	default:
        			break;
        	}
        	panel.add(fallsDisplay[i],c);
        	
        }
        
        //set lands
        c.fill = GridBagConstraints.HORIZONTAL;
        //c.ipady = 40; // make this component tall
        //c.weightx = 0.0;
        c.gridwidth = 8;
        c.gridx = 0;
        //first land
        c.gridy = 2;
        panel.add(bgDisplay[0], c);
        //second land
        c.gridy = 4;
        panel.add(bgDisplay[1], c); 

        btnStart = new JButton("Start");
        btnStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if(!START){
            		START = true;
            		btnStop.setText("Stop");
            		btnStop.setEnabled(true);
            		TickTime = iniTick;
            		btnStart.setEnabled(false);
            		SCORE = 0;
            		MISS = 0;
            		SPEED = 1;
                	loadGame("/res/automatons/manhole2-3.dot");
                	timer = new Timer();
                	//sTickTime = Integer.parseInt(speed.getText());
                	timer.schedule(new TimerTask() {  
                        @Override  
                        public void run() { 
                        	int tmpStep = currentSupport;
                        	if(supportJudge[0] && currentHole==0)
                        		tmpStep = 0;
                        	if(supportJudge[1] && currentHole==1)
                        		tmpStep = 1;
                        	if(supportJudge[2] && currentHole==2)
                        		tmpStep = 2;
                        	if(supportJudge[3] && currentHole==3)
                        		tmpStep = 3;
                        	for(int i=0;i<supportJudge.length;i++){
                        		supportJudge[i] = false;
                        	}
                        	supportJudge[currentSupport] = true;
                        	currentHole = -1;
                        	if(automaton.step(tmpStep,1))
                        		if(automaton.isMiss()){
                        			MISS++;
                        			int missloc = automaton.getMissLoc();
//                        			System.out.println(missloc);
                        			for(int i=0;i<falls.length;i++){
                        				if(i==missloc)
                        					falls[i]=1;
                        				else if(falls[i]!=0){
                        					falls[i] = (falls[i]+1)%4;
                        				}else{
                        					falls[i] = 0;
                        				}
//                        				System.out.println(falls[i]);
                        			}
//                        			System.out.println("========");
                        			//new FallDisplay(automaton.getMissLoc(),TickTime).start();
                        			updateDisplay();
                        		}
                        		else {            
//                        			System.out.println(automaton.isMiss());
                        			for(int i=0;i<falls.length;i++){
                        				if(falls[i]!=0){
                        					falls[i] = (falls[i]+1)%4;
                        				}else{
                        					falls[i] = 0;
                        				}
                        			}
                        			updateDisplay();		
                        		}
                        	// 
                        }  
                    }, TickTime, TickTime);
            	}   	
            }
        });
        
        btnStop = new JButton("Stop");
        btnStop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if(START){
            		START = false;
            		btnStart.setEnabled(true);
            		btnStop.setText("Continue");
            		timer.cancel();
//                    for (int i = 0; i < lblDisplay.length; i++) {
//                        lblDisplay[i].setIcon(imgBlank);
//                    }
//                    currentSupport = 0;
//                    for (int i = 0; i < supportsDisplay.length; i++) {
//                    	supportsDisplay[i].setIcon(currentSupport == i ? imgSupports[i] : imgBlank);
//                    }
            	}else{
            		START = true;
            		btnStop.setText("Stop");
            		btnStart.setEnabled(false);
                	timer = new Timer();
                	timer.schedule(new TimerTask() {  
                        @Override  
                        public void run() {  
                        	int tmpStep = currentSupport;
                        	if(supportJudge[0] && currentHole==0)
                        		tmpStep = 0;
                        	if(supportJudge[1] && currentHole==1)
                        		tmpStep = 1;
                        	if(supportJudge[2] && currentHole==2)
                        		tmpStep = 2;
                        	if(supportJudge[3] && currentHole==3)
                        		tmpStep = 3;
                        	for(int i=0;i<supportJudge.length;i++){
                        		supportJudge[i] = false;
                        	}
                        	supportJudge[currentSupport] = true;
                        	currentHole = -1;
                        	if(automaton.step(tmpStep,1))
                        		if(automaton.isMiss()){
                        			MISS++;
                        			int missloc = automaton.getMissLoc();
                        			for(int i=0;i<falls.length;i++){
                        				if(i==missloc)
                        					falls[i]=1;
                        				else if(falls[i]!=0){
                        					falls[i] = (falls[i]+1)%4;
                        				}else{
                        					falls[i] = 0;
                        				}
//                        				System.out.println(falls[i]);
                        			}
                        			//new FallDisplay(automaton.getMissLoc(),TickTime).start();
                        			updateDisplay();
                        		}
                        		else { 
                        			for(int i=0;i<falls.length;i++){
                        				if(falls[i]!=0){
                        					falls[i] = (falls[i]+1)%4;
                        				}else{
                        					falls[i] = 0;
                        				}
                        			}
//                        			System.out.println(automaton.isMiss());
                        			updateDisplay();		
                        		}
                        	// 
                        }  
                    }, TickTime, TickTime);
            	}
            }
        });
        
//      set on the score and miss
        scoreLabel = new JLabel("SCORE: "+SCORE+" ");
        missLabel = new JLabel("MISS: "+MISS+" ");
        speedLabel = new JLabel("SPEED: "+SPEED+" ");
        JPanel panelScore = new JPanel();
        panelScore.setBackground(new Color(0xFF, 0xFF, 0xFF));
        panelScore.add(scoreLabel);
        panelScore.add(missLabel);
        panelScore.add(speedLabel);

      //set on the buttons
        //speedLabel = new JLabel("Speed(ms):");
        speed = new JTextField(""+TickTime);
        JPanel panelControl = new JPanel();
        panelControl.setBackground(new Color(0xFF, 0xFF, 0xFF));
        //panelControl.add(speedLabel);
        //panelControl.add(speed);
        panelControl.add(btnStart);
        btnStop.setEnabled(false);
        panelControl.add(btnStop);
//        panelControl.add(btnRestart);

        
        JPanel panelWhole = new JPanel(new BorderLayout());
        panelWhole.add(panelScore,BorderLayout.NORTH);
        panelWhole.add(panel,BorderLayout.CENTER);
        panelWhole.add(panelControl,BorderLayout.SOUTH);
        
        getContentPane().add(panelWhole);
    }

    private void initKeyListener(){
    	Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
    	    public void eventDispatched(AWTEvent event) {
    	        if (((KeyEvent) event).getID() == KeyEvent.KEY_PRESSED) {
    	        	//放入自己的键盘监听事件
    	        	//((KeyEvent) event).getKeyCode();// 获取按键的code
    	            //((KeyEvent) event).getKeyChar();// 获取按键的字符
    	        	switch(((KeyEvent) event).getKeyChar()){
	    	        	case 'q':
	    	        		currentSupport = 0;
	    	        		supportJudge[0] = supportJudge[0] || true;
	    	        		break;
	    	        	case 'p':
	    	        		currentSupport = 1;
	    	        		supportJudge[1] = supportJudge[1] || true;
	    	        		break;
	    	        	case 'l':
	    	        		currentSupport = 2;
	    	        		supportJudge[2] = supportJudge[2] || true;
	    	        		break;
	    	        	case 'a':
	    	        		currentSupport = 3;
	    	        		supportJudge[3] = supportJudge[3] || true;
	    	        		break;
    	        	}
    	        	for(int i=0;i<supportsDisplay.length;i++){
    	        		supportsDisplay[i].setIcon(currentSupport == i ? imgSupports[i] : imgBlank);
    	        	}
    	        }
    	    }
    	}, AWTEvent.KEY_EVENT_MASK);
    	//该代码片段来自于: http://www.sharejs.com/codes/java/6737
    }

    private void loadGame(String game) {
        automaton = new Automaton(getClass().getResourceAsStream(game),false);  
        updateDisplay();
        for (int i = 0; i < supportsDisplay.length; i++) {
        	fallsDisplay[i].setIcon(imgBlank);
        }
    }

    public void updateDisplay() {
    	missLabel.setText("MISS: "+MISS);
    	for (int i = 0; i < gameLength*2; i++) {
            int isLocated = automaton.getDisplay(i);
            if(isLocated == 1){
            	if(i==3||i==6||i==11||i==14)
            		SCORE++;
            	if(i==2)
            		currentHole=0;
            	if(i==5)
            		currentHole=1;
            	if(i==10)
            		currentHole=2;
            	if(i==13)
            		currentHole=3;
            }
            if(SCORE%5 == 0 && SCORE>0 && SCORE>tmpScore){
            	tmpScore=SCORE;
            	if(TickTime*0.75 < oneTick)
            		TickTime = oneTick;
            	else{
            		TickTime = (int)(TickTime*0.75);
            		SPEED++;
            		System.out.println(SPEED+" "+TickTime);
            	}
            	timer.cancel();
            	timer = new Timer();
            	timer.schedule(new TimerTask() {  
                    @Override  
                    public void run() {  
                    	int tmpStep = currentSupport;
                    	if(supportJudge[0] && currentHole==0)
                    		tmpStep = 0;
                    	if(supportJudge[1] && currentHole==1)
                    		tmpStep = 1;
                    	if(supportJudge[2] && currentHole==2)
                    		tmpStep = 2;
                    	if(supportJudge[3] && currentHole==3)
                    		tmpStep = 3;
                    	for(int i=0;i<supportJudge.length;i++){
                    		supportJudge[i] = false;
                    	}
                    	currentHole = -1;
                    	supportJudge[currentSupport] = true;
                    	if(automaton.step(tmpStep,1))
                    		if(automaton.isMiss()){
                    			MISS++;
                    			int missloc = automaton.getMissLoc();
                    			for(int i=0;i<falls.length;i++){
                    				if(i==missloc)
                    					falls[i]=1;
                    				else if(falls[i]!=0){
                    					falls[i] = (falls[i]+1)%4;
                    				}else{
                    					falls[i] = 0;
                    				}
//                    				System.out.println(falls[i]);
                    			}
                    			//new FallDisplay(automaton.getMissLoc(),TickTime).start();
                    			updateDisplay();
                    		}
                    		else { 
                    			for(int i=0;i<falls.length;i++){
                    				if(falls[i]!=0){
                    					falls[i] = (falls[i]+1)%4;
                    				}else{
                    					falls[i] = 0;
                    				}
                    			}
//                    			System.out.println(automaton.isMiss());
                    			updateDisplay();		
                    		}
                    	// 
                    }  
                }, TickTime, TickTime);
            }
            lblDisplay[i].setIcon(isLocated==0 ? imgBlank : imgLocations[i]);
        }
    	for(int i=0;i<4;i++){
    		int isFall = falls[i];
    		int rand = (int) Math.round(Math.random() * (1));
            if (i>1){
            	rand = rand+2;
            }
            int fall = rand*3;
    		fallsDisplay[i].setIcon(isFall==0 ? imgBlank : imgFalls[fall+isFall-1]);
    	}
    	scoreLabel.setText("SCORE: "+SCORE);
    	speedLabel.setText("SPEED: "+SPEED);

//        automaton.printCurrent();
    }
    
    private ImageIcon zoomImageIcon(ImageIcon img, double rate) {
    	if(rate<=0.0)
    		return img;
    	int width = (int)(img.getIconWidth()*rate);
    	int height = (int)(img.getIconHeight()*rate);
    	img.setImage(img.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
    	return img;
    }
    
    class FallDisplay extends Thread {
    	private int location;
    	private int ticktime;
        public FallDisplay(int loc, int tick) {
        	location = loc;
        	ticktime = tick;
            //super(threadName);
        }
     
        public void run() {
            try {
            	int rand = (int) Math.round(Math.random() * (1));
                if (location>1){
                	rand = rand+2;
                }
                int fall = rand*3;
                fallsDisplay[location].setIcon(imgFalls[fall]);
				Thread.sleep(ticktime);
				fallsDisplay[location].setIcon(imgFalls[fall+1]);
				Thread.sleep(ticktime);
				fallsDisplay[location].setIcon(imgFalls[fall+2]);
				Thread.sleep(ticktime);
				fallsDisplay[location].setIcon(imgBlank);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
        }
    }
    
    private TimerTask tm = new TimerTask() {  
        @Override  
        public void run() {  
        	if(automaton.step(currentSupport,1))
        		if(automaton.isMiss()){
        			MISS++;
        			int missloc = automaton.getMissLoc();
//        			System.out.println(missloc);
        			for(int i=0;i<falls.length;i++){
        				if(i==missloc)
        					falls[i]=1;
        				else if(falls[i]!=0){
        					falls[i] = (falls[i]+1)%4;
        				}else{
        					falls[i] = 0;
        				}
//        				System.out.println(falls[i]);
        			}
//        			System.out.println("========");
        			//new FallDisplay(automaton.getMissLoc(),TickTime).start();
        			updateDisplay();
        		}
        		else {            
//        			System.out.println(automaton.isMiss());
        			for(int i=0;i<falls.length;i++){
        				if(falls[i]!=0){
        					falls[i] = (falls[i]+1)%4;
        				}else{
        					falls[i] = 0;
        				}
        			}
        			updateDisplay();		
        		}
        	// 
        }  
    };
    
    public static void main(String[] args) {
    	Manhole digi = new Manhole();
    	digi.init();
    	digi.setLocation(300, 300);           
//    	digi.setLocationRelativeTo(null); //本语句实现窗口居屏幕中央
    	digi.setSize(285,280);            //设置窗体的大小为300*200像素大小
    	digi.setResizable(false);       //设置窗体是否可以调整大小，参数为布尔值
    	digi.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //设置窗体可见，没有该语句，窗体将不可见，此语句必须有，否则没有界面就没有如何意义了
    	digi.setVisible( true); 
        
//        try {
//            Automaton automaton = new Automaton(new FileInputStream(args[0]));
//            Scanner scan = new Scanner(System.in);
//            while (true) {
//                automaton.printCurrent();
//                System.out.print("Next? ");
//                int i = scan.nextInt();
//                automaton.walk(i);
//            }
//        } catch (IOException ioe) {
//            System.err.println("Cannot read automaton from file.");
//        }
    }
}

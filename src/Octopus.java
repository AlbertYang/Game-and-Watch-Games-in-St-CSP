import java.awt.*;
import java.awt.event.*;
import java.util.Random;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;

// This is a trick.
public class Octopus extends JFrame {
    private JButton btnStart;
    private JButton btnStop;
    private JTextField speed;
    
    private ImageIcon imgBlank;
    private ImageIcon imgBackgrounds[];
    private ImageIcon imgLocations[];
    private ImageIcon imgTouch0[];
    private ImageIcon imgTouch1[];
    private ImageIcon imgTouch2[];
    private ImageIcon imgTouch3[];
    private ImageIcon imgTouch4[];
    private ImageIcon imgLife[];
    private ImageIcon imgCatch[];
    private ImageIcon imgPut;
    
    
    private JLabel bgDisplay[];
    private JLabel locDisplay[];
    private JLabel touch0Display[];
    private JLabel touch1Display[];
    private JLabel touch2Display[];
    private JLabel touch3Display[];
    private JLabel touch4Display[];
    private JLabel lifeDisplay[];
    private JLabel catchDisplay[];

    
    
    private JLabel scoreLabel;
    private JLabel speedLabel;

//    private Automaton automaton;
    
    private int location;
    private boolean grub;
    private int touchChoice;
    private boolean upDown;//to choose 1st or 2nd touch
    private int touch0;//0..2
    private int touch1;//0..3
    private int touch2;//0..4
    private int touch3;//0..3
    private int touch4;//0..2
    private int life;
    private boolean MISS;
    private boolean getSomething;
    
    private static double rate;
    private int SCORE;
    private boolean START;
    
//    private int tmpScore;

    private Timer timer = null;
    private int TickTime;
    private int iniTick;
    private int SPEED;
    private double speedup;
    
    public void initValues(){
    	location = 0;
    	grub = false;
    	
    	touchChoice = 0;
    	upDown = true;//init is up
    	touch0 = -1;
    	touch1 = -1;
    	touch2 = -1;
    	touch3 = -1;
    	touch4 = -1;
    	life = 2;
    	MISS = false;
    	getSomething = false;
    	iniTick = 500;
    	TickTime = iniTick;
        SCORE = 0;
        SPEED = 1;
        START = false;
    }

    public void init() {
    	speedup = 0.8;
    	initValues();

        rate = 0.8;

//    	tmpScore=0;
        initKeyListener();
        loadImages();
        initLayout();
//        loadGame("/res/automatons/manhole2-3.dot");
    }

    private void loadImages() {
    	imgBlank = new ImageIcon(getClass().getResource("/res/octopus_res/blank.png"));
    	imgBlank = zoomImageIcon(imgBlank,rate);
    	imgPut = new ImageIcon(getClass().getResource("/res/octopus_res/put.png"));
    	imgPut = zoomImageIcon(imgPut,rate);
    	
    	imgBackgrounds = new ImageIcon[1];
    	imgBackgrounds[0] = new ImageIcon(getClass().getResource("/res/octopus_res/bak.png"));
    	imgBackgrounds[0] = zoomImageIcon(imgBackgrounds[0],rate);
    	
    	imgLocations = new ImageIcon[8];
    	for (int i = 0; i < imgLocations.length; i++) {
    		imgLocations[i] = new ImageIcon(getClass().getResource("/res/octopus_res/loc"+i+".png"));
    		imgLocations[i] = zoomImageIcon(imgLocations[i],rate);
    	}
    	
    	imgLife = new ImageIcon[2];
    	for (int i = 0; i < imgLife.length; i++) {
    		imgLife[i] = new ImageIcon(getClass().getResource("/res/octopus_res/life"+i+".png"));
    		imgLife[i] = zoomImageIcon(imgLife[i],rate);
    	}
    	
    	imgCatch = new ImageIcon[2];
    	for (int i = 0; i < imgCatch.length; i++) {
    		imgCatch[i] = new ImageIcon(getClass().getResource("/res/octopus_res/catch"+i+".png"));
    		imgCatch[i] = zoomImageIcon(imgCatch[i],rate);
    	}
    	
    	imgTouch0 = new ImageIcon[3];
    	for (int i = 0; i < imgTouch0.length; i++) {
    		imgTouch0[i] = new ImageIcon(getClass().getResource("/res/octopus_res/t0-"+i+".png"));
    		imgTouch0[i] = zoomImageIcon(imgTouch0[i],rate);
    	}
    	imgTouch1 = new ImageIcon[4];
    	for (int i = 0; i < imgTouch1.length; i++) {
    		imgTouch1[i] = new ImageIcon(getClass().getResource("/res/octopus_res/t1-"+i+".png"));
    		imgTouch1[i] = zoomImageIcon(imgTouch1[i],rate);
    	}
    	imgTouch2 = new ImageIcon[5];
    	for (int i = 0; i < imgTouch2.length; i++) {
    		imgTouch2[i] = new ImageIcon(getClass().getResource("/res/octopus_res/t2-"+i+".png"));
    		imgTouch2[i] = zoomImageIcon(imgTouch2[i],rate);
    	}
    	imgTouch3 = new ImageIcon[4];
    	for (int i = 0; i < imgTouch3.length; i++) {
    		imgTouch3[i] = new ImageIcon(getClass().getResource("/res/octopus_res/t3-"+i+".png"));
    		imgTouch3[i] = zoomImageIcon(imgTouch3[i],rate);
    	}
    	imgTouch4 = new ImageIcon[3];
    	for (int i = 0; i < imgTouch4.length; i++) {
    		imgTouch4[i] = new ImageIcon(getClass().getResource("/res/octopus_res/t4-"+i+".png"));
    		imgTouch4[i] = zoomImageIcon(imgTouch4[i],rate);
    	}
    }

    private void initLayout() {
    	bgDisplay = new JLabel[1];
    	bgDisplay[0] = new JLabel(imgBackgrounds[0]);
    	
        locDisplay = new JLabel[1];
        for (int i = 0; i < locDisplay.length; i++) {
            locDisplay[i] = new JLabel(imgLocations[0]);
        }
        
        lifeDisplay = new JLabel[2];
        for (int i = 0; i < lifeDisplay.length; i++) {
        	lifeDisplay[i] = new JLabel(imgLife[i]);
        }
        
        catchDisplay = new JLabel[1];
        for (int i = 0; i < catchDisplay.length; i++) {
        	catchDisplay[i] = new JLabel(imgBlank);
        }
        
        touch0Display = new JLabel[3];
        for (int i = 0; i < touch0Display.length; i++) {
        	touch0Display[i] = new JLabel(imgBlank);
        }
        touch1Display = new JLabel[4];
        for (int i = 0; i < touch1Display.length; i++) {
        	touch1Display[i] = new JLabel(imgBlank);
        }
        touch2Display = new JLabel[5];
        for (int i = 0; i < touch2Display.length; i++) {
        	touch2Display[i] = new JLabel(imgBlank);
        }
        touch3Display = new JLabel[4];
        for (int i = 0; i < touch3Display.length; i++) {
        	touch3Display[i] = new JLabel(imgBlank);
        }
        touch4Display = new JLabel[3];
        for (int i = 0; i < touch4Display.length; i++) {
        	touch4Display[i] = new JLabel(imgBlank);
        }
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(0xFF, 0xFF, 0xFF));
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
    	c.gridwidth = 1;
    	c.gridx = 0;
    	c.gridy = 0;
    	
    	//set backgrounds
    	panel.add(bgDisplay[0], c);
        
        //set locations
        for(int i=0;i<locDisplay.length;i++){
        	panel.add(locDisplay[i], c);
        }
        
        //set life
        for(int i=0;i<lifeDisplay.length;i++){
        	panel.add(lifeDisplay[i], c);
        }
        
        //set catch
        for(int i=0;i<catchDisplay.length;i++){
        	panel.add(catchDisplay[i], c);
        }
        
        //set touch
        for(int i=0;i<touch0Display.length;i++){
        	panel.add(touch0Display[i], c);
        }
        for(int i=0;i<touch1Display.length;i++){
        	panel.add(touch1Display[i], c);
        }
        for(int i=0;i<touch2Display.length;i++){
        	panel.add(touch2Display[i], c);
        }
        for(int i=0;i<touch3Display.length;i++){
        	panel.add(touch3Display[i], c);
        }
        for(int i=0;i<touch4Display.length;i++){
        	panel.add(touch4Display[i], c);
        }
       
        btnStart = new JButton("Start");
        btnStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if(!START){
            		initValues();
            		START = true;
            		btnStop.setText("Pause");
            		btnStop.setEnabled(true);
            		for(int i=0;i<locDisplay.length;i++){
    	        		locDisplay[i].setIcon(imgLocations[location]);
    	        	}
            		for (int i = 0; i < lifeDisplay.length; i++) {
                    	lifeDisplay[i].setIcon(imgLife[i]);
                    }
    	        	scoreLabel.setText("SCORE: "+SCORE);
//            		updateDisplay();
            		btnStart.setEnabled(false);
//                	loadGame("/res/automatons/manhole2-3.dot");
                	startTimer();
            	}   	
            }
        });
        
        btnStop = new JButton("Pause");
        btnStop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	System.out.println(START);
            	if(START){
            		START = false;
            		btnStart.setEnabled(true);
            		btnStart.setText("Restart");
            		btnStop.setText("Continue");
            		timer.cancel();
            		System.out.println("sdsdsadasdas");
            	}else{
            		START = true;
            		btnStop.setText("Pause");
            		btnStart.setEnabled(false);
            		startTimer();
            	}
            }
        });
//        
////      set on the score and miss
        scoreLabel = new JLabel("SCORE: "+SCORE+" ");
        speedLabel = new JLabel("SPEED: "+SPEED+" ");
        JPanel panelScore = new JPanel();
        panelScore.setBackground(new Color(0xFF, 0xFF, 0xFF));
        panelScore.add(scoreLabel);
        panelScore.add(speedLabel);

        //set on the buttons
        JPanel panelControl = new JPanel();
        panelControl.setBackground(new Color(0xFF, 0xFF, 0xFF));
        panelControl.add(btnStart);
        btnStop.setEnabled(false);
        panelControl.add(btnStop);

        
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
    	        	switch(((KeyEvent) event).getKeyChar()){
	    	        	case 'z':
	    	        		if(!MISS && START){
	    	        			if(location>0){
		    	        			if(location>5){
		    	        				location = 4;
		    	        			}
		    	        			else
			    	        			location--;
		    	        		}else if(getSomething){
		    	        	    	getSomething=false;
		    	        	    	new BonusDisplay(200).start();
		    	        	    	new PutDisplay(300).start();
		    	        		}
	    	        		}
	    	        		break;
	    	        	case 'x':
	    	        		if(!MISS && START){
	    	        			if(location<5){
		    	        			location++;
		    	        			grub = false;
		    	        		}else if(location!=5){
		    	        			location = 5;
		    	        		}else if(!grub){
		    	        			location = 6;
		    	        			grub = !grub;
		    	        		}else{
		    	        			location = 7;
		    	        			grub = !grub;
		    	        			SCORE++;
		    	        			getSomething = true;
		        	        		scoreLabel.setText("SCORE: "+SCORE);
		        	        		if(SCORE%5==0 && TickTime>=100){
		        	        			SPEED++;
		        	        			TickTime *= speedup;
		        	        			timer.cancel();
		        	        			startTimer();
		        	        			speedLabel.setText("SPEED: "+SPEED);
		        	        			System.out.println(SPEED+" "+TickTime);
		        	        		}
		    	        		}
		    	        		
	    	        		}
	    	        		break;
    	        	}
    	        	if(!MISS){
    	        		for(int i=0;i<locDisplay.length;i++){
        	        		locDisplay[i].setIcon(imgLocations[location]);
        	        	}
    	        	}
    	        }
    	    }
    	}, AWTEvent.KEY_EVENT_MASK);
    	//该代码片段来自于: http://www.sharejs.com/codes/java/6737
    }

//    private void loadGame(String game) {
//        automaton = new Automaton(getClass().getResourceAsStream(game),false);  
//        updateDisplay();
//        for (int i = 0; i < supportsDisplay.length; i++) {
//        	fallsDisplay[i].setIcon(imgBlank);
//        }
//    }

    public void updateDisplay() {
    	if(MISS){
    		getSomething = false;
    		if(touch2<4)
    			touch2=1;
    		else
    			touch2=7;
    		if(touch3<3)
    			touch3=1;
    		else
    			touch3=5;
    		locDisplay[0].setIcon(imgBlank);
		}
    	int t0,t1,t2,t3,t4;
    	for(int i=0;i<touch0Display.length;i++){
			touch0Display[i].setIcon(imgBlank);
		}
		if(touch0<3)
			t0 = touch0+1;
		else
			t0 = 2*3-1-touch0;
		for(int i=0;i<t0;i++){
			touch0Display[i].setIcon(imgTouch0[i]);
		}
		
		for(int i=0;i<touch1Display.length;i++){
			touch1Display[i].setIcon(imgBlank);
		}
		if(touch1<4)
			t1 = touch1+1;
		else
			t1 = 2*4-1-touch1;
		for(int i=0;i<t1;i++){
			touch1Display[i].setIcon(imgTouch1[i]);
		}
		
		for(int i=0;i<touch2Display.length;i++){
			touch2Display[i].setIcon(imgBlank);
		}
		if(touch2<5)
			t2 = touch2+1;
		else
			t2 = 2*5-1-touch2;
		for(int i=0;i<t2;i++){
			touch2Display[i].setIcon(imgTouch2[i]);
		}
		
		for(int i=0;i<touch3Display.length;i++){
			touch3Display[i].setIcon(imgBlank);
		}
		if(touch3<4)
			t3 = touch3+1;
		else
			t3 = 2*4-1-touch3;
		for(int i=0;i<t3;i++){
			touch3Display[i].setIcon(imgTouch3[i]);
		}
		
		for(int i=0;i<touch4Display.length;i++){
			touch4Display[i].setIcon(imgBlank);
		}
		if(touch4<3)
			t4 = touch4+1;
		else
			t4 = 2*3-1-touch4;
		for(int i=0;i<t4;i++){
			touch4Display[i].setIcon(imgTouch4[i]);
		}
		
		if(MISS){
			timer.cancel();
			try {
				catchDisplay();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		if(life==0){
    			START = false;
        		btnStart.setEnabled(true);
        		btnStart.setText("Restart");
        		btnStop.setEnabled(false);
    		}else{
        		life--;
        		location = 0;
            	locDisplay[0].setIcon(imgLocations[location]);
        		for (int i = 0; i < lifeDisplay.length; i++) {
                	lifeDisplay[i].setIcon(i<life ? imgLife[i] : imgBlank);
                }
        		startTimer();
    		}
    	}
    	
    }
    
    public void catchDisplay() throws InterruptedException{
    	for(int i=0;i<3;i++){
    		catchDisplay[0].setIcon(imgCatch[0]);
    		Thread.sleep(500);
    		catchDisplay[0].setIcon(imgCatch[1]);
    		Thread.sleep(500);
    	}
    	catchDisplay[0].setIcon(imgBlank);
    	MISS=false;
    }
    
    
    private ImageIcon zoomImageIcon(ImageIcon img, double rate) {
    	if(rate<=0.0)
    		return img;
    	int width = (int)(img.getIconWidth()*rate);
    	int height = (int)(img.getIconHeight()*rate);
    	img.setImage(img.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
    	return img;
    }
    
    private void startTimer(){
    	timer = new Timer();
    	//sTickTime = Integer.parseInt(speed.getText());
    	timer.schedule(new TimerTask() {  
            @Override  
            public void run() {
            	switch(touchChoice){
            	case 0:
            		if(upDown){
            			if(touch0<4){
            				touch0++;
            			}
            			else{
            				touch0 = -1;
            				upDown = !upDown;
            			}
            		}else{
            			if(touch1<6)
                			touch1++;
                		else
                			touch1 = -1;
            		}
            		break;
            	case 2:
            		if(touch2<8)
            			touch2++;
            		else
            			touch2 = -1;
            		break;
            	case 1:
            		if(touch3<6)
            			touch3++;
            		else
            			touch3 = -1;
            		break;
            	case 3:
            		if(touch4<4)
            			touch4++;
            		else
            			touch4 = -1;
            		break;
            	}
            	MISS = (touch0==2 && location==1)
            			|| (touch1==3 && location==2)
            			|| (touch2==4 && location==3)
            			|| (touch3==3 && location==4)
            			|| (touch4==2 && location>4);
            	updateDisplay();
            	touchChoice = (touchChoice+1)%4;
            }  
        }, TickTime, TickTime);
    }
    
    class PutDisplay extends Thread {
    	private int ticktime;
        public PutDisplay(int tick) {
        	ticktime = tick;
            //super(threadName);
        }
     
        public void run() {
            locDisplay[0].setIcon(imgPut);
            try {
				Thread.sleep(ticktime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            locDisplay[0].setIcon(imgLocations[0]);  
        }
    }
    
    class BonusDisplay extends Thread {
    	private int ticktime;
        public BonusDisplay(int tick) {
        	ticktime = tick;
            //super(threadName);
        }
     
        public void run() {
        	for(int i=0;i<3;i++){
        		SCORE++;
    	    	scoreLabel.setText("SCORE: "+SCORE);
    	    	try {
    				Thread.sleep(ticktime);
    			} catch (InterruptedException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
        	}
        }
    }
    
    public static void main(String[] args) {
    	Octopus digi = new Octopus();
    	digi.init();
    	digi.setLocation(200, 100);           
//    	digi.setLocationRelativeTo(null); //本语句实现窗口居屏幕中央
    	digi.setSize((int)(800*rate),(int)(600*rate));            //设置窗体的大小为300*200像素大小
    	digi.setResizable(false);       //设置窗体是否可以调整大小，参数为布尔值
    	digi.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //设置窗体可见，没有该语句，窗体将不可见，此语句必须有，否则没有界面就没有如何意义了
    	digi.setVisible(true); 
        
    }
}

import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;

public class DigiInvader extends JFrame {
    private JButton btnAim;
    private JButton btnFire;
    private JButton btnStart;
    private JButton btnStop;
    private ImageIcon imgDigits[];
    private ImageIcon imgMonster;
    private ImageIcon imgSeparator;
    private ImageIcon imgBlank;
    private JLabel lblDisplay[];
    private JLabel theScore;
    private JLabel SCORE;
    private Automaton automaton;
    private int gameLength;
    private int aim;
    private int gameSize;
    private int score;
    private boolean START;
    private String title;
    private Timer timer = null;

    public void init() {
    	title = "res/automatons/game" + gameLength + ".dot";
        gameLength = 6;
        aim = 0;
        gameSize = 3;
        score = 0;
        START = false;
        loadImages();
        initLayout();
        //loadGame("/automatons/game" + gameLength + ".dot");
    }

    private void loadImages() {
        imgDigits = new ImageIcon[10];
        for (int i = 0; i < imgDigits.length; i++) {
            imgDigits[i] = new ImageIcon(getClass().getResource("res/Resources/" + i + ".png"));
        }

        imgMonster = new ImageIcon(getClass().getResource("res/Resources/n.png"));
        imgSeparator = new ImageIcon(getClass().getResource("res/Resources/s.png"));
        imgBlank = new ImageIcon(getClass().getResource("res/Resources/e.png"));
    }

    private void initLayout() {
        lblDisplay = new JLabel[gameLength + 2];
        lblDisplay[0] = new JLabel(imgDigits[0]);
        lblDisplay[1] = new JLabel(imgSeparator);
        for (int i = 2; i < lblDisplay.length; i++) {
            lblDisplay[i] = new JLabel(imgBlank);
        }

        JPanel panelDisplay = new JPanel();
        for (int i = 0; i < lblDisplay.length; i++) {
            panelDisplay.add(lblDisplay[i]);
        }

        btnAim = new JButton("Aim");
        btnAim.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if(START){
            		if(aim+1==gameSize){
            			aim = gameSize;
            			lblDisplay[0].setIcon(imgMonster);
            		}else{
            			if(aim==gameSize)
            				aim=0;
            			else
            				aim = (aim + 1) % gameSize;
            			lblDisplay[0].setIcon(imgDigits[aim]);
            		} 
            	}  
            }
        });

        btnFire = new JButton("Fire");
        btnFire.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if(START){
            		System.out.println("Fire!!!!");
            		if(automaton.step(aim,0))
            			updateDisplay();
            	} 
            }
        });
        
        btnStart = new JButton("Start");
        btnStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if(!START){
            		START = true;
                	loadGame("/res/automatons/game" + "M3" + ".dot");
                	timer = new Timer();
                	timer.schedule(new TimerTask() {  
                        @Override  
                        public void run() {  
                        	if(automaton.step(-2,0))
                  			  updateDisplay();
                        	//System.out.println("dfdfdf");  
                        }  
                    }, 2000, 1500);
            	}   	
            }
        });
        
        btnStop = new JButton("Stop");
        btnStop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if(START){
            		START = false;
            		timer.cancel();
            		aim = 0;
            		score = 0;
            		theScore.setText("0");
            		lblDisplay[0].setIcon(imgDigits[0]);
                    lblDisplay[1].setIcon(imgSeparator);
                    for (int i = 2; i < lblDisplay.length; i++) {
                        lblDisplay[i].setIcon(imgBlank);
                    }
            	}
            }
        });
        
        theScore = new JLabel("0");
        SCORE = new JLabel("SCORE: ");
        
        JPanel panelControl = new JPanel();
        panelControl.add(btnStart);
        panelControl.add(btnAim);
        panelControl.add(btnFire);
        panelControl.add(btnStop);
        panelControl.add(SCORE);
        panelControl.add(theScore);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(0xC8, 0xD5, 0xD1));
        panel.add(panelDisplay);
        panel.add(panelControl);

        getContentPane().add(panel);
    }

    private void loadGame(String game) {
        automaton = new Automaton(getClass().getResourceAsStream(game), gameLength);
        for (int i = 0; i < gameLength; i++) {
        	int digit;
        	if(i<gameLength-1)
        		digit = automaton.getDisplay(i);
        	else{
        		digit = (int) Math.round(Math.random() * (gameSize-1));
        		automaton.setLast(digit);
        	}
            lblDisplay[i + 2].setIcon(digit == -1 ? imgBlank : imgDigits[digit]);
        }
        automaton.printCurrent();
        
        //updateDisplay();
    }

    public void updateDisplay() {
    	for (int i = 0; i < gameLength; i++) {
            int digit = automaton.getDisplay(i);
            if(digit==gameSize){
            	lblDisplay[i + 2].setIcon(imgMonster);
            }else if(digit==-1){
            	lblDisplay[i + 2].setIcon(imgBlank);
            }else{
            	lblDisplay[i + 2].setIcon(imgDigits[digit]);
            }
        }
    	theScore.setText(""+automaton.getScore());
        automaton.printCurrent();
    }
    
    
    public static void main(String[] args) {
    	DigiInvader digi = new DigiInvader();
    	digi.init();
    	digi.setLocation(300, 300);           
    	//digi.setLocationRelativeTo(null); //本语句实现窗口居屏幕中央
    	digi.setSize(700,200);            //设置窗体的大小为300*200像素大小
    	digi.setResizable(false);       //设置窗体是否可以调整大小，参数为布尔值
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

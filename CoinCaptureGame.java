package coincapturegame;
import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.geom.RectangularShape;
import java.io.*;

class GameItem {  

    private String name;
    private double weight; 
    private int XLocation;
    private int YLocation;
    
    public void setName (String N) {
        name = N;
    }
    public String getName () {
        return name;
    }
    public void setWeight (double W) {
        weight = W;
    }
    public double getWeight () {
        return weight;
    }
    public void setXLocation (int XL) {
        XLocation = XL;
    }
    public int getXLocation () {
        return XLocation;
    }
    public void setYLocation (int YL) {
        YLocation = YL;
    }
    public int getYLocation () {
        return YLocation;
    }
}

class GoldCoin extends GameItem {
   
    private int CoinRadius = 5;
    private String CoinName = "GoldCoin";
    
    public void setCoinName (String CN) {
        CoinName = CN;
    }
    public String getCoinName () {
        return CoinName;
    }
    public void setCoinRadius (int CR) {
        CoinRadius = CR;
    }
    public int getCoinRadius () {
        return CoinRadius;
    }
    
    public GoldCoin (String insNum, int xval, int yval) {
        this.setCoinName(this.getCoinName() + insNum); //insNum gives a numerical index to the coin instantiated 
        this.setXLocation(xval);
        this.setYLocation(yval);
    }
}

class GamePlayerCircle extends Ellipse2D.Float  {     //maybe use implements Shape 
    
    ArrayList <GameItem> Inventory = new ArrayList <> (); //All items player collects
    ArrayList <GameItem> CoinPurse = new ArrayList <> (); //Only and All coins player collects
    
    private String playerName;
    private int playerRadius = 25;
    private int XPosition;
    private int YPosition;
    
    public void setPlayerName (String PN){
        playerName = PN;
    }
    public String getPlayerName (){
        return playerName;
    }
    public void setPlayerRadius (int PR){
        playerRadius = PR;
    }
    public int getPlayerRadius (){
        return playerRadius;
    }
    public void setXPosition (int XP){
        XPosition = XP;
    }
    public int getXPosition (){
        return XPosition;
    }
    public void setYPosition (int YP){
        YPosition = YP;
    }
    public int getYPosition (){
        return YPosition;
    }
}

//------------------------------------------------------------------------

class CoinGameCore {

    Random random = new Random();
    Scanner input = new Scanner (System.in); 
    
    GoldCoin GenesisCoin = new GoldCoin ("-1",100,100); //NOT USED good for testing
    
    GamePlayerCircle player = new GamePlayerCircle(); //make player object
    
    private int numUniqueItemsInGame;
    private int numCoins;
    private int numItemsInGame = numCoins+numUniqueItemsInGame;
    public int CoinCounter;
    public int coinposX [];
    public int coinposY []; //= new int [numCoins];
    
    private int XStart; //for player 
    private int YStart; //for player
    
    ArrayList <GameItem> ItemList = new ArrayList <>();  //for game
    ArrayList <GoldCoin> CoinList = new ArrayList <> (); //for game
    
    public void setNumCoins(int NumC){
        numCoins = NumC;
    }
    public int getNumCoins(){
        return numCoins;
    }
    
    class DrawingP extends JPanel {
        
        public void paintComponent (Graphics g) {
         
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, this.getWidth(), this.getHeight()); //make background black

            Graphics2D g2D = (Graphics2D) g; //cast for 2D graphics
            
            g2D.setColor(Color.CYAN);
            g2D.fill(player); 
            
    //--------------Here is where collision of player and objects is detected----------------//
    
            boolean CoinRemovalRequired = false; //if yes the given iteration will remove object from game
            int quickTracker = -1;               //stores index information of item to be altered/removed
            
            for (int i = 0; i < CoinList.size(); i++) {
                
                if (player.contains(CoinList.get(i).getXLocation(), CoinList.get(i).getYLocation()) == true){
                    quickTracker = i;
                    CoinRemovalRequired = true;
                    g2D.setColor(Color.CYAN);
                    g2D.fillOval(CoinList.get(i).getXLocation(), CoinList.get(i).getYLocation(), 
                                 CoinList.get(i).getCoinRadius(),CoinList.get(i).getCoinRadius()); 
                    System.out.printf("\nYou got %s\n", CoinList.get(i).getCoinName());
                    CoinCounter++;
                    CoinCountingLabel.setText("Coin Count: " + CoinCounter);
                } else {
                    
                    g2D.setColor(Color.yellow);
                    g2D.fillOval(CoinList.get(i).getXLocation(), CoinList.get(i).getYLocation(), 
                                 CoinList.get(i).getCoinRadius(),CoinList.get(i).getCoinRadius());    
                }   
            }
            
            if (CoinRemovalRequired == true && quickTracker >= 0 ){

                player.CoinPurse.add(CoinList.get(quickTracker));
                CoinList.remove(quickTracker);
                CoinRemovalRequired = false;
            }
            
            if (CoinList.size() == 0) {
            
                String YOUWIN = "YOU WIN!";
                Font BigWinnerFont = new Font("serif", Font.BOLD, 38);
                g2D.setColor(Color.RED);
                g2D.setFont(BigWinnerFont);
                g2D.drawString(YOUWIN, 260, 300);
            }
        }             
    }
//------------------------Here are the Instacne Variables for Componensts-----------------------//
    
    //Level 1           //Levels indicate sub-division in LayoutManager, with fundemantal -> lower level 
    JFrame TheFrame;

    //Level 2
    DrawingP GamePanel;
    JLabel ControlsLabel;
    JLabel CoinCountingLabel;
    
//------------------Properties of JFrame and JPanel Game will use--------------------//
    
    private int FrameHorizontalSize = 700;
    private int FrameVerticalSize = 700;
    private int GamePanelSafetyX = FrameHorizontalSize -20;
    private int GamePanelSafetyY = FrameHorizontalSize -130;
    Dimension MinSizeFrame = new Dimension(FrameHorizontalSize,FrameVerticalSize);
    Dimension MaxSizeFrame = new Dimension(FrameHorizontalSize,FrameVerticalSize);

    public void initializeGame (String insPlayerName){ //Maybe add more or add to game constructor 
        player.setPlayerName(insPlayerName);
        System.out.printf("\nWelcome %s!\n",player.getPlayerName());  
    }
    
    public void initializeStuff (int numC) {
        setNumCoins(numC); 
        CoinCounter = 0;
        
        coinposX = new int [numCoins];
        coinposY = new int [numCoins];
        
        XStart = random.nextInt(50)+ 300;
        YStart = random.nextInt(50)+ 25;
        
        player.setXPosition(XStart);
        player.setYPosition(YStart); 
        
        //Initializes the player object 
       
        player.setFrame(player.getXPosition(), player.getYPosition(), 
                        player.getPlayerRadius(), player.getPlayerRadius());
        
    //--------------Here the Coin initialization occurs-----------------//
        
        //This for loop generates array stored values for random X and Y coordinates 
        for (int i = 0; i < numCoins; i++) { 
            coinposX [i] = random.nextInt(500)+50;
            coinposY [i] = random.nextInt(500)+50;
            }

        //This for loop sets the coordinates for each indivdual coin object as one from the arrays.
        //It also creates a new coinobject for each coin, adding them all to ArrayList CoinList.
        //The String quickCounter is an string that stores the character of i for eachiteration. 
        //This allows the loop to make a name for each coin labled GoldCoini -> example for i = 3,
        //Its name is GoldCoin3. This is not a refernce hwoever as the refences are the ArrayList Indices
        
        String quickCounter;
        for (int i = 0; i < numCoins; i++) {
            
            quickCounter = Integer.toString(i);

            CoinList.add(new GoldCoin(quickCounter, coinposX [i], coinposY [i]));
        }
    }
    
    //-------------------Run is the meat and potatoes of the Game class-----------------//
    
    public void Run() {
        
        Dimension LabelDimension = new Dimension(40,40);
        Font CoinCountFont = new Font("serif", Font.PLAIN,20);
    
        TheFrame = new JFrame();
        GamePanel = new DrawingP();
        ControlsLabel = new JLabel("Use the arrow keys to move"); // need interaction keys
        CoinCountingLabel = new JLabel("Coin Count: " + CoinCounter);
        
        ControlsLabel.setHorizontalAlignment(JLabel.CENTER);
        CoinCountingLabel.setHorizontalAlignment(JLabel.CENTER);
        ControlsLabel.setPreferredSize(LabelDimension);
        CoinCountingLabel.setPreferredSize(LabelDimension);
        CoinCountingLabel.setFont(CoinCountFont);
        CoinCountingLabel.setBackground(Color.LIGHT_GRAY);
        
        GamePanel.addKeyListener(new MovePlayerListener());
       
        TheFrame.getContentPane().add(BorderLayout.CENTER, GamePanel);
        TheFrame.getContentPane().add(BorderLayout.SOUTH, ControlsLabel);
        TheFrame.getContentPane().add(BorderLayout.NORTH, CoinCountingLabel);
        TheFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        GamePanel.setBackground(Color.BLACK);
        
        TheFrame.setSize(FrameHorizontalSize, FrameVerticalSize);
        TheFrame.setMinimumSize(MinSizeFrame); 
        TheFrame.setMaximumSize(MaxSizeFrame);
        TheFrame.setVisible(true);
        TheFrame.repaint();        
        GamePanel.setFocusable(true);
    }
    
    //-------------The listner allows the player to move with arrow keys------------// 
    
    class MovePlayerListener implements KeyListener {
        
        public void keyTyped (KeyEvent ke){ //not needed - could be of use later on
            
        }
        public void keyPressed (KeyEvent ke){
            
            switch (ke.getKeyCode()) {
                
                case KeyEvent.VK_DOWN:
                    if (player.getYPosition() < GamePanelSafetyY) {
                    player.setYPosition(player.getYPosition()+10);
                    player.setFrame(player.getXPosition(), player.getYPosition(), 
                                    player.getPlayerRadius(), player.getPlayerRadius());
                    TheFrame.repaint();}
                    else {TheFrame.repaint();}
                    //PanelIDK.repaint();
                break;
                case KeyEvent.VK_UP:
                    if (player.getYPosition() > 0) {    
                    player.setYPosition(player.getYPosition()-10);
                    player.setFrame(player.getXPosition(), player.getYPosition(), 
                                    player.getPlayerRadius(), player.getPlayerRadius());
                    TheFrame.repaint();}
                    else {TheFrame.repaint();}
                    //PanelIDK.repaint();
                break;
                case KeyEvent.VK_LEFT:
                    if (player.getXPosition() > 0){    
                    player.setXPosition(player.getXPosition()-10);
                    player.setFrame(player.getXPosition(), player.getYPosition(), 
                                    player.getPlayerRadius(), player.getPlayerRadius());
                    TheFrame.repaint();}
                    else {TheFrame.repaint();}
                    //PanelIDK.repaint();
                break;
                case KeyEvent.VK_RIGHT:
                    if (player.getXPosition() < GamePanelSafetyX) { 
                    player.setXPosition(player.getXPosition()+10);
                    player.setFrame(player.getXPosition(), player.getYPosition(), 
                                    player.getPlayerRadius(), player.getPlayerRadius());
                    TheFrame.repaint();}
                    else {TheFrame.repaint();}
                    //PanelIDK.repaint();
                break;
            }  
        }
        public void keyReleased (KeyEvent ke){ //not needed - could be of use later on
        }
    }   
}

public class CoinCaptureGame {
    
    public static void main(String[] args) {

        CoinGameCore MyGame = new CoinGameCore();
        
//--One must run all methods in proper order: initializeGame,initializeStuff,Run
        String Tony = "Tony";
        MyGame.initializeGame(Tony);
        MyGame.initializeStuff(25); //needs number coins 
        MyGame.Run();
    }    
}


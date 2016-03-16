package simoriOn;

import java.util.Enumeration;
import javax.swing.JButton;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTextArea;

/**
 * GUI class for the Simori-ON.
 * Buttons have some functionality are subject to change.
 * Contains a nested MatrixButton and ONButton class.
 *
 * @author Leon Schiedermair
 * @author Petr Vavruska
 * @author James Badham
 * @author Matt Hennigan
 * @version 1.0
 */
@SuppressWarnings("serial")
public class GUI extends JFrame {
	
    private ButtonGroup controlButtons = new ButtonGroup();
    MatrixButton[][] matrixButtons; //holds all matrix buttons
     
    
    // Putting these here to avoid instantiating an icon for each matrixButton.
    Icon greyIcon = new ImageIcon(getClass().getResource("greyButton.png"));
    Icon orangeIcon = new ImageIcon(getClass().getResource("orangeButton.png"));
    
    private JButton OnButton = new JButton("ON");
    private JButton L1Button = new JButton("L1");
    private JButton L2Button = new JButton("L2");
    private JButton L3Button = new JButton("L3");
    private JButton L4Button = new JButton("L4");
    private JButton R1Button = new JButton("R1");
    private JButton R2Button = new JButton("R2");
    private JButton R3Button = new JButton("R3");
    private JButton R4Button = new JButton("R4");
    JTextArea LCDTextArea = new JTextArea();
    private JButton OKButton = new JButton("OK");
	
    /**
     * GUI class constructor.
     * Adds GUI buttons to the frame and sets their positioning.
     * 
     * @author Leon Schiedermair
     * @throws MidiUnavailableException
     * @throws InvalidMidiDataException 
     */
    public GUI() throws MidiUnavailableException, InvalidMidiDataException {
	
        setTitle("Simori-ON");
        setSize(600, 630);
	controlButtons.add(L1Button);
	controlButtons.add(L2Button);
	controlButtons.add(L3Button);
	controlButtons.add(L4Button);
	controlButtons.add(R1Button);
	controlButtons.add(R2Button);
	controlButtons.add(R3Button);
	controlButtons.add(R4Button);
	controlButtons.add(OKButton);
	this.matrixButtons = createMatrix();
		
	//Disable all buttons in controlButtons group and also Matrix buttons
	enableControls(false);	
		
        setLayout( null );
        OnButton.setBounds(270, 0, 60, 60); add(OnButton);
        L1Button.setBounds(0, 120, 60, 60); add(L1Button);
        L2Button.setBounds(0, 200, 60, 60); add(L2Button);
        L3Button.setBounds(0, 280, 60, 60); add(L3Button);
        L4Button.setBounds(0, 360, 60, 60); add(L4Button);
        R1Button.setBounds(540, 120, 60, 60); add(R1Button);
        R2Button.setBounds(540, 200, 60, 60); add(R2Button);
        R3Button.setBounds(540, 280, 60, 60); add(R3Button);
        R4Button.setBounds(540, 360, 60, 60); add(R4Button);
        LCDTextArea.setBounds(150, 550, 200, 20); add(LCDTextArea);
        LCDTextArea.setEditable(false);
        OKButton.setBounds(400, 550, 50, 50); add(OKButton);
        }
   
        /**
         * @author Petr Vavruska
         * This method enables all controls and "wakes up" the SimoriOn
         * @param state - boolean variable, controls are either enabled or disabled
         * 				  depending on value of state.
         */
	public void enableControls(boolean state) {
			System.out.println("enable controls :" + state);
            if(state==true) LCDTextArea.setText("READY");
            
            else  LCDTextArea.setText("");
            
            //enable the matrix
            for(int i = 0; i < 16; i++) {
                for (int j = 0; j < 16; j++) {
                    matrixButtons[i][j].setEnabled(state);
                    matrixButtons[i][j].illuminate(false);
                    }}
            //enable members of controlButtons group
            Enumeration<AbstractButton> enumeration = controlButtons.getElements();
            while (enumeration.hasMoreElements()) {
                enumeration.nextElement().setEnabled(state);
            }
	}
	
    /**
     * Creates the matrix buttons for the Simori-ON GUI. Sets their position on
     * the frame and returns a 2-dimensional array containing all matrix
     * buttons. Each row is represented by the first dimension of the array.
     * Each column is represented by the second dimension of the array.
     * 
     * @author Leon Schiedermair
     * @return The 2D array with all matrix buttons indexed by [row][column].
     */
    private MatrixButton[][] createMatrix(){
	
        // Loop counter for rows (countY) and columns (countX).
        int countY = 0;
        int countX = 0;
        this.matrixButtons = new MatrixButton[16][16];

        //i specifies y axis position of row of matrix buttons.
        for (int i=60; i<540; i+=30) {
            //j specifies x axis position of matrix buttons in the row.
            for (int j=60; j<540; j+=30) {
                matrixButtons[countY][countX] = new MatrixButton("");
                matrixButtons[countY][countX].setBounds(i, j, 30, 30);
                matrixButtons[countY][countX].x = countY;
                matrixButtons[countY][countX].y = countX;
                matrixButtons[countY][countX].setEnabled(false);
                add(matrixButtons[countY][countX]);
                countX++;
            }
            countX = 0;
            countY++;
        }
		
	return matrixButtons;
    }
    	
    /**
     * Nested class for the matrix buttons. Has some functionality such as 
     * illuminating on click, although subject to change. Buttons are by
     * default grey and circular and of size 30x30.
     * 
     * @author Leon Schiedermair
     * @author Petr Vavruska
     * @version 1.0
     */
    
    class MatrixButton extends JButton {
        
        boolean illuminated = false;
        boolean hasTone = false;
        int x,y,indexS;
        int countS=0;
        
        /**
         * Constructor for MatrixButton class. Takes a string s if text is
         * needed for the button although currently used only with empty strings
         * to ensure there is no text displayed on each button.
         * 
         * @author Leon Schiedermair
         * @author Petr Vavruska
         * @param s The text to be displayed on the button. Should usually be
         *          an empty string.
         */
        MatrixButton( String s ) {
            super( s );
            this.setIcon(greyIcon);
            }
        
        /**
         * Causes button icons to change to orange (illuminated) or grey (not
         * illuminated) depending on boolean light. Invoke method on a matrix
         * button to change its illuminated status.
         * 
         * @author Leon Schiedermair
         * @param light set to true to illuminate a button or false to turn off
         */
        public void illuminate(boolean light) {
            if (light==true) {
                this.setIcon(orangeIcon);
                this.illuminated = true;
            }
            else {
                this.setIcon(greyIcon);
                this.illuminated = false;
            }
        }
	 
    }

    /**
     * @author Petr Vavruska
     * @param x - specifies coordinate of the column to be illuminated
     * @param light - a boolean, specifies whether light should be turned ON/OFF
     */
    public void illuminate_column(int x,boolean light)
    { 
    	for(int i = 0; i < 16; i++) {
        for (int j = 0; j < 16; j++) {
        	if(i==x){
            matrixButtons[i][j].illuminate(light);
            }}
        }
    	}
    
    /**
     * @author Petr Vavruska
     * @param y - specifies coordinate of the row to be illuminated
     * @param light - a boolean, specifies whether light should be turned ON/OFF
     */
    public void illuminate_row(int x,boolean light)
    { 
    	for(int i = 0; i < 16; i++) {
        for (int j = 0; j < 16; j++) {
        	if(j==x){
            matrixButtons[i][j].illuminate(light);
            }}
        }
    	}
    	
    /**
     * Illuminates/turns off the four matrix buttons representing the clock hand
     * for a given column.
     * 
     * @param x     The x coordinate as an integer of the column to illuminate.
     * @param light Boolean specifying whether the clock hand should be
     *              illuminated or not.
     *              
     *              added: Check for boolean hasTone - if matrixButton is set, it won't be de-illuminated by clock hand
     */
    public void illuminateClockHand(int x, boolean light) {
    	if(matrixButtons[x][0].hasTone==false && light == false)
    		matrixButtons[x][0].illuminate(light);
    	else
    		matrixButtons[x][0].illuminate(true);
    	if(matrixButtons[x][5].hasTone==false && light == false)
    		matrixButtons[x][5].illuminate(light);
    	else
    		matrixButtons[x][5].illuminate(true);
    	if(matrixButtons[x][10].hasTone==false && light == false)
    		matrixButtons[x][10].illuminate(light);
    	else
    		matrixButtons[x][10].illuminate(true);
    	if(matrixButtons[x][15].hasTone==false && light == false)
    		matrixButtons[x][15].illuminate(light);
    	else
    		matrixButtons[x][15].illuminate(true);
    }
    
    /**
     * Unilluminates the clock hand matrix buttons for a given column.
     * 
     * @param x     The x coordinate as an integer of the column to illuminate.
     */
    public void unilluminateClockHand(int x) {
    	illuminateClockHand(x, false);
    }
    
    
    /**
     * Clears all illumination set for the matrix buttons and returns them to
     * their default state (not illuminated).
     */
    public void resetIllumination() {
        for(int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                matrixButtons[i][j].illuminate(false);
            }
        }
    }
    
       
    /**
     * @author Petr Vavruska
     * @return - returns okButton
     */
    public JButton getOkButton(){
    	return OKButton;
    }
    
    /**
     * @author Petr Vavruska
     * @return - returns OnButton
     */
    public JButton getOnButton(){
    	return OnButton;
    }
    
    /**
     * @author Petr Vavruska
     * @return - returns 
     */
    public MatrixButton[][] getMatrixButtons(){
    	return matrixButtons;
    }
    
   
    /**
     * These methods below return side control buttons.
     * @author Petr Vavruska
     * @return - returns a button
     */
    
    public JButton getL1(){return L1Button;}
    public JButton getL2(){return L2Button;}
    public JButton getL3(){return L3Button;}
    public JButton getL4(){return L4Button;}
    
    public JButton getR1(){return R1Button;}
    public JButton getR2(){return R2Button;}
    public JButton getR3(){return R3Button;}
    public JButton getR4(){return R4Button;}
    
    /**
     * This method reilluminates set matrix buttons based on boolean matrix property of the layer.
     * @author Petr Vavruska
     */
    public void repaintMatrix(boolean[][] matrix)
    {
    	for(int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
            	try{
                matrixButtons[i][j].illuminate(matrix[i][j]);
                if((matrix[i][j])==true)System.out.println((matrix[i][j])+"matrix bool");
                }catch (NullPointerException e) {
                	
        			continue;
            	}
                }
            }
    
    }
    
    
    
    /**
     * Generates JFrame/GUI.
     * 
     * @author Leon Schiedermair
     * @author Petr Vavruska
     * @throws MidiUnavailableException - makes use of Player class object
     * @throws InvalidMidiDataException 
     */
    public void createGUI(){
        this.setVisible( true );
        this.setLocationRelativeTo( null );
        this.setResizable( false );
        this.repaint();
    }
}
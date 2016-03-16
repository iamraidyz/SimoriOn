package simoriOn;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.JButton;

import simoriOn.GUI.MatrixButton;


/**
 * Abstract Mode Class has methods and fields common for all SimoriOn modes
 * @author Petr Vavruska
 */
public abstract class Mode {

	private Player player;
	private GUI view;
	private static SimoriOn controller;
	MatrixButton[][] matrix;
	
	private ActionListener onButtonListener;
	private ActionListener okButtonListener;
	private ActionListener matrixListener;
	
	public Mode(GUI view,SimoriOn controller){
		this.player=player; this.view=view;Mode.controller=controller;
	}

	/**
	 * Returns GUI used by the controller.
	 * @author Petr Vavruska
	 **/
	public GUI getView(){
		return this.view;
	}
	
	/**
	 * Returns Player used by the controller.
	 * @author Petr Vavruska
	 **/
	public Player getPlayer(){
		return controller.currentLayer.player;
	}
	
	/**
	 * Returns the controller for current instance of SimoriOn.
	 * @author Petr Vavruska
	 **/
	public SimoriOn getController(){
		return Mode.controller;
	}
	
	
	/**
	 * Adds actionListener to On button - common for all modes except OnOffMode
	 * @author Petr Vavruska
	 **/
	public void onButtonControl(){
		onButtonListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {                  
            	try {
					controller.getMode().onButtonClicked();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
      };                
      view.getOnButton().addActionListener(onButtonListener); 
	}
	

	/**
	 * Adds actionListener to Ok button.
	 * @author Petr Vavruska
	 **/
	public void okButtonControl(){
		JButton button =view.getOkButton();
		
		okButtonListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {                  
				controller.getMode().okButtonClicked();}};
		
	            button.addActionListener(okButtonListener);
	}
	

	/**
	 * Adds actionListener to MatriButtons, common for all modes except OnOff mode.
	 * @author Petr Vavruska
	 **/
	public void matrixButtonControl()
	{
		this.matrix=view.getMatrixButtons();
		matrixListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {                  
				try {
					controller.getMode().matrixButtonClicked(actionEvent.getSource());
				} catch (InvalidMidiDataException | MidiUnavailableException e) {
					System.out.println(e);
				}}};
		
		for(int i = 0; i < 16; i++) {
	        for (int j = 0; j < 16; j++) {
	            matrix[i][j].addActionListener(matrixListener);}
	        }
	}


	/**
	 * Removes listeners from MatrixButtons.
	 * @author Petr Vavruska
	 **/
	public void removeMatrixListeners(){
		for(int i = 0; i < 16; i++) {
	        for (int j = 0; j < 16; j++) {
	            matrix[i][j].removeActionListener(matrix[i][j].getActionListeners()[0]);
	            }
	        }
		
	}
	

	/**
	 * Removes listeners from all buttons except MatrixButtons.
	 * @author Petr Vavruska
	 **/
	public void removeListeners()
	{
		view.getOnButton().removeActionListener(view.getOnButton().getActionListeners()[0]);
		if (!(controller.getMode() instanceof OnOffMode)){ removeMatrixListeners();
		view.getOkButton().removeActionListener(view.getOkButton().getActionListeners()[0]);}
		
	}
	
	
	//add Turn OFF method, that could be called from any mode(except onOff) so we don't have to define behavior for On button so many times
	
	// Execute all addListeners for given Mode
	public abstract void addListeners();
	
	// Add behavior for both Player and GUI on OnButton click
	public abstract void onButtonClicked() throws InterruptedException;
	 
	// Add behavior for both Player and GUI on OkButton click
	public abstract void okButtonClicked();
	 
	// Add behavior for both Player and GUI on MatrixButton click
	public abstract void matrixButtonClicked(Object mB) throws InvalidMidiDataException, MidiUnavailableException;
	
}
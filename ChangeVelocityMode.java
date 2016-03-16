package simoriOn;


import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import simoriOn.GUI.MatrixButton;

/**
* This class will control functionality for the changeVelocityMode
* @author Matt Hennigan
* version 1.0
*/

public class ChangeVelocityMode extends Mode {
	
	
	public ChangeVelocityMode(GUI view, SimoriOn controller) {
		super(view, controller);
		}
	
	// Add all event listeners.
	public void addListeners(){
		
		super.matrixButtonControl();
		super.onButtonControl();
		super.okButtonControl();
		System.out.println("Event listeners added.");
	}


	@Override
	public void onButtonClicked() throws InterruptedException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void okButtonClicked() {
	    // Set text field to empty string.
	    getView().LCDTextArea.setText("");

		super.removeListeners();
		super.getView().enableControls(true);
		
		try {
		    super.getController().setPerf();
		} catch (InterruptedException e) {
		    // Ignore interrupted exception.
		}
	}

  /**
   * Method to illuminate the row and column surrounding a matrix button when clicked.
   * @author Matt Hennigan
   * @param i
   * @param j 
   */
  public void illuminateRowAndColumn(int i, int j) {
      super.getView().illuminate_row(i, true);
      super.getView().illuminate_column(j, true);
  }
	
	@Override
	public void matrixButtonClicked(Object o) throws InvalidMidiDataException, MidiUnavailableException {
		System.out.print("Changing velocity");
		MatrixButton mB=(MatrixButton) o;	
		int xCoord = mB.x;
		int yCoord = mB.y;
		illuminateRowAndColumn(yCoord, xCoord);
		
		int velocity = mB.x * mB.y;
		super.getPlayer().changeVelocity(velocity);
		
		// Set text to the new velocity.
		super.getView().LCDTextArea.setText("Velocity:" + velocity);
	}
}
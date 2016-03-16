package simoriOn;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import simoriOn.GUI.MatrixButton;



/**
 * Class to control the functionality of the Change Loop Speed Mode
 * @author James Badham
 */
public class ChangeLoopSpeedMode extends Mode{
    
    
    /**
     * Change loop speed mode constructor.
     * @author James Badham
     * @param player
     * @param view
     * @param controller 
     */
    public ChangeLoopSpeedMode(GUI view, SimoriOn controller) {
        super(view, controller);
    }
    /**
     * Overridden addListeners method
     * Adds all relevant event listeners.
     * @author James Badham
     */
    @Override
    public void addListeners() {
        super.onButtonControl();
	super.matrixButtonControl();
	super.okButtonControl();
    }
    /**
     * Method to illuminate each matrix button in the specified
     * row and column.
     * @author James Badham 
     * @param i
     * @param j 
     */
    public void rowAndColumn(int i, int j) {
        super.getView().illuminate_row(i, true);
        super.getView().illuminate_column(j, true);
    }

    /**
     * Overridden okButtonClicked method
     * removes listeners and clears matrix
     * buttons when the OK button is clicked
     * and performance mode is entered.
     * @author James Badham
     */
    @Override
    public void okButtonClicked() {
	super.removeListeners();
	super.getView().resetIllumination();
		
	try {
			super.getController().setPerf();
	} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	}
    }
		

    /**
     * Overridden matrixButtonClicked method
     * sets the loop speed to the bpm relating
     * to the position of the selected matrix
     * button.
     * @author James Badham
     * @param mB
     * @throws InvalidMidiDataException
     * @throws MidiUnavailableException
     */
    @Override
    public void matrixButtonClicked(Object mB) throws InvalidMidiDataException, MidiUnavailableException {
        MatrixButton button = (MatrixButton) mB;
        int xcoord = button.x;
        int ycoord = button.y;
        int bpm = xcoord +(ycoord*16);
        rowAndColumn(ycoord, xcoord);
        super.getPlayer().changeLoopSpeed(bpm);
        super.getView().LCDTextArea.setText(bpm + " BPM");
    }

	@Override
	public void onButtonClicked() throws InterruptedException {
		// TODO Auto-generated method stub
		
	}
    
    
    
}

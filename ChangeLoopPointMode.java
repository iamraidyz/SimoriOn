
package simoriOn;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import simoriOn.GUI.MatrixButton;

/**
 * Class to control the functionality of changing the loop point.
 * @author James Badham
 */
public class ChangeLoopPointMode extends Mode{
        
    /**
     * Change loop point mode constructor.
     * @author James Badham
     * @param player
     * @param view
     * @param controller 
     */
    public ChangeLoopPointMode(GUI view, SimoriOn controller) {
        super(view, controller);
    }
    
    /**
     * Overridden addListeners method
     * Adds all relevant event listeners.
     * @author James Badham
     */
    @Override
    public void addListeners() {
        System.out.println("Event listeners added.");
        super.onButtonControl();
	super.matrixButtonControl();
	super.okButtonControl();
    }

    @Override
    public void onButtonClicked() throws InterruptedException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
			e.printStackTrace();
	}
    }
    
    /**
     * Overridden matrixButtonClicked method
     * sets the loop point to the x coordinate
     * of the selected matrix button.
     * @author James Badham
     * @param mB
     * @throws InvalidMidiDataException
     * @throws MidiUnavailableException 
     */
    @Override
    public void matrixButtonClicked(Object mB) throws InvalidMidiDataException, MidiUnavailableException {
        MatrixButton button = (MatrixButton) mB;
        int column = button.x;
        super.getView().illuminate_column(column, true);
        super.getController().currentLayer.loopPoint = column;
    }
    
    
}
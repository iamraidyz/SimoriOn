package simoriOn;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import simoriOn.GUI.MatrixButton;

/**
 * @author Petr Vavruska
 * This class extends abstract Mode class and implements the actual Performance Mode and in the future will define behavior for all the buttons
 *  
 */
public class ChangeLayerMode extends Mode {
		int layerIndex = 0;
		int clicker =0;
        static boolean onOff;
        /**
    	 * @author Petr Vavruska
    	 * Constructor for OnOffMode. 
    	 * @param player - sound player to be controlled
    	 * @param view - GUI that own buttons and is modified in some cases
    	 * @param controller - controller for the SimoriOn, owns Mode attribute and switches between modes 
    	 */
	@SuppressWarnings("static-access")
	public ChangeLayerMode(GUI view, SimoriOn controller) {
		super(view, controller);
		// TODO Auto-generated constructor stub
        this.onOff = true;
	}

	/**
	 * @author Petr Vavruska
	 * This method add listeners to all implemented buttons.
	 */
	@Override
	public void addListeners() {
		super.onButtonControl();
		super.matrixButtonControl();
		super.okButtonControl();
	}


	/**
	 * @author Petr Vavruska
	 * This method adds behavior to ON Button - remove listeners from buttons in case device is turned off
	 * In case that device is turned on, controls are enabled and device is switched to Performance Mode
	 */
	@Override
	public void onButtonClicked() {
		super.removeListeners();
		super.getView().enableControls(false);
		super.getController().setOnOff();
		
        onOff = false;
        }

	/**
	 * @author iamraidyz Petr Vavruska
	 * This methods defines actions to be performed when ok button is clicked.
	 * Resets clicker flag, changes the current layer based on Y coordinate of
	 * matrix buttons pressed and switches back to performance mode.
	 */
	@SuppressWarnings("static-access")
	@Override
	public void okButtonClicked() {
		//TODO behavior
		//set controller.currentLayer to Y coordinate
		clicker=0;
		
		if(super.getController().layers[layerIndex]==null){
			try {
				super.getController().layers[layerIndex]=new Layer(layerIndex,super.getController());
			} catch (MidiUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidMidiDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		super.getController().currentLayer=super.getController().layers[layerIndex];
		super.getController().layers[layerIndex].active=true;
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
	 * @author Petr Vavruska
	 * This method is used to choose layer to be operated by simoriOn device.	 * 
	 * When button is clicked, the whole row is illuminated and index of selected layer presented on the LCD.
	 */
	@Override
	public void matrixButtonClicked(Object o) throws InvalidMidiDataException, MidiUnavailableException {
	MatrixButton mB=(MatrixButton) o;
	
	//clicker - clearing matrix when changing mind and selecting different layer before pressing ok
	clicker++;	
	super.getView().illuminate_row(mB.y, true);
	
	//de-illuminates previously selected row
	if(clicker!=1) super.getView().illuminate_row(15-layerIndex, false);
		System.out.println(clicker+"clicker result :"+clicker%2);
	
		layerIndex = 15 - mB.y;

	super.getView().LCDTextArea.setText(new Integer(layerIndex).toString());
	}
	
	
}
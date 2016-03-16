package simoriOn;

import java.awt.event.ActionEvent;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import java.awt.event.ActionListener;


/**
 * @author Petr Vavruska
 * This class extends abstract Mode class and implements the actual OnOff Mode and defines behavior for ON buttons, which is the only active one in this mode
 *  
 */
public class OnOffMode extends Mode {
	//actionListener for the ON Button
	private ActionListener onButtonListener;

	/**
	 * @author Petr Vavruska
	 * Constructor for OnOffMode. 
	 * @param player - sound player to be controlled
	 * @param view - GUI that own buttons and is modified in some cases
	 * @param controller - controller for the SimoriOn, owns Mode attribute and switches between modes 
	 */
	public OnOffMode(GUI view, SimoriOn controller) {
		super(view, controller);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @author Petr Vavruska
	 * This method add listener to ON Button, which is the only one active in OnOff Mode 
	 */
	public void addListeners(){
		System.out.println("added");
		
		//add onButtonClicked as behavior for On Button clicked event 
		onButtonListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {                  
				try {
					onButtonClicked();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}}};
				
				super.getView().getOnButton().addActionListener(onButtonListener);
	}
	
	/**
	 * @author Petr Vavruska
	 * This method adds behavior to ON Button - remove listeners from buttons in case device is turned off
	 * In case that device is turned on, controls are enabled and device is switched to Performance Mode
	 */
	@Override
	public void onButtonClicked() throws InterruptedException {
		super.removeListeners();
		super.getView().enableControls(true);
		super.getController().setPerf();

	}


	@Override
	public void okButtonClicked() {
		// No behavior in OnOFF Mode
		
	}

	@Override
	public void matrixButtonClicked(Object mB) throws InvalidMidiDataException, MidiUnavailableException {
		// No behavior in OnOFF Mode
		
	}


}
package simoriOn;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import simoriOn.GUI.MatrixButton;

/**
 * @author Petr Vavruska
 * This class extends abstract Mode class and implements the actual Performance Mode and in the future will define behavior for all the buttons
 *  
 */
public class PerformanceMode extends Mode {

    
		private ActionListener l1Listener;
		private ActionListener l2Listener;
		private ActionListener l3Listener;
		private ActionListener l4Listener;
		
		private ActionListener r1Listener;
		//private ActionListener r2Listener;
		//private ActionListener r3Listener;
		//private ActionListener r4Listener;
        static boolean onOff;
        /**
    	 * @author Petr Vavruska
    	 * Constructor for OnOffMode. 
    	 * @param player - sound player to be controlled
    	 * @param view - GUI that own buttons and is modified in some cases
    	 * @param controller - controller for the SimoriOn, owns Mode attribute and switches between modes 
    	 */
	@SuppressWarnings("static-access")
	public PerformanceMode(GUI view, SimoriOn controller) {
		super(view, controller);
		// TODO Auto-generated constructor stub
        this.onOff = true;
	}

	/**
	 * @author Petr Vavruska
	 * This method add listeners to all implemented buttons
	 */
	@Override
	public void addListeners() {
		super.onButtonControl();
		super.matrixButtonControl();
		super.okButtonControl();
		sideButtonsControl();
	}


	/**
	 * @author Petr Vavruska
	 * This method adds behavior to ON Button - remove listeners from buttons in case device is turned off
	 * In case that device is turned on, controls are enabled and device is switched to Performance Mode
	 */
	@Override
	public void onButtonClicked() {
		super.removeListeners();
		removeSideButtonsListeners();
		super.getView().enableControls(false);
		super.getController().setOnOff();
        onOff = false;
        }

	
	@Override
	public void okButtonClicked() {
		//No behavior in Performance Mode
	}

	/**
	 * @author Petr Vavruska
	 * This methods adds behavior to Matrix buttons 
	 * 
	 * When button is clicked, it's illuminated and corresponding midi event is added to the track.
	 * When already set button is clicked, it's de-illuminated and corresponding midi event is removed.
	 */
	@SuppressWarnings("static-access")
	@Override
	public void matrixButtonClicked(Object o) throws InvalidMidiDataException, MidiUnavailableException {
	MatrixButton mB=(MatrixButton) o;
	if(mB.illuminated==false)
	{
		super.getPlayer().addToTrack(mB.y,90,mB.x);
		super.getController().currentLayer.matrix[mB.x][mB.y]=true;
		System.out.println("illuminating :"+mB.x+" :"+mB.y);
		mB.illuminate(true);
		mB.hasTone=true;
		mB.indexS=mB.countS;
		mB.countS+=2;}
	else{
		super.getController().currentLayer.matrix[mB.x][mB.y]=false;
		super.getPlayer().removeFromTrack(mB.indexS);
		mB.illuminate(false);
		mB.hasTone=false;
    	mB.indexS=-2;
    	mB.countS-=2;
	}
}
	
	/**
	 * Remove behavior (listeners) from side control buttons
	 * @author Petr Vavruska
	 */
	public void removeSideButtonsListeners(){
		super.getView().getL1().removeActionListener(l1Listener);
		super.getView().getL2().removeActionListener(l2Listener);
		super.getView().getL3().removeActionListener(l3Listener);
		super.getView().getL4().removeActionListener(l4Listener);
		
		super.getView().getR1().removeActionListener(l4Listener);
	}
	

	/**
	 * @throws InterruptedException
	 * defines behavior for L1 button click - switch to ChangeVoiceMode
	 */
	public void L1ButtonClicked() throws InterruptedException {
		System.out.println("switching to Change Voice Mode");
		super.removeListeners();
		removeSideButtonsListeners();
		super.getController().setChangeVoiceMode();
	}
	
	

	/**
	 * @throws InterruptedException
	 * defines behavior for L2 button click - switch to ChangeVelocityMode
	 */
	public void L2ButtonClicked() throws InterruptedException {
		System.out.println("switching to L2 - change velocity");
		super.removeListeners();
		removeSideButtonsListeners();
		super.getController().setChangeVelocityMode();
	}


	/**
	 * @throws InterruptedException
	 * defines behavior for L3 button click - switch to ChangeLoopSpeedMode
	 */
	public void L3ButtonClicked() throws InterruptedException {
		System.out.println("switching to L3 - change loop speed");
		super.removeListeners();
		removeSideButtonsListeners();
		super.getController().setChangeLoopSpeedMode();
	}
	
	/**
         * @author James Badham
         * @throws java.lang.InterruptedException
         */
        public void L4ButtonClicked() throws InterruptedException {
            System.out.println("switching to L4 - change loop point");
            super.removeListeners();
            removeSideButtonsListeners();
            super.getController().setChangeLoopPointMode();
        }
	
	/**
	 * author Petr Vavruska
	 * @throws InterruptedException
	 * define behavior for R1 button click - switch to ChangeLayerMode
	 */
	public void R1ButtonClicked() throws InterruptedException {
		System.out.println("switching to R1 - change layer");
		super.removeListeners();
		removeSideButtonsListeners();
		super.getController().setChangeLayerMode();
	}


	/**
	 * This method adds behavior to side buttons (L1-4 & R1-4)
	 * @author Petr Vavruska
	 */
	public void sideButtonsControl()
	{
		System.out.println("adding side buttons control");
		
		//L1 button behavior
		l1Listener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {                  
            	try {
					L1ButtonClicked();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
      };                
      super.getView().getL1().addActionListener(l1Listener); 

		//L2 button behavior
      l2Listener = new ActionListener() {
          public void actionPerformed(ActionEvent actionEvent) {                  
          	try {
					L2ButtonClicked();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
          }
    };                
    super.getView().getL2().addActionListener(l2Listener);
    
	//L3 button behavior
    l3Listener = new ActionListener() {
        public void actionPerformed(ActionEvent actionEvent) {                  
        	try {
					L3ButtonClicked();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        }
  };
  super.getView().getL3().addActionListener(l3Listener); 
  
      l4Listener = new ActionListener() {
        public void actionPerformed(ActionEvent actionEvent) {                  
        	try {
					L4ButtonClicked();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        }
  };
  super.getView().getL4().addActionListener(l4Listener);
  
	//R1 button behavior
  r1Listener = new ActionListener() {
      public void actionPerformed(ActionEvent actionEvent) {                  
      	try {
					R1ButtonClicked();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
      }
};
super.getView().getR1().addActionListener(r1Listener);
	
	}
	
}
package simoriOn;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import simoriOn.GUI.MatrixButton;

/**
* This class will control functionality for the changeVoiceMode
* @author Matt Hennigan
* version 1.0
*/

public class ChangeVoiceMode extends Mode {

  
  /**
   * Change loop speed mode constructor.
   * @author Matt Hennigan
   * @param player
   * @param view
   * @param controller 
   */
    public ChangeVoiceMode(GUI view, SimoriOn controller) {
      super(view, controller);
    }

    /**
     * Overriden addListeners method.
     * Adds relevant event listeners.
     * @author Matt Hennigan
     */
    @Override
    public void addListeners() {
      super.onButtonControl();
      super.matrixButtonControl();
      super.okButtonControl();
        }
      
    /**
     * Overriden onButtonClicked method.
     * @author Matt Hennigan
     */
    @Override
    public void onButtonClicked() throws InterruptedException {
      // TODO Auto-generated method stub
      
    }

    /**
     * Overriden okButtonClicked method.
     * Removes listeners and clears matrixbuttons.
     * @author Matt Hennigan
     */
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
    
    /**
     * Overriden matrixButtonClicked method.
     * Sets the instrument to correspond to the index in the MatrixButtons.
     * @author Matt Hennigan
     * @param o
     * @throws InvalidMidiDataException
     * @throws MidiUnavailableException
     */
    @Override
    public void matrixButtonClicked(Object o) throws InvalidMidiDataException,
        MidiUnavailableException {
      System.out.print("Changing instrument voice.");
      MatrixButton mB =(MatrixButton) o;
      // Illuminate the corresponding row and column.
      int xCoord = mB.x;
      int yCoord = mB.y;
      illuminateRowAndColumn(yCoord, xCoord);
      
      int instrumentNumber = mB.x * mB.y;
      String instrumentName = super.getPlayer().setInstrument(instrumentNumber);
      // Print the instrument name to the display.
      super.getView().LCDTextArea.setText("Instrument:" + instrumentName);
    }
}
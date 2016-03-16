package simoriOn;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

/**
* First version of the SimoriOn class.
* This class is the "main" class and controller of the Simorion
* @author Petr Vavruska, James Badham, Matt Hennigan
* version 1.0
*/


public class SimoriOn {
	GUI view;
	Layer[] layers = new Layer[16];
	Layer currentLayer;
	boolean lock = false;
	
	Mode mode;
	Mode onOffMode;
	Mode performanceMode;
	Mode changeVelocityMode;
	Mode changeLoopSpeedMode;
	Mode changeLoopPointMode;
	Mode changeLayerMode;
	Mode changeVoiceMode;

	
	
	/**
	*  Constructor for SimoriOn class
	* @author Petr Vavruska
	* @param view - graphical interface to be controlled
	* @param player - player to be controlled
	 * @throws InvalidMidiDataException 
	 * @throws MidiUnavailableException 
	*/
	public SimoriOn(GUI view) throws MidiUnavailableException, InvalidMidiDataException{
	
	this.view=view;
	
	//create graphical interface
	view.createGUI();
	
	//create simoriOn modes
	this.onOffMode = new OnOffMode(view, this);
	this.performanceMode = new PerformanceMode(view,this);
	this.changeVelocityMode = new ChangeVelocityMode(view, this);
	this.changeLayerMode = new ChangeLayerMode(view,this);
	this.changeLoopSpeedMode = new ChangeLoopSpeedMode(view, this);
	this.changeLoopPointMode = new ChangeLoopPointMode(view, this);
	this.changeVoiceMode = new ChangeVoiceMode(view, this);
	
	System.out.println("settin layers");

		layers[0]= new Layer(0,this);
	
	layers[0].active=true;
	
	}
	
	/**
	 *@author Petr Vavruska
	 *	This method calls addListeners method of currently active Mode 
	 * 
	 */
	public void addModeListeners(){
		this.mode.addListeners();
	}
	
	public void stopThreads(){
		for(int i=0;i<16;i++){
		if(layers[i]!=null && layers[i].active==true){
			layers[i].player.playing=false;
			layers[i].looper.interrupt();	
		}
		}
    	currentLayer.clockhand.playing=false;
		currentLayer.chand.interrupt();
	}
	
	/**
	 * @author Petr Vavruska
	 * This method switches simoriOn to OnOff Mode, stops the sound loop and adds listener to On Button
	 */
	public void setOnOff(){
		currentLayer=layers[0];
		this.mode.addListeners();
		if(!(this.mode instanceof OnOffMode)){
		stopThreads();}
		this.mode=this.onOffMode;
	}
	
	/**
	 * @author Petr Vavruska
	 * @return - this function returns currently active Mode of the simoriOn
	 */
	public Mode getMode(){
		return this.mode;
	}
	
	public void startLayerThreads(){
		for(int i=0;i<16;i++){
			if(layers[i]!=null && layers[i].active==true){
				System.out.println("layer "+i+"is active: "+layers[i].active);
				layers[i].player.playing=true;
				layers[i].looper= new Thread((Runnable) layers[i].player);
				layers[i].looper.start();}	
		}
		currentLayer.chand= new Thread((Runnable) currentLayer.clockhand);
		currentLayer.chand.start();		
	}
	
	
	/**
	 * @author Petr Vavruska
	 * This method switches simoriOn to Performance Mode,
	 * it adds listeners to buttons and starts new thread for the sound-loop
	 */
	public void setPerf() throws InterruptedException{
		startLayerThreads();
		if(!(this.mode instanceof OnOffMode))
			{this.view.repaintMatrix(currentLayer.matrix);}
		
		currentLayer.clockhand.playing=true;				
		
		this.mode=this.performanceMode;
		this.mode.addListeners();
                
        }
        
		/**
		 * This method switches simoriOn to Change Velocity Mode.
		 * @throws InterruptedException
		 */
        public void setChangeVelocityMode() throws InterruptedException{
        	stopThreads();
        	//this stops looper and clockhand and reset matrix
        	view.resetIllumination();
        	
    		
        	this.mode=this.changeVelocityMode;
        	this.mode.addListeners();
        	// TODO
        	
        }
        
        /**
         * This method switches simoriOn to Change Layer Mode.
         * Resets illumination and adds new listeners, also sets flags for player a clockHand threads.
         * @throws InterruptedException
         * @author Petr Vavruska
         */
        public void setChangeLayerMode() throws InterruptedException{
        	stopThreads();
        	view.resetIllumination();
    		this.mode=this.changeLayerMode;
    		this.mode.addListeners();
    		
            }
        
        /**
		 * This method switches simoriOn to Change Loop Speed Mode.
		 * @throws InterruptedException
		 */
        public void setChangeLoopSpeedMode() throws InterruptedException{
        	stopThreads();
           this.mode=this.changeLoopSpeedMode;
           this.mode.addListeners();
           //this stops looper and clockhand and reset matrix
       	    view.resetIllumination();
       	    
          
       }

        /**
         * Method to change the current mode to Change Loop Point Mode.
         * @author James Badham 
         * @throws InterruptedException 
         */
        public void setChangeLoopPointMode() throws InterruptedException{
        	stopThreads();
           this.mode=this.changeLoopPointMode;
           this.mode.addListeners();
       	   //this stops looper and clockhand and reset matrix
       	    view.resetIllumination();
       	 
       }
       
       /**
         * Method to change the current mode to Change Voice Mode.
         * @author Matt Hennigan 
         * @throws InterruptedException 
         */
        public void setChangeVoiceMode() throws InterruptedException{
           this.mode=this.changeVoiceMode;
           this.mode.addListeners();
           stopThreads();
           view.resetIllumination();
          
       }
       
        
	
	/**
	 * @author Petr Vavruska
	* 	Main method for SimoriOn class
	* 	Instantiates a SimoriOn, creates the GUI and enables use of sound effects.
	* 	also switched SimoriOn to OnOff Mode
	* @param args
	* @throws javax.sound.midi.MidiUnavailableException
	*  @throws InvalidMidiDataException 
	*/
	public static void main (String[] args) throws MidiUnavailableException, InvalidMidiDataException 
	{
		GUI view = new GUI();
		SimoriOn simoriOn = new SimoriOn(view);
		//currentLayer.player.setSimorion(simoriOn);
		//enter onOffMode
		simoriOn.mode = simoriOn.onOffMode;
		simoriOn.setOnOff();
		
	}

}
package simoriOn;

import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Track;

/**
* Second version of the Player class.
* This class will handle all the sound effects for Simorion
* @implements Runnable - this class is runnable and has Run() method to play sounds on new thread.
* @author Petr Vavruska, Matt Hennigan, James Badham
* version 1.0
*/



public class Player implements Runnable{
	Layer currentLayer;
	int channel;
	int eventCount=0;
	Sequencer sequencer;
	Sequence seq;
	boolean playing=false;
	Instrument[] instruments;
	SimoriOn controller;
	Track track;
	
	
	/**
	* @author Petr Vavruska, Matt Hennigan
	* Constructor for Player class takes no arguments
	* @throws MidiUnavailableException - this class makes use of MidiSystem and Synthesizer
	*/	
	public Player(Layer layer)throws MidiUnavailableException,InvalidMidiDataException{
		this.currentLayer=layer;
		//Sequencer plays sequences created by simorion
		sequencer = MidiSystem.getSequencer();
		//Sequencer seq is where created tracks are stored
		 seq = new Sequence(Sequence.PPQ,1);

		// Create synthesiser and import soundbanks for instruments.
		 Synthesizer s = MidiSystem.getSynthesizer();
		 Soundbank soundbank = s.getDefaultSoundbank();
		 s.loadAllInstruments(soundbank);
		 instruments = soundbank.getInstruments();
		
		track = seq.createTrack();
		
	}	
	
	/**
	 * This method sets the instrument by taking it's index and returns the instruments name as a string.
	 * @author Matt Hennigan
   	* @param InstrumentNumber - specifies the instrument.
   	* @throws InvalidMidiDataException
	 */
	 public String setInstrument(int InstrumentNumber) throws InvalidMidiDataException{
	   // Ensures instrument numbers will not exceed the length given by numInstruments.
	   InstrumentNumber = InstrumentNumber%instruments.length;
	   ShortMessage instrumentChange = new ShortMessage();
	   instrumentChange.setMessage(ShortMessage.PROGRAM_CHANGE, channel, InstrumentNumber, 0);
	   this.track.add(new MidiEvent(instrumentChange,0));
	   Instrument instrument = instruments[InstrumentNumber];
	   //Return the instrument's name.
	   return instrument.getName();
	 	}
	
	
	/**
	 * This method takes an index and removes midi event from the corresponding time track
	 * @author Petr Vavruska
	 * @param index - specifies which event should be removed
	 * @throws InvalidMidiDataException
	 * @throws MidiUnavailableException
	 */
	public void removeFromTrack(int index)throws InvalidMidiDataException, MidiUnavailableException{
		System.out.println("Removing from track, Index: "+index);
		this.track.remove(this.track.get(index));
		this.track.remove(this.track.get(index));
	}
	
	/**
	 * This methods takes a velocity value as an argument and changes the velocity of the instruments to match.
	 * @author Matt Hennigan
	 * @param velocity - Specifies the velocity of the tone.
	 * @throws InvalidMidiDataException
	 */
	public void changeVelocity(int velocity) throws InvalidMidiDataException {
		currentLayer.velocity=velocity;
		Track track = this.track;
		
		int numTracks = track.size();
		for (int eventIndex = 0; eventIndex < numTracks; eventIndex++) {
			// ShortMessage message = (ShortMessage) track.get(eventIndex).getMessage();
			// message.setMessage(channel, message.getData1(), velocity);
		}
	}
	
	
        /**
         * This method encapsulates the setTempoInBPM method from
         * the Sequencer class, for use in the ChangeLoopSpeedMode class.
         * @author James Badham 
         * @param bpm 
         */
        public void changeLoopSpeed(int bpm) {
            sequencer.setTempoInBPM(bpm);
        }
	
	/**
	 * This methods takes several parameters and adds new midi event to the track.
	 * 
	 * @author Petr Vavruska
	 * @param pitch - specifies pitch of the tone - corresponds to Y coordinate of matrixButton pressed
	 * @param velocity - set to 90 by default for now, but will specify velocity of the tone in the future
	 * @param time - timely specifies where(when) in the track this tone should be played
	 * @throws InvalidMidiDataException
	 * @throws MidiUnavailableException
	 */
	public void addToTrack(int pitch,int velocity, long time) throws InvalidMidiDataException, MidiUnavailableException{
		System.out.println("ADDED :" + pitch + " , " + velocity + " , time: " + time);
		pitch=127-pitch*8;
		
		//create new midi message to add NOTE_ON event to the track
		ShortMessage message = new ShortMessage(ShortMessage.NOTE_ON,channel,pitch,velocity);
		ShortMessage messageOff = new ShortMessage(ShortMessage.NOTE_OFF,channel,pitch,velocity);
		
		//increments total number of events in the track
		this.eventCount+=2;
		MidiEvent event = new MidiEvent(message,time);
		MidiEvent event2 = new MidiEvent(messageOff,time+1);
		
		//adds NOTE_ON and NOTE_OFF events
		this.track.add(event);
		this.track.add(event2);
		
	}
	
	
	/**
	 *  This methods plays a sequence for non-specified amount of time.
	 *  Sound loop stops once the thread is interrupted.
	 *
	 * @author Petr Vavruska, James Badham
	 * @throws MidiUnavailableException
	 * @throws InvalidMidiDataException
	 * @throws InterruptedException
	 */
	public void startSequence() throws MidiUnavailableException, InvalidMidiDataException, InterruptedException{
		while(playing){
			long startTime = System.nanoTime();
			//System.out.println("Seq start+tempo: "+sequencer.getTempoInBPM());
		sequencer.open();
		sequencer.setTempoInBPM(currentLayer.tempo);
		sequencer.setSequence(seq);
		sequencer.start();
		synchronized(this){
		this.notifyAll();}
		long estimatedTime = System.nanoTime() - startTime;
		//System.out.println(estimatedTime);
		//sleeps thread for amount of time that equals to length of one loop, so all the sounds are played.
		//System.out.println("Sleeping player for: "+60/sequencer.getTempoInBPM()*currentLayer.loopPoint*1000+"looppoint: "+currentLayer.loopPoint);
		Thread.sleep((long) (60/sequencer.getTempoInBPM()*currentLayer.loopPoint)*1000);
	
		sequencer.stop();
		sequencer.close();
		}
	}
	
	/**
	 * This method changes the layer, which simoriOn is currently operating with.
	 * @param nextLayer - layer to be set as new currentLayer
	 */
	public void changeCurrentLayer(Layer nextLayer){
		this.currentLayer = nextLayer;
		this.channel=currentLayer.index;
	}
	
	public void setSimorion(SimoriOn controller){
		this.controller=controller;
	}
	
	
	
	/**
	 * This methods starts a new thread, which does the looping - plays sounds until the thread is interrupted.
	 * @author Petr Vavruska
	 */
	@Override
	public void run() {
		try {
			startSequence();
		} catch (MidiUnavailableException | InvalidMidiDataException | InterruptedException e) {
			System.out.println("Thread interrupted");
		}
		
	}
			
}
package simoriOn;

import javax.sound.midi.Track;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Soundbank;

/**
 * 
 * @author Petr Vavruska
 * 
 * This class defines Layer object - this object represents
 * a single layer of SimoriOn and is defined by it's attributes.
 * Every layer is instantiated with some default parameters.
 */
public class Layer {
	
	Soundbank bank;
	
	int index;
	int instrument = 5; 
	int velocity = 90;
	int tempo = 120;
	int loopPoint = 16;
	boolean active = false;
	Player player;
	ClockHand clockhand;
	Thread looper;
	Thread chand;
	GUI view;
	SimoriOn controller;
	
	//This boolean matrix stores data about set matrix buttons
	//use: re-illumination after return back to performance mode 
	boolean[][] matrix = new boolean[16][16];



public Layer(int index, SimoriOn controller) throws MidiUnavailableException, InvalidMidiDataException
{
	this.controller=controller;
	this.view=controller.view;
	this.player = new Player(this);	
	this.index = index;
	this.clockhand=new ClockHand(controller,this);
}

/**
 * This method returns name of the instrument used in given layer. 
 * */
public String getInstrumentName(){
	return bank.getInstruments()[instrument].getName();
}






}

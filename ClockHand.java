package simoriOn;

/**
 * Class to control the functionality of the clock hand.
 * @author Petr Vavruska
 */	

public class ClockHand implements Runnable {
	Layer layer;
	GUI view;
	SimoriOn controller;
	boolean playing=false;
	double tempo;
	
	public ClockHand(SimoriOn controller,Layer layer) {
		this.controller=controller;
		this.view = controller.view;
		this.layer= layer;
	}


	@Override
	public void run() {
		System.out.println("Startin clockhand!");
		startClock();
		
	}
		
	
	public void changeTempo(double tempo){
		this.tempo =tempo;
	}
        
        /**
         * Method to start the clock hand.
         * @author Petr Vavruska
         * @throws InterruptedException 
         */
	private void startClock() {
		
		tempo=(double) layer.player.sequencer.getTempoInBPM();
		System.out.println(tempo+"tempo");
                double delay = (60/tempo)*1000;
		int i=0;
		long startTime = System.nanoTime();
		System.out.println(delay+"delay");
		while(playing){
			
			
			if(i==0){System.out.println("Illuminating clockhand");}
			view.illuminateClockHand(i, true);
				try {
					Thread.sleep((long) delay);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					System.out.println("CHand interrupted");
					return;
				}
				
				view.illuminateClockHand(i, false);
				i++;
				if(i==layer.loopPoint){i=0;
				
				synchronized(this.layer.player){
					try {
						System.out.println("Chand waiting");
						this.layer.player.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}System.out.println("Chand running");}
				
				
				}
				
				}
		
		
		
	}
	

}
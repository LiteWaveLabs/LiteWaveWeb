package de.niklasfauth.litewave.utils;

import java.util.concurrent.LinkedBlockingQueue;

public class QueueSharer {
		
	    private static LinkedBlockingQueue queue;
	    
	    public QueueSharer(LinkedBlockingQueue queue) {
	        this.queue = queue;
	    }
	    
	    public static LinkedBlockingQueue getQueue() {
	    	return queue;
	    }
}

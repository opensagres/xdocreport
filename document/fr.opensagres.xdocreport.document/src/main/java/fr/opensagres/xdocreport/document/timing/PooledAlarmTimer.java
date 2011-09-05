/**
 * Copyright (C) 2011 Angelo Zerr <angelo.zerr@gmail.com> and Pascal Leclercq <pascal.leclercq@gmail.com>
 *
 * All rights reserved.
 *
 * Permission is hereby granted, free  of charge, to any person obtaining
 * a  copy  of this  software  and  associated  documentation files  (the
 * "Software"), to  deal in  the Software without  restriction, including
 * without limitation  the rights to  use, copy, modify,  merge, publish,
 * distribute,  sublicense, and/or sell  copies of  the Software,  and to
 * permit persons to whom the Software  is furnished to do so, subject to
 * the following conditions:
 *
 * The  above  copyright  notice  and  this permission  notice  shall  be
 * included in all copies or substantial portions of the Software.
 *
 * THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
 * EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
 * MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package fr.opensagres.xdocreport.document.timing;

import java.util.ArrayList;
import java.util.List;

/**
 * An alarm timer for use in a pool of threads.
 * 
 * @author Lars J. Nilsson
 */
public final class PooledAlarmTimer implements AlarmTimer {

	private final List<AlarmTimerListener> listeners;
	private final long timeout;
	
	private final Object runMonitor = new Object();
	private boolean runFlag = true;
	
	public PooledAlarmTimer(long timeout) {
		listeners = new ArrayList<AlarmTimerListener>();
		this.timeout = timeout;
	}
	
	public void addAlarmTimerListener(AlarmTimerListener lstnr) {
		synchronized(listeners) {
			listeners.add(lstnr);
		}
	}
	
	public void removeAlarmTimerListener(AlarmTimerListener lstnr) {
		synchronized(listeners) {
			listeners.remove(lstnr);
		}
	}

	public long getTimeout() {
		return timeout;
	}

	public boolean isActive() {
		synchronized(runMonitor) {
			return runFlag;
		}
	}

	public void stop() {
		synchronized(runMonitor) {
			runFlag = false;
			runMonitor.notify();
		}
	}

	public void run() {
		/*
		 * A short comment here, it is possible for Object.wait to 
		 * return before the timeout silently, thus we need to double check
		 * the actual elapsed time before calling it quits. /LJN
		 */
		while(isActive()) {
			try {
				
				doWait(timeout);
			} catch(InterruptedException e) {
				// Question: At this point, the thread pool is most 
				// likely shutting down, but I'm not entirely sure, does it
				// mean we should or shouldn't notify waiters? If we shouldn't
				// we should return from this method to kill the thread
				
				// return;
			}

			if ( isActive() )
				notifyListeners();
		}
	}

	// --- PRIVATE METHODS --- //
	
	/*
	 * Notify all listeners
	 */
	private void notifyListeners() {
		List<AlarmTimerListener> tempList = new ArrayList<AlarmTimerListener>(listeners);
		for (AlarmTimerListener alarmTimerListener : tempList) {
			alarmTimerListener.alarm(this);
		}
	}

	/*
	 * Wait on run monitor...
	 */
	private void doWait(long millis) throws InterruptedException {
		synchronized(runMonitor) {
			
			runMonitor.wait(millis);
		}
	}
}

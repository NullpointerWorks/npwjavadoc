package com.nullpointerworks.scanner;

/**
 * 
 * @author Michiel
 *
 */
public interface ScanTestInterface extends Runnable , ScanTestInterface2 
{
	/**
	 * valid commentary
	 * @return
	 */
	int getWidth();
	
	// no valid commentary
	int getHeight();
}

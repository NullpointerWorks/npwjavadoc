package com.nullpointerworks.scanner;

/**
 * 
 * @author Michiel
 *
 */
public interface ScanTestInterface 
{
	/**
	 * valid commentary
	 * @return
	 */
	int getWidth();
	
	// no valid commentary
	int getHeight();
	
	/**
	 * a default method
	 * @return
	 */
	default int getDepth()
	{
		return 0;
	}
	
	
}

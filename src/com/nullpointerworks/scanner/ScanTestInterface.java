package com.nullpointerworks.scanner;

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

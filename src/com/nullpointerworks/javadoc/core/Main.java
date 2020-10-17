package com.nullpointerworks.javadoc.core;

public class Main
{
	public static void main(String[] args) {new Main(args);}
	
	
	public Main(String[] args)
	{
		String core = 
			"D:/Development/Web/workspaces/git/NullpointerWorksAPI/core/";
		
		/*
		 * build version 1.0.0
		 */
		ModuleCore.build(core); 			// module	libnpw.core
		PackageCore.build(core); 			// package	com.nullpointerworks.core
			DrawCanvas.build(core);		// done
			Monitor.build(core);		// done
			PanelCanvas.build(core);	// done
			Window.build(core);			// done
			WindowMode.build(core);		// done
		
		PackageCoreBuffer.build(core);		// package	com.nullpointerworks.core.buffer
		
		PackageCoreBufferCon.build(core);	// package	com.nullpointerworks.core.buffer.concurrency
		
		PackageCoreInput.build(core);		// package	com.nullpointerworks.core.input
			Key.build(core);		// todo
		
		
		
		
		
		
		
	}
}

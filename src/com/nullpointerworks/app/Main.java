package com.nullpointerworks.app;

import com.nullpointerworks.javadoc.core.DrawCanvas;
import com.nullpointerworks.javadoc.core.Key;
import com.nullpointerworks.javadoc.core.ModuleCore;
import com.nullpointerworks.javadoc.core.Monitor;
import com.nullpointerworks.javadoc.core.PackageCore;
import com.nullpointerworks.javadoc.core.PackageCoreBuffer;
import com.nullpointerworks.javadoc.core.PackageCoreBufferCon;
import com.nullpointerworks.javadoc.core.PackageCoreInput;
import com.nullpointerworks.javadoc.core.PanelCanvas;
import com.nullpointerworks.javadoc.core.Window;
import com.nullpointerworks.javadoc.core.WindowMode;

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

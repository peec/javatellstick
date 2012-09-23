package pkj.no.gui.panel;

import org.eclipse.swt.widgets.*;


public abstract class Panel {

	protected Shell shell;
	
	
	public Panel(Shell shell){
		this.shell = shell;
	}
	
	
	public Shell getShell(){
		return shell;
	}
	
}

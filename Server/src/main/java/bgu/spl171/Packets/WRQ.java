package bgu.spl171.Packets;

import java.io.File;

import bgu.spl171.net.api.bidi.ConnectionsImp;
import bgu.spl171.net.srv.BlockingConnectionHandler;
import bgu.spl171.net.srv.NonBlockingConnectionHandler;

public class WRQ extends Packet{

	final int opCode=2;
	final String name;
	
	public WRQ(String name) {
		this.name=name;
	}

	public String getName(){
		return name;
	}
	
	public int getOpCode() {
		return opCode;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public <T> void operate(ConnectionsImp c, int connId) {
		File folder = new File("Files"+File.separator);
		File[] listOfFiles = folder.listFiles();
		boolean found=false;
		for (int i = 0; i < listOfFiles.length; i++) {				// Maybe we dont need Files and only dircs.
			if (listOfFiles[i].getName().equals(name)){
				ERROR e=new ERROR(5);
				e.operate(c, connId);
				found=true;
				break;
			}
		}
		if (!found){
			ACK a= new ACK(0);
			a.operate(c, connId);
			if (c.getMyHashy().get(connId) instanceof BlockingConnectionHandler){
				BlockingConnectionHandler bct= (BlockingConnectionHandler) c.getMyHashy().get(connId);
				bct.getProt().setDataName(name);
			} else {
				NonBlockingConnectionHandler nbct= (NonBlockingConnectionHandler) c.getMyHashy().get(connId);
				nbct.getProt().setDataName(name);
			}
		}
	}
}

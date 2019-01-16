package bgu.spl171.Packets;

import java.io.File;

import bgu.spl171.net.api.bidi.ConnectionsImp;

public class DELRQ extends Packet{

	final int opCode=8;
	String name;
	
	public DELRQ( String name) {
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
	public void operate(ConnectionsImp c, int connId) {
	    File file= new File("Files"+File.separator+""+name);
		if (file.delete()){
			ACK a=new ACK(0);
			a.operate(c, connId);
			BCAST bc= new BCAST(name, 0);
			bc.operate(c, connId);
		}else{
			ERROR e=new ERROR(1);
			e.operate(c, connId);
		}
	}

}

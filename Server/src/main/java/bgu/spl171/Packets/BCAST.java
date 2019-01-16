package bgu.spl171.Packets;

import bgu.spl171.net.api.bidi.ConnectionsImp;

public class BCAST <T> extends Packet{
	
	final int opCode=9;
	int add;			//1 for added, 0 removed
	String name;
	
	public BCAST(String name, int a) {
		this.name=name;
		this.add=a;
	}

	public int getOpCode() {
		return opCode;
	}
	public String getBcastName(){
		return name;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void operate(ConnectionsImp c, int connId) {
			c.broadcast(this);
	}

	public int getaddNum() {
		return add;
	}

	
}

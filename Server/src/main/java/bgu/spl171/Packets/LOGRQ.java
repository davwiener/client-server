package bgu.spl171.Packets;

import bgu.spl171.net.api.bidi.ConnectionsImp;
import bgu.spl171.net.srv.BlockingConnectionHandler;
import bgu.spl171.net.srv.ConnectionHandler;
import bgu.spl171.net.srv.NonBlockingConnectionHandler;

public class LOGRQ extends Packet{

	final int opCode=7;
	String name;
	boolean nameExist=false;
	
	public LOGRQ(String name) {
		this.name=name;
	}

	public int getOpCode() {
		return opCode;
	}
	
	public String getName(){
		return name;
	}
	
	@SuppressWarnings({ "rawtypes" })
	@Override
	public <T> void operate(ConnectionsImp c, int connId) {
		for (int i=0; i<c.getMyHashy().mappingCount(); i++){
			ConnectionHandler ch= (ConnectionHandler) c.getMyHashy().get(i);
			if (ch instanceof BlockingConnectionHandler && ch!=null){
				BlockingConnectionHandler bch= (BlockingConnectionHandler) ch;
				String name1=bch.getName();
				if (name1.equals(name)){
					nameExist=true;
					break;
				}
			}else {
				if (ch!=null){
					NonBlockingConnectionHandler nbch= (NonBlockingConnectionHandler) ch;
					String name1=nbch.getName();
					if (name1!=null){
						if (name1.equals(name)){
							nameExist=true;
							break;
						}
					}
				}
			}
		}
		// Its only for TPC, Need NONBLOCKING for Reactor
		if (nameExist){
			ERROR e=new ERROR(7);
			e.operate(c, connId);
		} else {
			ConnectionHandler ch= (ConnectionHandler) c.getMyHashy().get(connId);
			if (ch instanceof BlockingConnectionHandler){
				BlockingConnectionHandler bch= (BlockingConnectionHandler) ch;
				bch.setName(name);
			}else {
				NonBlockingConnectionHandler bch= (NonBlockingConnectionHandler) ch;
				bch.setName(name);
			}
			ACK a=new ACK(0);
			a.operate(c, connId);
		}
	}

	public <T> boolean isExist(){
		return nameExist;
	}
}

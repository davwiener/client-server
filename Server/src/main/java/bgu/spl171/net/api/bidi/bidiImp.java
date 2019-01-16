package bgu.spl171.net.api.bidi;

import bgu.spl171.Packets.*;
import bgu.spl171.net.srv.BlockingConnectionHandler;
import bgu.spl171.net.srv.NonBlockingConnectionHandler;

public class bidiImp<T> implements BidiMessagingProtocol<T> {

	boolean logged=false;					// IT SHOULD BE FALSE
	int connectionId;
	@SuppressWarnings("rawtypes")
	ConnectionsImp connections;
	boolean shouldTer=false;
	T message;
	boolean canWriteData=false;
	byte[] data= new byte[] { };
	String currDataName="";
	boolean canSend=false;
	private int start;
	private int end;
	private int blocknum;
	
	@SuppressWarnings("rawtypes")
	@Override
	public void start(int connectionId, Connections<T> connections) {
		this.connectionId=connectionId;
		this.connections=(ConnectionsImp) connections;

	}
	
	public void setSend(boolean a){
		this.canSend=a;
	}
	
	@Override
	public void process(T message) {
		this.message=message;
		Packet p1= (Packet)message;
		
		if (p1.getOpCode()>10 ||p1.getOpCode()<1){
			Packet pE= new ERROR(4);				// Illegal TFTP - come second
			setSend(true);
			pE.operate(connections,connectionId);
		}
		if (logged)
		{
			if (p1 instanceof LOGRQ){
				Packet pE= new ERROR(7);
				setSend(true);
				pE.operate(connections,connectionId);
			}else
				if(p1 instanceof DISC){
					setSend(true);
					p1.operate(connections,connectionId);
					logged=false;
					shouldTer=true;
				}else if (p1 instanceof ACK){
					if (canSend){
						senddata();
					}
					else{
						setSend(true);
					}
					// we can continue send RRQ. our currCommand is RRQ.
				} else{
					setSend(true);
					p1.operate(connections,connectionId);
				}
		} 
		else
		{		// NOT LOGGED
			if (p1 instanceof LOGRQ){		// LOG REQ
				setSend(true);
				if (!((LOGRQ) p1).isExist()){
					logged=true;
					p1.operate(connections,connectionId);
				}
				else{						// Name EXIST
					Packet pE= new ERROR(6);
					setSend(true);
					pE.operate(connections,connectionId);
				}
			}
			else{							// NOT LOG REQ
					Packet pE= new ERROR(6);				// not illegal TFTP but not logged.
					setSend(true);
					pE.operate(connections,connectionId);
			}		
		}
		if (shouldTerminate()){
			connections.disconnect(connectionId); //david change that was a eror.
		}
	}
	@Override
	public boolean shouldTerminate() {
		return shouldTer;
	}

	@SuppressWarnings("unchecked")
	public Connections<T> getConns(){
		return connections;
	}
	@SuppressWarnings("rawtypes")
	public void setConns(Connections<T> connections){
		this.connections=(ConnectionsImp) connections;
	}
	
	public int getId(){
		return connectionId;
	}
	public T getMessage(){
		return message;
		 
	}
	public void setData (byte[] data){
		this.data=data;
	}
	public byte[] getData(){
		return data;
	}
	public void setDataName(String s){
		currDataName=s;
	}
	public String getDataName(){
		return currDataName;
	}

	public void setblocknum(int blocknum){
		this.blocknum=blocknum;
	}
	
	public boolean canIt() {
		return canSend;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void senddata() {
		boolean backtonormol=false;
		if (end-start>=512){			// IF ITS NOT LAST
			@SuppressWarnings("unused")
			byte[] newdata=new byte[512];
		}
		else						// IF ITS LAST
			backtonormol=true;
		byte[] newdata=new byte[end-start];
		for (int i=start; i<end; i++){
			newdata[i-start]=data[i];
		}
		DATA d1= new DATA(Math.min(512, end-start),blocknum,newdata);
		d1.setLastBlock(backtonormol);
		if (connections.getMyHashy().get(connectionId) instanceof BlockingConnectionHandler){
			BlockingConnectionHandler bch= (BlockingConnectionHandler) connections.getMyHashy().get(connectionId);
			bch.send(d1);
		}else {
			NonBlockingConnectionHandler bch= (NonBlockingConnectionHandler) connections.getMyHashy().get(connectionId);
			bch.send(d1);
		}
		if (backtonormol)
			setSend(false);
		if (!backtonormol)
			setSend(true);
		blocknum++;
		start=end;
		end =Math.min(start+512, data.length);
		if (backtonormol){
			data= new byte[] { };
			currDataName="";
			start=0;
		}
}
	public void setstartend(int start, int end) {
		this.start=start;
		this.end=end;
	}
	public void setLog(boolean b) {
		this.logged=b;
	}
}
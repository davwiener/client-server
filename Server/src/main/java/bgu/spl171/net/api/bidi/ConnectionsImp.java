package bgu.spl171.net.api.bidi;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import bgu.spl171.Packets.ERROR;
import bgu.spl171.net.srv.BlockingConnectionHandler;
import bgu.spl171.net.srv.ConnectionHandler;
import bgu.spl171.net.srv.NonBlockingConnectionHandler;

public class ConnectionsImp<T> implements Connections<T> {

	ConcurrentHashMap<Integer,ConnectionHandler<T>> myHashy=new ConcurrentHashMap<>();
	int currId;
	
	public ConnectionsImp(){
		this.myHashy=new ConcurrentHashMap<>();
		this.currId=0;
	}
	
	public ConcurrentHashMap<Integer, ConnectionHandler<T>> getMyHashy() {
		return myHashy;
	}

	public void setMyHashy(ConcurrentHashMap<Integer, ConnectionHandler<T>> myHashy) {
		this.myHashy = myHashy;
	}

	@SuppressWarnings("rawtypes")
	public boolean add (ConnectionHandler<T> ch){
		if (myHashy.containsValue(ch)){
			return false;
		}
		myHashy.put(currId, ch);
		BlockingConnectionHandler bch= (BlockingConnectionHandler) ch;				// everywhere we do this, we need change to (if instanceof blocking - blocking, else - nonblocking)
		bch.setId(currId);
		currId++;
		return true;
	}
	
	@Override
	public boolean send(int connectionId, T msg) {
		if (myHashy.containsKey(connectionId)){
			myHashy.get(connectionId).send(msg);
			return true;
		}
		else
			return false;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void broadcast(T msg) {
		Collection<ConnectionHandler<T>> c1= myHashy.values();
		for (ConnectionHandler item: c1) {
			item.send(msg);
		}
		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void disconnect(int connectionId) {
		if (myHashy.containsKey(connectionId)){
			try {
				if (myHashy.get(connectionId) instanceof BlockingConnectionHandler){
					BlockingConnectionHandler bct = (BlockingConnectionHandler) myHashy.get(connectionId);
					bct.getProt().setLog(false);
					myHashy.get(connectionId).close();
				}else {
					NonBlockingConnectionHandler bct = (NonBlockingConnectionHandler) myHashy.get(connectionId);
					bct.getProt().setLog(false);
					myHashy.get(connectionId).close();
				}
				myHashy.remove(connectionId);
				
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
		else
			send(connectionId, (T)new ERROR(0));
	}
	public int getId(){
		return currId ;
	}

	public void reactorsAdd(NonBlockingConnectionHandler<T> handler, BidiMessagingProtocol myBidi) {
        myHashy.put(currId,handler);
        myBidi.start(currId,this);
        currId++;
	}
}

package bgu.spl171.Packets;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import bgu.spl171.net.api.bidi.ConnectionsImp;
import bgu.spl171.net.api.bidi.bidiImp;
import bgu.spl171.net.srv.BlockingConnectionHandler;
import bgu.spl171.net.srv.NonBlockingConnectionHandler;

public class RRQ <T> extends Packet{

	final int opCode=1;
	String name;
	
	public RRQ(String name) {
		this.name=name;
	}
	
	public String getName(){
		return name;
	}
	public int getOpCode() {
		return opCode;
	}
	
	@SuppressWarnings({ "rawtypes", "unused" })
	@Override
	public void operate(ConnectionsImp c, int connId) {
		File folder = new File("Files"+File.separator);
		File[] listOfFiles = folder.listFiles();
		boolean found=false;
		if (c.getMyHashy().get(connId) instanceof BlockingConnectionHandler){
			BlockingConnectionHandler bct= (BlockingConnectionHandler) c.getMyHashy().get(connId);
			long size = 0;
			for (int i = 0; i < listOfFiles.length; i++) {				
				if (listOfFiles[i].getName().equals(name)){
					found=true;
					bct.getProt().setDataName(name);
					size=(long) listOfFiles[i].getTotalSpace();
					break;
				}
			}
			if (found){
				Path path = Paths.get(folder+File.separator+name);
				byte[] data=null;
				try {
					data = Files.readAllBytes(path);
				} catch (IOException e) {
					e.printStackTrace();
				}
				int datasize=data.length;
				int start=0;
				int end=Math.min(512, datasize);
				bidiImp protocol =bct.getProt();
				protocol.setData(data);
				protocol.setstartend(start,end);
				protocol.setblocknum(0);
				protocol.senddata();
				
			}else
			{
				ERROR e=new ERROR(1);
				e.operate(c, connId);
			}
		}else {
			NonBlockingConnectionHandler bct= (NonBlockingConnectionHandler) c.getMyHashy().get(connId);
			long size = 0;
			for (int i = 0; i < listOfFiles.length; i++) {				
				if (listOfFiles[i].getName().equals(name)){
					found=true;
					bct.getProt().setDataName(name);
					size=(long) listOfFiles[i].getTotalSpace();
					break;
				}
			}
			if (found){
				Path path = Paths.get(folder+File.separator+name);
				byte[] data=null;
				try {
					data = Files.readAllBytes(path);
				} catch (IOException e) {
					e.printStackTrace();
				}
				int datasize=data.length;
				int start=0;
				int end=Math.min(512, datasize);
				bidiImp protocol =bct.getProt();
				protocol.setData(data);
				protocol.setstartend(start,end);
				protocol.setblocknum(0);
				protocol.senddata();
				
			}else
			{
				ERROR e=new ERROR(1);
				e.operate(c, connId);
			}
		}
		
	
	}
	
	
}

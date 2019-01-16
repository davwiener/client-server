package bgu.spl171.Packets;

import java.io.File;
import bgu.spl171.net.api.bidi.ConnectionsImp;
import bgu.spl171.net.api.bidi.bidiImp;
import bgu.spl171.net.srv.BlockingConnectionHandler;
import bgu.spl171.net.srv.NonBlockingConnectionHandler;

public class DIRQ extends Packet{

	final int opCode=6;
	
	public DIRQ() {
	}

	public int getOpCode() {
		return opCode;
	}

	@SuppressWarnings({"rawtypes", "unused"})
	@Override
	public <T> void operate(ConnectionsImp c, int connId) {
		String s="";
		File folder = new File("Files"+File.separator);
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {				// Maybe we dont need Files and only dircs.
			if (listOfFiles[i].isFile()) {
		        s=s+(listOfFiles[i].getName()+"\0");
		    }else if (listOfFiles[i].isDirectory()) {
		        s=s+(listOfFiles[i].getName()+"\0");
		      }
		}
		int datasize=s.getBytes().length;
		int count=datasize/512;
		int start=0;
		int end=Math.min(512, datasize);
		if(datasize%512!=0){
			count++;
		}
		byte[] data=new byte[datasize];
		data=s.getBytes();
		for (int i=0; i<count; i++){
			byte[] newdata= new byte[Math.min(512, datasize)];
			for (int j=0; j<newdata.length;j++){
				newdata[j]=data[j+(512*(count-1))];
			}
			int currdatanum=1;
			if (c.getMyHashy().get(connId) instanceof BlockingConnectionHandler){
				BlockingConnectionHandler bct=(BlockingConnectionHandler) c.getMyHashy().get(connId);
				bidiImp bi= bct.getProt();
				bi.setblocknum(1);
				bi.setData(data);
				bi.setstartend(start, end);
				bi.senddata();
				start=end+1;
				end =Math.min(start+512, datasize);
			}else {
				NonBlockingConnectionHandler bct=(NonBlockingConnectionHandler) c.getMyHashy().get(connId);
				bidiImp bi= bct.getProt();
				bi.setblocknum(1);
				bi.setData(data);
				bi.setstartend(start, end);
				bi.senddata();
				start=end+1;
				end =Math.min(start+512, datasize);
			}
			
		}
		/**
		 * add If blocking
		 * else nonBlocking
		 */
	}
		//david change it
		// maybe we need to divide this DATA to numerous data (MAX 512) and maybe encoder does it.
}



package bgu.spl171.Packets;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import bgu.spl171.net.api.bidi.ConnectionsImp;
import bgu.spl171.net.srv.BlockingConnectionHandler;
import bgu.spl171.net.srv.NonBlockingConnectionHandler;

public class DATA extends Packet{

	final int opCode=3;
	long packetSize=0;
	int blockNum;
	byte[] data;					// REPRESENT AS BYTE [] 
	boolean lastblockNum=true;
	
	public void setLastBlock(boolean a){
		lastblockNum=a;
	}
	
	public DATA(long dataSize, int bNum, byte[] data) {
		this.packetSize=dataSize;
		this.blockNum=bNum;
		this.data=data;
		
	}
	public DATA(int pSize, int bNum) {
		this.packetSize=pSize;
		this.blockNum=bNum;
				
	}

	public int getOpCode() {
		return opCode;
	}
	public short getPacketSize(){
		return (short) packetSize;
	}
	@SuppressWarnings("rawtypes")
	@Override
	public void operate(ConnectionsImp c, int connId) {
		if (packetSize<512){
			setLastBlock(true);
		}else{
			setLastBlock(false);
		}
		if(!lastblockNum){
			addData(data,c,connId);
		}
		else{
			if (c.getMyHashy().get(connId) instanceof BlockingConnectionHandler){
				BlockingConnectionHandler bct= (BlockingConnectionHandler) c.getMyHashy().get(connId);
				byte[] pervdata=bct.getProt().getData();
				byte[] newdata = new byte[(int) (pervdata.length +packetSize)];
				if(pervdata.length > 0){
					int j=pervdata.length;
					for (int i=0 ; i<newdata.length;i++){
						if (i<pervdata.length){
							newdata[i]=pervdata[i];
						}else{
							newdata[i]=data[i-j];
						}
					}
					data=newdata;
				}
				ACK a= new ACK(blockNum);
				String DataName=bct.getProt().getDataName();
				Path file = Paths.get("Files"+File.separator+DataName);
				try {
					Files.write(file, data);
				} catch (IOException e) {
					e.printStackTrace();
				}
				a.operate(c, connId);
				BCAST b= new BCAST(DataName,1);
				b.operate(c, connId);
				bct.getProt().setData(new byte[1 << 10]);
				bct.getProt().setblocknum(0);
				bct.getProt().setDataName("");
			}else {
				NonBlockingConnectionHandler bct= (NonBlockingConnectionHandler) c.getMyHashy().get(connId);
				byte[] pervdata=bct.getProt().getData();
				byte[] newdata = new byte[(int) (pervdata.length +packetSize)];
				if(pervdata.length > 0){
					int j=pervdata.length;
					for (int i=0 ; i<newdata.length;i++){
						if (i<pervdata.length){
							newdata[i]=pervdata[i];
						}else{
							newdata[i]=data[i-j];
						}
					}
					data=newdata;
				}
				ACK a= new ACK(blockNum);
				
				String DataName=bct.getProt().getDataName();
				Path file = Paths.get("Files"+File.separator+DataName);
				byte[] fixedData=new byte[data.length-1];
				for (int i=0; i<fixedData.length; i++){
					fixedData[i]=data[i+1];
				}
				try {
					Files.write(file, fixedData);
				} catch (IOException e) {
					e.printStackTrace();
				}
				a.operate(c, connId);
				BCAST b= new BCAST(DataName,1);
				b.operate(c, connId);
				bct.getProt().setData(new byte[1 << 10]);
				bct.getProt().setblocknum(0);
				bct.getProt().setDataName("");
			}
		}
	}
	@SuppressWarnings("rawtypes")
	public void addData(byte[] data,ConnectionsImp c,int connId){
		if(packetSize<512)
		{
			lastblockNum=true;
			operate(c,connId);
		}
		else
		{
			if (c.getMyHashy().get(connId) instanceof BlockingConnectionHandler){
				BlockingConnectionHandler bct= (BlockingConnectionHandler) c.getMyHashy().get(connId);
				byte[] pervdata=bct.getProt().getData();
				byte[] newdata = new byte[(int) (pervdata.length + packetSize)];
				int j=pervdata.length;
				for (int i=0 ; i<newdata.length;i++){
					if (i<pervdata.length){
						newdata[i]=pervdata[i];
					}else{
						newdata[i]=data[i-j];
					}
				}
				bct.getProt().setData(newdata);
				ACK a= new ACK(blockNum);
				a.operate(c, connId);
			}
			else {
				NonBlockingConnectionHandler bct= (NonBlockingConnectionHandler) c.getMyHashy().get(connId);
				byte[] pervdata=bct.getProt().getData();
				byte[] newdata = new byte[(int) (pervdata.length + packetSize)];
				int j=pervdata.length;
				for (int i=0 ; i<newdata.length;i++){
					if (i<pervdata.length){
						newdata[i]=pervdata[i];
					}else{
						newdata[i]=data[i-j];
					}
				}
				bct.getProt().setData(newdata);
				ACK a= new ACK(blockNum);
				a.operate(c, connId);
			}
			
		}
	}
	public void setname (String name){
		
	}
	public short getBlockNum() {
		return (short) blockNum;
	}
	public byte[] getData() {
		return data;
	}
}

package bgu.spl171.net.api.bidi;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import bgu.spl171.Packets.*;
import bgu.spl171.net.api.MessageEncoderDecoder;;

public class encDec <T> implements MessageEncoderDecoder<T> {

	/**
	 * Explanation for David:
	 * We will count to know when we can get opCode, we will use byte2Short to 
	 * translate it to our opCode, and we will save the opCode as variable. 
	 * another variable dataSize is for packet data.
	 * decodeNextByte : check all cases - ends in 0, else : DISC/DIRQ (10,6 Opcode)
	 * else ACK (Opcode4 and 4 bytes) else DATA: in function DataCheck
	 * ELSE ELSE ELSE BCAST can be with 0 in the middle, so we check if its bcast and we after the 4th byte, we knows its end
	 */
	
	private byte[] bytes = new byte[1 << 10]; //start with 1k
    private int len = 0;
    private Packet p1;
    private int count=0;
    private short op;
    private long dataSize;

    @SuppressWarnings("unchecked")
	@Override
    public T decodeNextByte(byte nextByte) {
        //notice that the top 128 ASCII characters have the same representation as their utf-8 counterparts
        //this allow us to do the following comparison
        count++;
        boolean pushed=false;
        if (count==2){	
        	pushByte(nextByte);
        	pushed=true;
        	op=bytes2Short(bytes);
        }
    	if (count==4 && op==3){			// we read 4 bytes and its DATA, we save dataSize
        	pushByte(nextByte);
        	pushed=true;
        	dataSize=bytes2ShortInTheMiddle(bytes,2,3);	
        } else{ 
        	if ((op==4 && count==4))
        	{									// ACK !
        		pushByte(nextByte);
        		pushed=true;
        		p1=(Packet) createPacket();
    	       	count=0;
    	       	this.bytes=new byte[1 << 10];
    	       	len=0;
    	       	op=0;
    	        return (T) p1;
        	}
        	if ((op!=4) && ((nextByte == '\0' && op!=9 && count>1 &&op!=3)||(nextByte == '\0' && op==7) || op==10 || op==6 || (op==9 && nextByte=='\0' && count>4) || (op==3 && dataCheck()))) {
        		if(op==3){
        			pushByte(nextByte);
        			pushed=true;
        		}
        		p1=(Packet) createPacket();
    	       	count=0;
    	       	this.bytes=new byte[1 << 10];
    	       	len=0;
    	       	op=0;
    	        return (T) p1;
    		} else{
    			if (!pushed){
    	        	   pushByte(nextByte);
    			}
    		}
        }
        return null;
    	//not a line yet
    }

    private boolean dataCheck() {
    	if (dataSize+6== count)
    		return true;
		return false;
	}

	/**
     * given function from assignment page, converting bytes into a short.
     * @param byteArr
     * @return short, OpCode (cause the array length is 2)
     */
    public short bytes2Short(byte[] byteArr)
    {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }
    
    /**
     * David, its our interpretation, for bytes2Short when not in start, taking places and giving short (For Data for example)
     * can be used for Block number as well...
     * @param byteArr
     * @return
     */
    public short bytes2ShortInTheMiddle(byte[] byteArr, int startPlace, int endPlace)
    {
    	byte[] arrClone= new byte[2];
    	arrClone[0]=byteArr[startPlace];
    	arrClone[1]=byteArr[endPlace];
        short result = (short)((arrClone[0] & 0xff) << 8);
        result += (short)(arrClone[1] & 0xff);
        return result;
    }
    /**
     * 
     * @param num
     * @return byte[] from the short given
     */
    public byte[] shortToBytes(short num)
    {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }
    /**
     * putting and converting Short in byte[]
     * @param b - Array of bytes
     * @param s - Short to convert
     * @param startPlace - starting place to put in Array
     * @param endPlace - endPlace to put in Array
     * @return
     */
    public byte[] short2BytesInTheMiddle(byte[] b, short s, int startPlace, int endPlace)
    {
        b[startPlace] = (byte)((s >> 8) & 0xFF);
        b[endPlace] = (byte)(s & 0xFF);
        return b;
    }
    
    public byte[] String2BytesInTheMiddle(byte[]b, String s, int startPlace){
    	byte[] be= s.getBytes();
    	for (int i=0; i<be.length;i++){
    		b[startPlace+i]=be[i];
    	}
    	return b;
    }
    
	@SuppressWarnings({ "rawtypes", "unused" })
	@Override
    public byte[] encode(T message) {
		byte[] sendBytes=null;
		byte[] opcode=shortToBytes((short) ((Packet)message).getOpCode());
		short op=(short) ((Packet) message).getOpCode();
		switch (op){
		case 1:{ //RRQ
			RRQ r=(RRQ) message;
			String name=r.getName();
			sendBytes= new byte[name.length()+3];
			sendBytes[0]=opcode[0];
			sendBytes[1]=opcode[1];
			byte[] stringname=name.getBytes();
			for (short i=0; i<name.length(); i++){
				sendBytes[i+2]=stringname[i];
			}
			sendBytes[name.length()+2]=0;
		}break;
		case 2:{ //WRQ
			WRQ w=(WRQ) message;
			String name=w.getName();
			sendBytes= new byte[name.length()+3];
			sendBytes[0]=opcode[0];
			sendBytes[1]=opcode[1];
			byte[] stringname=name.getBytes();
			for (short i=0; i<name.length(); i++){
				sendBytes[i+2]=stringname[i];
			}
			sendBytes[name.length()+2]=0;
		}break;
		case 3: {//data
			DATA d= (DATA) message;
			byte[] bData= d.getData();
			short shortpacketsize= d.getPacketSize();
			short sortblocknum=d.getBlockNum();
			byte[] packetsize=shortToBytes(shortpacketsize);
			sendBytes= new byte[shortpacketsize+6];
			byte [] blocknum=shortToBytes(sortblocknum);
			sendBytes[0]=opcode[0];
			sendBytes[1]=opcode[1];
			sendBytes[2]=packetsize[0];
			sendBytes[3]=packetsize[1];
			sendBytes[4]=blocknum[0];
			sendBytes[5]=blocknum[1];
			for (short i=6; i<shortpacketsize+6; i++){
				byte c=bData[i-6];
				sendBytes[i]=bData[i-6];
			}
		}break;
		case 4:{//ack
			ACK a=(ACK) message; 
			short sortblocknum=(short)a.getblockNum();
			sendBytes= new byte[4];
			byte [] blocknum=shortToBytes(sortblocknum);
			sendBytes[0]=opcode[0];
			sendBytes[1]=opcode[1];
			sendBytes[2]=blocknum[0];
			sendBytes[3]=blocknum[1];
		}break;
		case 5: {//error
			ERROR e=(ERROR) message; 
			short errorcode=(short)e.getErorCode();
			e.setErrorMsg();
			String s= e.getErorMsg();
			byte[] errorM=s.getBytes();
			sendBytes= new byte[5+errorM.length];
			byte [] erorcodebyte=shortToBytes(errorcode);
			sendBytes[0]=opcode[0];
			sendBytes[1]=opcode[1];
			sendBytes[2]=erorcodebyte[0];
			sendBytes[3]=erorcodebyte[1];
			for (int i=4; i<sendBytes.length-1; i++){
				sendBytes[i]=errorM[i-4];
			}
		}break;
		case 6: {//DIRQ
			sendBytes= new byte[2];
			sendBytes[0]=opcode[0];
			sendBytes[1]=opcode[1];
		}break;
		case 7: {//LOG
			LOGRQ l=(LOGRQ) message;
			String name=l.getName();
			sendBytes= new byte[name.length()+3];
			sendBytes[0]=opcode[0];
			sendBytes[1]=opcode[1];
			byte[] stringname=name.getBytes();
			for (short i=0; i<name.length(); i++){
				sendBytes[i+2]=stringname[i];
			}
			sendBytes[name.length()+2]=0;
		}break;
		case 8: {//DEL
			DELRQ d=(DELRQ) message;
			String name=d.getName();
			sendBytes= new byte[name.length()+3];
			sendBytes[0]=opcode[0];
			sendBytes[1]=opcode[1];
			byte[] stringname=name.getBytes();
			for (short i=0; i<name.length(); i++){
				sendBytes[i+2]=stringname[i];
			}
			sendBytes[name.length()+2]=0;
		}break;
		case 9: {//bcast
			BCAST b=(BCAST) message; 
			short sortblocknum=(short)b.getaddNum();
			byte addremoved=(byte) sortblocknum;
			String s=b.getBcastName();
			byte[] stringname=s.getBytes();
			sendBytes= new byte[4+stringname.length];
			sendBytes[0]=opcode[0];
			sendBytes[1]=opcode[1];
			sendBytes[2]=addremoved;
			sendBytes[sendBytes.length-1]='\0';
			for (short i=3; i<sendBytes.length-1; i++){
				sendBytes[i]=stringname[i-3];
			}
		}break;
		case 10: {//DISC
			sendBytes= new byte[2];
			sendBytes[0]=opcode[0];
			sendBytes[1]=opcode[1];
		}break;
		
		}
        return (sendBytes); //uses utf8 by default
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	private T createPacket() {//change to private
    	Packet p = null;
    	boolean goodOp=false;
    	switch (op){
    		case 1: {
    			String name = popString(2, count-1);						// Count or count-1 ? xD
    			p=new RRQ(name);		
    			goodOp=true;
    		}break;
			case 2: {
				String name = popString(2, count-1);
				String newname= ""+name+"";
    			p=new WRQ(newname);
    			goodOp=true;
   			}break;
			case 3: {
				
				int bNum=bytes2ShortInTheMiddle(bytes, 4, 5);
				byte[] dBytes= new byte[(int) dataSize];
				for (int i=0; i<dataSize; i++){
					dBytes[i]=bytes[i+6];
				}
				p=new DATA(dataSize, bNum, dBytes);
				goodOp=true;
			}break;
			case 4:{
				int bNum=bytes2ShortInTheMiddle(bytes, 2, 3);
				p=new ACK(bNum);
				goodOp=true;
			}break;
			case 5: {
				int errCode=bytes2ShortInTheMiddle(bytes, 2, 3);
				p=new ERROR(errCode);		// Can add Error msg as well.
				goodOp=true;
			}break;
			case 6: {
				p=new DIRQ();
				goodOp=true;
			}break;
			case 7: {
				String name= popString(2, count-1);
				p=new LOGRQ(name);
				goodOp=true;
			}break;
			case 8: {
				String filename = popString(2, count-1);
    			p=new DELRQ(filename);
    			goodOp=true;
    		}break;
			case 9: {
				int added=bytes2ShortInTheMiddle(bytes, 2, 2);
				String name= popString(3, count-1);					// FIX.
				p=new BCAST(name, added);
				goodOp=true;
			}break;
			case 10:{
				p=new DISC();
				goodOp=true;
			}break;
    	}
    	if (!goodOp){
    		p=new ERROR(4);
    	}
		return (T) p;
	}

	private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }
        bytes[len++] = nextByte;
    }

    public String popString(int start, int end) {
        //notice that we explicitly requesting that the string will be decoded from UTF-8
        //this is not actually required as it is the default encoding in java.
        String result = new String(bytes, start, end-2, StandardCharsets.UTF_8);
        len = 0;
        return result;
    }
    public String realpopString(byte[] bytes) {
    	
    	{
    	    String file_string = "";
    	    for(int i = 0; i < bytes.length; i++)
    	    {
    	    	if (bytes[i]=='\0')
    	    	{
    	    		file_string=file_string + " ";
    	    	}
    	    	else
    	    		file_string =file_string + (char)bytes[i];
    	    }
    	    return file_string;    
    	}
    }

}


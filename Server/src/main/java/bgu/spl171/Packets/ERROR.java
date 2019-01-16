package bgu.spl171.Packets;

import bgu.spl171.net.api.bidi.ConnectionsImp;
import bgu.spl171.net.srv.ConnectionHandler;

public class ERROR extends Packet{

	final int opCode=5;
	int ErrorCode;
	String ErrorMsg;
	
	public ERROR(int ErrorCode) {
		this.ErrorCode=ErrorCode;
	}

	public void setErrorMsg(){
		switch (ErrorCode){
		case 0: ErrorMsg="Not defined, see error message"; break;
		case 1: ErrorMsg="File not found"; break;
		case 2: ErrorMsg="Access violation"; break;
		case 3: ErrorMsg="Disk full or allocation exceeded";break;
		case 4: ErrorMsg="Illegal TFTP operationn";break;
		case 5: ErrorMsg="File already exists";break;
		case 6: ErrorMsg="User not logged in";break;
		case 7: ErrorMsg="User already logged in";break;
	}
	}
	public void printError(){
		// maybe its Error 6, maybe Error 6 + msg...
		System.out.println("Error "+ErrorCode+": "+ErrorMsg);
	}
	public int getOpCode() {
		return opCode;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void operate( ConnectionsImp c, int connId) {
		((ConnectionHandler)c.getMyHashy().get(connId)).send(new ERROR(ErrorCode));
	}

	public String getErorMsg() {
		return ErrorMsg;
	}

	public int getErorCode() {
		return ErrorCode;
	}

}

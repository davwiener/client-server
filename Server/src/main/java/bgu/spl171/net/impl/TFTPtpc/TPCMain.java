package bgu.spl171.net.impl.TFTPtpc;

import bgu.spl171.net.api.MessageEncoderDecoder;
import bgu.spl171.net.api.bidi.*;
import bgu.spl171.net.impl.rci.ObjectEncoderDecoder;
import bgu.spl171.net.impl.rci.RemoteCommandInvocationProtocol;
import bgu.spl171.net.srv.BlockingConnectionHandler;
import bgu.spl171.net.srv.ConnectionHandler;
import bgu.spl171.net.srv.Server;

import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.Random;

import javax.rmi.CORBA.Util;

import bgu.spl171.Packets.*;

@SuppressWarnings("unused")
public class TPCMain {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args){

		int i = Integer.parseInt(args[0]);
		 Server.threadPerClient(
	                i, //port
	                () ->  new bidiImp(), //protocol factory
	                () -> new encDec()  //message encoder decoder factory
	        ).serve();
	}
}

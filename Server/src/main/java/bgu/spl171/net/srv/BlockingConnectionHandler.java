package bgu.spl171.net.srv;

import bgu.spl171.net.api.MessageEncoderDecoder;
import bgu.spl171.net.api.bidi.BidiMessagingProtocol;
import bgu.spl171.net.api.bidi.bidiImp;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

public class BlockingConnectionHandler<T> implements Runnable, ConnectionHandler<T> {

    private final BidiMessagingProtocol<T> protocol;
    private final MessageEncoderDecoder<T> encdec;
    private final Socket sock;
    private BufferedInputStream in;
    private BufferedOutputStream out;
    private volatile boolean connected = true;
    private int id;
    private String name;
    private boolean started;

    public BlockingConnectionHandler(Socket sock, MessageEncoderDecoder<T> endDec, BidiMessagingProtocol<T> protocol) {
        this.sock = sock;
        this.encdec = endDec;
        this.protocol = protocol;
        started=false;
        name="";
    }
    
    public void setId(int id){
    	this.id=id;
    }
    
	@SuppressWarnings("rawtypes")
	public bidiImp getProt(){
    	return (bidiImp) protocol;
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public void run() {

        try (Socket sock = this.sock) { //just for automatic closing
            int read;
            in = new BufferedInputStream(sock.getInputStream()); 				// WRQ LIVING A LIE.MP3
            out = new BufferedOutputStream(sock.getOutputStream());
            T nextMessage;
            while (!protocol.shouldTerminate() &&connected && (read = in.read()) >= 0) {
            	if (!started){
            		protocol.start(id, ((bidiImp) protocol).getConns());
            		started=true;
            		nextMessage = (T) encdec.decodeNextByte((byte) read);	// should be nullptr alltime, but need save the 1st.
            	}
            	else
            	{
            		 nextMessage = (T) encdec.decodeNextByte((byte) read);
            		 if (nextMessage != null) {
            			 protocol.process(nextMessage);
            		 }
            	}
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void close() throws IOException {
        connected = false;
        sock.close();
    }

	@Override
	public void send(T msg) {
		try{
			byte[] b= encdec.encode(msg);
			out.write(b);
			out.flush();
		} catch (IOException ex){
			ex.printStackTrace();
		}	
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name2) {
		this.name=name2;
	}
}
/**
 * Created by perdor on 19/01/2017.
 */

package bgu.spl171.net.impl.TFTPreactor;

import bgu.spl171.net.api.bidi.bidiImp;
import bgu.spl171.net.api.bidi.encDec;
import bgu.spl171.net.srv.Server;

public class ReactorMain
{
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String [] args){
        int i = Integer.parseInt(args[0]);
        Server.reactor(
                Runtime.getRuntime().availableProcessors(),
                i, //port
                () ->  new bidiImp(), //protocol factory
                () -> new encDec() //message encoder decoder factory
        ).serve();
    }
}

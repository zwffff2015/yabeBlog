package controllers;

import play.mvc.Http.*;
import play.mvc.*;

/**
 * Author: DarrenZeng
 * Date: 2015-11-12
 */
public class MyWebSocket extends WebSocketController {
    public static void hello(String name) {
        outbound.send("Hello %s!", name);
    }

    public static void echo(){
        while(inbound.isOpen()) {
            Http.WebSocketEvent e = await(inbound.nextEvent());
            if(e instanceof Http.WebSocketFrame) {
                WebSocketFrame frame = (WebSocketFrame)e;
                if(!((WebSocketFrame) e).isBinary) {
                    if(frame.textData.equals("quit")) {
                        outbound.send("Bye!");
                        disconnect();
                    } else {
                        outbound.send("Echo: %s", frame.textData);
                    }
                }
            }
            if(e instanceof WebSocketClose) {
                System.out.println("Socket closed!");
            }
        }
    }
}

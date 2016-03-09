package models;

import play.libs.WS;
import play.libs.ws.WSAsync;

/**
 * Author: DarrenZeng
 * Date: 2015-11-12
 */
public class MyWSAsync extends WSAsync {
    public WS.WSRequest newRequest(String url, String encoding) {
        System.out.println(String.format("Access URL: %s", url));
        return super.newRequest(url, encoding);
    }
}

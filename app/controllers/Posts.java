package controllers;

/**
 * Author: DarrenZeng
 * Date: 2015-11-03
 */

import play.mvc.*;

@Check("admin")
@With(Secure.class)
public class Posts extends CRUD {
}

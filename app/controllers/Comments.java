package controllers;

import play.mvc.With;

/**
 * Author: DarrenZeng
 * Date: 2015-11-03
 */
@Check("admin")
@With(Secure.class)
public class Comments extends CRUD {
}

package controllers;

import models.User;
import play.mvc.With;

/**
 * Author: DarrenZeng
 * Date: 2015-11-03
 */
@Check("admin")
@With(Secure.class)
@CRUD.For(User.class)
public class RenameUsers extends CRUD {
}

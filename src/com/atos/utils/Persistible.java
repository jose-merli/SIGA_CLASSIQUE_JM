/**
 * <p>Title: Persistible </p>
 * <p>Description: class that handles a zip file </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: SchLumbergerSema </p>
 * @author 
 * @version 1.0
 */

package com.atos.utils;

public interface Persistible {

   public boolean add() throws ClsExceptions;


   public boolean update() throws ClsExceptions;


   public int delete() throws ClsExceptions;



}
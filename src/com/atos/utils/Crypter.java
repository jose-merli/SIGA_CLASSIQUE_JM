/**
 * <p>Title: Crypter </p>
 * <p>Description: class that handles a zip file </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: SchLumbergerSema </p>
 * @author 
 * @version 1.0
 */

package com.atos.utils;

public class Crypter
{
  String secret="";
  public String doIt(String password) {
    try {
      secret="";
      if (password==null) password="";
      byte[] ret=encryptSHA(password);
      for (int j=0;j<ret.length;j++) {
        secret+=hexValue(ret[j]);
      }
    }
    catch (Exception e){
      e.printStackTrace();
    }
    return secret;
  }

  public String hexValue(byte bt) {
    int low=bt&0x0F;
    int hi=(bt>>4)&0x0F;
    String ret="";
    if (low>= 0 && low<=9) {
      ret+=""+low;
    } else {
      ret+=(char)(((byte)'A')+(low-10));
    }
    if (hi>= 0 && hi<=9) {
      ret+=""+hi;
    } else {
      ret+=(char)(((byte)'A')+(hi-10));
    }
    return ret;
  }

  static byte[] encryptSHA(String x)   throws Exception
  {
    java.security.MessageDigest d =null;
    d = java.security.MessageDigest.getInstance("SHA-1");
    d.reset();
    d.update(x.getBytes());
    return  d.digest();
  }

  static byte[] encryptMD5(String x)   throws Exception
  {
    java.security.MessageDigest d =null;
    d = java.security.MessageDigest.getInstance("MD5");
    d.reset();
    d.update(x.getBytes());
    return  d.digest();
  }
}
package com.atos.utils;

import java.util.*;
import javax.servlet.http.*;

public class ClsMngData {

  public ClsMngData() {
  }
  /**
       *  Compare Hashtables
           * @param firstPassingHT The origin hashtable
           * @param secondPassingHT The last hashtable
           * @param request The HttpServletRequest to write log application
           * @return Vector with changed elements. Contains PairsKeys objects.
           * This class offers getIdObj() and getValueObj() methods to obtain each properties element.
           * @throws ClsExceptions
           *         Codes: 0 --> Hashtables sizes are diferents
           *                1 --> Hashtables keys are diferents
	 */

  public Vector compareHashtables(Hashtable firstPassingHT, Hashtable secondPassingHT, HttpServletRequest request) throws ClsExceptions
  {
    Vector vect= new Vector();

    if ((firstPassingHT.size())!=(secondPassingHT.size()))
    {
      ClsLogging.writeFileLog("Los tamanhos no son iguales "+firstPassingHT.size()+"<>"+secondPassingHT.size(),request,3);
      throw new ClsExceptions("0");
      }

    Enumeration enumkeys=firstPassingHT.keys();

    while (enumkeys.hasMoreElements())
    {
      Object obj = (Object)enumkeys.nextElement();
      if (!searchInOtherHashKeys(obj,secondPassingHT))
      {
        ClsLogging.writeFileLog("Las claves no son iguales ",request,3);
        throw new ClsExceptions("1");
       }
      Object obje = firstPassingHT.get(obj);
      boolean equals=obje.toString().trim().equals(secondPassingHT.get(obj).toString().trim());

      if (!equals)
      {
       //Decimals
        if (
            (obje.toString().startsWith(".")) &&
            (secondPassingHT.get(obj).toString().startsWith("0"))
            ) {
              ClsLogging.writeFileLog("Chequeo decimales "+obje.toString()+" & "+secondPassingHT.get(obj).toString(),request,3);
              Float flt1= new Float(obje.toString());
              Float flt2= new Float(secondPassingHT.get(obj).toString());
              if (flt1.compareTo(flt2)==0)
                equals = true;
              else
                equals =false;
        }
        if (
            (obje.toString().startsWith("0")) &&
            (secondPassingHT.get(obj).toString().startsWith("."))
            ) {
              ClsLogging.writeFileLog("Chequeo decimales "+obje.toString()+" & "+secondPassingHT.get(obj).toString(),request,3);
              Float flt1= new Float(obje.toString());
              Float flt2= new Float(secondPassingHT.get(obj).toString());
              if (flt1.compareTo(flt2)==0)
                equals = true;
              else
                equals =false;
        }
        if (
            (obje.toString().startsWith("-")) &&
            (secondPassingHT.get(obj).toString().substring(1,2).equals(".")) &&
            (secondPassingHT.get(obj).toString().startsWith("-"))
          )
        {
          ClsLogging.writeFileLog("Chequeo decimales "+obje.toString()+" & "+secondPassingHT.get(obj).toString(),request,3);
          Float flt1= new Float(obje.toString());
          Float flt2= new Float(secondPassingHT.get(obj).toString());
          if (flt1.compareTo(flt2)==0)
            equals = true;
          else
                equals =false;
        }
        if (
            (obje.toString().startsWith("-")) &&
            (obje.toString().substring(1,2).equals(".")) &&
            (secondPassingHT.get(obj).toString().startsWith("-"))
          )
        {
          ClsLogging.writeFileLog("Chequeo decimales "+obje.toString()+" & "+secondPassingHT.get(obj).toString(),request,3);
          Float flt1= new Float(obje.toString());
          Float flt2= new Float(secondPassingHT.get(obj).toString());
          if (flt1.compareTo(flt2)==0)
            equals = true;
          else
                equals =false;
        }
      }

      if (!equals) {
        PairsKeys pairs = new PairsKeys();
        pairs.setIdObj(obj);
        pairs.setValueObj(obje);
        vect.addElement(pairs);
      }
    }

    return vect;
  }

  public Vector compareHashtablesMinimum(Hashtable firstPassingHT, 
  		Hashtable secondPassingHT, 
		HttpServletRequest request) throws ClsExceptions
  {
    Vector vect= new Vector();

/*    if (firstPassingHT.size()>secondPassingHT.size()) { 
      ClsLogging.writeFileLog("First is greater! "+firstPassingHT.size()+"<>"+secondPassingHT.size(),request,3);
      throw new ClsExceptions("0");
    }  */

    Enumeration enumkeys=firstPassingHT.keys();

    while (enumkeys.hasMoreElements())
    {
      Object obj = (Object)enumkeys.nextElement();
      if (!searchInOtherHashKeys(obj,secondPassingHT))     {
      	continue;
/*        ClsLogging.writeFileLog("Keys are NOT identical! ",request,3);
        throw new ClsExceptions("1");  */
       }
      Object obje = firstPassingHT.get(obj);
      boolean equals=obje.toString().trim().equals(secondPassingHT.get(obj).toString().trim());

      
      ////////////////////////
      // Para el problema de la fecha 1901/01/01
//      ClsLogging.writeFileLog("Fecha1: " + obje.toString().trim() + " Fecha2:" + secondPassingHT.get(obj).toString().trim(), request, 3);
      if (!equals) {
      	if ((GstDate.compararFechas(obje.toString().trim(), "1901/01/01") == 0) && 
      		(GstDate.compararFechas(secondPassingHT.get(obj).toString().trim(), "1901/01/01") == 0 )) {
      		equals = true;
      	}
      }
      ////////////////////////
      
      if (!equals)
      {
       //Decimals
        if (
            (obje.toString().startsWith(".")) &&
            (secondPassingHT.get(obj).toString().startsWith("0"))
            ) {
              ClsLogging.writeFileLog("Chequeo decimales "+obje.toString()+" & "+secondPassingHT.get(obj).toString(),request,3);
              Float flt1= new Float(obje.toString());
              Float flt2= new Float(secondPassingHT.get(obj).toString());
              if (flt1.compareTo(flt2)==0)
                equals = true;
              else
                equals =false;
        }
        if (
            (obje.toString().startsWith("0")) &&
            (secondPassingHT.get(obj).toString().startsWith("."))
            ) {
              ClsLogging.writeFileLog("Chequeo decimales "+obje.toString()+" & "+secondPassingHT.get(obj).toString(),request,3);
              Float flt1= new Float(obje.toString());
              Float flt2= new Float(secondPassingHT.get(obj).toString());
              if (flt1.compareTo(flt2)==0)
                equals = true;
              else
                equals =false;
        }
        if (
            (obje.toString().startsWith("-")) &&
            (secondPassingHT.get(obj).toString().substring(1,2).equals(".")) &&
            (secondPassingHT.get(obj).toString().startsWith("-"))
          )
        {
          ClsLogging.writeFileLog("Chequeo decimales "+obje.toString()+" & "+secondPassingHT.get(obj).toString(),request,3);
          Float flt1= new Float(obje.toString());
          Float flt2= new Float(secondPassingHT.get(obj).toString());
          if (flt1.compareTo(flt2)==0)
            equals = true;
          else
                equals =false;
        }
        if (
            (obje.toString().startsWith("-")) &&
            (obje.toString().substring(1,2).equals(".")) &&
            (secondPassingHT.get(obj).toString().startsWith("-"))
          )
        {
          ClsLogging.writeFileLog("Chequeo decimales "+obje.toString()+" & "+secondPassingHT.get(obj).toString(),request,3);
          Float flt1= new Float(obje.toString());
          Float flt2= new Float(secondPassingHT.get(obj).toString());
          if (flt1.compareTo(flt2)==0)
            equals = true;
          else
                equals =false;
        }
        
       
        try {
       // Integer comprobarNumero=Integer.valueOf(obje.toString());
        ////////////////////////////////
        // Para comprobar el caso 100.0 = 100
        if ( obje.toString().indexOf(".") > 0 || secondPassingHT.get(obj).toString().indexOf(".") > 0 ) 
        {
			ClsLogging.writeFileLog("Chequeo decimales " + obje.toString() + " & " + secondPassingHT.get(obj).toString(),request,3);
			Double d1 = new Double(obje.toString());
			Double d2 = new Double(secondPassingHT.get(obj).toString());
			if (d1.compareTo(d2) == 0)
				equals = true;
			else
				equals = false;
		}
        ////////////////////////////////
        }catch (NumberFormatException e){
        	;
        }
        
        
      }

      if (!equals) {
        PairsKeys pairs = new PairsKeys();
        pairs.setIdObj(obj);
        pairs.setValueObj(obje);
        vect.addElement(pairs);
      }
    }

    return vect;
  }

  
  /**
         *  Search Hashtable Key
             * @param obj The object to search
             * @param ht The hashtable
             * @return boolean True if the object exists.
	 */

  private boolean searchInOtherHashKeys(Object obj, Hashtable ht)
  {
    boolean bool=true;
    for (int i=0;i<ht.size();i++)
    {
      if (!ht.containsKey(obj))
      {
        bool=false;
        break;
      }
    }

    return bool;
  }


}
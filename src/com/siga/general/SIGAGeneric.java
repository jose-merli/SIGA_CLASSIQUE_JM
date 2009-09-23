package com.siga.general;

import java.util.Vector;
import java.util.Enumeration;
import com.atos.utils.ClsExceptions;
import com.atos.utils.Table;
import javax.servlet.http.HttpServletRequest;


public class SIGAGeneric {
    /*	tableName 	--> nombre de la tabla Oracle en la clase Constants
    			Ej: "TableConstans.SIGA_SpecificType"
    	cls 		--> nombre de la Clase
    			Ej: "pssc.engineMaintenance.configuration.PSSCSpecificType"
    	pairsKeys	--> Vector de pares "Columna - Valor"
    			Ej: (String) Columna: "ColumnConstants.SIGA_PartNumber_PN"
    			      (String) Value: HashTable.get(ColumnConstants.SIGA_CaLimit_ITEM_PN)
    */

  HttpServletRequest req=null;

    public Object getInstance (String tableName, String cls,Vector pairsKeys, HttpServletRequest req )
        throws ClsExceptions{

		this.req=req;
      Table tableST=null;
      Object obj=null;

      try{
        tableST = new Table(this.req,tableName,cls);
        Enumeration enumer= pairsKeys.elements();
       while ( enumer.hasMoreElements())
        {
          com.atos.utils.PairsKeys pair = (com.atos.utils.PairsKeys)enumer.nextElement();
          String id=pair.getId();
          String value=pair.getValue();
          tableST.addFilter(id,value);
        }
        obj=tableST.search().firstElement();
      }
      catch(Exception e){
        throw new ClsExceptions (e,e.getMessage(),cls,"0","GEN00","14");
      }
      return obj;
     }

	 public Object getInstanceWrite (String tableName, String cls,Vector pairsKeys, HttpServletRequest req )
		 throws ClsExceptions{

		 this.req=req;
	   Table tableST=null;
	   Object obj=null;

	   try{
		 tableST = new Table(this.req,tableName,cls);
		 Enumeration enumer= pairsKeys.elements();
		while ( enumer.hasMoreElements())
		 {
		   com.atos.utils.PairsKeys pair = (com.atos.utils.PairsKeys)enumer.nextElement();
		   String id=pair.getId();
		   String value=pair.getValue();
		   tableST.addFilter(id,value);
		 }
		 obj=tableST.search(true).firstElement();
	   }
	   catch(Exception e){
		 throw new ClsExceptions (e,e.getMessage(),cls,"0","GEN00","14");
	   }
	   return obj;
     }


    }

package com.siga.generalRequirements.accessControl;

import java.util.*;
import com.atos.utils.*;
import javax.servlet.http.*;
import com.siga.general.SIGAGeneric;

/**
 * <p>Clase abstracta que representa el Gestor de Base de Datos.</p>
 * @author Luis Miguel Sánchez PIÑA
 * @version 1.2
 */

abstract public class SIGAGrDDBB extends SIGAGeneric
{
	protected Hashtable htRecord;
  	protected Hashtable htRecordBackup;
  	protected HttpServletRequest req;

  	abstract public boolean add() throws ClsExceptions;
  	abstract public int delete() throws ClsExceptions;
  	abstract public boolean update() throws ClsExceptions;

  	/**
   	* Constructor
   	*/
  	public SIGAGrDDBB()
  	{
    	this.req = null;
    	this.htRecord = null;
  	}

  	/**
   	* Constructor
   	* @param req Petición.
   	*/
  	public SIGAGrDDBB (HttpServletRequest req)
  	{
    	this.req = req;
    	this.htRecord = null;
  	}

  	/**
   	* Constructor
   	* @param req Petición
   	* @param htRec Hashtable
   	*/
  	public SIGAGrDDBB (HttpServletRequest req, Hashtable htRec)
  	{
    	this.req = req;
    	this.htRecord = htRec;
  	}

  	/**
   	* Devuelve una Hashtable con los datos.
   	*/
  	public Hashtable getData()
  	{
    	return this.htRecord;
  	}

  	/**
   	* Guarda los datos en la Hashtable.
   	* @param data Hashtable con los datos a guardar.
   	*/
 	public void setData(Hashtable data)
  	{
    	this.htRecord = data;
  	}

  	/**
   	* Guarda los datos en la Hashtable de backup.
   	* @param data Hashtable con los datos a guardar.
   	*/
  	public void setDataBackup(Hashtable data)
  	{
    	this.htRecordBackup = data;
  	}

  	/**
   	* Carga los nombres de los campos en un array de String.
   	*/
  	protected String[] loadFields()
  	{
    	String[] columns = new String[htRecord.size()];
    	int i=0;
    	Enumeration e= htRecord.keys();
    	
    	while(e.hasMoreElements())
    	{
      		columns[i] =e.nextElement().toString();
      		i++;
    	}
    
    	return columns;
  	}

  	/**
   	* Crea un registro en la Base de Datos.
   	* @param tablename Tabla sobre la que insertar el registro.
   	* @return boolean Indicando el resultado de la operación.
   	* @throws ClsExceptions
   	*/
  	protected boolean add(String tablename) throws ClsExceptions
  	{
    	ClsLogging.writeFileLog("Dentro del método 'add' de SIGAGrDDBB", req, 3);
    	
    	Row newRow = new Row();
    	int insertedRecords=0;
    	
    	try
    	{
      		newRow.load(htRecord);
      		String[] fields = loadFields();
      		insertedRecords = newRow.add(tablename,fields);
    	}
    	
    	catch (Exception ex)
    	{
      		if (req!=null)
      		{
        		ClsLogging.writeFileLog("ERROR insertando un registro en la tabla " + tablename, req, 3);
      		}
      		
      		else
      		{
        		ClsLogging.writeFileLogWithoutSession("ERROR insertando un registro en la tabla " + tablename, 3);
      		}
      		
      		ex.printStackTrace();
      		
      		throw new ClsExceptions(ex.getMessage(), "", "", "", "4");
    	}
    	
    	if (insertedRecords == 0)
    	{
    		return false;
    	}
    	
    	else
    	{
    		return true;
    	}
  	}


  	/**
   	* Borra un registro en la Base de Datos.
   	* @param tablename Tabla sobre la que insertar el registro.
   	* @param keyFields Lista con los nombres de los campos clave.
   	* @return int Número de registros borrados.
   	* @throws ClsExceptions
   	*/
  	protected int delete(String tablename, String[] keyFields) throws ClsExceptions
  	{
    	ClsLogging.writeFileLog("Dentro del método 'delete' de SIGAGrDDBB", req, 3);
    	Row deletedRow = new Row();
    	int deletedRecord = 0;
    	
    	try
    	{
      		deletedRow.load(htRecord);
      		deletedRecord = deletedRow.delete(tablename, keyFields);
    	}
    	
    	catch (Exception ex)
    	{
      		if (req!=null)
      		{
        		ClsLogging.writeFileLog("ERROR borrando un registro de la tabla " + tablename, req, 3);
      		}
      		
      		else
      		{
        		ClsLogging.writeFileLogWithoutSession("ERROR borrando un registro de la tabla " + tablename, 3);
      		}
      		
      		ex.printStackTrace();
      		
      		throw new ClsExceptions(ex.getMessage(), "", "", "", "4");
    	}
    	
    	return deletedRecord;
  	}

  	/**
   	* Modifica un registro de la Base de Datos.
   	* @param tablename Tabla sobre la que modificar el registro.
   	* @param fields Lista con los valores.
   	* @param updatableFields Lista con los campos.
   	* @return int Número de registros borrados.
   	* @throws ClsExceptions
   	*/
  	public boolean update(String tablename, String[] fields, String[] updatableFields) throws ClsExceptions
  	{
    	ClsLogging.writeFileLog("Dentro del método 'update' de SIGAGrDDBB", req, 3);
    	Row updatedRow = new Row();
    	int updatedRecords = 0;
    	
    	try
    	{
      		ClsLogging.writeFileLog("htrecord:"+this.htRecord+", htrecordbackup:"+this.htRecordBackup,req,3);
      
      		if ((this.htRecord!=null) && (this.htRecordBackup!=null))
      		{
        		ClsLogging.writeFileLog("Antes de load"+this.htRecord.size()+", "+this.htRecordBackup.size(),req,3);
      		}
      
      		updatedRow.load(this.htRecord);
      
      		if (this.htRecordBackup!=null)
      		{
      			ClsLogging.writeFileLog("Antes de setcomparedata"+this.htRecordBackup.size(),req,3);
      		}
      		
      		updatedRow.setCompareData(this.htRecordBackup,req);
      
      		updatedRecords = updatedRow.update(tablename, fields, updatableFields);
      
      		if (updatedRecords==0)
      		{
        		Vector v=updatedRow.getRecordsChanged();
        		
        		if(v.size()>0)
            	{
          			ClsLogging.writeFileLog("Hay cambios", req, 3);
          			Enumeration en=v.elements();
          			ClsLogging.writeFileLog("Elementos cambiados: ", req, 3);
          			ClsLogging.writeFileLog("********************", req, 3);
          			
          			while (en.hasMoreElements())
          			{
            			com.atos.utils.PairsKeys obj = (com.atos.utils.PairsKeys)en.nextElement();
            			ClsLogging.writeFileLog("Clave: "+obj.getIdObj(),req,3);
            			ClsLogging.writeFileLog("Valor: "+obj.getValueObj(),req,3);
            			ClsLogging.writeFileLog("********************",req,3);
          			}
          			
          			HttpSession ses = req.getSession();
          			ses.setAttribute("vectRecords", v);
        		}
      		}
      		
      		else
      		{
        		ClsLogging.writeFileLog("No hay cambios", req, 3);
      		}
    	}
    	
    	catch (Exception ex)
    	{
      		if (req!=null)
      		{
        		ClsLogging.writeFileLog("ERROR modificando un registro de la tabla " + tablename, req, 3);
      		}
      		
      		else
      		{
        		ClsLogging.writeFileLogWithoutSession("ERROR modificando un registro de la tabla " + tablename, 3);
      		}
      		
      		ex.printStackTrace();
      		
      		throw new ClsExceptions(ex.getMessage(), "", "", "", "4");
    	}
    
    	if (updatedRecords == 0)
    	{
    		return false;
    	}
    	
    	else
    	{
    		return true;
    	}
  }

  public void searchStatus() { }
}
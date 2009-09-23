package com.siga.administracion;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ClsConstants;
import com.atos.utils.Persistible;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.atos.utils.ColumnConstants;
import com.atos.utils.TableConstants;
import com.siga.Utilidades.UtilidadesMultidioma;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Schlumberger Sema
 * @version 1.0
 */

public class SIGAMasterTable implements Persistible {
	
	//public static final String TN_MASTER_TABLES="SIGA_TABLAS_MAESTRAS";
	public static final String TN_MASTER_TABLES="GEN_TABLAS_MAESTRAS";
	public static final String FN_ID_MASTER_TABLE="IDTABLAMAESTRA";     
	public static final String FN_ID_CODE_FIELD="IDCAMPOCODIGO";      
	public static final String FN_ID_TRANSLATION_TABLE="IDTABLATRADUCCION";  
	public static final String FN_ID_DESCRIPTION_FIELD="IDCAMPODESCRIPCION"; 
	public static final String FN_DESC_ACTION_PATH="PATHACCION";     
	public static final String FN_DESC_TABLE_ALIAS="ALIASTABLA";     
	public static final String FN_FLAG_LOGIC_DELETE="FLAGBORRADOLOGICO";  
	public static final String FN_FLAG_USES_LANG="FLAGUSALENGUAJE";    
	public static final String FN_ID_RESOURCE="IDRECURSO";           
	public static final String FN_CODE_LENGTH="LONGITUDCODIGO";      
	public static final String FN_DESC_LENGTH="LONGITUDDESCRIPCION";
	public static final String FN_TIPO_CODIGO="TIPOCODIGO";
	
	public static final String ALIAS_CODE_FIELD="CODE";  
	public static final String ALIAS_DESC_FIELD="DESCRIPTION";  
	
  	private Hashtable tableConfig;
  	private Hashtable data;
  	private Hashtable htFilters;
  	private Hashtable htRecordBackup;
  	private Row row = null;
  	private RowsContainer container = null;
  	private String user;
  	private int affectedRecords;
  	private Vector vChangedRecords;
	private UsrBean usrbean=null;

	  /**
	   * Constructor
	   */
	  public SIGAMasterTable() {
	  }

	  public void setUser(String s) {
	    user = s;
	  }

	  public String getUser() {
	    return user;
	  }

	  public void setAffectedRecords(int i) {
    	affectedRecords = i;
	  }

	  public int getAffectedRecords() {
	    return affectedRecords;
	  }

	  public void setData(Hashtable ht) {
	    data = ht;
	  }

	  public void setDataBackup(Hashtable data){
		   this.htRecordBackup = data;
	  }

	  public Vector getAllRecords() {
	    return (container == null) ? null : container.getAll();
	  }

	  public Hashtable getRecord() {
	    Hashtable ht = null;
	    if (row == null) {
	      if (container != null) {
	        Row r = (Row)container.get();
	        if (r != null) {
	          ht = r.getRow();
	        }
	      }
	    } else {
	      ht = row.getRow();
	    }
	    return ht;
	  }

	  public String getCodeLength(){
		//return this.codeLength;
		String cod=null;
		if(tableConfig!=null) {
			cod=(String)tableConfig.get(FN_CODE_LENGTH);
		}
		return cod;
	  }
	  
	  public void setCodeLength(String codeLength){
	  	if(tableConfig==null) {
	  		tableConfig=new Hashtable();
	  	}
	  	tableConfig.put(FN_CODE_LENGTH, codeLength);
	  }

	  public String getDescLength(){
		String des=null;
		if(tableConfig!=null) {
			des=(String)tableConfig.get(FN_DESC_LENGTH);
		}
		return des;
	  }
	
	  public void setDescLength(String descLength){
		if(tableConfig==null) {
			tableConfig=new Hashtable();
		}
		tableConfig.put(FN_DESC_LENGTH, descLength);
	  }

	  public String getTipoCodigo(){
			String des=null;
			if(tableConfig!=null) {
				des=(String)tableConfig.get(FN_TIPO_CODIGO);
			}
			return des;
		  }
		
	  public void setTipoCodigo(String tipoCodigo){
		if(tableConfig==null) {
			tableConfig=new Hashtable();
		}
		tableConfig.put(FN_TIPO_CODIGO, tipoCodigo);
	  }

	  public String getStrutsKey(){
		String key=null;
		if(tableConfig!=null) {
			key=(String)tableConfig.get(FN_ID_RESOURCE);
		}
		return key;
	  }

	  public void setStrutsKey(String strutsKey){
		if(tableConfig==null) {
			tableConfig=new Hashtable();
		}
		tableConfig.put(FN_ID_RESOURCE, strutsKey);
	  }

  /**
   * To create classes which contain queries
   * @param tableName Table Name
   * @throws ClsExceptions PSSC exception type
   */
  public void searchMasterTable(String tableName) throws ClsExceptions {
    String sql = " SELECT "+FN_ID_MASTER_TABLE+", "+FN_ID_TRANSLATION_TABLE+", "+FN_ID_CODE_FIELD+", " +
                 FN_ID_DESCRIPTION_FIELD+", "+FN_DESC_ACTION_PATH+", "+FN_DESC_TABLE_ALIAS+", " +
                 FN_FLAG_LOGIC_DELETE+", "+FN_FLAG_USES_LANG+", " +
				 " NVL("+FN_CODE_LENGTH+",3) "+FN_CODE_LENGTH+", "+
				 " NVL("+FN_DESC_LENGTH+",150) "+FN_DESC_LENGTH+", " +
				 FN_ID_RESOURCE+", " + FN_TIPO_CODIGO + " " +
                 " FROM "+ TN_MASTER_TABLES;
	if (tableName != null) {
      sql += " WHERE "+FN_ID_MASTER_TABLE+" = '" + tableName + "' ";
      row = new com.atos.utils.Row();
      if (row.find(sql)) {
        tableConfig = row.getRow();
      } else {
       	throw new ClsExceptions("Requested Master Table not found.", "","","","");
      }
    } else {
      container = new RowsContainer();
      container.query(sql);
    }

  }

  /**
   * To erase key fields which has empty value
   */

  private void eraseBlankValues(){
	Hashtable htFiltersFiltered=new Hashtable();
	Enumeration e= this.htFilters.keys();

	while(e.hasMoreElements()){
	  String key=(String) e.nextElement();
	  String value= ((String)this.htFilters.get(key)).trim();
	  if (!value.equals("")){
		htFiltersFiltered.put(key,value);
	  }
	}
	htFilters=htFiltersFiltered;
  }

  /**
	* To search records
	* @param ht Hashtable which contains the filters to apply
	* @throws ClsExceptions PSSC exception type
	* @return vSearch indicator of there are records
   */

  public String search(Hashtable ht) throws ClsExceptions{
	String vSearch = "0";
	String sql=null;
	this.htFilters = ht;

	if (tableConfig.get(FN_FLAG_USES_LANG).equals(ClsConstants.DB_TRUE)) {
	  sql = " SELECT T." + tableConfig.get(FN_ID_CODE_FIELD) + " AS "+ALIAS_CODE_FIELD+", " +
			" T." + tableConfig.get(FN_ID_DESCRIPTION_FIELD) + " AS "+ALIAS_DESC_FIELD+", " +
			" T."+ColumnConstants.FN_LANG_ID_LANGUAGE+ ", " + UtilidadesMultidioma.getCampoMultidioma("L."+ColumnConstants.FN_LANG_DESC_LANGUAGE, this.usrbean.getLanguage());
	  if(tableConfig.get(FN_FLAG_LOGIC_DELETE).equals(ClsConstants.DB_TRUE)) {
		sql += ", T."+ColumnConstants.FIELD_DELETED+" ";
	  }
	  sql += " FROM " + tableConfig.get(FN_ID_TRANSLATION_TABLE) + " T, " +
			 TableConstants.TABLE_LANGUAGE+" L " +
			 " WHERE T."+ColumnConstants.FN_LANG_ID_LANGUAGE+"= L."+ColumnConstants.FN_LANG_ID_LANGUAGE;
	} else {
	  sql = " SELECT T." + tableConfig.get(FN_ID_CODE_FIELD) + " AS "+ALIAS_CODE_FIELD+", " +
			" T." + tableConfig.get(FN_ID_DESCRIPTION_FIELD) + " AS "+ALIAS_DESC_FIELD+" ";
	  if(tableConfig.get(FN_FLAG_LOGIC_DELETE).equals(ClsConstants.DB_TRUE)) {
		sql += ", T."+ColumnConstants.FIELD_DELETED+" ";
	  }
	  sql += " FROM " + tableConfig.get(FN_ID_MASTER_TABLE) + " T ";
	}

	/* The hashtable htFilters must be filtered firstly
	to erase key fields which has a null or empty value
	*/
	eraseBlankValues();

	// We apply filters if there are filters to be applied

	if(!this.htFilters.isEmpty()){
	  sql+=" where ";
	  Enumeration e= htFilters.keys();

	  while(e.hasMoreElements()){
		String key=(String) e.nextElement();
		String value= (String)this.htFilters.get(key);
		value=value.replace('*','%');
		sql+= "UPPER("+key+") like '"+ value.toUpperCase()+"'";
		if (e.hasMoreElements()){
		  sql+=" and ";
		}
	  }//end while
	}
	sql = sql + " ORDER BY " + tableConfig.get(FN_ID_CODE_FIELD);
	try {
	  container = new RowsContainer();
	  if (container.query(sql)) vSearch ="1";

	} catch(ClsExceptions e) {
	  throw new ClsExceptions("No data found", "","","","");
	}
	return vSearch;
  }


  public void listAll(String mode) throws ClsExceptions {
    String sql = " ";
    if (tableConfig.get(FN_FLAG_USES_LANG).equals(ClsConstants.DB_TRUE)) {
      sql = " SELECT T." + tableConfig.get(FN_ID_CODE_FIELD) + " AS "+ALIAS_CODE_FIELD+", " +
            " T." + tableConfig.get(FN_ID_DESCRIPTION_FIELD) + " AS "+ALIAS_DESC_FIELD+", " +
            " T."+ColumnConstants.FN_LANG_ID_LANGUAGE + ", " + UtilidadesMultidioma.getCampoMultidioma("L."+ColumnConstants.FN_LANG_DESC_LANGUAGE, this.usrbean.getLanguage());
      if(tableConfig.get(FN_FLAG_LOGIC_DELETE).toString().equals(ClsConstants.DB_TRUE)) {
        sql += ", T."+ColumnConstants.FIELD_DELETED+" ";
      }
      sql += " FROM " + tableConfig.get(FN_ID_TRANSLATION_TABLE) + " T, " +
             TableConstants.TABLE_LANGUAGE+" L " +
             " WHERE T."+ColumnConstants.FN_LANG_ID_LANGUAGE+"= L."+ColumnConstants.FN_LANG_ID_LANGUAGE;
      if (mode.equals("edit")||mode.equals("query")) {
        sql += " AND T." + tableConfig.get(FN_ID_CODE_FIELD) + " = '" + data.get(tableConfig.get(FN_ID_CODE_FIELD)) + "' " +
               " AND T."+ColumnConstants.FN_LANG_ID_LANGUAGE+"= '" + data.get(ColumnConstants.FN_LANG_ID_LANGUAGE) + "' ";
      }
    } else {
      sql = " SELECT T." + tableConfig.get(FN_ID_CODE_FIELD) + " AS "+ALIAS_CODE_FIELD+", " +
            " T." + tableConfig.get(FN_ID_DESCRIPTION_FIELD) + " AS "+ALIAS_DESC_FIELD+" ";
      if(tableConfig.get(FN_FLAG_LOGIC_DELETE).equals(ClsConstants.DB_TRUE)) {
        sql += ", T."+ColumnConstants.FIELD_DELETED+" ";
      }
      sql += " FROM " + tableConfig.get(FN_ID_MASTER_TABLE) + " T ";
	  if (mode.equals("edit")||mode.equals("query")) {
        sql += " WHERE T." + tableConfig.get(FN_ID_CODE_FIELD) + " = '" + data.get(tableConfig.get(FN_ID_CODE_FIELD)) + "' ";
      }
	  sql = sql + " ORDER BY " + tableConfig.get(FN_ID_CODE_FIELD);
    }
    // Just one record!
	if (mode.equals("edit")||mode.equals("query")) {
      row = new Row();
      row.find(sql);
    } else {  // more than one records!
      container = new RowsContainer();
      container.setMaxSize(-1);
      container.query(sql);
    }
  }

  public int delete() throws ClsExceptions {
    int i =0;
	String sql="";
	Row rowDel = new Row();
	row.load(data);
	if (tableConfig.get(FN_FLAG_USES_LANG).equals(ClsConstants.DB_TRUE)){
	  sql = "SELECT * FROM "+ tableConfig.get(FN_ID_TRANSLATION_TABLE)+ " WHERE "+tableConfig.get(FN_ID_CODE_FIELD);
	  sql = sql+ " = "+data.get(tableConfig.get(FN_ID_CODE_FIELD));
	  if (rowDel.findForUpdate(sql)) {
		i = row.delete((String) tableConfig.get(FN_ID_TRANSLATION_TABLE), new String[]{(String)tableConfig.get(FN_ID_CODE_FIELD), ColumnConstants.FN_LANG_ID_LANGUAGE});
	  }
	} else {
	  sql = "SELECT * FROM "+ tableConfig.get(FN_ID_MASTER_TABLE)+ " WHERE "+tableConfig.get(FN_ID_CODE_FIELD);
	  sql = sql+ " = '"+data.get(tableConfig.get(FN_ID_CODE_FIELD))+"'";
	  if (rowDel.findForUpdate(sql)) {
		i = row.delete((String) tableConfig.get(FN_ID_MASTER_TABLE), new String[]{(String)tableConfig.get(FN_ID_CODE_FIELD)});
	  }
	}
	setAffectedRecords(i);
    return i;
  }

  public boolean add() throws ClsExceptions {
    boolean ok = true;
    String tableName;
    if(usrbean==null) {
		throw new ClsExceptions("Please, asign a NOT NULL value to attribute usrbean at class SIGAMasterTable before invoking add() method"); 
    }    	
    String usr=usrbean.getUserName();
    Object[] fields = new Object[] {};
    Vector vFields = new Vector();
    //aditional columns!
    if(row==null) row=new Row();
    row.load(data);
    row.setValue(ColumnConstants.FIELD_USER_MODIFICATION, usr);
    row.setValue(ColumnConstants.FIELD_DATE_MODIFICATION, "SYSDATE");

    vFields.add((String)tableConfig.get(FN_ID_CODE_FIELD));
    vFields.add(ColumnConstants.FIELD_USER_MODIFICATION);
    vFields.add(ColumnConstants.FIELD_DATE_MODIFICATION);
    if(tableConfig.get(FN_FLAG_LOGIC_DELETE).equals(ClsConstants.DB_TRUE)) {
      vFields.add(ColumnConstants.FIELD_DELETED);
    }
	tableName = (String) tableConfig.get(FN_ID_MASTER_TABLE);
	if (tableConfig.get(FN_FLAG_USES_LANG).equals(ClsConstants.DB_FALSE)) {
	  vFields.add((String) tableConfig.get(FN_ID_DESCRIPTION_FIELD));
	}
	fields = vFields.toArray();
	ok = (row.add(tableName, fields) == 1) ;

	//detail record adding!
	if (ok && tableConfig.get(FN_FLAG_USES_LANG).equals(ClsConstants.DB_TRUE)) {
	  tableName = (String) tableConfig.get(FN_ID_TRANSLATION_TABLE);
	  vFields.add((String) tableConfig.get(FN_ID_DESCRIPTION_FIELD));
	  vFields.add(ColumnConstants.FN_LANG_ID_LANGUAGE);
	  fields = vFields.toArray();
	  ok = (row.add(tableName, fields) == 1) ;
	}
	return ok;
  }

  /**
   * Some attributes can be modified
   * @return boolean
   * @throws ClsExceptions PSSC exception type
   */

	public boolean update() throws ClsExceptions {
		boolean ok = false;
    	String tableName;
	    
		if(usrbean==null) {
			throw new ClsExceptions("Please, asign a NOT NULL value to attribute usrbean at class SIGAMasterTable before invoking add() method"); 
		}    	

	    String usr=usrbean.getUserName();
	    Object[] updatableFields;
	    Object[] keyfields;
	
		ClsLogging.writeFileLog("Inside update method ",10);
	
		ClsLogging.writeFileLog("htrecord:"+this.data+", htrecordbackup:"+this.htRecordBackup,10);
		if ((this.data!=null)&&(this.htRecordBackup!=null)){
		  ClsLogging.writeFileLog("Before de load "+this.data.size()+", "+this.htRecordBackup.size(),10);
		}
		if(row==null) row=new Row();
		row.load(data);
	    row.setValue(ColumnConstants.FIELD_USER_MODIFICATION, usr);
	    row.setValue(ColumnConstants.FIELD_DATE_MODIFICATION, "SYSDATE");

	    // IF MASTER-DETAIL -- first updating the detail record	    
	    if (tableConfig.get(FN_FLAG_USES_LANG).equals(ClsConstants.DB_TRUE)) {
	      tableName = (String) tableConfig.get(FN_ID_TRANSLATION_TABLE);
	      keyfields = new String[] { 
	      	(String) tableConfig.get(FN_ID_CODE_FIELD),
			ColumnConstants.FN_LANG_ID_LANGUAGE 
			};
	    } else {
	      tableName = (String) tableConfig.get(FN_ID_MASTER_TABLE);
	      keyfields = new String[] { (String) tableConfig.get(FN_ID_CODE_FIELD)};
	    }

	if (this.htRecordBackup!=null) ClsLogging.writeFileLog("Before setcomparedata "+this.htRecordBackup.size(),10);
	row.setCompareData(this.htRecordBackup);

    if (tableConfig.get(FN_FLAG_LOGIC_DELETE).equals(ClsConstants.DB_TRUE)) {
      updatableFields=new String[] { 
      		(String)tableConfig.get(FN_ID_DESCRIPTION_FIELD),
        	ColumnConstants.FIELD_USER_MODIFICATION,
        	ColumnConstants.FIELD_DATE_MODIFICATION,
			ColumnConstants.FIELD_DELETED};
    } else {
      updatableFields=new String[] { 
      		(String)tableConfig.get(FN_ID_DESCRIPTION_FIELD),
        	ColumnConstants.FIELD_USER_MODIFICATION,
        	ColumnConstants.FIELD_DATE_MODIFICATION};
    }

    setAffectedRecords(row.update(tableName, keyfields, updatableFields));
	ClsLogging.writeFileLog("After update",10);

	if (getAffectedRecords()==0) {
	  Vector v=row.getRecordsChanged();
	  if(v.size()>0) {//Has had changes
		ClsLogging.writeFileLog("there were changes!!!",10);
		Enumeration en=v.elements();
		ClsLogging.writeFileLog("Changed elements:",10);
		ClsLogging.writeFileLog("**********",10);
		while (en.hasMoreElements()) {
		  com.atos.utils.PairsKeys obj = (com.atos.utils.PairsKeys)en.nextElement();
		  ClsLogging.writeFileLog("Key: "+obj.getIdObj(),10);
		  ClsLogging.writeFileLog("Value: "+obj.getValueObj(),10);
		  ClsLogging.writeFileLog("**********",10);
		}
		setVChangedRecords(v);
	  }
	}
	else
	{
	  ClsLogging.writeFileLog("No changes!!!",10);
	}

    ok = (getAffectedRecords() == 1);
    return ok;
  }

	/**
	 * @return
	 */
	public UsrBean getUsrbean() {
		return usrbean;
	}

	/**
	 * @param bean
	 */
	public void setUsrbean(UsrBean bean) {
		usrbean = bean;
	}

	/**
	 * @return
	 */
	public Vector getVChangedRecords() {
		return vChangedRecords;
	}

	/**
	 * @param vector
	 */
	public void setVChangedRecords(Vector vector) {
		vChangedRecords = vector;
	}

}
package com.atos.utils;
/**
 * <p>Title: ColumnConstants </p>
 * <p>Description: class that handles a zip file </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: SchLumbergerSema </p>
 * @author 
 * @version 1.0
 */


public class ColumnConstants {

// Mapeo de _TABLA
  public static final String _TABLA_BLA1 = "BLA1";
  
//MAPEO DE LOS CAMPOS GENERICOS BORRADO, USUARIO MODIFICACION y FECHA MODIFICACION 
	public static final String FIELD_DELETED="BORRADO";
	public static final String FIELD_USER_MODIFICATION="USUMODIFICACION";
	public static final String FIELD_DATE_MODIFICATION="FECHAMODIFICACION";

  
// Mapeo de la tabla de procesos
   public static final String FN_PROCESS_ID        = "IDPROCESO";
   public static final String FN_PROCESS_ID_PARENT = "IDPARENT";
   public static final String FN_PROCESS_ID_STREAM = "IDMODULO";
   public static final String FN_PROCESS_DESC      = "DESCRIPCION";
   public static final String FN_PROCESS_VIEW_AM   = "VISTAAM";
   public static final String FN_PROCESS_VIEW_AD   = "VISTAAD";
   public static final String FN_PROCESS_TRACE     = "TRAZA";
   public static final String FN_PROCESS_TRANS     = "TRANSACCION";
   public static final String FN_PROCESS_TARGET    = "TARGET";
   public static final String FN_PROCESS_USERMOD   = "USUMODIFICACION";
   public static final String FN_PROCESS_DATEMOD   = "FECHAMODIFICACION";
   public static final String FN_PROCESS_NIVEL     = "NIVEL";


  public static final String FN_PROFILE_ID_PROFILE="IDPERFIL";
  public static final String FN_PROFILE_DESC_PROFILE="DESCRIPCION";
  public static final String FN_PROFILE_PROFILE_LEVEL="NIVELPERFIL";
  
  // Mapeo de la tabla de accesos permitidos
  public static final String FN_ACCESS_RIGHT_VALUE  = "DERECHOACCESO";
  public static final String FN_ACCESS_RIGHT_PROCESS= FN_PROCESS_ID;
  public static final String FN_ACCESS_RIGHT_PROFILE= FN_PROFILE_ID_PROFILE;
  public static final String FN_ACCESS_RIGHT_INSTITUCION = "IDINSTITUCION";
  public static final String FN_ACCESS_RIGHT_USERMOD= "USUMODIFICACION";
  public static final String FN_ACCESS_RIGHT_DATEMOD= "FECHAMODIFICACION";
  

  public static final String FN_User_ID_USER = "IDUSER";
  public static final String FN_User_DESC_USER = "DESCRIPCION";
  public static final String FN_User_PASSWORD = "PASSWORD";
  public static final String FN_User_DATE_LAST_MODIFICATION = "DATE_LAST_MODIFICATION";
  public static final String FN_User_ID_LANGUAGE = "IDLENGUAJE";
  public static final String FN_User_ID_PROFILE = "IDPROFILE";
  public static final String FN_User_ID_AIRFORCE = "IDAIRFORCE";
  public static final String FN_User_ID_LOCATION = "IDLOCATION";
  public static final String FN_User_ACTIVATED = "STATUS";

  public static final String FN_LANG_ID_LANGUAGE="IDLENGUAJE";
  public static final String FN_LANG_DESC_LANGUAGE="DESCRIPCION";
  public static final String FN_LANG_DATE_MODIFICATION="FECHAMODIFICACION";
  public static final String FN_LANG_USER_MODIFICATION="USUMODIFICACION";

 //Mapeo de SIGA_USER_PARAM
  public static final String FN_UserParam_ID_USER="IDUSER";
  public static final String FN_UserParam_ID_PARAM="IDPARAM";
  public static final String FN_UserParam_DESC_PARAM="DESCRIPCION";
  public static final String FN_UserParam_VALUE="VALORPARAM";
  public static final String FN_UserParam_USER_MODIFICATION="USUMODIFICACION";
  public static final String FN_UserParam_DATE_MODIFICATION="FECHAMODIFICACION";
  
  //Mapeo de tabla ERROR_CATEGORIES
  public static final String FN_CATERROR_ID="IDCATEGORIAERROR";
  public static final String FN_CATERROR_PROCESS_ID=ColumnConstants.FN_PROCESS_ID;
  public static final String FN_CATERROR_DESC="DESCRIPCION";
  public static final String FN_CATERROR_DELETED="BORRADO";
  public static final String FN_CATERROR_DATE_MODIFICATION="FECHAMODIFICACION";
  public static final String FN_CATERROR_USER_MODIFICATION="USUMODIFICACION";
  
  public static final String FN_MERROR_TYPE_CATERROR_ID=ColumnConstants.FN_CATERROR_ID;
  public static final String FN_MERROR_TYPE_ID="IDTIPOERROR";
  public static final String FN_MERROR_TYPE_PERSISTENT="PERSISTENTE";
  public static final String FN_MERROR_TYPE_DELETED="BORRADO";
  public static final String FN_MERROR_DATE_MODIFICATION="FECHAMODIFICACION";
  public static final String FN_MERROR_USER_MODIFICATION="USUMODIFICACION";
  
  
  public static final String FN_DERROR_TYPE_CATERROR_ID=ColumnConstants.FN_MERROR_TYPE_CATERROR_ID;
  public static final String FN_DERROR_TYPE_ID=ColumnConstants.FN_MERROR_TYPE_ID;
  public static final String FN_DERROR_TYPE_ID_LANGUAGE=ColumnConstants.FN_LANG_ID_LANGUAGE;
  public static final String FN_DERROR_TYPE_DESC="DESCRIPCION";        
  public static final String FN_DERROR_TYPE_DELETED="BORRADO";                
  public static final String FN_DERROR_DATE_MODIFICATION="FECHAMODIFICACION";
  public static final String FN_DERROR_USER_MODIFICATION="USUMODIFICACION";
  
}

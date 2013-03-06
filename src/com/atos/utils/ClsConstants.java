/**
 * <p>Title: ClsConstants </p>
 * <p>Description: class that handles a zip file </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: SchLumbergerSema </p>
 * @author 
 * @version 1.0
 */

package com.atos.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

import org.redabogacia.sigaservices.app.util.PropertyReader;
import org.redabogacia.sigaservices.app.util.SIGAReferences.RESOURCE_FILES;



public class ClsConstants {
	
	
	// *****************************************************************
	//  S I G A
	// *****************************************************************
	
	// Idioma
	public static final String ESPANOL="ES";
	public static final String CATALAN="CA";
	public static final String EUSKERA="EU";
	public static final String GALLEGO="GL";
	
	public static final String ES="1";
	public static final String CA="2";
	public static final String EU="3";
	public static final String GL="4";
	
	
	// Configuración Interfaz
	public static final String RELATIVE_PATH_IMAGES="html/imagenes";
	public static final String RELATIVE_PATH_LOGOS="logos";
	public static final String PATH_ACCESO_DIRECTO_IMAGENES="imagenes";

	public static final String SEPARADOR = "	";
	// RGG 26/01/2005 Para fotografias de clientes
	public static final String RELATIVE_PATH_FOTOS="fotos";
	
    //	 Institucion por defecto
	public static final int INSTITUCION_POR_DEFECTO = 0;
	public static final int INSTITUCION_CGAE = 2000;
	public static final int INSTITUCION_CONSEJO = 3000;
	public static final int INSTITUCION_CONSEJOGENERAL = 2000;
	
	//	Tipo de identificación
	public static final int TIPO_IDENTIFICACION_NIF 		= 10;	
	public static final int TIPO_IDENTIFICACION_CIF 		= 20;	
	public static final int TIPO_IDENTIFICACION_TRESIDENTE 	= 40;	
	public static final int TIPO_IDENTIFICACION_PASAPORTE 	= 30;
	public static final int TIPO_IDENTIFICACION_OTRO	 	= 50;
	
	//Tipo parentésco
	public static final int TIPO_CONYUGE 					= 1;
	
	// Tipo persona
	//public static final String TIPO_IDPERSONA_FISICA	 	= "F";
	
	
	// Para la busqueda experta de criterios dinamicos
	public static final String TIPONUMERO	= "%%NUMERO%%";
	public static final String TIPOTEXTO	= "%%TEXTO%%";
	public static final String TIPOFECHA	= "%%FECHA%%";
	public static final String TIPOMULTIVALOR		= "%%MULTIVALOR@";
	
	public static final String ETIQUETASELECTOPEN	= "<SELECT>";
	public static final String ETIQUETASELECTCLOSE	= "</SELECT>";
	public static final String ETIQUETAFROMOPEN	    = "<FROM>";
	public static final String ETIQUETASFROMCLOSE	= "</FROM>";
	public static final String ETIQUETAWHEREOPEN	= "<WHERE>";
	public static final String ETIQUETASWHERECLOSE	= "</WHERE>";
	public static final String ETIQUETAGROUPBYOPEN	= "<GROUPBY>";
	public static final String ETIQUETASGROUPBYCLOSE= "</GROUPBY>";
	public static final String ETIQUETAORDERBYOPEN	= "<ORDERBY>";
	public static final String ETIQUETASORDERBYCLOSE= "</ORDERBY>";
	public static final String ETIQUETAOPERADOR   	= "%%OPERADOR%%";
	public static final String CONSTANTESUSTITUIRCOMILLAS 	= "#@#";
	public static final String ETIQUETAUNIONOPEN	    = "<UNION>";
	public static final String ETIQUETAUNIONCLOSE	    = "</UNION>";
	public static final String ETIQUETAUNIONALLOPEN	    = "<UNIONALL>";
	public static final String ETIQUETAUNIONALLCLOSE	= "</UNIONALL>";

	//	Estado de la solicitud de incorporación de colegiado
	public static final int ESTADO_SOLICITUD_PENDIENTE_DOC 		= 10;	
	public static final int ESTADO_SOLICITUD_PENDIENTE_APROBAR 	= 20;
	public static final int ESTADO_SOLICITUD_DENEGADA 			= 30;	
	public static final int ESTADO_SOLICITUD_SUSPENDIDA 		= 40;
	public static final int ESTADO_SOLICITUD_APROBADA 			= 50;	
	public static final int ESTADO_SOLICITUD_APUNTODECADUCAR    = 100;	
	public static final int ESTADO_SOLICITUD_CADUCADA           = 200;
	
	public static final String SOLICITUD_APUNTODECADUCAR 		="A punto de Caducar";	
	public static final String SOLICITUD_CADUCADA   		    ="Caducada";	

	//	Tipo de solicitud de incorporación de colegiado
	public static final int TIPO_SOLICITUD_EJERCIENTE_REINCORPORACION 	= 10;
	public static final int TIPO_SOLICITUD_NOEJERCIENTE_REINCORPORACION = 20;	
	public static final int TIPO_SOLICITUD_EJERCIENTE_INCORPORACION 	= 30;
	public static final int TIPO_SOLICITUD_NOEJERCIENTE_INCORPORACION 	= 40;	

	//	Tipo de colegiación
	public static final int TIPO_COLEGIACION_ESPANHOL 		= 10; 
	public static final int TIPO_COLEGIACION_COMUNITARIO 	= 20;
	
	// Estados colegiales
	public static final int ESTADO_COLEGIAL_SINEJERCER 		= 10;
	public static final int ESTADO_COLEGIAL_EJERCIENTE		= 20;
	public static final int ESTADO_COLEGIAL_BAJACOLEGIAL	= 30;
	public static final int ESTADO_COLEGIAL_INHABILITACION	= 40;
	public static final int ESTADO_COLEGIAL_SUSPENSION	= 	  50;
	public static final int ESTADO_COLEGIAL_ALTA	= 	  1020;// Correspnde a una combinacion del estado 10 y 20;
	
	// Tipo cambio histórico de censo
	public static final int TIPO_CAMBIO_HISTORICO_ESTADO_ALTA_COLEGIAL	= 1;
	public static final int TIPO_CAMBIO_HISTORICO_ESTADO_BAJA_COLEGIAL	= 2;
	public static final int TIPO_CAMBIO_HISTORICO_ESTADO_ALTA_EJERCICIO	= 3;
	public static final int TIPO_CAMBIO_HISTORICO_ESTADO_BAJA_EJERCICIO	= 4;
	public static final int TIPO_CAMBIO_HISTORICO_ESTADO_INHABILITACION	= 5;
	public static final int TIPO_CAMBIO_HISTORICO_ESTADO_SUSPENSION		= 6;
	public static final int TIPO_CAMBIO_HISTORICO_DATOS_GENERALES		= 10;
	public static final int TIPO_CAMBIO_HISTORICO_DATOS_COLEGIALES		= 20;
	public static final int TIPO_CAMBIO_HISTORICO_DIRECCIONES			= 30;
	public static final int TIPO_CAMBIO_HISTORICO_CUENTAS_BANCARIAS		= 40;
	public static final int TIPO_CAMBIO_HISTORICO_DATOS_CV				= 50;
	public static final int TIPO_CAMBIO_HISTORICO_DATOS_COMPONENTES		= 60;
	public static final int TIPO_CAMBIO_HISTORICO_DATOS_FACTURACION		= 70;
	public static final int TIPO_CAMBIO_HISTORICO_TURNOS				= 80;
	public static final int TIPO_CAMBIO_HISTORICO_EXPEDIENTES			= 90;
	
	public static final String HISTORICO_REGISTRO_ELIMINADO 			= "Registro eliminado";

    // Tipo direcciones que tienen logica asociada
	public static final int TIPO_DIRECCION_CENSOWEB		= 3;
	public static final int TIPO_DIRECCION_DESPACHO		= 2;
	public static final int TIPO_DIRECCION_GUIA	    	= 5;
	public static final int TIPO_DIRECCION_GUARDIA  	= 6;
	public static final int TIPO_DIRECCION_FACTURACION  = 8;
	public static final int TIPO_DIRECCION_PUBLICA		= 4;
	
	// Tipo Institucion
	public static final int TIPO_INTITUCION_CGAE	            = 1;
	public static final int TIPO_INTITUCION_CONSEJO_AUTONOMICO	= 2;
	public static final int TIPO_INTITUCION_COLEGIO	            = 3;
	

	// Tipos solicitudes de modificacion especificas
	public static final int TIPO_SOLICITUD_MODIF_DATOS_GENERALES	= 10;
	public static final int TIPO_SOLICITUD_MODIF_DIRECCIONES	    = 30;
	public static final int TIPO_SOLICITUD_MODIF_EXP_FOTO		    = 35;
	public static final int TIPO_SOLICITUD_MODIF_CUENTAS_BANCARIAS	= 40;	
	public static final int TIPO_SOLICITUD_MODIF_DATOS_CV			= 50;
	public static final int TIPO_SOLICITUD_MODIF_DATOS_FACTURACION	= 70;
	public static final int TIPO_SOLICITUD_MODIF_EXPEDIENTES	    = 90;
	
	// Estado solicitudes de modificacion
	public static final int ESTADO_SOLICITUD_MODIF_PENDIENTE	     = 10;
	public static final int ESTADO_SOLICITUD_MODIF_REALIZADA	     = 20;
	public static final int ESTADO_SOLICITUD_MODIF_DENEGADA	         = 30;
	
	// Tipo de Usuario empleado cuando el sistema actualiza automaticamente el histórico
	public static final int USUMODIFICACION_AUTOMATICO	= 0;
	
	
	// Formas de pago de Productos y Servicios
	public static final String TIPO_PAGO_INTERNET				= "I";
	public static final String TIPO_PAGO_SECRETARIA				= "S";
	public static final String TIPO_PAGO_INTERNET_SECRETARIA	= "A";
	
	// Tipos de formas de pago de Productos y Servicios
	public static final int TIPO_FORMAPAGO_TARJETA	= 10;
	public static final int TIPO_FORMAPAGO_FACTURA	= 20;
	public static final int TIPO_FORMAPAGO_METALICO	= 30;
	//LMS 04/09/2006
	//Añado los que faltan.
	public static final int TIPO_FORMAPAGO_TRANSFERENCIA = 40;
	public static final int TIPO_FORMAPAGO_CHEQUE_TALON = 50;
	public static final int TIPO_FORMAPAGO_EFECTOS = 60;
	public static final int TIPO_FORMAPAGO_RECIBO_CUENTA = 70;
	
	// Estado Productos
	public static final String PRODUCTO_PENDIENTE	= "P";
	public static final String PRODUCTO_ACEPTADO	= "A";
	public static final String PRODUCTO_DENEGADO	= "D";
	public static final String PRODUCTO_BAJA		= "B";
	public static final String PRODUCTO_PENDIENTE_GENERAR_PDF= "G";
	
	// Estado peticion compra
	public static final int ESTADO_PETICION_COMPRA_PENDIENTE	= 10;
	public static final int ESTADO_PETICION_COMPRA_PROCESADA	= 20;
	public static final int ESTADO_PETICION_COMPRA_BAJA	        = 30;
	
	// Tipo de peticion compra
	public static final String TIPO_PETICION_COMPRA_ALTA	= "A";
	public static final String TIPO_PETICION_COMPRA_BAJA	= "B";
	
	// MAV 06/05/2005
	// Tipo de Abono
	public static final String TIPO_ABONO_FACTURACION	= "FC";
	public static final String TIPO_ABONO_JUSTICIA_GRATUITA	= "JG";
	
	// Estado peticion compra
	public static final String INICIOFINALPONDERADO_INICIO	= "I";
	public static final String INICIOFINALPONDERADO_FINAL	= "F";
	public static final String INICIOFINALPONDERADO_PONDERADO	= "P";

	// Cuando se paga el producto o servicio solicitado
	public static final String MOMENTO_CARGO_SOLICITUD		= "S";
	public static final String MOMENTO_CARGO_PROXFACTURA	= "P";
	
	// Tipo  de Cuenta Bancaria
	public static final String TIPO_CUENTA_ABONO		= "A";
	public static final String TIPO_CUENTA_CARGO		= "C";
	public static final String TIPO_CUENTA_ABONO_CARGO	= "T";
	
	// relativo a etiquetas
	public static final String NOMBRE_PLANTILLA_ETIQUETA	= "plantillaEtiqueta.fo";
	public static final String ANCHO_ETIQUETA				= "6.5";
	public static final String ALTO_ETIQUETA				= "2.6";
			
	// Tipo  de caracer RGG
	public static final String TIPO_CARACTER_PRIVADO	= "P";
	public static final String TIPO_CARACTER_SEMIPUBLICO = "S";
	public static final String TIPO_CARACTER_PUBLICO	= "U";
	public static final String TIPO_CARACTER_PRIVADO_COLEGIADO	= "A";
	
	// Fcatura/abono Contabilizada
	public static final String FACTURA_ABONO_CONTABILIZADA	= "S";
	public static final String FACTURA_ABONO_NO_CONTABILIZADA = "N";
	// Monedas
	public static final String EURO		= "978";

			
	// Parametros de GenParametros
	public static final String PLAZO_EN_DIAS_ENTREGA_DOCSOLICITUD = "PLAZO_EN_DIAS_ENTREGA_DOC_SOLICITUD";     
	public static final String PLAZO_EN_DIAS_SALTAALARMA_ENTREGA_DOCSOLICITUD = "PLAZO_EN_DIAS_SALTA_ALARMA_ENTREGA_DOC_SOLICITUD";   
	public static final String MODULO_CENSO = "CEN";  
	public static final String MODULO_GENERAL = "GEN";
	public static final String MODULO_FACTURACION = "FAC";	
	public static final String MODULO_SJCS = "SCS";
	public static final String MODULO_FACTURACION_SJCS = "FCS";
	public static final String MODULO_PRODUCTOS = "PYS";
	public static final String MODULO_CERTIFICADOS = "CER";	
	public static final String PATH_FOTOS = "PATH_FOTOS";  
	public static final String PATH_APP = "PATH_APP";
	public static final String URL_TPV = "URL_TPV";
	public static final String MERCHANTID = "MERCHANTID";
	public static final String ACQUIREBIN = "ACQUIREBIN";
	public static final String TERMINALID = "TERMINALID";
	public static final String PASSWORD_FIRMA = "PASSWORD_FIRMA";
	public static final String SALTOS = "S";
	public static final String COMPENSACIONES = "C";
	public static final String OCULTAR_MOTIVO_MODIFICACION = "OCULTAR_MOTIVO_MODIFICACION";
	public static final String EXPORTAR_COLEGIADOS_ACOGIDOS_A_LOPD = "EXPORTAR_COLEGIADOS_ACOGIDOS_A_LOPD";
	
	
	
	//El idmodulo de facturacion en numero
	public static final int IDMODULOFACTURACION = 6;

	// tipos preferente RGG 04-02-2005
	public static final String TIPO_PREFERENTE_CORREO = "C";  
	public static final String TIPO_PREFERENTE_CORREOELECTRONICO = "E";  
	public static final String TIPO_PREFERENTE_FAX = "F";  
	public static final String TIPO_PREFERENTE_SMS = "S"; // jbd 04/06/2009
	
	// UserBean
	public static final String USERBEAN	= "USRBEAN";
	// *****************************************************************

	public static final String FILE_SEP = System.getProperty("file.separator");
	public static final String FILE_SEP_REP = (FILE_SEP.equals("\\")?"\\\\":"/");
	public static final String FILE_SEP_TO_REPLACE = (FILE_SEP.equals("\\")?"/":"\\\\");
	private static final String SEP_PATTERN="//";
	private static final String REPLACE_PATTERN=(FILE_SEP.equals("\\"))?FILE_SEP+FILE_SEP:FILE_SEP;
	
	
//	public static final String initPropsFile=System.getProperty("slbutils.initprops");	
	public static final String initPropsFile="INIT";	
	//public static final ResourceBundle initProperties=ResourceBundle.getBundle(initPropsFile);
	public static final Properties initProperties=PropertyReader.getProperties(RESOURCE_FILES.INIT);

	public static final String USER_DIR = System.getProperty("user.dir");
//	public static final String RES_DIR = "C://eclipse//workspace//SIGA92//WebContent";
	public static final String RES_DIR = initProperties.getProperty("init.application.path");

	public static final String APPLICATION_NAME = initProperties.getProperty("init.application.name");
  	public static final String DEFAULT_LANGUAGE= initProperties.getProperty("init.default.language");
	public static final String PATH_DOMAIN = initProperties.getProperty("init.application.domain");
	
	//Mascara para valores numericos obtenidos en informes
	public static final String FORMATONUMERO 	= "99999999999999999990D09";
  

/*  public static final String RES_PROP_DOMAIN =
      "config" + FILE_SEP + PATH_DOMAIN + FILE_SEP +
      "applications";
*/
  public static final String RES_PROP_DOMAIN=initProperties.getProperty("init.path.resprop.domain").replaceAll(SEP_PATTERN, REPLACE_PATTERN);
  public static final String RES_APP_PATH = initProperties.getProperty("init.path.res.app").replaceAll(SEP_PATTERN, REPLACE_PATTERN);

	  public static final String PROPERTIES = initProperties.getProperty("init.path.resources").replaceAll(SEP_PATTERN, REPLACE_PATTERN);
//	  public static final String RESOURCES_DIR_STRUTS = RES_DIR + RES_PROP_DOMAIN + RES_APP_PATH;
//	  public static final String RESOURCES_DIR = RES_DIR + RES_PROP_DOMAIN + PROPERTIES;
	
	  public static final String FOP_FONTS = initProperties.getProperty("init.path.fop.fonts").replaceAll(SEP_PATTERN, REPLACE_PATTERN);
	  public static final String FOP_FILE = initProperties.getProperty("init.fop.config").replaceAll(SEP_PATTERN, REPLACE_PATTERN);
//	  public static final String FOP_FONTS_DIR = RES_DIR + RES_PROP_DOMAIN + FOP_FONTS;
//	  public static final String FOP_CONFIG_FILE = RES_DIR + RES_PROP_DOMAIN + PROPERTIES + FOP_FILE;
	  
	  
	  public static final String IMAGESVIDEOUPLOAD = "";
		
	  //Database Constants
	  public static final String DB_TRUE=initProperties.getProperty("init.database.flag.true");
	  public static final String DB_FALSE=initProperties.getProperty("init.database.flag.false");
	  public static final String defaultORADateFormat=initProperties.getProperty("init.database.date.format.default");
	  public static final String defaultORALongDateFormat=initProperties.getProperty("init.database.date.format.long");
	  public static final String DATE_FORMAT_SQL=initProperties.getProperty("init.database.sql.date.format");
	  public static final String TIMESTAMP_FORMAT_SQL=initProperties.getProperty("init.database.sql.timestamp.format");
	  // RGG 29-12-2004
	  public static final String TIPO_PERSONA_FISICA="F";
	  public static final String TIPO_PERSONA_JURIDICA="J";
	  public static final String TIPO_SEXO_HOMBRE="H";
	  public static final String TIPO_SEXO_MUJER="M";
	  // RGG 13-01-2005
	  public static final String TIPO_CARGO_BANCO="B";
	  public static final String TIPO_CARGO_CAJA="C";

	  public static final String TIPO_CLIENTE_INSTITUCION="10";
	  public static final String TIPO_CLIENTE_COLEGIADO="20";
	  public static final String TIPO_CLIENTE_NOCOLEGIADO="30";
	  public static final String TIPO_CLIENTE_COLEGIADO_BAJA="40";
	  public static final String TIPO_CLIENTE_LETRADO="50";
	  
	  public static final String TIPO_ACCESO_PESTANAS_COLEGIADO="1";
	  public static final String TIPO_ACCESO_PESTANAS_NOCOLEGIADO="8"; // 8
	  public static final String TIPO_ACCESO_PESTANAS_FACTURACION_NOCOLEGIADO="11";
	  public static final String TIPO_ACCESO_PESTANAS_NOCOLEGIADO_FISICO="2";
	  public static final String TIPO_ACCESO_PESTANAS_LETRADO="16";
	  
	  //Tratamiento por defectos para clientes
	  public static final int TRATAMIENTO_DESCONOCIDO=1;

	  // RGG 07-02-2005
	  public static final int MAX_DIAS_DOCUMENTACION_ENTREGA=365;
	  
	  
	  
	  // hitos generales de facturacion
	  public static final int HITO_GENERAL_TURNO=10;
	  public static final int HITO_GENERAL_GUARDIA=20;
	  public static final int HITO_GENERAL_SOJ=30;
	  public static final int HITO_GENERAL_EJG=40;

	  // si es facturacion o pago
	  public static final String FACTURACION_SJCS="F";
	  public static final String PAGOS_SJCS="P";

	  // se ha facturado todo el procedimiento o solo un porcentaje
	  public static final String FACTURACION_COMPLETA="C";
	  public static final String FACTURACION_PARCIAL="P";
	  
	  // si el tipo de retención es LEC u OTRAS
	  public static final String TIPO_RETENCION_LEC="L";
	  public static final String TIPO_RETENCION_OTRAS="O";
	  
	  public static final String TIPO_RETENCION_PORCENTAJE="P";
	  public static final String TIPO_RETENCION_IMPORTEFIJO="F";
	  
	  //un espacio y un euro, para facilitar el trabajo
	  public static final String CODIGO_EURO=" \u0080";

	  // RGG nombres de los ficheros de plantillas de fo de informes SJCS
	  public static final String PLANTILLA_FO_FICHAFACTURACION="fichaFacturacion";
	  public static final String PLANTILLA_FO_FICHAPAGO="fichaPago";
	  public static final String PLANTILLA_FO_COLEGIADOPAGO="colegiadoPago";
	  public static final String PLANTILLA_FO_CERTIFICADOPAGO="certificadoPago";
	  public static final String PLANTILLA_FO_COLATURNOS="colaTurnos";
	  public static final String PLANTILLA_FO_COLAGUARDIAS="colaGuardias";
	  public static final String PLANTILLA_FO_SOLICITUDASISTENCIA="solicitudAsistencia";
	  
	  
	  public static final String TIPO_IDTIPOENVIO_TELEMATICO = "6";
	  

	  
	  //Java Date format constants 
	  /**
	   * #DATE_FORMAT_JAVA
	   * Standard intern date format :
	   *    yyyy/MM/dd HH:mm:ss
	   */
	  public static final String DATE_FORMAT_JAVA=			 "yyyy/MM/dd HH:mm:ss";
	  public static final String TIME_FORMAT_JAVA =			 "HH:mm:ss";
	  public static final String TIMESTAMP_FORMAT_JAVA = 	 "yyyy/MM/dd HH:mm:ss.SSS";
	  public static final String DATE_FORMAT_SHORT_ENGLISH = "dd/MM/yyyy";
	  public static final String DATE_FORMAT_SHORT_SPANISH = "dd/MM/yyyy";
	  public static final String DATE_FORMAT_MEDIUM_ENGLISH= "dd/MM/yyyy HH:mm";
	  public static final String DATE_FORMAT_MEDIUM_SPANISH= "dd/MM/yyyy HH:mm";
	  public static final String DATE_FORMAT_LONG_ENGLISH =  "dd/MM/yyyy HH:mm:ss";
	  public static final String DATE_FORMAT_LONG_SPANISH =  "dd/MM/yyyy HH:mm:ss";

/*
HTML Hyper Text Markup Language
TXT Text Documents
XML Extensible Markup Language
XSL Extensible Stylesheet Language
CSS Cascading Style Sheets
BMP Bitmap Image
DOC Microsoft Word Document
XLS Microsoft Excel Document
MDB Microsoft Access Database
*/


  public static final String Content_Type_HTML="text/html";
  public static final String Content_Type_TXT="text/txt";
  public static final String Content_Type_XML="text/xml";
  public static final String Content_Type_XSL="text/xsl";
  public static final String Content_Type_CSS="text/css";
  public static final String Content_Type_BMP="text/bmp";
  public static final String Content_Type_DOC="text/doc";
  public static final String Content_Type_XLS="text/xls";
  public static final String Content_Type_MDB="text/mdb";
  public static final String Content_Type_ZIP="application/x-zip-compressed";


  //Pago de SJCS:
  public static final String ESTADO_PAGO_ABIERTO = "10";
  public static final String ESTADO_PAGO_EJECUTADO = "20";
  public static final String ESTADO_PAGO_CERRADO = "30";
  public static final String CRITERIOS_PAGO_FACTURACION = "F";
  public static final String CRITERIOS_PAGO_PAGOS = "P";
  
  // RGG 05-04-2005 para el nombre de un parametro
  public static final String PATH_IMPRESO190 = "PATH_IMPRESO190";
  public static final String TIPO_SOPORTE190 = "SOPORTE_IMPRESO190";
  public static final String IMPRESO190_ENCODING =  "ISO-8859-1";
  
  
  // RGG 05-04-2005 PARA LOS CODIGOS DE PROVINCIA ESPECIALES
  public static final String CODIGO_PROVINCIA_CEUTA = "51";
  public static final String CODIGO_PROVINCIA_MELILLA = "52";

  public static final String TIPO_SOPORTE_CARTUCHO = "C";
  public static final String TIPO_SOPORTE_TELEMATICO = "T";
  public static final String TIPO_SOPORTE_DISQUETE = "D";
  
  // Mensaje de informacion en la solicitud de incorporacion. Es configurable por institucion:
  public static final String MENS_SOLICITUD = "MENS_SOLICITUD";
  
  // COMBO de Tipos de Registro de Personas no colegiadas.
  public static final String COMBO_TIPO_SOCIEDAD_SERVICIOS_JURIDICOS = "S";  
  public static final String COMBO_TIPO_SOCIEDAD_CIVIL 	  = "G";
  public static final String COMBO_TIPO_SOCIEDAD_LIMITADA = "B";
  public static final String COMBO_TIPO_SOCIEDAD_ANONIMA  = "A";
  public static final String COMBO_TIPO_PERSONAL 		  = "1";
  public static final String COMBO_TIPO_OTROS 		  	  = "0";
  public static final String COMBO_TIPO_SOCIEDAD_PROFESIONAL = "P";
  public static final String COMBO_TIPO_SOCIEDAD_MULTIDISCIPLINAR = "M";
  // COMBO de tipos de residencia para los clientes letrados
  
  public static final String COMBO_SIN_RESIDENCIA = "S";  
  public static final String COMBO_RESIDENCIA_MULTIPLE 	  = "M";

  
  // Estado de las acreditaciones (Actuaciones de Designas):
  public static final int ESTADO_ACREDITACION_INICIO         = 1;
  public static final int ESTADO_ACREDITACION_FINAL          = 2;
  public static final int ESTADO_ACREDITACION_COMPLETA       = 3;
  public static final int ESTADO_ACREDITACION_REGULARIZACION = 4;
  
//Estado de las solicitudes de los certificados:
  /*public static final String ESTADO_SOLCERT_PENDIENTE   = "1";
  public static final String ESTADO_SOLCERT_APROBADO   = "2";
  public static final String ESTADO_SOLCERT_ENVPROG = "3";
  public static final String ESTADO_SOLCERT_FINALIZADO = "4";
  public static final String ESTADO_SOLCERT_DENEGADO = "5";*/
 
  
  // Tipo del Periodo de los Días de Guardias
  public static final String TIPO_PERIODO_DIAS_GUARDIA_DIAS_NATURALES = "D";
  public static final String TIPO_PERIODO_DIAS_GUARDIA_SEMANAS_NATURALES = "S";
  public static final String TIPO_PERIODO_DIAS_GUARDIA_QUINCENAS_NATURALES = "Q";
  public static final String TIPO_PERIODO_DIAS_GUARDIA_MESES_NATURALES = "M";
  
  // Tipo de Días de la Guardia
  public static final String TIPO_DIAS_GUARDIA_TODOS = "T";
  public static final String TIPO_DIAS_GUARDIA_LABORABLES = "L";
  public static final String TIPO_DIAS_GUARDIA_FESTIVOS = "F";
  public static final String TIPO_DIAS_GUARDIA_FESTIVOS_SABADOS_DOMINGOS = "D";
//Identificador de contadores para la gestion de nuevos numeros de registro.
  
  
//Contadores de numEJG y numSOJ  
  public static final String EJG = "EJG";
  public static final String SOJ = "SOJ";
  
// Estado Actividad Letrado  
  public static final String ESTADO_LETRADO_ACTIVO = "10";
  public static final String ESTADO_LETRADO_BAJA = "20";
  
  // Tipo de Días de la Guardia
  public static final String LENGUAJE_ESP = "1";

  public static final String SOCIEDADSJ="SOCIEDADSJ";
  public static final String SOCIEDADSP="SSPP";
  public static final String FAC_ABONOS="FAC_ABONOS";
  
  
    
  // Tipos de Cambio ColaCambioLetrado
  public static final int COLA_CAMBIO_LETRADO_APROBACION_COLEGIACION = 10;
  public static final int COLA_CAMBIO_LETRADO_ACTIVACION_RESIDENCIA  = 20;
  public static final int COLA_CAMBIO_LETRADO_MODIFICACION_DIRECCION = 30;
  public static final int COLA_CAMBIO_LETRADO_BORRADO_DIRECCION = 40;
  public static final int COLA_CAMBIO_LETRADO_LOPD = 50;

  public static final String ID_PAIS_ESPANA ="191";
  
  // idtipoCV=4 para consultar las comisiones
  public static final int TIPOCV_COMISIONES=4;
  
  // Asignacion Conceptos Facturables / Contabilidad
  public static final String ASIGNACION_CONCEPTOS_CONTABILIDAD_CONFIGURACION_FIJO = "F";
  public static final String ASIGNACION_CONCEPTOS_CONTABILIDAD_CONFIGURACION_INCORPORAR_SUBCUENTA_CLIENTE = "C";
  public static final String ASIGNACION_CONCEPTOS_CONTABILIDAD_CONFIGURACION_INCORPORAR_SUBCUENTA_PYS = "P";
  
  //tipos de operadores
  public static final int ESVACIO_ALFANUMERICO = 20;
  public static final int ESVACIO_FECHA = 21;
  public static final int ESVACIO_NUMERICO= 22;
    
  //constante donde se mete el tipo de paginacion para las jsp (request.setAtributte(PARAM_PAGINACION,"PAGINACIONMODAL");)
  public static final String PARAM_PAGINACION="paginacion";
  
  //tipos de parametros de GenParametrosAdm
  //parametro para excluir los ejg denegados del informe de justificacion
  public static final String GEN_PARAM_INCLUIR_EJG_NOFAVORABLE="JUSTIFICACION_INCLUIR_EJG_NOFAVORABLE";
  public static final String GEN_PARAM_INCLUIR_EJG_PTECAJG="JUSTIFICACION_INCLUIR_EJG_PTECAJG";
  public static final String GEN_PARAM_INCLUIR_SIN_EJG="JUSTIFICACION_INCLUIR_SIN_EJG";
  public static final String GEN_PARAM_INCLUIR_EJG_SIN_RESOLUCION="JUSTIFICACION_INCLUIR_EJG_SIN_RESOLUCION";
  public static final String GEN_PARAM_MENSAJE_RESPONSABILIDAD_LETRADO="JUSTIFICACION_MENSAJE_RESPONSABILIDAD_LETRADO";
  public static final String GEN_PARAM_DESTACAR_EJG_NO_FAVORABLES="JUSTIFICACION_DESTACAR_EJG_NO_FAVORABLES";
  
  public static final String GEN_PARAM_IDTIPOCV_JUNTASGOBIERNO="IDTIPOCV_JUNTASGOBIERNO";
  
  
//parametro para aplicar logicade acreditaciones anterires a 2005
  public static final String GEN_PARAM_FACT_JA_2005="JUSTIFICACION_APLICAR_ACREDITACIONES_2005";
  public static final String GEN_PARAM_DELITOS_VE="DELITOS_VOLANTE_EXPRES";
  
  //Parametro que permite validar las guardias por parte de los colegiados desde la ficha colegial
  public static final String GEN_PARAM_VALIDA_GUARDIAS_COLEGIADO="VALIDA_GUARDIAS_COLEGIADO";
  //Parametro que dice cual es el colegio por defecto del EJG
  
  public static final String GEN_PARAM_TIPO_EJG_COLEGIO="TIPO_EJG_COLEGIO";
  
  public static final String PERMITIR_FACTURA_CERTIFICADO="PERMITIR_FACTURA_CERTIFICADO";
  
  public static String IDCAMPO_PARAPESTANACONF1="14";
  public static String IDCAMPO_PARAPESTANACONF2="15";
  
  // proceso RegTel
  public static String IDPROCESO_REGTEL_EXP="314";
  public static String IDPROCESO_REGTEL_EJG="94U";
  public static String IDPROCESO_REGTEL_CENSO="222";
  
  //public static final String GEN_PARAM_EEJG="EEJG";
  // Cambiado por la inc-6980
  public static final String GEN_PARAM_EEJG="ACTIVAR_SOLICITUD_EXP_ECONOMICO_JG";
  
  public static final String ACTIVAR_MENSAJE_DOCRESOLUCION_COLEGIADO="ACTIVAR_MENSAJE_DOCRESOLUCION_COLEGIADO";  
  public static final String GEN_PARAM_NUM_MESES_PAGO_TRAMO_LEC="NUM_MESES_PAGO_TRAMO_LEC";
  
  public static int MAX_SIZE_IMAGE_EMBEBED=100000;
  
  
//Permisos de Archivar.
  public static final String COMBO_MOSTRAR_SINARCHIVAR = "1";
  public static final String COMBO_MOSTRAR_ARCHIVADAS = "2";
  public static final String PERMISO_ARCHIVARSANCIONES = "56a";  
  
  //Tipos de fechas 
  public static final String COMBO_MOSTRAR_VACIO = "VACIO";
  public static final String COMBO_MOSTRAR_ACUERDO = "ACUERDO";
  public static final String COMBO_MOSTRAR_INICIO = "INICIO";
  public static final String COMBO_MOSTRAR_IMPOSICION= "IMPOSICIÓN";
  public static final String COMBO_MOSTRAR_FIRMEZA= "FIRMEZA";
  public static final String COMBO_MOSTRAR_FIN= "FIN";
  public static final String COMBO_MOSTRAR_REHABILITADO= "REHABILITADO";
  public static final String COMBO_MOSTRAR_RESOLUCION= "RESOLUCIÓN";
  public static final String COMBO_MOSTRAR_GENERACION= "GENERACIÓN";
  public static final String COMBO_MOSTRAR_ENVIO= "ENVIO";
  public static final String COMBO_MOSTRAR_RECEPCION= "RECEPCIÓN";
  
  
  //Estados de Remesa
  
    public static final int ESTADO_REMESA_GENERADA= 1;
    public static final int ESTADO_REMESA_ENVIADA= 2;
    public static final int ESTADO_REMESA_RECIBIDA= 3;

    public static final String GEN_PARAM_ACEPTAR_SOLICITUD_BAJA_TEMPORAL="ACEPTAR_SOLICITUD_BAJA_TEMPORAL";
	
	public static final String PARAM_ESFICHACOLEGIAL = "esFichaColegial";
	
	
	//IDCAMPOS para tipos de expedientes
	public static final int IDCAMPO_TIPOEXPEDIENTE_N_DISCIPLINARIO = 2;
	public static final int IDCAMPO_TIPOEXPEDIENTE_ESTADO = 3;
	public static final int IDCAMPO_TIPOEXPEDIENTE_INSTITUCION = 4;
	public static final int IDCAMPO_TIPOEXPEDIENTE_ASUNTOJUDICIAL = 5;
	public static final int IDCAMPO_TIPOEXPEDIENTE_ALERTAS = 6;
	public static final int IDCAMPO_TIPOEXPEDIENTE_DOCUMENTACION = 7;
	public static final int IDCAMPO_TIPOEXPEDIENTE_SEGUIMIENTO = 8;
	public static final int IDCAMPO_TIPOEXPEDIENTE_DENUNCIANTE = 9;
	public static final int IDCAMPO_TIPOEXPEDIENTE_PARTES = 10;
	public static final int IDCAMPO_TIPOEXPEDIENTE_RESOLUCION = 11;
	public static final int IDCAMPO_TIPOEXPEDIENTE_MINUTA_INICIAL = 12;
	public static final int IDCAMPO_TIPOEXPEDIENTE_RESULTADO_INFORME = 13;
	public static final int IDCAMPO_TIPOEXPEDIENTE_DENUNCIADO = 16;	
	public static final int IDCAMPO_TIPOEXPEDIENTE_MINUTA_FINAL = 17;
	public static final int IDCAMPO_TIPOEXPEDIENTE_DERECHOS_COLEGIALES = 18;
	public static final int IDCAMPO_TIPOEXPEDIENTE_SOLICITANTEEJG = 19;
	public static final int IDCAMPO_TIPOEXPEDIENTE_RELACIONEXPEDIENTE = 20;	

	 // proceso RegTel
	  public static String IDPROCESO_DESTINATARIOSEXPEDIENTE="11P";
	  
	  public static int IDTIPO_RESOLUCIONAUTO_MODYDENEGAR=3;
	  public static int IDTIPO_RESOLUCIONAUTO_MODYCONCEDER=1;
	  
	
	  public static final String ENVIO="1";  
	  
	  
  public ClsConstants() {
  }
  /**
   * Finds a record in a database table exists
   * @param tableName Name of the table
   * @param keyfields Fields to filter the query
   * @param data Hashtable containing the values for the filter fields
   * @return true if any record found, false otherwise
   * @throws ClsExceptions when an error getting or releasing the Connection occurs.
   */
  public static boolean exists (String tableName, Object[] keyfields, Hashtable data) throws ClsExceptions {
    Connection con=null;
    boolean exists = false;
    String sql = " SELECT COUNT(1) FROM " + tableName;
    if (keyfields != null) {
      String aux = " WHERE ";
      for (int i = 0; i < keyfields.length; i++) {
        if (data.get(keyfields[i]) != null) {
          sql += aux + keyfields[i] + " = '" + data.get(keyfields[i]) + "' ";
          aux = "AND ";
        }
      }//for
    } //keyfields != null

    Statement st = null;
    ResultSet rs = null;
    try {
      con = ClsMngBBDD.getConnection();
      st = con.createStatement();
      rs = st.executeQuery(sql);
      rs.next();
      exists = (rs.getInt(1) > 0);
    } catch (SQLException ex) {
      ex.printStackTrace();
    } finally {
      try {
        if (rs != null) rs.close();
        if (st != null) st.close();
        if (con != null) ClsMngBBDD.closeConnection(con);
      } catch (SQLException ex) {
        rs = null;
        st = null;
      }
    }
    return exists;
  }


  /**
   * @param tableName Table Name
   * @param codFieldName Code Field Name
   * @param descFieldName Description Field Name
   * @param languageCode String only when the table contains the LANGUAGE_CODE field, otherwise, set to null
   * @param deletedFlag  true when the method should return only the NOT DELETED rows
   * @param firstBlank   true when the first option should be blank
   * @return Vector containing all CodeDescHandler found for this table
   * @throws ClsExceptions
   */
  public static Vector loadCDHandlerCollection (String tableName, String codFieldName, String descFieldName, String languageCode, boolean deletedFlag, boolean firstBlank) throws ClsExceptions{
    Connection con = null;

    Vector v = null;
    String sql = " SELECT " + codFieldName + ", " + descFieldName + " FROM " + tableName;
    String aux = " WHERE ";

    if (languageCode != null && !languageCode.equals("")) {
      sql += aux + " id_language = '" + languageCode + "' ";
      aux = " AND ";
    }

    if (deletedFlag) {
      sql += aux + " deleted = 0 ";
      aux = " AND ";
    }

    Statement st = null;
    ResultSet rs = null;
    try {
      con = ClsMngBBDD.getConnection();
      st = con.createStatement();
      rs = st.executeQuery(sql);
      if (rs != null) {
        v = new Vector();
        if (firstBlank)
          v.add(new CodeDescHandler("", ""));
        while (rs.next()) {
          v.add(new CodeDescHandler(rs.getString(1), rs.getString(2)));
        }
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    } finally {
      try {
        if (rs != null) rs.close();
        if (st != null) st.close();
      } catch (SQLException ex) {
        rs = null;
        st = null;
      }
      ClsMngBBDD.closeConnection(con);
    }
    return v;
  }



  public static Vector loadCDHandlerCollection (String sqlStatement, boolean firstBlank) throws ClsExceptions{
    Vector v = null;

    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    try {
      con = ClsMngBBDD.getConnection();
      st = con.createStatement();
      rs = st.executeQuery(sqlStatement);
      if (rs != null) {
        v = new Vector();
        if (firstBlank)
          v.add(new CodeDescHandler("", ""));
        while (rs.next()) {
          v.add(new CodeDescHandler(rs.getString(1), rs.getString(2)));
        }
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    } finally {
      try {
        if (rs != null) rs.close();
        if (st != null) st.close();
      } catch (SQLException ex) {
        rs = null;
        st = null;
      }
      ClsMngBBDD.closeConnection(con);
    }
    return v;
  }

  public static Vector loadCDHandlerCollectionFromFile(String fileName, boolean firstBlank) {
    Vector v = new Vector();
    FileInputStream inputStream=null;
    BufferedReader bufferedReader = null;
    try {
      inputStream=new FileInputStream(fileName);
      InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
      bufferedReader = new BufferedReader(inputStreamReader);
      String s = bufferedReader.readLine();
      String key=null;
      String value=null;
      int i=-1;
      while(s!=null){
        i=s.indexOf("=");
        if(i!=-1){
          key=s.substring(0,i);
          value=s.substring(i+1,s.length());
          v.add(new CodeDescHandler(key,value));
          s = bufferedReader.readLine();
        }
      }
      inputStream.close();
      bufferedReader.close();

    } catch (IOException ex) {
      ex.printStackTrace();
    } finally {
      try {
    	  if (inputStream != null) inputStream.close();
          if (bufferedReader != null) bufferedReader.close();
      } catch (Exception eee) {}
      if(firstBlank){
        v.add(0,new CodeDescHandler("",""));
      }
    }
    return v;
  }


  /**
   * Adds a String to an Array
   * @param array Original String[]
   * @param o The String to add
   * @return String[] with the String added at the end
   */
  public static String[] addStringToArray(String[] array, String o) {
    String[] a = new String[array.length + 1];
    System.arraycopy(array, 0, a, 0, array.length);
    a[a.length -1] = o;
    return a;
  }

  /**
   * Fills a string with a character until a determined length. The characters
   * may be added at the end, or at the beggining of the String.
   *
   * @param s The string to fill.
   * @param c Character to add.
   * @param finalLength Final length of the string
   * @param appendAtEnd if true, the chars will be added at the end of the string,
   * else, chars will be added at the begining.
   * @return the string with the characters added
   */
  public static String addCharacterToString(String s, char c, int finalLength, boolean appendAtEnd) {
    while (s.length() < finalLength) {
      s = (appendAtEnd) ? s + c : c + s;
    }
    return s;
  }

  /**
   *
   * @param v Vector to which we add a code and a description
   * @param code Code Field Name
   * @param desc Description Field Name
   */
  public static void addStringtoCollection(Vector v, String code, String desc){
    if (v==null){
      v = new Vector();
    }
    v.add(new CodeDescHandler(code,desc));
  }
  public static boolean esConsejoGeneral(Object idInstitucion){
	  boolean esConsejoGeneral = false;	  
	  String strInstitucion = idInstitucion.toString();
	  if(strInstitucion.length()==6){
		  strInstitucion = strInstitucion.substring(2);
	  }
	  int institucionNumber = Integer.parseInt(strInstitucion); 
	  if (institucionNumber == INSTITUCION_CONSEJOGENERAL){ // General
		  esConsejoGeneral = true;
	  }
	  
	  return esConsejoGeneral;
  }

   public static  boolean esConsejoColegio(Object idInstitucion){
	  boolean esConsejoColegio = false;
	  String strInstitucion = idInstitucion.toString().substring(2);
	  int institucionNumber = Integer.parseInt(strInstitucion); 
	  
	  if (institucionNumber > INSTITUCION_CONSEJO){ // Consejo de Colegio
		  esConsejoColegio = true;
	  }
	  
	  return esConsejoColegio;
  }
   
   public static  boolean esColegio(Object idInstitucion){
	  boolean esColegio = false;
	  String strInstitucion = idInstitucion.toString().substring(2);
	  int institucionNumber = Integer.parseInt(strInstitucion); 
	  
	  if (institucionNumber > INSTITUCION_CONSEJOGENERAL && institucionNumber < INSTITUCION_CONSEJO){ 
		  esColegio = true;
	  }
	  
	  return esColegio;
  }
   
  	public static String getIdInstitucionGeneral(String idInstitucion){
		String profesion = idInstitucion.substring(0,2);
		return profesion + INSTITUCION_CONSEJOGENERAL;
	}


}
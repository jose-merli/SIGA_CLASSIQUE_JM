/*
 * Created on Mar 15, 2005
 * @author jmgrau
*/
package com.siga.beans;
 
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimePart;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.transaction.UserTransaction;

import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.autogen.model.ScsComunicaciones;
import org.redabogacia.sigaservices.app.autogen.model.ScsDesigna;
import org.redabogacia.sigaservices.app.autogen.model.ScsEjg;
import org.redabogacia.sigaservices.app.helper.SIGAServicesHelper;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import service.ServiciosECOS.ServiciosECOSServiceSOAPStub;
import service.ServiciosECOS.ServiciosECOSService_ServiceLocator;

import com.aspose.words.Document;
import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.ExceptionManager;
import com.atos.utils.FileHelper;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.ecos.ws.solicitarEnvio.DatosGrandeCuentaTO;
import com.ecos.ws.solicitarEnvio.ResultadoSolicitudEnvio;
import com.ecos.ws.solicitarEnvio.SolicitudEnvioSMS;
import com.siga.Utilidades.Paginador;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.certificados.Plantilla;
import com.siga.envios.UsuarioFax;
import com.siga.envios.ZetaFax;
import com.siga.envios.form.ImagenPlantillaForm;
import com.siga.envios.service.SalidaEnviosService;
import com.siga.general.EjecucionPLs;
import com.siga.general.SIGAException;
import com.siga.informes.MasterReport;
import com.siga.informes.MasterWords;
import com.sun.mail.smtp.SMTPAddressFailedException;

import es.satec.businessManager.BusinessException;
import es.satec.businessManager.BusinessManager;


public class EnvEnviosAdm extends MasterBeanAdministrador {

  
  public final static int ESTADO_INICIAL = 1;
  public final static int ESTADO_PROCESADO = 2;
  public final static int ESTADO_PROCESADO_ERRORES= 3;
  public final static int ESTADO_PENDIENTE_AUTOMATICO = 4;
  public final static int ESTADO_PROCESANDO = 5;

 

  public final static String NO_GENERAR = "N";
  public final static String GENERAR_ETIQUETAS = "G";
  public final static String IMPRIMIR_ETIQUETAS = "I";

  public final static String FECHA_CREACION = "C";
  public final static String FECHA_PROGRAMADA = "P";
  
  public final static int LONGITUD_SMS=155;
  
  public final static String TIPOARCHIVO_DOC="doc";
  public final static String TIPOARCHIVO_FO="fo";

	public EnvEnviosAdm(UsrBean usuario)
	{
	    super(EnvEnviosBean.T_NOMBRETABLA, usuario);
	}

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
     */
    protected String[] getCamposBean() {
        String[] campos = {
                EnvEnviosBean.C_IDINSTITUCION,
            	EnvEnviosBean.C_IDENVIO,
            	EnvEnviosBean.C_DESCRIPCION,
            	EnvEnviosBean.C_FECHACREACION,
            	EnvEnviosBean.C_FECHAPROGRAMADA,
            	EnvEnviosBean.C_GENERARDOCUMENTO,
            	EnvEnviosBean.C_IMPRIMIRETIQUETAS,
            	EnvEnviosBean.C_IDPLANTILLAENVIOS,
            	EnvEnviosBean.C_IDESTADO,
            	EnvEnviosBean.C_IDPLANTILLA,
            	EnvEnviosBean.C_IDIMPRESORA,
            	EnvEnviosBean.C_IDTIPOENVIOS,
            	EnvEnviosBean.C_IDTIPOINTERCAMBIOTELEMATICO,
            	EnvEnviosBean.C_CONSULTA,
            	EnvEnviosBean.C_ACUSERECIBO,
            	EnvEnviosBean.C_COMISIONAJG,
            	EnvEnviosBean.C_FECHAMODIFICACION,
            	EnvEnviosBean.C_USUMODIFICACION,
            	EnvEnviosBean.C_CSV
				};

		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
     */
    protected String[] getClavesBean() {

        String[] claves = {EnvEnviosBean.C_IDINSTITUCION, EnvEnviosBean.C_IDENVIO};
		return claves;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
     */
    protected String[] getOrdenCampos() {

        String[] campos = {EnvEnviosBean.C_DESCRIPCION};
		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#hashTableToBean(java.util.Hashtable)
     */
    protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
        EnvEnviosBean bean = null;

		try
		{
			bean = new EnvEnviosBean();

			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, EnvEnviosBean.C_IDINSTITUCION));
			bean.setIdEnvio(UtilidadesHash.getInteger(hash, EnvEnviosBean.C_IDENVIO));
			bean.setDescripcion(UtilidadesHash.getString(hash, EnvEnviosBean.C_DESCRIPCION));
			bean.setFechaCreacion(UtilidadesHash.getString(hash, EnvEnviosBean.C_FECHACREACION));
			bean.setFechaProgramada(UtilidadesHash.getString(hash, EnvEnviosBean.C_FECHAPROGRAMADA));
			bean.setGenerarDocumento(UtilidadesHash.getString(hash, EnvEnviosBean.C_GENERARDOCUMENTO));
			bean.setImprimirEtiquetas(UtilidadesHash.getString(hash, EnvEnviosBean.C_IMPRIMIRETIQUETAS));
			bean.setIdPlantillaEnvios(UtilidadesHash.getInteger(hash, EnvEnviosBean.C_IDPLANTILLAENVIOS));
			bean.setIdEstado(UtilidadesHash.getInteger(hash, EnvEnviosBean.C_IDESTADO));
			bean.setIdPlantilla(UtilidadesHash.getInteger(hash, EnvEnviosBean.C_IDPLANTILLA));
			bean.setIdImpresora(UtilidadesHash.getInteger(hash, EnvEnviosBean.C_IDIMPRESORA));
			bean.setIdTipoEnvios(UtilidadesHash.getInteger(hash, EnvEnviosBean.C_IDTIPOENVIOS));
			bean.setIdTipoIntercambioTelematico(UtilidadesHash.getString(hash, EnvEnviosBean.C_IDTIPOINTERCAMBIOTELEMATICO));
			
			bean.setConsulta(UtilidadesHash.getString(hash, EnvEnviosBean.C_CONSULTA));
			bean.setAcuseRecibo(UtilidadesHash.getString(hash, EnvEnviosBean.C_ACUSERECIBO));
			bean.setComisionAJG(UtilidadesHash.getShort(hash, EnvEnviosBean.C_COMISIONAJG));
			bean.setCSV(UtilidadesHash.getString(hash, EnvEnviosBean.C_CSV));

		}

		catch (Exception e)
		{
			ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.hashTableToBean(): " + e.getMessage(), e, 1);
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}

		return bean;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#beanToHashTable(com.siga.beans.MasterBean)
     */
    protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
        Hashtable htData = null;

		try
		{
			htData = new Hashtable();

			EnvEnviosBean b = (EnvEnviosBean) bean;

			UtilidadesHash.set(htData, EnvEnviosBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, EnvEnviosBean.C_IDENVIO, b.getIdEnvio());
			UtilidadesHash.set(htData, EnvEnviosBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(htData, EnvEnviosBean.C_FECHACREACION, b.getFechaCreacion());
			UtilidadesHash.set(htData, EnvEnviosBean.C_FECHAPROGRAMADA, b.getFechaProgramada());
			UtilidadesHash.set(htData, EnvEnviosBean.C_GENERARDOCUMENTO, b.getGenerarDocumento());
			UtilidadesHash.set(htData, EnvEnviosBean.C_IMPRIMIRETIQUETAS, b.getImprimirEtiquetas());
			UtilidadesHash.set(htData, EnvEnviosBean.C_IDPLANTILLAENVIOS, b.getIdPlantillaEnvios());
			UtilidadesHash.set(htData, EnvEnviosBean.C_IDESTADO, b.getIdEstado());
			UtilidadesHash.set(htData, EnvEnviosBean.C_IDPLANTILLA, b.getIdPlantilla());
			UtilidadesHash.set(htData, EnvEnviosBean.C_IDIMPRESORA, b.getIdImpresora());
			UtilidadesHash.set(htData, EnvEnviosBean.C_IDTIPOENVIOS, b.getIdTipoEnvios());
			UtilidadesHash.set(htData, EnvEnviosBean.C_IDTIPOINTERCAMBIOTELEMATICO, b.getIdTipoIntercambioTelematico());
			UtilidadesHash.set(htData, EnvEnviosBean.C_CONSULTA, b.getConsulta());
			UtilidadesHash.set(htData, EnvEnviosBean.C_ACUSERECIBO, b.getAcuseRecibo());
			UtilidadesHash.set(htData, EnvEnviosBean.C_COMISIONAJG, b.getComisionAJG());
			UtilidadesHash.set(htData, EnvEnviosBean.C_CSV, b.getCSV());

		}

		catch (Exception e)
		{
			ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.beanToHashTable(): " + e.getMessage(), e, 1);
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}

    public Integer getNewIdEnvio(String idInstitucion) throws ClsExceptions{
        Long idEnvio = getSecuenciaNextVal(EnvEnviosBean.SEQ_ENV_ENVIOS);
        SimpleDateFormat formato = new SimpleDateFormat("yyyy");
        int anyo = new Integer(formato.format(new Date())) - 2000;//esto lo hago asi para que el anyo 2100 quede como 100 y no como 00
        return new Integer(Integer.toString(anyo)+idEnvio);
    }

    public Integer getNewIdEnvio(UsrBean usrBean) throws ClsExceptions{
    	return getNewIdEnvio(usrBean.getLocation());
    }

/**
 * @author juan.grau
 *
 * Permite la b�squeda de env�os insertados.
 *
 * @param String fechaDesde
 * @param String fechaHasta
 * @param String idEstado
 * @param String nombreEnvio
 * @param String idTipoEnvios
 * @param String idInstitucion
 *
 * @return Vector (Rows)
 *
 * @throws ClsExceptions
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */

    public Paginador buscarEnvio (String idEnvio, String tipoFecha, String fechaDesde, String fechaHasta,
            				   String idEstado, String nombre,
            				   String idTipoEnvios, String idInstitucion,boolean isComision, boolean conArchivados)
    	throws ClsExceptions{

        Vector datos = new Vector();

		//NOMBRES TABLAS PARA LA JOIN
		String T_ENV_ENVIOS = EnvEnviosBean.T_NOMBRETABLA + " EN";
		String T_ENV_ESTADOENVIO = EnvEstadoEnvioBean.T_NOMBRETABLA + " ES";
		String T_ENV_TIPOENVIOS = EnvTipoEnviosBean.T_NOMBRETABLA + " TI";

		//Tabla env_envios
		String EN_IDENVIO = "EN." + EnvEnviosBean.C_IDENVIO;
		String EN_FECHACREACION = "EN." + EnvEnviosBean.C_FECHACREACION;
		String EN_FECHAPROGRAMADA = "EN." + EnvEnviosBean.C_FECHAPROGRAMADA;
		String EN_IDINSTITUCION = "EN." + EnvEnviosBean.C_IDINSTITUCION;
		String EN_DESCRIPCION = "EN." + EnvEnviosBean.C_DESCRIPCION;
		String EN_IDESTADO = "EN." + EnvEnviosBean.C_IDESTADO;
		String EN_IDTIPOENVIOS = "EN." + EnvEnviosBean.C_IDTIPOENVIOS;

		String fecha,fechaIsNull;
		if (tipoFecha.equalsIgnoreCase(FECHA_CREACION)){
		    fecha = "FECHA";
		    fechaIsNull = "";
		} else {
		    fecha = "FECHAPROGRAMADA";
		    fechaIsNull = " AND FECHAPROGRAMADA is not null";
		}
		//Tabla env_estadoenvio
		String ES_NOMBRE = "ES." + EnvEstadoEnvioBean.C_NOMBRE;
		String ES_IDESTADO = "ES." + EnvEstadoEnvioBean.C_IDESTADO;

		//Tabla env_tipoenvios
		String TI_NOMBRE = "TI." + EnvTipoEnviosBean.C_NOMBRE;
		String TI_IDTIPOENVIOS = "TI." + EnvTipoEnviosBean.C_IDTIPOENVIOS;

		//Formateo de fechas
		String dFechaDesde = null, dFechaHasta = null;
		if (fechaDesde!=null && !fechaDesde.trim().equals(""))
		        dFechaDesde = GstDate.getApplicationFormatDate("",fechaDesde);
		if (fechaHasta!=null && !fechaHasta.trim().equals(""))
	        dFechaHasta = GstDate.getApplicationFormatDate("",fechaHasta);

		// Acceso a BBDD
		RowsContainer rc = null;
		try {
			rc = new RowsContainer();

			StringBuilder sql = new StringBuilder();
			sql.append(" SELECT * FROM ( ");
			
			sql.append(" ( ");
			sql.append("SELECT ");
			sql.append(EN_IDENVIO); 
			sql.append(", ");
			sql.append(EN_DESCRIPCION );
			sql.append( ", ");
			sql.append(EN_FECHACREACION );
			sql.append( ", ");
			sql.append(EN_FECHAPROGRAMADA );
			sql.append( ", ");
			sql.append(ES_NOMBRE ); 
			sql.append( " AS ESTADO, ");
			sql.append(TI_IDTIPOENVIOS ); 
			sql.append( ", ");
			sql.append(EN_IDESTADO ); 
			sql.append( ", ");
			sql.append(TI_NOMBRE ); 
			sql.append( " AS TIPOENVIO, ");
			sql.append(" EN.FECHABAJA,COMISIONAJG,IDINSTITUCION ");
			

			sql.append(" FROM ");
			sql.append(T_ENV_ENVIOS ); 
			sql.append( ", " );
			sql.append(T_ENV_ESTADOENVIO ); 
			sql.append( ", " );
			sql.append(T_ENV_TIPOENVIOS);

		    sql.append(" WHERE ");
			sql.append( EN_IDESTADO );
			sql.append( " = " );
			sql.append( ES_IDESTADO);
			sql.append(" AND " );
			sql.append( EN_IDTIPOENVIOS + " = " ); 
			sql.append( TI_IDTIPOENVIOS);
			sql.append(" )  ");
				
			
			
			
			if(!conArchivados && !isComision && (idEnvio==null || idEnvio.equals(""))) {
				sql.append(" UNION ");
				
				sql.append(" ( ");
				sql.append("SELECT NULL IDENVIO,");
				sql.append(" EP.NOMBRE||' '|| f_siga_getnombre_colegiado(ED.IDINSTITUCION,ED.IDPERSONA,'0')  DESCRIPCION,");
				sql.append(" EPI.FECHAMODIFICACION  FECHA,");
				sql.append(" EP.FECHAPROGRAMADA	FECHAPROGRAMADA,");
				sql.append(" (SELECT F_SIGA_GETRECURSO(NOMBRE, 1) ESTADO FROM ENV_ESTADOENVIO WHERE IDESTADO=0) ESTADO,");
				sql.append(" TI.IDTIPOENVIOS,0 IDESTADO,");
				sql.append(" (SELECT F_SIGA_GETRECURSO(TI.NOMBRE ,1) AS VALOR FROM DUAL) TIPOENVIO,");
				sql.append(" NULL FECHABAJA,null COMISIONAJG,EPI.IDINSTITUCION");
				sql.append(" FROM ENV_PROGRAMINFORMES EPI ,");
				sql.append(" ENV_ENVIOPROGRAMADO EP,");
				sql.append(" ENV_DESTPROGRAMINFORMES ED,");
				sql.append(" ENV_TIPOENVIOS TI,");
				sql.append(" ENV_ESTADOENVIO ES");
				sql.append(" WHERE");
				sql.append(" EPI.IDINSTITUCION = EP.IDINSTITUCION");
				sql.append(" AND EPI.IDENVIO = EP.IDENVIO");
				sql.append(" AND EPI.IDINSTITUCION = ED.IDINSTITUCION");
				sql.append(" AND EPI.IDENVIO = ED.IDENVIO");
				sql.append(" AND EP.IDTIPOENVIOS = TI.IDTIPOENVIOS");
				sql.append(" AND EP.IDTIPOENVIOS = TI.IDTIPOENVIOS");
				sql.append(" AND EPI.ESTADO = 0");
				sql.append(" AND EPI.IDTIPOINFORME IN (");
				sql.append(" SELECT IDTIPOINFORME FROM ADM_TIPOINFORME at2 WHERE REGEXP_LIKE (PROGRAMACION, '^([01]?[0-9]|2[0-3]):[0-5][0-9]$')");
				sql.append(" )");
//				sql.append(" AND EP.FECHAPROGRAMADA >	SYSDATE ");
				sql.append(" ) ");
				
			}
			sql.append(" )  ");
			sql.append(" WHERE " ); 
		    sql.append( " IDINSTITUCION = " );
			sql.append( idInstitucion.toString());
			
			if(nombre!=null && !nombre.equals(""))
				sql.append(" AND "+ComodinBusquedas.prepararSentenciaCompleta(nombre.trim(),"DESCRIPCION" ));
			
			if (dFechaDesde!=null || dFechaHasta!=null) {
			    sql.append(" AND " ); 
				sql.append( GstDate.dateBetweenDesdeAndHasta(fecha,dFechaDesde,dFechaHasta));
			}
			
			if(idEstado!=null && !idEstado.equals("")){
					sql.append(" AND IDESTADO = " );
					sql.append(idEstado);
			}
			if(idTipoEnvios!=null && !idTipoEnvios.equals("")){
				sql.append(" AND IDTIPOENVIOS = " );
				sql.append(idTipoEnvios);
			}
			if(idEnvio!=null && !idEnvio.equals("")){
				sql.append(" AND IDENVIO =" );
				sql.append(idEnvio);
			}
			sql.append(fechaIsNull);
			if(isComision){
				sql.append(" AND COMISIONAJG = "+ClsConstants.DB_TRUE);	
			}else{
				sql.append(" AND (COMISIONAJG IS NULL OR COMISIONAJG = "+ClsConstants.DB_FALSE+" ) ");	
			}
			
			if(!conArchivados){
				sql.append(" AND FECHABAJA IS NULL ");	
			}	
			
			
			
			
			
			// RGG CAMBIO DE ORDEN sql.append(" ORDER BY " + EN_DESCRIPCION;
			sql.append(" ORDER BY FECHA DESC");

			ClsLogging.writeFileLog("EnvEnviosAdm -> QUERY: "+sql,10);

			
			 Paginador paginador = new Paginador(sql.toString());				
				int totalRegistros = paginador.getNumeroTotalRegistros();
				
				if (totalRegistros==0){					
					paginador =null;
				}
				return paginador;
		}
		catch (Exception e) {
			ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.buscarEnvio(): " + e.getMessage(), e, 1);
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D.");
		}
		
    }


    /**
     * @author juan.grau
     *
     * Permite la b�squeda de destinatarios manuales.
     *
     * @param String idInstitucion
     * @param String idEnvio
     *
     * @return Vector (Rows)
     *
     * @throws ClsExceptions
     *
     */

    public Vector getDestinatariosManuales (String idInstitucion, String idEnvio)
    	throws ClsExceptions{

        Vector datos = new Vector();

		//NOMBRES TABLAS PARA LA JOIN
		String T_ENV_DESTINATARIOS = EnvDestinatariosBean.T_NOMBRETABLA + " D";
		String T_CEN_PERSONA = CenPersonaBean.T_NOMBRETABLA + " P";
		String T_CEN_COLEGIADO = CenColegiadoBean.T_NOMBRETABLA + " C";

		//Tabla env_destinatarios
		String D_IDINSTITUCION = "D." + EnvDestinatariosBean.C_IDINSTITUCION;
		String D_IDENVIO = "D." + EnvDestinatariosBean.C_IDENVIO;
		String D_IDPERSONA = "D." + EnvDestinatariosBean.C_IDPERSONA;
		String D_NOMBRE = "D." + EnvDestinatariosBean.C_NOMBRE;
		String D_APELLIDO1 = "D." + EnvDestinatariosBean.C_APELLIDOS1;
		String D_APELLIDO2 = "D." + EnvDestinatariosBean.C_APELLIDOS2;
		String D_NIFCIF = "D." + EnvDestinatariosBean.C_NIFCIF;
		String D_TIPODESTINATARIO = "D." + EnvDestinatariosBean.C_TIPODESTINATARIO;

		//Tabla cen_persona
		String P_IDPERSONA = "P." + CenPersonaBean.C_IDPERSONA;
		String P_NOMBRE = "P." + CenPersonaBean.C_NOMBRE;
		String P_APELLIDO1 = "P." + CenPersonaBean.C_APELLIDOS1;
		String P_APELLIDO2 = "P." + CenPersonaBean.C_APELLIDOS2;
		String P_NIFCIF = "P." + CenPersonaBean.C_NIFCIF;

		//Tabla cen_colegiado
		String C_IDPERSONA = "C." + CenColegiadoBean.C_IDPERSONA;
		String C_NCOLEGIADO = "C." + CenColegiadoBean.C_NCOLEGIADO;
		String C_IDINSTITUCION = "C." + CenColegiadoBean.C_IDINSTITUCION;

		// Acceso a BBDD
		RowsContainer rc = null;
		try {
			rc = new RowsContainer();

			StringBuffer sql = new StringBuffer();
			sql.append("( ");
			sql.append("(SELECT ");
			sql.append("DECODE("+P_IDPERSONA+",-1,");  
			sql.append(D_NOMBRE + "||' '|| " + D_APELLIDO1 + "||' '|| " + D_APELLIDO2+",");
			sql.append(P_NOMBRE + "||' '|| " + P_APELLIDO1 + "||' '|| " + P_APELLIDO2 + ") AS NOMBREYAPELLIDOS, ");
			sql.append(C_NCOLEGIADO + ", ");
			sql.append("DECODE("+P_IDPERSONA+",-1,"+  D_NIFCIF + ", "+  P_NIFCIF + ") NIFCIF, ");
			sql.append(P_IDPERSONA);
			sql.append(", " + D_TIPODESTINATARIO );
			sql.append(",D.ORIGENDESTINATARIO,");
			sql.append("D.IDESTADO ");
				
			sql.append(" FROM "+T_ENV_DESTINATARIOS + ", "+ T_CEN_PERSONA + ", "+ T_CEN_COLEGIADO);
				
			sql.append(" WHERE ");
			sql.append(D_IDINSTITUCION + " = " + idInstitucion);
			sql.append(" AND " + D_IDENVIO + " = " + idEnvio);
			sql.append(" AND " + D_IDPERSONA + " = " + P_IDPERSONA);
			sql.append(" AND " + D_IDINSTITUCION + " = " + C_IDINSTITUCION + "(+)");
			sql.append(" AND " + D_IDPERSONA + " = " + C_IDPERSONA + "(+) "); 
			sql.append(" AND D.Tipodestinatario ='");
			sql.append(EnvDestinatariosBean.TIPODESTINATARIO_CENPERSONA);
			sql.append("' ) ");
			
			sql.append(" UNION ");
			sql.append(" (SELECT  D.NOMBRE || ' ' || D.APELLIDOS1 || ' ' || D.APELLIDOS2 AS NOMBREYAPELLIDOS,null,D.NIFCIF,D.IDPERSONA ");
			sql.append(", D.TIPODESTINATARIO");
			sql.append(",D.ORIGENDESTINATARIO,");
			sql.append("D.IDESTADO ");
			sql.append(" FROM ENV_DESTINATARIOS D ");
			sql.append(" WHERE D.IDINSTITUCION = ");
			sql.append(idInstitucion);
			sql.append(" AND D.IDENVIO = ");
			sql.append(idEnvio);
			sql.append(" AND D.Tipodestinatario ='");
			sql.append(EnvDestinatariosBean.TIPODESTINATARIO_SCSPERSONAJG);
			sql.append("') ");
			
			sql.append(" UNION ");
			sql.append(" (SELECT  D.NOMBRE || ' ' || D.APELLIDOS1 || ' ' || D.APELLIDOS2 AS NOMBREYAPELLIDOS,null,D.NIFCIF,D.IDPERSONA ");
			sql.append(", D.TIPODESTINATARIO");
			sql.append(",D.ORIGENDESTINATARIO,");
			sql.append("D.IDESTADO ");
			sql.append(" FROM ENV_DESTINATARIOS D ");
			sql.append(" WHERE D.IDINSTITUCION = ");
			sql.append(idInstitucion);
			sql.append(" AND D.IDENVIO = ");
			sql.append(idEnvio);
			sql.append(" AND D.Tipodestinatario ='");
			sql.append(EnvDestinatariosBean.TIPODESTINATARIO_SCSPROCURADOR);
			sql.append("') ");

			sql.append(" UNION ");
			sql.append(" (SELECT  D.NOMBRE || ' ' || D.APELLIDOS1 || ' ' || D.APELLIDOS2 AS NOMBREYAPELLIDOS,null,D.NIFCIF,D.IDPERSONA ");
			sql.append(", D.TIPODESTINATARIO");
			sql.append(",D.ORIGENDESTINATARIO,");
			sql.append("D.IDESTADO ");
			sql.append(" FROM ENV_DESTINATARIOS D ");
			sql.append(" WHERE D.IDINSTITUCION = ");
			sql.append(idInstitucion);
			sql.append(" AND D.IDENVIO = ");
			sql.append(idEnvio);
			sql.append(" AND D.Tipodestinatario ='");
			sql.append(EnvDestinatariosBean.TIPODESTINATARIO_SCSJUZGADO);
			sql.append("') ");
			
			sql.append(") ORDER BY IDESTADO DESC,  NOMBREYAPELLIDOS ");   
			
			//ClsLogging.writeFileLog("EnvEnviosAdm.getDestinatariosManuales -> QUERY: "+sql,3);
			
			if (rc.query(sql.toString())) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					datos.add(fila);
				}
			}
		}
		catch (Exception e) {
			ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.getDestinatariosManuales(): " + e.getMessage(), e, 1);
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D.");
		}
		return datos;
    }

    /**
     * @author juan.grau
     *
     * Permite la b�squeda de listas destinatarias.
     *
     * @param String idInstitucion
     * @param String idEnvio
     *
     * @return Vector (Rows)
     *
     * @throws ClsExceptions
     *
     */

    public Vector getListasDestinatarias (String idInstitucion, String idEnvio)
    	throws ClsExceptions{

        Vector datos = new Vector();

		//NOMBRES TABLAS PARA LA JOIN
		String T_ENV_LISTACORREOSENVIOS = EnvListaCorreosEnviosBean.T_NOMBRETABLA + " LE";
		String T_ENV_LISTACORREOS = EnvListaCorreosBean.T_NOMBRETABLA + " L";

		//Tabla env_listacorreosenvios
		String LE_IDINSTITUCION = "LE." + EnvListaCorreosEnviosBean.C_IDINSTITUCION;
		String LE_IDENVIO = "LE." + EnvListaCorreosEnviosBean.C_IDENVIO;
		String LE_IDLISTACORREO = "LE." + EnvListaCorreosEnviosBean.C_IDLISTACORREO;

		//Tabla env_listacorreos
		String L_IDINSTITUCION = "L." + EnvListaCorreosBean.C_IDINSTITUCION;
		String L_IDLISTACORREO = "L." + EnvListaCorreosBean.C_IDLISTACORREO;
		String L_NOMBRE = "L." + EnvListaCorreosBean.C_NOMBRE;
		String L_DINAMICA = "L." + EnvListaCorreosBean.C_DINAMICA;
		String L_DESCRIPCION = "L." + EnvListaCorreosBean.C_DESCRIPCION;

		// Acceso a BBDD
		RowsContainer rc = null;
		try {
			rc = new RowsContainer();

			String sql = "SELECT ";

		    sql += L_IDINSTITUCION + ", ";
		    sql += L_IDLISTACORREO + ", ";
		    sql += L_NOMBRE + ", ";
		    sql += L_DESCRIPCION + ", ";
		    sql += L_DINAMICA;

			sql += " FROM ";
		    sql += T_ENV_LISTACORREOSENVIOS + ", " +
		    	   T_ENV_LISTACORREOS;

		    sql += " WHERE ";
		    sql += LE_IDINSTITUCION + " = " + idInstitucion;
		    sql += " AND " + LE_IDENVIO + " = " + idEnvio;
			sql += " AND " + LE_IDINSTITUCION + " = " + L_IDINSTITUCION;
			sql += " AND " + LE_IDLISTACORREO + " = " + L_IDLISTACORREO;

			sql += " ORDER BY " + L_NOMBRE;

			ClsLogging.writeFileLog("EnvEnviosAdm.getListasDestinatarias -> QUERY: "+sql,3);

			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					datos.add(fila);
				}
			}
		}
		catch (Exception e) {
			ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.getListasDestinatarias(): " + e.getMessage(), e, 1);
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D.");
		}
		return datos;
    }

    /**
     * @author juan.grau
     *
     * Permite la b�squeda de remitentes manuales.
     *
     * @param String idInstitucion
     * @param String idEnvio
     *
     * @return Vector (Rows)
     *
     * @throws ClsExceptions
     *
     */

    public Vector getRemitentes (String idInstitucion, String idEnvio)
    	throws ClsExceptions{

        Vector datos = new Vector();

		//NOMBRES TABLAS PARA LA JOIN
		String T_ENV_REMITENTES = EnvRemitentesBean.T_NOMBRETABLA + " R";
		String T_CEN_PERSONA = CenPersonaBean.T_NOMBRETABLA + " P";
		String T_CEN_COLEGIADO = CenColegiadoBean.T_NOMBRETABLA + " C";

		//Tabla env_Remitentes
		String R_IDINSTITUCION = "R." + EnvRemitentesBean.C_IDINSTITUCION;
		String R_IDENVIO = "R." + EnvRemitentesBean.C_IDENVIO;
		String R_IDPERSONA = "R." + EnvRemitentesBean.C_IDPERSONA;
		String R_DESCRIPCION = "R." + EnvRemitentesBean.C_DESCRIPCION;

		//Tabla cen_persona
		String P_IDPERSONA = "P." + CenPersonaBean.C_IDPERSONA;
		String P_NOMBRE = "P." + CenPersonaBean.C_NOMBRE;
		String P_APELLIDO1 = "P." + CenPersonaBean.C_APELLIDOS1;
		String P_APELLIDO2 = "P." + CenPersonaBean.C_APELLIDOS2;
		String P_NIFCIF = "P." + CenPersonaBean.C_NIFCIF;

		//Tabla cen_colegiado
		String C_IDPERSONA = "C." + CenColegiadoBean.C_IDPERSONA;
		String C_NCOLEGIADO = "C." + CenColegiadoBean.C_NCOLEGIADO;
		String C_IDINSTITUCION = "C." + CenColegiadoBean.C_IDINSTITUCION;

		// Acceso a BBDD
		RowsContainer rc = null;
		try {
			rc = new RowsContainer();

			String sql = "SELECT (";

		    sql += P_NOMBRE + "||' '|| " + P_APELLIDO1 + "||' '|| " + P_APELLIDO2 + ") AS NOMBREYAPELLIDOS, ";
		    sql += R_DESCRIPCION + ", ";
		    sql += C_NCOLEGIADO + ", ";
		    sql += P_NIFCIF + ", ";
		    sql += P_IDPERSONA;

			sql += " FROM ";
		    sql += T_ENV_REMITENTES + ", " +
		    	   T_CEN_PERSONA + ", " +
		    	   T_CEN_COLEGIADO;

		    sql += " WHERE ";
		    sql += R_IDINSTITUCION + " = " + idInstitucion;
		    sql += " AND " + R_IDENVIO + " = " + idEnvio;
			sql += " AND " + R_IDPERSONA + " = " + P_IDPERSONA;
			sql += " AND " + R_IDINSTITUCION + " = " + C_IDINSTITUCION + "(+)";
			sql += " AND " + R_IDPERSONA + " = " + C_IDPERSONA + "(+)";

			sql += " ORDER BY NOMBREYAPELLIDOS";

			ClsLogging.writeFileLog("EnvEnviosAdm.getRemitentes -> QUERY: "+sql,3);

			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					datos.add(fila);
				}
			}
		}
		catch (Exception e) {
			ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.getRemitentes(): " + e.getMessage(), e, 1);
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D.");
		}
		return datos;
    }

    /**
     * @author juan.grau
     *
     * Permite la b�squeda de direcciones de una persona y de un tipo .
     *
     * @param String idPersona
     * @param String idInstitucion
     * @param String idTipoEnvio
     *
     * @return Vector (Rows)
     *
     * @throws ClsExceptions
     *
     */

    public Vector getDirecciones(String idInstitucion,
            					 String idPersona,
            					 String idTipoEnvio)
    	throws ClsExceptions{

        Vector direcciones = new Vector();
 		CenDireccionesAdm direccionesAdm = new CenDireccionesAdm(this.usrbean);

 		try{
 		    direcciones = direccionesAdm.selectDirecciones(Long.valueOf(idPersona),Integer.valueOf(idInstitucion), false);
 		}
 		catch (Exception e) {
 			ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.getDirecciones(): " + e.getMessage(), e, 1);
 			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D.");
 		}

 		Vector direccionesTipo = new Vector();
 		int tipo = 0;
 		try {
 		    tipo = Integer.valueOf(idTipoEnvio).intValue();
 		}
 		catch (Exception e) {
 		    tipo = -1;
 		}
 		boolean foundPrimeraPreferente = false;
 		switch (tipo) {
        case EnvTipoEnviosAdm.K_CORREO_ELECTRONICO:case EnvTipoEnviosAdm.K_DOCUMENTACIONLETRADO:
            for (int i=0;i<direcciones.size();i++){
     		    Hashtable htDir = (Hashtable)direcciones.get(i);
     		    String correoElectronico = (String)htDir.get(CenDireccionesBean.C_CORREOELECTRONICO);
                if (correoElectronico!=null && !correoElectronico.equals("")){
                	String preferente = (String)htDir.get(CenDireccionesBean.C_PREFERENTE); 
         		    if(preferente!=null && !preferente.equals("")){
         		    	if(preferente.contains(ClsConstants.TIPO_PREFERENTE_CORREOELECTRONICO)&&!foundPrimeraPreferente){
         		    		foundPrimeraPreferente = true;
         		    		direccionesTipo.add(0,htDir);
         		    	}else{
         		    		direccionesTipo.add(htDir);
         		    		
         		    	}
         		    	
         		    	
         		    }else{
         		    	direccionesTipo.add(htDir);
         		    }
     		    }
     		}
            break;
        case EnvTipoEnviosAdm.K_CORREO_ORDINARIO:
            for (int i=0;i<direcciones.size();i++){
                Hashtable htDir = (Hashtable)direcciones.get(i);
     		    String direccion = (String)htDir.get(CenDireccionesBean.C_DOMICILIO);
     		    String idPoblacion = (String)htDir.get(CenDireccionesBean.C_IDPOBLACION);
     		    String cp = (String)htDir.get(CenDireccionesBean.C_CODIGOPOSTAL);
                if ((direccion != null && !direccion.equals(""))
						&& ((idPoblacion != null && !idPoblacion.equals("")) || (cp != null && !cp.equals("")))) {
                	String preferente = (String)htDir.get(CenDireccionesBean.C_PREFERENTE);
                	if(preferente!=null){
         		    	if(preferente.contains(ClsConstants.TIPO_PREFERENTE_CORREO)&&!foundPrimeraPreferente){
         		    		foundPrimeraPreferente = true;
         		    		direccionesTipo.add(0,htDir);
         		    	}else{
         		    		direccionesTipo.add(htDir);
         		    		
         		    	}
         		    	
         		    }else{
         		    	direccionesTipo.add(htDir);
         		    }
                	
                	
				}
     		}
            break;
        case EnvTipoEnviosAdm.K_SMS:
        case EnvTipoEnviosAdm.K_BUROSMS:
            for (int i=0;i<direcciones.size();i++){
                Hashtable htDir = (Hashtable)direcciones.get(i);
                String movil = (String)htDir.get(CenDireccionesBean.C_MOVIL);
                if (movil!=null && !movil.equals("")){
                	String preferente = (String)htDir.get(CenDireccionesBean.C_PREFERENTE);
                	if(preferente!=null){
         		    	if(preferente.contains(ClsConstants.TIPO_PREFERENTE_SMS)&&!foundPrimeraPreferente){
         		    		foundPrimeraPreferente = true;
         		    		direccionesTipo.add(0,htDir);
         		    	}else{
         		    		direccionesTipo.add(htDir);
         		    		
         		    	}
         		    	
         		    }else{
         		    	direccionesTipo.add(htDir);
         		    }
     		    }
     		}
            break;
        case EnvTipoEnviosAdm.K_FAX:
            for (int i=0;i<direcciones.size();i++){
                Hashtable htDir = (Hashtable)direcciones.get(i);
     		    String fax1 = (String)htDir.get(CenDireccionesBean.C_FAX1);
     		    String fax2 = (String)htDir.get(CenDireccionesBean.C_FAX2);
     		    if ((fax1!=null && !fax1.equals(""))||
     		        (fax2!=null && !fax2.equals(""))){
     		    	String preferente = (String)htDir.get(CenDireccionesBean.C_PREFERENTE);
                	if(preferente!=null){
         		    	if(preferente.contains(ClsConstants.TIPO_PREFERENTE_FAX)&&!foundPrimeraPreferente){
         		    		foundPrimeraPreferente = true;
         		    		direccionesTipo.add(0,htDir);
         		    	}else{
         		    		direccionesTipo.add(htDir);
         		    		
         		    	}
         		    	
         		    }else{
         		    	direccionesTipo.add(htDir);
         		    }
     		    }
     		}
            break;
        default:
            direccionesTipo=direcciones;
            break;
        }

 		return direccionesTipo;
    }

    /**
     * Copia los campos de la plantilla a la tabla EnvCamposEnvios
     *
     * @param idInstitucion
     * @param id
     * @param idTipoEnvio
     * @param idPlantilla
     * @param tipoCampo
     * @throws ClsExceptions
     */
    public void copiarCamposPlantilla(Integer idInstitucion, Integer id, Integer idTipoEnvio, Integer idPlantilla) throws ClsExceptions 
	{
        EnvCamposPlantillaAdm cpAdm = new EnvCamposPlantillaAdm(this.usrbean);
        EnvCamposEnviosAdm ceAdm = new EnvCamposEnviosAdm(this.usrbean);

        //Borramos los campos de un env�o
        Hashtable ht = new Hashtable();
        ht.put(EnvCamposEnviosBean.C_IDINSTITUCION,idInstitucion);
        ht.put(EnvCamposEnviosBean.C_IDENVIO,id);
        String[] campos = {EnvCamposEnviosBean.C_IDINSTITUCION,
                			EnvCamposEnviosBean.C_IDENVIO};
        ceAdm.deleteDirect(ht,campos);

        //Copiamos los campos de la plantilla al env�o
        ht.remove(EnvCamposEnviosBean.C_IDENVIO);
        ht.put(EnvCamposPlantillaBean.C_IDTIPOENVIOS,idTipoEnvio);
        ht.put(EnvCamposPlantillaBean.C_IDPLANTILLAENVIOS,idPlantilla);
        Vector vCamposPlantilla = cpAdm.select(ht);

        if (vCamposPlantilla!=null){
	        for (int i=0;i<vCamposPlantilla.size();i++){
	            EnvCamposPlantillaBean cpBean = (EnvCamposPlantillaBean) vCamposPlantilla.elementAt(i);
	            EnvCamposEnviosBean ceBean = new EnvCamposEnviosBean();

	            ceBean.setIdEnvio(id);
	            ceBean.setIdInstitucion(idInstitucion);
	            ceBean.setIdCampo(cpBean.getIdCampo());
	            ceBean.setIdFormato(cpBean.getIdFormato());
	            ceBean.setTipoCampo(cpBean.getTipoCampo());
	            ceBean.setValor(cpBean.getValor());	             
	            ceAdm.insert(ceBean);
	        }
        }

        // Copiamos los remitentes de la plantilla
        try {
        	this.copiarRemitentesDesdePlantilla(idInstitucion, id, idTipoEnvio, idPlantilla);
        }
        catch (Exception e) {
        	ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.copiarCamposPlantilla(): " + e.getMessage(), e, 1);
        	throw new ClsExceptions(e, "Error al copiar remitentes en el envio");
        }
    }
    public void copiarCamposPlantilla(Integer idInstitucion, Integer id, Integer idTipoEnvio, Integer idPlantilla,Object bean) throws ClsExceptions, SIGAException {
        EnvCamposPlantillaAdm cpAdm = new EnvCamposPlantillaAdm(this.usrbean);
        EnvCamposEnviosAdm ceAdm = new EnvCamposEnviosAdm(this.usrbean);

        //Borramos los campos de un env�o
        Hashtable ht = new Hashtable();
        ht.put(EnvCamposEnviosBean.C_IDINSTITUCION,idInstitucion);
        ht.put(EnvCamposEnviosBean.C_IDENVIO,id);
        String[] campos = {EnvCamposEnviosBean.C_IDINSTITUCION,
                			EnvCamposEnviosBean.C_IDENVIO};
        ceAdm.deleteDirect(ht,campos);

        //Copiamos los campos de la plantilla al env�o
        ht.remove(EnvCamposEnviosBean.C_IDENVIO);
        ht.put(EnvCamposPlantillaBean.C_IDTIPOENVIOS,idTipoEnvio);
        ht.put(EnvCamposPlantillaBean.C_IDPLANTILLAENVIOS,idPlantilla);
        Vector vCamposPlantilla = cpAdm.select(ht);

        if (vCamposPlantilla!=null){
	        for (int i=0;i<vCamposPlantilla.size();i++){
	            EnvCamposPlantillaBean cpBean = (EnvCamposPlantillaBean) vCamposPlantilla.elementAt(i);
	            EnvCamposEnviosBean ceBean = new EnvCamposEnviosBean();

	            ceBean.setIdEnvio(id);
	            ceBean.setIdInstitucion(idInstitucion);
	            ceBean.setIdCampo(cpBean.getIdCampo());
	            ceBean.setIdFormato(cpBean.getIdFormato());
	            ceBean.setTipoCampo(cpBean.getTipoCampo());

	    	    if(cpBean.getTipoCampo().equals(EnvCamposAdm.K_TIPOCAMPO_E)){
	    	    	if(cpBean.getIdCampo().toString().equals(EnvCamposPlantillaAdm.K_IDCAMPO_CUERPO)){
	    	    		Hashtable htDatosEnvio = new Hashtable();
	    	    		StringBuffer asunto = new StringBuffer();
	    	    		
	    	    		if(bean instanceof ExpAlertaBean){
	    	    			ExpAlertaBean expAlertaBean = (ExpAlertaBean)bean;
	    	    			asunto.append(expAlertaBean.getAnioExpediente());
	    	    			asunto.append(" / ");
	    	    			asunto.append(expAlertaBean.getNumeroExpediente());
		    	    		
		    	    		
	    	    			htDatosEnvio.put("EXP_MENSAJE_ALERTA", expAlertaBean.getTexto());
	    	    			htDatosEnvio.put("EXP_NUMERO", asunto.toString());
	    	    			try {
								htDatosEnvio.put("EXP_FECHA_ALERTA", GstDate.getFormatedDateLong("",expAlertaBean.getFechaAlerta()));
							} catch (Exception e) {
								ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.copiarCamposPlantilla() Error al a�adir el campo EXP_FECHA_ALERTA: " + e.getMessage(), e, 1);
							}
	    	    			htDatosEnvio.put("EXP_TIPO", expAlertaBean.getTipoExpediente().getNombre());
	    	    			if (expAlertaBean.getFase() != null){
	    	    				htDatosEnvio.put("EXP_FASE", expAlertaBean.getFase().getNombre());
	    	    			}
	    	    			if (expAlertaBean.getEstado() != null){
	    	    				htDatosEnvio.put("EXP_ESTADO", expAlertaBean.getEstado().getNombre());
	    	    			}	    	    			
	    	    		}
	    	    		
	    	    		if(bean instanceof ScsEJGBean){	    	    			
	    	    			ScsEJGBean ejgBean = (ScsEJGBean)bean;
	    	    			Hashtable ejgHashtable =  ejgBean.getOriginalHash();	    	    			
	    	    			this.obtenerDatosEnvioScsEJGBean(ejgHashtable!=null?ejgHashtable:new Hashtable(), htDatosEnvio, false);
	    	    			
	    	    		}
	    	    		
	    	    		if(bean instanceof ScsDesignaBean){	    	    			
	    	    			ScsDesignaBean designaBean = (ScsDesignaBean)bean;
	    	    			Hashtable designaHashtable =  designaBean.getOriginalHash();	    	    			
	    	    			this.obtenerDatosEnvioScsEJGBean(designaHashtable!=null?designaHashtable:new Hashtable(), htDatosEnvio, true);
	    	    			
	    	    		}
	    	    		
	    	    		String sCuerpo = sustituirEtiquetas(cpBean.getValor(),htDatosEnvio);
	    	    		cpBean.setValor(sCuerpo);
	    	    		
	    	    		
	    	    	}else if(cpBean.getIdCampo().toString().equals(EnvCamposPlantillaAdm.K_IDCAMPO_ASUNTO)){
	    	    		Hashtable htDatosEnvio = new Hashtable();
	    	    		StringBuffer asunto = new StringBuffer();
	    	    		//asunto.append(cpBean.getValor());
	    	    		if(bean instanceof ExpAlertaBean){
	    	    			ExpAlertaBean expAlertaBean = (ExpAlertaBean)bean;
		    	    		asunto.append(expAlertaBean.getAnioExpediente());
		    	    		asunto.append(" / ");
	    	    			asunto.append(expAlertaBean.getNumeroExpediente());
		    	    		
		    	    		htDatosEnvio.put("EXP_MENSAJE_ALERTA", expAlertaBean.getTexto());
	    	    			htDatosEnvio.put("EXP_NUMERO", asunto.toString());
	    	    			try {
								htDatosEnvio.put("EXP_FECHA_ALERTA", GstDate.getFormatedDateLong("",expAlertaBean.getFechaAlerta()));
							} catch (Exception e) {
								ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.copiarCamposPlantilla() Error al a�adir el campo EXP_FECHA_ALERTA: " + e.getMessage(), e, 1);
								
							}
	    	    			htDatosEnvio.put("EXP_TIPO", expAlertaBean.getTipoExpediente().getNombre());
	    	    			if (expAlertaBean.getFase() != null){
	    	    				htDatosEnvio.put("EXP_FASE", expAlertaBean.getFase().getNombre());
	    	    			}
	    	    			if (expAlertaBean.getEstado() != null){
	    	    				htDatosEnvio.put("EXP_ESTADO", expAlertaBean.getEstado().getNombre());
	    	    			}	    	    				
	    	    		}
	    	    		
	    	    		if(bean instanceof ScsEJGBean){	    	    			
	    	    			ScsEJGBean ejgBean = (ScsEJGBean)bean;
	    	    			Hashtable ejgHashtable =  ejgBean.getOriginalHash();	    	    			
	    	    			this.obtenerDatosEnvioScsEJGBean(ejgHashtable!=null?ejgHashtable:new Hashtable(), htDatosEnvio, false);
	    	    			
	    	    		}
	    	    		
	    	    		if(bean instanceof ScsDesignaBean){	    	    			
	    	    			ScsDesignaBean designaBean = (ScsDesignaBean)bean;
	    	    			Hashtable designaHashtable =  designaBean.getOriginalHash();	    	    			
	    	    			this.obtenerDatosEnvioScsEJGBean(designaHashtable!=null?designaHashtable:new Hashtable(), htDatosEnvio, false);
	    	    			  
	    	    		}
	    	    			    	    		
	    	    		String sAsunto = sustituirEtiquetas(cpBean.getValor(),htDatosEnvio);
	    	    		cpBean.setValor(sAsunto);	    	    		
	    	    	}	    	    		
	    	    	
	    	    }else if(cpBean.getTipoCampo().equals(EnvCamposAdm.K_TIPOCAMPO_S)){
	    	    	if(cpBean.getIdCampo().toString().equals(EnvCamposPlantillaAdm.K_IDCAMPO_SMS)){
	    	    		
	    	    		Hashtable htDatosEnvio = new Hashtable();
	    	    		if(bean instanceof ScsEJGBean){	    	    			
	    	    			ScsEJGBean ejgBean = (ScsEJGBean)bean;
	    	    			Hashtable ejgHashtable =  ejgBean.getOriginalHash();	    	    			
	    	    			this.obtenerDatosEnvioScsEJGBean(ejgHashtable!=null?ejgHashtable:new Hashtable(), htDatosEnvio, true);
	    	    		}
	    	    		
	    	    		if(bean instanceof ScsDesignaBean){	    	    			
	    	    			ScsDesignaBean designaBean = (ScsDesignaBean)bean;
	    	    			Hashtable designaHashtable =  designaBean.getOriginalHash();	    	    			
	    	    			this.obtenerDatosEnvioScsEJGBean(designaHashtable!=null?designaHashtable:new Hashtable(), htDatosEnvio, true);
	    	    			  
	    	    		}

	    	    		String sCuerpo = sustituirEtiquetas(cpBean.getValor(),htDatosEnvio);
	    	    		cpBean.setValor(sCuerpo);
	    	    	}	    	    	
	    	    }
	    	    
	    	    ceBean.setValor(cpBean.getValor());	            
	            ceAdm.insert(ceBean);
	        }
        }

        // Copiamos los remitentes de la plantilla
        try {
        	this.copiarRemitentesDesdePlantilla(idInstitucion, id, idTipoEnvio, idPlantilla);
        }
        catch (Exception e) {
        	ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.copiarCamposPlantilla(): " + e.getMessage(), e, 1);
        	throw new ClsExceptions(e, "Error al copiar remitentes en el envio");
        }
    }
    
    /**
     * Obtenci�n de los datos del EJG para los envios
     * @param ejgHashtable
     * @param htDatosEnvio
     * @param bSms
     */
    public void obtenerDatosEnvioScsEJGBean (Hashtable ejgHashtable, Hashtable htDatosEnvio, boolean bSms) {
    	
    	//Recorro el hashtable de entrada para que est�n todas las etiquetas disponibles no solamente estas
    	Enumeration e = ejgHashtable.keys();
                               
        while (e.hasMoreElements())
        {
            String key =  (String) e.nextElement();
            
            //Elimino las �reas (son vectores), no se van a mostrar las �reas en el mail ni en el sms
            if(ejgHashtable.get(key) instanceof String){
                               
	            String valor = (String)ejgHashtable.get(key);
	            htDatosEnvio.put(key, valor);

	            if(key.equalsIgnoreCase("NUMERO_EJG")){
	            	
	            	if(ejgHashtable.get("NUMERO_EJG")!=null)
	            		htDatosEnvio.put("EJG_NU",(String) ejgHashtable.get("NUMERO_EJG"));
	            }
	            
				if(key.equalsIgnoreCase("N_APELLI_1_LETRADO_DESIGNADO")){
					
					if(ejgHashtable.get("N_APELLI_1_LETRADO_DESIGNADO")!=null)
						htDatosEnvio.put("EJG_LETRADODES_NOMBRE_APELLI_1",(String) ejgHashtable.get("N_APELLI_1_LETRADO_DESIGNADO"));
					else
						htDatosEnvio.put("EJG_LETRADODES_NOMBRE_APELLI_1","");
				}
				
				if(key.equalsIgnoreCase("N_APEL_1_2_LETRADO_DESIGNADO")){
					
					if(ejgHashtable.get("N_APEL_1_2_LETRADO_DESIGNADO")!=null)
						htDatosEnvio.put("EJG_LETRADODES_NOMBRE_APEL_1_2",(String) ejgHashtable.get("N_APEL_1_2_LETRADO_DESIGNADO"));
					else
						htDatosEnvio.put("EJG_LETRADODES_NOMBRE_APEL_1_2","");
				}
				
				if(key.equalsIgnoreCase("APEL_1_2_N_LETRADO_DESIGNADO")){
					
					if(ejgHashtable.get("APEL_1_2_N_LETRADO_DESIGNADO")!=null)
						htDatosEnvio.put("EJG_LETRADODES_APEL_1_2_NOMBRE",(String) ejgHashtable.get("APEL_1_2_N_LETRADO_DESIGNADO"));
					else
						htDatosEnvio.put("EJG_LETRADODES_APEL_1_2_NOMBRE","");
				}
				
				if(key.equalsIgnoreCase("TELEFONODESPACHO_LET_DESIGNADO")){
				
					if(ejgHashtable.get("TELEFONODESPACHO_LET_DESIGNADO")!=null){
						String telefonoDespacho =  (String) ejgHashtable.get("TELEFONODESPACHO_LET_DESIGNADO");
						if(telefonoDespacho.length()>13 && bSms)
							telefonoDespacho = telefonoDespacho.substring(0,13);
						htDatosEnvio.put("EJG_TLFNO", telefonoDespacho);
					}else
						htDatosEnvio.put("EJG_TLFNO", "");
				}
				
				if(key.equalsIgnoreCase("DESC_TIPODICTAMENEJG")){
					
					if(ejgHashtable.get("DESC_TIPODICTAMENEJG")!=null){
						String dictamenEJG =  (String) ejgHashtable.get("DESC_TIPODICTAMENEJG");
						if(dictamenEJG.length()>10 && bSms)
							dictamenEJG = dictamenEJG.substring(0,10);			
						htDatosEnvio.put("EJG_DICTAM",dictamenEJG);
					}else{
						htDatosEnvio.put("EJG_DICTAM","");	    	    					    	    				
					}	
				}
				
				if(key.equalsIgnoreCase("NUM_PROCEDIMIENTO_EJG")){
					
					if(ejgHashtable.get("NUM_PROCEDIMIENTO_EJG")!=null)
						htDatosEnvio.put("EJG_NUMERO_PROCEDIMIENTO", (String) ejgHashtable.get("NUM_PROCEDIMIENTO_EJG"));
					else
						htDatosEnvio.put("EJG_NUMERO_PROCEDIMIENTO","");
				}
				if(key.equalsIgnoreCase("ASUNTO_EJG")){
					
					if(ejgHashtable.get("ASUNTO_EJG")!=null)
						htDatosEnvio.put("EJG_ASUNTO", (String) ejgHashtable.get("ASUNTO_EJG"));
					else
						htDatosEnvio.put("EJG_ASUNTO","");
				}
	            

            } //Fin si es un string   
                           
      } //Fin lectura etiquetas

    }
    
    /**
     * Funcion que copia los remitentes de la plantilla si los tiene. 
     * Si no se establece uno por defecto
     * @param idInstitucion
     * @param idEnvio
     * @param idTipoEnvios
     * @param idPlantillaEnvios
     * @throws SIGAException
     */
    public void copiarRemitentesDesdePlantilla (Integer idInstitucion, Integer idEnvio, Integer idTipoEnvios, Integer idPlantillaEnvios) throws SIGAException
    {
        try {
        	// Verificamos is la plantilla tiene remitentes
        	EnvPlantillaRemitentesAdm admPlantillaRemitente = new EnvPlantillaRemitentesAdm (this.usrbean);
        	Hashtable h = new Hashtable();
        	UtilidadesHash.set(h, EnvPlantillaRemitentesBean.C_IDINSTITUCION,     idInstitucion);
        	UtilidadesHash.set(h, EnvPlantillaRemitentesBean.C_IDTIPOENVIOS,      idTipoEnvios);
        	UtilidadesHash.set(h, EnvPlantillaRemitentesBean.C_IDPLANTILLAENVIOS, idPlantillaEnvios);
        	Vector remitentes = admPlantillaRemitente.select(h);

        	// Borramos los actuales los remitentes
    	    h.clear();
        	UtilidadesHash.set(h, EnvRemitentesBean.C_IDENVIO,      idEnvio);
        	UtilidadesHash.set(h, EnvRemitentesBean.C_IDINSTITUCION,idInstitucion);
            String[] campos = {EnvRemitentesBean.C_IDINSTITUCION, EnvRemitentesBean.C_IDENVIO};
    	    EnvRemitentesAdm remitentesAdm = new EnvRemitentesAdm(this.usrbean);
        	remitentesAdm.deleteDirect(h, campos);

        	// Si la plantilla no tiene remitentes, se estable uno por defecto
        	if (remitentes.size() <= 0) {
        		this.addRemitentesPorDefecto(idInstitucion, idEnvio, idTipoEnvios, idPlantillaEnvios);
        	}
        	else {
        		this.addRemitentesDesdePlantilla(idInstitucion, idEnvio, idTipoEnvios, idPlantillaEnvios, remitentes);
        	}       		
        }
        catch (Exception e1) {
        	ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.copiarRemitentesDesePlantilla(): " + e1.getMessage(), e1, 1);
            throw new SIGAException("messages.general.error",e1);
        }
    }
        	
    private void addRemitentesDesdePlantilla (Integer idInstitucion, Integer idEnvio, Integer idTipoEnvios, Integer idPlantillaEnvios, Vector remitentes) throws SIGAException
	{
    	try {
    		
    	    if (remitentes.size() < 1) {
        		this.addRemitentesPorDefecto(idInstitucion, idEnvio, idTipoEnvios, idPlantillaEnvios);
    	    	return;
    	    }

    	    EnvRemitentesAdm remitentesAdm = new EnvRemitentesAdm(this.usrbean);

        	// Copiamos todos los remitentes de la plantilla
    		for (int i = 0; i < remitentes.size(); i++) {
    			EnvPlantillaRemitentesBean b = (EnvPlantillaRemitentesBean) remitentes.get(i);
    			
    	        EnvRemitentesBean remBean = new EnvRemitentesBean();
    	        remBean.setIdEnvio(idEnvio);
    	        remBean.setIdInstitucion(b.getIdInstitucion());
    	        remBean.setIdPersona(b.getIdPersona());
    	        remBean.setDescripcion(b.getDescripcion());
		        remBean.setDomicilio(b.getDomicilio());
		        remBean.setIdPoblacion(b.getIdPoblacion());
		        remBean.setPoblacionExtranjera(b.getPoblacionExtranjera());
		        remBean.setIdProvincia(b.getIdProvincia());
		        remBean.setIdPais(b.getIdPais());
		        if (remBean.getIdPais().equals("")) remBean.setIdPais(ClsConstants.ID_PAIS_ESPANA); 
		        remBean.setCodigoPostal(b.getCodigoPostal());
		        remBean.setCorreoElectronico(b.getCorreoElectronico());
		        remBean.setFax1(b.getFax1());
		        remBean.setFax2(b.getFax2());

		        //remitentesAdm.insert(remBean);
			    //Insertamos bean si no existe en la tabla
			    if (!remitentesAdm.existeRemitente("" + idEnvio, "" + idInstitucion, "" + b.getIdPersona())){
			        remitentesAdm.insert(remBean);
			    } 
			    else {
			        throw new SIGAException("messages.envios.error.existeelemento");
			    } 
    		}
        } 
        catch (Exception e1) {
        	ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.addRemitentesDesdePlantilla(): " + e1.getMessage(), e1, 1);
            throw new SIGAException("Error al copiar remitentes desde plantilla",e1);
        }
    }
	
    private void addRemitentesPorDefecto(Integer idInstitucion, Integer idEnvio, Integer idTipoEnvios, Integer idPlantillaEnvios) throws SIGAException
	{
        try {
            CenInstitucionAdm instAdm = new CenInstitucionAdm(this.usrbean);
	        Hashtable htPk = new Hashtable();
	        htPk.put(CenInstitucionBean.C_IDINSTITUCION,idInstitucion);
	        CenInstitucionBean instBean = (CenInstitucionBean)instAdm.selectByPK(htPk).firstElement();       
	        
	        Long idPersona = new Long(instBean.getIdPersona().longValue());
	        CenClienteAdm clienteAdm = new CenClienteAdm(this.usrbean);
	        Vector direcciones = clienteAdm.getDirecciones(idPersona,idInstitucion, false);                
	        
	        EnvRemitentesBean remBean = new EnvRemitentesBean();
	        remBean.setIdEnvio(idEnvio);
	        remBean.setIdInstitucion(idInstitucion);
	        remBean.setIdPersona(idPersona);
	        remBean.setDescripcion("");
	        
	        if (direcciones!=null && direcciones.size()>0) {
		        Hashtable htDir = (Hashtable) direcciones.firstElement();
		        remBean.setDomicilio((String)htDir.get(CenDireccionesBean.C_DOMICILIO));
		        remBean.setIdPoblacion((String)htDir.get(CenDireccionesBean.C_IDPOBLACION));
		        remBean.setPoblacionExtranjera((String)htDir.get(CenDireccionesBean.C_POBLACIONEXTRANJERA));
		        remBean.setIdProvincia((String)htDir.get(CenDireccionesBean.C_IDPROVINCIA));
		        remBean.setIdPais((String)htDir.get(CenDireccionesBean.C_IDPAIS));
		        if (remBean.getIdPais().equals("")) remBean.setIdPais(ClsConstants.ID_PAIS_ESPANA); 
		        remBean.setCodigoPostal((String)htDir.get(CenDireccionesBean.C_CODIGOPOSTAL));
		        remBean.setCorreoElectronico((String)htDir.get(CenDireccionesBean.C_CORREOELECTRONICO));
		        remBean.setFax1((String)htDir.get(CenDireccionesBean.C_FAX1));
		        remBean.setFax2((String)htDir.get(CenDireccionesBean.C_FAX2));
	        }
	        
		    //Insertamos bean si no existe en la tabla
    	    EnvRemitentesAdm remitentesAdm = new EnvRemitentesAdm(this.usrbean);
		    if (!remitentesAdm.existeRemitente("" + idEnvio, "" + idInstitucion, "" + idPersona)){
		        remitentesAdm.insert(remBean);
		    } 
		    else {
		        throw new SIGAException("messages.envios.error.existeelemento");
		    } 
        } 
        catch (SIGAException e1) {
        	ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.addRemitentesPorDefecto(): " + e1.getMessage(), e1, 1);
            throw e1;
        } 
        catch (Exception e1) {
        	ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.addRemitentesPorDefecto(): " + e1.getMessage(), e1, 1);
            throw new SIGAException("Error al a�adir los remitentes por defecto",e1);
        }
    }
    
    public String getPathEnvio(String idInstitucion, String idEnvio) throws SIGAException,ClsExceptions
	{
	    EnvEnviosAdm envAdm = new EnvEnviosAdm(this.usrbean);
	    Hashtable htPk = new Hashtable();
	    htPk.put(EnvEnviosBean.C_IDINSTITUCION,idInstitucion);
	    htPk.put(EnvEnviosBean.C_IDENVIO,idEnvio);
	    EnvEnviosBean envioBean = (EnvEnviosBean)envAdm.selectByPK(htPk).firstElement();
	
	    SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);
	    Calendar cal = Calendar.getInstance();
		Date d;
	    try {
	        d = sdf.parse(envioBean.getFechaCreacion());
	        cal.setTime(d);
	    } catch (ParseException e1) {
	    	ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.getPathEnvio(): " + e1.getMessage(), e1, 1);
	    	throw new ClsExceptions(e1,e1.getMessage());
	    }
	    String anio = String.valueOf(cal.get(Calendar.YEAR));
	    String mes = String.valueOf(cal.get(Calendar.MONTH)+1);
	
	    EnvDocumentosDestinatariosAdm docAdm = new EnvDocumentosDestinatariosAdm(this.usrbean);
	    String pathDoc;
		try {
	        pathDoc = docAdm.getPathDocumentosFromDB();
	    } catch (ClsExceptions e2) {
	    	ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.getPathEnvio(): " + e2.getMessage(), e2, 1);
	    	throw e2;
	    }
	
	    //String sDirectorio = [PATH_DOCUMENTOSADJUNTOS/idInstitucion/AAAA/MM/idEnvio
	    String sDirectorio = pathDoc + File.separator +
							 idInstitucion + File.separator +
							 anio + File.separator +
							 mes + File.separator + idEnvio;
	
	    return sDirectorio;
	}
    
	
    public String getPathEnvio(EnvEnviosBean envioBean)
	throws SIGAException,ClsExceptions{

	    SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);
	    Calendar cal = Calendar.getInstance();
		Date d;
	    try {
	        d = sdf.parse(envioBean.getFechaCreacion());
	        cal.setTime(d);
	    } catch (ParseException e1) {
	    	ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.getPathEnvio(): " + e1.getMessage(), e1, 1);
	    	throw new ClsExceptions(e1,e1.getMessage());
	    }
	    String anio = String.valueOf(cal.get(Calendar.YEAR));
	    String mes = String.valueOf(cal.get(Calendar.MONTH)+1);
	
	    EnvDocumentosDestinatariosAdm docAdm = new EnvDocumentosDestinatariosAdm(this.usrbean);
	    String pathDoc;
		try {
	        pathDoc = docAdm.getPathDocumentosFromDB();
	    } catch (ClsExceptions e2) {
	    	ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.getPathEnvio(): " + e2.getMessage(), e2, 1);
	    	throw e2;
	    }
	
	    //String sDirectorio = [PATH_DOCUMENTOSADJUNTOS/idInstitucion/AAAA/MM/idEnvio
	    String sDirectorio = pathDoc + File.separator +
							 envioBean.getIdInstitucion().toString() + File.separator +
							 anio + File.separator +
							 mes + File.separator + envioBean.getIdEnvio().toString();
	
	    return sDirectorio;
	}
	
   
    

    public String getAsunto(Integer idInstitucion, Integer idEnvio)
    	throws SIGAException,ClsExceptions{

        String sAsunto = "";

        Hashtable htPk = new Hashtable();
        htPk.put(EnvCamposEnviosBean.C_IDINSTITUCION,idInstitucion);
        htPk.put(EnvCamposEnviosBean.C_IDENVIO,idEnvio);
        htPk.put(EnvCamposEnviosBean.C_TIPOCAMPO,EnvCamposAdm.K_TIPOCAMPO_E);
        htPk.put(EnvCamposEnviosBean.C_IDCAMPO,EnvCamposPlantillaAdm.K_IDCAMPO_ASUNTO);

        EnvCamposEnviosAdm camposEnviosAdm = new EnvCamposEnviosAdm(this.usrbean);
        EnvCamposEnviosBean camposEnviosBean = null;
        try {
            Vector vCampo = camposEnviosAdm.selectByPK(htPk);
	        if (vCampo.size()>0){
	            camposEnviosBean = (EnvCamposEnviosBean)camposEnviosAdm.selectByPK(htPk).firstElement();
	            sAsunto = camposEnviosBean.getValor();
	        }
        } catch (ClsExceptions e) {
        	ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.getAsunto(): " + e.getMessage(), e, 1);
            throw new ClsExceptions(e,"Error obteniendo los datos del destinatario");
        }
        return sAsunto;
    }

    public String getCuerpo(Integer idInstitucion, Integer idEnvio)
		throws SIGAException,ClsExceptions{

        String sCuerpo = "";

	    Hashtable htPk = new Hashtable();
	    htPk.put(EnvCamposEnviosBean.C_IDINSTITUCION,idInstitucion);
	    htPk.put(EnvCamposEnviosBean.C_IDENVIO,idEnvio);
	    htPk.put(EnvCamposEnviosBean.C_TIPOCAMPO,EnvCamposAdm.K_TIPOCAMPO_E);
	    htPk.put(EnvCamposEnviosBean.C_IDCAMPO,EnvCamposPlantillaAdm.K_IDCAMPO_CUERPO);

	    EnvCamposEnviosAdm camposEnviosAdm = new EnvCamposEnviosAdm(this.usrbean);
	    EnvCamposEnviosBean camposEnviosBean = null;
	    try {
	        Vector vCampo = camposEnviosAdm.selectByPK(htPk);
	        if (vCampo.size()>0){
	            camposEnviosBean = (EnvCamposEnviosBean)vCampo.firstElement();
	        	sCuerpo = camposEnviosBean.getValor();
	        }
	    } catch (ClsExceptions e) {
	    	ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.getCuerpo(): " + e.getMessage(), e, 1);
	        throw new ClsExceptions(e,"Error obteniendo el cuerpo del mensaje");
	    }
	    return sCuerpo;
	}
    
    public String getCuerpoSMS(Integer idInstitucion, Integer idEnvio)
		throws SIGAException,ClsExceptions{
	
	    String sCuerpo = "";
	
	    Hashtable htPk = new Hashtable();
	    htPk.put(EnvCamposEnviosBean.C_IDINSTITUCION,idInstitucion);
	    htPk.put(EnvCamposEnviosBean.C_IDENVIO,idEnvio);
	    htPk.put(EnvCamposEnviosBean.C_TIPOCAMPO,EnvCamposAdm.K_TIPOCAMPO_S);
	    htPk.put(EnvCamposEnviosBean.C_IDCAMPO,EnvCamposPlantillaAdm.K_IDCAMPO_SMS);
	
	    EnvCamposEnviosAdm camposEnviosAdm = new EnvCamposEnviosAdm(this.usrbean);
	    EnvCamposEnviosBean camposEnviosBean = null;
	    try {
	        Vector vCampo = camposEnviosAdm.selectByPK(htPk);
	        if (vCampo.size()>0){
	            camposEnviosBean = (EnvCamposEnviosBean)vCampo.firstElement();
	        	sCuerpo = camposEnviosBean.getValor();
	        }
	    } catch (ClsExceptions e) {
	    	ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.getCuerpoSMS(): " + e.getMessage(), e, 1);
	        throw new ClsExceptions(e,"Error obteniendo el cuerpo del mensaje");
	    }
	    return sCuerpo;
	}
    
    public void setAsunto(Integer idInstitucion, Integer idEnvio, String sAsunto) 
		throws SIGAException{
	    
	    Hashtable htPk = new Hashtable();
	    htPk.put(EnvCamposEnviosBean.C_IDINSTITUCION,idInstitucion);
	    htPk.put(EnvCamposEnviosBean.C_IDENVIO,idEnvio);
	    htPk.put(EnvCamposEnviosBean.C_TIPOCAMPO,EnvCamposAdm.K_TIPOCAMPO_E);
	    htPk.put(EnvCamposEnviosBean.C_IDCAMPO,EnvCamposPlantillaAdm.K_IDCAMPO_ASUNTO);
	    
	    EnvCamposEnviosAdm camposEnviosAdm = new EnvCamposEnviosAdm(this.usrbean);
	    EnvCamposEnviosBean camposEnviosBean = null;
	    try {
	        Vector vCampo = camposEnviosAdm.selectByPKForUpdate(htPk);
	        if (vCampo.size()>0){
	            //el campo exist�a, lo actualizamos.
	            camposEnviosBean = (EnvCamposEnviosBean)camposEnviosAdm.selectByPK(htPk).firstElement();
	            camposEnviosBean.setValor(sAsunto);
	            
	            camposEnviosAdm.update(camposEnviosBean);
	        } else {
	            //el campo no exist�a, lo creamos.
	            camposEnviosBean = new EnvCamposEnviosBean();
	            camposEnviosBean.setIdEnvio(idEnvio);
	            camposEnviosBean.setIdInstitucion(idInstitucion);
	            camposEnviosBean.setIdCampo(Integer.valueOf(EnvCamposPlantillaAdm.K_IDCAMPO_ASUNTO));
	            camposEnviosBean.setTipoCampo(EnvCamposAdm.K_TIPOCAMPO_E);
	            camposEnviosBean.setValor(sAsunto);
	            
	            camposEnviosAdm.insert(camposEnviosBean);
	        }
	    } catch (ClsExceptions e) {
	    	ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.setAsunto(): " + e.getMessage(), e, 1);
	        throw new SIGAException("messages.envios.error.establecerasunto",e);
	    }
	}
    
    public void setCuerpo(Integer idInstitucion, Integer idEnvio, String sCuerpo) 
		throws SIGAException, ClsExceptions{
	    
	    Hashtable htPk = new Hashtable();
	    htPk.put(EnvCamposEnviosBean.C_IDINSTITUCION,idInstitucion);
	    htPk.put(EnvCamposEnviosBean.C_IDENVIO,idEnvio);
	    htPk.put(EnvCamposEnviosBean.C_TIPOCAMPO,EnvCamposAdm.K_TIPOCAMPO_E);
	    htPk.put(EnvCamposEnviosBean.C_IDCAMPO,EnvCamposPlantillaAdm.K_IDCAMPO_CUERPO);
	    
	    EnvCamposEnviosAdm camposEnviosAdm = new EnvCamposEnviosAdm(this.usrbean);
	    EnvCamposEnviosBean camposEnviosBean = null;
	    try {
	        Vector vCampo = camposEnviosAdm.selectByPKForUpdate(htPk);
	        if (vCampo.size()>0){
	            //el campo exist�a, lo actualizamos.
	            camposEnviosBean = (EnvCamposEnviosBean)camposEnviosAdm.selectByPK(htPk).firstElement();
	            camposEnviosBean.setValor(sCuerpo);
	            
	            camposEnviosAdm.update(camposEnviosBean);
	        } else {
	            //el campo no exist�a, lo creamos.
	            camposEnviosBean = new EnvCamposEnviosBean();
	            camposEnviosBean.setIdEnvio(idEnvio);
	            camposEnviosBean.setIdInstitucion(idInstitucion);
	            camposEnviosBean.setIdCampo(Integer.valueOf(EnvCamposPlantillaAdm.K_IDCAMPO_CUERPO));
	            camposEnviosBean.setTipoCampo(EnvCamposAdm.K_TIPOCAMPO_E);
	            camposEnviosBean.setValor(sCuerpo);
	            
	            camposEnviosAdm.insert(camposEnviosBean);
	        }
	    } catch (ClsExceptions e) {
	    	ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.setCuerpo(): " + e.getMessage(), e, 1);
	        throw new ClsExceptions(e, "Error estableciendo el cuerpo del mensaje");
	    }
	}
    
    public void setTextoSms(Integer idInstitucion, Integer idEnvio, String sCuerpo) 
		throws SIGAException, ClsExceptions{
	    
	    Hashtable htPk = new Hashtable();
	    htPk.put(EnvCamposEnviosBean.C_IDINSTITUCION,idInstitucion);
	    htPk.put(EnvCamposEnviosBean.C_IDENVIO,idEnvio);
	    htPk.put(EnvCamposEnviosBean.C_TIPOCAMPO,EnvCamposAdm.K_TIPOCAMPO_S);
	    htPk.put(EnvCamposEnviosBean.C_IDCAMPO,EnvCamposPlantillaAdm.K_IDCAMPO_SMS);
	    
	    EnvCamposEnviosAdm camposEnviosAdm = new EnvCamposEnviosAdm(this.usrbean);
	    EnvCamposEnviosBean camposEnviosBean = null;
	    try {
	        Vector vCampo = camposEnviosAdm.selectByPKForUpdate(htPk);
	        if (vCampo.size()>0){
	            //el campo exist�a, lo actualizamos.
	            camposEnviosBean = (EnvCamposEnviosBean)camposEnviosAdm.selectByPK(htPk).firstElement();
	            camposEnviosBean.setValor(sCuerpo);
	            
	            camposEnviosAdm.update(camposEnviosBean);
	        } else {
	            //el campo no exist�a, lo creamos.
	            camposEnviosBean = new EnvCamposEnviosBean();
	            camposEnviosBean.setIdEnvio(idEnvio);
	            camposEnviosBean.setIdInstitucion(idInstitucion);
	            camposEnviosBean.setIdCampo(Integer.valueOf(EnvCamposPlantillaAdm.K_IDCAMPO_SMS));
	            camposEnviosBean.setTipoCampo(EnvCamposAdm.K_TIPOCAMPO_S);
	            camposEnviosBean.setValor(sCuerpo);
	            
	            camposEnviosAdm.insert(camposEnviosBean);
	        }
	    } catch (ClsExceptions e) {
	    	ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.setTextoSms(): " + e.getMessage(), e, 1);
	        throw new ClsExceptions(e, "Error estableciendo el cuerpo del mensaje");
	    }
	}

    private Hashtable getDatosEnvio(EnvDestinatariosBean beanDestinatario, String consulta) throws SIGAException, ClsExceptions
	{
    	Hashtable htDatos = getDatosEnvio(beanDestinatario.getIdInstitucion(), beanDestinatario.getIdEnvio(), beanDestinatario.getIdPersona(), consulta,beanDestinatario.getTipoDestinatario());
    	    	
    	if(beanDestinatario.getDomicilio()!=null)
    		htDatos.put("DIRECCION", beanDestinatario.getDomicilio());
    	else
    		htDatos.put("DIRECCION", "");
    	
    	if(beanDestinatario.getCodigoPostal()!=null)
    		htDatos.put("CODPOSTAL", beanDestinatario.getCodigoPostal());
    	else
    		htDatos.put("CODPOSTAL", "");
    	
    	if(beanDestinatario.getPais()!=null)
    		htDatos.put("PAIS", beanDestinatario.getPais());
    	else
    		htDatos.put("PAIS", "");
    	
    	if(beanDestinatario.getProvincia()!=null)
    		htDatos.put("PROVINCIA", beanDestinatario.getProvincia());
    	else
    		htDatos.put("PROVINCIA", "");
    	
    	if(beanDestinatario.getPoblacion()!=null)
    		htDatos.put("POBLACION", beanDestinatario.getPoblacion());
    	else
    		htDatos.put("POBLACION", "");

    	
    	if(beanDestinatario.getMovil()!=null)
    		htDatos.put("MOVIL", beanDestinatario.getMovil());
    	else
    		htDatos.put("MOVIL", "");
    	
    	if(beanDestinatario.getFax1()!=null)
    		htDatos.put("FAX1", beanDestinatario.getFax1());
    	else
    		htDatos.put("FAX1", "");
    	
    	if(beanDestinatario.getFax2()!=null)
    		htDatos.put("FAX2", beanDestinatario.getFax2());
    	else
    		htDatos.put("FAX2", "");
    	
    	if(beanDestinatario.getCorreoElectronico()!=null)
    		htDatos.put("CORREOELECTRONICO", beanDestinatario.getCorreoElectronico());
    	else
    		htDatos.put("CORREOELECTRONICO", "");

    	
    	
    	return htDatos;
	}
	private Hashtable getDatosEnvio(Integer idInstitucion, Integer idEnvio, Long idPersona, String consulta,String tipoDestinatario) throws SIGAException, ClsExceptions
	{
		try
		{
			String sSQL = "";
			Hashtable htDatos = new Hashtable();

			if (consulta==null)
			{
			    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.QUERY);
				//ReadProperties rp = new ReadProperties("SIGA.properties");
				if(CenPersonaAdm.K_PERSONA_GENERICA.equals(idPersona)){
		        	sSQL = rp.returnProperty("envios.consulta.sinPersona");
				}else{
					if(tipoDestinatario!=null&&(tipoDestinatario.equals(EnvDestinatariosBean.TIPODESTINATARIO_SCSPERSONAJG))){
						sSQL = rp.returnProperty("envios.consulta.conPersonaJG");
					}else if(tipoDestinatario!=null&&tipoDestinatario.equals(EnvDestinatariosBean.TIPODESTINATARIO_SCSJUZGADO)){
						sSQL = rp.returnProperty("envios.consulta.conJuzgado");						
					}else if(tipoDestinatario!=null&&tipoDestinatario.equals(EnvDestinatariosBean.TIPODESTINATARIO_SCSPROCURADOR)){
						sSQL = rp.returnProperty("envios.consulta.conProcurador");
						
					}else{
						
						
						sSQL = rp.returnProperty("envios.consulta.conPersona");
					}
				}
			}
			else
			{
				sSQL = consulta;
			}

        	sSQL = sSQL.replaceFirst("@idinstitucion@", ""+idInstitucion);
        	sSQL = sSQL.replaceFirst("@idenvio@", ""+idEnvio);
        	sSQL = sSQL.replaceFirst("@idpersona@", ""+idPersona);
        	sSQL = sSQL.replaceAll("(?i)" + "@IDIOMA@", this.usrbean.getLanguage());

	        RowsContainer rc = find(sSQL);

	        if (rc==null || rc.size()==0)
	        {
	            throw new SIGAException("messages.envios.error.noexistendatos", null, null);
	        }

			Row fila = (Row) rc.get(0);
			htDatos = (Hashtable)fila.getRow();
			
//			if(vCampos!=null && vCampos.size()>1){
//				for (int i = 0; i < vCampos.size(); i++) {
//					Hashtable htRegistro = (Hashtable) vCampos.get(i);
//					//Buscamos si hay alguna de direccion para hacer la query
//					if(htRegistro.get("DIRECCION")!=null && !htRegistro.get("DIRECCION").toString().equals("")
//							||htRegistro.get("CODPOSTAL")!=null && !htRegistro.get("CODPOSTAL").toString().equals("")
//							||htRegistro.get("PAIS")!=null && !htRegistro.get("PAIS").toString().equals("")
//							||htRegistro.get("PROVINCIA")!=null && !htRegistro.get("PROVINCIA").toString().equals("")
//							||htRegistro.get("POBLACION")!=null && !htRegistro.get("POBLACION").toString().equals("")
//							||htRegistro.get("TELEFONO1")!=null && !htRegistro.get("TELEFONO1").toString().equals("")
//							||htRegistro.get("TELEFONO2")!=null && !htRegistro.get("TELEFONO2").toString().equals("")
//							||htRegistro.get("MOVIL")!=null && !htRegistro.get("MOVIL").toString().equals("")
//							||htRegistro.get("FAX1")!=null && !htRegistro.get("FAX1").toString().equals("")
//							||htRegistro.get("FAX2")!=null && !htRegistro.get("FAX2").toString().equals("")
//							||htRegistro.get("CORREOELECTRONICO")!=null && !htRegistro.get("CORREOELECTRONICO").toString().equals("")
//							||htRegistro.get("PAGINAWEB")!=null && !htRegistro.get("PAGINAWEB").toString().equals("")
//							){
//						  
//						
//						
//					}
//					
//				}
//				
//			}

			if (htDatos == null)
			{
			    throw new SIGAException("messages.envios.error.noexistendatos", null, null);
			}

            // RGG pongo el dato del IDENVIO
            htDatos.put("IDENVIO",idEnvio.toString());

	        return htDatos;
        } catch (SIGAException e) {	
        	ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.getDatosEnvio(): " + e.getMessage(), e, 1);
            throw e;
		}
		catch(Exception e)
		{
			ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.getDatosEnvio(): " + e.getMessage(), e, 1);
			throw new ClsExceptions(e,"Error obteniendo los datos del destinatario");
		}
	}

	public Hashtable darFormatoCampos(Integer idIntitucion, Integer idEnvio, String idioma, Hashtable ht,Vector vCampos) throws ClsExceptions
	{
	    Hashtable htDatos = new Hashtable();

	    try
	    {
	        

	        String sNombreCampo="";
	        String sValorCampo="";

	        for (int i=0; i<vCampos.size(); i++)
	        {
	            Hashtable htAux = (Hashtable)vCampos.elementAt(i);

			    //String sIdNombre = (String)htAux.get(CerCamposCertificadosBean.C_IDCAMPOCERTIFICADO);
			    String sTipoCampo = (String)htAux.get(CerCamposCertificadosBean.C_TIPOCAMPO);
			    String sCapturarDatos = (String)htAux.get(CerCamposCertificadosBean.C_CAPTURARDATOS);
			    String sNombre = (String)htAux.get(CerCamposCertificadosBean.C_NOMBRE);
			    String sValor = (String)htAux.get(CerProducInstiCampCertifBean.C_VALOR);
			    String sIdFormato = (String)htAux.get(CerFormatosBean.C_IDFORMATO);
			    //String sDescripcion = (String)htAux.get(CerFormatosBean.C_DESCRIPCION);
			    String sFormato = (String)htAux.get(CerFormatosBean.C_FORMATO);
			    sNombreCampo = sNombre;
			    sValorCampo = (sCapturarDatos!=null && sCapturarDatos.equalsIgnoreCase("S")) ? sValor : (String)ht.get(sNombre);
			    		    
			    if (sValorCampo==null || sValorCampo.trim().equals(""))
			    {
			        sValorCampo="";
			    }

			    else
			    {
		            if (sTipoCampo.equals(CerCamposCertificadosAdm.T_ALFANUMERICO))
		            {
		                if (sIdFormato.equals(CerFormatosAdm.K_TODO_MAYUSCULAS))
		                {
		                    sValorCampo=sValorCampo.toUpperCase();
		                }

		                else if (sIdFormato.equals(CerFormatosAdm.K_TODO_MINUSCULAS))
		                {
		                    sValorCampo=sValorCampo.toLowerCase();
		                }

		                else if (sIdFormato.equals(CerFormatosAdm.K_PRIMERA_MAYUSCULA))
		                {
		                    sValorCampo = sValorCampo.substring(0,1).toUpperCase() + sValorCampo.substring(1).toLowerCase();
		                }

		                else if (sIdFormato.equals(CerFormatosAdm.K_PRIMERAS_MAYUSCULAS))
		                {
		                    StringTokenizer st = new StringTokenizer(sValorCampo, " ");
		                    sValorCampo="";

		                    while (st.hasMoreTokens())
		                    {
		                        String token = st.nextToken();

		                        sValorCampo += token.substring(0,1).toUpperCase() + token.substring(1).toLowerCase();
		                        sValorCampo += " ";
		                    }
		                }

		                /*else
		                {
		                    throw new ClsExceptions(null, "Error al dar formato a los campos");
		                }*/
		            }

		            /*else if (sTipoCampo.equals(CerCamposCertificadosAdm.T_NUMERICO))
		            {
		                try
		                {
		                    //De momento no se hace nada.
		                }

		                catch(Exception e)
		                {
		                    throw new ClsExceptions(e, "Error al dar formato num�rico");
		                }
		            }*/

		            else if (sTipoCampo.equals(CerCamposCertificadosAdm.T_FECHA))
		            {
		                try
		                {
		                    SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);

		                    Date out = sdf.parse(sValorCampo);
		                    
		                    Locale locale;
	                        SimpleDateFormat sdfout;
	                        
		                    if (sFormato==null||sFormato.equals("") ||(sFormato!=null && sFormato.equalsIgnoreCase("dia de mes de anio"))){
		                        locale = new Locale("ES");
		                        sdfout = new SimpleDateFormat("dd/MM/yyyy",locale);
		                    } else {
		                        //Obtenemos el idioma
		                        if (sFormato.indexOf("%%")==-1){
			                        locale = new Locale("ES","es");
			                        sdfout = new SimpleDateFormat(sFormato,locale);
		                        } else {
		                            String language = sFormato.substring(sFormato.length()-2, sFormato.length()-1);
		                            sFormato = sFormato.substring(0,sFormato.length()-4);
		                            locale = new Locale("ES",language);			                        
		                        }
		                        sdfout = new SimpleDateFormat(sFormato,locale);
		                    }
		                    if(sFormato!=null && sFormato.equalsIgnoreCase("dia de mes de anio")){
		                    	
		                    	String fecha = sdfout.format(out);
		                    	sValorCampo = EjecucionPLs.ejecutarPLPKG_SIGA_FECHA_EN_LETRA(fecha,"dma",idioma);
		                    	//sValorCampo =  rc[0];
		                    }
		                    else {
		                    	sValorCampo=sdfout.format(out);
		                	}

		                 	//sValorCampo=sdfout.format(out);
		                
		            	} catch(Exception e)
		                {
		            		ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.darFormatoCampos(): " + e.getMessage(), e, 1);
		                    throw new ClsExceptions(e, "Error al dar formato fecha");
		                }
		                
		            } else {
		                throw new ClsExceptions(null, "Error al dar formato a los campos");
		            }
			    }

			    htDatos.put(sNombreCampo, sValorCampo);
	        }
	    }
	    catch(Exception e)
	    {
	    	ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.buscarEnvio(): " + e.getMessage(), e, 1);
	        throw new ClsExceptions(e, "Error al dar formato a los campos");
	    }

	    return htDatos;
	}
	
	

	private void obtenerBeanResultados(String idInstitucion, String idEnvio,
			Vector vDestDin, RowsContainer rcDestDin) {
		EnvDestinatariosBean destBean;
		String nombre;
		String apellido1;
		String apellido2;
		CenPersonaAdm personaAdm = new CenPersonaAdm(this.usrbean);
		
		for (int i = 0; i < rcDestDin.size(); i++)	{
			Row fila = (Row) rcDestDin.get(i);
			//Montamos bean de destinatario
			
			 try {
		    	CenPersonaBean personaBean = personaAdm.getIdentificador(Long.valueOf(fila.getString(EnvDestinatariosBean.C_IDPERSONA)));
		    	nombre = personaBean.getNombre();
		    	apellido1 = personaBean.getApellido1();
		    	apellido2 = personaBean.getApellido2();
		    } catch (Exception e){
		    	nombre="";
		    	apellido1="";
		    	apellido2="";
		    }
			destBean = new EnvDestinatariosBean();
			destBean.setNombre(nombre);
		    destBean.setApellidos1(apellido1);
		    destBean.setApellidos2(apellido2);
		   /*****************************************************/ 
		    destBean.setIdInstitucion(Integer.valueOf(idInstitucion));
		    destBean.setIdEnvio(Integer.valueOf(idEnvio));
		    destBean.setIdPersona(Long.valueOf(fila.getString(EnvDestinatariosBean.C_IDPERSONA)));
		    destBean.setCodigoPostal(fila.getString(CenDireccionesBean.C_CODIGOPOSTAL));
		    destBean.setCorreoElectronico(fila.getString(CenDireccionesBean.C_CORREOELECTRONICO));
		    destBean.setDomicilio(fila.getString(CenDireccionesBean.C_DOMICILIO));
		    destBean.setFax1(fila.getString(CenDireccionesBean.C_FAX1));
		    destBean.setFax2(fila.getString(CenDireccionesBean.C_FAX2));
		    destBean.setIdPais(fila.getString(CenDireccionesBean.C_IDPAIS));
		    destBean.setIdProvincia(fila.getString(CenDireccionesBean.C_IDPROVINCIA));
		    destBean.setIdPoblacion(fila.getString(CenDireccionesBean.C_IDPOBLACION));
		    destBean.setPoblacionExtranjera(fila.getString(CenDireccionesBean.C_POBLACIONEXTRANJERA));
		    destBean.setMovil(fila.getString(CenDireccionesBean.C_MOVIL));
		    
		    vDestDin.add(destBean);
		}
	}
	

	public Hashtable getCamposCorreoElectronico(EnvEnviosBean envBean,EnvDestinatariosBean beanDestinatario
			, Long idPersona, String consulta,Hashtable htDatosEnvio) throws SIGAException,ClsExceptions {
    
	    if (!envBean.getIdTipoEnvios().equals(Integer.valueOf(EnvTipoEnviosAdm.K_CORREO_ELECTRONICO))&&!envBean.getIdTipoEnvios().equals(Integer.valueOf(EnvTipoEnviosAdm.K_DOCUMENTACIONLETRADO))){
	        throw new ClsExceptions("Tipo de env�o electr�nico incorrecto");
	    }
	    List<ImagenPlantillaForm> lImagenes = (List<ImagenPlantillaForm>) htDatosEnvio.get("imagenesPlantilla");
	    if(lImagenes!=null){
		    for(ImagenPlantillaForm imagenPlantilla:lImagenes){
		    	EnvImagenPlantillaBean imagenPlantillaBean = imagenPlantilla.getImagenPlantillaBean();
		    	if(imagenPlantillaBean.isEmbebed()){
		    		htDatosEnvio.put(imagenPlantillaBean.getNombreParseo(), imagenPlantillaBean.getImagenSrcEmbebida("/"));
		    		
		    	}else{
		    		htDatosEnvio.put(imagenPlantillaBean.getNombreParseo(), imagenPlantillaBean.getImagenSrcExterna("/"));
		    		lImagenes.remove(imagenPlantillaBean);
		    	}
		    }
		    htDatosEnvio.remove("imagenesPlantilla");
	    }
	    Hashtable htPk = new Hashtable();
	    htPk.put(EnvCamposEnviosBean.C_IDINSTITUCION,envBean.getIdInstitucion());
	    htPk.put(EnvCamposEnviosBean.C_IDENVIO,envBean.getIdEnvio());
	    htPk.put(EnvCamposEnviosBean.C_TIPOCAMPO,EnvCamposAdm.K_TIPOCAMPO_E);
	    htPk.put(EnvCamposEnviosBean.C_IDCAMPO,EnvCamposPlantillaAdm.K_IDCAMPO_CUERPO);

	    String sAsunto = "";
	    String sCuerpo = "";
	    EnvCamposEnviosAdm camposEnviosAdm = new EnvCamposEnviosAdm(this.usrbean);
	    EnvCamposEnviosBean camposEnviosBean = null;

	    try {
	        Vector vCampo = camposEnviosAdm.selectByPK(htPk);
	        if (vCampo.size()>0){
	            camposEnviosBean = (EnvCamposEnviosBean)vCampo.firstElement();
	        	sCuerpo = camposEnviosBean.getValor();
	        }
	    } catch (ClsExceptions e) {
	    	ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.getCamposCorreoElectronico(): " + e.getMessage(), e, 1);
	        throw new ClsExceptions(e,"Error obteniendo el cuerpo del mensaje");
	    }

	    htPk.put(EnvCamposEnviosBean.C_IDCAMPO,EnvCamposPlantillaAdm.K_IDCAMPO_ASUNTO);
	    try {
	        Vector vCampo = camposEnviosAdm.selectByPK(htPk);
	        if (vCampo.size()>0){
	            camposEnviosBean = (EnvCamposEnviosBean)vCampo.firstElement();
	        	sAsunto = camposEnviosBean.getValor();
	        }
	    } catch (ClsExceptions e) {
	    	ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.getCamposCorreoElectronico(): " + e.getMessage(), e, 1);
	        throw new ClsExceptions(e,"Error obteniendo el asunto del mensaje");
	    }

	    //Obtenemos los valores de las etiquetas y los formateamos
//	    EnvCamposEnviosAdm admCampos = new EnvCamposEnviosAdm(this.usrbean);            
//        Vector vCampos = admCampos.obtenerCamposEnvios(envBean.getIdInstitucion().toString(), envBean.getIdEnvio().toString(), "");
//	    
//	    
//	    Hashtable htDatosEnvio = getDatosEnvio(beanDestinatario, consulta);
//	    Hashtable htDatosEnvioForm = null;
//	    try {
//	        htDatosEnvioForm = darFormatoCampos(envBean.getIdInstitucion(),envBean.getIdEnvio(),this.usrbean.getLanguage(), htDatosEnvio,vCampos);
//	    } catch (Exception e1) {
//	        throw new ClsExceptions(e1,"Error dando formato a los campos del env�o electr�nico");
//	    }
	    
	    //Sustituimos las etiquetas por sus valores
	    sAsunto = sustituirEtiquetas(sAsunto,htDatosEnvio);
	    sCuerpo = sustituirEtiquetas(sCuerpo,htDatosEnvio);
	    
	    Hashtable htCorreo = new Hashtable();
	    htCorreo.put("asunto",sAsunto);
	    htCorreo.put("cuerpo",sCuerpo);
	    return htCorreo;        
	}
	
	private String getTextoSMS(EnvEnviosBean envBean,EnvDestinatariosBean beanDestinatario, Long idPersona, String consulta) 
	throws SIGAException,ClsExceptions {
    
	    if (!envBean.getIdTipoEnvios().equals(Integer.valueOf(EnvTipoEnviosAdm.K_SMS)) && !envBean.getIdTipoEnvios().equals(Integer.valueOf(EnvTipoEnviosAdm.K_BUROSMS))){
	        throw new ClsExceptions("Tipo de env�o SMS o BuroSMS incorrecto");
	    }
	    
	    Hashtable htPk = new Hashtable();
	    htPk.put(EnvCamposEnviosBean.C_IDINSTITUCION,envBean.getIdInstitucion());
	    htPk.put(EnvCamposEnviosBean.C_IDENVIO,envBean.getIdEnvio());
	    htPk.put(EnvCamposEnviosBean.C_TIPOCAMPO,EnvCamposAdm.K_TIPOCAMPO_S);
	    htPk.put(EnvCamposEnviosBean.C_IDCAMPO,EnvCamposPlantillaAdm.K_IDCAMPO_SMS);

	    String sTexto = "";
	    EnvCamposEnviosAdm camposEnviosAdm = new EnvCamposEnviosAdm(this.usrbean);
	    EnvCamposEnviosBean camposEnviosBean = null;

	    try {
	        Vector vCampo = camposEnviosAdm.selectByPK(htPk);
	        if (vCampo.size()>0){
	            camposEnviosBean = (EnvCamposEnviosBean)vCampo.firstElement();
	            sTexto = camposEnviosBean.getValor();
	        }
	    } catch (ClsExceptions e) {
	    	ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.getTextoSMS(): " + e.getMessage(), e, 1);
	        throw new ClsExceptions(e,"Error obteniendo el texto del mensaje");
	    }
	    EnvCamposEnviosAdm admCampos = new EnvCamposEnviosAdm(this.usrbean);            
        Vector vCampos = admCampos.obtenerCamposEnvios(envBean.getIdInstitucion().toString(), envBean.getIdEnvio().toString(), "");
	    //Obtenemos los valores de las etiquetas y los formateamos
	    Hashtable htDatosEnvio = getDatosEnvio(beanDestinatario, consulta);
	    Hashtable htDatosEnvioForm = null;
	    try {
	        htDatosEnvioForm = darFormatoCampos(envBean.getIdInstitucion(),envBean.getIdEnvio(),this.usrbean.getLanguage(), htDatosEnvio,vCampos);
	    } catch (Exception e1) {
	    	ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.getTextoSMS(): " + e1.getMessage(), e1, 1);
	        throw new ClsExceptions(e1,"Error dando formato a los campos del env�o SMS o BuroSMS");
	    }
	    
	    //Sustituimos las etiquetas por sus valores
	    sTexto = sustituirEtiquetas(sTexto,htDatosEnvioForm);
	    
	    return sTexto;        
	}
	
	public String sustituirEtiquetas(String sArchivo, Hashtable etiquetas) throws SIGAException, ClsExceptions{
		String retorno = sustituirEtiquetas(sArchivo, etiquetas, AppConstants.MARCAS_ETIQUETAS_REEMPLAZO_TEXTO_ANTIGUO);
		retorno = sustituirEtiquetas(retorno, etiquetas, AppConstants.MARCAS_ETIQUETAS_REEMPLAZO_TEXTO);
		return retorno;
	}
	
	private String sustituirEtiquetas(String sArchivo, Hashtable etiquetas, String marcaInicioFin) throws SIGAException, ClsExceptions{
		try {
			if (!etiquetas.isEmpty()) {
				String key = "";
				for (Enumeration e = etiquetas.keys(); e.hasMoreElements();) {
					key = (String) e.nextElement();
					final Pattern pattern = Pattern.compile(marcaInicioFin + key + marcaInicioFin);
					final Matcher matcher = pattern.matcher(sArchivo);
					sArchivo = matcher.replaceAll((String) etiquetas.get(key));
				}
			}
			return sArchivo;
		} catch (Exception e) {
			ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.sustituirEtiquetas(): " + e.getMessage(), e, 1);
			throw new ClsExceptions(e, "Error sustituyendo etiquetas");
		}
	}

	public Row getDireccionPreferenteInstitucion(Integer idInstitucion,int idTipoEnvio)
	    throws SIGAException,ClsExceptions{
	        
	    
	    CenInstitucionAdm instAdm = new CenInstitucionAdm(this.usrbean);
        Hashtable htPkIns = new Hashtable();
        htPkIns.put(CenInstitucionBean.C_IDINSTITUCION,idInstitucion);
        CenInstitucionBean instBean;
        try {
            instBean = (CenInstitucionBean)instAdm.selectByPK(htPkIns).firstElement();
        } catch (ClsExceptions e) {
        	ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.getDireccionPreferenteInstitucion(): " + e.getMessage(), e, 1);
            throw new SIGAException("messages.general.error",e);
        }
        Long idPersona = instBean.getIdPersona();
        
        //NOMBRE TABLA
		String T_CEN_DIRECCIONES = CenDireccionesBean.T_NOMBRETABLA + " DI";
		
		//Tabla cen_Direcciones
		String DI_IDINSTITUCION = " DI." + CenDireccionesBean.C_IDINSTITUCION;
		String DI_IDPERSONA = " DI." + CenDireccionesBean.C_IDPERSONA;
		String DI_IDDIRECCION = " DI." + CenDireccionesBean.C_IDDIRECCION;
		String DI_FECHABAJA = " DI." + CenDireccionesBean.C_FECHABAJA;
		
		//Acceso a BBDD
		RowsContainer rcDir = null;
		Row fila = null;
		
		try {
			rcDir = new RowsContainer();

			String sql = "SELECT * FROM ";
		    sql += T_CEN_DIRECCIONES;

		    sql += " WHERE ";
		    sql += DI_IDINSTITUCION + " = " + idInstitucion;
		    sql += " AND "+DI_FECHABAJA + " IS NULL ";
			sql += " AND " + DI_IDPERSONA + " = " + idPersona;
			sql += " AND " + DI_IDDIRECCION + " = f_siga_getdireccion(" + 
													idInstitucion + "," + 
													idPersona + "," +
													idTipoEnvio + ")";
			
			ClsLogging.writeFileLog("EnvEnviosAdm.getDireccionPreferenteInstitucion -> QUERY: "+sql,10);
			if (rcDir.query(sql)) {
				if (rcDir.size()==1){
					fila = (Row) rcDir.get();					
				}
			}
			
		}catch(Exception e){
			ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.getDireccionPreferenteInstitucion(): " + e.getMessage(), e, 1);
            throw new ClsExceptions(e,"Error obteniendo la direcci�n preferente del destinatario");
//	        throw new SIGAException("messages.envios.error.obtenciondestinatarios",e);
	    }
		return fila;
	}


	private void insertarMensajeLogHT(EnvDestinatariosBean destBean, Hashtable htErrores, String mensaje)
	throws SIGAException{

	    if (destBean!=null) {
	        if (mensaje.length()>500) {
	        	mensaje = mensaje.substring(0,500);
	        }
	    	htErrores.put(destBean,mensaje);
	    }
	}

	private void insertarMensajeLogHT(EnvDestinatariosBean destBean, Hashtable htErrores, Exception e)
	throws SIGAException{
		ClsLogging.writeFileLog("EnvEnviosAdm.insertarMensajeLogHT() - INICIO");
		String mensajeNuevo = "";
	    String languageCode = "";
	    String userCode = "";
	    String institucion = "";

	    if (destBean!=null) {

			ClsLogging.writeFileLog("ERROR AL ENVIAR. Mensaje: "+e.getLocalizedMessage(),1);

		    if (e==null){
		      ClsLogging.writeFileLog("ERROR ENVIOS: @@@@ Excepcion NULA @@@@", 1);
		      ClsLogging.writeFileLog("ERROR ENVIOS: @@@@ La excepcion no ha sido preparada @@@@", 1);
		    }
		    else
		    {
		      
		      try {
		        ExceptionManager mgr = new ExceptionManager(e, languageCode, this.usrbean.getUserName(), null, this.usrbean.getLocation());
		        ClsLogging.writeFileLogError(mgr.getLogCompleteMessage(languageCode), e, this.usrbean, 1);
		      } catch (ClsExceptions ex) { 
		    	  ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.insertarMensajeLogHT(): " + ex.getMessage(), ex, 1);
		    	  ex.printStackTrace();
		      }
		    }
			if (e instanceof SIGAException) {
				SIGAException se = (SIGAException) e;
				//ClsLogging.writeFileLogError("ERROR AL ENVIAR. Mensaje: "+se.getMsg(""),se,7);
				mensajeNuevo += se.getMsg("");// + " ----- ";
				/*
		      	StringWriter sw = new StringWriter();
		      	PrintWriter pw = new PrintWriter(sw);
		      	se.printStackTrace(pw);
		      	mensajeNuevo += sw.toString();^
		      	*/
			} else {
				//ClsLogging.writeFileLogError("ERROR AL ENVIAR. Mensaje: "+e.getLocalizedMessage(),e,7);
				mensajeNuevo += e.getLocalizedMessage();// + " ----- ";
				/*
		      	StringWriter sw = new StringWriter();
		      	PrintWriter pw = new PrintWriter(sw);
		      	e.printStackTrace(pw);
		      	mensajeNuevo += sw.toString();
				*/
			}
			
			insertarMensajeLogHT(destBean,htErrores,mensajeNuevo);
	    }
	    ClsLogging.writeFileLog("EnvEnviosAdm.insertarMensajeLogHT() - FIN");
	}

	
	private String obtenerIdPersonaInstitucion(Integer idInstitucion)
	throws SIGAException {

		String salida = "";
	    CenInstitucionAdm institucionAdm = new CenInstitucionAdm(this.usrbean);
	    Hashtable htDest = new Hashtable();
	    htDest.put(CenInstitucionBean.C_IDINSTITUCION,idInstitucion);
	    
	    try {
	    	CenInstitucionBean instiBean = (CenInstitucionBean)institucionAdm.selectByPK(htDest).firstElement();
	        if (instiBean!=null) {
	        	salida = instiBean.getIdPersona().toString();
	        }
	        return salida;
	    } catch (ClsExceptions e) {
	    	ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.obtenerIdPersonaInstitucion(): " + e.getMessage(), e, 1);
	        throw new SIGAException(e.getMessage());
	    }	        
    }
        
	

	public void generarLogEnvioHT(Vector vDestinatarios,EnvRemitentesBean remitente,String documentos, Hashtable htErrores, EnvEnviosBean envBean)
	throws SIGAException, ClsExceptions {
	    
		BufferedWriter bwOut = null;
	    String sIdInstitucion = String.valueOf(envBean.getIdInstitucion());
	    String sIdEnvio = String.valueOf(envBean.getIdEnvio());
	    String sFicheroLog = "";
	    try {
	        // RGG creo los directorios
	    	FileHelper.mkdirs(this.getPathEnvio(sIdInstitucion,sIdEnvio));
	    	// RGG 08-09-2005 Cambio para que el fichero de log sea unico 
	        sFicheroLog = this.getPathEnvio(sIdInstitucion,sIdEnvio) + File.separator + "informeEnvio" + ".log.xls"; 
	        File borrar = new File(sFicheroLog);
	        if (borrar.exists()) borrar.delete();
	        bwOut = new BufferedWriter(new FileWriter(sFicheroLog));
	    } catch (Exception e1) {
	    	ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.generarLogEnvioHT(): " + e1.getMessage(), e1, 1);
	        throw new SIGAException("messages.general.error",e1);
	    }
	    
	    final String separador = ClsConstants.SEPARADOR; 
        //String sLineaCabecera = "ENVIO"+separador+"NIF/CIF"+separador+"NOMBRE"+separador+"APELLIDO 1"+separador+"APELLIDO 2"+separador+"FAX 1"+separador+"FAX 2"+separador+"MOVIL"+separador+"CORREO ELECTRONICO"+separador+"DOMICILIO"+separador+"PROVINCIA"+separador+"POBLACION"+separador+"PAIS"+separador+"MENSAJE"+separador;
        String cadRemitente = "";
        String txtRemitente = "";
        if(remitente!=null){
        	cadRemitente = "REMITENTE"+separador;
        	if(remitente.getDescripcion()!=null && !remitente.getDescripcion().equals("")){
	        	
	     	    txtRemitente = remitente.getDescripcion(); 
        	}else{
        		CenPersonaAdm personaAdm = new CenPersonaAdm(this.usrbean);
	     	    Hashtable htPk = new Hashtable();
	     	    htPk.put(CenPersonaBean.C_IDPERSONA,remitente.getIdPersona());
	     	  	Vector vPersona = personaAdm.select(htPk);
	     	    CenPersonaBean personaBean = (CenPersonaBean) vPersona.firstElement();
	     	    txtRemitente = personaBean.getNombre()+" "+personaBean.getApellido1();
        		
        	}
        }
        
	    String sLineaCabecera = "ENVIO"+separador+"DESCRIPCION"+separador+"FECHA CREACION"+separador+cadRemitente+"NIF/CIF"+separador+"NOMBRE"+separador+"APELLIDO 1"+separador+"APELLIDO 2"+separador+"FAX 1"+separador+"FAX 2"+separador+"MOVIL"+separador+"TEL.DESPACHO"+separador+"CORREO ELECTRONICO"+separador+"DOMICILIO"+separador+"PROVINCIA"+separador+"POBLACION"+separador+"PAIS"+separador+"MENSAJE"+separador+"DOCUMENTOS"+separador;
        //envBean.getDescripcion()
        //envBean.getFecha Creacion envio
        //Remitente
        //envBean.getUsuMod()
        
        CenPoblacionesAdm admPob = new CenPoblacionesAdm(this.usrbean);
        CenProvinciaAdm admPro = new CenProvinciaAdm(this.usrbean);
        CenPaisAdm admPais = new CenPaisAdm(this.usrbean);
        
	    //Por cada bean insertamos una l�nea en el archivo de log, 
	    //con todos los campos del bean separados por comas
	    if (vDestinatarios!=null && vDestinatarios.size()>0) {
	        for (int i=0;i<vDestinatarios.size();i++){
	        	EnvDestinatariosBean destBean = (EnvDestinatariosBean)vDestinatarios.elementAt(i);
	            String sLinea = "";
	            
	            /*
	            while (eCampos.hasMoreElements()){
	            	Object key = eCampos.nextElement();
	            	Object val = htBean.get(key);
            		sLinea = sLinea + val.toString() + separador;
	            }
	            sLineaCabecera = sLineaCabecera + "MENSAJE" + separador;
	            */
	            sLinea += sIdEnvio + separador;
	            sLinea += envBean.getDescripcion() + separador;
	            if(envBean.getFechaCreacion().equalsIgnoreCase("SYSDATE"))
	            	sLinea += GstDate.getHoyJava() + separador;
	            else
	            	sLinea += GstDate.getFormatedDateLong("", envBean.getFechaCreacion())  + separador;
	            if(remitente!=null)
	            	sLinea += txtRemitente + separador;
	            
	            
	            if(destBean.getTipoDestinatario()!=null && destBean.getTipoDestinatario().equalsIgnoreCase(EnvDestinatariosBean.TIPODESTINATARIO_SCSPERSONAJG)){
	            	Hashtable htPer = new Hashtable();
					htPer.put(ScsPersonaJGBean.C_IDPERSONA,destBean.getIdPersona());
					htPer.put(ScsPersonaJGBean.C_IDINSTITUCION,destBean.getIdInstitucion());
					ScsPersonaJGAdm admPerJG = new ScsPersonaJGAdm(this.usrbean);
					Vector v = admPerJG.selectByPK(htPer);
					ScsPersonaJGBean beanPersonaJG = null;
					if (v!=null && v.size()>0) {
						beanPersonaJG = (ScsPersonaJGBean) v.get(0);
					sLinea += beanPersonaJG.getNif() + separador;
					sLinea += beanPersonaJG.getNombre() + separador;
					sLinea += beanPersonaJG.getApellido1() + separador;
					sLinea += beanPersonaJG.getApellido2() + separador;
	            
					}else{					
						sLinea += "" + separador;
						sLinea += "" + separador;
						sLinea += "" + separador;
						sLinea += "" + separador;							
					}
					
	            }else if(destBean.getTipoDestinatario()!=null && destBean.getTipoDestinatario().equalsIgnoreCase(EnvDestinatariosBean.TIPODESTINATARIO_SCSJUZGADO)){
	            	Hashtable htJuz = new Hashtable();
					htJuz.put(ScsJuzgadoBean.C_IDJUZGADO,destBean.getIdPersona());
					htJuz.put(ScsJuzgadoBean.C_IDINSTITUCION,destBean.getIdInstitucion());
					ScsJuzgadoAdm admJuz = new ScsJuzgadoAdm(this.usrbean);
					Vector v = admJuz.selectByPK(htJuz);
					ScsJuzgadoBean beanJuzgado= null;
					if (v!=null && v.size()>0) {
						beanJuzgado = (ScsJuzgadoBean) v.get(0);
					sLinea += "" + separador;
					sLinea += beanJuzgado.getNombre() + separador;
					sLinea += "" + separador;
					sLinea += "" + separador;
					
					}else{
						sLinea += "" + separador;
						sLinea += "" + separador;
						sLinea += "" + separador;
						sLinea += "" + separador;							
					}
					
	            }else if(destBean.getTipoDestinatario()!=null && destBean.getTipoDestinatario().equalsIgnoreCase(EnvDestinatariosBean.TIPODESTINATARIO_SCSPROCURADOR)){
	            	Hashtable htProc = new Hashtable();
					htProc.put(ScsProcuradorBean.C_IDPROCURADOR,destBean.getIdPersona());
					htProc.put(ScsProcuradorBean.C_IDINSTITUCION,destBean.getIdInstitucion());
					ScsProcuradorAdm procuradorAdm = new ScsProcuradorAdm(this.usrbean);
					Vector v = procuradorAdm.selectByPK(htProc);
					ScsProcuradorBean procuradorBean = null;
					if (v!=null && v.size()>0) {
						procuradorBean = (ScsProcuradorBean) v.get(0);
						sLinea += "" + separador;
						sLinea += procuradorBean.getNombre() + separador;
						sLinea += procuradorBean.getApellido1() + separador;
						sLinea += procuradorBean.getApellido2() + separador;				
					
	            }else{
						sLinea += "" + separador;
						sLinea += "" + separador;
						sLinea += "" + separador;
						sLinea += "" + separador;							
					}
					
	            }else if(destBean.getTipoDestinatario()!=null && destBean.getTipoDestinatario().equalsIgnoreCase(EnvDestinatariosBean.TIPODESTINATARIO_CENPERSONA)){
	            	CenPersonaAdm admPer = new CenPersonaAdm(this.usrbean);
		            Hashtable htPer = new Hashtable();
					htPer.put(CenPersonaBean.C_IDPERSONA,destBean.getIdPersona());
					Vector v = admPer.selectByPK(htPer);
					CenPersonaBean beanPersona = null;
					if (v!=null && v.size()>0) {
						beanPersona = (CenPersonaBean) v.get(0);
					sLinea += beanPersona.getNIFCIF() + separador;
					sLinea += beanPersona.getNombre() + separador;
					sLinea += beanPersona.getApellido1() + separador;
					sLinea += beanPersona.getApellido2() + separador;
						
					}else{
						sLinea += "" + separador;
						sLinea += "" + separador;
						sLinea += "" + separador;
						sLinea += "" + separador;							
					}
					
	            }else{
	            	throw new SIGAException("Este tipo de persona no est� controlado por el sistema");
	            }
	            sLinea += destBean.getFax1() + separador;
	            sLinea += destBean.getFax2() + separador;
	            sLinea += destBean.getMovil() + separador;
	            GenParametrosAdm paramAdm = new GenParametrosAdm(this.usrbean);
	    		String preferencia = paramAdm.getValor(destBean.getIdInstitucion().toString(),"ENV","TIPO_ENVIO_PREFERENTE","1");		    		
	    		
	    		Hashtable direcciones = new Hashtable();
	    		EnvEnviosAdm enviosAdm = new EnvEnviosAdm(this.usrbean);        		 
	    		CenDireccionesAdm direccionAdm = new CenDireccionesAdm(this.usrbean);
    			direcciones=direccionAdm.getEntradaDireccionEspecifica(destBean.getIdPersona().toString(),destBean.getIdInstitucion().toString(),"2");
    		 
    			//si tiene direccion de tipo despacho saca el telefono1 del despacho
    		    if (direcciones!=null && direcciones.size()>0) {		           
    		    	Hashtable htDir = (Hashtable)direcciones;
			    	sLinea += htDir.get(CenDireccionesBean.C_TELEFONO1)+separador;				      			       
    		    }else {
    		    	//si no tiene direccion de tipo despacho se busca la direcci�n preferente y se saca el telefono1 despacho.
    		    		Hashtable direccion=direccionAdm.getEntradaDireccionEspecifica(destBean.getIdPersona().toString(),destBean.getIdInstitucion().toString(),preferencia);		    				
	    				if (direccion!=null && direccion.size()!=0) {
	    					Hashtable htDir = (Hashtable)direccion;
	    					sLinea += htDir.get(CenDireccionesBean.C_TELEFONO1)+separador;		    					
	    				}else {    					
	    					//si no tiene ni direccion de tipo despacho, ni una direcci�n preferente se saca el telefono1 de la primera direcci�n que se encuentre.
	    					direccion=direccionAdm.getEntradaDireccionEspecifica(destBean.getIdPersona().toString(),destBean.getIdInstitucion().toString(),"");
	    					if (direccion!=null || direccion.size()!=0) {
	    					Hashtable htDir = (Hashtable)direccion;
	    					//String telefono=htDir.get(CenDireccionesBean.C_TELEFONO1).toString();
	    					if ((htDir.get(CenDireccionesBean.C_TELEFONO1))!=null){
	    						sLinea += htDir.get(CenDireccionesBean.C_TELEFONO1)+separador;	
	    					}else{
	    						sLinea += " " +separador;
	    					}
	    					
	    					}
	    				}        		    	
    		    }
	            sLinea += destBean.getCorreoElectronico() + separador;
	            sLinea += UtilidadesString.sustituirParaExcell(destBean.getDomicilio()) + separador;
	            if(destBean.getProvincia()!=null && !destBean.getProvincia().trim().equals("") )
	            	sLinea += destBean.getProvincia() + separador;
	            else
	            	sLinea += admPro.getDescripcion(destBean.getIdProvincia()) + separador;
	            
	            
	            if (destBean.getIdPais().equals("") || destBean.getIdPais().equals(ClsConstants.ID_PAIS_ESPANA)) {
	            	if(destBean.getPoblacion()!=null && !destBean.getPoblacion().trim().equals("") )
		            	sLinea += destBean.getPoblacion() + separador;
	            	else
	            		sLinea += admPob.getDescripcion(destBean.getIdPoblacion()) + separador;
	            	sLinea += " " + separador;
	            } else {
	            	if(destBean.getPoblacionExtranjera()!=null && !destBean.getPoblacionExtranjera().equalsIgnoreCase("null"))
	            		sLinea += destBean.getPoblacionExtranjera() + separador;
	            	else
	            		sLinea += "" + separador;
	            	if(destBean.getPais()!=null && !destBean.getPais().trim().equals("") )
		            	sLinea += destBean.getPais() + separador;
	            	else
	            		sLinea += admPais.getDescripcion(destBean.getIdPais()) + separador;
	            }
	           
	                String err = (htErrores.get(destBean)!=null)?"ERROR: "+(String)htErrores.get(destBean):"OK";
        		    sLinea = sLinea + UtilidadesString.sustituirParaExcell(UtilidadesString.getMensajeIdioma("ES", err)) + separador; 
        		    if(documentos!=null)
        		    	sLinea += documentos + separador;
        		
        			
        		  
	            try {
	            	if (i==0) {
	            		// RGG cambio a formato DOS
	            		sLineaCabecera += "\r\n";
	            		bwOut.write(sLineaCabecera);
	            		//bwOut.newLine();
	            	}
	            	ClsLogging.writeFileLogWithoutSession(sLinea);
	        		// RGG cambio a formato DOS
	        		sLinea += "\r\n";
	            	bwOut.write(sLinea);
	                //bwOut.newLine();
	                
	            } catch (Exception e2) {
	            	ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.generarLogEnvioHT(): " + e2.getMessage(), e2, 1);
	                throw new SIGAException("messages.general.error",e2);
	            }
	        
	        }
	    
	    } else {
	        try {
	            bwOut.write("EL ENVIO NO TIENE DESTINATARIOS. NO SE HA ENVIADO NADA.");
	            bwOut.newLine();
	        } catch (Exception e2) {
	        	ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.generarLogEnvioHT(): " + e2.getMessage(), e2, 1);
	            throw new SIGAException("messages.general.error",e2);
	        }
	    }
	    
	    try {
	        bwOut.close();
	    } catch (IOException e2) {
	    	ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.generarLogEnvioHT(): " + e2.getMessage(), e2, 1);
	        throw new SIGAException("messages.general.error",e2);
	    }        
	    
	}
	
	public void generarLogEnvioHTTuneado(Vector vDestinatarios,EnvRemitentesBean remitente,String documentos, Hashtable htErrores, EnvEnviosBean envBean)
			throws SIGAException, ClsExceptions {
			    
				BufferedWriter bwOut = null;
			    String sIdInstitucion = String.valueOf(envBean.getIdInstitucion());
			    String sIdEnvio = String.valueOf(envBean.getIdEnvio());
			    String sFicheroLog = "";
			    try {
			        // RGG creo los directorios
			    	FileHelper.mkdirs(this.getPathEnvio(sIdInstitucion,sIdEnvio));
			    	// RGG 08-09-2005 Cambio para que el fichero de log sea unico 
			        sFicheroLog = this.getPathEnvio(sIdInstitucion,sIdEnvio) + File.separator + "informeEnvio" + ".log.xls"; 
			        File borrar = new File(sFicheroLog);
			        if (borrar.exists()) borrar.delete();
			        bwOut = new BufferedWriter(new FileWriter(sFicheroLog));
			    } catch (Exception e1) {
			    	ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.generarLgEnvioHTTuneado(): " + e1.getMessage(), e1, 1);
			        throw new SIGAException("messages.general.error",e1);
			    }
			    
			    final String separador = ClsConstants.SEPARADOR; 
		        //String sLineaCabecera = "ENVIO"+separador+"NIF/CIF"+separador+"NOMBRE"+separador+"APELLIDO 1"+separador+"APELLIDO 2"+separador+"FAX 1"+separador+"FAX 2"+separador+"MOVIL"+separador+"CORREO ELECTRONICO"+separador+"DOMICILIO"+separador+"PROVINCIA"+separador+"POBLACION"+separador+"PAIS"+separador+"MENSAJE"+separador;
		        String cadRemitente = "";
		        String txtRemitente = "";
		        if(remitente!=null){
		        	cadRemitente = "REMITENTE"+separador;
		        	if(remitente.getDescripcion()!=null && !remitente.getDescripcion().equals("")){
			        	
			     	    txtRemitente = remitente.getDescripcion(); 
		        	}else{
		        		CenPersonaAdm personaAdm = new CenPersonaAdm(this.usrbean);
			     	    Hashtable htPk = new Hashtable();
			     	    htPk.put(CenPersonaBean.C_IDPERSONA,remitente.getIdPersona());
			     	  	Vector vPersona = personaAdm.select(htPk);
			     	    CenPersonaBean personaBean = (CenPersonaBean) vPersona.firstElement();
			     	    txtRemitente = personaBean.getNombre()+" "+personaBean.getApellido1();
		        		
		        	}
		        }
		        
			    String sLineaCabecera = "ENVIO"+separador+"DESCRIPCION"+separador+"FECHA CREACION"+separador+cadRemitente+"NIF/CIF"+separador+"NOMBRE"+separador+"APELLIDO 1"+separador+"APELLIDO 2"+separador+"FAX 1"+separador+"FAX 2"+separador+"MOVIL"+separador+"TEL.DESPACHO"+separador+"CORREO ELECTRONICO"+separador+"DOMICILIO"+separador+"PROVINCIA"+separador+"POBLACION"+separador+"PAIS"+separador+"MENSAJE"+separador+"DOCUMENTOS"+separador;
		        //envBean.getDescripcion()
		        //envBean.getFecha Creacion envio
		        //Remitente
		        //envBean.getUsuMod()
		        
			    //Por cada bean insertamos una l�nea en el archivo de log, 
			    //con todos los campos del bean separados por comas
			    if (vDestinatarios!=null && vDestinatarios.size()>0) {
			        for (int i=0;i<vDestinatarios.size();i++){
			        	EnvDestinatariosBean destBean = (EnvDestinatariosBean)vDestinatarios.elementAt(i);
			            String sLinea = "";
			            
			            /*
			            while (eCampos.hasMoreElements()){
			            	Object key = eCampos.nextElement();
			            	Object val = htBean.get(key);
		            		sLinea = sLinea + val.toString() + separador;
			            }
			            sLineaCabecera = sLineaCabecera + "MENSAJE" + separador;
			            */
			            sLinea += sIdEnvio + separador;
			            sLinea += envBean.getDescripcion() + separador;
			            if(envBean.getFechaCreacion().equalsIgnoreCase("SYSDATE"))
			            	sLinea += GstDate.getHoyJava() + separador;
			            else
			            	sLinea += GstDate.getFormatedDateLong("", envBean.getFechaCreacion())  + separador;
			            if(remitente!=null)
			            	sLinea += txtRemitente + separador;
			            
			            
			            if(destBean.getTipoDestinatario()!=null && destBean.getTipoDestinatario().equalsIgnoreCase(EnvDestinatariosBean.TIPODESTINATARIO_SCSPERSONAJG)){
//			            	Hashtable htPer = new Hashtable();
//							htPer.put(ScsPersonaJGBean.C_IDPERSONA,destBean.getIdPersona());
//							htPer.put(ScsPersonaJGBean.C_IDINSTITUCION,destBean.getIdInstitucion());
//							ScsPersonaJGAdm admPerJG = new ScsPersonaJGAdm(this.usrbean);
//							Vector v = admPerJG.selectByPK(htPer);
//							ScsPersonaJGBean beanPersonaJG = null;
//							if (v!=null && v.size()>0) {
//								beanPersonaJG = (ScsPersonaJGBean) v.get(0);
							sLinea += destBean.getNifcif()!=null?destBean.getNifcif():"" ;
							sLinea += separador;
							sLinea += destBean.getNombre() + separador;
							sLinea += destBean.getApellidos1() + separador;
							sLinea += destBean.getApellidos2()!=null?destBean.getApellidos2():"" ;
							sLinea += separador;
			            
							
							
			            }else if(destBean.getTipoDestinatario()!=null && destBean.getTipoDestinatario().equalsIgnoreCase(EnvDestinatariosBean.TIPODESTINATARIO_SCSJUZGADO)){
			            	sLinea += destBean.getNifcif()!=null?destBean.getNifcif():"" ;
			            	sLinea += separador;
							sLinea += destBean.getNombre() + separador;
							sLinea += destBean.getApellidos1() + separador;
							sLinea += destBean.getApellidos2()!=null?destBean.getApellidos2():"" ;
							sLinea += separador;

							
			            }else if(destBean.getTipoDestinatario()!=null && destBean.getTipoDestinatario().equalsIgnoreCase(EnvDestinatariosBean.TIPODESTINATARIO_SCSPROCURADOR)){
			            	sLinea += destBean.getNifcif()!=null?destBean.getNifcif():"" ;
			            	sLinea += separador;
							sLinea += destBean.getNombre() + separador;
							sLinea += destBean.getApellidos1() + separador;
							sLinea += destBean.getApellidos2()!=null?destBean.getApellidos2():"" ;
							sLinea += separador;

							
			            }else if(destBean.getTipoDestinatario()!=null && destBean.getTipoDestinatario().equalsIgnoreCase(EnvDestinatariosBean.TIPODESTINATARIO_CENPERSONA)){
			            	sLinea += destBean.getNifcif()!=null?destBean.getNifcif():"";
			            	sLinea += separador;
							sLinea += destBean.getNombre() + separador; 
							sLinea += destBean.getApellidos1() + separador;
							sLinea += destBean.getApellidos2()!=null?destBean.getApellidos2():"" ;
							sLinea += separador;

							
			            }else{
			            	throw new SIGAException("Este tipo de persona no est� controlado por el sistema");
			            }
			            sLinea += destBean.getFax1() + separador;
			            sLinea += destBean.getFax2() + separador;
			            sLinea += destBean.getMovil() + separador;
			            GenParametrosAdm paramAdm = new GenParametrosAdm(this.usrbean);
			    		String preferencia = paramAdm.getValor(destBean.getIdInstitucion().toString(),"ENV","TIPO_ENVIO_PREFERENTE","1");		    		
			    		
			    		Hashtable direcciones = new Hashtable();
			    		EnvEnviosAdm enviosAdm = new EnvEnviosAdm(this.usrbean);        		 
			    		CenDireccionesAdm direccionAdm = new CenDireccionesAdm(this.usrbean);
		    			direcciones=direccionAdm.getEntradaDireccionEspecifica(destBean.getIdPersona().toString(),destBean.getIdInstitucion().toString(),"2");
		    		 
		    			//si tiene direccion de tipo despacho saca el telefono1 del despacho
		    		    if (direcciones!=null && direcciones.size()>0) {		           
		    		    	Hashtable htDir = (Hashtable)direcciones;
					    	sLinea += htDir.get(CenDireccionesBean.C_TELEFONO1)+separador;				      			       
		    		    }else {
		    		    	//si no tiene direccion de tipo despacho se busca la direcci�n preferente y se saca el telefono1 despacho.
		    		    		Hashtable direccion=direccionAdm.getEntradaDireccionEspecifica(destBean.getIdPersona().toString(),destBean.getIdInstitucion().toString(),preferencia);		    				
			    				if (direccion!=null && direccion.size()!=0) {
			    					Hashtable htDir = (Hashtable)direccion;
			    					sLinea += htDir.get(CenDireccionesBean.C_TELEFONO1)+separador;		    					
			    				}else {    					
			    					//si no tiene ni direccion de tipo despacho, ni una direcci�n preferente se saca el telefono1 de la primera direcci�n que se encuentre.
			    					direccion=direccionAdm.getEntradaDireccionEspecifica(destBean.getIdPersona().toString(),destBean.getIdInstitucion().toString(),"");
			    					if (direccion!=null || direccion.size()!=0) {
			    					Hashtable htDir = (Hashtable)direccion;
			    					//String telefono=htDir.get(CenDireccionesBean.C_TELEFONO1).toString();
			    					if ((htDir.get(CenDireccionesBean.C_TELEFONO1))!=null){
			    						sLinea += htDir.get(CenDireccionesBean.C_TELEFONO1)+separador;	
			    					}else{
			    						sLinea += " " +separador;
			    					}
			    					
			    					}
			    				}        		    	
		    		    }
			            sLinea += destBean.getCorreoElectronico() + separador;
			            sLinea += UtilidadesString.sustituirParaExcell(destBean.getDomicilio()) + separador;
			            if(destBean.getProvincia()!=null && !destBean.getProvincia().trim().equals("") )
			            	sLinea += destBean.getProvincia() + separador;
			            else
			            	sLinea += "" + separador;
			            
			            
			            if (destBean.getIdPais().equals("") || destBean.getIdPais().equals(ClsConstants.ID_PAIS_ESPANA)) {
			            	if(destBean.getPoblacion()!=null && !destBean.getPoblacion().trim().equals("") )
				            	sLinea += destBean.getPoblacion() + separador;
			            	else
			            		sLinea += "" + separador;
			            	sLinea += " " + separador;
			            } else {
			            	if(destBean.getPoblacionExtranjera()!=null && !destBean.getPoblacionExtranjera().equalsIgnoreCase("null"))
			            		sLinea += destBean.getPoblacionExtranjera() + separador;
			            	else
			            		sLinea += "" + separador;
			            	if(destBean.getPais()!=null && !destBean.getPais().trim().equals("") )
				            	sLinea += destBean.getPais() + separador;
			            	else
			            		sLinea += "" + separador;
			            }
			           
			                String err = (htErrores.get(destBean)!=null)?"ERROR: "+(String)htErrores.get(destBean):"OK";
		        		    sLinea = sLinea + UtilidadesString.sustituirParaExcell(UtilidadesString.getMensajeIdioma("ES", err)) + separador; 
		        		    if(documentos!=null)
		        		    	sLinea += documentos + separador;
		        		
		        			
		        		  
			            try {
			            	if (i==0) {
			            		// RGG cambio a formato DOS
			            		sLineaCabecera += "\r\n";
			            		bwOut.write(sLineaCabecera);
			            		//bwOut.newLine();
			            	}
//			            	ClsLogging.writeFileLogWithoutSession(sLinea);
			        		// RGG cambio a formato DOS
			        		sLinea += "\r\n";
			            	bwOut.write(sLinea);
			                //bwOut.newLine();
			                
			            } catch (Exception e2) {
			            	ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.generarLogEnvioHTTuneado(): " + e2.getMessage(), e2, 1);
			                throw new SIGAException("messages.general.error",e2);
			            }
			        
			        }
			    
			    } else {
			        try {
			            bwOut.write("EL ENVIO NO TIENE DESTINATARIOS. NO SE HA ENVIADO NADA.");
			            bwOut.newLine();
			        } catch (Exception e2) {
			        	ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.generarLogEnvioHTTuneado(): " + e2.getMessage(), e2, 1);
			            throw new SIGAException("messages.general.error",e2);
			        }
			    }
			    
			    try {
			        bwOut.close();
			    } catch (IOException e2) {
			    	ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.generarLogEnvioHTTuneado(): " + e2.getMessage(), e2, 1);
			        throw new SIGAException("messages.general.error",e2);
			    }        
			    
			}


	
	public void generarLogEnvioExceptionHT(EnvEnviosBean envBean, Exception ex)
	throws SIGAException, ClsExceptions{
    
	    BufferedWriter bwOut = null;
	    String sIdInstitucion = String.valueOf(envBean.getIdInstitucion());
	    String sIdEnvio = String.valueOf(envBean.getIdEnvio());
	    
	    String sFicheroLog = "";
	    try {
	        // RGG creo los directorios
	    	FileHelper.mkdirs(this.getPathEnvio(sIdInstitucion,sIdEnvio));
	
	        sFicheroLog = this.getPathEnvio(sIdInstitucion,sIdEnvio) + File.separator + "informeEnvio" + ".log.xls"; 
	        File borrar = new File(sFicheroLog);
	        if (borrar.exists()) borrar.delete();
	        
	        bwOut = new BufferedWriter(new FileWriter(sFicheroLog));
	    } catch (Exception e1) {
	    	ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.generarLogEnvioExceptionHT(): " + e1.getMessage(), e1, 1);
	        throw new ClsExceptions(e1,"Error al crear fichero de log Envios");
	    }
	    
		String mensajeNuevo = "ERROR AL ENVIAR. Mensaje: ";
		if (ex instanceof SIGAException) {
			SIGAException se = (SIGAException) ex;
			ClsLogging.writeFileLogError("ERROR AL ENVIAR. Mensaje: "+se.getMsg(""),se,3);
			mensajeNuevo += se.getMsg("") + " ----- ";
	      	StringWriter sw = new StringWriter();
	      	PrintWriter pw = new PrintWriter(sw);
	      	se.printStackTrace(pw);
	      	mensajeNuevo += sw.toString();
	
		} else {
		
			ClsLogging.writeFileLogError("ERROR AL ENVIAR. Mensaje: "+ex.getLocalizedMessage(),ex,3);
			mensajeNuevo += ex.getLocalizedMessage() + " ----- ";
	      	StringWriter sw = new StringWriter();
	      	PrintWriter pw = new PrintWriter(sw);
	      	ex.printStackTrace(pw);
	      	mensajeNuevo += sw.toString();
		}
	
		try {
			// RGG cambio a formato DOS
			mensajeNuevo += "\r\n";
	        bwOut.write(mensajeNuevo);
	        //bwOut.newLine();
	    } catch (Exception e2) {
	    	ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.generarLogEnvioHTTuneado(): " + e2.getMessage(), e2, 1);
	        throw new SIGAException("messages.general.error",e2,new String[] {"modulo.envios"});
	    }
	
	    
	    try {
	        bwOut.close();
	    } catch (IOException e2) {
	    	ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.generarLogEnvioHTTuneado(): " + e2.getMessage(), e2, 1);
	        throw new SIGAException("messages.general.error",e2,new String[] {"modulo.envios"});
	    }        
	}


	public String getPathDescargaEnviosOrdinarios(EnvEnviosBean envBean) throws ClsExceptions{
		SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);
        Calendar cal = Calendar.getInstance();
		Date dat;
        try {
            dat = sdf.parse(envBean.getFechaCreacion());
            
        } catch (ParseException e1) {
        	dat = new Date();
            //throw new ClsExceptions();
        }
        cal.setTime(dat);
        String anio = String.valueOf(cal.get(Calendar.YEAR));
        String mes = String.valueOf(cal.get(Calendar.MONTH)+1);
        String dia = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));

        // Obtengo el pathFTP
        String pathFTP = "";
        GenParametrosAdm paramAdm = new GenParametrosAdm(this.usrbean);
    	pathFTP = paramAdm.getValor(envBean.getIdInstitucion().toString(),"ENV","PATH_DESCARGA_ENVIOS_ORDINARIOS","");
        
        Date hoy = new Date();
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd_HHmm");
        String sHoy = sdf2.format(hoy);
        
        String pathDestino = pathFTP + File.separator + envBean.getIdInstitucion().toString() + File.separator + anio + File.separator + mes + File.separator + dia + File.separator + envBean.getIdEnvio().toString() + File.separator + sHoy + File.separator;
		return pathDestino;
		
	}
	/**
	 * 
	 * @param envBean. Bean del envio definido. Tendra la plantilla del envio y la plantilla de generacion. 
	 * @param vDestinatarios
	 * @param htErrores
	 * @param generarLog
	 * @return
	 * @throws SIGAException
	 * @throws ClsExceptions
	 */
	public String enviarCorreoOrdinario(EnvEnviosBean envBean, Vector vDestinatarios, Hashtable htErrores, boolean generarLog) 
	throws SIGAException,ClsExceptions{

		boolean errores = false;
		String nombreFicheroXX="";
		String nombreDestinatarioXX="";
		String idenvio=envBean.getIdEnvio().toString();
		String idInstitucion=envBean.getIdInstitucion().toString();
		String pathDocumentosAdjuntos="";
		EnvEnviosAdm envioAdm = new EnvEnviosAdm(this.usrbean);	
		StringBuffer txtDocumentos = new StringBuffer();
		try{
			
			/**PathDocumentosAdjuntos, que es la direcci�n donde se guarda en local
			 *  o la carpeta que se designe los archivos**/			
			try {
				   pathDocumentosAdjuntos = envioAdm.getPathEnvio(idInstitucion,idenvio);
				} catch (Exception e) {
					ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.enviarCorreoOrdinario(): " + e.getMessage(), e, 1);
					new ClsExceptions (e, "Error al recuperar el envio");
			   	}

			// PREPARACION
			//////////////////////////////////
			if (!envBean.getIdTipoEnvios().equals(Integer.valueOf(EnvTipoEnviosAdm.K_CORREO_ORDINARIO))){
				throw new ClsExceptions("Tipo de env�o ordinario incorrecto");
			}
			// PLANTILLA DE GENERACION. 
			//Con esta plantilla se genera un documento que se generara por cada envio y cada destinatario. 
			//Luego se movera al lugar ftp especificado en por el pathDestino 
			String pathDestino = getPathDescargaEnviosOrdinarios(envBean);
			// BUCLE DE DESTINATARIOS
			//////////////////////////////////
			boolean existePlantilla = false;
			if (vDestinatarios!=null){
				//Obtenemos el tipo de archivo de la plantilla y el archivo de la plantilla
				String tipoArchivoPlantilla = null;
				File fPlantillaGeneracion = null;
				if(envBean.getIdPlantilla()!=null){
					EnvPlantillaGeneracionAdm admPlantilla = new EnvPlantillaGeneracionAdm(this.usrbean);
					fPlantillaGeneracion = admPlantilla.obtenerPlantilla(""+envBean.getIdInstitucion(), 
							""+envBean.getIdTipoEnvios(), 
							""+envBean.getIdPlantillaEnvios(), 
							""+envBean.getIdPlantilla());

					Hashtable htPkPlantillaGeneracion = new Hashtable();
					htPkPlantillaGeneracion.put(EnvPlantillaGeneracionBean.C_IDINSTITUCION,envBean.getIdInstitucion());
					htPkPlantillaGeneracion.put(EnvPlantillaGeneracionBean.C_IDTIPOENVIOS,envBean.getIdTipoEnvios());
					htPkPlantillaGeneracion.put(EnvPlantillaGeneracionBean.C_IDPLANTILLAENVIOS,envBean.getIdPlantillaEnvios());
					htPkPlantillaGeneracion.put(EnvPlantillaGeneracionBean.C_IDPLANTILLA,envBean.getIdPlantilla());

					Vector vPlant = admPlantilla.selectByPK(htPkPlantillaGeneracion);	    
					EnvPlantillaGeneracionBean plantBean = (EnvPlantillaGeneracionBean)vPlant.firstElement();
					tipoArchivoPlantilla = plantBean.getTipoArchivo();
				}
				//ACUMULAMOS POBLACIONES, PAISES Y PROVINCIAS PARA EVITAR HACER QUERYS A LA BBDD
				Hashtable htPoblaciones = new Hashtable();
				Hashtable htProvincia = new Hashtable();
				Hashtable htPaises = new Hashtable();
				List listaParaZip = new ArrayList();
				File fDocumento = null;
				String nombreFicheroZIP="FicheroEnvio_" +UtilidadesBDAdm.getFechaCompletaBD("").replaceAll("/","").replaceAll(":","").replaceAll(" ","");
			
				
				for (int l=0;l<vDestinatarios.size();l++){
					txtDocumentos = new StringBuffer();
					EnvDestinatariosBean destBean = (EnvDestinatariosBean) vDestinatarios.elementAt(l);
					actualizaPaisDestinatario(destBean, htPaises);
					actualizaPoblacionDestinatario(destBean, htPoblaciones);
					actualizaProvincia(destBean, htProvincia);
					try {

						//GENERAMOS EL PDF
						//////////////////////////////////
						String pathArchivoGenerado = null;
						if (envBean.getIdPlantilla()!=null && fPlantillaGeneracion!=null){
							existePlantilla = true;
							EnvCamposEnviosAdm admCampos = new EnvCamposEnviosAdm(this.usrbean);            
							Vector vCampos = admCampos.obtenerCamposEnvios(destBean.getIdInstitucion().toString(), destBean.getIdEnvio().toString(), "");
							EnvEnviosAdm admEnvio = new EnvEnviosAdm(this.usrbean);
							Hashtable htDatos = admEnvio.getDatosEnvio(destBean, null);
							htDatos = admEnvio.darFormatoCampos(destBean.getIdInstitucion(), destBean.getIdEnvio(), this.usrbean.getLanguage(), htDatos,vCampos);
							pathArchivoGenerado = generarDocumentoEnvioPDFDestinatario(envBean,destBean,fPlantillaGeneracion,tipoArchivoPlantilla,htDatos);

						}
						// CREACION DEL ENVIO
						//////////////////////////////////
						String idPersona = String.valueOf(destBean.getIdPersona());

						// NOMBRE DEL FICHERO
						CenPersonaAdm perAdm = new CenPersonaAdm(this.usrbean);
						Hashtable htPk2 = new Hashtable();
						htPk2.put(CenPersonaBean.C_IDPERSONA,idPersona);
						CenPersonaBean perBean = null;
						String nifcif = "";

						perBean = (CenPersonaBean)perAdm.selectByPK(htPk2).firstElement();
						nifcif = perBean.getNIFCIF().trim();
						nombreDestinatarioXX=perBean.getNombre() + " " + perBean.getApellido1() + " " + perBean.getApellido2();

						String sIdEnvio = envBean.getIdEnvio().toString();
						if (sIdEnvio.length()<8) {
							sIdEnvio = UtilidadesString.formatea(sIdEnvio,8,true);
						}
						String nombre = envBean.getIdInstitucion().toString() + "_" + sIdEnvio + "_" + nifcif + "_";
						int contadorFicheros = 1;

						//DOCUMENTOS ADJUNTOS
						//////////////////////////////////
						/* archivo pdf: [idPersona].pdf */
						if (pathArchivoGenerado!=null){
//							String sGenerado = sDirPdf + File.separator + idPersona + ".pdf";
							String tipoArchivo = pathArchivoGenerado.substring(pathArchivoGenerado.lastIndexOf("."));
							String sCopiado = pathDestino  + nombre + UtilidadesString.formatea(new Integer(contadorFicheros).toString(),4,true) + tipoArchivo;
							// DAVID: NOMBRE DEL FICHERO SI TIENEN UNA PLANTILLA ASOCIADA.
							nombreFicheroXX=nombre + UtilidadesString.formatea(new Integer(contadorFicheros).toString(),4,true) + tipoArchivo;
							contadorFicheros++;							
							File fGenerado = new File(pathArchivoGenerado);
							txtDocumentos.append(fGenerado.getName());
							txtDocumentos.append(",");
							File fCopiado = new File(sCopiado);
							FileHelper.mkdirs(fCopiado.getParentFile().getAbsolutePath());
							if (fGenerado.exists()) {
								copiarFichero(fGenerado,fCopiado);
							}						
							
							String path = envioAdm.getPathEnvio(envBean) + File.separator + "documentosdest" +File.separator +nombreFicheroXX;					
							File fpath= new File(path);
							if(fGenerado==null || !fGenerado.exists()){					    	
									 if(!fpath.exists()){
										 throw new SIGAException("messages.general.error.ficheroNoExiste");
									 }
								 }	
							/**Renombramos el fGenerado con el nombre que tiene el fpath que es el fichero que tiene
							 *  el nombre que le daremos al fichero fGenerado**/
							fGenerado.renameTo(fpath);
							/**Se guarda en el vector de listaParaZip para guardar los diferentes ficheros, para guardarlo despues en un zip**/
							listaParaZip.add(fpath);
						}
						if(l==0){
						/** documentos adjuntos de env�o**/
						EnvDocumentosAdm docAdm = new EnvDocumentosAdm(this.usrbean);
						Hashtable ht = new Hashtable();
						ht.put(EnvDocumentosBean.C_IDINSTITUCION,envBean.getIdInstitucion());
						ht.put(EnvDocumentosBean.C_IDENVIO,envBean.getIdEnvio());
						Vector vDocs = docAdm.select(ht);
						if (vDocs!=null){
							for (int d=0;d<vDocs.size();d++){
								EnvDocumentosBean docBean = (EnvDocumentosBean)vDocs.elementAt(d);
								String tipoArchivo = docBean.getPathDocumento().substring( docBean.getPathDocumento().lastIndexOf("."));
								String idDoc = String.valueOf(docBean.getIdDocumento());
								String Descripcion= (String)docBean.getDescripcion();						
								String Pathdocumento= (String) docBean.getPathDocumento();								
								File fDoc = docAdm.getFile(idInstitucion,idenvio,idDoc);
								String rutaAlm = pathDocumentosAdjuntos + ClsConstants.FILE_SEP;					
								String direccionPlantilla=fDoc.getPath();	
								String direccionPlantilla1=rutaAlm+ Pathdocumento;	 							  	
								File archivo = new 	File(direccionPlantilla);	
								txtDocumentos.append(docBean.getDescripcion());
								txtDocumentos.append(",");
								File f2 = new File(direccionPlantilla1);								
								// RGG 14-07-2005 COPIO CADA DOCUMENTO ADJUNTO A LA CARPETA DE CADA IDPERSONA
								if(archivo==null || !archivo.exists()){					    	
									 if(!f2.exists()){
										 throw new SIGAException("messages.general.error.ficheroNoExiste");
									 }
								 }									   
								/**Renombramos el archivo con el nombre que tiene el f2**/
								archivo.renameTo(f2);
								/**Se guarda en el vector de listaParaZip los datos de los diferentes archivos**/
								listaParaZip.add(f2);							
							
								/** RGG 14-07-2005 COPIO CADA DOCUMENTO ADJUNTO A LA CARPETA DE CADA IDPERSONA**/								
								File fAdjunto = new File(direccionPlantilla);
								String sCopiadoAdjunto = pathDestino + Pathdocumento;
								File fCopiadoAdjunto = new File(sCopiadoAdjunto);
								FileHelper.mkdirs(fCopiadoAdjunto.getParentFile().getAbsolutePath());
								if (!fAdjunto.exists()) {								
									if(f2.exists()){
										/**Renombramos el fichero fAdjunto con el nombre del fichero f2 
										 * que contiene el nombre que le queremos dar al fichero, posteriormente copiamos al ftp.**/
										fAdjunto.renameTo(f2);
										copiarFichero(f2,fCopiadoAdjunto);
									 }else
										  throw new SIGAException("messages.general.error.ficheroNoExiste");
								}
								
							}
						}
					}
						
						// RGG 08/06/2009 ESTADISTICA
						EnvEstatEnvioAdm admEstat = new EnvEstatEnvioAdm(this.usrbean);
						admEstat.insertarApunte(envBean.getIdInstitucion(),envBean.getIdEnvio(),envBean.getIdTipoEnvios(),new Long(idPersona));
					}catch (Exception e){
						errores = true;
						insertarMensajeLogHT(destBean, htErrores, e);
						if(e instanceof SIGAException){
							SIGAException sigaException = (SIGAException)e;
							if(sigaException.getLiteral().equals("envios.definir.literal.errorAlmacenarEnvio"))
								break;

						}
					}

				}// FOR
				
				/**Aqui zippeamos lo que tiene listaParaZip y lo movemos al ftp**/				
				String sCopiadoAdjunto = pathDestino; 
				File ficheroSalida = null;
				String rutaServidorDescargasZip = pathDocumentosAdjuntos;
				rutaServidorDescargasZip += ClsConstants.FILE_SEP+idInstitucion+ClsConstants.FILE_SEP+"temp"+ File.separator;
				FileHelper.mkdirs(rutaServidorDescargasZip);
				File ruta = new File(rutaServidorDescargasZip);					
				if (listaParaZip.size()!=0) {
					ArrayList ficherosPDF= new ArrayList();
					for (int i=0;i<listaParaZip.size();i++){								
						File f = (File) listaParaZip.get(i);							 
					    ficherosPDF.add(f);							
					}
					Plantilla plantilla = new Plantilla(); 
					plantilla.doZip(rutaServidorDescargasZip,nombreFicheroZIP,ficherosPDF, false);				
					ficheroSalida = new File(rutaServidorDescargasZip + nombreFicheroZIP + ".zip");
				 }
				 Date hoy = new Date();
				 SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd_HHmm");
				 String sHoy = sdf2.format(hoy);		
				 nombreFicheroZIP= sHoy;
				 FileHelper.mkdirs(sCopiadoAdjunto);
				 File fCopiadoAdjunto = new File(sCopiadoAdjunto+nombreFicheroZIP+".zip");
				 if (ficheroSalida.exists()) {					
						copiarFichero(ficheroSalida,fCopiadoAdjunto);						
						SIGAServicesHelper.borrarDirectorio(ruta);
				 }				
				
				 // incXXXX // Controlamos que no se generen etiquetas si no hay destinatarios (puede que no sea null, pero este vacio)
				 if(vDestinatarios.size()>0){
					 //Despues de todo el proceso, generamos/imprimimos las etiquetas
					 String sEtiquetas = envBean.getImprimirEtiquetas();
					 if (sEtiquetas.equals(EnvEnviosAdm.GENERAR_ETIQUETAS)){
						 String sRutaEtiquetas = generarEtiquetas(String.valueOf(envBean.getIdInstitucion()),String.valueOf(envBean.getIdEnvio()),pathDestino,vDestinatarios);
					 }else if (sEtiquetas.equals(EnvEnviosAdm.IMPRIMIR_ETIQUETAS)){
					 }
				 }
				 
			}

			if (generarLog) {
				if(existePlantilla)
					this.generarLogEnvioHT(vDestinatarios,null,txtDocumentos.toString(), htErrores, envBean);
				else{
					Vector unicoDestinatario = new Vector();
					if(vDestinatarios!=null && vDestinatarios.size()>0)
						unicoDestinatario.add((EnvDestinatariosBean)vDestinatarios.get(0));
					this.generarLogEnvioHT(unicoDestinatario,null,txtDocumentos.toString(), htErrores, envBean);
				}
			}




			// RGG Esto son errores de destinatario, se resuelven poniendo estado malo
			if (errores){
				return EnvEstadoEnvioAdm.K_ESTADOENVIO_PROCESADOCONERRORES;
			} else {
				return EnvEstadoEnvioAdm.K_ESTADOENVIO_PROCESADO;
			}


		} catch (SIGAException e) {
			ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.enviarCorreoOrdinario(): " + e.getMessage(), e, 1);
			this.generarLogEnvioExceptionHT(envBean, e);
			throw e;
		} catch(Exception e){
			ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.enviarCorreoOrdinario(): " + e.getMessage(), e, 1);
			this.generarLogEnvioExceptionHT(envBean, e);
			throw new ClsExceptions(e,"Error general en env�o ordinario");
		}		
	}


	/** 
	 * Genera un documento PDF con las etiquetas relacionadas con los parametros pasados
	 * @param  idInstitucion - identificador de la institucion	
	 * @param  idEnvio - identificador del envio
	 * @return  String - nombre del fichero generado  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public String generarEtiquetas(String institucion, String envio, String pathDestino, Vector datos) throws ClsExceptions, SIGAException{
    
		File ficFOP=null;
		EnvDestinatariosBean datosEtiqueta= null;
		String nombrePDF="";
			
		try {
			
//			EnvTempDestinatariosAdm admEnvfiosTemp =  new EnvTempDestinatariosAdm(this.usrbean);
			//Vector datos=admEnviosTemp.getDatosEtiquetas(institucion, envio);

			if (!datos.isEmpty()){
				boolean correcto=true;
				
				// Recojo los parametros pertinentes
				GenParametrosAdm paramAdm = new GenParametrosAdm(this.usrbean);
				String rutaPlantilla=paramAdm.getValor(institucion,"CER","PATH_PLANTILLAS","");
				String rutaEtiquetas=paramAdm.getValor(institucion,"ENV","PATH_DOCUMENTOSADJUNTOS","");
				String margenX=paramAdm.getValor(institucion,"ENV","ETIQUETAS_MARGEN_COORDENADAX","");
				String margenY=paramAdm.getValor(institucion,"ENV","ETIQUETAS_MARGEN_COORDENADAY","");
				String interlineadoColumna=paramAdm.getValor(institucion,"ENV","ETIQUETAS_DISTANCIA_COLUMNA","");
				String interlineadoFila=paramAdm.getValor(institucion,"ENV","ETIQUETAS_DISTANCIA_FILA","");
				
				String etiquetaAncho = paramAdm.getValor(institucion,"ENV","ETIQUETAS_ANCHO","");
				String etiquetaAlto  = paramAdm.getValor(institucion,"ENV","ETIQUETAS_ALTO","");

				// Gestion de nombres de ficheros (plantilla)
	    		String barra = "";
	    		String nombreFichero = ClsConstants.NOMBRE_PLANTILLA_ETIQUETA;
	    		if (rutaPlantilla.indexOf("/") > -1){ 
	    			barra = "/";
	    		}
	    		if (rutaPlantilla.indexOf("\\") > -1){ 
	    			barra = "\\";
	    		}   	    		
	     		rutaPlantilla += barra+institucion+barra+nombreFichero;
				File rutaFOP=new File(rutaPlantilla);
				if (!rutaFOP.exists()){
					//throw new SIGAException("facturacion.nuevoFichero.literal.errorAcceso");					
					throw new SIGAException("messages.envios.error.noPlantilla");
				} else
			    if (!rutaFOP.canRead()){
		            throw new ClsExceptions("Error de acceso a la plantilla generando etiquetas");
			    }
				// Gestion de nombres de ficheros (PDF generado)
	    		if (rutaEtiquetas.indexOf("/") > -1){ 
	    			barra = "/";
	    		}
	    		if (rutaEtiquetas.indexOf("\\") > -1){ 
	    			barra = "\\";
	    		}
	    		
	    		// Sustituye a lo anterior comentado para coordinar ruta con Juanmi 
				rutaEtiquetas=getPathEnvio(institucion,envio)+barra;
				FileHelper.mkdirs(rutaEtiquetas);
				ficFOP = new File(rutaEtiquetas+"temp_"+envio+".fo");
				if (ficFOP.exists()){
					if(!ficFOP.delete()){
			            throw new ClsExceptions("Error de acceso a la plantilla generando etiquetas fo");
					}
				}
				
				Plantilla plantilla = new Plantilla(this.usrbean);
				
				// Parametros p�ra ubicaci�n etiquetas
				int fila=0;
				int columna=0;
				int topeColumnas=etiquetasFilaDinA4(new Float(etiquetaAncho).floatValue(),new Float(margenX).floatValue(),new Float(interlineadoColumna).floatValue());
				int topeFilas=etiquetasColumnaDinA4(new Float(etiquetaAlto).floatValue(),new Float(margenY).floatValue(),new Float(interlineadoFila).floatValue());

	    		// Insertamos cabecera del documento
	    		correcto=plantilla.insercionCabeceraSimpleEtiquetasFOP(ficFOP,"etiquetas",margenX,margenX,margenY,margenY);
				
				Enumeration listaEnvios=datos.elements();
				while ((correcto)&&(listaEnvios.hasMoreElements())){
					
					// Obtenemos los datos
					datosEtiqueta = (EnvDestinatariosBean)listaEnvios.nextElement();
					
		   	    	// Incorporaci�n etiqueta
					correcto=plantilla.insercionCabeceraBloqueFOP(ficFOP, new Float (etiquetaAlto).floatValue(), new Float(etiquetaAncho).floatValue(), 
							                                              new Integer (fila).intValue(), new Integer(columna).intValue(), 
							                                              new Float (interlineadoFila).floatValue(), new Float (interlineadoColumna).floatValue());
					correcto=plantilla.obtencionEtiqueta(ficFOP,rutaPlantilla, datosEtiqueta, institucion, envio,this.usrbean);
					correcto=plantilla.insercionPieBloqueFOP(ficFOP);

					// Gestion medidas
					columna++;
					if (columna>=topeColumnas){
						columna=0;
						fila++;
					}
					if (fila>=topeFilas){
						fila=0;
			   	    	// Incorporacion pagina nueva
						correcto=plantilla.insercionSimplePaginaNuevaDocumentoFOP(ficFOP);
					}
				}
				
				// Insertamos fin documento
	    		correcto=plantilla.insercionPieSimpleDocumentoFOP(ficFOP);
				
				// Obtencion fichero PDF
				if (correcto){
					
					nombrePDF=rutaEtiquetas+institucion+"_"+UtilidadesString.formatea(envio,8,true) +"_"+"ETIQ.pdf";
					File ficPDF=new File(nombrePDF);
					MasterReport masterReport = new MasterReport();
					masterReport.convertFO2PDF(ficFOP, ficPDF);
					ficFOP.delete();
					
					// AQUI COPIA EL FICHERO GENERADO DE ETIQUETAS (ficPDF)
					FileHelper.mkdirs(pathDestino);
					File destino = new File(pathDestino + File.separator + ficPDF.getName());
					copiarFichero(ficPDF,destino);
					
					
				}
				else{
					ficFOP.delete();					
				}
				
			}
			else{
				
			}
				
		} catch (SIGAException e) {
			ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.generarEtiquetas(): " + e.getMessage(), e, 1);
			throw e;
	    } catch(Exception e){
	    	ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.generarEtiquetas(): " + e.getMessage(), e, 1);
            throw new ClsExceptions(e,"Error generando etiquetas");
		}
	    return nombrePDF;
	}
	
	/** 
	 * Devuelve el path de un documento PDF generado por el metodo generarEtiquetas() <br>
	 * atendiendo unica y exclusivamente al caso de que el formato del documento generado <br>
	 * sea del estilo "institucion_envio_ETIQ.pdf"
	 * @param  idInstitucion - identificador de la institucion	
	 * @param  idEnvio - identificador del envio
	 * @return  String - nombre del fichero generado  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public String getPathEtiquetas(String institucion, String envio) throws ClsExceptions{
    
		String nombrePDF="";
			
		try {
			//Relleno con 0 el nombre del envio:
			String nombreEnvio = UtilidadesString.formatea(envio,8,true);
			
			//Calculo la ruta al fichero del envio:
			nombrePDF = this.getPathEnvio(institucion, envio)+File.separator+institucion+"_"+nombreEnvio+"_"+"ETIQ.pdf";
				
	    } catch(Exception e){
	    	ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.getPathEtiquetas(): " + e.getMessage(), e, 1);
	    	throw new ClsExceptions (e, "Error al generar el path de las etiquetas");
		}
	    return nombrePDF;
	}

	/** 
	 * Obtiene el numero de etiquetas posibles por fila en una DinA4
	 * @param  anchuraEtiqueta - anchura de la etiqueta	
	 * @param  margen - margen izdo/dcho simetrico a ambos lados de la pagina
	 * @param  distanciaEtiquetas - distancia minima entre etiquetas
	 * @return  int - numero de etiquetas posibles  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	private int etiquetasFilaDinA4(float anchuraEtiqueta, float margen, float distanciaEtiquetas) throws ClsExceptions{
    
		int resultado=0;	
		try {
			if ((margen>=0.0) && (anchuraEtiqueta>=0.0) && (distanciaEtiquetas>=0.0)){
				while(((2*margen)+(resultado*(anchuraEtiqueta))+((resultado-1)*(distanciaEtiquetas)))<=21.0){
					resultado++;
				}
				if (resultado>0){
					resultado=resultado-1;
				}
				else{
					resultado=0;
				}
			}				
		} catch(Exception e){
			ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.etiquetasFilaDinA4(): " + e.getMessage(), e, 1);
			throw new ClsExceptions (e, "Error al obtener numero de etiquetas por fila");
		}
		return resultado;
	}
	
	/** 
	 * Obtiene el numero de etiquetas posibles por columna en una DinA4
	 * @param  alturaEtiqueta - anchura de la etiqueta	
	 * @param  margen - margen cabecera/pie simetrico
	 * @param  distanciaEtiquetas - distancia minima entre etiquetas
	 * @return  int - numero de etiquetas posibles  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	private int etiquetasColumnaDinA4(float altoEtiqueta, float margenSuperior, float distanciaEtiquetas) throws ClsExceptions{
    
		int filas = 0;	
		try {
			if ((margenSuperior >= 0.0) && (altoEtiqueta >= 0.0) && (distanciaEtiquetas >= 0.0)){

				
				// Antes:
//				while(((2*margen)+(resultado*(anchuraEtiqueta))+((resultado-1)*(distanciaEtiquetas)))<=29.7){
//					resultado++;
//				}
//				if (resultado>0){
//					resultado=resultado-1;
//				}
//				else{
//					resultado=0;
//				}
				
				// Ahora
				float tamPagina = (float) 29.7;	// Esto no se debe poner aqui pero ...
				while (tamPagina - margenSuperior >= filas * altoEtiqueta + (filas - 1) * distanciaEtiquetas ) {
					filas ++;	
				}
				filas--;
			}
		} 
		catch(Exception e){
			ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.etiquetasColumnaDinA4(): " + e.getMessage(), e, 1);
			throw new ClsExceptions (e, "Error al obtener numero de etiquetas por columna");
		}
		return filas;
	}
	
	public Vector getEnviosPendientes(String idInst)
		throws SIGAException,ClsExceptions{
	    
	    Vector enviosBeans = new Vector();
	    
	    //NOMBRE TABLA
		String T_ENV_ENVIOS = EnvEnviosBean.T_NOMBRETABLA;
		
		//Tabla cen_Direcciones
		String C_IDINSTITUCION = EnvEnviosBean.C_IDINSTITUCION;
		String C_IDESTADO = EnvEnviosBean.C_IDESTADO;
		String C_FECHAPROGRAMADA = EnvEnviosBean.C_FECHAPROGRAMADA;
		
		//Acceso a BBDD
		try {

			String sql = " WHERE ";
		    sql += C_IDINSTITUCION + " = " + idInst;
			sql += " AND " + C_IDESTADO + " = " + EnvEstadoEnvioAdm.K_ESTADOENVIO_PENDIENTE_AUTOMATICO;
			sql += " AND " + C_FECHAPROGRAMADA + " <= SYSDATE";
			
			sql += " AND envio is null ";
		    //sql += " AND " + EnvEnviosBean.C_IDENVIO + " = 3732";
					
			enviosBeans = this.select(sql);
			return enviosBeans;
			
        } catch (ClsExceptions e) {
        	ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.getEnviosPendientes(): " + e.getMessage(), e, 1);
			throw new ClsExceptions (e, "Error al obtener los env�os pendientes");
//	        throw new SIGAException("messages.general.error",e);
	    }
	}
	

	public String enviarFax(EnvEnviosBean envBean, Vector vDestinatarios, Hashtable htErrores, boolean generarLog) 
	throws SIGAException,ClsExceptions{
    
	    boolean errores = false;
	    String nombreFicheroXX="";
	    String nombreDestinatarioXX="";
		Vector ficherosFax = new Vector();		
		StringBuffer documentos = new StringBuffer();
		try{
	        
		    if (!envBean.getIdTipoEnvios().equals(Integer.valueOf(EnvTipoEnviosAdm.K_FAX))){
				throw new ClsExceptions ("Tipo de env�o por fax incorrecto");
		    }
	
	        SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);
	        Calendar cal = Calendar.getInstance();
			Date dat;
	        try {
	            dat = sdf.parse(envBean.getFechaCreacion());
	            cal.setTime(dat);
	        } catch (ParseException e1) {
	        	ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.enviarFax(): " + e1.getMessage(), e1, 1);
	            throw e1;
	        }
	        String anio = String.valueOf(cal.get(Calendar.YEAR));
	        String mes = String.valueOf(cal.get(Calendar.MONTH)+1);
	        String dia = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
	
	        // Obtengo el pathFTP y pathFicherosZETAFAX:
	        String pathFTP="", pathFicherosZETAFAX="";
	        GenParametrosAdm paramAdm = new GenParametrosAdm(this.usrbean);
	        try {
	        	pathFTP = paramAdm.getValor(envBean.getIdInstitucion().toString(),"ENV","PATH_DESCARGA_ENVIOS_FAX","");
	        	pathFicherosZETAFAX = paramAdm.getValor(envBean.getIdInstitucion().toString(),"ENV","PATH_DESCARGA_ENVIOS_FAX_SERVIDOR","");
	        } catch (Exception e) {
	        	ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.enviarFax(): " + e.getMessage(), e, 1);
	        	pathFTP="";
	        	pathFicherosZETAFAX="";
	        	throw e;
	        }
	
	        Date hoy = new Date();
	        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd_HHmm");
	        String sHoy = sdf2.format(hoy);
	        
	        String pathDestino = pathFTP + File.separator + envBean.getIdInstitucion().toString() + File.separator + anio + File.separator + mes + File.separator + dia + File.separator + envBean.getIdEnvio().toString() + File.separator + sHoy + File.separator;  
	        String pathDestinoZETAFAX = pathFicherosZETAFAX + File.separator + envBean.getIdInstitucion().toString() + File.separator + anio + File.separator + mes + File.separator + dia + File.separator + envBean.getIdEnvio().toString() + File.separator + sHoy;
	    
	        
		    
        	
	        // BUCLE DE DESTINATARIOS
	        ///////////////////////////
	        if (vDestinatarios!=null){
	        	
	        	// PLANTILLAS SE GENERACION
		        //Obtenemos el tipo de archivo de la plantilla y el archivo de la plantilla
	         	String tipoArchivoPlantilla = null;
	        	File fPlantilla = null;
	        	if(envBean.getIdPlantilla()!=null){
	        		EnvPlantillaGeneracionAdm admPlantilla = new EnvPlantillaGeneracionAdm(this.usrbean);
	                fPlantilla = admPlantilla.obtenerPlantilla(""+envBean.getIdInstitucion(), 
	                        										""+envBean.getIdTipoEnvios(), 
	                        										""+envBean.getIdPlantillaEnvios(), 
	                        										""+envBean.getIdPlantilla());
	                
	        		
		        	Hashtable htPkPlantillaGeneracion = new Hashtable();
		            htPkPlantillaGeneracion.put(EnvPlantillaGeneracionBean.C_IDINSTITUCION,envBean.getIdInstitucion());
		            htPkPlantillaGeneracion.put(EnvPlantillaGeneracionBean.C_IDTIPOENVIOS,envBean.getIdTipoEnvios());
		            htPkPlantillaGeneracion.put(EnvPlantillaGeneracionBean.C_IDPLANTILLAENVIOS,envBean.getIdPlantillaEnvios());
		            htPkPlantillaGeneracion.put(EnvPlantillaGeneracionBean.C_IDPLANTILLA,envBean.getIdPlantilla());
		        	
		            
		            Vector vPlant = admPlantilla.selectByPK(htPkPlantillaGeneracion);	    
		    	    EnvPlantillaGeneracionBean plantBean = (EnvPlantillaGeneracionBean)vPlant.firstElement();
		    	    tipoArchivoPlantilla = plantBean.getTipoArchivo();
	        	}
	        	
	        	
	        	//ACUMULAMOS POBLACIONES, PAISES Y PROVINCIAS PARA EVITAR HACER QUERYS A LA BBDD
	    	    Hashtable htPoblaciones = new Hashtable();
	    	    Hashtable htProvincia = new Hashtable();
	    	    Hashtable htPaises = new Hashtable();
	        for (int l=0;l<vDestinatarios.size();l++){
	        	documentos = new StringBuffer();
	        	EnvDestinatariosBean destBean = (EnvDestinatariosBean) vDestinatarios.elementAt(l);
	        	actualizaPaisDestinatario(destBean, htPaises);
		        actualizaPoblacionDestinatario(destBean, htPoblaciones);
		        actualizaProvincia(destBean, htProvincia);
	            try{
	        	
			        // GENERAMOS EL PDF DEL ENVIO
			        ////////////////////////////
			        String pathArchivoGenerado=null;
			        String sDirPdf = null;
			        if (envBean.getIdPlantilla()!=null && fPlantilla!=null){ 
			        	//aunque no exista plantilla para envio continuamos para enviar documento adjunto
			        	EnvCamposEnviosAdm admCampos = new EnvCamposEnviosAdm(this.usrbean);            
			            Vector vCampos = admCampos.obtenerCamposEnvios(destBean.getIdInstitucion().toString(), destBean.getIdEnvio().toString(), "");
			            EnvEnviosAdm admEnvio = new EnvEnviosAdm(this.usrbean);
			            Hashtable htDatos = admEnvio.getDatosEnvio(destBean, null);
			            
						htDatos = admEnvio.darFormatoCampos(destBean.getIdInstitucion(), destBean.getIdEnvio(), this.usrbean.getLanguage(), htDatos,vCampos);
			        	pathArchivoGenerado = generarDocumentoEnvioPDFDestinatario(envBean,destBean,fPlantilla,tipoArchivoPlantilla,htDatos);
		            	sDirPdf = getPathEnvio(envBean)+  File.separator + "documentosdest";
				    }
		        
		    	    ficherosFax = new Vector(); 
		            String fax;

		            if (destBean.getFax1()!=null && !destBean.getFax1().equals("")){
		                fax = destBean.getFax1();
		            } else if (destBean.getFax2()!=null && !destBean.getFax2().equals("")){
		                fax = destBean.getFax2();
		            } else {
		            	throw new SIGAException("messages.envios.error.noNumeroFax");
		            }
		            String idPersona = String.valueOf(destBean.getIdPersona());
		    	    	    		
		            // NOMBRE DEL FICHERO
			    	CenPersonaAdm perAdm = new CenPersonaAdm(this.usrbean);
				    Hashtable htPk2 = new Hashtable();
				    htPk2.put(CenPersonaBean.C_IDPERSONA,idPersona);
				    CenPersonaBean perBean = null;
				    String nifcif = "";
				    try {
				        perBean = (CenPersonaBean)perAdm.selectByPK(htPk2).firstElement();
				        nifcif = perBean.getNIFCIF().trim();
		    	        nombreDestinatarioXX=perBean.getNombre() + " " + perBean.getApellido1() + " " + perBean.getApellido2();
				    } catch (ClsExceptions e) {
				    	ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.enviarFax(): " + e.getMessage(), e, 1);
				        throw new SIGAException("messages.envios.error.noDatosPersona",e);
				    }
			        String sIdEnvio = envBean.getIdEnvio().toString();
			        if (sIdEnvio.length()<8) {
			        	sIdEnvio = UtilidadesString.formatea(sIdEnvio,8,true);
			        }
			        String nombre = envBean.getIdInstitucion().toString() + "_" + sIdEnvio + "_" + nifcif + "_";
			        int contadorFicheros = 1;
		            
		            
		    		//DOCUMENTOS ADJUNTOS
		    	    
		    	    /* archivo pdf: [idPersona].pdf */
		    	    if (pathArchivoGenerado!=null){
//		    	        String sGenerado = sDirPdf + File.separator + idPersona + ".pdf";
		    	    	String tipoArchivo = pathArchivoGenerado.substring(pathArchivoGenerado.lastIndexOf("."));
		    	        File fGenerado = new File(pathArchivoGenerado);
		    	        documentos.append(fGenerado.getName());
		    	        documentos.append(",");
		    	        String sCopiadoGenerado = pathDestino + File.separator + nombre + UtilidadesString.formatea(new Integer(contadorFicheros).toString(),4,true) + tipoArchivo;
		    	        nombreFicheroXX = pathDestinoZETAFAX + File.separator + nombre + UtilidadesString.formatea(new Integer(contadorFicheros).toString(),4,true) + tipoArchivo;
		    	        contadorFicheros++;
	
		    	        File fCopiadoGenerado = new File(sCopiadoGenerado);
		    	        FileHelper.mkdirs(fCopiadoGenerado.getParentFile().getAbsolutePath());
		    	        if (fGenerado.exists()) {
		    	        	copiarFichero(fGenerado,fCopiadoGenerado);
		    	        }
	
		    	        //ZETAFAX: Anhado el fichero al vector de ficheros para el ZetaFax:
		    	        ficherosFax.add(nombreFicheroXX);
		    	    }
		    	    
		    	    /* documentos adjuntos de env�o*/
		    	    EnvDocumentosAdm docAdm = new EnvDocumentosAdm(this.usrbean);
		    	    Hashtable htPk = new Hashtable();
		    	    htPk.put(EnvEnviosBean.C_IDINSTITUCION,envBean.getIdInstitucion());
		    	    htPk.put(EnvEnviosBean.C_IDENVIO,envBean.getIdEnvio());
		    	    Vector vDocs = docAdm.select(htPk);
		    	    if (vDocs!=null)
		    	    for (int d=0;d<vDocs.size();d++){
		    	        EnvDocumentosBean docBean = (EnvDocumentosBean)vDocs.elementAt(d);
		    	        
		    	        String tipoArchivo = docBean.getPathDocumento().substring(docBean.getPathDocumento().lastIndexOf("."));
		    	        String idDoc = String.valueOf(docBean.getIdDocumento());
		    	        File fDoc = docAdm.getFile(envBean,idDoc);
		    	        documentos.append(docBean.getDescripcion());
		    	        documentos.append(",");
		    	        String sAdjunto = fDoc.getPath();
		    	        File fAdjunto = new File(sAdjunto);
		    	        String sCopiadoAdjunto = pathDestino + File.separator + nombre + UtilidadesString.formatea(new Integer(contadorFicheros).toString(),4,true) + tipoArchivo;
		    	        nombreFicheroXX = pathDestinoZETAFAX + File.separator + nombre + UtilidadesString.formatea(new Integer(contadorFicheros).toString(),4,true) + tipoArchivo;
		    	        contadorFicheros++;
	
		    	        File fCopiadoAdjunto = new File(sCopiadoAdjunto);
		    	        FileHelper.mkdirs(fCopiadoAdjunto.getParentFile().getAbsolutePath());
		    	        if (fAdjunto.exists()) {
		    	        	copiarFichero(fAdjunto,fCopiadoAdjunto);
		    	        }
	
		    	        //ZETAFAX: Anhado el fichero al vector de ficheros para el ZetaFax:
		    	        ficherosFax.add(nombreFicheroXX);
		       	    }
		    	    
		    	    /* documentos adjuntos de destinatario*/
		    	    // RGG Atencion, estos documentos no se usan, queda comentado el codigo.
	    	    
		    	    // Generamos un fax por destinatario con todos sus ficheros adjuntos:
		    	    // NOTA: Si se ha producido una excepcion lanza una ClsException.
		    	    //

		    	    this.generarFax(destBean, envBean, fax, nombre, ficherosFax);
		    		
		    	    // RGG 08/06/2009 ESTADISTICA
		    	    EnvEstatEnvioAdm admEstat = new EnvEstatEnvioAdm(this.usrbean);
		    	    admEstat.insertarApunte(envBean.getIdInstitucion(),envBean.getIdEnvio(),envBean.getIdTipoEnvios(),new Long(idPersona));

		    	    
	            } catch (Exception e){
	            	ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.enviarFax(): " + e.getMessage(), e, 1);
	                errores = true;
	                insertarMensajeLogHT(destBean, htErrores, e);
	            }
	            
	    	} // FOR
		}
	        
	        // GENERAR EL LOG SI PROCEDE
	        /////////////////////////////
	        if (generarLog) {
	        	generarLogEnvioHT(vDestinatarios,null,documentos.toString(), htErrores, envBean);
	        }
	
	        // RGG Esto son errores de destinatario, se resuelven poniendo estado malo
	        if (errores){
	        	return EnvEstadoEnvioAdm.K_ESTADOENVIO_PROCESADOCONERRORES;
	        } else {
	            return EnvEstadoEnvioAdm.K_ESTADOENVIO_PROCESADO;
	        }
	        
		} catch (SIGAException e) {
			ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.enviarFax(): " + e.getMessage(), e, 1);
	        this.generarLogEnvioExceptionHT(envBean,e);
			throw e;
	    } catch(Exception e){
	    	ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.enviarFax(): " + e.getMessage(), e, 1);
	    	this.generarLogEnvioExceptionHT(envBean,e);
			throw new ClsExceptions (e, "Error general enviando fax");
		}		
	}
	
	
	private void generarFax(EnvDestinatariosBean destBean, EnvEnviosBean envBean, String fax, String nombre, Vector ficherosFax) throws SIGAException, ClsExceptions{
		
		try {
			//Usuarios Destinatarios:
		    UsuarioFax usuario = new UsuarioFax(((destBean.getNombre()!=null)?destBean.getNombre():"")+" "+((destBean.getApellidos1()!=null)?destBean.getApellidos1():"")+" "+((destBean.getApellidos2()!=null)?destBean.getApellidos2():""), fax);
		    Vector destinatariosFax = new Vector();
		    destinatariosFax.add(usuario);
		    
		    //Datos de los Remitentes:
		    EnvRemitentesAdm remitentesAdm = new EnvRemitentesAdm(this.usrbean);
		    CenInstitucionAdm instiAdm = new CenInstitucionAdm(this.usrbean);
		    Vector vRemitentes = remitentesAdm.getRemitentes(envBean.getIdInstitucion().toString(),envBean.getIdEnvio().toString());
		    String nombreRemitentes = remitentesAdm.getNombreRemitentes(vRemitentes); 		    	    		    	    	
		    String nombreInstitucion = instiAdm.getNombreInstitucion(envBean.getIdInstitucion().toString()); 		    	    		    	    	
	
		    ZetaFax zetaFax = null;		    	    
		    //Por todos los remitentes envio un fax:
	   		//Parametros del Fax:
			zetaFax = new ZetaFax(nombreRemitentes, destinatariosFax, ficherosFax);
			zetaFax.setPrioridad(ZetaFax.PRIORIDAD_NORMAL);
			zetaFax.setAsunto(envBean.getDescripcion());	
			zetaFax.setOrganisation(nombreInstitucion);	
			
			// Comentada la parte que hace que el fax no se envie hasta que el usuario lo confirme con ZetaFax Cliente:		
			//zetaFax.setHold(ZetaFax.YES); //El mensaje no se enviara hasta que el cliente lo confirme
			//zetaFax.setPreview(ZetaFax.YES); //Prepara la vista Previa del Fax. Para usarlo debemos poner a YES el parametro HOLD.		
			
			//Creo el fax con el nombre del fichero temporal para el ZSUBMIT sin la  extension:			    		
			zetaFax.crearDocumentoFax(nombre,this.usrbean);

		} catch (ClsExceptions e2) {
			ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.generarFax(): " + e2.getMessage(), e2, 1);
			throw new SIGAException(e2.getMessage(),e2);
		} catch (Exception e) {
			ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.generarFax(): " + e.getMessage(), e, 1);
			throw new ClsExceptions (e, "Error general generando el fax a enviar");
		}
	}
	
	private void copiarFichero(File origen, File destino) {
		
		// copio el fichero al destino
		BufferedInputStream bufferLectura = null;
		BufferedOutputStream writer = null;
		try {
		
			writer = new BufferedOutputStream(new FileOutputStream(destino, false));

			if (!origen.exists())
				throw new Exception("El fichero origen no existe: " + origen.getAbsolutePath());
		    else {
			    if (!origen.canRead())
					throw new Exception("Error de lectura: " + origen.getAbsolutePath());
			    else {
		    		bufferLectura = new BufferedInputStream(new FileInputStream(origen));												
					// Lectura de la plantilla linea a linea
		    		byte buf[] = new byte[1000];
					int ret=0;
		    		while ((ret=bufferLectura.read(buf))!=-1){
						writer.write(buf,0,ret);
			    	}
			    	bufferLectura.close();
			    }
		    }
			writer.close();
		} 
		catch(IOException _ex) {
			try {
				bufferLectura.close();
			} catch (Exception e) {}
			try {
				writer.close();
			} catch (Exception e) {}
			// continuamos
			
		} catch(Exception _ex) {
			try {
				bufferLectura.close();
			} catch (Exception e) {}
			try {
				writer.close();
			} catch (Exception e) {}
			// continuamos
		}
					
	}

	public String enviarCorreoElectronico(EnvEnviosBean envBean, Vector vDestinatarios, Hashtable htErrores, boolean generarLog)
			throws SIGAException, ClsExceptions {

		boolean errores = false;
		Transport tr = null;

		try {
			EnvEnviosAdm envAdm = new EnvEnviosAdm(this.usrbean);
			EnvDestinatariosAdm envDestinatariosAdm = new EnvDestinatariosAdm(this.usrbean);

			// COMPROBACION
			// ///////////////////////////////////
			if (!envBean.getIdTipoEnvios().equals(Integer.valueOf(EnvTipoEnviosAdm.K_CORREO_ELECTRONICO))
					&& !envBean.getIdTipoEnvios().equals(Integer.valueOf(EnvTipoEnviosAdm.K_DOCUMENTACIONLETRADO))) {
				throw new ClsExceptions("Tipo de envio electr�nico incorrecto");
			}

			// OBTENCI�N DE SERVIDOR DE CORREO
			// ///////////////////////////////////
			Context ctx = new InitialContext();
			ReadProperties rp = new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
			String smtpSesion = rp.returnProperty("mail.smtp.sesion");
			if (smtpSesion == null || smtpSesion.equals(""))
				smtpSesion = "CorreoSIGA";
			Session sesion = (Session) javax.rmi.PortableRemoteObject.narrow(ctx.lookup(smtpSesion), Session.class);
			ctx.close();

			// RGG autenticar SMTP
			sesion.getProperties().put("mail.smtp.auth", "true");
			sesion.getProperties().put("mail.smtp.port", rp.returnProperty("mail.smtp.port"));

			// OBTENCION DE REMITENTE
			// ///////////////////////////////////
			EnvRemitentesAdm remAdm = new EnvRemitentesAdm(this.usrbean);
			Hashtable htPk = new Hashtable();
			htPk.put(EnvEnviosBean.C_IDINSTITUCION, envBean.getIdInstitucion());
			htPk.put(EnvEnviosBean.C_IDENVIO, envBean.getIdEnvio());
			Vector vRem = remAdm.select(htPk);
			String sFrom = "";
			EnvRemitentesBean remBean = null;
			StringBuffer txtDocumentos = new StringBuffer("");
			if (vRem.size() > 0) {
				// obtengo la primera de la lista
				remBean = (EnvRemitentesBean) vRem.firstElement();
				sFrom = remBean.getCorreoElectronico();
			} else {
				// obtengo la de la institucion
				Row dirPref = getDireccionPreferenteInstitucion(envBean.getIdInstitucion(), EnvTipoEnviosAdm.K_CORREO_ELECTRONICO);
				sFrom = dirPref.getString(EnvRemitentesBean.C_CORREOELECTRONICO);
			}

			// PLANTILLA DE GENERACION
			// ///////////////////////////////////
			// Obtenemos el archivo con la plantilla

			// BUCLE DE DESTINATARIOS
			// ///////////////////////////////////

			if (vDestinatarios != null) {

				// Obtenemos el tipo de archivo de la plantilla y el archivo de la plantilla
				String tipoArchivoPlantilla = null;
				File fPlantilla = null;

				if (envBean.getIdPlantilla() != null) {
					EnvPlantillaGeneracionAdm admPlantilla = new EnvPlantillaGeneracionAdm(this.usrbean);
					fPlantilla = admPlantilla.obtenerPlantilla("" + envBean.getIdInstitucion(), "" + envBean.getIdTipoEnvios(), ""
							+ envBean.getIdPlantillaEnvios(), "" + envBean.getIdPlantilla());

					Hashtable htPkPlantillaGeneracion = new Hashtable();
					htPkPlantillaGeneracion.put(EnvPlantillaGeneracionBean.C_IDINSTITUCION, envBean.getIdInstitucion());
					htPkPlantillaGeneracion.put(EnvPlantillaGeneracionBean.C_IDTIPOENVIOS, envBean.getIdTipoEnvios());
					htPkPlantillaGeneracion.put(EnvPlantillaGeneracionBean.C_IDPLANTILLAENVIOS, envBean.getIdPlantillaEnvios());
					htPkPlantillaGeneracion.put(EnvPlantillaGeneracionBean.C_IDPLANTILLA, envBean.getIdPlantilla());

					Vector vPlant = admPlantilla.selectByPK(htPkPlantillaGeneracion);
					EnvPlantillaGeneracionBean plantBean = (EnvPlantillaGeneracionBean) vPlant.firstElement();
					tipoArchivoPlantilla = plantBean.getTipoArchivo();

				}
				EnvImagenPlantillaBean beanImagenes = new EnvImagenPlantillaBean();
				beanImagenes.setIdInstitucion(envBean.getIdInstitucion());
				beanImagenes.setIdTipoEnvios(envBean.getIdTipoEnvios());
				beanImagenes.setIdPlantillaEnvios(envBean.getIdPlantillaEnvios());
				EnvImagenPlantillaAdm admImagenPlantilla = new EnvImagenPlantillaAdm(this.usrbean);
				List<ImagenPlantillaForm> lImagenes = admImagenPlantilla.getImagenes(beanImagenes);

				// ACUMULAMOS POBLACIONES, PAISES Y PROVINCIAS PARA EVITAR HACER QUERYS A LA BBDD
				Hashtable htPoblaciones = new Hashtable();
				Hashtable htProvincia = new Hashtable();
				Hashtable htPaises = new Hashtable();
				String descripcionFrom = sFrom;
				if (remBean != null && remBean.getDescripcion() != null && !remBean.getDescripcion().trim().equals(""))
					descripcionFrom = remBean.getDescripcion().trim();
				else if (remBean.getIdPersona() != null) {
					CenPersonaAdm personaAdm = new CenPersonaAdm(usrbean);
					descripcionFrom = personaAdm.obtenerNombreApellidosJSP(remBean.getIdPersona().toString());
					remBean.setDescripcion(descripcionFrom);
				}
				EnvCamposEnviosAdm admCampos = new EnvCamposEnviosAdm(this.usrbean);
				Vector vCampos = admCampos.obtenerCamposEnvios(envBean.getIdInstitucion().toString(), envBean.getIdEnvio().toString(), "");

				boolean isErrorEnvioIndividual = false;
				for (int l = 0; l < vDestinatarios.size(); l++) {
					isErrorEnvioIndividual = false;
					if (tr == null) {
						tr = sesion.getTransport("smtp");
						tr.connect(rp.returnProperty("mail.smtp.host"), rp.returnProperty("mail.smtp.user"),
								rp.returnProperty("mail.smtp.pwd"));
					} else if (!tr.isConnected()) {
						tr.connect(rp.returnProperty("mail.smtp.host"), rp.returnProperty("mail.smtp.user"),
								rp.returnProperty("mail.smtp.pwd"));
					}

					txtDocumentos = new StringBuffer();
					EnvDestinatariosBean destBean = (EnvDestinatariosBean) vDestinatarios.elementAt(l);
					// if(destBean.getCorreoElectronico()==null || destBean.getCorreoElectronico().trim().equalsIgnoreCase(""))
					// System.out.println("Bingo");
					actualizaPaisDestinatario(destBean, htPaises);
					actualizaPoblacionDestinatario(destBean, htPoblaciones);
					actualizaProvincia(destBean, htProvincia);

					String pathArchivoGenerado = null;
					String sDirPdf = null;

					// ENVIO PARA CADA DESTINATARIO
					// ///////////////////////////////////
					Hashtable htDatos = null;
					try {

						// GENERACION DEL PDF DEL ENVIO SI PROCEDE
						// ///////////////////////////////////

						if (envBean.getIdPlantilla() != null && fPlantilla != null) {
							// Si no tiene plantilla no enviamos documento,
							// pero continuamos para mandar el correo

							EnvEnviosAdm admEnvio = new EnvEnviosAdm(this.usrbean);
							htDatos = admEnvio.getDatosEnvio(destBean, null);

							htDatos = admEnvio.darFormatoCampos(destBean.getIdInstitucion(), destBean.getIdEnvio(),
									this.usrbean.getLanguage(), htDatos, vCampos);
							pathArchivoGenerado = generarDocumentoEnvioPDFDestinatario(envBean, destBean, fPlantilla, tipoArchivoPlantilla,
									htDatos);

							// Ruta donde guardamos los pdf
							sDirPdf = getPathEnvio(envBean) + File.separator + "documentosdest";
						}

						String sTo = destBean.getCorreoElectronico();
						if (sTo == null || sTo.trim().equals(""))
							throw new SMTPAddressFailedException(new InternetAddress(sFrom), null, 0, UtilidadesString.getMensajeIdioma(
									usrbean, "messages.envios.errorSinEmail"));

						// Se crea un nuevo Mensaje.
						MimeMessage mensaje = new MimeMessage(sesion);

						// Se especifica la direccion de origen.
						GenParametrosAdm paramAdm = new GenParametrosAdm(this.usrbean);
						String default_from = paramAdm.getValor(remBean.getIdInstitucion().toString(), "ENV", "DEFAULT_EMAIL_FROM", "");
						if (SIGAServicesHelper.isValidEmailAddress(default_from)) {
							// Si esta indicada una direccion de origen unica, entonces se sustituye el FROM aunque se mantiene en el REPLY-TO
							mensaje.setFrom(new InternetAddress(default_from, descripcionFrom));
							javax.mail.Address[] replyToAddresses = { new InternetAddress(sFrom, descripcionFrom) };
							mensaje.setReplyTo(replyToAddresses);
						} else {
							mensaje.setFrom(new InternetAddress(sFrom,descripcionFrom));
						}

						// Acuse de recibo
						if (envBean.getAcuseRecibo() != null && envBean.getAcuseRecibo().equals(ClsConstants.DB_TRUE))
							mensaje.addHeader("Disposition-Notification-To", sFrom);
						InternetAddress toInternetAddress = new InternetAddress(sTo);
						// Se especifica la direcci�n de destino.
						mensaje.addRecipient(MimeMessage.RecipientType.TO, toInternetAddress);

						String idPersona = String.valueOf(destBean.getIdPersona());

						// MENSAJE DE CORREO ELECTRONICO
						// ///////////////////////////////////
						// Obtenemos asunto y cuerpo del correo
						String consulta = envBean.getConsulta().equals("") ? null : envBean.getConsulta();
						// Si NO TIENE PLANTILLA NO SE HAN OBTENIDO LOS DATOS DE LA CONSULTA, LUEGO LOS OBTENEMOS
						if (htDatos == null) {

							EnvEnviosAdm admEnvio = new EnvEnviosAdm(this.usrbean);
							htDatos = admEnvio.getDatosEnvio(destBean, null);
							htDatos = admEnvio.darFormatoCampos(destBean.getIdInstitucion(), destBean.getIdEnvio(),
									this.usrbean.getLanguage(), htDatos, vCampos);

						}

						if (lImagenes != null && lImagenes.size() > 0)
							htDatos.put("imagenesPlantilla", lImagenes);
						Hashtable htCorreo = getCamposCorreoElectronico(envBean, destBean, Long.valueOf(idPersona), consulta, htDatos);
						String sAsunto = (htCorreo.get("asunto") == null) ? "" : (String) htCorreo.get("asunto");

						// Se especifica el texto del correo.

						// Se especifica el asunto del correo.
						mensaje.setSubject(sAsunto, "ISO-8859-1");
						mensaje.setHeader("Content-Type", "text/html; charset=\"ISO-8859-1\"");

						// Create your new message part
						// Se especifica que el correo es MultiPart: texto + fichero.
						// MimeMultipart multipart = new MimeMultipart("related");
						// MimeMultipart multipart = new MimeMultipart();

						String sCuerpo = (htCorreo.get("cuerpo") == null) ? "" : (String) htCorreo.get("cuerpo");

						MimeMultipart mixedMultipart = new MimeMultipart("mixed");
						MimeBodyPart mixedBodyPart = new MimeBodyPart();

						MimeMultipart relatedMultipart = new MimeMultipart("related");
						MimeBodyPart relatedBodyPart = new MimeBodyPart();

						// MimeMultipart alternativeMultipart = new MimeMultipart("alternative");
						// MimeBodyPart alternativeBodyPart = new MimeBodyPart();
						// alternativeBodyPart.setContent(relatedMultipart);

						// alternative message
						addContentToMultipart(relatedMultipart, sCuerpo);

						// Hierarchy
						mixedBodyPart.setContent(relatedMultipart);

						mixedMultipart.addBodyPart(mixedBodyPart);
						// mixedMultipart.addBodyPart(relatedBodyPart);
						// mixedMultipart.addBodyPart(alternativeBodyPart);
						// multipartRoot.a

						// add a part for the image

						if (lImagenes != null && lImagenes.size() > 0) {

							for (ImagenPlantillaForm imagenPlantilla : lImagenes) {

								EnvImagenPlantillaBean imagenPlantillaBean = imagenPlantilla.getImagenPlantillaBean();
								if (imagenPlantillaBean.isEmbebed()) {
									if (sCuerpo.indexOf(imagenPlantillaBean.getImagenSrcEmbebida("/")) != -1) {
										addCIDToMultipart(relatedMultipart, imagenPlantillaBean.getPathImagen(null, File.separator),
												imagenPlantillaBean.getNombre());
									}
								}
							}

						}
						// attach a pdf
						// Documentos adjuntos
						String sAttachment, sAttach;

						// ADJUNTAR EL PDF DEL ENVIO
						// ///////////////////////////////////
						/* archivo pdf: [idPersona].pdf */
						if (pathArchivoGenerado != null) {
							String[] pathGenerado = pathArchivoGenerado.split("\\\\");
							txtDocumentos.append(pathGenerado[pathGenerado.length - 1]);
							txtDocumentos.append(",");

							sAttachment = pathArchivoGenerado;
							sAttach = pathArchivoGenerado.substring(pathArchivoGenerado.lastIndexOf(File.separator) + 1);
							addAttachToMultipart(mixedMultipart, pathArchivoGenerado, sAttach);
						}

						// DOCUMENTOS ADJUNTOS
						// ///////////////////////////////////
						/* documentos adjuntos de env�o */
						// Solo adjuntamos docuemntos al envio NO es de tipo Docuemntacion letrado
						if (envBean.getIdTipoEnvios().equals(Integer.valueOf(EnvTipoEnviosAdm.K_CORREO_ELECTRONICO))) {
							EnvDocumentosAdm docAdm = new EnvDocumentosAdm(this.usrbean);
							Vector vDocs = docAdm.select(htPk);
							for (int d = 0; d < vDocs.size(); d++) {
								EnvDocumentosBean docBean = (EnvDocumentosBean) vDocs.elementAt(d);
								String idDoc = String.valueOf(docBean.getIdDocumento());
								File fDoc = docAdm.getFile(envBean, idDoc);
								sAttachment = fDoc.getPath();
								sAttach = docBean.getPathDocumento();
								addAttachToMultipart(mixedMultipart, fDoc.getPath(), docBean.getPathDocumento());
								txtDocumentos.append(docBean.getDescripcion());
								txtDocumentos.append(",");

							}
						}

						// Associate multi-part with message
						mensaje.setContent(mixedMultipart);

						// enviando!!!
						tr.sendMessage(mensaje, mensaje.getAllRecipients());

						// apuntando el estado
						EnvEstatEnvioAdm admEstat = new EnvEstatEnvioAdm(this.usrbean);
						admEstat.insertarApunteExtra(envBean.getIdInstitucion(), envBean.getIdEnvio(), envBean.getIdTipoEnvios(), new Long(idPersona), sTo);

					} catch (SMTPAddressFailedException e) {
						ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.enviarCorreoElectrnico(): " + e.getMessage(), e, 1);
						isErrorEnvioIndividual = true;
						errores = true;
						insertarMensajeLogHT(destBean, htErrores, e);
					} catch (SendFailedException e) {
						ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.enviarCorreoElectrnico(): " + e.getMessage(), e, 1);
						isErrorEnvioIndividual = true;
						errores = true;
						insertarMensajeLogHT(destBean, htErrores, e);
					} catch (javax.mail.MessagingException e) {
						ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.enviarCorreoElectrnico(): " + e.getMessage(), e, 1);
						isErrorEnvioIndividual = true;
						errores = true;
						insertarMensajeLogHT(destBean, htErrores, e.getNextException());
					} catch (Exception e) {
						ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.enviarCorreoElectrnico(): " + e.getMessage(), e, 1);
						isErrorEnvioIndividual = true;
						errores = true;
						insertarMensajeLogHT(destBean, htErrores, e);
					} finally {

						Hashtable htPkDest = new Hashtable();
						htPkDest.put(EnvDestinatariosBean.C_IDINSTITUCION, destBean.getIdInstitucion().toString());
						htPkDest.put(EnvDestinatariosBean.C_IDENVIO, destBean.getIdEnvio().toString());
						htPkDest.put(EnvDestinatariosBean.C_IDPERSONA, destBean.getIdPersona().toString());
						if (isErrorEnvioIndividual)
							htPkDest.put(EnvDestinatariosBean.C_IDESTADO, EnvEnviosAdm.ESTADO_PROCESADO_ERRORES);
						else
							htPkDest.put(EnvDestinatariosBean.C_IDESTADO, EnvEnviosAdm.ESTADO_PROCESADO);
						String[] claves = { EnvDestinatariosBean.C_IDINSTITUCION, EnvDestinatariosBean.C_IDENVIO,
								EnvDestinatariosBean.C_IDPERSONA };
						String[] campos = { EnvDestinatariosBean.C_IDESTADO };

						envDestinatariosAdm.updateDirect(htPkDest, claves, campos);
					}
				} // FOR
			} // IF

			// GENERAR EL LOG DEL ENVIO
			if (generarLog) {
				generarLogEnvioHTTuneado(vDestinatarios, remBean, txtDocumentos.toString(), htErrores, envBean);
			}

			// HAN OCURRIDO ERRORES DE DESTINATARIO
			if (errores) {
				return EnvEstadoEnvioAdm.K_ESTADOENVIO_PROCESADOCONERRORES;
			} else {
				return EnvEstadoEnvioAdm.K_ESTADOENVIO_PROCESADO;
			}

		} catch (SIGAException e) {
			ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.enviarCorreoElectronico(): " + e.getMessage(), e, 1);
			throw e;
		} catch (Exception e) {
			ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.enviarCorreoElectronico(): " + e.getMessage(), e, 1);
			throw new ClsExceptions(e, "Error enviando correo electr�nico");
		} finally {
			// cerramos el transport
			try {
				tr.close();
			} catch (Exception e) {
			}
		}
	}
	
	public void addContentToMultipart(MimeMultipart multipart,String htmlText) throws Exception
	{
	// first part (the html)
		BodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(htmlText, "text/html");
		// add it
		multipart.addBodyPart(messageBodyPart);
	}
	
	// -----
	/**
	* A�ade al mensaje un cid:name utilizado para guardar las imagenes referenciadas en el HTML de la forma <img src=cid:name />
	* @param cidname identificador que se le da a la imagen. suele ser un string generado aleatoriamente.
	* @param pathname ruta del fichero que almacena la imagen
	* @throws Exception excepcion levantada en caso de error
	*/
	public void addCIDToMultipart(MimeMultipart multipart,String pathname,String cidname) throws Exception
	{
		DataSource fds = new FileDataSource(pathname);
		BodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setDataHandler(new DataHandler(fds));
		messageBodyPart.setHeader("Content-ID","<"+cidname+">");
		multipart.addBodyPart(messageBodyPart);
	}
	// ----
	/**
	* A�ade un attachement al mensaje de email
	* @param pathname ruta del fichero
	* @throws Exception excepcion levantada en caso de error
	*/
	public void addAttachToMultipart(MimeMultipart multipart,String pathname,String name) throws Exception
	{
		File file = new File(pathname);
		BodyPart messageBodyPart = new MimeBodyPart();
		DataSource ds = new FileDataSource(file);
		messageBodyPart.setDataHandler(new DataHandler(ds));
		messageBodyPart.setFileName(name);
		messageBodyPart.setDisposition(MimePart.ATTACHMENT);
		
		multipart.addBodyPart(messageBodyPart);
	}
	
	private void actualizaPoblacionDestinatario(EnvDestinatariosBean destinatarioBean,Hashtable htPoblaciones) throws ClsExceptions, SIGAException{
		String descPoblacion = null;
		if (destinatarioBean.getIdPais().equals("") || destinatarioBean.getIdPais().equals(ClsConstants.ID_PAIS_ESPANA)) {
			CenPoblacionesAdm admPob = new CenPoblacionesAdm(this.usrbean);
			if(destinatarioBean.getIdPoblacion()!=null && !destinatarioBean.getIdPoblacion().trim().equals("")){
	        	if(htPoblaciones.containsKey(destinatarioBean.getIdPoblacion().trim()))
	        		descPoblacion = (String)htPoblaciones.get(destinatarioBean.getIdPoblacion().trim());
	        	else
	        		descPoblacion = admPob.getDescripcion(destinatarioBean.getIdPoblacion());
	        	htPoblaciones.put(destinatarioBean.getIdPoblacion().trim(), descPoblacion);
	        }else{
	        	descPoblacion = "";
	        	
	        }
			
        } else {
        	descPoblacion = destinatarioBean.getPoblacionExtranjera();
        }
		if(descPoblacion!=null && !descPoblacion.equalsIgnoreCase("null"))
			destinatarioBean.setPoblacion(descPoblacion);
		else
			destinatarioBean.setPoblacion("");
		
	}
	private void actualizaProvincia(EnvDestinatariosBean destinatarioBean,Hashtable htProvincias) throws ClsExceptions, SIGAException{
		CenProvinciaAdm admPro = new CenProvinciaAdm(this.usrbean);
		String descProvincia = null;
		if(destinatarioBean.getIdProvincia()!=null && !destinatarioBean.getIdProvincia().trim().equals("")){
        	if(htProvincias.containsKey(destinatarioBean.getIdProvincia().trim()))
        		descProvincia = (String)htProvincias.get(destinatarioBean.getIdProvincia().trim());
        	else
        		descProvincia = admPro.getDescripcion(destinatarioBean.getIdProvincia());
        	htProvincias.put(destinatarioBean.getIdProvincia().trim(), descProvincia);
        }else{
        	descProvincia = "";
        	
        }
		if(descProvincia!=null)
			destinatarioBean.setProvincia(descProvincia);
		else
			destinatarioBean.setProvincia("");
        
		
	}
	private void actualizaPaisDestinatario(EnvDestinatariosBean destinatarioBean,Hashtable htPaises) throws ClsExceptions, SIGAException{
		
		
        
        CenPaisAdm admPais = new CenPaisAdm(this.usrbean);
        String descPais = null;
        if(destinatarioBean.getIdPais()!=null && !destinatarioBean.getIdPais().trim().equals("")){
        	if(htPaises.containsKey(destinatarioBean.getIdPais().trim()))
        		descPais = (String)htPaises.get(destinatarioBean.getIdPais().trim());
        	else
        		descPais = admPais.getDescripcion(destinatarioBean.getIdPais());
        	htPaises.put(destinatarioBean.getIdPais().trim(), descPais);
        }else{
        	descPais = "";
        	
        }
        if(descPais!=null)
        	destinatarioBean.setPais(descPais);
		else
			destinatarioBean.setPais("");
        
        	
	}

	public String getNombreArchivoEnvioPlantillaDestinatario(EnvEnviosBean beanEnvio,Long idPersonaDestinatario,String tipoArchivoPlantilla) {
		String nombreFin = null;

		if (tipoArchivoPlantilla.equals(TIPOARCHIVO_FO)) {
			
			nombreFin = idPersonaDestinatario+".pdf";

		} else if (tipoArchivoPlantilla.equals(TIPOARCHIVO_DOC)) {
			//
			nombreFin = beanEnvio.getIdInstitucion().toString() + "_" + beanEnvio.getIdEnvio().toString() + "_" +idPersonaDestinatario + ".doc";

		} else {

			nombreFin = beanEnvio.getIdInstitucion().toString() + "_" + beanEnvio.getIdEnvio().toString() + "_" + idPersonaDestinatario+ "." + tipoArchivoPlantilla;

		}

		return nombreFin;

	}

	public String generarDocumentoEnvioPDFDestinatario(EnvEnviosBean beanEnvio,EnvDestinatariosBean beanDestinatario
			,File fPlantilla, String tipoArchivoPlantilla, Hashtable htDatos) throws SIGAException, ClsExceptions
	{
        try
        {           

            if (fPlantilla==null)
            {
                throw new SIGAException("messages.envios.error.noPlantilla");
            } else {
            	if (!fPlantilla.exists()) {
                    throw new SIGAException("messages.envios.error.noPlantilla");
            	} else
			    if (!fPlantilla.canRead()){
					throw new ClsExceptions ("Error de lectura del fichero: "+fPlantilla.getAbsolutePath());
			    }            		 
            }
            File ficheroSalida = null;
            Plantilla plantilla = new Plantilla(fPlantilla,this.usrbean);

            EnvEnviosAdm admEnvio = new EnvEnviosAdm(this.usrbean);
            
            
			
			if(tipoArchivoPlantilla.equals(TIPOARCHIVO_FO)){
				String path = admEnvio.getPathEnvio(beanEnvio) + File.separator + "documentosdest";			
				
	            
	            
	            // Generamos el archivo temporal que se obtendr� de sustituir las etiquetas
	            // de la plantilla
	            String nombreFin = path + File.separator + beanEnvio.getIdInstitucion().toString() + "_" + beanEnvio.getIdEnvio().toString() + "_" + beanDestinatario.getIdPersona();
	            File fIn = new File(nombreFin);
	            
	            // fIN contendr� el archivo obtenido de sustituir las etiquetas a la plantilla.
	            plantilla.sustituirEtiquetas(htDatos, fIn);
	            
	            String nombreFout = path + File.separator + beanDestinatario.getIdPersona() + ".pdf";
//	            File fOut = new File(nombreFout);
	            ficheroSalida = new File(nombreFout);
	            // El path base para los recursos ser� al path donde se almacena la plantilla
	            
	            try {
	            	MasterReport masterReport = new  MasterReport();
	            	masterReport.convertFO2PDF(fIn, ficheroSalida, fPlantilla.getParent());
	            } catch (Exception e) {
	        		ClsLogging.writeFileLogError("Error convirtiendo PDF.  Mensaje:" + e.getLocalizedMessage(),e,3);
	            	throw new ClsExceptions(e,"Error al convertir a PDF");
	            }
	            
	            // Borramos el temporal
	            fIn.delete();
			}else if(tipoArchivoPlantilla.equals(TIPOARCHIVO_DOC)){
//				File filePlantillaDoc = new File(fPlantilla.getPath());
//				filePlantillaDoc.createNewFile();
				MasterWords masterWord = new MasterWords(fPlantilla.getPath());
				Document documento = masterWord.nuevoDocumento();
				documento = masterWord.sustituyeDocumento(
						documento, htDatos);
				//identificador = identificador + ".doc";
				String path = admEnvio.getPathEnvio(beanEnvio) + File.separator + "documentosdest";
	            
				FileHelper.mkdirs(path);
				
	            // Generamos el archivo temporal que se obtendr� de sustituir las etiquetas
	            // de la plantilla
	            String nombreFin =  beanEnvio.getIdInstitucion().toString() + "_" + beanEnvio.getIdEnvio().toString() + "_" + beanDestinatario.getIdPersona()+".doc";
				
	            
				
				ficheroSalida = masterWord.grabaDocumento(documento,path,
						nombreFin);
				
				
			}else{
				
				String path = admEnvio.getPathEnvio(beanEnvio) + File.separator + "documentosdest";
				FileHelper.mkdirs(path);
				String nombreFin =  beanEnvio.getIdInstitucion().toString() + "_" + beanEnvio.getIdEnvio().toString() + "_" + beanDestinatario.getIdPersona()+"."+tipoArchivoPlantilla;
				ficheroSalida = SIGAServicesHelper.copyFile(fPlantilla, path, nombreFin);
				
			}

            return ficheroSalida.getPath();

        } catch (SIGAException e1) {
    		ClsLogging.writeFileLogError("Error generando PDF.  Mensaje:" + e1.getMsg(""),e1,3);
            throw e1;
            
        }catch(Exception e){
    		ClsLogging.writeFileLogError("Error generando PDF.  Mensaje:" + e.getLocalizedMessage(),e,3);
			throw new ClsExceptions (e, "Error general generando PDF ");
        }
	}

	
	public boolean existePlantillaEnvio(String idInstitucion, String idEnvio)  throws SIGAException, ClsExceptions {
	
		boolean existe = false;
		String sql = "select idinstitucion, idenvio, idtipoenvios, idplantillaenvios from env_envios where idinstitucion="+idInstitucion+" and idenvio=" + idEnvio +
		" and (idtipoenvios,idplantillaenvios) in (select idtipoenvios, idplantillaenvios from env_plantillasenvios)";
		RowsContainer v = this.find(sql);
		if (v!=null && v.size()>0) {
			existe=true;
		}
		return existe;
		
	}

	public String enviarSMS(EnvEnviosBean envBean,  Vector vDestinatarios, Hashtable htErrores, boolean generarLog) 
	throws SIGAException,ClsExceptions{

		boolean errores = false;

		try{

			// COMPROBACI�N
			/////////////////////////////////////
			if (!envBean.getIdTipoEnvios().equals(Integer.valueOf(EnvTipoEnviosAdm.K_SMS))){
				throw new ClsExceptions("Tipo de envio electr�nico incorrecto");
			}

			ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
			EnvDestinatariosAdm envDestinatariosAdm = new EnvDestinatariosAdm(this.usrbean);
			// BUCLE DE DESTINATARIOS
			/////////////////////////////////////
			if (vDestinatarios!=null) {
				//ACUMULAMOS POBLACIONES, PAISES Y PROVINCIAS PARA EVITAR HACER QUERYS A LA BBDD
				Hashtable htPoblaciones = new Hashtable();
				Hashtable htProvincia = new Hashtable();
				Hashtable htPaises = new Hashtable();
				boolean isErrorEnvioIndividual = false;
				for (int l=0;l<vDestinatarios.size();l++) {
					isErrorEnvioIndividual = false;
					EnvDestinatariosBean destBean = (EnvDestinatariosBean) vDestinatarios.elementAt(l);
					actualizaPaisDestinatario(destBean, htPaises);
					actualizaPoblacionDestinatario(destBean, htPoblaciones);
					actualizaProvincia(destBean, htProvincia);

					boolean generado=false;
					String sDirPdf = null;

					try{
						GenParametrosAdm paramAdm = new GenParametrosAdm(this.usrbean);
						String url_locator = paramAdm.getValor(destBean.getIdInstitucion().toString(),"ENV","SMS_URL_LOCATOR","");
						String url_service = paramAdm.getValor(destBean.getIdInstitucion().toString(),"ENV","SMS_URL_SERVICE","");
						String idClienteECOS = paramAdm.getValor(destBean.getIdInstitucion().toString(),"ENV","SMS_CLIENTE_ECOS","");
						if (url_locator.equals("") || url_service.equals("") || idClienteECOS.equals("")) {
							throw new ClsExceptions("No existen datos de configuraci�n en par�metros para envio SMS.");
						}

						String idPersona = String.valueOf(destBean.getIdPersona());
						String consulta = envBean.getConsulta().equals("")?null:envBean.getConsulta();						

						// texto
						String sTexto = getTextoSMS(envBean,destBean, Long.valueOf(idPersona),consulta);
						if (sTexto==null || "".equals(sTexto)){
							throw new ClsExceptions("No existe texto del mensaje.");
						}
						if (sTexto.length()>LONGITUD_SMS) 
							sTexto=sTexto.substring(LONGITUD_SMS-1);

						

						// destinatario
						String sTo = destBean.getMovil();										
						if (sTo==null || sTo.trim().equals("")) {
							throw new ClsExceptions("No existe n�mero de m�vil v�lido para el destinatario.");
						}
						
						
						sTo = UtilidadesString.getNumeroConPrefijoECOS(sTo);
						
						
						
						//BEGIN BNS INC_09400_SIGA
						//Comprobamos que el destinatario del SMS tienen n�mero de movil y este tiene un formato correcto
						//Realmente ya se ha comprobado en Envio.addDocumentosDestinatario (444/452)
						if (!UtilidadesString.esNumMovilECOS(sTo)){
							throw new ClsExceptions("El formato del n�mero de m�vil "+sTo+" es incorrecto. Formato correcto (+xx)6xxxxxxxx / (+xx)7xxxxxxxx");
						}
						String[] listaTOs = new String[] {sTo};
						
						ClsLogging.writeFileLog("locator: " + url_locator,10);
						ClsLogging.writeFileLog("service: " + url_service,10);

						ServiciosECOSService_ServiceLocator locator = new ServiciosECOSService_ServiceLocator();
						ServiciosECOSServiceSOAPStub stub = new ServiciosECOSServiceSOAPStub(new URL(url_service),locator);
						
						//END BNS INC_09400_SIGA

						//SMS
						SolicitudEnvioSMS sesms01 = new SolicitudEnvioSMS();
						sesms01.setIdClienteECOS(idClienteECOS);
						sesms01.setIdColegio(destBean.getIdInstitucion().toString());
						sesms01.setListaTOs(listaTOs);
						sesms01.setTexto(sTexto);
						sesms01.setIsProgramado(false);
						sesms01.setIsSMSCertificado(false);
						sesms01.setDatosGrandeCuenta(new DatosGrandeCuentaTO());
						
						
//						//Configuracion SSL 
//						System.setProperty("javax.net.ssl.keyStore", "C:\\bea92\\jdk150_10\\jre\\bin\\prueba.pfx");
//						System.setProperty("javax.net.ssl.keyStorePassword", "1111");
//						System.setProperty("javax.net.ssl.keyStoreType", "pkcs12");
//						System.setProperty("javax.net.ssl.trustStore", "C:\\bea92\\jdk150_10\\jre\\bin\\truststore");
//						System.setProperty("javax.net.ssl.trustStorePassword", "truststore");
////						System.setProperty("javax.net.debug", "ssl" );
////						System.setProperty( "java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol" );
////						Security.addProvider( new com.sun.net.ssl.internal.ssl.Provider() );
						
						Date ini = new Date();
						ClsLogging.writeFileLog("Enviando solicitud SMS...",10);
						ResultadoSolicitudEnvio response03 = stub.enviarSMS(sesms01);
						Date fin = new Date();
						ClsLogging.writeFileLog("Recibida respuesta SMS. TIEMPO: "+new Long((fin.getTime()-ini.getTime())).toString(),10);

						ClsLogging.writeFileLog("Resultado de la operaci�n: " + response03.getResultado(),10);
						if (response03.getResultado().indexOf("KO")!=-1) {
							// ERROR
							String msg = "Comprobar destinos disponibles: Espa�a, Francia, Italia, Hungr�a y Ruman�a";
							throw new ClsExceptions("Error en el servicio ECOS. "+response03.getResultado() + ".\t" + msg);	
						}

						ClsLogging.writeFileLog("ID de solicitud: " + response03.getIdSolicitud(),10);

						// RGG 08/06/2009 ESTADISTICA
						EnvEstatEnvioAdm admEstat = new EnvEstatEnvioAdm(this.usrbean);
						admEstat.insertarApunteExtra(envBean.getIdInstitucion(),envBean.getIdEnvio(),envBean.getIdTipoEnvios(),new Long(idPersona),sTo);


					} catch(UnsupportedClassVersionError e){
						ClsLogging.writeFileLog("ERROR - EnvEnviosAdm.enviarSMS(): " + e.getMessage(), 1);
						ClsExceptions ex = new ClsExceptions("UnsupportedClassVersionError: "+e.toString());
						errores = true;
						isErrorEnvioIndividual = true;
						insertarMensajeLogHT(destBean,htErrores, ex);
					} catch (Exception e){
						ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.enviarSMS(): " + e.getMessage(), e, 1);
						errores = true;
						isErrorEnvioIndividual = true;
						insertarMensajeLogHT(destBean,htErrores, e);
					} catch (Throwable t){
						ClsLogging.writeFileLog("ERROR - EnvEnviosAdm.enviarSMS(): " + t.getMessage(), 1);
						errores = true;
						isErrorEnvioIndividual = true;
						t.printStackTrace();
						insertarMensajeLogHT(destBean,htErrores, new ClsExceptions("ERROR THROWABLE en env�os: "+t.toString()));
					}finally{
		            	
		    			Hashtable htPkDest = new Hashtable();
		    			htPkDest.put(EnvDestinatariosBean.C_IDINSTITUCION,destBean.getIdInstitucion().toString());
		    			htPkDest.put(EnvDestinatariosBean.C_IDENVIO,destBean.getIdEnvio().toString());
		    			htPkDest.put(EnvDestinatariosBean.C_IDPERSONA,destBean.getIdPersona().toString());
		    			if(isErrorEnvioIndividual)
		    				htPkDest.put(EnvDestinatariosBean.C_IDESTADO, EnvEnviosAdm.ESTADO_PROCESADO_ERRORES);
		    			else
		    				htPkDest.put(EnvDestinatariosBean.C_IDESTADO, EnvEnviosAdm.ESTADO_PROCESADO);
		    			String[] claves = {EnvDestinatariosBean.C_IDINSTITUCION, 
		              		   EnvDestinatariosBean.C_IDENVIO , EnvDestinatariosBean.C_IDPERSONA         		   };
		    			 String[] campos = { EnvDestinatariosBean.C_IDESTADO};
		    			 
		    			envDestinatariosAdm.updateDirect(htPkDest, claves,campos);
		            	
		            }

				} // FOR
			} // IF


			// GENERAR EL LOG DEL ENVIO
			/////////////////////////////////////
			if (generarLog) {
				generarLogEnvioHT(vDestinatarios,null,null, htErrores, envBean);
			}

			// HAN OCURRIDO ERRORES DE DESTINATARIO
			/////////////////////////////////////
			if (errores){
				return EnvEstadoEnvioAdm.K_ESTADOENVIO_PROCESADOCONERRORES;
			} else {
				return EnvEstadoEnvioAdm.K_ESTADOENVIO_PROCESADO;
			}

		} catch (SIGAException e) { 
			ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.enviarSMS(): " + e.getMessage(), e, 1);
			throw e;
		} catch(Exception e){
			ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.enviarSMS(): " + e.getMessage(), e, 1);
			throw new ClsExceptions(e,"Error enviando correo electr�nico");
		} 
	}
	
	public String enviarBuroSMS(EnvEnviosBean envBean,  Vector vDestinatarios, Hashtable htErrores, boolean generarLog) 
	throws SIGAException,ClsExceptions{
    
    boolean errores = false;
    Transport tr = null;
    
    try{

        // COMPROBACI�N
        /////////////////////////////////////
        if (!envBean.getIdTipoEnvios().equals(Integer.valueOf(EnvTipoEnviosAdm.K_BUROSMS))){
            throw new ClsExceptions("Tipo de envio electr�nico incorrecto");
        }
        

	    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//		ReadProperties rp = new ReadProperties("SIGA.properties");	
		
	    EnvDestinatariosAdm envDestinatariosAdm = new EnvDestinatariosAdm(this.usrbean);
	    // OBTENCION DE REMITENTE 
        /////////////////////////////////////
        EnvRemitentesAdm remAdm = new EnvRemitentesAdm(this.usrbean);
	    Hashtable htPk = new Hashtable();
	    htPk.put(EnvEnviosBean.C_IDINSTITUCION,envBean.getIdInstitucion());
	    htPk.put(EnvEnviosBean.C_IDENVIO,envBean.getIdEnvio());
        Vector vRem = remAdm.select(htPk);
        String sFrom = "";
        if (vRem.size()>0){
        	// obtengo la primera de la lista
        	EnvRemitentesBean remBean = (EnvRemitentesBean) vRem.firstElement();
	        sFrom = remBean.getCorreoElectronico();
        }else{
        	// obtengo la de la institucion
            Row dirPref = getDireccionPreferenteInstitucion(envBean.getIdInstitucion(),EnvTipoEnviosAdm.K_CORREO_ELECTRONICO);
            sFrom = dirPref.getString(EnvRemitentesBean.C_CORREOELECTRONICO);
        }

        

        
        // BUCLE DE DESTINATARIOS
        /////////////////////////////////////
        if (vDestinatarios!=null) {
        	 //ACUMULAMOS POBLACIONES, PAISES Y PROVINCIAS PARA EVITAR HACER QUERYS A LA BBDD
    	    Hashtable htPoblaciones = new Hashtable();
    	    Hashtable htProvincia = new Hashtable();
    	    Hashtable htPaises = new Hashtable();
    	    
    	    boolean isErrorEnvioIndividual = false;
				
	        for (int l=0;l<vDestinatarios.size();l++) {
	        	isErrorEnvioIndividual = false;

	        	EnvDestinatariosBean destBean = (EnvDestinatariosBean) vDestinatarios.elementAt(l);
	        	actualizaPaisDestinatario(destBean, htPaises);
		        actualizaPoblacionDestinatario(destBean, htPoblaciones);
		        actualizaProvincia(destBean, htProvincia);
                
                boolean generado=false;
		        String sDirPdf = null;
		        
	            // ENVIO PARA CADA DESTINATARIO
		        /////////////////////////////////////
		        try{
		        
	        
//		            String sTo = destBean.getCorreoElectronico();
//		            
//		            //Se crea un nuevo Mensaje.
//		    	    MimeMessage mensaje = new MimeMessage(sesion);
//		    	    
//		    	    //Se especifica la direcci�n de origen.
////		    	    mensaje.setFrom(new InternetAddress(sFrom));
//		    	    
//		    	    //Se especifica la direcci�n de destino.
//		    	    mensaje.addRecipient(Message.RecipientType.TO, new InternetAddress(sTo));
//		    	    
//		    	    //Se especifica que el correo es MultiPart: texto + fichero.
//		    	    MimeMultipart multipart = new MimeMultipart();
//		    		
//		    		//Documentos adjuntos
//		    	    MimeBodyPart bodyPart = new MimeBodyPart();    	    
//		    	    String sAttachment,sAttach;
//		    	    String idPersona = String.valueOf(destBean.getIdPersona());
//		    	    
//		            // MENSAJE DE CORREO ELECTRONICO
//			        /////////////////////////////////////
//		    	    //Obtenemos asunto y cuerpo del correo
//		    	    String consulta = envBean.getConsulta().equals("")?null:envBean.getConsulta();
//			        Hashtable htCorreo = getCamposCorreoElectronico(envBean, Long.valueOf(idPersona),consulta);
//			        String sAsunto = (htCorreo.get("asunto")==null)?"":(String)htCorreo.get("asunto");
//			        String sCuerpo = (htCorreo.get("cuerpo")==null)?"":(String)htCorreo.get("cuerpo");
//			        
//			        //Se especifica el texto del correo.
//		    	    bodyPart = new MimeBodyPart();
//		    	    bodyPart.setText(sCuerpo,"ISO-8859-1");
//		    	    multipart.addBodyPart(bodyPart);        	
//		    		
//		    	    //Se especifica el asunto del correo.
//		    	    mensaje.setSubject(sAsunto,"ISO-8859-1");	    	
//		    	    mensaje.setHeader("Content-Type","text/plain; charset=\"ISO-8859-1\"");
//		    	    //Se anhade el contenido al fichero.
//		    	    mensaje.setContent(multipart);
//		    	    
//		    	    // Se env�a el correo.
//		    	    //Transport.send(mensaje);
//		    	    tr.sendMessage(mensaje, mensaje.getAllRecipients());

		            
//		            String url_locator = "http://10.60.3.80:7001/ecos/wsecos/services/";
//		        	String url_service = "http://10.60.3.80:7001/ecos/wsecos/services/ServiciosECOSService.service";
//		        	String idClienteECOS = "idclienteecos";

		        	GenParametrosAdm paramAdm = new GenParametrosAdm(this.usrbean);
		        	String url_locator = paramAdm.getValor(destBean.getIdInstitucion().toString(),"ENV","SMS_URL_LOCATOR","");
		        	String url_service = paramAdm.getValor(destBean.getIdInstitucion().toString(),"ENV","SMS_URL_SERVICE","");
		        	String idClienteECOS = paramAdm.getValor(destBean.getIdInstitucion().toString(),"ENV","SMS_CLIENTE_ECOS","");
		        	if (url_locator.equals("") || url_service.equals("") || idClienteECOS.equals("")) {
		        	    throw new ClsExceptions("No existen datos de configuraci�n en par�metros para envio SMS.");
		        	}
		            
		            String idPersona = String.valueOf(destBean.getIdPersona());
		    	    String consulta = envBean.getConsulta().equals("")?null:envBean.getConsulta();

		            
		    	    if (sFrom==null || sFrom.trim().equals("")) {
		    	       throw new ClsExceptions("No existe n�mero de email de remitente para la confiraci�n.");
		    	    }
		    	    
					// texto
					String sTexto = getTextoSMS(envBean,destBean, Long.valueOf(idPersona),consulta);
					if (sTexto==null || "".equals(sTexto)){
						throw new ClsExceptions("No existe texto del mensaje.");
					}
					if (sTexto.length()>LONGITUD_SMS) 
						sTexto=sTexto.substring(LONGITUD_SMS-1);						
		            
		           
					
					// destinatario
					String sTo = destBean.getMovil();									
					if (sTo==null || sTo.trim().equals("")) {
						throw new ClsExceptions("No existe n�mero de m�vil v�lido para el destinatario.");
					}
					
					
					sTo = UtilidadesString.getNumeroConPrefijoECOS(sTo);
					
					//BEGIN BNS INC_09400_SIGA
					//Comprobamos que el destinatario del SMS tienen n�mero de movil y este tiene un formato correcto
					// Realmente ya se ha comprobado en Envio.addDocumentosDestinatario (444/452)
					if (!UtilidadesString.esNumMovilECOS(sTo)){
						throw new ClsExceptions("El formato del n�mero de m�vil "+sTo+" es incorrecto. Formato correcto (+xx)6xxxxxxxx / (+xx)7xxxxxxxx");
					}
					 ClsLogging.writeFileLog("locator: " + url_locator,10);
					ClsLogging.writeFileLog("service: " + url_service,10);
					
					ServiciosECOSService_ServiceLocator locator = new ServiciosECOSService_ServiceLocator(url_locator);
					ServiciosECOSServiceSOAPStub stub = new ServiciosECOSServiceSOAPStub(new URL(url_service), locator);
					
					String[] listaTOs = new String[] {sTo};
					//END BNS INC_09400_SIGA
					
					//SMS
					SolicitudEnvioSMS sesms01 = new SolicitudEnvioSMS();
					sesms01.setIdClienteECOS(idClienteECOS);
					sesms01.setIdColegio(destBean.getIdInstitucion().toString());
					sesms01.setListaTOs(listaTOs);
					sesms01.setTexto(sTexto);
					sesms01.setIsProgramado(false);
					sesms01.setIsSMSCertificado(true);
					sesms01.setEmail(sFrom);
					sesms01.setDatosGrandeCuenta(new DatosGrandeCuentaTO());					

					Date ini = new Date();
					ClsLogging.writeFileLog("Enviando solicitud BuroSMS...",10);
					ResultadoSolicitudEnvio response03 = stub.enviarSMS(sesms01);
					
					Date fin = new Date();
					ClsLogging.writeFileLog("Recibida respuesta BuroSMS. TIEMPO: "+new Long((fin.getTime()-ini.getTime())).toString(),10);
					
					ClsLogging.writeFileLog("Resultado de la operaci�n: " + response03.getResultado(),10);
	    	        if (response03.getResultado().indexOf("KO")!=-1) {
	    	        	// ERROR
						String msg = "Comprobar destinos disponibles: Espa�a, Francia, Italia, Hungr�a y Ruman�a";
						throw new ClsExceptions("Error en el servicio ECOS. "+response03.getResultado() + ".\t" + msg);	
	    	        }

					ClsLogging.writeFileLog("ID de solicitud: " + response03.getIdSolicitud(),10);
					
					//CR7 - INC_11336_SIGA. Hay que a�adir un nuevo campo en EnvEnvios para guardar el idSolicitud
					htPk.put(EnvEnviosBean.C_IDSOLICITUDECOS,response03.getIdSolicitud());
					String[] campos = {EnvEnviosBean.C_IDSOLICITUDECOS};
					this.updateDirect(htPk, this.getClavesBean(), campos);
		            
		    	    // RGG 08/06/2009 ESTADISTICA
		    	    EnvEstatEnvioAdm admEstat = new EnvEstatEnvioAdm(this.usrbean);
		    	    admEstat.insertarApunteExtra(envBean.getIdInstitucion(),envBean.getIdEnvio(),envBean.getIdTipoEnvios(),new Long(idPersona),sTo);

		    	    
		        } catch(UnsupportedClassVersionError e){
		        	ClsLogging.writeFileLog("ERROR - EnvEnviosAdm.enviarBuroSMS(): " + e.getMessage(), 1);
		            ClsExceptions ex = new ClsExceptions("UnsupportedClassVersionError: "+e.toString());
		            errores = true;
		            isErrorEnvioIndividual = true;
	                insertarMensajeLogHT(destBean,htErrores, ex);
	            } catch (Exception e){
	            	ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.enviarBuroSMS(): " + e.getMessage(), e, 1);
	                errores = true;
	                isErrorEnvioIndividual = true;
	                insertarMensajeLogHT(destBean,htErrores, e);
	            } catch (Throwable t){
	            	ClsLogging.writeFileLog("ERROR - EnvEnviosAdm.enviarBuroSMS(): " + t.getMessage(), 1);
	                errores = true;
	                isErrorEnvioIndividual = true;
	                t.printStackTrace();
	                insertarMensajeLogHT(destBean,htErrores, new ClsExceptions("ERROR THROWABLE en env�os: "+t.toString()));
	            }finally{
	            	
	    			Hashtable htPkDest = new Hashtable();
	    			htPkDest.put(EnvDestinatariosBean.C_IDINSTITUCION,destBean.getIdInstitucion().toString());
	    			htPkDest.put(EnvDestinatariosBean.C_IDENVIO,destBean.getIdEnvio().toString());
	    			htPkDest.put(EnvDestinatariosBean.C_IDPERSONA,destBean.getIdPersona().toString());
	    			if(isErrorEnvioIndividual)
	    				htPkDest.put(EnvDestinatariosBean.C_IDESTADO, EnvEnviosAdm.ESTADO_PROCESADO_ERRORES);
	    			else
	    				htPkDest.put(EnvDestinatariosBean.C_IDESTADO, EnvEnviosAdm.ESTADO_PROCESADO);
	    			String[] claves = {EnvDestinatariosBean.C_IDINSTITUCION, 
	              		   EnvDestinatariosBean.C_IDENVIO , EnvDestinatariosBean.C_IDPERSONA         		   };
	    			 String[] campos = { EnvDestinatariosBean.C_IDESTADO};
	    			 
	    			envDestinatariosAdm.updateDirect(htPkDest, claves,campos);
	            	
	            }

	        } // FOR
        } // IF


        // GENERAR EL LOG DEL ENVIO
        /////////////////////////////////////
        if (generarLog) {
        	generarLogEnvioHT(vDestinatarios, null,null, htErrores, envBean);
        }
        
        // HAN OCURRIDO ERRORES DE DESTINATARIO
        /////////////////////////////////////
        if (errores){
        	return EnvEstadoEnvioAdm.K_ESTADOENVIO_PROCESADOCONERRORES;
        } else {
            return EnvEstadoEnvioAdm.K_ESTADOENVIO_PROCESADO;
        }
        
    } catch (SIGAException e) { 
    	ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.enviarBuroSMS(): " + e.getMessage(), e, 1);
		throw e;
    } catch(Exception e){
    	ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.enviarBuroSMS(): " + e.getMessage(), e, 1);
        throw new ClsExceptions(e,"Error enviando correo electr�nico");
	} finally {
        // cerramos el transport
	    try {
	    	tr.close();
	    } catch (Exception e) {}

	}
	}

  
    public Vector getDireccionEspecifica(String idInstitucion,String idPersona,String idDireccion,String idTipoEnvio)
    		throws ClsExceptions{

    	
		Vector direcciones = new Vector();
		CenDireccionesAdm direccionesAdm = new CenDireccionesAdm(this.usrbean);
		
		// primero buscamos el iddireccion por tipo de direccion. SI no existe buscamos con el criterio anterior.
		Vector vDir = direccionesAdm.getDireccionEspecificaConTipo(idInstitucion, idPersona, idDireccion, idTipoEnvio);
		if (vDir!=null && vDir.size()>0) {
			return vDir;
		} else {		
			return getDirecciones(idInstitucion, idPersona, idTipoEnvio);
		}
	}

    public static Integer relanzarEnviosProcesando() throws ClsExceptions{
    	String sql = " UPDATE " + EnvEnviosBean.T_NOMBRETABLA + " SET IDESTADO = " + ESTADO_INICIAL + " WHERE IDESTADO = " + ESTADO_PROCESANDO;
    	Connection connec = null;
    	PreparedStatement SqlStatement = null;
    	Integer updatedRecords = null;
    	try{
	    	connec = ClsMngBBDD.getConnection();
	    	SqlStatement = connec.prepareStatement(sql);
	    	updatedRecords = SqlStatement.executeUpdate();
    	} catch (SQLException exc) {
	       	ClsLogging.writeFileLog("Error en UPDATE BIND: "+ exc.getMessage() + " SQL:"+sql,3);
		    ClsExceptions psscEx = new ClsExceptions(exc,exc.getMessage().substring(0, exc.getMessage().length() - 1));
			psscEx.setErrorType("9");
			psscEx.setParam(EnvEnviosBean.T_NOMBRETABLA);
			throw psscEx;
	    } finally {
			try {
				if (SqlStatement != null) {
					SqlStatement.close();
				}
				
				if (connec != null) {
					ClsMngBBDD.closeConnection(connec);
				}
			} catch (SQLException exc) {
				if (connec != null) {
					ClsMngBBDD.closeConnection(connec);
				}
				
				ClsExceptions psscEx = new ClsExceptions(exc,exc.getMessage().substring(0, exc.getMessage().length() - 1));
				psscEx.setErrorType("9");
				psscEx.setParam(EnvEnviosBean.T_NOMBRETABLA);
				
				throw psscEx;
			}
		}
    	return updatedRecords;
    }
    public boolean insert(EnvEnviosBean bean) throws ClsExceptions{
    	bean.setComisionAJG(this.usrbean.isComision()?Short.valueOf(ClsConstants.DB_TRUE):Short.valueOf(ClsConstants.DB_FALSE));
		try {
			return this.insert(this.beanToHashTable(bean));
		}
		catch (Exception e)	{
			ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.insert(): " + e.getMessage(), e, 1);
			throw new ClsExceptions (e,  e.getMessage());
		}
	}
    private void insertarComunicacionDesignaSalida(ScsComunicaciones scsComunicaciones, UsrBean usr)throws BusinessException{
    	StringBuffer s = new StringBuffer();
    	s.append("insert into SCS_COMUNICACIONES (IDCOMUNICACION, IDINSTITUCION, IDENVIOSALIDA,  ");
    	s.append(" DESIGNAANIO, DESIGNANUMERO, DESIGNAIDTURNO, FECHAMODIFICACION, " );
    	s.append(" USUMODIFICACION ) values ((SELECT NVL(MAX(IDCOMUNICACION), 0) + 1 FROM scs_comunicaciones),");
    	
		s.append(scsComunicaciones.getIdinstitucion());
		s.append(",");
		s.append(scsComunicaciones.getIdenviosalida());
		s.append(",");
    	s.append(scsComunicaciones.getDesignaanio());
		s.append(",");
		s.append(scsComunicaciones.getDesignanumero());
		s.append(",");
		s.append(scsComunicaciones.getDesignaidturno());
		s.append(",");
		s.append("sysdate");
		s.append(",");
		s.append(usr.getUserName());
		s.append(")");
		try {
			insertSQL(s.toString());
		} catch (ClsExceptions e) {
			ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.insertarComunicacionDesignaSalida(): " + e.getMessage(), e, 1);
			throw new BusinessException("Error al insertarComunicacionDesigna"+e.toString());
		}
    	
    }
    
    public void insertarComunicacionDesignaSalida(List<ScsDesigna> designas,
			Long idEnvio,UsrBean usrBean)  {
		ScsComunicaciones scsComunicaciones = new ScsComunicaciones();
		for (ScsDesigna scsDesigna : designas) {
			
			
			scsComunicaciones.setDesignaanio(scsDesigna.getAnio());
			scsComunicaciones.setDesignaidturno(scsDesigna.getIdturno());
			scsComunicaciones.setDesignanumero(scsDesigna.getNumero());
			scsComunicaciones.setIdinstitucion(scsDesigna.getIdinstitucion());
			scsComunicaciones.setIdenviosalida(idEnvio);
			insertarComunicacionDesignaSalida(scsComunicaciones,usrBean);
			
		}
	}
    
    
    private void insertarComunicacionDesignaEntrada(ScsComunicaciones scsComunicaciones, UsrBean usr)throws BusinessException{
    	StringBuffer s = new StringBuffer();
    	s.append("insert into SCS_COMUNICACIONES (IDCOMUNICACION, IDINSTITUCION, IDENVIOENTRADA,  ");
    	s.append(" DESIGNAANIO, DESIGNANUMERO, DESIGNAIDTURNO, FECHAMODIFICACION, " );
    	s.append(" USUMODIFICACION ) values (NVL(SELECT MAX(IDCOMUNICACION) + 1 FROM scs_comunicaciones), 1),");
    	
		s.append(scsComunicaciones.getIdinstitucion());
		s.append(",");
		s.append(scsComunicaciones.getIdenvioentrada());
		s.append(",");
    	s.append(scsComunicaciones.getDesignaanio());
		s.append(",");
		s.append(scsComunicaciones.getDesignanumero());
		s.append(",");
		s.append(scsComunicaciones.getDesignaidturno());
		s.append(",");
		s.append("sysdate");
		s.append(",");
		s.append(usr.getUserName());
		try {
			insertSQL(s.toString());
		} catch (ClsExceptions e) {
			ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.insertarComunicacionDesignaEntrada(): " + e.getMessage(), e, 1);
			throw new BusinessException("Error al insertarComunicacionDesignaEntrada"+e.toString());
		}
    	
    }
    
    public void insertarComunicacionDesignaEntrada(List<ScsDesigna> designas,
    		Long idEnvio,UsrBean usrBean)  {
		ScsComunicaciones scsComunicaciones = new ScsComunicaciones();
		for (ScsDesigna scsDesigna : designas) {
			
			
			scsComunicaciones.setDesignaanio(scsDesigna.getAnio());
			scsComunicaciones.setDesignaidturno(scsDesigna.getIdturno());
			scsComunicaciones.setDesignanumero(scsDesigna.getNumero());
			scsComunicaciones.setIdinstitucion(scsDesigna.getIdinstitucion());
			scsComunicaciones.setIdenvioentrada(idEnvio);
			insertarComunicacionDesignaEntrada(scsComunicaciones,usrBean);
			
		}
	}
    
    
    
	
    private void insertarComunicacionEjgSalida(ScsComunicaciones scsComunicaciones, UsrBean usr)throws BusinessException{
    	StringBuffer s = new StringBuffer();
    	s.append("insert into SCS_COMUNICACIONES (IDCOMUNICACION, IDINSTITUCION, IDENVIOSALIDA,  ");
    	s.append(" EJGANIO, EJGNUMERO,     EJGIDTIPO, FECHAMODIFICACION, " );
    	s.append(" USUMODIFICACION ) values ((SELECT NVL(MAX(IDCOMUNICACION), 0) + 1 FROM scs_comunicaciones),");
    	
		s.append(scsComunicaciones.getIdinstitucion());
		s.append(",");
		s.append(scsComunicaciones.getIdenviosalida());
		s.append(",");
    	s.append(scsComunicaciones.getEjganio());
		s.append(",");
		s.append(scsComunicaciones.getEjgnumero());
		s.append(",");
		s.append(scsComunicaciones.getEjgidtipo());
		s.append(",");
		s.append("sysdate");
		s.append(",");
		s.append(usr.getUserName());
		s.append(")");
		
		try {
			insertSQL(s.toString());
		} catch (ClsExceptions e) {
			ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.insertarComunicacionEjgSalida(): " + e.getMessage(), e, 1);
			throw new BusinessException("Error al insertarComunicacionEjgSalida"+e.toString());
		}
    	
    }
    
    private void insertarComunicacionSalida(ScsComunicaciones scsComunicaciones, UsrBean usr)throws BusinessException{
    	StringBuffer s = new StringBuffer();
    	s.append("insert into SCS_COMUNICACIONES (IDCOMUNICACION, IDINSTITUCION, IDENVIOSALIDA,  ");
    	s.append(" EJGANIO, EJGNUMERO,     EJGIDTIPO,DESIGNAANIO,  DESIGNANUMERO, 	  DESIGNAIDTURNO, FECHAMODIFICACION, " );
    	s.append(" USUMODIFICACION ) values ((SELECT NVL(MAX(IDCOMUNICACION), 0) + 1 FROM scs_comunicaciones),");
		s.append(scsComunicaciones.getIdinstitucion());
		s.append(",");
		s.append(scsComunicaciones.getIdenviosalida());
		s.append(",");
    	s.append(scsComunicaciones.getEjganio());
		s.append(",");
		s.append(scsComunicaciones.getEjgnumero());
		s.append(",");
		s.append(scsComunicaciones.getEjgidtipo());
		s.append(",");
		s.append(scsComunicaciones.getDesignaanio());
		s.append(",");
		s.append(scsComunicaciones.getDesignanumero());
		s.append(",");
		s.append(scsComunicaciones.getDesignaidturno());
		s.append(",");
		s.append("sysdate");
		s.append(",");
		s.append(usr.getUserName());
		s.append(")");
		
		try {
			insertSQL(s.toString());
		} catch (ClsExceptions e) {
			ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.insertarComunicacionSalida(): " + e.getMessage(), e, 1);
			throw new BusinessException("Error al insertarComunicacionSalida"+e.toString());
		}
    	
    }
    
    
    
    
	

	public void insertarComunicacionEjgSalida(List<ScsEjg> ejgs, Long idEnvio, UsrBean usr)
			throws BusinessException {
		ScsComunicaciones scsComunicaciones = new ScsComunicaciones();
		for (ScsEjg scsEjg : ejgs) {
			scsComunicaciones.setEjganio(scsEjg.getAnio());
			scsComunicaciones.setEjgidtipo(scsEjg.getIdtipoejg());
			scsComunicaciones.setEjgnumero(scsEjg.getNumero());
			scsComunicaciones.setIdinstitucion(scsEjg.getIdinstitucion());
			scsComunicaciones.setIdenviosalida(idEnvio);
			scsComunicaciones.setFechamodificacion(new Date());
			insertarComunicacionEjgSalida(scsComunicaciones, usr);
		}
		
	}

	
	

	
	private void insertarComunicacionEjgEntrada(ScsComunicaciones scsComunicaciones, UsrBean usr)throws BusinessException{
    	StringBuffer s = new StringBuffer();
    	s.append("insert into SCS_COMUNICACIONES (IDCOMUNICACION, IDINSTITUCION, IDENVIOENTRADA,  ");
    	s.append(" EJGANIO, EJGNUMERO,     EJGIDTIPO, FECHAMODIFICACION, " );
    	s.append(" USUMODIFICACION ) values (NVL(SELECT MAX(IDCOMUNICACION) + 1 FROM scs_comunicaciones), 1),");
    	
		s.append(scsComunicaciones.getIdinstitucion());
		s.append(",");
		s.append(scsComunicaciones.getIdenvioentrada());
		s.append(",");
		s.append(scsComunicaciones.getEjganio());
		s.append(",");
		s.append(scsComunicaciones.getEjgnumero());
		s.append(",");
		s.append(scsComunicaciones.getEjgidtipo());
		s.append(",");
		s.append("sysdate");
		s.append(",");
		s.append(usr.getUserName());
		try {
			insertSQL(s.toString());
		} catch (ClsExceptions e) {
			ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.insertarComunicacionEjgEntrada(): " + e.getMessage(), e, 1);
			throw new BusinessException("Error al insertarComunicacionEjgEntrada"+e.toString());
		}
    	
    }
    
    public void insertarComunicacionEjgEntrada(List<ScsEjg> ejgs,
    		Long idEnvio,UsrBean usrBean)  {
		ScsComunicaciones scsComunicaciones = new ScsComunicaciones();
		for (ScsEjg scsEjg : ejgs) {
			scsComunicaciones.setEjganio(scsEjg.getAnio());
			scsComunicaciones.setEjgidtipo(scsEjg.getIdtipoejg());
			scsComunicaciones.setEjgnumero(scsEjg.getNumero());
			scsComunicaciones.setIdinstitucion(scsEjg.getIdinstitucion());
			scsComunicaciones.setIdenvioentrada(idEnvio);
			scsComunicaciones.setFechamodificacion(new Date());
			insertarComunicacionEjgEntrada(scsComunicaciones, usrBean);
		}
	}
    
    public Long	getSecuenciaNextVal(String nombreSecuencia)throws ClsExceptions {
		int contador = 0;
		Long id=null;
		
		RowsContainer rc = new RowsContainer(); 
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append(nombreSecuencia);
		sql.append(".NEXTVAL from DUAL");
		try {		
			if (rc.findForUpdate(sql.toString())) {	
				Row fila = (Row) rc.get(0);
				id = Long.valueOf((String)fila.getRow().get("NEXTVAL"));														
			}
		}	
		catch (ClsExceptions e) {	
			ClsLogging.writeFileLogError("ERROR - EnvEnviosAdm.gtSecuenciaNextVal(): " + e.getMessage(), e, 1);
			throw new ClsExceptions (e, "Error al ejecutar el 'getSecuenciaNextVal' en BBDD");		
		}		
		return id;
	}
    /**
     * 
     * @param idEnvio
     * @param idInstitucion
     * @return devuelve el nuevo idEnvio generado
     * @throws SIGAException
     */
    public Integer reenviar(Integer idEnvio,Short idInstitucion) throws SIGAException{
    	return reenviar(idEnvio, idInstitucion,null);
    }
    /**
     * 
     * @param idOldEnvio
     * @param idInstitucion
     * @param envDestinatariosVector
     * @return devuelve el nuevo idEnvio generado
     * @throws SIGAException
     */
	private Integer reenviar(Integer idOldEnvio,Short idInstitucion,Vector<EnvDestinatariosBean> envDestinatariosVector) throws SIGAException{
		
		UserTransaction tx = null;
		EnvCamposEnviosAdm envCamposEnviosAdm = new EnvCamposEnviosAdm(this.usrbean);
		EnvDestinatariosAdm envDestinatariosAdm = new EnvDestinatariosAdm(this.usrbean);
		EnvListaCorreosEnviosAdm envListaCorreosEnviosAdm = new EnvListaCorreosEnviosAdm(this.usrbean);
		EnvRemitentesAdm envRemitentesAdm = new EnvRemitentesAdm(this.usrbean);
		EnvDocumentosAdm envDocumentosAdm = new EnvDocumentosAdm(this.usrbean);
		SalidaEnviosService salidaEnviosService = (SalidaEnviosService)BusinessManager.getInstance().getService(SalidaEnviosService.class);
		Hashtable pkEnvioHashtable = new Hashtable();
		pkEnvioHashtable.put(EnvEnviosBean.C_IDINSTITUCION,idInstitucion);
		pkEnvioHashtable.put(EnvEnviosBean.C_IDENVIO,idOldEnvio);
		Vector envioVector;
		Integer idNewEnvio = null;
		try {
			envioVector = selectByPK(pkEnvioHashtable);
			EnvEnviosBean envioBean = (EnvEnviosBean) envioVector.get(0);
			envioBean.setIdEstado(new Integer(EnvEnviosAdm.ESTADO_INICIAL));
			envioBean.setFechaCreacion("SYSDATE");
			envioBean.setFechaProgramada(null);
			envioBean.setCSV(null);
			
			// Obtenemos todos los campos configurados para el envio 
			Vector<EnvCamposEnviosBean> envCamposEnviosVector = envCamposEnviosAdm.select(pkEnvioHashtable);
			
			//Obtenemos las listas de correo configuradas
			Vector<EnvListaCorreosEnviosBean> envListaCorreosEnviosVector = null;
			if(envDestinatariosVector!=null){
				envListaCorreosEnviosVector =  envListaCorreosEnviosAdm.select(pkEnvioHashtable);
			}
			
			//Obtenemos los remitentes
			Vector<EnvRemitentesBean> envRemitentesVector = envRemitentesAdm.select(pkEnvioHashtable);
			
			//Obtenemos los docuemntos
			Vector<EnvDocumentosBean> envDocumentosVector = envDocumentosAdm.select(pkEnvioHashtable);
			
			//Obtenemos las comunicaciones de designa o ejg que ha realizado esta comunicacion
			List<ScsComunicaciones> scsComunicaciones = salidaEnviosService.getComunicaciones(idOldEnvio.longValue(), idInstitucion);
			
			tx = this.usrbean.getTransaction();
			tx.begin();
			
			idNewEnvio = getNewIdEnvio(idInstitucion.toString());
			envioBean.setIdEnvio(idNewEnvio);
			
			
			//Insertamos el nuevo envio
			insert(envioBean);
			
			//Recorremos e insertamos
			for (EnvCamposEnviosBean camposEnviosBean : envCamposEnviosVector) {
				camposEnviosBean.setIdEnvio(idNewEnvio);
				envCamposEnviosAdm.insert(camposEnviosBean);
				
			}
			if(envDestinatariosVector!=null){
				//Recorremos e insertamos
				for (EnvDestinatariosBean envDestinatariosBean : envDestinatariosVector) {
					envDestinatariosBean.setIdEnvio(idNewEnvio);
					envDestinatariosBean.setIdEstado(null);
					envDestinatariosAdm.insert(envDestinatariosBean);
				}
			}
			//Recorremos e insertamos
			if(envListaCorreosEnviosVector != null){
				for (EnvListaCorreosEnviosBean envListaCorreosEnviosBean : envListaCorreosEnviosVector) {
					envListaCorreosEnviosBean.setIdEnvio(idNewEnvio);
					envListaCorreosEnviosAdm.insert(envListaCorreosEnviosBean);
				}
			}
			//Recorremos e insertamos
			for (EnvRemitentesBean envRemitentesBean : envRemitentesVector) {
				envRemitentesBean.setIdEnvio(idNewEnvio);
				envRemitentesAdm.insert(envRemitentesBean);
			}
			//Recorremos e insertamos
			for (EnvDocumentosBean envDocumentosBean : envDocumentosVector) {
				envDocumentosBean.setIdEnvio(idNewEnvio);
				envDocumentosAdm.insert(envDocumentosBean);
				duplicarDocumento(idInstitucion, idNewEnvio, envDocumentosBean.getIdDocumento(), idOldEnvio);
			}
			//Recorremos e insertamos
			for (ScsComunicaciones scsComunicacion : scsComunicaciones) {
				scsComunicacion.setIdenviosalida(idNewEnvio.longValue());
				this.insertarComunicacionSalida(scsComunicacion,usrbean);
			}
			
			tx.commit();
			
			
		} catch (ClsExceptions e) {
			try {
				if(tx!=null)
					tx.rollback();
				ClsLogging.writeFileLogError("Error al reenviar el mensaje",e,10);
				throw new SIGAException("general.error.noDisponible");
			} catch (Exception ex) {
				ClsLogging.writeFileLogError("Error al reenviar el mensaje",ex,10);
				throw new SIGAException("general.error.noDisponible");	
			}
			
		} catch (Exception e) {
			try {
				if(tx!=null)
					tx.rollback();
				ClsLogging.writeFileLogError("Error al reenviar el mensaje",e,10);
				throw new SIGAException("general.error.noDisponible");
			} catch (Exception ex) {
				ClsLogging.writeFileLogError("Error al reenviar el mensaje",ex,10);
				throw new SIGAException("general.error.noDisponible");	
			}
			
		}
		return idNewEnvio;
		
	}
	private void duplicarDocumento(Short idInstitucion,Integer idNewEnvio,Integer idDocumento, Integer idOldEnvio)throws SIGAException{
		StringBuilder newNombreDocumento = new StringBuilder();
		newNombreDocumento.append(idInstitucion);
		newNombreDocumento.append("_");
		newNombreDocumento.append(idNewEnvio);
		newNombreDocumento.append("_");
		newNombreDocumento.append(idDocumento);
		StringBuilder oldNombreDocumento = new StringBuilder();
		oldNombreDocumento.append(idInstitucion);
		oldNombreDocumento.append("_");
		oldNombreDocumento.append(idOldEnvio);
		oldNombreDocumento.append("_");
		oldNombreDocumento.append(idDocumento);
		
		
		
    	try {
    		String newPathDirectorio = getPathEnvio(idInstitucion.toString(),idNewEnvio.toString());
			FileHelper.mkdirs(newPathDirectorio);
    		String oldPathDirectorio = getPathEnvio(idInstitucion.toString(),idOldEnvio.toString());
    		
    		
    		StringBuilder oldPathDocumento = new StringBuilder(oldPathDirectorio);
    		oldPathDocumento.append(File.separator);
    		oldPathDocumento.append(oldNombreDocumento);

    		SIGAServicesHelper.copyFile(new File(oldPathDocumento.toString()), newPathDirectorio.toString(), newNombreDocumento.toString());
    		
		} catch (Exception e) {
			ClsLogging.writeFileLogError("Error al duplicarDocumento: " + e.getMessage(), e, 10);
			throw new SIGAException("general.error.noDisponible");
		}
    	
    	
		
		
	}
	/**
	 * 
	 * @param idEnvio
	 * @param idInstitucion
	 * @return devuelve el nuevo idEnvio generado
	 * @throws SIGAException
	 */
	public Integer enviarDeNuevo(Integer idEnvio,Short idInstitucion) throws SIGAException{
		
		EnvDestinatariosAdm envDestinatariosAdm = new EnvDestinatariosAdm(this.usrbean);

		try {
			Hashtable pkEnvioHashtable = new Hashtable();
			pkEnvioHashtable.put(EnvEnviosBean.C_IDINSTITUCION,idInstitucion);
			pkEnvioHashtable.put(EnvEnviosBean.C_IDENVIO,idEnvio);
			//Obtenemos todos los destinatario individuales del envio
			pkEnvioHashtable.put(EnvDestinatariosBean.C_ORIGENDESTINATARIO, EnvDestinatariosBean.ORIGENDESTINATARIO_INDIVIDUAL);
			Vector<EnvDestinatariosBean> envDestinatariosVector = envDestinatariosAdm.select(pkEnvioHashtable);
			return reenviar(idEnvio, idInstitucion, envDestinatariosVector);
		} catch (ClsExceptions e) {
			ClsLogging.writeFileLogError("Error al reenviar el mensaje",e,10);
			throw new SIGAException("general.error.noDisponible");
		}
		
	} 
    
}
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.TreeMap;
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

import service.ServiciosECOS.ServiciosECOSServiceSOAPStub;
import service.ServiciosECOS.ServiciosECOSService_ServiceLocator;

import com.aspose.words.Document;
import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.ExceptionManager;
import com.atos.utils.GstDate;
import com.atos.utils.ReadProperties;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.ecos.ws.solicitarEnvio.DatosGrandeCuentaTO;
import com.ecos.ws.solicitarEnvio.ResultadoSolicitudEnvio;
import com.ecos.ws.solicitarEnvio.SolicitudEnvioSMS;
import com.siga.Utilidades.Paginador;
import com.siga.Utilidades.SIGAReferences;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.certificados.Plantilla;
import com.siga.consultas.CriterioDinamico;
import com.siga.envios.UsuarioFax;
import com.siga.envios.ZetaFax;
import com.siga.envios.form.ImagenPlantillaForm;
import com.siga.general.EjecucionPLs;
import com.siga.general.SIGAException;
import com.siga.informes.MasterWords;
import com.sun.mail.smtp.SMTPAddressFailedException;
import com.siga.envios.Envio;


public class EnvEnviosAdm extends MasterBeanAdministrador {

  
  public final static int ESTADO_INICIAL = 1;
  public final static int ESTADO_PROCESADO = 2;
  public final static int ESTADO_PROCESADO_ERRORES= 3;
  public final static int ESTADO_PENDIENTE_AUTOMATICO = 4;

  public final static int TIPO_CORREO_ELECTRONICO = 1;
  public final static int TIPO_CORREO_ORDINARIO = 2;
  public final static int TIPO_FAX = 3;
  public final static int TIPO_SMS = 4;
  public final static int TIPO_BUROSMS = 5;

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
            	EnvEnviosBean.C_CONSULTA,
            	EnvEnviosBean.C_ACUSERECIBO,
            	EnvEnviosBean.C_FECHAMODIFICACION,
            	EnvEnviosBean.C_USUMODIFICACION
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
			bean.setConsulta(UtilidadesHash.getString(hash, EnvEnviosBean.C_CONSULTA));
			bean.setAcuseRecibo(UtilidadesHash.getString(hash, EnvEnviosBean.C_ACUSERECIBO));

		}

		catch (Exception e)
		{
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
			UtilidadesHash.set(htData, EnvEnviosBean.C_CONSULTA, b.getConsulta());
			UtilidadesHash.set(htData, EnvEnviosBean.C_ACUSERECIBO, b.getAcuseRecibo());

		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}

    public Integer getNewIdEnvio(String idInstitucion) throws ClsExceptions{
        Long idEnvio = getSecuenciaNextVal(EnvEnviosBean.SEQ_ENV_ENVIOS);
        SimpleDateFormat formato = new SimpleDateFormat("yyyy");
        return new Integer(formato.format(new Date())+idEnvio);
    }

    public Integer getNewIdEnvio(UsrBean usrBean) throws ClsExceptions{
    	return getNewIdEnvio(usrBean.getLocation());
    }

/**
 * @author juan.grau
 *
 * Permite la búsqueda de envíos insertados.
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
            				   String idTipoEnvios, String idInstitucion)
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
		    fecha = EN_FECHACREACION;
		    fechaIsNull = "";
		} else {
		    fecha = EN_FECHAPROGRAMADA;
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

			String sql = "SELECT ";

		    sql += EN_IDENVIO + ", ";
		    sql += EN_DESCRIPCION + ", ";
		    sql += EN_FECHACREACION + ", ";
		    sql += EN_FECHAPROGRAMADA + ", ";
		    sql += ES_NOMBRE + " AS ESTADO, ";
		    sql += TI_IDTIPOENVIOS + ", ";
		    sql += EN_IDESTADO + ", ";
		    sql += TI_NOMBRE + " AS TIPOENVIO";

			sql += " FROM ";
		    sql += T_ENV_ENVIOS + ", " +
		    	   T_ENV_ESTADOENVIO + ", " +
		    	   T_ENV_TIPOENVIOS;

		    sql += " WHERE ";
		    sql += EN_IDINSTITUCION + " = " + idInstitucion.toString();
		    sql += (nombre!=null && !nombre.equals("")) ? " AND "+ComodinBusquedas.prepararSentenciaCompleta(nombre.trim(),EN_DESCRIPCION ) : "";
			if (dFechaDesde!=null || dFechaHasta!=null)
			    sql += " AND " + GstDate.dateBetweenDesdeAndHasta(fecha,dFechaDesde,dFechaHasta);
			sql += " AND " + EN_IDESTADO + " = " + ES_IDESTADO;
			sql += (idEstado!=null && !idEstado.equals("")) ? " AND " + ES_IDESTADO +" = "+ idEstado : "";
			sql += " AND " + EN_IDTIPOENVIOS + " = " + TI_IDTIPOENVIOS;
			sql += (idTipoEnvios!=null && !idTipoEnvios.equals("")) ? " AND " + EN_IDTIPOENVIOS +" = "+ idTipoEnvios : "";
			sql += (idEnvio!=null && !idEnvio.equals("")) ? " AND " + EN_IDENVIO +" = "+ idEnvio : "";

			sql += fechaIsNull;

			// RGG CAMBIO DE ORDEN sql += " ORDER BY " + EN_DESCRIPCION;
			sql += " ORDER BY " + EN_FECHACREACION + " DESC";

			ClsLogging.writeFileLog("EnvEnviosAdm -> QUERY: "+sql,10);

			/*if (rc.queryNLS(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					datos.add(fila);
				}
			}*/
			 Paginador paginador = new Paginador(sql);				
				int totalRegistros = paginador.getNumeroTotalRegistros();
				
				if (totalRegistros==0){					
					paginador =null;
				}
				return paginador;
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D.");
		}
		
    }


    /**
     * @author juan.grau
     *
     * Permite la búsqueda de destinatarios manuales.
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
			sql.append(" (SELECT  D.NOMBRE || ' ' || D.APELLIDOS1 || ' ' || D.APELLIDOS2 AS NOMBREYAPELLIDOS,D.NIFCIF,null,D.IDPERSONA ");
			sql.append(", D.TIPODESTINATARIO");
			sql.append(" FROM ENV_DESTINATARIOS D ");
			sql.append(" WHERE D.IDINSTITUCION = ");
			sql.append(idInstitucion);
			sql.append(" AND D.IDENVIO = ");
			sql.append(idEnvio);
			sql.append(" AND D.Tipodestinatario ='");
			sql.append(EnvDestinatariosBean.TIPODESTINATARIO_SCSPERSONAJG);
			sql.append("') ");
			
			sql.append(" UNION ");
			sql.append(" (SELECT  D.NOMBRE || ' ' || D.APELLIDOS1 || ' ' || D.APELLIDOS2 AS NOMBREYAPELLIDOS,D.NIFCIF,null,D.IDPERSONA ");
			sql.append(", D.TIPODESTINATARIO");
			sql.append(" FROM ENV_DESTINATARIOS D ");
			sql.append(" WHERE D.IDINSTITUCION = ");
			sql.append(idInstitucion);
			sql.append(" AND D.IDENVIO = ");
			sql.append(idEnvio);
			sql.append(" AND D.Tipodestinatario ='");
			sql.append(EnvDestinatariosBean.TIPODESTINATARIO_SCSPROCURADOR);
			sql.append("') ");

			sql.append(" UNION ");
			sql.append(" (SELECT  D.NOMBRE || ' ' || D.APELLIDOS1 || ' ' || D.APELLIDOS2 AS NOMBREYAPELLIDOS,D.NIFCIF,null,D.IDPERSONA ");
			sql.append(", D.TIPODESTINATARIO");
			sql.append(" FROM ENV_DESTINATARIOS D ");
			sql.append(" WHERE D.IDINSTITUCION = ");
			sql.append(idInstitucion);
			sql.append(" AND D.IDENVIO = ");
			sql.append(idEnvio);
			sql.append(" AND D.Tipodestinatario ='");
			sql.append(EnvDestinatariosBean.TIPODESTINATARIO_SCSJUZGADO);
			sql.append("') ");
			
			sql.append(") ORDER BY NOMBREYAPELLIDOS ");   
			
			//ClsLogging.writeFileLog("EnvEnviosAdm.getDestinatariosManuales -> QUERY: "+sql,3);
			
			if (rc.query(sql.toString())) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					datos.add(fila);
				}
			}
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D.");
		}
		return datos;
    }


    /**
     * @author juan.grau
     *
     * Permite la búsqueda de listas destinatarias.
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
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D.");
		}
		return datos;
    }

    /**
     * @author juan.grau
     *
     * Permite la búsqueda de remitentes manuales.
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
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D.");
		}
		return datos;
    }

    /**
     * @author juan.grau
     *
     * Permite la búsqueda de direcciones de una persona y de un tipo .
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
        case TIPO_CORREO_ELECTRONICO:
            for (int i=0;i<direcciones.size();i++){
     		    Hashtable htDir = (Hashtable)direcciones.get(i);
     		    String correoElectronico = (String)htDir.get(CenDireccionesBean.C_CORREOELECTRONICO);
                if (correoElectronico!=null && !correoElectronico.equals("")){
                	String preferente = (String)htDir.get(CenDireccionesBean.C_PREFERENTE); 
         		    if(preferente!=null){
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
        case TIPO_CORREO_ORDINARIO:
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
        case TIPO_SMS:
        case TIPO_BUROSMS:
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
        case TIPO_FAX:
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

        //Borramos los campos de un envío
        Hashtable ht = new Hashtable();
        ht.put(EnvCamposEnviosBean.C_IDINSTITUCION,idInstitucion);
        ht.put(EnvCamposEnviosBean.C_IDENVIO,id);
        String[] campos = {EnvCamposEnviosBean.C_IDINSTITUCION,
                			EnvCamposEnviosBean.C_IDENVIO};
        ceAdm.deleteDirect(ht,campos);

        //Copiamos los campos de la plantilla al envío
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
//	            ceBean.setValor("Pepe");	            
	            ceBean.setValor(cpBean.getValor());	            
	            
	            ceAdm.insert(ceBean);
	        }
        }

        // Copiamos los remitentes de la plantilla
        try {
        	this.copiarRemitentesDesdePlantilla(idInstitucion, id, idTipoEnvio, idPlantilla);
        }
        catch (Exception e) {
        	throw new ClsExceptions(e, "Error al copiar remitentes en el envio");
        }
    }
    public void copiarCamposPlantilla(Integer idInstitucion, Integer id, Integer idTipoEnvio, Integer idPlantilla,Object bean) throws ClsExceptions, SIGAException 
	{
        EnvCamposPlantillaAdm cpAdm = new EnvCamposPlantillaAdm(this.usrbean);
        EnvCamposEnviosAdm ceAdm = new EnvCamposEnviosAdm(this.usrbean);

        //Borramos los campos de un envío
        Hashtable ht = new Hashtable();
        ht.put(EnvCamposEnviosBean.C_IDINSTITUCION,idInstitucion);
        ht.put(EnvCamposEnviosBean.C_IDENVIO,id);
        String[] campos = {EnvCamposEnviosBean.C_IDINSTITUCION,
                			EnvCamposEnviosBean.C_IDENVIO};
        ceAdm.deleteDirect(ht,campos);

        //Copiamos los campos de la plantilla al envío
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

							}
	    	    			htDatosEnvio.put("EXP_TIPO", expAlertaBean.getTipoExpediente().getNombre());
	    	    			htDatosEnvio.put("EXP_FASE", expAlertaBean.getFase().getNombre());
	    	    			htDatosEnvio.put("EXP_ESTADO", expAlertaBean.getEstado().getNombre());
	    	    			
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

								
							}
	    	    			htDatosEnvio.put("EXP_TIPO", expAlertaBean.getTipoExpediente().getNombre());
	    	    			htDatosEnvio.put("EXP_FASE", expAlertaBean.getFase().getNombre());
	    	    			htDatosEnvio.put("EXP_ESTADO", expAlertaBean.getEstado().getNombre());
	    	    				
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
	    	    			htDatosEnvio.put("EJG_NU",(String) ejgHashtable.get("NUMERO_EJG"));
	    	    			if(ejgHashtable.get("N_APELLI_1_LETRADO_DESIGNADO")!=null)
	    	    				htDatosEnvio.put("EJG_LETRADODES_NOMBRE_APELLI_1",(String) ejgHashtable.get("N_APELLI_1_LETRADO_DESIGNADO"));
	    	    			else
	    	    				htDatosEnvio.put("EJG_LETRADODES_NOMBRE_APELLI_1","");
	    	    			if(ejgHashtable.get("N_APEL_1_2_LETRADO_DESIGNADO")!=null)
	    	    				htDatosEnvio.put("EJG_LETRADODES_NOMBRE_APEL_1_2",(String) ejgHashtable.get("N_APEL_1_2_LETRADO_DESIGNADO"));
	    	    			else
	    	    				htDatosEnvio.put("EJG_LETRADODES_NOMBRE_APEL_1_2","");
	    	    			if(ejgHashtable.get("APEL_1_2_N_LETRADO_DESIGNADO")!=null)
	    	    				htDatosEnvio.put("EJG_LETRADODES_APEL_1_2_NOMBRE",(String) ejgHashtable.get("APEL_1_2_N_LETRADO_DESIGNADO"));
	    	    			else
	    	    				htDatosEnvio.put("EJG_LETRADODES_APEL_1_2_NOMBRE","");
	    	    			if(ejgHashtable.get("TELEFONODESPACHO_LET_DESIGNADO")!=null){
	    	    				String telefonoDespacho =  (String) ejgHashtable.get("TELEFONODESPACHO_LET_DESIGNADO");
	    	    				if(telefonoDespacho.length()>13)
	    	    					telefonoDespacho = telefonoDespacho.substring(0,13);
	    	    				htDatosEnvio.put("EJG_TLFNO", telefonoDespacho);
	    	    			}else
	    	    				htDatosEnvio.put("EJG_TLFNO", "");
	    	    			if(ejgHashtable.get("DESC_TIPODICTAMENEJG")!=null){
	    	    				String dictamenEJG =  (String) ejgHashtable.get("DESC_TIPODICTAMENEJG");
	    	    				if(dictamenEJG.length()>10)
	    	    					dictamenEJG = dictamenEJG.substring(0,10);
	    	    				
	    	    				htDatosEnvio.put("EJG_DICTAM",dictamenEJG);
	    	    			}else{
	    	    				htDatosEnvio.put("EJG_DICTAM","");
	    	    				
	    	    				
	    	    			}
	    	    			
	    	    			
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
        	throw new ClsExceptions(e, "Error al copiar remitentes en el envio");
        }
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
            throw e1;
        } 
        catch (Exception e1) {
            throw new SIGAException("Error al añadir los remitentes por defecto",e1);
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
	    	throw new ClsExceptions(e1,e1.getMessage());
	    }
	    String anio = String.valueOf(cal.get(Calendar.YEAR));
	    String mes = String.valueOf(cal.get(Calendar.MONTH)+1);
	
	    EnvDocumentosDestinatariosAdm docAdm = new EnvDocumentosDestinatariosAdm(this.usrbean);
	    String pathDoc;
		try {
	        pathDoc = docAdm.getPathDocumentosFromDB();
	    } catch (ClsExceptions e2) {
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
	    	throw new ClsExceptions(e1,e1.getMessage());
	    }
	    String anio = String.valueOf(cal.get(Calendar.YEAR));
	    String mes = String.valueOf(cal.get(Calendar.MONTH)+1);
	
	    EnvDocumentosDestinatariosAdm docAdm = new EnvDocumentosDestinatariosAdm(this.usrbean);
	    String pathDoc;
		try {
	        pathDoc = docAdm.getPathDocumentosFromDB();
	    } catch (ClsExceptions e2) {
	    	throw e2;
	    }
	
	    //String sDirectorio = [PATH_DOCUMENTOSADJUNTOS/idInstitucion/AAAA/MM/idEnvio
	    String sDirectorio = pathDoc + File.separator +
							 envioBean.getIdInstitucion().toString() + File.separator +
							 anio + File.separator +
							 mes + File.separator + envioBean.getIdEnvio().toString();
	
	    return sDirectorio;
	}
	
    public void borrarEnvio(String idInstitucion, String idEnvio,String idTipoEnvio,UsrBean userBean)
    	throws Exception{

       
    	UserTransaction tx = userBean.getTransaction();
		try {
			String sql="select * from env_envios  e " +
			"  where e.idenvio in (select t.idenviodoc from env_comunicacionmorosos t" +
			"                      where idinstitucion=" +userBean.getLocation()+
			"                      and idenviodoc="+idEnvio+")"+
			"  and idinstitucion="+userBean.getLocation();
	
			Vector resultado = (Vector)selectGenerico(sql);
			
			EnvEstatEnvioAdm estatEnvioAdm = new EnvEstatEnvioAdm (usrbean);
			
			tx.begin();
	
			if (resultado!=null && resultado.size()>=1){ 
				EnvComunicacionMorososAdm admComunicaMorosos = new EnvComunicacionMorososAdm(userBean);
				Vector  morososVector = admComunicaMorosos.getEnvioMorosos(idInstitucion, idEnvio);
				for (int i = 0; i < morososVector.size(); i++) {
					Hashtable htEnvioMorosos = (Hashtable)morososVector.get(i);
					TreeMap<Long, Hashtable> tmIdEnviosAActualizar = getActualizacionIdEnviosMorosos(htEnvioMorosos);
					if(htEnvioMorosos!=null)
						borraReferenciaMorosos(idInstitucion,idEnvio,htEnvioMorosos,tmIdEnviosAActualizar);
				
				}	
		
		
			}
			Hashtable htEnvio = new Hashtable();
			htEnvio.put(EnvEnviosBean.C_IDINSTITUCION, idInstitucion);
			htEnvio.put(EnvEnviosBean.C_IDENVIO, idEnvio);
			String rutaDocus = getPathEnvio(idInstitucion,idEnvio);
			// Borramos los registros de BBDD
			delete(htEnvio);
			estatEnvioAdm.borrarEnvio(idInstitucion,idEnvio, idTipoEnvio);
			
			File fDir = new File(rutaDocus);
			borrarDirectorio(fDir);
			tx.commit();

        } catch (Exception e) {	                
        	try {
    			if (tx!=null) {
    				tx.rollback();
    			}
    		} catch (Exception el) {
    		}
    		throw e;
    		
        }
    }
    private void borraReferenciaMorosos(String idInstitucion, String idEnvio,
    		Hashtable htEnvioMoroso,TreeMap tmEnviosMorososAActualizar)throws Exception{
    	EnvComunicacionMorososAdm admComunicaMorosos = new EnvComunicacionMorososAdm(this.usrbean);
        
        
        admComunicaMorosos.delete(htEnvioMoroso);

        if(tmEnviosMorososAActualizar!=null && tmEnviosMorososAActualizar.size()>0){
        	 Iterator itComunicacionMorosos = tmEnviosMorososAActualizar.keySet().iterator();
        	int idEnvioActualizado = 1;
        	 while (itComunicacionMorosos.hasNext()) {
        		Long key = (Long) itComunicacionMorosos.next();
        		Hashtable comunicacionMorosos = (Hashtable) tmEnviosMorososAActualizar.get(key);
        		Hashtable comunicacionMorososOld = (Hashtable)comunicacionMorosos.clone();
        		UtilidadesHash.set(comunicacionMorosos, EnvComunicacionMorososBean.C_IDENVIO, ""+idEnvioActualizado);
        		if(!UtilidadesHash.getString(comunicacionMorosos, EnvComunicacionMorososBean.C_IDENVIO).equals(UtilidadesHash.getString(comunicacionMorososOld, EnvComunicacionMorososBean.C_IDENVIO))){
        			admComunicaMorosos.delete(comunicacionMorososOld);
        			admComunicaMorosos.insert(comunicacionMorosos);
        		}
        		idEnvioActualizado++;
        	}	
        }
        
        
    }
    /**
     * Devolvemos un Treemap con los envios que se han realizado para la factura/persona/insntitucion
     * actualizando su idEnvio del envio que se quiere eliminar
     * Devolveremos null en el caso que el envio a borrar sea el unico o el ultimo
     * @param htEnvioMoroso
     * @return
     * @throws Exception
     */
    public TreeMap<Long,Hashtable> getActualizacionIdEnviosMorosos(Hashtable htEnvioMoroso)throws Exception{
    	EnvComunicacionMorososAdm admComunicaMorosos = new EnvComunicacionMorososAdm(this.usrbean);
        TreeMap tmComunicacionMorosos = admComunicaMorosos.getComunicacionesMorosos(htEnvioMoroso);
        StringBuffer key = new StringBuffer();
        key.append(UtilidadesHash.getString(htEnvioMoroso, EnvComunicacionMorososBean.C_IDINSTITUCION));
        key.append(UtilidadesHash.getString(htEnvioMoroso, EnvComunicacionMorososBean.C_IDPERSONA));
        key.append(UtilidadesHash.getString(htEnvioMoroso, EnvComunicacionMorososBean.C_IDFACTURA));
        key.append(UtilidadesHash.getString(htEnvioMoroso, EnvComunicacionMorososBean.C_IDENVIO));
        //si solo hay uno se va es el que se va a eliminar luego no hay que actualizar nada.
        //Asi mismo si se elimina el ultimo envio tampoco actualiaremos ids
        if(tmComunicacionMorosos.size()>1 && !(tmComunicacionMorosos.lastKey().toString()).equals(key.toString())){
        	//Borramos el envio de moroso asociado al envio
        	tmComunicacionMorosos.remove(Long.valueOf(key.toString()));
	    	
        }else{
        	tmComunicacionMorosos = null;
        	
        }
        return tmComunicacionMorosos;
    }
    public void borrarDirectorio(File fDir){
        if (fDir.exists()){
            File [] files = fDir.listFiles();
            for (int i=0;i<files.length;i++){
                if (files[i].isDirectory()){
                    borrarDirectorio(files[i]);
                } else {
                    files[i].delete();
                }
                fDir.delete();
            }
        }
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
	            //el campo existía, lo actualizamos.
	            camposEnviosBean = (EnvCamposEnviosBean)camposEnviosAdm.selectByPK(htPk).firstElement();
	            camposEnviosBean.setValor(sAsunto);
	            
	            camposEnviosAdm.update(camposEnviosBean);
	        } else {
	            //el campo no existía, lo creamos.
	            camposEnviosBean = new EnvCamposEnviosBean();
	            camposEnviosBean.setIdEnvio(idEnvio);
	            camposEnviosBean.setIdInstitucion(idInstitucion);
	            camposEnviosBean.setIdCampo(Integer.valueOf(EnvCamposPlantillaAdm.K_IDCAMPO_ASUNTO));
	            camposEnviosBean.setTipoCampo(EnvCamposAdm.K_TIPOCAMPO_E);
	            camposEnviosBean.setValor(sAsunto);
	            
	            camposEnviosAdm.insert(camposEnviosBean);
	        }
	    } catch (ClsExceptions e) {
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
	            //el campo existía, lo actualizamos.
	            camposEnviosBean = (EnvCamposEnviosBean)camposEnviosAdm.selectByPK(htPk).firstElement();
	            camposEnviosBean.setValor(sCuerpo);
	            
	            camposEnviosAdm.update(camposEnviosBean);
	        } else {
	            //el campo no existía, lo creamos.
	            camposEnviosBean = new EnvCamposEnviosBean();
	            camposEnviosBean.setIdEnvio(idEnvio);
	            camposEnviosBean.setIdInstitucion(idInstitucion);
	            camposEnviosBean.setIdCampo(Integer.valueOf(EnvCamposPlantillaAdm.K_IDCAMPO_CUERPO));
	            camposEnviosBean.setTipoCampo(EnvCamposAdm.K_TIPOCAMPO_E);
	            camposEnviosBean.setValor(sCuerpo);
	            
	            camposEnviosAdm.insert(camposEnviosBean);
	        }
	    } catch (ClsExceptions e) {
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
	            //el campo existía, lo actualizamos.
	            camposEnviosBean = (EnvCamposEnviosBean)camposEnviosAdm.selectByPK(htPk).firstElement();
	            camposEnviosBean.setValor(sCuerpo);
	            
	            camposEnviosAdm.update(camposEnviosBean);
	        } else {
	            //el campo no existía, lo creamos.
	            camposEnviosBean = new EnvCamposEnviosBean();
	            camposEnviosBean.setIdEnvio(idEnvio);
	            camposEnviosBean.setIdInstitucion(idInstitucion);
	            camposEnviosBean.setIdCampo(Integer.valueOf(EnvCamposPlantillaAdm.K_IDCAMPO_SMS));
	            camposEnviosBean.setTipoCampo(EnvCamposAdm.K_TIPOCAMPO_S);
	            camposEnviosBean.setValor(sCuerpo);
	            
	            camposEnviosAdm.insert(camposEnviosBean);
	        }
	    } catch (ClsExceptions e) {
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
			    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
				//ReadProperties rp = new ReadProperties("SIGA.properties");
				if(CenPersonaAdm.K_PERSONA_GENERICA.equals(idPersona)){
		        	sSQL = rp.returnProperty("envios.consulta.sinPersona");
				}else{
					if(tipoDestinatario!=null&&tipoDestinatario.equals(EnvDestinatariosBean.TIPODESTINATARIO_SCSPERSONAJG)){
						sSQL = rp.returnProperty("envios.consulta.conPersonaJG");
					}else if(tipoDestinatario!=null&&tipoDestinatario.equals(EnvDestinatariosBean.TIPODESTINATARIO_SCSJUZGADO)){
						sSQL = rp.returnProperty("envios.consulta.conJuzgado");						
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
            throw e;
		}
		catch(Exception e)
		{
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
		                    throw new ClsExceptions(e, "Error al dar formato numérico");
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
	        throw new ClsExceptions(e, "Error al dar formato a los campos");
	    }

	    return htDatos;
	}
	
	public Vector getDestinatarios(String idInstitucion, String idEnvio, String idTipoEnvio) 
		throws SIGAException, ClsExceptions{
	    
	    Vector vDestTotales;
	    
	    Vector vManNoDin;
	    
	    Vector vDestRows = new Vector();
	    Vector vDestManuales = new Vector();
	    Vector vDestListaNoDinamicaBeans = new Vector();
	    Hashtable htDirec = new Hashtable();
	    
	    // Selección de destinatarios manuales
	    
	    Hashtable htPk = new Hashtable();
	    htPk.put(EnvDestinatariosBean.C_IDINSTITUCION,idInstitucion);
	    htPk.put(EnvDestinatariosBean.C_IDENVIO,idEnvio);
	  //FIXME  AQUI SE LLAMA 1
	    EnvDestinatariosAdm destAdm = new EnvDestinatariosAdm(this.usrbean);
	    try {
            vDestManuales = destAdm.select(htPk);
        } catch (ClsExceptions e1) {
            throw new ClsExceptions(e1,"Error obteniendo los destinatarios del envio");
        }
	    
	    
	    //NOMBRES TABLAS PARA LA JOIN
		String T_ENV_ENVIOS = EnvEnviosBean.T_NOMBRETABLA + " EN";
		String T_ENV_LISTACORREOSENVIOS = EnvListaCorreosEnviosBean.T_NOMBRETABLA + " LE";
		String T_ENV_LISTACORREOS = EnvListaCorreosBean.T_NOMBRETABLA + " LI";
		String T_ENV_COMPONENTESLISTACORREO = EnvComponentesListaCorreoBean.T_NOMBRETABLA + " CL";
		String T_ENV_LISTACORREOCONSULTA = EnvListaCorreoConsultaBean.T_NOMBRETABLA + " LC";
		String T_CEN_DIRECCIONES = CenDireccionesBean.T_NOMBRETABLA + " DI";
		String T_CEN_TIPODIRECCIONES = CenDireccionTipoDireccionBean.T_NOMBRETABLA + " TD";
		String T_CON_CONSULTA = ConConsultaBean.T_NOMBRETABLA + " C";
		
		//Tabla env_Envios
		String EN_IDINSTITUCION = "EN." + EnvEnviosBean.C_IDINSTITUCION;
		String EN_IDENVIO = "EN." + EnvEnviosBean.C_IDENVIO;
		
		//Tabla env_ListaCorreosEnvios
		String LE_IDINSTITUCION = "LE." + EnvListaCorreosEnviosBean.C_IDINSTITUCION;
		String LE_IDENVIO = "LE." + EnvListaCorreosEnviosBean.C_IDENVIO;
		String LE_IDLISTACORREO = "LE." + EnvListaCorreosEnviosBean.C_IDLISTACORREO;
		
		//Tabla env_ListaCorreos
		String LI_IDINSTITUCION = "LI." + EnvListaCorreosBean.C_IDINSTITUCION;
		String LI_IDLISTACORREO = "LI." + EnvListaCorreosBean.C_IDLISTACORREO;
		String LI_DINAMICA = "LI." + EnvListaCorreosBean.C_DINAMICA;
		
		//Tabla env_ComponentesListaCorreos
		String CL_IDINSTITUCION = "CL." + EnvComponentesListaCorreoBean.C_IDINSTITUCION;
		String CL_IDLISTACORREO = "CL." + EnvComponentesListaCorreoBean.C_IDLISTACORREO;
		String CL_IDPERSONA = "CL." + EnvComponentesListaCorreoBean.C_IDPERSONA;
		
		//Tabla env_ListaCorreoConsulta
		String LC_IDINSTITUCION = "LC." + EnvListaCorreoConsultaBean.C_IDINSTITUCION;
		String LC_IDLISTACORREO = "LC." + EnvListaCorreoConsultaBean.C_IDLISTACORREO;
		String LC_IDCONSULTA = "LC." + EnvListaCorreoConsultaBean.C_IDCONSULTA;
		String LC_IDINSTITUCION_CON = "LC." + EnvListaCorreoConsultaBean.C_IDINSTITUCION_CON;
		
		//Tabla con_Consulta
		String C_IDINSTITUCION = "C." + ConConsultaBean.C_IDINSTITUCION;
		String C_IDCONSULTA = "C." + ConConsultaBean.C_IDCONSULTA;
		String C_SENTENCIA = "C." + ConConsultaBean.C_SENTENCIA;
		String C_ESEXPERTA = "C." + ConConsultaBean.C_ESEXPERTA;
		
		//Tabla cen_Direcciones
		String DI_IDINSTITUCION = "DI." + CenDireccionesBean.C_IDINSTITUCION;
		String DI_IDPERSONA = "DI." + CenDireccionesBean.C_IDPERSONA;
		String DI_IDDIRECCION = "DI." + CenDireccionesBean.C_IDDIRECCION;
		String DI_FECHABAJA = "DI." + CenDireccionesBean.C_FECHABAJA;
		
		//Tabla cen_Direcciones_tipodireccion
		String TD_IDINSTITUCION = "TD." + CenDireccionTipoDireccionBean.C_IDINSTITUCION;
		String TD_IDPERSONA = "TD." + CenDireccionTipoDireccionBean.C_IDPERSONA;
		String TD_IDDIRECCION = "TD." + CenDireccionTipoDireccionBean.C_IDDIRECCION;
		String TD_IDTIPODIRECCION = "TD." + CenDireccionTipoDireccionBean.C_IDTIPODIRECCION;
		String sql ="";
		//*********** Seleccion de destinatarios de lista no dinamica***********
	    
		// Acceso a BBDD
		RowsContainer rcDes = null;
		try {
		    rcDes = new RowsContainer();

		     sql =
		    	"SELECT DISTINCT * FROM "+
		    	T_ENV_ENVIOS + ", " +
		    	T_ENV_LISTACORREOSENVIOS + ", " +
		    	T_ENV_LISTACORREOS + ", " +
		    	T_ENV_COMPONENTESLISTACORREO+
		    	" WHERE "+EN_IDINSTITUCION + " = " + idInstitucion+
		    	" AND " + EN_IDENVIO + " = " + idEnvio+
		    	" AND " + EN_IDINSTITUCION + " = " + LE_IDINSTITUCION+
		    	" AND " + EN_IDENVIO + " = " + LE_IDENVIO+
		    	" AND " + LE_IDINSTITUCION + " = " + LI_IDINSTITUCION+
		    	" AND " + LI_IDINSTITUCION + " = " + CL_IDINSTITUCION+
		    	" AND " + LE_IDLISTACORREO + " = " + LI_IDLISTACORREO+
		    	" AND " + LI_IDLISTACORREO + " = " + CL_IDLISTACORREO;

			ClsLogging.writeFileLog("EnvEnviosAdm.getDestinatarios.ListaNoDin -> QUERY: "+sql,10);

			if (rcDes.query(sql)) {
				for (int i = 0; i < rcDes.size(); i++)	{
					Row fila = (Row) rcDes.get(i);
					vDestRows.add(fila);
				}
			}
			
		}catch(Exception e){
//	        throw new SIGAException("messages.envios.error.obtenciondestinatarios",e);
            throw new ClsExceptions(e,"Error obteniendo los destinatarios del envio");

	    }
		
				
		//*********** Seleccion de direcciones ************
		
		//Acceso a BBDD
		RowsContainer rcDir = null;
		
		try {
			rcDir = new RowsContainer();

			 sql =
				"SELECT DISTINCT * FROM "+
				T_ENV_ENVIOS + ", " +
				T_ENV_LISTACORREOSENVIOS + ", " +
				T_ENV_LISTACORREOS + ", " +
				T_ENV_COMPONENTESLISTACORREO + ", " +
				T_CEN_DIRECCIONES+
				
				" WHERE "+EN_IDINSTITUCION + " = " + idInstitucion+
				" AND " + EN_IDENVIO + " = " + idEnvio+
				" AND " + EN_IDINSTITUCION + " = " + LE_IDINSTITUCION+
				" AND " + EN_IDENVIO + " = " + LE_IDENVIO+
				" AND " + LE_IDINSTITUCION + " = " + LI_IDINSTITUCION+
				" AND " + LI_IDINSTITUCION + " = " + CL_IDINSTITUCION+
				" AND " + LE_IDLISTACORREO + " = " + LI_IDLISTACORREO+
				" AND " + LI_IDLISTACORREO + " = " + CL_IDLISTACORREO+
				" AND " + DI_IDINSTITUCION + " = " + CL_IDINSTITUCION+
				" AND " + CL_IDPERSONA + " = " + DI_IDPERSONA+
				" AND " + DI_FECHABAJA + " IS NULL "+
				" AND " + DI_IDDIRECCION + 
				" = f_siga_getdireccion(" +EN_IDINSTITUCION + "," +CL_IDPERSONA + "," +idTipoEnvio + ")";
				
			
			ClsLogging.writeFileLog("EnvEnviosAdm.getDestinatarios.direcciones -> QUERY: "+sql,10);

			if (rcDir.query(sql)) {// vemos si existe direccion preferente de tipo envio
			 	
				for (int i = 0; i < rcDir.size(); i++)	{
					Row fila = (Row) rcDir.get(i);
					String idPersona = fila.getString(CenDireccionesBean.C_IDPERSONA);
					htDirec.put(idPersona,fila);
				}
			}else{
				sql =
					"SELECT DISTINCT * FROM "+
					T_ENV_ENVIOS + ", " +
					T_ENV_LISTACORREOSENVIOS + ", " +
					T_ENV_LISTACORREOS + ", " +
					T_ENV_COMPONENTESLISTACORREO + ", " +
					T_CEN_DIRECCIONES+", "+
					T_CEN_TIPODIRECCIONES+
					" WHERE "+EN_IDINSTITUCION + " = " + idInstitucion+
					" AND " + EN_IDENVIO + " = " + idEnvio+
					" AND " + EN_IDINSTITUCION + " = " + LE_IDINSTITUCION+
					" AND " + EN_IDENVIO + " = " + LE_IDENVIO+
					" AND " + LE_IDINSTITUCION + " = " + LI_IDINSTITUCION+
					" AND " + LI_IDINSTITUCION + " = " + CL_IDINSTITUCION+
					" AND " + LE_IDLISTACORREO + " = " + LI_IDLISTACORREO+
					" AND " + LI_IDLISTACORREO + " = " + CL_IDLISTACORREO+
					" AND " + DI_IDINSTITUCION + " = " + CL_IDINSTITUCION+
					" AND " + CL_IDPERSONA + " = " + DI_IDPERSONA+
					" AND " + DI_FECHABAJA + " IS NULL "+
					" AND " + DI_IDINSTITUCION + " = " + TD_IDINSTITUCION+
					" AND " + DI_IDPERSONA + " = " + TD_IDPERSONA+
					" AND " + DI_IDDIRECCION + " = " + TD_IDDIRECCION+
					" AND " + TD_IDTIPODIRECCION +" = 3" +
					" AND ROWNUM<2";// miramos si existe alguna direccion de tipo correo
			 	 
				if (rcDir.query(sql)) {// vemos si existe direccion de tipo despacho
					 
						for (int i = 0; i < rcDir.size(); i++)	{
							Row fila = (Row) rcDir.get(i);
							String idPersona = fila.getString(CenDireccionesBean.C_IDPERSONA);
							htDirec.put(idPersona,fila);
						}
				}else{
					 sql =
						"SELECT DISTINCT * FROM "+
						T_ENV_ENVIOS + ", " +
						T_ENV_LISTACORREOSENVIOS + ", " +
						T_ENV_LISTACORREOS + ", " +
						T_ENV_COMPONENTESLISTACORREO + ", " +
						T_CEN_DIRECCIONES+", "+
						T_CEN_TIPODIRECCIONES+
						
						" WHERE "+EN_IDINSTITUCION + " = " + idInstitucion+
						" AND " + EN_IDENVIO + " = " + idEnvio+
						" AND " + EN_IDINSTITUCION + " = " + LE_IDINSTITUCION+
						" AND " + EN_IDENVIO + " = " + LE_IDENVIO+
						" AND " + LE_IDINSTITUCION + " = " + LI_IDINSTITUCION+
						" AND " + LI_IDINSTITUCION + " = " + CL_IDINSTITUCION+
						" AND " + LE_IDLISTACORREO + " = " + LI_IDLISTACORREO+
						" AND " + LI_IDLISTACORREO + " = " + CL_IDLISTACORREO+
						" AND " + DI_IDINSTITUCION + " = " + CL_IDINSTITUCION+
						" AND " + CL_IDPERSONA + " = " + DI_IDPERSONA+
						" AND " + DI_FECHABAJA + " IS NULL "+
						" AND " + DI_IDINSTITUCION + " = " + TD_IDINSTITUCION+
						" AND " + DI_IDPERSONA + " = " + TD_IDPERSONA+
						" AND " + DI_IDDIRECCION + " = " + TD_IDDIRECCION+
						" AND " + TD_IDTIPODIRECCION +" = 2"+// miramos si existe alguna direccion de tipo despacho
						" AND ROWNUM<2";
				 	if (rcDir.query(sql)) {//vemos si existe alguna direccion de tipo correo
							
							for (int i = 0; i < rcDir.size(); i++)	{
								Row fila = (Row) rcDir.get(i);
								String idPersona = fila.getString(CenDireccionesBean.C_IDPERSONA);
								htDirec.put(idPersona,fila);
							}
							
				 	}else{
				 		 sql =
							"SELECT DISTINCT * FROM "+
							T_ENV_ENVIOS + ", " +
							T_ENV_LISTACORREOSENVIOS + ", " +
							T_ENV_LISTACORREOS + ", " +
							T_ENV_COMPONENTESLISTACORREO + ", " +
							T_CEN_DIRECCIONES+", "+
							T_CEN_TIPODIRECCIONES+
							
							" WHERE "+EN_IDINSTITUCION + " = " + idInstitucion+
							" AND " + EN_IDENVIO + " = " + idEnvio+
							" AND " + EN_IDINSTITUCION + " = " + LE_IDINSTITUCION+
							" AND " + EN_IDENVIO + " = " + LE_IDENVIO+
							" AND " + LE_IDINSTITUCION + " = " + LI_IDINSTITUCION+
							" AND " + LI_IDINSTITUCION + " = " + CL_IDINSTITUCION+
							" AND " + LE_IDLISTACORREO + " = " + LI_IDLISTACORREO+
							" AND " + LI_IDLISTACORREO + " = " + CL_IDLISTACORREO+
							" AND " + DI_IDINSTITUCION + " = " + CL_IDINSTITUCION+
							" AND " + CL_IDPERSONA + " = " + DI_IDPERSONA+
							" AND " + DI_FECHABAJA + " IS NULL "+
							" AND ROWNUM<2";
					 	if (rcDir.query(sql)) {//vemos si existe cualquier direccion
							
								for (int i = 0; i < rcDir.size(); i++)	{
									Row fila = (Row) rcDir.get(i);
									String idPersona = fila.getString(CenDireccionesBean.C_IDPERSONA);
									htDirec.put(idPersona,fila);
								}
					 	}
				 		
				}
					
			}	 
			}	 	
		}catch(Exception e){
//	        throw new SIGAException("messages.envios.error.obtenciondestinatarios",e);
            throw new ClsExceptions(e,"Error obteniendo los destinatarios del envio");

	    }
		
		
		//************* Creamos los beans de las direcciones obtenidas **********
		
		for (int i=0;i<vDestRows.size();i++){
		    Row rDest = (Row)vDestRows.elementAt(i);
		    String idPersona = rDest.getString(EnvDestinatariosBean.C_IDPERSONA);
		    Row rDir = (Row)htDirec.get(idPersona);
		    if (rDir!=null){
		        //Si tenemos direccion, creamos el bean de destinatario		    	
		        EnvDestinatariosBean destBean = new EnvDestinatariosBean();
		        CenPersonaAdm personaAdm = new CenPersonaAdm(this.usrbean);
		        String nombre=null, apellido1=null, apellido2=null;
		        try {
		        	CenPersonaBean personaBean = personaAdm.getIdentificador(new Long(idPersona));
		        	nombre = personaBean.getNombre();
		        	apellido1 = personaBean.getApellido1();
		        	apellido2 = personaBean.getApellido2();
		        } catch (Exception e){
		        	nombre="";
		        	apellido1="";
		        	apellido2="";
		        }
		        destBean.setNombre(nombre);
		        destBean.setApellidos1(apellido1);
		        destBean.setApellidos2(apellido2);
		        destBean.setIdInstitucion(Integer.valueOf(idInstitucion));
		        destBean.setIdEnvio(Integer.valueOf(idEnvio));
		        destBean.setIdPersona(Long.valueOf(idPersona));
		        destBean.setCodigoPostal(rDir.getString(CenDireccionesBean.C_CODIGOPOSTAL));
		        destBean.setCorreoElectronico(rDir.getString(CenDireccionesBean.C_CORREOELECTRONICO));
		        destBean.setDomicilio(rDir.getString(CenDireccionesBean.C_DOMICILIO));
		        destBean.setFax1(rDir.getString(CenDireccionesBean.C_FAX1));
		        destBean.setFax2(rDir.getString(CenDireccionesBean.C_FAX2));
		        destBean.setIdPais(rDir.getString(CenDireccionesBean.C_IDPAIS));
		        destBean.setIdProvincia(rDir.getString(CenDireccionesBean.C_IDPROVINCIA));
		        destBean.setIdPoblacion(rDir.getString(CenDireccionesBean.C_IDPOBLACION));
		        destBean.setPoblacionExtranjera(rDir.getString(CenDireccionesBean.C_POBLACIONEXTRANJERA));
		        destBean.setMovil(rDir.getString(CenDireccionesBean.C_MOVIL));
		        
		        vDestListaNoDinamicaBeans.add(destBean);
		        
		    } else {
		        //El destinatario no tiene dirección. Lo mostraremos en un log
		    	ClsLogging.writeFileLog("El destinatario " + idPersona + "no tiene direccion",10);

	            // RGG ??
		        //Si NO tenemos direccion, creamos el bean de destinatario igualmente, pero sin direccion		    	
		        EnvDestinatariosBean destBean = new EnvDestinatariosBean();
		        CenPersonaAdm personaAdm = new CenPersonaAdm(this.usrbean);
		        String nombre=null, apellido1=null, apellido2=null;
		        try {
		        	CenPersonaBean personaBean = personaAdm.getIdentificador(new Long(idPersona));
		        	nombre = personaBean.getNombre();
		        	apellido1 = personaBean.getApellido1();
		        	apellido2 = personaBean.getApellido2();
		        } catch (Exception e){
		        	nombre="";
		        	apellido1="";
		        	apellido2="";
		        }
		        destBean.setNombre(nombre);
		        destBean.setApellidos1(apellido1);
		        destBean.setApellidos2(apellido2);
		        destBean.setIdInstitucion(Integer.valueOf(idInstitucion));
		        destBean.setIdEnvio(Integer.valueOf(idEnvio));
		        destBean.setIdPersona(Long.valueOf(idPersona));

		        vDestListaNoDinamicaBeans.add(destBean);
/*
		        try {
		        	insertarDestTemp(destBean);
		        	insertarMensajeLog(destBean, "El destinatario " + idPersona + "no tiene direccion");
		        } catch (Exception e) {}
*/
		    }
		}
		
		
		//Anhadimos los destinatarios manuales a los de la lista, comprobando que
		//no están ya incluidos.
		if (vDestListaNoDinamicaBeans.size()>0){
		    if (vDestManuales.size()>0){		
				for (int x=0;x<vDestManuales.size();x++){
				    boolean existe = false;
				    EnvDestinatariosBean destMan = null;
				    for (int y=0;y<vDestListaNoDinamicaBeans.size();y++){
				        destMan = (EnvDestinatariosBean)vDestManuales.elementAt(x);
				        if((destMan.getTipoDestinatario()!=null && destMan.getTipoDestinatario().equalsIgnoreCase(EnvDestinatariosBean.TIPODESTINATARIO_CENPERSONA)|| destMan.getTipoDestinatario()==null))
				        {
				        	EnvDestinatariosBean destLista = (EnvDestinatariosBean)vDestListaNoDinamicaBeans.elementAt(y);
					        if (destMan.getIdPersona().equals(destLista.getIdPersona())){
					            existe = true;
					            break;
					        }
				        }else{
				        	break;
				        }
				    }
				    if (!existe) vDestListaNoDinamicaBeans.add(destMan);
				}				
			}
		    vManNoDin = vDestListaNoDinamicaBeans;
		} else if (vDestManuales.size()>0){
		    vManNoDin = vDestManuales;		    
		} else {
		    vManNoDin = null;
		}
		
		
		/************ Destinatarios de listas dinámicas ***************/
		
		// 1. Obtenemos todas las queries distintas para el envío y la institución en cuestión
		
		Vector vQueries = new Vector();
		Vector vQueriesExp = new Vector();
		
		//Acceso a BBDD
		RowsContainer rcQueries = null;
		try {
		    rcQueries = new RowsContainer();

			sql = "SELECT " + C_SENTENCIA + "," + C_ESEXPERTA + "," + C_IDCONSULTA + " FROM ";
		    sql +=  T_CON_CONSULTA;

		    sql += " WHERE EXISTS ( SELECT * FROM ";
		    sql +=  T_ENV_LISTACORREOSENVIOS + ", " +
		    		T_ENV_LISTACORREOS + ", " +
		    		T_ENV_LISTACORREOCONSULTA;
		    sql += " WHERE ";
		    sql += LE_IDINSTITUCION + " = " + idInstitucion;
		    sql += " AND " + LE_IDENVIO + " = " + idEnvio;
		    
			sql += " AND " + LE_IDINSTITUCION + " = " + LI_IDINSTITUCION;
			sql += " AND " + LE_IDLISTACORREO + " = " + LI_IDLISTACORREO;
			sql += " AND " + LE_IDINSTITUCION + " = " + LI_IDINSTITUCION;
			sql += " AND " + LE_IDLISTACORREO + " = " + LC_IDLISTACORREO;
			sql += " AND " + LE_IDINSTITUCION + " = " + LC_IDINSTITUCION;
			//2009-CGAE-119-INC-CAT-035
			//se recupera la consulta de la institución que la creo, no de la institución donde se va a usar
			//puesto que se podría recuperar una consulta erronea
			sql += " AND " + LC_IDINSTITUCION_CON + " = " + C_IDINSTITUCION;
			sql += " AND " + LC_IDCONSULTA + " = " + C_IDCONSULTA;
			sql += " AND " + LI_DINAMICA + " = 'S')";
					
			ClsLogging.writeFileLog("EnvEnviosAdm.getDestinatarios.queries -> QUERY: "+sql,10);

			if (rcQueries.query(sql)) {
				for (int i = 0; i < rcQueries.size(); i++)	{
					Row fila = (Row) rcQueries.get(i);
					String sQuery = fila.getString(ConConsultaBean.C_SENTENCIA);
					//reemplazamos la etiqueta de tipo de envío en la query
					sQuery = sQuery.replaceFirst(EnvTipoEnviosAdm.CONS_TIPOENVIO,idTipoEnvio);
					//Anhadimos la query al vector resultante de queries
					if("1".equals(fila.getString(ConConsultaBean.C_ESEXPERTA))){
						vQueriesExp.add(fila);
					}
					else{
						vQueries.add(sQuery);
					}
				}
			}
		} catch (ClsExceptions e1) {
//            throw new SIGAException("messages.envios.error.obtenciondestinatarios",e1);
            throw new ClsExceptions(e1,"Error obteniendo los destinatarios del envio");
			
        }
		//2. Montamos y ejecutamos la query que obtendrá 
		//   el vector de destinatarios de las listas dinámicas
		Vector vDestDin = new Vector();
		
		if (vQueries.size()>0){
		     sql = "SELECT * FROM ";
		    for (int i = 0; i < vQueries.size(); i++) {
                sql += "(" + vQueries.elementAt(i) + ") UNION ";                    
            }
		    //borramos el último UNION
		    sql = sql.substring(0,sql.length()-7);
		    ClsLogging.writeFileLog("EnvEnviosAdm.getDestinatarios ->"+ sql,10);
		    
		    //Acceso a BBDD
			try {
				RowsContainer rcDestDin = new RowsContainer();
				if (rcDestDin.query(sql)) {
					obtenerBeanResultados(idInstitucion, idEnvio, vDestDin, rcDestDin);
				}
			} catch (ClsExceptions e1) {
//	            throw new SIGAException("messages.envios.error.obtenciondestinatarios",e1);
	            throw new ClsExceptions(e1,"Error obteniendo los destinatarios del envio");
				
	        }					
		}
		
		//2009-CGAE-119-INC-CAT-035
		//3. Ejecuta las consultas expertas 
		if (vQueriesExp.size()>0){
		    for (int i = 0; i < vQueriesExp.size(); i++) {
				//Hay que crear el objeto ConConsultaBean 
		    	//Las consultas expertas para envios no pueden tener criterios dinamicos, por eso
		    	//el último parametro de la llamada a procesarEjecutarConsulta es un vector vacío
				Row fila = (Row) rcQueries.get(i);
				ConConsultaAdm conAdm = new ConConsultaAdm(usrbean);
				ConConsultaBean conBean = new ConConsultaBean();
				conBean.setEsExperta("1");
				conBean.setSentencia(fila.getString(ConConsultaBean.C_SENTENCIA));
				conBean.setTipoConsulta(ConConsultaAdm.TIPO_CONSULTA_ENV);
				conBean.setIdInstitucion(Integer.valueOf(idInstitucion));
				conBean.setIdConsulta(Integer.valueOf(fila.getString(ConConsultaBean.C_IDCONSULTA)));
				
				Hashtable ht = conAdm.procesarEjecutarConsulta(idTipoEnvio, conBean, new CriterioDinamico[0], true);
	
				sql = (String) ht.get("sentencia");
				Hashtable codigosOrdenados = (Hashtable) ht.get("codigosOrdenados");
				RowsContainer rcDestDin = new RowsContainer();
				rcDestDin.findBind(sql,codigosOrdenados);
				obtenerBeanResultados(idInstitucion, idEnvio, vDestDin, rcDestDin);
            }
		}
		
		//Anhadimos los destinatarios manuales y no dinámicos a los de las listas dinámicas,		
		//comprobando que no están ya incluidos.
		if (vDestDin!=null && vDestDin.size()>0){
		    if (vManNoDin!=null && vManNoDin.size()>0){		
				for (int x=0;x<vManNoDin.size();x++){
				    boolean existe = false;
				    EnvDestinatariosBean destMan = null;
				    for (int y=0;y<vDestDin.size();y++){
				        destMan = (EnvDestinatariosBean)vManNoDin.elementAt(x);
				        if((destMan.getTipoDestinatario()!=null && destMan.getTipoDestinatario().equalsIgnoreCase(EnvDestinatariosBean.TIPODESTINATARIO_CENPERSONA)|| destMan.getTipoDestinatario()==null))
					       {
				        	EnvDestinatariosBean destLista = (EnvDestinatariosBean)vDestDin.elementAt(y);
					        if (destMan.getIdPersona().equals(destLista.getIdPersona())){
					            existe = true;
					            break;
					        }
				        }else{
				        	break;
				        }
				    }
				    if (!existe) vDestDin.add(destMan);
				}				
			}
		    vDestTotales = vDestDin;
		} else if (vManNoDin!=null && vManNoDin.size()>0){
		    vDestTotales = vManNoDin;		    
		} else {
            //insertarMensajeLog(null, "No hay destinatarios para el envío");
			vDestTotales = null;
		}
		
		return vDestTotales;
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
	
/*	
	public Hashtable getCamposCorreoElectronico(Integer idInstitucion, Integer idEnvio, Long idPersona, String consulta) 
	throws SIGAException,ClsExceptions {
    
	    EnvEnviosAdm envAdm = new EnvEnviosAdm(this.usrbean);
	    Hashtable htPk = new Hashtable();
	    htPk.put(EnvEnviosBean.C_IDINSTITUCION,idInstitucion);
	    htPk.put(EnvEnviosBean.C_IDENVIO,idEnvio);
	    EnvEnviosBean envBean = null;
	    try {
	        envBean = (EnvEnviosBean)envAdm.selectByPK(htPk).firstElement();
	    } catch (ClsExceptions e) {
	        throw e;
	    }
	    if (!envBean.getIdTipoEnvios().equals(Integer.valueOf(EnvTipoEnviosAdm.K_CORREO_ELECTRONICO))){
	        throw new ClsExceptions("Tipo de envío electrónico incorrecto");
	//        throw new SIGAException("messages.envios.error.tipoenvioincorrecto ");
	    }
	    
	    String sAsunto = (getAsunto(idInstitucion,idEnvio)==null)?"":getAsunto(idInstitucion,idEnvio);
	    String sCuerpo = (getCuerpo(idInstitucion,idEnvio)==null)?"":getCuerpo(idInstitucion,idEnvio);
	    
	    //Obtenemos los valores de las etiquetas y los formateamos
	    Hashtable htDatosEnvio = getDatosEnvio(idInstitucion,idEnvio,idPersona, consulta);
	    Hashtable htDatosEnvioForm = null;
	    try {
	        htDatosEnvioForm = darFormatoCampos(idInstitucion,idEnvio,htDatosEnvio);
	    } catch (Exception e1) {
	        throw new ClsExceptions(e1,"Error dando formato a los campos del envío electrónico");
	//        throw new SIGAException("messages.general.error",e1);
	    }
	    
	    //Sustituimos las etiquetas por sus valores
	    sAsunto = sustituirEtiquetas(sAsunto,htDatosEnvioForm);
	    sCuerpo = sustituirEtiquetas(sCuerpo,htDatosEnvioForm);
	    
	    Hashtable htCorreo = new Hashtable();
	    htCorreo.put("asunto",sAsunto);
	    htCorreo.put("cuerpo",sCuerpo);
	    return htCorreo;        
	}
*/
	public Hashtable getCamposCorreoElectronico(EnvEnviosBean envBean,EnvDestinatariosBean beanDestinatario
			, Long idPersona, String consulta,Hashtable htDatosEnvio) throws SIGAException,ClsExceptions {
    
	    if (!envBean.getIdTipoEnvios().equals(Integer.valueOf(EnvTipoEnviosAdm.K_CORREO_ELECTRONICO))){
	        throw new ClsExceptions("Tipo de envío electrónico incorrecto");
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
//	        throw new ClsExceptions(e1,"Error dando formato a los campos del envío electrónico");
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
	        throw new ClsExceptions("Tipo de envío SMS o BuroSMS incorrecto");
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
	        throw new ClsExceptions(e1,"Error dando formato a los campos del envío SMS o BuroSMS");
	    }
	    
	    //Sustituimos las etiquetas por sus valores
	    sTexto = sustituirEtiquetas(sTexto,htDatosEnvioForm);
	    
	    return sTexto;        
	}
	
	public String sustituirEtiquetas(String sArchivo, Hashtable etiquetas) throws SIGAException, ClsExceptions{
	try {
		if (!etiquetas.isEmpty()){
			String key = "";
			for (Enumeration e = etiquetas.keys(); e.hasMoreElements() ;) {
		         key = (String)e.nextElement();
		         final Pattern pattern = Pattern.compile("%%"+key+"%%");
		         final Matcher matcher = pattern.matcher( sArchivo );
//		         sArchivo = matcher.replaceAll(UtilidadesString.formato_ISO_8859_1((String)etiquetas.get(key)));
		         sArchivo = matcher.replaceAll((String)etiquetas.get(key));
		    }
		}
	    return sArchivo;
	} catch (Exception e) {
        throw new ClsExceptions(e,"Error sustituyendo etiquetas");
//		throw new SIGAException("messages.general.error",e);
	}
	}
		
/*	
	public String enviarCorreoElectronico(Integer idInstitucion, Integer idEnvio, boolean generarLog) 
		throws SIGAException,ClsExceptions{
	    
	    boolean errores = false;
	        
        try{
		    EnvEnviosAdm envAdm = new EnvEnviosAdm(this.usrbean);
		    Hashtable htPk = new Hashtable();
		    htPk.put(EnvEnviosBean.C_IDINSTITUCION,idInstitucion);
		    htPk.put(EnvEnviosBean.C_IDENVIO,idEnvio);
		    EnvEnviosBean envBean = null;

		    try {
	            envBean = (EnvEnviosBean)envAdm.selectByPK(htPk).firstElement();
	        } catch (Exception e) {
	            //insertarMensajeLog(idInstitucion, idEnvio, e.getMessage());
	            throw e;
	        }
	        if (!envBean.getIdTipoEnvios().equals(Integer.valueOf(EnvTipoEnviosAdm.K_CORREO_ELECTRONICO))){
	            //insertarMensajeLog(idInstitucion, idEnvio, "Tipo de envío incorrecto");
	            throw new ClsExceptions("Tipo de envio electrónico incorrecto");
	            //throw new SIGAException("messages.envios.error.tipoenvioincorrecto ");
	        }
	        
	        //Se obtiene el contexto para obtener el objeto Session Mail mediante JNDI.
		    Context ctx = new InitialContext();
		    Session sesion = (Session)javax.rmi.PortableRemoteObject.narrow(ctx.lookup("CorreoSIGA"), Session.class);
		    ctx.close();
		    
		    //Obtención remitente
	        EnvRemitentesAdm remAdm = new EnvRemitentesAdm(this.usrbean);
	        Vector vRem = remAdm.select(htPk);
	        String sFrom = "";
	        if (vRem.size()==1){
	            EnvRemitentesBean remBean = (EnvRemitentesBean) vRem.firstElement();
		        sFrom = remBean.getCorreoElectronico();
	        }else{
	            Row dirPref = getDireccionPreferenteInstitucion(idInstitucion,TIPO_CORREO_ELECTRONICO);
	            sFrom = dirPref.getString(EnvRemitentesBean.C_CORREOELECTRONICO);
	        }
	        
	        
	        //Generamos los pdf
	        boolean generados=false;
	        String sDirPdf = null;
	        if (envBean.getIdPlantilla()==null){
		        //Si no tiene plantilla no enviamos documento, 
		        //pero continuamos para mandar el correo
		        //throw new SIGAException("envios.definir.literal.errorAlmacenarEnvio");
		        
		    } else {
	            Envio envio = new Envio(envBean,this.usrbean);
	            generados = envio.generarDocumentoEnvioPDF(idInstitucion,idEnvio);
	            
	            //Ruta donde guardamos los pdf
		        sDirPdf = envAdm.getPathEnvio(
		        		String.valueOf(idInstitucion),
						String.valueOf(idEnvio))+File.separator + "documentosdest";
		    }
	        
	        Vector vDestinatarios = null;
	        
	        try {
	        	vDestinatarios = getDestinatarios(String.valueOf(idInstitucion), 
	                								 String.valueOf(idEnvio),
	                								 String.valueOf(envBean.getIdTipoEnvios()));
	        } catch (Exception e) {
	            //insertarMensajeLog(idInstitucion, idEnvio, e.getMessage());
	            throw e;
	        }
	        
	        if (vDestinatarios!=null) {
		        for (int l=0;l<vDestinatarios.size();l++){
		            EnvDestinatariosBean destBean = (EnvDestinatariosBean) vDestinatarios.elementAt(l);
		            try {
	                    insertarDestTemp(destBean);
	                } catch (Exception e1) {
		                insertarMensajeLog(destBean, e1);
	                    throw e1;
	                }
		        }
		        // Generamos un mensaje para cada destinatario
		        // Si no se produce el envio para un destinatario, se inserta un mensaje en la tabla temporal.
		        for (int i=0;i<vDestinatarios.size();i++){
		            EnvDestinatariosBean destBean = null;
		            try{
			            destBean = (EnvDestinatariosBean) vDestinatarios.elementAt(i);
			            String sTo = destBean.getCorreoElectronico();
			            
			            //Se crea un nuevo Mensaje.
			    	    MimeMessage mensaje = new MimeMessage(sesion);
			    	    
			    	    //Se especifica la dirección de origen.
			    	    mensaje.setFrom(new InternetAddress(sFrom));
			    	    
			    	    //Se especifica la dirección de destino.
			    	    mensaje.addRecipient(Message.RecipientType.TO, new InternetAddress(sTo));
			    	    
			    	    //Se especifica que el correo es MultiPart: texto + fichero.
			    	    MimeMultipart multipart = new MimeMultipart();
			    	    
			    		
			    		//Documentos adjuntos
			    	    MimeBodyPart bodyPart = new MimeBodyPart();    	    
			    	    String sAttachment,sAttach;
			    	    String idPersona = String.valueOf(destBean.getIdPersona());
			    	    
			    	    if (generados){
			    	        sAttachment = sDirPdf + File.separator + idPersona + ".pdf";
				    	    sAttach = idPersona + ".pdf";
				    	    DataSource source = new FileDataSource(sAttachment);				    
				    	    bodyPart.setDataHandler(new DataHandler(source));
				    	    bodyPart.setFileName(sAttach);
				    	    multipart.addBodyPart(bodyPart);
			    	    }
			    	    EnvDocumentosAdm docAdm = new EnvDocumentosAdm(this.usrbean);
			    	    Vector vDocs = docAdm.select(htPk);
			    	    for (int d=0;d<vDocs.size();d++){
			    	        EnvDocumentosBean docBean = (EnvDocumentosBean)vDocs.elementAt(d);
			    	        String idDoc = String.valueOf(docBean.getIdDocumento());
			    	        File fDoc = docAdm.getFile(String.valueOf(idInstitucion),
			    	                					String.valueOf(idEnvio),idDoc);
			    	        sAttachment = fDoc.getPath();
			    	        //sAttach = fDoc.getName();
			    	        sAttach = docBean.getPathDocumento();
			    	        
			    	        DataSource source = new FileDataSource(sAttachment);
			    	        bodyPart = new MimeBodyPart();    	    
			        	    bodyPart.setDataHandler(new DataHandler(source));
			        	    bodyPart.setFileName(sAttach);
			        	    multipart.addBodyPart(bodyPart);
			    	    }
			    	    
			    	    
			    	    // RGG Atencion, estos documentos no se usan, queda comentado el codigo.
			    	    
			    	    EnvDocumentosDestinatariosAdm docDestAdm = new EnvDocumentosDestinatariosAdm(this.usrbean);
			    	    Hashtable htPkD = (Hashtable)htPk.clone();
			    	    htPkD.put(EnvDocumentosDestinatariosBean.C_IDPERSONA,idPersona);
			    	    Vector vDocsDest = docDestAdm.select(htPkD);
			    	    for (int dd=0;dd<vDocsDest.size();dd++){
			    	        EnvDocumentosDestinatariosBean docDestBean = (EnvDocumentosDestinatariosBean)vDocsDest.elementAt(dd);
			    	        String idDoc = String.valueOf(docDestBean.getIdDocumento());
			    	        File fDoc = docDestAdm.getFile(String.valueOf(idInstitucion),
			    	                					String.valueOf(idEnvio),idDoc, idPersona);
			    	        sAttachment = fDoc.getPath();
			    	        //sAttach = fDoc.getName();
			    	        sAttach = docDestBean.getPathDocumento();
			    	        
			    	        DataSource source = new FileDataSource(sAttachment);
			    	        bodyPart = new MimeBodyPart();    	    
			        	    bodyPart.setDataHandler(new DataHandler(source));
			        	    bodyPart.setFileName(sAttach);
			        	    multipart.addBodyPart(bodyPart);^
			    	    }
			    	    
			    	    //Obtenemos asunto y cuerpo del correo
			    	    String consulta = envBean.getConsulta().equals("")?null:envBean.getConsulta();
				        Hashtable htCorreo = getCamposCorreoElectronico(idInstitucion,idEnvio,
				                Long.valueOf(idPersona),consulta);
				        String sAsunto = (htCorreo.get("asunto")==null)?"":(String)htCorreo.get("asunto");
				        String sCuerpo = (htCorreo.get("cuerpo")==null)?"":(String)htCorreo.get("cuerpo");
				        
				        //Se especifica el texto del correo.
			    	    bodyPart = new MimeBodyPart();
			    	    bodyPart.setText(sCuerpo,"ISO-8859-1");
			    	    multipart.addBodyPart(bodyPart);        	
			    		
			    	    //Se especifica el asunto del correo.
			    	    mensaje.setSubject(sAsunto,"ISO-8859-1");	    	
			    	    //mensaje.setHeader("Content-Transfert-Encoding", "base64");
			    	    mensaje.setHeader("Content-Type","text/plain; charset=\"ISO-8859-1\"");
			    	    //Se anhade el contenido al fichero.
			    	    mensaje.setContent(multipart);
			    	    
			    		
			    		//Se envía el correo.
			    	    Transport.send(mensaje);
			    	    
			    	    
			    	    
		            } catch (Exception e){
		                errores = true;
		                insertarMensajeLog(destBean, e);
		            }
		    	}
	        }

	        this.generarLogEnvio(idInstitucion, idEnvio);

	        // RGG Esto son errores de destinatario, se resuelven poniendo estado malo
	        if (errores){
	        	return EnvEstadoEnvioAdm.K_ESTADOENVIO_PROCESADOCONERRORES;
	        } else {
	            return EnvEstadoEnvioAdm.K_ESTADOENVIO_PROCESADO;
	        }
	        // RGG esto son errores bloqueantes?? de modo que salta excepcion y no hace nada.
        } catch (SIGAException e) { 
            this.generarLogEnvioException(idInstitucion, idEnvio,e);
			throw e;
        } catch(Exception e){
            this.generarLogEnvioException(idInstitucion, idEnvio,e);
	        // RGG 07/07/2005 return EnvEstadoEnvioAdm.K_ESTADOENVIO_PENDIENTE_MANUAL;
//	        throw new SIGAException("messages.envios.error.enviandoCorreoElectronico",e);
            throw new ClsExceptions(e,"Error enviando correo electrónico");
            
    	}
        
    	
	}
*/	
	public Row getDireccionPreferenteInstitucion(Integer idInstitucion,int idTipoEnvio)
	    throws SIGAException,ClsExceptions{
	        
	    
	    CenInstitucionAdm instAdm = new CenInstitucionAdm(this.usrbean);
        Hashtable htPkIns = new Hashtable();
        htPkIns.put(CenInstitucionBean.C_IDINSTITUCION,idInstitucion);
        CenInstitucionBean instBean;
        try {
            instBean = (CenInstitucionBean)instAdm.selectByPK(htPkIns).firstElement();
        } catch (ClsExceptions e) {
            throw new SIGAException("messages.general.error",e);
        }
        Integer idPersona = instBean.getIdPersona();
        
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
            throw new ClsExceptions(e,"Error obteniendo la dirección preferente del destinatario");
//	        throw new SIGAException("messages.envios.error.obtenciondestinatarios",e);
	    }
		return fila;
	}

/*	
	private void insertarDestTemp(EnvDestinatariosBean destBean)
		throws SIGAException, ClsExceptions{
	    	    	    
	    EnvTempDestinatariosAdm destTempAdm = new EnvTempDestinatariosAdm(this.usrbean);
	    EnvTempDestinatariosBean tempBean = new EnvTempDestinatariosBean();
	    
	    tempBean.setCodigoPostal(destBean.getCodigoPostal());
	    tempBean.setCorreoElectronico(destBean.getCorreoElectronico());
	    tempBean.setDomicilio(destBean.getDomicilio());
	    tempBean.setFax1(destBean.getFax1());
	    tempBean.setFax2(destBean.getFax2());
	    tempBean.setIdEnvio(destBean.getIdEnvio());
	    tempBean.setIdInstitucion(destBean.getIdInstitucion());
	    tempBean.setIdPais(destBean.getIdPais());
	    tempBean.setIdPersona(destBean.getIdPersona());
	    tempBean.setIdPoblacion(destBean.getIdPoblacion());
	    tempBean.setIdProvincia(destBean.getIdProvincia());
	    tempBean.setMensaje("OK");
	    
	    //si la persona no es genérica (id=-1), buscamos el nombre, apellidos y nif
	    //en la tabla de personas para la tabla temporal.
	    try {
		    if (!destBean.getIdPersona().equals(CenPersonaAdm.K_PERSONA_GENERICA)){
		        Hashtable htPk = new Hashtable();
		        htPk.put(CenPersonaBean.C_IDPERSONA,destBean.getIdPersona());
		        CenPersonaAdm persAdm = new CenPersonaAdm(this.usrbean);
		        CenPersonaBean persBean = (CenPersonaBean) persAdm.selectByPK(htPk).firstElement();
		        tempBean.setApellidos1(persBean.getApellido1());
			    tempBean.setApellidos2(persBean.getApellido2());
			    tempBean.setNifcif(persBean.getNIFCIF());
			    tempBean.setNombre(persBean.getNombre());
		    } else {
			    tempBean.setApellidos1(destBean.getApellidos1());
			    tempBean.setApellidos2(destBean.getApellidos2());
			    tempBean.setNifcif(destBean.getNifcif());
			    tempBean.setNombre(destBean.getNombre());
		    }
		    destTempAdm.insert(tempBean);
	    } catch (Exception e) {
	    	e.printStackTrace();
            // RGG no muestro el error puesto que se va a utilizar ese registro para escribir los errores. 
	    	//throw new ClsExceptions(e,"Error al insertar destinatario.");
        }	    
	}
*/	
/*	
	private void insertarMensajeLog(EnvDestinatariosBean destBean, String mensaje)
	throws SIGAException{

    if (destBean!=null) {
	    EnvTempDestinatariosAdm destTempAdm = new EnvTempDestinatariosAdm(this.usrbean);
	    Hashtable htDest = new Hashtable();
	    htDest.put(EnvTempDestinatariosBean.C_IDINSTITUCION,destBean.getIdInstitucion());
	    htDest.put(EnvTempDestinatariosBean.C_IDENVIO,destBean.getIdEnvio());
	    htDest.put(EnvTempDestinatariosBean.C_IDPERSONA,destBean.getIdPersona());
	    
	    try {
	        EnvTempDestinatariosBean destTempBean = (EnvTempDestinatariosBean)destTempAdm.selectByPKForUpdate(htDest).firstElement();

	        if (mensaje.length()>500) {
	        	mensaje = mensaje.substring(0,500);
	        }
	        destTempBean.setMensaje(mensaje);
	        destTempAdm.update(destTempBean);
	    } catch (ClsExceptions e) {
	    }	        
    }
}
*/
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
	}
/*
	private void insertarMensajeLog(EnvDestinatariosBean destBean, Exception e)
	throws SIGAException{
		String mensajeNuevo = "";
		
	    String languageCode = "";
	    String userCode = "";
	    String institucion = "";

		ClsLogging.writeFileLog("ERROR AL ENVIAR. Mensaje: "+e.getLocalizedMessage(),1);

	    if (e==null){
	      ClsLogging.writeFileLog("ERROR ENVIOS: @@@@ Excepcion NULA @@@@", 1);
	      ClsLogging.writeFileLog("ERROR ENVIOS: @@@@ La excepcion no ha sido preparada @@@@", 1);
	    }
	    else
	    {
	      
	      try {
	        ExceptionManager mgr = new ExceptionManager(e, languageCode, userCode, null, institucion);
	        ClsLogging.writeFileLogError(mgr.getLogCompleteMessage(languageCode), e, 1);
	      } catch (ClsExceptions ex) { 
	      	//don't capture this exception, but send original without treating
	      	ex.printStackTrace();
	      }
	    }
		
		
		
		if (e instanceof SIGAException) {
			SIGAException se = (SIGAException) e;
			ClsLogging.writeFileLogError("ERROR AL ENVIAR. Mensaje: "+se.getMsg(""),se,7);
			mensajeNuevo += se.getMsg("");// + " ----- ";

		} else {
			ClsLogging.writeFileLogError("ERROR AL ENVIAR. Mensaje: "+e.getLocalizedMessage(),e,7);
			mensajeNuevo += e.getLocalizedMessage();// + " ----- ";
		}
		
		insertarMensajeLog(destBean,mensajeNuevo);
    }
        
*/
/*	
	private void insertarMensajeLog(Integer idInstitucion, Integer idEnvio, String mensaje)
	throws SIGAException{

    if (idInstitucion!=null && idEnvio!=null) {
	    EnvTempDestinatariosAdm destTempAdm = new EnvTempDestinatariosAdm(this.usrbean);
	    Hashtable htDest = new Hashtable();
	    htDest.put(EnvTempDestinatariosBean.C_IDINSTITUCION,idInstitucion);
	    htDest.put(EnvTempDestinatariosBean.C_IDENVIO,idEnvio);
	    htDest.put(EnvTempDestinatariosBean.C_IDPERSONA,this.obtenerIdPersonaInstitucion(idInstitucion));
	    
	    try {
	        EnvTempDestinatariosBean destTempBean = (EnvTempDestinatariosBean)destTempAdm.selectByPKForUpdate(htDest).firstElement();
	        if (mensaje.length()>500) {
	        	mensaje = mensaje.substring(0,500);
	        }
	        destTempBean.setMensaje(mensaje);
	        destTempAdm.update(destTempBean);
	    } catch (ClsExceptions e) {
	      	//don't capture this exception, but send original without treating
	      	e.printStackTrace();
	    }	        
    }
	}
*/
	
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
	        throw new SIGAException(e.getMessage());
	    }	        
    }
        
	
/*	
	public void generarLogEnvio(Integer idInstitucion, Integer idEnvio)
	throws SIGAException{
    
    //Obtenemos el fichero de log (HH_mm.log)
	BufferedWriter bwOut = null;
    String sIdInstitucion = String.valueOf(idInstitucion);
    String sIdEnvio = String.valueOf(idEnvio);
    
            
    String sFicheroLog = "";
    try {
        // RGG creo los directorios
    	File auxDirectorios = new File(this.getPathEnvio(sIdInstitucion,sIdEnvio));
    	auxDirectorios.mkdirs();

    	// RGG 08-09-2005 Cambio para que el fichero de log sea unico 
        //sFicheroLog = this.getPathEnvio(sIdInstitucion,sIdEnvio) + File.separator + sTime + ".log.xls";        
        sFicheroLog = this.getPathEnvio(sIdInstitucion,sIdEnvio) + File.separator + "informeEnvio" + ".log.xls"; 
        File borrar = new File(sFicheroLog);
        if (borrar.exists()) borrar.delete();
        
        bwOut = new BufferedWriter(new FileWriter(sFicheroLog));
    } catch (Exception e1) {
        throw new SIGAException("messages.general.error",e1);
    }
    
    //Obtenemos los beans de la tabla temporal de destinatarios
    EnvTempDestinatariosAdm tempAdm = new EnvTempDestinatariosAdm(this.usrbean);
    Hashtable htEnvio = new Hashtable();
    htEnvio.put(EnvTempDestinatariosBean.C_IDINSTITUCION,idInstitucion);
    htEnvio.put(EnvTempDestinatariosBean.C_IDENVIO,idEnvio);
    Vector vTempDest = new Vector();
    try {
        vTempDest = tempAdm.select(htEnvio);
    } catch (ClsExceptions e) {
        throw new SIGAException("messages.general.error",e);
    }
    
    final String separador = ClsConstants.SEPARADOR; 
    //Por cada bean insertamos una línea en el archivo de log, 
    //con todos los campos del bean separados por comas
    if (vTempDest!=null && vTempDest.size()>0) {
        for (int i=0;i<vTempDest.size();i++){
            EnvTempDestinatariosBean tempBean = (EnvTempDestinatariosBean)vTempDest.elementAt(i);
            Hashtable htBean = tempBean.getOriginalHash();
            Enumeration eCampos = htBean.keys();
            String sLinea = "";
            String sLineaCabecera = "";
            int contador = 0;
            while (eCampos.hasMoreElements()){
            	contador ++;
            	Object key = eCampos.nextElement();
            	Object val = htBean.get(key);
            	sLineaCabecera = sLineaCabecera + key.toString() + separador;
            	if (key.toString().equals("MENSAJE")) {
            		sLinea = sLinea + UtilidadesString.getMensajeIdioma("ES", val.toString()) + separador;
            	} else {
            		sLinea = sLinea + val.toString() + separador;
            	}
            }

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
                
                // RGG borro todo lo que queda if (tempBean.getMensaje().equalsIgnoreCase("OK")){
                tempAdm.delete(tempBean);

            } catch (Exception e2) {
                throw new SIGAException("messages.general.error",e2);
            }
        
        }
    
    } else {
        try {
            bwOut.write("EL ENVIO NO TIENE DESTINATARIOS. NO SE HA ENVIADO NADA.");
            bwOut.newLine();
        } catch (Exception e2) {
            throw new SIGAException("messages.general.error",e2);
        }
    }
    
    try {
        bwOut.close();
    } catch (IOException e2) {
        throw new SIGAException("messages.general.error",e2);
    }        
    
	}
*/
	public void generarLogEnvioHT(Vector vDestinatarios,EnvRemitentesBean remitente,String documentos, Hashtable htErrores, EnvEnviosBean envBean)
	throws SIGAException, ClsExceptions {
	    
		BufferedWriter bwOut = null;
	    String sIdInstitucion = String.valueOf(envBean.getIdInstitucion());
	    String sIdEnvio = String.valueOf(envBean.getIdEnvio());
	    String sFicheroLog = "";
	    try {
	        // RGG creo los directorios
	    	File auxDirectorios = new File(this.getPathEnvio(sIdInstitucion,sIdEnvio));
	    	auxDirectorios.mkdirs();
	    	// RGG 08-09-2005 Cambio para que el fichero de log sea unico 
	        sFicheroLog = this.getPathEnvio(sIdInstitucion,sIdEnvio) + File.separator + "informeEnvio" + ".log.xls"; 
	        File borrar = new File(sFicheroLog);
	        if (borrar.exists()) borrar.delete();
	        bwOut = new BufferedWriter(new FileWriter(sFicheroLog));
	    } catch (Exception e1) {
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
        
	    //Por cada bean insertamos una línea en el archivo de log, 
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
					}
					
					sLinea += beanPersonaJG.getNif() + separador;
					sLinea += beanPersonaJG.getNombre() + separador;
					sLinea += beanPersonaJG.getApellido1() + separador;
					sLinea += beanPersonaJG.getApellido2() + separador;
	            
	            }else if(destBean.getTipoDestinatario()!=null && destBean.getTipoDestinatario().equalsIgnoreCase(EnvDestinatariosBean.TIPODESTINATARIO_SCSJUZGADO)){
	            	Hashtable htJuz = new Hashtable();
					htJuz.put(ScsJuzgadoBean.C_IDJUZGADO,destBean.getIdPersona());
					htJuz.put(ScsJuzgadoBean.C_IDINSTITUCION,destBean.getIdInstitucion());
					ScsJuzgadoAdm admJuz = new ScsJuzgadoAdm(this.usrbean);
					Vector v = admJuz.selectByPK(htJuz);
					ScsJuzgadoBean beanJuzgado= null;
					if (v!=null && v.size()>0) {
						beanJuzgado = (ScsJuzgadoBean) v.get(0);
					}
					
					sLinea += beanJuzgado.getNombre() + separador;
					
	            }else{
	            	CenPersonaAdm admPer = new CenPersonaAdm(this.usrbean);
		            Hashtable htPer = new Hashtable();
					htPer.put(CenPersonaBean.C_IDPERSONA,destBean.getIdPersona());
					Vector v = admPer.selectByPK(htPer);
					CenPersonaBean beanPersona = null;
					if (v!=null && v.size()>0) {
						beanPersona = (CenPersonaBean) v.get(0);
					}
					
					sLinea += beanPersona.getNIFCIF() + separador;
					sLinea += beanPersona.getNombre() + separador;
					sLinea += beanPersona.getApellido1() + separador;
					sLinea += beanPersona.getApellido2() + separador;
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
    		    	//si no tiene direccion de tipo despacho se busca la dirección preferente y se saca el telefono1 despacho.
    		    		Hashtable direccion=direccionAdm.getEntradaDireccionEspecifica(destBean.getIdPersona().toString(),destBean.getIdInstitucion().toString(),preferencia);		    				
	    				if (direccion!=null && direccion.size()!=0) {
	    					Hashtable htDir = (Hashtable)direccion;
	    					sLinea += htDir.get(CenDireccionesBean.C_TELEFONO1)+separador;		    					
	    				}else {    					
	    					//si no tiene ni direccion de tipo despacho, ni una dirección preferente se saca el telefono1 de la primera dirección que se encuentre.
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
	            	sLinea += destBean.getPoblacionExtranjera() + separador;
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
	                throw new SIGAException("messages.general.error",e2);
	            }
	        
	        }
	    
	    } else {
	        try {
	            bwOut.write("EL ENVIO NO TIENE DESTINATARIOS. NO SE HA ENVIADO NADA.");
	            bwOut.newLine();
	        } catch (Exception e2) {
	            throw new SIGAException("messages.general.error",e2);
	        }
	    }
	    
	    try {
	        bwOut.close();
	    } catch (IOException e2) {
	        throw new SIGAException("messages.general.error",e2);
	    }        
	    
	}

/*	
	public void generarLogEnvioException(Integer idInstitucion, Integer idEnvio, Exception ex)
	throws SIGAException{
    
    //Obtenemos el fichero de log (HH_mm.log)
    BufferedWriter bwOut = null;
    String sIdInstitucion = String.valueOf(idInstitucion);
    String sIdEnvio = String.valueOf(idEnvio);
    
            
    String sFicheroLog = "";
    try {
        // RGG creo los directorios
    	File auxDirectorios = new File(this.getPathEnvio(sIdInstitucion,sIdEnvio));
    	auxDirectorios.mkdirs();

    	// RGG 08-09-2005 Cambio para que el fichero de log sea unico 
        //sFicheroLog = this.getPathEnvio(sIdInstitucion,sIdEnvio) + File.separator + sTime + ".log.xls";        
        sFicheroLog = this.getPathEnvio(sIdInstitucion,sIdEnvio) + File.separator + "informeEnvio" + ".log.xls"; 
        File borrar = new File(sFicheroLog);
        if (borrar.exists()) borrar.delete();
        
        bwOut = new BufferedWriter(new FileWriter(sFicheroLog));
    } catch (Exception e1) {
        throw new SIGAException("messages.general.error",e1,new String[] {"modulo.envios"});
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
        throw new SIGAException("messages.general.error",e2,new String[] {"modulo.envios"});
    }

    
    try {
        bwOut.close();
    } catch (IOException e2) {
        throw new SIGAException("messages.general.error",e2,new String[] {"modulo.envios"});
    }        

    //Obtenemos los beans de la tabla temporal de destinatarios PARA BORRALOS
    EnvTempDestinatariosAdm tempAdm = new EnvTempDestinatariosAdm(this.usrbean);
    Hashtable htEnvio = new Hashtable();
    htEnvio.put(EnvTempDestinatariosBean.C_IDINSTITUCION,idInstitucion);
    htEnvio.put(EnvTempDestinatariosBean.C_IDENVIO,idEnvio);
    Vector vTempDest = new Vector();
    try {
        vTempDest = tempAdm.select(htEnvio);
    } catch (ClsExceptions e) {
        throw new SIGAException("messages.general.error",e);
    }
    if (vTempDest!=null && vTempDest.size()>0) {
        // AQUI DEBERIA BORRAR EL DESTINATARIO TEMPORAL
        for (int j=0;j<vTempDest.size();j++){
        	try {
        		EnvTempDestinatariosBean tempBean2 = (EnvTempDestinatariosBean)vTempDest.elementAt(j);
	            tempAdm.delete(tempBean2);
	        } catch (Exception e2) {
	            throw new SIGAException("messages.general.error",e2);
	        }
        }  
    }
    
	}
*/
	
	public void generarLogEnvioExceptionHT(EnvEnviosBean envBean, Exception ex)
	throws SIGAException, ClsExceptions{
    
	    BufferedWriter bwOut = null;
	    String sIdInstitucion = String.valueOf(envBean.getIdInstitucion());
	    String sIdEnvio = String.valueOf(envBean.getIdEnvio());
	    
	    String sFicheroLog = "";
	    try {
	        // RGG creo los directorios
	    	File auxDirectorios = new File(this.getPathEnvio(sIdInstitucion,sIdEnvio));
	    	auxDirectorios.mkdirs();
	
	        sFicheroLog = this.getPathEnvio(sIdInstitucion,sIdEnvio) + File.separator + "informeEnvio" + ".log.xls"; 
	        File borrar = new File(sFicheroLog);
	        if (borrar.exists()) borrar.delete();
	        
	        bwOut = new BufferedWriter(new FileWriter(sFicheroLog));
	    } catch (Exception e1) {
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
	        throw new SIGAException("messages.general.error",e2,new String[] {"modulo.envios"});
	    }
	
	    
	    try {
	        bwOut.close();
	    } catch (IOException e2) {
	        throw new SIGAException("messages.general.error",e2,new String[] {"modulo.envios"});
	    }        
	}

/*	
	public String enviarCorreoOrdinario(Integer idInstitucion, Integer idEnvio) 
	throws SIGAException,ClsExceptions{
    
    boolean errores = false;
    // DAVID: VARIABLES
    String nombreFicheroXX="";
    String nombreDestinatarioXX="";


    try{

    	
    	
        //////////////////////////////////
    	// PREPARACION
        //////////////////////////////////
    	EnvEnviosAdm envAdm = new EnvEnviosAdm(this.usrbean);
	    Hashtable htPk = new Hashtable();
	    htPk.put(EnvEnviosBean.C_IDINSTITUCION,idInstitucion);
	    htPk.put(EnvEnviosBean.C_IDENVIO,idEnvio);
	    EnvEnviosBean envBean = null;
	    envBean = (EnvEnviosBean)envAdm.selectByPK(htPk).firstElement();
	    
	    if (!envBean.getIdTipoEnvios().equals(Integer.valueOf(EnvTipoEnviosAdm.K_CORREO_ORDINARIO))){
            throw new ClsExceptions("Tipo de envío ordinario incorrecto");
//	        throw new SIGAException("messages.envios.error.tipoenvioincorrecto ");
	    }

        SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);
        Calendar cal = Calendar.getInstance();
		Date dat;
        try {
            dat = sdf.parse(envBean.getFechaCreacion());
            cal.setTime(dat);
        } catch (ParseException e1) {
            throw e1;
        }
        String anio = String.valueOf(cal.get(Calendar.YEAR));
        String mes = String.valueOf(cal.get(Calendar.MONTH)+1);
        String dia = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));

        // Obtengo el pathFTP
        String pathFTP = "";
        GenParametrosAdm paramAdm = new GenParametrosAdm(this.usrbean);
    	pathFTP = paramAdm.getValor(idInstitucion.toString(),"ENV","PATH_DESCARGA_ENVIOS_ORDINARIOS","");
        
        Date hoy = new Date();
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd_HHmm");
        String sHoy = sdf2.format(hoy);
        
        String pathDestino = pathFTP + File.separator + idInstitucion.toString() + File.separator + anio + File.separator + mes + File.separator + dia + File.separator + idEnvio.toString() + File.separator + sHoy + File.separator;  
	    
        //Se obtiene la property que contiene el comando shell para imprimir documentos
	    //ReadProperties properties = new ReadProperties("SIGA.properties");
	    //String comandoShell = properties.returnProperty("envios.correoordinario.comandoshell",true);

	    //Obtenemos la impresora del envío
	    //String idImpresora = String.valueOf(envBean.getIdImpresora());
	    
        //////////////////////////////////
        //GENERAMOS TODOS LOS PDF
        //////////////////////////////////
        boolean generados=false;
        String sDirPdf = null;
        if (envBean.getIdPlantilla()==null){
	        throw new SIGAException("envios.definir.literal.errorAlmacenarEnvio");
	        
	    } else {
            Envio envio = new Envio(envBean,this.usrbean);
            generados = envio.generarDocumentoEnvioPDF(idInstitucion,idEnvio);
            //Ruta donde guardamos los pdf
	        try {
	        	sDirPdf = envAdm.getPathEnvio(String.valueOf(idInstitucion),
							String.valueOf(idEnvio))+ File.separator + "documentosdest";
	        } catch (Exception e) {
	            throw e;
	        }
	    }
        
        
        Vector vDestinatarios = null;
        try {
        	vDestinatarios = getDestinatarios(String.valueOf(idInstitucion), 
                								 String.valueOf(idEnvio),
                								 String.valueOf(envBean.getIdTipoEnvios()));
        } catch (Exception e) {
            throw e;
        }
        
        
        //////////////////////////////////
        // INSERCION DE PERSONAS EN ENV_TEMP_DESTINATARIOS
        //////////////////////////////////

        
        //Primero limpiamos la tabla temporal por si quedaban registros con errores
        String claves[] = {EnvTempDestinatariosBean.C_IDINSTITUCION, 
                			EnvTempDestinatariosBean.C_IDENVIO};
        EnvTempDestinatariosAdm tempAdm = new EnvTempDestinatariosAdm(this.usrbean);
        tempAdm.deleteDirect(htPk,claves);
        if (vDestinatarios!=null)
        for (int l=0;l<vDestinatarios.size();l++){
            EnvDestinatariosBean destBean = (EnvDestinatariosBean) vDestinatarios.elementAt(l);
            try {
                insertarDestTemp(destBean);
            } catch (SIGAException e1) {
                insertarMensajeLog(destBean, e1);
                throw e1;
            }
        }

    	// ESTO ESTA CONTROLADO HASTA QUE SE SEPA COMO VA A SER LA IMPRESION DE DOCUMENTOS
    
        //////////////////////////////////
        // CREACION DE ENVIOS Y ADJUNTOS Y DEMAS
        //////////////////////////////////

        // Generamos los documentos necesarios para cada destinatario
        // Si no se produce el envio para un destinatario, se inserta un mensaje en la tabla temporal.
    	if (vDestinatarios!=null)
    	for (int i=0;i<vDestinatarios.size();i++){
            EnvDestinatariosBean destBean = null;
            try{
	            destBean = (EnvDestinatariosBean) vDestinatarios.elementAt(i);
	            String idPersona = String.valueOf(destBean.getIdPersona());
	    	    	    		
	            // NOMBRE DEL FICHERO
		    	CenPersonaAdm perAdm = new CenPersonaAdm(this.usrbean);
			    Hashtable htPk2 = new Hashtable();
			    htPk2.put(CenPersonaBean.C_IDPERSONA,idPersona);
			    CenPersonaBean perBean = null;
			    String nifcif = "";

			    perBean = (CenPersonaBean)perAdm.selectByPK(htPk2).firstElement();
		        nifcif = perBean.getNIFCIF().trim();
    	        // DAVID: NOMBRE DESTINATARIO
    	        nombreDestinatarioXX=perBean.getNombre() + " " + perBean.getApellido1() + " " + perBean.getApellido2();

		        String sIdEnvio = idEnvio.toString();
		        if (sIdEnvio.length()<8) {
		        	sIdEnvio = UtilidadesString.formatea(sIdEnvio,8,true);
		        }
		        String nombre = idInstitucion.toString() + "_" + sIdEnvio + "_" + nifcif + "_";
		        int contadorFicheros = 1;
	            
	            
	    		//DOCUMENTOS ADJUNTOS
	            if (generados){
	    	        String sGenerado = sDirPdf + File.separator + idPersona + ".pdf";
	    	        
	    	        String sCopiado = pathDestino + File.separator + nombre + UtilidadesString.formatea(new Integer(contadorFicheros).toString(),4,true) + ".pdf";
	    	        // DAVID: NOMBRE DEL FICHERO
	    	        nombreFicheroXX=nombre + UtilidadesString.formatea(new Integer(contadorFicheros).toString(),4,true) + ".pdf";
	    	        contadorFicheros++;
	    	        
	    	        File fGenerado = new File(sGenerado);
	    	        File fCopiado = new File(sCopiado);
	    	        fCopiado.getParentFile().mkdirs();
	    	        if (fGenerado.exists()) {
    	        		copiarFichero(fGenerado,fCopiado);
	    	        		
	    	        }
	    	        // borro el pdf generado en origen
	    	        // BUENO , POR AHORA NO fGenerado.deleteOnExit();
	    	        // RGG 14-07-2005 ESTO YA NO ES NECESARIO MANDARLO A IMPRIMIR
		            
	    	        //String sComando = comandoShell + " " + idImpresora + " " + sGenerado;
	    	        //Process p = Runtime.getRuntime().exec(sComando);
	    	        
	    	    }
	    	    
	    	    EnvDocumentosAdm docAdm = new EnvDocumentosAdm(this.usrbean);
	    	    Vector vDocs = docAdm.select(htPk);
	    	    if (vDocs!=null)
	    	    for (int d=0;d<vDocs.size();d++){
	    	        EnvDocumentosBean docBean = (EnvDocumentosBean)vDocs.elementAt(d);
	    	        String idDoc = String.valueOf(docBean.getIdDocumento());
	    	        File fDoc = docAdm.getFile(String.valueOf(idInstitucion),
	    	                					String.valueOf(idEnvio),idDoc);
	    	        
	    	        
	    	        // RGG 14-07-2005 COPIO CADA DOCUMENTO ADJUNTO A LA CARPETA DE CADA IDPERSONA
	    	        
	    	        
	    	        
	    	        String sAdjunto = fDoc.getPath();
	    	        
	    	        File fAdjunto = new File(sAdjunto);
	    	        String sCopiadoAdjunto = pathDestino + File.separator + nombre + UtilidadesString.formatea(new Integer(contadorFicheros).toString(),4,true) + ".pdf";
	    	        // DAVID: NOMBRE DEL FICHERO
	    	        nombreFicheroXX=nombre + UtilidadesString.formatea(new Integer(contadorFicheros).toString(),4,true);
	    	        contadorFicheros++;
	    	        File fCopiadoAdjunto = new File(sCopiadoAdjunto);
	    	        fCopiadoAdjunto.getParentFile().mkdirs();
	    	        if (fAdjunto.exists()) {
	    	        	copiarFichero(fAdjunto,fCopiadoAdjunto);
	    	        }
	    	        // RGG 14-07-2005 ESTO YA NO ES NECESARIO MANDARLO A IMPRIMIR
	    	        //String sComando = comandoShell + " " + idImpresora + " " + sAdjunto;
	    	        //Process p = Runtime.getRuntime().exec(sComando);
  	        
	    	    }
    	    
	    	    EnvDocumentosDestinatariosAdm docDestAdm = new EnvDocumentosDestinatariosAdm(this.usrbean);
	    	    Hashtable htPkD = (Hashtable)htPk.clone();
	    	    htPkD.put(EnvDocumentosDestinatariosBean.C_IDPERSONA,idPersona);
	    	    Vector vDocsDest = docDestAdm.select(htPkD);
	    	    if (vDocsDest!=null)
	    	    for (int dd=0;dd<vDocsDest.size();dd++){
	    	        EnvDocumentosDestinatariosBean docDestBean = (EnvDocumentosDestinatariosBean)vDocsDest.elementAt(dd);
	    	        String idDoc = String.valueOf(docDestBean.getIdDocumento());
	    	        File fDoc = docDestAdm.getFile(String.valueOf(idInstitucion),
	    	                					String.valueOf(idEnvio),idDoc, idPersona);
	    	        String sAdjuntoDest = fDoc.getPath();
	    	        File fAdjuntoDest = new File(sAdjuntoDest);
	    	        String sCopiadoAdjuntoDest = pathDestino + File.separator + nombre + UtilidadesString.formatea(new Integer(contadorFicheros).toString(),4,true) + ".pdf";
	    	        // DAVID: NOMBRE DEL FICHERO
	    	        nombreFicheroXX=nombre + UtilidadesString.formatea(new Integer(contadorFicheros).toString(),4,true);
	    	        contadorFicheros++;
	    	        File fCopiadoAdjuntoDest = new File(sCopiadoAdjuntoDest);
	    	        fCopiadoAdjuntoDest.getParentFile().mkdirs();
	    	        if (fAdjuntoDest.exists()) {
	    	        	copiarFichero(fAdjuntoDest,fCopiadoAdjuntoDest);
	    	        }
	    	    }

            } catch (Exception e){
                errores = true;
                insertarMensajeLog(destBean, e);
            }
    	}
    
	        

        //Despues de todo el proceso, generamos/imprimimos las etiquetas
        String sEtiquetas = envBean.getImprimirEtiquetas();
        if (sEtiquetas.equals(EnvEnviosAdm.GENERAR_ETIQUETAS)){
            String sRutaEtiquetas = generarEtiquetas(String.valueOf(idInstitucion),String.valueOf(envBean.getIdEnvio()),pathDestino);
        }else if (sEtiquetas.equals(EnvEnviosAdm.IMPRIMIR_ETIQUETAS)){
        }
        this.generarLogEnvio(idInstitucion, idEnvio);

        // RGG Esto son errores de destinatario, se resuelven poniendo estado malo
        if (errores){
        	return EnvEstadoEnvioAdm.K_ESTADOENVIO_PROCESADOCONERRORES;
        } else {
            return EnvEstadoEnvioAdm.K_ESTADOENVIO_PROCESADO;
        }
        
        
	} catch (SIGAException e) {
        this.generarLogEnvioException(idInstitucion, idEnvio,e);
		throw e;
    } catch(Exception e){
        this.generarLogEnvioException(idInstitucion, idEnvio,e);
        // RGG 07/07/2005 return EnvEstadoEnvioAdm.K_ESTADOENVIO_PENDIENTE_MANUAL;
        throw new ClsExceptions(e,"Error general en evnío ordinario");
//        throw new SIGAException("messages.envios.error.enviandoCorreoOrdinario",e);

	}		
}
*/
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
			
			/**PathDocumentosAdjuntos, que es la dirección donde se guarda en local
			 *  o la carpeta que se designe los archivos**/			
			try {
				   pathDocumentosAdjuntos = envioAdm.getPathEnvio(idInstitucion,idenvio);
				} catch (Exception e) {
					 		new ClsExceptions (e, "Error al recuperar el envio");
			   	}

			// PREPARACION
			//////////////////////////////////
			if (!envBean.getIdTipoEnvios().equals(Integer.valueOf(EnvTipoEnviosAdm.K_CORREO_ORDINARIO))){
				throw new ClsExceptions("Tipo de envío ordinario incorrecto");
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
							fCopiado.getParentFile().mkdirs();
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
						/** documentos adjuntos de envío**/
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
								fCopiadoAdjunto.getParentFile().mkdirs();
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
				File ruta = new File(rutaServidorDescargasZip);					
				if(!ruta.exists()){
						ruta.mkdirs();
				}				
				if (listaParaZip.size()!=0) {
					ArrayList ficherosPDF= new ArrayList();
					for (int i=0;i<listaParaZip.size();i++){								
						File f = (File) listaParaZip.get(i);							 
					    ficherosPDF.add(f);							
					}					
					Plantilla.doZip(rutaServidorDescargasZip,nombreFicheroZIP,ficherosPDF, false);				
					ficheroSalida = new File(rutaServidorDescargasZip + nombreFicheroZIP + ".zip");
				 }
				 Date hoy = new Date();
				 SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd_HHmm");
				 String sHoy = sdf2.format(hoy);		
				 nombreFicheroZIP= sHoy;				 
				 File fCopiadoAdjunto = new File(sCopiadoAdjunto+nombreFicheroZIP+".zip");
				 fCopiadoAdjunto.getParentFile().mkdirs();
				 if (ficheroSalida.exists()) {					
						copiarFichero(ficheroSalida,fCopiadoAdjunto);						
						borrarDirectorio(ruta);
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
			this.generarLogEnvioExceptionHT(envBean, e);
			throw e;
		} catch(Exception e){
			this.generarLogEnvioExceptionHT(envBean, e);
			throw new ClsExceptions(e,"Error general en envío ordinario");
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
				File ficRutaFOP=new File(rutaEtiquetas);
				if (!ficRutaFOP.exists()){
					if(!ficRutaFOP.mkdirs()){
						throw new SIGAException("messages.envios.error.noPlantilla");
					}
				}
				ficFOP = new File(rutaEtiquetas+"temp_"+envio+".fo");
				if (ficFOP.exists()){
					if(!ficFOP.delete()){
			            throw new ClsExceptions("Error de acceso a la plantilla generando etiquetas fo");
					}
				}
				
				Plantilla plantilla = new Plantilla(this.usrbean);
				
				// Parametros pàra ubicación etiquetas
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
					
		   	    	// Incorporación etiqueta
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
					plantilla.convertFO2PDF(ficFOP, ficPDF);
					ficFOP.delete();
					
					// AQUI COPIA EL FICHERO GENERADO DE ETIQUETAS (ficPDF)
					new File(pathDestino).mkdirs();
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
			throw e;
	    } catch(Exception e){
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
		    //sql += " AND " + EnvEnviosBean.C_IDENVIO + " = 3732";
					
			enviosBeans = this.select(sql);
			return enviosBeans;
			
        } catch (ClsExceptions e) {
			throw new ClsExceptions (e, "Error al obtener los envíos pendientes");
//	        throw new SIGAException("messages.general.error",e);
	    }
	}
	
/*	
	public String enviarFax(Integer idInstitucion, Integer idEnvio) 
		throws SIGAException,ClsExceptions{
	    
	    boolean errores = false;
        // DAVID: VARIABLES
        String nombreFicheroXX="";
        String nombreDestinatarioXX="";

        //Vector de Usuarios destinatarios del ZETAFAX:
		//Vector destinatariosFax = null;
		//UsuarioFax usuarioFax = null; 
		//Vector de ficheros PDF para el ZETAFAX:
		Vector ficherosFax = new Vector();		

		try{
	        
		    EnvEnviosAdm envAdm = new EnvEnviosAdm(this.usrbean);
		    Hashtable htPk = new Hashtable();
		    htPk.put(EnvEnviosBean.C_IDINSTITUCION,idInstitucion);
		    htPk.put(EnvEnviosBean.C_IDENVIO,idEnvio);
		    EnvEnviosBean envBean = null;
	        envBean = (EnvEnviosBean)envAdm.selectByPK(htPk).firstElement();

		    if (!envBean.getIdTipoEnvios().equals(Integer.valueOf(EnvTipoEnviosAdm.K_FAX))){
				throw new ClsExceptions ("Tipo de envío por fax incorrecto");
//		        throw new SIGAException("messages.envios.error.tipoenvioincorrecto ");
		    }
	
	        SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);
	        Calendar cal = Calendar.getInstance();
			Date dat;
	        try {
	            dat = sdf.parse(envBean.getFechaCreacion());
	            cal.setTime(dat);
	        } catch (ParseException e1) {
	            throw e1;
	        }
	        String anio = String.valueOf(cal.get(Calendar.YEAR));
	        String mes = String.valueOf(cal.get(Calendar.MONTH)+1);
	        String dia = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
	
	        // Obtengo el pathFTP y pathFicherosZETAFAX:
	        String pathFTP="", pathFicherosZETAFAX="";
	        GenParametrosAdm paramAdm = new GenParametrosAdm(this.usrbean);
	        try {
	        	pathFTP = paramAdm.getValor(idInstitucion.toString(),"ENV","PATH_DESCARGA_ENVIOS_FAX","");
	        	pathFicherosZETAFAX = paramAdm.getValor(idInstitucion.toString(),"ENV","PATH_DESCARGA_ENVIOS_FAX_SERVIDOR","");
	        } catch (Exception e) {
	        	pathFTP="";
	        	pathFicherosZETAFAX="";
	        	throw e;
	        }

	        Date hoy = new Date();
	        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd_HHmm");
	        String sHoy = sdf2.format(hoy);
	        
	        String pathDestino = pathFTP + File.separator + idInstitucion.toString() + File.separator + anio + File.separator + mes + File.separator + dia + File.separator + idEnvio.toString() + File.separator + sHoy + File.separator;  
	        //PATH donde buscar el fichero para enviar en el fax:
	        String pathDestinoZETAFAX = pathFicherosZETAFAX + File.separator + idInstitucion.toString() + File.separator + anio + File.separator + mes + File.separator + dia + File.separator + idEnvio.toString() + File.separator + sHoy;

	    
	        //Se obtiene la property que contiene el comando shell para enviar fax
		    //ReadProperties properties = new ReadProperties("SIGA.properties");
		    //String comandoShell = properties.returnProperty("envios.fax.comandoshell",true);
		        
	        //Generamos los pdf
	        boolean generados=false;
	        String sDirPdf = null;
	        if (envBean.getIdPlantilla()==null){ //aunque no exista plantilla para envio continuamos para enviar documento adjunto
		    //    throw new SIGAException("envios.definir.literal.errorAlmacenarEnvio");
		        
		    }
	        else {
	            Envio envio = new Envio(envBean,this.usrbean);
	            generados = envio.generarDocumentoEnvioPDF(idInstitucion,idEnvio);
	            //Ruta donde guardamos los pdf
	            try {
	            	sDirPdf = envAdm.getPathEnvio(String.valueOf(idInstitucion),
						 		String.valueOf(idEnvio))+  File.separator + "documentosdest";
	            } catch (Exception e) {
	            	throw e;
	            }
		    }
	        Vector vDestinatarios = null;
        	vDestinatarios = getDestinatarios(String.valueOf(idInstitucion), 
	                								 String.valueOf(idEnvio),
	                								 String.valueOf(envBean.getIdTipoEnvios()));
	        
	        if (vDestinatarios!=null)
	        for (int l=0;l<vDestinatarios.size();l++){
	            EnvDestinatariosBean destBean = (EnvDestinatariosBean) vDestinatarios.elementAt(l);
	            try {
	                insertarDestTemp(destBean);
	            } catch (Exception e1) {
	                insertarMensajeLog(destBean, e1);
	                throw e1;
	            }
	        }
	        
	        // Generamos los documentos necesarios para cada destinatario
	        // Si no se produce el envio para un destinatario, se inserta un mensaje en la tabla temporal.
	        if (vDestinatarios!=null)
	        for (int i=0;i<vDestinatarios.size();i++){
	            EnvDestinatariosBean destBean = null;
	            
	    	    // RGG 06-10-2005 borro el array de ficheros de fax para que no se envien dos veces.
	    	    ficherosFax = new Vector(); 

	            try{
		            destBean = (EnvDestinatariosBean) vDestinatarios.elementAt(i);
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
		    	        // DAVID: NOMBRE DESTINATARIO
		    	        nombreDestinatarioXX=perBean.getNombre() + " " + perBean.getApellido1() + " " + perBean.getApellido2();
				    } catch (ClsExceptions e) {
				        throw new SIGAException("messages.envios.error.noDatosPersona",e);
				    }
			        String sIdEnvio = idEnvio.toString();
			        if (sIdEnvio.length()<8) {
			        	sIdEnvio = UtilidadesString.formatea(sIdEnvio,8,true);
			        }
			        String nombre = idInstitucion.toString() + "_" + sIdEnvio + "_" + nifcif + "_";
			        int contadorFicheros = 1;
		            
		            
		    		//DOCUMENTOS ADJUNTOS
		    	    
		    	    if (generados){
		    	        String sGenerado = sDirPdf + File.separator + idPersona + ".pdf";
		    	        
		    	        File fGenerado = new File(sGenerado);
		    	        String sCopiadoGenerado = pathDestino + File.separator + nombre + UtilidadesString.formatea(new Integer(contadorFicheros).toString(),4,true) + ".pdf";
		    	        // DAVID: NOMBRE DEL FICHERO		    	        
		    	        nombreFicheroXX = pathDestinoZETAFAX + File.separator + nombre + UtilidadesString.formatea(new Integer(contadorFicheros).toString(),4,true) + ".pdf";
		    	        contadorFicheros++;

		    	        File fCopiadoGenerado = new File(sCopiadoGenerado);
		    	        fCopiadoGenerado.getParentFile().mkdirs();
		    	        if (fGenerado.exists()) {
		    	        	copiarFichero(fGenerado,fCopiadoGenerado);
		    	        }

		    	        //ZETAFAX: Anhado el fichero al vector de ficheros para el ZetaFax:
		    	        ficherosFax.add(nombreFicheroXX);
		    	    }
		    	    
		    	    EnvDocumentosAdm docAdm = new EnvDocumentosAdm(this.usrbean);
		    	    Vector vDocs = docAdm.select(htPk);
		    	    if (vDocs!=null)
		    	    for (int d=0;d<vDocs.size();d++){
		    	        EnvDocumentosBean docBean = (EnvDocumentosBean)vDocs.elementAt(d);
		    	        String idDoc = String.valueOf(docBean.getIdDocumento());
		    	        File fDoc = docAdm.getFile(String.valueOf(idInstitucion),
		    	                					String.valueOf(idEnvio),idDoc);
		    	        String sAdjunto = fDoc.getPath();
		    	        File fAdjunto = new File(sAdjunto);
		    	        String sCopiadoAdjunto = pathDestino + File.separator + nombre + UtilidadesString.formatea(new Integer(contadorFicheros).toString(),4,true) + ".pdf";
		    	        // DAVID: NOMBRE DEL FICHERO
		    	        nombreFicheroXX = pathDestinoZETAFAX + File.separator + nombre + UtilidadesString.formatea(new Integer(contadorFicheros).toString(),4,true) + ".pdf";
		    	        contadorFicheros++;


		    	        File fCopiadoAdjunto = new File(sCopiadoAdjunto);
		    	        fCopiadoAdjunto.getParentFile().mkdirs();
		    	        if (fAdjunto.exists()) {
		    	        	copiarFichero(fAdjunto,fCopiadoAdjunto);
		    	        }

		    	        //ZETAFAX: Anhado el fichero al vector de ficheros para el ZetaFax:
		    	        ficherosFax.add(nombreFicheroXX);
		       	    }
		    	    
		    	    EnvDocumentosDestinatariosAdm docDestAdm = new EnvDocumentosDestinatariosAdm(this.usrbean);
		    	    Hashtable htPkD = (Hashtable)htPk.clone();
		    	    htPkD.put(EnvDocumentosDestinatariosBean.C_IDPERSONA,idPersona);
		    	    Vector vDocsDest = docDestAdm.select(htPkD);
		    	    
		    	    if (vDocsDest!=null)
		    	    for (int dd=0;dd<vDocsDest.size();dd++){
		    	        EnvDocumentosDestinatariosBean docDestBean = (EnvDocumentosDestinatariosBean)vDocsDest.elementAt(dd);
		    	        String idDoc = String.valueOf(docDestBean.getIdDocumento());
		    	        File fDoc = docDestAdm.getFile(String.valueOf(idInstitucion),
		    	                					String.valueOf(idEnvio),idDoc, idPersona);
		    	        String sAdjuntoDest = fDoc.getPath();
		    	        File fAdjuntoDest = new File(sAdjuntoDest);
		    	        String sCopiadoAdjuntoDest = pathDestino + File.separator + nombre + UtilidadesString.formatea(new Integer(contadorFicheros).toString(),4,true) + ".pdf";
		    	        // DAVID: NOMBRE DEL FICHERO
		    	        nombreFicheroXX = pathDestinoZETAFAX + File.separator + nombre + UtilidadesString.formatea(new Integer(contadorFicheros).toString(),4,true) + ".pdf";
		    	        contadorFicheros++;

		    	        File fCopiadoAdjuntoDest = new File(sCopiadoAdjuntoDest);
		    	        fCopiadoAdjuntoDest.getParentFile().mkdirs();
		    	        if (fAdjuntoDest.exists()) {
		    	        	copiarFichero(fAdjuntoDest,fCopiadoAdjuntoDest);
		    	        }

		    	        //ZETAFAX: Anhado el fichero al vector de ficheros para el ZetaFax:
		    	        ficherosFax.add(nombreFicheroXX);
		    	    }

		    	    //		    	    
		    	    // Generamos un fax por destinatario con todos sus ficheros adjuntos:
		    	    // NOTA: Si se ha producido una excepcion lanza una ClsException.
		    	    //
		    	    this.generarFax(destBean, envBean, fax, nombre, ficherosFax);
		    		
	            } catch (Exception e){
	                errores = true;
	                insertarMensajeLog(destBean, e);
	            }
	            
	    	}//Bucle por destinatarios
	        
	        this.generarLogEnvio(idInstitucion, idEnvio);

	        // RGG Esto son errores de destinatario, se resuelven poniendo estado malo
	        if (errores){
	        	return EnvEstadoEnvioAdm.K_ESTADOENVIO_PROCESADOCONERRORES;
	        } else {
	            return EnvEstadoEnvioAdm.K_ESTADOENVIO_PROCESADO;
	        }
	        
		} catch (SIGAException e) {
            this.generarLogEnvioException(idInstitucion, idEnvio,e);
			throw e;
	    } catch(Exception e){
            this.generarLogEnvioException(idInstitucion, idEnvio,e);
	        // RGG 07/07/2005 return EnvEstadoEnvioAdm.K_ESTADOENVIO_PENDIENTE_MANUAL;
			throw new ClsExceptions (e, "Error general enviando fax");
//	        throw new SIGAException("messages.envios.error.enviandoFax",e);
		}		
	}
*/
	
	public String enviarFax(EnvEnviosBean envBean, Vector vDestinatarios, Hashtable htErrores, boolean generarLog) 
	throws SIGAException,ClsExceptions{
    
	    boolean errores = false;
	    String nombreFicheroXX="";
	    String nombreDestinatarioXX="";
		Vector ficherosFax = new Vector();		
		StringBuffer documentos = new StringBuffer();
		try{
	        
		    if (!envBean.getIdTipoEnvios().equals(Integer.valueOf(EnvTipoEnviosAdm.K_FAX))){
				throw new ClsExceptions ("Tipo de envío por fax incorrecto");
		    }
	
	        SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);
	        Calendar cal = Calendar.getInstance();
			Date dat;
	        try {
	            dat = sdf.parse(envBean.getFechaCreacion());
	            cal.setTime(dat);
	        } catch (ParseException e1) {
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
		    	        fCopiadoGenerado.getParentFile().mkdirs();
		    	        if (fGenerado.exists()) {
		    	        	copiarFichero(fGenerado,fCopiadoGenerado);
		    	        }
	
		    	        //ZETAFAX: Anhado el fichero al vector de ficheros para el ZetaFax:
		    	        ficherosFax.add(nombreFicheroXX);
		    	    }
		    	    
		    	    /* documentos adjuntos de envío*/
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
		    	        fCopiadoAdjunto.getParentFile().mkdirs();
		    	        if (fAdjunto.exists()) {
		    	        	copiarFichero(fAdjunto,fCopiadoAdjunto);
		    	        }
	
		    	        //ZETAFAX: Anhado el fichero al vector de ficheros para el ZetaFax:
		    	        ficherosFax.add(nombreFicheroXX);
		       	    }
		    	    
		    	    /* documentos adjuntos de destinatario*/
		    	    // RGG Atencion, estos documentos no se usan, queda comentado el codigo.
	    	    	/*
		    	    EnvDocumentosDestinatariosAdm docDestAdm = new EnvDocumentosDestinatariosAdm(this.usrbean);
		    	    Hashtable htPkD = (Hashtable)htPk.clone();
		    	    htPkD.put(EnvDocumentosDestinatariosBean.C_IDPERSONA,idPersona);
		    	    Vector vDocsDest = docDestAdm.select(htPkD);
		    	    
		    	    if (vDocsDest!=null)
		    	    for (int dd=0;dd<vDocsDest.size();dd++){
		    	        EnvDocumentosDestinatariosBean docDestBean = (EnvDocumentosDestinatariosBean)vDocsDest.elementAt(dd);
		    	        String idDoc = String.valueOf(docDestBean.getIdDocumento());
		    	        File fDoc = docDestAdm.getFile(envBean,idDoc, idPersona);
		    	        String sAdjuntoDest = fDoc.getPath();
		    	        File fAdjuntoDest = new File(sAdjuntoDest);
		    	        String sCopiadoAdjuntoDest = pathDestino + File.separator + nombre + UtilidadesString.formatea(new Integer(contadorFicheros).toString(),4,true) + ".pdf";
		    	        nombreFicheroXX = pathDestinoZETAFAX + File.separator + nombre + UtilidadesString.formatea(new Integer(contadorFicheros).toString(),4,true) + ".pdf";
		    	        contadorFicheros++;
	
		    	        File fCopiadoAdjuntoDest = new File(sCopiadoAdjuntoDest);
		    	        fCopiadoAdjuntoDest.getParentFile().mkdirs();
		    	        if (fAdjuntoDest.exists()) {
		    	        	copiarFichero(fAdjuntoDest,fCopiadoAdjuntoDest);
		    	        }
	
		    	        //ZETAFAX: Anhado el fichero al vector de ficheros para el ZetaFax:
		    	        ficherosFax.add(nombreFicheroXX);
		    	    }
		    	    */
		    	    
		    	    //		    	    
		    	    // Generamos un fax por destinatario con todos sus ficheros adjuntos:
		    	    // NOTA: Si se ha producido una excepcion lanza una ClsException.
		    	    //

		    	    this.generarFax(destBean, envBean, fax, nombre, ficherosFax);
		    		
		    	    // RGG 08/06/2009 ESTADISTICA
		    	    EnvEstatEnvioAdm admEstat = new EnvEstatEnvioAdm(this.usrbean);
		    	    admEstat.insertarApunte(envBean.getIdInstitucion(),envBean.getIdEnvio(),envBean.getIdTipoEnvios(),new Long(idPersona));

		    	    
	            } catch (Exception e){
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
	        this.generarLogEnvioExceptionHT(envBean,e);
			throw e;
	    } catch(Exception e){
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
			throw new SIGAException(e2.getMessage(),e2);
		} catch (Exception e) {
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

	public String enviarCorreoElectronico(EnvEnviosBean envBean,  Vector vDestinatarios, 
			Hashtable htErrores, boolean generarLog) 
	throws SIGAException,ClsExceptions{
    
    boolean errores = false;
    Transport tr = null;
    
    try{
	    EnvEnviosAdm envAdm = new EnvEnviosAdm(this.usrbean);

        // COMPROBACIÓN
        /////////////////////////////////////
        if (!envBean.getIdTipoEnvios().equals(Integer.valueOf(EnvTipoEnviosAdm.K_CORREO_ELECTRONICO))){
            throw new ClsExceptions("Tipo de envio electrónico incorrecto");
        }
        
        // OBTENCIÓN DE SERVIDOR DE CORREO
        /////////////////////////////////////
	    Context ctx = new InitialContext();
	    Session sesion = (Session)javax.rmi.PortableRemoteObject.narrow(ctx.lookup("CorreoSIGA"), Session.class);
	    ctx.close();

	    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//		ReadProperties rp = new ReadProperties("SIGA.properties");	
		
	    // RGG autenticar SMTP
	    sesion.getProperties().put("mail.smtp.auth", "true");
	    tr = sesion.getTransport("smtp");
	    tr.connect(rp.returnProperty("mail.smtp.host"),rp.returnProperty("mail.smtp.user"), rp.returnProperty("mail.smtp.pwd"));
	   


	    /*
        Session sesion2 = (Session)javax.rmi.PortableRemoteObject.narrow(ctx.lookup("CorreoSIGA"), Session.class);
	    Authenticator authenticator = new Authenticator();
	    Properties  props = sesion2.getProperties();
	    props.setProperty("mail.smtp.submitter", authenticator.getPasswordAuthentication().getUserName());
	    props.setProperty("mail.smtp.auth", "true");
	    Session sesion = Session.getInstance(props,authenticator);
	    */
	    


	    // OBTENCION DE REMITENTE 
        /////////////////////////////////////
        EnvRemitentesAdm remAdm = new EnvRemitentesAdm(this.usrbean);
	    Hashtable htPk = new Hashtable();
	    htPk.put(EnvEnviosBean.C_IDINSTITUCION,envBean.getIdInstitucion());
	    htPk.put(EnvEnviosBean.C_IDENVIO,envBean.getIdEnvio());
        Vector vRem = remAdm.select(htPk);
        String sFrom = "";
        EnvRemitentesBean remBean = null;
        StringBuffer txtDocumentos = new StringBuffer("");
        if (vRem.size()>0){
        	// obtengo la primera de la lista
        	remBean = (EnvRemitentesBean) vRem.firstElement();
	        sFrom = remBean.getCorreoElectronico();
        }else{
        	// obtengo la de la institucion
            Row dirPref = getDireccionPreferenteInstitucion(envBean.getIdInstitucion(),TIPO_CORREO_ELECTRONICO);
            sFrom = dirPref.getString(EnvRemitentesBean.C_CORREOELECTRONICO);
        }

        
	    // PLANTILLA DE GENERACION
        /////////////////////////////////////
        // Obtenemos el archivo con la plantilla
        
        
                
        // BUCLE DE DESTINATARIOS
        /////////////////////////////////////
	    
	    
	    
	    
        if (vDestinatarios!=null) {
        	
    	    
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
        	EnvImagenPlantillaBean beanImagenes = new EnvImagenPlantillaBean();
        	beanImagenes.setIdInstitucion(envBean.getIdInstitucion());
        	beanImagenes.setIdTipoEnvios(envBean.getIdTipoEnvios());
        	beanImagenes.setIdPlantillaEnvios(envBean.getIdPlantillaEnvios());
        	EnvImagenPlantillaAdm admImagenPlantilla = new EnvImagenPlantillaAdm(this.usrbean);
    	    List<ImagenPlantillaForm> lImagenes = admImagenPlantilla.getImagenes(beanImagenes);
        	
        	
        	//ACUMULAMOS POBLACIONES, PAISES Y PROVINCIAS PARA EVITAR HACER QUERYS A LA BBDD
        	Hashtable htPoblaciones = new Hashtable();
    	    Hashtable htProvincia = new Hashtable();
    	    Hashtable htPaises = new Hashtable();
	        for (int l=0;l<vDestinatarios.size();l++) {

	        	txtDocumentos = new StringBuffer();
	        	EnvDestinatariosBean destBean = (EnvDestinatariosBean) vDestinatarios.elementAt(l);
	            actualizaPaisDestinatario(destBean, htPaises);
	            actualizaPoblacionDestinatario(destBean, htPoblaciones);
	            actualizaProvincia(destBean, htProvincia);
                
                String pathArchivoGenerado = null;
		        String sDirPdf = null;
		        
	            // ENVIO PARA CADA DESTINATARIO
		        /////////////////////////////////////
		        Hashtable htDatos = null;
		        try{
		        
		        	// GENERACION DEL PDF DEL ENVIO SI PROCEDE
		        	/////////////////////////////////////
			        if (envBean.getIdPlantilla()!=null && fPlantilla!=null){
				        //Si no tiene plantilla no enviamos documento, 
				        //pero continuamos para mandar el correo
			        	
			        	EnvCamposEnviosAdm admCampos = new EnvCamposEnviosAdm(this.usrbean);            
			            Vector vCampos = admCampos.obtenerCamposEnvios(destBean.getIdInstitucion().toString(), destBean.getIdEnvio().toString(), "");
			            EnvEnviosAdm admEnvio = new EnvEnviosAdm(this.usrbean);
			            htDatos = admEnvio.getDatosEnvio(destBean, null);
			            
						htDatos = admEnvio.darFormatoCampos(destBean.getIdInstitucion(), destBean.getIdEnvio(), this.usrbean.getLanguage(), htDatos,vCampos);
			        	pathArchivoGenerado = generarDocumentoEnvioPDFDestinatario(envBean, destBean, fPlantilla,tipoArchivoPlantilla,htDatos);
			        	

			        	//Ruta donde guardamos los pdf
				        sDirPdf = getPathEnvio(envBean)+File.separator + "documentosdest";
				    }
	        
		            String sTo = destBean.getCorreoElectronico();
		            if(sTo==null ||sTo.trim().equals(""))
		            	throw new SMTPAddressFailedException(new InternetAddress(sFrom),null,0,UtilidadesString.getMensajeIdioma(usrbean,"messages.envios.errorSinEmail"));
		            
		            //Se crea un nuevo Mensaje.
		    	    MimeMessage mensaje = new MimeMessage(sesion);
		    	    
		    	    //Se especifica la dirección de origen.
//		    	    mensaje.setFrom(new InternetAddress(sFrom));
		    	    //ATTENCION INCIDENCIA. DESCOMENTAR ESTO
		    	    String descripcionFrom = sFrom;
		    	    if(remBean!=null && remBean.getDescripcion()!=null && !remBean.getDescripcion().trim().equals(""))
		    	    	descripcionFrom = remBean.getDescripcion().trim();
		    	    else if(remBean.getIdPersona()!=null){
		    	    	CenPersonaAdm personaAdm = new CenPersonaAdm(usrbean);
		    	    	descripcionFrom =  personaAdm.obtenerNombreApellidosJSP(remBean.getIdPersona().toString());
		    	    }
		    	    mensaje.setFrom(new InternetAddress(sFrom,descripcionFrom));
			    	 // Acuse de recibo
		    	    if(envBean.getAcuseRecibo()!=null && envBean.getAcuseRecibo().equals(ClsConstants.DB_TRUE))
		    	    	mensaje.addHeader("Disposition-Notification-To",sFrom);
		    	    InternetAddress toInternetAddress = new InternetAddress(sTo);
		    	    //Se especifica la dirección de destino.
		    	    mensaje.addRecipient(MimeMessage.RecipientType.TO,toInternetAddress );
		    	    
		    	    
		    	    String idPersona = String.valueOf(destBean.getIdPersona());
		    	    
		            // MENSAJE DE CORREO ELECTRONICO
			        /////////////////////////////////////
		    	    //Obtenemos asunto y cuerpo del correo
		    	    String consulta = envBean.getConsulta().equals("")?null:envBean.getConsulta();
		    	    //Si NO TIENE PLANTILLA NO SE HAN OBTENIDO LOS DATOS DE LA CONSULTA, LUEGO LOS OBTENEMOS
		    	    if(htDatos==null){
		    	    	EnvCamposEnviosAdm admCampos = new EnvCamposEnviosAdm(this.usrbean);            
			            Vector vCampos = admCampos.obtenerCamposEnvios(destBean.getIdInstitucion().toString(), destBean.getIdEnvio().toString(), "");
			            EnvEnviosAdm admEnvio = new EnvEnviosAdm(this.usrbean);
			            htDatos = admEnvio.getDatosEnvio(destBean, null);
						htDatos = admEnvio.darFormatoCampos(destBean.getIdInstitucion(), destBean.getIdEnvio(), this.usrbean.getLanguage(), htDatos,vCampos);
		    	    	
		    	    }
		    	    
		    	    if(lImagenes!= null && lImagenes.size()>0)
		    	    	htDatos.put("imagenesPlantilla", lImagenes);
			        Hashtable htCorreo = getCamposCorreoElectronico(envBean, destBean,Long.valueOf(idPersona),consulta,htDatos);
			        String sAsunto = (htCorreo.get("asunto")==null)?"":(String)htCorreo.get("asunto");
			        
			        
			        //Se especifica el texto del correo.

			        //Se especifica el asunto del correo.
		    	    mensaje.setSubject(sAsunto,"ISO-8859-1");
		    	    mensaje.setHeader("Content-Type","text/html; charset=\"ISO-8859-1\"");
		    	    

		   
		    	    
		    	    
		    	    // Create your new message part
		    	    // Se especifica que el correo es MultiPart: texto + fichero.
		    	    // MimeMultipart multipart = new MimeMultipart("related");
//		    	    MimeMultipart multipart = new MimeMultipart();
		    	    
		    	    String sCuerpo = (htCorreo.get("cuerpo")==null)?"":(String)htCorreo.get("cuerpo");
		    	    

		    	    MimeMultipart mixedMultipart = new MimeMultipart("mixed");
		    	    MimeBodyPart mixedBodyPart = new MimeBodyPart();
		    	    
		    	    MimeMultipart relatedMultipart = new MimeMultipart("related");
		    	    MimeBodyPart relatedBodyPart = new MimeBodyPart();
		    	    
		    	    
//		    	    MimeMultipart alternativeMultipart = new MimeMultipart("alternative");
//		    	    MimeBodyPart alternativeBodyPart = new MimeBodyPart();
//		    	    alternativeBodyPart.setContent(relatedMultipart);

		    	    
		    	    //alternative message
		    	    addContentToMultipart(relatedMultipart,sCuerpo);

		    	    //Hierarchy
		    	    mixedBodyPart.setContent(relatedMultipart);
		    	    
		    	    mixedMultipart.addBodyPart(mixedBodyPart);
//		    	    mixedMultipart.addBodyPart(relatedBodyPart);
//		    	    mixedMultipart.addBodyPart(alternativeBodyPart);
//		    	    multipartRoot.a
		    	    

		    	    //add a part for the image
		    	    
		    	    if (lImagenes!=null && lImagenes.size()>0){
			    	    
			    	    for(ImagenPlantillaForm imagenPlantilla:lImagenes){
			    	    	
			    	    	EnvImagenPlantillaBean imagenPlantillaBean = imagenPlantilla.getImagenPlantillaBean();
			    	    	if(imagenPlantillaBean.isEmbebed()){
			    	    		if(sCuerpo.indexOf(imagenPlantillaBean.getImagenSrcEmbebida("/"))!=-1){
					    	    	addCIDToMultipart(relatedMultipart,imagenPlantillaBean.getPathImagen(null,File.separator),imagenPlantillaBean.getNombre());
				    	    	}
			    	    	}
			    	    }
			    	    
		    	    }
		    	    //attach a pdf
		    	  //Documentos adjuntos
		    	    String sAttachment,sAttach;
		    	   
		    	    
		            // ADJUNTAR EL PDF DEL ENVIO
			        /////////////////////////////////////
		    	    /* archivo pdf: [idPersona].pdf */
		    	    if (pathArchivoGenerado!=null){
			        	String [] pathGenerado = pathArchivoGenerado.split("\\\\");  
			        	txtDocumentos.append(pathGenerado[pathGenerado.length-1]);
		    	        txtDocumentos.append(",");
		    	    	
		    	    	sAttachment = pathArchivoGenerado;
			    	    sAttach = pathArchivoGenerado.substring(pathArchivoGenerado.lastIndexOf(File.separator)+1);
			    	    addAttachToMultipart(mixedMultipart, pathArchivoGenerado, sAttach);
		    	    }
		    	    
		            // DOCUMENTOS ADJUNTOS
			        /////////////////////////////////////
		    	    /* documentos adjuntos de envío*/
		    	    EnvDocumentosAdm docAdm = new EnvDocumentosAdm(this.usrbean);
		    	    Vector vDocs = docAdm.select(htPk);
		    	    for (int d=0;d<vDocs.size();d++){
		    	        EnvDocumentosBean docBean = (EnvDocumentosBean)vDocs.elementAt(d);
		    	        String idDoc = String.valueOf(docBean.getIdDocumento());
		    	        File fDoc = docAdm.getFile(envBean,idDoc);
		    	        sAttachment = fDoc.getPath();
		    	        sAttach = docBean.getPathDocumento();
		    	        addAttachToMultipart(mixedMultipart, fDoc.getPath(), docBean.getPathDocumento());
		    	        txtDocumentos.append(docBean.getDescripcion());
		    	        txtDocumentos.append(",");
		    	      
		    	    }
		    	    
		    	  
		    	    
		    	    
		    	    // Associate multi-part with message
		    	    mensaje.setContent(mixedMultipart);
		    	   
		    	    tr.sendMessage(mensaje, mensaje.getAllRecipients());
		    	    
		    	    EnvEstatEnvioAdm admEstat = new EnvEstatEnvioAdm(this.usrbean);
		    	    admEstat.insertarApunteExtra(envBean.getIdInstitucion(),envBean.getIdEnvio(),envBean.getIdTipoEnvios(),new Long(idPersona),sTo);		    	    
		    	    ////////////////////////////////////////////////////////////////////////////////
		    	    // RGG 08/06/2009 ESTADISTICA
		    	    
	            }catch (SMTPAddressFailedException e){
	                errores = true;
	                insertarMensajeLogHT(destBean,htErrores, e);
	            }catch (SendFailedException e){
	                errores = true;
	                insertarMensajeLogHT(destBean,htErrores, e);
	            } catch (Exception e){
	            	
	                errores = true;
	                insertarMensajeLogHT(destBean,htErrores, e);
	            }

	        } // FOR
        } // IF


        // GENERAR EL LOG DEL ENVIO
        /////////////////////////////////////
        if (generarLog) {
        	generarLogEnvioHT(vDestinatarios,remBean,txtDocumentos.toString(), htErrores, envBean);
        }
        
        // HAN OCURRIDO ERRORES DE DESTINATARIO
        /////////////////////////////////////
        if (errores){
        	return EnvEstadoEnvioAdm.K_ESTADOENVIO_PROCESADOCONERRORES;
        } else {
            return EnvEstadoEnvioAdm.K_ESTADOENVIO_PROCESADO;
        }

    } catch (SIGAException e) { 
		throw e;
    } catch(Exception e){
        throw new ClsExceptions(e,"Error enviando correo electrónico");
	} finally {
        // cerramos el transport
	    try {
	    	tr.close();
	    } catch (Exception e) {}

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
	* Añade al mensaje un cid:name utilizado para guardar las imagenes referenciadas en el HTML de la forma <img src=cid:name />
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
	* Añade un attachement al mensaje de email
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
		if(descPoblacion!=null)
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
				
	            
	            
	            // Generamos el archivo temporal que se obtendrá de sustituir las etiquetas
	            // de la plantilla
	            String nombreFin = path + File.separator + beanEnvio.getIdInstitucion().toString() + "_" + beanEnvio.getIdEnvio().toString() + "_" + beanDestinatario.getIdPersona();
	            File fIn = new File(nombreFin);
	            
	            // fIN contendrá el archivo obtenido de sustituir las etiquetas a la plantilla.
	            plantilla.sustituirEtiquetas(htDatos, fIn);
	            
	            String nombreFout = path + File.separator + beanDestinatario.getIdPersona() + ".pdf";
//	            File fOut = new File(nombreFout);
	            ficheroSalida = new File(nombreFout);
	            // El path base para los recursos será al path donde se almacena la plantilla
	            
	            try {
	            	plantilla.convertFO2PDF(fIn, ficheroSalida, fPlantilla.getParent());
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
	            
				File crear = new File(path);
				if (!crear.exists())
					crear.mkdirs();
				
	            // Generamos el archivo temporal que se obtendrá de sustituir las etiquetas
	            // de la plantilla
	            String nombreFin =  beanEnvio.getIdInstitucion().toString() + "_" + beanEnvio.getIdEnvio().toString() + "_" + beanDestinatario.getIdPersona()+".doc";
				
	            
				
				ficheroSalida = masterWord.grabaDocumento(documento,path,
						nombreFin);
				
				
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

			// COMPROBACIÓN
			/////////////////////////////////////
			if (!envBean.getIdTipoEnvios().equals(Integer.valueOf(EnvTipoEnviosAdm.K_SMS))){
				throw new ClsExceptions("Tipo de envio electrónico incorrecto");
			}

			ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);

			// BUCLE DE DESTINATARIOS
			/////////////////////////////////////
			if (vDestinatarios!=null) {
				//ACUMULAMOS POBLACIONES, PAISES Y PROVINCIAS PARA EVITAR HACER QUERYS A LA BBDD
				Hashtable htPoblaciones = new Hashtable();
				Hashtable htProvincia = new Hashtable();
				Hashtable htPaises = new Hashtable();
				for (int l=0;l<vDestinatarios.size();l++) {

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
							throw new ClsExceptions("No existen datos de configuración en parámetros para envio SMS.");
						}

						String idPersona = String.valueOf(destBean.getIdPersona());
						String consulta = envBean.getConsulta().equals("")?null:envBean.getConsulta();

						// destinatario
						String sTo = destBean.getMovil();
						sTo = getNumeroConPrefijo(sTo);						
						if (sTo==null || sTo.trim().equals("")) {
							throw new ClsExceptions("No existe número de móvil para el destinatario.");
						}

						// texto
						String sTexto = getTextoSMS(envBean,destBean, Long.valueOf(idPersona),consulta);
						if (sTexto==null || "".equals(sTexto)){
							throw new ClsExceptions("No existe texto del mensaje.");
						}
						if (sTexto.length()>LONGITUD_SMS) 
							sTexto=sTexto.substring(LONGITUD_SMS-1);

						ClsLogging.writeFileLog("locator: " + url_locator,10);
						ClsLogging.writeFileLog("service: " + url_service,10);

						ServiciosECOSService_ServiceLocator locator = new ServiciosECOSService_ServiceLocator();
						ServiciosECOSServiceSOAPStub stub = new ServiciosECOSServiceSOAPStub(new URL(url_service),locator);


						//SMS
						SolicitudEnvioSMS sesms01 = new SolicitudEnvioSMS();
						sesms01.setIdClienteECOS(idClienteECOS);
						sesms01.setIdColegio(destBean.getIdInstitucion().toString());
						sesms01.setListaTOs(new String[] {sTo});
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

						ClsLogging.writeFileLog("Resultado de la operación: " + response03.getResultado(),10);
						if (response03.getResultado().indexOf("KO")!=-1) {
							// ERROR
							String msg = "Comprobar destinos disponibles: España, Francia, Italia, Hungría y Rumanía";
							throw new ClsExceptions("Error en el servicio ECOS. "+response03.getResultado() + ".\t" + msg);	
						}

						ClsLogging.writeFileLog("ID de solicitud: " + response03.getIdSolicitud(),10);

						// RGG 08/06/2009 ESTADISTICA
						EnvEstatEnvioAdm admEstat = new EnvEstatEnvioAdm(this.usrbean);
						admEstat.insertarApunteExtra(envBean.getIdInstitucion(),envBean.getIdEnvio(),envBean.getIdTipoEnvios(),new Long(idPersona),sTo);


					} catch(UnsupportedClassVersionError e){
						ClsExceptions ex = new ClsExceptions("UnsupportedClassVersionError: "+e.toString());
						errores = true;
						insertarMensajeLogHT(destBean,htErrores, ex);
					} catch (Exception e){
						errores = true;
						insertarMensajeLogHT(destBean,htErrores, e);
					} catch (Throwable t){
						errores = true;
						t.printStackTrace();
						insertarMensajeLogHT(destBean,htErrores, new ClsExceptions("ERROR THROWABLE en envíos: "+t.toString()));
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
			throw e;
		} catch(Exception e){
			throw new ClsExceptions(e,"Error enviando correo electrónico");
		} 
	}
	
	public String enviarBuroSMS(EnvEnviosBean envBean,  Vector vDestinatarios, Hashtable htErrores, boolean generarLog) 
	throws SIGAException,ClsExceptions{
    
    boolean errores = false;
    Transport tr = null;
    
    try{
	    EnvEnviosAdm envAdm = new EnvEnviosAdm(this.usrbean);

        // COMPROBACIÓN
        /////////////////////////////////////
        if (!envBean.getIdTipoEnvios().equals(Integer.valueOf(EnvTipoEnviosAdm.K_BUROSMS))){
            throw new ClsExceptions("Tipo de envio electrónico incorrecto");
        }
        

	    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//		ReadProperties rp = new ReadProperties("SIGA.properties");	
		
	   





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
            Row dirPref = getDireccionPreferenteInstitucion(envBean.getIdInstitucion(),TIPO_CORREO_ELECTRONICO);
            sFrom = dirPref.getString(EnvRemitentesBean.C_CORREOELECTRONICO);
        }

        

        
        // BUCLE DE DESTINATARIOS
        /////////////////////////////////////
        if (vDestinatarios!=null) {
        	 //ACUMULAMOS POBLACIONES, PAISES Y PROVINCIAS PARA EVITAR HACER QUERYS A LA BBDD
    	    Hashtable htPoblaciones = new Hashtable();
    	    Hashtable htProvincia = new Hashtable();
    	    Hashtable htPaises = new Hashtable();
	        for (int l=0;l<vDestinatarios.size();l++) {

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
//		    	    //Se especifica la dirección de origen.
////		    	    mensaje.setFrom(new InternetAddress(sFrom));
//		    	    
//		    	    //Se especifica la dirección de destino.
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
//		    	    // Se envía el correo.
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
		        	    throw new ClsExceptions("No existen datos de configuración en parámetros para envio SMS.");
		        	}
		            
		            String idPersona = String.valueOf(destBean.getIdPersona());
		    	    String consulta = envBean.getConsulta().equals("")?null:envBean.getConsulta();

					// destinatario
					String sTo = destBean.getMovil();
					sTo = getNumeroConPrefijo(sTo);						
					if (sTo==null || sTo.trim().equals("")) {
						throw new ClsExceptions("No existe número de móvil para el destinatario.");
					}

		            
		    	    if (sFrom==null || sFrom.trim().equals("")) {
		    	       throw new ClsExceptions("No existe número de email de remitente para la confiración.");
		    	    }
		    	    
					// texto
					String sTexto = getTextoSMS(envBean,destBean, Long.valueOf(idPersona),consulta);
					if (sTexto==null || "".equals(sTexto)){
						throw new ClsExceptions("No existe texto del mensaje.");
					}
					if (sTexto.length()>LONGITUD_SMS) 
						sTexto=sTexto.substring(LONGITUD_SMS-1);						
		            
		            ClsLogging.writeFileLog("locator: " + url_locator,10);
					ClsLogging.writeFileLog("service: " + url_service,10);
					
					ServiciosECOSService_ServiceLocator locator = new ServiciosECOSService_ServiceLocator(url_locator);
					ServiciosECOSServiceSOAPStub stub = new ServiciosECOSServiceSOAPStub(new URL(url_service), locator);
					
					
					//SMS
					SolicitudEnvioSMS sesms01 = new SolicitudEnvioSMS();
					sesms01.setIdClienteECOS(idClienteECOS);
					sesms01.setIdColegio(destBean.getIdInstitucion().toString());
					sesms01.setListaTOs(new String[] {sTo});
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
					
					ClsLogging.writeFileLog("Resultado de la operación: " + response03.getResultado(),10);
	    	        if (response03.getResultado().indexOf("KO")!=-1) {
	    	        	// ERROR
						String msg = "Comprobar destinos disponibles: España, Francia, Italia, Hungría y Rumanía";
						throw new ClsExceptions("Error en el servicio ECOS. "+response03.getResultado() + ".\t" + msg);	
	    	        }

					ClsLogging.writeFileLog("ID de solicitud: " + response03.getIdSolicitud(),10);
							            
		            
		    	    // RGG 08/06/2009 ESTADISTICA
		    	    EnvEstatEnvioAdm admEstat = new EnvEstatEnvioAdm(this.usrbean);
		    	    admEstat.insertarApunteExtra(envBean.getIdInstitucion(),envBean.getIdEnvio(),envBean.getIdTipoEnvios(),new Long(idPersona),sTo);

		    	    
		        } catch(UnsupportedClassVersionError e){
		            ClsExceptions ex = new ClsExceptions("UnsupportedClassVersionError: "+e.toString());
		            errores = true;
	                insertarMensajeLogHT(destBean,htErrores, ex);
	            } catch (Exception e){
	                errores = true;
	                insertarMensajeLogHT(destBean,htErrores, e);
	            } catch (Throwable t){
	                errores = true;
	                t.printStackTrace();
	                insertarMensajeLogHT(destBean,htErrores, new ClsExceptions("ERROR THROWABLE en envíos: "+t.toString()));
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
		throw e;
    } catch(Exception e){
        throw new ClsExceptions(e,"Error enviando correo electrónico");
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

	
    /**
     * Devuelve un numero de movil con el prefijo del pais entre parentesis.
     * Si no tiene prefijo de pais se utiliza el de España
     * @param numero
     * @return
     */
	private String getNumeroConPrefijo(String numero) {
    	String prefijo = "+34";
    	if (numero.startsWith("+")){
    		prefijo = numero.substring(0, 3);
    		numero = numero.substring(3);
    	}
    	numero = "("+prefijo+")"+numero;
    	ClsLogging.writeFileLog("NUMERO DE MOVIL:" + numero,10);    		
    	return numero;
    }


}
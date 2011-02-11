
package com.siga.beans;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.GstDate;
import com.atos.utils.ReadProperties;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.SIGAReferences;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesNumero;
import com.siga.Utilidades.UtilidadesString;
import com.siga.facturacion.form.ContabilidadForm;
import com.siga.facturacionSJCS.UtilidadesFacturacionSJCS;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;


/**
 * Implementa las operaciones sobre la base de datos, es decir: select, insert, update... a la tabla SCS_INSCRIPCIONTURNO
 * 
 * @author carlos.vidal
 * @since 1/11/2004 
 */

public class FacRegistroFichContaAdm extends MasterBeanAdministrador {

	private int asiento = 0;
	private int numeroTransaccion = 200;
	// MAV 6/9/2005  Solucion incidencia /////
	private String SEPARADOR = "\t";	 // private String SEPARADOR = ",";
	//////////////////////////////////////////
	
	private String CONTABILIDAD_VENTAS 		= "";
	private String CONTABILIDAD_IVA 		= "";
	private String CONTABILIDAD_TARJETAS 	= "";
	private String CONTABILIDAD_CAJA 		= "";
	private String CONTAB_CAJA_ABONO		= "";	
	private String CONTABILIDAD_COMPENSACION = "";
	private String CONTABILIDAD_CAJA_ANTICIPOS	= "";
	private String CONTABILIDAD_INGRESOS_EXTRA	= "";
	private String ANTICIPOS_CLI 			= "";
	private String CONTABILIDAD_GASTOSBANCARIOS	= "";
	private String CONTABILIDAD_DEVOL_FACTURAS	= "";
	private String CONTAB_SERVICIOS_PROFESIONALES	= "";
	private String CONTAB_MOVIMIENTOS_VARIOS	= "";
	private String CONTAB_MOV_VARIOS_NEGATIVOS	= "";
	private String CONTAB_IRPF	= "";
	private String CONTAB_RETENCIONESJUDICIALES	= "";
	private String CONTAB_VENTAS_PRODUCTOS	= "";
	private String CONTAB_VENTAS_SERVICIOS	= "";
	private String CONCEPTO_ASIENTO1		= "general.literal.asiento1"; 	// Factura Nº
	private String CONCEPTO_ASIENTO2		= "general.literal.asiento2"; 	// Abono Nº
	private String CONCEPTO_ASIENTO3		= "general.literal.asiento3"; 	// Pago por caja. Factura Nº
	private String CONCEPTO_ASIENTO3_2		= "general.literal.asiento3_2"; 	// Pago por caja. Factura Nº
	private String CONCEPTO_ASIENTO4		= "general.literal.asiento4"; 	// Pago por banco. Factura Nº
	private String CONCEPTO_ASIENTO5		= "general.literal.asiento5"; 	// Devolucion por banco. Factura Nº
	private String CONCEPTO_ASIENTO6		= "general.literal.asiento6"; 	// Pago por caja. Abono Nº
	private String CONCEPTO_ASIENTO7		= "general.literal.asiento7"; 	// Pago por banco. Abono Nº
	private String CONCEPTO_ASIENTO10		= "general.literal.asiento3_1"; // Pago por tarjeta. Factura Nº
	private String CONCEPTO_ASIENTO3_2010   = "general.literal.asiento3_2010"; // Compensación por caja
	//private String CONCEPTO_ASIENTO12		= "general.literal.asiento11"; // Factura Servicios
	//private String CONCEPTO_ASIENTO11		= "general.literal.asiento12"; // Factura Productos
	private String CONCEPTO_ASIENTO8		= "general.literal.asiento8"; // Reparto Pagos justicia gratuita
	private String CONCEPTO_ASIENTO9		= "general.literal.asiento9"; // Pagos Justicia gratuita por banco
	private String CONCEPTO_ASIENTO13		= "general.literal.asiento13"; // Pagos Justicia gratuita por caja 
	
	private String CONCEPTO_ASIENTO14		= "general.literal.asiento14"; // Alta de anticipos 
	private String CONCEPTO_ASIENTO15		= "general.literal.asiento15"; // Liquidación de anticipos 
	
	
	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public FacRegistroFichContaAdm (UsrBean usuario) {
		super( FacRegistroFichContaBean.T_NOMBRETABLA, usuario);
	}

	/** Funcion getCamposTabla ()
	 * Devuelve los campos que queremos recuperar desde el select
	 * para rellenar la tabla de la página "listarTurnos.jsp"
	 * 
	 * @return String campos que recupera desde la select
	 */
	protected String[] getCamposTabla(){
		String[] campos = {	FacRegistroFichContaBean.T_NOMBRETABLA+"."+FacRegistroFichContaBean.C_IDCONTABILIDAD+" "+FacRegistroFichContaBean.C_IDCONTABILIDAD,
				FacRegistroFichContaBean.T_NOMBRETABLA+"."+FacRegistroFichContaBean.C_FECHACREACION+" "+FacRegistroFichContaBean.C_FECHACREACION,
				FacRegistroFichContaBean.T_NOMBRETABLA+"."+FacRegistroFichContaBean.C_FECHADESDE+" "+FacRegistroFichContaBean.C_FECHADESDE,					
				FacRegistroFichContaBean.T_NOMBRETABLA+"."+FacRegistroFichContaBean.C_FECHAHASTA+" "+FacRegistroFichContaBean.C_FECHAHASTA,
				FacRegistroFichContaBean.T_NOMBRETABLA+"."+FacRegistroFichContaBean.C_NUMEROASIENTOS+" "+FacRegistroFichContaBean.C_NUMEROASIENTOS,
				FacRegistroFichContaBean.T_NOMBRETABLA+"."+FacRegistroFichContaBean.C_NOMBREFICHERO+" "+FacRegistroFichContaBean.C_NOMBREFICHERO,
				FacRegistroFichContaBean.T_NOMBRETABLA+"."+FacRegistroFichContaBean.C_ESTADO+" "+FacRegistroFichContaBean.C_ESTADO,
				FacRegistroFichContaBean.T_NOMBRETABLA+"."+FacRegistroFichContaBean.C_IDINSTITUCION+" "+FacRegistroFichContaBean.C_IDINSTITUCION};
		return campos;
	}
	
	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * 
	 */
	protected String[] getCamposBean() {
		String[] campos = {	FacRegistroFichContaBean.C_IDCONTABILIDAD,	FacRegistroFichContaBean.C_FECHACREACION,
				FacRegistroFichContaBean.C_FECHADESDE,					FacRegistroFichContaBean.C_FECHAHASTA,
				FacRegistroFichContaBean.C_NUMEROASIENTOS,				FacRegistroFichContaBean.C_FECHAMODIFICACION,
				FacRegistroFichContaBean.C_USUMODIFICACION,				FacRegistroFichContaBean.C_NOMBREFICHERO,			
				FacRegistroFichContaBean.C_ESTADO,				FacRegistroFichContaBean.C_IDINSTITUCION};
		return campos;
	}
	
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 * 
	 */
	protected String[] getClavesBean() {
		String[] campos = {	FacRegistroFichContaBean.C_IDINSTITUCION, FacRegistroFichContaBean.C_IDCONTABILIDAD};
		return campos;
	}
	
	/** Funcion getClavesTabla ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 *  con formato "NombreTabla.NombreCampo"
	 * 
	 */
	protected String[] getClavesTabla() {
		String[] campos = {	FacRegistroFichContaBean.T_NOMBRETABLA+"."+FacRegistroFichContaBean.C_IDINSTITUCION, 	
				FacRegistroFichContaBean.T_NOMBRETABLA+"."+FacRegistroFichContaBean.C_IDCONTABILIDAD};
		return campos;
	}

	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * 
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		FacRegistroFichContaBean bean = null;
		try{
			bean = new FacRegistroFichContaBean();
			bean.setIdContabilidad(UtilidadesHash.getInteger(hash,FacRegistroFichContaBean.C_IDCONTABILIDAD));
			bean.setFechaCreacion(UtilidadesHash.getString(hash,FacRegistroFichContaBean.C_FECHACREACION));
			bean.setFechaDesde(UtilidadesHash.getString(hash,FacRegistroFichContaBean.C_FECHADESDE));
			bean.setFechaHasta(UtilidadesHash.getString(hash,FacRegistroFichContaBean.C_FECHAHASTA));
			bean.setNumeroAsientos(UtilidadesHash.getInteger(hash,FacRegistroFichContaBean.C_NUMEROASIENTOS));
			bean.setNombreFichero(UtilidadesHash.getString(hash,FacRegistroFichContaBean.C_NOMBREFICHERO));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,FacRegistroFichContaBean.C_IDINSTITUCION));
			bean.setEstado(UtilidadesHash.getInteger(hash,FacRegistroFichContaBean.C_ESTADO));
		}
		catch(Exception e){
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	/** Funcion beanToHashTable (MasterBean bean)
	 *  @param bean para crear el hashtable asociado
	 *  @return hashtable con la información del bean
	 * 
	 */
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try{
			hash = new Hashtable();
			FacRegistroFichContaBean b = (FacRegistroFichContaBean) bean;
			hash.put(FacRegistroFichContaBean.C_IDCONTABILIDAD,b.getIdContabilidad());
			hash.put(FacRegistroFichContaBean.C_FECHACREACION ,b.getFechaCreacion());
			hash.put(FacRegistroFichContaBean.C_FECHADESDE    ,b.getFechaDesde());
			hash.put(FacRegistroFichContaBean.C_FECHAHASTA    ,b.getFechaHasta());
			hash.put(FacRegistroFichContaBean.C_NUMEROASIENTOS,b.getNumeroAsientos());
			hash.put(FacRegistroFichContaBean.C_NOMBREFICHERO ,b.getNombreFichero());
			hash.put(FacRegistroFichContaBean.C_IDINSTITUCION ,b.getIdInstitucion());
			hash.put(FacRegistroFichContaBean.C_ESTADO,b.getEstado());
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	}

	/** Funcion getOrdenCampos ()
	 *  @return String[] conjunto de valores con los campos por los que se deberá ordenar la select
	 *  que se ejecute sobre esta tabla
	 */
	protected String[] getOrdenCampos(){
	    String[] campos = {	FacRegistroFichContaBean.T_NOMBRETABLA+"."+FacRegistroFichContaBean.C_FECHACREACION + " DESC "};
		return campos;
	}

	/** Funcion select(String where)
	 *	@param where clausula "where" de la sentencia "select" que queremos ejecutar
	 *  @return Vector todos los registros que se seleccionen en BBDD 
	 *  
	 *
	 */
	public Vector select(String where) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			String sql = UtilidadesBDAdm.sqlSelect(this.nombreTabla, this.getCamposTabla());
			sql += where;
			sql += this.getOrdenCampos()!=null ? UtilidadesBDAdm.sqlOrderBy(this.getOrdenCampos()) : UtilidadesBDAdm.sqlOrderBy(this.getClavesBean());

			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					MasterBean registro = (MasterBean) this.hashTableToBean(fila.getRow()); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
	}
	
	public Vector selectGenericaBind(String where, Hashtable data) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			String sql = UtilidadesBDAdm.sqlSelect(this.nombreTabla, this.getCamposBean());
			sql += " " + where;
			sql += this.getOrdenCampos()!=null ? UtilidadesBDAdm.sqlOrderBy(this.getOrdenCampos()) : UtilidadesBDAdm.sqlOrderBy(this.getClavesBean());
			
			if (rc.queryBind(sql,data)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					MasterBean registro = (MasterBean) this.hashTableToBeanInicial(fila.getRow()); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el \"select\" en B.D."); 
		}
		return datos;
	}	
	
	
	
	/**
	 * Efectúa un SELECT en la tabla FAC_REGISTROFICHCONTA con los datos introducidos. 
	 * 
	 * @param sql. Consulta a realizar
	 * @return Vector de Hashtable con los registros que cumplan la sentencia sql 
	 */
	public Vector selectTabla(String sql) throws ClsExceptions {
		Vector v = new Vector();
		RowsContainer rc = null;
		try{
			rc = new RowsContainer(); 
			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow(); 
					if (registro != null) 
						v.add(registro);
				}
			}
		}
		catch(ClsExceptions e){
			throw e;
		}
		return v;
	}
	public Vector selectTablaBind(String sql, Hashtable codigos) throws ClsExceptions {
		Vector v = new Vector();
		RowsContainer rc = null;
		try{
			rc = new RowsContainer(); 
			if (rc.queryBind(sql, codigos)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow(); 
					if (registro != null) 
						v.add(registro);
				}
			}
		}
		catch(ClsExceptions e){
		    throw e;
		}
		return v;
	}
	
	
	/// traido desde MantContabilidadAction
	
	public void contabilidadesProgramadas(HttpServletRequest request, String idInstitucion) throws SIGAException, ClsExceptions
	{
	    try {
	       Hashtable codigos = new Hashtable();
	       codigos.put(new Integer(1),idInstitucion);
	       codigos.put(new Integer(2),"1"); // ESTADO PROGRAMADO
	       Vector v = this.selectBind("WHERE IDINSTITUCION=:1 AND ESTADO=:2",codigos);
	       if (v!=null && v.size()>0) {
	           for (int i=0;i<v.size();i++) {
		           FacRegistroFichContaBean bean = (FacRegistroFichContaBean) v.get(i);
		           // Genero los multiples ficheros pendientes
	               this.generarFicheroContabilidad(bean);
	           }
	       }
	    
		} catch (Exception e) {
		    throw new ClsExceptions(e,"Error al lanzar las contabilidades programadas");
		}
	}

	public void crearContabilidad(String fechaDesde, String fechaHasta) throws SIGAException, ClsExceptions
	{
		try
		{
			// Obtenemos el siguiente idcontabilidad
			String sql = "SELECT nvl(MAX(IDCONTABILIDAD+1),0) IDCONTABILIDAD FROM Fac_Registrofichconta WHERE IDINSTITUCION ="+this.usrbean.getLocation();
			Vector vResultado		=(Vector)this.selectTabla(sql);
			Hashtable h 			= (Hashtable) vResultado.get(0);
			String idcontabilidad 	= (String)h.get("IDCONTABILIDAD");

			// Creamos la fecha actual
			java.text.SimpleDateFormat formador = new java.text.SimpleDateFormat("dd-MM-yyyy");
			String fecha = formador.format(new Date());
			String nombreFichero = idcontabilidad+"_"+fecha+".xls";

			// Insertamos el registro en la tabla de ficheros de contabilidad
			Hashtable laHash = new Hashtable();
			laHash.put(FacRegistroFichContaBean.C_FECHACREACION		,"sysdate");
			laHash.put(FacRegistroFichContaBean.C_FECHADESDE		,GstDate.getApplicationFormatDate("ES",fechaDesde));
			laHash.put(FacRegistroFichContaBean.C_FECHAHASTA		,GstDate.getApplicationFormatDate("ES",fechaHasta));
			laHash.put(FacRegistroFichContaBean.C_IDCONTABILIDAD	,idcontabilidad);
			laHash.put(FacRegistroFichContaBean.C_IDINSTITUCION		,this.usrbean.getLocation());
			laHash.put(FacRegistroFichContaBean.C_NOMBREFICHERO		,nombreFichero);
			laHash.put(FacRegistroFichContaBean.C_NUMEROASIENTOS	,String.valueOf(asiento));
			laHash.put(FacRegistroFichContaBean.C_ESTADO 			,"1"); // programado
			if (!this.insert(laHash)) {
			    throw new ClsExceptions("Error al crear el registro de contabilidad: "+this.getError());
			}
		} catch (Exception e) {
		    throw new ClsExceptions(e,"Error al crear el registro de contabilidad");
		}
	}

	/**
	 * 
	 * @param fechaDesde
	 * @param fechaHasta
	 * @param usr
	 * @return
	 * @throws SIGAException
	 */
	public boolean generarFicheroContabilidad(FacRegistroFichContaBean beanContab) throws SIGAException, ClsExceptions
	{
		boolean correcto = false;
		UserTransaction tx = null;
		java.io.PrintWriter pwcontabilidad=null;
		java.io.FileOutputStream focontabilidad = null;
		tx = this.usrbean.getTransactionPesada(); 
		
		try
		{
			String idcontabilidad 	= beanContab.getIdContabilidad().toString();

			// PREPARAMOS EL FICHERO
			// Se crea el directorio en el servidor web.
		    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//			ReadProperties rp = new ReadProperties("SIGA.properties");
			String sRutaFisicaJava = rp.returnProperty("contabilidad.directorioFisicoContabilidad");
			String directorio = sRutaFisicaJava+File.separator+this.usrbean.getLocation();
			File fDirectorio = new File(directorio);
			fDirectorio.mkdirs();

			String nombreFichero = beanContab.getNombreFichero();
			String fechaDesde = GstDate.getFormatedDateShort("ES",beanContab.getFechaDesde());
			String fechaHasta = GstDate.getFormatedDateShort("ES",beanContab.getFechaHasta());

			String contabilidad  = sRutaFisicaJava+File.separator+beanContab.getIdInstitucion()+File.separator+nombreFichero;
			focontabilidad 	= new java.io.FileOutputStream(contabilidad);
			pwcontabilidad 	= new java.io.PrintWriter(focontabilidad);

			this.crearCuentas(beanContab.getIdInstitucion().toString(),this.usrbean);

			asiento = 0;
			
			pwcontabilidad.write(UtilidadesString.getMensajeIdioma("1","general.literal.cabeceraAsiento.asiento") + SEPARADOR +
					UtilidadesString.getMensajeIdioma("1","general.literal.cabeceraAsiento.fecha") + SEPARADOR +
					UtilidadesString.getMensajeIdioma("1","general.literal.cabeceraAsiento.cuenta") + SEPARADOR +
					UtilidadesString.getMensajeIdioma("1","general.literal.cabeceraAsiento.concepto") + SEPARADOR +
					UtilidadesString.getMensajeIdioma("1","general.literal.cabeceraAsiento.documento") + SEPARADOR +
					UtilidadesString.getMensajeIdioma("1","general.literal.cabeceraAsiento.debe") + SEPARADOR +
					UtilidadesString.getMensajeIdioma("1","general.literal.cabeceraAsiento.haber") + SEPARADOR +
					UtilidadesString.getMensajeIdioma("1","general.literal.cabeceraAsiento.baseImponible") + SEPARADOR +
					UtilidadesString.getMensajeIdioma("1","general.literal.cabeceraAsiento.tipoIVA") + SEPARADOR +
					UtilidadesString.getMensajeIdioma("1","general.literal.cabeceraAsiento.contrapartida") + "\n");
			/////////////////////////
			
			//le cambio el estado a en proceso
			tx.begin();
			beanContab.setEstado(new Integer(FacRegistroFichContaBean.ESTADO_ENPROCESO));
		    if (!this.updateDirect(beanContab)) {
		        throw new ClsExceptions("Error al actualizar el estado de la contabildiad. en proceso. "+this.getError());
		    }
		    tx.commit();

			
			// RGG 24/10/2007 
			// SE PONEN COMMITS INTERNEDIOS PARA QUE EL PROCESO AGUANTE EL TIEMPO QUE TARDA.
			
			
			// -----------------------------------------------------------------------------------------------------------------
			// RGG 03/11/2006 Asiento 1 
			// FACTURA 
			// Ventas (700)	 			--> Bruto por servicio (1) --> 0
			// IVA 				 	--> Iva (2)		     --> 0
			// Cliente(430.xxx)			--> 0		            --> Negocio(1)+Negocio(2)
			ClsLogging.writeFileLog("********* CONTABILIDAD ASIENTO 1",10);
			pwcontabilidad=generaAsiento1(pwcontabilidad,fechaDesde,fechaHasta,tx);
			

			/////////////////////////////////////////////
			// RGG 20/11/2007 esto ya no se apunta asi
			/*
			// RGG 03/11/2006 Asiento 12 
			// FACTURA SERVICIOS
			// 1------> servicios
			// Ingresos. prest. serv. (705.xxx) --> Bruto por servicio (1) 	--> 0
			// Cliente(430.xxx)					--> 0						--> Negocio(1)
			pwcontabilidad=generaAsiento12(pwcontabilidad,fechaDesde,fechaHasta,tx);

			// RGG 03/11/2006 Asiento 11
			// FACTURA PRODUCTOS
			// 11------> productos
			// Ventas de productos    (701.xxx) --> Bruto por producto (1) 	--> 0
			// Cliente(430.xxx)					--> 0						--> Negocio(1)
			pwcontabilidad=generaAsiento11(pwcontabilidad,fechaDesde,fechaHasta,tx);
			////////////////////////////////////////////
			*/
			
			// -----------------------------------------------------------------------------------------------------------------
			// RGG 03/11/2006 Asiento 2 
			// ABONO (ASOCIADO A DEVOLUCION) 
			// 2------>
			// Cliente(430.xxxx)            --> abono(1)+abono(2)	--> 0
			// Devolucion factura (708)		--> 0					--> abono(1)
			// IVA (477)					--> 0					--> abono(2)
			
			ClsLogging.writeFileLog("********* CONTABILIDAD ASIENTO 2",10);
			pwcontabilidad=generaAsiento2(pwcontabilidad,fechaDesde,fechaHasta,tx);
			
			
			// -----------------------------------------------------------------------------------------------------------------
			// RGG 03/11/2006 Asiento 2b 
			// ABONO (POR IMPORTE EXCESIVO EN FACTURA) 
			// 2------>
			// Cliente(430.xxxx)            --> abono(1)+abono(2)	--> 0
			// Devolucion factura (708)		--> 0					--> abono(1)
			// IVA (477)					--> 0					--> abono(2)
			ClsLogging.writeFileLog("********* CONTABILIDAD ASIENTO 2B",10);
			pwcontabilidad=generaAsiento2b(pwcontabilidad,fechaDesde,fechaHasta,tx);
			
			// -----------------------------------------------------------------------------------------------------------------
			// RGG 03/11/2006 Asiento 3
			// PAGO POR CAJA (ABONOS) 
			// 3------> HABER --> BEDE
			// Cliente(430.xxx ) 	-->  pagoporcaja --> 0
			// Caja(570) 		 	-->  0			 --> pagoporcaja
			// Caja Anticipos (438) -->  0			 --> pagoporcaja  || RGG 24/03/2009
			// OJO, parece que se estaban metiendo tambien los de pago por tarjeta
			ClsLogging.writeFileLog("********* CONTABILIDAD ASIENTO 3",10);
			pwcontabilidad=generaAsiento3(pwcontabilidad,fechaDesde,fechaHasta,tx);

			// -----------------------------------------------------------------------------------------------------------------
			// RGG 03/11/2006 Asiento 4 
			// PAGO POR BANCO 
			// 4------> Banco
			// Cliente(430.xxx) 	-->  facturaincluidaendisquete 	--> 0
			// Banco(572.1xxx) 		-->  0			 				--> facturaincludidaendisquete
			ClsLogging.writeFileLog("********* CONTABILIDAD ASIENTO 4",10);
			pwcontabilidad=generaAsiento4(pwcontabilidad,fechaDesde,fechaHasta,tx);

			// -----------------------------------------------------------------------------------------------------------------
			// RGG 03/11/2006 Asiento 10 
			// PAGO POR TARJETA 
			// 10------> Tarjeta
			// Cliente(430.xxx) 	-->  facturaincluidaendisquete 	--> 0
			// Banco(572.2xxx) 		-->  0			 				--> facturaincludidaendisquete
			ClsLogging.writeFileLog("********* CONTABILIDAD ASIENTO 10",10);
			pwcontabilidad=generaAsiento10(pwcontabilidad,fechaDesde,fechaHasta,tx);

			// -----------------------------------------------------------------------------------------------------------------
			// RGG 03/11/2006 Asiento 5
			// DEVOLUCION DE FACTURA POR BANCO 
			// 5------>
			// Banco(572.1xxxx) 			-->  facturaincluidaendisquete --> 0
			// Cliente(430.xxx)				-->  0			 			   --> facturaincludidaendisquete
			// Si hay gastos bancarios
			// Banco(572.1xxxx) 			-->  GASTOSDEVOLUCION 			--> 0
			// Gastos bancarios(626)		-->  0			 				--> GASTOSDEVOLUCION
			ClsLogging.writeFileLog("********* CONTABILIDAD ASIENTO 5",10);
			pwcontabilidad=generaAsiento5(pwcontabilidad,fechaDesde,fechaHasta,tx);
			
			// -----------------------------------------------------------------------------------------------------------------
			// RGG 03/11/2006 Asiento 14
			// ALTA DE ANTICIPOS DE SERVICIOS. 
			// 14------> HABER --> BEDE
			// Caja anticipos (572.3xxxx) 	-->  importe del anticipo 	--> 0
			// Anticipos Cliente(438.xxx)	-->  0			 			--> importe del anticipo
			ClsLogging.writeFileLog("********* CONTABILIDAD ASIENTO 14",10);
			pwcontabilidad=generaAsiento14(pwcontabilidad,fechaDesde,fechaHasta,tx);
			
			// -----------------------------------------------------------------------------------------------------------------
			// RGG 03/11/2006 Asiento 15
			// LIQUIDACION DE ANTICIPOS DE SERVICIOS AL COLEGIO. 
			// 14------> HABER --> BEDE
			// Anticipos Cliente(438.xxx) 			-->  importe de liquidacion	--> 0
			// Ingresos extraordinarios (778xxxx)	-->  0			 			--> importe de liquidacion
			ClsLogging.writeFileLog("********* CONTABILIDAD ASIENTO 15",10);
			pwcontabilidad=generaAsiento15(pwcontabilidad,fechaDesde,fechaHasta,tx);
			
			// -----------------------------------------------------------------------------------------------------------------
			// RGG 03/11/2006 Asiento 6
			// PAGO POR CAJA: ABONO 
			// 6------>
			// Caja(570) 		-->  pagoabonoefectivo 	--> 0
			// Cliente(430.xxx)	-->  0			 		--> pagoabonoefectivo
			
			// jbd // Segun peticion de Javier Jimenez (Badajoz) sobran los pagos por caja de abono (Quitar los asientos de "Pago por caja. Abono")
			if(beanContab.getIdInstitucion()!=2010){
				pwcontabilidad=generaAsiento6(pwcontabilidad,fechaDesde,fechaHasta,tx);
			}
			
			// -----------------------------------------------------------------------------------------------------------------
			// RGG 03/11/2006 Asiento 7
			// PAGO POR BANCO (ABONOS) 
			// 7------>
			// Banco (572.1xxx) 		-->  pagoabonobanco	 	--> 0
			// Cliente(430.xxx)			-->  0			 		--> pagoabonobanco
			pwcontabilidad=generaAsiento7(pwcontabilidad,fechaDesde,fechaHasta,tx);

/* RGG 29/11/2007 SE QUITAN HASTA QUE SE HAGAN BIEN Y SE PONGAN EN SU MENU NUEVO

						// -----------------------------------------------------------------------------------------------------------------
			// RGG 03/11/2006 PAGOS DE JUSTICIA GRATUITA
			// 8 ---> reparto pagos sjcs
			// nomenclatura de fichero: #cuenta , haber, debe
			// Serv. prof. indep.(623) -->  0                   --> pago bruto por persona
			// Movim. varios (623.1)   -->  Movs. negativos     --> Movs. positivos
			// Accredores    (410.xxx) -->  Pago neto por per.  --> 0
			// IRPF          (475)     -->  Retencion IRPF per. --> 0
			// Ret. Judiciales (623.3) -->  Valor reten.        --> 0
			pwcontabilidad=generaAsiento8(pwcontabilidad,fechaDesde,fechaHasta,tx);
			
			// -----------------------------------------------------------------------------------------------------------------
			// RGG 03/11/2006 PAGOS DE JUSTICIA GRATUITA BANCO
			// 9 ---> pago de sjcs por cuenta bancaria
			// nomenclatura de fichero: #cuenta , haber, debe
			// Accredores    (410.xxx) -->  0                   --> Pago neto por per.
			// Banco         (572.1xxx)   -->  Pago neto por per.  --> 0
			pwcontabilidad=generaAsiento9(pwcontabilidad,fechaDesde,fechaHasta,tx);
			
			// -----------------------------------------------------------------------------------------------------------------
			// RGG 03/11/2006 PAGOS DE JUSTICIA GRATUITA CAJA
			// 13 ---> pago de sjcs por caja
			// nomenclatura de fichero: #cuenta , haber, debe
			// Accredores    (410.xxx) 	-->  0                   --> Pago neto por per.
			// Caja         (570)   	-->  Pago neto por per.  --> 0
			pwcontabilidad=generaAsiento13(pwcontabilidad,fechaDesde,fechaHasta,tx);
			
*/			
			
			
			ClsLogging.writeFileLog("********* FIN DE CONTABILIDAD",10);
			
			
			// Cerramos el fichero y guardamos un registro en la tabla de fichero.
			correcto = true;
			pwcontabilidad.close();
			
			//le cambio el estado a terminado
			tx.begin();
			beanContab.setEstado(new Integer(FacRegistroFichContaBean.ESTADO_TERMINADO));
			beanContab.setNumeroAsientos(new Integer(asiento));
		    if (!this.updateDirect(beanContab)) {
		        throw new ClsExceptions("Error al actualizar el estado de la contabildiad. terminado. "+this.getError());
		    }
		    tx.commit();

		}
		catch (Exception e) 
		{
			//le cambio el estado a error
			try{ tx.begin(); } catch (Exception e3) {};
			beanContab.setEstado(new Integer(FacRegistroFichContaBean.ESTADO_ERROR));
		    if (!this.updateDirect(beanContab)) {
		        throw new ClsExceptions("Error al actualizar el estado de la contabildiad. error. "+this.getError());
		    }
			try{ tx.commit(); } catch (Exception e3) {};
		    
		    try { pwcontabilidad.close(); } catch (Exception e2) {};
			throw new ClsExceptions(e,"Error al generar elficheo de contabilidad.");
		}
		finally {
			try {
				focontabilidad.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return correcto;
	}

	
	/**
	 * 
	 * @param pwcontabilidad
	 * @param fechaDesde
	 * @param fechaHasta
	 * @param usr
	 * @return
	 * @throws SIGAException
	 */
	// FACTURA
	private java.io.PrintWriter generaAsiento1(java.io.PrintWriter pwcontabilidad, String fechaDesde, String fechaHasta, UserTransaction tx) throws SIGAException, ClsExceptions
	{
	    
	    // RGG 20/11/2007 ULTIMO CAMBIO APLICADO A CONTABILIDAD.  AHORA SE HACE A NIVEL DE LINEA DE FACTURA
	    
		// Tipos de asientos.
		// nomenclatura de fichero: #cuenta , haber, debe
		// 1------> 
		// Ventas. (700) 					--> Bruto por servicio (1) 	--> 0
		// IVA 								--> Iva (2)					--> 0
		// Cliente(430.xxx)					--> 0						--> Negocio(1)+Negocio(2)
		String concepto 		= "";
		String asientoClientes 	= ""; 
		String asientoIngresos 	= ""; 
		String asientoIVA 	= ""; 
		// Variables
		Vector vResultado = null, vLineasFacturas = null, vContabilidad = null;
		Hashtable hash = null, laHash = null;
		String select = null, sql = null, imp = null, importeIva = null;
		Vector vAsiento1=new Vector();
		// Beans
		FacFacturaAdm facturaAdm 	= new FacFacturaAdm(this.usrbean);
		
		int contador = 0;             
        Hashtable codigos = new Hashtable();
        
		if(vContabilidad !=null && vContabilidad.size()>0)	CONTABILIDAD_IVA = ((GenParametrosBean) vContabilidad.get(0)).getValor();
		try
		{
		    // RGG 21/11/2007
			select = " SELECT F.IDFACTURA,F.NUMEROFACTURA, (L.CANTIDAD * L.PRECIOUNITARIO) IMPNETO, ((L.CANTIDAD * L.PRECIOUNITARIO) * (L.IVA/100)) IMPIVA, L.IVA, DECODE(F.IDPERSONADEUDOR,NULL,F.IDPERSONA,F.IDPERSONADEUDOR) IDPERSONA, F.FECHAEMISION, " +
			" L.DESCRIPCION, L.CTAPRODUCTOSERVICIO, L.CTAIVA, P.CONFDEUDOR, P.CONFINGRESOS, P.CTAINGRESOS, P.CTACLIENTES " +
			" FROM FAC_FACTURA F, FAC_LINEAFACTURA L, FAC_FACTURACIONPROGRAMADA P " +
			" WHERE F.IDINSTITUCION = L.IDINSTITUCION " + 
			" AND   F.IDFACTURA = L.IDFACTURA " + 
			" AND F.IDINSTITUCION = P.IDINSTITUCION " +
			" AND F.IDSERIEFACTURACION = P.IDSERIEFACTURACION " +
			" AND F.IDPROGRAMACION = P.IDPROGRAMACION " ;
			contador++;
			codigos.put(new Integer(contador),this.usrbean.getLocation());
			select+=" AND F.IDINSTITUCION = :" + contador +
			        " AND F.NUMEROFACTURA IS NOT NULL ";

			/*if(!fechaDesde.equals("") && !fechaHasta.equals(""))
				select+=" AND F.FECHAEMISION >= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaDesde)+"','YYYY/MM/DD hh24:mi:ss') AND F.FECHAEMISION <= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaHasta)+"','YYYY/MM/DD hh24:mi:ss')";
			else if(!fechaDesde.equals(""))
				select+=" AND F.FECHAEMISION >= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaDesde)+"','YYYY/MM/DD hh24:mi:ss')";
			else if(!fechaHasta.equals(""))
				select+=" AND F.FECHAEMISION <= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaHasta)+"','YYYY/MM/DD hh24:mi:ss')";*/
			
			
			String fDesdeInc = fechaDesde; 
			   String fHastaInc = fechaHasta;
				if ((fDesdeInc != null && !fDesdeInc.trim().equals("")) || (fHastaInc != null && !fHastaInc.trim().equals(""))) {
					
					if (!fDesdeInc.equals(""))
						fDesdeInc = GstDate.getApplicationFormatDate("", fDesdeInc); 
					if (!fHastaInc.equals(""))
						fHastaInc = GstDate.getApplicationFormatDate("", fHastaInc);
					Vector vCondicion=GstDate.dateBetweenDesdeAndHastaBind("F.FECHAEMISION", fDesdeInc, fHastaInc,contador,codigos);
					
					contador=new Integer(vCondicion.get(0).toString()).intValue();
					select +=" AND " + vCondicion.get(1) ;
				}	
			
			select+=" ORDER BY F.IDFACTURA ";
			
			//vLineasFacturas=(Vector)adm.selectTabla(select);
			vLineasFacturas=(Vector)this.selectTablaBind(select,codigos);
			
			String idFacturaAnt = ""; 
			String idFactura = "";  
			 
			for(int x=0;x<vLineasFacturas.size();x++)
			{
			    hash = (Hashtable) vLineasFacturas.get(x);
				
			    idFactura = (String)hash.get("IDFACTURA");
				    
				// PARA CADA LINEA FACTURA COMPRUEBO LA CONFIGURACIÓN DE CUENTAS
			    String confClientes=(String)hash.get("CONFDEUDOR");
			    String confIngresos=(String)hash.get("CONFINGRESOS");
			    String ctaClientes=(String)hash.get("CTACLIENTES");
			    String ctaIngresos=(String)hash.get("CTAINGRESOS");
			 
			        
			    // importes
			    imp=UtilidadesNumero.redondea((String)hash.get("IMPNETO"), 2);
			    importeIva= UtilidadesNumero.redondea((String)hash.get("IMPIVA"), 2);
			    String porcentajeIva= UtilidadesNumero.redondea((String)hash.get("IVA"), 2);
			   
			    
			    
			    // Control de iva 0
			    boolean ivacero=false;
			    try {
			        Double d = new Double(porcentajeIva);
			        if (d.doubleValue()==0.0) 
			            ivacero=true;
			    } catch (NumberFormatException nf) {
			    }
			    
			    // concepto
				concepto = UtilidadesString.sustituirParaExcell(UtilidadesString.getMensajeIdioma(this.usrbean,CONCEPTO_ASIENTO1) + ": "+(String)hash.get("DESCRIPCION"));
				
				// asientos configurables
				asientoIVA = (String)hash.get("CTAIVA");
				if (confClientes.equals("F")) {
				    asientoClientes =  ctaClientes;
				} else {
				    asientoClientes =  ctaClientes + obtenerAsientoContable(this.usrbean.getLocation(),(String)hash.get("IDPERSONA"));
				}
				if (confIngresos.equals("F")) {
				    asientoIngresos =  ctaIngresos;
				} else if (confIngresos.equals("C")) {
				    asientoIngresos =  ctaIngresos + obtenerAsientoContable(this.usrbean.getLocation(),(String)hash.get("IDPERSONA"));
				} else {
				    asientoIngresos =  ctaIngresos + (String)hash.get("CTAPRODUCTOSERVICIO");
				}
				
				
				// aumentamos el contador de asientos
				asiento++;
				
				Hashtable a = new Hashtable();
				
				// Escribimos 1º APUNTE
				a.clear();
				UtilidadesHash.set(a, "FECHA", 			UtilidadesHash.getShortDate(hash, FacFacturaBean.C_FECHAEMISION));
				UtilidadesHash.set(a, "CONCEPTO", 		concepto);
				UtilidadesHash.set(a, "DOCUMENTO", 		UtilidadesHash.getString(hash, "NUMEROFACTURA"));
				UtilidadesHash.set(a, "CUENTA", 		asientoClientes);
				UtilidadesHash.set(a, "DEBE", 			"" + (Double.parseDouble(imp) + Double.parseDouble(importeIva)));
				UtilidadesHash.set(a, "HABER", 			"0");
				UtilidadesHash.set(a, "BASEIMPONIBLE", 	"");
				UtilidadesHash.set(a, "IVA", 			"");
				UtilidadesHash.set(a, "CONTRAPARTIDA", 	asientoIngresos);
				pwcontabilidad.write(this.generarLineaAbono(asiento, a));
				
				// Escribimos 2º APUNTE
				a.clear();
				UtilidadesHash.set(a, "FECHA", 			UtilidadesHash.getShortDate(hash, FacFacturaBean.C_FECHAEMISION));
				UtilidadesHash.set(a, "CONCEPTO", 		concepto);
				UtilidadesHash.set(a, "DOCUMENTO", 		UtilidadesHash.getString(hash, "NUMEROFACTURA"));
				UtilidadesHash.set(a, "CUENTA", 		asientoIngresos);
				UtilidadesHash.set(a, "DEBE", 			"0");
				UtilidadesHash.set(a, "HABER", 			imp);
				UtilidadesHash.set(a, "BASEIMPONIBLE", 	"");
				UtilidadesHash.set(a, "IVA", 			"");
				UtilidadesHash.set(a, "CONTRAPARTIDA", 	asientoClientes);
				pwcontabilidad.write(this.generarLineaAbono(asiento, a));
				
				// Escribimos 3º APUNTE
				if (!ivacero) {
					a.clear();
					UtilidadesHash.set(a, "FECHA", 			UtilidadesHash.getShortDate(hash, FacFacturaBean.C_FECHAEMISION));
					UtilidadesHash.set(a, "CONCEPTO", 		concepto);
					UtilidadesHash.set(a, "DOCUMENTO", 		UtilidadesHash.getString(hash, "NUMEROFACTURA"));
					UtilidadesHash.set(a, "CUENTA", 		asientoIVA);
					UtilidadesHash.set(a, "DEBE", 			"0");
					UtilidadesHash.set(a, "HABER", 			importeIva);
					UtilidadesHash.set(a, "BASEIMPONIBLE", 	imp);
					UtilidadesHash.set(a, "IVA", 			porcentajeIva);
					UtilidadesHash.set(a, "CONTRAPARTIDA", 	asientoClientes);
					pwcontabilidad.write(this.generarLineaAbono(asiento, a));
				}
				
				///////////////////////////////
				
				
				// ACTUALIZACION DE CONTABILIZADA
				if (!idFactura.equals(idFacturaAnt)) {
				    // Modificamos solamente cuando cambia la factura
				    
					laHash = new Hashtable();
					laHash.put("CONTABILIZADA",ClsConstants.FACTURA_ABONO_CONTABILIZADA);
					laHash.put("IDINSTITUCION",this.usrbean.getLocation());
					laHash.put("IDFACTURA",hash.get("IDFACTURA"));
					laHash.put("USUMODIFICACION",this.usrbean.getUserName());
					laHash.put("FECHAMODIFICACION","sysdate");
					
					vAsiento1.add(laHash);
					//facturaAdm.updateDirect(laHash,claves,campos);
					
					// actualizo el idfactura anterior
					idFacturaAnt = idFactura;
				}
			}
			if(vAsiento1.size()>0){
				String contabilizada=(String)((Hashtable)vAsiento1.get(0)).get("CONTABILIZADA");
				String usuModificacion=(String)((Hashtable)vAsiento1.get(0)).get("USUMODIFICACION");
				String fechaModificacion=(String)((Hashtable)vAsiento1.get(0)).get("FECHAMODIFICACION");
				
				// Ahora
				int cuantosRegEnIN=800;// se Ha modificado la actualizacion para que las claves no contengan mas de 1000 elementos ya que si no da error la actualizacion porque oracle
				                       // tiene esa restriccion en las listas. Ahora las listas tendran como mucho 800 elementos

				String sqlUpdate="UPDATE "+FacFacturaBean.T_NOMBRETABLA+" SET "+FacFacturaBean.C_CONTABILIZADA+"='"+contabilizada+"', "+FacFacturaBean.C_USUMODIFICACION+"="+usuModificacion+", "+FacFacturaBean.C_FECHAMODIFICACION+"="+fechaModificacion;
				int con = 0;
				tx.begin();
				for(int l = 0; l < vAsiento1.size(); l++){
				    con++;
				    if (con%numeroTransaccion==0) {
				        tx.commit();
				        tx.begin();
				    }
					String claves = (String)((Hashtable)vAsiento1.get(l)).get("IDFACTURA");
					Hashtable codigosUpdate = new Hashtable();
					codigosUpdate.put(new Integer(1),this.usrbean.getLocation());
					codigosUpdate.put(new Integer(2),claves);
					facturaAdm.insertSQLBind(sqlUpdate +" WHERE IDINSTITUCION=:1 AND IDFACTURA=:2",codigosUpdate);
				}
				tx.commit();
				
				
			}
			return pwcontabilidad;
		}
		catch (Exception e) 
		{
			pwcontabilidad.write("Error en asiento1: " + e.getMessage());
			try { tx.rollback(); } catch (Exception ee) {}
		    throw new ClsExceptions(e,"Error al generar asiento 1");
		} 
	}
	
	/**
	 * 
	 * @param pwcontabilidad
	 * @param fechaDesde
	 * @param fechaHasta
	 * @param usr
	 * @return
	 * @throws SIGAException
	 */
	// REPARTO PAGOA JUSTICIA GRATUITA

	private java.io.PrintWriter generaAsiento8(java.io.PrintWriter pwcontabilidad, String fechaDesde, String fechaHasta, UserTransaction tx) throws SIGAException, ClsExceptions
	{
		// Tipos de asientos.
		// nomenclatura de fichero: #cuenta , haber, debe
		// 8 ---> reparto pagos sjcs
		// Serv. prof. indep.(623) -->  0                   --> pago bruto por persona
		// Movim. varios (623.1)   -->  Movs. negativos     --> Movs. positivos
		// IRPF          (475)     -->  Retencion IRPF per. --> 0
		// Ret. Judiciales (623.3) -->  Valor reten.        --> 0
		// Accredores    (410.xxx) -->  Pago neto por per.  --> 0
		
		String concepto 		= "";
		String asientoContable 	= ""; // Obtener de bd.
		
		// Variables
		Vector vResultado = null, vPagos = null, vContabilidad = null;
		Hashtable hash = null, laHash = null;
		String select = null, sql = null, imp = null, importeIva = null;
		
		Vector vAsiento8=new Vector();
		Vector vAsiento8Persona=new Vector();

		// Beans
		FcsPagosJGAdm pagoAdm 	= new FcsPagosJGAdm(this.usrbean);
		if(vContabilidad !=null && vContabilidad.size()>0)	CONTABILIDAD_IVA = ((GenParametrosBean) vContabilidad.get(0)).getValor();
		int contador = 0;             
        Hashtable codigos = new Hashtable();
		
		try
		{
			// OBTENGO LOS PAGOS IMPLICADOS
			select = " select P.IDPAGOSJG IDPAGO, e.fechaestado FECHAESTADO from fcs_pagosjg p , fcs_pagos_estadospagos e "+ 
					" where p.idinstitucion = e.idinstitucion " ;
			contador++;
			codigos.put(new Integer(contador),this.usrbean.getLocation());
			select +=" AND p.idinstitucion = :"+contador +
					" and   p.idpagosjg = e.idpagosjg " ;
			contador++;
			codigos.put(new Integer(contador),ClsConstants.ESTADO_PAGO_CERRADO);		
			select +=" AND e.IDESTADOPAGOSJG=:"+contador+" " + 
					" and  e.FECHAESTADO = (select max(ee.fechaestado) " + 
 	  				"   from fcs_pagos_estadospagos ee " +
 	  				"   where ee.IDINSTITUCION = e.idinstitucion "+
					"  and  ee.IDPAGOSJG = e.IDPAGOSJG) ";
					
			/*if(!fechaDesde.equals("") && !fechaHasta.equals(""))
				select+=" AND e.fechaestado >= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaDesde)+"','YYYY/MM/DD hh24:mi:ss') AND e.fechaestado <= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaHasta)+"','YYYY/MM/DD hh24:mi:ss')";
			else if(!fechaDesde.equals(""))
				select+=" AND e.fechaestado >= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaDesde)+"','YYYY/MM/DD hh24:mi:ss')";
			else if(!fechaHasta.equals(""))
				select+=" AND e.fechaestado <= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaHasta)+"','YYYY/MM/DD hh24:mi:ss')";
			*/
			String fDesdeInc = fechaDesde; 
			   String fHastaInc = fechaHasta;
				if ((fDesdeInc != null && !fDesdeInc.trim().equals("")) || (fHastaInc != null && !fHastaInc.trim().equals(""))) {
					
					if (!fDesdeInc.equals(""))
						fDesdeInc = GstDate.getApplicationFormatDate("", fDesdeInc); 
					if (!fHastaInc.equals(""))
						fHastaInc = GstDate.getApplicationFormatDate("", fHastaInc);
					Vector vCondicion=GstDate.dateBetweenDesdeAndHastaBind("e.fechaestado", fDesdeInc, fHastaInc,contador,codigos);
					
					contador=new Integer(vCondicion.get(0).toString()).intValue();
					select +=" AND " + vCondicion.get(1) ;
				}	

			
			
			// Obtenemos el concepto
			concepto = UtilidadesString.sustituirParaExcell(UtilidadesString.getMensajeIdioma(this.usrbean,CONCEPTO_ASIENTO8));
			// RGG concepto = obtenerDescripcion(CONCEPTO_ASIENTO8 ,usr.getLanguage(),usr);
			
			vPagos=(Vector)this.selectTablaBind(select,codigos);
			int contador1;             
	        Hashtable codigos1 = new Hashtable();
			for(int x=0;x<vPagos.size();x++)
			{   contador1=0;
				
				// PARA CADA PAGO BUSCO LAS PERSONAS (PORQUE LOS PAGOS SON DE MUCHOS LETRADOS)
				hash = (Hashtable) vPagos.get(x);
				String idpago=(String)hash.get("IDPAGO");
				String fechapago=(String)hash.get("FECHAESTADO");
				int xx=0;

				// BUSCO A LAS PERSONAS IMPLICADAS EN EL PAGO
				Vector vPersonas=null;
				sql = "SELECT   idpersonasjcs, SUM (baseirpf) AS baseirpf, SUM (totalimportesjcs) AS totalimportesjcs, " +
					" SUM (importetotalretenciones) AS importetotalretenciones, " +
					" SUM (importetotalmovimientos) AS importetotalmovimientos, " +
					" SUM (totalimporteirpf) AS totalimporteirpf " +
					" FROM ((SELECT   idpersona AS idpersonasjcs,sum(porcentajeirpf) AS porcentajeIRPF, SUM (importepagado) AS baseirpf, " +
					" SUM (importepagado) AS totalimportesjcs, " +
					" 0 AS importetotalretenciones, " +
					" 0 AS importetotalmovimientos, " +
					" SUM (importeirpf) AS totalimporteirpf " +
					" FROM fcs_pago_actuaciondesigna " ;
				contador1++;
				codigos1.put(new Integer(contador1),this.usrbean.getLocation());
				sql +=	" WHERE idinstitucion = :"+contador1 ;
				contador1++;
				codigos1.put(new Integer(contador1),idpago);
				sql +=	" AND idpagosjg = :" + contador1 + " " +
					" GROUP BY idpersona,porcentajeIRPF) " +
					" UNION ALL " +
					" (SELECT   idpersona AS idpersonasjcs,sum(porcentajeirpf) AS porcentajeIRPF,SUM (importepagado) AS baseirpf, " +
					" SUM (importepagado) AS totalimportesjcs, " +
					" 0 AS importetotalretenciones, " +
					" 0 AS importetotalmovimientos, " +
					" SUM (importeirpf) AS totalimporteirpf " +
					" FROM fcs_pago_apunte " ;
				contador1++;
				codigos1.put(new Integer(contador1),this.usrbean.getLocation());
				sql +=	" WHERE idinstitucion = :"+contador1 ;
				contador1++;
				codigos1.put(new Integer(contador1),idpago);
				sql +=	" AND idpagosjg = :" + contador1 + " " +
					" GROUP BY idpersona,porcentajeIRPF) " +
					" UNION ALL " +
					" (SELECT   idpersona AS idpersonasjcs, sum(porcentajeirpf) AS porcentajeIRPF,SUM (importepagado) AS baseirpf," +
					" SUM (importepagado) AS totalimportesjcs, " +
					" 0 AS importetotalretenciones, " +
					" 0 AS importetotalmovimientos, " +
					" SUM (importeirpf) AS totalimporteirpf " +
					" FROM fcs_pago_soj " ;
				contador1++;
				codigos1.put(new Integer(contador1),this.usrbean.getLocation());
				sql +=" WHERE idinstitucion = :"+contador1 ;
				contador1++;
				codigos1.put(new Integer(contador1),idpago);
				sql +=" AND idpagosjg = :" + contador1 + " " +
					" GROUP BY idpersona,porcentajeIRPF) " +
					" UNION ALL " +
					" (SELECT   idpersona AS idpersonasjcs,sum(porcentajeirpf) AS porcentajeIRPF,SUM (importepagado) AS baseirpf, " +
					" SUM (importepagado) AS totalimportesjcs, " +
					" 0 AS importetotalretenciones, " +
					" 0 AS importetotalmovimientos, " +
					" SUM (importeirpf) AS totalimporteirpf " +
					" FROM fcs_pago_ejg " ;
				contador1++;
				codigos1.put(new Integer(contador1),this.usrbean.getLocation());
				sql +=" WHERE idinstitucion = :"+contador1 ;
				contador1++;
				codigos1.put(new Integer(contador1),idpago);
				sql +=" AND idpagosjg = :" + contador1 + " " +
					" GROUP BY idpersona,porcentajeIRPF) " +
					" UNION ALL " +
					" (SELECT   idpersona AS idpersonasjcs,sum(porcentajeirpf) AS porcentajeIRPF,SUM (cantidad) AS baseirpf,0 AS totalimportesjcs, " +
					" 0 AS importetotalretenciones, " +
					" SUM (cantidad) AS importetotalmovimientos, " +
					" SUM (importeirpf) AS totalimporteirpf " +
					" FROM fcs_movimientosvarios " ;
				contador1++;
				codigos1.put(new Integer(contador1),this.usrbean.getLocation());
				sql +=" WHERE idinstitucion = :"+contador1 ;
				contador1++;
				codigos1.put(new Integer(contador1),idpago);
				sql +=" AND idpagosjg = :" + contador1 + " " +
					" GROUP BY idpersona,porcentajeIRPF) " +
					" UNION ALL " +
					" (SELECT   idpersona AS idpersonasjcs, 0 AS porcentajeIRPF,0 AS baseirpf,0 AS totalimportesjcs, " +
					" SUM (importeretenido) AS importetotalretenciones, " +
					" 0 AS importetotalmovimientos, 0 AS totalimporteirpf " +
					" FROM fcs_cobros_retencionjudicial " ;
				contador1++;
				codigos1.put(new Integer(contador1),this.usrbean.getLocation());
				sql +=" WHERE idinstitucion = :"+contador1;
				contador1++;
				codigos1.put(new Integer(contador1),idpago);
				sql +=" AND idpagosjg = :" + contador1 + " " +
					" GROUP BY idpersona)) " +
					" GROUP BY idpersonasjcs ";

				vPersonas=(Vector)this.selectTablaBind(sql,codigos1);
				int contador2;             
		        Hashtable codigos2 = new Hashtable();
				// BUCLE DE PERSONAS
				for(xx=0;xx<vPersonas.size();xx++)
				{ contador2=0;
				
					// SE CREA EL ASIENTO
					asiento++;

					Hashtable hash2 = (Hashtable) vPersonas.get(xx);
					String idpersona = (String)hash2.get("IDPERSONASJCS");

					// Con el IDPERSONA, obtenemos de CEN_CLIENTE, el asiento contable.
					asientoContable =  obtenerAsientoContableAcreedor(this.usrbean.getLocation(),idpersona);
					
					// PREPARO LAS VARIABLES QUE RECOGERAN LOS VALORES POR PERSONA
					String totalimportesjcs = "0";
					String totalimporteretenciones = "0";
					String totalimportemovimientos = "0";
					String totalimportemovimientosnegativos = "0";
					String totalimporteIRPF = "0";
					String porcentajeIRPF = "0";
					String baseIRPF = "0";
					Vector vAux=null;
					double totalgeneralimporteIRPF = 0;
					double totalgeneralimportemovimientos = 0;
					double totalgeneralimporteretenciones = 0;
					double totalgeneralimportemovimientosnegativos = 0;
					
					/////////////////////////////////////////////////////
					// SERVICIOS A PROFESIONALES INDEPENDIENTES
					sql = " select pa.idpersona IDPERSONASJCS, sum(pa.importepagado) TOTALIMPORTESJCS "+
					" from fcs_pagosjg p, fcs_pago_apunte pa "+
					" where p.idinstitucion=pa.idinstitucion "+
					" and   p.idpagosjg=pa.idpagosjg ";
					contador2++;
					codigos2.put(new Integer(contador2),this.usrbean.getLocation());
					sql +=" and p.idinstitucion=:"+contador2 ;
					contador2++;
					codigos2.put(new Integer(contador2),idpago);
					sql +=" and p.idpagosjg=:"+contador;
					contador2++;
					codigos2.put(new Integer(contador2),idpersona);
					sql +=" and pa.idpersona=:"+contador+
					" group by pa.idpersona"; 
						
					vAux=(Vector)this.selectTablaBind(sql,codigos2);
					if (vAux!=null && vAux.size()>0) {
						Hashtable hash3 = (Hashtable) vAux.get(0);
						totalimportesjcs = (String)hash3.get("TOTALIMPORTESJCS");
					
						Hashtable a = new Hashtable();
							
						// Escribimos 1º APUNTE (serv. a prof. indep.)
						UtilidadesHash.set(a, "FECHA", 			UtilidadesHash.getShortDate(hash, "FECHAESTADO"));
						UtilidadesHash.set(a, "CONCEPTO", 		concepto);
						UtilidadesHash.set(a, "DOCUMENTO", 		UtilidadesHash.getString(hash, "IDPAGO"));
						UtilidadesHash.set(a, "CUENTA", 		CONTAB_SERVICIOS_PROFESIONALES);
						UtilidadesHash.set(a, "DEBE", 			"" + (Double.parseDouble(totalimportesjcs)));
						UtilidadesHash.set(a, "HABER", 			"0");
						UtilidadesHash.set(a, "BASEIMPONIBLE", 	"");
						UtilidadesHash.set(a, "IVA", 			"");
						UtilidadesHash.set(a, "CONTRAPARTIDA", 	asientoContable);
						pwcontabilidad.write(this.generarLineaAbono(asiento, a));
					
					}
					
					int contador3=0;             
			        Hashtable codigos3 = new Hashtable();
					////////////////////////////////////////////////////////////
					// MOVIMIENTOS VARIOS POSITIVOS
					sql = " select idpersona IDPERSONASJCS, idmovimiento IDMOVIMIENTO,  sum(cantidad) IMPORTETOTALMOVIMIENTOS " +
					" from fcs_movimientosvarios " ;
					contador3++;
					codigos3.put(new Integer(contador3),this.usrbean.getLocation());
					sql +=" where idinstitucion=:"+contador3;
					contador3++;
					codigos3.put(new Integer(contador3),idpago);
					sql +=" and idpagosjg=:"+contador3 ;
					contador3++;
					codigos3.put(new Integer(contador3),idpersona);
					sql +=" and idpersona=:"+contador3 +
					" and cantidad>0 " +
					" group by idpersona, idmovimiento"; 
						
					vAux=(Vector)this.selectTablaBind(sql, codigos3);
					for(int xy=0;xy<vAux.size();xy++)
					{

						Hashtable hash3 = (Hashtable) vAux.get(xy);

						totalimportemovimientos = (String)hash3.get("IMPORTETOTALMOVIMIENTOS");
						String idmovimiento = (String)hash3.get("IDMOVIMIENTO");
						
						totalgeneralimportemovimientos+=Double.parseDouble(totalimportemovimientos);
						
						// Con el IDPERSONA, obtenemos de CEN_CLIENTE, el asiento contable.
						asientoContable =  obtenerAsientoContableAcreedor(this.usrbean.getLocation(),idpersona);
						
						Hashtable a = new Hashtable();
							
						// Escribimos 2º APUNTE (MOVIMIENTOS VARIOS)
						UtilidadesHash.set(a, "FECHA", 			UtilidadesHash.getShortDate(hash, "FECHAESTADO"));
						UtilidadesHash.set(a, "CONCEPTO", 		concepto);
						UtilidadesHash.set(a, "DOCUMENTO", 		UtilidadesHash.getString(hash, "IDPAGO"));
						UtilidadesHash.set(a, "CUENTA", 		CONTAB_MOVIMIENTOS_VARIOS);
						UtilidadesHash.set(a, "DEBE", 			"" + (Double.parseDouble(totalimportemovimientos)));
						UtilidadesHash.set(a, "HABER", 			"0");
						UtilidadesHash.set(a, "BASEIMPONIBLE", 	"");
						UtilidadesHash.set(a, "IVA", 			"");
						UtilidadesHash.set(a, "CONTRAPARTIDA", 	asientoContable);
						pwcontabilidad.write(this.generarLineaAbono(asiento, a));

//						// actualizamos a contabilizado el movimiento
						FcsMovimientosVariosAdm movimientoAdm = new FcsMovimientosVariosAdm(this.usrbean); 
						String[] claves2 = {"IDINSTITUCION","IDMOVIMIENTO"};
						String[] campos2 = {"CONTABILIZADO","USUMODIFICACION","FECHAMODIFICACION"};
						laHash = new Hashtable();
						laHash.put("CONTABILIZADO",ClsConstants.FACTURA_ABONO_CONTABILIZADA);
						laHash.put("IDINSTITUCION",this.usrbean.getLocation());
						laHash.put("IDMOVIMIENTO",idmovimiento);
						laHash.put("USUMODIFICACION",this.usrbean.getUserName());
						laHash.put("FECHAMODIFICACION","sysdate");
						
						vAsiento8.add(laHash);
						//movimientoAdm.updateDirect(laHash,claves2,campos2);
						
					}						
				
					int contador4=0;             
			        Hashtable codigos4 = new Hashtable();
					////////////////////////////////////////////////////////////
					// MOVIMIENTOS VARIOS NEGATIVOS
					sql = " select idpersona IDPERSONASJCS, idmovimiento IDMOVIMIENTO,  sum(cantidad) IMPORTETOTALMOVIMIENTOS " +
					" from fcs_movimientosvarios " ;
					contador4++;
					codigos4.put(new Integer(contador4),this.usrbean.getLocation());
					sql +=" where idinstitucion=:"+contador4 ;
					contador4++;
					codigos4.put(new Integer(contador4),idpago);
					sql +=" and idpagosjg=:"+contador4 ;
					contador4++;
					codigos4.put(new Integer(contador4),idpersona);
					sql +=" and idpersona=:"+contador4 +
					      " and cantidad<0 " +
					      " group by idpersona, idmovimiento"; 
					FcsMovimientosVariosAdm movimientoAdm = new FcsMovimientosVariosAdm(this.usrbean);
					vAux=(Vector)this.selectTablaBind(sql,codigos4);
					for(int xy=0;xy<vAux.size();xy++)
					{

						Hashtable hash3 = (Hashtable) vAux.get(xy);

						totalimportemovimientosnegativos = (String)hash3.get("IMPORTETOTALMOVIMIENTOS");
						String idmovimiento = (String)hash3.get("IDMOVIMIENTO");
						
						totalgeneralimportemovimientosnegativos+=Double.parseDouble(totalimportemovimientosnegativos);
						
						Hashtable a = new Hashtable();
							
						// Escribimos 3º APUNTE (MOVIMIENTOS VARIOS NEGATIVOS)
						UtilidadesHash.set(a, "FECHA", 			UtilidadesHash.getShortDate(hash, "FECHAESTADO"));
						UtilidadesHash.set(a, "CONCEPTO", 		concepto);
						UtilidadesHash.set(a, "DOCUMENTO", 		UtilidadesHash.getString(hash, "IDPAGO"));
						UtilidadesHash.set(a, "CUENTA", 		CONTAB_MOV_VARIOS_NEGATIVOS);
						UtilidadesHash.set(a, "DEBE", 			"0");
						UtilidadesHash.set(a, "HABER", 			"" + Math.abs(Double.parseDouble(totalimportemovimientosnegativos)));
						UtilidadesHash.set(a, "BASEIMPONIBLE", 	"");
						UtilidadesHash.set(a, "IVA", 			"");
						UtilidadesHash.set(a, "CONTRAPARTIDA", 	CONTAB_SERVICIOS_PROFESIONALES);
						pwcontabilidad.write(this.generarLineaAbono(asiento, a));

						// actualizamos a contabilizado el movimiento
						movimientoAdm = new FcsMovimientosVariosAdm(this.usrbean); 

						laHash = new Hashtable();
						laHash.put("CONTABILIZADO",ClsConstants.FACTURA_ABONO_CONTABILIZADA);
						laHash.put("IDINSTITUCION",this.usrbean.getLocation());
						laHash.put("IDMOVIMIENTO",idmovimiento);
						laHash.put("USUMODIFICACION",this.usrbean.getUserName());
						laHash.put("FECHAMODIFICACION","sysdate");
						
						vAsiento8.add(laHash);
//						movimientoAdm.updateDirect(laHash,claves2,campos2);
						
					}	
					if(vAsiento8.size()>0){
						String contabilizada=(String)((Hashtable)vAsiento8.get(0)).get("CONTABILIZADO");
						String usuModificacion=(String)((Hashtable)vAsiento8.get(0)).get("USUMODIFICACION");
						String fechaModificacion=(String)((Hashtable)vAsiento8.get(0)).get("FECHAMODIFICACION");
						
						

						String sqlUpdate="UPDATE "+FcsMovimientosVariosBean.T_NOMBRETABLA+" SET "+FcsMovimientosVariosBean.C_CONTABILIZADO+"='"+contabilizada+"', "+FcsMovimientosVariosBean.C_USUMODIFICACION+"="+usuModificacion+", "+FcsMovimientosVariosBean.C_FECHAMODIFICACION+"="+fechaModificacion;
						int con = 0;
						tx.begin();
						for(int l = 0; l < vAsiento8.size(); l++){
						    con++;
						    if (con%numeroTransaccion==0) {
						        tx.commit();
						        tx.begin();
						    }
							String claves2 = (String)((Hashtable)vAsiento8.get(l)).get("IDMOVIMIENTO");
							Hashtable codigosUpdate = new Hashtable();
							codigosUpdate.put(new Integer(1),this.usrbean.getLocation());
							codigosUpdate.put(new Integer(2),claves2);
							movimientoAdm.insertSQLBind(sqlUpdate +" WHERE IDINSTITUCION=:1 AND IDMOVIMIENTO=:2",codigosUpdate);
						}
						tx.commit();
						
						
					}
					
					int contador5=0;             
			        Hashtable codigos5 = new Hashtable();
					////////////////////////////////////////////////////////////
					// IRPF
					sql = " SELECT idpersona idpersonasjcs, porcentajeirpf PORCENTAJEIRPF, sum(totalimporteirpf) TOTALIMPORTEIRPF, sum(baseirpf) BASEIRPF "+
					" from ( "+
					" select idpersona, porcentajeirpf PORCENTAJEIRPF, sum(importeirpf) TOTALIMPORTEIRPF, sum(cantidad) BASEIRPF "+
					" from fcs_movimientosvarios ";
					contador5++;
					codigos5.put(new Integer(contador5),this.usrbean.getLocation());
					sql +=" where idinstitucion=:" +contador5;
					contador5++;
					codigos5.put(new Integer(contador5),idpago);
					sql +=" and idpagosjg=:" +contador5;
					contador5++;
					codigos5.put(new Integer(contador5),idpersona);
					sql +=" and idpersona=:" +contador5+
					" group by idpersona, porcentajeirpf "+
					" UNION "+
					" select pa.idpersona idpersonasjcs, pa.porcentajeirpf PORCENTAJEIRPF, sum(pa.importeirpf) TOTALIMPORTEIRPF, sum(pa.importepagado) BASEIRPF "+
					" from fcs_pagosjg p, fcs_pago_apunte pa "+
					" where p.idinstitucion=pa.idinstitucion "+
					" and   p.idpagosjg=pa.idpagosjg ";
					contador5++;
					codigos5.put(new Integer(contador5),this.usrbean.getLocation());
					sql +=" and p.idinstitucion=:" +contador5;
					contador5++;
					codigos5.put(new Integer(contador5),idpago);
					sql +=" and p.idpagosjg=:" +contador5;
					contador5++;
					codigos5.put(new Integer(contador5),idpersona);
					sql +=" and pa.idpersona=:" +contador5+
					      " group by pa.idpersona,porcentajeirpf) "+
					      " group by idpersona, porcentajeirpf";

						
					vAux=(Vector)this.selectTablaBind(sql,codigos5);
					for(int xy=0;xy<vAux.size();xy++)
					{

						Hashtable hash3 = (Hashtable) vAux.get(xy);

						totalimporteIRPF = (String)hash3.get("TOTALIMPORTEIRPF");
						porcentajeIRPF = (String)hash3.get("PORCENTAJEIRPF");
						baseIRPF = (String)hash3.get("BASEIRPF");
						
						totalgeneralimporteIRPF+=Double.parseDouble(totalimporteIRPF);
						
				
						Hashtable a = new Hashtable();
							
						// Escribimos 4º APUNTE (IRPF)
						UtilidadesHash.set(a, "FECHA", 			UtilidadesHash.getShortDate(hash, "FECHAESTADO"));
						UtilidadesHash.set(a, "CONCEPTO", 		concepto);
						UtilidadesHash.set(a, "DOCUMENTO", 		UtilidadesHash.getString(hash, "IDPAGO"));
						UtilidadesHash.set(a, "CUENTA", 		CONTAB_IRPF);
						UtilidadesHash.set(a, "DEBE", 			"0");
						UtilidadesHash.set(a, "HABER", 			"" + (Double.parseDouble(totalimporteIRPF)));
						UtilidadesHash.set(a, "BASEIMPONIBLE", 	"" + (Double.parseDouble(baseIRPF)));
						UtilidadesHash.set(a, "IVA", 			"" + (Double.parseDouble(porcentajeIRPF)));
						UtilidadesHash.set(a, "CONTRAPARTIDA", 	CONTAB_SERVICIOS_PROFESIONALES);
						pwcontabilidad.write(this.generarLineaAbono(asiento, a));

						
					}						
					int contador6=0;             
			        Hashtable codigos6 = new Hashtable();
					////////////////////////////////////////////////////////////
					// RETENCIONES JUDICIALES
					sql = " select rt.idpersona , rt.idretencion IDRETENCION,  sum(crt.importeretenido) IMPORTETOTALRETENCIONES " +
					" from fcs_retenciones_judiciales rt, fcs_cobros_retencionjudicial crt  " +
					" where rt.idinstitucion = crt.idinstitucion " +
					" and rt.idpersona = crt.idpersona " +
					" and rt.idretencion = crt.idretencion " ;
					contador6++;
					codigos6.put(new Integer(contador6),this.usrbean.getLocation());
					sql +=" and rt.idinstitucion=;"+contador6 ;
					contador6++;
					codigos6.put(new Integer(contador6),idpago);
					sql +=" and crt.idpagosjg=:"+contador6 ;
					contador6++;
					codigos6.put(new Integer(contador6),idpersona);
					sql +=" and rt.idpersona=:"+contador6 +
					" group by rt.idpersona, rt.idretencion";
					FcsRetencionesJudicialesAdm retencionAdm = new FcsRetencionesJudicialesAdm(this.usrbean);
					vAux=(Vector)this.selectTablaBind(sql,codigos6);
					for(int xy=0;xy<vAux.size();xy++)
					{

						Hashtable hash3 = (Hashtable) vAux.get(xy);

						totalimporteretenciones = (String)hash3.get("IMPORTETOTALRETENCIONES");
						String idretencion = (String)hash3.get("IDRETENCION");

						totalgeneralimporteretenciones+=Double.parseDouble(totalimporteretenciones);

						Hashtable a = new Hashtable();
							
						// Escribimos 5º APUNTE (RETENCIONES JUDICIALES)
						UtilidadesHash.set(a, "FECHA", 			UtilidadesHash.getShortDate(hash, "FECHAESTADO"));
						UtilidadesHash.set(a, "CONCEPTO", 		concepto);
						UtilidadesHash.set(a, "DOCUMENTO", 		UtilidadesHash.getString(hash, "IDPAGO"));
						UtilidadesHash.set(a, "CUENTA", 		CONTAB_RETENCIONESJUDICIALES);
						UtilidadesHash.set(a, "DEBE", 			"0");
						UtilidadesHash.set(a, "HABER", 			"" + Math.abs(Double.parseDouble(totalimporteretenciones)));
						UtilidadesHash.set(a, "BASEIMPONIBLE", 	"");
						UtilidadesHash.set(a, "IVA", 			"");
						UtilidadesHash.set(a, "CONTRAPARTIDA", 	CONTAB_SERVICIOS_PROFESIONALES);
						pwcontabilidad.write(this.generarLineaAbono(asiento, a));

						// actualizamos a contabilizado el movimiento
						retencionAdm = new FcsRetencionesJudicialesAdm(this.usrbean); 
//						String[] claves2 = {"IDINSTITUCION","IDPERSONA", "IDRETENCION"};
//						String[] campos2 = {"CONTABILIZADO","USUMODIFICACION","FECHAMODIFICACION"};
						laHash = new Hashtable();
						laHash.put("CONTABILIZADO",ClsConstants.FACTURA_ABONO_CONTABILIZADA);
						laHash.put("IDINSTITUCION",this.usrbean.getLocation());
						laHash.put("IDPERSONA",idpersona);
						laHash.put("IDRETENCION",idretencion);
						laHash.put("USUMODIFICACION",this.usrbean.getUserName());
						laHash.put("FECHAMODIFICACION","sysdate");
						vAsiento8.add(laHash);
//						retencionAdm.updateDirect(laHash,claves2,campos2);
						
					}	

//					String[] claves3 = {"IDINSTITUCION","IDPERSONA", "IDRETENCION"};
//					String[] campos3 = {"CONTABILIZADO","USUMODIFICACION","FECHAMODIFICACION"};
//					for(int l=0;l<vAsiento8.size();l++){
//					retencionAdm.updateDirect((Hashtable)vAsiento8.get(l),claves3,campos3);
//					}
					if(vAsiento8.size()>0){
						String contabilizada2=(String)((Hashtable)vAsiento8.get(0)).get("CONTABILIZADO");
						String usuModificacion2=(String)((Hashtable)vAsiento8.get(0)).get("USUMODIFICACION");
						String fechaModificacion2=(String)((Hashtable)vAsiento8.get(0)).get("FECHAMODIFICACION");
						
						/*String claves2="'"+(String)((Hashtable)vAsiento8.get(0)).get("IDINSTITUCION")+"%%%"+(String)((Hashtable)vAsiento8.get(0)).get("IDPERSONA")+"%%%"+(String)((Hashtable)vAsiento8.get(0)).get("IDRETENCION")+"'";
						
						for(int l=1;l<vAsiento8.size();l++){
							claves2=claves2+",'"+(String)((Hashtable)vAsiento8.get(l)).get("IDINSTITUCION")+"%%%"+(String)((Hashtable)vAsiento8.get(l)).get("IDPERSONA")+"%%%"+(String)((Hashtable)vAsiento8.get(l)).get("IDRETENCION")+"'";
						}
						String sqlUpdate2="UPDATE "+FcsRetencionesJudicialesBean.T_NOMBRETABLA+" SET "+FcsRetencionesJudicialesBean.C_CONTABILIZADO+"='"+contabilizada2+"', "+FcsRetencionesJudicialesBean.C_USUMODIFICACION+"="+usuModificacion2+", "+FacFacturaBean.C_FECHAMODIFICACION+"="+fechaModificacion2;
						sqlUpdate2=sqlUpdate2+" WHERE (IDINSTITUCION||'%%%'||IDPERSONA||'%%%'||IDRETENCION) IN ("+claves2+")";
						retencionAdm.insertSQL(sqlUpdate2);*/
						
						int cuantosRegEnIN=800;// se Ha modificado la actualizacion para que las claves no contengan mas de 1000 elementos ya que si no da error la actualizacion porque oracle
		                // tiene esa restriccion en las listas. Ahora las listas tendran como mucho 800 elementos
						
						String sqlUpdate2="UPDATE "+FcsRetencionesJudicialesBean.T_NOMBRETABLA+" SET "+FcsRetencionesJudicialesBean.C_CONTABILIZADO+"='"+contabilizada2+"', "+FcsRetencionesJudicialesBean.C_USUMODIFICACION+"="+usuModificacion2+", "+FacFacturaBean.C_FECHAMODIFICACION+"="+fechaModificacion2;
						int con = 0;
						tx.begin();
						for(int l = 0; l < vAsiento8.size(); l++){
						    con++;
						    if (con%numeroTransaccion==0) {
						        tx.commit();
						        tx.begin();
						    }
							String claves = (String)((Hashtable)vAsiento8.get(l)).get("IDPERSONA");
							String claves2 = (String)((Hashtable)vAsiento8.get(l)).get("IDRETENCION");
							Hashtable codigosUpdate = new Hashtable();
							codigosUpdate.put(new Integer(1),this.usrbean.getLocation());
							codigosUpdate.put(new Integer(2),claves);
							codigosUpdate.put(new Integer(3),claves2);
							movimientoAdm.insertSQLBind(sqlUpdate2 +" WHERE IDINSTITUCION=:1 AND IDPERSONA=:2 AND IDRETENCION=:3",codigosUpdate);
						}
						tx.commit();
						
						
					}
					////////////////////////////////////////////////////////////
					// APUNTE ACREEDOR
		
					Hashtable a = new Hashtable();
					
					// CALCULO DEL TOTAL
					double aux = ((Double.parseDouble(totalimportesjcs) + totalgeneralimportemovimientos - Math.abs(totalgeneralimportemovimientosnegativos)) - totalgeneralimporteIRPF) - Math.abs(totalgeneralimporteretenciones); 
					
					// Escribimos 5º APUNTE (RETENCIONES JUDICIALES)
					UtilidadesHash.set(a, "FECHA", 			UtilidadesHash.getShortDate(hash, "FECHAESTADO"));
					UtilidadesHash.set(a, "CONCEPTO", 		concepto);
					UtilidadesHash.set(a, "DOCUMENTO", 		UtilidadesHash.getString(hash, "IDPAGO"));
					UtilidadesHash.set(a, "CUENTA", 		asientoContable);
					UtilidadesHash.set(a, "DEBE", 			"0");
					UtilidadesHash.set(a, "HABER", 			"" + aux);
					UtilidadesHash.set(a, "BASEIMPONIBLE", 	"");
					UtilidadesHash.set(a, "IVA", 			"");
					UtilidadesHash.set(a, "CONTRAPARTIDA", 	CONTAB_SERVICIOS_PROFESIONALES);
					pwcontabilidad.write(this.generarLineaAbono(asiento, a));					

					
					////////////////////////////////////////////////////////////
				
				}	// FIN DEL BUCLE POR PERSONA					
						

				///////////////////////////////////////////////////////////////////////
				///////////////////////////////////////////////////////////////////////
				///////////////////////////////////////////////////////////////////////
				///////////////////////////////////////////////////////////////////////
				
				String[] claves = {"IDINSTITUCION","IDPAGOSJG"};
				String[] campos = {"CONTABILIZADO","USUMODIFICACION","FECHAMODIFICACION"};
				laHash = new Hashtable();
				laHash.put("CONTABILIZADO",ClsConstants.FACTURA_ABONO_CONTABILIZADA);
				laHash.put("IDINSTITUCION",this.usrbean.getLocation());
				laHash.put("IDPAGOSJG",idpago);
				laHash.put("USUMODIFICACION",this.usrbean.getUserName());
				laHash.put("FECHAMODIFICACION","sysdate");
				
				vAsiento8Persona.add(laHash);

			} 

			if(vAsiento8Persona.size()>0){
				String contabilizada=(String)((Hashtable)vAsiento8Persona.get(0)).get("CONTABILIZADO");
				String usuModificacion=(String)((Hashtable)vAsiento8Persona.get(0)).get("USUMODIFICACION");
				String fechaModificacion=(String)((Hashtable)vAsiento8Persona.get(0)).get("FECHAMODIFICACION");
				

				String sqlUpdate="UPDATE "+FcsPagosJGBean.T_NOMBRETABLA+" SET "+FcsPagosJGBean.C_CONTABILIZADO+"='"+contabilizada+"', "+FcsPagosJGBean.C_USUMODIFICACION+"="+usuModificacion+", "+FcsPagosJGBean.C_FECHAMODIFICACION+"="+fechaModificacion;
				int con = 0;
				tx.begin();
				for(int l = 0; l < vAsiento8Persona.size(); l++){
				    con++;
				    if (con%numeroTransaccion==0) {
				        tx.commit();
				        tx.begin();
				    }
					String claves2 = (String)((Hashtable)vAsiento8Persona.get(l)).get("IDPAGOSJG");
					Hashtable codigosUpdate = new Hashtable();
					codigosUpdate.put(new Integer(1),this.usrbean.getLocation());
					codigosUpdate.put(new Integer(2),claves2);
					pagoAdm.insertSQLBind(sqlUpdate +" WHERE IDINSTITUCION=:1 AND IDPAGOSJG=:2",codigosUpdate);
				}
				tx.commit();
			
			}
			
			return pwcontabilidad;
		}
		catch (Exception e) 
		{
			pwcontabilidad.write("Error en asiento8: " + e.getMessage());
			try { tx.rollback(); } catch (Exception ee) {}
		    throw new ClsExceptions(e,"Error al generar asiento 8");
		} 
	}

	/**
	 * 
	 * @param pwcontabilidad
	 * @param fechaDesde
	 * @param fechaHasta
	 * @param usr
	 * @return
	 * @throws SIGAException
	 */
	// PAGOS DE JUSTICIA GRATUITA POR BANCO
	private java.io.PrintWriter generaAsiento9(java.io.PrintWriter pwcontabilidad, String fechaDesde, String fechaHasta, UserTransaction tx) throws SIGAException, ClsExceptions
	{
		// Tipos de asientos.
		// nomenclatura de fichero: #cuenta , haber, debe
		// 9 ---> pago de sjcs por cuenta bancaria
		// nomenclatura de fichero: #cuenta , haber, debe
		// Accredores    (410.xxx) -->  0                   --> Pago neto por per.
		// Banco         (572.1xxx)   -->  Pago neto por per.  --> 0
	
		String concepto 		= "";
		String asientoContable 	= ""; // Obtener de bd.
		
		// Variables
		Vector vResultado = null, vPagos = null, vContabilidad = null;
		Hashtable hash = null, laHash = null;
		String select = null, sql = null, imp = null, importeIva = null;
		
		Vector vAsiento9=new Vector();

		// Beans
		FcsPagosJGAdm pagoAdm 	= new FcsPagosJGAdm(this.usrbean);
		if(vContabilidad !=null && vContabilidad.size()>0)	CONTABILIDAD_IVA = ((GenParametrosBean) vContabilidad.get(0)).getValor();
		int contador = 0;             
        Hashtable codigos = new Hashtable();
		
		try
		{
			// OBTENGO LOS PAGOS IMPLICADOS (los mismos que he apuntado)
			select = " select P.IDPAGOSJG IDPAGO, e.fechaestado FECHAESTADO from fcs_pagosjg p , fcs_pagos_estadospagos e "+ 
					" where p.idinstitucion = e.idinstitucion " ;
			contador++;
			codigos.put(new Integer(contador),this.usrbean.getLocation());
			select+=" AND p.idinstitucion = "+contador +
					" and   p.idpagosjg = e.idpagosjg " ;
			contador++;
			codigos.put(new Integer(contador),ClsConstants.ESTADO_PAGO_CERRADO);
			select+=" AND e.IDESTADOPAGOSJG=:"+contador+ 
					" and  e.FECHAESTADO = (select max(ee.fechaestado) " + 
 	  				"   from fcs_pagos_estadospagos ee " +
 	  				"   where ee.IDINSTITUCION = e.idinstitucion "+
					"  and  ee.IDPAGOSJG = e.IDPAGOSJG) ";
					
			/*if(!fechaDesde.equals("") && !fechaHasta.equals(""))
				select+=" AND e.fechaestado >= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaDesde)+"','YYYY/MM/DD hh24:mi:ss') AND e.fechaestado <= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaHasta)+"','YYYY/MM/DD hh24:mi:ss')";
			else if(!fechaDesde.equals(""))
				select+=" AND e.fechaestado >= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaDesde)+"','YYYY/MM/DD hh24:mi:ss')";
			else if(!fechaHasta.equals(""))
				select+=" AND e.fechaestado <= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaHasta)+"','YYYY/MM/DD hh24:mi:ss')";
			*/
			String fDesdeInc = fechaDesde; 
			   String fHastaInc = fechaHasta;
				if ((fDesdeInc != null && !fDesdeInc.trim().equals("")) || (fHastaInc != null && !fHastaInc.trim().equals(""))) {
					
					if (!fDesdeInc.equals(""))
						fDesdeInc = GstDate.getApplicationFormatDate("", fDesdeInc); 
					if (!fHastaInc.equals(""))
						fHastaInc = GstDate.getApplicationFormatDate("", fHastaInc);
					Vector vCondicion=GstDate.dateBetweenDesdeAndHastaBind("e.fechaestado", fDesdeInc, fHastaInc,contador,codigos);
					
					contador=new Integer(vCondicion.get(0).toString()).intValue();
					select +=" AND " + vCondicion.get(1) ;
				}	
			
			vPagos=(Vector)this.selectTablaBind(select,codigos);
			int contador1 ;             
	        Hashtable codigos1 = new Hashtable();
			for(int x=0;x<vPagos.size();x++)
			{    contador1=0;
				
				
				// PARA CADA PAGO CALCULO LOS TOTALES POR PERSONA (PORQUE LOS PAGOS SON DE MUCHOS LETRADOS)
				hash = (Hashtable) vPagos.get(x);
				String idpago=(String)hash.get("IDPAGO");
				String fechapago=(String)hash.get("FECHAESTADO");
				
				sql = "select a.idpersona IDPERSONASJCS,  a.idabono IDABONO, da.BANCOS_CODIGO , sum(pa.IMPORTEABONADO) CANTIDAD " +  
				" from fac_abono a, fac_abonoincluidoendisquete pa, fac_disqueteabonos da" +
				" where a.idinstitucion=pa.idinstitucion " +
				" and a.idabono=pa.idabono " +
				" and pa.IDINSTITUCION = da.IDINSTITUCION " +
				" and pa.IDDISQUETEABONO = da.IDDISQUETEABONO " ;
				contador1++;
				codigos1.put(new Integer(contador1),this.usrbean.getLocation());
				select+=" and a.idinstitucion=:" + contador1 ;
				contador1++;
				codigos1.put(new Integer(contador1),idpago);
				select+=" and a.idpagosjg= :" + contador1 +
				        " group by a.idpersona, a.idabono, da.BANCOS_CODIGO";
					
				Vector vPersonas=(Vector)this.selectTablaBind(sql,codigos1);
				
				
				for(int xx=0;xx<vPersonas.size();xx++)
				{  
					
						Hashtable hash2 = (Hashtable) vPersonas.get(xx);

						String idpersona = (String)hash2.get("IDPERSONASJCS");
						String idabono = (String)hash2.get("IDABONO");
						String cantidad = (String)hash2.get("CANTIDAD");
						
						// Con el IDPERSONA, obtenemos de CEN_CLIENTE, el asiento contable.
						asientoContable =  obtenerAsientoContableAcreedor(this.usrbean.getLocation(),idpersona);
						
						// Con el BANCOS_CODIGO, obtenemos de FAC_BANCOINSTITUCION, el numerocuenta.
						String asientoContableBanco	= obtenerAsientoContableBanco(this.usrbean.getLocation(),(String)hash2.get("BANCOS_CODIGO"));  
						
						// Obtenemos el concepto
						concepto = UtilidadesString.sustituirParaExcell(UtilidadesString.getMensajeIdioma(this.usrbean,CONCEPTO_ASIENTO9));
						// RGG concepto = obtenerDescripcion(CONCEPTO_ASIENTO9 ,usr.getLanguage(),usr);
				
						// SE CREA EL ASIENTO
						asiento++;
				
						Hashtable a = new Hashtable();
						
//						 Escribimos 1º APUNTE (ACREEDORES)
						UtilidadesHash.set(a, "FECHA", 			UtilidadesHash.getShortDate(hash, "FECHAESTADO"));
						UtilidadesHash.set(a, "CONCEPTO", 		concepto);
						UtilidadesHash.set(a, "DOCUMENTO", 		UtilidadesHash.getString(hash, "IDPAGO"));
						UtilidadesHash.set(a, "CUENTA", 		asientoContable);
						UtilidadesHash.set(a, "DEBE", 			"" + (Double.parseDouble(cantidad)));
						UtilidadesHash.set(a, "HABER", 			"0");
						UtilidadesHash.set(a, "BASEIMPONIBLE", 	"");
						UtilidadesHash.set(a, "IVA", 			"");
						UtilidadesHash.set(a, "CONTRAPARTIDA", 	asientoContableBanco);
						pwcontabilidad.write(this.generarLineaAbono(asiento, a));
						
						
//						 Escribimos 2º APUNTE (BANCO)
						UtilidadesHash.set(a, "FECHA", 			UtilidadesHash.getShortDate(hash, "FECHAESTADO"));
						UtilidadesHash.set(a, "CONCEPTO", 		concepto);
						UtilidadesHash.set(a, "DOCUMENTO", 		UtilidadesHash.getString(hash, "IDPAGO"));
						UtilidadesHash.set(a, "CUENTA", 		asientoContableBanco);
						UtilidadesHash.set(a, "DEBE", 			"0");
						UtilidadesHash.set(a, "HABER", 			"" + (Double.parseDouble(cantidad)));
						UtilidadesHash.set(a, "BASEIMPONIBLE", 	"");
						UtilidadesHash.set(a, "IVA", 			"");
						UtilidadesHash.set(a, "CONTRAPARTIDA", 	asientoContable);
						pwcontabilidad.write(this.generarLineaAbono(asiento, a));
						
						
					
					
				}
				///////////////////////////////
				
//				String[] claves = {"IDINSTITUCION","IDPAGOSJG"};
//				String[] campos = {"CONTABILIZADO","USUMODIFICACION","FECHAMODIFICACION"};
				laHash = new Hashtable();
				laHash.put("CONTABILIZADO",ClsConstants.FACTURA_ABONO_CONTABILIZADA);
				laHash.put("IDINSTITUCION",this.usrbean.getLocation());
				laHash.put("IDPAGOSJG",idpago);
				laHash.put("USUMODIFICACION",this.usrbean.getUserName());
				laHash.put("FECHAMODIFICACION","sysdate");
				
				vAsiento9.add(laHash);
//				pagoAdm.updateDirect(laHash,claves,campos);
			}
//			String[] claves = {"IDINSTITUCION","IDPAGOSJG"};
//			String[] campos = {"CONTABILIZADO","USUMODIFICACION","FECHAMODIFICACION"};
//			for(int l=0;l<vAsiento9.size();l++){
//				pagoAdm.updateDirect((Hashtable)vAsiento9.get(l),claves,campos);
//			}
			if(vAsiento9.size()>0){
				String contabilizada=(String)((Hashtable)vAsiento9.get(0)).get("CONTABILIZADO");
				String usuModificacion=(String)((Hashtable)vAsiento9.get(0)).get("USUMODIFICACION");
				String fechaModificacion=(String)((Hashtable)vAsiento9.get(0)).get("FECHAMODIFICACION");
				

				String sqlUpdate="UPDATE "+FcsPagosJGBean.T_NOMBRETABLA+" SET "+FcsPagosJGBean.C_CONTABILIZADO+"='"+contabilizada+"', "+FcsPagosJGBean.C_USUMODIFICACION+"="+usuModificacion+", "+FcsPagosJGBean.C_FECHAMODIFICACION+"="+fechaModificacion;
				int con = 0;
				tx.begin();
				for(int l = 0; l < vAsiento9.size(); l++){
				    con++;
				    if (con%numeroTransaccion==0) {
				        tx.commit();
				        tx.begin();
				    }
					String claves = (String)((Hashtable)vAsiento9.get(l)).get("IDPAGOSJG");
					Hashtable codigosUpdate = new Hashtable();
					codigosUpdate.put(new Integer(1),this.usrbean.getLocation());
					codigosUpdate.put(new Integer(2),claves);
					pagoAdm.insertSQLBind(sqlUpdate +" WHERE IDINSTITUCION=:1 AND IDPAGOSJG=:2",codigosUpdate);
				}
				tx.commit();

			
			}
			return pwcontabilidad;
		}
		catch (Exception e) 
		{
			pwcontabilidad.write("Error en asiento9: " + e.getMessage());

			try { tx.rollback(); } catch (Exception ee) {}
		    throw new ClsExceptions(e,"Error al generar asiento 9");
		} 
	}

	/**
	 * 
	 * @param pwcontabilidad
	 * @param fechaDesde
	 * @param fechaHasta
	 * @param usr
	 * @return
	 * @throws SIGAException
	 */
	// PAGOS DE JUSTICIA GRATUITA POR CAJA
	private java.io.PrintWriter generaAsiento13(java.io.PrintWriter pwcontabilidad, String fechaDesde, String fechaHasta, UserTransaction tx) throws SIGAException, ClsExceptions
	{
		// Tipos de asientos.
		// nomenclatura de fichero: #cuenta , haber, debe
		// RGG 03/11/2006 PAGOS DE JUSTICIA GRATUITA CAJA
		// 13 ---> pago de sjcs por caja
		// nomenclatura de fichero: #cuenta , haber, debe
		// Accredores    (410.xxx) 	-->  0                   --> Pago neto por per.
		// Caja         (570)   	-->  Pago neto por per.  --> 0

		String concepto 		= "";
		String asientoContable 	= ""; // Obtener de bd.
		
		// Variables
		Vector vResultado = null, vPagos = null, vContabilidad = null;
		Hashtable hash = null, laHash = null;
		String select = null, sql = null, imp = null, importeIva = null;
		Vector vAsiento13=new Vector();

		// Beans
		FcsPagosJGAdm pagoAdm 	= new FcsPagosJGAdm(this.usrbean);
		if(vContabilidad !=null && vContabilidad.size()>0)	CONTABILIDAD_IVA = ((GenParametrosBean) vContabilidad.get(0)).getValor();
		int contador = 0;             
        Hashtable codigos = new Hashtable();
		
		try
		{
			// OBTENGO LOS PAGOS IMPLICADOS (los mismos que he apuntado)
			select = " select P.IDPAGOSJG IDPAGO, e.fechaestado FECHAESTADO from fcs_pagosjg p , fcs_pagos_estadospagos e "+ 
					" where p.idinstitucion = e.idinstitucion " ;
			contador++;
			codigos.put(new Integer(contador),this.usrbean.getLocation());
			select+=" AND p.idinstitucion = :"+contador +
					" and   p.idpagosjg = e.idpagosjg " ;
			contador++;
			codigos.put(new Integer(contador),ClsConstants.ESTADO_PAGO_CERRADO);
			select+=" AND e.IDESTADOPAGOSJG=:"+contador + 
					" and  e.FECHAESTADO = (select max(ee.fechaestado) " + 
 	  				"   from fcs_pagos_estadospagos ee " +
 	  				"   where ee.IDINSTITUCION = e.idinstitucion "+
					"  and  ee.IDPAGOSJG = e.IDPAGOSJG) ";
					
			/*if(!fechaDesde.equals("") && !fechaHasta.equals(""))
				select+=" AND e.fechaestado >= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaDesde)+"','YYYY/MM/DD hh24:mi:ss') AND e.fechaestado <= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaHasta)+"','YYYY/MM/DD hh24:mi:ss')";
			else if(!fechaDesde.equals(""))
				select+=" AND e.fechaestado >= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaDesde)+"','YYYY/MM/DD hh24:mi:ss')";
			else if(!fechaHasta.equals(""))
				select+=" AND e.fechaestado <= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaHasta)+"','YYYY/MM/DD hh24:mi:ss')";
			*/
			String fDesdeInc = fechaDesde; 
			   String fHastaInc = fechaHasta;
				if ((fDesdeInc != null && !fDesdeInc.trim().equals("")) || (fHastaInc != null && !fHastaInc.trim().equals(""))) {
					
					if (!fDesdeInc.equals(""))
						fDesdeInc = GstDate.getApplicationFormatDate("", fDesdeInc); 
					if (!fHastaInc.equals(""))
						fHastaInc = GstDate.getApplicationFormatDate("", fHastaInc);
					Vector vCondicion=GstDate.dateBetweenDesdeAndHastaBind("e.fechaestado", fDesdeInc, fHastaInc,contador,codigos);
					
					contador=new Integer(vCondicion.get(0).toString()).intValue();
					select +=" AND " + vCondicion.get(1) ;
				}	
			
			vPagos=(Vector)this.selectTablaBind(select,codigos);
			int contador1;             
	        Hashtable codigos1 = new Hashtable();
			for(int x=0;x<vPagos.size();x++)
			{
				contador1 = 0;
				
				// PARA CADA PAGO CALCULO LOS TOTALES POR PERSONA (PORQUE LOS PAGOS SON DE MUCHOS LETRADOS)
				hash = (Hashtable) vPagos.get(x);
				String idpago=(String)hash.get("IDPAGO");
				String fechapago=(String)hash.get("FECHAESTADO");
				
				sql = "select a.idpersona IDPERSONASJCS, a.idabono IDABONO, sum(pa.importe) CANTIDAD  " +
				" from fac_abono a, fac_pagoabonoefectivo pa " +
				" where a.idinstitucion=pa.idinstitucion " +
				" and a.idabono=pa.idabono " ;
				contador1++;
				codigos1.put(new Integer(contador1),ClsConstants.ESTADO_PAGO_CERRADO);
				select+=" and a.idinstitucion=:"+contador1 ;
				contador1++;
				codigos1.put(new Integer(contador1),idpago);
				select+=" and a.idpagosjg= :" + contador1 +
				        " group by a.idpersona, a.idabono";
					
				Vector vPersonas=(Vector)this.selectTablaBind(sql,codigos1);
					
				for(int xx=0;xx<vPersonas.size();xx++)
				{
					
					Hashtable hash2 = (Hashtable) vPersonas.get(xx);

					String idpersona = (String)hash2.get("IDPERSONASJCS");
					String idabono = (String)hash2.get("IDABONO");
					String cantidad = (String)hash2.get("CANTIDAD");
					
					// Con el IDPERSONA, obtenemos de CEN_CLIENTE, el asiento contable.
					asientoContable =  obtenerAsientoContableAcreedor(this.usrbean.getLocation(),idpersona);
					
					// Obtenemos el concepto
					concepto = UtilidadesString.sustituirParaExcell(UtilidadesString.getMensajeIdioma(this.usrbean,CONCEPTO_ASIENTO9));
					// RGG concepto = obtenerDescripcion(CONCEPTO_ASIENTO9 ,usr.getLanguage(),usr);
			
					// SE CREA EL ASIENTO
					asiento++;
			
					Hashtable a = new Hashtable();
					
//						 Escribimos 1º APUNTE (ACREEDORES)
					UtilidadesHash.set(a, "FECHA", 			UtilidadesHash.getShortDate(hash, "FECHAESTADO"));
					UtilidadesHash.set(a, "CONCEPTO", 		concepto);
					UtilidadesHash.set(a, "DOCUMENTO", 		UtilidadesHash.getString(hash, "IDPAGO"));
					UtilidadesHash.set(a, "CUENTA", 		asientoContable);
					UtilidadesHash.set(a, "DEBE", 			"" + (Double.parseDouble(cantidad)));
					UtilidadesHash.set(a, "HABER", 			"0");
					UtilidadesHash.set(a, "BASEIMPONIBLE", 	"");
					UtilidadesHash.set(a, "IVA", 			"");
					UtilidadesHash.set(a, "CONTRAPARTIDA", 	CONTABILIDAD_CAJA);
					pwcontabilidad.write(this.generarLineaAbono(asiento, a));
					
					
//						 Escribimos 2º APUNTE (BANCO)
					UtilidadesHash.set(a, "FECHA", 			UtilidadesHash.getShortDate(hash, "FECHAESTADO"));
					UtilidadesHash.set(a, "CONCEPTO", 		concepto);
					UtilidadesHash.set(a, "DOCUMENTO", 		UtilidadesHash.getString(hash, "IDPAGO"));
					UtilidadesHash.set(a, "CUENTA", 		CONTABILIDAD_CAJA);
					UtilidadesHash.set(a, "DEBE", 			"0");
					UtilidadesHash.set(a, "HABER", 			"" + (Double.parseDouble(cantidad)));
					UtilidadesHash.set(a, "BASEIMPONIBLE", 	"");
					UtilidadesHash.set(a, "IVA", 			"");
					UtilidadesHash.set(a, "CONTRAPARTIDA", 	asientoContable);
					pwcontabilidad.write(this.generarLineaAbono(asiento, a));
					
					
				
				
				}
				///////////////////////////////
				
//				String[] claves = {"IDINSTITUCION","IDPAGOSJG"};
//				String[] campos = {"CONTABILIZADO","USUMODIFICACION","FECHAMODIFICACION"};
				laHash = new Hashtable();
				laHash.put("CONTABILIZADO",ClsConstants.FACTURA_ABONO_CONTABILIZADA);
				laHash.put("IDINSTITUCION",this.usrbean.getLocation());
				laHash.put("IDPAGOSJG",idpago);
				laHash.put("USUMODIFICACION",this.usrbean.getUserName());
				laHash.put("FECHAMODIFICACION","sysdate");
				
				vAsiento13.add(laHash);
//				pagoAdm.updateDirect(laHash,claves,campos);
			}
//			String[] claves = {"IDINSTITUCION","IDPAGOSJG"};
//			String[] campos = {"CONTABILIZADO","USUMODIFICACION","FECHAMODIFICACION"};
//			for(int l=0;l<vAsiento13.size();l++){
//				pagoAdm.updateDirect((Hashtable)vAsiento13.get(l),claves,campos);
//			}
			
			if(vAsiento13.size()>0){
				String contabilizada=(String)((Hashtable)vAsiento13.get(0)).get("CONTABILIZADO");
				String usuModificacion=(String)((Hashtable)vAsiento13.get(0)).get("USUMODIFICACION");
				String fechaModificacion=(String)((Hashtable)vAsiento13.get(0)).get("FECHAMODIFICACION");
				
				/*String claves="'"+(String)((Hashtable)vAsiento13.get(0)).get("IDINSTITUCION")+"%%%"+(String)((Hashtable)vAsiento13.get(0)).get("IDPAGOSJG")+"'";
				
				for(int l=1;l<vAsiento13.size();l++){
					claves=claves+",'"+(String)((Hashtable)vAsiento13.get(l)).get("IDINSTITUCION")+"%%%"+(String)((Hashtable)vAsiento13.get(l)).get("IDPAGOSJG")+"'";
				}
				String sqlUpdate="UPDATE "+FcsPagosJGBean.T_NOMBRETABLA+" SET "+FcsPagosJGBean.C_CONTABILIZADO+"='"+contabilizada+"', "+FcsPagosJGBean.C_USUMODIFICACION+"="+usuModificacion+", "+FcsPagosJGBean.C_FECHAMODIFICACION+"="+fechaModificacion;
				sqlUpdate=sqlUpdate+" WHERE (IDINSTITUCION||'%%%'||IDPAGOSJG) IN ("+claves+")";
				pagoAdm.insertSQL(sqlUpdate);*/
				
				int cuantosRegEnIN=800;// se Ha modificado la actualizacion para que las claves no contengan mas de 1000 elementos ya que si no da error la actualizacion porque oracle
                // tiene esa restriccion en las listas. Ahora las listas tendran como mucho 800 elementos
				String claves = "";
				int contadorUpdate=1;
				Hashtable codigosUpdate = new Hashtable();
				
				codigosUpdate.put(new Integer(contadorUpdate),this.usrbean.getLocation());
				
				String claves1=" WHERE IDINSTITUCION=:"+contadorUpdate;
				String sqlUpdate="UPDATE "+FcsPagosJGBean.T_NOMBRETABLA+" SET "+FcsPagosJGBean.C_CONTABILIZADO+"='"+contabilizada+"', "+FcsPagosJGBean.C_USUMODIFICACION+"="+usuModificacion+", "+FcsPagosJGBean.C_FECHAMODIFICACION+"="+fechaModificacion;
				for(int l = 0; l < vAsiento13.size(); l+=cuantosRegEnIN){
					claves = "";
				    int ii;
					for (ii = 0; ii < cuantosRegEnIN && l+ii < vAsiento13.size(); ii++) {
						claves=claves+","+(String)((Hashtable)vAsiento13.get(l+ii)).get("IDPAGOSJG");
					}
					claves = claves.substring(1);
					pagoAdm.insertSQL(sqlUpdate +claves1+ " AND (IDPAGOSJG) IN ("+claves+")");
                }
			}
			return pwcontabilidad;
		}
		catch (Exception e) 
		{
			pwcontabilidad.write("Error en asiento13: " + e.getMessage());
			try { tx.rollback(); } catch (Exception ee) {}
		    throw new ClsExceptions(e,"Error al generar asiento 13");
		} 
	}

	
	// FACTURA PRODUCTOS
	/*
	private java.io.PrintWriter generaAsiento11(java.io.PrintWriter pwcontabilidad, String fechaDesde, String fechaHasta, UserTransaction tx) throws SIGAException, ClsExceptions
	{
		// Tipos de asientos.
		// vamos a generar dos tipos de asientos 1. Uno para productos y otro para servicios.
		// nomenclatura de fichero: #cuenta , haber, debe
		// 1------> PRODUCTOS
		// Ingresos. venta. produ. (701.xxx) --> Bruto por servicio (1) 	--> 0
		// Cliente(430.xxx)					 --> 0						--> Negocio(1)
		String concepto 		= "";
		String asientoContable 	= ""; // Obtener de bd.
		// Variables
		Vector vResultado = null, vFacturas = null, vContabilidad = null;
		Hashtable hash = null, laHash = null;
		String select = null, sql = null, imp = null, importeIva = null;
		// Beans
		FacRegistroFichContaAdm adm = new FacRegistroFichContaAdm(usr);
		FacFacturaAdm facturaAdm 	= new FacFacturaAdm(usr);
		if(vContabilidad !=null && vContabilidad.size()>0)	CONTABILIDAD_IVA = ((GenParametrosBean) vContabilidad.get(0)).getValor();
		try
		{
			// OBTENGO LAS FACTURAS IMPLICADAS DE PRODUCTOS
			select = " SELECT f.IDFACTURA, f.NUMEROFACTURA, f.IDPERSONA, (lf.preciounitario*lf.cantidad) IMPORTE, f.FECHAEMISION , c.IDINSTITUCION, c.IDTIPOPRODUCTO, c.IDPRODUCTO, c.IDPRODUCTOINSTITUCION " + 
			" from fac_factura f, fac_lineafactura lf , pys_compra c " +
//			" , FAC_FACTURAESTADO FE WHERE F.IDINSTITUCION=FE.IDINSTITUCION AND F.IDFACTURA=FE.IDFACTURA AND FE.ESTADO<>'7' " +
//			" where F_SIGA_ESTADOSFACTURA(f.IDINSTITUCION,f.IDFACTURA)<>7  " +
			" where F.NUMEROFACTURA IS NOT NULL  " +
			" AND f.IDINSTITUCION = lf.IDINSTITUCION " + 
			" and f.IDFACTURA = lf.IDFACTURA " + 
			" and c.IDINSTITUCION = lf.IDINSTITUCION " + 
			" and c.IDFACTURA = lf.IDFACTURA " + 
			" and c.NUMEROLINEA = lf.NUMEROLINEA " +
			" and f.IDINSTITUCION = "+usr.getLocation();
			if(!fechaDesde.equals("") && !fechaHasta.equals(""))
				select+=" AND f.FECHAEMISION >= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaDesde)+"','YYYY/MM/DD hh24:mi:ss') AND f.FECHAEMISION <= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaHasta)+"','YYYY/MM/DD hh24:mi:ss')";
			else if(!fechaDesde.equals(""))
				select+=" AND f.FECHAEMISION >= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaDesde)+"','YYYY/MM/DD hh24:mi:ss')";
			else if(!fechaHasta.equals(""))
				select+=" AND f.FECHAEMISION <= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaHasta)+"','YYYY/MM/DD hh24:mi:ss')";
			vFacturas=(Vector)adm.selectTabla(select);
			
			for(int x=0;x<vFacturas.size();x++)
			{
				// PARA CADA FACTURA CALCULO LOS TOTALES
				hash = (Hashtable) vFacturas.get(x);
				imp = (String) hash.get("IMPORTE");

				// Obtenemos el asiento contable del productos
				asientoContable=obtenerAsientoContableProducto((String) hash.get("IDINSTITUCION"),(String) hash.get("IDTIPOPRODUCTO"),(String) hash.get("IDPRODUCTO"),(String) hash.get("IDPRODUCTOINSTITUCION"),usr);
				
				// Obtenemos el concepto
				concepto = UtilidadesString.sustituirParaExcell(UtilidadesString.getMensajeIdioma(usr,CONCEPTO_ASIENTO11));
				// RGG concepto = obtenerDescripcion(CONCEPTO_ASIENTO11,usr.getLanguage(),usr);
				
				// SE CREA EL ASIENTO
				asiento++;
				
				Hashtable a = new Hashtable();
				
				// Escribimos 1º APUNTE
				UtilidadesHash.set(a, "FECHA", 			UtilidadesHash.getShortDate(hash, FacFacturaBean.C_FECHAEMISION));
				UtilidadesHash.set(a, "CONCEPTO", 		concepto);
				UtilidadesHash.set(a, "DOCUMENTO", 		UtilidadesHash.getString(hash, "NUMEROFACTURA"));
				UtilidadesHash.set(a, "CUENTA", 		asientoContable);
				UtilidadesHash.set(a, "DEBE", 			"0");
				UtilidadesHash.set(a, "HABER", 			"" + (Double.parseDouble(imp)));
				UtilidadesHash.set(a, "BASEIMPONIBLE", 	"");
				UtilidadesHash.set(a, "IVA", 			"");
				UtilidadesHash.set(a, "CONTRAPARTIDA", 	CONTAB_VENTAS_PRODUCTOS);
				pwcontabilidad.write(this.generarLineaAbono(asiento, a));
				
				// Escribimos 2º APUNTE
				a.clear();
				UtilidadesHash.set(a, "FECHA", 			UtilidadesHash.getShortDate(hash, FacFacturaBean.C_FECHAEMISION));
				UtilidadesHash.set(a, "CONCEPTO", 		concepto);
				UtilidadesHash.set(a, "DOCUMENTO", 		UtilidadesHash.getString(hash, "NUMEROFACTURA"));
				UtilidadesHash.set(a, "CUENTA", 		CONTAB_VENTAS_PRODUCTOS);
				UtilidadesHash.set(a, "DEBE", 			"" + (Double.parseDouble(imp)));
				UtilidadesHash.set(a, "HABER", 			"0");
				UtilidadesHash.set(a, "BASEIMPONIBLE", 	"");
				UtilidadesHash.set(a, "IVA", 			"");
				UtilidadesHash.set(a, "CONTRAPARTIDA", 	asientoContable);
				pwcontabilidad.write(this.generarLineaAbono(asiento, a));
				
				
				// SE SUPONE YA MARCADA COMO CONTABILIZADA
				String[] claves = {"IDINSTITUCION","IDFACTURA"};
				String[] campos = {"CONTABILIZADA","USUMODIFICACION","FECHAMODIFICACION"};
				laHash = new Hashtable();
				laHash.put("CONTABILIZADA",ClsConstants.FACTURA_ABONO_CONTABILIZADA);
				laHash.put("IDINSTITUCION",usr.getLocation());
				laHash.put("IDFACTURA",hash.get("IDFACTURA"));
				laHash.put("USUMODIFICACION",usr.getUserName());
				laHash.put("FECHAMODIFICACION","sysdate");
				facturaAdm.updateDirect(laHash,claves,campos);
			}
			return pwcontabilidad;
		}
		catch (Exception e) 
		{
			pwcontabilidad.write("Error en asiento11: " + e.getMessage());
			try { tx.rollback(); } catch (Exception ee) {}
		    throw new ClsExceptions(e,"Error al generar asiento 11");
		} 
	}
	*/
	
	
	// FACTURA SERVICIOS
	/*	
	private java.io.PrintWriter generaAsiento12(java.io.PrintWriter pwcontabilidad, String fechaDesde, String fechaHasta, UserTransaction tx) throws SIGAException, ClsExceptions
	{
		// Tipos de asientos.
		// vamos a generar dos tipos de asientos 1. Uno para productos y otro para servicios.
		// nomenclatura de fichero: #cuenta , haber, debe
		// 1------> SERVICIOS
		// Ingresos. prestacion servicios. (705.xxx) --> Bruto por servicio (1) 	--> 0
		// Cliente(430.xxx)							 --> 0						--> Negocio(1)
		String concepto 		= "";
		String asientoContable 	= ""; // Obtener de bd.
		// Variables
		Vector vResultado = null, vFacturas = null, vContabilidad = null;
		Hashtable hash = null, laHash = null;
		String select = null, sql = null, imp = null, importeIva = null;
		// Beans
		FacRegistroFichContaAdm adm = new FacRegistroFichContaAdm(usr);
		FacFacturaAdm facturaAdm 	= new FacFacturaAdm(usr);
		if(vContabilidad !=null && vContabilidad.size()>0)	CONTABILIDAD_IVA = ((GenParametrosBean) vContabilidad.get(0)).getValor();
		try
		{
			// OBTENGO LAS FACTURAS IMPLICADAS DE PRODUCTOS
			select = " SELECT f.IDFACTURA, f.NUMEROFACTURA, f.IDPERSONA, (lf.preciounitario*lf.cantidad) IMPORTE, f.FECHAEMISION , c.IDINSTITUCION, c.IDTIPOSERVICIOS, c.IDSERVICIO, c.IDSERVICIOSINSTITUCION " + 
			" from fac_factura f, fac_lineafactura lf , fac_facturacionsuscripcion c " +
//			" , FAC_FACTURAESTADO FE WHERE F.IDINSTITUCION=FE.IDINSTITUCION AND F.IDFACTURA=FE.IDFACTURA AND FE.ESTADO<>'7' " +
//			" where F_SIGA_ESTADOSFACTURA(f.IDINSTITUCION,f.IDFACTURA)<>7  " +
			" where F.NUMEROFACTURA IS NOT NULL  " +
			" AND f.IDINSTITUCION = lf.IDINSTITUCION " + 
			" and f.IDFACTURA = lf.IDFACTURA " + 
			" and c.IDINSTITUCION = lf.IDINSTITUCION " + 
			" and c.IDFACTURA = lf.IDFACTURA " + 
			" and c.NUMEROLINEA = lf.NUMEROLINEA " +
			" and f.IDINSTITUCION = "+usr.getLocation();

			if(!fechaDesde.equals("") && !fechaHasta.equals(""))
				select+=" AND f.FECHAEMISION >= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaDesde)+"','YYYY/MM/DD hh24:mi:ss') AND f.FECHAEMISION <= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaHasta)+"','YYYY/MM/DD hh24:mi:ss')";
			else if(!fechaDesde.equals(""))
				select+=" AND f.FECHAEMISION >= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaDesde)+"','YYYY/MM/DD hh24:mi:ss')";
			else if(!fechaHasta.equals(""))
				select+=" AND f.FECHAEMISION <= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaHasta)+"','YYYY/MM/DD hh24:mi:ss')";
			vFacturas=(Vector)adm.selectTabla(select);
			
			for(int x=0;x<vFacturas.size();x++)
			{
				// PARA CADA FACTURA CALCULO LOS TOTALES
				hash = (Hashtable) vFacturas.get(x);
				imp = (String) hash.get("IMPORTE");

				// Obtenemos el asiento contable del productos
				asientoContable=obtenerAsientoContableServicio((String) hash.get("IDINSTITUCION"),(String) hash.get("IDTIPOSERVICIOS"),(String) hash.get("IDSERVICIO"),(String) hash.get("IDSERVICIOSINSTITUCION"), usr);
				
				// Obtenemos el concepto
				concepto = UtilidadesString.sustituirParaExcell(UtilidadesString.getMensajeIdioma(usr,CONCEPTO_ASIENTO12));
				// RGG concepto = obtenerDescripcion(CONCEPTO_ASIENTO12,usr.getLanguage(),usr);
				
				// SE CREA EL ASIENTO
				asiento++;
				
				Hashtable a = new Hashtable();
				
				// Escribimos 1º APUNTE
				UtilidadesHash.set(a, "FECHA", 			UtilidadesHash.getShortDate(hash, FacFacturaBean.C_FECHAEMISION));
				UtilidadesHash.set(a, "CONCEPTO", 		concepto);
				UtilidadesHash.set(a, "DOCUMENTO", 		UtilidadesHash.getString(hash, "NUMEROFACTURA"));
				UtilidadesHash.set(a, "CUENTA", 		asientoContable);
				UtilidadesHash.set(a, "DEBE", 			"0");
				UtilidadesHash.set(a, "HABER", 			"" + (Double.parseDouble(imp)));
				UtilidadesHash.set(a, "BASEIMPONIBLE", 	"");
				UtilidadesHash.set(a, "IVA", 			"");
				UtilidadesHash.set(a, "CONTRAPARTIDA", 	CONTAB_VENTAS_SERVICIOS);
				pwcontabilidad.write(this.generarLineaAbono(asiento, a));
				
				// Escribimos 2º APUNTE
				a.clear();
				UtilidadesHash.set(a, "FECHA", 			UtilidadesHash.getShortDate(hash, FacFacturaBean.C_FECHAEMISION));
				UtilidadesHash.set(a, "CONCEPTO", 		concepto);
				UtilidadesHash.set(a, "DOCUMENTO", 		UtilidadesHash.getString(hash, "NUMEROFACTURA"));
				UtilidadesHash.set(a, "CUENTA", 		CONTAB_VENTAS_SERVICIOS);
				UtilidadesHash.set(a, "DEBE", 			"" + (Double.parseDouble(imp)));
				UtilidadesHash.set(a, "HABER", 			"0");
				UtilidadesHash.set(a, "BASEIMPONIBLE", 	"");
				UtilidadesHash.set(a, "IVA", 			"");
				UtilidadesHash.set(a, "CONTRAPARTIDA", 	asientoContable);
				pwcontabilidad.write(this.generarLineaAbono(asiento, a));
				
				
				// SE SUPONE YA MARCADA COMO CONTABILIZADA
				String[] claves = {"IDINSTITUCION","IDFACTURA"};
				String[] campos = {"CONTABILIZADA","USUMODIFICACION","FECHAMODIFICACION"};
				laHash = new Hashtable();
				laHash.put("CONTABILIZADA",ClsConstants.FACTURA_ABONO_CONTABILIZADA);
				laHash.put("IDINSTITUCION",usr.getLocation());
				laHash.put("IDFACTURA",hash.get("IDFACTURA"));
				laHash.put("USUMODIFICACION",usr.getUserName());
				laHash.put("FECHAMODIFICACION","sysdate");
				facturaAdm.updateDirect(laHash,claves,campos);
			}
			return pwcontabilidad;
		}
		catch (Exception e) 
		{
			pwcontabilidad.write("Error en asiento12: " + e.getMessage());
			try { tx.rollback(); } catch (Exception ee) {}
		    throw new ClsExceptions(e,"Error al generar asiento 12");
		} 
	}
	*/
	
	/**
	 * 
	 * @param pwcontabilidad
	 * @param fechaDesde
	 * @param fechaHasta
	 * @param usr
	 * @return
	 * @throws SIGAException
	 */
	// ABONO (devoluciones de facturas completas)
	private java.io.PrintWriter generaAsiento2(java.io.PrintWriter pwcontabilidad, String fechaDesde, String fechaHasta, UserTransaction tx) throws SIGAException, ClsExceptions
	{
	    // RGG 21/11/2007
		String concepto 		= "";
		String asientoContable 	= null;
		// Variables
		Vector vResultado 	= null, vLineasAbonos 	= null;
		Hashtable hash 		= null, laHash 	= null;
		String select 		= null, sql 	= null, imp = null, importeIva = null;
		Vector vAsiento2=new Vector();
		// Beans
		FacAbonoAdm abonoAdm 	= new FacAbonoAdm(this.usrbean);
		int contador = 0;             
        Hashtable codigos = new Hashtable();
		try
		{
		    // RGG atencion a los importes negativos
			select = " SELECT A.IDABONO, A.NUMEROABONO, A.IDPERSONA, A.FECHA, F.NUMEROFACTURA, (L.CANTIDAD * L.PRECIOUNITARIO * -1) IMPNETO, L.IVA, " + 
			    " ((L.CANTIDAD * L.PRECIOUNITARIO) * (L.IVA/100) * -1) IMPIVA, DECODE(F.IDPERSONADEUDOR,NULL,F.IDPERSONA,F.IDPERSONADEUDOR) IDPERSONA, " +
			    " L.DESCRIPCION, L.CTAPRODUCTOSERVICIO, L.CTAIVA, P.CONFDEUDOR, P.CONFINGRESOS, P.CTAINGRESOS, P.CTACLIENTES " +
			    " FROM   FAC_ABONO A, FAC_FACTURA F, FAC_LINEAFACTURA L, FAC_FACTURACIONPROGRAMADA P " +
			    " WHERE  A.IDINSTITUCION = F.IDINSTITUCION " +
			    " AND    A.IDFACTURA = F.IDFACTURA " +
			    " AND    F.IDINSTITUCION = L.IDINSTITUCION " +
			    " AND    F.IDFACTURA = L.IDFACTURA " +
			    " AND    F.IDINSTITUCION = P.IDINSTITUCION " +
			    " AND    F.IDSERIEFACTURACION = P.IDSERIEFACTURACION " +
			    " AND    F.IDPROGRAMACION = P.IDPROGRAMACION " +
			    " AND    A.IDPAGOSJG IS NULL  " ; // pagos no sjcs
			contador++;
			codigos.put(new Integer(contador),"0");
			select+= " AND    A.IMPEXCESIVO =:"+contador; // pagos no importe excesivo 
			contador++;
			codigos.put(new Integer(contador),this.usrbean.getLocation());
			select += " AND    A.IDINSTITUCION = :"+contador;
			
			
			String fDesdeInc = fechaDesde; 
			   String fHastaInc = fechaHasta;
				if ((fDesdeInc != null && !fDesdeInc.trim().equals("")) || (fHastaInc != null && !fHastaInc.trim().equals(""))) {
					
					if (!fDesdeInc.equals(""))
						fDesdeInc = GstDate.getApplicationFormatDate("", fDesdeInc); 
					if (!fHastaInc.equals(""))
						fHastaInc = GstDate.getApplicationFormatDate("", fHastaInc);
					Vector vCondicion=GstDate.dateBetweenDesdeAndHastaBind("A.FECHA", fDesdeInc, fHastaInc,contador,codigos);
					
					contador=new Integer(vCondicion.get(0).toString()).intValue();
					select +=" AND " + vCondicion.get(1) ;
				}	
				
			/*if(!fechaDesde.equals("") && !fechaHasta.equals(""))
				select+=" AND A.FECHA >= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaDesde)+"','YYYY/MM/DD hh24:mi:ss') AND A.FECHA <= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaHasta)+"','YYYY/MM/DD hh24:mi:ss')";
			else if(!fechaDesde.equals(""))
				select+=" AND A.FECHA >= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaDesde)+"','YYYY/MM/DD hh24:mi:ss')";
			else if(!fechaHasta.equals(""))
				select+=" AND A.FECHA <= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaHasta)+"','YYYY/MM/DD hh24:mi:ss')";
			*/
			select += " ORDER BY A.IDABONO ";
				    
			vLineasAbonos=(Vector)this.selectTablaBind(select,codigos);
			
			String idAbonoAnt = ""; 
			String idAbono = ""; 
			String asientoClientes = ""; 
			String asientoIngresos = ""; 
			
			
			for(int x=0;x<vLineasAbonos.size();x++)
			{
			    hash = (Hashtable) vLineasAbonos.get(x);
				
			    idAbono = (String)hash.get("IDABONO");
				    
				// PARA CADA LINEA FACTURA COMPRUEBO LA CONFIGURACIÓN DE CUENTAS
			    String confClientes=(String)hash.get("CONFDEUDOR");
			    String confIngresos=(String)hash.get("CONFINGRESOS");
			    String ctaClientes=(String)hash.get("CTACLIENTES");
			    String ctaIngresos=(String)hash.get("CTAINGRESOS");
			 
			        
			    // importes
			    imp=UtilidadesNumero.redondea((String)hash.get("IMPNETO"), 2);
			    importeIva= UtilidadesNumero.redondea((String)hash.get("IMPIVA"), 2);
			    String porcentajeIva= UtilidadesNumero.redondea((String)hash.get("IVA"), 2);
			    
			    // Control de iva 0
			    boolean ivacero=false;
			    try {
			        Double d = new Double(porcentajeIva);
			        if (d.doubleValue()==0.0) 
			            ivacero=true;
			    } catch (NumberFormatException nf) {
			    }

			    // concepto
				concepto = UtilidadesString.sustituirParaExcell(UtilidadesString.getMensajeIdioma(this.usrbean,CONCEPTO_ASIENTO2) + ": "+(String)hash.get("DESCRIPCION"));
				
				// asientos configurables
				String asientoIVA = (String)hash.get("CTAIVA");
				if (confClientes.equals("F")) {
				    asientoClientes =  ctaClientes;
				} else {
				    asientoClientes =  ctaClientes + obtenerAsientoContable(this.usrbean.getLocation(),(String)hash.get("IDPERSONA"));
				}
				if (confIngresos.equals("F")) {
				    asientoIngresos =  ctaIngresos;
				} else if (confIngresos.equals("C")) {
				    asientoIngresos =  ctaIngresos + obtenerAsientoContable(this.usrbean.getLocation(),(String)hash.get("IDPERSONA"));
				} else {
				    asientoIngresos =  ctaIngresos + (String)hash.get("CTAPRODUCTOSERVICIO");
				}

				// aumentamos el contador de asientos
				asiento++;
				
				Hashtable a = new Hashtable();
					
				// Escribimos 1º APUNTE
				a.clear();
				UtilidadesHash.set(a, "FECHA", 			UtilidadesHash.getShortDate(hash, "FECHA"));
				UtilidadesHash.set(a, "CONCEPTO", 		concepto);
				UtilidadesHash.set(a, "DOCUMENTO", 		UtilidadesHash.getString(hash, "NUMEROABONO"));
				UtilidadesHash.set(a, "CUENTA", 		asientoClientes);
				UtilidadesHash.set(a, "DEBE", 			"" + (Double.parseDouble(imp) + Double.parseDouble(importeIva)));
				UtilidadesHash.set(a, "HABER", 			"0");
				UtilidadesHash.set(a, "BASEIMPONIBLE", 	"");
				UtilidadesHash.set(a, "IVA", 			"");
				UtilidadesHash.set(a, "CONTRAPARTIDA", 	asientoIngresos);
				pwcontabilidad.write(this.generarLineaAbono(asiento, a));
				
				// Escribimos 2º APUNTE
				a.clear();
				UtilidadesHash.set(a, "FECHA", 			UtilidadesHash.getShortDate(hash, "FECHA"));
				UtilidadesHash.set(a, "CONCEPTO", 		concepto);
				UtilidadesHash.set(a, "DOCUMENTO", 		UtilidadesHash.getString(hash, "NUMEROABONO"));
				UtilidadesHash.set(a, "CUENTA", 		asientoIngresos);
				UtilidadesHash.set(a, "DEBE", 			"0");
				UtilidadesHash.set(a, "HABER", 			imp);
				UtilidadesHash.set(a, "BASEIMPONIBLE", 	"");
				UtilidadesHash.set(a, "IVA", 			"");
				UtilidadesHash.set(a, "CONTRAPARTIDA", 	asientoClientes);
				pwcontabilidad.write(this.generarLineaAbono(asiento, a));
				
				// Escribimos 3º APUNTE
				if (!ivacero) { 
					a.clear();
					UtilidadesHash.set(a, "FECHA", 			UtilidadesHash.getShortDate(hash, "FECHA"));
					UtilidadesHash.set(a, "CONCEPTO", 		concepto);
					UtilidadesHash.set(a, "DOCUMENTO", 		UtilidadesHash.getString(hash, "NUMEROABONO"));
					UtilidadesHash.set(a, "CUENTA", 		asientoIVA);
					UtilidadesHash.set(a, "DEBE", 			"0");
					UtilidadesHash.set(a, "HABER", 			importeIva);
					UtilidadesHash.set(a, "BASEIMPONIBLE", 	imp);
					UtilidadesHash.set(a, "IVA", 			porcentajeIva);
					UtilidadesHash.set(a, "CONTRAPARTIDA", 	asientoClientes);
					pwcontabilidad.write(this.generarLineaAbono(asiento, a));
				}
			
				///////////////////////////////
				
				if (!idAbono.equals(idAbonoAnt)) {
//					String[] claves = {"IDINSTITUCION","IDABONO"};
//					String[] campos = {"CONTABILIZADA","USUMODIFICACION","FECHAMODIFICACION"};
					laHash = new Hashtable();
					laHash.put("CONTABILIZADA",ClsConstants.FACTURA_ABONO_CONTABILIZADA);
					laHash.put("IDINSTITUCION",this.usrbean.getLocation());
					laHash.put("IDABONO",hash.get("IDABONO"));
					laHash.put("USUMODIFICACION",this.usrbean.getUserName());
					laHash.put("FECHAMODIFICACION","sysdate");
					
					vAsiento2.add(laHash);
//					abonoAdm.updateDirect(laHash,claves,campos);
//					
					idAbonoAnt = idAbono;
				}
			}
//			String[] claves = {"IDINSTITUCION","IDABONO"};
//			String[] campos = {"CONTABILIZADA","USUMODIFICACION","FECHAMODIFICACION"};
//			for(int l=0;l<vAsiento2.size();l++){
//				abonoAdm.updateDirect((Hashtable)vAsiento2.get(l),claves,campos);
//			}
			
			if(vAsiento2.size()>0){
				String contabilizada=(String)((Hashtable)vAsiento2.get(0)).get("CONTABILIZADA");
				String usuModificacion=(String)((Hashtable)vAsiento2.get(0)).get("USUMODIFICACION");
				String fechaModificacion=(String)((Hashtable)vAsiento2.get(0)).get("FECHAMODIFICACION");
				
			

				

				String sqlUpdate="UPDATE "+FacAbonoBean.T_NOMBRETABLA+" SET "+FacAbonoBean.C_CONTABILIZADA+"='"+contabilizada+"', "+FacAbonoBean.C_USUMODIFICACION+"="+usuModificacion+", "+FacAbonoBean.C_FECHAMODIFICACION+"="+fechaModificacion;
				int con = 0;
				tx.begin();
				for(int l = 0; l < vAsiento2.size(); l++){
				    con++;
				    if (con%numeroTransaccion==0) {
				        tx.commit();
				        tx.begin();
				    }
					String claves = (String)((Hashtable)vAsiento2.get(l)).get("IDABONO");
					Hashtable codigosUpdate = new Hashtable();
					codigosUpdate.put(new Integer(1),this.usrbean.getLocation());
					codigosUpdate.put(new Integer(2),claves);
					abonoAdm.insertSQLBind(sqlUpdate +" WHERE IDINSTITUCION=:1 AND IDABONO=:2",codigosUpdate);
				}
				tx.commit();
				
			}
			return pwcontabilidad;
		}
		catch (Exception e) 
		{
			pwcontabilidad.write("Error en asiento2: " + e.getMessage());
			try { tx.rollback(); } catch (Exception ee) {}
		    throw new ClsExceptions(e,"Error al generar asiento 2");
		} 
	}
	
	/**
	 * 
	 * @param pwcontabilidad
	 * @param fechaDesde
	 * @param fechaHasta
	 * @param usr
	 * @return
	 * @throws SIGAException
	 */
	// ABONO (devoluciones de facturas completas)
	private java.io.PrintWriter generaAsiento2b(java.io.PrintWriter pwcontabilidad, String fechaDesde, String fechaHasta, UserTransaction tx) throws SIGAException, ClsExceptions
	{
	    // RGG 21/11/2007
		String concepto 		= "";
		String asientoContable 	= null;
		// Variables
		Vector vResultado 	= null, vLineasAbonos 	= null;
		Hashtable hash 		= null, laHash 	= null;
		String select 		= null, sql 	= null, imp = null, importeIva = null;
		Vector vAsiento2b=new Vector();
		// Beans
		FacAbonoAdm abonoAdm 	= new FacAbonoAdm(this.usrbean);
		int contador = 0;             
        Hashtable codigos = new Hashtable();
		try
		{
		    // RGG atencion a los importes negativos
			select = " SELECT A.IDABONO, A.NUMEROABONO, A.IDPERSONA, A.FECHA, " + 
			    " (LA.CANTIDAD * LA.PRECIOUNITARIO * -1) IMPNETO, ((LA.CANTIDAD * LA.PRECIOUNITARIO) * (LA.IVA/100) * -1) IMPIVA, LA.IVA, LA.DESCRIPCIONLINEA DESCRIPCION, L.CTAPRODUCTOSERVICIO, L.CTAIVA, " + 
			    " F.NUMEROFACTURA, DECODE(F.IDPERSONADEUDOR,NULL,F.IDPERSONA,F.IDPERSONADEUDOR) IDPERSONA,  " +
			    " P.CONFDEUDOR, P.CONFINGRESOS, P.CTAINGRESOS, P.CTACLIENTES " +
			    " FROM   FAC_ABONO A, FAC_FACTURA F, FAC_LINEAABONO LA, FAC_LINEAFACTURA L, FAC_FACTURACIONPROGRAMADA P " +
			    " WHERE  A.IDINSTITUCION = F.IDINSTITUCION " +
			    " AND    A.IDFACTURA = F.IDFACTURA " +
			    " AND    A.IDINSTITUCION = LA.IDINSTITUCION " +
			    " AND    A.IDABONO = LA.IDABONO " +
			    " AND    LA.IDINSTITUCION = L.IDINSTITUCION (+) " +
			    " AND    LA.IDFACTURA = L.IDFACTURA(+) " +
			    " AND    LA.LINEAFACTURA = L.NUMEROLINEA(+) " +
			    " AND    F.IDINSTITUCION = P.IDINSTITUCION " +
			    " AND    F.IDSERIEFACTURACION = P.IDSERIEFACTURACION " +
			    " AND    F.IDPROGRAMACION = P.IDPROGRAMACION " +
			    " AND    A.IDPAGOSJG IS NULL  " + // pagos no sjcs
			    " AND    A.IMPEXCESIVO = '1'  " ; // pagos de importe excesivo
			contador++;
			codigos.put(new Integer(contador),this.usrbean.getLocation());
			select+=" AND    A.IDINSTITUCION = :"+contador;
			    
			/*if(!fechaDesde.equals("") && !fechaHasta.equals(""))
				select+=" AND A.FECHA >= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaDesde)+"','YYYY/MM/DD hh24:mi:ss') AND A.FECHA <= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaHasta)+"','YYYY/MM/DD hh24:mi:ss')";
			else if(!fechaDesde.equals(""))
				select+=" AND A.FECHA >= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaDesde)+"','YYYY/MM/DD hh24:mi:ss')";
			else if(!fechaHasta.equals(""))
				select+=" AND A.FECHA <= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaHasta)+"','YYYY/MM/DD hh24:mi:ss')";
			*/
			String fDesdeInc = fechaDesde; 
			   String fHastaInc = fechaHasta;
				if ((fDesdeInc != null && !fDesdeInc.trim().equals("")) || (fHastaInc != null && !fHastaInc.trim().equals(""))) {
					
					if (!fDesdeInc.equals(""))
						fDesdeInc = GstDate.getApplicationFormatDate("", fDesdeInc); 
					if (!fHastaInc.equals(""))
						fHastaInc = GstDate.getApplicationFormatDate("", fHastaInc);
					Vector vCondicion=GstDate.dateBetweenDesdeAndHastaBind("A.FECHA", fDesdeInc, fHastaInc,contador,codigos);
					
					contador=new Integer(vCondicion.get(0).toString()).intValue();
					select +=" AND " + vCondicion.get(1) ;
				}	
			select += " ORDER BY A.IDABONO ";
				    
			vLineasAbonos=(Vector)this.selectTablaBind(select,codigos);
			
			String idAbonoAnt = ""; 
			String idAbono = ""; 
			String asientoClientes = ""; 
			String asientoIngresos = ""; 
			
			
			for(int x=0;x<vLineasAbonos.size();x++)
			{
			    hash = (Hashtable) vLineasAbonos.get(x);
				
			    idAbono = (String)hash.get("IDABONO");
				    
				// PARA CADA LINEA FACTURA COMPRUEBO LA CONFIGURACIÓN DE CUENTAS
			    String confClientes=(String)hash.get("CONFDEUDOR");
			    String confIngresos=(String)hash.get("CONFINGRESOS");
			    String ctaClientes=(String)hash.get("CTACLIENTES");
			    String ctaIngresos=(String)hash.get("CTAINGRESOS");
			 
			        
			    // importes
			    imp=UtilidadesNumero.redondea((String)hash.get("IMPNETO"), 2);
			    importeIva= UtilidadesNumero.redondea((String)hash.get("IMPIVA"), 2);
			    String porcentajeIva= UtilidadesNumero.redondea((String)hash.get("IVA"), 2);
			    
			    // Control de iva 0
			    boolean ivacero=false;
			    try {
			        Double d = new Double(porcentajeIva);
			        if (d.doubleValue()==0.0) 
			            ivacero=true;
			    } catch (NumberFormatException nf) {
			    }

			    // concepto
				concepto = UtilidadesString.sustituirParaExcell(UtilidadesString.getMensajeIdioma(this.usrbean,CONCEPTO_ASIENTO2) + ": "+(String)hash.get("DESCRIPCION"));
				
				// asientos configurables
				String asientoIVA = (String)hash.get("CTAIVA");
				if (confClientes.equals("F")) {
				    asientoClientes =  ctaClientes;
				} else {
				    asientoClientes =  ctaClientes + obtenerAsientoContable(this.usrbean.getLocation(),(String)hash.get("IDPERSONA"));
				}
				if (confIngresos.equals("F")) {
				    asientoIngresos =  ctaIngresos;
				} else if (confIngresos.equals("C")) {
				    asientoIngresos =  ctaIngresos + obtenerAsientoContable(this.usrbean.getLocation(),(String)hash.get("IDPERSONA"));
				} else {
				    asientoIngresos =  ctaIngresos + (String)hash.get("CTAPRODUCTOSERVICIO");
				}

				// aumentamos el contador de asientos
				asiento++;
				
				Hashtable a = new Hashtable();
					
				// Escribimos 1º APUNTE
				a.clear();
				UtilidadesHash.set(a, "FECHA", 			UtilidadesHash.getShortDate(hash, "FECHA"));
				UtilidadesHash.set(a, "CONCEPTO", 		concepto);
				UtilidadesHash.set(a, "DOCUMENTO", 		UtilidadesHash.getString(hash, "NUMEROABONO"));
				UtilidadesHash.set(a, "CUENTA", 		asientoClientes);
				UtilidadesHash.set(a, "DEBE", 			"" + (Double.parseDouble(imp) + Double.parseDouble(importeIva)));
				UtilidadesHash.set(a, "HABER", 			"0");
				UtilidadesHash.set(a, "BASEIMPONIBLE", 	"");
				UtilidadesHash.set(a, "IVA", 			"");
				UtilidadesHash.set(a, "CONTRAPARTIDA", 	asientoIngresos);
				pwcontabilidad.write(this.generarLineaAbono(asiento, a));
				
				// Escribimos 2º APUNTE
				a.clear();
				UtilidadesHash.set(a, "FECHA", 			UtilidadesHash.getShortDate(hash, "FECHA"));
				UtilidadesHash.set(a, "CONCEPTO", 		concepto);
				UtilidadesHash.set(a, "DOCUMENTO", 		UtilidadesHash.getString(hash, "NUMEROABONO"));
				UtilidadesHash.set(a, "CUENTA", 		asientoIngresos);
				UtilidadesHash.set(a, "DEBE", 			"0");
				UtilidadesHash.set(a, "HABER", 			imp);
				UtilidadesHash.set(a, "BASEIMPONIBLE", 	"");
				UtilidadesHash.set(a, "IVA", 			"");
				UtilidadesHash.set(a, "CONTRAPARTIDA", 	asientoClientes);
				pwcontabilidad.write(this.generarLineaAbono(asiento, a));
				
				// Escribimos 3º APUNTE
				if (!ivacero) {
					a.clear();
					UtilidadesHash.set(a, "FECHA", 			UtilidadesHash.getShortDate(hash, "FECHA"));
					UtilidadesHash.set(a, "CONCEPTO", 		concepto);
					UtilidadesHash.set(a, "DOCUMENTO", 		UtilidadesHash.getString(hash, "NUMEROABONO"));
					UtilidadesHash.set(a, "CUENTA", 		asientoIVA);
					UtilidadesHash.set(a, "DEBE", 			"0");
					UtilidadesHash.set(a, "HABER", 			importeIva);
					UtilidadesHash.set(a, "BASEIMPONIBLE", 	imp);
					UtilidadesHash.set(a, "IVA", 			porcentajeIva);
					UtilidadesHash.set(a, "CONTRAPARTIDA", 	asientoClientes);
					pwcontabilidad.write(this.generarLineaAbono(asiento, a));
				}
			
				///////////////////////////////
				
				if (!idAbono.equals(idAbonoAnt)) {
					String[] claves = {"IDINSTITUCION","IDABONO"};
					String[] campos = {"CONTABILIZADA","USUMODIFICACION","FECHAMODIFICACION"};
					laHash = new Hashtable();
					laHash.put("CONTABILIZADA",ClsConstants.FACTURA_ABONO_CONTABILIZADA);
					laHash.put("IDINSTITUCION",this.usrbean.getLocation());
					laHash.put("IDABONO",hash.get("IDABONO"));
					laHash.put("USUMODIFICACION",this.usrbean.getUserName());
					laHash.put("FECHAMODIFICACION","sysdate");
					
					vAsiento2b.add(laHash);
//					abonoAdm.updateDirect(laHash,claves,campos);
					
					idAbonoAnt = idAbono;
				}
			}
//			String[] claves = {"IDINSTITUCION","IDABONO"};
//			String[] campos = {"CONTABILIZADA","USUMODIFICACION","FECHAMODIFICACION"};
//			for(int l=0;l<vAsiento2b.size();l++){
//				abonoAdm.updateDirect((Hashtable)vAsiento2b.get(l),claves,campos);
//			}
			
			if(vAsiento2b.size()>0){
				String contabilizada=(String)((Hashtable)vAsiento2b.get(0)).get("CONTABILIZADA");
				String usuModificacion=(String)((Hashtable)vAsiento2b.get(0)).get("USUMODIFICACION");
				String fechaModificacion=(String)((Hashtable)vAsiento2b.get(0)).get("FECHAMODIFICACION");
				
				String sqlUpdate="UPDATE "+FacAbonoBean.T_NOMBRETABLA+" SET "+FacAbonoBean.C_CONTABILIZADA+"='"+contabilizada+"', "+FacAbonoBean.C_USUMODIFICACION+"="+usuModificacion+", "+FacAbonoBean.C_FECHAMODIFICACION+"="+fechaModificacion;
				int con = 0;
				tx.begin();
				for(int l = 0; l < vAsiento2b.size(); l++){
				    con++;
				    if (con%numeroTransaccion==0) {
				        tx.commit();
				        tx.begin();
				    }
					String claves = (String)((Hashtable)vAsiento2b.get(l)).get("IDABONO");
					Hashtable codigosUpdate = new Hashtable();
					codigosUpdate.put(new Integer(1),this.usrbean.getLocation());
					codigosUpdate.put(new Integer(2),claves);
					abonoAdm.insertSQLBind(sqlUpdate +" WHERE IDINSTITUCION=:1 AND IDABONO=:2",codigosUpdate);
				}
				tx.commit();				
			}
			return pwcontabilidad;
		}
		catch (Exception e) 
		{
			pwcontabilidad.write("Error en asiento2b: " + e.getMessage());
			try { tx.rollback(); } catch (Exception ee) {}
		    throw new ClsExceptions(e,"Error al generar asiento 2b");
		} 
	}
	
	/**
	 * 
	 * @param pwcontabilidad
	 * @param fechaDesde
	 * @param fechaHasta
	 * @param usr
	 * @return
	 * @throws SIGAException
	 */
	// PAGO POR CAJA
	private java.io.PrintWriter generaAsiento3(java.io.PrintWriter pwcontabilidad, String fechaDesde, String fechaHasta, UserTransaction tx) throws SIGAException, ClsExceptions
	{
		
	    // RGG 21/11/2007
	    String concepto 		= "";
	    String conceptoAnticipo 		= "";
		String conceptoT 		= "";
		String asientoContable 	= null;
		// Variables
		Vector vAbono 	= null;
		Hashtable hash 		= null, laHash 	= null;
		String select 		= null, imp = null;
		Vector vAsiento3=new Vector();
		// Beans
		FacPagosPorCajaAdm pagosPorCajaAdm 	= new FacPagosPorCajaAdm(this.usrbean);
		int contador = 0;             
        Hashtable codigos = new Hashtable();
		try
		{

// pagos por caja sobre la factura
			select= "  SELECT '0' AS ANTICIPO, A.IDFACTURA IDFACTURA, B.NUMEROFACTURA, A.TARJETA TARJETA, P.CONFDEUDOR, P.CTACLIENTES, A.IMPORTE IMPORTE, A.TIPOAPUNTE TIPOAPUNTE, " +
		    " DECODE(B.IDPERSONADEUDOR, NULL, B.IDPERSONA, B.IDPERSONADEUDOR) IDPERSONA, " +
		    " A.FECHA FECHA " +
		    " FROM FAC_PAGOSPORCAJA A, FAC_FACTURA B, FAC_FACTURACIONPROGRAMADA P " +
		    " WHERE B.IDINSTITUCION  = A.IDINSTITUCION  " +
		    " AND B.IDFACTURA  = A.IDFACTURA  " +
		    " AND B.IDINSTITUCION = P.IDINSTITUCION " +
		    " AND B.IDSERIEFACTURACION = P.IDSERIEFACTURACION " +
		    " AND B.IDPROGRAMACION = P.IDPROGRAMACION " +
		    " AND B.NUMEROFACTURA IS NOT NULL " ;
			contador++;
			codigos.put(new Integer(contador),this.usrbean.getLocation());
			select+= " AND A.IDINSTITUCION = :" + contador;
			contador++;
			codigos.put(new Integer(contador),"N");
			select+= " AND A.TARJETA = :"+contador;
			
			String fDesdeInc = fechaDesde; 
			String fHastaInc = fechaHasta;
			if ((fDesdeInc != null && !fDesdeInc.trim().equals("")) || (fHastaInc != null && !fHastaInc.trim().equals(""))) {
				
				if (!fDesdeInc.equals(""))
					fDesdeInc = GstDate.getApplicationFormatDate("", fDesdeInc); 
				if (!fHastaInc.equals(""))
					fHastaInc = GstDate.getApplicationFormatDate("", fHastaInc);
				Vector vCondicion=GstDate.dateBetweenDesdeAndHastaBind("FECHA", fDesdeInc, fHastaInc,contador,codigos);
				
				contador=new Integer(vCondicion.get(0).toString()).intValue();
				select +=" AND " + vCondicion.get(1) ;
			}		
			
				
		    /*if(!fechaDesde.equals("") && !fechaHasta.equals(""))
				select+=" AND FECHA >= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaDesde)+"','YYYY/MM/DD hh24:mi:ss') AND FECHA <= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaHasta)+"','YYYY/MM/DD hh24:mi:ss')";
			else if(!fechaDesde.equals(""))
				select+=" AND FECHA >= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaDesde)+"','YYYY/MM/DD hh24:mi:ss')";
			else if(!fechaHasta.equals(""))
				select+=" AND FECHA <= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaHasta)+"','YYYY/MM/DD hh24:mi:ss')";
			*/

		    select += " UNION " +
//ANTICIPADO PRODUCTOS
			" select '1' AS ANTICIPO, B.IDFACTURA IDFACTURA , B.NUMEROFACTURA, 'S' TARJETA, P.CONFDEUDOR, P.CTACLIENTES, L.IMPORTEANTICIPADO IMPORTE, ' ' TIPOAPUNTE, " +
			" DECODE(B.IDPERSONADEUDOR, NULL, B.IDPERSONA, B.IDPERSONADEUDOR) IDPERSONA, " +
			" B.FECHAEMISION " +
			"    from FAC_FACTURA B, FAC_FACTURACIONPROGRAMADA P, FAC_LINEAFACTURA L, PYS_COMPRA C " +
			" where B.IDINSTITUCION = P.IDINSTITUCION " +
			"    AND B.IDSERIEFACTURACION = P.IDSERIEFACTURACION " +
			"    AND B.IDPROGRAMACION = P.IDPROGRAMACION " +
			"    AND B.IDINSTITUCION=L.IDINSTITUCION " +
			"    AND B.IDFACTURA=L.IDFACTURA " +
			"    AND L.IDINSTITUCION=C.IDINSTITUCION " +
			"    AND L.IDFACTURA=C.IDFACTURA " +
			"      AND L.NUMEROLINEA=C.NUMEROLINEA " ;
		    contador++;
			codigos.put(new Integer(contador),String.valueOf(ClsConstants.TIPO_FORMAPAGO_METALICO));
			select +="    AND C.IDFORMAPAGO=:" + contador;  // PAGO POR TIPO_FORMAPAGO_METALICO 
			contador++;
			codigos.put(new Integer(contador),this.usrbean.getLocation());
			select +="    AND B.idinstitucion=:" + contador +
					"     AND B.IMPTOTALANTICIPADO <>0 " +
					//"     AND pkg_siga_totalesfactura.totalanticipado(B.IDINSTITUCION, B.IDFACTURA) <>0 " +
					"     AND B.NUMEROFACTURA IS NOT NULL ";
		     fDesdeInc = fechaDesde; 
			 fHastaInc = fechaHasta;
				if ((fDesdeInc != null && !fDesdeInc.trim().equals("")) || (fHastaInc != null && !fHastaInc.trim().equals(""))) {
					
					if (!fDesdeInc.equals(""))
						fDesdeInc = GstDate.getApplicationFormatDate("", fDesdeInc); 
					if (!fHastaInc.equals(""))
						fHastaInc = GstDate.getApplicationFormatDate("", fHastaInc);
					Vector vCondicion=GstDate.dateBetweenDesdeAndHastaBind("B.FECHAEMISION", fDesdeInc, fHastaInc,contador,codigos);
					
					contador=new Integer(vCondicion.get(0).toString()).intValue();
					select +=" AND " + vCondicion.get(1) ;
			}		
			/*if(!fechaDesde.equals("") && !fechaHasta.equals(""))
				select+=" AND B.FECHAEMISION >= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaDesde)+"','YYYY/MM/DD hh24:mi:ss') AND B.FECHAEMISION <= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaHasta)+"','YYYY/MM/DD hh24:mi:ss')";
			else if(!fechaDesde.equals(""))
				select+=" AND B.FECHAEMISION >= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaDesde)+"','YYYY/MM/DD hh24:mi:ss')";
			else if(!fechaHasta.equals(""))
				select+=" AND B.FECHAEMISION <= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaHasta)+"','YYYY/MM/DD hh24:mi:ss')";
			*/
			select += "  UNION  " +
			
//ANTICIPADO SERVICIOS
			"  select '1' AS ANTICIPO, B.IDFACTURA IDFACTURA, B.NUMEROFACTURA, 'S' TARJETA, P.CONFDEUDOR, P.CTACLIENTES, L.IMPORTEANTICIPADO IMPORTE, ' ' TIPOAPUNTE,   " +
			"         DECODE(B.IDPERSONADEUDOR, NULL, B.IDPERSONA, B.IDPERSONADEUDOR) IDPERSONA, " +
			"         B.FECHAEMISION " +
			"  from FAC_FACTURA B, FAC_FACTURACIONPROGRAMADA P, FAC_LINEAFACTURA L, FAC_FACTURACIONSUSCRIPCION S, PYS_SUSCRIPCION SUS " +
			"  where B.IDINSTITUCION = P.IDINSTITUCION " +
			"    AND B.IDSERIEFACTURACION = P.IDSERIEFACTURACION " +
			"    AND B.IDPROGRAMACION = P.IDPROGRAMACION " +
			"    AND B.IDINSTITUCION=L.IDINSTITUCION " +
			"    AND B.IDFACTURA=L.IDFACTURA " +
			"    AND L.IDINSTITUCION=S.IDINSTITUCION " +
			"    AND L.IDFACTURA=S.IDFACTURA " +
			"    AND L.NUMEROLINEA=S.NUMEROLINEA " +
			"    AND S.IDINSTITUCION=SUS.IDINSTITUCION " +
			"    AND S.IDSUSCRIPCION=SUS.IDSUSCRIPCION " ;
			contador++;
			codigos.put(new Integer(contador),String.valueOf(ClsConstants.TIPO_FORMAPAGO_METALICO));
			select +="    AND SUS.IDFORMAPAGO=:" + contador ;  // PAGO POR TIPO_FORMAPAGO_METALICO 
			contador++;
			codigos.put(new Integer(contador),this.usrbean.getLocation());
			select +="    AND B.idinstitucion=:"+ contador +
						"    AND B.IMPTOTALANTICIPADO <>0 " +
						//"    AND pkg_siga_totalesfactura.totalanticipado(B.IDINSTITUCION, B.IDFACTURA) <>0 " +
			         "    AND B.NUMEROFACTURA IS NOT NULL ";
			
			/*if(!fechaDesde.equals("") && !fechaHasta.equals(""))
				select+=" AND B.FECHAEMISION >= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaDesde)+"','YYYY/MM/DD hh24:mi:ss') AND B.FECHAEMISION <= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaHasta)+"','YYYY/MM/DD hh24:mi:ss')";
			else if(!fechaDesde.equals(""))
				select+=" AND B.FECHAEMISION >= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaDesde)+"','YYYY/MM/DD hh24:mi:ss')";
			else if(!fechaHasta.equals(""))
				select+=" AND B.FECHAEMISION <= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaHasta)+"','YYYY/MM/DD hh24:mi:ss')";
			*/
			 fDesdeInc = fechaDesde; 
			 fHastaInc = fechaHasta;
				if ((fDesdeInc != null && !fDesdeInc.trim().equals("")) || (fHastaInc != null && !fHastaInc.trim().equals(""))) {
					
					if (!fDesdeInc.equals(""))
						fDesdeInc = GstDate.getApplicationFormatDate("", fDesdeInc); 
					if (!fHastaInc.equals(""))
						fHastaInc = GstDate.getApplicationFormatDate("", fHastaInc);
					Vector vCondicion=GstDate.dateBetweenDesdeAndHastaBind("B.FECHAEMISION", fDesdeInc, fHastaInc,contador,codigos);
					
					contador=new Integer(vCondicion.get(0).toString()).intValue();
					select +=" AND " + vCondicion.get(1) ;
			   }		
			select += "    ORDER BY IDFACTURA ";
			
			String idFactura ="";
			String idFacturaAnt ="";
			
			// Descripcion del concepto
			// inc7745 // jbd // Para Badajoz (2010) cambiamos el concepto
			// Esto finalmente se va a hacer por factura.
			/*if(this.usrbean.getLocation().equalsIgnoreCase("2010")){
				concepto = UtilidadesString.sustituirParaExcell(UtilidadesString.getMensajeIdioma(this.usrbean,CONCEPTO_ASIENTO3_2010));
			}else{
				concepto = UtilidadesString.sustituirParaExcell(UtilidadesString.getMensajeIdioma(this.usrbean,CONCEPTO_ASIENTO3));
			}*/
			concepto = UtilidadesString.sustituirParaExcell(UtilidadesString.getMensajeIdioma(this.usrbean,CONCEPTO_ASIENTO3));
			conceptoAnticipo = UtilidadesString.sustituirParaExcell(UtilidadesString.getMensajeIdioma(this.usrbean,CONCEPTO_ASIENTO3_2));
			
			vAbono=(Vector)this.selectTablaBind(select,codigos);
			for(int x=0;x<vAbono.size();x++)
			{
				
				asiento++;
				
				hash = (Hashtable) vAbono.get(x);
				idFactura = (String)hash.get("IDFACTURA");
				
				imp = UtilidadesNumero.redondea((String)hash.get("IMPORTE"),2);
				String confClientes = (String)hash.get("CONFDEUDOR");
				String ctaClientes = (String)hash.get("CTACLIENTES");
				String anticipo = (String)hash.get("ANTICIPO");
				String tipoApunte = (String)hash.get("TIPOAPUNTE");
				
				// Con el IDPERSONA, obtenemos de CEN_CLIENTE, el asiento contable.
				String asientoCliente= obtenerAsientoContable(this.usrbean.getLocation(),(String)hash.get("IDPERSONA"));
				if (confClientes.equals("F")) {
				    asientoContable =  ctaClientes;
				} else {
				    asientoContable =  ctaClientes + asientoCliente;
				}
				String asientoAnticiposCliente = ANTICIPOS_CLI + asientoCliente;
				String asientoCompensacionCliente = CONTABILIDAD_COMPENSACION + asientoCliente;
				
				Hashtable a = new Hashtable();
				
				
				// RGG 24/03/2009 cambio para discriminar pagos por caja de pagos anticipados.
				if (anticipo!=null && anticipo.trim().equals("1")) {
					// PAGO ANTICIPADO
					// Escribimos 1º apunte
					a.clear();
					UtilidadesHash.set(a, "FECHA", 			UtilidadesHash.getShortDate(hash, "FECHA"));
					UtilidadesHash.set(a, "CONCEPTO", 		conceptoAnticipo);
					UtilidadesHash.set(a, "DOCUMENTO", 		UtilidadesHash.getString(hash, "NUMEROFACTURA"));
					UtilidadesHash.set(a, "CUENTA", 		asientoContable);
					UtilidadesHash.set(a, "DEBE", 			"0");
					UtilidadesHash.set(a, "HABER", 			imp);
					UtilidadesHash.set(a, "CONTRAPARTIDA", 	asientoAnticiposCliente);
					pwcontabilidad.write(this.generarLineaAbono(asiento, a));

					// pago anticipado
					// Escribimos 3º apunte
					a.clear();
					UtilidadesHash.set(a, "FECHA", 			UtilidadesHash.getShortDate(hash, "FECHA"));
					UtilidadesHash.set(a, "CONCEPTO", 		conceptoAnticipo);
					UtilidadesHash.set(a, "DOCUMENTO", 		UtilidadesHash.getString(hash, "NUMEROFACTURA"));
					UtilidadesHash.set(a, "CUENTA", 		asientoAnticiposCliente);
					UtilidadesHash.set(a, "DEBE", 			imp);
					UtilidadesHash.set(a, "HABER", 			"0");
					UtilidadesHash.set(a, "CONTRAPARTIDA", 	asientoContable);
					pwcontabilidad.write(this.generarLineaAbono(asiento, a));
				} else
				if (tipoApunte!=null && tipoApunte.trim().equals("C")) {
					// PAGO COMPENSADO
				    // Escribimos 1º apunte
					a.clear();
					UtilidadesHash.set(a, "FECHA", 			UtilidadesHash.getShortDate(hash, "FECHA"));
					UtilidadesHash.set(a, "CONCEPTO", 		concepto);
					UtilidadesHash.set(a, "DOCUMENTO", 		UtilidadesHash.getString(hash, "NUMEROFACTURA"));
					UtilidadesHash.set(a, "CUENTA", 		asientoContable);
					UtilidadesHash.set(a, "DEBE", 			"0");
					UtilidadesHash.set(a, "HABER", 			imp);
					UtilidadesHash.set(a, "CONTRAPARTIDA", 	asientoCompensacionCliente);
					pwcontabilidad.write(this.generarLineaAbono(asiento, a));

				    // pago por caja sobre factura
					// Escribimos 2º apunte
					a.clear();
					UtilidadesHash.set(a, "FECHA", 			UtilidadesHash.getShortDate(hash, "FECHA"));
					UtilidadesHash.set(a, "CONCEPTO", 		concepto);
					UtilidadesHash.set(a, "DOCUMENTO", 		UtilidadesHash.getString(hash, "NUMEROFACTURA"));
					UtilidadesHash.set(a, "CUENTA", 		asientoCompensacionCliente);
					UtilidadesHash.set(a, "DEBE", 			imp);
					UtilidadesHash.set(a, "HABER", 			"0");
					UtilidadesHash.set(a, "CONTRAPARTIDA", 	asientoContable);
					pwcontabilidad.write(this.generarLineaAbono(asiento, a));
				} else {
					// PAGO NORMAL POR CAJA (NO TARJETA)
					// Escribimos 1º apunte
					a.clear();
					UtilidadesHash.set(a, "FECHA", 			UtilidadesHash.getShortDate(hash, "FECHA"));
					UtilidadesHash.set(a, "CONCEPTO", 		concepto);
					UtilidadesHash.set(a, "DOCUMENTO", 		UtilidadesHash.getString(hash, "NUMEROFACTURA"));
					UtilidadesHash.set(a, "CUENTA", 		asientoContable);
					UtilidadesHash.set(a, "DEBE", 			"0");
					UtilidadesHash.set(a, "HABER", 			imp);
					UtilidadesHash.set(a, "CONTRAPARTIDA", 	CONTABILIDAD_CAJA);
					pwcontabilidad.write(this.generarLineaAbono(asiento, a));

				    // pago por caja sobre factura
					// Escribimos 2º apunte
					a.clear();
					UtilidadesHash.set(a, "FECHA", 			UtilidadesHash.getShortDate(hash, "FECHA"));
					UtilidadesHash.set(a, "CONCEPTO", 		concepto);
					UtilidadesHash.set(a, "DOCUMENTO", 		UtilidadesHash.getString(hash, "NUMEROFACTURA"));
					UtilidadesHash.set(a, "CUENTA", 		CONTABILIDAD_CAJA);
					UtilidadesHash.set(a, "DEBE", 			imp);
					UtilidadesHash.set(a, "HABER", 			"0");
					UtilidadesHash.set(a, "CONTRAPARTIDA", 	asientoContable);
					pwcontabilidad.write(this.generarLineaAbono(asiento, a));
				    
				}

				//////////////////////////////////////////////////////
				
				if (!idFactura.equals(idFacturaAnt)) {
				    // Solamente cuando cambia idfactura
//				    String[] claves = {"IDINSTITUCION","IDFACTURA"};
//					String[] campos = {"CONTABILIZADO","USUMODIFICACION","FECHAMODIFICACION"};
					laHash = new Hashtable();
					laHash.put("CONTABILIZADO",ClsConstants.FACTURA_ABONO_CONTABILIZADA);
					laHash.put("IDINSTITUCION",this.usrbean.getLocation());
					laHash.put("IDFACTURA",hash.get("IDFACTURA"));
					laHash.put("USUMODIFICACION",this.usrbean.getUserName());
					laHash.put("FECHAMODIFICACION","sysdate");
					
					vAsiento3.add(laHash);
//					pagosPorCajaAdm.updateDirect(laHash,claves,campos);
				
					idFacturaAnt = idFactura;
				}
			}
//		    String[] claves = {"IDINSTITUCION","IDFACTURA"};
//			String[] campos = {"CONTABILIZADO","USUMODIFICACION","FECHAMODIFICACION"};
//			for(int l=0;l<vAsiento3.size();l++){
//				pagosPorCajaAdm.updateDirect((Hashtable)vAsiento3.get(l),claves,campos);
//			}
			
			if(vAsiento3.size()>0){
				String contabilizada=(String)((Hashtable)vAsiento3.get(0)).get("CONTABILIZADO");
				String usuModificacion=(String)((Hashtable)vAsiento3.get(0)).get("USUMODIFICACION");
				String fechaModificacion=(String)((Hashtable)vAsiento3.get(0)).get("FECHAMODIFICACION");
				
				String sqlUpdate="UPDATE "+FacPagosPorCajaBean.T_NOMBRETABLA+" SET "+FacPagosPorCajaBean.C_CONTABILIZADO+"='"+contabilizada+"', "+FacPagosPorCajaBean.C_USUMODIFICACION+"="+usuModificacion+", "+FacPagosPorCajaBean.C_FECHAMODIFICACION+"="+fechaModificacion;
				int con = 0;
				tx.begin();
				for(int l = 0; l < vAsiento3.size(); l++){
				    con++;
				    if (con%numeroTransaccion==0) {
				        tx.commit();
				        tx.begin();
				    }
					String claves = (String)((Hashtable)vAsiento3.get(l)).get("IDFACTURA");
					Hashtable codigosUpdate = new Hashtable();
					codigosUpdate.put(new Integer(1),this.usrbean.getLocation());
					codigosUpdate.put(new Integer(2),claves);
					pagosPorCajaAdm.insertSQLBind(sqlUpdate +" WHERE IDINSTITUCION=:1 AND IDFACTURA=:2",codigosUpdate);
				}
				tx.commit();				
				
			}
			return pwcontabilidad;
		}
		catch (Exception e) 
		{
			pwcontabilidad.write("Error en asiento3: " + e.getMessage());
			try { tx.rollback(); } catch (Exception ee) {}
		    throw new ClsExceptions(e,"Error al generar asiento 3");
		}
	}
	
	/**
	 * 
	 * @param pwcontabilidad
	 * @param fechaDesde
	 * @param fechaHasta
	 * @param usr
	 * @return
	 * @throws SIGAException
	 */
	// PAGO CON TARJETA
	private java.io.PrintWriter generaAsiento10 (java.io.PrintWriter pwcontabilidad, String fechaDesde, String fechaHasta, UserTransaction tx) throws SIGAException, ClsExceptions
	{

	    String concepto 		= "";
		String conceptoT 		= "";
		String asientoContable 	= null;
		// Variables
		Vector vAbono 	= null;
		Hashtable hash 		= null, laHash 	= null;
		String select 		= null, imp = null;
		Vector vAsiento10=new Vector();
		// Beans
		FacPagosPorCajaAdm pagosPorCajaAdm 	= new FacPagosPorCajaAdm(this.usrbean);
		int contador = 0;             
        Hashtable codigos = new Hashtable();
		try
		{
			select= "  SELECT A.IDFACTURA IDFACTURA, B.NUMEROFACTURA, A.TARJETA TARJETA, P.CONFDEUDOR, P.CTACLIENTES, A.IMPORTE IMPORTE, " +
			    " DECODE(B.IDPERSONADEUDOR, NULL, B.IDPERSONA, B.IDPERSONADEUDOR) IDPERSONA, " +
			    " A.FECHA FECHA " +
			    " FROM FAC_PAGOSPORCAJA A, FAC_FACTURA B, FAC_FACTURACIONPROGRAMADA P " +
			    " WHERE B.IDINSTITUCION  = A.IDINSTITUCION  " +
			    " AND B.IDFACTURA  = A.IDFACTURA  " +
			    " AND B.IDINSTITUCION = P.IDINSTITUCION " +
			    " AND B.IDSERIEFACTURACION = P.IDSERIEFACTURACION " +
			    " AND B.IDPROGRAMACION = P.IDPROGRAMACION " +
			    " AND B.NUMEROFACTURA IS NOT NULL " ;
			contador++;
			codigos.put(new Integer(contador),this.usrbean.getLocation());
			select+=" AND A.IDINSTITUCION = :"+ contador ;
			
			contador++;
			codigos.put(new Integer(contador),"S");
			select+= " AND A.TARJETA = :"+contador;
	
			  /*  if(!fechaDesde.equals("") && !fechaHasta.equals(""))
					select+=" AND FECHA >= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaDesde)+"','YYYY/MM/DD hh24:mi:ss') AND FECHA <= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaHasta)+"','YYYY/MM/DD hh24:mi:ss')";
				else if(!fechaDesde.equals(""))
					select+=" AND FECHA >= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaDesde)+"','YYYY/MM/DD hh24:mi:ss')";
				else if(!fechaHasta.equals(""))
					select+=" AND FECHA <= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaHasta)+"','YYYY/MM/DD hh24:mi:ss')";
			*/	
			String fDesdeInc = fechaDesde; 
			   String fHastaInc = fechaHasta;
				if ((fDesdeInc != null && !fDesdeInc.trim().equals("")) || (fHastaInc != null && !fHastaInc.trim().equals(""))) {
					
					if (!fDesdeInc.equals(""))
						fDesdeInc = GstDate.getApplicationFormatDate("", fDesdeInc); 
					if (!fHastaInc.equals(""))
						fHastaInc = GstDate.getApplicationFormatDate("", fHastaInc);
					Vector vCondicion=GstDate.dateBetweenDesdeAndHastaBind("FECHA", fDesdeInc, fHastaInc,contador,codigos);
					
					contador=new Integer(vCondicion.get(0).toString()).intValue();
					select +=" AND " + vCondicion.get(1) ;
				}	
   
			    select += " UNION " +
// ANTICIPADO PRODUCTOS
				" select B.IDFACTURA IDFACTURA , B.NUMEROFACTURA, 'S' TARJETA, P.CONFDEUDOR, P.CTACLIENTES, L.IMPORTEANTICIPADO IMPORTE,  " +
				" DECODE(B.IDPERSONADEUDOR, NULL, B.IDPERSONA, B.IDPERSONADEUDOR) IDPERSONA, " +
				" B.FECHAEMISION " +
				"    from FAC_FACTURA B, FAC_FACTURACIONPROGRAMADA P, FAC_LINEAFACTURA L, PYS_COMPRA C " +
				" where B.IDINSTITUCION = P.IDINSTITUCION " +
				"    AND B.IDSERIEFACTURACION = P.IDSERIEFACTURACION " +
				"    AND B.IDPROGRAMACION = P.IDPROGRAMACION " +
				"    AND B.IDINSTITUCION=L.IDINSTITUCION " +
				"    AND B.IDFACTURA=L.IDFACTURA " +
				"    AND L.IDINSTITUCION=C.IDINSTITUCION " +
				"    AND L.IDFACTURA=C.IDFACTURA " +
				"      AND L.NUMEROLINEA=C.NUMEROLINEA " ;
			    contador++;
				codigos.put(new Integer(contador),String.valueOf(ClsConstants.TIPO_FORMAPAGO_TARJETA));
				select+="    AND C.IDFORMAPAGO=:"+ contador ;  // PAGO POR TARJETA
				
				contador++;
				codigos.put(new Integer(contador),this.usrbean.getLocation());
				select+="    AND B.idinstitucion=:"+ contador +
				        "    and B.IMPTOTALANTICIPADO <>0 " +
						//"    AND pkg_siga_totalesfactura.totalanticipado(B.IDINSTITUCION, B.IDFACTURA) <>0 " +
				        "    AND B.NUMEROFACTURA IS NOT NULL ";
				/*if(!fechaDesde.equals("") && !fechaHasta.equals(""))
					select+=" AND B.FECHAEMISION >= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaDesde)+"','YYYY/MM/DD hh24:mi:ss') AND B.FECHAEMISION <= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaHasta)+"','YYYY/MM/DD hh24:mi:ss')";
				else if(!fechaDesde.equals(""))
					select+=" AND B.FECHAEMISION >= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaDesde)+"','YYYY/MM/DD hh24:mi:ss')";
				else if(!fechaHasta.equals(""))
					select+=" AND B.FECHAEMISION <= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaHasta)+"','YYYY/MM/DD hh24:mi:ss')";
				*/
				
				String fDesdeInc1 = fechaDesde; 
				   String fHastaInc1 = fechaHasta;
					if ((fDesdeInc1 != null && !fDesdeInc1.trim().equals("")) || (fHastaInc1 != null && !fHastaInc1.trim().equals(""))) {
						
						if (!fDesdeInc1.equals(""))
							fDesdeInc1 = GstDate.getApplicationFormatDate("", fDesdeInc1); 
						if (!fHastaInc1.equals(""))
							fHastaInc1 = GstDate.getApplicationFormatDate("", fHastaInc1);
						Vector vCondicion=GstDate.dateBetweenDesdeAndHastaBind("B.FECHAEMISION", fDesdeInc1, fHastaInc1,contador,codigos);
						
						contador=new Integer(vCondicion.get(0).toString()).intValue();
						select +=" AND " + vCondicion.get(1) ;
					}	
				select += "  UNION   " +
				
// ANTICIPADO SERVICIOS
				"  select B.IDFACTURA IDFACTURA, B.NUMEROFACTURA, 'S' TARJETA, P.CONFDEUDOR, P.CTACLIENTES, L.IMPORTEANTICIPADO IMPORTE,  " +
				"         DECODE(B.IDPERSONADEUDOR, NULL, B.IDPERSONA, B.IDPERSONADEUDOR) IDPERSONA, " +
				"         B.FECHAEMISION " +
				"  from FAC_FACTURA B, FAC_FACTURACIONPROGRAMADA P, FAC_LINEAFACTURA L, FAC_FACTURACIONSUSCRIPCION S, PYS_SUSCRIPCION SUS " +
				"  where B.IDINSTITUCION = P.IDINSTITUCION " +
				"    AND B.IDSERIEFACTURACION = P.IDSERIEFACTURACION " +
				"    AND B.IDPROGRAMACION = P.IDPROGRAMACION " +
				"    AND B.IDINSTITUCION=L.IDINSTITUCION " +
				"    AND B.IDFACTURA=L.IDFACTURA " +
				"    AND L.IDINSTITUCION=S.IDINSTITUCION " +
				"    AND L.IDFACTURA=S.IDFACTURA " +
				"    AND L.NUMEROLINEA=S.NUMEROLINEA " +
				"    AND S.IDINSTITUCION=SUS.IDINSTITUCION " +
				"    AND S.IDSUSCRIPCION=SUS.IDSUSCRIPCION " ;
				 contador++;
					codigos.put(new Integer(contador),String.valueOf(ClsConstants.TIPO_FORMAPAGO_TARJETA));
					select+="    AND SUS.IDFORMAPAGO=:" + contador ;  // PAGO POR TARJETA
				 contador++;
					codigos.put(new Integer(contador),this.usrbean.getLocation());	
					select+="    AND B.idinstitucion=:" + contador +
							"	 AND B.IMPTOTALANTICIPADO <>0 " +
				            //"    AND pkg_siga_totalesfactura.totalanticipado(B.IDINSTITUCION, B.IDFACTURA) <>0 " +
				            "    AND B.NUMEROFACTURA IS NOT NULL ";
				/*if(!fechaDesde.equals("") && !fechaHasta.equals(""))
					select+=" AND B.FECHAEMISION >= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaDesde)+"','YYYY/MM/DD hh24:mi:ss') AND B.FECHAEMISION <= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaHasta)+"','YYYY/MM/DD hh24:mi:ss')";
				else if(!fechaDesde.equals(""))
					select+=" AND B.FECHAEMISION >= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaDesde)+"','YYYY/MM/DD hh24:mi:ss')";
				else if(!fechaHasta.equals(""))
					select+=" AND B.FECHAEMISION <= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaHasta)+"','YYYY/MM/DD hh24:mi:ss')";
				*/
					
					String fDesdeInc2 = fechaDesde; 
					   String fHastaInc2 = fechaHasta;
						if ((fDesdeInc2 != null && !fDesdeInc2.trim().equals("")) || (fHastaInc2 != null && !fHastaInc2.trim().equals(""))) {
							
							if (!fDesdeInc2.equals(""))
								fDesdeInc2 = GstDate.getApplicationFormatDate("", fDesdeInc2); 
							if (!fHastaInc2.equals(""))
								fHastaInc2 = GstDate.getApplicationFormatDate("", fHastaInc2);
							Vector vCondicion=GstDate.dateBetweenDesdeAndHastaBind("B.FECHAEMISION", fDesdeInc2, fHastaInc2,contador,codigos);
							
							contador=new Integer(vCondicion.get(0).toString()).intValue();
							select +=" AND " + vCondicion.get(1) ;
						}		
				select += "    ORDER BY IDFACTURA ";

			vAbono=(Vector)this.selectTablaBind(select,codigos);
			
			String idFactura ="";
			String idFacturaAnt ="";

			// Descripcion del concepto
			conceptoT = UtilidadesString.sustituirParaExcell(UtilidadesString.getMensajeIdioma(this.usrbean,CONCEPTO_ASIENTO10));


			for(int x=0;x<vAbono.size();x++)
			{
			    asiento++;

			    hash = (Hashtable) vAbono.get(x);
				idFactura = (String)hash.get("IDFACTURA");
				
				imp = UtilidadesNumero.redondea((String)hash.get("IMPORTE"),2);
				String confClientes = (String)hash.get("CONFDEUDOR");
				String ctaClientes = (String)hash.get("CTACLIENTES");
				
				// Con el IDPERSONA, obtenemos de CEN_CLIENTE, el asiento contable.
				if (confClientes.equals("F")) {
				    asientoContable =  ctaClientes;
				} else {
				    asientoContable =  ctaClientes + obtenerAsientoContable(this.usrbean.getLocation(),(String)hash.get("IDPERSONA"));
				}
				
				// SE CREA EL ASIENTO
				Hashtable a = new Hashtable();
				
				// Escribimos 1º asiento
				a.clear();
				UtilidadesHash.set(a, "FECHA", 			UtilidadesHash.getShortDate(hash, "FECHA"));
				UtilidadesHash.set(a, "CONCEPTO", 		conceptoT);
				UtilidadesHash.set(a, "DOCUMENTO", 		UtilidadesHash.getString(hash, "NUMEROFACTURA"));
				UtilidadesHash.set(a, "CUENTA", 		asientoContable);
				UtilidadesHash.set(a, "DEBE", 			"0");
				UtilidadesHash.set(a, "HABER", 			imp);
				UtilidadesHash.set(a, "CONTRAPARTIDA", 	CONTABILIDAD_TARJETAS);
				pwcontabilidad.write(this.generarLineaAbono(asiento, a));
				
				// Escribimos 2º asiento
				a.clear();
				UtilidadesHash.set(a, "FECHA", 			UtilidadesHash.getShortDate(hash, "FECHA"));
				UtilidadesHash.set(a, "CONCEPTO", 		conceptoT);
				UtilidadesHash.set(a, "DOCUMENTO", 		UtilidadesHash.getString(hash, "NUMEROFACTURA"));
				UtilidadesHash.set(a, "CUENTA", 		CONTABILIDAD_TARJETAS);
				UtilidadesHash.set(a, "DEBE", 			imp);
				UtilidadesHash.set(a, "HABER", 			"0");
				UtilidadesHash.set(a, "CONTRAPARTIDA", 	asientoContable);
				pwcontabilidad.write(this.generarLineaAbono(asiento, a));
				/////////////////////////////////////////

				if (!idFactura.equals(idFacturaAnt)) {
					// Solamente si cambia
//				    String[] claves = {"IDINSTITUCION","IDFACTURA"};
//					String[] campos = {"CONTABILIZADO","USUMODIFICACION","FECHAMODIFICACION"};
					laHash = new Hashtable();
					laHash.put("CONTABILIZADO",ClsConstants.FACTURA_ABONO_CONTABILIZADA);
					laHash.put("IDINSTITUCION",this.usrbean.getLocation());
					laHash.put("IDFACTURA",hash.get("IDFACTURA"));
					laHash.put("USUMODIFICACION",this.usrbean.getUserName());
					laHash.put("FECHAMODIFICACION","sysdate");
					
					vAsiento10.add(laHash);
//					pagosPorCajaAdm.updateDirect(laHash,claves,campos);
					
					idFacturaAnt = idFactura;
					
				}
			}
//		    String[] claves = {"IDINSTITUCION","IDFACTURA"};
//			String[] campos = {"CONTABILIZADO","USUMODIFICACION","FECHAMODIFICACION"};
//			for(int l=0;l<vAsiento10.size();l++){
//				pagosPorCajaAdm.updateDirect((Hashtable)vAsiento10.get(l),claves,campos);
//			}
			if(vAsiento10.size()>0){
				String contabilizada=(String)((Hashtable)vAsiento10.get(0)).get("CONTABILIZADO");
				String usuModificacion=(String)((Hashtable)vAsiento10.get(0)).get("USUMODIFICACION");
				String fechaModificacion=(String)((Hashtable)vAsiento10.get(0)).get("FECHAMODIFICACION");
				
				

				String sqlUpdate="UPDATE "+FacPagosPorCajaBean.T_NOMBRETABLA+" SET "+FacPagosPorCajaBean.C_CONTABILIZADO+"='"+contabilizada+"', "+FacPagosPorCajaBean.C_USUMODIFICACION+"="+usuModificacion+", "+FacPagosPorCajaBean.C_FECHAMODIFICACION+"="+fechaModificacion;
				int con = 0;
				tx.begin();
				for(int l = 0; l < vAsiento10.size(); l++){
				    con++;
				    if (con%numeroTransaccion==0) {
				        tx.commit();
				        tx.begin();
				    }
					String claves = (String)((Hashtable)vAsiento10.get(l)).get("IDFACTURA");
					Hashtable codigosUpdate = new Hashtable();
					codigosUpdate.put(new Integer(1),this.usrbean.getLocation());
					codigosUpdate.put(new Integer(2),claves);
					pagosPorCajaAdm.insertSQLBind(sqlUpdate +" WHERE IDINSTITUCION=:1 AND IDFACTURA=:2",codigosUpdate);
				}
				tx.commit();
				
			}
			return pwcontabilidad;
		}
		catch (Exception e) 
		{
			pwcontabilidad.write("Error en asiento10: " + e.getMessage());
			try { tx.rollback(); } catch (Exception ee) {}
		    throw new ClsExceptions(e,"Error al generar asiento 10");
		}
	}
	
	/**
	 * 
	 * @param pwcontabilidad
	 * @param fechaDesde
	 * @param fechaHasta
	 * @param usr
	 * @return
	 * @throws SIGAException
	 */
	// PAGO POR BANCO: FACTURA
	private java.io.PrintWriter generaAsiento4(java.io.PrintWriter pwcontabilidad, String fechaDesde, String fechaHasta, UserTransaction tx) throws SIGAException, ClsExceptions
	{
	    // RGG 21/11/2007
	    String concepto 		= "";
		String asientoContable 	= null, asientoContableBanco = null;
		// Variables
		Vector vAbono 	= null;
		Hashtable hash 		= null, laHash 	= null;
		String select 		= null, imp = null;
		Vector vAsiento4=new Vector();
		// Beans
		FacFacturaIncluidaEnDisqueteAdm facturaIncludidaEnDisqueteAdm 	= new FacFacturaIncluidaEnDisqueteAdm(this.usrbean);
		int contador = 0;             
        Hashtable codigos = new Hashtable();
		try
		{
			select= " SELECT A.IDFACTURA IDFACTURA, C.NUMEROFACTURA, A.IMPORTE IMPORTE, B.BANCOS_CODIGO BANCOS_CODIGO,  " + 
			    " DECODE(C.IDPERSONADEUDOR,NULL,C.IDPERSONA,C.IDPERSONADEUDOR) IDPERSONA, B.FECHACREACION FECHACREACION, P.CONFDEUDOR, P.CTACLIENTES " +
			    " FROM FAC_FACTURAINCLUIDAENDISQUETE A, FAC_DISQUETECARGOS B, FAC_FACTURA C, FAC_FACTURACIONPROGRAMADA P " +
			    " WHERE C.IDINSTITUCION = P.IDINSTITUCION " +
			    " AND C.IDSERIEFACTURACION = P.IDSERIEFACTURACION " +
			    " AND C.IDPROGRAMACION = P.IDPROGRAMACION " +
			    " AND A.IDINSTITUCION = B.IDINSTITUCION  " +
			    " AND A.IDDISQUETECARGOS = B.IDDISQUETECARGOS " +
			    " AND A.IDFACTURA = C.IDFACTURA  " +
			    " AND A.IDINSTITUCION = C.IDINSTITUCION " +
			    " AND C.NUMEROFACTURA IS NOT NULL " ;
			contador++;
			codigos.put(new Integer(contador),this.usrbean.getLocation());
			select+= " AND A.IDINSTITUCION =:"+contador;
			String fDesdeInc = fechaDesde; 
			   String fHastaInc = fechaHasta;
				if ((fDesdeInc != null && !fDesdeInc.trim().equals("")) || (fHastaInc != null && !fHastaInc.trim().equals(""))) {
					
					if (!fDesdeInc.equals(""))
						fDesdeInc = GstDate.getApplicationFormatDate("", fDesdeInc); 
					if (!fHastaInc.equals(""))
						fHastaInc = GstDate.getApplicationFormatDate("", fHastaInc);
					Vector vCondicion=GstDate.dateBetweenDesdeAndHastaBind("B.FECHACREACION", fDesdeInc, fHastaInc,contador,codigos);
					
					contador=new Integer(vCondicion.get(0).toString()).intValue();
					select +=" AND " + vCondicion.get(1) ;
				}	
			
			/*if(!fechaDesde.equals("") && !fechaHasta.equals(""))
				select+=" AND B.FECHACREACION >= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaDesde)+"','YYYY/MM/DD hh24:mi:ss') AND B.FECHACREACION <= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaHasta)+"','YYYY/MM/DD hh24:mi:ss')";
			else if(!fechaDesde.equals(""))
				select+=" AND B.FECHACREACION >= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaDesde)+"','YYYY/MM/DD hh24:mi:ss')";
			else if(!fechaHasta.equals(""))
				select+=" AND B.FECHACREACION <= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaHasta)+"','YYYY/MM/DD hh24:mi:ss')";
			*/
			select += " ORDER BY A.IDFACTURA ";
			
			vAbono=(Vector)this.selectTablaBind(select,codigos);

			String idFactura ="";
			String idFacturaAnt ="";

			// Descripcion del concepto
			concepto = UtilidadesString.sustituirParaExcell(UtilidadesString.getMensajeIdioma(this.usrbean,CONCEPTO_ASIENTO4));
			
			for(int x=0;x<vAbono.size();x++)
			{
				hash = (Hashtable) vAbono.get(x);

				asiento++;
				
				idFactura = (String)hash.get("IDFACTURA");
				
				imp = UtilidadesNumero.redondea((String)hash.get("IMPORTE"),2);
				
				String confClientes = (String)hash.get("CONFDEUDOR");
				String ctaClientes = (String)hash.get("CTACLIENTES");
				
				// Con el IDPERSONA, obtenemos de CEN_CLIENTE, el asiento contable.
				if (confClientes.equals("F")) {
				    asientoContable =  ctaClientes;
				} else {
				    asientoContable =  ctaClientes + obtenerAsientoContable(this.usrbean.getLocation(),(String)hash.get("IDPERSONA"));
				}
				
				// Con el BANCOS_CODIGO, obtenemos de FAC_BANCOINSTITUCION, el numerocuenta.
				asientoContableBanco	= obtenerAsientoContableBanco(this.usrbean.getLocation(),(String)hash.get("BANCOS_CODIGO"));  
				
				
				Hashtable a = new Hashtable();
				
				// Escribimos 1º asiento
				UtilidadesHash.set(a, "FECHA", 			UtilidadesHash.getShortDate(hash, "FECHACREACION"));
				UtilidadesHash.set(a, "CONCEPTO", 		concepto);
				UtilidadesHash.set(a, "DOCUMENTO", 		UtilidadesHash.getString(hash, "NUMEROFACTURA"));
				UtilidadesHash.set(a, "CUENTA", 		asientoContable);
				UtilidadesHash.set(a, "DEBE", 			"0");
				UtilidadesHash.set(a, "HABER", 			imp);
				UtilidadesHash.set(a, "CONTRAPARTIDA", 	asientoContableBanco);
				pwcontabilidad.write(this.generarLineaAbono(asiento, a));
				
				// Escribimos 2º asiento
				a.clear();
				UtilidadesHash.set(a, "FECHA", 			UtilidadesHash.getShortDate(hash, "FECHACREACION"));
				UtilidadesHash.set(a, "CONCEPTO", 		concepto);
				UtilidadesHash.set(a, "DOCUMENTO", 		UtilidadesHash.getString(hash, "NUMEROFACTURA"));
				UtilidadesHash.set(a, "CUENTA", 		asientoContableBanco);
				UtilidadesHash.set(a, "DEBE", 			imp);
				UtilidadesHash.set(a, "HABER", 			"0");
				UtilidadesHash.set(a, "CONTRAPARTIDA", 	asientoContable);
				pwcontabilidad.write(this.generarLineaAbono(asiento, a));
			
				/////////////////////////////////////////
				
				if (!idFactura.equals(idFacturaAnt)) {
//					String[] claves = {"IDINSTITUCION","IDFACTURA"};
//					String[] campos = {"CONTABILIZADA","USUMODIFICACION","FECHAMODIFICACION"};
					laHash = new Hashtable();
					laHash.put("CONTABILIZADA",ClsConstants.FACTURA_ABONO_CONTABILIZADA);
					laHash.put("IDINSTITUCION",this.usrbean.getLocation());
					laHash.put("IDFACTURA",hash.get("IDFACTURA"));
					laHash.put("USUMODIFICACION",this.usrbean.getUserName());
					laHash.put("FECHAMODIFICACION","sysdate");
					
					vAsiento4.add(laHash);
//					facturaIncludidaEnDisqueteAdm.updateDirect(laHash,claves,campos);
					
					idFacturaAnt = idFactura;
				}
			}
//			String[] claves = {"IDINSTITUCION","IDFACTURA"};
//			String[] campos = {"CONTABILIZADA","USUMODIFICACION","FECHAMODIFICACION"};
//			for(int l=0;l<vAsiento4.size();l++){
//				facturaIncludidaEnDisqueteAdm.updateDirect((Hashtable)vAsiento4.get(l),claves,campos);
//			}
			
			if(vAsiento4.size()>0){
				String contabilizada=(String)((Hashtable)vAsiento4.get(0)).get("CONTABILIZADA");
				String usuModificacion=(String)((Hashtable)vAsiento4.get(0)).get("USUMODIFICACION");
				String fechaModificacion=(String)((Hashtable)vAsiento4.get(0)).get("FECHAMODIFICACION");


				String sqlUpdate="UPDATE "+FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA+" SET "+FacFacturaIncluidaEnDisqueteBean.C_CONTABILIZADA+"='"+contabilizada+"', "+FacFacturaIncluidaEnDisqueteBean.C_USUMODIFICACION+"="+usuModificacion+", "+FacFacturaIncluidaEnDisqueteBean.C_FECHAMODIFICACION+"="+fechaModificacion;
				int con = 0;
				tx.begin();
				for(int l = 0; l < vAsiento4.size(); l++){
				    con++;
				    if (con%numeroTransaccion==0) {
				        tx.commit();
				        tx.begin();
				    }
					String claves = (String)((Hashtable)vAsiento4.get(l)).get("IDFACTURA");
					Hashtable codigosUpdate = new Hashtable();
					codigosUpdate.put(new Integer(1),this.usrbean.getLocation());
					codigosUpdate.put(new Integer(2),claves);
					facturaIncludidaEnDisqueteAdm.insertSQLBind(sqlUpdate +" WHERE IDINSTITUCION=:1 AND IDFACTURA=:2",codigosUpdate);
				}
				tx.commit();
				
			}
			
			return pwcontabilidad;
		}
		catch (Exception e) 
		{
			pwcontabilidad.write("Error en asiento4: " + e.getMessage());
			try { tx.rollback(); } catch (Exception ee) {}
		    throw new ClsExceptions(e,"Error al generar asiento 4");
		}
	}

	
	/**
	 * 
	 * @param pwcontabilidad
	 * @param fechaDesde
	 * @param fechaHasta
	 * @param usr
	 * @return
	 * @throws SIGAException
	 */
	// DEVOLUCION POR BANCO: FACTURA
	private java.io.PrintWriter generaAsiento5(java.io.PrintWriter pwcontabilidad, String fechaDesde, String fechaHasta, UserTransaction tx) throws SIGAException, ClsExceptions
	{

	    // RGG 21/11/2007
		String concepto 		= "";
		String asientoContable 	= null, asientoContableBanco = null, gastosDevolucion = null, cargarCliente = null;
		// Variables
		Vector vAbono 	= null;
		Hashtable hash 		= null, laHash 	= null;
		String select 		= null, imp = null;
		Vector vAsiento5=new Vector();
		// Beans
		FacLineaDevoluDisqBancoAdm lineaDevoludisqbancoAdm = new FacLineaDevoluDisqBancoAdm(this.usrbean);
		int contador = 0;             
        Hashtable codigos = new Hashtable();
		try
		{
			select= " SELECT A.IDFACTURA IDFACTURA, DECODE(D.IDPERSONADEUDOR,NULL,D.IDPERSONA,D.IDPERSONADEUDOR) IDPERSONA, A.IMPORTE IMPORTE, B.IDDISQUETEDEVOLUCIONES IDDISQUETEDEVOLUCIONES, "+ 
			    " B.GASTOSDEVOLUCION GASTOSDEVOLUCION, B.CARGARCLIENTE CARGARCLIENTE, C.BANCOS_CODIGO BANCOS_CODIGO,  "+
			    " D.IDPERSONA IDPERSONA, C.FECHAGENERACION FECHAGENERACION, D.NUMEROFACTURA, P.CONFDEUDOR, P.CTACLIENTES "+
			    " FROM FAC_FACTURAINCLUIDAENDISQUETE A, FAC_LINEADEVOLUDISQBANCO B, FAC_DISQUETEDEVOLUCIONES C, FAC_FACTURA D, FAC_FACTURACIONPROGRAMADA P "+
			    " WHERE D.IDINSTITUCION = P.IDINSTITUCION "+
			    " AND   D.IDSERIEFACTURACION = P.IDSERIEFACTURACION "+
			    " AND   D.IDPROGRAMACION = P.IDPROGRAMACION "+
			    " AND A.IDINSTITUCION = B.IDINSTITUCION  "+
			    " AND A.IDDISQUETECARGOS = B.IDDISQUETECARGOS "+ 
			    " AND A.IDFACTURAINCLUIDAENDISQUETE = B.IDFACTURAINCLUIDAENDISQUETE "+ 
			    " AND A.IDINSTITUCION = C.IDINSTITUCION  "+
			    " AND B.IDDISQUETEDEVOLUCIONES = C.IDDISQUETEDEVOLUCIONES "+ 
			    " AND A.IDFACTURA = D.IDFACTURA  "+
			    " AND A.IDINSTITUCION = D.IDINSTITUCION "+
			    " AND D.NUMEROFACTURA IS NOT NULL   ";
			
	        contador++;
			codigos.put(new Integer(contador),this.usrbean.getLocation());
			select+= " AND A.IDINSTITUCION =:"+contador;

			/*if(!fechaDesde.equals("") && !fechaHasta.equals(""))
				select+=" AND C.FECHAGENERACION >= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaDesde)+"','YYYY/MM/DD hh24:mi:ss') AND C.FECHAGENERACION <= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaHasta)+"','YYYY/MM/DD hh24:mi:ss')";
			else if(!fechaDesde.equals(""))
				select+=" AND C.FECHAGENERACION >= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaDesde)+"','YYYY/MM/DD hh24:mi:ss')";
			else if(!fechaHasta.equals(""))
				select+=" AND C.FECHAGENERACION <= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaHasta)+"','YYYY/MM/DD hh24:mi:ss')";
			*/
			String fDesdeInc = fechaDesde; 
			String fHastaInc = fechaHasta;
				if ((fDesdeInc != null && !fDesdeInc.trim().equals("")) || (fHastaInc != null && !fHastaInc.trim().equals(""))) {
					
					if (!fDesdeInc.equals(""))
						fDesdeInc = GstDate.getApplicationFormatDate("", fDesdeInc); 
					if (!fHastaInc.equals(""))
						fHastaInc = GstDate.getApplicationFormatDate("", fHastaInc);
					Vector vCondicion=GstDate.dateBetweenDesdeAndHastaBind("C.FECHAGENERACION", fDesdeInc, fHastaInc,contador,codigos);
					
					contador=new Integer(vCondicion.get(0).toString()).intValue();
					select +=" AND " + vCondicion.get(1) ;
				}	
			select += " ORDER BY C.IDDISQUETEDEVOLUCIONES ";
			
			vAbono=(Vector)this.selectTablaBind(select,codigos);
			
			String idDisquete ="";
			String idDisqueteAnt ="";

			// Descripcion del concepto
			concepto = UtilidadesString.sustituirParaExcell(UtilidadesString.getMensajeIdioma(this.usrbean,CONCEPTO_ASIENTO5));
			
			for(int x=0;x<vAbono.size();x++)
			{
				asiento++;

				hash = (Hashtable) vAbono.get(x);

				idDisquete = (String)hash.get("IDDISQUETEDEVOLUCIONES");
				
				imp = UtilidadesNumero.redondea((String)hash.get("IMPORTE"),2);
				
				String confClientes = (String)hash.get("CONFDEUDOR");
				String ctaClientes = (String)hash.get("CTACLIENTES");
				
				// Con el IDPERSONA, obtenemos de CEN_CLIENTE, el asiento contable.
				if (confClientes.equals("F")) {
				    asientoContable =  ctaClientes;
				} else {
				    asientoContable =  ctaClientes + obtenerAsientoContable(this.usrbean.getLocation(),(String)hash.get("IDPERSONA"));
				}
				

				gastosDevolucion = (String) hash.get("GASTOSDEVOLUCION");
				cargarCliente = (String) hash.get("CARGARCLIENTE");

				// Con el BANCOS_CODIGO, obtenemos de FAC_BANCOINSTITUCION, el numerocuenta.
				asientoContableBanco	= obtenerAsientoContableBanco(this.usrbean.getLocation(),(String)hash.get("BANCOS_CODIGO"));  

				///////////////////////////////////////////////////////
				Hashtable a = new Hashtable();
				
				// Escribimos 1º asiento
				UtilidadesHash.set(a, "FECHA", 			UtilidadesHash.getShortDate(hash, "FECHAGENERACION"));
				UtilidadesHash.set(a, "CONCEPTO", 		concepto);
				UtilidadesHash.set(a, "DOCUMENTO", 		UtilidadesHash.getString(hash, "NUMEROFACTURA"));
				UtilidadesHash.set(a, "CUENTA", 		asientoContable);
				UtilidadesHash.set(a, "DEBE", 			imp);
				UtilidadesHash.set(a, "HABER", 			"0");
				UtilidadesHash.set(a, "BASEIMPONIBLE", 	"");
				UtilidadesHash.set(a, "IVA", 			"");
				UtilidadesHash.set(a, "CONTRAPARTIDA", 	asientoContableBanco);
				pwcontabilidad.write(this.generarLineaAbono(asiento, a));
				
				// Escribimos 2º asiento
				a.clear();
				UtilidadesHash.set(a, "FECHA", 			UtilidadesHash.getShortDate(hash, "FECHAGENERACION"));
				UtilidadesHash.set(a, "CONCEPTO", 		concepto);
				UtilidadesHash.set(a, "DOCUMENTO", 		UtilidadesHash.getString(hash, "NUMEROFACTURA"));
				UtilidadesHash.set(a, "CUENTA", 		asientoContableBanco);
				UtilidadesHash.set(a, "DEBE", 			"0");
				UtilidadesHash.set(a, "HABER", 			imp);
				UtilidadesHash.set(a, "BASEIMPONIBLE", 	"");
				UtilidadesHash.set(a, "IVA", 			"");
				UtilidadesHash.set(a, "CONTRAPARTIDA", 	asientoContable);
				pwcontabilidad.write(this.generarLineaAbono(asiento, a));
				
				
				if (gastosDevolucion!=null && !gastosDevolucion.trim().equals("")) {
					// tiene gastos
					
					// Los paga la insitucion SIEMPRE
				    gastosDevolucion = UtilidadesNumero.redondea(gastosDevolucion,2);
				    
					// Escribimos 3º asiento
					a.clear();
					UtilidadesHash.set(a, "FECHA", 			UtilidadesHash.getShortDate(hash, "FECHAGENERACION"));
					UtilidadesHash.set(a, "CONCEPTO", 		concepto);
					UtilidadesHash.set(a, "DOCUMENTO", 		UtilidadesHash.getString(hash, "NUMEROFACTURA"));
					UtilidadesHash.set(a, "CUENTA", 		CONTABILIDAD_GASTOSBANCARIOS);
					UtilidadesHash.set(a, "DEBE", 			gastosDevolucion);
					UtilidadesHash.set(a, "HABER", 			"0");
					UtilidadesHash.set(a, "BASEIMPONIBLE", 	"");
					UtilidadesHash.set(a, "IVA", 			"");
					UtilidadesHash.set(a, "CONTRAPARTIDA", 	asientoContableBanco);
					pwcontabilidad.write(this.generarLineaAbono(asiento, a));

					// Escribimos 4º asiento
					a.clear();
					UtilidadesHash.set(a, "FECHA", 			UtilidadesHash.getShortDate(hash, "FECHAGENERACION"));
					UtilidadesHash.set(a, "CONCEPTO", 		concepto);
					UtilidadesHash.set(a, "DOCUMENTO", 		UtilidadesHash.getString(hash, "NUMEROFACTURA"));
					UtilidadesHash.set(a, "CUENTA", 		asientoContableBanco);
					UtilidadesHash.set(a, "DEBE", 			"0");
					UtilidadesHash.set(a, "HABER", 			gastosDevolucion);
					UtilidadesHash.set(a, "BASEIMPONIBLE", 	"");
					UtilidadesHash.set(a, "IVA", 			"");
					UtilidadesHash.set(a, "CONTRAPARTIDA", 	CONTABILIDAD_GASTOSBANCARIOS);
					pwcontabilidad.write(this.generarLineaAbono(asiento, a));
					
				}
				
				///////////////////////////////////////////////////////
//				
				if (!idDisqueteAnt.equals(idDisquete)) {
//					String[] claves = {"IDINSTITUCION","IDDISQUETEDEVOLUCIONES"};
//					String[] campos = {"CONTABILIZADA","USUMODIFICACION","FECHAMODIFICACION"};
					laHash = new Hashtable();
					laHash.put("CONTABILIZADA",ClsConstants.FACTURA_ABONO_CONTABILIZADA);
					laHash.put("IDINSTITUCION",this.usrbean.getLocation());
					laHash.put("IDDISQUETEDEVOLUCIONES",hash.get("IDDISQUETEDEVOLUCIONES"));
					laHash.put("USUMODIFICACION",this.usrbean.getUserName());
					laHash.put("FECHAMODIFICACION","sysdate");
					
					vAsiento5.add(laHash);
//					lineaDevoludisqbancoAdm.updateDirect(laHash,claves,campos);
					idDisqueteAnt = idDisquete;
				}
			}
//			String[] claves = {"IDINSTITUCION","IDDISQUETEDEVOLUCIONES"};
//			String[] campos = {"CONTABILIZADA","USUMODIFICACION","FECHAMODIFICACION"};
//			for(int l=0;l<vAsiento5.size();l++){
//				lineaDevoludisqbancoAdm.updateDirect((Hashtable)vAsiento5.get(l),claves,campos);
//			}
			
			if(vAsiento5.size()>0){
				String contabilizada=(String)((Hashtable)vAsiento5.get(0)).get("CONTABILIZADA");
				String usuModificacion=(String)((Hashtable)vAsiento5.get(0)).get("USUMODIFICACION");
				String fechaModificacion=(String)((Hashtable)vAsiento5.get(0)).get("FECHAMODIFICACION");
				

				String sqlUpdate="UPDATE "+FacLineaDevoluDisqBancoBean.T_NOMBRETABLA+" SET "+FacLineaDevoluDisqBancoBean.C_CONTABILIZADA+"='"+contabilizada+"', "+FacLineaDevoluDisqBancoBean.C_USUMODIFICACION+"="+usuModificacion+", "+FacLineaDevoluDisqBancoBean.C_FECHAMODIFICACION+"="+fechaModificacion;
				int con = 0;
				tx.begin();
				for(int l = 0; l < vAsiento5.size(); l++){
				    con++;
				    if (con%numeroTransaccion==0) {
				        tx.commit();
				        tx.begin();
				    }
					String claves = (String)((Hashtable)vAsiento5.get(l)).get("IDDISQUETEDEVOLUCIONES");
					Hashtable codigosUpdate = new Hashtable();
					codigosUpdate.put(new Integer(1),this.usrbean.getLocation());
					codigosUpdate.put(new Integer(2),claves);
					lineaDevoludisqbancoAdm.insertSQLBind(sqlUpdate +" WHERE IDINSTITUCION=:1 AND IDDISQUETEDEVOLUCIONES=:2",codigosUpdate);
				}
				tx.commit();
				
			}
			
			return pwcontabilidad;
		}
		catch (Exception e) 
		{
			pwcontabilidad.write("Error en asiento5: " + e.getMessage());
			try { tx.rollback(); } catch (Exception ee) {}
		    throw new ClsExceptions(e,"Error al generar asiento 5");
		}
	}

	/**
	 * 
	 * @param pwcontabilidad
	 * @param fechaDesde
	 * @param fechaHasta
	 * @param usr
	 * @return
	 * @throws SIGAException
	 */
	// ALTA DE ANTICIPOS
	private java.io.PrintWriter generaAsiento14(java.io.PrintWriter pwcontabilidad, String fechaDesde, String fechaHasta, UserTransaction tx) throws SIGAException, ClsExceptions
	{
	    String concepto 		= "";
		String asientoContable 	= null, asientoContableCajaAnticipos = null;
		// Variables
		Vector vAnticipo 	= null;
		Hashtable hash 		= null, laHash 	= null;
		String select 		= null, imp = null;
		Vector vAsiento14=new Vector();
		// Beans
		PysAnticipoLetradoAdm anticipoLetradoAdm 	= new PysAnticipoLetradoAdm(this.usrbean);
		int contador = 0;             
        Hashtable codigos = new Hashtable();
		try
		{
			select= " SELECT IDANTICIPO, DESCRIPCION, IMPORTEINICIAL, IDPERSONA, FECHA, CTACONTABLE FROM PYS_ANTICIPOLETRADO " ;
			contador++;
			codigos.put(new Integer(contador),this.usrbean.getLocation());
			select+= " WHERE IDINSTITUCION =:"+contador;
			String fDesdeInc = fechaDesde; 
			   String fHastaInc = fechaHasta;
				if ((fDesdeInc != null && !fDesdeInc.trim().equals("")) || (fHastaInc != null && !fHastaInc.trim().equals(""))) {
					
					if (!fDesdeInc.equals(""))
						fDesdeInc = GstDate.getApplicationFormatDate("", fDesdeInc); 
					if (!fHastaInc.equals(""))
						fHastaInc = GstDate.getApplicationFormatDate("", fHastaInc);
					Vector vCondicion=GstDate.dateBetweenDesdeAndHastaBind("FECHA", fDesdeInc, fHastaInc,contador,codigos);
					
					contador=new Integer(vCondicion.get(0).toString()).intValue();
					select +=" AND " + vCondicion.get(1) ;
				}	

			select += " ORDER BY IDANTICIPO ";
			
			vAnticipo=(Vector)this.selectTablaBind(select,codigos);

			String idAnticipo ="";
			String idAnticipoAnt ="";

			for(int x=0;x<vAnticipo.size();x++)
			{
				hash = (Hashtable) vAnticipo.get(x);

				asiento++;
				
				// Descripcion del concepto
				concepto = UtilidadesString.sustituirParaExcell(UtilidadesString.getMensajeIdioma(this.usrbean,CONCEPTO_ASIENTO14) + " " + (String)hash.get("DESCRIPCION"));
				
				idAnticipo = (String)hash.get("IDANTICIPO");
				
				imp = UtilidadesNumero.redondea((String)hash.get("IMPORTEINICIAL"),2);
				
				String ctaClientes = (String)hash.get("CTACONTABLE");
				
				// Con el IDPERSONA, obtenemos de CEN_CLIENTE, el asiento contable.
			    asientoContable =  ctaClientes + obtenerAsientoContable(this.usrbean.getLocation(),(String)hash.get("IDPERSONA"));
				
				// Con el BANCOS_CODIGO, obtenemos de FAC_BANCOINSTITUCION, el numerocuenta.
			    asientoContableCajaAnticipos	= CONTABILIDAD_CAJA_ANTICIPOS;
				
				
				Hashtable a = new Hashtable();
				
				// Escribimos 1º asiento
				UtilidadesHash.set(a, "FECHA", 			UtilidadesHash.getShortDate(hash, "FECHA"));
				UtilidadesHash.set(a, "CONCEPTO", 		concepto);
				UtilidadesHash.set(a, "DOCUMENTO", 		UtilidadesHash.getString(hash, "IDANTICIPO"));
				UtilidadesHash.set(a, "CUENTA", 		asientoContable);
				UtilidadesHash.set(a, "DEBE", 			imp);
				UtilidadesHash.set(a, "HABER", 			"0");
				UtilidadesHash.set(a, "CONTRAPARTIDA", 	asientoContableCajaAnticipos);
				pwcontabilidad.write(this.generarLineaAbono(asiento, a));
				
				// Escribimos 2º asiento
				a.clear();
				UtilidadesHash.set(a, "FECHA", 			UtilidadesHash.getShortDate(hash, "FECHA"));
				UtilidadesHash.set(a, "CONCEPTO", 		concepto);
				UtilidadesHash.set(a, "DOCUMENTO", 		UtilidadesHash.getString(hash, "IDANTICIPO"));
				UtilidadesHash.set(a, "CUENTA", 		asientoContableCajaAnticipos);
				UtilidadesHash.set(a, "DEBE", 			"0");
				UtilidadesHash.set(a, "HABER", 			imp);
				UtilidadesHash.set(a, "CONTRAPARTIDA", 	asientoContable);
				pwcontabilidad.write(this.generarLineaAbono(asiento, a));
	
				/////////////////////////////////////////
				
				if (!idAnticipo.equals(idAnticipoAnt)) {
					laHash = new Hashtable();
					laHash.put("CONTABILIZADA",ClsConstants.FACTURA_ABONO_CONTABILIZADA);
					laHash.put("IDINSTITUCION",this.usrbean.getLocation());
					laHash.put("IDPERSONA",hash.get("IDPERSONA"));
					laHash.put("IDANTICIPO",hash.get("IDANTICIPO"));
					laHash.put("USUMODIFICACION",this.usrbean.getUserName());
					laHash.put("FECHAMODIFICACION","sysdate");
					
					vAsiento14.add(laHash);
					idAnticipoAnt = idAnticipo;
				}
			}
			
			if(vAsiento14.size()>0){
				String contabilizada=(String)((Hashtable)vAsiento14.get(0)).get("CONTABILIZADA");
				String usuModificacion=(String)((Hashtable)vAsiento14.get(0)).get("USUMODIFICACION");
				String fechaModificacion=(String)((Hashtable)vAsiento14.get(0)).get("FECHAMODIFICACION");
				
				String sqlUpdate="UPDATE "+PysAnticipoLetradoBean.T_NOMBRETABLA+" SET "+PysAnticipoLetradoBean.C_CONTABILIZADO+"='"+contabilizada+"', "+FacFacturaIncluidaEnDisqueteBean.C_USUMODIFICACION+"="+usuModificacion+", "+FacFacturaIncluidaEnDisqueteBean.C_FECHAMODIFICACION+"="+fechaModificacion;
				int con = 0;
				tx.begin();
				for(int l = 0; l < vAsiento14.size(); l++){
				    con++;
				    if (con%numeroTransaccion==0) {
				        tx.commit();
				        tx.begin();
				    }
					String claves = (String)((Hashtable)vAsiento14.get(l)).get("IDPERSONA");
					String claves2 = (String)((Hashtable)vAsiento14.get(l)).get("IDANTICIPO");
					Hashtable codigosUpdate = new Hashtable();
					codigosUpdate.put(new Integer(1),this.usrbean.getLocation());
					codigosUpdate.put(new Integer(2),claves);
					codigosUpdate.put(new Integer(3),claves2);
					anticipoLetradoAdm.insertSQLBind(sqlUpdate +" WHERE IDINSTITUCION=:1 AND IDPERSONA=:2 AND IDANTICIPO=:3",codigosUpdate);
				}
				tx.commit();
				
			}
			
			return pwcontabilidad;
		}
		catch (Exception e) 
		{
			pwcontabilidad.write("Error en asiento14: " + e.getMessage());
			try { tx.rollback(); } catch (Exception ee) {}
		    throw new ClsExceptions(e,"Error al generar asiento 14");
		}
	}

	/**
	 * 
	 * @param pwcontabilidad
	 * @param fechaDesde
	 * @param fechaHasta
	 * @param usr
	 * @return
	 * @throws SIGAException
	 */
	// LIQUIDACIÓ DE ANTICIPOS PARA EL COLEGIO
	private java.io.PrintWriter generaAsiento15(java.io.PrintWriter pwcontabilidad, String fechaDesde, String fechaHasta, UserTransaction tx) throws SIGAException, ClsExceptions
	{
	    String concepto 		= "";
		String asientoContableIngresosExtra = null;
		// Variables
		Vector vAnticipo 	= null;
		Hashtable hash 		= null, laHash 	= null;
		String select 		= null, imp = null;

		// Beans
		int contador = 0;             
        Hashtable codigos = new Hashtable();
		try
		{
			select= " SELECT IDANTICIPO, IMPORTEANTICIPADO, IDPERSONA, FECHAEFECTIVA  FROM PYS_LINEAANTICIPO WHERE LIQUIDACION='1' " ;
			contador++;
			codigos.put(new Integer(contador),this.usrbean.getLocation());
			select+= " AND IDINSTITUCION =:"+contador;
			String fDesdeInc = fechaDesde; 
			   String fHastaInc = fechaHasta;
				if ((fDesdeInc != null && !fDesdeInc.trim().equals("")) || (fHastaInc != null && !fHastaInc.trim().equals(""))) {
					
					if (!fDesdeInc.equals(""))
						fDesdeInc = GstDate.getApplicationFormatDate("", fDesdeInc); 
					if (!fHastaInc.equals(""))
						fHastaInc = GstDate.getApplicationFormatDate("", fHastaInc);
					Vector vCondicion=GstDate.dateBetweenDesdeAndHastaBind("FECHAEFECTIVA", fDesdeInc, fHastaInc,contador,codigos);
					
					contador=new Integer(vCondicion.get(0).toString()).intValue();
					select +=" AND " + vCondicion.get(1) ;
				}	

			select += " ORDER BY IDANTICIPO ";
			
			vAnticipo=(Vector)this.selectTablaBind(select,codigos);

			String idAnticipo ="";
			String idAnticipoAnt ="";

			// Descripcion del concepto
			concepto = UtilidadesString.sustituirParaExcell(UtilidadesString.getMensajeIdioma(this.usrbean,CONCEPTO_ASIENTO15));
			int con=0;
			tx.begin();
			for(int x=0;x<vAnticipo.size();x++)
			{
			    con++;
			    if (con%numeroTransaccion==0) {
			        tx.commit();
			        tx.begin(); 
			    }

				hash = (Hashtable) vAnticipo.get(x);

				asiento++;
				
				idAnticipo = (String)hash.get("IDANTICIPO");
				
				imp = UtilidadesNumero.redondea((String)hash.get("IMPORTEANTICIPADO"),2);
				
				String asientoCliente= obtenerAsientoContable(this.usrbean.getLocation(),(String)hash.get("IDPERSONA"));
				String asientoAnticiposCliente = ANTICIPOS_CLI + asientoCliente;
				
				String ctaClientes = CONTABILIDAD_CAJA_ANTICIPOS;
				
				
				// Con el BANCOS_CODIGO, obtenemos de FAC_BANCOINSTITUCION, el numerocuenta.
			    asientoContableIngresosExtra	= CONTABILIDAD_INGRESOS_EXTRA;
				
				
				Hashtable a = new Hashtable();
				
				// Escribimos 1º asiento
				UtilidadesHash.set(a, "FECHA", 			UtilidadesHash.getShortDate(hash, "FECHAEFECTIVA"));
				UtilidadesHash.set(a, "CONCEPTO", 		concepto);
				UtilidadesHash.set(a, "DOCUMENTO", 		UtilidadesHash.getString(hash, "IDANTICIPO"));
				UtilidadesHash.set(a, "CUENTA", 		asientoContableIngresosExtra);
				UtilidadesHash.set(a, "DEBE", 			imp);
				UtilidadesHash.set(a, "HABER", 			"0");
				UtilidadesHash.set(a, "CONTRAPARTIDA", 	asientoAnticiposCliente);
				pwcontabilidad.write(this.generarLineaAbono(asiento, a));
				
				// Escribimos 2º asiento
				a.clear();
				UtilidadesHash.set(a, "FECHA", 			UtilidadesHash.getShortDate(hash, "FECHAEFECTIVA"));
				UtilidadesHash.set(a, "CONCEPTO", 		concepto);
				UtilidadesHash.set(a, "DOCUMENTO", 		UtilidadesHash.getString(hash, "IDANTICIPO"));
				UtilidadesHash.set(a, "CUENTA", 		asientoAnticiposCliente);
				UtilidadesHash.set(a, "DEBE", 			"0");
				UtilidadesHash.set(a, "HABER", 			imp);
				UtilidadesHash.set(a, "CONTRAPARTIDA", 	asientoContableIngresosExtra);
				pwcontabilidad.write(this.generarLineaAbono(asiento, a));
	
				/////////////////////////////////////////
				
			}
			
			tx.commit();
			
			// 	NO APUNTAMOS QUE HA SIDO CONTABILIZADO
			
			return pwcontabilidad;
		}
		catch (Exception e) 
		{
			pwcontabilidad.write("Error en asiento15: " + e.getMessage());
			try { tx.rollback(); } catch (Exception ee) {}
		    throw new ClsExceptions(e,"Error al generar asiento 15");
		}
	}


	/**
	 * 
	 * @param pwcontabilidad
	 * @param fechaDesde
	 * @param fechaHasta
	 * @param usr
	 * @return
	 * @throws SIGAException
	 */
	// PAGO POR CAJA: ABONO

	private java.io.PrintWriter generaAsiento6(java.io.PrintWriter pwcontabilidad, String fechaDesde, String fechaHasta, UserTransaction tx) throws SIGAException, ClsExceptions
	{
	    
	    // RGG 21/11/2007
		String concepto 		= "";
		String asientoContable 	= null;
		String asientoContableCajaAbono="";
		// Variables
		Vector vAbono 	= null;
		Hashtable hash 		= null, laHash 	= null;
		String select 		= null, imp = null;
		// Beans
		FacPagoAbonoEfectivoAdm facPagoAbonoEfectivoAdm 	= new FacPagoAbonoEfectivoAdm(this.usrbean);
		try
		{
			select = " SELECT A.IDABONO IDABONO, B.NUMEROABONO NUMEROABONO, A.IMPORTE IMPORTE, B.IDPERSONA IDPERSONA, A.FECHA FECHA, (SELECT '1' FROM FAC_PAGOSPORCAJA PC WHERE PC.IDINSTITUCION=A.IDINSTITUCION AND PC.IDABONO=A.IDABONO AND PC.IDPAGOABONO=A.IDPAGOABONO) AS COMPENSADO " +
			" FROM FAC_PAGOABONOEFECTIVO A , FAC_ABONO B "+
			" WHERE A.IDINSTITUCION = "+this.usrbean.getLocation()+" AND B.IDPAGOSJG is null AND A.IDINSTITUCION = B.IDINSTITUCION AND A.IDABONO = B.IDABONO";
			
			if(!fechaDesde.equals("") && !fechaHasta.equals(""))
				select+=" AND A.FECHA >= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaDesde)+"','YYYY/MM/DD hh24:mi:ss') AND A.FECHA <= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaHasta)+"','YYYY/MM/DD hh24:mi:ss')";
			else if(!fechaDesde.equals(""))
				select+=" AND A.FECHA >= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaDesde)+"','YYYY/MM/DD hh24:mi:ss')";
			else if(!fechaHasta.equals(""))
				select+=" AND A.FECHA <= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaHasta)+"','YYYY/MM/DD hh24:mi:ss')";
			
			vAbono=(Vector)this.selectTabla(select);
			int con=0;
			tx.begin();
			for(int x=0;x<vAbono.size();x++)
			{
			    con++;
			    if (con%numeroTransaccion==0) {
			        tx.commit();
			        tx.begin(); 
			    }
				hash = (Hashtable) vAbono.get(x);
				imp = (String) hash.get("IMPORTE");
				// Con el IDPERSONA, obtenemos de CEN_CLIENTE, el asiento contable.
				asientoContable =  obtenerAsientoContable(this.usrbean.getLocation(),(String)hash.get("IDPERSONA"));
				String compensado = (String)hash.get("COMPENSADO");
				// Descripcion del concepto
				concepto = UtilidadesString.sustituirParaExcell(UtilidadesString.getMensajeIdioma(this.usrbean,CONCEPTO_ASIENTO6));
				// RGG concepto  = obtenerDescripcion(CONCEPTO_ASIENTO6,usr.getLanguage(),usr);
				asiento++;
				
				String asientoCompensacionCliente = CONTABILIDAD_COMPENSACION + asientoContable;
				
				
				asientoContableCajaAbono=CONTAB_CAJA_ABONO;
				
				Hashtable a = new Hashtable();
				
				if (compensado!=null && compensado.trim().equals("1")) {
					// PAGO COMPENSADO
					// Escribimos 1º asiento
					UtilidadesHash.set(a, "FECHA", 			UtilidadesHash.getShortDate(hash, "FECHA"));
					UtilidadesHash.set(a, "CONCEPTO", 		concepto);
					UtilidadesHash.set(a, "DOCUMENTO", 		UtilidadesHash.getString(hash, "NUMEROABONO"));
					UtilidadesHash.set(a, "CUENTA", 		asientoCompensacionCliente);
					UtilidadesHash.set(a, "DEBE", 			imp);
					UtilidadesHash.set(a, "HABER", 			"0");
					UtilidadesHash.set(a, "BASEIMPONIBLE", 	"");
					UtilidadesHash.set(a, "IVA", 			"");
					UtilidadesHash.set(a, "CONTRAPARTIDA", 	asientoContableCajaAbono);
					pwcontabilidad.write(this.generarLineaAbono(asiento, a));
					
					// Escribimos 2º asiento
					a.clear();
					UtilidadesHash.set(a, "FECHA", 			UtilidadesHash.getShortDate(hash, "FECHA"));
					UtilidadesHash.set(a, "CONCEPTO", 		concepto);
					UtilidadesHash.set(a, "DOCUMENTO", 		UtilidadesHash.getString(hash, "NUMEROABONO"));
					UtilidadesHash.set(a, "CUENTA", 		asientoContableCajaAbono);
					UtilidadesHash.set(a, "DEBE", 			"0");
					UtilidadesHash.set(a, "HABER", 			imp);
					UtilidadesHash.set(a, "BASEIMPONIBLE", 	"");
					UtilidadesHash.set(a, "IVA", 			"");
					UtilidadesHash.set(a, "CONTRAPARTIDA", 	asientoCompensacionCliente);
					pwcontabilidad.write(this.generarLineaAbono(asiento, a));
					///////////////////////////////////////////////////////
					
				} else {
					// PAGO ANTICIPADO O CAJA NORMAL
					// Escribimos 1º asiento
					UtilidadesHash.set(a, "FECHA", 			UtilidadesHash.getShortDate(hash, "FECHA"));
					UtilidadesHash.set(a, "CONCEPTO", 		concepto);
					UtilidadesHash.set(a, "DOCUMENTO", 		UtilidadesHash.getString(hash, "NUMEROABONO"));
					UtilidadesHash.set(a, "CUENTA", 		CONTABILIDAD_CAJA);
					UtilidadesHash.set(a, "DEBE", 			imp);
					UtilidadesHash.set(a, "HABER", 			"0");
					UtilidadesHash.set(a, "BASEIMPONIBLE", 	"");
					UtilidadesHash.set(a, "IVA", 			"");
					UtilidadesHash.set(a, "CONTRAPARTIDA", 	asientoContable);
					pwcontabilidad.write(this.generarLineaAbono(asiento, a));
					
					// Escribimos 2º asiento
					a.clear();
					UtilidadesHash.set(a, "FECHA", 			UtilidadesHash.getShortDate(hash, "FECHA"));
					UtilidadesHash.set(a, "CONCEPTO", 		concepto);
					UtilidadesHash.set(a, "DOCUMENTO", 		UtilidadesHash.getString(hash, "NUMEROABONO"));
					UtilidadesHash.set(a, "CUENTA", 		asientoContable);
					UtilidadesHash.set(a, "DEBE", 			"0");
					UtilidadesHash.set(a, "HABER", 			imp);
					UtilidadesHash.set(a, "BASEIMPONIBLE", 	"");
					UtilidadesHash.set(a, "IVA", 			"");
					UtilidadesHash.set(a, "CONTRAPARTIDA", 	CONTABILIDAD_CAJA);
					pwcontabilidad.write(this.generarLineaAbono(asiento, a));
					///////////////////////////////////////////////////////
				}

				String[] claves = {"IDINSTITUCION","IDABONO"};
				String[] campos = {"CONTABILIZADO","USUMODIFICACION","FECHAMODIFICACION"};
				laHash = new Hashtable();
				laHash.put("CONTABILIZADO",ClsConstants.FACTURA_ABONO_CONTABILIZADA);
				laHash.put("IDINSTITUCION",this.usrbean.getLocation());
				laHash.put("IDABONO",hash.get("IDABONO"));
				laHash.put("USUMODIFICACION",this.usrbean.getUserName());
				laHash.put("FECHAMODIFICACION","sysdate");
				facPagoAbonoEfectivoAdm.updateDirect(laHash,claves,campos);
			}
			tx.commit();
			return pwcontabilidad;
		}
		catch (Exception e) 
		{
			pwcontabilidad.write("Error en asiento6: " + e.getMessage());
			try { tx.rollback(); } catch (Exception ee) {}
		    throw new ClsExceptions(e,"Error al generar asiento 6");
		}
	}

	
	/**
	 * 
	 * @param pwcontabilidad
	 * @param fechaDesde
	 * @param fechaHasta
	 * @param usr
	 * @return
	 * @throws SIGAException
	 */
	// PAGO POR BANCO: ABONO

	private java.io.PrintWriter generaAsiento7(java.io.PrintWriter pwcontabilidad, String fechaDesde, String fechaHasta, UserTransaction tx) throws SIGAException, ClsExceptions
	{
		// Tipos de asientos.
		// 7------>
		// Banco (572.1xxx) 		-->  pagoabonobanco	 	--> 0
		// Cliente(430.xxx)			-->  0			 		--> pagoabonobanco
		String concepto 		= "Pago por banco. Abono Nº";
		String asientoContable 	= null, asientoContableBanco = null;
		// Variables
		Vector vAbono 	= null;
		Hashtable hash 		= null, laHash 	= null;
		String select 		= null, imp = null;
		// Beans
		FacAbonoIncluidoEnDisqueteAdm facAbonoIncluidoEnDisqueteAdm 	= new FacAbonoIncluidoEnDisqueteAdm(this.usrbean);
		try
		{
			select= " SELECT A.IDABONO IDABONO, C.NUMEROABONO NUMEROABONO, A.IDDISQUETEABONO IDDISQUETEABONO, (A.IMPORTEABONADO * -1) IMPORTE, B.BANCOS_CODIGO BANCOS_CODIGO, C.IDPERSONA IDPERSONA, B.FECHA FECHA"+
			" FROM FAC_ABONOINCLUIDOENDISQUETE A, FAC_DISQUETEABONOS B, FAC_ABONO C"+
			" WHERE A.IDINSTITUCION = "+this.usrbean.getLocation()+" AND C.IDPAGOSJG is null AND A.IDDISQUETEABONO = B.IDDISQUETEABONO AND A.IDINSTITUCION = B.IDINSTITUCION "+
			" AND A.IDINSTITUCION = C.IDINSTITUCION AND A.IDDISQUETEABONO = B.IDDISQUETEABONO AND "+
			" A.IDINSTITUCION = C.IDINSTITUCION AND A.IDABONO = C.IDABONO ";
			if(!fechaDesde.equals("") && !fechaHasta.equals(""))
				select+=" AND B.FECHA >= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaDesde)+"','YYYY/MM/DD hh24:mi:ss') AND B.FECHA <= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaHasta)+"','YYYY/MM/DD hh24:mi:ss')";
			else if(!fechaDesde.equals(""))
				select+=" AND B.FECHA >= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaDesde)+"','YYYY/MM/DD hh24:mi:ss')";
			else if(!fechaHasta.equals(""))
				select+=" AND B.FECHA <= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaHasta)+"','YYYY/MM/DD hh24:mi:ss')";
			vAbono=(Vector)this.selectTabla(select);
			int con=0;
			tx.begin();
			for(int x=0;x<vAbono.size();x++)
			{
			    con++;
			    if (con%numeroTransaccion==0) {
			        tx.commit();
			        tx.begin(); 
			    }
				hash = (Hashtable) vAbono.get(x);
				imp = (String) hash.get("IMPORTE");
				// Con el IDPERSONA, obtenemos de CEN_CLIENTE, el asiento contable.
				asientoContable = obtenerAsientoContable(this.usrbean.getLocation(),(String)hash.get("IDPERSONA"));
				// Con el BANCOS_CODIGO, obtenemos de FAC_BANCOINSTITUCION, el numerocuenta.
				asientoContableBanco	= obtenerAsientoContableBanco(this.usrbean.getLocation(),(String)hash.get("BANCOS_CODIGO"));  
				// Descripcion del concepto
				concepto = UtilidadesString.sustituirParaExcell(UtilidadesString.getMensajeIdioma(this.usrbean,CONCEPTO_ASIENTO7));
				// RGG concepto  = obtenerDescripcion(CONCEPTO_ASIENTO7,usr.getLanguage(),usr);
				asiento++;

				Hashtable a = new Hashtable();
				
				// Escribimos 1º asiento
				UtilidadesHash.set(a, "FECHA", 			UtilidadesHash.getShortDate(hash, "FECHA"));
				UtilidadesHash.set(a, "CONCEPTO", 		concepto);
				UtilidadesHash.set(a, "DOCUMENTO", 		UtilidadesHash.getString(hash, "NUMEROABONO"));
				UtilidadesHash.set(a, "CUENTA", 		asientoContableBanco);
				UtilidadesHash.set(a, "DEBE", 			imp);
				UtilidadesHash.set(a, "HABER", 			"0");
				UtilidadesHash.set(a, "BASEIMPONIBLE", 	"");
				UtilidadesHash.set(a, "IVA", 			"");
				UtilidadesHash.set(a, "CONTRAPARTIDA", 	asientoContable);
				pwcontabilidad.write(this.generarLineaAbono(asiento, a));
				
				// Escribimos 2º asiento
				a.clear();
				UtilidadesHash.set(a, "FECHA", 			UtilidadesHash.getShortDate(hash, "FECHA"));
				UtilidadesHash.set(a, "CONCEPTO", 		concepto);
				UtilidadesHash.set(a, "DOCUMENTO", 		UtilidadesHash.getString(hash, "NUMEROABONO"));
				UtilidadesHash.set(a, "CUENTA", 		asientoContable);
				UtilidadesHash.set(a, "DEBE", 			"0");
				UtilidadesHash.set(a, "HABER", 			imp);
				UtilidadesHash.set(a, "BASEIMPONIBLE", 	"");
				UtilidadesHash.set(a, "IVA", 			"");
				UtilidadesHash.set(a, "CONTRAPARTIDA", 	asientoContableBanco);
				pwcontabilidad.write(this.generarLineaAbono(asiento, a));
				///////////////////////////////////////////////////////
				
				String[] claves = {"IDINSTITUCION","IDABONO"};
				String[] campos = {"CONTABILIZADO","USUMODIFICACION","FECHAMODIFICACION"};
				laHash = new Hashtable();
				laHash.put("CONTABILIZADO",ClsConstants.FACTURA_ABONO_CONTABILIZADA);
				laHash.put("IDINSTITUCION",this.usrbean.getLocation());
				laHash.put("IDABONO",hash.get("IDABONO"));
				laHash.put("USUMODIFICACION",this.usrbean.getUserName());
				laHash.put("FECHAMODIFICACION","sysdate");
				facAbonoIncluidoEnDisqueteAdm.updateDirect(laHash,claves,campos);
			}
			tx.commit();
			
			return pwcontabilidad;
		}
		catch (Exception e) 
		{
			pwcontabilidad.write("Error en asiento7: " + e.getMessage());
			try { tx.rollback(); } catch (Exception ee) {}
		    throw new ClsExceptions(e,"Error al generar asiento 7");
		}
	}

	
	
	/**
	 * generarLineaAbono
	 * Metodo que formatea los datos a mostrar correspondiendtes a un asiento. 
	 * @param asiento
	 * @param datos
	 * @return String formteado
	 * @author DCG
	 */
	private String generarLineaAbono (int asiento, Hashtable datos)
	{									 	
		String linea = "";
		Double importeDebe = UtilidadesHash.getDouble(datos, "DEBE");
		Double importeHaber = UtilidadesHash.getDouble(datos, "HABER");
		Double baseImponible = UtilidadesHash.getDouble(datos, "BASEIMPONIBLE");
		String baseImp = "";
		if(importeDebe<0.0){
			importeHaber = -1 * importeDebe;
			importeDebe = 0.0;
		}
		if(importeHaber<0.0){
			importeDebe = -1 * importeHaber;
			importeHaber = 0.0;
		}
		if(baseImponible!=null&&baseImponible<0.0){
			baseImponible = -1 * baseImponible;
			baseImp = baseImponible.toString().replace('.', ',');
		}else{
			baseImp = (UtilidadesHash.getString(datos, "BASEIMPONIBLE") != null ? UtilidadesHash.getString(datos, "BASEIMPONIBLE").replace('.',','):"");
		}
		linea = // Numero Asiento
			asiento 
			+ SEPARADOR +	
			
			// Fecha
			(UtilidadesHash.getString(datos, "FECHA") != null ? UtilidadesHash.getString(datos, "FECHA"):"") 
			+ SEPARADOR +
			
			// Cuenta
			(UtilidadesHash.getString(datos, "CUENTA") != null ? UtilidadesHash.getString(datos, "CUENTA"):"") 
			+ SEPARADOR +
			
			// Concepto
			(UtilidadesHash.getString(datos, "CONCEPTO") != null ? UtilidadesHash.getString(datos, "CONCEPTO"):"") 
			+ SEPARADOR +
			
			// Documento
			//(UtilidadesHash.getString(datos, "DOCUMENTO") != null ?  UtilidadesHash.getString(datos, "DOCUMENTO")+"\u00A0":"") 
			(UtilidadesHash.getString(datos, "DOCUMENTO") != null ?  UtilidadesHash.getString(datos, "DOCUMENTO"):"") 
			+ SEPARADOR +
			
			// Debe
			//(UtilidadesHash.getString(datos, "DEBE") != null ? UtilidadesHash.getString(datos, "DEBE").replace('.',','):"")
			importeDebe.toString().replace('.', ',')
			+ SEPARADOR +
			
			// Haber
			//(UtilidadesHash.getString(datos, "HABER") != null ? UtilidadesHash.getString(datos, "HABER").replace('.',','):"")
			importeHaber.toString().replace('.', ',')
			+ SEPARADOR +
			
			// Base Imponible
			//(UtilidadesHash.getString(datos, "BASEIMPONIBLE") != null ? UtilidadesHash.getString(datos, "BASEIMPONIBLE").replace('.',','):"")
			baseImp
			+ SEPARADOR +
			
			// IVA
			(UtilidadesHash.getString(datos, "IVA") != null ? UtilidadesHash.getString(datos, "IVA"):"") 
			+ SEPARADOR +
			
			// Contrapartida
			(UtilidadesHash.getString(datos, "CONTRAPARTIDA") != null ? UtilidadesHash.getString(datos, "CONTRAPARTIDA"):"") 
//			+ "\n";
			// RGG 28/02/2007 cambio para meter salto de linea formato DOS
			+ "\r\n";
		
		return linea;
	}
	/**
	 * 
	 * @param idInstitucion
	 * @param idpersona
	 * @param user
	 * @return
	 * @throws SIGAException
	 */
	private String obtenerAsientoContable(String idInstitucion, String idpersona) throws SIGAException, ClsExceptions
	{
		String asientoContable = "";
		            
        Hashtable codigos = new Hashtable();
		try
		{
			CenClienteAdm cenCliente = new CenClienteAdm(this.usrbean);
			// Con el IDPERSONA, obtenemos de CEN_CLIENTE, el asiento contable.
			codigos.put(new Integer(1),idInstitucion);
			String where = " WHERE IDINSTITUCION = :1";
			codigos.put(new Integer(2),idpersona);
			where +=       " AND IDPERSONA = :2"; 
			Vector vResultado=(Vector)cenCliente.selectBind(where,codigos);
			if(vResultado == null || vResultado.size()==0)
			    throw new ClsExceptions("No se ha encontrado la cuenta contable");
			asientoContable =  String.valueOf(((CenClienteBean) vResultado.get(0)).getAsientoContable());
		}
		catch(Exception e)
		{
		    throw new ClsExceptions(e,"Error al obtener asiento contable");
		}
		return asientoContable;
	}
	
	/**
	 * 
	 * @param idInstitucion
	 * @param idpersona
	 * @param user
	 * @return
	 * @throws SIGAException
	 */
	private String obtenerAsientoContableAcreedor(String idInstitucion, String idpersona) throws SIGAException, ClsExceptions
	{
		String asientoContable = "";
		Hashtable codigos = new Hashtable();
		try
		{
			CenColegiadoAdm cenColegiado = new CenColegiadoAdm(this.usrbean);
			// Con el IDPERSONA, obtenemos de CEN_CLIENTE, el asiento contable.
			codigos.put(new Integer(1),idInstitucion);
			String where = " WHERE IDINSTITUCION = :1";
			codigos.put(new Integer(2),idpersona);
			where+=" AND IDPERSONA = :2"; 
			Vector vResultado=(Vector)cenColegiado.selectBind(where,codigos);
			if(vResultado == null || vResultado.size()==0)
			    throw new ClsExceptions("No se ha encontrado la cuenta contable");
			asientoContable =  String.valueOf(((CenColegiadoBean) vResultado.get(0)).getCuentaContableSJCS());
		}
		catch(Exception e)
		{
		    throw new ClsExceptions(e,"Error al obtener asiento contable acreedor");
		}
		return asientoContable;
	}
	
	/**
	 * 
	 * @param idInstitucion
	 * @param bancosCodigo
	 * @param user
	 * @return
	 * @throws SIGAException
	 */
	private String obtenerAsientoContableBanco(String idInstitucion, String bancosCodigo) throws SIGAException, ClsExceptions
	{
		String asientoContable = "";
		Hashtable codigos = new Hashtable();
		try
		{
			FacBancoInstitucionAdm facBancoInstitucionAdm = new FacBancoInstitucionAdm(this.usrbean);
			// Con el BANCOS_CODIGO, obtenemos de FAC_BANCOINSTITUCION, el numerocuenta.
			codigos.put(new Integer(1),idInstitucion);
			String where = " WHERE IDINSTITUCION = :1";
			codigos.put(new Integer(2),bancosCodigo);
			where +=       " AND BANCOS_CODIGO = :2"; 
			Vector vResultado=(Vector)facBancoInstitucionAdm.selectBind(where,codigos);
			if(vResultado == null || vResultado.size()==0)
			    throw new ClsExceptions("No se ha encontrado la cuenta contable");
			asientoContable =  String.valueOf(((FacBancoInstitucionBean) vResultado.get(0)).getAsientoContable());
		}
		catch(Exception e)
		{
		    throw new ClsExceptions(e,"Error al obtener asiento contable banco");
		}
		return asientoContable;
	}
	
	/**
	 * 
	 * @param idInstitucion
	 * @param idtipoproducto
	 * @param idproducto
	 * @param idproductoinstitucion
	 * @return String con el numero de cuenta contable
	 * @throws SIGAException
	 */
	private String obtenerAsientoContableProducto(String idInstitucion, String idtipoproducto, String idproducto, String idproductoinstitucion) throws SIGAException, ClsExceptions
	{
		String asientoContable = "";
		Hashtable codigos = new Hashtable();
		try
		{
			PysProductosInstitucionAdm pysProductosInstitucionAdm = new PysProductosInstitucionAdm(this.usrbean);
			// Con el BANCOS_CODIGO, obtenemos de FAC_BANCOINSTITUCION, el numerocuenta.
			codigos.put(new Integer(1),idInstitucion);
			String where = " WHERE IDINSTITUCION = :1";
			codigos.put(new Integer(2),idtipoproducto);
			where +=" AND IDTIPOPRODUCTO = :2";
			codigos.put(new Integer(3),idproducto);
			where +=" AND IDPRODUCTO = :3";
			codigos.put(new Integer(4),idproductoinstitucion);
			where +=" AND IDPRODUCTOINSTITUCION = :4"; 
			Vector vResultado=(Vector)pysProductosInstitucionAdm.selectBind(where,codigos);
			if(vResultado == null || vResultado.size()==0)
			    throw new ClsExceptions("No se ha encontrado la cuenta contable");
			asientoContable =  String.valueOf(((PysProductosInstitucionBean) vResultado.get(0)).getCuentacontable());
		}
		catch(Exception e)
		{
		    throw new ClsExceptions(e,"Error al obtener asiento contable producto");
		}
		return asientoContable;
	}
	
	/**
	 * 
	 * @param idInstitucion
	 * @param idtiposervicio
	 * @param idservicio
	 * @param idservicioinstitucion
	 * @return String con el numero de cuenta contable
	 * @throws SIGAException
	 */
	private String obtenerAsientoContableServicio(String idInstitucion, String idtiposervicio, String idservicio, String idservicioinstitucion) throws SIGAException, ClsExceptions
	{
		String asientoContable = "";
		Hashtable codigos = new Hashtable();
		try
		{
			PysServiciosInstitucionAdm pysServiciosInstitucionAdm = new PysServiciosInstitucionAdm(this.usrbean);
			codigos.put(new Integer(1),idInstitucion);
			// Con el BANCOS_CODIGO, obtenemos de FAC_BANCOINSTITUCION, el numerocuenta.
			String where = " WHERE IDINSTITUCION = :1";
			codigos.put(new Integer(2),idtiposervicio);
			where +=" AND IDTIPOSERVICIOS = :2";
			codigos.put(new Integer(3),idservicio);
			where +=" AND IDSERVICIO = :3";
			codigos.put(new Integer(4),idservicioinstitucion);
			where +=" AND IDSERVICIOSINSTITUCION = :4"; 
			Vector vResultado=(Vector)pysServiciosInstitucionAdm.selectBind(where,codigos);
			if(vResultado == null || vResultado.size()==0)
				throw new SIGAException("messages.general.error",null,new String[] {"modulo.facturacion"});
			asientoContable =  String.valueOf(((PysServiciosInstitucionBean) vResultado.get(0)).getCuentacontable());
		}
		catch(Exception e)
		{
		    throw new ClsExceptions(e,"Error al obtener asiento contable servicio");
		}
		return asientoContable;
	}
	
	/**
	 * 
	 * @param fechaDesde
	 * @param fechaHasta
	 * @param idinstitucion
	 * @param user
	 * @return
	 * @throws ClsExceptions
	 */
	public boolean existeContabilidad(String fechaDesde, String fechaHasta, String idinstitucion) throws ClsExceptions
	{
		boolean existe = false;
		String where = null, sql = null;
		Vector result = null;
        Hashtable codigos = new Hashtable();
		try
		{
		    codigos.put(new Integer(1),fechaHasta);
		    sql = " WHERE (FECHADESDE < TO_DATE(:1,'DD/MM/YYYY hh24:mi:ss')";
		    codigos.put(new Integer(2),fechaHasta);
		    sql+= " AND TO_DATE(:2,'DD/MM/YYYY hh24:mi:ss') < FECHAHASTA) " ;
		    codigos.put(new Integer(3),fechaDesde);
		    sql+=" OR    (FECHADESDE < TO_DATE(:3,'DD/MM/YYYY hh24:mi:ss')";
		    codigos.put(new Integer(4),fechaDesde);
		    sql+=" AND TO_DATE(:4,'DD/MM/YYYY hh24:mi:ss') < FECHADESDE) ";
		    
		    result = this.selectBind(sql,codigos);
		    if (result!=null && result.size()>0) {
		        existe = true;
		    }
		    return existe;
		    

		    
		}
		catch(Exception e)
		{
		    throw new ClsExceptions(e,"Error comprobando existencia de contabilidad");
		}
	}
	

	
	/**
	 * 
	 * @param idInstitucion
	 * @param user
	 * @throws ClsExceptions
	 */
	private void crearCuentas(String idInstitucion, UsrBean user) throws ClsExceptions
	{
		try	{
			GenParametrosAdm genParametros = new GenParametrosAdm(user);
			CONTABILIDAD_VENTAS = genParametros.getValor(idInstitucion.toString(),"FAC","CONTABILIDAD_VENTAS",null);
			CONTABILIDAD_IVA = genParametros.getValor(idInstitucion.toString(),"FAC","CONTABILIDAD_IVA",null);
			CONTABILIDAD_TARJETAS = genParametros.getValor(idInstitucion.toString(),"FAC","CONTABILIDAD_TARJETAS",null);
			CONTABILIDAD_CAJA = genParametros.getValor(idInstitucion.toString(),"FAC","CONTABILIDAD_CAJA",null);
			CONTABILIDAD_COMPENSACION = genParametros.getValor(idInstitucion.toString(),"FAC","CONTAB_COMPENSACIONES",null);
			CONTABILIDAD_CAJA_ANTICIPOS = genParametros.getValor(idInstitucion.toString(),"FAC","CONTAB_ANTICIPOS_CAJA",null);
			CONTABILIDAD_INGRESOS_EXTRA = genParametros.getValor(idInstitucion.toString(),"FAC","CONTAB_INGRESOS_EXTRA",null);
			
			ANTICIPOS_CLI = genParametros.getValor(idInstitucion.toString(),"FAC","CONTAB_ANTICIPOS_CLI",null);
			CONTABILIDAD_GASTOSBANCARIOS = genParametros.getValor(idInstitucion.toString(),"FAC","CONTAB_GASTOS_BANCARIOS",null);
			// RGG 06/11/2006
			CONTABILIDAD_DEVOL_FACTURAS = genParametros.getValor(idInstitucion.toString(),"FAC","CONTAB_DEVOL_FACTURAS",null);
			CONTAB_SERVICIOS_PROFESIONALES = genParametros.getValor(idInstitucion.toString(),"FAC","CONTAB_SERVICIOS_PROFESIONALES",null);
			CONTAB_MOVIMIENTOS_VARIOS = genParametros.getValor(idInstitucion.toString(),"FAC","CONTAB_MOVIMIENTOS_VARIOS",null);
			CONTAB_MOV_VARIOS_NEGATIVOS = genParametros.getValor(idInstitucion.toString(),"FAC","CONTAB_MOV_VARIOS_NEGATIVOS",null);
			CONTAB_RETENCIONESJUDICIALES = genParametros.getValor(idInstitucion.toString(),"FAC","CONTAB_RETENCIONESJUDICIALES",null);
			CONTAB_VENTAS_PRODUCTOS = genParametros.getValor(idInstitucion.toString(),"FAC","CONTAB_VENTAS_PRODUCTOS",null);
			CONTAB_VENTAS_SERVICIOS = genParametros.getValor(idInstitucion.toString(),"FAC","CONTAB_VENTAS_SERVICIOS",null);
			CONTAB_IRPF = genParametros.getValor(idInstitucion.toString(),"FAC","CONTAB_IRPF",null);
			CONTAB_CAJA_ABONO = genParametros.getValor(idInstitucion.toString(),"FAC","CONTAB_CAJA_ABONO",null);
		}
		catch(Exception e)
		{
			throw new ClsExceptions(e,"Error creando cuentas");
		}
	}
	
	
	
	
}
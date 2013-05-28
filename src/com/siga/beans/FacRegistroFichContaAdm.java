
package com.siga.beans;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.UserTransaction;

import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesNumero;
import com.siga.Utilidades.UtilidadesString;
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
	private String SEPARADOR = "\t";	
	
	private String CONTABILIDAD_IVA 		    = "";
	private String CONTABILIDAD_TARJETAS 	    = "";
	private String CONTABILIDAD_CAJA 		    = "";
	private String CONTAB_CAJA_ABONO		    = "";	
	private String CONTABILIDAD_COMPENSACION    = "";
	private String CONTABILIDAD_CAJA_ANTICIPOS	= "";
	private String CONTABILIDAD_INGRESOS_EXTRA	= "";
	private String ANTICIPOS_CLI 			    = "";
	private String CONTABILIDAD_GASTOSBANCARIOS	= "";

	private String CONCEPTO_ASIENTO1		= "general.literal.asiento1"; 	    // Factura
	private String CONCEPTO_ASIENTO2		= "general.literal.asiento2"; 	    // Factura Rectificativa Nº
	private String CONCEPTO_ASIENTO3		= "general.literal.asiento3"; 	    // Pago por caja. Factura 
	private String CONCEPTO_ASIENTO3_2		= "general.literal.asiento3_2"; 	// Pago Anticipado. Factura 
	private String CONCEPTO_ASIENTO4		= "general.literal.asiento4"; 	    // Pago por banco. Factura 
	private String CONCEPTO_ASIENTO5		= "general.literal.asiento5"; 	    // Devolucion por banco. Factura Nº
	private String CONCEPTO_ASIENTO6		= "general.literal.asiento6"; 	    // Pago por caja. Factura Rectificativa Nº 
	private String CONCEPTO_ASIENTO7		= "general.literal.asiento7"; 	    // Pago por banco. Factura Rectificativa Nº
	private String CONCEPTO_ASIENTO10		= "general.literal.asiento3_1";     // Pago por tarjeta. Factura
	private String CONCEPTO_ASIENTO3_2010   = "general.literal.asiento3_2010";  // Compensación por caja
	private String CONCEPTO_ASIENTO14		= "general.literal.asiento14";      // Alta de anticipos 
	private String CONCEPTO_ASIENTO15		= "general.literal.asiento15";      // Liquidación de anticipos de Letrado por Baja Colegial 
	
	
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
		PrintWriter pwcontabilidad=null;
		FileOutputStream focontabilidad = null;
		tx = this.usrbean.getTransactionPesada(); 
		
		try
		{
			String idcontabilidad 	= beanContab.getIdContabilidad().toString();

			// PREPARAMOS EL FICHERO
			// Se crea el directorio en el servidor web.
		    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
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
			// FACTURA. Apunta asiento contable de facturas generadas, no pagada.
			// Ventas (700)	 			--> Bruto por servicio (1) --> 0
			// IVA 				 	    --> Iva (2)		           --> 0
			// Cliente(430.xxx)			--> 0		               --> Negocio(1)+Negocio(2)
			// -----------------------------------------------------------------------------------------------------------------		    
		    
			ClsLogging.writeFileLog("********* CONTABILIDAD ASIENTO 1",10);
			pwcontabilidad=generaAsiento1(pwcontabilidad,fechaDesde,fechaHasta,tx);
					
			// -----------------------------------------------------------------------------------------------------------------
			// RGG 03/11/2006 Asiento 2 
			// ABONO (POR IMPORTE EXCESIVO EN FACTURA) 
			// 2------>
			// Cliente(430.xxxx)            --> abono(1)+abono(2)	--> 0
			// Devolucion factura (708)		--> 0					--> abono(1)
			// IVA (477)					--> 0					--> abono(2)
			// -----------------------------------------------------------------------------------------------------------------
			
			ClsLogging.writeFileLog("********* CONTABILIDAD ASIENTO 2",10);
			pwcontabilidad=generaAsiento2(pwcontabilidad,fechaDesde,fechaHasta,tx);

			
			// -----------------------------------------------------------------------------------------------------------------
			// RGG 03/11/2006 Asiento 3
			// PAGO POR CAJA 
			// 3------> HABER --> BEDE
			// Cliente(430.xxx ) 	-->  pagoporcaja --> 0
			// Caja(570) 		 	-->  0			 --> pagoporcaja
			// OJO, parece que se estaban metiendo tambien los de pago por tarjeta
			// -----------------------------------------------------------------------------------------------------------------
			
			ClsLogging.writeFileLog("********* CONTABILIDAD ASIENTO 3",10);
			pwcontabilidad=generaAsiento3(pwcontabilidad,fechaDesde,fechaHasta,tx);

			// -----------------------------------------------------------------------------------------------------------------
			// RGG 03/11/2006 Asiento 4 
			// PAGO POR BANCO 
			// 4------> Banco
			// Cliente(430.xxx) 	-->  facturaincluidaendisquete 	--> 0
			// Banco(572.1xxx) 		-->  0			 				--> facturaincludidaendisquete
			// -----------------------------------------------------------------------------------------------------------------			
			
			ClsLogging.writeFileLog("********* CONTABILIDAD ASIENTO 4",10);
			pwcontabilidad=generaAsiento4(pwcontabilidad,fechaDesde,fechaHasta,tx);

			// -----------------------------------------------------------------------------------------------------------------
			// RGG 03/11/2006 Asiento 10 
			// PAGO POR TARJETA 
			// 10------> Tarjeta
			// Cliente(430.xxx) 	-->  facturaincluidaendisquete 	--> 0
			// Banco(572.2xxx) 		-->  0			 				--> facturaincludidaendisquete
			// -----------------------------------------------------------------------------------------------------------------
			
			ClsLogging.writeFileLog("********* CONTABILIDAD ASIENTO 5",10);
			pwcontabilidad=generaAsiento5(pwcontabilidad,fechaDesde,fechaHasta,tx);

			// -----------------------------------------------------------------------------------------------------------------
			// RGG 03/11/2006 Asiento 5
			// DEVOLUCION DE FACTURA POR BANCO 
			// 5------>
			// Banco(572.1xxxx) 			-->  facturaincluidaendisquete --> 0
			// Cliente(430.xxx)				-->  0			 			   --> facturaincludidaendisquete
			// Si hay gastos bancarios
			// Banco(572.1xxxx) 			-->  GASTOSDEVOLUCION 			--> 0
			// Gastos bancarios(626)		-->  0			 				--> GASTOSDEVOLUCION
			// -----------------------------------------------------------------------------------------------------------------
			
			ClsLogging.writeFileLog("********* CONTABILIDAD ASIENTO 6",10);
			pwcontabilidad=generaAsiento6(pwcontabilidad,fechaDesde,fechaHasta,tx);
			
			// -----------------------------------------------------------------------------------------------------------------
			// RGG 03/11/2006 Asiento 14
			// ALTA DE ANTICIPOS DE SERVICIOS. 
			// 14------> HABER --> BEDE
			// Caja anticipos (572.3xxxx) 	-->  importe del anticipo 	--> 0
			// Anticipos Cliente(438.xxx)	-->  0			 			--> importe del anticipo
			ClsLogging.writeFileLog("********* CONTABILIDAD ASIENTO 7",10);
			pwcontabilidad=generaAsiento7(pwcontabilidad,fechaDesde,fechaHasta,tx);
			
			// -----------------------------------------------------------------------------------------------------------------
			// RGG 03/11/2006 Asiento 15
			// LIQUIDACION DE ANTICIPOS DE SERVICIOS AL COLEGIO. 
			// 14------> HABER --> BEDE
			// Anticipos Cliente(438.xxx) 			-->  importe de liquidacion	--> 0
			// Ingresos extraordinarios (778xxxx)	-->  0			 			--> importe de liquidacion
			ClsLogging.writeFileLog("********* CONTABILIDAD ASIENTO 7A",10);
			pwcontabilidad=generaAsiento7A(pwcontabilidad,fechaDesde,fechaHasta,tx);

			// -----------------------------------------------------------------------------------------------------------------
			// RGG 03/11/2006 Asiento 15
			// ANTICIPOS DE SERVICIOS Y PRODUCTOS
			// 14------> HABER --> BEDE
			// Anticipos Cliente(438.xxx) 			-->  importe de liquidacion	--> 0
			// Ingresos extraordinarios (778xxxx)	-->  0			 			--> importe de liquidacion
			ClsLogging.writeFileLog("********* CONTABILIDAD ASIENTO 7B",10);
			pwcontabilidad=generaAsiento7B(pwcontabilidad,fechaDesde,fechaHasta,tx);			
			
			// -----------------------------------------------------------------------------------------------------------------
			// RGG 03/11/2006 Asiento 2A
			// PAGO POR CAJA: ABONO 
			// 6------>
			// Caja(570) 		-->  pagoabonoefectivo 	--> 0
			// Cliente(430.xxx)	-->  0			 		--> pagoabonoefectivo
			
			// jbd // Segun peticion de Javier Jimenez (Badajoz) sobran los pagos por caja de abono (Quitar los asientos de "Pago por caja. Abono")
			ClsLogging.writeFileLog("********* CONTABILIDAD ASIENTO 2A",10);
			pwcontabilidad=generaAsiento2A(pwcontabilidad,fechaDesde,fechaHasta,tx);
			
			// -----------------------------------------------------------------------------------------------------------------
			// RGG 03/11/2006 Asiento 2B
			// PAGO POR BANCO (ABONOS) 
			// 7------>
			// Banco (572.1xxx) 		-->  pagoabonobanco	 	--> 0
			// Cliente(430.xxx)			-->  0			 		--> pagoabonobanco
			ClsLogging.writeFileLog("********* CONTABILIDAD ASIENTO 2B",10);
			pwcontabilidad=generaAsiento2B(pwcontabilidad,fechaDesde,fechaHasta,tx);
			
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
	/*                                            INICIO fACTURAS                                                                        */
    // RGG 20/11/2007 ULTIMO CAMBIO APLICADO A CONTABILIDAD.  AHORA SE HACE A NIVEL DE LINEA DE FACTURA
    
	// Tipos de asientos.
	// nomenclatura de fichero: #cuenta , haber, debe
	// 1------> 
	// Ventas. (700) 					--> Bruto por servicio (1) 	--> 0
	// IVA 								--> Iva (2)					--> 0
	// Cliente(430.xxx)					--> 0						--> Negocio(1)+Negocio(2)	
	private java.io.PrintWriter generaAsiento1(java.io.PrintWriter pwcontabilidad, String fechaDesde, String fechaHasta, UserTransaction tx) throws SIGAException, ClsExceptions{
		
		String concepto 		= "";
		String asientoClientes 	= ""; 
		String asientoIngresos 	= ""; 
		String asientoIVA 	    = "";  
		Vector vLineasFacturas  = null; 
		Vector vContabilidad    = null;
		Hashtable hash          = null; 
		Hashtable laHash        = null;
		String select 			= null;  
		String imp 				= null; 
		String importeIva 		= null;
		Vector vAsiento1		= new Vector();
		int contador 			= 0;             
        Hashtable codigos 		= new Hashtable();		
		// Beans
		FacFacturaAdm facturaAdm 	= new FacFacturaAdm(this.usrbean);
		

        
		if(vContabilidad !=null && vContabilidad.size()>0)	
			CONTABILIDAD_IVA = ((GenParametrosBean) vContabilidad.get(0)).getValor();
		try{
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

			vLineasFacturas=(Vector)this.selectTablaBind(select,codigos);
			
			String idFacturaAnt = ""; 
			String idFactura = "";  
			 
			for(int x=0;x<vLineasFacturas.size();x++){
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
	
	/*                                            FIN fACTURAS                                                                              */	
	
	/*                                            INICIO ABONOS                                                                             */                                                    
	

	/**
	 * 
	 * @param pwcontabilidad
	 * @param fechaDesde
	 * @param fechaHasta
	 * @param usr
	 * @return
	 * @throws SIGAException
	 */
	// ABONO 
	private java.io.PrintWriter generaAsiento2(java.io.PrintWriter pwcontabilidad, String fechaDesde, String fechaHasta, UserTransaction tx) throws SIGAException, ClsExceptions{
	    // RGG 21/11/2007
		String concepto 		= "";
		String asientoContable 	= null;
		Vector vResultado 	    = null;
		Vector vLineasAbonos 	= null;
		Hashtable hash 		    = null; 
		Hashtable laHash 	    = null;
		String select 		    = null; 
		String sql 	            = null; 
		String imp              = null; 
		String importeIva       = null;
		Vector vAsiento2        = new Vector();
		int contador            = 0;             
        Hashtable codigos       = new Hashtable();		
		// Beans
		FacAbonoAdm abonoAdm 	= new FacAbonoAdm(this.usrbean);

		try{
		    // RGG atencion a los importes negativos
			select = " SELECT A.IDABONO, A.NUMEROABONO, A.IDPERSONA, A.FECHA, " + 
			    " (LA.CANTIDAD * LA.PRECIOUNITARIO * -1) IMPNETO, ((LA.CANTIDAD * LA.PRECIOUNITARIO) * (LA.IVA/100) * -1) IMPIVA, LA.IVA, LA.DESCRIPCIONLINEA DESCRIPCION, L.CTAPRODUCTOSERVICIO, L.CTAIVA, " + 
			    " F.NUMEROFACTURA NUMEROFACTURA, DECODE(F.IDPERSONADEUDOR,NULL,F.IDPERSONA,F.IDPERSONADEUDOR) IDPERSONA,  " +
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
			    " AND    A.IDPAGOSJG IS NULL  " ; // pagos no sjcs
			contador++;
			codigos.put(new Integer(contador),this.usrbean.getLocation());
			select+=" AND    A.IDINSTITUCION = :"+contador;

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
			
			
			for(int x=0;x<vLineasAbonos.size();x++){
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
				concepto = UtilidadesString.sustituirParaExcell(UtilidadesString.getMensajeIdioma(this.usrbean,CONCEPTO_ASIENTO2) + ": "+(String)hash.get("NUMEROFACTURA"));
				
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
					
					a.clear();
					UtilidadesHash.set(a, "FECHA", 			UtilidadesHash.getShortDate(hash, "FECHA"));
					UtilidadesHash.set(a, "CONCEPTO", 		concepto);
					UtilidadesHash.set(a, "DOCUMENTO", 		UtilidadesHash.getString(hash, "NUMEROABONO"));
					UtilidadesHash.set(a, "CUENTA", 		asientoIngresos);
					UtilidadesHash.set(a, "DEBE", 			"" + (Double.parseDouble(imp) + Double.parseDouble(importeIva)));
					UtilidadesHash.set(a, "HABER", 			"0");
					UtilidadesHash.set(a, "BASEIMPONIBLE", 	"");
					UtilidadesHash.set(a, "IVA", 			"");
					UtilidadesHash.set(a, "CONTRAPARTIDA", 	asientoClientes);
					pwcontabilidad.write(this.generarLineaAbono(asiento, a));				
					
					a.clear();
					UtilidadesHash.set(a, "FECHA", 			UtilidadesHash.getShortDate(hash, "FECHA"));
					UtilidadesHash.set(a, "CONCEPTO", 		concepto);
					UtilidadesHash.set(a, "DOCUMENTO", 		UtilidadesHash.getString(hash, "NUMEROABONO"));
					UtilidadesHash.set(a, "CUENTA", 		asientoClientes);
					UtilidadesHash.set(a, "DEBE", 			"0");
					UtilidadesHash.set(a, "HABER", 			imp);
					UtilidadesHash.set(a, "BASEIMPONIBLE", 	"");
					UtilidadesHash.set(a, "IVA", 			"");
					UtilidadesHash.set(a, "CONTRAPARTIDA", 	asientoIngresos);
					pwcontabilidad.write(this.generarLineaAbono(asiento, a));

					// Escribimos 3º APUNTE
					if (!ivacero) {
						a.clear();
						UtilidadesHash.set(a, "FECHA", 			UtilidadesHash.getShortDate(hash, "FECHA"));
						UtilidadesHash.set(a, "CONCEPTO", 		concepto);
						UtilidadesHash.set(a, "DOCUMENTO", 		UtilidadesHash.getString(hash, "NUMEROABONO"));
						UtilidadesHash.set(a, "CUENTA", 		asientoClientes);
						UtilidadesHash.set(a, "DEBE", 			"0");
						UtilidadesHash.set(a, "HABER", 			importeIva);
						UtilidadesHash.set(a, "BASEIMPONIBLE", 	imp);
						UtilidadesHash.set(a, "IVA", 			porcentajeIva);
						UtilidadesHash.set(a, "CONTRAPARTIDA", 	asientoIVA);
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
					
					vAsiento2.add(laHash);				
					idAbonoAnt = idAbono;
				}
			}

			
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
			pwcontabilidad.write("Error en asiento2B: " + e.getMessage());
			try { tx.rollback(); } catch (Exception ee) {}
		    throw new ClsExceptions(e,"Error al generar asiento 2B");
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
	// PAGO POR CAJA: ABONO FACTURA RECTIFICATIVA

	private java.io.PrintWriter generaAsiento2A(java.io.PrintWriter pwcontabilidad, String fechaDesde, String fechaHasta, UserTransaction tx) throws SIGAException, ClsExceptions{
	    
	    // RGG 21/11/2007
		String concepto 				= "";
		String asientoContable 			= null;
		String asientoContableCajaAbono ="";
		Vector vAbono 					= null;
		Hashtable hash 					= null; 
		Hashtable laHash 				= null;
		String select 					= null;
		String imp 						= null;
		// Beans
		FacPagoAbonoEfectivoAdm facPagoAbonoEfectivoAdm 	= new FacPagoAbonoEfectivoAdm(this.usrbean);
		
		try{
			select = " SELECT abonoefectivo.idabono idabono, abono.numeroabono numeroabono, abonoefectivo.importe importe,abono.idpersona idpersona, abonoefectivo.fecha fecha, factura.numerofactura numerofactura, " +
			" (SELECT '1' FROM fac_pagosporcaja pc WHERE pc.idinstitucion = abonoefectivo.idinstitucion AND pc.idabono = abonoefectivo.idabono AND pc.idpagoabono = abonoefectivo.idpagoabono) AS compensado " +
			" FROM fac_pagoabonoefectivo abonoefectivo, fac_abono abono, fac_pagosporcaja pagocaja, fac_factura factura "+
			" WHERE abonoefectivo.IDINSTITUCION = "+this.usrbean.getLocation()+" AND abono.idpagosjg IS NULL " +
			" and abono.idinstitucion = factura.idinstitucion and abono.idfactura = factura.idfactura" +
			" AND abonoefectivo.idinstitucion = abono.idinstitucion AND abonoefectivo.idabono = abono.idabono AND abonoefectivo.idinstitucion = pagocaja.idinstitucion "+
			" AND abono.idfactura = pagocaja.idfactura and abonoefectivo.idabono = pagocaja.idabono and abonoefectivo.IDPAGOABONO = pagocaja.IDPAGOABONO " +
			" and (exists (select pagocaja2.IDFACTURA from fac_pagosporcaja pagocaja2 "+
			" where abono.idinstitucion = pagocaja2.idinstitucion and abono.idfactura = pagocaja2.idfactura and pagocaja2.idabono is null) "+
			" or   exists (select disquete.IDFACTURA from FAC_FACTURAINCLUIDAENDISQUETE disquete " +
			" where abono.idinstitucion = disquete.idinstitucion and abono.idfactura = disquete.idfactura and disquete.DEVUELTA like 'N') ) ";
			
			if(!fechaDesde.equals("") && !fechaHasta.equals(""))
				select+=" AND abonoefectivo.FECHA >= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaDesde)+"','YYYY/MM/DD hh24:mi:ss') AND abonoefectivo.FECHA <= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaHasta)+"','YYYY/MM/DD hh24:mi:ss')";
			else if(!fechaDesde.equals(""))
				select+=" AND abonoefectivo.FECHA >= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaDesde)+"','YYYY/MM/DD hh24:mi:ss')";
			else if(!fechaHasta.equals(""))
				select+=" AND abonoefectivo.FECHA <= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaHasta)+"','YYYY/MM/DD hh24:mi:ss')";
			
			vAbono=(Vector)this.selectTabla(select);
			int con=0;
			tx.begin();
			for(int x=0;x<vAbono.size();x++){
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
				concepto = UtilidadesString.sustituirParaExcell(UtilidadesString.getMensajeIdioma(this.usrbean,CONCEPTO_ASIENTO6)+ ": "+(String)hash.get("NUMEROFACTURA"));
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
		catch (Exception e) {
			pwcontabilidad.write("Error en asiento2C: " + e.getMessage());
			try { tx.rollback(); } catch (Exception ee) {}
		    throw new ClsExceptions(e,"Error al generar asiento 2C");
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
	// Tipos de asientos.
	// 7------>
	// Banco (572.1xxx) 		-->  pagoabonobanco	 	--> 0
	// Cliente(430.xxx)			-->  0			 		--> pagoabonobanco	

	private java.io.PrintWriter generaAsiento2B(java.io.PrintWriter pwcontabilidad, String fechaDesde, String fechaHasta, UserTransaction tx) throws SIGAException, ClsExceptions{

		String concepto 			= "";
		String asientoContable 		= null; 
		String asientoContableBanco = null;
		Vector vAbono 				= null;
		Hashtable hash 				= null;
		Hashtable laHash 			= null;
		String select 				= null;
		String imp 					= null;
		// Beans
		FacAbonoIncluidoEnDisqueteAdm facAbonoIncluidoEnDisqueteAdm 	= new FacAbonoIncluidoEnDisqueteAdm(this.usrbean);
		try{
			select= " SELECT abonoincluida.idabono idabono, abono.numeroabono numeroabono, abonoincluida.iddisqueteabono iddisqueteabono, "+
			" abonoincluida.importeabonado importe, disqueteabono.bancos_codigo bancos_codigo, abono.idpersona idpersona, "+
			" disqueteabono.fecha fecha, abono.estado, factura.numerofactura numerofactura "+
			" FROM fac_abonoincluidoendisquete abonoincluida, fac_disqueteabonos disqueteabono, fac_abono abono, fac_factura factura "+
			" WHERE abono.idinstitucion = factura.idinstitucion" +
			" and abono.idfactura = factura.idfactura "+
			" and abono.idinstitucion = abonoincluida.idinstitucion "+
			" AND abono.idabono = abonoincluida.idabono "+
			" AND abonoincluida.idinstitucion = disqueteabono.idinstitucion "+
			" AND abonoincluida.iddisqueteabono = disqueteabono.iddisqueteabono "+
			" AND abono.idpagosjg IS NULL "+
			" and abono.idinstitucion = "+ this.usrbean.getLocation() +
			" and abono.estado = 1 "+
			" and (exists (select pagocaja.IDFACTURA from fac_pagosporcaja pagocaja "+
			" where abono.idinstitucion = pagocaja.idinstitucion and abono.idfactura = pagocaja.idfactura and pagocaja.idabono is null) "+
			" or   exists (select disquete2.IDFACTURA from FAC_FACTURAINCLUIDAENDISQUETE disquete2 " +
			" where abono.idinstitucion = disquete2.idinstitucion and abono.idfactura = disquete2.idfactura and disquete2.DEVUELTA like 'N') ) ";
			if(!fechaDesde.equals("") && !fechaHasta.equals(""))
				select+=" AND disqueteabono.FECHA >= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaDesde)+"','YYYY/MM/DD hh24:mi:ss') AND disqueteabono.FECHA <= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaHasta)+"','YYYY/MM/DD hh24:mi:ss')";
			else if(!fechaDesde.equals(""))
				select+=" AND disqueteabono.FECHA >= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaDesde)+"','YYYY/MM/DD hh24:mi:ss')";
			else if(!fechaHasta.equals(""))
				select+=" AND disqueteabono.FECHA <= TO_DATE('"+GstDate.getApplicationFormatDate("",fechaHasta)+"','YYYY/MM/DD hh24:mi:ss')";
			vAbono=(Vector)this.selectTabla(select);
			int con=0;
			tx.begin();
			for(int x=0;x<vAbono.size();x++){
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
				concepto = UtilidadesString.sustituirParaExcell(UtilidadesString.getMensajeIdioma(this.usrbean,CONCEPTO_ASIENTO7)+ ": "+(String)hash.get("NUMEROFACTURA"));
				// RGG concepto  = obtenerDescripcion(CONCEPTO_ASIENTO7,usr.getLanguage(),usr);
				asiento++;

				Hashtable a = new Hashtable();
				
				// Escribimos 1º asiento
				UtilidadesHash.set(a, "FECHA", 			UtilidadesHash.getShortDate(hash, "FECHA"));
				UtilidadesHash.set(a, "CONCEPTO", 		concepto);
				UtilidadesHash.set(a, "DOCUMENTO", 		UtilidadesHash.getString(hash, "NUMEROABONO"));
				UtilidadesHash.set(a, "CUENTA", 		asientoContable);
				UtilidadesHash.set(a, "DEBE", 			imp);
				UtilidadesHash.set(a, "HABER", 			"0");
				UtilidadesHash.set(a, "BASEIMPONIBLE", 	"");
				UtilidadesHash.set(a, "IVA", 			"");
				UtilidadesHash.set(a, "CONTRAPARTIDA", 	asientoContableBanco);
				pwcontabilidad.write(this.generarLineaAbono(asiento, a));				
				
				// Escribimos 2º asiento
				a.clear();
				UtilidadesHash.set(a, "FECHA", 			UtilidadesHash.getShortDate(hash, "FECHA"));
				UtilidadesHash.set(a, "CONCEPTO", 		concepto);
				UtilidadesHash.set(a, "DOCUMENTO", 		UtilidadesHash.getString(hash, "NUMEROABONO"));
				UtilidadesHash.set(a, "CUENTA", 		asientoContableBanco);
				UtilidadesHash.set(a, "DEBE", 			"0");
				UtilidadesHash.set(a, "HABER", 			imp);
				UtilidadesHash.set(a, "BASEIMPONIBLE", 	"");
				UtilidadesHash.set(a, "IVA", 			"");
				UtilidadesHash.set(a, "CONTRAPARTIDA", 	asientoContable);
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
		catch (Exception e) {
			pwcontabilidad.write("Error en asiento2D: " + e.getMessage());
			try { tx.rollback(); } catch (Exception ee) {}
		    throw new ClsExceptions(e,"Error al generar asiento 2D");
		}
	}	
	
	/*                                            FIN fACTURAS                                                                              */	
	
	/**
	 * 
	 * @param pwcontabilidad
	 * @param fechaDesde
	 * @param fechaHasta
	 * @param usr
	 * @return
	 * @throws SIGAException
	 */
	
	/*                                            INICIO PAGO POR CAJA                                                                        */
	
	private java.io.PrintWriter generaAsiento3(java.io.PrintWriter pwcontabilidad, String fechaDesde, String fechaHasta, UserTransaction tx) throws SIGAException, ClsExceptions{
		
	    // RGG 21/11/2007
	    String concepto 		        = "";
	    String conceptoCompensado 		= "";
		String asientoContable 	        = null;
		Vector vPagoCaja                = null;
		Hashtable hash 		            = null; 
		Hashtable laHash 	            = null;
		String select 		            = null;  
		String imp                      = null;
		Vector vAsiento3                = new Vector();
		int contador                    = 0;             
        Hashtable codigos               = new Hashtable();		
		// Beans
		FacPagosPorCajaAdm pagosPorCajaAdm 	= new FacPagosPorCajaAdm(this.usrbean);

		try{

			// pagos por caja sobre la factura, obteniendoo el idapunte para comprobar si la factura ha sido compensada o no
			select= "  SELECT '0' AS ANTICIPO, A.IDFACTURA IDFACTURA, B.NUMEROFACTURA, A.TARJETA TARJETA, P.CONFDEUDOR, P.CTACLIENTES, A.IMPORTE IMPORTE, A.TIPOAPUNTE TIPOAPUNTE, " +
		    " DECODE(B.IDPERSONADEUDOR, NULL, B.IDPERSONA, B.IDPERSONADEUDOR) IDPERSONA, " +
		    " A.FECHA FECHA " +
		    " FROM FAC_PAGOSPORCAJA A, FAC_FACTURA B, FAC_FACTURACIONPROGRAMADA P " +
		    " WHERE B.IDINSTITUCION  = A.IDINSTITUCION  " +
		    " AND B.IDFACTURA  = A.IDFACTURA  " +
		    " AND B.IDINSTITUCION = P.IDINSTITUCION " +
		    " AND B.IDSERIEFACTURACION = P.IDSERIEFACTURACION " +
		    " AND B.IDPROGRAMACION = P.IDPROGRAMACION " +
		    " AND B.NUMEROFACTURA IS NOT NULL " +
			" and a.idabono is null " ;
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

			select += "    ORDER BY IDFACTURA ";
			
			String idFactura ="";
			String idFacturaAnt ="";
			
			conceptoCompensado = UtilidadesString.sustituirParaExcell(UtilidadesString.getMensajeIdioma(this.usrbean,CONCEPTO_ASIENTO3_2010));	
			concepto = UtilidadesString.sustituirParaExcell(UtilidadesString.getMensajeIdioma(this.usrbean,CONCEPTO_ASIENTO3));
			
			vPagoCaja=(Vector)this.selectTablaBind(select,codigos);
			for(int x=0;x<vPagoCaja.size();x++){
				asiento++;
				
				hash = (Hashtable) vPagoCaja.get(x);
				idFactura = (String)hash.get("IDFACTURA");
				
				imp = UtilidadesNumero.redondea((String)hash.get("IMPORTE"),2);
				String confClientes = (String)hash.get("CONFDEUDOR");
				String ctaClientes = (String)hash.get("CTACLIENTES");
				String tipoApunte = (String)hash.get("TIPOAPUNTE");
				
				// Con el IDPERSONA, obtenemos de CEN_CLIENTE, el asiento contable.
				String asientoCliente= obtenerAsientoContable(this.usrbean.getLocation(),(String)hash.get("IDPERSONA"));
				if (confClientes.equals("F")) {
				    asientoContable =  ctaClientes;
				} else {
				    asientoContable =  ctaClientes + asientoCliente;
				}
				String asientoCompensacionCliente = CONTABILIDAD_COMPENSACION + asientoCliente;
				
				Hashtable a = new Hashtable();
				
				if (tipoApunte!=null && tipoApunte.trim().equals("C")) {
					// PAGO COMPENSADO
				    // Escribimos 1º apunte
					a.clear();
					UtilidadesHash.set(a, "FECHA", 			UtilidadesHash.getShortDate(hash, "FECHA"));
					UtilidadesHash.set(a, "CONCEPTO", 		conceptoCompensado);
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
					UtilidadesHash.set(a, "CONCEPTO", 		conceptoCompensado);
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
					laHash = new Hashtable();
					laHash.put("CONTABILIZADO",ClsConstants.FACTURA_ABONO_CONTABILIZADA);
					laHash.put("IDINSTITUCION",this.usrbean.getLocation());
					laHash.put("IDFACTURA",hash.get("IDFACTURA"));
					laHash.put("USUMODIFICACION",this.usrbean.getUserName());
					laHash.put("FECHAMODIFICACION","sysdate");
					
					vAsiento3.add(laHash);
					idFacturaAnt = idFactura;
				}
			}
			
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
		catch (Exception e) {
			pwcontabilidad.write("Error en asiento3: " + e.getMessage());
			try { tx.rollback(); } catch (Exception ee) {}
		    throw new ClsExceptions(e,"Error al generar asiento 3");
		}
	}

	/*                                            FIN PAGO POR CAJA                                                                        */	
	
	/**
	 * 
	 * @param pwcontabilidad
	 * @param fechaDesde
	 * @param fechaHasta
	 * @param usr
	 * @return
	 * @throws SIGAException
	 */
	
	/*                                            INICIO PAGO POR TARJETA                                                                        */
	
	private java.io.PrintWriter generaAsiento5 (java.io.PrintWriter pwcontabilidad, String fechaDesde, String fechaHasta, UserTransaction tx) throws SIGAException, ClsExceptions
	{

	    String concepto 		= "";
		String conceptoT 		= "";
		String asientoContable 	= null;
		// Variables
		Vector vPagoTarjeta     = null;
		Hashtable hash 		    = null; 
		Hashtable laHash 	    = null;
		String select 		    = null; 
		String imp              = null;
		Vector vAsiento5        = new Vector();
		int contador            = 0;             
        Hashtable codigos       = new Hashtable();		
		// Beans
		FacPagosPorCajaAdm pagosPorCajaAdm 	= new FacPagosPorCajaAdm(this.usrbean);

		try{
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
			select+=" AND A.IDINSTITUCION = :"+ contador;
			
			contador++;
			codigos.put(new Integer(contador),"S");
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
   
				select += "    ORDER BY IDFACTURA ";

			vPagoTarjeta=(Vector)this.selectTablaBind(select,codigos);
			
			String idFactura ="";
			String idFacturaAnt ="";

			// Descripcion del concepto
			conceptoT = UtilidadesString.sustituirParaExcell(UtilidadesString.getMensajeIdioma(this.usrbean,CONCEPTO_ASIENTO10));


			for(int x=0;x<vPagoTarjeta.size();x++){
			    
				asiento++;

			    hash = (Hashtable) vPagoTarjeta.get(x);
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
					laHash = new Hashtable();
					laHash.put("CONTABILIZADO",ClsConstants.FACTURA_ABONO_CONTABILIZADA);
					laHash.put("IDINSTITUCION",this.usrbean.getLocation());
					laHash.put("IDFACTURA",hash.get("IDFACTURA"));
					laHash.put("USUMODIFICACION",this.usrbean.getUserName());
					laHash.put("FECHAMODIFICACION","sysdate");
					
					vAsiento5.add(laHash);
					idFacturaAnt = idFactura;
					
				}
			}

			if(vAsiento5.size()>0){
				String contabilizada=(String)((Hashtable)vAsiento5.get(0)).get("CONTABILIZADO");
				String usuModificacion=(String)((Hashtable)vAsiento5.get(0)).get("USUMODIFICACION");
				String fechaModificacion=(String)((Hashtable)vAsiento5.get(0)).get("FECHAMODIFICACION");

				String sqlUpdate="UPDATE "+FacPagosPorCajaBean.T_NOMBRETABLA+" SET "+FacPagosPorCajaBean.C_CONTABILIZADO+"='"+contabilizada+"', "+FacPagosPorCajaBean.C_USUMODIFICACION+"="+usuModificacion+", "+FacPagosPorCajaBean.C_FECHAMODIFICACION+"="+fechaModificacion;
				int con = 0;
				tx.begin();
				for(int l = 0; l < vAsiento5.size(); l++){
				    con++;
				    if (con%numeroTransaccion==0) {
				        tx.commit();
				        tx.begin();
				    }
					String claves = (String)((Hashtable)vAsiento5.get(l)).get("IDFACTURA");
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
			pwcontabilidad.write("Error en asiento5: " + e.getMessage());
			try { tx.rollback(); } catch (Exception ee) {}
		    throw new ClsExceptions(e,"Error al generar asiento 5");
		}
	}
	
	/*                                            FIN PAGO POR TARJETA                                                                        */	
	
	/**
	 * 
	 * @param pwcontabilidad
	 * @param fechaDesde
	 * @param fechaHasta
	 * @param usr
	 * @return
	 * @throws SIGAException
	 */
	
	/*                                            INICIO PAGO POR BANCO                                                                        */
	
	private java.io.PrintWriter generaAsiento4(java.io.PrintWriter pwcontabilidad, String fechaDesde, String fechaHasta, UserTransaction tx) throws SIGAException, ClsExceptions{
	    // RGG 21/11/2007
	    String concepto 		    = "";
		String asientoContable 	    = null; 
		String asientoContableBanco = null;
		Vector vPagoBanco           = null;
		Hashtable hash 		        = null; 
		Hashtable laHash 	        = null;
		String select 		        = null; 
		String imp                  = null;
		Vector vAsiento4            = new Vector();
		int contador                = 0;             
        Hashtable codigos           = new Hashtable();
		// Beans
		FacFacturaIncluidaEnDisqueteAdm facturaIncludidaEnDisqueteAdm 	= new FacFacturaIncluidaEnDisqueteAdm(this.usrbean);

		try{
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

			select += " ORDER BY A.IDFACTURA ";
			
			vPagoBanco=(Vector)this.selectTablaBind(select,codigos);

			String idFactura ="";
			String idFacturaAnt ="";

			// Descripcion del concepto
			concepto = UtilidadesString.sustituirParaExcell(UtilidadesString.getMensajeIdioma(this.usrbean,CONCEPTO_ASIENTO4));
			
			for(int x=0;x<vPagoBanco.size();x++){
				hash = (Hashtable) vPagoBanco.get(x);

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
					laHash = new Hashtable();
					laHash.put("CONTABILIZADA",ClsConstants.FACTURA_ABONO_CONTABILIZADA);
					laHash.put("IDINSTITUCION",this.usrbean.getLocation());
					laHash.put("IDFACTURA",hash.get("IDFACTURA"));
					laHash.put("USUMODIFICACION",this.usrbean.getUserName());
					laHash.put("FECHAMODIFICACION","sysdate");
					
					vAsiento4.add(laHash);
					idFacturaAnt = idFactura;
				}
			}
			
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

	/*                                            FIN PAGO POR BANCO                                                                        */	
	
	/**
	 * 
	 * @param pwcontabilidad
	 * @param fechaDesde
	 * @param fechaHasta
	 * @param usr
	 * @return
	 * @throws SIGAException
	 */
	
	/*                                            INICIO DEVOLUCIONES                                                                        */
	
	private java.io.PrintWriter generaAsiento6(java.io.PrintWriter pwcontabilidad, String fechaDesde, String fechaHasta, UserTransaction tx) throws SIGAException, ClsExceptions{

	    // RGG 21/11/2007
		String concepto 		    = "";
		String conceptoComision     = "";
		String asientoContable 	    = null; 
		String asientoContableBanco = null; 
		String gastosDevolucion     = null; 
		String cargarCliente 		= null;
		Vector vDevolucion 			= null;
		Hashtable hash 				= null;
		Hashtable laHash 			= null;
		String select 				= null; 
		String imp 					= null;
		Vector vAsiento6			= new Vector();
		int contador 				= 0;             
        Hashtable codigos 			= new Hashtable();		
		// Beans
		FacLineaDevoluDisqBancoAdm lineaDevoludisqbancoAdm = new FacLineaDevoluDisqBancoAdm(this.usrbean);

		try {
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
			
			vDevolucion=(Vector)this.selectTablaBind(select,codigos);
			
			String idDisquete ="";
			String idDisqueteAnt ="";

			// Descripcion del concepto
			concepto = UtilidadesString.sustituirParaExcell(UtilidadesString.getMensajeIdioma(this.usrbean,CONCEPTO_ASIENTO5));
			
			for(int x=0;x<vDevolucion.size();x++){
				asiento++;

				hash = (Hashtable) vDevolucion.get(x);

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
				
				
		/*		if (gastosDevolucion!=null && !gastosDevolucion.trim().equals("")) {
					// tiene gastos
					
					// Los paga la insitucion SIEMPRE
				    gastosDevolucion = UtilidadesNumero.redondea(gastosDevolucion,2);
				    
					// Escribimos 3º asiento
					a.clear();
					UtilidadesHash.set(a, "FECHA", 			UtilidadesHash.getShortDate(hash, "FECHAGENERACION"));
					UtilidadesHash.set(a, "CONCEPTO", 		conceptoComision);
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
					UtilidadesHash.set(a, "CONCEPTO", 		conceptoComision);
					UtilidadesHash.set(a, "DOCUMENTO", 		UtilidadesHash.getString(hash, "NUMEROFACTURA"));
					UtilidadesHash.set(a, "CUENTA", 		asientoContableBanco);
					UtilidadesHash.set(a, "DEBE", 			"0");
					UtilidadesHash.set(a, "HABER", 			gastosDevolucion);
					UtilidadesHash.set(a, "BASEIMPONIBLE", 	"");
					UtilidadesHash.set(a, "IVA", 			"");
					UtilidadesHash.set(a, "CONTRAPARTIDA", 	CONTABILIDAD_GASTOSBANCARIOS);
					pwcontabilidad.write(this.generarLineaAbono(asiento, a));
					
				}*/  //No se Registra contablemente hablando
				
				///////////////////////////////////////////////////////
//				
				if (!idDisqueteAnt.equals(idDisquete)) {
					laHash = new Hashtable();
					laHash.put("CONTABILIZADA",ClsConstants.FACTURA_ABONO_CONTABILIZADA);
					laHash.put("IDINSTITUCION",this.usrbean.getLocation());
					laHash.put("IDDISQUETEDEVOLUCIONES",hash.get("IDDISQUETEDEVOLUCIONES"));
					laHash.put("USUMODIFICACION",this.usrbean.getUserName());
					laHash.put("FECHAMODIFICACION","sysdate");
					
					vAsiento6.add(laHash);
					idDisqueteAnt = idDisquete;
				}
			}
			
			if(vAsiento6.size()>0){
				String contabilizada=(String)((Hashtable)vAsiento6.get(0)).get("CONTABILIZADA");
				String usuModificacion=(String)((Hashtable)vAsiento6.get(0)).get("USUMODIFICACION");
				String fechaModificacion=(String)((Hashtable)vAsiento6.get(0)).get("FECHAMODIFICACION");
				
				String sqlUpdate="UPDATE "+FacLineaDevoluDisqBancoBean.T_NOMBRETABLA+" SET "+FacLineaDevoluDisqBancoBean.C_CONTABILIZADA+"='"+contabilizada+"', "+FacLineaDevoluDisqBancoBean.C_USUMODIFICACION+"="+usuModificacion+", "+FacLineaDevoluDisqBancoBean.C_FECHAMODIFICACION+"="+fechaModificacion;
				int con = 0;
				tx.begin();
				for(int l = 0; l < vAsiento6.size(); l++){
				    con++;
				    if (con%numeroTransaccion==0) {
				        tx.commit();
				        tx.begin();
				    }
					String claves = (String)((Hashtable)vAsiento6.get(l)).get("IDDISQUETEDEVOLUCIONES");
					Hashtable codigosUpdate = new Hashtable();
					codigosUpdate.put(new Integer(1),this.usrbean.getLocation());
					codigosUpdate.put(new Integer(2),claves);
					lineaDevoludisqbancoAdm.insertSQLBind(sqlUpdate +" WHERE IDINSTITUCION=:1 AND IDDISQUETEDEVOLUCIONES=:2",codigosUpdate);
				}
				tx.commit();
				
			}
			
			return pwcontabilidad;
		}
		catch (Exception e) {
			pwcontabilidad.write("Error en asiento6: " + e.getMessage());
			try { tx.rollback(); } catch (Exception ee) {}
		    throw new ClsExceptions(e,"Error al generar asiento 6");
		}
	}
	
	/*                                            FIN DEVOLUCIONES                                                                        */	

	/*                                            INICIO ANTICIPOS                                                                         */	
	
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
	private java.io.PrintWriter generaAsiento7(java.io.PrintWriter pwcontabilidad, String fechaDesde, String fechaHasta, UserTransaction tx) throws SIGAException, ClsExceptions{
	    String concepto 		            = "";
		String asientoContable 	            = null; 
		String asientoContableCajaAnticipos = null;
		Vector vAnticipo 					= null;
		Hashtable hash 						= null; 
		Hashtable laHash 					= null;
		String select 						= null; 
		String imp 							= null;
		Vector vAsiento7 					= new Vector();
		int contador 						= 0;             
        Hashtable codigos 					= new Hashtable();		
		// Beans
		PysAnticipoLetradoAdm anticipoLetradoAdm 	= new PysAnticipoLetradoAdm(this.usrbean);

		try{
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
					
					vAsiento7.add(laHash);
					idAnticipoAnt = idAnticipo;
				}
			}
			
			if(vAsiento7.size()>0){
				String contabilizada=(String)((Hashtable)vAsiento7.get(0)).get("CONTABILIZADA");
				String usuModificacion=(String)((Hashtable)vAsiento7.get(0)).get("USUMODIFICACION");
				String fechaModificacion=(String)((Hashtable)vAsiento7.get(0)).get("FECHAMODIFICACION");
				
				String sqlUpdate="UPDATE "+PysAnticipoLetradoBean.T_NOMBRETABLA+" SET "+PysAnticipoLetradoBean.C_CONTABILIZADO+"='"+contabilizada+"', "+FacFacturaIncluidaEnDisqueteBean.C_USUMODIFICACION+"="+usuModificacion+", "+FacFacturaIncluidaEnDisqueteBean.C_FECHAMODIFICACION+"="+fechaModificacion;
				int con = 0;
				tx.begin();
				for(int l = 0; l < vAsiento7.size(); l++){
				    con++;
				    if (con%numeroTransaccion==0) {
				        tx.commit();
				        tx.begin();
				    }
					String claves = (String)((Hashtable)vAsiento7.get(l)).get("IDPERSONA");
					String claves2 = (String)((Hashtable)vAsiento7.get(l)).get("IDANTICIPO");
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
			pwcontabilidad.write("Error en asiento7: " + e.getMessage());
			try { tx.rollback(); } catch (Exception ee) {}
		    throw new ClsExceptions(e,"Error al generar asiento 7");
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
	private java.io.PrintWriter generaAsiento7A(java.io.PrintWriter pwcontabilidad, String fechaDesde, String fechaHasta, UserTransaction tx) throws SIGAException, ClsExceptions
	{
	    String concepto 		            = "";
		String asientoContableIngresosExtra = null;
		Vector vAnticipo 					= null;
		Hashtable hash 						= null; 
		Hashtable laHash 					= null;
		String select 						= null; 
		String imp 							= null;
		int contador 						= 0;             
        Hashtable codigos 					= new Hashtable();		

		// Beans

		try{
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
			for(int x=0;x<vAnticipo.size();x++){
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
		catch (Exception e) {
			pwcontabilidad.write("Error en asiento7A: " + e.getMessage());
			try { tx.rollback(); } catch (Exception ee) {}
		    throw new ClsExceptions(e,"Error al generar asiento 7A");
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
	// ANTICIPOS DE PRODUCTOS Y SERVICIOS - PAGOS POR CAJA
	private java.io.PrintWriter generaAsiento7B(java.io.PrintWriter pwcontabilidad, String fechaDesde, String fechaHasta, UserTransaction tx) throws SIGAException, ClsExceptions {
	
	    String concepto 		        = "";
	    String conceptoAnticipo 		= "";
		String asientoContable 	        = null;
		Vector vAnticipo                = null;
		Hashtable hash 		            = null; 
		Hashtable laHash 	            = null;
		String select 		            = null; 
		String imp                      = null;
		Vector vAsiento7B               = new Vector();
		int contador                    = 0;             
	    Hashtable codigos               = new Hashtable();		
		// Beans
		FacPagosPorCajaAdm pagosPorCajaAdm 	= new FacPagosPorCajaAdm(this.usrbean);

		try {
	
	//ANTICIPADO PRODUCTOS
			select =  " select '1' AS ANTICIPO, B.IDFACTURA IDFACTURA , B.NUMEROFACTURA, 'S' TARJETA, P.CONFDEUDOR, P.CTACLIENTES, L.IMPORTEANTICIPADO IMPORTE, " +
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
			"    AND L.NUMEROLINEA=C.NUMEROLINEA " ;
		    contador++;
			codigos.put(new Integer(contador),this.usrbean.getLocation());
			select +="    AND B.idinstitucion=:" + contador +
					"     AND l.importeanticipado <>0 " +
					"     AND B.NUMEROFACTURA IS NOT NULL ";
		     String fDesdeInc = fechaDesde; 
			 String fHastaInc = fechaHasta;
				if ((fDesdeInc != null && !fDesdeInc.trim().equals("")) || (fHastaInc != null && !fHastaInc.trim().equals(""))) {
					
					if (!fDesdeInc.equals(""))
						fDesdeInc = GstDate.getApplicationFormatDate("", fDesdeInc); 
					if (!fHastaInc.equals(""))
						fHastaInc = GstDate.getApplicationFormatDate("", fHastaInc);
					Vector vCondicion=GstDate.dateBetweenDesdeAndHastaBind("B.FECHAEMISION", fDesdeInc, fHastaInc,contador,codigos);
					
					contador=new Integer(vCondicion.get(0).toString()).intValue();
					select +=" AND " + vCondicion.get(1) ;
			}		
			select += "  UNION  " +
			
	//ANTICIPADO SERVICIOS
			"  select '1' AS ANTICIPO, B.IDFACTURA IDFACTURA, B.NUMEROFACTURA, 'S' TARJETA, P.CONFDEUDOR, P.CTACLIENTES, L.IMPORTEANTICIPADO IMPORTE,  " +
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
			codigos.put(new Integer(contador),this.usrbean.getLocation());
			select +="    AND B.idinstitucion=:"+ contador +
					 "    AND l.importeanticipado <>0 " +
			         "    AND B.NUMEROFACTURA IS NOT NULL ";
	
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
		
			concepto = UtilidadesString.sustituirParaExcell(UtilidadesString.getMensajeIdioma(this.usrbean,CONCEPTO_ASIENTO3));
			conceptoAnticipo = UtilidadesString.sustituirParaExcell(UtilidadesString.getMensajeIdioma(this.usrbean,CONCEPTO_ASIENTO3_2));
			
			vAnticipo=(Vector)this.selectTablaBind(select,codigos);
			for(int x=0;x<vAnticipo.size();x++)
			{
				
				asiento++;
				
				hash = (Hashtable) vAnticipo.get(x);
				idFactura = (String)hash.get("IDFACTURA");
				
				imp = UtilidadesNumero.redondea((String)hash.get("IMPORTE"),2);
				String confClientes = (String)hash.get("CONFDEUDOR");
				String ctaClientes = (String)hash.get("CTACLIENTES");
				String anticipo = (String)hash.get("ANTICIPO");
				
				// Con el IDPERSONA, obtenemos de CEN_CLIENTE, el asiento contable.
				String asientoCliente= obtenerAsientoContable(this.usrbean.getLocation(),(String)hash.get("IDPERSONA"));
				if (confClientes.equals("F")) {
				    asientoContable =  ctaClientes;
				} else {
				    asientoContable =  ctaClientes + asientoCliente;
				}
				String asientoAnticiposCliente = ANTICIPOS_CLI + asientoCliente;
				
				Hashtable a = new Hashtable();
				
				

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
	
				//////////////////////////////////////////////////////
				
				if (!idFactura.equals(idFacturaAnt)) {
				    // Solamente cuando cambia idfactura
					laHash = new Hashtable();
					laHash.put("CONTABILIZADO",ClsConstants.FACTURA_ABONO_CONTABILIZADA);
					laHash.put("IDINSTITUCION",this.usrbean.getLocation());
					laHash.put("IDFACTURA",hash.get("IDFACTURA"));
					laHash.put("USUMODIFICACION",this.usrbean.getUserName());
					laHash.put("FECHAMODIFICACION","sysdate");
					
					vAsiento7B.add(laHash);
					idFacturaAnt = idFactura;
				}
			}
			
			if(vAsiento7B.size()>0){
				String contabilizada=(String)((Hashtable)vAsiento7B.get(0)).get("CONTABILIZADO");
				String usuModificacion=(String)((Hashtable)vAsiento7B.get(0)).get("USUMODIFICACION");
				String fechaModificacion=(String)((Hashtable)vAsiento7B.get(0)).get("FECHAMODIFICACION");
				
				String sqlUpdate="UPDATE "+FacPagosPorCajaBean.T_NOMBRETABLA+" SET "+FacPagosPorCajaBean.C_CONTABILIZADO+"='"+contabilizada+"', "+FacPagosPorCajaBean.C_USUMODIFICACION+"="+usuModificacion+", "+FacPagosPorCajaBean.C_FECHAMODIFICACION+"="+fechaModificacion;
				int con = 0;
				tx.begin();
				for(int l = 0; l < vAsiento7B.size(); l++){
				    con++;
				    if (con%numeroTransaccion==0) {
				        tx.commit();
				        tx.begin();
				    }
					String claves = (String)((Hashtable)vAsiento7B.get(l)).get("IDFACTURA");
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
			pwcontabilidad.write("Error en asiento7B: " + e.getMessage());
			try { tx.rollback(); } catch (Exception ee) {}
		    throw new ClsExceptions(e,"Error al generar asiento 7B");
		}
	}

	/*                                            FIN ANTICIPOS                                                                         */	
	
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
			(UtilidadesHash.getString(datos, "DOCUMENTO") != null ?  UtilidadesHash.getString(datos, "DOCUMENTO"):"") 
			+ SEPARADOR +
			
			// Debe
			importeDebe.toString().replace('.', ',')
			+ SEPARADOR +
			
			// Haber
			importeHaber.toString().replace('.', ',')
			+ SEPARADOR +
			
			// Base Imponible
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
			CONTABILIDAD_IVA = genParametros.getValor(idInstitucion.toString(),"FAC","CONTABILIDAD_IVA",null);
			CONTABILIDAD_TARJETAS = genParametros.getValor(idInstitucion.toString(),"FAC","CONTABILIDAD_TARJETAS",null);
			CONTABILIDAD_CAJA = genParametros.getValor(idInstitucion.toString(),"FAC","CONTABILIDAD_CAJA",null);
			CONTABILIDAD_COMPENSACION = genParametros.getValor(idInstitucion.toString(),"FAC","CONTAB_COMPENSACIONES",null);
			CONTABILIDAD_CAJA_ANTICIPOS = genParametros.getValor(idInstitucion.toString(),"FAC","CONTAB_ANTICIPOS_CAJA",null);
			CONTABILIDAD_INGRESOS_EXTRA = genParametros.getValor(idInstitucion.toString(),"FAC","CONTAB_INGRESOS_EXTRA",null);
			ANTICIPOS_CLI = genParametros.getValor(idInstitucion.toString(),"FAC","CONTAB_ANTICIPOS_CLI",null);
			CONTABILIDAD_GASTOSBANCARIOS = genParametros.getValor(idInstitucion.toString(),"FAC","CONTAB_GASTOS_BANCARIOS",null);
			// RGG 06/11/2006
			CONTAB_CAJA_ABONO = genParametros.getValor(idInstitucion.toString(),"FAC","CONTAB_CAJA_ABONO",null);
		}
		catch(Exception e)
		{
			throw new ClsExceptions(e,"Error creando cuentas");
		}
	}
	
	
	
	
}
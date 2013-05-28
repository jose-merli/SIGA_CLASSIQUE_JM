package com.siga.beans;

import java.io.File;
import java.io.IOException;
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
import com.siga.Utilidades.UtilidadesString;
import com.siga.general.SIGAException;

public class FcsRegistroFichContaAdm extends MasterBeanAdministrador{

	private int asiento = 0;
	private int numeroTransaccion = 200;
	private String SEPARADOR = "\t";	 
	
	private String CONTABILIDAD_IVA 		= "";
	private String CONTABILIDAD_CAJA 		= "";
	
	private String CONTAB_SERVICIOS_PROFESIONALES	= "";
	private String CONTAB_MOVIMIENTOS_VARIOS	= "";
	private String CONTAB_MOV_VARIOS_NEGATIVOS	= "";
	private String CONTAB_IRPF	= "";
	private String CONTAB_RETENCIONESJUDICIALES	= "";

	private String CONCEPTO_ASIENTO8		= "general.literal.asiento8";       // Reparto Pagos SJCS
	private String CONCEPTO_ASIENTO9		= "general.literal.asiento9";       // Abono SJCS por banco

	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public FcsRegistroFichContaAdm (UsrBean usuario) {
		super( FcsRegistroFichContaBean.T_NOMBRETABLA, usuario);
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
			


/* RGG 29/11/2007 SE QUITAN HASTA QUE SE HAGAN BIEN Y SE PONGAN EN SU MENU NUEVO*/

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
			CONTABILIDAD_CAJA = genParametros.getValor(idInstitucion.toString(),"FAC","CONTABILIDAD_CAJA",null);

			// RGG 06/11/2006
			CONTAB_SERVICIOS_PROFESIONALES = genParametros.getValor(idInstitucion.toString(),"FAC","CONTAB_SERVICIOS_PROFESIONALES",null);
			CONTAB_MOVIMIENTOS_VARIOS = genParametros.getValor(idInstitucion.toString(),"FAC","CONTAB_MOVIMIENTOS_VARIOS",null);
			CONTAB_MOV_VARIOS_NEGATIVOS = genParametros.getValor(idInstitucion.toString(),"FAC","CONTAB_MOV_VARIOS_NEGATIVOS",null);
			CONTAB_RETENCIONESJUDICIALES = genParametros.getValor(idInstitucion.toString(),"FAC","CONTAB_RETENCIONESJUDICIALES",null);
			CONTAB_IRPF = genParametros.getValor(idInstitucion.toString(),"FAC","CONTAB_IRPF",null);

		}
		catch(Exception e)
		{
			throw new ClsExceptions(e,"Error creando cuentas");
		}
	}	
}

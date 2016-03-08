//VERSIONES:
//Creacion: david.sanchezp 18-03-2005
//jose.barrientos 28-02-2009: A�adidos los campos banco_codigo y concepto

package com.siga.beans;


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.PaginadorCaseSensitiveBind;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.general.CenVisibilidad;
import com.siga.general.SIGAException;
import com.siga.informes.form.MantenimientoInformesForm;

/**
* Administrador de Pagos de Justicia Gratuita de la tabla FCS_PAGOSJG
* 
* @author david.sanchezp
*/
public class FcsPagosJGAdm extends MasterBeanAdministrador {

	
	public FcsPagosJGAdm(UsrBean usuario) {
		super(FcsPagosJGBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean() {
		String [] campos = {FcsPagosJGBean.C_IDINSTITUCION, 	FcsPagosJGBean.C_IDPAGOSJG,				
							FcsPagosJGBean.C_IDFACTURACION, 	FcsPagosJGBean.C_NOMBRE,
							FcsPagosJGBean.C_ABREVIATURA, 		FcsPagosJGBean.C_FECHADESDE,
							FcsPagosJGBean.C_FECHAHASTA, 		FcsPagosJGBean.C_CRITERIOPAGOTURNO,
							FcsPagosJGBean.C_IMPORTEPAGADO,		FcsPagosJGBean.C_IMPORTEREPARTIR,
							FcsPagosJGBean.C_IMPORTEEJG,		FcsPagosJGBean.C_IMPORTEGUARDIA,
							FcsPagosJGBean.C_IMPORTEMINIMO,		FcsPagosJGBean.C_IMPORTEOFICIO,
							FcsPagosJGBean.C_IMPORTESOJ,		FcsPagosJGBean.C_CONTABILIZADO,
							FcsPagosJGBean.C_IDPROPOTROS,		FcsPagosJGBean.C_IDPROPSEPA,
							FcsPagosJGBean.C_FECHAMODIFICACION, FcsPagosJGBean.C_USUMODIFICACION,
							FcsPagosJGBean.C_IDSUFIJO, 		    FcsPagosJGBean.C_BANCOS_CODIGO};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {FcsPagosJGBean.C_IDINSTITUCION, FcsPagosJGBean.C_IDPAGOSJG};
		return claves;
	}

	protected String[] getOrdenCampos() {
		return getClavesBean();
	}

	//aalg: INC_06787_SIGA. Para hacer la consulta con nvl en los importes
	protected String[] getCamposBeanNVL() {
		String [] campos = {FcsPagosJGBean.C_IDINSTITUCION, 	FcsPagosJGBean.C_IDPAGOSJG,				
							FcsPagosJGBean.C_IDFACTURACION, 	FcsPagosJGBean.C_NOMBRE,
							FcsPagosJGBean.C_ABREVIATURA, 		FcsPagosJGBean.C_FECHADESDE,
							FcsPagosJGBean.C_FECHAHASTA, 		FcsPagosJGBean.C_CRITERIOPAGOTURNO,
							"nvl(" + FcsPagosJGBean.C_IMPORTEPAGADO + ", 0) "+ FcsPagosJGBean.C_IMPORTEPAGADO,	
							"nvl(" + FcsPagosJGBean.C_IMPORTEREPARTIR+ ", 0 )"+ FcsPagosJGBean.C_IMPORTEREPARTIR,
							"nvl(" +FcsPagosJGBean.C_IMPORTEEJG+ ", 0) "+ FcsPagosJGBean.C_IMPORTEEJG,	
							"nvl(" + FcsPagosJGBean.C_IMPORTEGUARDIA+ ", 0) "+ FcsPagosJGBean.C_IMPORTEGUARDIA,
							"nvl(" +FcsPagosJGBean.C_IMPORTEMINIMO+ ", 0) "+ FcsPagosJGBean.C_IMPORTEMINIMO,	
							"nvl(" + FcsPagosJGBean.C_IMPORTEOFICIO+ ", 0 )"+ FcsPagosJGBean.C_IMPORTEOFICIO,
							"nvl(" +FcsPagosJGBean.C_IMPORTESOJ+ ", 0) "+ FcsPagosJGBean.C_IMPORTESOJ,		
							FcsPagosJGBean.C_CONTABILIZADO,
							FcsPagosJGBean.C_BANCOS_CODIGO,		
							"nvl(" +FcsPagosJGBean.C_IDPROPOTROS+ ", 0) " + FcsPagosJGBean.C_IDPROPOTROS,		
							"nvl(" +FcsPagosJGBean.C_IDPROPSEPA+ ", 0) " + FcsPagosJGBean.C_IDPROPSEPA,		
							"nvl(" +FcsPagosJGBean.C_IDSUFIJO+ ", 0) " + FcsPagosJGBean.C_IDSUFIJO,	
							FcsPagosJGBean.C_FECHAMODIFICACION, FcsPagosJGBean.C_USUMODIFICACION};
		return campos;
	}
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		FcsPagosJGBean bean = null;
		
		try {
			bean = new FcsPagosJGBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,FcsPagosJGBean.C_IDINSTITUCION));
			bean.setIdPagosJG(UtilidadesHash.getInteger(hash,FcsPagosJGBean.C_IDPAGOSJG));
			bean.setIdFacturacion(UtilidadesHash.getInteger(hash,FcsPagosJGBean.C_IDFACTURACION));
			bean.setNombre(UtilidadesHash.getString(hash,FcsPagosJGBean.C_NOMBRE));
			bean.setAbreviatura(UtilidadesHash.getString(hash,FcsPagosJGBean.C_ABREVIATURA));
			bean.setFechaDesde(UtilidadesHash.getString(hash,FcsPagosJGBean.C_FECHADESDE));
			bean.setFechaHasta(UtilidadesHash.getString(hash,FcsPagosJGBean.C_FECHAHASTA));
			bean.setCriterioPagoTurno(UtilidadesHash.getString(hash,FcsPagosJGBean.C_CRITERIOPAGOTURNO));
			bean.setImporteRepartir(UtilidadesHash.getDouble(hash,FcsPagosJGBean.C_IMPORTEREPARTIR));
			bean.setImportePagado(UtilidadesHash.getDouble(hash,FcsPagosJGBean.C_IMPORTEPAGADO));
			bean.setImporteEJG(UtilidadesHash.getDouble(hash,FcsPagosJGBean.C_IMPORTEEJG));
			bean.setImporteGuardia(UtilidadesHash.getDouble(hash,FcsPagosJGBean.C_IMPORTEGUARDIA));
			bean.setImporteMinimo(UtilidadesHash.getDouble(hash,FcsPagosJGBean.C_IMPORTEMINIMO));
			bean.setImporteOficio(UtilidadesHash.getDouble(hash,FcsPagosJGBean.C_IMPORTEOFICIO));
			bean.setImporteSOJ(UtilidadesHash.getDouble(hash,FcsPagosJGBean.C_IMPORTESOJ));
			bean.setFechaMod(UtilidadesHash.getString(hash,FcsPagosJGBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,FcsPagosJGBean.C_USUMODIFICACION));
			bean.setContabilizado(UtilidadesHash.getString(hash,FcsPagosJGBean.C_CONTABILIZADO));
			bean.setBancosCodigo(UtilidadesHash.getString(hash,FcsPagosJGBean.C_BANCOS_CODIGO));
			bean.setIdpropOtros(UtilidadesHash.getInteger(hash,FcsPagosJGBean.C_IDPROPOTROS));
			bean.setIdpropSEPA(UtilidadesHash.getInteger(hash,FcsPagosJGBean.C_IDPROPSEPA));
			bean.setIdsufijo(UtilidadesHash.getInteger(hash,FcsPagosJGBean.C_IDSUFIJO));
		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	public Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable htData = null;
		FcsPagosJGBean beanJG = (FcsPagosJGBean) bean;
		try {
			htData = new Hashtable();			
			UtilidadesHash.set(htData, FcsPagosJGBean.C_IDINSTITUCION,	beanJG.getIdInstitucion());
			UtilidadesHash.set(htData, FcsPagosJGBean.C_IDPAGOSJG,	beanJG.getIdPagosJG());
			UtilidadesHash.set(htData, FcsPagosJGBean.C_IDFACTURACION, beanJG.getIdFacturacion());
			UtilidadesHash.set(htData, FcsPagosJGBean.C_NOMBRE, beanJG.getNombre());
			UtilidadesHash.set(htData, FcsPagosJGBean.C_ABREVIATURA, beanJG.getAbreviatura());
			UtilidadesHash.set(htData, FcsPagosJGBean.C_FECHADESDE, beanJG.getFechaDesde());
			UtilidadesHash.set(htData, FcsPagosJGBean.C_FECHAHASTA, beanJG.getFechaHasta());
			UtilidadesHash.set(htData, FcsPagosJGBean.C_CRITERIOPAGOTURNO, beanJG.getCriterioPagoTurno());
			UtilidadesHash.set(htData, FcsPagosJGBean.C_IMPORTEREPARTIR, beanJG.getImporteRepartir());			
			UtilidadesHash.set(htData, FcsPagosJGBean.C_IMPORTEPAGADO, beanJG.getImportePagado());
			UtilidadesHash.set(htData, FcsPagosJGBean.C_IMPORTEEJG, beanJG.getImporteEJG());
			UtilidadesHash.set(htData, FcsPagosJGBean.C_IMPORTEGUARDIA, beanJG.getImporteGuardia());
			UtilidadesHash.set(htData, FcsPagosJGBean.C_IMPORTEMINIMO, beanJG.getImporteMinimo());
			UtilidadesHash.set(htData, FcsPagosJGBean.C_IMPORTEOFICIO, beanJG.getImporteOficio());
			UtilidadesHash.set(htData, FcsPagosJGBean.C_IMPORTESOJ, beanJG.getImporteSOJ());
			UtilidadesHash.set(htData, FcsPagosJGBean.C_FECHAMODIFICACION, beanJG.getFechaMod());
			UtilidadesHash.set(htData, FcsPagosJGBean.C_USUMODIFICACION, beanJG.getUsuMod());
			UtilidadesHash.set(htData, FcsPagosJGBean.C_CONTABILIZADO, beanJG.getContabilizado());
			UtilidadesHash.set(htData, FcsPagosJGBean.C_BANCOS_CODIGO, beanJG.getBancosCodigo());
			UtilidadesHash.set(htData, FcsPagosJGBean.C_IDPROPOTROS, beanJG.getIdpropOtros());
			UtilidadesHash.set(htData, FcsPagosJGBean.C_IDPROPSEPA, beanJG.getIdpropSEPA());
			UtilidadesHash.set(htData, FcsPagosJGBean.C_IDSUFIJO, beanJG.getIdsufijo());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}
	
	/**
	 * Obtiene los datos del estado m�s reciente del pago pasado como par�metro.
	 * @param Integer: idInstitucion
	 * @param Integer: idPago
	 * @return Hashtable: datos del estado del pago
	 */
	public Hashtable getEstadoPago(Integer idInstitucion, Integer idPago) throws ClsExceptions{
		Hashtable estado = null;
		String consulta = null;
		
		//Creo la consulta que me devuelve los registros ordenados por la fecha de estado y en orden descendente:
		consulta =  "SELECT ";
		consulta += UtilidadesMultidioma.getCampoMultidioma(" E."+FcsEstadosPagosBean.C_DESCRIPCION,this.usrbean.getLanguage())+",";
		consulta += " P."+FcsPagosEstadosPagosBean.C_IDESTADOPAGOSJG+",";
		consulta += " P."+FcsPagosEstadosPagosBean.C_FECHAESTADO;
		consulta += " FROM ";
		consulta += FcsPagosEstadosPagosBean.T_NOMBRETABLA+" P,";
		consulta += FcsEstadosPagosBean.T_NOMBRETABLA+" E";
		consulta += " WHERE ";
		//Condiciones:
		consulta += " P."+FcsPagosEstadosPagosBean.C_IDINSTITUCION+"="+idInstitucion.toString();
		consulta += " AND P."+FcsPagosEstadosPagosBean.C_IDPAGOSJG+"="+idPago.toString();		
		//JOIN:
		consulta += " AND P."+FcsPagosEstadosPagosBean.C_IDESTADOPAGOSJG+"=E."+FcsEstadosPagosBean.C_IDESTADOPAGOSJG;
		consulta += " ORDER BY P."+FcsPagosEstadosPagosBean.C_FECHAESTADO+" DESC";
		
		//Obtengo los datos del primer registro que es el de fecha estado m�s reciente:
		estado = new Hashtable();
		estado = (Hashtable)this.selectGenerico(consulta).get(0);

		return estado;
	}

	/**
	 * Insertar en un vector cada fila como una tabla hash del resultado de ejecutar la query tirando 
	 * del pool NLS 
	 * @param String consulta: consulta SQL del SELECT almacenada en un String.
	 * @return Vector con tablas hash. Cada tabla hash es una fila del resultado del select en la base de datos.
	 * @throws ClsExceptions
	 */
	public Vector selectGenerico(String consulta) throws ClsExceptions 
	{
		return selectGenerico(consulta, false);
	}
	
	public Vector selectGenericoNLS(String consulta) throws ClsExceptions 
	{
		return selectGenericoNLS(consulta, false);
	}

	/**
	 * Insertar en un vector cada fila como una tabla hash del resultado de ejecutar la query 
	 * @param String consulta: consulta SQL del SELECT almacenada en un String.
	 * @param boolean bRW: true si usamos el pool de Lectura/Escritura.
	 * @return Vector con tablas hash. Cada tabla hash es una fila del resultado del select en la base de datos.
	 * @throws ClsExceptions
	 */
	public Vector selectGenerico(String consulta, boolean bRW) throws ClsExceptions {
		Vector datos = new Vector();
		boolean salida = false;
		
		// Acceso a BBDD	
		try { 
			RowsContainer rc = new RowsContainer(); 	
			
			//Si uso el pool de lectura/escritura
			if (bRW)
				salida = rc.findForUpdate(consulta);
			else
				salida = rc.query(consulta);
			if (salida) {
				for (int i = 0; i < rc.size(); i++)	{		
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow();
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en FcsPagosJGAdm.selectGenerico(). Consulta SQL:"+consulta);
		}
		return datos;	
	}

	/**
	 * Insertar en un vector cada fila como una tabla hash del resultado de ejecutar la query 
	 * @param String consulta: consulta SQL del SELECT almacenada en un String.
	 * @param boolean bRW: true si usamos el pool de Lectura/Escritura.
	 * @return Vector con tablas hash. Cada tabla hash es una fila del resultado del select en la base de datos.
	 * @throws ClsExceptions
	 */
	public Vector selectGenericoNLS(String consulta, boolean bRW) throws ClsExceptions {
		Vector datos = new Vector();
		boolean salida = false;
		
		// Acceso a BBDD	
		try { 
			RowsContainer rc = new RowsContainer(); 	
			
			//Si uso el pool de lectura/escritura
			if (bRW)
				salida = rc.findForUpdateNLS(consulta);
			else
				salida = rc.queryNLS(consulta);
			if (salida) {
				for (int i = 0; i < rc.size(); i++)	{		
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow();
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en FcsPagosJGAdm.selectGenerico(). Consulta SQL:"+consulta);
		}
		return datos;	
	}
	
	/**
	 * Devuelve un vector con los pagos que cumplen con
	 * los criterios de b�squeda pasados como par�metros
	 */
	public Vector getPagos (Hashtable criterios, String idInstitucionLocation)
			throws ClsExceptions, SIGAException
	{
		Vector vResultados = new Vector();
		String consulta = null;
		
		try
		{
			consulta =
				"select pag."+FcsPagosJGBean.C_IDINSTITUCION+", " +
				"       ins."+CenInstitucionBean.C_ABREVIATURA+"," +
				"       pag."+FcsPagosJGBean.C_IDPAGOSJG+", " +
				"       pag."+FcsPagosJGBean.C_IDFACTURACION+", " +
				"       pag."+FcsPagosJGBean.C_NOMBRE+", " +
				"       pag."+FcsPagosJGBean.C_ABREVIATURA+", " +
				"       pag."+FcsPagosJGBean.C_FECHADESDE+", " +
				"       pag."+FcsPagosJGBean.C_FECHAHASTA+", " +
				"       pag."+FcsPagosJGBean.C_CRITERIOPAGOTURNO+", " +
				"       pag."+FcsPagosJGBean.C_IMPORTEREPARTIR+", " +	
				"       pag."+FcsPagosJGBean.C_BANCOS_CODIGO+", " +
				"       pag."+FcsPagosJGBean.C_IDPROPOTROS+", " +
				"       pag."+FcsPagosJGBean.C_IDPROPSEPA+", " +
				"       pag."+FcsPagosJGBean.C_IDSUFIJO+", " +
				"       (select "+UtilidadesMultidioma.getCampoMultidioma ("estados."+FcsEstadosPagosBean.C_DESCRIPCION, this.usrbean.getLanguage())+" " +
				"          from "+FcsEstadosPagosBean.T_NOMBRETABLA+" ESTADOS " +
				"         where estados."+FcsEstadosPagosBean.C_IDESTADOPAGOSJG+" = est."+FcsPagosEstadosPagosBean.C_IDESTADOPAGOSJG+" " +
				"       ) as DESESTADO, " +
				"       est."+FcsPagosEstadosPagosBean.C_IDESTADOPAGOSJG+" as IDESTADO, " +
				"       est."+FcsPagosEstadosPagosBean.C_FECHAESTADO+" as FECHAESTADO " +
				"  from "+FcsPagosJGBean.T_NOMBRETABLA+" PAG, " +
				"       "+FcsPagosEstadosPagosBean.T_NOMBRETABLA+" EST, " +
				"       "+CenInstitucionBean.T_NOMBRETABLA+" INS " +
				" where pag."+FcsPagosJGBean.C_IDINSTITUCION+" = est."+FcsPagosEstadosPagosBean.C_IDINSTITUCION+" " +
				"   and pag."+FcsPagosJGBean.C_IDPAGOSJG+" = est."+FcsPagosEstadosPagosBean.C_IDPAGOSJG+" " +
				"   and est."+FcsPagosEstadosPagosBean.C_FECHAESTADO+" = " +
				"       (select max(est2."+FcsPagosEstadosPagosBean.C_FECHAESTADO+") " +
				"          from "+FcsPagosEstadosPagosBean.T_NOMBRETABLA+" EST2 " +
				"         where est2."+FcsPagosEstadosPagosBean.C_IDINSTITUCION+" = est."+FcsPagosEstadosPagosBean.C_IDINSTITUCION+" " +
				"           and est2."+FcsPagosEstadosPagosBean.C_IDPAGOSJG+" = est."+FcsPagosEstadosPagosBean.C_IDPAGOSJG+") " +
				"   and pag."+FcsPagosJGBean.C_IDINSTITUCION+" = ins."+CenInstitucionBean.C_IDINSTITUCION;
			
			//Busqueda por institucion:
			String institucion = (String) criterios.get ("idInstitucion");
			if (institucion!=null && !institucion.trim().equals("")) {
				consulta +=
				"   and pag."+FcsPagosJGBean.C_IDINSTITUCION+" = "+institucion;
			} else {
				consulta +=
				"   and pag."+FcsPagosJGBean.C_IDINSTITUCION+" IN ("+CenVisibilidad.getVisibilidadInstitucion (idInstitucionLocation)+") ";
			}
			
			//Busqueda por estado:
			String idEstado = (String) criterios.get ("idEstado");
			if (idEstado!=null && !idEstado.trim().equals("")) {
				consulta +=
				"   and est."+FcsPagosEstadosPagosBean.C_IDESTADOPAGOSJG+" = "+idEstado;
			}
			
			//Busqueda por nombre:
			String nombre = (String) criterios.get ("nombre");
			if (nombre!=null && !nombre.trim().equals("")) {
				consulta +=
				"   and ("+ComodinBusquedas.prepararSentenciaCompleta (nombre.trim(), "pag."+FcsPagosJGBean.C_NOMBRE)+") ";
			}
			
			//Busqueda por fechas:
			String fechaDesde = (String) criterios.get ("fechaIni");
			String fechaHasta = (String) criterios.get("fechaFin");
			if (fechaDesde!=null && !fechaDesde.trim().equals("")) {
				consulta +=
				"   and pag."+FcsPagosJGBean.C_FECHADESDE+" >= " +
				"       to_date ('"+GstDate.dateFormatoJava (fechaDesde)+"', '"+ClsConstants.DATE_FORMAT_SQL+"')";
			}
			if (fechaHasta!=null && !fechaHasta.trim().equals("")) {
				consulta +=
				"   and pag."+FcsPagosJGBean.C_FECHAHASTA+" <= " +
				"       to_date ('"+GstDate.dateSuma24hFormatoJava (fechaHasta)+"', '"+ClsConstants.DATE_FORMAT_SQL+"')";
			}
			
			//order by
			consulta +=
				" order by ins."+CenInstitucionBean.C_NOMBRE+", " +
				"          pag."+FcsPagosJGBean.C_FECHADESDE+" desc ";
			
			//ejecutando la consulta
			vResultados = this.selectGenericoNLS (consulta);
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error en FcsPagosJGAdm.getPagos");
		}
		
		return vResultados;
	} //getPagos ()
	
	
	/**
	 * Devuelve un vector con los pagos que cumplen con
	 * los criterios de b�squeda pasados como par�metros
	 */
	public Vector getPagosParaCerrar (Hashtable criterios, String idInstitucionLocation)
			throws ClsExceptions, SIGAException
	{
		Vector vResultados = new Vector();
		String consulta = null;
		
		try
		{
			consulta =
				"select pag."+FcsPagosJGBean.C_IDINSTITUCION+", " +
				"       ins."+CenInstitucionBean.C_ABREVIATURA+"," +
				"       pag."+FcsPagosJGBean.C_IDPAGOSJG+", " +
				"       pag."+FcsPagosJGBean.C_IDFACTURACION+", " +
				"       pag."+FcsPagosJGBean.C_NOMBRE+", " +
				"       pag."+FcsPagosJGBean.C_ABREVIATURA+", " +
				"       pag."+FcsPagosJGBean.C_FECHADESDE+", " +
				"       pag."+FcsPagosJGBean.C_FECHAHASTA+", " +
				"       pag."+FcsPagosJGBean.C_CRITERIOPAGOTURNO+", " +
				"       pag."+FcsPagosJGBean.C_IMPORTEREPARTIR+", " +
				"       pag."+FcsPagosJGBean.C_BANCOS_CODIGO+", " +
				"       pag."+FcsPagosJGBean.C_IDPROPOTROS+", " +
				"       pag."+FcsPagosJGBean.C_IDPROPSEPA+", " +
				"       pag."+FcsPagosJGBean.C_IDSUFIJO+", " +
				"       (select "+UtilidadesMultidioma.getCampoMultidioma ("estados."+FcsEstadosPagosBean.C_DESCRIPCION, this.usrbean.getLanguage())+" " +
				"          from "+FcsEstadosPagosBean.T_NOMBRETABLA+" ESTADOS " +
				"         where estados."+FcsEstadosPagosBean.C_IDESTADOPAGOSJG+" = est."+FcsPagosEstadosPagosBean.C_IDESTADOPAGOSJG+" " +
				"       ) as DESESTADO, " +
				"       est."+FcsPagosEstadosPagosBean.C_IDESTADOPAGOSJG+" as IDESTADO, " +
				"       est."+FcsPagosEstadosPagosBean.C_FECHAESTADO+" as FECHAESTADO " +
				"  from "+FcsPagosJGBean.T_NOMBRETABLA+" PAG, " +
				"       "+FcsPagosEstadosPagosBean.T_NOMBRETABLA+" EST, " +
				"       "+CenInstitucionBean.T_NOMBRETABLA+" INS " +
				" where pag."+FcsPagosJGBean.C_IDINSTITUCION+" = est."+FcsPagosEstadosPagosBean.C_IDINSTITUCION+" " +
				"   and pag."+FcsPagosJGBean.C_IDPAGOSJG+" = est."+FcsPagosEstadosPagosBean.C_IDPAGOSJG+" " +
				"   and est."+FcsPagosEstadosPagosBean.C_FECHAESTADO+" = " +
				"       (select max(est2."+FcsPagosEstadosPagosBean.C_FECHAESTADO+") " +
				"          from "+FcsPagosEstadosPagosBean.T_NOMBRETABLA+" EST2 " +
				"         where est2."+FcsPagosEstadosPagosBean.C_IDINSTITUCION+" = est."+FcsPagosEstadosPagosBean.C_IDINSTITUCION+" " +
				"           and est2."+FcsPagosEstadosPagosBean.C_IDPAGOSJG+" = est."+FcsPagosEstadosPagosBean.C_IDPAGOSJG+") " +
				"   and pag."+FcsPagosJGBean.C_IDINSTITUCION+" = ins."+CenInstitucionBean.C_IDINSTITUCION;
			
			//Busqueda por institucion:
			String institucion = (String) criterios.get ("idInstitucion");
			if (institucion!=null && !institucion.trim().equals("")) {
				consulta +=
				"   and pag."+FcsPagosJGBean.C_IDINSTITUCION+" = "+institucion;
			} else {
				consulta +=
				"   and pag."+FcsPagosJGBean.C_IDINSTITUCION+" IN ("+CenVisibilidad.getVisibilidadInstitucion (idInstitucionLocation)+") ";
			}

			//Busqueda por idpago:
			String idpagosjg = (String) criterios.get ("idPagosjg");
			if (idpagosjg!=null && !idpagosjg.trim().equals("")) {
				consulta +=
				"   and pag."+FcsPagosJGBean.C_IDPAGOSJG+" = "+idpagosjg;
			}

			//Busqueda por estado:
			String idEstado = (String) criterios.get ("idEstado");
			if (idEstado!=null && !idEstado.trim().equals("")) {
				consulta +=
				"   and est."+FcsPagosEstadosPagosBean.C_IDESTADOPAGOSJG+" = "+idEstado;
			}
			
			//Busqueda por nombre:
			String nombre = (String) criterios.get ("nombre");
			if (nombre!=null && !nombre.trim().equals("")) {
				consulta +=
				"   and ("+ComodinBusquedas.prepararSentenciaCompleta (nombre.trim(), "pag."+FcsPagosJGBean.C_NOMBRE)+") ";
			}
			
			//Busqueda por fechas:
			String fechaDesde = (String) criterios.get ("fechaIni");
			String fechaHasta = (String) criterios.get("fechaFin");
			if (fechaDesde!=null && !fechaDesde.trim().equals("")) {
				consulta +=
				"   and pag."+FcsPagosJGBean.C_FECHADESDE+" >= " +
				"       to_date ('"+GstDate.dateFormatoJava (fechaDesde)+"', '"+ClsConstants.DATE_FORMAT_SQL+"')";
			}
			if (fechaHasta!=null && !fechaHasta.trim().equals("")) {
				consulta +=
				"   and pag."+FcsPagosJGBean.C_FECHAHASTA+" <= " +
				"       to_date ('"+GstDate.dateSuma24hFormatoJava (fechaHasta)+"', '"+ClsConstants.DATE_FORMAT_SQL+"')";
			}
			
			//order by
			consulta +=
				" order by ins."+CenInstitucionBean.C_NOMBRE+", " +
				"          pag."+FcsPagosJGBean.C_FECHADESDE+" desc ";
			
			//ejecutando la consulta
			vResultados = this.selectGenerico (consulta);
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error en FcsPagosJGAdm.getPagos");
		}
		
		return vResultados;
	} //getPagos ()
	
	/**
	 * Calcula un nuevo identificador para la tabla.
	 * @param idInstitucion
	 * @return
	 * @throws ClsExceptions
	 */
	public String getNuevoId(String idInstitucion) throws ClsExceptions{
		String id = null;
		
		String consulta = " SELECT (MAX(" + FcsPagosJGBean.C_IDPAGOSJG + ") + 1) AS "+ FcsPagosJGBean.C_IDPAGOSJG + 
						  " FROM " + FcsPagosJGBean.T_NOMBRETABLA +
					      " WHERE " + FcsPagosJGBean.C_IDINSTITUCION + " = " + idInstitucion;
		
		Vector v = this.selectGenerico(consulta);
		if (v==null || v.size()==0 || (((String)((Hashtable)v.get(0)).get(FcsPagosJGBean.C_IDPAGOSJG)).equals("")))
			id = "1";
		else
			id = (String)((Hashtable)v.get(0)).get(FcsPagosJGBean.C_IDPAGOSJG);
		return id;
	}
	
	/**
	 * Devuelve true si hay error: existe uno o mas pagos sin cerrar.
	 * @param idInstitucion
	 * @param idFacturacion
	 * @return
	 * @throws ClsExceptions
	 */
	public boolean hayEstadoPagoNoCerrados(String idInstitucion, String idFacturacion) throws ClsExceptions {
		Vector registros = null;
		String consulta = null;
		
		//Creo la consulta que me devuelve los registros ordenados por la fecha de estado y en orden descendente:
		consulta =  "SELECT count(1) AS TOTAL ";
		consulta += " FROM ";
		consulta += FcsPagosJGBean.T_NOMBRETABLA+" P";
		consulta += " WHERE ";
		//Condiciones:
		consulta += " P."+FcsPagosJGBean.C_IDINSTITUCION+" = "+idInstitucion;
		consulta += " AND P."+FcsPagosJGBean.C_IDFACTURACION+" = "+idFacturacion;		
		consulta += " AND ( ";
		consulta += " select max(e."+FcsPagosEstadosPagosBean.C_IDESTADOPAGOSJG+")";
		consulta += " from "+FcsPagosEstadosPagosBean.T_NOMBRETABLA+" e ";
		consulta += " where e."+FcsPagosEstadosPagosBean.C_IDINSTITUCION+" = P."+FcsPagosJGBean.C_IDINSTITUCION;
		consulta += " and e."+FcsPagosEstadosPagosBean.C_IDPAGOSJG+" = P."+FcsPagosJGBean.C_IDPAGOSJG;
		consulta += " ) <> "+ClsConstants.ESTADO_PAGO_CERRADO;
		
		//Obtengo los datos del primer registro que es el de fecha estado m�s reciente:
		registros = this.selectGenerico(consulta);
		String contador = (String)((Hashtable)(registros.get(0))).get("TOTAL");
		
		if (!contador.equals("0")) return true;
		else return false;
	}
	
	
	
	
	/**
	 * Funcion que devuelve los idPersona de los colegiados que interviene en un pago
	 * en Actuaciones Designas, Asistencias, EJG's, Guardias y/o SOJ's y Movimientos.  
	 * 
	 * Devuelve Vector de Hashtables con IDPERSONA de cada colegiado
	 * 
	 * @param idInstitucion
	 * @param idPago
	 * @return
	 */
	public static int listaPagoSoloIncluirMorosos = 0;
	public static int listaPagoSoloIncluirNoMorosos = 1;
	public static int listaPagoTodos = 2;
	
	public Vector  getColegiadosAPagar(String idInstitucion, String idPago, int caseMorosos) throws ClsExceptions
	{
		// donde devolveremos el resultado
		Vector resultado = new Vector();
        Hashtable codigos = new Hashtable();
        
		// select a ejecutar
		int contador = 0;	            
		
		// select a ejecutar
		StringBuffer consulta = new StringBuffer();
		consulta.append(" SELECT pc." + FcsPagoColegiadoBean.C_IDPERORIGEN + " AS IDPERSONA_SJCS ");
		consulta.append(" FROM " + FcsPagoColegiadoBean.T_NOMBRETABLA);
		consulta.append(" pc ");
		contador++;
		codigos.put(new Integer(contador),idInstitucion);
		consulta.append(" WHERE pc." + FcsPagoColegiadoBean.C_IDINSTITUCION + "=:" + contador);
		contador++;
		codigos.put(new Integer(contador),idPago);
		consulta.append(" AND pc." + FcsPagoColegiadoBean.C_IDPAGOSJG + "=:" + contador);	
		switch (caseMorosos) {
			case 0:
				// ESTADOS = 1:Pagado; 2:PendienteCaja; 4:Devuelta; 5:PendienteBanco; 7:EnRevision; 8:Anulada
				consulta.append(" and exists (SELECT 1 FROM FAC_FACTURA F where f.idpersona = pc.IDPERORIGEN and f.idinstitucion = pc.idinstitucion AND F.NUMEROFACTURA > '0' AND F.IMPTOTALPORPAGAR > 0 AND F.ESTADO IN (2,4,5)) "); 
				break;
			case 1:
				consulta.append(" and not exists (SELECT 1 FROM FAC_FACTURA F where f.idpersona = pc.IDPERORIGEN and f.idinstitucion = pc.idinstitucion AND F.NUMEROFACTURA > '0' AND F.IMPTOTALPORPAGAR > 0 AND F.ESTADO IN (2,4,5)) ");
				break;
			default:
				break;
		}
		
			
//		consulta.append(" ORDER BY IDPERSONA_SJCS ASC");
							
		try
		{
			resultado = (Vector)this.selectGenericoBind(consulta.toString(),codigos);
		}
		catch(Exception e)
		{
			throw new ClsExceptions (e,"Error en FcsPAgosJG.getColegiadosAPagar()" + consulta);
		}
		return resultado; 
	}
	
	
	/**
	 * Funcion que devuelve los idPersona de los colegiados que interviene en un pago
	 * en Actuaciones Designas, Asistencias, EJG's, Guardias y/o SOJ's y Movimientos.  
	 * Solo obtiene aquellos cuyo total SJCS es mayor que 0.
	 * Devuelve Vector de Hashtables con IDPERSONA de cada colegiado.
	 * 
	 * @param idInstitucion
	 * @param idPago
	 * @return
	 */
	public Vector getColegiadosInformeCartaPago(String idInstitucion, String idPago) throws ClsExceptions
	{
		// donde devolveremos el resultado
		Vector resultado = new Vector();
        Hashtable codigos = new Hashtable();
        
		// select a ejecutar
		int contador = 0;	            
		
		// select a ejecutar
		StringBuffer consulta = new StringBuffer();
		consulta.append(" SELECT " + FcsPagoColegiadoBean.C_IDPERORIGEN + " AS IDPERSONA_SJCS");
		consulta.append(" FROM " + FcsPagoColegiadoBean.T_NOMBRETABLA);
		contador++;
		codigos.put(new Integer(contador),idInstitucion);
		consulta.append(" WHERE " + FcsPagoColegiadoBean.C_IDINSTITUCION + "=:" + contador);
		contador++;
		codigos.put(new Integer(contador),idPago);
		consulta.append(" AND " + FcsPagoColegiadoBean.C_IDPAGOSJG + "=:" + contador);	
		consulta.append(" AND " + FcsPagoColegiadoBean.C_IMPOFICIO + "+");	
		consulta.append(FcsPagoColegiadoBean.C_IMPASISTENCIA + "+");	
		consulta.append(FcsPagoColegiadoBean.C_IMPSOJ + "+");	
		consulta.append(FcsPagoColegiadoBean.C_IMPEJG + " > 0");	
		
		consulta.append(" ORDER BY IDPERSONA_SJCS ASC");
							
		try
		{
			resultado = (Vector)this.selectGenericoBind(consulta.toString(),codigos);
		}
		catch(Exception e)
		{
			throw new ClsExceptions (e,"Error en FcsPAgosJG.getColegiadosAPagar()" + consulta);
		}
		return resultado; 
	}

	
	public boolean excedePagoFacturacion(String idInstitucion, String idFacturacion, double importeActual) throws ClsExceptions {
		FcsFacturacionJGAdm factAdm = new FcsFacturacionJGAdm(this.usrbean);
		double totalPagado=0, totalFacturado=0;
		boolean excede = false;
		
		try {
			totalPagado = Double.valueOf(factAdm.getTotalPagado(idInstitucion, idFacturacion)).doubleValue();
			totalFacturado = Double.valueOf(factAdm.getTotalFacturado(idInstitucion, idFacturacion)).doubleValue();
			if ((totalPagado +importeActual) > totalFacturado) excede = true;
			else excede = false;
		} catch (Exception e) {
			throw new ClsExceptions (e,"Error en FcsPAgosJG.excedePagoFacturacion()");
		}
		return excede;
	}



	// DCG ///////////////////////////
	
	/**
	 * 
	 * @param idInstitucion
	 * @param idFacturacion
	 * @return
	 */
	private Long getImportePendienteGenerico (Integer idInstitucion, Integer idFacturacion, String campo) {
		try {
			String sql = " SELECT SUM(" + campo + ") AS IMPORTE" +
						 " FROM " + FcsPagosJGBean.T_NOMBRETABLA +
						 " WHERE " + FcsPagosJGBean.C_IDINSTITUCION + " = " + idInstitucion + 
						 " AND " + FcsPagosJGBean.C_IDFACTURACION + " = " + idFacturacion;

			Vector v = this.selectGenerico(sql);
			if ((v != null) && (v.size() == 1)) {
				Hashtable a = (Hashtable) v.get(0);
				return UtilidadesHash.getLong(a, "IMPORTE");
			}
			return null;
		}
		catch (Exception e) {
			return null;
		}
	}

	/**
	 * 
	 * @param idInstitucion
	 * @param idFacturacion
	 * @return
	 */
	public Long getImportePendienteDePago (Integer idInstitucion, Integer idFacturacion) {
		return this.getImportePendienteGenerico(idInstitucion, idFacturacion, FcsPagosJGBean.C_IMPORTEPAGADO);
	}

	/**
	 * 
	 * @param idInstitucion
	 * @param idFacturacion
	 * @return
	 */
	public Long getImporteTotalPagoRealizadoTurnosOficio (Integer idInstitucion, Integer idFacturacion) {
		return this.getImportePendienteGenerico(idInstitucion, idFacturacion, FcsPagosJGBean.C_IMPORTEOFICIO);
	}
	
	/**
	 * 
	 * @param idInstitucion
	 * @param idFacturacion
	 * @return
	 */
	public Long getImporteTotalPagoRealizadoGuardias (Integer idInstitucion, Integer idFacturacion) {
		return this.getImportePendienteGenerico(idInstitucion, idFacturacion, FcsPagosJGBean.C_IMPORTEGUARDIA);
	}
	
	/**
	 * 
	 * @param idInstitucion
	 * @param idFacturacion
	 * @return
	 */
	public Long getImporteTotalPagoRealizadoSOJ (Integer idInstitucion, Integer idFacturacion) {
		return this.getImportePendienteGenerico(idInstitucion, idFacturacion, FcsPagosJGBean.C_IMPORTESOJ);
	}

	/**
	 * 
	 * @param idInstitucion
	 * @param idFacturacion
	 * @return
	 */
	public Long getImporteTotalPagoRealizadoEJG (Integer idInstitucion, Integer idFacturacion) {
		return this.getImportePendienteGenerico(idInstitucion, idFacturacion, FcsPagosJGBean.C_IMPORTEEJG);
	}
	
	private String getQueryDetallePago (MantenimientoInformesForm form,
									   String idInstitucion, Hashtable codigos, String idioma) 
	{
		//Variables
		int contador = 0;
		boolean isFiltrado;
		String sql;
		
		
		//Iniciando consulta si existe un filtrado
		sql = "SELECT * FROM ( ";
		isFiltrado = false;
		if ((form.getLetrado()!=null				&& !form.getLetrado().equals(""))||
			(form.getInteresadoNif()!=null			&& !form.getInteresadoNif().equals(""))||
			(form.getInteresadoNombre()!=null		&& !form.getInteresadoNombre().equals(""))||
			(form.getInteresadoApellido1()!=null	&& !form.getInteresadoApellido1().equals(""))||
			(form.getInteresadoApellido2()!=null	&& !form.getInteresadoApellido2().equals("")))
		{
			isFiltrado = true;
		}
		
		//Obtiene la consulta base
		sql += getQueryDetallePagoColegiadoPaginador(idInstitucion, form.getIdPago(),null, idioma);

		// no se genera carta de pagos si no tiene pagos de SJCS
		sql += ") WHERE totalImporteSJCS > 0 ";

		if (isFiltrado) {
			sql +=
				" AND 1=1 ";

			if ((form.getLetrado()!=null && !form.getLetrado().equals(""))) {
				contador++;
				codigos.put(new Integer(contador), form.getLetrado());
				sql +=
					"   AND idpersonaSJCS = :"+contador;
			}
			boolean isInteresado = false;

			if ((form.getInteresadoNif()!=null && !form.getInteresadoNif().equals(""))) {
				sql +=
					"   AND idpersonaSJCS in (select pers.idpersona " +
					"                           from cen_persona pers " +
					"                          where ";
				contador++;
				codigos.put(new Integer(contador), form.getInteresadoNif().toUpperCase());
				sql += ComodinBusquedas.prepararSentenciaCompletaBind(
						form.getInteresadoNif().toUpperCase(),
						"upper(pers.nifcif)", contador, codigos);
				isInteresado = true;
			}

			if ((form.getInteresadoNombre()!=null && !form.getInteresadoNombre().equals(""))) {
				if (isInteresado) {
					sql +=
						"                            AND ";
				}
				else {
					sql +=
						"   AND idpersonaSJCS in (select pers.idpersona " +
						"                           from cen_persona pers " +
						"                          where ";
					isInteresado = true;
				}
				contador++;
				codigos.put(new Integer(contador), form.getInteresadoNombre().toUpperCase());
				sql += ComodinBusquedas.prepararSentenciaCompletaBind(
						form.getInteresadoNombre().toUpperCase(),
						"upper(pers.nombre)", contador, codigos);
			}

			if ((form.getInteresadoApellido1()!=null && !form.getInteresadoApellido1().equals(""))) {
				if (isInteresado) {
					sql +=
						"                            AND ";
				}
				else {
					sql +=
						"   AND idpersonaSJCS in (select pers.idpersona " +
						"                           from cen_persona pers " +
						"                          where ";
					isInteresado = true;
				}
				contador++;
				codigos.put(new Integer(contador), form.getInteresadoApellido1().toUpperCase());
				sql += ComodinBusquedas.prepararSentenciaCompletaBind(
						form.getInteresadoApellido1().toUpperCase(),
						"upper(pers.apellidos1)", contador, codigos);
			}

			if ((form.getInteresadoApellido2()!=null && !form.getInteresadoApellido2().equals(""))) {
				if (isInteresado) {
					sql +=
						"                            AND ";
				}
				else {
					sql +=
						"   AND idpersonaSJCS in (select pers.idpersona " +
						"                           from cen_persona pers " +
						"                          where ";
					isInteresado = true;
				}
				contador++;
				codigos.put(new Integer(contador), form.getInteresadoApellido2().toUpperCase());
				sql += ComodinBusquedas.prepararSentenciaCompletaBind(
						form.getInteresadoApellido2().toUpperCase(),
						"upper(pers.apellidos2)", contador, codigos);
				isInteresado = true;
			}

			if (isInteresado)
				sql +=	"                        ) ";
		}
		
		sql+=" ORDER BY nombre";
		
		return sql;
		
	} //getQueryDetallePago()
	
	
		private String getQueryDetallePagoInicioFin (MantenimientoInformesForm form,String idPagos,
									   String idInstitucion, Hashtable codigos, String idioma) 
	{
		//Variables
		int contador = 0;
		boolean isFiltrado;
		String sql;
		
		
		//Iniciando consulta si existe un filtrado
		sql = "SELECT * FROM ( ";
		isFiltrado = false;
		if ((form.getLetrado()!=null				&& !form.getLetrado().equals(""))||
			(form.getInteresadoNif()!=null			&& !form.getInteresadoNif().equals(""))||
			(form.getInteresadoNombre()!=null		&& !form.getInteresadoNombre().equals(""))||
			(form.getInteresadoApellido1()!=null	&& !form.getInteresadoApellido1().equals(""))||
			(form.getInteresadoApellido2()!=null	&& !form.getInteresadoApellido2().equals("")))
		{
			isFiltrado = true;
		}
		
		//Obtiene la consulta base
		sql += getQueryDetallePagoColegiadoPaginadorInicioFin(idInstitucion, idPagos,null, idioma);

		// no se genera carta de pagos si no tiene pagos de SJCS
		sql += ") WHERE totalImporteSJCS > 0 ";

		if (isFiltrado) {
			sql +=
				" AND 1=1 ";

			if ((form.getLetrado()!=null && !form.getLetrado().equals(""))) {
				contador++;
				codigos.put(new Integer(contador), form.getLetrado());
				sql +=
					"   AND idpersonaSJCS = :"+contador;
			}
			boolean isInteresado = false;

			if ((form.getInteresadoNif()!=null && !form.getInteresadoNif().equals(""))) {
				sql +=
					"   AND idpersonaSJCS in (select pers.idpersona " +
					"                           from cen_persona pers " +
					"                          where ";
				contador++;
				codigos.put(new Integer(contador), form.getInteresadoNif().toUpperCase());
				sql += ComodinBusquedas.prepararSentenciaCompletaBind(
						form.getInteresadoNif().toUpperCase(),
						"upper(pers.nifcif)", contador, codigos);
				isInteresado = true;
			}

			if ((form.getInteresadoNombre()!=null && !form.getInteresadoNombre().equals(""))) {
				if (isInteresado) {
					sql +=
						"                            AND ";
				}
				else {
					sql +=
						"   AND idpersonaSJCS in (select pers.idpersona " +
						"                           from cen_persona pers " +
						"                          where ";
					isInteresado = true;
				}
				contador++;
				codigos.put(new Integer(contador), form.getInteresadoNombre().toUpperCase());
				sql += ComodinBusquedas.prepararSentenciaCompletaBind(
						form.getInteresadoNombre().toUpperCase(),
						"upper(pers.nombre)", contador, codigos);
			}

			if ((form.getInteresadoApellido1()!=null && !form.getInteresadoApellido1().equals(""))) {
				if (isInteresado) {
					sql +=
						"                            AND ";
				}
				else {
					sql +=
						"   AND idpersonaSJCS in (select pers.idpersona " +
						"                           from cen_persona pers " +
						"                          where ";
					isInteresado = true;
				}
				contador++;
				codigos.put(new Integer(contador), form.getInteresadoApellido1().toUpperCase());
				sql += ComodinBusquedas.prepararSentenciaCompletaBind(
						form.getInteresadoApellido1().toUpperCase(),
						"upper(pers.apellidos1)", contador, codigos);
			}

			if ((form.getInteresadoApellido2()!=null && !form.getInteresadoApellido2().equals(""))) {
				if (isInteresado) {
					sql +=
						"                            AND ";
				}
				else {
					sql +=
						"   AND idpersonaSJCS in (select pers.idpersona " +
						"                           from cen_persona pers " +
						"                          where ";
					isInteresado = true;
				}
				contador++;
				codigos.put(new Integer(contador), form.getInteresadoApellido2().toUpperCase());
				sql += ComodinBusquedas.prepararSentenciaCompletaBind(
						form.getInteresadoApellido2().toUpperCase(),
						"upper(pers.apellidos2)", contador, codigos);
				isInteresado = true;
			}

			if (isInteresado)
				sql +=	"                        ) ";
		}
		
		sql+=" ORDER BY nombre";
		
		return sql;
		
	} //getQueryDetallePago()
	
	public String getQueryDetallePagoColegiado(String idInstitucion, String idPagosJg, boolean irpf, String idioma) {
		return getQueryDetallePagoColegiado(idInstitucion, idPagosJg, null, irpf, idioma);
	} //getQueryDetallePagoColegiado()
	
	/**
	 * Obtiene los datos de Detalle de Pago:
	 * usado en el Excel de Detalle de Letrado, por ejemplo 
	 */
	public String getQueryDetallePagoColegiado(String idInstitucion, String idPagosJg, String idPersona, boolean irpf, String idioma) {		
		String sql = " SELECT PC.IDINSTITUCION, " +
			" PC.IDPAGOSJG as IDPAGOS, " + 						
			" SUM(PC.IMPOFICIO + PC.IMPASISTENCIA + PC.IMPEJG + PC.IMPSOJ) AS TOTALIMPORTESJCS, " + 
			" SUM(PC.IMPRET) AS IMPORTETOTALRETENCIONES, " + 
			" SUM(PC.IMPMOVVAR) AS IMPORTETOTALMOVIMIENTOS, " + 			
			" SUM(PC.IMPIRPF) AS TOTALIMPORTEIRPF, "+
			
			" SUM(PC.IMPOFICIO + PC.IMPASISTENCIA + PC.IMPEJG + PC.IMPSOJ) + "+ 
			" SUM(PC.IMPRET) + "+ 
			" SUM(PC.IMPMOVVAR) + "+ 
			" SUM(PC.IMPIRPF) AS TOTALFINAL, "+
			" SUM(PC.IMPIRPF) AS TOTALIMPORTEIVA, ";
		
		if (irpf) {
			sql += " PC.IDPERDESTINO as IDPERSONASJCS ";
			
		} else {
			sql += " PC.IDCUENTA, " +
				" PC.IDPERDESTINO, " +
				" PC.PORCENTAJEIRPF AS TIPOIRPF, " +
				" SUM(PC.IMPOFICIO) AS IMPORTETOTALOFICIO, " + 
				" SUM(PC.IMPASISTENCIA) AS IMPORTETOTALASISTENCIA, " + 
				" SUM(PC.IMPEJG) AS IMPORTETOTALEJG, " + 
				" SUM(PC.IMPSOJ) AS IMPORTETOTALSOJ, " + 	
				" DECODE(PC.IDPERDESTINO, PC.IDPERORIGEN, 'Colegiado', 'Sociedad') AS DESTINATARIO, " + 
				" F_SIGA_GETRECURSO_ETIQUETA(DECODE(PC.IDCUENTA, NULL, 'gratuita.pagos.porCaja', 'gratuita.pagos.porBanco'), " + idioma + ") AS FORMADEPAGO, " + 
				" ( " +
					" SELECT B.NOMBRE " + 
					" FROM CEN_CUENTASBANCARIAS CB " + 
						" INNER JOIN CEN_BANCOS B " + 
						" ON CB.CBO_CODIGO = B.CODIGO " + 
					" WHERE PC.IDPERDESTINO = CB.IDPERSONA " + 
						" AND PC.IDINSTITUCION = CB.IDINSTITUCION " + 
						" AND PC.IDCUENTA = CB.IDCUENTA " + 
				" ) AS NOMBREBANCO, " + 		
				" ( " +
					" SELECT F_SIGA_FORMATOIBAN(CB.IBAN) AS CUENTA " + 
					" FROM CEN_CUENTASBANCARIAS CB " + 
					" WHERE PC.IDPERDESTINO = CB.IDPERSONA " + 
						" AND PC.IDINSTITUCION = CB.IDINSTITUCION " + 
						" AND PC.IDCUENTA = CB.IDCUENTA " + 
				" ) AS NUMEROCUENTA, " + 
				" PC.IDPERORIGEN as IDPERSONASJCS ";
		}
		
		sql += " FROM FCS_PAGO_COLEGIADO PC " + 
			" WHERE PC.IDINSTITUCION = " + idInstitucion +
				" AND PC.IDPAGOSJG = NVL(" + idPagosJg + ", PC.IDPAGOSJG) ";
		
		if(idPersona!=null) {
			if (irpf) {
				sql += " AND PC.IDPERDESTINO = " + idPersona;
			} else {
				sql += " AND PC.IDPERORIGEN = " + idPersona;
			}
		}
		
		if (irpf) {
			//sql += " AND PC.IMPIRPF > 0 " +
			sql +=	" GROUP BY PC.IDPERDESTINO, PC.IDPAGOSJG, PC.IDINSTITUCION, PC.PORCENTAJEIRPF, PC.IDCUENTA ";
		} else {
			sql += " GROUP BY PC.IDPERORIGEN, PC.IDPERDESTINO, PC.IDPAGOSJG, PC.IDINSTITUCION, PC.PORCENTAJEIRPF, PC.IDCUENTA ";
		}
		
		return sql;
	} //getQueryDetallePagoColegiado()
	
		/**
	 * Obtiene los datos de Detalle de Pago:
	 * usado en el Excel de Detalle de Letrado, por ejemplo 
	 */
	public String getQueryDetallePagoColegiadoInicioFin(String idInstitucion,
											   String idPagos, 
											   String idPersona, 
											   boolean irpf,
											   String idioma)
	{
		StringBuffer sql = new StringBuffer();
		
		sql.append("select ");
		if (irpf)
			sql.append("   idperdestino as idpersonaSJCS, ");
		else
			sql.append("   pc.IDPERORIGEN as idpersonaSJCS, ");
		
		sql.append("       pc.idpagosjg as idpagos,pj.nombre AS NOMBREPAGO, ");
		sql.append("       sum(pc.impOficio + pc.impAsistencia + pc.impEJG + pc.impSOJ) as totalImporteSJCS, ");
		sql.append("       sum(pc.impRet) as importeTotalRetenciones, ");
		sql.append("       sum(pc.impMovVar) as importeTotalMovimientos, ");
		sql.append("       sum(pc.impOficio + pc.impAsistencia + pc.impEJG + pc.impSOJ + pc.impMovVar) as TOTALIMPORTEBRUTO, ");
		
		sql.append("       sum(pc.impirpf) as TOTALIMPORTEIRPF, ");
		sql.append("       (sum(pc.impOficio + pc.impAsistencia + pc.impEJG + pc.impSOJ + pc.impMovVar)+(sum(pc.impirpf))+(sum(pc.impRet))) as IMPORTETOTAL, ");
		
		sql.append("       pc.idinstitucion, ");
		sql.append("       f_siga_getrecurso_etiqueta(decode(");
		sql.append("       (select a.idcuenta ");
		sql.append("          from FAC_ABONO A ");
		sql.append("         where idperdestino = a.idpersona ");
		sql.append("           and pc.idinstitucion = a.idinstitucion ");
		sql.append("           and pc.idpagosjg = a.idpagosjg ");
		sql.append("           and rownum = 1), ");
		sql.append("       null, 'gratuita.pagos.porCaja', 'gratuita.pagos.porBanco'), "+idioma+") ");
		sql.append("       as FORMADEPAGO ");
		
		sql.append("  from FCS_PAGO_COLEGIADO pc, cen_persona cen, fcs_pagosjg pj ");
		sql.append(" where pc.IDINSTITUCION = "+idInstitucion+" ");
		sql.append("   and pc.IDPAGOSJG IN("+idPagos+") ");
		sql.append("   and pc.IDPERORIGEN = nvl("+idPersona+", pc.IDPERORIGEN) ");
		sql.append("   and cen.idpersona = nvl("+idPersona+", pc.IDPERORIGEN) ");
		sql.append("   AND pj.idinstitucion = pc.idinstitucion ");
		sql.append("   AND pj.idpagosjg = pc.Idpagosjg   ");
		                           
                              
		if (irpf)
			sql.append(
					"  and pc.porcentajeirpf > 0 ");		
		sql.append(" group by cen.apellidos1,cen.apellidos2, pc.IDPERORIGEN, pc.IDPERDESTINO, pc.IDPAGOSJG, pc.IDINSTITUCION,  pj.nombre, pj.fechadesde ");
		sql.append(" ORDER BY cen.apellidos1, cen.apellidos2, pj.fechadesde");
		
		
		
		
		
		return sql.toString();
	} //getQueryDetallePagoColegiado()
	
	public String getQueryDetallePagoColegiadoPaginador(String idInstitucion,
														String idPagosJg,
														String idPersona,
														String idioma)
	{
		StringBuffer sql = new StringBuffer();
		sql.append(" select pagoAColegiados.*, ");
		sql.append("        f_siga_calculoncolegiado(pagoAColegiados.idinstitucion, pagoAColegiados.idpersonaSJCS) as NCOLEGIADO, ");
		sql.append("        (select p.apellidos1 || ' ' || p.apellidos2 || ', ' || p.nombre ");
		sql.append("           from cen_persona p, cen_cliente c ");
		sql.append("          where p.idpersona = c.idpersona ");
		sql.append("            and c.idinstitucion = pagoAColegiados.idinstitucion ");
		sql.append("            and c.idpersona = pagoAColegiados.idpersonaSJCS) as NOMBRE ");
		sql.append("   from ( ");
		
		sql.append(getQueryDetallePagoColegiado(idInstitucion, idPagosJg, idPersona, false, idioma));
		
		sql.append("        ) pagoAColegiados ");
		
		return sql.toString();
	} // getQueryDetallePagoColegiadoPaginador()
	
	public String getQueryDetallePagoColegiadoPaginadorInicioFin(String idInstitucion,
														String idPagoss,
														String idPersona,
														String idioma)
	{
		StringBuffer sql = new StringBuffer();
		sql.append(" select pagoAColegiados.*,");
		sql.append("        f_siga_calculoncolegiado(pagoAColegiados.idinstitucion, pagoAColegiados.idpersonaSJCS) as NCOLEGIADO, ");
		sql.append("        (select p.apellidos1 || ' ' || p.apellidos2 || ', ' || p.nombre ");
		sql.append("           from cen_persona p, cen_cliente c ");
		sql.append("          where p.idpersona = c.idpersona ");
		sql.append("            and c.idinstitucion = pagoAColegiados.idinstitucion ");
		sql.append("            and c.idpersona = pagoAColegiados.idpersonaSJCS) as NOMBRE ");
		sql.append("   from ( ");
		
		sql.append(getQueryDetallePagoColegiadoInicioFin(idInstitucion, idPagoss, idPersona, false, idioma));
		
		sql.append("        ) pagoAColegiados ");
		
		return sql.toString();
	} // getQueryDetallePagoColegiadoPaginador()
	
	public PaginadorCaseSensitiveBind getPaginadorDetallePago(MantenimientoInformesForm form, String idPagos,String idInstitucion,String idioma)
			throws ClsExceptions
	{
		Hashtable codigos = new Hashtable();
		String sql = getQueryDetallePagoInicioFin(form,idPagos,idInstitucion, codigos, idioma);
		
		try {
			PaginadorCaseSensitiveBind paginador = new PaginadorCaseSensitiveBind(
					sql, codigos);
			int totalRegistros = paginador.getNumeroTotalRegistros();
			if (totalRegistros == 0)
				paginador = null;
			
			return paginador;
			
		} catch (Exception e) {
			throw new ClsExceptions(e,
					"Error en FcsPAgosJG.getPaginadorDetallePago()" + sql);
		}
	} //getPaginadorDetallePago()
	
	public Vector getDetallePago(MantenimientoInformesForm form, String idioma)
			throws ClsExceptions, Exception
	{
		String sql = "";
		Hashtable htCodigos = new Hashtable();
		
		try {
			sql = getQueryDetallePago(form, form.getIdInstitucion(), htCodigos,
					idioma);
			return selectGenericoBind(sql, htCodigos);
			
		} catch (Exception e) {
			throw new ClsExceptions(e, "Error en FcsPAgosJG.getDetallePago()"
					+ sql);
		}
	} //getDetallePago()
	/**
	 * Devuelve los datos del pago <code>idpago</code> para cada colegiado incluido en el pago.
	 */
	public Vector getDetallePago(Integer idInstitucion, Integer idPago, String idioma)
			throws ClsExceptions, Exception
	{
		String sql = "";
		try {
			sql = getQueryDetallePagoColegiado(idInstitucion.toString(), idPago
					.toString(), false, idioma);
			return selectGenerico(sql);
			
		} catch (Exception e) {
			throw new ClsExceptions(e, "Error en FcsPAgosJG.getDetallePago()"
					+ sql);
		}
	} //getDetallePago()
	
	/**
	 * Devuelve los datos del pago <code>idpago</code> para cada colegiado incluido en el pago.
	 * Igual que getDetallePago, pero a�adimos las columnas de nombreapellido y ncolegiado para
	 * que no se tengan que hacer otras 2 querys luego
	 */
	public Vector getDetallePagoExt(Integer idInstitucion, Integer idPago, String idioma)
			throws ClsExceptions, Exception
	{
		//mhg - INC_08011_SIGA
		String sql= "select pago.*, " +
					" cen.APELLIDOS1 || ' ' || cen.APELLIDOS2 || ', ' || cen.NOMBRE AS NOMBREPERSONA, "+
					" decode(col.comunitario, 1, col.ncomunitario, col.ncolegiado) as NCOLEGIADO " +
					" from (";
		try {
			sql += getQueryDetallePagoColegiado(idInstitucion.toString(), idPago
					.toString(), false, idioma);
			sql += ") pago, "+
				" cen_persona   cen, cen_colegiado col " +
				" where pago.idpersonaSJCS = cen.idpersona " +
				" and col.idpersona = cen.idpersona " +
				" and col.idinstitucion ="+idInstitucion.toString();
			return selectGenerico(sql);
			
		} catch (Exception e) {
			throw new ClsExceptions(e, "Error en FcsPAgosJG.getDetallePago()"
					+ sql);
		}
	} //getDetallePago()
	
	/**
	 * Obtiene el importe total y los importes y porcentajes pendientes para cada concepto de la facturacion.
	 */
	public Hashtable getConceptosPendientesYTotal(Integer idInstitucion, Integer idFacturacion, boolean sinPagosAbiertos) throws ClsExceptions{

		StringBuffer consulta = new StringBuffer();

		consulta.append("select aux.*, ");
		consulta.append("(select FJG.IMPORTEOFICIO  from FCS_FACTURACIONJG FJG where FJG.IDINSTITUCION = "+idInstitucion.toString()+" AND FJG.IDFACTURACION = "+idFacturacion.toString()+") as TOTALOFICIO, ");
		consulta.append("(select FJG.IMPORTEGUARDIA from FCS_FACTURACIONJG FJG where FJG.IDINSTITUCION = "+idInstitucion.toString()+" AND FJG.IDFACTURACION = "+idFacturacion.toString()+") as TOTALGUARDIA, ");
		consulta.append("(select FJG.IMPORTEEJG     from FCS_FACTURACIONJG FJG where FJG.IDINSTITUCION = "+idInstitucion.toString()+" AND FJG.IDFACTURACION = "+idFacturacion.toString()+") as TOTALEJG, ");
		consulta.append("(select FJG.IMPORTESOJ     from FCS_FACTURACIONJG FJG where FJG.IDINSTITUCION = "+idInstitucion.toString()+" AND FJG.IDFACTURACION = "+idFacturacion.toString()+") as TOTALSOJ ");
		consulta.append("from ( ");
		consulta.append("SELECT NVL(SUM (P.IMPORTEPAGADO),0) as TOTALIMPORTEPAGADO, ");
		consulta.append(" NVL(SUM (P.IMPORTEOFICIO),0) as TOTALIMPORTEPAGADOOFICIO, ");
		consulta.append(" NVL(SUM (P.IMPORTEGUARDIA),0)  as TOTALIMPORTEPAGADOGUARDIA, ");
		consulta.append(" NVL(SUM (P.IMPORTEEJG),0)  as TOTALIMPORTEPAGADOEJG, ");
		consulta.append(" NVL(SUM (P.IMPORTESOJ),0)  as TOTALIMPORTEPAGADOSOJ, ");
		consulta.append(" NVL(decode(max(F.IMPORTEOFICIO),  0, 0, round( SUM(P.IMPORTEOFICIO)  * 100 / max(F.IMPORTEOFICIO)  ,2)),0)  as TOTALPORCENTAJEPAGADOOFICIO, ");
		consulta.append(" NVL(decode(max(F.IMPORTEGUARDIA), 0, 0, round( SUM(P.IMPORTEGUARDIA) * 100 / max(F.IMPORTEGUARDIA) ,2)),0)  as TOTALPORCENTAJEPAGADOGUARDIA, ");
		consulta.append(" NVL(decode(max(F.IMPORTEEJG),     0, 0, round( SUM(P.IMPORTEEJG)     * 100 / max(F.IMPORTEEJG)     ,2)),0)  as TOTALPORCENTAJEPAGADOEJG, ");
		consulta.append(" NVL(decode(max(F.IMPORTESOJ),     0, 0, round( SUM(P.IMPORTESOJ)     * 100 / max(F.IMPORTESOJ)     ,2)),0)  as TOTALPORCENTAJEPAGADOSOJ ");
		consulta.append(" FROM FCS_PAGOSJG P, FCS_FACTURACIONJG F");
		consulta.append(" WHERE ");
		consulta.append(" P.IDINSTITUCION="+idInstitucion.toString());
		consulta.append(" AND P.IDFACTURACION="+idFacturacion.toString());
		consulta.append(" AND F.IDINSTITUCION = P.IDINSTITUCION");
		consulta.append(" AND F.IDFACTURACION = P.IDFACTURACION");
		if (sinPagosAbiertos){
			consulta.append(" AND (SELECT max(ep.idestadopagosjg) FROM FCS_PAGOS_ESTADOSPAGOS EP WHERE EP.IDINSTITUCION = "+idInstitucion.toString()+" AND EP.IDPAGOSJG = p.idpagosjg) > 10");
		}
		consulta.append(" ) aux");
		
		Hashtable totalConceptos = (Hashtable)this.selectGenerico(consulta.toString()).get(0);

		return totalConceptos;
	}

	
	/**
	 * Actualizar el identificador de cuenta, columna localizada en la tabla fcs_pago_colegiado
	 * @param idInstitucion
	 * @param idCuenta
	 * @param idPagosJG
	 * @param idPersona
	 * @param porcentajeIRPF
	 * @return
	 * @throws ClsExceptions
	 */
	public boolean updatePagoIdCuenta(
			String idInstitucion, String idCuenta, String idPagosJG, String idPersona) throws ClsExceptions {
		//query con la select a ejecutar
		StringBuffer consulta = new StringBuffer();
		consulta.append("UPDATE FCS_PAGO_COLEGIADO SET idcuenta = ");
		consulta.append(idCuenta);
		consulta.append(" WHERE IDINSTITUCION = ");
		consulta.append(idInstitucion);
		consulta.append(" AND idperorigen = ");
		consulta.append(idPersona);
		consulta.append(" AND IDPAGOSJG = ");
		consulta.append(idPagosJG);
		int resultado;				
		try{
			resultado = ClsMngBBDD.executeUpdate(consulta.toString());
		}catch(Exception e){
			throw new ClsExceptions (e,"Error en FcsMovimientosVarios.getMovimientos()"+consulta);
		}
		return (resultado > 0);
	}	

	
	
	/**
	 * 
	 * @param idInstitucion
	 * @param idTurno
	 * @param sEstado
	 * @return
	 * @throws ClsExceptions
	 */
	public List<FcsPagosJGBean> getPagosInformes(String idInstitucion, String idTurno, String sEstado) throws ClsExceptions {
				
		String sql = " SELECT P." + FcsPagosJGBean.C_IDPAGOSJG + ", " +
				" TO_CHAR(E." + FcsPagosEstadosPagosBean.C_FECHAESTADO + ", 'dd/mm/yy')  || ' - ' || P." + FcsPagosJGBean.C_NOMBRE + " || ' (' || TO_CHAR(P." + FcsPagosJGBean.C_FECHADESDE + ", 'dd/mm/yy') || '-' ||TO_CHAR(P." + FcsPagosJGBean.C_FECHAHASTA + ", 'dd/mm/yy') || ')' AS " + FcsPagosJGBean.C_NOMBRE +
			" FROM " + FcsPagosJGBean.T_NOMBRETABLA + " P, " + 
				FcsPagosEstadosPagosBean.T_NOMBRETABLA + " E, " +
				FcsFacturacionJGBean.T_NOMBRETABLA + " FACT, " + 
				FcsFactGrupoFactHitoBean.T_NOMBRETABLA + " HITO, " + 
				ScsGrupoFacturacionBean.T_NOMBRETABLA + " GRUP " +
			" WHERE (GRUP." + ScsGrupoFacturacionBean.C_IDGRUPOFACTURACION + " = " + idTurno + " OR " + idTurno + " = -1) " +
				" AND P." + FcsPagosJGBean.C_IDINSTITUCION + " = " + idInstitucion +
				" AND E." + FcsPagosEstadosPagosBean.C_FECHAESTADO + " = (SELECT MAX(ES." + FcsPagosEstadosPagosBean.C_FECHAESTADO + ") FROM " + FcsPagosEstadosPagosBean.T_NOMBRETABLA + " ES WHERE ES." + FcsPagosEstadosPagosBean.C_IDPAGOSJG + " = P." + FcsPagosJGBean.C_IDPAGOSJG + " AND ES." + FcsPagosEstadosPagosBean.C_IDINSTITUCION + " = P." + FcsPagosJGBean.C_IDINSTITUCION + ") " +
				" AND (SELECT ESTADO." + FcsPagosEstadosPagosBean.C_IDESTADOPAGOSJG + " FROM " + FcsPagosEstadosPagosBean.T_NOMBRETABLA + " ESTADO WHERE ESTADO." + FcsPagosEstadosPagosBean.C_FECHAESTADO + " = E." + FcsPagosEstadosPagosBean.C_FECHAESTADO + " AND ESTADO." + FcsPagosEstadosPagosBean.C_IDINSTITUCION + " = E." + FcsPagosEstadosPagosBean.C_IDINSTITUCION + " AND ESTADO." + FcsPagosEstadosPagosBean.C_IDPAGOSJG + " = E." + FcsPagosEstadosPagosBean.C_IDPAGOSJG + ") > " + sEstado + 
				" AND P." + FcsPagosJGBean.C_IDINSTITUCION + " = E." + FcsPagosEstadosPagosBean.C_IDINSTITUCION +
				" AND P." + FcsPagosJGBean.C_IDPAGOSJG + " = E." + FcsPagosEstadosPagosBean.C_IDPAGOSJG + 
				" AND P." + FcsPagosJGBean.C_IDINSTITUCION + " = FACT." + FcsFacturacionJGBean.C_IDINSTITUCION +  
				" AND P." + FcsPagosJGBean.C_IDFACTURACION + " = FACT." + FcsFacturacionJGBean.C_IDFACTURACION +
				" AND FACT." + FcsFacturacionJGBean.C_IDINSTITUCION + " = HITO." + FcsFactGrupoFactHitoBean.C_IDINSTITUCION +
				" AND FACT." + FcsFacturacionJGBean.C_IDFACTURACION + " = HITO." + FcsFactGrupoFactHitoBean.C_IDFACTURACION + 
				" AND HITO." + FcsFactGrupoFactHitoBean.C_IDINSTITUCION + " = GRUP." + ScsGrupoFacturacionBean.C_IDINSTITUCION + 
				" AND HITO." + FcsFactGrupoFactHitoBean.C_IDGRUPOFACTURACION + " = GRUP." + ScsGrupoFacturacionBean.C_IDGRUPOFACTURACION +
			" GROUP BY P." + FcsPagosJGBean.C_IDPAGOSJG + ", " +
				" P." + FcsPagosJGBean.C_FECHADESDE + ", " +
				" P." + FcsPagosJGBean.C_FECHAHASTA + ", " +
				" P." + FcsPagosJGBean.C_NOMBRE + ", " +
				" E." + FcsPagosEstadosPagosBean.C_FECHAESTADO + 
			" ORDER BY E." + FcsPagosEstadosPagosBean.C_FECHAESTADO + " DESC, " +
				" P." + FcsPagosJGBean.C_FECHADESDE + " DESC";
		
		List<FcsPagosJGBean> aPagos = new ArrayList<FcsPagosJGBean>();
		try {
			RowsContainer rc = new RowsContainer();
			
			if (rc.query(sql)) {
    			for (int i = 0; i < rc.size(); i++){
            		Row fila = (Row) rc.get(i);
            		Hashtable<String, Object> htFila=fila.getRow();
            		
            		FcsPagosJGBean pagoBean = new FcsPagosJGBean();            		
            		pagoBean.setIdPagosJG(UtilidadesHash.getInteger(htFila, FcsPagosJGBean.C_IDPAGOSJG));
            		pagoBean.setNombre(UtilidadesHash.getString(htFila, FcsPagosJGBean.C_NOMBRE));
            		aPagos.add(pagoBean);
            	}
            } 
       } catch (Exception e) {
       		throw new ClsExceptions (e, "Error al ejecutar consulta.");
       }
		
       return aPagos;		
	}	
	
	public List<Hashtable> getFacturacionesGruposPagos(String idPago, String idInstitucion) throws ClsExceptions {
		
		StringBuffer sql = new StringBuffer();
		sql.append(" select Distinct fcs_pagosjg.idfacturacion,FCS_FACT_GRUPOFACT_HITO.Idgrupofacturacion ");
		sql.append(" from fcs_pagosjg, FCS_FACT_GRUPOFACT_HITO ");
		sql.append(" where FCS_FACT_GRUPOFACT_HITO.IDINSTITUCION(+) = fcs_pagosjg.idinstitucion ");
		sql.append(" and FCS_FACT_GRUPOFACT_HITO.IDFACTURACION(+) = fcs_pagosjg.idfacturacion ");
		sql.append(" and fcs_pagosjg.idpagosjg = ");
		sql.append(idPago);
		sql.append(" and fcs_pagosjg.idinstitucion = ");
		sql.append(idInstitucion);
		
		
		
		List<Hashtable> facturacionesGruposList = new ArrayList<Hashtable>();
		try {
			RowsContainer rc = new RowsContainer();
			
			if (rc.query(sql.toString())) {
    			for (int i = 0; i < rc.size(); i++){
            		Row fila = (Row) rc.get(i);
            		Hashtable<String, Object> htFila=fila.getRow();
            		
            		
            		facturacionesGruposList.add(htFila);
            	}
            } 
       } catch (Exception e) {
       		throw new ClsExceptions (e, "Error al ejecutar consulta.");
       }
		
       return facturacionesGruposList;		
	}	
	
	
	
	
	/**aalg: INC_06787_SIGA. Para hacer la consulta con nvl en los importes 
	 * Funcion selectNVL (String where)
	 * @param criteros para filtrar el select con nvl en los importes, campo where 
	 *  @return vector con los registros encontrados. El objeto es de tipo administrador del bean 
	 * */
	public Vector selectNVL(String where) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			String sql = UtilidadesBDAdm.sqlSelect(this.nombreTabla, this.getCamposBeanNVL());
			sql += " " + where;
			sql += this.getOrdenCampos()!=null ? UtilidadesBDAdm.sqlOrderBy(this.getOrdenCampos()) : UtilidadesBDAdm.sqlOrderBy(this.getClavesBean());
			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					MasterBean registro = (MasterBean) this.hashTableToBeanInicial(fila.getRow()); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, e.getMessage()); 
		}
		return datos;
	}
} 
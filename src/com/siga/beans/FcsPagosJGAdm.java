//VERSIONES:
//Creacion: david.sanchezp 18-03-2005
//jose.barrientos 28-02-2009: Añadidos los campos banco_codigo y concepto

package com.siga.beans;


import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.general.CenVisibilidad;
import com.siga.general.SIGAException;
import com.siga.informes.form.MantenimientoInformesForm;
import com.siga.Utilidades.PaginadorCaseSensitiveBind;

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
							FcsPagosJGBean.C_BANCOS_CODIGO,		FcsPagosJGBean.C_CONCEPTO,
							FcsPagosJGBean.C_FECHAMODIFICACION, FcsPagosJGBean.C_USUMODIFICACION};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {FcsPagosJGBean.C_IDINSTITUCION, FcsPagosJGBean.C_IDPAGOSJG};
		return claves;
	}

	protected String[] getOrdenCampos() {
		return getClavesBean();
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
			bean.setConcepto(UtilidadesHash.getString(hash,FcsPagosJGBean.C_CONCEPTO));
			bean.setBancosCodigo(UtilidadesHash.getString(hash,FcsPagosJGBean.C_BANCOS_CODIGO));
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
			UtilidadesHash.set(htData, FcsPagosJGBean.C_CONCEPTO, beanJG.getConcepto());
			UtilidadesHash.set(htData, FcsPagosJGBean.C_BANCOS_CODIGO, beanJG.getBancosCodigo());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}
	
	/**
	 * Obtiene los datos del estado más reciente del pago pasado como parámetro.
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
		
		//Obtengo los datos del primer registro que es el de fecha estado más reciente:
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
	 * los criterios de búsqueda pasados como parámetros
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
				"       pag."+FcsPagosJGBean.C_CONCEPTO+", " +
				"       pag."+FcsPagosJGBean.C_BANCOS_CODIGO+", " +
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
	 * los criterios de búsqueda pasados como parámetros
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
				"       pag."+FcsPagosJGBean.C_CONCEPTO+", " +
				"       pag."+FcsPagosJGBean.C_BANCOS_CODIGO+", " +
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
				consulta += " select pagos."+FcsPagosEstadosPagosBean.C_IDESTADOPAGOSJG;
				consulta += " from "+FcsPagosEstadosPagosBean.T_NOMBRETABLA+" pagos ";
				consulta += " where pagos."+FcsPagosEstadosPagosBean.C_FECHAESTADO+" = ( ";
						consulta += " select max(e."+FcsPagosEstadosPagosBean.C_FECHAESTADO+")";
						consulta += " from "+FcsPagosEstadosPagosBean.T_NOMBRETABLA+" e";
						consulta += " where e."+FcsPagosEstadosPagosBean.C_IDINSTITUCION+" = P."+FcsPagosJGBean.C_IDINSTITUCION;
						consulta += " and e."+FcsPagosEstadosPagosBean.C_IDPAGOSJG+" = P."+FcsPagosJGBean.C_IDPAGOSJG;
						consulta += " ) ";
		consulta += " ) <> "+ClsConstants.ESTADO_PAGO_CERRADO;
		
		//Obtengo los datos del primer registro que es el de fecha estado más reciente:
		registros = this.selectGenerico(consulta);
		String contador = (String)((Hashtable)(registros.get(0))).get("TOTAL");
		
		if (!contador.equals("0")) return true;
		else return false;
	}
	
	/**
	 * Funcion que devuelve las Actuaciones Designas, Asistencias, EJG's, Guardias y/o SOJ's 
	 * a pagar por un colegiado en particular. Devuelve un vector con los siguientes datos en cada posicion:
	 * 
	 * 0.-turnos de oficio<br>
	 * 1.-guardias presenciales<br>
	 * 2.-asistencias<br>
	 * 3.-actuaciones<br>
	 * 4.-expedientesSoj<br>
	 * 5.-expedientesEjg<br>
	 * 6.-movimientos Varios<br>
	 * 7.-retenciones judiciales (si el estado es = 30)<br>
	 * 
	 * @param idInstitucion
	 * @param idPago
	 * @param idPersona
	 * @return
	 */
//	public Vector getDetallePorColegiado (String idInstitucion, String idPago, String idPersona )
//	{
//		//resultado final
//		Vector resultado = new Vector();
//		//turnos de oficio
//		Vector actuacionDesigna = new Vector();
//		//guardias presenciales
//		Vector guardiasPresenciales = new Vector();
//		//asistencias
//		Vector asistencias = new Vector();
//		//actuaciones
//		Vector actuaciones = new Vector();
//		//expedientesSoj
//		Vector expedientesSoj = new Vector();
//		//expedientesEjg
//		Vector expedientesEjg = new Vector();
//		//movimientos varios
//		Vector movimientos = new Vector();
//		//retenciones Judiciales
//		Vector retencionesJudiciales = new Vector();
//		
//		//consultamos el estado del pago
//		Integer idInst = new Integer(idInstitucion);
//		Integer idPag = new Integer (idPago);
//		
//		//variable para saber si el estado del pago es = 30
//		boolean cerrado = false;
//		try{
//			Hashtable hash = (Hashtable)this.getEstadoPago(idInst,idPag);
//			cerrado = ((String)hash.get(FcsEstadosPagosBean.C_IDESTADOPAGOSJG)).equals(ClsConstants.ESTADO_PAGO_CERRADO);
//		}catch(Exception e){}
//		
//		//llenamos cada uno de los Vectores parciales
//		try{
//			FcsPagoActuacionDesignaAdm actDesAdm = new FcsPagoActuacionDesignaAdm (this.usrbean);
//			actuacionDesigna = (Vector)actDesAdm.getTurnosOficio(idInstitucion,idPago,idPersona);
//		}catch(Exception e){
//			actuacionDesigna = null;
//		}
//		try{
//			FcsPagoGuardiasColegiadoAdm guarColAdm = new FcsPagoGuardiasColegiadoAdm (this.usrbean);
//			guardiasPresenciales = (Vector)guarColAdm.getGuardiasPresenciales(idInstitucion,idPago,idPersona);
//		}catch(Exception e){
//			guardiasPresenciales = null;
//		}
//		try{
//			FcsPagoAsistenciaAdm asisAdm = new FcsPagoAsistenciaAdm (this.usrbean);
//			asistencias = (Vector)asisAdm.getAsistencias(idInstitucion,idPago,idPersona);
//		}catch(Exception e){
//			asistencias = null;
//		}
//		try{
//			FcsPagoActuacionAsistAdm actAsisAdm = new FcsPagoActuacionAsistAdm (this.usrbean);
//			actuaciones = (Vector)actAsisAdm.getActuacionAsistencias(idInstitucion,idPago,idPersona);
//		}catch(Exception e){
//			actuaciones = null;
//		}
//		try{
//			FcsPagoSojAdm sojAdm = new FcsPagoSojAdm (this.usrbean);
//			expedientesSoj = (Vector)sojAdm.getExpedientesSoj(idInstitucion,idPago,idPersona);
//		}catch(Exception e){
//			expedientesSoj = null;
//		}
//		try{
//			FcsPagoEjgAdm ejgAdm = new FcsPagoEjgAdm (this.usrbean);
//			expedientesEjg = (Vector)ejgAdm.getExpedientesEjg(idInstitucion,idPago,idPersona);
//		}catch(Exception e){
//			expedientesEjg = null;
//		}
//		try{
//			FcsMovimientosVariosAdm movimientosAdm = new FcsMovimientosVariosAdm (this.usrbean);
//			movimientos = (Vector)movimientosAdm.getMovimientos(idInstitucion,idPago,idPersona);
//		}catch(Exception e){
//			expedientesEjg = null;
//		}
//		if(cerrado){
//			try{
//				FcsCobrosRetencionJudicialAdm retencionesJudAdm = new FcsCobrosRetencionJudicialAdm(this.usrbean);
//				retencionesJudiciales = (Vector)retencionesJudAdm.getRetenciones(idInstitucion,idPago,idPersona);
//			}catch(Exception e){
//				expedientesEjg = null;
//			}
//		}
//		
//		
//		//los metemos todos en el Vector resultado
//		resultado.add(0, actuacionDesigna);
//		resultado.add(1, guardiasPresenciales);
//		resultado.add(2, asistencias);
//		resultado.add(3, actuaciones); 
//		resultado.add(4, expedientesSoj);
//		resultado.add(5, expedientesEjg);
//		resultado.add(6, movimientos);
//		if (cerrado)resultado.add(7, retencionesJudiciales);
//		
//		return resultado; 
//	}
	
	
	
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
	
	public Vector  getColegiadosAPagar(String idInstitucion, String idPago) throws ClsExceptions
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
	
	public String getQueryDetallePago (MantenimientoInformesForm form,
									   String idInstitucion, Hashtable codigos) 
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
		sql += getQueryDetallePagoColegiado(idInstitucion, form.getIdPago());

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
		return sql;
		
	} //getQueryDetallePago()
	
	
	/**
	 * 
	 * @param idInstitucion
	 * @param idPagosJg si es null se obtienen los datos de todos los pagos de la institucion.
	 * @return
	 */
	public String getQueryDetallePagoColegiado (String idInstitucion, String idPagosJg) 
	{
		return getQueryDetallePagoColegiado(idInstitucion, idPagosJg, null);
	}	

	/**
	 * 
	 * @param idInstitucion
	 * @param idPagosJg si es null se obtienen los datos de todos los pagos de la institucion.
	 * @return
	 */
	public String getQueryDetallePagoColegiado (String idInstitucion, String idPagosJg, String idPersona) 
	{
		StringBuffer sql = new StringBuffer();

		sql.append("select IDPERORIGEN as idpersonaSJCS,");
		sql.append(" idpagosjg as idpagos, ");		
		sql.append(" sum(impOficio + impAsistencia + impEJG + impSOJ) as totalImporteSJCS,");
		sql.append(" sum(impRet) as importeTotalRetenciones,");
		sql.append(" sum(impMovVar) as importeTotalMovimientos,");
		sql.append(" -1*abs(sum(impOficio + impAsistencia + impEJG + impSOJ + impMovVar) * max(impirpf) / 100) as TOTALIMPORTEIRPF");
		sql.append(" from FCS_PAGO_COLEGIADO");
		sql.append(" where IDINSTITUCION = ");	sql.append(idInstitucion);
		sql.append(" and IDPAGOSJG = nvl("+idPagosJg+", IDPAGOSJG)");
		sql.append(" and IDPERORIGEN = nvl("+idPersona+", IDPERORIGEN)");
		sql.append(" group by IDPERORIGEN, IDPAGOSJG ");	

		return sql.toString();
	} 




	
	public PaginadorCaseSensitiveBind  getPaginadorDetallePago(MantenimientoInformesForm form,String idInstitucion) throws ClsExceptions
	{
			            
        Hashtable codigos = new Hashtable();
		// select a ejecutar
		String sql = getQueryDetallePago(form, idInstitucion,  codigos);
							
		try
		{
			PaginadorCaseSensitiveBind paginador = new PaginadorCaseSensitiveBind(sql,codigos);				
			int totalRegistros = paginador.getNumeroTotalRegistros();
			
			if (totalRegistros==0){					
				paginador =null;
			}
			return paginador;
		}
		catch(Exception e)
		{
			throw new ClsExceptions (e,"Error en FcsPAgosJG.getPaginadorDetallePago()" + sql);
		}
		
	}
	public Vector getDetallePago (MantenimientoInformesForm form) 
	throws ClsExceptions , Exception{
		String sql = "";
		try {
			
			Hashtable htCodigos = new Hashtable();
			sql = getQueryDetallePago(form, form.getIdInstitucion(),htCodigos);
			

			return this.selectGenericoBind(sql, htCodigos);
		}
		catch (Exception e) {
			throw new ClsExceptions (e,"Error en FcsPAgosJG.getDetallePago()" + sql);
		}
	}
	
	
	/**
	 * Devuelve los datos del pago <code>idpago</code> para cada colegiado incluido en el pago.
	 * @param idInstitucion
	 * @param idPago
	 * @return
	 */
	public Vector getDetallePago (Integer idInstitucion, Integer idPago) 
	throws ClsExceptions , Exception{
		String sql = null;
		try {
			sql = getQueryDetallePagoColegiado(idInstitucion.toString(),idPago.toString());
			
			return selectGenerico(sql);
		}
		catch (Exception e) {
			throw new ClsExceptions (e,"Error en FcsPAgosJG.getDetallePago()" + sql);
		}
	}
	
	
	
	/**
	 * Obtiene el importe total y los importes y porcentajes pendientes para cada concepto de la facturacion.
	 * @param Integer: idInstitucion
	 * @param Integer: idPago
	 * @return Hashtable: 
	 */
	public Hashtable getConceptosPendientesYTotal(Integer idInstitucion, Integer idFacturacion) throws ClsExceptions{

		StringBuffer consulta = new StringBuffer();

		consulta.append("SELECT SUM (P.IMPORTEPAGADO) as TOTALIMPORTEPAGADO, ");
		consulta.append(" SUM (P.IMPORTEOFICIO) as TOTALIMPORTEPAGADOOFICIO, ");
		consulta.append(" SUM (P.IMPORTEGUARDIA)  as TOTALIMPORTEPAGADOGUARDIA, ");
		consulta.append(" SUM (P.IMPORTEEJG)  as TOTALIMPORTEPAGADOEJG, ");
		consulta.append(" SUM (P.IMPORTESOJ)  as TOTALIMPORTEPAGADOSOJ, ");
		consulta.append(" decode(max(F.IMPORTEOFICIO),  0, 0, round( SUM(P.IMPORTEOFICIO)  * 100 / max(F.IMPORTEOFICIO)  ,2))  as TOTALPORCENTAJEPAGADOOFICIO, ");
		consulta.append(" decode(max(F.IMPORTEGUARDIA), 0, 0, round( SUM(P.IMPORTEGUARDIA) * 100 / max(F.IMPORTEGUARDIA) ,2))  as TOTALPORCENTAJEPAGADOGUARDIA, ");
		consulta.append(" decode(max(F.IMPORTEEJG),     0, 0, round( SUM(P.IMPORTEEJG)     * 100 / max(F.IMPORTEEJG)     ,2))  as TOTALPORCENTAJEPAGADOEJG, ");
		consulta.append(" decode(max(F.IMPORTESOJ),     0, 0, round( SUM(P.IMPORTESOJ)     * 100 / max(F.IMPORTESOJ)     ,2))  as TOTALPORCENTAJEPAGADOSOJ, ");
		consulta.append(" max(F.IMPORTEOFICIO)  as TOTALOFICIO, ");
		consulta.append(" max(F.IMPORTEGUARDIA) as TOTALGUARDIA, ");
		consulta.append(" max(F.IMPORTEEJG)     as TOTALEJG, ");
		consulta.append(" max(F.IMPORTESOJ)     as TOTALSOJ ");
		consulta.append(" FROM FCS_PAGOSJG P, FCS_FACTURACIONJG F");
		consulta.append(" WHERE ");
		consulta.append(" P.IDINSTITUCION="+idInstitucion.toString());
		consulta.append(" AND P.IDFACTURACION="+idFacturacion.toString());
		consulta.append(" AND F.IDINSTITUCION = P.IDINSTITUCION");
		consulta.append(" AND F.IDFACTURACION = P.IDFACTURACION");

		Hashtable totalConceptos = (Hashtable)this.selectGenerico(consulta.toString()).get(0);

		return totalConceptos;
	}

} 
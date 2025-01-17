//VERSIONES:
// raul.ggonzalez 08-03-2005 creacion
//Modificado por david.sanchez para incluir nuevos metodos: getTotalPagado() y getTotalFacturado().
//Modificado por ruben.fernandez para incluir en getFacturacion() el campo REGULARIZACI�N en la consulta.

package com.siga.beans;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.redabogacia.sigaservices.app.AppConstants.ESTADO_FACTURACION;
import org.redabogacia.sigaservices.app.services.fac.PcajgAlcActService;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.FileHelper;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.SIGALogging;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.Utilidades.UtilidadesNumero;
import com.siga.Utilidades.UtilidadesString;
import com.siga.Utilidades.paginadores.Paginador;
import com.siga.Utilidades.paginadores.PaginadorCaseSensitive;
import com.siga.facturacionSJCS.UtilidadesFacturacionSJCS;
import com.siga.facturacionSJCS.form.GenerarImpreso190Form;
import com.siga.general.CenVisibilidad;
import com.siga.general.SIGAException;
import com.siga.informes.InformePersonalizable;
import com.siga.informes.form.MantenimientoInformesForm;
import com.siga.ws.CajgConfiguracion;

import es.satec.businessManager.BusinessManager;

/**
* Administrador de Facturacion de justicia gratuita
* @author AtosOrigin 08-03-2005
* @version: 15/05/2006: david.sanchezp: correcciones para que saque datos de importes e irpfs.
*/
public class FcsFacturacionJGAdm extends MasterBeanAdministrador {

	
	private static Boolean alguienEjecutando=Boolean.FALSE;
	
	public FcsFacturacionJGAdm(UsrBean usuario) {
		super(FcsFacturacionJGBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean() {
		String [] campos = {FcsFacturacionJGBean.C_FECHADESDE, 
							FcsFacturacionJGBean.C_FECHAHASTA,
							FcsFacturacionJGBean.C_FECHAMODIFICACION,
							FcsFacturacionJGBean.C_IDFACTURACION,
							FcsFacturacionJGBean.C_IDFACTURACION_REGULARIZA,
							FcsFacturacionJGBean.C_IDINSTITUCION,
							FcsFacturacionJGBean.C_IMPORTEEJG,
							FcsFacturacionJGBean.C_IMPORTEGUARDIA,
							FcsFacturacionJGBean.C_IMPORTEOFICIO,
							FcsFacturacionJGBean.C_IMPORTESOJ,
							FcsFacturacionJGBean.C_IMPORTETOTAL,
							FcsFacturacionJGBean.C_NOMBRE,
							FcsFacturacionJGBean.C_PREVISION,
							FcsFacturacionJGBean.C_REGULARIZACION,
							FcsFacturacionJGBean.C_NOMBREFISICO,
							FcsFacturacionJGBean.C_USUMODIFICACION,
							FcsFacturacionJGBean.C_IDECOMCOLA};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {FcsFacturacionJGBean.C_IDINSTITUCION, FcsFacturacionJGBean.C_IDFACTURACION};
		return claves;
	}

	protected String[] getOrdenCampos() {
		return getClavesBean();
	}

	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		FcsFacturacionJGBean bean = null;
		
		try {
			bean = new FcsFacturacionJGBean();
			bean.setFechaDesde(UtilidadesHash.getString(hash, FcsFacturacionJGBean.C_FECHADESDE));
			bean.setFechaHasta(UtilidadesHash.getString(hash, FcsFacturacionJGBean.C_FECHAHASTA));
			bean.setFechaMod(UtilidadesHash.getString(hash, FcsFacturacionJGBean.C_FECHAMODIFICACION));
			bean.setIdFacturacion(UtilidadesHash.getInteger(hash, FcsFacturacionJGBean.C_IDFACTURACION));
			bean.setIdFacturacion_regulariza(UtilidadesHash.getInteger(hash, FcsFacturacionJGBean.C_IDFACTURACION_REGULARIZA));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, FcsFacturacionJGBean.C_IDINSTITUCION));
			bean.setImporteEJG(UtilidadesHash.getDouble(hash, FcsFacturacionJGBean.C_IMPORTEEJG));
			bean.setImporteGuardia(UtilidadesHash.getDouble(hash, FcsFacturacionJGBean.C_IMPORTEGUARDIA));
			bean.setImporteOficio(UtilidadesHash.getDouble(hash, FcsFacturacionJGBean.C_IMPORTEOFICIO));
			bean.setImporteSOJ(UtilidadesHash.getDouble(hash, FcsFacturacionJGBean.C_IMPORTESOJ));
			bean.setImporteTotal(UtilidadesHash.getDouble(hash, FcsFacturacionJGBean.C_IMPORTETOTAL));
			bean.setNombre(UtilidadesHash.getString(hash, FcsFacturacionJGBean.C_NOMBRE));
			bean.setPrevision(UtilidadesHash.getString(hash, FcsFacturacionJGBean.C_PREVISION));
			bean.setRegularizacion(UtilidadesHash.getString(hash, FcsFacturacionJGBean.C_REGULARIZACION));
			bean.setNombreFisico(UtilidadesHash.getString(hash, FcsFacturacionJGBean.C_NOMBREFISICO));
			bean.setUsuMod(UtilidadesHash.getInteger(hash, FcsFacturacionJGBean.C_USUMODIFICACION));
			bean.setIdecomcola(UtilidadesHash.getLong(hash, FcsFacturacionJGBean.C_IDECOMCOLA));
		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	public Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			FcsFacturacionJGBean b = (FcsFacturacionJGBean) bean;
			UtilidadesHash.set(htData, FcsFacturacionJGBean.C_FECHADESDE, b.getFechaDesde());
			UtilidadesHash.set(htData, FcsFacturacionJGBean.C_FECHAHASTA, b.getFechaHasta());
			UtilidadesHash.set(htData, FcsFacturacionJGBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, FcsFacturacionJGBean.C_IDFACTURACION, b.getIdFacturacion());
			UtilidadesHash.set(htData, FcsFacturacionJGBean.C_IDFACTURACION_REGULARIZA, b.getIdFacturacion_regulariza());
			UtilidadesHash.set(htData, FcsFacturacionJGBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, FcsFacturacionJGBean.C_IMPORTEEJG, b.getImporteEJG());
			UtilidadesHash.set(htData, FcsFacturacionJGBean.C_IMPORTEGUARDIA, b.getImporteGuardia());
			UtilidadesHash.set(htData, FcsFacturacionJGBean.C_IMPORTEOFICIO, b.getImporteOficio());
			UtilidadesHash.set(htData, FcsFacturacionJGBean.C_IMPORTESOJ, b.getImporteSOJ());
			UtilidadesHash.set(htData, FcsFacturacionJGBean.C_IMPORTETOTAL, b.getImporteTotal());
			UtilidadesHash.set(htData, FcsFacturacionJGBean.C_NOMBRE, b.getNombre());
			UtilidadesHash.set(htData, FcsFacturacionJGBean.C_PREVISION, b.getPrevision());
			UtilidadesHash.set(htData, FcsFacturacionJGBean.C_REGULARIZACION, b.getRegularizacion());
			UtilidadesHash.set(htData, FcsFacturacionJGBean.C_NOMBREFISICO, b.getNombreFisico());
			UtilidadesHash.set(htData, FcsFacturacionJGBean.C_USUMODIFICACION, b.getUsuMod());
			UtilidadesHash.set(htData, FcsFacturacionJGBean.C_IDECOMCOLA, b.getIdecomcola());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}
	
	/**
	 * Devuelve un vector con las facturaciones buscadas 
	 */	
	public Paginador getFacturaciones (Hashtable criterios,
									   String idInstitucionLocation, String prevision)
			throws ClsExceptions, SIGAException
	{
		try
		{

			String sql =
				"select fac."+FcsFacturacionJGBean.C_IDINSTITUCION+", " +
				"       ins."+CenInstitucionBean.C_ABREVIATURA+", " +
				"       fac."+FcsFacturacionJGBean.C_IDFACTURACION+", " +
				"       fac."+FcsFacturacionJGBean.C_FECHADESDE+", " +
				"       fac."+FcsFacturacionJGBean.C_FECHAHASTA+", " +
				"       fac."+FcsFacturacionJGBean.C_NOMBRE+", " +
				"       decode(fac."+FcsFacturacionJGBean.C_REGULARIZACION+", " +
				"              '"+ClsConstants.DB_TRUE+"', 'Si', 'No') as "+FcsFacturacionJGBean.C_REGULARIZACION+", " +
				"       (select "+UtilidadesMultidioma.getCampoMultidioma ("estados."+FcsEstadosFacturacionBean.C_DESCRIPCION, this.usrbean.getLanguage())+ " " +
				"          from "+FcsEstadosFacturacionBean.T_NOMBRETABLA+" ESTADOS " +
				"         where est."+FcsFactEstadosFacturacionBean.C_IDESTADOFACTURACION+" = estados."+FcsEstadosFacturacionBean.C_IDESTADOFACTURACION+" " +
				"       ) as DESESTADO, " +
				"       est."+FcsFactEstadosFacturacionBean.C_IDESTADOFACTURACION+" as IDESTADO, " +
				"       est."+FcsFactEstadosFacturacionBean.C_FECHAESTADO+" as FECHAESTADO, " +
				"  Decode((Select Count(*)  "+
                "            From Fcs_Facturacionjg       Facpos, "+
                "            Fcs_Fact_Grupofact_Hito Grupos, "+
                "            Fcs_Fact_Grupofact_Hito Gru " +
                "            Where Fac.Idinstitucion = Gru.Idinstitucion "+
                "            And Fac.Idfacturacion = Gru.Idfacturacion "+
                "            And Facpos.Idinstitucion = Grupos.Idinstitucion "+
                "            And Facpos.Idfacturacion = Grupos.Idfacturacion "+
                "            And Facpos.Idinstitucion = Fac.Idinstitucion "+
                "            And Facpos.prevision = Fac.prevision "+
                "            And Facpos.Fechadesde > Fac.Fechadesde "+
                "            And Grupos.Idgrupofacturacion =  Gru.Idgrupofacturacion "+
                "            And Grupos.Idhitogeneral = Gru.Idhitogeneral), 0, '1','0') BORRAPORGRUPO, "  +
                " (Select Decode(Est.Idestadofacturacion, 10,'1',20,'1',60,'1','0') "+   
                "         From Fcs_Fact_Estadosfacturacion Est "+
                "          Where Fac.Idinstitucion = Est.Idinstitucion "+
                "          And Fac.Idfacturacion = Est.Idfacturacion "+                          
                "          And Est.Idordenestado = "+  
                "             	(Select Max(Est2.Idordenestado) "+
                " 		         From Fcs_Fact_Estadosfacturacion Est2 "+
                "   		     Where Est2.Idinstitucion = Est.Idinstitucion "+
                "           	 And Est2.Idfacturacion = Est.Idfacturacion)  And Rownum = 1) BORRARPORESTADO "+                           
				"  from "+FcsFacturacionJGBean.T_NOMBRETABLA+" FAC, " +
				"       "+FcsFactEstadosFacturacionBean.T_NOMBRETABLA+" EST, " +
				"       "+CenInstitucionBean.T_NOMBRETABLA+" INS " +
				" where fac."+FcsFacturacionJGBean.C_IDINSTITUCION+" = EST."+FcsFactEstadosFacturacionBean.C_IDINSTITUCION+" " +
				"   and fac."+FcsFacturacionJGBean.C_IDFACTURACION+" = EST."+FcsFactEstadosFacturacionBean.C_IDFACTURACION+" " +
				"   and est."+FcsFactEstadosFacturacionBean.C_IDORDENESTADO+"="+
				"         (Select Max(est2."+FcsFactEstadosFacturacionBean.C_IDORDENESTADO+") "+
			    "            from "+FcsFactEstadosFacturacionBean.T_NOMBRETABLA+" EST2 " +
				"           where est2."+FcsFactEstadosFacturacionBean.C_IDINSTITUCION+" = est."+FcsFactEstadosFacturacionBean.C_IDINSTITUCION+" " +
				"           and est2."+FcsFactEstadosFacturacionBean.C_IDFACTURACION+" = est."+FcsFactEstadosFacturacionBean.C_IDFACTURACION+" " +
				"       ) " +	                           
				"   and fac."+FcsFacturacionJGBean.C_IDINSTITUCION+" = ins."+CenInstitucionBean.C_IDINSTITUCION+" " +
				"   and fac."+FcsFacturacionJGBean.C_PREVISION+" = '"+ prevision +"'";
			
			//institucion
			String institucion = (String) criterios.get ("idInstitucion");
			if (institucion!=null && !institucion.trim().equals("")) {
				sql +=
				"   and fac."+FcsFacturacionJGBean.C_IDINSTITUCION+" = "+institucion;
			} else {
				sql +=
				"   and fac."+FcsFacturacionJGBean.C_IDINSTITUCION+" IN ("+CenVisibilidad.getVisibilidadInstitucion(idInstitucionLocation)+")";
			}
			
			//estado
			String estado = (String) criterios.get ("idEstado");
			if (estado!=null && !estado.trim().equals("")) {
				sql +=
				"   and est."+FcsFactEstadosFacturacionBean.C_IDESTADOFACTURACION+" = " + estado;
			}
			
			//serie
			String nombre = (String) criterios.get ("nombre");
			if (nombre!=null && !nombre.trim().equals("")) {
				sql +=
				"   and ("+ComodinBusquedas.prepararSentenciaCompleta (nombre.trim(), "fac."+FcsFacturacionJGBean.C_NOMBRE)+") ";
			}
			
			//hitos
			String hito = (String) criterios.get ("hito");
			if (hito!=null && !hito.trim().equals("")) {
				sql +=
				"   and exists (select * " +
				"                 from "+FcsFactGrupoFactHitoBean.T_NOMBRETABLA+" HIT " +
				"                where hit."+FcsFactGrupoFactHitoBean.C_IDFACTURACION+" = fac."+FcsFacturacionJGBean.C_IDFACTURACION+" " +
				"                  and hit."+FcsFactGrupoFactHitoBean.C_IDINSTITUCION+" = fac."+FcsFacturacionJGBean.C_IDINSTITUCION+" " +
				"                  and hit."+FcsFactGrupoFactHitoBean.C_IDHITOGENERAL+" = "+hito+" " +
				"              )";
			}
			
			//fechas
			String fechaDesde = (String) criterios.get ("fechaIni");
			String fechaHasta = (String) criterios.get ("fechaFin");
			if (fechaDesde!=null && !fechaDesde.trim().equals("")) {
				sql +=
				"   and fac."+FcsFacturacionJGBean.C_FECHADESDE+" >= " +
				"       to_date ('"+GstDate.dateFormatoJava(fechaDesde)+"', '"+ClsConstants.DATE_FORMAT_SQL+"')";
			}
			if (fechaHasta!=null && !fechaHasta.trim().equals("")) {
				sql +=
				"   and fac."+FcsFacturacionJGBean.C_FECHAHASTA+" <= " +
				"       to_date ('"+GstDate.dateSuma24hFormatoJava(fechaHasta)+"', '"+ClsConstants.DATE_FORMAT_SQL+"')";
			}
			
			
			
			//order by
			sql +=
				" order by ins."+CenInstitucionBean.C_NOMBRE+", " +
				"          fac."+FcsFacturacionJGBean.C_FECHADESDE+" desc ";
			
			//ejecutando la consulta
			Paginador paginador = new Paginador (sql);
			int totalRegistros = paginador.getNumeroTotalRegistros();
			
			if (totalRegistros==0) {
				paginador=null;
			} else {
				paginador.getNumeroRegistrosPorPagina();
				paginador.obtenerPagina (1);
			}
			return paginador;
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error en getFacturaciones");
		}
	} //getFacturaciones ()
	
	/**
	 * Devuelve un Hashtable con el identificador del estado, la descripcion y la fecha de estado de la facturaci�n  
	 * @param idInstitucion 
	 * @param idFacturacion 
	 * @return bean de estado facturacion 
	 */	
	public Hashtable getEstadoFacturacion(String idInstitucion, String idFacturacion)throws ClsExceptions, SIGAException{
		
		Hashtable salida = null;
		RowsContainer rc = null;
		try{
			String sql = " SELECT "+
	 		  FcsEstadosFacturacionBean.T_NOMBRETABLA+"."+FcsEstadosFacturacionBean.C_IDESTADOFACTURACION + ", "+ 
	 		  UtilidadesMultidioma.getCampoMultidioma(FcsEstadosFacturacionBean.T_NOMBRETABLA+"."+FcsEstadosFacturacionBean.C_DESCRIPCION,this.usrbean.getLanguage())+ ", "+
	 		  " est."+FcsFactEstadosFacturacionBean.C_FECHAESTADO+ " " + FcsFactEstadosFacturacionBean.C_FECHAESTADO +
	 		  " from  "+FcsFactEstadosFacturacionBean.T_NOMBRETABLA+"  est, "+FcsEstadosFacturacionBean.T_NOMBRETABLA+" " + 
			  " where est."+FcsFactEstadosFacturacionBean.C_IDORDENESTADO+" = " +
			  "         (Select Max(est2."+FcsFactEstadosFacturacionBean.C_IDORDENESTADO+") "+
			  "            from "+FcsFactEstadosFacturacionBean.T_NOMBRETABLA+" EST2 " +
			  "           where est2."+FcsFactEstadosFacturacionBean.C_IDINSTITUCION+" = est."+FcsFactEstadosFacturacionBean.C_IDINSTITUCION+" " +
			  "           and est2."+FcsFactEstadosFacturacionBean.C_IDFACTURACION+" = est."+FcsFactEstadosFacturacionBean.C_IDFACTURACION+" " +
			  "       ) " +	                          
			  " and   est."+FcsFactEstadosFacturacionBean.C_IDFACTURACION+" = "+idFacturacion +
			  " and   est."+FcsFactEstadosFacturacionBean.C_IDINSTITUCION+" = "+idInstitucion +
			  " and   est."+FcsFactEstadosFacturacionBean.C_IDESTADOFACTURACION+" = "+ FcsEstadosFacturacionBean.T_NOMBRETABLA +"."+FcsEstadosFacturacionBean.C_IDESTADOFACTURACION;
			
			
			rc = this.find(sql);
			if (rc!=null) {
				if (rc.size()>0) {
					Row fila = (Row) rc.get(0);
					salida = (Hashtable)fila.getRow();
				}
			}
		}

		catch(Exception e) {
			throw new ClsExceptions (e, "Error en getEstadoFacturacion");
		}
		return salida;
	}
	
	/**
	 * Devuelve el bean del grupo de facturacion
	 * @param idInstitucion 
	 * @param idFacturacion  
	 * @return bean de grupo de facturacion
	 */
	public Vector getGruposFacturacion (String idInstitucion, String idFacturacion)throws ClsExceptions, SIGAException
	{
		FcsFactGrupoFactHitoAdm grupoAdm = new FcsFactGrupoFactHitoAdm(this.usrbean);
		ScsGrupoFacturacionBean grupoBean = new ScsGrupoFacturacionBean();
		Vector resultado = new Vector();
		RowsContainer rc = null;
		String consulta =	" select  " + UtilidadesMultidioma.getCampoMultidiomaSimple(ScsGrupoFacturacionBean.T_NOMBRETABLA + "." + ScsGrupoFacturacionBean.C_NOMBRE , this.usrbean.getLanguage())+ " NOMBRE, "+
									" NUL(" + FcsFactGrupoFactHitoBean.T_NOMBRETABLA + "." +	FcsFactGrupoFactHitoBean.C_IDGRUPOFACTURACION + ",0) IDGRUPOFACTURACION " +
							" from " + ScsGrupoFacturacionBean.T_NOMBRETABLA + " , "+ FcsFactGrupoFactHitoBean.T_NOMBRETABLA + " " +
							" where " + ScsGrupoFacturacionBean.T_NOMBRETABLA + "." + ScsGrupoFacturacionBean.C_IDINSTITUCION + "= " + FcsFactGrupoFactHitoBean.T_NOMBRETABLA + "." + FcsFactGrupoFactHitoBean.C_IDINSTITUCION + 
							" and " + ScsGrupoFacturacionBean.T_NOMBRETABLA + "." + ScsGrupoFacturacionBean.C_IDGRUPOFACTURACION + "= " + FcsFactGrupoFactHitoBean.T_NOMBRETABLA + "." + FcsFactGrupoFactHitoBean.C_IDGRUPOFACTURACION + 
							" and " + ScsGrupoFacturacionBean.T_NOMBRETABLA + "." + ScsGrupoFacturacionBean.C_IDINSTITUCION + "=" + idInstitucion +
							" and " + FcsFactGrupoFactHitoBean.T_NOMBRETABLA + "." + FcsFactGrupoFactHitoBean.C_IDFACTURACION + "=" + idFacturacion+
							" order by " + ScsGrupoFacturacionBean.T_NOMBRETABLA + "." + ScsGrupoFacturacionBean.C_NOMBRE+ " "; 
		try{
			rc = this.find(consulta);
			if (rc!=null) {
				if (rc.size()>0) {
					for (int i = 0; i<rc.size(); i++){
						Row fila = (Row) rc.get(i);
						Hashtable registro = (Hashtable)fila.getRow();
						grupoBean = (ScsGrupoFacturacionBean)grupoAdm.hashTableToBean(registro);
						resultado.add(grupoBean);
					}
				}
			}
		}catch(Exception e){
			throw new ClsExceptions (e, "Error en getGrupoFacturacion");
		}
		return resultado;
	}
	
	
	/**
	 * Devuelve un Vector con los criterios de facturacion
	 * @param idInstitucion 
	 * @param idFacturacion  
	 * @return Vector con grupos de facturacion
	 */
	public Vector getCriteriosFacturacion (String idInstitucion, String idFacturacion)throws ClsExceptions, SIGAException
	{
		
		
		Vector vHito = new Vector();
		RowsContainer rc = null;
		String consulta =	" select  " + UtilidadesMultidioma.getCampoMultidiomaSimple(ScsGrupoFacturacionBean.T_NOMBRETABLA + "." + ScsGrupoFacturacionBean.C_NOMBRE,this.usrbean.getLanguage()) + " NOMBRE, " + UtilidadesMultidioma.getCampoMultidiomaSimple(FcsHitoGeneralBean.T_NOMBRETABLA + "." + FcsHitoGeneralBean.C_DESCRIPCION, this.usrbean.getLanguage()) + " DESCRIPCION,"+
							ScsGrupoFacturacionBean.T_NOMBRETABLA + "." + ScsGrupoFacturacionBean.C_IDGRUPOFACTURACION + " IDGRUPOFACTURACION," + FcsHitoGeneralBean.T_NOMBRETABLA + "." + FcsHitoGeneralBean.C_IDHITOGENERAL + " IDHITOGENERAL"+
							", "+FcsFactGrupoFactHitoBean.T_NOMBRETABLA + "." + FcsFactGrupoFactHitoBean.C_FACTCONVENIO+""+
							" from " + FcsFactGrupoFactHitoBean.T_NOMBRETABLA + " , "+ ScsGrupoFacturacionBean.T_NOMBRETABLA + ", " + FcsHitoGeneralBean.T_NOMBRETABLA + " " +
							" where " + FcsFactGrupoFactHitoBean.T_NOMBRETABLA + "." + FcsFactGrupoFactHitoBean.C_IDGRUPOFACTURACION + " = " + ScsGrupoFacturacionBean.T_NOMBRETABLA + "." + ScsGrupoFacturacionBean.C_IDGRUPOFACTURACION + " (+) " + 
							" and " + FcsFactGrupoFactHitoBean.T_NOMBRETABLA + "." + FcsFactGrupoFactHitoBean.C_IDINSTITUCION + "= " + ScsGrupoFacturacionBean.T_NOMBRETABLA + "." + ScsGrupoFacturacionBean.C_IDINSTITUCION + " " + 
							" and " + FcsFactGrupoFactHitoBean.T_NOMBRETABLA + "." + FcsFactGrupoFactHitoBean.C_IDHITOGENERAL + "= " + FcsHitoGeneralBean.T_NOMBRETABLA + "." + FcsHitoGeneralBean.C_IDHITOGENERAL + " " + 
							" and " + FcsFactGrupoFactHitoBean.T_NOMBRETABLA + "." + FcsFactGrupoFactHitoBean.C_IDINSTITUCION + "=" + idInstitucion +
							" and " + FcsFactGrupoFactHitoBean.T_NOMBRETABLA + "." + FcsFactGrupoFactHitoBean.C_IDFACTURACION+ "=" + idFacturacion +
							" order by " + FcsFactGrupoFactHitoBean.T_NOMBRETABLA + "." + FcsFactGrupoFactHitoBean.C_IDHITOGENERAL + " "; 
		try{
			rc = this.find(consulta);
			if (rc!=null) {
				if (rc.size()>0) {
					for (int i = 0; i<rc.size(); i++){
						Row fila = (Row) rc.get(i);
						Hashtable registro = (Hashtable)fila.getRow();
						vHito.add(registro);
					}
				}
			}
		}catch(Exception e){
			throw new ClsExceptions (e, "Error en getHitosGenerales");
		}
		return vHito;
	}
	
	/**
	 * Funcion que recibiendo como parametro una facturacion devuelve un booleano indicando si la facturacion se solapa en<br>
	 * en tiempo y criterios con otra facturacion ya existente.<br>
	 * Los resultado son:<br>
	 * true: si se solapan<br>
	 * false: no se solapan<br>
	 * 
	 * @param beanFacturacion 
	 * @return boolean resultado; <br>
	 * true: si se solapan<br>
	 * false: no se solapan<br>
	 * 
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public boolean existeFacturacionMismoPerdiodo (FcsFacturacionJGBean beanFacturacion, FcsFactGrupoFactHitoBean beanCriterio,Integer valorPcajgActivo)throws ClsExceptions
	{
		
		boolean resultado = false;
		//variables con los resultados de las consultas
		String nFacturaciones = "0", nGrupos ="0";
		//variables con los resultados de las consultas de tipo entero
		int nFactInt=0, nGruInt=0;
		String fechaHasta=null, fechaDesde=null;
		FcsFacturacionJGAdm facturacionAdm = new FcsFacturacionJGAdm (this.usrbean);
		
		//Caso especial: si el hito es de Turnos devolvemos falso
		if ((beanCriterio != null) && beanCriterio.getIdHitoGeneral().intValue() == ClsConstants.HITO_GENERAL_TURNO) {
			return false;
		}
		
		//Caso Normal:
		//Preparamos las fechas:
		//Si provocan excepcione es porque no estan en formato largo sino en corto. En ese caso tomamos directamente su valor del bean:
		try {
			fechaHasta = GstDate.getFormatedDateShort("",beanFacturacion.getFechaHasta());
		} catch (Exception e) {
			fechaHasta = beanFacturacion.getFechaHasta();
		}
		try {
			fechaDesde = GstDate.getFormatedDateShort("",beanFacturacion.getFechaDesde());
		} catch (Exception e) {
			fechaDesde = beanFacturacion.getFechaDesde();
		}
		String consultaFact = "";
		String consultaGrupos = "";
		
		//preparamos las consultas
		//Caso de insertar un nuevo criterio:
		if (beanCriterio == null) {
			
			//la que consulta el n�mero de facturaciones que coinciden en el periodo facturado
			consultaFact = 	" SELECT count(1) as NFACTURACIONES "+
				" FROM " + FcsFacturacionJGBean.T_NOMBRETABLA +
				" WHERE " + FcsFacturacionJGBean.C_REGULARIZACION + " = '"+ ClsConstants.DB_FALSE + "'" +
					" AND " + FcsFacturacionJGBean.C_PREVISION + " = '"+ ClsConstants.DB_FALSE + "'" +								
					" AND ((TO_DATE('" + fechaDesde + "', 'DD/MM/YYYY') >= " + FcsFacturacionJGBean.C_FECHADESDE +
						" AND TO_DATE('" + fechaDesde + "', 'DD/MM/YYYY') <= " + FcsFacturacionJGBean.C_FECHAHASTA + ")" +
						" OR (TO_DATE('" + fechaDesde + "', 'DD/MM/YYYY') <= " + FcsFacturacionJGBean.C_FECHADESDE + 
						" AND TO_DATE('" + fechaHasta +"', 'DD/MM/YYYY') >= " + FcsFacturacionJGBean.C_FECHADESDE + "))" +								
					" AND "+ FcsFacturacionJGBean.C_IDINSTITUCION+"="+beanFacturacion.getIdInstitucion().toString()+
					" AND "+ FcsFacturacionJGBean.C_IDFACTURACION+"<>"+beanFacturacion.getIdFacturacion().toString();	

	    	consultaGrupos = " SELECT COUNT(h." + FcsFactGrupoFactHitoBean.C_IDFACTURACION + ") NGRUPOS " + 
				" FROM " + FcsFactGrupoFactHitoBean.T_NOMBRETABLA + " h, " +
	    			FcsFacturacionJGBean.T_NOMBRETABLA + " fact, " +
	    			FcsFactGrupoFactHitoBean.T_NOMBRETABLA + " h1 " +
				" WHERE h1." + FcsFactGrupoFactHitoBean.C_IDINSTITUCION + " = " + beanFacturacion.getIdInstitucion().toString() +					
					" AND h1." + FcsFactGrupoFactHitoBean.C_IDFACTURACION + " = " + beanFacturacion.getIdFacturacion().toString() +
					
					" AND h." + FcsFactGrupoFactHitoBean.C_IDINSTITUCION + " = h1." + FcsFactGrupoFactHitoBean.C_IDINSTITUCION +
					" AND h." + FcsFactGrupoFactHitoBean.C_IDFACTURACION + " <> h1." + FcsFactGrupoFactHitoBean.C_IDFACTURACION +
					" AND h." + FcsFactGrupoFactHitoBean.C_IDGRUPOFACTURACION + " = h1." + FcsFactGrupoFactHitoBean.C_IDGRUPOFACTURACION +																		
					" AND h." + FcsFactGrupoFactHitoBean.C_IDHITOGENERAL + " = h1." + FcsFactGrupoFactHitoBean.C_IDHITOGENERAL +					
					" AND h." + FcsFactGrupoFactHitoBean.C_IDHITOGENERAL + " <> " + ClsConstants.HITO_GENERAL_TURNO +
					
					" AND fact." + FcsFacturacionJGBean.C_IDINSTITUCION + " = h." + FcsFactGrupoFactHitoBean.C_IDINSTITUCION +
					" AND fact." + FcsFacturacionJGBean.C_IDFACTURACION + " = h." + FcsFactGrupoFactHitoBean.C_IDFACTURACION +
					" AND fact." + FcsFacturacionJGBean.C_PREVISION + " = 0 " +
					" AND fact." + FcsFacturacionJGBean.C_REGULARIZACION + " = 0  " +					
					
					" AND ((TO_DATE('" + fechaDesde + "', 'DD/MM/YYYY') >= fact." + FcsFacturacionJGBean.C_FECHADESDE +
						" AND TO_DATE('" + fechaDesde + "', 'DD/MM/YYYY') <= fact." + FcsFacturacionJGBean.C_FECHAHASTA + ")" +
						" OR (TO_DATE('" + fechaDesde + "', 'DD/MM/YYYY') <= fact." + FcsFacturacionJGBean.C_FECHADESDE + 
						" AND TO_DATE('" + fechaHasta +"', 'DD/MM/YYYY') >= fact." + FcsFacturacionJGBean.C_FECHADESDE + "))";					
			
		//Caso de modificar una facturacion:
		} else {
	    	consultaGrupos = " SELECT COUNT(h." + FcsFactGrupoFactHitoBean.C_IDFACTURACION + ") NGRUPOS " + 
				" FROM " + FcsFactGrupoFactHitoBean.T_NOMBRETABLA + " h, " +	    			
					FcsFacturacionJGBean.T_NOMBRETABLA + " fact " +
				" WHERE h." + FcsFactGrupoFactHitoBean.C_IDINSTITUCION + " = " +beanFacturacion.getIdInstitucion().toString() +
					" AND h." + FcsFactGrupoFactHitoBean.C_IDFACTURACION + " <> "+ beanFacturacion.getIdFacturacion().toString() +
					" AND h." + FcsFactGrupoFactHitoBean.C_IDGRUPOFACTURACION + " = " + beanCriterio.getIdGrupoFacturacion().toString() +
					" AND h." + FcsFactGrupoFactHitoBean.C_IDHITOGENERAL + " = " + beanCriterio.getIdHitoGeneral().toString() +
					" and h." + FcsFactGrupoFactHitoBean.C_IDHITOGENERAL + " <> " + ClsConstants.HITO_GENERAL_TURNO +
					" AND h." + FcsFactGrupoFactHitoBean.C_IDINSTITUCION + " = fact." + FcsFacturacionJGBean.C_IDINSTITUCION +
					" AND h." + FcsFactGrupoFactHitoBean.C_IDFACTURACION + " = fact." + FcsFacturacionJGBean.C_IDFACTURACION ;
	    			if(valorPcajgActivo==CajgConfiguracion.TIPO_CAJG_TXT_ALCALA)
	    				if(beanCriterio.getFactConvenio()!=null)
	    					consultaGrupos += " AND nvl(h." + FcsFactGrupoFactHitoBean.C_FACTCONVENIO + ","+beanCriterio.getFactConvenio()+") = " + beanCriterio.getFactConvenio();
	    				else
	    					consultaGrupos += " AND h." + FcsFactGrupoFactHitoBean.C_FACTCONVENIO + " is not null";
					
					consultaGrupos += " AND fact." + FcsFacturacionJGBean.C_PREVISION + " = 0 " +
					" AND fact." + FcsFacturacionJGBean.C_REGULARIZACION + " = 0  " +
					" AND ((TO_DATE('" + fechaDesde + "', 'DD/MM/YYYY') >= fact." + FcsFacturacionJGBean.C_FECHADESDE +
						" AND TO_DATE('" + fechaDesde + "', 'DD/MM/YYYY') <= fact." + FcsFacturacionJGBean.C_FECHAHASTA + ")" +
						" OR (TO_DATE('" + fechaDesde + "', 'DD/MM/YYYY') <= fact." + FcsFacturacionJGBean.C_FECHADESDE + 
						" AND TO_DATE('" + fechaHasta +"', 'DD/MM/YYYY') >= fact." + FcsFacturacionJGBean.C_FECHADESDE + "))";				
		}
		
		//hashtable para recoger el resultado de la consulta
		Hashtable htResultadoFact = new Hashtable();
		Hashtable htResultadoGrup = new Hashtable();
		
		if (!consultaFact.equals("")) {
			//ejecutamos la consulta de las facturaciones
			try{
				htResultadoFact = (Hashtable)((Vector)facturacionAdm.selectGenerico(consultaFact)).get(0);
				
				//recogemos el resultado
				nFacturaciones = (String)htResultadoFact.get("NFACTURACIONES");
				
			}catch(Exception e){
				ClsLogging.writeFileLogWithoutSession("Error FcsFacturacionJGAdm.existeFacturacionMismoPerdiodo(), SQL:"+ consultaFact, 10);
				throw new ClsExceptions(e,"Error en selectGenerico");
			}
			//pasamos os resultados a int
			if ((nFacturaciones!=null)&&(!nFacturaciones.equals("")))
					nFactInt = Integer.parseInt(nFacturaciones);
		} else {
		    nFactInt=1;
		}
		
		//ejecutamos la consulta de los grupos
		try{
			htResultadoGrup = (Hashtable)((Vector)facturacionAdm.selectGenerico(consultaGrupos)).get(0);
			
			//recogemos el resultado
			nGrupos = (String)htResultadoGrup.get("NGRUPOS");
			
		}catch(Exception e){
			throw new ClsExceptions(e,"Error en selectGenerico");
		}
		
		
		if ((nGrupos!=null)&&(!nGrupos.equals("")))
			nGruInt = Integer.parseInt(nGrupos);
		
		//si hay varias facturaciones en el mismo periodo y con los mismo criterios de facturacion, se solapan
		resultado = (nFactInt > 0 ) && (nGruInt>0);
		
		return resultado;
	}
	
	
	/**
	 * Prepara los datos, para posteriormente insertarlos en la base de datos. La preparaci�n consiste en calcular el
	 * identificador de la FACTURAJG que se va a insertar. Es necesario que en la Hashtable de entrada haya una Key IDINSTITUCION con 
	 * el valor del identificador de institucion. Si en la Hash de entrada se le pasa los campos FechaDesde y FechaHasta los cambia a formato Aplicacion.   
	 * 
	 * @param entrada Hashtable con los campos a insertar. De tipo "Hashtable". 
	 * @return Hashtable con los campos adaptados.
	 */
	
	public static Hashtable prepararInsert (Hashtable entrada)throws ClsExceptions 
	{
		
		RowsContainer rc = null;	
		
		try { rc = new RowsContainer(); }
		catch(Exception e) { e.printStackTrace(); }
		
		String sql ="SELECT (MAX(IDFACTURACION) + 1) AS IDFACTURACION FROM " + FcsFacturacionJGBean.T_NOMBRETABLA + " where " + FcsFacturacionJGBean.C_IDINSTITUCION + "=" + (String)entrada.get("IDINSTITUCION");	
		try {		
			if (rc.query(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();			
				if (prueba.get("IDFACTURACION").equals("")) {
					entrada.put(FcsFacturacionJGBean.C_IDFACTURACION,"1");
				}
				else entrada.put(FcsFacturacionJGBean.C_IDFACTURACION,(String)prueba.get(FcsFacturacionJGBean.C_IDFACTURACION));								
			}
		}	
		catch (ClsExceptions e) {		
			throw new ClsExceptions (e, "Error al ejecutar el 'prepararInsert' en B.D.");		
		}
		
		//si el has tambien contenia las keys de FECHADESDE y FECHAHASTA, las convierte al formato correcto para insertar
		//en otro caso no falla
		try{
			entrada.put(FcsFacturacionJGBean.C_FECHADESDE, GstDate.getApplicationFormatDate("",(String)entrada.get(FcsFacturacionJGBean.C_FECHADESDE)));
		}catch(Exception e){}
		try{
			entrada.put(FcsFacturacionJGBean.C_FECHAHASTA, GstDate.getApplicationFormatDate("",(String)entrada.get(FcsFacturacionJGBean.C_FECHAHASTA)));
		}catch(Exception e){}
		return entrada;
	}
	
	/**
	 * Comprueba si una facturacion tiene ya una regularizacion 
	 * con estado diferente a Lista para Consejo
	 * @param la institucion 
	 * @param la facturacion
	 * @return boolean true si es cierto
	 */
	
	public boolean checkFacturacionRegularizacion (String idInstitucion, String idFacturacion)throws ClsExceptions 
	{
		RowsContainer rc = null;
		boolean salida = false;
		try { rc = new RowsContainer(); }
		catch(Exception e) { e.printStackTrace(); }
		
		String sql ="SELECT "+FcsFacturacionJGBean.C_IDFACTURACION+" FROM " + FcsFacturacionJGBean.T_NOMBRETABLA + 
		" WHERE "+FcsFacturacionJGBean.C_IDINSTITUCION+"="+idInstitucion +  
		" AND "+FcsFacturacionJGBean.C_REGULARIZACION+"='" + ClsConstants.DB_TRUE + "'" + 
		" AND (SELECT "+FcsFactEstadosFacturacionBean.C_IDESTADOFACTURACION+" FROM "+FcsFactEstadosFacturacionBean.T_NOMBRETABLA+
		"	   WHERE "+FcsFactEstadosFacturacionBean.T_NOMBRETABLA+"."+FcsFactEstadosFacturacionBean.C_IDINSTITUCION+"="+idInstitucion+
		"	   AND "+FcsFactEstadosFacturacionBean.T_NOMBRETABLA+"."+FcsFactEstadosFacturacionBean.C_IDFACTURACION+"="+idFacturacion +
		"  AND "+FcsFactEstadosFacturacionBean.C_IDORDENESTADO+" = " +
		"         (Select Max(est2."+FcsFactEstadosFacturacionBean.C_IDORDENESTADO+") "+
		"            from "+FcsFactEstadosFacturacionBean.T_NOMBRETABLA+" EST2 " +
		"				   WHERE EST2."+FcsFactEstadosFacturacionBean.C_IDINSTITUCION+"="+FcsFactEstadosFacturacionBean.T_NOMBRETABLA+"."+FcsFactEstadosFacturacionBean.C_IDINSTITUCION+
		"				   AND EST2."+FcsFactEstadosFacturacionBean.C_IDFACTURACION+"="+FcsFactEstadosFacturacionBean.T_NOMBRETABLA+"."+FcsFactEstadosFacturacionBean.C_IDFACTURACION+"))<>"+ESTADO_FACTURACION.ESTADO_FACTURACION_LISTA_CONSEJO.getCodigo();
		                          
		try {		
			if (rc.query(sql)) {
				if (rc!=null && rc.size()>0) {
					salida = true;
				}
			}
		}	
		catch (ClsExceptions e) {		
			throw new ClsExceptions (e, "Error al consultar BD");		
		}
		return salida;
	}
	
	/**
	 * Comprueba si una facturacion tiene ya una regularizacion en estado abierta 
	 * @param la institucion 
	 * @param la facturacion
	 * @return boolean true si es cierto
	 */
	
	public boolean checkTieneRegularizacionAbierta (String idInstitucion, String idFacturacion)throws ClsExceptions 
	{
		RowsContainer rc = null;
		boolean salida = false;
		try { rc = new RowsContainer(); }
		catch(Exception e) { e.printStackTrace(); }
		
		String sql ="SELECT "+FcsFacturacionJGBean.C_IDFACTURACION+" FROM " + FcsFacturacionJGBean.T_NOMBRETABLA + 
		" WHERE "+FcsFacturacionJGBean.C_IDINSTITUCION+"="+idInstitucion +  
		" AND "+FcsFacturacionJGBean.C_REGULARIZACION+"='" + ClsConstants.DB_TRUE + "'" + 
		" AND (SELECT "+FcsFactEstadosFacturacionBean.C_IDESTADOFACTURACION+" FROM "+FcsFactEstadosFacturacionBean.T_NOMBRETABLA+
		"	   WHERE "+FcsFactEstadosFacturacionBean.T_NOMBRETABLA+"."+FcsFactEstadosFacturacionBean.C_IDINSTITUCION+"="+idInstitucion+
		"	   AND "+FcsFactEstadosFacturacionBean.T_NOMBRETABLA+"."+FcsFactEstadosFacturacionBean.C_IDFACTURACION+"="+idFacturacion +
		"  AND "+FcsFactEstadosFacturacionBean.C_IDORDENESTADO+" = " +
		"         (Select Max(est2."+FcsFactEstadosFacturacionBean.C_IDORDENESTADO+") "+
		"            from "+FcsFactEstadosFacturacionBean.T_NOMBRETABLA+" EST2 " +
		"				   WHERE EST2."+FcsFactEstadosFacturacionBean.C_IDINSTITUCION+"="+FcsFactEstadosFacturacionBean.T_NOMBRETABLA+"."+FcsFactEstadosFacturacionBean.C_IDINSTITUCION+
		"				   AND EST2."+FcsFactEstadosFacturacionBean.C_IDFACTURACION+"="+FcsFactEstadosFacturacionBean.T_NOMBRETABLA+"."+FcsFactEstadosFacturacionBean.C_IDFACTURACION+"))="+ESTADO_FACTURACION.ESTADO_FACTURACION_ABIERTA.getCodigo();		                          
		try {		
			if (rc.query(sql)) {
				if (rc!=null && rc.size()>0) {
					salida = true;
				}
			}
		}	
		catch (ClsExceptions e) {		
			throw new ClsExceptions (e, "Error al consultar BD");		
		}
		return salida;
	}
	
	/**
	 * Inserta una regularizacion 
	 * @param la institucion 
	 * @param la facturacion
	 * @return boolean true si es cierto
	 */
	
	public Integer insertarRegularizacion(String idInstitucion, String idFacturacion, String nombre) throws ClsExceptions, SIGAException {
		Integer nuevoIdFacturacion = null;

		// control de estado
		if (this.checkFacturacionRegularizacion(idInstitucion, idFacturacion)) {
			throw new SIGAException("messages.factSJCS.error.regularizacionAbierta");
		}

		// busco a que facturacion pertenece
		Hashtable criterios = new Hashtable();
		criterios.put(FcsFacturacionJGBean.C_IDFACTURACION, idFacturacion);
		criterios.put(FcsFacturacionJGBean.C_IDINSTITUCION, idInstitucion);
		Vector res = this.select(criterios);
		FcsFacturacionJGBean bean = null;
		if (res != null && res.size() > 0) {
			bean = (FcsFacturacionJGBean) res.get(0);
		}

		if (bean != null) {
			// creo la nueva facturacion 
			FcsFacturacionJGBean regularizaBean = new FcsFacturacionJGBean();
			nuevoIdFacturacion = this.getNuevoId(idInstitucion);
			regularizaBean.setIdFacturacion(nuevoIdFacturacion);
			regularizaBean.setIdFacturacion_regulariza(bean.getIdFacturacion());
			regularizaBean.setIdInstitucion(bean.getIdInstitucion());
			regularizaBean.setFechaDesde(bean.getFechaDesde());
			regularizaBean.setFechaHasta(bean.getFechaHasta());
			regularizaBean.setNombre(nombre);
			regularizaBean.setRegularizacion(ClsConstants.DB_TRUE);
			regularizaBean.setPrevision(bean.getPrevision());
		/** CR - en local esto no funciona, por tanto como es lo mismo se inserta a null 
			regularizaBean.setImporteEJG(UtilidadesNumero.getDouble("0"));
			regularizaBean.setImporteGuardia(UtilidadesNumero.getDouble("0"));
			regularizaBean.setImporteOficio(UtilidadesNumero.getDouble("0"));
			regularizaBean.setImporteSOJ(UtilidadesNumero.getDouble("0"));
			regularizaBean.setImporteTotal(UtilidadesNumero.getDouble("0"));
		**/			
			Hashtable hashRegularizacion = this.beanToHashTable(regularizaBean);
			// la inserto
			if (!this.insert(hashRegularizacion)) {
				throw new SIGAException(this.getError());
			}
			
			// inserto los grupos hito
			RowsContainer rc = null;
			String consulta =	" SELECT IDINSTITUCION, IDFACTURACION, IDGRUPOFACTURACION, IDHITOGENERAL FROM FCS_FACT_GRUPOFACT_HITO " +
								" WHERE IDINSTITUCION=" + idInstitucion + " AND IDFACTURACION="+idFacturacion;
			try{
				rc = this.find(consulta);
				if (rc != null) {
					FcsFactGrupoFactHitoAdm admGrupoHito = new FcsFactGrupoFactHitoAdm(this.usrbean);
					if (rc.size() > 0) {
						for (int i = 0; i < rc.size(); i++) {
							Row fila = (Row) rc.get(i);
							Hashtable registro = (Hashtable) fila.getRow();
							registro.put("IDFACTURACION", nuevoIdFacturacion);
							if (!admGrupoHito.insert(registro)) {
								throw new SIGAException(this.getError());
							}
						}
					}
				}
			} catch (Exception e) {
				throw new ClsExceptions(e, "Error al consultar grupos");
			}

			// inserto el estado
			FcsFactEstadosFacturacionAdm admEstado = new FcsFactEstadosFacturacionAdm(this.usrbean);
			FcsFactEstadosFacturacionBean beanEstado = new FcsFactEstadosFacturacionBean();
			beanEstado.setIdFacturacion(nuevoIdFacturacion);
			beanEstado.setIdInstitucion(new Integer(idInstitucion));
			beanEstado.setFechaEstado("SYSDATE");
			beanEstado.setIdOrdenEstado(1);// al inicio sera un uno ya que sera el primero
			beanEstado.setIdEstadoFacturacion(new Integer(ESTADO_FACTURACION.ESTADO_FACTURACION_ABIERTA.getCodigo()));
			if (!admEstado.insert(beanEstado)) {
				throw new SIGAException(this.getError());
			}
		}
		return nuevoIdFacturacion;
	}
	
	
	/**
	 * Obtiene un nuevo id de facturacion 
	 * @param la institucion 
	 * @return Integer id nuevo
	 */
	
	public Integer getNuevoId (String idInstitucion)throws ClsExceptions 
	{
		RowsContainer rc = null;
		Integer salida = null;
		try { rc = new RowsContainer(); }
		catch(Exception e) { e.printStackTrace(); }
		
		String sql ="SELECT MAX("+FcsFacturacionJGBean.C_IDFACTURACION+")+1 AS MAXIMO FROM " + FcsFacturacionJGBean.T_NOMBRETABLA + 
		" WHERE "+FcsFacturacionJGBean.C_IDINSTITUCION+"="+idInstitucion;  
		
		try {		
			if (rc.query(sql)) {
				if (rc!=null && rc.size()>0) {
					Row fila = (Row)rc.get(0);
					Hashtable aux = fila.getRow();
					String maximo = (String) aux.get("MAXIMO");
					if (maximo.equals("")) {
						salida = new Integer(1);
					} else {
						salida = new Integer(maximo);
					}
				}
			}
		}	
		catch (ClsExceptions e) {		
			throw new ClsExceptions (e, "Error al consultar BD");		
		}
		return salida;
	}

	/**
	 * Funcion que devuelve los idPersona de los colegiados que interviene en una facturacion,
	 * en Actuaciones Designas, Asistencias, EJG's, Guardias y/o SOJ's  
	 * 
	 * Devuelve Vector de Hashtables con IDPERSONA de cada colegiado
	 * 
	 * @param idInstitucion
	 * @param idFacturacion
	 * @return
	 */
	public Vector getColegiadosFacturables (String idInstitucion, String idFacturacion) throws ClsExceptions
	{
		//donde devolveremos el resultado
		Vector resultado = new Vector();
		//select a ejecutar
		String consulta = 	" SELECT DISTINCT " + FcsFactActuacionAsistenciaBean.C_IDPERSONA +  
							" FROM " + FcsFactActuacionAsistenciaBean.T_NOMBRETABLA +
							" WHERE " + FcsFactActuacionAsistenciaBean.C_IDINSTITUCION + "=" + idInstitucion +
							" AND " + FcsFactActuacionAsistenciaBean.C_IDFACTURACION + "=" + idFacturacion + 
							" UNION " + 
							" SELECT DISTINCT " + FcsFactActuacionDesignaBean.C_IDPERSONA +  
							" FROM " + FcsFactActuacionDesignaBean.T_NOMBRETABLA +
							" WHERE " + FcsFactActuacionDesignaBean.C_IDINSTITUCION + "=" + idInstitucion +
							" AND " + FcsFactActuacionDesignaBean.C_IDFACTURACION + "=" + idFacturacion + 
							" UNION " + 
							" SELECT DISTINCT " + FcsFactAsistenciaBean.C_IDPERSONA +
							" FROM " + FcsFactAsistenciaBean.T_NOMBRETABLA +
							" WHERE " + FcsFactAsistenciaBean.C_IDINSTITUCION + "=" + idInstitucion +
							" AND " + FcsFactAsistenciaBean.C_IDFACTURACION + "=" + idFacturacion + 
							" UNION " + 
							" SELECT DISTINCT " + FcsFactEjgBean.C_IDPERSONA +
							" FROM " + FcsFactEjgBean.T_NOMBRETABLA +
							" WHERE " + FcsFactEjgBean.C_IDINSTITUCION + "=" + idInstitucion +
							" AND " + FcsFactEjgBean.C_IDFACTURACION + "=" + idFacturacion + 
							" UNION " + 
							" SELECT DISTINCT " + FcsFactGuardiasColegiadoBean.C_IDPERSONA +
							" FROM " + FcsFactGuardiasColegiadoBean.T_NOMBRETABLA +
							" WHERE " + FcsFactGuardiasColegiadoBean.C_IDINSTITUCION + "=" + idInstitucion +
							" AND " + FcsFactGuardiasColegiadoBean.C_IDFACTURACION + "=" + idFacturacion + 
							" UNION " + 
							" SELECT DISTINCT " + FcsFactSojBean.C_IDPERSONA + 
							" FROM " + FcsFactSojBean.T_NOMBRETABLA +
							" WHERE " + FcsFactSojBean.C_IDINSTITUCION + "=" + idInstitucion +
							" AND " + FcsFactSojBean.C_IDFACTURACION + "=" + idFacturacion + " ";
		try
		{
			//ejecutamos la consulta
			resultado = (Vector)this.selectGenerico(consulta);
		}
		catch(Exception e)
		{
			throw new ClsExceptions (e,"Error en FcsFacturacionJG.getColegiadosFacturables()" + consulta);
		}
		return resultado; 
	}
	
	/**
	 * Funcion que devuelve las Actuaciones Designas, Asistencias, EJG's, Guardias y/o SOJ's 
	 * a facturar por un colegiado en particular. Devuelve un vector con los siguientes datos en cada posicion:
	 * 
	 * 0.-turnos de oficio<br>
	 * 1.-guardias presenciales<br>
	 * 2.-asistencias<br>
	 * 3.-actuaciones<br>
	 * 4.-expedientesSoj<br>
	 * 5.-expedientesEjg<br>
	 * 
	 * @param idInstitucion
	 * @param idFacturacion
	 * @param idPersona
	 * @return
	 */
	public Vector getDetallePorColegiado (String idInstitucion, String idFacturacion, String idPersona )
	{
		//resultado final
		Vector resultado = new Vector();
		//turnos de oficio
		Vector actuacionDesigna = new Vector();
		//guardias presenciales
		Vector guardiasPresenciales = new Vector();
		//asistencias
		Vector asistencias = new Vector();
		//actuaciones
		Vector actuaciones = new Vector();
		//expedientesSoj
		Vector expedientesSoj = new Vector();
		//expedientesEjg
		Vector expedientesEjg = new Vector();
		
		//llenamos cada uno de los Vectores parciales
		try{
			FcsFactActuacionDesignaAdm actDesAdm = new FcsFactActuacionDesignaAdm (this.usrbean);
			actuacionDesigna = (Vector)actDesAdm.getTurnosOficio(idInstitucion,idFacturacion,idPersona);
		}catch(Exception e){
			actuacionDesigna = null;
		}
		try{
			FcsFactGuardiasColegiadoAdm guarColAdm = new FcsFactGuardiasColegiadoAdm(this.usrbean);
			guardiasPresenciales = (Vector)guarColAdm.getGuardiasPresenciales(idInstitucion,idFacturacion,idPersona);
		}catch(Exception e){
			guardiasPresenciales = null;
		}
		try{
			FcsFactAsistenciaAdm asisAdm = new FcsFactAsistenciaAdm(this.usrbean);
			asistencias = (Vector)asisAdm.getAsistencias(idInstitucion,idFacturacion,idPersona);
		}catch(Exception e){
			asistencias = null;
		}
		try{
			FcsFactActuacionAsistenciaAdm actAsisAdm = new FcsFactActuacionAsistenciaAdm (this.usrbean);
			actuaciones = (Vector)actAsisAdm.getActuacionAsistencias(idInstitucion,idFacturacion,idPersona);
		}catch(Exception e){
			actuaciones = null;
		}
		try{
			FcsFactSojAdm sojAdm = new FcsFactSojAdm(this.usrbean);
			expedientesSoj = (Vector)sojAdm.getExpedientesSoj(idInstitucion,idFacturacion,idPersona);
		}catch(Exception e){
			expedientesSoj = null;
		}
		try{
			FcsFactEjgAdm ejgAdm = new FcsFactEjgAdm (this.usrbean);
			expedientesEjg = (Vector)ejgAdm.getExpedientesEjg(idInstitucion,idFacturacion,idPersona);
		}catch(Exception e){
			expedientesEjg = null;
		}
		
		//los metemos todos en el Vector resultado
		resultado.add(0, actuacionDesigna);
		resultado.add(1, guardiasPresenciales);
		resultado.add(2, asistencias);
		resultado.add(3, actuaciones); 
		resultado.add(4, expedientesSoj);
		resultado.add(5, expedientesEjg);
		
		return resultado; 
	}
	
	/**
	 * Devuelve en un Vector de Hashtables, registros de la BD que son resultado de ejecutar la select.  
	 * @param String select: consulta SQL del SELECT almacenada en un String.
	 * @return Vector con Hashtables. Cada Hashtable es una fila del resultado del select en la base de datos.
	 * @throws ClsExceptions
	 */
	public Vector selectGenerico(String select) throws ClsExceptions {
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			if (rc.query(select)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable) fila.getRow(); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en FcsFactSojAdm.selectGenerico(). Consulta SQL:"+select);
		}
		return datos;	
	}
	
	/**
	 * Funcion que crea un fichero con para el impreso 190 con los datos de IRPF de las personas durante un anho  
	 * @param fichero para asignar la salida
	 * @param anio a tratar
	 * @param telefono de contacto de la institucion
	 * @param nombre de persona de contacto de la institucion
	 * @param codigo de provincia de la institucion aplicable a los receptores
	 * @param idInstitucion a tratar
	 * @return fichero generado
	 */
	public File generarImpreso190 (GenerarImpreso190Form miform, String idInstitucion) throws SIGAException, ClsExceptions
	{

		String consulta1 = "";
		Vector resultado1 = null;
		Hashtable importes = new Hashtable();
		Hashtable irpfs = new Hashtable();
		Hashtable datos = new Hashtable();
		Hashtable clavesM190 = new Hashtable();
		double importeTotal = 0;
		double irpfTotal = 0;
		File fichero = null;
		File ficheroErrores = null;
		File fichero190 = null;
		boolean  hayError = false;
		Vector vErrores = new Vector();
		
		try
		{
			String anio = miform.getAnio();
			String telefonoContacto = miform.getTelefonoContacto();
			String nombreContacto = miform.getNombreContacto();
			String apellido1Contacto = miform.getApellido1Contacto();
			String apellido2Contacto = miform.getApellido2Contacto();
			String soporte = miform.getSoporte();
			
			// obtengo el parametro
			Hashtable datosInstitucion = getDatosInstitucion(idInstitucion);

			//CR7 - Ahora la provincia se coger� de la direccion de facturacion del colegio.
			CenDireccionesAdm direccionesAdm = new CenDireccionesAdm(this.usrbean);
			CenDireccionesBean direccionesBean = direccionesAdm.obtenerDireccionTipo((String)datosInstitucion.get("IDPERSONA"), idInstitucion, String.valueOf(ClsConstants.TIPO_DIRECCION_FACTURACION));			
			String codigoProvincia = direccionesBean.getIdProvincia();
			
			//select a ejecutar: obtener pagos cerrados en el anho de entrada
			consulta1 =
				"SELECT distinct IDPAGOSJG " +
				"  FROM FAC_ABONO " +
				" WHERE FAC_ABONO.IDINSTITUCION = "+ idInstitucion +" " +
				"   AND to_char(FAC_ABONO.FECHA, 'YYYY') = "+ anio +" " +
				"   AND FAC_ABONO.IDPAGOSJG is not null";

			//ejecutamos la consulta
			resultado1 = (Vector)this.selectGenerico(consulta1);
			
			if (resultado1!=null && resultado1.size()>0) {

				String sPagos = ""; 
				for (int i = 0; i < resultado1.size(); i++) {
					sPagos += UtilidadesHash.getString((Hashtable)resultado1.get(i), "IDPAGOSJG");
					if (i < resultado1.size() - 1) sPagos += ",";
				}
				
				StringBuffer select = new StringBuffer();
				select.append(" SELECT IDPERSONAIMPRESO, Sum(importeirpf) As TOTALIMPORTEIRPF, Sum(importepagado) As TOTALIMPORTEPAGADO, CLAVEM190 ");
				select.append("  FROM ( " );
				select.append("   SELECT " + FcsPagoColegiadoBean.C_IDPERDESTINO + " AS IDPERSONAIMPRESO, ");
				select.append(" 	(-1) * " + FcsPagoColegiadoBean.C_IMPIRPF + "  AS importeirpf, ");
				select.append(		FcsPagoColegiadoBean.C_IMPOFICIO + " + ");
				select.append(      FcsPagoColegiadoBean.C_IMPASISTENCIA + " + ");
				select.append(      FcsPagoColegiadoBean.C_IMPEJG + " + ");
				select.append(      FcsPagoColegiadoBean.C_IMPSOJ + " + ");
				select.append(      FcsPagoColegiadoBean.C_IMPMOVVAR + " AS importepagado, ");
				select.append(    " NVL((SELECT " + ScsRetencionesBean.C_CLAVEM190 );
				select.append(		   " FROM  " + ScsRetencionesBean.T_NOMBRETABLA + " Ret ");
				select.append(		   " WHERE Ret." + ScsRetencionesBean.C_IDRETENCION + " = Pag." + FcsPagoColegiadoBean.C_IDRETENCION + "), 'G01') AS CLAVEM190 ");
				select.append("   FROM "  + FcsPagoColegiadoBean.T_NOMBRETABLA + " Pag ");
				select.append("   WHERE " + FcsPagoColegiadoBean.C_IDINSTITUCION + " = " + idInstitucion);
				select.append("     AND "   + FcsPagoColegiadoBean.C_IDPAGOSJG + " IN (" + sPagos + ")");
				select.append("     AND "   + FcsPagoColegiadoBean.C_PORCENTAJEIRPF + " > 0 ");
				select.append("     AND ("  + FcsPagoColegiadoBean.C_IMPOFICIO + " > 0 or ");
				select.append( 				  FcsPagoColegiadoBean.C_IMPASISTENCIA + " > 0 or ");
				select.append( 				  FcsPagoColegiadoBean.C_IMPEJG + " > 0 or ");
				select.append( 				  FcsPagoColegiadoBean.C_IMPSOJ + " > 0 ) ");
				select.append(" 		) " );
				select.append(" GROUP BY IDPERSONAIMPRESO, CLAVEM190");

				Vector vIRPF = (Vector) this.selectGenerico(select.toString());
				if (vIRPF != null && vIRPF.size() > 0) {					
					for (int j = 0; j < vIRPF.size(); j++) {
						Hashtable aux = (Hashtable) vIRPF.get(j);
						String idPersona = UtilidadesHash.getString(aux, "IDPERSONAIMPRESO");
						Double importeIRPFPersona = UtilidadesHash.getDouble(aux, "TOTALIMPORTEIRPF");
						Double importePagadoPersona = UtilidadesHash.getDouble(aux, "TOTALIMPORTEPAGADO");
						String claveM190 = UtilidadesHash.getString(aux, "CLAVEM190");
						// Controlamos aqui que el IRPF no sea nulo (SL) para no acumular su importe
						if (!importeIRPFPersona.equals(new Double(0.0))){ 

							// Datos de la persona
							Hashtable hashPersona = getDatosPersona(idPersona);
							
							//Control para ver que tenemos todos los datos necesarios para generar el fichero:
							String tipoIdentificacion = (String)hashPersona.get("IDTIPOIDENTIFICACION");
							String nombre = (String)hashPersona.get("NOMBRE");
							String apellidos1 = (String)hashPersona.get("APELLIDOS1");
							String apellidos2 = (String)hashPersona.get("APELLIDOS2");
							String numIdentificacion = (String)hashPersona.get("NIDENTIFICACION");
							hashPersona.put("IDPERSONA", idPersona);
							boolean errorPersona = false;
							Hashtable hashError = (Hashtable)hashPersona.clone();
							String mensajeError = "";
							if (nombre==null || nombre.equals("")) {
								mensajeError = "ERROR: el nombre es necesario";
								hayError = true;	
							}							
							if (apellidos1==null || apellidos1.equals("")) {
								if (mensajeError.equals(""))
									mensajeError += "ERROR: el apellidos1 es necesario";
								else
									mensajeError += ", el apellidos1 es necesario";
								hayError = true;
							}
							if (hayError){
								hashError.put("ERROR", mensajeError);
								vErrores.add(hashError);
							}
							
							//Si no hay error lo a�ado y recalculo irpfs e importes:
							if (!hayError) {
								datos.put(idPersona+claveM190, hashPersona);
	
								// IRPF
								irpfs.put(idPersona+claveM190, importeIRPFPersona);
								irpfTotal += importeIRPFPersona.doubleValue();
	
								// Importe
								importes.put(idPersona+claveM190, importePagadoPersona);
								importeTotal += importePagadoPersona.doubleValue();		
								
								//Claves Modelo 190
								clavesM190.put(idPersona+claveM190, claveM190);
							}
						}
					}
				}
			}				
		
			// realizo la generaci�n del directorio y del fichero:
			String sNombreFichero = miform.getNombreFicheroOriginal();
			 if (miform.getNombreFicheroOriginal()!="" && miform.getNombreFicheroOriginal().indexOf(".190")<0){
	    	   sNombreFichero = miform.getNombreFicheroOriginal()+".190";
			 }  
	    	String sDirectorio = getPathTemporal(idInstitucion);
	    	String sCamino = sDirectorio + File.separator + idInstitucion;
	    	
	    	//LOG_IMPRESO190_[IDINSTITUCION]_[A�O]_[FECHA_EJECUCION]
	        Calendar cal = Calendar.getInstance();
            Date hora = cal.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMMMM-dd_hh:mm:ss");
            String fechaEjecucion = sdf.format(hora);
    		String sNombreLog = "IMPRESO190_"+anio+".log.xls";
			
	    	// creo el directorio si no existe:
    		FileHelper.mkdirs(sCamino);

	    	String sNombreCompletoFichero = sCamino + File.separator + sNombreFichero;
	    	String sNombreFicheroErrorLog = sCamino + File.separator + sNombreLog;	    	
	    	String sNombreFicheroZip = sCamino + File.separator + sNombreFichero+".zip"; 
			
			
			//Generamos el fichero 190 si no ha habido ningun error:
			if (!hayError)
				fichero = generarModelo190(sNombreCompletoFichero, anio, telefonoContacto, nombreContacto, 
					       apellido1Contacto, apellido2Contacto, soporte, codigoProvincia, 
					       irpfs, importes, datosInstitucion, datos, irpfTotal, importeTotal,clavesM190);				
			else {
				fichero190 = generarModelo190(sNombreCompletoFichero, anio, telefonoContacto, nombreContacto, 
					       					  apellido1Contacto, apellido2Contacto, soporte, codigoProvincia, 
											  irpfs, importes, datosInstitucion, datos, irpfTotal, importeTotal,clavesM190);
				ficheroErrores = generarLogImpreso190(sNombreFicheroErrorLog, vErrores);
				
//				FileInputStream inputImpreso190 = null;
//				FileInputStream inputFicheroErrores = null;
				ZipOutputStream outZIP = null;
				FileInputStream in = null;
				
				try {
					//Genero un .ZIP con los ficheros de log de errores y el informe 190 de los que he podido generar:
//					inputImpreso190 = new FileInputStream(fichero190);
//					inputFicheroErrores = new FileInputStream(ficheroErrores);
					
			        outZIP = new ZipOutputStream(new FileOutputStream(sNombreFicheroZip));
					byte[] buf = new byte[1024];
	
					for (int i=0; i<2; i++) {
						String fileName = "";
						in = null;
						if (i==0) { //Para a�adir el impreso 190
							fileName = sNombreFichero;
							in = new FileInputStream(fichero190);
						} else { //Para a�adir el log
							fileName = sNombreLog;
							in = new FileInputStream(ficheroErrores);
						}
						
						outZIP.putNextEntry(new ZipEntry(fileName));
			              
		                // Transfer bytes from the file to the ZIP file
			            int len;
			            while ((len = in.read(buf)) > 0) {
			            	outZIP.write(buf, 0, len);
			            }
			              
			            // Complete the entry
			            outZIP.closeEntry();
			            in.close();
					}
					outZIP.close();				
					
					fichero = new File(sNombreFicheroZip);
				} catch (Exception e) {
/*					if (inputImpreso190!=null)
						inputImpreso190.close();
					if (inputFicheroErrores!=null)
						inputFicheroErrores.close();
*/					if (outZIP!=null)
						outZIP.close();
					if (in!=null)
						in.close();
					throw new ClsExceptions (e,"Error en FcsFacturacionJG.generarImpreso190() al crear el .ZIP"); 
				}
			}	
		} catch(SIGAException e) {
			throw e;
		} catch(Exception e) {
			throw new ClsExceptions (e,"Error en FcsFacturacionJG.generarImpreso190()");
		}
		return fichero; 
	}
	
	
	
	///////////////////////////////////////////
	// RECORRIDO PARA FORMAR EL FICHERO
	// 1.- MODELO 190: REGISTRO DE RETENEDOR
	// 2.- MODELO 190: REGISTROS DE PERCEPCION
	///////////////////////////////////////////	
	private File generarModelo190(String nombreFichero, String anio, String telefonoContacto, String nombreContacto, 
			String apellido1Contacto, String apellido2Contacto, String soporte, String codigoProvincia, 
			Hashtable irpfs, Hashtable importes, Hashtable datosInstitucion, Hashtable datos
			, double irpfTotal, double importeTotal,Hashtable clavesM190) throws SIGAException, ClsExceptions {
	
		BufferedWriter bw = null;
		File fichero = null;
		
		try {
			fichero = new File(nombreFichero);
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fichero),ClsConstants.IMPRESO190_ENCODING));
			
			if (irpfs.size()==0) {
				throw new SIGAException("messages.factSJCS.noRetenciones190");
			}
				
			// registro unico de institucion
			//
			// MODELO 190: REGISTRO DE RETENEDOR
			//
			String linea = "";
			int nreg=importes.size();
			linea += "1"; //1: tipo registro
			linea += "190"; //2-4: modelo declaracion
			linea += formatea(anio,4,true); //5-8 ejercicio (anyo)
			linea += formatea(datosInstitucion.get("NIDENTIFICACION"),9,true); //9-17: NIF del declarante
			
			String nombreInstitucion = datosInstitucion.get("NOMBREINSTITUCION").toString();
			nombreInstitucion = nombreInstitucion.toUpperCase();
			nombreInstitucion = UtilidadesString.quitarAcentos((nombreInstitucion));
			nombreInstitucion = UtilidadesString.validarModelo190(nombreInstitucion);
			linea += formatea(nombreInstitucion,40,false); //18-57: apellidos y nombre, denominaci�n o raz�n social del declarante (nombre de la institucion)
			
			linea += soporte; //58: tipo de soporte
			linea += formatea(telefonoContacto,9,true); //59-67: telefono de contacto
			
			apellido1Contacto = apellido1Contacto.toUpperCase();
			apellido2Contacto = apellido2Contacto.toUpperCase();
			nombreContacto = nombreContacto.toUpperCase();
			apellido1Contacto = UtilidadesString.quitarAcentos(apellido1Contacto);
			apellido1Contacto = UtilidadesString.validarModelo190(apellido1Contacto);
			apellido2Contacto = UtilidadesString.quitarAcentos(apellido2Contacto);
			apellido2Contacto = UtilidadesString.validarModelo190(apellido2Contacto);
			nombreContacto    = UtilidadesString.quitarAcentos(nombreContacto);
			nombreContacto    = UtilidadesString.validarModelo190(nombreContacto);
			linea += formatea(apellido1Contacto + " " + apellido2Contacto + " " + nombreContacto,40,false); //68-107: nombre de contacto
			
			linea += "170"+relleno("0",10); //108-120: numero identificativo de la declaracion
			linea += rellenoPosiciones(" ",121,122); //121-122: complementaria
			linea += rellenoPosiciones("0",123,135); //123-135: numero identificativo declaracion anterior
			linea += formatea(String.valueOf(nreg),9,true);; //136-144: numero de percepciones (DE MOMENTO SOLO LOS IRPFS)
			
			//145-160: importe total
			Vector valor = desdoblarDouble(new Double(importeTotal));
			linea += formatea(valor.get(0),1,false); //145: signo
			linea += formatea(valor.get(1),13,true); //146-158: entera
			linea += formatea(valor.get(2),2,true); //159-160: decimal
			
			//161-175: retencion total			
			valor = desdoblarDouble(new Double (irpfTotal));
			linea += formatea(valor.get(1),13,true); //161-173: entera
			linea += formatea(valor.get(2),2,true); //174-175: decimal
			
			linea += rellenoPosiciones(" ",176,225); //176-225: correo electronico de contacto (ahora mismo no lo tenemos, pero en el cambio de 2017 dejaron este hueco que se puede usar)
			linea += rellenoPosiciones(" ",226,487); //226-487: relleno
			linea += rellenoPosiciones(" ",488,500); //488-500: sello electronico
			
			// escribo
			// cambio a formato DOS
			linea += "\r\n";
			bw.write(linea);
			//bw.newLine();

			//
			// MODELO 190: REGISTROS DE PERCEPCION
			//
			Enumeration claves = importes.keys();
			while (claves.hasMoreElements()) {
				Object o = claves.nextElement();
				String persona = (String)o;
				Hashtable datosPersonaRegistro = (Hashtable) datos.get(persona);
				
				// registro unico de persona
				linea = "";
				linea += "2"; //1: tipo registro
				linea += "190"; //2-4: modelo declaracion
				linea += formatea(anio,4,true); //5-8: ejercicio (anyo)
				linea += formatea(datosInstitucion.get("NIDENTIFICACION"),9,true); //9-17: NIF declarante
				linea += formatea(datosPersonaRegistro.get("NIDENTIFICACION"),9,true); //18-26: NIF perceptor
				linea += rellenoPosiciones(" ",27,35); //27-35: NIF representante legal
				
				String apellido = (String)datosPersonaRegistro.get("APELLIDOS1");
				//Si es sociedad sin abreviatura se deja solo el nombre
				String nombrePerceptor = (String) datosPersonaRegistro.get(apellido.equals("#NA") ? "NOMBRE" : "NOMBREPERSONA");
				nombrePerceptor = nombrePerceptor.toUpperCase();  
				nombrePerceptor = UtilidadesString.quitarAcentos(nombrePerceptor);
				nombrePerceptor = UtilidadesString.validarModelo190(nombrePerceptor);
				linea += formatea(nombrePerceptor,40,false); //36-75: apellidos y nombre o denominaci�n del perceptor
				
				linea += formatea(codigoProvincia,2,true); //76-77: provincia
				linea += (String)clavesM190.get(persona); //78-80: clave+subclave de percepcion ("G01" casi siempre)
				
				//Obtengo el IRPF y el importe para esta persona:
				Double personaImporteTotalDouble = (Double)importes.get(persona);
				Double personaIrpfTotalDouble = (Double)irpfs.get(persona);
				// Si no hay retenciones "no debe aparecer" en el archivo
				if(!personaIrpfTotalDouble.equals(new Double(0))){
				
					//81-94: percepciones dinerarias
					valor = desdoblarDouble(personaImporteTotalDouble); 
					linea += formatea(valor.get(0),1,false); //81: signo
					linea += formatea(valor.get(1),11,true); //82-92 entera
					linea += formateaDecimal(valor.get(2),2); //93-94 decimal
					
					//95-107: retenciones practicadas
					valor = desdoblarDouble(personaIrpfTotalDouble); 
					linea += formatea(valor.get(1),11,true); //95-105: entera
					linea += formateaDecimal(valor.get(2),2); //106-107: decimal
					
					//108-147: percepciones en especie (no aplican)
					linea += " "; //108: signo de percepcion en especie
					linea += rellenoPosiciones("0",109,121); //109-121: valor de percepcion en especie
					linea += rellenoPosiciones("0",122,134); //122-134: valor de percepcion en especie
					linea += rellenoPosiciones("0",135,147); //135-147: valor de percepcion en especie
					
					linea += rellenoPosiciones("0",148,151); //148-151: ejercicio devengo
					
					boolean esCeutaOMelilla = (codigoProvincia.equals(ClsConstants.CODIGO_PROVINCIA_CEUTA)||codigoProvincia.equals(ClsConstants.CODIGO_PROVINCIA_MELILLA));
					linea += esCeutaOMelilla ? "1":"0"; //152: caso de ceuta y melilla
					
					//153-254: Datos adicionales no necesarios para nuestro caso
					linea += rellenoPosiciones("0",153,156); //153-156: anyo nacimiento
					linea += "0"; //157: situacion familiar
					linea += rellenoPosiciones(" ",158,166); //158-166: nif conyuge
					linea += "0"; //167: discapacidad
					linea += "0"; //168: contrato o relacion
					linea += "0"; //169: TITULAR UNIDAD DE CONVIVENCIA
					linea += "0"; //170: movilidad geografica
					linea += rellenoPosiciones("0",171,183); //171-183: reducciones aplicables
					linea += rellenoPosiciones("0",184,196); //184-196: gastos deducibles
					linea += rellenoPosiciones("0",197,209); //197-209: pensiones compensatorias
					linea += rellenoPosiciones("0",210,222); //210-222: anualidades por alimentos
					linea += rellenoPosiciones("0",223,228); //223-228: hijos y otros descendientes
					linea += rellenoPosiciones("0",229,240); //229-240: hijos y otros descendientes con discapacidad
					linea += rellenoPosiciones("0",241,244); //241-244: ascendientes
					linea += rellenoPosiciones("0",245,250); //245-250: ascendientes con discapacidad
					linea += rellenoPosiciones("0",251,253); //251-253: n� de hijos
					linea += "0"; //254: prestamo vivienda
					
					//255-281: percepciones dinerarias derivadas de incapacidad laboral (no aplican)
					linea += " "; //255: Signo de las percepciones dinerarias derivadas de incapacidad laboral
					linea += rellenoPosiciones("0",256,281); //256-281: importe de las percepciones dinerarias derivadas de incapacidad laboral
					//282-321: percepciones en especie derivadas de incapacidad laboral (no aplican)
					linea += " "; //282: Signo de las percepciones en especie derivadas de incapacidad laboral
					linea += rellenoPosiciones("0",283,321); //283-321: importe de las percepciones en especie derivadas de incapacidad laboral
					
					linea += "0"; //322: COMPLEMENTO AYUDA PARA LA INFANCIA
					linea += rellenoPosiciones("0",323,387); //323-387: RETENCIONES E INGRESOS A CUENTA INGRESADOS EN EL ESTADO, EN LAS	DIPUTACIONES FORALES DEL PAIS VASCO Y EN LA COMUNIDAD FORAL DE NAVARRA 
					linea += rellenoPosiciones(" ",388,500); //323-500: blancos 
					
					// cambio a formato DOS
					linea += "\r\n";
					
					// escribiendo la linea
					bw.write(linea);
				}
			}
			//bw.flush();
			//bw.newLine();
			bw.close();
		} catch (IOException e) {
			try {
			if (bw!=null) 
				bw.close();
			} catch (Exception e2) {
				throw new ClsExceptions (e2,"Error en FcsFacturacionJG.generarModelo190()");	
			}
		} catch (SIGAException siga){
			throw siga;
		} catch (Exception e) {
			throw new ClsExceptions (e,"Error en FcsFacturacionJG.generarModelo190()");	
		}
		return fichero;
	}
	
	
	// formatea un dato a una longitud rellenando por la izquierda a ceros 
	// o por la derecha a blancos en funcion de si es numerico para el impreso 190
	private String formatea (Object datoOrig, int longitud, boolean numerico) throws ClsExceptions {
		String salida= "";
		if (datoOrig==null) {
			if (numerico) {
				salida = relleno("0",longitud);  
			} else {
				salida = relleno(" ",longitud);
			}
		} else {
			String dato = datoOrig.toString();
			if (dato.length()==0) {
				if (numerico) {
					salida = relleno("0",longitud);  
				} else {
					salida = relleno(" ",longitud);
				}
			} else
			if (dato.length()>longitud) {
				// mayor
				if (numerico) {
					salida = dato.substring(dato.length() - longitud, dato.length());  
				} else {
					salida = dato.substring(0, longitud);
				}
			} else
			if (dato.length()<longitud) {
				// menor
				if (numerico) {
					salida = relleno("0",longitud - dato.length()) + dato;  
				} else {
					salida = dato + relleno(" ",longitud - dato.length());
				}
			} else {
				// es igual
				salida = dato;
			}
		}	
		
		salida = salida.toUpperCase();
		salida = UtilidadesString.quitarAcentos(salida);
		return salida;	
	}

	
	/**
	 * Rellena con ceros a la izquierda.
	 * @param datoOrig
	 * @param longitud
	 * @return
	 * @throws ClsExceptions
	 */
	private String formateaDecimal (Object datoOrig, int longitud) throws ClsExceptions {
		String salida= datoOrig.toString();
		if (datoOrig==null) {
			salida += relleno("0",longitud);  
		} 
		else {
			salida += relleno("0",longitud - salida.length());
		}
		return salida;
	}
	
	
	private String rellenoPosiciones (String caracter, int inicio, int fin) throws ClsExceptions {
		return relleno(caracter, Math.abs(fin-inicio)+1);
	}
	/** 
	 * Crea un string de longitud x relleno de caracteres para el impreso 190
	 * @param caracter
	 * @param longitud
	 * @return
	 * @throws ClsExceptions
	 */
	private String relleno (String caracter, int longitud) throws ClsExceptions {
		String salida= "";
		
		for (int i=0;i<longitud;i++) {
			salida += caracter;
		}
		
		return salida;	
	}

	// quita los acentos
	private String quitarAcentos (String cadena) throws ClsExceptions {
		
		cadena = cadena.replaceAll("�","A");
		cadena = cadena.replaceAll("�","E");
		cadena = cadena.replaceAll("�","I");
		cadena = cadena.replaceAll("�","O");
		cadena = cadena.replaceAll("�","U");
		
		cadena = cadena.replaceAll("�","A");
		cadena = cadena.replaceAll("�","E");
		cadena = cadena.replaceAll("�","I");
		cadena = cadena.replaceAll("�","O");
		cadena = cadena.replaceAll("�","U");
/*		
		cadena = cadena.replaceAll("�","A");
		cadena = cadena.replaceAll("�","E");
		cadena = cadena.replaceAll("�","I");
		cadena = cadena.replaceAll("�","O");
		cadena = cadena.replaceAll("�","U");
*/
		cadena = cadena.replaceAll("�","A");
		cadena = cadena.replaceAll("�","E");
		cadena = cadena.replaceAll("�","I");
		cadena = cadena.replaceAll("�","O");
		cadena = cadena.replaceAll("�","U");
		
		return cadena;	
	}

	
	// vector con el signo, la parte entera y la parte decimal (dos posiciones decimales) para el impreso 190
	private Vector desdoblarDouble (Double valor) throws ClsExceptions {
		Vector salida= new Vector();
		
		String signo = " ";
		
		if (valor == null) {
			salida.add("");
			salida.add("");
			salida.add("");
		} else {
			String sValor = null;
			if (valor.doubleValue()<0) {
				sValor = String.format("%.2f",valor).replace(",", ".");
				// le quitamos el signo
				sValor = sValor.substring(1,sValor.length());
				signo = "N";
			} else {
				sValor = String.format("%.2f",valor).replace(",", ".");
			}
				
			salida.add(signo);

			int pos = sValor.indexOf(".");
			if (pos!=-1) {
				// tiene punto
				salida.add(sValor.substring(0,pos));
				salida.add(sValor.substring(pos+1,sValor.length()));
			} else {
				// NO tiene punto
				salida.add(sValor);
				salida.add("");
			}
		}
		
		return salida;	
	}

	/**
	 * Funcion que obtiene los datos de una persona para el impreso 190  
	 * @param idPersona
	 * @return Hashtable con los datos
	 */
	public Hashtable getDatosPersona (String idPersona) throws ClsExceptions
	{
		//donde devolveremos el resultado
		Hashtable resultado = null;
		//select a ejecutar
		String consulta = 	" SELECT PER."+CenPersonaBean.C_NIFCIF+" AS NIDENTIFICACION, " +
							" PER."+CenPersonaBean.C_IDTIPOIDENTIFICACION+" AS IDTIPOIDENTIFICACION, " +
							" PER."+CenPersonaBean.C_APELLIDOS1+" AS APELLIDOS1," +
							" PER."+CenPersonaBean.C_APELLIDOS2+" AS APELLIDOS2," +
							" PER."+CenPersonaBean.C_NOMBRE+" AS NOMBRE," +
							" PER."+CenPersonaBean.C_APELLIDOS1+" || ' ' || PER."+CenPersonaBean.C_APELLIDOS2+" || ', ' || PER."+CenPersonaBean.C_NOMBRE+" AS NOMBREPERSONA " +
							" FROM   "+CenPersonaBean.T_NOMBRETABLA+" PER" +
							" WHERE  PER."+CenPersonaBean.C_IDPERSONA+"="+idPersona;
		try
		{
			//ejecutamos la consulta
			Vector res = (Vector)this.selectGenerico(consulta);
			if (res!=null && res.size()>0) {
				resultado = (Hashtable) res.get(0);
			}
		}
		catch(Exception e)
		{
			throw new ClsExceptions (e,"Error en FcsFacturacionJG.getDatosPersona()" + consulta);
		}
		return resultado; 
	}
	
	/**
	 * Funcion que obtiene los datos de una institucion para el impreso 190  
	 * @param idInstitucion
	 * @return Hashtable con los datos
	 */
	private Hashtable getDatosInstitucion (String idInstitucion) throws ClsExceptions
	{
		//donde devolveremos el resultado
		Hashtable resultado = null;
		//select a ejecutar
		String consulta = 	" SELECT  PER."+CenPersonaBean.C_NIFCIF+" AS NIDENTIFICACION, " +  
									" PER."+CenPersonaBean.C_IDPERSONA+" AS IDPERSONA, " +
									/** INC-2569*/    
									" PER."+CenInstitucionBean.C_NOMBRE+" AS NOMBREINSTITUCION " +
							" FROM   "+CenInstitucionBean.T_NOMBRETABLA+" INS, "+CenPersonaBean.T_NOMBRETABLA+" PER " + 
							" WHERE  INS."+CenInstitucionBean.C_IDPERSONA+" = PER."+CenPersonaBean.C_IDPERSONA+" " +
							" AND    INS."+CenInstitucionBean.C_IDINSTITUCION+"=" + idInstitucion;
		try
		{
			//ejecutamos la consulta
			Vector res = (Vector)this.selectGenerico(consulta);
			if (res!=null && res.size()>0) {
				resultado = (Hashtable) res.get(0);
			}
		}
		catch(Exception e)
		{
			throw new ClsExceptions (e,"Error en FcsFacturacionJG.getDatosInstitucion()" + consulta);
		}
		return resultado; 
	}
	
	/**
	 * Devuelve el Total Pagado para una Institucion y un idFacturacion.
	 * @param idInstitucion
	 * @param idFacturacion
	 * @return
	 * @throws ClsExceptions
	 */
	public String getTotalPagado(String idInstitucion, String idFacturacion) throws ClsExceptions {
		String consulta="", salida="0";		
		
		consulta = " Select SUM("+FcsPagosJGBean.C_IMPORTEPAGADO+") AS "+FcsPagosJGBean.C_IMPORTEPAGADO+
			       " FROM "+FcsPagosJGBean.T_NOMBRETABLA+
				   " WHERE "+FcsPagosJGBean.C_IDINSTITUCION+" = "+idInstitucion+
				   " AND "+FcsPagosJGBean.C_IDFACTURACION+" = "+idFacturacion;
		try {
			Vector VResultado = this.selectGenerico(consulta);
			salida = (String)((Hashtable)VResultado.elementAt(0)).get(FcsPagosJGBean.C_IMPORTEPAGADO);		
			if (salida.equals(""))
				salida = "0";
		} catch (Exception e) {
			salida = "0";
		}
		return salida;
	}

	/**
	 * Devuelve el Total Facturado para una Institucion y un idFacturacion.
	 * @param idInstitucion
	 * @param idFacturacion
	 * @return
	 * @throws ClsExceptions
	 */
	public String getTotalFacturado(String idInstitucion, String idFacturacion) throws ClsExceptions {
		String consulta="", salida="0";
		
		consulta = " Select "+FcsFacturacionJGBean.C_IMPORTETOTAL+
			       " FROM "+FcsFacturacionJGBean.T_NOMBRETABLA+
				   " WHERE "+FcsFacturacionJGBean.C_IDINSTITUCION+" = "+idInstitucion+
				   " AND "+FcsFacturacionJGBean.C_IDFACTURACION+" = "+idFacturacion;
		try {
			Vector VResultado = this.selectGenerico(consulta);
			salida = (String)((Hashtable)VResultado.elementAt(0)).get(FcsFacturacionJGBean.C_IMPORTETOTAL);
			if (salida.equals(""))
				salida = "0";
		} catch (Exception e) {
			salida = "0";
		}
		return salida;
	}
	/**
	 * Devuelve un vector con las previsiones buscadas 
	 * @param criterios - hashtable con los criteros de busqueda 
	 * @return vector con el resultado 
	 */	
	public Vector getPrevisiones (Hashtable criterios, String idInstitucionLocation)throws ClsExceptions, SIGAException{
		Vector v = new Vector();
		RowsContainer rc = null;
		try{
			String sql = " SELECT "+
			 FcsFacturacionJGBean.T_NOMBRETABLA+"."+FcsFacturacionJGBean.C_IDINSTITUCION + "," + 
			 CenInstitucionBean.T_NOMBRETABLA+"."+CenInstitucionBean.C_ABREVIATURA+ "," + 
			 FcsFacturacionJGBean.T_NOMBRETABLA+"."+FcsFacturacionJGBean.C_IDFACTURACION + "," +  
			 FcsFacturacionJGBean.T_NOMBRETABLA+"."+FcsFacturacionJGBean.C_FECHADESDE +   "," +
			 FcsFacturacionJGBean.T_NOMBRETABLA+"."+FcsFacturacionJGBean.C_FECHAHASTA +   "," +
			 FcsFacturacionJGBean.T_NOMBRETABLA+"."+FcsFacturacionJGBean.C_NOMBRE +   "," +
			 "DECODE(" + FcsFacturacionJGBean.T_NOMBRETABLA+"."+FcsFacturacionJGBean.C_REGULARIZACION + ",'" + ClsConstants.DB_TRUE + "','Si','No') as " + FcsFacturacionJGBean.C_REGULARIZACION + " " +
 			 " FROM   " + FcsFacturacionJGBean.T_NOMBRETABLA + ", " + CenInstitucionBean.T_NOMBRETABLA + 
			 " WHERE  " + FcsFacturacionJGBean.T_NOMBRETABLA + "." + FcsFacturacionJGBean.C_IDINSTITUCION + "=" + CenInstitucionBean.T_NOMBRETABLA + "."+CenInstitucionBean.C_IDINSTITUCION +  
			 " AND    " + FcsFacturacionJGBean.T_NOMBRETABLA + "." + FcsFacturacionJGBean.C_PREVISION + "='" + ClsConstants.DB_TRUE + "'" +
			 " AND " + CenInstitucionBean.T_NOMBRETABLA + "."+CenInstitucionBean.C_IDINSTITUCION + "=" + getIDInstitucion();  
			 
			 
//			 serie
			 String nombre = (String) criterios.get("nombre");
			 if (nombre!=null && !nombre.trim().equals("")) {
		     		  sql+= " AND ("+ComodinBusquedas.prepararSentenciaCompleta(nombre.trim(),FcsFacturacionJGBean.T_NOMBRETABLA+"."+FcsFacturacionJGBean.C_NOMBRE )+") ";
		     }

//			 hitos
			 String hito = (String) criterios.get("hito");
			 if (hito!=null && !hito.trim().equals("")) {
				 sql += " AND EXISTS ( select "+FcsFactGrupoFactHitoBean.T_NOMBRETABLA+"."+FcsFactGrupoFactHitoBean.C_IDHITOGENERAL+
				 " 		  from  "+FcsFactGrupoFactHitoBean.T_NOMBRETABLA + 
				 " 		  where "+FcsFactGrupoFactHitoBean.T_NOMBRETABLA+"."+FcsFactGrupoFactHitoBean.C_IDFACTURACION+" = "+ FcsFacturacionJGBean.C_IDFACTURACION +
				 " 		  and   "+FcsFactGrupoFactHitoBean.T_NOMBRETABLA+"."+FcsFactGrupoFactHitoBean.C_IDINSTITUCION+" = "+ FcsFacturacionJGBean.C_IDINSTITUCION +
				 " 		  and   "+FcsFactGrupoFactHitoBean.T_NOMBRETABLA+"."+FcsFactGrupoFactHitoBean.C_IDHITOGENERAL+" = "+ hito +
				 " 		  )";
		     }

//			 fechas
			 String fechaDesde = (String) criterios.get("fechaDesde");
			 String fechaHasta = (String) criterios.get("fechaHasta");
			 if (fechaDesde!=null && !fechaDesde.trim().equals("")){								 
				sql +=" AND " +
				FcsFacturacionJGBean.T_NOMBRETABLA +"."+ FcsFacturacionJGBean.C_FECHADESDE + ">= TO_DATE ('" + GstDate.dateFormatoJava(fechaDesde) + "', '"+ClsConstants.DATE_FORMAT_SQL+"')";
			 }							
			 if (fechaHasta!=null && !fechaHasta.trim().equals("")){								 
				sql +=" AND " +
				FcsFacturacionJGBean.T_NOMBRETABLA +"."+ FcsFacturacionJGBean.C_FECHAHASTA + "<= TO_DATE ('" + GstDate.dateSuma24hFormatoJava(fechaHasta) + "', '"+ClsConstants.DATE_FORMAT_SQL+"')";
			 }							

			 
    		sql+= " ORDER BY " +FcsFacturacionJGBean.T_NOMBRETABLA+"."+FcsFacturacionJGBean.C_FECHADESDE +   " DESC "; 
			
			rc = this.findNLS(sql);
			if (rc!=null) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow(); 
					if (registro != null) 
						v.add(registro);
				}
			}
		}

		catch(Exception e) {
			throw new ClsExceptions (e, "Error en getPrevisiones");
		}
		return v;
	}
	
	public Hashtable getImportesTotalesFacturacion (String idInstitucion, String idFacturacion) 
	{
		try {
			String sql = " SELECT " + FcsFacturacionJGBean.C_IMPORTEOFICIO + ", " + 
									  FcsFacturacionJGBean.C_IMPORTEGUARDIA + ", " + 
									  FcsFacturacionJGBean.C_IMPORTEEJG + ", " + 
									  FcsFacturacionJGBean.C_IMPORTESOJ + ", " +
									  FcsFacturacionJGBean.C_IMPORTETOTAL + 
						 " FROM "   + FcsFacturacionJGBean.T_NOMBRETABLA +
						 " WHERE "  + FcsFacturacionJGBean.C_IDINSTITUCION + " = " + idInstitucion+  
						 " AND "    + FcsFacturacionJGBean.C_IDFACTURACION + " = " + idFacturacion;
	
			Vector v = this.selectGenerico(sql);
			if ((v != null) && (v.size() != 1)) 
				return null;
			return (Hashtable)v.get(0);
		}
		catch (Exception e) {
			return null;
		}
	}

	public Hashtable getImportesTotalesPagosRealizados (String idInstitucion, String idFacturacion) 
	{
		try {
			String sql = " SELECT SUM(" + FcsPagosJGBean.C_IMPORTEOFICIO  + ") AS " + FcsPagosJGBean.C_IMPORTEOFICIO + ", " +
								" SUM(" + FcsPagosJGBean.C_IMPORTEGUARDIA + ") AS " + FcsPagosJGBean.C_IMPORTEGUARDIA + ", " +
								" SUM(" + FcsPagosJGBean.C_IMPORTEEJG     + ") AS " + FcsPagosJGBean.C_IMPORTEEJG + ", " +
								" SUM(" + FcsPagosJGBean.C_IMPORTESOJ     + ") AS " + FcsPagosJGBean.C_IMPORTESOJ + ", " +
								" SUM(" + FcsPagosJGBean.C_IMPORTEPAGADO  + ") AS " + FcsPagosJGBean.C_IMPORTEPAGADO + 
						 " FROM "   + FcsPagosJGBean.T_NOMBRETABLA +
						 " WHERE "  + FcsPagosJGBean.C_IDINSTITUCION + " = " + idInstitucion+  
						 " AND "    + FcsPagosJGBean.C_IDFACTURACION + " = " + idFacturacion;
	
			Vector v = this.selectGenerico(sql);
			if ((v != null) && (v.size() != 1)) 
				return null;
			return (Hashtable)v.get(0);
		}
		catch (Exception e) {
			return null;
		}
	}

	public Hashtable getImportesTotalesPagoActual (String idInstitucion, String idPago) 
	{
		try {
			String sql = " SELECT " + FcsPagosJGBean.C_IMPORTEOFICIO + ", " +
									  FcsPagosJGBean.C_IMPORTEGUARDIA + ", " +
									  FcsPagosJGBean.C_IMPORTEEJG + ", " + 
									  FcsPagosJGBean.C_IMPORTESOJ + ", " +
									  FcsPagosJGBean.C_IMPORTEPAGADO + 
						 " FROM "   + FcsPagosJGBean.T_NOMBRETABLA +
						 " WHERE "  + FcsPagosJGBean.C_IDINSTITUCION + " = " + idInstitucion+  
						 " AND "    + FcsPagosJGBean.C_IDPAGOSJG + " = " + idPago;
	
			Vector v = this.selectGenerico(sql);
			if ((v != null) && (v.size() != 1)) 
				return null;
			return (Hashtable)v.get(0);
		}
		catch (Exception e) {
			return null;
		}
	}

	/**
	 * Devuelvo un String con nDecimales decimales redondeando hacia arriba y sin comas (de la parte de los miles, millones,...):
	 * @param importe
	 * @param nDecimales
	 * @return
	 */
	private static String formatearDouble(double importe, int nDecimales) {
		//Preparo un objeto para realizar el formateo
		NumberFormat formateo = NumberFormat.getNumberInstance();
		formateo.setMaximumFractionDigits(nDecimales);
		formateo.setMinimumFractionDigits(nDecimales);
		String salida = formateo.format(importe);
		salida = UtilidadesString.replaceAllIgnoreCase(salida,",","");
		salida = UtilidadesString.replaceAllIgnoreCase(salida,".","");
		//Devuelvo la salida con nDecimales decimales redondeando hacia arriba y sin comas: 
		return salida;
	}

	private String getPathTemporal(String idInstitucion) throws ClsExceptions {
		GenParametrosAdm admParam = new GenParametrosAdm(this.usrbean);
		String valor = admParam.getValor(idInstitucion,"FCS",ClsConstants.PATH_IMPRESO190,null);
		return valor;
	}
	
	private File generarLogImpreso190(String nombreFichero, Vector vErrores) throws ClsExceptions {
		BufferedWriter bw = null;
		File fichero = null;
		
		try {
			fichero = new File(nombreFichero);
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fichero),ClsConstants.IMPRESO190_ENCODING));
			
            Calendar cal = Calendar.getInstance();
            Date hora = cal.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd-hh:mm:ss");
            String horaActual = sdf.format(hora);
            //final String SEPARADOR = ",";
            
            String linea = "ERROR"+ClsConstants.SEPARADOR+"FECHA EJECUCION"+ClsConstants.SEPARADOR+"IDPERSONA"+ClsConstants.SEPARADOR+"NUMERO IDENTIFICACION"+ClsConstants.SEPARADOR+"NOMBRE"+ClsConstants.SEPARADOR+"APELLIDOS1"+ClsConstants.SEPARADOR+"APELLIDOS2"+ClsConstants.SEPARADOR+"DESCRIPCION ERROR";
			//Escribo:
			// cambio a formato DOS
			linea += "\r\n";
			bw.write(linea);
			//bw.newLine();
            
			Iterator iter = vErrores.iterator();
			while (iter.hasNext()) {
				Hashtable hashPersona = (Hashtable)iter.next();
				String stipoIdentificacion = (String)hashPersona.get("IDTIPOIDENTIFICACION");
				String numeroIdentificacion = (String)hashPersona.get("NIDENTIFICACION");
				String tipoIdentificacion = getTipoIdentificacion(Integer.parseInt(stipoIdentificacion));
				String nombre = (String)hashPersona.get("NOMBRE");
				String apellidos1 = (String)hashPersona.get("APELLIDOS1");
				String apellidos2 = (String)hashPersona.get("APELLIDOS2");
				String idPersona = (String)hashPersona.get("IDPERSONA");			
				String mensajeError = (String)hashPersona.get("DESCRIPCION_ERROR");

				//Construyo la linea de error del tipo:
				// ERROR [Fecha traza] [idpersona][numero identificacion][nombre y apellidos] [descripcion del error]				
				linea = "[ERROR]"+ClsConstants.SEPARADOR+horaActual+ClsConstants.SEPARADOR+idPersona+ClsConstants.SEPARADOR+numeroIdentificacion+ClsConstants.SEPARADOR+nombre+ClsConstants.SEPARADOR+apellidos1+ClsConstants.SEPARADOR+apellidos2+ClsConstants.SEPARADOR+mensajeError;
				
				//Escribo:
				// cambio a formato DOS
				linea += "\r\n";
				bw.write(linea);
				//bw.newLine();
			}

			bw.close();
		} catch (IOException e) {
			try {
			if (bw!=null) 
				bw.close();
			} catch (Exception e2) {
				throw new ClsExceptions (e2,"Error en FcsFacturacionJG.generarLogImpreso190()");	
			}
			throw new ClsExceptions (e,"Error en FcsFacturacionJG.generarLogImpreso190()");	
		} catch (Exception e) {
			throw new ClsExceptions (e,"Error en FcsFacturacionJG.generarLogImpreso190()");	
		}
		return fichero;
	}
	
	private String getTipoIdentificacion(int tipo){
		String sal = "";
		
		switch(tipo) {
			case ClsConstants.TIPO_IDENTIFICACION_NIF: sal="NIF";
			case ClsConstants.TIPO_IDENTIFICACION_CIF: sal="CIF";
			case ClsConstants.TIPO_IDENTIFICACION_OTRO: sal="OTRO";
			case ClsConstants.TIPO_IDENTIFICACION_PASAPORTE: sal="PASAPORTE";
			case ClsConstants.TIPO_IDENTIFICACION_TRESIDENTE: sal="NIE";
		}
		
		return sal;
	}
	
	public Hashtable obtenerComunes (String idInstitucion,
			 String facturaciones,
			 String idioma) throws ClsExceptions
	{
		//Controles generales
		Hashtable hashDatosObtenidos;
		
		//Consulta que se hara
		String sql = 
			"select ins.nombre as NOMBRE_INSTITUCION, " +
			"       F_SIGA_GETDIRECCIONCLIENTE(ins.idinstitucion, ins.idpersona, 3, 3) as POBLACION_INSTITUCION, " +
			"       ins_con.nombre as NOMBRE_CONSEJO, " +
			"       F_SIGA_GETDIRECCIONCLIENTE(ins_con.idinstitucion, ins_con.idpersona, 3, 3) as POBLACION_CONSEJO, " +
			"       F_SIGA_GETNOMBRESFACTURACIONES(" + idInstitucion + ",'"+ facturaciones + "') as NOMBRE_FACTURACION," +
			"       to_char (max(est.fechaestado), 'DD') as DIAFACTURACION, " +
			"       to_char (max(est.fechaestado), 'MM') as MESFACTURACION, " +
			"       to_char (max(est.fechaestado), 'YYYY') as ANIOFACTURACION, " +
			"       pkg_siga_fecha_en_letra.f_siga_fechaenletra(max(est.fechaestado), 'D', "+idioma+") as DIAFACTURACION_ENLETRA, " +
			"       pkg_siga_fecha_en_letra.f_siga_fechaenletra(max(est.fechaestado), 'M', "+idioma+") as MESFACTURACION_ENLETRA, " +
			"       pkg_siga_fecha_en_letra.f_siga_fechaenletra(max(est.fechaestado), 'A', "+idioma+") as ANIOFACTURACION_ENLETRA, " +
			"       lower(pkg_siga_fecha_en_letra.f_siga_fechacompletaenletra(max(est.fechaestado), 'DMA', "+idioma+")) as FECHAFACTURACION_DDDMMMYYY, " +
			"       pkg_siga_fecha_en_letra.f_siga_fechacompletaenletra(max(est.fechaestado), 'M', "+idioma+") as FECHAFACTURACION_DDMMMYYYY, " +
			"       sum(fac.importeguardia) as IMPORTEGUARDIA " +
			
			"  from FCS_FACTURACIONJG           FAC, " +
			"       FCS_FACT_ESTADOSFACTURACION EST, " +
			"       CEN_INSTITUCION             INS, " +
			"       CEN_INSTITUCION             INS_CON " +
			
			" where fac.idinstitucion = est.idinstitucion " +
			"   and fac.idfacturacion = est.idfacturacion " +
			"  And est.Idordenestado = "+
            "      (Select Max(Est2.Idordenestado) "+
            "         From Fcs_Fact_Estadosfacturacion Est2 "+
            "         Where Est2.Idinstitucion = est.Idinstitucion "+
            "         And Est2.Idfacturacion = est.Idfacturacion) "+		
			"   and fac.idinstitucion = ins.idinstitucion " +
			"   and ins.cen_inst_idinstitucion = ins_con.idinstitucion " +
			"   and fac.idinstitucion = "+idInstitucion+" " +
			"   and fac.idfacturacion in (" + facturaciones + ")" +
			
			" group by ins.nombre, ins.idinstitucion, ins.idpersona, ins_con.nombre, ins_con.idinstitucion, ins_con.idpersona" ;
			
		
		hashDatosObtenidos = null;
		try {
			Vector v = this.selectGenerico (sql);
			hashDatosObtenidos = (Hashtable) v.get (0);
		}
		catch (ClsExceptions e) {
			e.printStackTrace();
		}
		
		return hashDatosObtenidos;
	} //obtenerComunes()
	
	
	public Vector informeFJGPorHitos (String idInstitucion,
									  String facturaciones,
									  String region,
									  String idioma)
		throws ClsExceptions
	{
		/*String[] region =
		{
				"ApuntesPorHitoYGuardia"
		};*/
		
		String sql = null;
		boolean esRegionVG = region.endsWith ("_SiVG");
		
		if (region.equalsIgnoreCase ("ApuntesPorHitoYGuardia")) {
			sql = 
				"select turno, " +
				"       guardia, " +
				"       decode(idTipoGuardia, " +
				"              '0', " +
				"              f_siga_getrecurso_etiqueta('general.no', "+idioma+"), " +
				"              f_siga_getrecurso_etiqueta('general.yes', "+idioma+")) vg, " +
				"       hito, " +
				"       hitodesc, " +
				"       f_siga_formatonumero(precio, 2) precio, " +
				"       precio precio_num, " +
				"       sum(cantidad) numero, " +
				"       f_siga_formatonumero(precio * sum(cantidad), 2) importe, " +
				"       precio * sum(cantidad) importe_num " +
				"  from ( /*Formas de facturar las guardias*/ " +
				"        select tur.abreviatura turno, " +
				"               gua.nombre guardia, " +
				"               nvl(gua.idTipoGuardia, '0') idTipoGuardia, " +
				"               hit.nombre hito, " +
				"               f_siga_getrecurso(hit.descripcion, "+idioma+") hitodesc, " +
				"               0 idapunte, " +
				"               hitgua.preciohito precio, " +
				"               0 cantidad " +
				"          from SCS_TURNO                 TUR, " +
				"               SCS_GUARDIASTURNO         GUA, " +
				"               SCS_HITOFACTURABLEGUARDIA HITGUA, " +
				"               SCS_HITOFACTURABLE        HIT " +
				"         where tur.idinstitucion = gua.idinstitucion " +
				"           and tur.idturno = gua.idturno " +
				"           and gua.idinstitucion = hitgua.idinstitucion " +
				"           and gua.idturno = hitgua.idturno " +
				"           and gua.idguardia = hitgua.idguardia " +
				"           and hitgua.idhito = hit.idhito " +
				"           and hit.idhito in " +
				"               (select hit2.idhito " +
				"                  from SCS_HITOFACTURABLE        HIT2, " +
				"                       SCS_HITOFACTURABLEGUARDIA HITGUA " +
				"                 where hitgua.idinstitucion = gua.idinstitucion " +
				"                   and hitgua.idturno = gua.idturno " +
				"                   and hitgua.idguardia = gua.idguardia " +
				"                   and (case when " +
				"                        hitgua.idhito = 1 and " +
				"                        (select 1 " +
				"                           from SCS_HITOFACTURABLEGUARDIA HITGUA_ND " +
				"                          where hitgua_nd.idinstitucion = hitgua.idinstitucion " +
				"                            and hitgua_nd.idturno = hitgua.idturno " +
				"                            and hitgua_nd.idguardia = hitgua.idguardia " +
				"                            and hitgua_nd.idhito = 46) = 1 then 44 else " +
				"                        hitgua.idhito end) = hit2.idhitoconfiguracion " +
				"                   and hitgua.agrupar = '0') " +
				"               " +
				"           and hitgua.idinstitucion = "+idInstitucion+" " +
				"           and exists " +
				"         (select * " +
				"                  from FCS_FACT_GRUPOFACT_HITO FAC " +
				"                 where fac.idinstitucion = tur.idinstitucion " +
				"                   and fac.idgrupofacturacion = tur.idgrupofacturacion " +
				"                   and fac.idfacturacion in ("+facturaciones+")" +
				"                   and fac.idhitogeneral = 20/*Guardias*/) " +
				"         " +
				"        union all " +
				"         " +
				"        /*Actuaciones por unidades*/ " +
				"        select tur.abreviatura turno, " +
				"               gua.nombre guardia, " +
				"               nvl(gua.idTipoGuardia, '0') idTipoGuardia, " +
				"               hit.nombre hito, " +
				"               f_siga_getrecurso(hit.descripcion, "+idioma+") hitodesc, " +
				"               facact.idapunte idapunte, " +
				"               facact.precioaplicado precio, " +
				"               1 cantidad " +
				"          from SCS_TURNO                    TUR, " +
				"               SCS_GUARDIASTURNO            GUA, " +
				"               FCS_FACT_ACTUACIONASISTENCIA FACACT, " +
				"               SCS_HITOFACTURABLE           HIT " +
				"         where tur.idinstitucion = gua.idinstitucion " +
				"           and tur.idturno = gua.idturno " +
				"           and exists (select * " +
				"                  from FCS_FACT_APUNTE APU " +
				"                 where gua.idinstitucion = apu.idinstitucion " +
				"                   and gua.idturno = apu.idturno " +
				"                   and gua.idguardia = apu.idguardia " +
				"                   and apu.idinstitucion = facact.idinstitucion " +
				"                   and apu.idfacturacion = facact.idfacturacion " +
				"                   and apu.idapunte = facact.idapunte) " +
				"           and not exists " +
				"         (select * " +
				"                  from FCS_FACT_APUNTE APU " +
				"                 where gua.idinstitucion = apu.idinstitucion " +
				"                   and gua.idturno = apu.idturno " +
				"                   and gua.idguardia = apu.idguardia " +
				"                   and apu.idinstitucion = facact.idinstitucion " +
				"                   and apu.idfacturacion = facact.idfacturacion " +
				"                   and apu.idapunte = facact.idapunte " +
				"                   and apu.idhito not in (7, 9)) /*Ac, AcFG*/ " +
				"           and not exists " +
				"         (select * " +
				"                  from FCS_FACT_GUARDIASCOLEGIADO APU, SCS_ASISTENCIA ASI " +
				"                 where gua.idinstitucion = apu.idinstitucion " +
				"                   and gua.idturno = apu.idturno " +
				"                   and gua.idguardia = apu.idguardia " +
				"                   and asi.idinstitucion = facact.idinstitucion " +
				"                   and asi.anio = facact.anio " +
				"                   and asi.numero = facact.numero " +
				"                   and apu.idinstitucion = facact.idinstitucion " +
				"                   and apu.idfacturacion = facact.idfacturacion " +
				"                   and apu.idapunte = facact.idapunte " +
				"                   and trunc(apu.fechafin) = trunc(asi.fechahora) " +
				"                   and apu.idhito not in (7, 9)) /*Ac, AcFG*/ " +
				"           and facact.idhito = hit.idhito " +
				"           and hit.aplicablea = 'ACTUACION' " +
				"           and facact.idhito in " +
				"               (select hit2.idhito " +
				"                  from SCS_HITOFACTURABLE        HIT2, " +
				"                       SCS_HITOFACTURABLEGUARDIA HITGUA " +
				"                 where hitgua.idinstitucion = gua.idinstitucion " +
				"                   and hitgua.idturno = gua.idturno " +
				"                   and hitgua.idguardia = gua.idguardia " +
				"                   and (case when " +
				"                        hitgua.idhito = 1 /*GAs*/ " +
				"                        and " +
				"                        (select 1 " +
				"                           from SCS_HITOFACTURABLEGUARDIA HITGUA_ND " +
				"                          where hitgua_nd.idinstitucion = hitgua.idinstitucion " +
				"                            and hitgua_nd.idturno = hitgua.idturno " +
				"                            and hitgua_nd.idguardia = hitgua.idguardia " +
				"                            and hitgua_nd.idhito = 46 /*NDAc*/ " +
				"                         ) = 1 then 44 /*GAc*/ " +
				"                        else hitgua.idhito end) = hit2.idhitoconfiguracion) " +
				"           and facact.idinstitucion = "+idInstitucion+" " +
				"           and facact.idfacturacion in ("+facturaciones+") " +
				"         " +
				"        union all " +
				"         " +
				"        /*Asistencias por unidades*/ " +
				"        select tur.abreviatura turno, " +
				"               gua.nombre guardia, " +
				"               nvl(gua.idTipoGuardia, '0') idTipoGuardia, " +
				"               hit.nombre hito, " +
				"               f_siga_getrecurso(hit.descripcion, "+idioma+") hitodesc, " +
				"               facasi.idapunte idapunte, " +
				"               facasi.precioaplicado precio, " +
				"               1 cantidad " +
				"          from SCS_TURNO           TUR, " +
				"               SCS_GUARDIASTURNO   GUA, " +
				"               FCS_FACT_ASISTENCIA FACASI, " +
				"               SCS_HITOFACTURABLE  HIT " +
				"         where tur.idinstitucion = gua.idinstitucion " +
				"           and tur.idturno = gua.idturno " +
				"           and exists (select * " +
				"                  from FCS_FACT_APUNTE APU " +
				"                 where gua.idinstitucion = apu.idinstitucion " +
				"                   and gua.idturno = apu.idturno " +
				"                   and gua.idguardia = apu.idguardia " +
				"                   and apu.idinstitucion = facasi.idinstitucion " +
				"                   and apu.idfacturacion = facasi.idfacturacion " +
				"                   and apu.idapunte = facasi.idapunte) " +
				"           and not exists " +
				"         (select * " +
				"                  from FCS_FACT_APUNTE APU " +
				"                 where gua.idinstitucion = apu.idinstitucion " +
				"                   and gua.idturno = apu.idturno " +
				"                   and gua.idguardia = apu.idguardia " +
				"                   and apu.idinstitucion = facasi.idinstitucion " +
				"                   and apu.idfacturacion = facasi.idfacturacion " +
				"                   and apu.idapunte = facasi.idapunte " +
				"                   and apu.idhito not in (5)) /*As*/ " +
				"           and not exists " +
				"         (select * " +
				"                  from FCS_FACT_GUARDIASCOLEGIADO APU " +
				"                 where gua.idinstitucion = apu.idinstitucion " +
				"                   and gua.idturno = apu.idturno " +
				"                   and gua.idguardia = apu.idguardia " +
				"                   and apu.idinstitucion = facasi.idinstitucion " +
				"                   and apu.idfacturacion = facasi.idfacturacion " +
				"                   and apu.idapunte = facasi.idapunte " +
				"                   and trunc(apu.fechafin) = trunc(facasi.fechahora) " +
				"                   and apu.idhito not in (5)) /*As*/ " +
				"           and facasi.idhito = hit.idhito " +
				"           and hit.aplicablea = 'ASISTENCIA' " +
				"           and facasi.idhito in " +
				"               (select hit2.idhito " +
				"                  from SCS_HITOFACTURABLE        HIT2, " +
				"                       SCS_HITOFACTURABLEGUARDIA HITGUA " +
				"                 where hitgua.idinstitucion = gua.idinstitucion " +
				"                   and hitgua.idturno = gua.idturno " +
				"                   and hitgua.idguardia = gua.idguardia " +
				"                   and (case when " +
				"                        hitgua.idhito = 1 /*GAs*/ " +
				"                        and " +
				"                        (select 1 " +
				"                           from SCS_HITOFACTURABLEGUARDIA HITGUA_ND " +
				"                          where hitgua_nd.idinstitucion = hitgua.idinstitucion " +
				"                            and hitgua_nd.idturno = hitgua.idturno " +
				"                            and hitgua_nd.idguardia = hitgua.idguardia " +
				"                            and hitgua_nd.idhito = 46 /*NDAc*/ " +
				"                         ) = 1 then 44 /*GAc*/ " +
				"                        else hitgua.idhito end) = hit2.idhitoconfiguracion) " +
				"           and facasi.idinstitucion = "+idInstitucion+" " +
				"           and facasi.idfacturacion in ("+facturaciones+") " +
				"         " +
				"        union all " +
				"         " +
				"        /*Dias de guardia mas o menos normales*/ " +
				"        select tur.abreviatura turno, " +
				"               gua.nombre guardia, " +
				"               nvl(gua.idTipoGuardia, '0') idTipoGuardia, " +
				"               hit.nombre hito, " +
				"               f_siga_getrecurso(hit.descripcion, "+idioma+") hitodesc, " +
				"               facgua.idapunte idapunte, " +
				"               facgua.precioaplicado precio, " +
				"               1 cantidad " +
				"          from SCS_TURNO                  TUR, " +
				"               SCS_GUARDIASTURNO          GUA, " +
				"               FCS_FACT_GUARDIASCOLEGIADO FACGUA, " +
				"               SCS_HITOFACTURABLE         HIT " +
				"         where tur.idinstitucion = gua.idinstitucion " +
				"           and tur.idturno = gua.idturno " +
				"           and exists (select * " +
				"                  from FCS_FACT_APUNTE APU " +
				"                 where gua.idinstitucion = apu.idinstitucion " +
				"                   and gua.idturno = apu.idturno " +
				"                   and gua.idguardia = apu.idguardia " +
				"                   and apu.idinstitucion = facgua.idinstitucion " +
				"                   and apu.idfacturacion = facgua.idfacturacion " +
				"                   and apu.idapunte = facgua.idapunte) " +
				"           and facgua.idhito = hit.idhito " +
				"           and hit.aplicablea = 'DIAOGUARDIA' " +
				"           and not exists " +
				"         (select * " +
				"                  from FCS_FACT_APUNTE APU " +
				"                 where gua.idinstitucion = apu.idinstitucion " +
				"                   and gua.idturno = apu.idturno " +
				"                   and gua.idguardia = apu.idguardia " +
				"                   and apu.idinstitucion = facgua.idinstitucion " +
				"                   and apu.idfacturacion = facgua.idfacturacion " +
				"                   and apu.idapunte = facgua.idapunte " +
				"                   and apu.idhito in (27, 29, 18, 34, 17, 33, 31, 26)) " +
				"              /*As+, Ac+, AcMax+, AcMin+, AsMax+, AsMin+, AcFG+, AcFGMax+*/ " +
				"           and not exists " +
				"         (select * " +
				"                  from FCS_FACT_GUARDIASCOLEGIADO FACGUA2 " +
				"                 where facgua.idinstitucion = facgua2.idinstitucion " +
				"                   and facgua.idfacturacion = facgua2.idfacturacion " +
				"                   and facgua.idapunte = facgua2.idapunte " +
				"                   and facgua.fechafin = facgua2.fechafin " +
				"                   and facgua2.idhito in (27, 29, 18, 34, 17, 33, 31, 26)) " +
				"              /*As+, Ac+, AcMax+, AcMin+, AsMax+, AsMin+, AcFG+, AcFGMax+*/ " +
				"           and (facgua.idhito in (19, 10) or /*AcMin, AsMin*/ " +
				"               not exists (select * " +
				"                             from FCS_FACT_GUARDIASCOLEGIADO FACGUA2 " +
				"                            where facgua.idinstitucion = facgua2.idinstitucion " +
				"                              and facgua.idfacturacion = facgua2.idfacturacion " +
				"                              and facgua.idapunte = facgua2.idapunte " +
				"                              and facgua.fechafin = facgua2.fechafin " +
				"                              and upper(facgua2.motivo) like '%TP%')) " +
				"           and facgua.idhito in " +
				"               (select hit2.idhito " +
				"                  from SCS_HITOFACTURABLE        HIT2, " +
				"                       SCS_HITOFACTURABLEGUARDIA HITGUA " +
				"                 where hitgua.idinstitucion = gua.idinstitucion " +
				"                   and hitgua.idturno = gua.idturno " +
				"                   and hitgua.idguardia = gua.idguardia " +
				"                   and (case when " +
				"                        hitgua.idhito = 1 /*GAs*/ " +
				"                        and " +
				"                        (select 1 " +
				"                           from SCS_HITOFACTURABLEGUARDIA HITGUA_ND " +
				"                          where hitgua_nd.idinstitucion = hitgua.idinstitucion " +
				"                            and hitgua_nd.idturno = hitgua.idturno " +
				"                            and hitgua_nd.idguardia = hitgua.idguardia " +
				"                            and hitgua_nd.idhito = 46 /*NDAc*/ " +
				"                         ) = 1 then 44 /*GAc*/ " +
				"                        else hitgua.idhito end) = hit2.idhitoconfiguracion " +
				"                   and hitgua.agrupar = '0') " +
				"           and facgua.idinstitucion = "+idInstitucion+" " +
				"           and facgua.idfacturacion in ("+facturaciones+") " +
				"         " +
				"        union all " +
				"         " +
				"        /*Cabeceras de guardia mas o menos normales*/ " +
				"        select tur.abreviatura turno, " +
				"               gua.nombre guardia, " +
				"               nvl(gua.idTipoGuardia, '0') idTipoGuardia, " +
				"               hit.nombre hito, " +
				"               f_siga_getrecurso(hit.descripcion, "+idioma+") hitodesc, " +
				"               apu.idapunte idapunte, " +
				"               apu.precioaplicado precio, " +
				"               1 cantidad " +
				"          from SCS_TURNO          TUR, " +
				"               SCS_GUARDIASTURNO  GUA, " +
				"               FCS_FACT_APUNTE    APU, " +
				"               SCS_HITOFACTURABLE HIT " +
				"         where tur.idinstitucion = gua.idinstitucion " +
				"           and tur.idturno = gua.idturno " +
				"           and gua.idinstitucion = apu.idinstitucion " +
				"           and gua.idturno = apu.idturno " +
				"           and gua.idguardia = apu.idguardia " +
				"           and apu.idhito = hit.idhito " +
				"           and hit.aplicablea = 'DIAOGUARDIA' " +
				"           and apu.idhito not in (27, 29, 18, 34, 17, 33, 31, 26) " +
				"              /*As+, Ac+, AcMax+, AcMin+, AsMax+, AsMin+, AcFG+, AcFGMax+*/ " +
				"           and apu.idhito in " +
				"               (select hit2.idhito " +
				"                  from SCS_HITOFACTURABLE        HIT2, " +
				"                       SCS_HITOFACTURABLEGUARDIA HITGUA " +
				"                 where hitgua.idinstitucion = gua.idinstitucion " +
				"                   and hitgua.idturno = gua.idturno " +
				"                   and hitgua.idguardia = gua.idguardia " +
				"                   and (case when " +
				"                        hitgua.idhito = 1 /*GAs*/ " +
				"                        and " +
				"                        (select 1 " +
				"                           from SCS_HITOFACTURABLEGUARDIA HITGUA_ND " +
				"                          where hitgua_nd.idinstitucion = hitgua.idinstitucion " +
				"                            and hitgua_nd.idturno = hitgua.idturno " +
				"                            and hitgua_nd.idguardia = hitgua.idguardia " +
				"                            and hitgua_nd.idhito = 46 /*NDAc*/ " +
				"                         ) = 1 then 44 /*GAc*/ " +
				"                        else hitgua.idhito end) = hit2.idhitoconfiguracion " +
				"                   and hitgua.agrupar = '1') " +
				"           and apu.idinstitucion = "+idInstitucion+" " +
				"           and apu.idfacturacion in ("+facturaciones+") " +
				"         " +
				"        union all " +
				"         " +
				"        /*Dias de guardia de facturacion por tipos*/ " +
				"        select tur.abreviatura turno, " +
				"               gua.nombre guardia, " +
				"               nvl(gua.idTipoGuardia, '0') idTipoGuardia, " +
				"               hit.nombre hito, " +
				"               f_siga_getrecurso(hit.descripcion, "+idioma+") hitodesc, " +
				"               facgua.idapunte idapunte, " +
				"               facgua.precioaplicado precio, " +
				"               1 cantidad " +
				"          from SCS_TURNO                  TUR, " +
				"               SCS_GUARDIASTURNO          GUA, " +
				"               FCS_FACT_GUARDIASCOLEGIADO FACGUA, " +
				"               SCS_HITOFACTURABLE         HIT " +
				"         where tur.idinstitucion = gua.idinstitucion " +
				"           and tur.idturno = gua.idturno " +
				"           and exists (select * " +
				"                  from FCS_FACT_APUNTE APU " +
				"                 where gua.idinstitucion = apu.idinstitucion " +
				"                   and gua.idturno = apu.idturno " +
				"                   and gua.idguardia = apu.idguardia " +
				"                   and apu.idinstitucion = facgua.idinstitucion " +
				"                   and apu.idfacturacion = facgua.idfacturacion " +
				"                   and apu.idapunte = facgua.idapunte) " +
				"           and not exists (select * " +
				"                  from FCS_FACT_APUNTE APU " +
				"                 where gua.idinstitucion = apu.idinstitucion " +
				"                   and gua.idturno = apu.idturno " +
				"                   and gua.idguardia = apu.idguardia " +
				"                   and apu.idinstitucion = facgua.idinstitucion " +
				"                   and apu.idfacturacion = facgua.idfacturacion " +
				"                   and apu.idapunte = facgua.idapunte" +
				"                   and apu.motivo like '%+') " +
				"           and facgua.idhito = hit.idhito " +
				"           and upper(facgua.motivo) like '%TP%' " +
				"           and not exists " +
				"         (select * " +
				"                  from FCS_FACT_GUARDIASCOLEGIADO FACGUA2 " +
				"                 where facgua.idinstitucion = facgua2.idinstitucion " +
				"                   and facgua.idfacturacion = facgua2.idfacturacion " +
				"                   and facgua.idapunte = facgua2.idapunte " +
				"                   and facgua.fechafin = facgua2.fechafin " +
				"                   and facgua2.idhito in (27, 29, 18, 34, 17, 33, 31, 26)) " +
				"              /*As+, Ac+, AcMax+, AcMin+, AsMax+, AsMin+, AcFG+, AcFGMax+*/ " +
				"           and facgua.idinstitucion = "+idInstitucion+" " +
				"           and facgua.idfacturacion in ("+facturaciones+") " +
				"         " +
				"        union all " +
				"         " +
				"        /*Dias de guardia devengadas - Precios raros*/ " +
				"        select tur.abreviatura turno, " +
				"               gua.nombre guardia, " +
				"               nvl(gua.idTipoGuardia, '0') idTipoGuardia, " +
				"               hit.nombre hito, " +
				"               f_siga_getrecurso(hit.descripcion, "+idioma+") hitodesc, " +
				"               facgua.idapunte idapunte, " +
				"               facgua.precioaplicado precio, " +
				"               1 cantidad " +
				"          from SCS_TURNO                  TUR, " +
				"               SCS_GUARDIASTURNO          GUA, " +
				"               FCS_FACT_GUARDIASCOLEGIADO FACGUA, " +
				"               SCS_HITOFACTURABLE         HIT " +
				"         where tur.idinstitucion = gua.idinstitucion " +
				"           and tur.idturno = gua.idturno " +
				"           and gua.idinstitucion = facgua.idinstitucion " +
				"           and gua.idturno = facgua.idturno " +
				"           and gua.idguardia = facgua.idguardia " +
				"           and facgua.idhito = hit.idhito " +
				"           and facgua.idhito in (27, 29, 18, 34, 17, 33, 31, 26) " +
				"              /*As+, Ac+, AcMax+, AcMin+, AsMax+, AsMin+, AcFG+, AcFGMax+*/ " +
				"           and not exists " +
				"         (select * " +
				"                  from FCS_FACT_APUNTE APU " +
				"                 where apu.idinstitucion = facgua.idinstitucion " +
				"                   and apu.idfacturacion = facgua.idfacturacion " +
				"                   and apu.idapunte = facgua.idapunte " +
				"                   and apu.idhito in (27, 29, 18, 34, 17, 33, 31, 26)) " +
				"              /*As+, Ac+, AcMax+, AcMin+, AsMax+, AsMin+, AcFG+, AcFGMax+*/ " +
				"           and facgua.idinstitucion = "+idInstitucion+" " +
				"           and facgua.idfacturacion in ("+facturaciones+") " +
				"         " +
				"        union all " +
				"         " +
				"        /*Cabeceras devengadas - Precios raros*/ " +
				"        select tur.abreviatura turno, " +
				"               gua.nombre guardia, " +
				"               nvl(gua.idTipoGuardia, '0') idTipoGuardia, " +
				"               hit.nombre hito, " +
				"               f_siga_getrecurso(hit.descripcion, "+idioma+") hitodesc, " +
				"               apu.idapunte idapunte, " +
				"               apu.precioaplicado precio, " +
				"               1 cantidad " +
				"          from SCS_TURNO          TUR, " +
				"               SCS_GUARDIASTURNO  GUA, " +
				"               FCS_FACT_APUNTE    APU, " +
				"               SCS_HITOFACTURABLE HIT " +
				"         where tur.idinstitucion = gua.idinstitucion " +
				"           and tur.idturno = gua.idturno " +
				"           and gua.idinstitucion = apu.idinstitucion " +
				"           and gua.idturno = apu.idturno " +
				"           and gua.idguardia = apu.idguardia " +
				"           and apu.idhito = hit.idhito " +
				"           and apu.idhito in (27, 29, 18, 34, 17, 33, 31, 26) " +
				"              /*As+, Ac+, AcMax+, AcMin+, AsMax+, AsMin+, AcFG+, AcFGMax+*/ " +
				"           and apu.idinstitucion = "+idInstitucion+" " +
				"           and apu.idfacturacion in ("+facturaciones+") " +
				"         " +
				"        union all " +
				"         " +
				"        /*Costes Fijos*/ " +
				"        select tur.abreviatura turno, " +
				"               gua.nombre guardia, " +
				"               nvl(gua.idTipoGuardia, '0') idTipoGuardia, " +
				"               f_siga_getrecurso_etiqueta('gratuita.mantActuacion.literal.Coste', "+idioma+") hito, " +
				"               f_siga_getrecurso(cos.descripcion, "+idioma+") hitodesc, " +
				"               0 idapunte, " +
				"               tipcos.importe precio, " +
				"               0 cantidad " +
				"          from SCS_TURNO                  TUR, " +
				"               SCS_GUARDIASTURNO          GUA, " +
				"               SCS_TIPOACTUACIONCOSTEFIJO TIPCOS, " +
				"               SCS_COSTEFIJO              COS " +
				"         where tur.idinstitucion = gua.idinstitucion " +
				"           and tur.idturno = gua.idturno " +
				"           and gua.idinstitucion = tipcos.idinstitucion " +
				"           and tipcos.idinstitucion = cos.idinstitucion " +
				"           and tipcos.idcostefijo = cos.idcostefijo " +
				"           and tipcos.idinstitucion = "+idInstitucion+" " +
				"           and exists " +
				"         (select * " +
				"                  from FCS_FACT_GRUPOFACT_HITO FAC " +
				"                 where fac.idinstitucion = tur.idinstitucion " +
				"                   and fac.idgrupofacturacion = tur.idgrupofacturacion " +
				"                   and fac.idfacturacion in ("+facturaciones+") " +
				"                   and fac.idhitogeneral = 20/*Guardias*/) " +
				"         " +
				"        union all " +
				"         " +
				"        select tur.abreviatura turno, " +
				"               gua.nombre guardia, " +
				"               nvl(gua.idTipoGuardia, '0') idTipoGuardia, " +
				"               f_siga_getrecurso_etiqueta('gratuita.mantActuacion.literal.Coste', "+idioma+") hito, " +
				"               f_siga_getrecurso(cos.descripcion, "+idioma+") hitodesc, " +
				"               facact.idapunte idapunte, " +
				"               facact.preciocostesfijos precio, " +
				"               1 cantidad " +
				"          from SCS_TURNO                    TUR, " +
				"               SCS_GUARDIASTURNO            GUA, " +
				"               FCS_FACT_ACTUACIONASISTENCIA FACACT, " +
				"               SCS_ASISTENCIA               ASI, " +
				"               SCS_ACTUACIONASISTCOSTEFIJO  ACTCOS, " +
				"               SCS_COSTEFIJO                COS " +
				"         where tur.idinstitucion = gua.idinstitucion " +
				"           and tur.idturno = gua.idturno " +
				"           and gua.idinstitucion = asi.idinstitucion " +
				"           and gua.idturno = asi.idturno " +
				"           and gua.idguardia = asi.idguardia " +
				"           and asi.idinstitucion = facact.idinstitucion " +
				"           and asi.anio = facact.anio " +
				"           and asi.numero = facact.numero " +
				"           and facact.idinstitucion = actcos.idinstitucion " +
				"           and facact.anio = actcos.anio " +
				"           and facact.numero = actcos.numero " +
				"           and facact.idactuacion = actcos.idactuacion " +
				"           and actcos.idinstitucion = cos.idinstitucion " +
				"           and actcos.idcostefijo = cos.idcostefijo " +
				"           and facact.preciocostesfijos <> 0 " +
				"           and not exists " +
				"         (select * " +
				"                  from FCS_FACT_ACTUACIONASISTENCIA FACACT2 " +
				"                 where facact.idinstitucion = facact2.idinstitucion " +
				"                   and facact.anio = facact2.anio " +
				"                   and facact.numero = facact2.numero " +
				"                   and facact.idactuacion = facact2.idactuacion " +
				"                   and facact.idfacturacion > facact2.idfacturacion) " +
				"           and facact.idinstitucion = "+idInstitucion+" " +
				"           and facact.idfacturacion in ("+facturaciones+")) " +

				" group by idTipoGuardia, turno, guardia, hito, hitodesc, precio " +
				" order by idTipoGuardia, turno, guardia, hito, hitodesc, precio";
		}
		
		return this.selectGenerico (sql);
	} //informeFJGPorHitos()
    
    /**
     * CONSULTA MODULOS Y BASES DE COMPENSACION
     * 
     * @param idInstitucion
     * @param idFacturacion
     * @param idJurisdiccion
     * @param idioma
     * @return
     * @throws ClsExceptions
     */
	public Vector informeFacturaPag3_1 (String idInstitucion,
										String facturaciones,
										String idJurisdiccion,
										String idioma)
			throws ClsExceptions
	{
		String sql = 
			"select f_siga_getrecurso(j.descripcion, "+idioma+") NOMBRE_JURISDICCION, " +
			"       p.nombre NOMBRE_MODULO, " +
			"       p.codigo CODIGO_MODULO, " +
			"       f_siga_formatonumero(p.precio, 2) VALOR_MODULO, " +
			"       (select count(*) " +
			"          from scs_actuaciondesigna a, " +
			"               scs_procedimientos p2, " +
			"               fcs_fact_actuaciondesigna ad, " +
			"               scs_acreditacionprocedimiento ap, " +
			"               scs_acreditacion acr" +
			"         where a.idinstitucion = ad.idinstitucion " +
			"           and a.idturno = ad.idturno " +
			"           and a.anio = ad.anio " +
			"           and a.numero = ad.numero " +
			"           and a.numeroasunto = ad.numeroasunto " +
			"           and a.idinstitucion_proc = p2.idinstitucion " +
			"           and a.idprocedimiento = p2.idprocedimiento " +
			"           and a.idacreditacion = acr.idacreditacion " +
			"           and a.idinstitucion_proc = ap.idinstitucion " +
			"           and a.idprocedimiento = ap.idprocedimiento " +
			"           and a.idacreditacion = ap.idacreditacion " +
			"           and p2.idinstitucion = p.idinstitucion " +
			"           and p2.idprocedimiento = p.idprocedimiento " +
			"           and a.idinstitucion = "+idInstitucion+" " +
			//"           and ad.idfacturacion = "+idFacturacion+" " +
			"           and ad.idfacturacion in  (" + facturaciones + ")" +
			"           and acr.idtipoacreditacion = 1" +
			"           and a.idacreditacion <> 5) as INICIO, " +
			"       (select count(*) " +
			"          from scs_actuaciondesigna a, " +
			"               scs_procedimientos p2, " +
			"               fcs_fact_actuaciondesigna ad, " +
			"               scs_acreditacionprocedimiento ap, " +
			"               scs_acreditacion acr" +
			"         where a.idinstitucion = ad.idinstitucion " +
			"           and a.idturno = ad.idturno " +
			"           and a.anio = ad.anio " +
			"           and a.numero = ad.numero " +
			"           and a.numeroasunto = ad.numeroasunto " +
			"           and a.idinstitucion_proc = p2.idinstitucion " +
			"           and a.idprocedimiento = p2.idprocedimiento " +
			"           and a.idacreditacion = acr.idacreditacion " +
			"           and a.idinstitucion_proc = ap.idinstitucion " +
			"           and a.idprocedimiento = ap.idprocedimiento " +
			"           and a.idacreditacion = ap.idacreditacion " +
			"           and p2.idinstitucion = p.idinstitucion " +
			"           and p2.idprocedimiento = p.idprocedimiento " +
			"           and a.idinstitucion = "+idInstitucion+" " +
			//"           and ad.idfacturacion = "+idFacturacion+" " +
			"           and ad.idfacturacion in  (" + facturaciones + ")" +
			"           and acr.idtipoacreditacion = 2" +
			"           and a.idacreditacion <> 5) as FIN, " +
			"       (select count(*) " +
			"          from scs_actuaciondesigna a, " +
			"               scs_procedimientos p2, " +
			"               fcs_fact_actuaciondesigna ad, " +
			"               scs_acreditacionprocedimiento ap, " +
			"               scs_acreditacion acr" +
			"         where a.idinstitucion = ad.idinstitucion " +
			"           and a.idturno = ad.idturno " +
			"           and a.anio = ad.anio " +
			"           and a.numero = ad.numero " +
			"           and a.numeroasunto = ad.numeroasunto " +
			"           and a.idinstitucion_proc = p2.idinstitucion " +
			"           and a.idprocedimiento = p2.idprocedimiento " +
			"           and a.idacreditacion = acr.idacreditacion " +
			"           and a.idinstitucion_proc = ap.idinstitucion " +
			"           and a.idprocedimiento = ap.idprocedimiento " +
			"           and a.idacreditacion = ap.idacreditacion " +
			"           and p2.idinstitucion = p.idinstitucion " +
			"           and p2.idprocedimiento = p.idprocedimiento " +
			"           and a.idinstitucion = "+idInstitucion+" " +
			//"           and ad.idfacturacion = "+idFacturacion+" " +
			"           and ad.idfacturacion in  (" + facturaciones + ")" +
			"           and acr.idtipoacreditacion = 3" +
			"           and a.idacreditacion <> 5) as INICIOFIN, " +
			"       (select count(*) " +
			"          from scs_actuaciondesigna a, " +
			"               scs_procedimientos p2, " +
			"               fcs_fact_actuaciondesigna ad, " +
			"               scs_acreditacionprocedimiento ap, " +
			"               scs_acreditacion acr" +
			"         where a.idinstitucion = ad.idinstitucion " +
			"           and a.idturno = ad.idturno " +
			"           and a.anio = ad.anio " +
			"           and a.numero = ad.numero " +
			"           and a.numeroasunto = ad.numeroasunto " +
			"           and a.idinstitucion_proc = p2.idinstitucion " +
			"           and a.idprocedimiento = p2.idprocedimiento " +
			"           and a.idacreditacion = acr.idacreditacion " +
			"           and a.idinstitucion_proc = ap.idinstitucion " +
			"           and a.idprocedimiento = ap.idprocedimiento " +
			"           and a.idacreditacion = ap.idacreditacion " +
			"           and p2.idinstitucion = p.idinstitucion " +
			"           and p2.idprocedimiento = p.idprocedimiento " +
			"           and a.idinstitucion = "+idInstitucion+" " +
			//"           and ad.idfacturacion = "+idFacturacion+" " +
			"           and ad.idfacturacion in  (" + facturaciones + ")" +
			"           and acr.idtipoacreditacion in (1,2,3)" +
			"           and a.idacreditacion <> 5) as NUM_ASUNTOS, " +
			"       (select count(*) " +
			"          from scs_actuaciondesigna a, " +
			"               scs_procedimientos p2, " +
			"               fcs_fact_actuaciondesigna ad, " +
			"               scs_acreditacionprocedimiento ap, " +
			"               scs_acreditacion acr" +
			"         where a.idinstitucion = ad.idinstitucion " +
			"           and a.idturno = ad.idturno " +
			"           and a.anio = ad.anio " +
			"           and a.numero = ad.numero " +
			"           and a.numeroasunto = ad.numeroasunto " +
			"           and a.idinstitucion_proc = p2.idinstitucion " +
			"           and a.idprocedimiento = p2.idprocedimiento " +
			"           and a.idacreditacion = acr.idacreditacion " +
			"           and a.idinstitucion_proc = ap.idinstitucion " +
			"           and a.idprocedimiento = ap.idprocedimiento " +
			"           and a.idacreditacion = ap.idacreditacion " +
			"           and p2.idinstitucion = p.idinstitucion " +
			"           and p2.idprocedimiento = p.idprocedimiento " +
			"           and a.idinstitucion = "+idInstitucion+" " +
			//"           and ad.idfacturacion = "+idFacturacion+" " +
			"           and ad.idfacturacion in  (" + facturaciones + ")" +
			"           and a.idacreditacion = 5) as EXTRAJUD, " +
			"       f_siga_formatonumero(" +
			"         (select nvl(sum(ad.IMPORTEFACTURADO), 0) " +
			"            from scs_actuaciondesigna a, " +
			"                 scs_procedimientos p2, " +
			"                 fcs_fact_actuaciondesigna ad, " +
			"                 scs_acreditacionprocedimiento ap, " +
			"                 scs_acreditacion acr " +
			"           where a.idinstitucion = ad.idinstitucion " +
			"             and a.idturno = ad.idturno " +
			"             and a.anio = ad.anio " +
			"             and a.numero = ad.numero " +
			"             and a.numeroasunto = ad.numeroasunto " +
			"             and a.idinstitucion_proc = p2.idinstitucion " +
			"             and a.idprocedimiento = p2.idprocedimiento " +
			"             and a.idacreditacion = acr.idacreditacion " +
			"             and a.idinstitucion_proc = ap.idinstitucion " +
			"             and a.idprocedimiento = ap.idprocedimiento " +
			"             and a.idacreditacion = ap.idacreditacion " +
			"             and p2.idinstitucion = p.idinstitucion " +
			"             and p2.idprocedimiento = p.idprocedimiento " +
			"             and a.idinstitucion = "+idInstitucion+" " +
			//"             and ad.idfacturacion = "+idFacturacion+" " +
			"           and ad.idfacturacion in  (" + facturaciones + ")" +
			"             and acr.idtipoacreditacion in (1,2,3)" +
			"             and a.idacreditacion <> 5), 2) as VALOR, " +
			"       (select nvl(sum(ad.IMPORTEFACTURADO), 0) " +
			"          from scs_actuaciondesigna a, " +
			"               scs_procedimientos p2, " +
			"               fcs_fact_actuaciondesigna ad, " +
			"               scs_acreditacionprocedimiento ap, " +
			"               scs_acreditacion acr " +
			"         where a.idinstitucion = ad.idinstitucion " +
			"           and a.idturno = ad.idturno " +
			"           and a.anio = ad.anio " +
			"           and a.numero = ad.numero " +
			"           and a.numeroasunto = ad.numeroasunto " +
			"           and a.idinstitucion_proc = p2.idinstitucion " +
			"           and a.idprocedimiento = p2.idprocedimiento " +
			"           and a.idacreditacion = acr.idacreditacion " +
			"           and a.idinstitucion_proc = ap.idinstitucion " +
			"           and a.idprocedimiento = ap.idprocedimiento " +
			"           and a.idacreditacion = ap.idacreditacion " +
			"           and p2.idinstitucion = p.idinstitucion " +
			"           and p2.idprocedimiento = p.idprocedimiento " +
			"           and a.idinstitucion = "+idInstitucion+" " +
			//"           and ad.idfacturacion = "+idFacturacion+" " +
			"           and ad.idfacturacion in  (" + facturaciones + ")" +
			"           and acr.idtipoacreditacion in (1,2,3)" +
			"           and a.idacreditacion <> 5) as VALOR_NUM, " +
			"       f_siga_formatonumero(" +
			"         (select nvl(sum(ad.IMPORTEFACTURADO), 0) " +
			"            from scs_actuaciondesigna a, " +
			"                 scs_procedimientos p2, " +
			"                 fcs_fact_actuaciondesigna ad, " +
			"                 scs_acreditacionprocedimiento ap, " +
			"                 scs_acreditacion acr " +
			"           where a.idinstitucion = ad.idinstitucion " +
			"             and a.idturno = ad.idturno " +
			"             and a.anio = ad.anio " +
			"             and a.numero = ad.numero " +
			"             and a.numeroasunto = ad.numeroasunto " +
			"             and a.idinstitucion_proc = p2.idinstitucion " +
			"             and a.idprocedimiento = p2.idprocedimiento " +
			"             and a.idacreditacion = acr.idacreditacion " +
			"             and a.idinstitucion_proc = ap.idinstitucion " +
			"             and a.idprocedimiento = ap.idprocedimiento " +
			"             and a.idacreditacion = ap.idacreditacion " +
			"             and p2.idinstitucion = p.idinstitucion " +
			"             and p2.idprocedimiento = p.idprocedimiento " +
			"             and a.idinstitucion = "+idInstitucion+" " +
			//"             and ad.idfacturacion = "+idFacturacion+" " +
			"           and ad.idfacturacion in  (" + facturaciones + ")" +
			"             and a.idacreditacion = 5), 2) as VALOR_EXTRAJUD, " +
			"       (select nvl(sum(ad.IMPORTEFACTURADO), 0) " +
			"          from scs_actuaciondesigna a, " +
			"               scs_procedimientos p2, " +
			"               fcs_fact_actuaciondesigna ad, " +
			"               scs_acreditacionprocedimiento ap, " +
			"               scs_acreditacion acr " +
			"         where a.idinstitucion = ad.idinstitucion " +
			"           and a.idturno = ad.idturno " +
			"           and a.anio = ad.anio " +
			"           and a.numero = ad.numero " +
			"           and a.numeroasunto = ad.numeroasunto " +
			"           and a.idinstitucion_proc = p2.idinstitucion " +
			"           and a.idprocedimiento = p2.idprocedimiento " +
			"           and a.idacreditacion = acr.idacreditacion " +
			"           and a.idinstitucion_proc = ap.idinstitucion " +
			"           and a.idprocedimiento = ap.idprocedimiento " +
			"           and a.idacreditacion = ap.idacreditacion " +
			"           and p2.idinstitucion = p.idinstitucion " +
			"           and p2.idprocedimiento = p.idprocedimiento " +
			"           and a.idinstitucion = "+idInstitucion+" " +
			//"           and ad.idfacturacion = "+idFacturacion+" " +
			"           and ad.idfacturacion in  (" + facturaciones + ")" +
			"           and a.idacreditacion = 5) as VALOR_NUM_EXTRAJUD " +
			
			"  from scs_jurisdiccion j, " +
			"       scs_procedimientos p " +
			
			" where j.idjurisdiccion = p.idjurisdiccion " +
			"   and p.idinstitucion = "+idInstitucion+" " +
			"   and p.idjurisdiccion = "+idJurisdiccion+" " +
			"   and p.vigente = 1 " +
			
			" order by NOMBRE_JURISDICCION, p.orden, p.nombre";
		
		return this.selectGenerico (sql);
	} //informeFacturaPag3_1()
	
	public Vector informeFJGAsistencias (String idInstitucion,
										 String facturaciones,
										 String region,
										 String idioma)
		throws ClsExceptions
	{
		/*String[] region =
		{
				"GuardiasSimples_NoVG", "GuardiasSimples_SiVG", 
				"GuardiasDobladas_NoVG", "GuardiasDobladas_SiVG", 
				"MaximosPorAsistencias_NoVG", "MaximosPorAsistencias_SiVG", 
				"MaximosPorActuaciones_NoVG", "MaximosPorActuaciones_SiVG", 
				"Asistencias_NoVG", "Asistencias_SiVG", 
				"Actuaciones_NoVG", "Actuaciones_SiVG",
				"ActuacionesFG_NoVG", "ActuacionesFG_SiVG",
				
				//las siguientes regiones son uniones de las de arriba
				"GuardiasDobladas+Maximos_NoVG", "GuardiasDobladas+Maximos_SiVG", 
				"Actuac+ActuacFG_NoVG", "Actuac+ActuacFG_SiVG"
		};*/
		
		String sql = null;
		boolean esRegionVG = region.endsWith ("_SiVG");
		
		if (region.startsWith ("GuardiasSimples_")) {
			sql = 
				"select sum(subcantidad) as CANTIDAD, " +
				"       f_siga_formatonumero(subprecio, 2) as PRECIO, " +
				"       f_siga_formatonumero(sum(subimporte_num), 2) as IMPORTE, " +
				"       sum(subimporte_num) as IMPORTE_NUM " +
				" " +
				"/*Consulta de precios*/ " +
				"  from (select 0              as SUBCANTIDAD, " +
				"               hit.preciohito as SUBPRECIO, " +
				"               0              as SUBIMPORTE_NUM " +
				"          from SCS_HITOFACTURABLEGUARDIA HIT, SCS_GUARDIASTURNO GUATUR, FCS_FACT_GRUPOFACT_HITO GHITO, SCS_TURNO TUR " +
				"         where guatur.idinstitucion = hit.idinstitucion " +
				"           and guatur.idturno = hit.idturno " +
				"           and guatur.idguardia = hit.idguardia " +
				"           and hit.idhito in (1, 44) /*GAs, GAc*/ " +
				"           and guatur.idinstitucion = "+idInstitucion+" " +
				"           and nvl(guatur.idTipoGuardia, '0') "+(esRegionVG ? "=" : "<>")+" '1' " +
	            "		    and ghito.idfacturacion in  (" + facturaciones + ")" +
				"		    and ghito.idinstitucion = hit.idinstitucion " +
				"		    and ghito.idgrupofacturacion = tur.idgrupofacturacion " +
				"		    and ghito.idinstitucion = tur.idinstitucion " +
				"		    and tur.idinstitucion = guatur.idinstitucion " +
				"		    and tur.idturno = guatur.idturno " +
				"         " +
				"        union " +
				"         " +
				"        /*Guardias simples*/ " +
				"        select sum(decode(fac.regularizacion, '0', 1, 0)) as SUBCANTIDAD, " +
				"               fuc.precioaplicado as SUBPRECIO, " +
				"               sum(fuc.precioaplicado) as SUBIMPORTE_NUM " +
				"          from FCS_FACT_GUARDIASCOLEGIADO FUC, SCS_GUARDIASTURNO GUATUR, FCS_FACTURACIONJG FAC " +
				"         where fuc.idinstitucion = guatur.idinstitucion " +
				"           and fuc.idturno = guatur.idturno " +
				"           and fuc.idguardia = guatur.idguardia " +
				"           and fuc.idinstitucion = fac.idinstitucion " +
				"           and fuc.idfacturacion = fac.idfacturacion " +
				"           and fuc.precioaplicado > 0 " +
				"           and (upper(fuc.motivo) like 'GA%' or upper(fuc.motivo) = 'GS%') " +
				"           and fuc.idinstitucion = "+idInstitucion+" " +
				"           and fuc.idfacturacion in  (" + facturaciones + ")" +
				"           and nvl(guatur.idTipoGuardia, '0') "+(esRegionVG ? "=" : "<>")+" '1' " +
		        "           and (select 1 " +
				"                  from scs_hitofacturableguardia h " +
				"                 where h.idinstitucion = fuc.idinstitucion " +
				"                   and h.idturno = fuc.idturno " +
				"                   and h.idguardia = fuc.idguardia " +
				"                   and h.idhito in (44, 1) " +
				"                   and h.agrupar = '0') = 1 " +
				"         group by fuc.precioaplicado " +
				"	union all  " +
				"		 select sum(decode(fac.regularizacion, '0', 1, 0)) as SUBCANTIDAD, " +
				" 	            fap.precioaplicado as SUBPRECIO," +
				"               sum(fap.precioaplicado) as SUBIMPORTE_NUM" +
				"          from fcs_fact_apunte fap, SCS_GUARDIASTURNO GUATUR, FCS_FACTURACIONJG FAC " +
				"         where fap.Idinstitucion = GUATUR.Idinstitucion" +
				"           and fap.Idturno = GUATUR.Idturno" +
				"           and fap.idguardia = GUATUR.idguardia" +
				"           and fap.idinstitucion = fac.idinstitucion " +
				"        	and fap.idfacturacion = fac.idfacturacion " +			    
				"       	and (upper(fap.motivo) like 'GA%' or upper(fap.motivo) = 'GS%')" +
				"       	and fap.idinstitucion = "+idInstitucion+" " +
				"       	and fap.idfacturacion in  (" + facturaciones + ")" +
				"           and nvl(guatur.idTipoGuardia, '0') "+(esRegionVG ? "=" : "<>")+" '1' " +
				"       	and fap.precioaplicado > 0" +
				"  		    and (select 1" +
				"              	   from scs_hitofacturableguardia h" +
				"             	  where h.idinstitucion = fap.idinstitucion" +
				"                   and h.idturno = fap.idturno" +
				"                   and h.idguardia = fap.idguardia" +
				"                   and h.idhito in (44, 1)" +
				"                   and h.agrupar = '1') = 1" +
				"     	  group by fap.precioaplicado" +
				"         " +			
			      /*Guardias simples*/
				"        union " +
				"         " +
				"        /*Guardias dobles devengadas*/ " +
				"        select sum(decode(fac.regularizacion, '0', 1, 0)) as SUBCANTIDAD, " +
				"               fuc.precioaplicado as SUBPRECIO, " +
				"               sum(fuc.precioaplicado) as SUBIMPORTE_NUM " +
				"          from FCS_FACT_GUARDIASCOLEGIADO FUC, SCS_GUARDIASTURNO GUATUR, FCS_FACTURACIONJG FAC " +
				"         where fuc.idinstitucion = guatur.idinstitucion " +
				"           and fuc.idturno = guatur.idturno " +
				"           and fuc.idguardia = guatur.idguardia " +
				"           and fuc.idinstitucion = fac.idinstitucion " +
				"           and fuc.idfacturacion = fac.idfacturacion " +
				"           and fuc.precioaplicado > 0 " +
				"           and upper(fuc.motivo) like 'GD%' " +
				"           and fuc.precioaplicado in " +
				"               (select hit.preciohito " +
				"                  from SCS_HITOFACTURABLEGUARDIA HIT " +
				"                 where guatur.idinstitucion = hit.idinstitucion " +
				"                   and guatur.idturno = hit.idturno " +
				"                   and guatur.idguardia = hit.idguardia " +
				"                   and hit.idhito in (1, 44) /*GAs, GAc*/ " +
				"                ) " +
				"           and fuc.idinstitucion = "+idInstitucion+" " +
				//"           and fuc.idfacturacion = "+idFacturacion+" " +
				"           and fuc.idfacturacion in  (" + facturaciones + ")" +
				"           and nvl(guatur.idTipoGuardia, '0') "+(esRegionVG ? "=" : "<>")+" '1' " +
				"         group by fuc.precioaplicado) " +
				" " +
				" group by subprecio " +
				" having subprecio > 0";
		}
		else if (region.startsWith ("GuardiasDobladas_")) {
			sql = 
				"select sum(subcantidad) as CANTIDAD, " +
				"       f_siga_formatonumero(subprecio, 2) as PRECIO, " +
				"       f_siga_formatonumero(sum(subimporte_num), 2) as IMPORTE, " +
				"       sum(subimporte_num) as IMPORTE_NUM " +
				" " +
				"/*Consulta de precios*/ " +
				"  from (select 0              as SUBCANTIDAD, " +
				"               hit.preciohito as SUBPRECIO, " +
				"               0              as SUBIMPORTE_NUM " +
				"          from SCS_HITOFACTURABLEGUARDIA HIT, SCS_GUARDIASTURNO GUATUR, FCS_FACT_GRUPOFACT_HITO GHITO, SCS_TURNO TUR " +
				"         where guatur.idinstitucion = hit.idinstitucion " +
				"           and guatur.idturno = hit.idturno " +
				"           and guatur.idguardia = hit.idguardia " +
				"           and hit.idhito in (2, 4) /*GDAs, GDAc*/ " +
				"           and guatur.idinstitucion = "+idInstitucion+" " +
				"           and nvl(guatur.idTipoGuardia, '0') "+(esRegionVG ? "=" : "<>")+" '1' " +
	            "		    and ghito.idfacturacion in  (" + facturaciones + ")" +
				"		    and ghito.idinstitucion = hit.idinstitucion " +
				"		    and ghito.idgrupofacturacion = tur.idgrupofacturacion " +
				"		    and ghito.idinstitucion = tur.idinstitucion " +
				"		    and tur.idinstitucion = guatur.idinstitucion " +
				"		    and tur.idturno = guatur.idturno " +
				"         " +
				"        union " +
				"         " +
				"        /*Guardias dobladas sin devengar*/ " +
				"        select sum(decode(fac.regularizacion, '0', 1, 0)) as SUBCANTIDAD, " +
				"               fuc.precioaplicado as SUBPRECIO, " +
				"               sum(fuc.precioaplicado) as SUBIMPORTE_NUM " +
				"          from FCS_FACT_GUARDIASCOLEGIADO FUC, SCS_GUARDIASTURNO GUATUR, FCS_FACTURACIONJG FAC " +
				"         where fuc.idinstitucion = guatur.idinstitucion " +
				"           and fuc.idinstitucion = fac.idinstitucion " +
				"           and fuc.idfacturacion = fac.idfacturacion " +
				"           and fuc.idturno = guatur.idturno " +
				"           and fuc.idguardia = guatur.idguardia " +
				"           and fuc.precioaplicado > 0 " +
				"           and upper(fuc.motivo) like 'GD%' " +
				"           and fuc.precioaplicado not in " +
				"               (select hit.preciohito " +
				"                  from SCS_HITOFACTURABLEGUARDIA HIT " +
				"                 where guatur.idinstitucion = hit.idinstitucion " +
				"                   and guatur.idturno = hit.idturno " +
				"                   and guatur.idguardia = hit.idguardia " +
				"                   and hit.idhito in (1, 44) /*GAs, GAc*/ " +
				"                ) " +
				"           and fuc.idinstitucion = "+idInstitucion+" " +
				//"           and fuc.idfacturacion = "+idFacturacion+" " +
				"           and fuc.idfacturacion in  (" + facturaciones + ")" +
				"           and nvl(guatur.idTipoGuardia, '0') "+(esRegionVG ? "=" : "<>")+" '1' " +
				"         group by fuc.precioaplicado) " +
				" " +
				" group by subprecio " +
				" having subprecio > 0";
		}
		else if (region.startsWith ("MaximosPorAsistencias_")) {
			sql = 
				"select sum(subcantidad) as CANTIDAD, " +
				"       f_siga_formatonumero(subprecio, 2) as PRECIO, " +
				"       f_siga_formatonumero(sum(subimporte_num), 2) as IMPORTE, " +
				"       sum(subimporte_num) as IMPORTE_NUM " +
				" " +
				"/*Consulta de precios*/ " +
				"  from (select 0              as SUBCANTIDAD, " +
				"               hit.preciohito as SUBPRECIO, " +
				"               0              as SUBIMPORTE_NUM " +
				"          from SCS_HITOFACTURABLEGUARDIA HIT, SCS_GUARDIASTURNO GUATUR, FCS_FACT_GRUPOFACT_HITO GHITO, SCS_TURNO TUR " +
				"         where guatur.idinstitucion = hit.idinstitucion " +
				"           and guatur.idturno = hit.idturno " +
				"           and guatur.idguardia = hit.idguardia " +
				"           and hit.idhito in (3) /*AsMax*/ " +
				"           and guatur.idinstitucion = "+idInstitucion+" " +
				"           and nvl(guatur.idTipoGuardia, '0') "+(esRegionVG ? "=" : "<>")+" '1' " +
	            "		    and ghito.idfacturacion in  (" + facturaciones + ")" +
				"		    and ghito.idinstitucion = hit.idinstitucion " +
				"		    and ghito.idgrupofacturacion = tur.idgrupofacturacion " +
				"		    and ghito.idinstitucion = tur.idinstitucion " +
				"		    and tur.idinstitucion = guatur.idinstitucion " +
				"		    and tur.idturno = guatur.idturno " +
				"         " +
				"        union " +
				"         " +
				"        /*Maximos por asistencias*/ " +
				"        select sum(decode(fac.regularizacion, '0', 1, 0)) as SUBCANTIDAD, " +
				"               fuc.precioaplicado as SUBPRECIO, " +
				"               sum(fuc.precioaplicado) as SUBIMPORTE_NUM " +
				"          from FCS_FACT_GUARDIASCOLEGIADO FUC, SCS_GUARDIASTURNO GUATUR, FCS_FACTURACIONJG FAC " +
				"         where fuc.idinstitucion = guatur.idinstitucion " +
				"           and fuc.idturno = guatur.idturno " +
				"           and fuc.idinstitucion = fac.idinstitucion " +
				"           and fuc.idfacturacion = fac.idfacturacion " +
				"           and fuc.idguardia = guatur.idguardia " +
				"           and fuc.precioaplicado > 0 " +
				"           and upper(fuc.motivo) like 'ASMAX%' " +
				"           and fuc.idinstitucion = "+idInstitucion+" " +
				//"           and fuc.idfacturacion = "+idFacturacion+" " +
				"           and fuc.idfacturacion in  (" + facturaciones + ")" +
				"           and nvl(guatur.idTipoGuardia, '0') "+(esRegionVG ? "=" : "<>")+" '1' " +
				"         group by fuc.precioaplicado) " +
				" " +
				" group by subprecio " +
				" having subprecio > 0";
		}
		else if (region.startsWith ("MaximosPorActuaciones_")) {
			sql = 
				"select sum(subcantidad) as CANTIDAD, " +
				"       f_siga_formatonumero(subprecio, 2) as PRECIO, " +
				"       f_siga_formatonumero(sum(subimporte_num), 2) as IMPORTE, " +
				"       sum(subimporte_num) as IMPORTE_NUM " +
				" " +
				"/*Consulta de precios*/ " +
				"  from (select 0              as SUBCANTIDAD, " +
				"               hit.preciohito as SUBPRECIO, " +
				"               0              as SUBIMPORTE_NUM " +
				"          from SCS_HITOFACTURABLEGUARDIA HIT, SCS_GUARDIASTURNO GUATUR, FCS_FACT_GRUPOFACT_HITO GHITO, SCS_TURNO TUR " +
				"         where guatur.idinstitucion = hit.idinstitucion " +
				"           and guatur.idturno = hit.idturno " +
				"           and guatur.idguardia = hit.idguardia " +
				"           and hit.idhito in (8) /*AcMax*/ " +
				"           and guatur.idinstitucion = "+idInstitucion+" " +
				"           and nvl(guatur.idTipoGuardia, '0') <> '1' " +
	            "		    and ghito.idfacturacion in  (" + facturaciones + ")" +
				"		    and ghito.idinstitucion = hit.idinstitucion " +
				"		    and ghito.idgrupofacturacion = tur.idgrupofacturacion " +
				"		    and ghito.idinstitucion = tur.idinstitucion " +
				"		    and tur.idinstitucion = guatur.idinstitucion " +
				"		    and tur.idturno = guatur.idturno " +
				"         " +
				"        union " +
				"         " +
				"        /*Maximos por actuaciones*/ " +
				"        select sum(decode(fac.regularizacion, '0', 1, 0)) as SUBCANTIDAD, " +
				"               fuc.precioaplicado as SUBPRECIO, " +
				"               sum(fuc.precioaplicado) as SUBIMPORTE_NUM " +
				"          from FCS_FACT_GUARDIASCOLEGIADO FUC, SCS_GUARDIASTURNO GUATUR, FCS_FACTURACIONJG FAC " +
				"         where fuc.idinstitucion = guatur.idinstitucion " +
				"           and fuc.idinstitucion = fac.idinstitucion " +
				"           and fuc.idfacturacion = fac.idfacturacion " +
				"           and fuc.idturno = guatur.idturno " +
				"           and fuc.idguardia = guatur.idguardia " +
				"           and fuc.precioaplicado > 0 " +
				"           and upper(fuc.motivo) like 'ACMAX%' " +
				"           and fuc.idinstitucion = "+idInstitucion+" " +
				//"           and fuc.idfacturacion = "+idFacturacion+" " +
				"           and fuc.idfacturacion in  (" + facturaciones + ")" +
				"           and nvl(guatur.idTipoGuardia, '0') "+(esRegionVG ? "=" : "<>")+" '1' " +
				"         group by fuc.precioaplicado) " +
				" " +
				" group by subprecio " +
				" having subprecio > 0";
		}
		else if (region.startsWith ("Asistencias_")) {
			sql = 
				"select sum(subcantidad) as CANTIDAD, " +
				"       f_siga_formatonumero(subprecio, 2) as PRECIO, " +
				"       f_siga_formatonumero(sum(subimporte_num), 2) as IMPORTE, " +
				"       sum(subimporte_num) as IMPORTE_NUM " +
				" " +
				"/*Consulta de precios*/ " +
				"  from (select 0              as SUBCANTIDAD, " +
				"               hit.preciohito as SUBPRECIO, " +
				"               0              as SUBIMPORTE_NUM " +
				"          from SCS_HITOFACTURABLEGUARDIA HIT, SCS_GUARDIASTURNO GUATUR, FCS_FACT_GRUPOFACT_HITO GHITO, SCS_TURNO TUR " +
				"         where guatur.idinstitucion = hit.idinstitucion " +
				"           and guatur.idturno = hit.idturno " +
				"           and guatur.idguardia = hit.idguardia " +
				"           and hit.idhito in (5) /*As*/ " +
				"           and guatur.idinstitucion = "+idInstitucion+" " +
				"           and nvl(guatur.idTipoGuardia, '0') "+(esRegionVG ? "=" : "<>")+" '1' " +
	            "		    and ghito.idfacturacion in  (" + facturaciones + ")" +
				"		    and ghito.idinstitucion = hit.idinstitucion " +
				"		    and ghito.idgrupofacturacion = tur.idgrupofacturacion " +
				"		    and ghito.idinstitucion = tur.idinstitucion " +
				"		    and tur.idinstitucion = guatur.idinstitucion " +
				"		    and tur.idturno = guatur.idturno " +
				"         " +
				"        union " +
				"         " +
				"        /*Asistencias*/ " +
				"        select sum(decode(fac.regularizacion, '0', 1, 0)) as SUBCANTIDAD, " +
				"               fas.precioaplicado as SUBPRECIO, " +
				"               sum(fas.precioaplicado) as SUBIMPORTE_NUM " +
				"          from FCS_FACT_ASISTENCIA FAS, " +
				"               SCS_ASISTENCIA      ASI, " +
				"               FCS_FACTURACIONJG   FAC, " +
				"               SCS_GUARDIASTURNO   GUATUR " +
				"         where fas.idinstitucion = asi.idinstitucion " +
				"           and fas.anio = asi.anio " +
				"           and fas.numero = asi.numero " +
				"           and asi.idinstitucion = guatur.idinstitucion " +
				"           and fas.idinstitucion = fac.idinstitucion " +
				"           and fas.idfacturacion = fac.idfacturacion" +
				"           and asi.idturno = guatur.idturno " +
				"           and asi.idguardia = guatur.idguardia " +
				"           and fas.precioaplicado > 0 " +
				"           and upper(fas.motivo) like 'AS' " +
				"           and not exists " +
				"         (select * " +
				"                  from FCS_FACT_GUARDIASCOLEGIADO FUC " +
				"                 where fas.idinstitucion = fuc.idinstitucion " +
				"                   and fas.idfacturacion = fuc.idfacturacion " +
				"                   and fas.idapunte = fuc.idapunte " +
				"                   and trunc(asi.fechahora) = trunc(fuc.fechafin) " +
				"                   and asi.idpersonacolegiado = fuc.idpersona " +
				"                   and upper(fuc.motivo) like 'ASMAX%') " +
				"           and not exists " +
				"         (select * " +
				"                  from FCS_FACT_APUNTE FGC " +
				"                 where fas.idinstitucion = fgc.idinstitucion " +
				"                   and fas.idfacturacion = fgc.idfacturacion " +
				"                   and fas.idapunte = fgc.idapunte " +
				"                   and upper(fgc.motivo) like 'ASMAX%') " +
				"           and fas.idinstitucion = "+idInstitucion+" " +
				//"           and fas.idfacturacion = "+idFacturacion+" " +
				"           and fas.idfacturacion in  (" + facturaciones + ")" +
				"           and nvl(guatur.idTipoGuardia, '0') "+(esRegionVG ? "=" : "<>")+" '1' " +
				"         group by fas.precioaplicado) " +
				" " +
				" group by subprecio " +
				" having subprecio > 0";
		}
		else if (region.startsWith ("Actuaciones_")) {
			sql = 
				"select sum(subcantidad) as CANTIDAD, " +
				"       f_siga_formatonumero(subprecio, 2) as PRECIO, " +
				"       f_siga_formatonumero(sum(subimporte_num), 2) as IMPORTE, " +
				"       sum(subimporte_num) as IMPORTE_NUM " +
				" " +
				"/*Consulta de precios*/ " +
				"  from (select 0              as SUBCANTIDAD, " +
				"               hit.preciohito as SUBPRECIO, " +
				"               0              as SUBIMPORTE_NUM " +
				"          from SCS_HITOFACTURABLEGUARDIA HIT, SCS_GUARDIASTURNO GUATUR, FCS_FACT_GRUPOFACT_HITO GHITO, SCS_TURNO TUR " +
				"         where guatur.idinstitucion = hit.idinstitucion " +
				"           and guatur.idturno = hit.idturno " +
				"           and guatur.idguardia = hit.idguardia " +
				"           and hit.idhito in (7) /*Ac*/ " +
				"           and guatur.idinstitucion = "+idInstitucion+" " +
				"           and nvl(guatur.idTipoGuardia, '0') "+(esRegionVG ? "=" : "<>")+" '1' " +
	            "		    and ghito.idfacturacion in  (" + facturaciones + ")" +
				"		    and ghito.idinstitucion = hit.idinstitucion " +
				"		    and ghito.idgrupofacturacion = tur.idgrupofacturacion " +
				"		    and ghito.idinstitucion = tur.idinstitucion " +
				"		    and tur.idinstitucion = guatur.idinstitucion " +
				"		    and tur.idturno = guatur.idturno " +
				"         " +
				"        union " +
				"         " +
				"        /*Actuaciones*/ " +
				"        select sum(decode(facu.regularizacion, '0', 1, 0)) as SUBCANTIDAD, " +
				"               fac.precioaplicado as SUBPRECIO, " +
				"               sum(fac.precioaplicado) as SUBIMPORTE_NUM " +
				"          from FCS_FACT_ACTUACIONASISTENCIA FAC, " +
				"               FCS_FACTURACIONJG            FACU, " +
				"               SCS_ACTUACIONASISTENCIA      ACT, " +
				"               SCS_ASISTENCIA               ASI, " +
				"               SCS_GUARDIASTURNO            GUATUR " +
				"         where fac.idinstitucion = act.idinstitucion " +
				"           and fac.anio = act.anio " +
				"           and fac.numero = act.numero " +
				"           and fac.idactuacion = act.idactuacion " +
				"           and facu.idinstitucion = fac.idinstitucion " +
				"           and facu.idfacturacion = fac.idfacturacion" +
				"           and act.idinstitucion = asi.idinstitucion " +
				"           and act.anio = asi.anio " +
				"           and act.numero = asi.numero " +
				"           and asi.idinstitucion = guatur.idinstitucion " +
				"           and asi.idturno = guatur.idturno " +
				"           and asi.idguardia = guatur.idguardia " +
				"           and fac.precioaplicado > 0 " +
				"           and upper(fac.motivo) like 'AC' " +
				"           and not exists " +
				"         (select * " +
				"                  from FCS_FACT_GUARDIASCOLEGIADO FUC " +
				"                 where fac.idinstitucion = fuc.idinstitucion " +
				"                   and fac.idfacturacion = fuc.idfacturacion " +
				"                   and fac.idapunte = fuc.idapunte " +
				"                   and trunc(asi.fechahora) = trunc(fuc.fechafin) " +
				"                   and asi.idpersonacolegiado = fuc.idpersona " +
				"                   and upper(fuc.motivo) like 'ACMAX%') " +
				"           and not exists " +
				"         (select * " +
				"                  from FCS_FACT_APUNTE FGC " +
				"                 where fac.idinstitucion = fgc.idinstitucion " +
				"                   and fac.idfacturacion = fgc.idfacturacion " +
				"                   and fac.idapunte = fgc.idapunte " +
				"                   and upper(fgc.motivo) like 'ACMAX%') " +
				"           and fac.idinstitucion = "+idInstitucion+" " +
				//"           and fac.idfacturacion = "+idFacturacion+" " +
				"           and fac.idfacturacion in  (" + facturaciones + ")" +
				"           and nvl(guatur.idTipoGuardia, '0') "+(esRegionVG ? "=" : "<>")+" '1' " +
				"         group by fac.precioaplicado) " +
				" " +
				" group by subprecio " +
				" having subprecio > 0";
		}
		else if (region.startsWith ("ActuacionesFG_")) {
			sql =
				"select sum(subcantidad) as CANTIDAD, " +
				"       f_siga_formatonumero(subprecio, 2) as PRECIO, " +
				"       f_siga_formatonumero(sum(subimporte_num), 2) as IMPORTE, " +
				"       sum(subimporte_num) as IMPORTE_NUM " +
				" " +
				"/*Consulta de precios*/ " +
				"  from (select 0              as SUBCANTIDAD, " +
				"               hit.preciohito as SUBPRECIO, " +
				"               0              as SUBIMPORTE_NUM " +
				"          from SCS_HITOFACTURABLEGUARDIA HIT, SCS_GUARDIASTURNO GUATUR, FCS_FACT_GRUPOFACT_HITO GHITO, SCS_TURNO TUR " +
				"         where guatur.idinstitucion = hit.idinstitucion " +
				"           and guatur.idturno = hit.idturno " +
				"           and guatur.idguardia = hit.idguardia " +
				"           and hit.idhito in (9) /*AcFG*/ " +
				"           and guatur.idinstitucion = "+idInstitucion+" " +
				"           and nvl(guatur.idTipoGuardia, '0') "+(esRegionVG ? "=" : "<>")+" '1' " +
	            "		    and ghito.idfacturacion in  (" + facturaciones + ")" +
				"		    and ghito.idinstitucion = hit.idinstitucion " +
				"		    and ghito.idgrupofacturacion = tur.idgrupofacturacion " +
				"		    and ghito.idinstitucion = tur.idinstitucion " +
				"		    and tur.idinstitucion = guatur.idinstitucion " +
				"		    and tur.idturno = guatur.idturno " +
				"         " +
				"        union " +
				"         " +
				"        /*Actuaciones FG*/ " +
				"        select sum(apu.precioaplicado) / decode(hitgua.preciohito, null, 1, 0, 1, hitgua.preciohito) as SUBCANTIDAD, " +
				"               hitgua.preciohito as SUBPRECIO, " +
				"               sum(apu.precioaplicado) as SUBIMPORTE_NUM " +
				"          from FCS_FACT_APUNTE           APU, " +
				"               SCS_GUARDIASTURNO         GUATUR, " +
				"               SCS_HITOFACTURABLEGUARDIA HITGUA " +
				"         where apu.idinstitucion = guatur.idinstitucion " +
				"           and apu.idturno = guatur.idturno " +
				"           and apu.idguardia = guatur.idguardia " +
				"           and guatur.idinstitucion = hitgua.idinstitucion " +
				"           and guatur.idturno = hitgua.idturno " +
				"           and guatur.idguardia = hitgua.idguardia " +
				"           and upper(apu.motivo) in ('ACFG', 'ACFG+') " +
				"           and hitgua.idhito = 9 /*AcFG*/ " +
				"           and apu.idinstitucion = "+idInstitucion+" " +
				//"           and apu.idfacturacion = "+idFacturacion+" " +
				"           and apu.idfacturacion in  (" + facturaciones + ")" +
				"           and nvl(guatur.idTipoGuardia, '0') "+(esRegionVG ? "=" : "<>")+" '1' " +
				"         group by hitgua.preciohito) " +
				" " +
				" group by subprecio " +
				" having subprecio > 0";
		}
		else if (region.startsWith ("GuardiasDobladas+Maximos_")) {
			sql = 
				"select sum(subcantidad) as CANTIDAD, " +
				"       f_siga_formatonumero(subprecio, 2) as PRECIO, " +
				"       f_siga_formatonumero(sum(subimporte_num), 2) as IMPORTE, " +
				"       sum(subimporte_num) as IMPORTE_NUM " +
				" " +
				"/*Consulta de precios*/ " +
				"  from (select 0              as SUBCANTIDAD, " +
				"               hit.preciohito as SUBPRECIO, " +
				"               0              as SUBIMPORTE_NUM " +
				"          from SCS_HITOFACTURABLEGUARDIA HIT, SCS_GUARDIASTURNO GUATUR, FCS_FACT_GRUPOFACT_HITO GHITO, SCS_TURNO TUR " +
				"         where guatur.idinstitucion = hit.idinstitucion " +
				"           and guatur.idturno = hit.idturno " +
				"           and guatur.idguardia = hit.idguardia " +
				"           and hit.idhito in (2, 4, 3, 8) " +
				"           and guatur.idinstitucion = "+idInstitucion+" " +
				"           and nvl(guatur.idTipoGuardia, '0') "+(esRegionVG ? "=" : "<>")+" '1' " +
	            "		    and ghito.idfacturacion in  (" + facturaciones + ")" +
				"		    and ghito.idinstitucion = hit.idinstitucion " +
				"		    and ghito.idgrupofacturacion = tur.idgrupofacturacion " +
				"		    and ghito.idinstitucion = tur.idinstitucion " +
				"		    and tur.idinstitucion = guatur.idinstitucion " +
				"		    and tur.idturno = guatur.idturno " +
				"         " +
				"        union " +
				"         " +
				"        /*Guardias dobladas sin devengar*/ " +
				"        select sum(decode(fac.regularizacion, '0', 1, 0)) as SUBCANTIDAD, " +
				"               fuc.precioaplicado as SUBPRECIO, " +
				"               sum(fuc.precioaplicado) as SUBIMPORTE_NUM " +
				"          from FCS_FACT_GUARDIASCOLEGIADO FUC, SCS_GUARDIASTURNO GUATUR, FCS_FACTURACIONJG FAC  " +
				"         where fuc.idinstitucion = guatur.idinstitucion " +
				"           and fuc.idturno = guatur.idturno " +
				"           and fuc.idguardia = guatur.idguardia " +
				"           and fuc.idinstitucion = fac.idinstitucion " +
				"           and fuc.idfacturacion = fac.idfacturacion" +
				"           and fuc.precioaplicado > 0 " +
				"           and upper(fuc.motivo) like 'GD%' " +
				"           and fuc.precioaplicado not in " +
				"               (select hit.preciohito " +
				"                  from SCS_HITOFACTURABLEGUARDIA HIT " +
				"                 where guatur.idinstitucion = hit.idinstitucion " +
				"                   and guatur.idturno = hit.idturno " +
				"                   and guatur.idguardia = hit.idguardia " +
				"                   and hit.idhito in (1, 44) /*GAs, GAc*/ " +
				"                ) " +
				"           and fuc.idinstitucion = "+idInstitucion+" " +
				//"           and fuc.idfacturacion = "+idFacturacion+" " +
				"           and fuc.idfacturacion in  (" + facturaciones + ")" +
				"           and nvl(guatur.idTipoGuardia, '0') "+(esRegionVG ? "=" : "<>")+" '1' " +
				"         group by fuc.precioaplicado " +
				"         " +
				"        union all " +
				"         " +
				"        /*Maximos por asistencias*/ " +
				"        select sum(decode(fac.regularizacion, '0', 1, 0)) as SUBCANTIDAD, " +
				"               fuc.precioaplicado as SUBPRECIO, " +
				"               sum(fuc.precioaplicado) as SUBIMPORTE_NUM " +
				"          from FCS_FACT_GUARDIASCOLEGIADO FUC, SCS_GUARDIASTURNO GUATUR, FCS_FACTURACIONJG FAC " +
				"         where fuc.idinstitucion = guatur.idinstitucion " +
				"           and fuc.idturno = guatur.idturno " +
				"           and fuc.idguardia = guatur.idguardia " +
				"           and fuc.idinstitucion = fac.idinstitucion " +
				"           and fuc.idfacturacion = fac.idfacturacion" +
				"           and fuc.precioaplicado > 0 " +
				"           and upper(fuc.motivo) like 'ASMAX%' " +
				"           and fuc.idinstitucion = "+idInstitucion+" " +
				//"           and fuc.idfacturacion = "+idFacturacion+" " +
				"           and fuc.idfacturacion in  (" + facturaciones + ")" +
				"           and nvl(guatur.idTipoGuardia, '0') "+(esRegionVG ? "=" : "<>")+" '1' " +
				"         group by fuc.precioaplicado " +
				"         " +
				"        union all " +
				"         " +
				"        /*Maximos por actuaciones*/ " +
				"        select sum(decode(fac.regularizacion, '0', 1, 0)) as SUBCANTIDAD, " +
				"               fuc.precioaplicado as SUBPRECIO, " +
				"               sum(fuc.precioaplicado) as SUBIMPORTE_NUM " +
				"          from FCS_FACT_GUARDIASCOLEGIADO FUC, SCS_GUARDIASTURNO GUATUR, FCS_FACTURACIONJG FAC " +
				"         where fuc.idinstitucion = guatur.idinstitucion " +
				"           and fuc.idturno = guatur.idturno " +
				"           and fuc.idguardia = guatur.idguardia " +
				"           and fuc.idinstitucion = fac.idinstitucion " +
				"           and fuc.idfacturacion = fac.idfacturacion" +
				"           and fuc.precioaplicado > 0 " +
				"           and upper(fuc.motivo) like 'ACMAX%' " +
				"           and fuc.idinstitucion = "+idInstitucion+" " +
				//"           and fuc.idfacturacion = "+idFacturacion+" " +
				"           and fuc.idfacturacion in  (" + facturaciones + ")" +
				"           and nvl(guatur.idTipoGuardia, '0') "+(esRegionVG ? "=" : "<>")+" '1' " +
				"         group by fuc.precioaplicado) " +
				" " +
				" group by subprecio " +
				" having subprecio > 0";
		}
		else if (region.startsWith ("Actuac+ActuacFG_")) {
			sql = 
				"select sum(subcantidad) as CANTIDAD, " +
				"       f_siga_formatonumero(subprecio, 2) as PRECIO, " +
				"       f_siga_formatonumero(sum(subimporte_num), 2) as IMPORTE, " +
				"       sum(subimporte_num) as IMPORTE_NUM " +
				" " +
				"/*Consulta de precios*/ " +
				"  from (select 0              as SUBCANTIDAD, " +
				"               hit.preciohito as SUBPRECIO, " +
				"               0              as SUBIMPORTE_NUM " +
				"          from SCS_HITOFACTURABLEGUARDIA HIT, SCS_GUARDIASTURNO GUATUR, FCS_FACT_GRUPOFACT_HITO GHITO, SCS_TURNO TUR " +
				"         where guatur.idinstitucion = hit.idinstitucion " +
				"           and guatur.idturno = hit.idturno " +
				"           and guatur.idguardia = hit.idguardia " +
				"           and hit.idhito in (7, 9) " +
				"           and guatur.idinstitucion = "+idInstitucion+" " +
				"           and nvl(guatur.idTipoGuardia, '0') "+(esRegionVG ? "=" : "<>")+" '1' " +
	            "		    and ghito.idfacturacion in  (" + facturaciones + ")" +
				"		    and ghito.idinstitucion = hit.idinstitucion " +
				"		    and ghito.idgrupofacturacion = tur.idgrupofacturacion " +
				"		    and ghito.idinstitucion = tur.idinstitucion " +
				"		    and tur.idinstitucion = guatur.idinstitucion " +
				"		    and tur.idturno = guatur.idturno " +
				"         " +
				"        union " +
				"         " +
				"        /*Actuaciones*/ " +
				"        select sum(decode(facu.regularizacion, '0', 1, 0)) as SUBCANTIDAD, " +
				"               fac.precioaplicado as SUBPRECIO, " +
				"               sum(fac.precioaplicado) as SUBIMPORTE_NUM " +
				"          from FCS_FACT_ACTUACIONASISTENCIA FAC, " +
				"               SCS_ACTUACIONASISTENCIA      ACT, " +
				"               SCS_ASISTENCIA               ASI, " +
				"               FCS_FACTURACIONJG            FACU, " +
				"               SCS_GUARDIASTURNO            GUATUR " +
				"         where fac.idinstitucion = act.idinstitucion " +
				"           and fac.anio = act.anio " +
				"           and fac.numero = act.numero " +
				"           and fac.idactuacion = act.idactuacion " +
				"           and act.idinstitucion = asi.idinstitucion " +
				"           and facu.idinstitucion = fac.idinstitucion " +
				"           and facu.idfacturacion = fac.idfacturacion" +
				"           and act.anio = asi.anio " +
				"           and act.numero = asi.numero " +
				"           and asi.idinstitucion = guatur.idinstitucion " +
				"           and asi.idturno = guatur.idturno " +
				"           and asi.idguardia = guatur.idguardia " +
				"           and fac.precioaplicado > 0 " +
				"           and upper(fac.motivo) like 'AC' " +
				"           and not exists " +
				"         (select * " +
				"                  from FCS_FACT_GUARDIASCOLEGIADO FUC " +
				"                 where fac.idinstitucion = fuc.idinstitucion " +
				"                   and fac.idfacturacion = fuc.idfacturacion " +
				"                   and fac.idapunte = fuc.idapunte " +
				"                   and trunc(asi.fechahora) = trunc(fuc.fechafin) " +
				"                   and asi.idpersonacolegiado = fuc.idpersona " +
				"                   and upper(fuc.motivo) like 'ACMAX%') " +
				"           and not exists " +
				"         (select * " +
				"                  from FCS_FACT_APUNTE FGC " +
				"                 where fac.idinstitucion = fgc.idinstitucion " +
				"                   and fac.idfacturacion = fgc.idfacturacion " +
				"                   and fac.idapunte = fgc.idapunte " +
				"                   and upper(fgc.motivo) like 'ACMAX%') " +
				"           and fac.idinstitucion = "+idInstitucion+" " +
				//"           and fac.idfacturacion = "+idFacturacion+" " +
				"           and fac.idfacturacion in  (" + facturaciones + ")" +
				"           and nvl(guatur.idTipoGuardia, '0') "+(esRegionVG ? "=" : "<>")+" '1' " +
				"         group by fac.precioaplicado " +
				"         " +
				"        union all " +
				"         " +
				"        /*Actuaciones FG*/ " +
				"        select sum(apu.precioaplicado) / decode(hitgua.preciohito, null, 1, 0, 1, hitgua.preciohito) as SUBCANTIDAD, " +
				"               hitgua.preciohito as SUBPRECIO, " +
				"               sum(apu.precioaplicado) as SUBIMPORTE_NUM " +
				"          from FCS_FACT_APUNTE           APU, " +
				"               SCS_GUARDIASTURNO         GUATUR, " +
				"               SCS_HITOFACTURABLEGUARDIA HITGUA " +
				"         where apu.idinstitucion = guatur.idinstitucion " +
				"           and apu.idturno = guatur.idturno " +
				"           and apu.idguardia = guatur.idguardia " +
				"           and guatur.idinstitucion = hitgua.idinstitucion " +
				"           and guatur.idturno = hitgua.idturno " +
				"           and guatur.idguardia = hitgua.idguardia " +
				"           and upper(apu.motivo) in ('ACFG', 'ACFG+') " +
				"           and hitgua.idhito = 9 /*AcFG*/ " +
				"           and apu.idinstitucion = "+idInstitucion+" " +
				//"           and apu.idfacturacion = "+idFacturacion+" " +
				"           and apu.idfacturacion in  (" + facturaciones + ")" +
				"           and nvl(guatur.idTipoGuardia, '0') "+(esRegionVG ? "=" : "<>")+" '1' " +
				"         group by hitgua.preciohito) " +
				" " +
				" group by subprecio " +
				" having subprecio > 0";
		}
		
		return this.selectGenerico (sql);
	} //informeFJGAsistencias	

	public void ejecutarFacturacion(String idInstitucion, String idFacturacion, UserTransaction tx, boolean esRegularizacion) throws ClsExceptions, SIGAException {
	    FcsFactEstadosFacturacionAdm admEstado = new FcsFactEstadosFacturacionAdm(this.usrbean);
		FcsFactEstadosFacturacionBean beanEstado = null;

		SimpleDateFormat sdfLong = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); 
		Date dat = Calendar.getInstance().getTime();
		String fecha = sdfLong.format(dat);

	    // Fichero de log
		GenParametrosAdm paramAdm = new GenParametrosAdm(this.usrbean);
		String pathFicheros = paramAdm.getValor("" + idInstitucion, "FCS", "PATH_PREVISIONES_BD", null);
		String sNombreFichero = pathFicheros + File.separator + "LOG_ERROR_" + idInstitucion + "_" + idFacturacion + ".log";
		File ficheroLog = new File(sNombreFichero);
		if (ficheroLog!=null && ficheroLog.exists()) {
		    ficheroLog.delete();
		}
	    SIGALogging log = new SIGALogging(sNombreFichero);

		try {
		    
		    
			
			//////////////////////////////////
			// cambio de estado
		    tx.begin();
			beanEstado = new FcsFactEstadosFacturacionBean();
			beanEstado.setIdInstitucion(new Integer(idInstitucion));
			beanEstado.setIdFacturacion(new Integer(idFacturacion));
			beanEstado.setIdEstadoFacturacion(new Integer(ESTADO_FACTURACION.ESTADO_FACTURACION_EN_EJECUCION.getCodigo()));
			beanEstado.setFechaEstado("SYSDATE");			
			beanEstado.setIdOrdenEstado(new Integer(admEstado.getIdordenestadoMaximo(idInstitucion, idFacturacion)));
			admEstado.insert(beanEstado);
			boolean prevision = false;
			tx.commit();
			
			Hashtable criterios = new Hashtable();
			criterios.put(FcsFacturacionJGBean.C_IDINSTITUCION,idInstitucion);
			criterios.put(FcsFacturacionJGBean.C_IDFACTURACION,idFacturacion);
			Vector v = (Vector)this.select(criterios);
			if (v!=null && v.size()>0) {
				tx.begin();
				
				FcsFacturacionJGBean beanFac = (FcsFacturacionJGBean)v.get(0);
				if(beanFac.getPrevision().equals("1"))prevision=true;
				Hashtable estado = this.getEstadoFacturacion(idInstitucion,idFacturacion);
				String idEstado = (String) estado.get(FcsEstadosFacturacionBean.C_IDESTADOFACTURACION);

				// proceso de facturacion
				double  importeTotal = 0;
				Double  importeOficio = null, 
				importeGuardia = null, 
				importeSOJ = null,  
				importeEJG = null;
				
				//////////////////////////////////
				// TURNOS DE OFICIO rgg 16-03-2005

				Object[] param_in_facturacion = new Object[3];
				param_in_facturacion[0] = beanFac.getIdInstitucion().toString(); // IDINSTITUCION
				param_in_facturacion[1] = beanFac.getIdFacturacion().toString(); // IDFACTURACION 
				param_in_facturacion[2] = beanFac.getUsuMod().toString();        // USUMODIFICACION

				String resultado[] = new String[3];
				resultado = ClsMngBBDD.callPLProcedure("{call "+
						(esRegularizacion?"PKG_SIGA_REGULARIZACION_SJCS.PROC_FCS_REGULAR_TURNOS_OFI":"PKG_SIGA_FACTURACION_SJCS.PROC_FCS_FACTURAR_TURNOS_OFI")+
						"(?,?,?,?,?,?)}", 3, param_in_facturacion);
				if (!resultado[1].equalsIgnoreCase("0")) {
					ClsLogging.writeFileLog("Error en PL = "+(String)resultado[2],3);
					throw new ClsExceptions ("Ha ocurrido un error al ejecutar la facturaci�n de Turnos de Oficio: "+(String)resultado[2]);
				}
				importeOficio = new Double(resultado[0].replaceAll(",","."));
				importeTotal += importeOficio.doubleValue();


				//////////////////////////////////
				// GUARDIAS rgg 22-03-2005

				param_in_facturacion = new Object[3];
				param_in_facturacion[0] = beanFac.getIdInstitucion().toString(); // IDINSTITUCION
				param_in_facturacion[1] = beanFac.getIdFacturacion().toString(); // IDFACTURACION
				param_in_facturacion[2] = beanFac.getUsuMod().toString(); // USUMODIFICACION

				resultado = new String[3];
				resultado = ClsMngBBDD.callPLProcedure("{call "+
						(esRegularizacion?"PKG_SIGA_REGULARIZACION_SJCS.PROC_FCS_REGULAR_GUARDIAS":"PKG_SIGA_FACTURACION_SJCS.PROC_FCS_FACTURAR_GUARDIAS")+
						"(?,?,?,?,?,?)}", 3, param_in_facturacion);
				if (!resultado[1].equalsIgnoreCase("0")) {
					ClsLogging.writeFileLog("Error en PL = "+(String)resultado[2],3);
					throw new ClsExceptions ("Ha ocurrido un error al ejecutar la facturaci�n de Guardias: "+(String)resultado[2]);
				} 
				importeGuardia = new Double(resultado[0].replaceAll(",","."));
				importeTotal += importeGuardia.doubleValue();

				//////////////////////////////////
				// EXPEDIENTES SOJ rgg 22-03-2005

				param_in_facturacion = new Object[3];
				param_in_facturacion[0] = beanFac.getIdInstitucion().toString(); // IDINSTITUCION
				param_in_facturacion[1] = beanFac.getIdFacturacion().toString(); // IDFACTURACION
				param_in_facturacion[2] = beanFac.getUsuMod().toString(); 		 // USUMODIFICACION

				resultado = new String[3];
				resultado = ClsMngBBDD.callPLProcedure("{call "+
						(esRegularizacion?"PKG_SIGA_REGULARIZACION_SJCS.PROC_FCS_REGULAR_SOJ":"PKG_SIGA_FACTURACION_SJCS.PROC_FCS_FACTURAR_SOJ")+
						"(?,?,?,?,?,?)}", 3, param_in_facturacion);
				if (!resultado[1].equalsIgnoreCase("0")) {
					ClsLogging.writeFileLog("Error en PL = "+(String)resultado[2],3);
					throw new ClsExceptions ("Ha ocurrido un error al ejecutar la facturaci�n de Expedientes de Orientaci�n Jur�dica: "+(String)resultado[2]);
				} 
				importeSOJ = new Double(resultado[0].replaceAll(",","."));
				importeTotal += importeSOJ.doubleValue();


				//////////////////////////////////
				// EXPEDIENTES EJG rgg 22-03-2005

				param_in_facturacion = new Object[3];
				param_in_facturacion[0] = beanFac.getIdInstitucion().toString(); // IDINSTITUCION
				param_in_facturacion[1] = beanFac.getIdFacturacion().toString(); // IDFACTURACION
				param_in_facturacion[2] = beanFac.getUsuMod().toString(); 		 // USUMODIFICACION

				resultado = new String[3];
				resultado = ClsMngBBDD.callPLProcedure("{call "+
						(esRegularizacion?"PKG_SIGA_REGULARIZACION_SJCS.PROC_FCS_REGULAR_EJG":"PKG_SIGA_FACTURACION_SJCS.PROC_FCS_FACTURAR_EJG")+
						"(?,?,?,?,?,?)}", 3, param_in_facturacion);
				if (!resultado[1].equalsIgnoreCase("0")) {
					ClsLogging.writeFileLog("Error en PL = "+(String)resultado[2],3);
					throw new ClsExceptions ("Ha ocurrido un error al ejecutar la facturaci�n de Expedientes de Justicia Gratuita: "+(String)resultado[2]);
				} 

				importeEJG = new Double(resultado[0].replaceAll(",","."));
				importeTotal += importeEJG.doubleValue();
				
				if(prevision){
					//////////////////////////////////////
					/// CREAMOS EL INFORME
					ArrayList filtrosInforme = this.getFiltrosInforme(beanFac.getIdInstitucion().toString(), beanFac.getIdFacturacion().toString());
					InformePersonalizable informePersonalizable = new InformePersonalizable();
					File fichero = informePersonalizable.getFicheroGenerado(usrbean,  InformePersonalizable.I_INFORMEFACTSJCS,null, filtrosInforme);
					beanFac.setNombreFisico(fichero.getPath());
				
					tx.rollback();
				}else{
					tx.commit();
				}


				//////////////////////////////////
				// ACTUALIZO EL TOTAL
				tx.begin();
				beanFac.setImporteEJG(importeEJG);
				beanFac.setImporteGuardia(importeGuardia);
				beanFac.setImporteOficio(importeOficio);
				beanFac.setImporteSOJ(importeSOJ);
				beanFac.setImporteTotal(new Double(importeTotal));
				if (!this.update(beanFac)) {
					throw new SIGAException(this.getError());
				}
				tx.commit();
			}	

			// Exportacion de datos a EXCEL: Se ha comentado este metodo por que no se quiere utilizar
			//UtilidadesFacturacionSJCS.exportarDatosFacturacion(new Integer(idInstitucion), new Integer(idFacturacion), this.usrbean);			
	

			//////////////////////////////////
			// cambio de estado
			tx.begin();
			beanEstado = new FcsFactEstadosFacturacionBean();
			beanEstado.setIdInstitucion(new Integer(idInstitucion));
			beanEstado.setIdFacturacion(new Integer(idFacturacion));
			beanEstado.setIdEstadoFacturacion(new Integer(ESTADO_FACTURACION.ESTADO_FACTURACION_EJECUTADA.getCodigo()));
			beanEstado.setFechaEstado("SYSDATE");
			beanEstado.setIdOrdenEstado(new Integer(admEstado.getIdordenestadoMaximo(idInstitucion, idFacturacion)));
			Thread.sleep(1000);
			admEstado.insert(beanEstado);
			tx.commit();
			
		} catch (SIGAException e) {
			try {

			    ClsLogging.writeFileLogError("Error al ejecutar facturacion SJCS.",e,3);
			    log.writeLimpio(UtilidadesString.getMensajeIdioma(this.usrbean,"mensaje.error.logFacturacion.cabecera")+" "+fecha);
			    log.writeLimpioError(e,idInstitucion,this.usrbean.getUserName());

			    tx.rollback();


			    //////////////////////////////////
				// cambio de estado
				tx.begin();
				beanEstado = new FcsFactEstadosFacturacionBean();
				beanEstado.setIdInstitucion(new Integer(idInstitucion));
				beanEstado.setIdFacturacion(new Integer(idFacturacion));
				beanEstado.setIdEstadoFacturacion(new Integer(ESTADO_FACTURACION.ESTADO_FACTURACION_ABIERTA.getCodigo()));
				beanEstado.setIdOrdenEstado(new Integer(admEstado.getIdordenestadoMaximo(idInstitucion, idFacturacion)));
				beanEstado.setFechaEstado("SYSDATE");
				Thread.sleep(1000);
				admEstado.insert(beanEstado);
				tx.commit();
			} catch (Exception ee) {
			    ClsLogging.writeFileLogError("Error al cambiar de estado facturacion SJCS por error.",ee,3);
			}
		    throw e;
		} catch (Exception e) {
			try {

			    ClsLogging.writeFileLogError("Error al ejecutar facturacion SJCS.",e,3);
			    log.writeLimpio(UtilidadesString.getMensajeIdioma(this.usrbean,"mensaje.error.logFacturacion.cabecera")+" "+fecha);
			    log.writeLimpioError(e,idInstitucion,this.usrbean.getUserName());

			    tx.rollback();

			    //////////////////////////////////
				// cambio de estado
				tx.begin();
				beanEstado = new FcsFactEstadosFacturacionBean();
				beanEstado.setIdInstitucion(new Integer(idInstitucion));
				beanEstado.setIdFacturacion(new Integer(idFacturacion));
				beanEstado.setIdEstadoFacturacion(new Integer(ESTADO_FACTURACION.ESTADO_FACTURACION_ABIERTA.getCodigo()));
				beanEstado.setIdOrdenEstado(new Integer(admEstado.getIdordenestadoMaximo(idInstitucion, idFacturacion)));
				beanEstado.setFechaEstado("SYSDATE");
				Thread.sleep(1000);
				admEstado.insert(beanEstado);
				tx.commit();
			} catch (Exception ee) {
			    ClsLogging.writeFileLogError("Error al cambiar de estado facturacion SJCS por error.",ee,3);
			}
		    throw new ClsExceptions(e,"Error en la ejecuci�n de la Facturaci�n SJCS. idinstitucion="+idInstitucion+" idfacturacion="+idFacturacion);
		}
	}
	
	public boolean isAlguienEjecutando(){
		synchronized(FcsFacturacionJGAdm.alguienEjecutando){
			if (!FcsFacturacionJGAdm.alguienEjecutando){
				FcsFacturacionJGAdm.alguienEjecutando=Boolean.TRUE;
				return false;
			} else {
				return true;
			}
		}
	}
	private void setNadieEjecutando(){
		synchronized(FcsFacturacionJGAdm.alguienEjecutando){
			FcsFacturacionJGAdm.alguienEjecutando=Boolean.FALSE;
		}
	}
	
	public ArrayList<HashMap<String, String>> getFiltrosInforme(String idInstitucion, String idFacturacion)throws ClsExceptions,SIGAException{
		
		String rutaFichero = "";
		InformePersonalizable inf = new InformePersonalizable();
		ArrayList<HashMap<String, String>> filtrosInforme = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> filtro;
		
		if (isAlguienEjecutando()){			
			throw new SIGAException(UtilidadesString.getMensajeIdioma(this.usrbean,"mensaje.error.facturacionsjcs.wait"));			
		}

		try {			
			filtro = new HashMap<String, String>();
			filtro.put(AdmTipoFiltroInformeBean.C_NOMBRECAMPO, "IDINSTITUCION");
			filtro.put("VALOR", idInstitucion);
			filtrosInforme.add(filtro);
			filtro = new HashMap<String, String>();
			filtro.put(AdmTipoFiltroInformeBean.C_NOMBRECAMPO, "FACTURACIONES");
			filtro.put("VALOR", idFacturacion);
			filtrosInforme.add(filtro);
			filtro = new HashMap<String, String>();
			filtro.put(AdmTipoFiltroInformeBean.C_NOMBRECAMPO, "IDIOMA");
			filtro.put("VALOR", this.usrbean.getLanguage());
			filtrosInforme.add(filtro);			
			
			
		} finally {
			setNadieEjecutando();
		}
		
		return filtrosInforme;							
	}
	
	public boolean facturacionesSJCSProgramadas(String idInstitucion,UsrBean usr) throws SIGAException, ClsExceptions
	{
		boolean ejecutafacturacion=false;
		
	    try {
	    	
			Hashtable codigos = new Hashtable();
			codigos.put(new Integer(1),idInstitucion);
			
			codigos.put(new Integer(2),new Integer(ESTADO_FACTURACION.ESTADO_FACTURACION_EN_EJECUCION.getCodigo()).toString());
			
			String sql = "SELECT F.IDFACTURACION, F.REGULARIZACION "+
		    		" FROM FCS_FACT_ESTADOSFACTURACION E, FCS_FACTURACIONJG F "+
    		 		" WHERE F.IDINSTITUCION = E.IDINSTITUCION "+
    		 		" AND F.IDFACTURACION = E.IDFACTURACION "+
    		 		" AND E.IDINSTITUCION=:1 "+
    		 		" AND E.IDESTADOFACTURACION =:2 "+
    		 			    		 	
    		 		" AND E.IDORDENESTADO= "+
    		 		" (Select Max(Est2.IDORDENESTADO) "+
    		 		"	From FCS_FACT_ESTADOSFACTURACION Est2 "+
    		 		"   Where Est2.IDINSTITUCION = E.IDINSTITUCION "+
    		 		"   And Est2.IDFACTURACION = E.IDFACTURACION) ";	
			//Si encontramos una fcaturacion en ejecucion es por que se ha parado inesperadamente el proceso de facturacion(por una caida)
			//Por lo tanto inseertamos el nuevo estado abierta para que vuelva a procesarla y no se quede siempre en ejecucion
			RowsContainer rc=new RowsContainer();
			rc.findBind(sql,codigos);
			if(rc!=null && rc.size()>0){
				try {
					ClsLogging.writeFileLogWithoutSession("Iniciando relanzamiento de procesos de facturaci�n bloqeuados.",1);
		        	relanzarFacturacion();
		            ClsLogging.writeFileLogWithoutSession(" > Procesos de facturaci�n relanzados OK.",1);
		            ClsLogging.writeFileLogWithoutSession("",1);
		        } catch(ClsExceptions e) {
		            ClsLogging.writeFileLogWithoutSession(" > Procesos de facturaci�n relanzados ERROR.\r\n" + e,1);
		            ClsLogging.writeFileLogWithoutSession("",1);
		            throw new ServletException("Error al relanzar procesos de facturaci�n en ejecuci�n: "+ e.toString());
		        }
			}
			
			
			codigos.put(new Integer(2),new Integer(ESTADO_FACTURACION.ESTADO_FACTURACION_PROGRAMADA.getCodigo()).toString());
			
			
			
			
			
			UserTransaction tx = usr.getTransactionPesada();
			rc.findBind(sql,codigos);
			if(rc!=null && rc.size()>0){
				int size=rc.size();
				for(int i=0;i<size;i++){
					Row r1=(Row)rc.get(i);
					Hashtable htAux=r1.getRow();
					String idFacturacion = (String) htAux.get("IDFACTURACION");
					String regularizacion = (String) htAux.get("REGULARIZACION");
					boolean bRegularizacion = false;
					if (regularizacion!=null && regularizacion.trim().equals("1")) {
					    bRegularizacion = true;
					}
					
					// Genero los multiples ficheros pendientes
					if (bRegularizacion)
					    this.ejecutarFacturacion(idInstitucion,idFacturacion,tx,true);    
					else
					    this.ejecutarFacturacion(idInstitucion,idFacturacion,tx,false);
					ejecutafacturacion=true;
					
						
			    }
			}
	    
		} catch (Exception e) {
		    throw new ClsExceptions(e,"Error al lanzar las ejecuciones de facturacion SJCS programadas");
		}
		return ejecutafacturacion;
	}
	
	public boolean yaHaSidoEjecutada (String idInstitucion,String idFacturacion) throws SIGAException, ClsExceptions
	{
	    boolean salida=false;
	    try {
			Hashtable codigos = new Hashtable();
			codigos.put(new Integer(1),new Integer(ESTADO_FACTURACION.ESTADO_FACTURACION_ABIERTA.getCodigo()).toString());
			codigos.put(new Integer(2),idInstitucion);
			codigos.put(new Integer(3),idFacturacion);
			String sql = "SELECT E.IDFACTURACION "+
			    		" FROM FCS_FACT_ESTADOSFACTURACION E "+
			    		" WHERE E.IDESTADOFACTURACION =:1 "+
			    		" AND E.IDINSTITUCION=:2 "+
			    		" AND E.IDFACTURACION=:3 ";
			   
			RowsContainer rc=new RowsContainer();
			rc.findBind(sql,codigos);
			if(rc!=null && rc.size()>0){
				int size=rc.size();
				if (size>1) {
				    salida=true;
				}
			}
	    
		} catch (Exception e) {
		    throw new ClsExceptions(e,"Error al comprobar si ya ha sido ejecutada");
		}
		return salida;
	}
	
	public PaginadorCaseSensitive getPaginadorDetalleFacturacion(MantenimientoInformesForm form, String idInstitucion,String facturaciones, String idPersona) throws ClsExceptions {
		try{
			String sql = "  SELECT  per.nombre NOMBRECOL, per.apellidos1 , per.apellidos2 , importes.idpersona,  fac.nombre , fac.idfacturacion,  "+
						 "          to_char(fac.fechadesde,'dd/mm/yyyy') fechadesde, to_char(fac.fechahasta,'dd/mm/yyyy')fechahasta," +
					     "  		decode(col.comunitario,1,col.ncomunitario,col.ncolegiado) ncolegiado," +
						 "  		SUM(impguardia) + SUM(impoficio) + SUM(impejg) + SUM(impsoj)IMPORTETOTAL," +
					     " 			SUM(impguardia)IMPORTEGUARDIA," +
					     " 			SUM(impoficio)IMPORTEOFICIO," +
					     " 			SUM(impejg)IMPORTEEJG," +
					     " 			SUM(impsoj)IMPORTESOJ" +
						 "  FROM (SELECT idpersona, " +
						 "                        idInstitucion, " +
						 "                        idFacturacion, " +
						 "                        precioaplicado + preciocostesfijos impguardia, " +
						 "                        0 impoficio, " +
						 "                        0 impejg, " +
						 "                        0 impsoj " +
						 "                   FROM fcs_fact_apunte " +
						 "                 UNION ALL " +
						 "                 SELECT idpersona, " +
						 "                       idInstitucion, " +
						 "                       idFacturacion, " +
						 "                        0, " +
						 "                        IMPORTEFACTURADO impoficio, " +
						 "                        0, " +
						 "                        0 " +
						 "                   FROM fcs_fact_actuaciondesigna " +
						 "                 UNION ALL " +
						 "                 SELECT idpersona, " +
						 "                        idInstitucion, " +
						 "                        idFacturacion, " +
						 "                        0, " +
						 "                        0, " +
						 "                        precioaplicado impejg, " +
						 "                        0 " +
						 "                   FROM fcs_fact_ejg " +
						 "                 UNION ALL " +
						 "                 SELECT idpersona, " +
						 "                        idInstitucion, " +
						 "                        idFacturacion, " +
						 "                        0, " +
						 "                        0, " +
						 "                        0, " +
						 "                        precioaplicado impsoj " +
						 "                   FROM fcs_fact_soj) importes, " +
						 "        cen_persona per, " +						 
						 "        cen_colegiado col, " +
						 "        fcs_facturacionjg fac " +
						 "  WHERE col.idpersona = importes.idpersona " +
						 "    AND col.idinstitucion = importes.idinstitucion " +
						 "    AND col.idpersona = per.idpersona " +
						 "    AND fac.idfacturacion = importes.idFacturacion " +
						 "    AND fac.idinstitucion = importes.idInstitucion " +
						 "    AND fac.idInstitucion = "+idInstitucion ;
						 
						if (facturaciones != null && !facturaciones.equals("")) {
							 sql = sql + "   AND fac.idfacturacion IN ("+facturaciones+") ";
						 }
			
						 if (idPersona != null && !idPersona.equals("")) {
							sql = sql + "    AND col.idpersona= " + idPersona;
						 }
						 
						 if (form.getInteresadoNif() != null && !form.getInteresadoNif().equals("")) {
							sql = sql + "    AND per.nifcif like '" + form.getInteresadoNif() +"%'";
						 }
						 
						 if (form.getInteresadoNombre() != null && !form.getInteresadoNombre().equals("")) {
							sql = sql + "    AND per.nombre like '" + form.getInteresadoNombre() +"%'";
						 }
						 						 
						 if (form.getInteresadoApellido1() != null && !form.getInteresadoApellido1().equals("")) {
							sql = sql + "    AND per.apellidos1 like '" + form.getInteresadoApellido1() +"%'";
						 }
						 						 						 
						 if (form.getInteresadoApellido2() != null && !form.getInteresadoApellido2().equals("")) {
							sql = sql + "    AND per.apellidos2 like '" + form.getInteresadoApellido2() +"%'";
						 }
			
						sql = sql + "	GROUP BY importes.idpersona, col.ncolegiado,col.comunitario,col.ncomunitario, fac.nombre, fac.fechadesde, fac.fechahasta,fac.idfacturacion,per.nombre,per.apellidos1,per.apellidos2 "+
									"	ORDER BY fac.fechadesde desc,per.apellidos1,per.apellidos2,per.nombre ";
			//ejecutando la consulta
			PaginadorCaseSensitive paginador = new PaginadorCaseSensitive (sql);
			int totalRegistros = paginador.getNumeroTotalRegistros();
			
			if (totalRegistros==0) {
				paginador=null;
			} else {
				paginador.getNumeroRegistrosPorPagina();
				paginador.obtenerPagina (1);
			}
			return paginador;
		
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error en getFacturaciones");
		}
	}//getPaginadorDetalleFacturacion()

	
	public Hashtable obtenerDetalleFacturacion(String idInstitucion, String idFacturacion, String idPersona) throws ClsExceptions {
		RowsContainer rc = null;
		Hashtable result = new Hashtable();
		try {
			String sql = "SELECT FAC.NOMBRE AS NOMBRE_FACTURACION, " +
						 	" TO_CHAR(FAC.FECHADESDE, 'dd/mm/yyyy') AS FECHADESDE, " +
						 	" TO_CHAR(FAC.FECHAHASTA, 'dd/mm/yyyy') AS FECHAHASTA, " +
						 	" SUM(IMPGUARDIA) + SUM(IMPOFICIO) + SUM(IMPEJG) + SUM(IMPSOJ) AS IMPORTETOTAL, " +
						 	" SUM(IMPGUARDIA) AS IMPORTEGUARDIA, " +
						 	" SUM(IMPOFICIO) AS IMPORTEOFICIO, " +
						 	" SUM(IMPEJG) AS IMPORTEEJG, " +
						 	" SUM(IMPSOJ) AS IMPORTESOJ " +
						 " FROM (" +
							 	" SELECT IDPERSONA, " +
							 		" IDINSTITUCION, " +
							 		" IDFACTURACION, " +
							 		" PRECIOAPLICADO + PRECIOCOSTESFIJOS AS IMPGUARDIA, " +
							 		" 0 AS IMPOFICIO, " +
							 		" 0 AS IMPEJG, " +
							 		" 0 AS IMPSOJ " +
							 	" FROM FCS_FACT_APUNTE " +
						 	" UNION ALL " +
							 	" SELECT IDPERSONA, " +
							 		" IDINSTITUCION, " +
							 		" IDFACTURACION, " +
							 		" 0 AS IMPGUARDIA, " +
							 		" IMPORTEFACTURADO AS IMPOFICIO, " +
							 		" 0 AS IMPEJG, " +
							 		" 0 AS IMPSOJ " +
						 		" FROM FCS_FACT_ACTUACIONDESIGNA " +
					 		" UNION ALL " +
						 		" SELECT IDPERSONA, " +
						 			" IDINSTITUCION, " +
						 			" IDFACTURACION, " +
						 			" 0 AS IMPGUARDIA, " +
						 			" 0 AS IMPOFICIO, " +
						 			" PRECIOAPLICADO AS IMPEJG, " +
						 			" 0 AS IMPSOJ " +
						 		" FROM FCS_FACT_EJG " +
					 		" UNION ALL " +
						 		" SELECT IDPERSONA, " +
						 			" IDINSTITUCION, " +
						 			" IDFACTURACION, " +
						 			" 0 AS IMPGUARDIA, " +
						 			" 0 AS IMPOFICIO, " +
						 			" 0 AS IMPEJG, " +
						 			" PRECIOAPLICADO  AS IMPSOJ " +
						 		" FROM FCS_FACT_SOJ " +
						 	") IMPORTES, " +
						 	" CEN_COLEGIADO COL, " +
						 	" FCS_FACTURACIONJG FAC " +
						 " WHERE COL.IDPERSONA = IMPORTES.IDPERSONA " +
						 	" AND COL.IDINSTITUCION = IMPORTES.IDINSTITUCION " +
						 	" AND FAC.IDFACTURACION = IMPORTES.IDFACTURACION " +
						 	" AND FAC.IDINSTITUCION = IMPORTES.IDINSTITUCION " +
						 	" AND IMPORTES.IDFACTURACION = " + idFacturacion +
						 	" AND IMPORTES.IDINSTITUCION = " + idInstitucion +
						 	" AND IMPORTES.IDPERSONA = " + idPersona +
						 " GROUP BY FAC.NOMBRE, FAC.FECHADESDE, FAC.FECHAHASTA, FAC.IDFACTURACION " +
						 " ORDER BY FAC.FECHADESDE DESC ";
		
			rc = new RowsContainer();
			rc.find(sql);
			if (rc != null && rc.size() > 0) {
				Row r = (Row) rc.get(0);
				result.putAll(r.getRow());
				
				result.put("IMPORTETOTAL", UtilidadesNumero.formato(r.getString("IMPORTETOTAL")));
				result.put("FECHADESDE", r.getString("FECHADESDE"));
				result.put("FECHAHASTA", r.getString("FECHAHASTA"));
				result.put("IMPORTEGUARDIA", UtilidadesNumero.formato(r.getString("IMPORTEGUARDIA")));
				result.put("IMPORTEOFICIO", UtilidadesNumero.formato(r.getString("IMPORTEOFICIO")));
				result.put("IMPORTEEJG", UtilidadesNumero.formato(r.getString("IMPORTEEJG")));
				result.put("IMPORTESOJ", UtilidadesNumero.formato(r.getString("IMPORTESOJ")));
			}
			
		} catch (Exception e) {
			throw new ClsExceptions(e, "Error al generar el informe");
		}
		
		return result;
	}

	public Vector getVectorDetalleFacturacionPorColegiado(String idInstitucion, String idPersona) throws ClsExceptions {
			String sql = "  SELECT  per.nombre NOMBRECOL, per.apellidos1 , per.apellidos2 , importes.idpersona, col.ncolegiado, fac.nombre NOMBREFACT, fac.idfacturacion,  "+
						 "          to_char(fac.fechadesde,'dd/mm/yyyy') fechadesde, to_char(fac.fechahasta,'dd/mm/yyyy')fechahasta," +
					     "  		SUM(impguardia) + SUM(impoficio) + SUM(impejg) + SUM(impsoj)IMPORTETOTAL," +
					     " 			SUM(impguardia)IMPORTEGUARDIA," +
					     " 			SUM(impoficio)IMPORTEOFICIO," +
					     " 			SUM(impejg)IMPORTEEJG," +
					     " 			SUM(impsoj)IMPORTESOJ" +
						 "  FROM (SELECT idpersona, " +
						 "                        idInstitucion, " +
						 "                        idFacturacion, " +
						 "                        precioaplicado + preciocostesfijos impguardia, " +
						 "                        0 impoficio, " +
						 "                        0 impejg, " +
						 "                        0 impsoj " +
						 "                   FROM fcs_fact_apunte " +
						 "                 UNION ALL " +
						 "                 SELECT idpersona, " +
						 "                       idInstitucion, " +
						 "                       idFacturacion, " +
						 "                        0, " +
						 "                        IMPORTEFACTURADO impoficio, " +
						 "                        0, " +
						 "                        0 " +
						 "                   FROM fcs_fact_actuaciondesigna " +
						 "                 UNION ALL " +
						 "                 SELECT idpersona, " +
						 "                        idInstitucion, " +
						 "                        idFacturacion, " +
						 "                        0, " +
						 "                        0, " +
						 "                        precioaplicado impejg, " +
						 "                        0 " +
						 "                   FROM fcs_fact_ejg " +
						 "                 UNION ALL " +
						 "                 SELECT idpersona, " +
						 "                        idInstitucion, " +
						 "                        idFacturacion, " +
						 "                        0, " +
						 "                        0, " +
						 "                        0, " +
						 "                        precioaplicado impsoj " +
						 "                   FROM fcs_fact_soj) importes, " +
						 "        cen_persona per, " +						 
						 "        cen_colegiado col, " +
						 "        fcs_facturacionjg fac " +
						 "  WHERE col.idpersona = importes.idpersona " +
						 "    AND col.idinstitucion = importes.idinstitucion " +
						 "    AND col.idpersona = per.idpersona " +
						 "    AND fac.idfacturacion = importes.idFacturacion " +
						 "    AND fac.idinstitucion = importes.idInstitucion " +
						 "    AND fac.idInstitucion = "+idInstitucion  +
						 "    AND col.idpersona= " + idPersona +
						 "	GROUP BY importes.idpersona, col.ncolegiado, fac.nombre, fac.fechadesde, fac.fechahasta,fac.idfacturacion,per.apellidos1,per.apellidos2,per.nombre "+
						 "	ORDER BY fac.fechadesde desc,per.apellidos1,per.apellidos2,per.nombre ";
			
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable) fila.getRow(); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en getVectorDetalleFacturacionPorColegiado. Consulta SQL:"+sql);
		}
		return datos;	
	}
	
	/**
	 * 
	 * @param idInstitucion
	 * @param idTurno
	 * @param sEstados
	 * @return
	 * @throws ClsExceptions
	 */
	public List<FcsFacturacionJGBean> getFacturacionesInformes(String idInstitucion, String idTurno, String sEstados) throws ClsExceptions {
		Hashtable<Integer, Object> htCodigos = new Hashtable<Integer, Object>();
		
		String sql = " SELECT F." + FcsFacturacionJGBean.C_IDFACTURACION + ", " +
				" TO_CHAR (F." + FcsFacturacionJGBean.C_FECHADESDE + ", 'dd/mm/yyyy') || '-' || TO_CHAR (F." + FcsFacturacionJGBean.C_FECHAHASTA + ", 'dd/mm/yyyy') || ' - ' || F." + FcsFacturacionJGBean.C_NOMBRE + " AS " + FcsFacturacionJGBean.C_NOMBRE +
			" FROM " + FcsFacturacionJGBean.T_NOMBRETABLA + " F, " +
				FcsFactEstadosFacturacionBean.T_NOMBRETABLA + " EST, " +
				FcsFactGrupoFactHitoBean.T_NOMBRETABLA + " HITO " +
			" WHERE (HITO.idgrupofacturacion = " + idTurno + " OR " + idTurno + " = -1) " +
				" AND F." + FcsFacturacionJGBean.C_IDINSTITUCION + " = " + idInstitucion + 
				" AND (F." + FcsFacturacionJGBean.C_PREVISION + " IS NULL OR F." + FcsFacturacionJGBean.C_PREVISION + " ='0') " +
				" AND F." + FcsFacturacionJGBean.C_IDINSTITUCION + " = EST." + FcsFactEstadosFacturacionBean.C_IDINSTITUCION +
				" AND F." + FcsFacturacionJGBean.C_IDFACTURACION + " = EST." + FcsFactEstadosFacturacionBean.C_IDFACTURACION +
				" AND EST." + FcsFactEstadosFacturacionBean.C_IDORDENESTADO + " = ( " +
					" SELECT MAX(EST2." + FcsFactEstadosFacturacionBean.C_IDORDENESTADO + ") " +
					" FROM " + FcsFactEstadosFacturacionBean.T_NOMBRETABLA + " EST2 " +
					" WHERE EST2." + FcsFactEstadosFacturacionBean.C_IDINSTITUCION + " = EST." + FcsFactEstadosFacturacionBean.C_IDINSTITUCION +
					" AND EST2." + FcsFactEstadosFacturacionBean.C_IDFACTURACION + " = EST." + FcsFactEstadosFacturacionBean.C_IDFACTURACION + ") " +
				" AND EST.IDESTADOFACTURACION IN (" + sEstados + ") " +
				" AND F." + FcsFacturacionJGBean.C_IDINSTITUCION + " = HITO." + FcsFactGrupoFactHitoBean.C_IDINSTITUCION +
				" AND F." + FcsFacturacionJGBean.C_IDFACTURACION + " = HITO." + FcsFactGrupoFactHitoBean.C_IDFACTURACION +
			" GROUP BY F." + FcsFacturacionJGBean.C_IDFACTURACION + ", " +
				" F." + FcsFacturacionJGBean.C_FECHADESDE + ", " +
				" F." + FcsFacturacionJGBean.C_FECHAHASTA + ", " +
				" F." + FcsFacturacionJGBean.C_NOMBRE + 
			" ORDER BY F." + FcsFacturacionJGBean.C_FECHADESDE + " DESC";
		
		List<FcsFacturacionJGBean> aFacturas = new ArrayList<FcsFacturacionJGBean>();
		try {
			RowsContainer rc = new RowsContainer();
			
			if (rc.query(sql)) {
    			for (int i = 0; i < rc.size(); i++){
            		Row fila = (Row) rc.get(i);
            		Hashtable<String, Object> htFila=fila.getRow();
            		
            		FcsFacturacionJGBean facturaBean = new FcsFacturacionJGBean();            		
            		facturaBean.setIdFacturacion(UtilidadesHash.getInteger(htFila, FcsFacturacionJGBean.C_IDFACTURACION));
            		facturaBean.setNombre(UtilidadesHash.getString(htFila, FcsFacturacionJGBean.C_NOMBRE));
            		aFacturas.add(facturaBean);
            	}
            } 
       } catch (Exception e) {
       		throw new ClsExceptions (e, "Error al ejecutar consulta.");
       }
		
       return aFacturas;		
	}

	/**
	 * BNS INC_10644_SIGA: RELANZA LAS FACTURACIONES QUE SE HAN QUEDADO EN EJECUCI�N. BORRA TODOS LOS DATOS RELATIVOS
	 * 						A LAS FACTURACIONES CON ESTADO 'EN EJECUCI�N' (INCLUYENDO LOS FICHEROS) PARA VOLVER A INSERTARLAS.
	 **/
	public void relanzarFacturacion() throws ClsExceptions {
		String sql = "SELECT E.*, F."+FcsFacturacionJGBean.C_FECHADESDE+", F."+FcsFacturacionJGBean.C_FECHAHASTA+", F."+FcsFacturacionJGBean.C_NOMBRE+
				" FROM "+FcsFactEstadosFacturacionBean.T_NOMBRETABLA+" E JOIN "+FcsFacturacionJGBean.T_NOMBRETABLA+" F"+
				" ON E."+FcsFactEstadosFacturacionBean.C_IDINSTITUCION+" = F."+FcsFacturacionJGBean.C_IDINSTITUCION+" AND E."+
				FcsFactEstadosFacturacionBean.C_IDFACTURACION+" = F."+FcsFacturacionJGBean.C_IDFACTURACION+
				" WHERE E."+FcsFactEstadosFacturacionBean.C_IDINSTITUCION+" || '_' || E."+FcsFactEstadosFacturacionBean.C_IDFACTURACION+
				" IN (SELECT EST."+FcsFacturacionJGBean.C_IDINSTITUCION+" || '_' || EST."+FcsFacturacionJGBean.C_IDFACTURACION+" FROM "+
				FcsFacturacionJGBean.T_NOMBRETABLA+" FAC, "+FcsFactEstadosFacturacionBean.T_NOMBRETABLA+" EST WHERE "+
				"FAC."+FcsFacturacionJGBean.C_IDINSTITUCION+" = EST."+FcsFactEstadosFacturacionBean.C_IDINSTITUCION+" AND "+
				"FAC."+FcsFacturacionJGBean.C_IDFACTURACION+" = EST."+FcsFactEstadosFacturacionBean.C_IDFACTURACION+" AND "+
				"EST."+FcsFactEstadosFacturacionBean.C_IDORDENESTADO+" = (SELECT MAX(EST2."+FcsFactEstadosFacturacionBean.C_IDORDENESTADO+
				") FROM "+FcsFactEstadosFacturacionBean.T_NOMBRETABLA+" EST2 WHERE "+
				"EST2."+FcsFactEstadosFacturacionBean.C_IDINSTITUCION+" = EST."+FcsFactEstadosFacturacionBean.C_IDINSTITUCION+" AND "+
				"EST2."+FcsFactEstadosFacturacionBean.C_IDFACTURACION+" = EST."+FcsFactEstadosFacturacionBean.C_IDFACTURACION+") AND "+
				"FAC."+FcsFacturacionJGBean.C_PREVISION+" = '0' AND "+
				"EST."+FcsFactEstadosFacturacionBean.C_IDESTADOFACTURACION+" = 40) AND "+
				"E."+FcsFactEstadosFacturacionBean.C_IDESTADOFACTURACION+" = 40";
				
    	try{
    		RowsContainer rc = null;
			rc = new RowsContainer();
			ClsLogging.writeFileLogWithoutSession(" >> Obteniendo facturaciones en ejecuci�n.",1);
            ClsLogging.writeFileLogWithoutSession("",1);
			if (rc.query(sql)) {
				ClsLogging.writeFileLogWithoutSession(" >> "+rc.size()+" facturaciones en ejecuci�n encontradas.",1);
	            ClsLogging.writeFileLogWithoutSession("",1);
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable FacturacionArelanzar = (Hashtable) fila.getRow(); 
					if (FacturacionArelanzar != null) {
						UserTransaction tx = null;
						String idFacturacion = "";
						String idInstitucion = "";
						try{
							// PARA CADA FACTURACI�N EN EJECUCI�N
							idFacturacion = (String) FacturacionArelanzar.get(FcsFactEstadosFacturacionBean.C_IDFACTURACION);
							idInstitucion = (String) FacturacionArelanzar.get(FcsFactEstadosFacturacionBean.C_IDINSTITUCION);
							String usrName = (String) FacturacionArelanzar.get(FcsFactEstadosFacturacionBean.C_USUMODIFICACION);
							String fechaDeInicio = (String) FacturacionArelanzar.get(FcsFacturacionJGBean.C_FECHADESDE);
							String fechaDeFin = (String) FacturacionArelanzar.get(FcsFacturacionJGBean.C_FECHAHASTA);
							Vector criterio = null;
							
							ClsLogging.writeFileLogWithoutSession(" >> Relanzando facturaci�n en ejecuci�n idInstitucion="+idInstitucion+" idFacturacion="+idFacturacion+".",1);
				            ClsLogging.writeFileLogWithoutSession("",1);
							
							// 1 - GUARDAMOS EL CRITERIO
							criterio = FcsFactGrupoFactHitoAdm.guardarCriterio(idFacturacion, idInstitucion);
							
							//Obtenemos una transacci�n pesada 
							Context ctx = new InitialContext();
							tx = (UserTransaction) ctx.lookup(UsrBean.trans);
							tx.begin();
							try {
							    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
								tx.setTransactionTimeout(new Integer(rp.returnProperty("siga.jta.timeout.pesada")).intValue());
							} catch (SystemException se) {
								ClsLogging.writeFileLogError("Error al establecer JTA timeout PESADA",se,3);
							}
							
							// 2 - BORRAMOS LA FACTURACI�N Y LOS FICHEROS ASOCIADOS
							borrarFacturacion(idInstitucion, idFacturacion, null);
							
							// 3 - VOLVEMOS A INSERTAR LA FACTURACI�N
							Hashtable datos = new Hashtable();						
							datos.put(FcsFacturacionJGBean.C_IDINSTITUCION, FacturacionArelanzar.get(FcsFactEstadosFacturacionBean.C_IDINSTITUCION));
							datos.put(FcsFacturacionJGBean.C_IDFACTURACION, FacturacionArelanzar.get(FcsFactEstadosFacturacionBean.C_IDFACTURACION));
							datos.put(FcsFacturacionJGBean.C_NOMBRE, FacturacionArelanzar.get(FcsFacturacionJGBean.C_NOMBRE));
							datos.put(FcsFacturacionJGBean.C_FECHADESDE, fechaDeInicio);
							datos.put(FcsFacturacionJGBean.C_FECHAHASTA, fechaDeFin);
							datos.put(FcsFacturacionJGBean.C_USUMODIFICACION, FacturacionArelanzar.get(FcsFacturacionJGBean.C_USUMODIFICACION));
							insertar(datos, null, idInstitucion, fechaDeInicio, fechaDeFin, usrName);
							
							// 4 - LA DEJAMOS PROGRAMADA
							FcsFactEstadosFacturacionAdm fcsFactEstadosFacturacionAdm = new FcsFactEstadosFacturacionAdm(null);
							FcsFactEstadosFacturacionBean beanEstado = new FcsFactEstadosFacturacionBean();
							beanEstado.setIdInstitucion(new Integer(idInstitucion));
							beanEstado.setIdFacturacion(new Integer(idFacturacion));
							beanEstado.setIdEstadoFacturacion(new Integer(ESTADO_FACTURACION.ESTADO_FACTURACION_PROGRAMADA.getCodigo()));
							beanEstado.setFechaEstado("SYSDATE");
							beanEstado.setIdOrdenEstado(2);
							beanEstado.setUsuMod(Integer.valueOf((String) FacturacionArelanzar.get(FcsFacturacionJGBean.C_USUMODIFICACION)));
							fcsFactEstadosFacturacionAdm.insert(beanEstado);
							
							ClsLogging.writeFileLogWithoutSession(" >> Facturaci�n en ejecuci�n idInstitucion="+idInstitucion+" idFacturacion="+idFacturacion+" RELANZADO OK.",1);
				            ClsLogging.writeFileLogWithoutSession("",1);
							tx.commit();
						} catch (Exception e) {
							ClsLogging.writeFileLogWithoutSession(" >> Facturaci�n en ejecuci�n idInstitucion="+idInstitucion+" idFacturacion="+idFacturacion+" RELANZADO ERROR: "+e.getMessage()+".",1);
				            ClsLogging.writeFileLogWithoutSession("",1);
				            if (tx != null){
				            	try{
				            		tx.rollback();
				            	} catch (Exception e2) {}
				            	tx = null;
				            }
						}
					}
				}
			} else {
				ClsLogging.writeFileLogWithoutSession(" >> NINGUNA facturaci�n en ejecuci�n encontrada.",1);
	            ClsLogging.writeFileLogWithoutSession("",1);
			}
    		    	
	    } catch (Exception e) {
	    	ClsLogging.writeFileLog(">> Error en relanzarFacturacion "+ e.getMessage(), 3);
		    ClsExceptions psscEx = new ClsExceptions(e,e.getMessage().substring(0, e.getMessage().length() - 1));
			psscEx.setErrorType("9");
			throw psscEx;
		} finally {
			// EN CUALQUIER CASO LANZAMOS EL PROCESO DE FACTURACI�N PARA QUE SE EJECUTEN LAS PROGRAMADAS
			ClsLogging.writeFileLogWithoutSession(" >> Relanzando servicio de proceso autom�tico r�pido para tratar las facturacines programadas (RELANZADAS).",1);
            ClsLogging.writeFileLogWithoutSession("",1);
//			SIGASvlProcesoAutomaticoRapido.NotificarAhora(SIGASvlProcesoAutomaticoRapido.procesoRapidoFacturacion);
		}
	}
	
	/**
	 * Borra fisicamente una facturacion de SJCS
	 * @param idInstitucion
	 * @param idFacturacion
	 * @param usr
	 * @throws SIGAException
	 * @throws ClsExceptions
	 */
	public void borrarFacturacion(String idInstitucion, String idFacturacion, UsrBean usr) throws SIGAException, ClsExceptions
	{
		// comprobando si la facturaci�n tiene mov. varios asociados
		FcsMovimientosVariosAdm movVariosAdm = new FcsMovimientosVariosAdm(usr);

		Hashtable hMovVarios = new Hashtable();
		hMovVarios.put(FcsMovimientosVariosBean.C_IDINSTITUCION, idInstitucion);
		hMovVarios.put(FcsMovimientosVariosBean.C_IDFACTURACION, idFacturacion);
		Vector vmovs = movVariosAdm.select(hMovVarios);
		if (vmovs != null && vmovs.size() > 0) {
			throw new SIGAException("factSJCS.facturacion.error.borrarFact.mov");
		}

		// ejecutando el borrado de la facturacion en BD
		Object[] param_in = new Object[2];
		param_in[0] = idInstitucion;
		param_in[1] = idFacturacion;
		String resultadoPl[] = new String[2];
		resultadoPl = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_FACTURACION_SJCS.PROC_FCS_BORRAR_FACTURACION (?,?,?,?)}", 2, param_in);

		if (!((String) resultadoPl[0]).equals("0"))
			throw new SIGAException("messages.deleted.error");

		// borrado fisico de ficheros del servidor web
		Hashtable nombreFicheros = UtilidadesFacturacionSJCS.getNombreFicherosFacturacion(new Integer(idInstitucion), new Integer(idFacturacion), usr);
		UtilidadesFacturacionSJCS.borrarFicheros(new Integer(idInstitucion), nombreFicheros, usr);
		
		if (idInstitucion != null && CajgConfiguracion.TIPO_CAJG_TXT_ALCALA == CajgConfiguracion.getTipoCAJG(Integer.parseInt(idInstitucion))) {
			//para alcala tambi�n debemos borrar la informaci�n de los ficheros de errores
			BusinessManager bm = BusinessManager.getInstance();
			bm.startTransaction();
			PcajgAlcActService pcajgAlcActService = (PcajgAlcActService) bm.getService(PcajgAlcActService.class);
			pcajgAlcActService.deletePcajgAlcActErrorCamByExample(Short.parseShort(idInstitucion), Integer.parseInt(idFacturacion));
		}
		
	} // borrarFacturacion()

	public void insertar(Hashtable datos, UsrBean usr, String idInstitucion, String fechaDeInicio, String fechaDeFin, String usrName) throws ClsExceptions, SIGAException {
		FcsFactEstadosFacturacionAdm fcsFactEstadosFacturacionAdm = new FcsFactEstadosFacturacionAdm(usr);
		//calculamos el nuevo idFacturacion
		datos.put("IDINSTITUCION", idInstitucion);
		if (usr != null && usr.getStrutsTrans().equalsIgnoreCase("FCS_MantenimientoPrevisiones")) {
			datos.put("PREVISION",ClsConstants.DB_TRUE);
		} else if (usr == null || usr.getStrutsTrans().equalsIgnoreCase("CEN_MantenimientoFacturacion")) {
			datos.put("PREVISION",ClsConstants.DB_FALSE);	
		}
		if(datos.get("IDFACTURACION")==null || datos.get("IDFACTURACION").toString().trim().equalsIgnoreCase("")){
			prepararInsert(datos);
		} else{
			//si el has tambien contenia las keys de FECHADESDE y FECHAHASTA, las convierte al formato correcto para insertar
			//en otro caso no falla
			try{
				datos.put(FcsFacturacionJGBean.C_FECHADESDE, GstDate.getApplicationFormatDate("",(String)datos.get(FcsFacturacionJGBean.C_FECHADESDE)));
				datos.put(FcsFacturacionJGBean.C_FECHAHASTA, GstDate.getApplicationFormatDate("",(String)datos.get(FcsFacturacionJGBean.C_FECHAHASTA)));
			}catch(Exception e){}
			
		}
		//ponemos el campo regularizacion a false			
		datos.put(FcsFacturacionJGBean.C_REGULARIZACION,ClsConstants.DB_FALSE);
		
		String idfacturacion=((String)datos.get("IDFACTURACION"));
		
		//preparamos la insercion en estados fact
		Hashtable estado = new Hashtable();
		estado.put(FcsFactEstadosFacturacionBean.C_IDFACTURACION , datos.get("IDFACTURACION"));
		estado.put(FcsFactEstadosFacturacionBean.C_FECHAESTADO , "sysdate");
		estado.put(FcsFactEstadosFacturacionBean.C_FECHAMODIFICACION, "sysdate");
		estado.put(FcsFactEstadosFacturacionBean.C_IDESTADOFACTURACION, String.valueOf(ESTADO_FACTURACION.ESTADO_FACTURACION_ABIERTA.getCodigo()));
		estado.put(FcsFactEstadosFacturacionBean.C_IDINSTITUCION, idInstitucion);
		estado.put(FcsFactEstadosFacturacionBean.C_USUMODIFICACION , usrName);
		estado.put(FcsFactEstadosFacturacionBean.C_IDORDENESTADO , "1");//al inicio sera un uno ya que sera el primero
		
		if(!this.insert(datos))
			throw new SIGAException("Error al insertar el elemento en BD");
		
		if(!fcsFactEstadosFacturacionAdm.insert(estado))
			throw new SIGAException("Error al insertar el elemento en BD");
	}
	
}

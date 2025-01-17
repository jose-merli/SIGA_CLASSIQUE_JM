/*
 * Created on 17/09/2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

import java.util.Hashtable;

import org.redabogacia.sigaservices.app.AppConstants;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.PaginadorCaseSensitive;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.CajgRemesaBean.TIPOREMESA;
import com.siga.gratuita.form.DefinicionRemesas_CAJG_Form;

/**
 * @author fernando.gomez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CajgRemesaAdm extends MasterBeanAdministrador {

	public CajgRemesaAdm (UsrBean usu) {
		super (CajgRemesaBean.T_NOMBRETABLA, usu);
	}	


	protected String[] getCamposBean() {
		String [] campos = {CajgRemesaBean.C_IDINSTITUCION, CajgRemesaBean.C_IDREMESA,
							CajgRemesaBean.C_PREFIJO, 		CajgRemesaBean.C_NUMERO,
							CajgRemesaBean.C_SUFIJO,		CajgRemesaBean.C_DESCRIPCION,
							CajgRemesaBean.C_IDINTERCAMBIO, CajgRemesaBean.C_IDECOMCOLA,
							CajgRemesaBean.C_IDTIPOREMESA,CajgRemesaBean.C_IDGRUPOEXPEDIENTES,  
							CajgRemesaBean.C_FECHAMODIFICACION,	CajgRemesaBean.C_USUMODIFICACION};
		return campos;
	}


	protected String[] getClavesBean() {
		String[] campos = { CajgRemesaBean.C_IDINSTITUCION, 
							CajgRemesaBean.C_IDREMESA};
		return campos;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {
		return getClavesBean();
	}

	
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		CajgRemesaBean bean = null;
		try{
			bean = new CajgRemesaBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,CajgRemesaBean.C_IDINSTITUCION));
			bean.setIdRemesa(UtilidadesHash.getInteger(hash,CajgRemesaBean.C_IDREMESA));
			bean.setPrefijo(UtilidadesHash.getString(hash, CajgRemesaBean.C_PREFIJO));
			bean.setNumero(UtilidadesHash.getString(hash,CajgRemesaBean.C_NUMERO));
			bean.setSufijo(UtilidadesHash.getString(hash, CajgRemesaBean.C_SUFIJO));
			bean.setDescripcion(UtilidadesHash.getString(hash, CajgRemesaBean.C_DESCRIPCION));
			bean.setIdIntercambio(UtilidadesHash.getInteger(hash,CajgRemesaBean.C_IDINTERCAMBIO));
			bean.setIdTipoRemesa(UtilidadesHash.getShort(hash,CajgRemesaBean.C_IDTIPOREMESA));
			bean.setIdGrupoExpedientes(UtilidadesHash.getShort(hash,CajgRemesaBean.C_IDGRUPOEXPEDIENTES));
			bean.setIdecomcola(UtilidadesHash.getLong(hash,CajgRemesaBean.C_IDECOMCOLA));
			
			bean.setFechaMod(UtilidadesHash.getString (hash,CajgRemesaBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,CajgRemesaBean.C_USUMODIFICACION));
			
		}
		catch(Exception e){
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	

	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try{
			hash = new Hashtable();
			CajgRemesaBean b = (CajgRemesaBean) bean;
			
			UtilidadesHash.set(hash, CajgRemesaBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(hash, CajgRemesaBean.C_IDREMESA, b.getIdRemesa());
			UtilidadesHash.set(hash, CajgRemesaBean.C_PREFIJO, b.getPrefijo());
			UtilidadesHash.set(hash, CajgRemesaBean.C_NUMERO, b.getNumero());
			UtilidadesHash.set(hash, CajgRemesaBean.C_SUFIJO, b.getSufijo());
			UtilidadesHash.set(hash, CajgRemesaBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(hash, CajgRemesaBean.C_IDINTERCAMBIO, b.getIdIntercambio());
			UtilidadesHash.set(hash, CajgRemesaBean.C_IDTIPOREMESA, b.getIdTipoRemesa());
			UtilidadesHash.set(hash, CajgRemesaBean.C_IDGRUPOEXPEDIENTES, b.getIdGrupoExpedientes());
			UtilidadesHash.set(hash, CajgRemesaBean.C_IDECOMCOLA, b.getIdecomcola());
			
			UtilidadesHash.set(hash, CajgRemesaBean.C_FECHAMODIFICACION, b.getFechaMod());	
			UtilidadesHash.set(hash, CajgRemesaBean.C_USUMODIFICACION, b.getUsuMod());	
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	}

	public String SeleccionarMaximo(String  idinstitucion) throws ClsExceptions 
	{
		RowsContainer rc = null;		
		String numeroMaximo = null;		
		
		try { 
			rc = new RowsContainer();		
			
			
			String sql ="SELECT (MAX("+ CajgRemesaBean.C_IDREMESA + ") + 1) AS IDREMESA FROM " + nombreTabla + " WHERE IDINSTITUCION = " + idinstitucion;			 
			if (rc.query(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();			
				if (prueba.get("IDREMESA").equals("")) {
					numeroMaximo = "1";
				}
				else numeroMaximo = prueba.get("IDREMESA").toString();				
			}						
		}	
		catch (ClsExceptions e) {
			throw e;			
		}
		catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN PREPARAR INSERCI�N. C�LCULO DE NUMERO");
		};
		
		return numeroMaximo;
	}
	public PaginadorCaseSensitive getPaginadorRemesas(DefinicionRemesas_CAJG_Form miForm ) throws ClsExceptions{
		Hashtable miHash =  miForm.getDatos();
		int estado=0;
		String consulta = "select r.idremesa AS IDREMESA,       r.idinstitucion AS idinstitucion,r.prefijo AS PREFIJO,r.numero AS NUMERO,r.sufijo AS SUFIJO,"
				+ "r.descripcion AS DESCRIPCION_REMESA," + "" +
				" e.idestado IDESTADO"
				+ "," + "f_siga_getrecurso(t.descripcion, " + this.usrbean.getLanguage()
				+ ") AS ESTADO, r.IDGRUPOEXPEDIENTES "
				+ " from cajg_remesa r, cajg_remesaestados e, cajg_tipoestadoremesa t" + " where r.idinstitucion = " + new Integer(this.usrbean.getLocation())
				+ "" + " and r.idinstitucion = e.idinstitucion" + " and r.idremesa = e.idremesa" + " and e.idestado = t.idestado"
				+ " and e.idestado = (select max(idestado)" + " from cajg_remesaestados" + " where idinstitucion = e.idinstitucion"
				+ " and idremesa = e.idremesa) ";
			
		
		if(miForm.getIdTipoRemesa()!=null && !miForm.getIdTipoRemesa().equalsIgnoreCase(CajgRemesaBean.TIPOREMESA.REMESA_EJGS.getIdTipo())){
			consulta += " AND IDTIPOREMESA = "+miForm.getIdTipoRemesa();
		}else{
			consulta += " AND (IDTIPOREMESA IS NULL OR  IDTIPOREMESA = "+CajgRemesaBean.TIPOREMESA.REMESA_EJGS.getIdTipo()+") ";
		}
		
		
		if ((String) miHash.get(CajgRemesaBean.C_PREFIJO) != null && (!((String) miHash.get(CajgRemesaBean.C_PREFIJO)).equals(""))) {					
			consulta += " AND " + ComodinBusquedas.prepararSentenciaNLS(((String) miHash.get(CajgRemesaBean.C_PREFIJO)).trim(), "r." + CajgRemesaBean.C_PREFIJO);
		}
		if ((String) miHash.get(CajgRemesaBean.C_NUMERO) != null && (!((String) miHash.get(CajgRemesaBean.C_NUMERO)).equals(""))) {					
			consulta += " AND " + ComodinBusquedas.prepararSentenciaNLS(((String) miHash.get(CajgRemesaBean.C_NUMERO)).trim(), "r." + CajgRemesaBean.C_NUMERO);
		}
		if ((String) miHash.get(CajgRemesaBean.C_SUFIJO) != null && (!((String) miHash.get(CajgRemesaBean.C_SUFIJO)).equals(""))) {
			consulta += " AND " + ComodinBusquedas.prepararSentenciaNLS(((String) miHash.get(CajgRemesaBean.C_SUFIJO)).trim(), "r." + CajgRemesaBean.C_SUFIJO);					
		}
		
		if ((String) miHash.get("DESCRIPCION") != null && (!((String) miHash.get("DESCRIPCION")).equals(""))) {
			// consulta +=" and r.descripcion like
			// '%"+(String)miHash.get("DESCRIPCION")+"'";
			consulta += " AND " + ComodinBusquedas.prepararSentenciaNLS(((String) miHash.get("DESCRIPCION")).trim(), "r.descripcion");
		}
		if ((String) miHash.get("IDESTADO") != null && (!((String) miHash.get("IDESTADO")).equals(""))) {
			consulta += " and e.idestado=" + (String) miHash.get("IDESTADO") + "";
		}
		
		//definiendo los tipos de fechas.
		String tipofecha=miForm.getTipoFecha();				
		if (tipofecha.equals(ClsConstants.COMBO_MOSTRAR_GENERACION)){
			estado=ClsConstants.ESTADO_REMESA_GENERADA;					
		}else if (tipofecha.equals(ClsConstants.COMBO_MOSTRAR_ENVIO)){
				  estado=ClsConstants.ESTADO_REMESA_ENVIADA;
				}else if (tipofecha.equals(ClsConstants.COMBO_MOSTRAR_RECEPCION)){	
						estado=ClsConstants.ESTADO_REMESA_RECIBIDA;
				}
		

		if ((tipofecha.equals(ClsConstants.COMBO_MOSTRAR_GENERACION)) || (tipofecha.equals(ClsConstants.COMBO_MOSTRAR_ENVIO)) || (tipofecha.equals(ClsConstants.COMBO_MOSTRAR_RECEPCION))){				
			if ((!miForm.getFechaInicioBuscar().trim().equals("")) && (!miForm.getFechaFinBuscar().trim().equals(""))){
					consulta += " and f_siga_get_fechaEstadoRemesa(e.idinstitucion, e.idremesa,"+estado+")>= TO_DATE('"+miForm.getFechaInicioBuscar()+"', 'DD/MM/YYYY')"+
			       " and f_siga_get_fechaEstadoRemesa(e.idinstitucion, e.idremesa, "+estado+")<= TO_DATE('"+miForm.getFechaFinBuscar()+"', 'DD/MM/YYYY')";					
			}else if(miForm.getFechaInicioBuscar().trim().equals("")&&  (!miForm.getFechaFinBuscar().trim().equals(""))){
					consulta +=" and f_siga_get_fechaEstadoRemesa(e.idinstitucion, e.idremesa,"+estado+") <= TO_DATE('"+miForm.getFechaFinBuscar()+"', 'DD/MM/YYYY')";
				  }else if(!miForm.getFechaInicioBuscar().trim().equals("")&&  (miForm.getFechaFinBuscar().trim().equals(""))){
					  consulta +=" and f_siga_get_fechaEstadoRemesa(e.idinstitucion, e.idremesa, "+estado+") >= TO_DATE('"+miForm.getFechaInicioBuscar()+"', 'DD/MM/YYYY')";
				  }
		}
		if( (miForm.getAnioEJG()!=null && !miForm.getAnioEJG().equals("")) || (miForm.getCodigoEJG()!=null && !miForm.getCodigoEJG().equals(""))){
			consulta +=" AND (R.IDREMESA, R.IDINSTITUCION ) IN "
					+"(                   SELECT IDREMESA, IDINSTITUCIONREMESA"
					+" FROM CAJG_EJGREMESA ER, SCS_EJG EJG "
                   
					+" WHERE ER.IDINSTITUCION = EJG.IDINSTITUCION "
                      +"AND ER.ANIO = EJG.ANIO "
                      +"AND ER.IDTIPOEJG = EJG.IDTIPOEJG "
                      +"AND ER.NUMERO = EJG.NUMERO "
                      +"AND EJG.IDINSTITUCION = ";
			consulta+= this.usrbean.getLocation()+" ";
			
			 
			if(miForm.getCodigoEJG()!=null && !miForm.getCodigoEJG().equals("")){
				consulta +=" AND EJG.NUMEJG =  ";
				consulta += miForm.getCodigoEJG();
			}
			if(miForm.getAnioEJG()!=null && !miForm.getAnioEJG().equals("")){
                      
				consulta +=" AND EJG.ANIO = ";
				consulta += miForm.getAnioEJG();
			}
            
			
			
			consulta +=") ";
			
		}
		
		consulta += " order by r.prefijo DESC,r.numero DESC, r.sufijo DESC";		
		return new PaginadorCaseSensitive(consulta);
	} 
	
	public Hashtable getDatosRemesa(String idRemesa, String idInstitucion) throws ClsExceptions {
		Hashtable salida = new Hashtable();
		
		try {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(" ");
			
			stringBuilder.append("SELECT (SELECT DECODE(COUNT(1), 0, NULL, COUNT(1)) ");
			stringBuilder.append("FROM CAJG_EJGREMESA ER ");
			stringBuilder.append("WHERE ER.IDEJGREMESA IN ");
			stringBuilder.append("(SELECT RE.IDEJGREMESA ");
			stringBuilder.append("FROM CAJG_RESPUESTA_EJGREMESA RE ");
			stringBuilder.append(") ");
			stringBuilder.append("AND (ER.ANIO, ER.NUMERO, ER.IDTIPOEJG, ER.IDINSTITUCION) NOT IN ");
			stringBuilder.append("(SELECT ER2.ANIO, ER2.NUMERO, ER2.IDTIPOEJG, ER2.IDINSTITUCION ");
			stringBuilder.append("FROM CAJG_EJGREMESA ER2, CAJG_REMESA REM2 ");
			stringBuilder.append("WHERE ER2.IDINSTITUCION = REM2.IDINSTITUCION ");
			stringBuilder.append("AND ER2.IDREMESA = REM2.IDREMESA ");
			stringBuilder.append("AND REM2.IDTIPOREMESA = REM.IDTIPOREMESA ");
			stringBuilder.append("AND ER2.IDINSTITUCION = ER.IDINSTITUCION ");
			stringBuilder.append("AND ER.IDREMESA < ER2.IDREMESA) ");
			stringBuilder.append("AND ER.IDREMESA = REM.IDREMESA ");
			stringBuilder.append("AND ER.IDINSTITUCION =  REM.IDINSTITUCION) CUENTA_INCIDENCIAS, ");
			stringBuilder.append("(SELECT COUNT(1) ");
			stringBuilder.append("FROM CAJG_EJGREMESA ER ");
			stringBuilder.append("WHERE ER.IDINSTITUCION =  REM.IDINSTITUCION ");
			stringBuilder.append("AND ER.IDREMESA = REM.IDREMESA) CUENTA_EXPEDIENTES, ");
			
		 
			stringBuilder.append("F_SIGA_GET_FECHAESTADOREMESA( REM.IDINSTITUCION, REM.IDREMESA, 1) AS FECHAGENERACION, ");
			stringBuilder.append("F_SIGA_GET_FECHAESTADOREMESA( REM.IDINSTITUCION, REM.IDREMESA, 2) AS FECHAENVIO, ");
			stringBuilder.append("F_SIGA_GET_FECHAESTADOREMESA( REM.IDINSTITUCION, REM.IDREMESA, 3) AS FECHARECEPCION ");
			stringBuilder.append(", REM.IDGRUPOEXPEDIENTES ");
			
			stringBuilder.append("FROM CAJG_REMESA REM ");
			stringBuilder.append("WHERE REM.IDINSTITUCION = ");
			stringBuilder.append(idInstitucion);
			stringBuilder.append(" AND REM.IDREMESA = ");
			stringBuilder.append(idRemesa);
			
			
			
			
			
			RowsContainer rc = new RowsContainer(); 
			if (rc.find(stringBuilder.toString())) {
				if (rc.size() > 0) {
					Row fila = (Row) rc.get(0);
					salida = fila.getRow();	                  
				}
			} 
				
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion de la remesa");
		}
		
		return salida;
	}	
	
	
}
package com.siga.beans;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.censo.form.AnexosCuentasBancariasForm;

public class CenAnexosCuentasBancariasAdm extends MasterBeanAdministrador {
	
	private StringBuilder sqlMandatosSelect = new StringBuilder();
	private StringBuilder sqlMandatosFrom = new StringBuilder();
	private StringBuilder sqlAnexosSelect = new StringBuilder();
	private StringBuilder sqlAnexosFrom = new StringBuilder();
			
	public CenAnexosCuentasBancariasAdm(UsrBean usuario) {
	    super(CenAnexosCuentasBancariasBean.T_NOMBRETABLA, usuario);
	    
	    StringBuilder sql = new StringBuilder();
	    sql.append("SELECT MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_IDINSTITUCION);
	    sql.append(" AS ");
	    sql.append(CenAnexosCuentasBancariasBean.C_IDINSTITUCION);
	    sql.append(", MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_IDPERSONA);
	    sql.append(" AS ");
	    sql.append(CenAnexosCuentasBancariasBean.C_IDPERSONA);
	    sql.append(", MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_IDCUENTA);
	    sql.append(" AS ");
	    sql.append(CenAnexosCuentasBancariasBean.C_IDCUENTA);
	    sql.append(", MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_IDMANDATO);
	    sql.append(" AS ");
	    sql.append(CenAnexosCuentasBancariasBean.C_IDMANDATO);
	    sql.append(", NULL AS ");
	    sql.append(CenAnexosCuentasBancariasBean.C_IDANEXO);
	    sql.append(", NULL AS ");
	    sql.append(CenAnexosCuentasBancariasBean.C_ORIGEN);
	    sql.append(", NULL AS ");
	    sql.append(CenAnexosCuentasBancariasBean.C_DESCRIPCION);
	    sql.append(", TO_CHAR(MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_FIRMA_FECHA);
	    sql.append(", 'DD/MM/YYYY') AS ");
	    sql.append(CenAnexosCuentasBancariasBean.C_FIRMA_FECHA);
	    sql.append(", MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_FIRMA_LUGAR);
	    sql.append(" AS ");
	    sql.append(CenAnexosCuentasBancariasBean.C_FIRMA_LUGAR);
	    sql.append(", MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_IDFICHEROFIRMA);
	    sql.append(" AS ");
	    sql.append(CenAnexosCuentasBancariasBean.C_IDFICHEROFIRMA);
	    sql.append(", TO_CHAR(MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_FECHACREACION);
	    sql.append(", 'DD/MM/YYYY') AS ");
	    sql.append(CenAnexosCuentasBancariasBean.C_FECHACREACION);
	    sql.append(", MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_USUCREACION);
	    sql.append(" AS ");
	    sql.append(CenAnexosCuentasBancariasBean.C_USUCREACION);
	    sql.append(", TO_CHAR(MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_FECHAMODIFICACION);
	    sql.append(", 'DD/MM/YYYY') AS ");
	    sql.append(CenAnexosCuentasBancariasBean.C_FECHAMODIFICACION);
	    sql.append(", MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_USUMODIFICACION);
	    sql.append(" AS ");
	    sql.append(CenAnexosCuentasBancariasBean.C_USUMODIFICACION);
	    sql.append(", MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_FECHACREACION);
	    sql.append(" AS ");
	    sql.append(CenAnexosCuentasBancariasBean.C_FECHAORDEN);
	    sql.append(", MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_FECHAUSO);
	    sql.append(", NULL AS ");
	    sql.append(CenAnexosCuentasBancariasBean.C_ESAUTOMATICO);
	    this.sqlMandatosSelect = sql;	  
	    
	    sql = new StringBuilder();
	    sql.append(" FROM ");
	    sql.append(CenMandatosCuentasBancariasBean.T_NOMBRETABLA);
	    sql.append(" MANDATOS ");;
	    this.sqlMandatosFrom = sql;
	    
	    sql = new StringBuilder();
	    sql.append("SELECT ANEXOS.");
	    sql.append(CenAnexosCuentasBancariasBean.C_IDINSTITUCION);
	    sql.append(", ANEXOS.");
	    sql.append(CenAnexosCuentasBancariasBean.C_IDPERSONA);
	    sql.append(", ANEXOS.");
	    sql.append(CenAnexosCuentasBancariasBean.C_IDCUENTA);
	    sql.append(", ANEXOS.");
	    sql.append(CenAnexosCuentasBancariasBean.C_IDMANDATO);
	    sql.append(", ANEXOS.");
	    sql.append(CenAnexosCuentasBancariasBean.C_IDANEXO);
	    sql.append(", ANEXOS.");
	    sql.append(CenAnexosCuentasBancariasBean.C_ORIGEN);
	    sql.append(", ANEXOS.");
	    sql.append(CenAnexosCuentasBancariasBean.C_DESCRIPCION);
	    sql.append(", TO_CHAR(ANEXOS.");
	    sql.append(CenAnexosCuentasBancariasBean.C_FIRMA_FECHA);
	    sql.append(", 'DD/MM/YYYY') AS ");
	    sql.append(CenAnexosCuentasBancariasBean.C_FIRMA_FECHA);
	    sql.append(", ANEXOS.");
	    sql.append(CenAnexosCuentasBancariasBean.C_FIRMA_LUGAR);
	    sql.append(", ANEXOS.");
	    sql.append(CenAnexosCuentasBancariasBean.C_IDFICHEROFIRMA);
	    sql.append(", TO_CHAR(ANEXOS.");
	    sql.append(CenAnexosCuentasBancariasBean.C_FECHACREACION);
	    sql.append(", 'DD/MM/YYYY') AS ");
	    sql.append(CenAnexosCuentasBancariasBean.C_FECHACREACION);
	    sql.append(", ANEXOS.");
	    sql.append(CenAnexosCuentasBancariasBean.C_USUCREACION);
	    sql.append(", TO_CHAR(ANEXOS.");
	    sql.append(CenAnexosCuentasBancariasBean.C_FECHAMODIFICACION);
	    sql.append(", 'DD/MM/YYYY') AS ");
	    sql.append(CenAnexosCuentasBancariasBean.C_FECHAMODIFICACION);
	    sql.append(", ANEXOS.");
	    sql.append(CenAnexosCuentasBancariasBean.C_USUMODIFICACION);
	    sql.append(", ANEXOS.");
	    sql.append(CenAnexosCuentasBancariasBean.C_FECHACREACION);
	    sql.append(" AS ");
	    sql.append(CenAnexosCuentasBancariasBean.C_FECHAORDEN);
	    sql.append(", NULL AS ");
	    sql.append(CenMandatosCuentasBancariasBean.C_FECHAUSO);
	    sql.append(", ANEXOS.");
	    sql.append(CenAnexosCuentasBancariasBean.C_ESAUTOMATICO);	 	    
	    this.sqlAnexosSelect = sql;
	    
	    sql = new StringBuilder();
	    sql.append(" FROM ");
	    sql.append(CenAnexosCuentasBancariasBean.T_NOMBRETABLA);
	    sql.append(" ANEXOS ");;
	    this.sqlAnexosFrom = sql;
	}

	protected String[] getCamposBean() {
		String[] campos = {
			CenAnexosCuentasBancariasBean.C_IDINSTITUCION, 
			CenAnexosCuentasBancariasBean.C_IDPERSONA, 
			CenAnexosCuentasBancariasBean.C_IDCUENTA, 
			CenAnexosCuentasBancariasBean.C_IDMANDATO,
			CenAnexosCuentasBancariasBean.C_IDANEXO,
			CenAnexosCuentasBancariasBean.C_FECHACREACION,
			CenAnexosCuentasBancariasBean.C_USUCREACION,
			CenAnexosCuentasBancariasBean.C_ORIGEN,
			CenAnexosCuentasBancariasBean.C_DESCRIPCION, 
			CenAnexosCuentasBancariasBean.C_FIRMA_FECHA,
			CenAnexosCuentasBancariasBean.C_FIRMA_LUGAR, 
			CenAnexosCuentasBancariasBean.C_IDFICHEROFIRMA,
			CenAnexosCuentasBancariasBean.C_FECHAMODIFICACION,
			CenAnexosCuentasBancariasBean.C_USUMODIFICACION};

		return campos;
	}

	protected String[] getClavesBean() {
		String[] claves = {
			CenAnexosCuentasBancariasBean.C_IDINSTITUCION, 
			CenAnexosCuentasBancariasBean.C_IDPERSONA, 
			CenAnexosCuentasBancariasBean.C_IDCUENTA, 
			CenAnexosCuentasBancariasBean.C_IDMANDATO,
			CenAnexosCuentasBancariasBean.C_IDANEXO};

		return claves;
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		CenAnexosCuentasBancariasBean bean = new CenAnexosCuentasBancariasBean();;

		try	{
			bean.setIdInstitucion(UtilidadesHash.getString(hash, CenAnexosCuentasBancariasBean.C_IDINSTITUCION)); 
			bean.setIdPersona(UtilidadesHash.getString(hash, CenAnexosCuentasBancariasBean.C_IDPERSONA)); 
			bean.setIdCuenta(UtilidadesHash.getString(hash, CenAnexosCuentasBancariasBean.C_IDCUENTA)); 
			bean.setIdMandato(UtilidadesHash.getString(hash, CenAnexosCuentasBancariasBean.C_IDMANDATO));
			bean.setIdAnexo(UtilidadesHash.getString(hash, CenAnexosCuentasBancariasBean.C_IDANEXO));
			bean.setFechaCreacion(UtilidadesHash.getString(hash, CenAnexosCuentasBancariasBean.C_FECHACREACION));
			bean.setUsuCreacion(UtilidadesHash.getString(hash, CenAnexosCuentasBancariasBean.C_USUCREACION));
			bean.setOrigen(UtilidadesHash.getString(hash, CenAnexosCuentasBancariasBean.C_ORIGEN)); 
			bean.setDescripcion(UtilidadesHash.getString(hash, CenAnexosCuentasBancariasBean.C_DESCRIPCION));
			bean.setFirmaFecha(UtilidadesHash.getString(hash, CenAnexosCuentasBancariasBean.C_FIRMA_FECHA));
			bean.setFirmaLugar(UtilidadesHash.getString(hash, CenAnexosCuentasBancariasBean.C_FIRMA_LUGAR)); 
			bean.setIdFicheroFirma(UtilidadesHash.getString(hash, CenAnexosCuentasBancariasBean.C_IDFICHEROFIRMA));
			bean.setFechaMod(UtilidadesHash.getString(hash, CenAnexosCuentasBancariasBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash, CenAnexosCuentasBancariasBean.C_USUMODIFICACION));
			bean.setFechaUso(UtilidadesHash.getString(hash, CenMandatosCuentasBancariasBean.C_FECHAUSO));
			bean.setTipoAnexoAutomatico(UtilidadesHash.getString(hash, CenAnexosCuentasBancariasBean.C_ESAUTOMATICO));
			
		} catch (Exception e) {
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}

		return bean;
	}

	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable<String,Object> htAnexo = new Hashtable<String,Object>();

		try {			
			CenAnexosCuentasBancariasBean beanAnexo = (CenAnexosCuentasBancariasBean) bean;
			
			UtilidadesHash.set(htAnexo, CenAnexosCuentasBancariasBean.C_IDINSTITUCION, beanAnexo.getIdInstitucion());
			UtilidadesHash.set(htAnexo, CenAnexosCuentasBancariasBean.C_IDPERSONA, beanAnexo.getIdPersona());
			UtilidadesHash.set(htAnexo, CenAnexosCuentasBancariasBean.C_IDCUENTA, beanAnexo.getIdCuenta());
			UtilidadesHash.set(htAnexo, CenAnexosCuentasBancariasBean.C_IDMANDATO, beanAnexo.getIdMandato());
			UtilidadesHash.set(htAnexo, CenAnexosCuentasBancariasBean.C_IDANEXO, beanAnexo.getIdAnexo());
			UtilidadesHash.set(htAnexo, CenAnexosCuentasBancariasBean.C_FECHACREACION, beanAnexo.getFechaCreacion());
			UtilidadesHash.set(htAnexo, CenAnexosCuentasBancariasBean.C_USUCREACION, beanAnexo.getUsuCreacion());
			UtilidadesHash.set(htAnexo, CenAnexosCuentasBancariasBean.C_ORIGEN, beanAnexo.getOrigen());
			UtilidadesHash.set(htAnexo, CenAnexosCuentasBancariasBean.C_DESCRIPCION, beanAnexo.getDescripcion());
			UtilidadesHash.set(htAnexo, CenAnexosCuentasBancariasBean.C_FIRMA_FECHA, beanAnexo.getFirmaFecha());
			UtilidadesHash.set(htAnexo, CenAnexosCuentasBancariasBean.C_FIRMA_LUGAR, beanAnexo.getFirmaLugar());
			UtilidadesHash.set(htAnexo, CenAnexosCuentasBancariasBean.C_IDFICHEROFIRMA, beanAnexo.getIdFicheroFirma());
			UtilidadesHash.set(htAnexo, CenAnexosCuentasBancariasBean.C_FECHAMODIFICACION, beanAnexo.getFechaMod());
			UtilidadesHash.set(htAnexo, CenAnexosCuentasBancariasBean.C_USUMODIFICACION, beanAnexo.getUsuMod());
			
		} catch (Exception e) {
			htAnexo = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htAnexo;
	}

    protected String[] getOrdenCampos() {
        return null;
    } 
    
    /**
     * Obtengo el mandato y anexos de un mandato
     * @param beanAnexo
     * @return
     * @throws ClsExceptions
     */
	public Vector<CenAnexosCuentasBancariasBean> obtenerAnexos(CenMandatosCuentasBancariasBean beanMandato) throws ClsExceptions {
		try {
			// JPT: CEN_MANDATOS_CUENTASBANCARIAS  UNION  CEN_ANEXOS_CUENTASBANCARIAS
			StringBuilder sql = new StringBuilder(); 
			sql.append(this.sqlMandatosSelect); 
			sql.append(this.sqlMandatosFrom); 
			sql.append(" WHERE MANDATOS.");
			sql.append(CenAnexosCuentasBancariasBean.C_IDINSTITUCION);
			sql.append(" = ");
			sql.append(beanMandato.getIdInstitucion());
			sql.append(" AND MANDATOS.");
			sql.append(CenAnexosCuentasBancariasBean.C_IDPERSONA);
			sql.append(" = ");
			sql.append(beanMandato.getIdPersona()); 
			sql.append(" AND MANDATOS.");
			sql.append(CenAnexosCuentasBancariasBean.C_IDCUENTA);
			sql.append(" = ");
			sql.append(beanMandato.getIdCuenta());
			sql.append(" AND MANDATOS.");
			sql.append(CenAnexosCuentasBancariasBean.C_IDMANDATO);
			sql.append(" = ");
			sql.append(beanMandato.getIdMandato());
			sql.append(" UNION ALL ");	
			sql.append(this.sqlAnexosSelect); 
			sql.append(this.sqlAnexosFrom); 
			sql.append(" WHERE ANEXOS.");
			sql.append(CenAnexosCuentasBancariasBean.C_IDINSTITUCION);
			sql.append(" = ");
			sql.append(beanMandato.getIdInstitucion());
			sql.append(" AND ANEXOS.");
			sql.append(CenAnexosCuentasBancariasBean.C_IDPERSONA);
			sql.append(" = ");
			sql.append(beanMandato.getIdPersona()); 
			sql.append(" AND ANEXOS.");
			sql.append(CenAnexosCuentasBancariasBean.C_IDCUENTA);
			sql.append(" = ");
			sql.append(beanMandato.getIdCuenta());
			sql.append(" AND ANEXOS.");
			sql.append(CenAnexosCuentasBancariasBean.C_IDMANDATO);
			sql.append(" = ");
			sql.append(beanMandato.getIdMandato());
			sql.append(" ORDER BY ");
			sql.append(CenAnexosCuentasBancariasBean.C_FECHAORDEN);
			sql.append(" ASC");
			
			Vector<CenAnexosCuentasBancariasBean> vAnexos = null;
			RowsContainer rc = new RowsContainer(); 												
			if (rc.find(sql.toString())) {        	   
				vAnexos = new Vector<CenAnexosCuentasBancariasBean>();
    			for (int i = 0; i < rc.size(); i++){
					Row fila = (Row) rc.get(i);
					CenAnexosCuentasBancariasBean anexoBean = (CenAnexosCuentasBancariasBean) this.hashTableToBean(fila.getRow());					
					vAnexos.add(anexoBean);
				}
            }		
			
			return vAnexos;
			
		} catch(Exception e) {			
			throw new ClsExceptions (e, "Error al ejecutar obtenerAnexos");
		}
	}      		
	
    /**
     * Obtengo los datos de un anexo
     * @param beanAnexo
     * @return
     * @throws ClsExceptions
     */
	public CenAnexosCuentasBancariasBean obtenerAnexo(CenAnexosCuentasBancariasBean beanAnexo) throws ClsExceptions {
		try {
			StringBuilder sql = new StringBuilder(); 
			sql.append(this.sqlAnexosSelect); 
			sql.append(this.sqlAnexosFrom); 
			sql.append(" WHERE ");
			sql.append(CenAnexosCuentasBancariasBean.C_IDINSTITUCION);
			sql.append(" = ");
			sql.append(beanAnexo.getIdInstitucion());
			sql.append(" AND ");
			sql.append(CenAnexosCuentasBancariasBean.C_IDPERSONA);
			sql.append(" = ");
			sql.append(beanAnexo.getIdPersona()); 
			sql.append(" AND ");
			sql.append(CenAnexosCuentasBancariasBean.C_IDCUENTA);
			sql.append(" = ");
			sql.append(beanAnexo.getIdCuenta()); 
			sql.append(" AND ");
			sql.append(CenAnexosCuentasBancariasBean.C_IDMANDATO);
			sql.append(" = ");
			sql.append(beanAnexo.getIdMandato());
			sql.append(" AND ");
			sql.append(CenAnexosCuentasBancariasBean.C_IDANEXO);
			sql.append(" = ");
			sql.append(beanAnexo.getIdAnexo());
			
			RowsContainer rc = new RowsContainer(); 												
			if (rc.find(sql.toString()) && rc.size()>0) {
				Row fila = (Row) rc.get(0);
				beanAnexo = (CenAnexosCuentasBancariasBean) this.hashTableToBean(fila.getRow());
            }		
			
			return beanAnexo;
			
		} catch(Exception e) {			
			throw new ClsExceptions (e, "Error al ejecutar obtenerAnexo");
		}
	}
	
	/**
	 * 
	 * @param beanAnexo
	 * @param isFirmado
	 * @return
	 */
	private String getSqlObtenerAnexo(CenAnexosCuentasBancariasBean beanAnexo, Boolean isFirmado){
		StringBuffer sql = new StringBuffer();
		sql.append(this.sqlAnexosSelect);
		sql.append(this.sqlAnexosFrom);
		sql.append(" WHERE ");
		sql.append(CenAnexosCuentasBancariasBean.C_IDINSTITUCION);
		sql.append(" = ");
		sql.append(beanAnexo.getIdInstitucion());
		sql.append(" AND ");
		sql.append(CenAnexosCuentasBancariasBean.C_IDPERSONA);
		sql.append(" = ");
		sql.append(beanAnexo.getIdPersona());
		
		if (beanAnexo.getIdCuenta()!=null) {
			sql.append(" AND ");
			sql.append(CenAnexosCuentasBancariasBean.C_IDCUENTA);
			sql.append(" = ");
			sql.append(beanAnexo.getIdCuenta());
		}
		
		if (beanAnexo.getIdMandato()!=null) {
			sql.append(" AND ");
			sql.append(CenAnexosCuentasBancariasBean.C_IDMANDATO);
			sql.append(" = ");
			sql.append(beanAnexo.getIdMandato());
		} else {
			if (isFirmado!=null) {
				if (isFirmado) {
					sql.append(" AND ");
					sql.append(CenAnexosCuentasBancariasBean.C_FIRMA_FECHA);
					sql.append(" IS NOT NULL ");
				} else {
					sql.append(" AND  ");
					sql.append(CenAnexosCuentasBancariasBean.C_FIRMA_FECHA);
					sql.append(" IS  NULL ");
				}
			}	
		}
		
		if (beanAnexo.getIdAnexo()!=null) {
			sql.append(" AND ");
			sql.append(CenAnexosCuentasBancariasBean.C_IDANEXO);
			sql.append(" = ");
			sql.append(beanAnexo.getIdAnexo());
		}
		
		return sql.toString();
	} 
	
	/**
	 * 
	 * @param beanAnexo
	 * @param isFirmado
	 * @return
	 * @throws ClsExceptions
	 */
	public List<Hashtable<String,Object>> getAnexos(CenAnexosCuentasBancariasBean beanAnexo,Boolean isFirmado) throws ClsExceptions {
		List<Hashtable<String,Object>> anexosList = null;		
		try {
			CenMandatosCuentasBancariasAdm cenMandatosCuentasBancariasAdm = new CenMandatosCuentasBancariasAdm(this.usrbean);
			// JPT: CEN_MANDATOS_CUENTASBANCARIAS UNION CEN_CUENTASBANCARIAS UNION CEN_BANCOS													
			anexosList = new ArrayList<Hashtable<String,Object>>();
			Hashtable<String, Hashtable<String,Object>> auxHashtable = new Hashtable<String, Hashtable<String,Object>>();
			Hashtable<String, Object> mandato = null;			
			RowsContainer rc = new RowsContainer(); 
			if (rc.find(getSqlObtenerAnexo(beanAnexo,isFirmado)) && rc.size()>0) {
				for (int i = 0; i < rc.size(); i++) {
					Row fila = (Row) rc.get(i);
					CenMandatosCuentasBancariasBean beanMandato = new CenMandatosCuentasBancariasBean();
					beanMandato.setIdInstitucion(beanAnexo.getIdInstitucion());
					beanMandato.setIdPersona(beanAnexo.getIdPersona());
					beanMandato.setIdCuenta(fila.getString("IDCUENTA"));
					String idMandato = fila.getString("IDMANDATO");
					beanMandato.setIdMandato(idMandato);
					if (!auxHashtable.containsKey(idMandato)) {
						mandato =  cenMandatosCuentasBancariasAdm.getMandato(beanMandato,isFirmado);
					} else {
						mandato = auxHashtable.get(idMandato);
					}
						
					auxHashtable.put(idMandato, mandato);
					
					Hashtable<String,Object> anexo = new Hashtable<String,Object>();
					anexo.putAll(mandato);
					anexo.putAll(fila.getRow());
					
					anexosList.add(anexo);
				}
            }		
			
			return anexosList;
			
		} catch(Exception e) {			
			throw new ClsExceptions (e, "Error al ejecutar getAnexos");
		}
	}     	
	
	/**
	 * Actualizo los campos que se pueden modificar en CEN_ANEXOS_CUENTASBANCARIAS
	 * @param beanAnexo
	 * @return
	 * @throws ClsExceptions
	 */
	public boolean modificarFirmaAnexo(CenAnexosCuentasBancariasBean beanAnexo) throws ClsExceptions {
		try {
			CenAnexosCuentasBancariasBean beanAnexoOld = (CenAnexosCuentasBancariasBean) (selectByPK(beanToHashTable(beanAnexo))).get(0);		
			if (beanAnexo.getFirmaFecha()!=null && !beanAnexo.getFirmaFecha().equals("")) {
				beanAnexo.setFirmaFecha(GstDate.getApplicationFormatDate(this.usrbean.getLanguage(), beanAnexo.getFirmaFecha()));
			}
			
			return this.update(beanAnexo,beanAnexoOld);
							
		} catch(Exception e) {			
			throw new ClsExceptions (e, "Error al modificarFirmaAnexo");
		}		
	}		
	
	/**
	 * Insertar un nuevo anexos en CEN_ANEXOS_CUENTASBANCARIAS
	 * @param beanAnexo
	 * @return
	 * @throws ClsExceptions
	 */
	public void insertarFirmaAnexo(CenAnexosCuentasBancariasBean beanAnexo) throws ClsExceptions {
		try {
			// Calculo nuevo identificador de anexo
			Integer idAnexo = this.getNuevoID(beanAnexo);
			
			// Calculo la fecha de la firma
			if (beanAnexo.getFirmaFecha()!=null && !beanAnexo.getFirmaFecha().equals("")) {
				beanAnexo.setFirmaFecha(GstDate.getApplicationFormatDate(this.usrbean.getLanguage(), beanAnexo.getFirmaFecha()));
			}
			
			// Guardo el identificador del anexo en el bean
			beanAnexo.setIdAnexo(idAnexo.toString());
			beanAnexo.setUsuCreacion(this.usrbean.getUserName());
			beanAnexo.setFechaCreacion("sysdate");
			
			this.insert(beanAnexo);
						
		} catch(Exception e) {			
			throw new ClsExceptions (e, "Error al insertarFirmaAnexo");
		}		
	}		
	
	/**
	 * Obtiene un nuevo ID del curriculum de una persona e institucion determinada
	 * @author daniel.campos 12-01-05
	 * @version 1	 
	 * @param Bean datos de la cuenta.
	 * @return nuevo ID.
	 */
	private Integer getNuevoID (CenAnexosCuentasBancariasBean beanAnexo) throws ClsExceptions {
		Integer idAnexo = null;
		try {				
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT NVL(MAX(");
			sql.append(CenAnexosCuentasBancariasBean.C_IDANEXO);
			sql.append("),0) + 1 AS ");
			sql.append(CenAnexosCuentasBancariasBean.C_IDANEXO); 
			sql.append(" FROM ");
			sql.append(CenAnexosCuentasBancariasBean.T_NOMBRETABLA);
			sql.append(" WHERE ");
			sql.append(CenAnexosCuentasBancariasBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CenAnexosCuentasBancariasBean.C_IDINSTITUCION);
			sql.append(" = ");
			sql.append(beanAnexo.getIdInstitucion());
			sql.append(" AND ");
			sql.append(CenAnexosCuentasBancariasBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CenAnexosCuentasBancariasBean.C_IDPERSONA);
			sql.append(" = ");
			sql.append(beanAnexo.getIdPersona());
			sql.append(" AND ");
			sql.append(CenAnexosCuentasBancariasBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CenAnexosCuentasBancariasBean.C_IDCUENTA);
			sql.append(" = ");
			sql.append(beanAnexo.getIdCuenta());
			sql.append(" AND ");
			sql.append(CenAnexosCuentasBancariasBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CenAnexosCuentasBancariasBean.C_IDMANDATO);
			sql.append(" = ");
			sql.append(beanAnexo.getIdMandato());

			RowsContainer rc = new RowsContainer(); 
			if (rc.find(sql.toString()) && rc.size()>0) {
				Row fila = (Row) rc.get(0);
				idAnexo = UtilidadesHash.getInteger(fila.getRow(), CenAnexosCuentasBancariasBean.C_IDANEXO);
			}
			
		} catch (Exception e) {		
			throw new ClsExceptions (e, "Error al ejecutar getNuevoID");		
		}
		
		return idAnexo;
	}
	
	/**
	 * Elimino un anexos de CEN_ANEXOS_CUENTASBANCARIAS
	 * @param beanAnexo
	 * @return
	 * @throws ClsExceptions
	 */
	public boolean borrarFirmaAnexo(CenAnexosCuentasBancariasBean beanAnexo) throws ClsExceptions {
		try {			
			return (this.delete(beanAnexo));
							
		} catch(Exception e) {			
			throw new ClsExceptions (e, "Error al ejecutar borrarFirmaAnexo");
		}		
	}		
	
	/**
	 * 
	 * @param anexoMandatosCuentasBancariasForm
	 * @return
	 * @throws ClsExceptions
	 */
	public boolean asociarFichero(AnexosCuentasBancariasForm anexoMandatosCuentasBancariasForm) throws ClsExceptions {
		try {
			// Calculo la fecha de la firma
			StringBuffer sql = new StringBuffer();
			sql.append("UPDATE ");
			sql.append(CenAnexosCuentasBancariasBean.T_NOMBRETABLA);
			
			if (anexoMandatosCuentasBancariasForm.getIdFichero()!=null && !anexoMandatosCuentasBancariasForm.getIdFichero().equals("")) {
				sql.append(" SET IDFICHEROFIRMA = ");
				sql.append(anexoMandatosCuentasBancariasForm.getIdFichero());
			} else {
				sql.append(" SET IDFICHEROFIRMA = NULL ");
			}
						
			sql.append(" WHERE ");
			sql.append(CenAnexosCuentasBancariasBean.C_IDINSTITUCION);
			sql.append(" = ");
			sql.append(anexoMandatosCuentasBancariasForm.getIdInstitucion());
			sql.append(" AND ");
			sql.append(CenAnexosCuentasBancariasBean.C_IDPERSONA);
			sql.append(" = ");
			sql.append(anexoMandatosCuentasBancariasForm.getIdPersona()); 
			sql.append(" AND ");
			sql.append(CenAnexosCuentasBancariasBean.C_IDCUENTA);
			sql.append(" = ");
			sql.append(anexoMandatosCuentasBancariasForm.getIdCuenta()); 
			sql.append(" AND ");
			sql.append(CenAnexosCuentasBancariasBean.C_IDMANDATO);
			sql.append(" = ");
			sql.append(anexoMandatosCuentasBancariasForm.getIdMandato());
			sql.append(" AND ");
			sql.append(CenAnexosCuentasBancariasBean.C_IDANEXO);
			sql.append(" = ");
			sql.append(anexoMandatosCuentasBancariasForm.getIdAnexo());
			
			Row row = new Row();									
			return (row.updateSQL(sql.toString())>0);
							
		} catch(Exception e) {			
			throw new ClsExceptions (e, "Error al ejecutar asociarFichero");
		}		
	}	
}
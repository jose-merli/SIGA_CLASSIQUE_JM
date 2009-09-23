/*
 * VERSIONES:
 * julio.vicente - 11-03-2005 - Creación
 */

package com.siga.beans;

import java.util.Hashtable;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.SIGAException;


public class FacAbonoIncluidoEnDisqueteAdm extends MasterBeanAdministrador {
	
	public FacAbonoIncluidoEnDisqueteAdm (UsrBean usu) {
		super (FacAbonoIncluidoEnDisqueteBean.T_NOMBRETABLA, usu);
	}
	
	protected String[] getCamposBean() {
		String [] campos = {FacAbonoIncluidoEnDisqueteBean.C_IDINSTITUCION, 		
						    FacAbonoIncluidoEnDisqueteBean.C_CONTABILIZADO,							
							FacAbonoIncluidoEnDisqueteBean.C_IDABONO,
							FacAbonoIncluidoEnDisqueteBean.C_IDDISQUETEABONO,
							FacAbonoIncluidoEnDisqueteBean.C_IMPORTEABONADO,							
							FacAbonoIncluidoEnDisqueteBean.C_USUMODIFICACION,
							FacAbonoIncluidoEnDisqueteBean.C_FECHAMODIFICACION};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {FacAbonoIncluidoEnDisqueteBean.C_IDINSTITUCION, FacAbonoIncluidoEnDisqueteBean.C_IDABONO, FacAbonoIncluidoEnDisqueteBean.C_IDDISQUETEABONO};
		return claves;
	}
	
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		FacAbonoIncluidoEnDisqueteBean bean = null;
		
		try {
			bean = new FacAbonoIncluidoEnDisqueteBean();
			bean.setIdInstitucion				(UtilidadesHash.getInteger(hash, FacAbonoIncluidoEnDisqueteBean.C_IDINSTITUCION));
			bean.setContabilizado				(UtilidadesHash.getLong(hash, FacAbonoIncluidoEnDisqueteBean.C_CONTABILIZADO));
			bean.setFecha						(UtilidadesHash.getString(hash, FacAbonoIncluidoEnDisqueteBean.C_FECHA));
			bean.setIdDisqueteAbono				(UtilidadesHash.getLong(hash, FacAbonoIncluidoEnDisqueteBean.C_IDDISQUETEABONO));
			bean.setIdAbono						(UtilidadesHash.getLong(hash, FacAbonoIncluidoEnDisqueteBean.C_IDABONO));
			bean.setImporteAbonado				(UtilidadesHash.getDouble(hash, FacAbonoIncluidoEnDisqueteBean.C_IMPORTEABONADO));			
			bean.setFechaMod					(UtilidadesHash.getString(hash, FacAbonoIncluidoEnDisqueteBean.C_FECHAMODIFICACION));
			bean.setUsuMod						(UtilidadesHash.getInteger(hash, FacAbonoIncluidoEnDisqueteBean.C_USUMODIFICACION));
		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		
		return bean;
	}

	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			FacAbonoIncluidoEnDisqueteBean b = (FacAbonoIncluidoEnDisqueteBean) bean;
			UtilidadesHash.set(htData, FacAbonoIncluidoEnDisqueteBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, FacAbonoIncluidoEnDisqueteBean.C_CONTABILIZADO, b.getContabilizado());
			UtilidadesHash.set(htData, FacAbonoIncluidoEnDisqueteBean.C_FECHA, b.getFecha());
			UtilidadesHash.set(htData, FacAbonoIncluidoEnDisqueteBean.C_IDDISQUETEABONO, b.getIdDisqueteAbono());
			UtilidadesHash.set(htData, FacAbonoIncluidoEnDisqueteBean.C_IDABONO, b.getIdAbono());
			UtilidadesHash.set(htData, FacAbonoIncluidoEnDisqueteBean.C_IMPORTEABONADO, b.getImporteAbonado());
			UtilidadesHash.set(htData, FacAbonoIncluidoEnDisqueteBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, FacAbonoIncluidoEnDisqueteBean.C_USUMODIFICACION, b.getUsuMod());
			
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {
		return this.getClavesBean();
	}	
	
	/** 
	 * Obtiene el importe asociado al abono pasado como parametro de un disquete
	 * @param  institucion - identifiacdor de la institucion 
	 * @param  abono - identifiacdor del abono
	 * @return  Double - importe abonado  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	public Double getImporteAbonado(String institucion, String abono) throws ClsExceptions, SIGAException{
		Double resultado=null;
		
		try { 
            RowsContainer rc = new RowsContainer();
            Hashtable codigos = new Hashtable();
            codigos.put(new Integer(1),institucion);
            codigos.put(new Integer(2),abono);


			String sql;
			sql ="SELECT IMPPENDIENTEPORABONAR AS IMPORTE FROM FAC_ABONO WHERE IDINSTITUCION=:1 AND IDABONO=:2";
//			sql ="SELECT PKG_SIGA_TOTALESABONO.PENDIENTEPORABONAR(:1,:2)AS IMPORTE FROM DUAL";
		
			if (rc.findForUpdateBind(sql,codigos)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();			
				if (prueba.get("IMPORTE").equals("")) {
					resultado=new Double(0);
				}
				else 
					resultado=UtilidadesHash.getDouble(prueba, "IMPORTE");;								
			}
		}	
//		catch (SIGAException e) {
//			throw e;
//		}

		catch (ClsExceptions e) {		
			throw new ClsExceptions (e, "Error al ejecutar getImporteAbonado en B.D.");		
		}
		return resultado;
	}
	
}

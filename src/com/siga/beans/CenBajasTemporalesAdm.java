/*
 * Created on Mar 15, 2005
 * @author jtacosta
*/
package com.siga.beans;
 
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.gratuita.util.calendarioSJCS.LetradoGuardia;

public class CenBajasTemporalesAdm extends MasterBeanAdministrador {


	public CenBajasTemporalesAdm(UsrBean usuario)
	{
	    super(CenBajasTemporalesBean.T_NOMBRETABLA, usuario);
	}

	
	
    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
     */
    protected String[] getCamposBean() {
        String[] campos = {
                CenBajasTemporalesBean.C_IDINSTITUCION,
                CenBajasTemporalesBean.C_IDPERSONA,
                CenBajasTemporalesBean.C_FECHABT,
                CenBajasTemporalesBean.C_TIPO,
                CenBajasTemporalesBean.C_FECHADESDE,
                CenBajasTemporalesBean.C_FECHAHASTA,
                CenBajasTemporalesBean.C_FECHAALTA,
                CenBajasTemporalesBean.C_DESCRIPCION,
            	CenBajasTemporalesBean.C_FECHAMODIFICACION,
            	CenBajasTemporalesBean.C_USUMODIFICACION
				};
        
		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
     */
    protected String[] getClavesBean() {

        String[] claves = {
        		CenBajasTemporalesBean.C_IDINSTITUCION,
                CenBajasTemporalesBean.C_IDPERSONA,
                CenBajasTemporalesBean.C_FECHABT};
		return claves;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
     */
    protected String[] getOrdenCampos() {

        String[] campos = {CenBajasTemporalesBean.C_FECHADESDE};
		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#hashTableToBean(java.util.Hashtable)
     */
    protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
        CenBajasTemporalesBean bean = null;

		try
		{
			bean = new CenBajasTemporalesBean();

			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, CenBajasTemporalesBean.C_IDINSTITUCION));
			bean.setIdPersona(UtilidadesHash.getLong(hash, CenBajasTemporalesBean.C_IDPERSONA));
			bean.setFechaBT(UtilidadesHash.getString(hash, CenBajasTemporalesBean.C_FECHABT));
			bean.setTipo(UtilidadesHash.getString(hash, CenBajasTemporalesBean.C_TIPO));
			bean.setFechaDesde(UtilidadesHash.getString(hash, CenBajasTemporalesBean.C_FECHADESDE));
			bean.setFechaHasta(UtilidadesHash.getString(hash, CenBajasTemporalesBean.C_FECHAHASTA));
			bean.setFechaAlta(UtilidadesHash.getString(hash, CenBajasTemporalesBean.C_FECHAALTA));
			bean.setDescripcion(UtilidadesHash.getString(hash, CenBajasTemporalesBean.C_DESCRIPCION));

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
    public Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
        Hashtable htData = null;

		try
		{
			htData = new Hashtable();

			CenBajasTemporalesBean b = (CenBajasTemporalesBean) bean;

			UtilidadesHash.set(htData, CenBajasTemporalesBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, CenBajasTemporalesBean.C_IDPERSONA, b.getIdPersona());
			UtilidadesHash.set(htData, CenBajasTemporalesBean.C_FECHABT, b.getFechaBT());
			UtilidadesHash.set(htData, CenBajasTemporalesBean.C_TIPO, b.getTipo());
			UtilidadesHash.set(htData, CenBajasTemporalesBean.C_FECHADESDE, b.getFechaDesde());
			UtilidadesHash.set(htData, CenBajasTemporalesBean.C_FECHAHASTA, b.getFechaHasta());
			UtilidadesHash.set(htData, CenBajasTemporalesBean.C_FECHAALTA, b.getFechaAlta());
			UtilidadesHash.set(htData, CenBajasTemporalesBean.C_DESCRIPCION, b.getDescripcion());
			

			

		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}

  
    /**
     * 
     * @param estado 1 ó 0. Sera uno cuando se quieran los pagos ya enviados.0 con los pagos pendienmtes de enviar
     * @param idInstitucion
     * @return
     * @throws ClsExceptions
     */

    public Map<String,CenBajasTemporalesBean> getDiasBajaTemporal(Long idColegiado, Integer idInstitucion)
			throws ClsExceptions {

    	Map<String,CenBajasTemporalesBean> mSalida = null;
		try {
			Hashtable htCodigos = new Hashtable();
			int keyContador = 0;
			StringBuffer select = new StringBuffer();
			select.append(" SELECT * FROM CEN_BAJASTEMPORALES T ");
			select.append(" WHERE T.IDINSTITUCION = :");
			
			keyContador++;
			select.append(keyContador);
			htCodigos.put(new Integer(keyContador), idInstitucion);
			select.append(" AND T.IDPERSONA = :");
			keyContador++;
			select.append(keyContador);
			htCodigos.put(new Integer(keyContador), idColegiado);

			Vector datos = this.selectGenericoBind(select.toString(), htCodigos);
	
			mSalida = new  TreeMap<String,CenBajasTemporalesBean>();
			for (int i = 0; i < datos.size(); i++) {
				Hashtable ht = (Hashtable) datos.get(i);
				mSalida.put(GstDate.getFormatedDateShort("",(String)ht.get(CenBajasTemporalesBean.C_FECHABT)),(CenBajasTemporalesBean)this.hashTableToBean(ht));
			}
				
			
		}
		catch (ClsExceptions e) {
			throw e;
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener los getDiasVacaciones: "+ e.toString());
		}
		return mSalida;
		
	}
    /**
     * eSTE METODO NOS DEVUELVE UN MAP DE LOS LETRADOS CON BAJA TEMPORAL DE LA LISTA DE LETRADOS
     * @param alLetrados
     * @return
     * @throws ClsExceptions
     */
    public int getNumLetradosConBajaTemporal(ArrayList alLetrados,ArrayList alPeriodosDiasGuardia )
	throws ClsExceptions {

    	int numLetradosConBajaTemporal = 0;		
    	try {
			Hashtable htCodigos = new Hashtable();
			int keyContador = 0;
			StringBuffer select = new StringBuffer();
			select.append(" SELECT count(*) numLetradosConBajaTemporal FROM CEN_BAJASTEMPORALES T ");
			select.append(" WHERE ");
			select.append(" (T.IDINSTITUCION,T.IDPERSONA) IN ( ");
			for (int i = 0; i < alLetrados.size(); i++) {
				LetradoGuardia letrado = (LetradoGuardia)alLetrados.get(i);
				if(i!=0)
					select.append(" ,");
	
				select.append(" (:");
				keyContador++;
				select.append(keyContador);
				htCodigos.put(new Integer(keyContador), letrado.getIdInstitucion());
				select.append(" , :");
				keyContador++;
				select.append(keyContador);
				htCodigos.put(new Integer(keyContador), letrado.getIdPersona());
				select.append(") ");
				}
			select.append(") ");
			select.append(" AND T.FECHABT IN (");
			
			for (int i = 0; i < alPeriodosDiasGuardia.size(); i++) {
				String fechaPeriodo = (String)alPeriodosDiasGuardia.get(i);
				if(i==0)
				select.append(" :");
				else
					select.append(" , :");
				keyContador++;
				select.append(keyContador);
				htCodigos.put(new Integer(keyContador), fechaPeriodo);
			    
			}
			select.append(")");
			
			Vector datos = this.selectGenericoBind(select.toString(), htCodigos);
		
			if(datos !=null && datos.size()>0) {
				Hashtable ht = (Hashtable) datos.get(0);
				numLetradosConBajaTemporal = Integer.parseInt((String)ht.get("NUMLETRADOSCONBAJATEMPORAL"));
				
			}
				
			
		}
		catch (ClsExceptions e) {
			throw e;
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener los getNumLetradosConBajaTemporal: "+ e.toString());
		}
		return numLetradosConBajaTemporal;
		
		}
    
    
    
   
   


}
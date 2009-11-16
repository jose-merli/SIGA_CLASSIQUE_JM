/*
 * Created on Dec 27, 2004
 * @author jmgrau 
*/
package com.siga.beans;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
/**
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ExpPlazoEstadoClasificacionAdm extends MasterBeanAdministrador {

	public ExpPlazoEstadoClasificacionAdm(UsrBean usuario)
	{
	    super(ExpPlazoEstadoClasificacionBean.T_NOMBRETABLA, usuario);
	}
    
    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
     */
    protected String[] getCamposBean() {
        String[] campos = {
                ExpPlazoEstadoClasificacionBean.C_IDFASE,
				ExpPlazoEstadoClasificacionBean.C_IDESTADO,
				ExpPlazoEstadoClasificacionBean.C_IDCLASIFICACION,
				ExpPlazoEstadoClasificacionBean.C_PLAZO,
				ExpPlazoEstadoClasificacionBean.C_FECHAMODIFICACION,
				ExpPlazoEstadoClasificacionBean.C_USUMODIFICACION,
				ExpPlazoEstadoClasificacionBean.C_IDINSTITUCION,
				ExpPlazoEstadoClasificacionBean.C_IDTIPOEXPEDIENTE
				};

		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
     */
    protected String[] getClavesBean() {
        
        String[] claves = {ExpPlazoEstadoClasificacionBean.C_IDINSTITUCION, ExpPlazoEstadoClasificacionBean.C_IDTIPOEXPEDIENTE, ExpPlazoEstadoClasificacionBean.C_IDFASE, ExpPlazoEstadoClasificacionBean.C_IDESTADO, ExpPlazoEstadoClasificacionBean.C_IDCLASIFICACION};
		return claves;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
     */
    protected String[] getOrdenCampos() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#hashTableToBean(java.util.Hashtable)
     */
    protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
        ExpPlazoEstadoClasificacionBean bean = null;

		try
		{
			bean = new ExpPlazoEstadoClasificacionBean();
			
			bean.setIdFase(UtilidadesHash.getInteger(hash, ExpPlazoEstadoClasificacionBean.C_IDFASE));
			bean.setIdEstado(UtilidadesHash.getInteger(hash, ExpPlazoEstadoClasificacionBean.C_IDESTADO));
			bean.setIdClasificacion(UtilidadesHash.getInteger(hash, ExpPlazoEstadoClasificacionBean.C_IDCLASIFICACION));
			bean.setPlazo(UtilidadesHash.getString(hash, ExpPlazoEstadoClasificacionBean.C_PLAZO));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, ExpPlazoEstadoClasificacionBean.C_IDINSTITUCION));
			bean.setIdTipoExpediente(UtilidadesHash.getInteger(hash, ExpPlazoEstadoClasificacionBean.C_IDTIPOEXPEDIENTE));
			
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
    protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
        Hashtable htData = null;

		try
		{
			htData = new Hashtable();

			ExpPlazoEstadoClasificacionBean b = (ExpPlazoEstadoClasificacionBean) bean;

			UtilidadesHash.set(htData, ExpPlazoEstadoClasificacionBean.C_IDFASE, b.getIdFase());
			UtilidadesHash.set(htData, ExpPlazoEstadoClasificacionBean.C_IDESTADO, b.getIdEstado());			
			UtilidadesHash.set(htData, ExpPlazoEstadoClasificacionBean.C_IDCLASIFICACION, b.getIdClasificacion());
			UtilidadesHash.set(htData, ExpPlazoEstadoClasificacionBean.C_PLAZO, b.getPlazo());
			UtilidadesHash.set(htData, ExpPlazoEstadoClasificacionBean.C_IDINSTITUCION, b.getIdInstitucion());			
			UtilidadesHash.set(htData, ExpPlazoEstadoClasificacionBean.C_IDTIPOEXPEDIENTE, b.getIdTipoExpediente());
		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}
    
    public Vector selectClasifPlazo(String where1, String where2) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		//DATOS A EXTRAER
		String C_CLASIFICACION = "C." + ExpClasificacionesBean.C_NOMBRE + " AS CLASIFICACION";
		String P_PLAZO = "P." + ExpPlazoEstadoClasificacionBean.C_PLAZO;
		
		
		//NOMBRES TABLAS PARA LA JOIN
		String T_EXP_PLAZO = ExpPlazoEstadoClasificacionBean.T_NOMBRETABLA + " P";
		String T_EXP_CLASIFICACION = ExpClasificacionesBean.T_NOMBRETABLA + " C";
		
		//NOMBRES COLUMNAS PARA LA JOIN
		
		//Tabla exp_plazo
		String P_IDINSTITUCION="P." + ExpPlazoEstadoClasificacionBean.C_IDINSTITUCION;
		String P_IDTIPOEXPEDIENTE="P." + ExpPlazoEstadoClasificacionBean.C_IDTIPOEXPEDIENTE;
		String P_IDCLASIFICACION="P." + ExpPlazoEstadoClasificacionBean.C_IDCLASIFICACION;
		
		//Tabla exp_clasificacion
		String C_IDINSTITUCION="C." + ExpClasificacionesBean.C_IDINSTITUCION;
		String C_IDTIPOEXPEDIENTE="C." + ExpClasificacionesBean.C_IDTIPOEXPEDIENTE;
		String C_IDCLASIFICACION="C." + ExpClasificacionesBean.C_IDCLASIFICACION;
				
		
		//Subselect de plazos
		String subselect = "";
		if (!where1.equals("")){
			subselect = ", NVL((SELECT ";
			subselect += P_PLAZO + " FROM " + T_EXP_PLAZO + " WHERE ";
			subselect += P_IDINSTITUCION + "=" + C_IDINSTITUCION;
			subselect += " AND " + P_IDTIPOEXPEDIENTE + "=" + C_IDTIPOEXPEDIENTE;
			subselect += " AND " + P_IDCLASIFICACION + "=" + C_IDCLASIFICACION;
			subselect += " AND " + where1 + "),'NULO') AS PLAZO" ;
		} else {
		    subselect = ", 'NULO' AS PLAZO";
		}
		
		//select completa
		String sql = "SELECT ";
	    
		sql += C_CLASIFICACION +","+C_IDCLASIFICACION + subselect + " FROM ";	
		sql += T_EXP_CLASIFICACION + " WHERE " +where2;
		sql += " ORDER BY CLASIFICACION";
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer();			
			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);										
					datos.add(fila);					
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
		}
    
    /** Funcion establecerFechaFinal (ExpExpedienteBean bean)
	 * Calcula el plazo a partir de la fecha inicial, y hace set de la fecha final en el bean.
	 * La fecha de prorroga se 'setea' a "".
	 * @param bean del expediente
	 * @return true si ha ido bien
	 * @exception ClsExceptions
	 * */
    public boolean establecerFechaFinal(ExpExpedienteBean bean) throws ClsExceptions{
    	
    	try{
    		Hashtable hash = new Hashtable();
			hash.put(ExpPlazoEstadoClasificacionBean.C_IDINSTITUCION,bean.getIdInstitucion_tipoExpediente());
			hash.put(ExpPlazoEstadoClasificacionBean.C_IDFASE,bean.getIdFase());
			hash.put(ExpPlazoEstadoClasificacionBean.C_IDESTADO,bean.getIdEstado());
			hash.put(ExpPlazoEstadoClasificacionBean.C_IDTIPOEXPEDIENTE,bean.getIdTipoExpediente());
			hash.put(ExpPlazoEstadoClasificacionBean.C_IDCLASIFICACION,bean.getIdClasificacion());
			
			Vector datosPlazo = select(hash);
			int valorPlazo =  -1;
			int tipoPlazo =  -1;
			
			if (datosPlazo !=null && datosPlazo.size()>0) {
				ExpPlazoEstadoClasificacionBean plazoBean = (ExpPlazoEstadoClasificacionBean)datosPlazo.elementAt(0);
				valorPlazo =  plazoBean.getValorPlazo();
				tipoPlazo =  plazoBean.getTipoPlazo();
			} else {
				valorPlazo = 0;
				tipoPlazo = ExpPlazoEstadoClasificacionBean.DIAS_NATURALES; 
			}
			
			String fechaInicial = bean.getFechaInicialEstado();
			SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);
			Date d=sdf.parse(fechaInicial);
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			boolean bHabiles=false;
			String fechaFinal="";
			
			switch (tipoPlazo){
				case ExpPlazoEstadoClasificacionBean.DIAS_NATURALES:				
					cal.add(Calendar.DATE,valorPlazo);					
					break;
				case ExpPlazoEstadoClasificacionBean.MESES:
					cal.add(Calendar.MONTH,valorPlazo);
					break;
				case ExpPlazoEstadoClasificacionBean.ANIOS:
					cal.add(Calendar.YEAR,valorPlazo);
					break;
				case ExpPlazoEstadoClasificacionBean.DIAS_HABILES:
					bHabiles=true;
					ScsCalendarioGuardiasAdm calAdm = new ScsCalendarioGuardiasAdm(this.usrbean);
					//String fAux=GstDate.getFormatedDateShort("",fechaInicial);
					//Convertimos la fecha de BBDD a formato DD/MM/YYYY:
					SimpleDateFormat sdf2 = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);
					Date datFormat = sdf2.parse(fechaInicial);
					sdf2.applyPattern(ClsConstants.DATE_FORMAT_SHORT_SPANISH);//"dd/MM/yyyy"
					String fAux = sdf2.format(datFormat);					
					fAux = calAdm.obtenerFechaFinLaborable(fAux,String.valueOf(valorPlazo),String.valueOf(bean.getIdInstitucion_tipoExpediente()));
					fechaFinal=GstDate.getApplicationFormatDate("",fAux);
					break;					
			}
			if (!bHabiles){
				d=cal.getTime();
				sdf.applyPattern(ClsConstants.DATE_FORMAT_JAVA);
				fechaFinal=sdf.format(d);
			}
			
			if(valorPlazo>0){
				bean.setFechaFinalEstado(fechaFinal);
				bean.setFechaProrrogaEstado("");
			}else{
				bean.setFechaFinalEstado("");
				bean.setFechaProrrogaEstado("");
			}
			return true;
			
			
    	}catch(ClsExceptions e){
    		throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D.");			
		}catch(Exception e){
			throw new ClsExceptions (e,"Elemento nulo");
		}
			
    	
    }
}

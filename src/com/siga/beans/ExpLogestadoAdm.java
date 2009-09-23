/*
 * Created on Mar 1, 2005
 * @author emilio.grau
 *
 */
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

/**
 * Administrador del Bean de ExpLogestado
 */
public class ExpLogestadoAdm extends MasterBeanAdministrador {
	
	public ExpLogestadoAdm(UsrBean usuario)
	{
	    super(ExpLogestadoBean.T_NOMBRETABLA, usuario);
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
	 */
	protected String[] getCamposBean() {
		
		String[] campos = {ExpLogestadoBean.C_IDINSTITUCION,
				ExpLogestadoBean.C_IDINSTITUCION_TIPOEXPEDIENTE,
				ExpLogestadoBean.C_IDTIPOEXPEDIENTE,
				ExpLogestadoBean.C_NUMEROEXPEDIENTE,
				ExpLogestadoBean.C_ANIOEXPEDIENTE,		
				ExpLogestadoBean.C_FECHAINICIALESTADO,
				ExpLogestadoBean.C_FECHAFINALESTADO,
				ExpLogestadoBean.C_NOMBREESTADO,
				ExpLogestadoBean.C_IDFASE,
				ExpLogestadoBean.C_IDESTADO,
				ExpLogestadoBean.C_FECHAMODIFICACION,
				ExpLogestadoBean.C_USUMODIFICACION};

			return campos;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
	 */
	protected String[] getClavesBean() {

		String[] claves = {ExpLogestadoBean.C_IDINSTITUCION, 
				ExpLogestadoBean.C_IDINSTITUCION_TIPOEXPEDIENTE, 
				ExpLogestadoBean.C_IDTIPOEXPEDIENTE, 
				ExpLogestadoBean.C_NUMEROEXPEDIENTE, 
				ExpLogestadoBean.C_ANIOEXPEDIENTE,
				ExpLogestadoBean.C_FECHAINICIALESTADO};

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
		
		ExpLogestadoBean bean = null;

		try
		{
			bean = new ExpLogestadoBean();
						
			bean.setNumeroExpediente(UtilidadesHash.getInteger(hash, ExpLogestadoBean.C_NUMEROEXPEDIENTE));
			bean.setFechaInicialEstado(UtilidadesHash.getString(hash, ExpLogestadoBean.C_FECHAINICIALESTADO));
			bean.setFechaFinalEstado(UtilidadesHash.getString(hash, ExpLogestadoBean.C_FECHAFINALESTADO));
			bean.setFechaModificacion(UtilidadesHash.getString(hash, ExpLogestadoBean.C_FECHAMODIFICACION));
			bean.setUsuModificacion(UtilidadesHash.getInteger(hash, ExpLogestadoBean.C_USUMODIFICACION));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, ExpLogestadoBean.C_IDINSTITUCION));
			bean.setAnioExpediente(UtilidadesHash.getInteger(hash, ExpLogestadoBean.C_ANIOEXPEDIENTE));
			bean.setIdTipoExpediente(UtilidadesHash.getInteger(hash, ExpLogestadoBean.C_IDTIPOEXPEDIENTE));
			bean.setIdInstitucion_tipoExpediente(UtilidadesHash.getInteger(hash, ExpLogestadoBean.C_IDINSTITUCION_TIPOEXPEDIENTE));
			bean.setNombreEstado(UtilidadesHash.getString(hash, ExpLogestadoBean.C_NOMBREESTADO));
			bean.setIdFase(UtilidadesHash.getInteger(hash, ExpLogestadoBean.C_IDFASE));
			bean.setIdEstado(UtilidadesHash.getInteger(hash, ExpLogestadoBean.C_IDESTADO));
			
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

			ExpLogestadoBean b = (ExpLogestadoBean) bean;

			UtilidadesHash.set(htData, ExpLogestadoBean.C_NUMEROEXPEDIENTE, b.getNumeroExpediente());
			UtilidadesHash.set(htData, ExpLogestadoBean.C_FECHAINICIALESTADO, b.getFechaInicialEstado());
			UtilidadesHash.set(htData, ExpLogestadoBean.C_FECHAFINALESTADO, b.getFechaFinalEstado());
			UtilidadesHash.set(htData, ExpLogestadoBean.C_FECHAMODIFICACION, b.getFechaModificacion());
			UtilidadesHash.set(htData, ExpLogestadoBean.C_USUMODIFICACION, b.getUsuModificacion());
			UtilidadesHash.set(htData, ExpLogestadoBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, ExpLogestadoBean.C_ANIOEXPEDIENTE, b.getAnioExpediente());
			UtilidadesHash.set(htData, ExpLogestadoBean.C_IDTIPOEXPEDIENTE, b.getIdTipoExpediente());
			UtilidadesHash.set(htData, ExpLogestadoBean.C_IDINSTITUCION_TIPOEXPEDIENTE, b.getIdInstitucion_tipoExpediente());
			UtilidadesHash.set(htData, ExpLogestadoBean.C_NOMBREESTADO, b.getNombreEstado());
			UtilidadesHash.set(htData, ExpLogestadoBean.C_IDFASE, b.getIdFase());
			UtilidadesHash.set(htData, ExpLogestadoBean.C_IDESTADO, b.getIdEstado());	
			
		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}
	
	/**
	 * Método para insertar/modificar registros en el log de estados
	 * Si existe un estado nuevo, se le pone fecha final al antiguo y se inserta el nuevo.
	 * Si no existe el nuevo, se inserta un nuevo registro con los datos del estado 'antiguo'.
	 * @param ExpExpedienteBean
	 * @param ExpEstadosBean antiguo
	 * @param ExpEstadosBean nuevo
	 * @return void
	 */	
	public void insertarEntrada(ExpExpedienteBean expediente, ExpEstadosBean antiguo, ExpEstadosBean nuevo) throws ClsExceptions{
		
		try{
			if (antiguo!=null){
				
				String where = " WHERE ";
				where += ExpLogestadoBean.C_IDINSTITUCION+" = "+expediente.getIdInstitucion();
				where += " AND "+ExpLogestadoBean.C_IDINSTITUCION_TIPOEXPEDIENTE+" = "+expediente.getIdInstitucion_tipoExpediente();
				where += " AND "+ExpLogestadoBean.C_IDTIPOEXPEDIENTE+" = "+expediente.getIdTipoExpediente();
				where += " AND "+ExpLogestadoBean.C_NUMEROEXPEDIENTE+" = "+expediente.getNumeroExpediente();
				where += " AND "+ExpLogestadoBean.C_ANIOEXPEDIENTE+" = "+expediente.getAnioExpediente();			
				
				Vector vLog = this.selectForUpdate(where);
				if (!vLog.isEmpty()){
					ExpLogestadoBean logBean = (ExpLogestadoBean)vLog.lastElement();
							
					// modificamos la fecha final de la entrada del log que coincida con el estado antiguo
					if (antiguo.getIdFase().equals(logBean.getIdFase()) && antiguo.getIdEstado().equals(logBean.getIdEstado()) && logBean.getFechaFinalEstado().equals("")){
						logBean.setFechaFinalEstado("sysdate");
						this.update(logBean);
					}
				}
				
				//Insertamos una nueva entrada en el log con los datos del estado nuevo
				ExpLogestadoBean newLogBean = new ExpLogestadoBean();
				newLogBean.setIdInstitucion(expediente.getIdInstitucion());
				newLogBean.setIdInstitucion_tipoExpediente(expediente.getIdInstitucion_tipoExpediente());
				newLogBean.setIdTipoExpediente(expediente.getIdTipoExpediente());
				newLogBean.setNumeroExpediente(expediente.getNumeroExpediente());
				newLogBean.setAnioExpediente(expediente.getAnioExpediente());
				newLogBean.setFechaInicialEstado("sysdate");
				newLogBean.setFechaFinalEstado(null);
				newLogBean.setNombreEstado(nuevo.getNombre());
				newLogBean.setIdFase(nuevo.getIdFase());
				newLogBean.setIdEstado(nuevo.getIdEstado());
				
				this.insert(newLogBean);
				
			}else{
				// insertamos una entrada en el log con la fechafinal=null
				ExpLogestadoBean newLogBean = new ExpLogestadoBean();
				newLogBean.setIdInstitucion(expediente.getIdInstitucion());
				newLogBean.setIdInstitucion_tipoExpediente(expediente.getIdInstitucion_tipoExpediente());
				newLogBean.setIdTipoExpediente(expediente.getIdTipoExpediente());
				newLogBean.setNumeroExpediente(expediente.getNumeroExpediente());
				newLogBean.setAnioExpediente(expediente.getAnioExpediente());
				newLogBean.setFechaInicialEstado("sysdate");
				newLogBean.setFechaFinalEstado(null);
				newLogBean.setNombreEstado(nuevo.getNombre());
				newLogBean.setIdFase(nuevo.getIdFase());
				newLogBean.setIdEstado(nuevo.getIdEstado());
				
				this.insert(newLogBean);
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'insert' en B.D."); 
		}
		
	}


}

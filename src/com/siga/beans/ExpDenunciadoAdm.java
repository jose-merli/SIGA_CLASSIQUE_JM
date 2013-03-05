/*
 * Created on Jan 24, 2005
 * @author emilio.grau
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

/**
 * Administrador del Bean de Denunciados de un expediente
 */
public class ExpDenunciadoAdm extends MasterBeanAdministrador {
	
	public ExpDenunciadoAdm(UsrBean usuario)
	{
	    super(ExpDenunciadoBean.T_NOMBRETABLA, usuario);
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
	 */
	protected String[] getCamposBean() {
		
		String[] campos = {ExpDenunciadoBean.C_IDINSTITUCION,
				ExpDenunciadoBean.C_IDINSTITUCION_TIPOEXPEDIENTE,
				ExpDenunciadoBean.C_IDTIPOEXPEDIENTE,
				ExpDenunciadoBean.C_NUMEROEXPEDIENTE,
				ExpDenunciadoBean.C_ANIOEXPEDIENTE,
				ExpDenunciadoBean.C_IDDENUNCIADO,
				ExpDenunciadoBean.C_IDPERSONA,
				ExpDenunciadoBean.C_IDDIRECCION,
				ExpDenunciadoBean.C_FECHAMODIFICACION,
				ExpDenunciadoBean.C_USUMODIFICACION,
				ExpDenunciadoBean.C_IDINSTITUCIONORIGEN};
			return campos;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
	 */
	protected String[] getClavesBean() {
		
		String[] claves = {ExpDenunciadoBean.C_IDINSTITUCION,
				ExpDenunciadoBean.C_IDINSTITUCION_TIPOEXPEDIENTE,
				ExpDenunciadoBean.C_IDTIPOEXPEDIENTE,
				ExpDenunciadoBean.C_NUMEROEXPEDIENTE,
				ExpDenunciadoBean.C_ANIOEXPEDIENTE,
				ExpDenunciadoBean.C_IDDENUNCIADO};

		return claves;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {
		return this.getClavesBean();
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#hashTableToBean(java.util.Hashtable)
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions 
	{
		ExpDenunciadoBean bean = null;
		try {
			bean = new ExpDenunciadoBean();			
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, ExpDenunciadoBean.C_IDINSTITUCION));
			bean.setIdInstitucion_TipoExpediente(UtilidadesHash.getInteger(hash, ExpDenunciadoBean.C_IDINSTITUCION_TIPOEXPEDIENTE));
			bean.setIdTipoExpediente(UtilidadesHash.getInteger(hash, ExpDenunciadoBean.C_IDTIPOEXPEDIENTE));
			bean.setNumeroExpediente(UtilidadesHash.getInteger(hash, ExpDenunciadoBean.C_NUMEROEXPEDIENTE));
			bean.setAnioExpediente(UtilidadesHash.getInteger(hash, ExpDenunciadoBean.C_ANIOEXPEDIENTE));
			bean.setIdDenunciado(UtilidadesHash.getInteger(hash, ExpDenunciadoBean.C_IDDENUNCIADO));
			bean.setIdPersona(UtilidadesHash.getLong(hash, ExpDenunciadoBean.C_IDPERSONA));
			bean.setIdDireccion(UtilidadesHash.getLong(hash, ExpDenunciadoBean.C_IDDIRECCION));
			bean.setFechaModificacion(UtilidadesHash.getString(hash, ExpDenunciadoBean.C_FECHAMODIFICACION));			
			bean.setUsuModificacion(UtilidadesHash.getInteger(hash, ExpDenunciadoBean.C_USUMODIFICACION));
			bean.setIdInstitucionOrigen(UtilidadesHash.getInteger(hash, ExpDenunciadoBean.C_IDINSTITUCIONORIGEN));
		}
		catch (Exception e)	{
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
			ExpDenunciadoBean b = (ExpDenunciadoBean) bean;
			UtilidadesHash.set(htData, ExpDenunciadoBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, ExpDenunciadoBean.C_IDINSTITUCION_TIPOEXPEDIENTE, b.getIdInstitucion_TipoExpediente());
			UtilidadesHash.set(htData, ExpDenunciadoBean.C_IDTIPOEXPEDIENTE, b.getIdTipoExpediente());
			UtilidadesHash.set(htData, ExpDenunciadoBean.C_NUMEROEXPEDIENTE, b.getNumeroExpediente());
			UtilidadesHash.set(htData, ExpDenunciadoBean.C_ANIOEXPEDIENTE, b.getAnioExpediente());
			UtilidadesHash.set(htData, ExpDenunciadoBean.C_IDDENUNCIADO, b.getIdDenunciado());
			UtilidadesHash.set(htData, ExpDenunciadoBean.C_IDPERSONA, b.getIdPersona());
			UtilidadesHash.set(htData, ExpDenunciadoBean.C_IDDIRECCION, b.getIdDireccion());
			UtilidadesHash.set(htData, ExpDenunciadoBean.C_FECHAMODIFICACION, b.getFechaModificacion());
			UtilidadesHash.set(htData, ExpDenunciadoBean.C_USUMODIFICACION, b.getUsuModificacion());
			UtilidadesHash.set(htData, ExpDenunciadoBean.C_IDINSTITUCIONORIGEN, b.getIdInstitucionOrigen());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;
	}
	
	/** Funcion getNewIdDenunciado (Hashtable hash)
	 * Genera el id de un nuevo Denunciado
	 * @param bean con la clave primaria sin el idDenunciado
	 * @return nuevo idDenunciado
	 * */
	public Integer getNewIdDenunciado(ExpDenunciadoBean bean) throws ClsExceptions 
	{		
		int nuevoIdDenunciado = 1;
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 						
			
	        String sql = "SELECT MAX("+ExpDenunciadoBean.C_IDDENUNCIADO+") AS ULTIMODENUNCIADO ";
	        
			sql += " FROM "+ExpDenunciadoBean.T_NOMBRETABLA;
		    		    		
			sql += " WHERE ";
			sql += ExpDenunciadoBean.C_IDINSTITUCION + " = " + bean.getIdInstitucion() + " AND ";
			sql += ExpDenunciadoBean.C_IDINSTITUCION_TIPOEXPEDIENTE + " = " + bean.getIdInstitucion_TipoExpediente() + " AND ";
			sql += ExpDenunciadoBean.C_IDTIPOEXPEDIENTE + " = " + bean.getIdTipoExpediente() + " AND ";
			sql += ExpDenunciadoBean.C_NUMEROEXPEDIENTE + " = " + bean.getNumeroExpediente() + " AND ";
			sql += ExpDenunciadoBean.C_ANIOEXPEDIENTE + " = " + bean.getAnioExpediente();
						
			if (rc.find(sql)) {
				Hashtable htRow=((Row)rc.get(0)).getRow();
				if(!((String)htRow.get("ULTIMODENUNCIADO")).equals("")) {
					Integer ultimoDenunciadoInt = Integer.valueOf((String)htRow.get("ULTIMODENUNCIADO"));
					int ultimoDenunciado=ultimoDenunciadoInt.intValue();
					ultimoDenunciado++;
					nuevoIdDenunciado = ultimoDenunciado;
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return new Integer (nuevoIdDenunciado);
	}
	
	/**
	 * 
	 * Devuelve la persona a la que hace referencia el id de denunciado
	 * @param idInstitucion
	 * @param idInstitucion_TipoExpediente
	 * @param idTipoExpediente
	 * @param numExpediente
	 * @param anioExpediente
	 * @param idDenunciado
	 * @return
	 * @throws ClsExceptions
	 * @author borjans
	 * @since 31/01/2013
	 */
	public CenPersonaBean getPersonaDenunciadoById(Integer idInstitucion, Integer idInstitucion_TipoExpediente, Integer idTipoExpediente, String numExpediente, Integer anioExpediente, Integer idDenunciado) throws ClsExceptions {
		CenPersonaBean personaBean = null;
		Hashtable hashDenunciado = new Hashtable();
		hashDenunciado.put(ExpDenunciadoBean.C_IDINSTITUCION,idInstitucion);
	    hashDenunciado.put(ExpDenunciadoBean.C_IDINSTITUCION_TIPOEXPEDIENTE,idInstitucion_TipoExpediente);
	    hashDenunciado.put(ExpDenunciadoBean.C_IDTIPOEXPEDIENTE,idTipoExpediente);
	    hashDenunciado.put(ExpDenunciadoBean.C_NUMEROEXPEDIENTE,numExpediente);
	    hashDenunciado.put(ExpDenunciadoBean.C_ANIOEXPEDIENTE,anioExpediente);
	    hashDenunciado.put(ExpDenunciadoBean.C_IDDENUNCIADO,idDenunciado);
	    Vector datosDenunciadoPpal = select(hashDenunciado);
	    if (datosDenunciadoPpal != null && datosDenunciadoPpal.size() > 0){
	    	CenPersonaAdm personaAdm = new CenPersonaAdm (usrbean);
	    	ExpDenunciadoBean denunciadoPpal = (ExpDenunciadoBean) datosDenunciadoPpal.get(0);
	    	Hashtable hashIdPers = new Hashtable();		
				hashIdPers.put(CenPersonaBean.C_IDPERSONA,denunciadoPpal.getIdPersona());
				Vector vPersona = personaAdm.selectByPK(hashIdPers);
				personaBean = (CenPersonaBean) vPersona.elementAt(0);
	    }
	    return personaBean;
	}
	
	public ExpDenunciadoBean getDenunciado(Integer idInstitucion, Integer idInstitucion_TipoExpediente, Integer idTipoExpediente, String numExpediente, Integer anioExpediente, Long idPersonaDenunciado) throws ClsExceptions {
		ExpDenunciadoBean denunciadoBean = null;
		Hashtable hashDenunciado = new Hashtable();
		hashDenunciado.put(ExpDenunciadoBean.C_IDINSTITUCION,idInstitucion);
	    hashDenunciado.put(ExpDenunciadoBean.C_IDINSTITUCION_TIPOEXPEDIENTE,idInstitucion_TipoExpediente);
	    hashDenunciado.put(ExpDenunciadoBean.C_IDTIPOEXPEDIENTE,idTipoExpediente);
	    hashDenunciado.put(ExpDenunciadoBean.C_NUMEROEXPEDIENTE,numExpediente);
	    hashDenunciado.put(ExpDenunciadoBean.C_ANIOEXPEDIENTE,anioExpediente);
	    hashDenunciado.put(ExpDenunciadoBean.C_IDPERSONA,idPersonaDenunciado);
	    Vector datosDenunciadoPpal = select(hashDenunciado);
	    if (datosDenunciadoPpal != null && datosDenunciadoPpal.size() > 0){
	    	denunciadoBean = (ExpDenunciadoBean) datosDenunciadoPpal.get(0);
	    	
	    }
	    return denunciadoBean;
	}
	
	public List getDenunciados(ExpExpedienteBean beanExp) throws ClsExceptions {
		try {
			Hashtable htCodigos = new Hashtable();
			int keyContador = 0;
			StringBuffer where = new StringBuffer(" WHERE ");
		
			where.append(ExpDenunciadoBean.C_IDINSTITUCION);
			keyContador++;
			htCodigos.put(new Integer(keyContador), beanExp.getIdInstitucion());
			where.append("=:");
			where.append(keyContador);
			where.append(" AND ");
			
			
			where.append(ExpDenunciadoBean.C_IDINSTITUCION_TIPOEXPEDIENTE);
			keyContador++;
			htCodigos.put(new Integer(keyContador), beanExp.getIdInstitucion_tipoExpediente());
			where.append("=:");
			where.append(keyContador);
			where.append(" AND ");
			where.append(ExpDenunciadoBean.C_IDTIPOEXPEDIENTE);
			keyContador++;
			htCodigos.put(new Integer(keyContador), beanExp.getIdTipoExpediente());
			where.append("=:");
			where.append(keyContador);
			where.append(" AND ");
			where.append(ExpDenunciadoBean.C_NUMEROEXPEDIENTE);
			keyContador++;
			htCodigos.put(new Integer(keyContador), beanExp.getNumeroExpediente());
			where.append("=:");
			where.append(keyContador);
			where.append(" AND ");
			where.append(ExpDenunciadoBean.C_ANIOEXPEDIENTE);
			keyContador++;
			htCodigos.put(new Integer(keyContador), beanExp.getAnioExpediente());
			where.append("=:");
			where.append(keyContador);
		
			return selectBind(where.toString(), htCodigos);
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error ExpDenunciadoAdm.getDenunciados.");
		}
	}
}
/*
 * --mIGRAMOS LOS DATOS DE LOS DENUNCIADOS DEL EXPEDIENTE A LA TABLA DE DENUNCIADOS
declare
cursor c_aux is select idpersona,
       IDINSTITUCION,
       IDINSTITUCION_TIPOEXPEDIENTE,
       IDTIPOEXPEDIENTE,
       NUMEROEXPEDIENTE,
       ANIOEXPEDIENTE
  from exp_expediente;

begin
 
 for rec in c_aux loop
  begin
   insert into EXP_DENUNCIADO
  (IDINSTITUCION,  IDTIPOEXPEDIENTE,  NUMEROEXPEDIENTE,  ANIOEXPEDIENTE,
   IDDENUNCIADO,  FECHAMODIFICACION,  USUMODIFICACION,
   IDINSTITUCION_TIPOEXPEDIENTE,  IDPERSONA,  IDDIRECCION)
values
  (rec.IDINSTITUCION,
   rec.IDTIPOEXPEDIENTE,
   rec.NUMEROEXPEDIENTE,
   rec.ANIOEXPEDIENTE,
   1
,
   sysdate,
   0,
   rec.IDINSTITUCION_TIPOEXPEDIENTE,
   rec.idpersona,
   null);
   UPDATE EXP_CAMPOTIPOEXPEDIENTE 
   SET VISIBLE = 'S' 
   WHERE IDCAMPO = 16 
   AND IDINSTITUCION = rec.IDINSTITUCION_TIPOEXPEDIENTE 
   AND IDTIPOEXPEDIENTE = rec.IDTIPOEXPEDIENTE;
   

 exception
  when others then
       dbms_output.put_line('Error='||sqlerrm);  
       rollback;
 end;   
 --rollback;
 commit;
 end loop;

end;  
/
 * */

/*
 * Created on Feb 11, 2005
 * @author emilio.grau
 *
 */
package com.siga.beans;

import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.expedientes.form.ExpSeguimientoForm;
import com.siga.general.SIGAException;

/**
 * Administrador del Bean de Anotaciones de un expediente
 */
public class ExpAnotacionAdm extends MasterBeanAdministrador {

	public ExpAnotacionAdm(UsrBean usuario)
	{
	    super(ExpAnotacionBean.T_NOMBRETABLA, usuario);
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
	 */
	protected String[] getCamposBean() {
		
		String[] campos = {ExpAnotacionBean.C_IDINSTITUCION,
				ExpAnotacionBean.C_IDINSTITUCION_TIPOEXPEDIENTE,
				ExpAnotacionBean.C_IDTIPOEXPEDIENTE,
				ExpAnotacionBean.C_IDTIPOANOTACION,
				ExpAnotacionBean.C_IDANOTACION,
				ExpAnotacionBean.C_FECHAANOTACION,
				ExpAnotacionBean.C_DESCRIPCION,
				ExpAnotacionBean.C_REGENTRADA,
				ExpAnotacionBean.C_REGSALIDA,
				ExpAnotacionBean.C_IDFASE,
				ExpAnotacionBean.C_IDESTADO,
				ExpAnotacionBean.C_NUMEROEXPEDIENTE,
				ExpAnotacionBean.C_ANIOEXPEDIENTE,
				ExpAnotacionBean.C_IDUSUARIO,
				ExpAnotacionBean.C_IDINSTITUCION_USUARIO,
				ExpAnotacionBean.C_AUTOMATICO,
				ExpAnotacionBean.C_FECHAMODIFICACION,
				ExpAnotacionBean.C_USUMODIFICACION};
				
			return campos;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
	 */
	protected String[] getClavesBean() {
		
		String[] claves = {ExpAnotacionBean.C_IDINSTITUCION,
				ExpAnotacionBean.C_IDINSTITUCION_TIPOEXPEDIENTE,
				ExpAnotacionBean.C_IDTIPOEXPEDIENTE,
				ExpAnotacionBean.C_NUMEROEXPEDIENTE,
				ExpAnotacionBean.C_ANIOEXPEDIENTE,
				ExpAnotacionBean.C_IDANOTACION};

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
		
		ExpAnotacionBean bean = null;

		try
		{
			bean = new ExpAnotacionBean();
						
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, ExpAnotacionBean.C_IDINSTITUCION));
			bean.setIdInstitucion_TipoExpediente(UtilidadesHash.getInteger(hash, ExpAnotacionBean.C_IDINSTITUCION_TIPOEXPEDIENTE));
			bean.setIdTipoExpediente(UtilidadesHash.getInteger(hash, ExpAnotacionBean.C_IDTIPOEXPEDIENTE));
			bean.setIdTipoAnotacion(UtilidadesHash.getInteger(hash, ExpAnotacionBean.C_IDTIPOANOTACION));
			bean.setIdAnotacion(UtilidadesHash.getInteger(hash, ExpAnotacionBean.C_IDANOTACION));
			bean.setFechaAnotacion(UtilidadesHash.getString(hash, ExpAnotacionBean.C_FECHAANOTACION));
			bean.setDescripcion(UtilidadesHash.getString(hash, ExpAnotacionBean.C_DESCRIPCION));
			bean.setRegEntrada(UtilidadesHash.getString(hash, ExpAnotacionBean.C_REGENTRADA));
			bean.setRegSalida(UtilidadesHash.getString(hash, ExpAnotacionBean.C_REGSALIDA));
			bean.setIdFase(UtilidadesHash.getInteger(hash, ExpAnotacionBean.C_IDFASE));
			bean.setIdEstado(UtilidadesHash.getInteger(hash, ExpAnotacionBean.C_IDESTADO));
			bean.setNumeroExpediente(UtilidadesHash.getInteger(hash, ExpAnotacionBean.C_NUMEROEXPEDIENTE));
			bean.setAnioExpediente(UtilidadesHash.getInteger(hash, ExpAnotacionBean.C_ANIOEXPEDIENTE));
			bean.setIdUsuario(UtilidadesHash.getInteger(hash, ExpAnotacionBean.C_IDUSUARIO));
			bean.setIdInstitucion_Usuario(UtilidadesHash.getInteger(hash, ExpAnotacionBean.C_IDINSTITUCION_USUARIO));
			bean.setAutomatico(UtilidadesHash.getString(hash, ExpAnotacionBean.C_AUTOMATICO));			
			bean.setFechaModificacion(UtilidadesHash.getString(hash, ExpAnotacionBean.C_FECHAMODIFICACION));			
			bean.setUsuModificacion(UtilidadesHash.getInteger(hash, ExpAnotacionBean.C_USUMODIFICACION));
		
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

			ExpAnotacionBean b = (ExpAnotacionBean) bean;

			UtilidadesHash.set(htData, ExpAnotacionBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, ExpAnotacionBean.C_IDINSTITUCION_TIPOEXPEDIENTE, b.getIdInstitucion_TipoExpediente());
			UtilidadesHash.set(htData, ExpAnotacionBean.C_IDTIPOEXPEDIENTE, b.getIdTipoExpediente());
			UtilidadesHash.set(htData, ExpAnotacionBean.C_IDTIPOANOTACION, b.getIdTipoAnotacion());
			UtilidadesHash.set(htData, ExpAnotacionBean.C_IDANOTACION, b.getIdAnotacion());
			UtilidadesHash.set(htData, ExpAnotacionBean.C_FECHAANOTACION, b.getFechaAnotacion());
			UtilidadesHash.set(htData, ExpAnotacionBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(htData, ExpAnotacionBean.C_REGENTRADA, b.getRegEntrada());
			UtilidadesHash.set(htData, ExpAnotacionBean.C_REGSALIDA, b.getRegSalida());
			UtilidadesHash.set(htData, ExpAnotacionBean.C_IDFASE, b.getIdFase());
			UtilidadesHash.set(htData, ExpAnotacionBean.C_IDESTADO, b.getIdEstado());
			UtilidadesHash.set(htData, ExpAnotacionBean.C_NUMEROEXPEDIENTE, b.getNumeroExpediente());
			UtilidadesHash.set(htData, ExpAnotacionBean.C_ANIOEXPEDIENTE, b.getAnioExpediente());
			UtilidadesHash.set(htData, ExpAnotacionBean.C_IDUSUARIO, b.getIdUsuario());
			UtilidadesHash.set(htData, ExpAnotacionBean.C_IDINSTITUCION_USUARIO, b.getIdInstitucion_Usuario());
			UtilidadesHash.set(htData, ExpAnotacionBean.C_AUTOMATICO, b.getAutomatico());
			UtilidadesHash.set(htData, ExpAnotacionBean.C_FECHAMODIFICACION, b.getFechaModificacion());
			UtilidadesHash.set(htData, ExpAnotacionBean.C_USUMODIFICACION, b.getUsuModificacion());
			
		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}


	
	public Vector getAnotacionesExpediente(ExpSeguimientoForm form , UsrBean user) throws ClsExceptions 
	{
		
	    	Vector datos = new Vector();
	    	try {
	    		
	    		int indiceCodigo = 0;
	    		Map codigos = new Hashtable();
	    		
	    		String [] fields = this.getCamposBean();
	    		String idInstitucion = form.getIdInstitucion();
		    	String idInstitucion_TipoExpediente = form.getIdInstitucionTipoExpediente();
		    	String idTipoExpediente = form.getIdTipoExpediente();
		    	String numExpediente = form.getNumeroExpediente();
		    	String anioExpediente = form.getAnioExpediente();
		    	
		    	Hashtable hash = new Hashtable();
				hash.put(ExpExpedienteBean.C_IDINSTITUCION,idInstitucion);
				hash.put(ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE,idInstitucion_TipoExpediente);
				hash.put(ExpExpedienteBean.C_IDTIPOEXPEDIENTE,idTipoExpediente);
				hash.put(ExpExpedienteBean.C_NUMEROEXPEDIENTE,numExpediente);
				hash.put(ExpExpedienteBean.C_ANIOEXPEDIENTE,anioExpediente);
		
		        
		        
		        
		        

		       

				//-----------
		        
		        
				//NOMBRES COLUMNAS PARA LA JOIN
				//Tabla EXP_ESTADOS
				//String E_NOMBRE="E."+ExpEstadosBean.C_NOMBRE;
				String E_IDINSTITUCION="E."+ExpEstadosBean.C_IDINSTITUCION;
				String E_IDTIPOEXPEDIENTE="E."+ExpEstadosBean.C_IDTIPOEXPEDIENTE;
				String E_IDFASE="E."+ExpEstadosBean.C_IDFASE;
				String E_IDESTADO="E."+ExpEstadosBean.C_IDESTADO;
				
				//Tabla EXP_FASES
				String F_IDINSTITUCION="F."+ExpFasesBean.C_IDINSTITUCION;
				String F_IDTIPOEXPEDIENTE="F."+ExpFasesBean.C_IDTIPOEXPEDIENTE;
				String F_IDFASE="F."+ExpFasesBean.C_IDFASE;
				
				//Tabla EXP_TIPOANOTACION
				String T_IDINSTITUCION="T."+ExpTiposAnotacionesBean.C_IDINSTITUCION;
				String T_IDTIPOEXPEDIENTE="T."+ExpTiposAnotacionesBean.C_IDTIPOEXPEDIENTE;
				String T_IDTIPOANOTACION="T."+ExpTiposAnotacionesBean.C_IDTIPOANOTACION;
				
				//Tabla ADM_USUARIOS
				String U_IDINSTITUCION="U."+AdmUsuariosBean.C_IDINSTITUCION;
				String U_IDUSUARIO="U."+AdmUsuariosBean.C_IDUSUARIO;
				
				//Tabla CEN_INSTITUCION
				String I_IDINSTITUCION="I."+CenInstitucionBean.C_IDINSTITUCION;
				
				String where = " WHERE ";		
				
				
				
				
			    //join de las tablas ANOTACION A, ESTADOS E, FASES F, TIPOANOTACION T, INSTITUCION I, USUARIOS U
		        
				where += " A."+ExpAnotacionBean.C_IDINSTITUCION_TIPOEXPEDIENTE+" = "+E_IDINSTITUCION+"(+)";
		        where += " AND A."+ExpAnotacionBean.C_IDTIPOEXPEDIENTE+" = "+E_IDTIPOEXPEDIENTE+"(+)";
		        where += " AND A."+ExpAnotacionBean.C_IDFASE+" = "+E_IDFASE+"(+)";
		        where += " AND A."+ExpAnotacionBean.C_IDESTADO+" = "+E_IDESTADO+"(+)";
		        where += " AND A."+ExpAnotacionBean.C_ANIOEXPEDIENTE+" = "+anioExpediente;
		        where += " AND A."+ExpAnotacionBean.C_NUMEROEXPEDIENTE+" = "+numExpediente;
		        
		        where += " AND A."+ExpAnotacionBean.C_IDINSTITUCION_TIPOEXPEDIENTE+" = "+F_IDINSTITUCION+"(+)";
		        where += " AND A."+ExpAnotacionBean.C_IDTIPOEXPEDIENTE+" = "+F_IDTIPOEXPEDIENTE+"(+)";
		        where += " AND A."+ExpAnotacionBean.C_IDFASE+" = "+F_IDFASE+"(+)";
		        			
		        where += " AND A."+ExpAnotacionBean.C_IDINSTITUCION_TIPOEXPEDIENTE+" = "+T_IDINSTITUCION;
		        where += " AND A."+ExpAnotacionBean.C_IDTIPOEXPEDIENTE+" = "+T_IDTIPOEXPEDIENTE;
		        where += " AND A."+ExpAnotacionBean.C_IDTIPOANOTACION+" = "+T_IDTIPOANOTACION;
		        
		        where += " AND A."+ExpAnotacionBean.C_IDINSTITUCION_USUARIO+" = "+U_IDINSTITUCION+"(+)";
		        where += " AND A."+ExpAnotacionBean.C_IDUSUARIO+" = "+U_IDUSUARIO+"(+)";
		        
		        where += " AND A."+ExpAnotacionBean.C_IDINSTITUCION_USUARIO+" = "+I_IDINSTITUCION;
	    		
		        indiceCodigo++;
    			codigos.put(new Integer(indiceCodigo),idInstitucion);
		        where += " AND A."+ExpAnotacionBean.C_IDINSTITUCION + " = :" + indiceCodigo;
		        indiceCodigo++;
    			codigos.put(new Integer(indiceCodigo),idInstitucion_TipoExpediente);
				where += " AND A."+ExpAnotacionBean.C_IDINSTITUCION_TIPOEXPEDIENTE + " =:" + indiceCodigo;
				indiceCodigo++;
    			codigos.put(new Integer(indiceCodigo),idTipoExpediente);
				where += " AND A."+ExpAnotacionBean.C_IDTIPOEXPEDIENTE + " =:" + indiceCodigo;
				
				if(form.getIdFase()!=null && !form.getIdFase().equals("")){
	    			indiceCodigo++;
	    			codigos.put(new Integer(indiceCodigo),form.getIdFase());
	    			where += " AND A."+ExpAnotacionBean.C_IDFASE+"=:"+indiceCodigo;

	    		}
	    		if(form.getIdEstado()!=null && !form.getIdEstado().equals("")){
	    			indiceCodigo++;
	    			codigos.put(new Integer(indiceCodigo),form.getIdEstado());
	    			where += " AND A."+ExpAnotacionBean.C_IDESTADO+" =:"+indiceCodigo;

	    		}
	    		if(form.getIdTipoAnotacion()!=null && !form.getIdTipoAnotacion().equals("")){
	    			indiceCodigo++;
	    			codigos.put(new Integer(indiceCodigo),form.getIdTipoAnotacion());
	    			where += " AND A."+ExpAnotacionBean.C_IDANOTACION+"=:"+indiceCodigo;

	    		}
	    		if(form.getIdUsuario()!=null && !form.getIdUsuario().equals("")){
	    			indiceCodigo++;
	    			codigos.put(new Integer(indiceCodigo),form.getIdUsuario());
	    			where += " AND A."+ExpAnotacionBean.C_IDUSUARIO+"=:"+indiceCodigo;

	    		}
	    		if((form.getFechaDesde()!=null && !form.getFechaDesde().equals(""))||(form.getFechaHasta()!=null && !form.getFechaHasta().equals(""))){
	    			String fDesde = GstDate.getApplicationFormatDate("", form.getFechaDesde());
	    			String fHasta = GstDate.getApplicationFormatDate("", form.getFechaHasta());
	    			where += " AND "+GstDate.dateBetweenDesdeAndHastaBind("A."+ExpAnotacionBean.C_FECHAANOTACION,fDesde, fHasta, indiceCodigo, (Hashtable)codigos).get(1);

	    		}
		        
		        
	    		
		        String sql = "SELECT ";
		        sql += "DECODE ("+user.getLocation()+",A."+ExpAnotacionBean.C_IDINSTITUCION_USUARIO+",DECODE(0, A.idusuario, '"+UtilidadesString.getMensajeIdioma(this.usrbean,"general.automatico")+"', U.DESCRIPCION),I."+CenInstitucionBean.C_ABREVIATURA+") AS USUARIO, ";
		        //todos los campos de la tabla exp_anotacion separados por coma
			    for(int i=0;i<fields.length; i++){
				  sql += "A."+fields[i]+", ";
				}
			    		    
			    sql += "T."+ExpTiposAnotacionesBean.C_NOMBRE+" AS TIPO, ";
			    sql += "F."+ExpFasesBean.C_NOMBRE+" AS FASE, ";
			    sql += "E."+ExpEstadosBean.C_ESAUTOMATICO+" AS AUTOMATICO, ";
			    sql += "E."+ExpEstadosBean.C_NOMBRE+" AS ESTADO ";
			    
				sql += " FROM ";
			    sql += ExpAnotacionBean.T_NOMBRETABLA+" A, "+ExpEstadosBean.T_NOMBRETABLA+" E, "+ExpFasesBean.T_NOMBRETABLA+" F, "+ExpTiposAnotacionesBean.T_NOMBRETABLA+" T, "+AdmUsuariosBean.T_NOMBRETABLA+" U, "+CenInstitucionBean.T_NOMBRETABLA+" I";
			    		    		
				sql += " " + where;
				sql += " ORDER BY A."+ExpAnotacionBean.C_FECHAANOTACION+", 1, F."+ExpFasesBean.C_NOMBRE+", E."+ExpEstadosBean.C_NOMBRE+", T."+ExpTiposAnotacionesBean.C_NOMBRE;
	    		
	    		
	    		// Acceso a BBDD
	    		RowsContainer rc = null;

	    		rc = new RowsContainer();			

	    		//ClsLogging.writeFileLog("ExpAlertaAdm -> QUERY: "+sql,3);

	    		if (rc.queryBind(sql, (Hashtable)codigos)) {
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
		
		
	
	
	
	
	/** Funcion getNewIdAnotacion (Hashtable hash)
	 * Genera el id de una nueva Anotación
	 * @param hash con la clave primaria sin el idAnotacion
	 * @return nuevo idDocumento  
	 * */
	public Integer getNewIdAnotacion(Hashtable hash) throws ClsExceptions 
	{		
		int nuevoIdAnotacion = 1;
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 						
			
	        String sql = "SELECT MAX("+ExpAnotacionBean.C_IDANOTACION+") AS ULTIMOIDANOTACION ";
	        //todos los campos de la tabla exp_expediente separados por coma
		    
		    
			sql += " FROM "+ExpAnotacionBean.T_NOMBRETABLA;
		    		    		
			sql += " WHERE ";
			sql += ExpAnotacionBean.C_IDINSTITUCION+" = "+hash.get(ExpAnotacionBean.C_IDINSTITUCION)+" AND ";
			sql += ExpAnotacionBean.C_IDINSTITUCION_TIPOEXPEDIENTE+" = "+hash.get(ExpAnotacionBean.C_IDINSTITUCION_TIPOEXPEDIENTE)+" AND ";
			sql += ExpAnotacionBean.C_IDTIPOEXPEDIENTE+" = "+hash.get(ExpAnotacionBean.C_IDTIPOEXPEDIENTE)+" AND ";
			sql += ExpAnotacionBean.C_NUMEROEXPEDIENTE+" = "+hash.get(ExpAnotacionBean.C_NUMEROEXPEDIENTE)+" AND ";
			sql += ExpAnotacionBean.C_ANIOEXPEDIENTE+" = "+hash.get(ExpAnotacionBean.C_ANIOEXPEDIENTE);
						
			if (rc.find(sql)) {
				Hashtable htRow=((Row)rc.get(0)).getRow();
				if(!((String)htRow.get("ULTIMOIDANOTACION")).equals("")) {
					Integer ultimaAnotacionInt = Integer.valueOf((String)htRow.get("ULTIMOIDANOTACION"));
					int ultimaAnotacion = ultimaAnotacionInt.intValue();
					ultimaAnotacion++;
					nuevoIdAnotacion = ultimaAnotacion;
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return new Integer (nuevoIdAnotacion);
	}
	public Integer getNewIdAnotacion(ExpExpedienteBean bean) throws ClsExceptions 
	{		
		int nuevoIdAnotacion = 1;
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 						
			
	        String sql = "SELECT MAX("+ExpAnotacionBean.C_IDANOTACION+") AS ULTIMOIDANOTACION ";
	        //todos los campos de la tabla exp_expediente separados por coma
		    
		    
			sql += " FROM "+ExpAnotacionBean.T_NOMBRETABLA;
		    		    		
			sql += " WHERE ";
			sql += ExpAnotacionBean.C_IDINSTITUCION+" = "+bean.getIdInstitucion().toString()+" AND ";
			sql += ExpAnotacionBean.C_IDINSTITUCION_TIPOEXPEDIENTE+" = "+bean.getIdInstitucion_tipoExpediente().toString()+" AND ";
			sql += ExpAnotacionBean.C_IDTIPOEXPEDIENTE+" = "+bean.getIdTipoExpediente().toString()+" AND ";
			sql += ExpAnotacionBean.C_NUMEROEXPEDIENTE+" = "+bean.getNumeroExpediente().toString()+" AND ";
			sql += ExpAnotacionBean.C_ANIOEXPEDIENTE+" = "+bean.getAnioExpediente().toString();
						
			if (rc.find(sql)) {
				Hashtable htRow=((Row)rc.get(0)).getRow();
				if(!((String)htRow.get("ULTIMOIDANOTACION")).equals("")) {
					Integer ultimaAnotacionInt = Integer.valueOf((String)htRow.get("ULTIMOIDANOTACION"));
					int ultimaAnotacion = ultimaAnotacionInt.intValue();
					ultimaAnotacion++;
					nuevoIdAnotacion = ultimaAnotacion;
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return new Integer (nuevoIdAnotacion);
	}
	public void insertarAnotacionEnvioComunicacion(String idInstitucion, 
			Integer idInstitucionTipoExp, Integer idTipoExp, Integer numeroExpediente, 
			Integer anioExpediente	,String idPersona, UsrBean usrBean)throws SIGAException,ClsExceptions{
		ExpAnotacionBean anotBean = new ExpAnotacionBean();	    
		ExpAnotacionAdm anotAdm = new ExpAnotacionAdm(usrBean);

		anotBean.setIdInstitucion(new Integer(idInstitucion));
		anotBean.setIdInstitucion_TipoExpediente(idInstitucionTipoExp);
		anotBean.setIdTipoExpediente(idTipoExp);
		anotBean.setIdTipoAnotacion(ExpTiposAnotacionesAdm.codigoTipoComunicacion);	    
		anotBean.setFechaAnotacion("sysdate");
		CenPersonaAdm persAdm = new CenPersonaAdm(usrBean);		
		StringBuffer descripcion = new StringBuffer(UtilidadesString.getMensajeIdioma(usrBean,"expedientes.tipoAnotacion.comunicacion.mensaje"));
		descripcion.append(" ");
		descripcion.append(persAdm.obtenerNombreApellidos(idPersona));
			
		anotBean.setDescripcion(descripcion.toString());
		//anotBean.setDescripcion("");

		anotBean.setNumeroExpediente(numeroExpediente);
		anotBean.setAnioExpediente(anioExpediente);
		
		// RGG Es una anotación automatica, para que se vea en la consulta de seguimiento.
		anotBean.setAutomatico("S");
		
		anotBean.setIdUsuario(new Integer(usrBean.getUserName()));
		anotBean.setIdInstitucion_Usuario(new Integer(usrBean.getLocation()));

		//Nuevo idAnotacion
		Hashtable datosAnot = new Hashtable();
		datosAnot.put(ExpAnotacionBean.C_IDINSTITUCION,idInstitucion);
		datosAnot.put(ExpAnotacionBean.C_IDINSTITUCION_TIPOEXPEDIENTE,idInstitucionTipoExp);
		datosAnot.put(ExpAnotacionBean.C_IDTIPOEXPEDIENTE,idTipoExp);
		datosAnot.put(ExpAnotacionBean.C_NUMEROEXPEDIENTE,numeroExpediente);
		datosAnot.put(ExpAnotacionBean.C_ANIOEXPEDIENTE,anioExpediente);

		anotBean.setIdAnotacion(anotAdm.getNewIdAnotacion(datosAnot));

		//Ahora procedemos a insertarlo
		anotAdm.insert(anotBean);	 

}
	
    public boolean insertarAnotacionAutomatica(ExpExpedienteBean expBean, String texto) throws ClsExceptions 
	{
		try{	
    		ExpAnotacionBean anotacionBean = new ExpAnotacionBean();
			
    		anotacionBean.setAnioExpediente(expBean.getAnioExpediente());
			anotacionBean.setIdInstitucion(expBean.getIdInstitucion());
			anotacionBean.setIdInstitucion_TipoExpediente(expBean.getIdInstitucion_tipoExpediente());
			anotacionBean.setIdTipoExpediente(expBean.getIdTipoExpediente());
			anotacionBean.setNumeroExpediente(expBean.getNumeroExpediente());
			anotacionBean.setFechaAnotacion("SYSDATE");
			anotacionBean.setIdAnotacion(this.getNewIdAnotacion(expBean));
			anotacionBean.setIdEstado(expBean.getIdEstado());
			anotacionBean.setIdFase(expBean.getIdFase());
			anotacionBean.setDescripcion(texto);
			anotacionBean.setAutomatico("S");
			anotacionBean.setIdUsuario(new Integer(this.usrbean.getUserName()));
			anotacionBean.setIdInstitucion_Usuario(expBean.getIdInstitucion());
			anotacionBean.setIdTipoAnotacion(ExpTiposAnotacionesAdm.codigoTipoAutomatico);	    
			
	        if (!this.insert(anotacionBean)){
	            throw new ClsExceptions("Error al insertar anotación: "+this.getError());
	        }
        	return true;
		} catch (ClsExceptions e) {
	        throw new ClsExceptions (e, "Error al insertar alerta en B.D.");
	    }	    
		
	}
    
	
}

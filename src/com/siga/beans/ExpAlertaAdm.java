/*
 * Created on Dec 27, 2004
 * @author jmgrau 
*/
package com.siga.beans;

import java.util.Hashtable;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.Paginador;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.envios.EnvioInformesGenericos;
import com.siga.expedientes.form.BusquedaAlertaForm;

/**
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ExpAlertaAdm extends MasterBeanAdministrador {

	public ExpAlertaAdm(UsrBean usuario)
	{
	    super(ExpAlertaBean.T_NOMBRETABLA, usuario);
	}
    
    protected String[] getCamposBean() {
        
        String[] campos = {
				ExpAlertaBean.C_IDINSTITUCION,
				ExpAlertaBean.C_IDTIPOEXPEDIENTE,
				ExpAlertaBean.C_NUMEROEXPEDIENTE,
				ExpAlertaBean.C_ANIOEXPEDIENTE,
				ExpAlertaBean.C_IDALERTA,
				ExpAlertaBean.C_TEXTO,
				ExpAlertaBean.C_FECHAALERTA,
				ExpAlertaBean.C_IDINSTITUCIONTIPOEXPEDIENTE,
				ExpAlertaBean.C_IDFASE,
				ExpAlertaBean.C_IDESTADO,
				ExpAlertaBean.C_FECHAMODIFICACION,
            	ExpAlertaBean.C_USUMODIFICACION,            	
				ExpAlertaBean.C_BORRADO           	
				};

		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
     */
    protected String[] getClavesBean() {
        
        String[] claves = {ExpAlertaBean.C_IDINSTITUCION, 
                			ExpAlertaBean.C_IDINSTITUCIONTIPOEXPEDIENTE, 
                			ExpAlertaBean.C_IDTIPOEXPEDIENTE, 
                			ExpAlertaBean.C_NUMEROEXPEDIENTE, 
                			ExpAlertaBean.C_ANIOEXPEDIENTE, 
                			ExpAlertaBean.C_IDALERTA};
		return claves;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
     */
    protected String[] getOrdenCampos() {
        return null;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#hashTableToBean(java.util.Hashtable)
     */
    protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
        ExpAlertaBean bean = null;

		try
		{
			bean = new ExpAlertaBean();
			
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, ExpAlertaBean.C_IDINSTITUCION));
			bean.setIdTipoExpediente(UtilidadesHash.getInteger(hash, ExpAlertaBean.C_IDTIPOEXPEDIENTE));
			bean.setNumeroExpediente(UtilidadesHash.getInteger(hash, ExpAlertaBean.C_NUMEROEXPEDIENTE));
			bean.setAnioExpediente(UtilidadesHash.getInteger(hash, ExpAlertaBean.C_ANIOEXPEDIENTE));
			bean.setIdAlerta(UtilidadesHash.getInteger(hash, ExpAlertaBean.C_IDALERTA));
			bean.setTexto(UtilidadesHash.getString(hash, ExpAlertaBean.C_TEXTO));
			bean.setFechaAlerta(UtilidadesHash.getString(hash, ExpAlertaBean.C_FECHAALERTA));
			bean.setIdInstitucionTipoExpediente(UtilidadesHash.getInteger(hash, ExpAlertaBean.C_IDINSTITUCIONTIPOEXPEDIENTE));
			bean.setIdFase(UtilidadesHash.getInteger(hash, ExpAlertaBean.C_IDFASE));
			bean.setIdEstado(UtilidadesHash.getInteger(hash, ExpAlertaBean.C_IDESTADO));
			bean.setBorrado(UtilidadesHash.getString(hash, ExpAlertaBean.C_BORRADO));
			
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

			ExpAlertaBean b = (ExpAlertaBean) bean;
			
			UtilidadesHash.set(htData, ExpAlertaBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, ExpAlertaBean.C_IDTIPOEXPEDIENTE, b.getIdTipoExpediente());
			UtilidadesHash.set(htData, ExpAlertaBean.C_NUMEROEXPEDIENTE, b.getNumeroExpediente());
			UtilidadesHash.set(htData, ExpAlertaBean.C_ANIOEXPEDIENTE, b.getAnioExpediente());
			UtilidadesHash.set(htData, ExpAlertaBean.C_IDALERTA, b.getIdAlerta());
			UtilidadesHash.set(htData, ExpAlertaBean.C_TEXTO, b.getTexto());
			UtilidadesHash.set(htData, ExpAlertaBean.C_FECHAALERTA, b.getFechaAlerta());
			UtilidadesHash.set(htData, ExpAlertaBean.C_IDINSTITUCIONTIPOEXPEDIENTE, b.getIdInstitucionTipoExpediente());
			UtilidadesHash.set(htData, ExpAlertaBean.C_IDFASE, b.getIdFase());
			UtilidadesHash.set(htData, ExpAlertaBean.C_IDESTADO, b.getIdEstado());
			UtilidadesHash.set(htData, ExpAlertaBean.C_BORRADO, b.getBorrado());		
			
		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}

    public Vector selectAlertas(String where) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		//DATOS A EXTRAER
		String A_FECHAALERTA = "A." + ExpAlertaBean.C_FECHAALERTA;
		String F_FASE = "F." + ExpFasesBean.C_NOMBRE + " AS FASE ";
		String E_ESTADO = "E." + ExpEstadosBean.C_NOMBRE + " AS ESTADO ";		
		String A_TEXTO = "A." + ExpAlertaBean.C_TEXTO;
		String A_IDALERTA = "A." + ExpAlertaBean.C_IDALERTA;
		
		
		//NOMBRES TABLAS PARA LA JOIN
		String T_EXP_ALERTA = ExpAlertaBean.T_NOMBRETABLA + " A";
		String T_EXP_FASES = ExpFasesBean.T_NOMBRETABLA + " F";
		String T_EXP_ESTADOS = ExpEstadosBean.T_NOMBRETABLA + " E";

			
		//select completa
		String sql = "SELECT ";
	    
		sql += A_FECHAALERTA +","+F_FASE +","+ E_ESTADO +","+ A_TEXTO +","+ A_IDALERTA + " FROM ";	
		sql += T_EXP_ALERTA + "," + T_EXP_FASES +","+ T_EXP_ESTADOS + " " +where;
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer();			
			
			ClsLogging.writeFileLog("ExpAlertaAdm -> QUERY: "+sql,3);
			
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
    
   
    public Vector getAlertas(BusquedaAlertaForm form,UsrBean userBean) throws ClsExceptions 
    {
    	Vector datos = new Vector();
    	try {

    		int indiceCodigo = 0;
    		Map codigos = new Hashtable();


    		String A_IDINSTITUCION="A." + ExpAlertaBean.C_IDINSTITUCION;
    		String A_IDINSTITUCIONTIPOEXPEDIENTE="A." + ExpAlertaBean.C_IDINSTITUCIONTIPOEXPEDIENTE;
    		String A_IDTIPOEXPEDIENTE="A." + ExpAlertaBean.C_IDTIPOEXPEDIENTE;
    		String A_IDFASE="A." + ExpAlertaBean.C_IDFASE;
    		String A_IDESTADO="A." + ExpAlertaBean.C_IDESTADO;
    		String A_NUMEROEXPEDIENTE="A." + ExpAlertaBean.C_NUMEROEXPEDIENTE;
    		String A_ANIOEXPEDIENTE="A." + ExpAlertaBean.C_ANIOEXPEDIENTE;
    		String A_BORRADO="A." + ExpAlertaBean.C_BORRADO;

    		//Tabla exp_tipoexpediente
    		String T_IDINSTITUCION="T." + ExpTipoExpedienteBean.C_IDINSTITUCION;
    		String T_IDTIPOEXPEDIENTE="T." + ExpTipoExpedienteBean.C_IDTIPOEXPEDIENTE;

    		//LMS 09/08/2006
    		//Se añade la relación con la tabla de permisos de expedientes.
    		//Tabla EXP_PERMISOSTIPOSEXPEDIENTES
    		//String E_IDINSTITUCION="X."+ExpPermisosTiposExpedientesBean.C_IDINSTITUCION;
    		String E_IDPERFIL="X."+ExpPermisosTiposExpedientesBean.C_IDPERFIL;
    		String E_IDINSTITUCIONTIPOEXPEDIENTE="X."+ExpPermisosTiposExpedientesBean.C_IDINSTITUCIONTIPOEXPEDIENTE;
    		String E_IDTIPOEXPEDIENTE="X."+ExpPermisosTiposExpedientesBean.C_IDTIPOEXPEDIENTE;

    		String A_FECHAALERTA = "A." + ExpAlertaBean.C_FECHAALERTA;
    		String T_TIPOEXPEDIENTE = "T." + ExpTipoExpedienteBean.C_NOMBRE + " AS NOMBRETIPOEXPEDIENTE ";
    		String A_TEXTO = "A." + ExpAlertaBean.C_TEXTO;
    		String A_IDALERTA = "A." + ExpAlertaBean.C_IDALERTA;


    		//NOMBRES TABLAS PARA LA JOIN
    		String T_EXP_ALERTA = ExpAlertaBean.T_NOMBRETABLA + " A";
    		String T_EXP_TIPOEXPEDIENTE = ExpTipoExpedienteBean.T_NOMBRETABLA + " T";

    		//LMS 09/08/2006
    		//Se añade el control de permisos sobre el tipo de expediente.
    		String T_EXP_PERMISOSTIPOSEXPEDIENTES = ExpPermisosTiposExpedientesBean.T_NOMBRETABLA + " X";


    		//Valores recogidos del formulario para la búsqueda
    		String comboTipoExp = form.getComboTipoExpediente();

    		//getComboTipoExpediente nos está devolviendo (idinstitucion,idtipoexpediente)
    		String idinstitucion_tipoexpediente = "";
    		if (comboTipoExp!=null && !comboTipoExp.equals("")){
    			StringTokenizer st = new StringTokenizer(comboTipoExp,",");
    			idinstitucion_tipoexpediente=st.nextToken();
    			form.setTipoExpediente(st.nextToken());        	
    		}else{
    			idinstitucion_tipoexpediente="";
    			form.setTipoExpediente("");  
    		}

    		String tipoExpediente = form.getTipoExpediente();
    		String numeroExpediente = form.getNumeroExpediente();
    		String anioExpediente = form.getAnioExpediente();

    		String where = " WHERE ";

    		//join de las tablas TIPOEXPEDIENTE T, ALERTA A
    		indiceCodigo++;
    		codigos.put(new Integer(indiceCodigo),form.getIdInstitucion());

    		where += A_IDINSTITUCION + " = :" + indiceCodigo;

    		where += " AND " + A_IDTIPOEXPEDIENTE + " = " + T_IDTIPOEXPEDIENTE;
    		where += " AND " + A_IDINSTITUCIONTIPOEXPEDIENTE + " = " + T_IDINSTITUCION;
    		where += " AND A.IDINSTITUCION_TIPOEXPEDIENTE =  EST.IDINSTITUCION ";
    		where += " AND A.IDFASE=EST.IDFASE AND A.IDESTADO=EST.IDESTADO ";
    		where += " AND A.IDTIPOEXPEDIENTE=EST.IDTIPOEXPEDIENTE ";
    		where += " AND EST.IDINSTITUCION = FAS.IDINSTITUCION  "; 
    		where += " AND EST.IDTIPOEXPEDIENTE = FAS.IDTIPOEXPEDIENTE ";
    		where += " AND EST.IDFASE = FAS.IDFASE ";

    		//campos de búsqueda

    		if(tipoExpediente!=null && !tipoExpediente.equals("")){
    			indiceCodigo++;
    			codigos.put(new Integer(indiceCodigo),tipoExpediente);
    			where += " AND " + A_IDTIPOEXPEDIENTE + " = :" + indiceCodigo ;
    		}
    		if(numeroExpediente!=null && !numeroExpediente.equals("")){
    			indiceCodigo++;
    			codigos.put(new Integer(indiceCodigo),numeroExpediente);
    			where += " AND "+ComodinBusquedas.prepararSentenciaCompletaBind(":"+indiceCodigo,A_NUMEROEXPEDIENTE ) ;
    		}

    		if(anioExpediente!=null && !anioExpediente.equals("")){
    			indiceCodigo++;
    			codigos.put(new Integer(indiceCodigo),anioExpediente);
    			where += " AND "+ComodinBusquedas.prepararSentenciaCompletaBind(":"+indiceCodigo,A_ANIOEXPEDIENTE ) ;
    		}
    		//registros no borrados lógicamente (campo Borrado='N')
    		indiceCodigo++;
    		codigos.put(new Integer(indiceCodigo),"N");
    		where += " AND " + A_BORRADO + " = :"+indiceCodigo;

    		//LMS 09/08/2006
    		//Se añade el control de permisos sobre el tipo de expediente.
    		where += " AND " + E_IDINSTITUCIONTIPOEXPEDIENTE + " = " + A_IDINSTITUCION;
    		where += " AND " + E_IDTIPOEXPEDIENTE + " = " + A_IDTIPOEXPEDIENTE;
    		where += " AND " + E_IDPERFIL + " IN (";

    		String[] aPerfiles = userBean.getProfile();

    		for (int i=0; i<aPerfiles.length; i++, where+=",")
    		{
    			indiceCodigo++;
    			codigos.put(new Integer(indiceCodigo),aPerfiles[i] );
    			where += ":" +indiceCodigo  ;
    		}

    		where = where.substring(0,where.length()-1);
    		where += ")";
    		if(form.getIdFase()!=null && !form.getIdFase().equals("")){
    			indiceCodigo++;
    			codigos.put(new Integer(indiceCodigo),form.getIdFase());
    			where += " AND "+A_IDFASE+"=:"+indiceCodigo;

    		}
    		if(form.getIdEstado()!=null && !form.getIdEstado().equals("")){
    			indiceCodigo++;
    			codigos.put(new Integer(indiceCodigo),form.getIdEstado());
    			where += " AND "+A_IDESTADO+"=:"+indiceCodigo;

    		}

    		if((form.getFechaDesde()!=null && !form.getFechaDesde().equals(""))||(form.getFechaHasta()!=null && !form.getFechaHasta().equals(""))){
    			
    			String fDesde = GstDate.getApplicationFormatDate("", form.getFechaDesde());
    			String fHasta = GstDate.getApplicationFormatDate("", form.getFechaHasta());
    			where += " AND "+GstDate.dateBetweenDesdeAndHastaBind(A_FECHAALERTA, fDesde, fHasta, indiceCodigo, (Hashtable)codigos).get(1);

    		}

//  		if(form.getFechaHasta()!=null && !form.getFechaHasta().equals("")){
//  		where += " AND "+A_FECHAALERTA+">="+GstDate.getApplicationFormatDate("",form.getFechaHasta());

//  		}




    		//ÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑ

    		//DATOS A EXTRAER



    		//select completa
    		String sql = "SELECT ";

    		sql += A_FECHAALERTA +"," + T_TIPOEXPEDIENTE +","+ A_NUMEROEXPEDIENTE +","+
    		A_ANIOEXPEDIENTE +","+ A_TEXTO +","+ A_IDALERTA +","+ A_IDINSTITUCION +","+ 
    		A_IDINSTITUCIONTIPOEXPEDIENTE +","+ A_IDTIPOEXPEDIENTE+
    			",EST.NOMBRE EST_NOMBRE,    FAS.NOMBRE FAS_NOMBRE "
    		
    		
    		+ " FROM ";	
    		sql += T_EXP_ALERTA + "," + T_EXP_TIPOEXPEDIENTE + "," + T_EXP_PERMISOSTIPOSEXPEDIENTES;
    		sql += " ,EXP_ESTADO EST, EXP_FASES FAS ";
    		sql += " " + where;

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

    public boolean insertarAlerta(ExpExpedienteBean expBean, String texto) throws ClsExceptions 
	{
		try{	
    		ExpAlertaBean alertaBean = new ExpAlertaBean();
			
			alertaBean.setAnioExpediente(expBean.getAnioExpediente());
			alertaBean.setBorrado("N");
			alertaBean.setFechaAlerta("SYSDATE");
			alertaBean.setIdAlerta(this.getNewIdAlerta(expBean.getIdInstitucion().toString(),
			        								   expBean.getIdInstitucion_tipoExpediente().toString(),
			        								   expBean.getIdTipoExpediente().toString(),
			        								   expBean.getNumeroExpediente().toString(),
			        								   expBean.getAnioExpediente().toString()));
			alertaBean.setIdEstado(expBean.getIdEstado());
			alertaBean.setIdFase(expBean.getIdFase());
			alertaBean.setIdInstitucion(expBean.getIdInstitucion());
			alertaBean.setIdInstitucionTipoExpediente(expBean.getIdInstitucion_tipoExpediente());
			alertaBean.setIdTipoExpediente(expBean.getIdTipoExpediente());
			alertaBean.setNumeroExpediente(expBean.getNumeroExpediente());
			alertaBean.setTexto(texto);
			Hashtable tipoExpHashtable = new Hashtable();
			tipoExpHashtable.put(ExpTipoExpedienteBean.C_IDINSTITUCION,alertaBean.getIdInstitucionTipoExpediente());
			tipoExpHashtable.put(ExpTipoExpedienteBean.C_IDTIPOEXPEDIENTE,alertaBean.getIdTipoExpediente());
			ExpTipoExpedienteAdm tipoExpedienteAdm = new ExpTipoExpedienteAdm(this.usrbean);
			Vector tipoExpedienteVector=tipoExpedienteAdm.selectByPK(tipoExpHashtable);
			ExpTipoExpedienteBean tipoExpedienteBean =  (ExpTipoExpedienteBean) tipoExpedienteVector.get(0);
			
	        if (this.insert(alertaBean)){
	        	if(tipoExpedienteBean.getEnviarAvisos()!=null && tipoExpedienteBean.getEnviarAvisos().intValue()==Integer.parseInt(ClsConstants.DB_TRUE)){
	        		EnvioInformesGenericos envio = new EnvioInformesGenericos();
	        		envio.enviarAvisoAlerta(tipoExpedienteBean,alertaBean, this.usrbean);
	        	}
	        	return true;
	        }else{
	        	return false;
	        }
		} catch (ClsExceptions  e) {
	        throw new ClsExceptions (e, "Error al insertar alerta en B.D.");
	    } catch (Exception e) {
	    	throw new ClsExceptions (e, "Error al insertar alerta en B.D.");
		}	    
		
	}
    

    
    
    public Integer getNewIdAlerta(String _idinstitucion, 
            					  String _idinstituciontipoexpediente, 
            					  String _idtipoexpediente, 
            					  String _numeroexpediente, 
            					  String _anioexpediente) throws ClsExceptions{
        RowsContainer rows=new RowsContainer();
        String sql="SELECT MAX(" + ExpAlertaBean.C_IDALERTA + 
			") AS MAXVALOR FROM " + ExpAlertaBean.T_NOMBRETABLA + 
			" WHERE " + ExpAlertaBean.C_IDTIPOEXPEDIENTE + "=" +_idtipoexpediente +
			" AND " + ExpAlertaBean.C_IDINSTITUCION + "="+ _idinstitucion +
			" AND " + ExpAlertaBean.C_IDINSTITUCIONTIPOEXPEDIENTE + "="+ _idinstituciontipoexpediente +
			" AND " + ExpAlertaBean.C_ANIOEXPEDIENTE + "="+ _anioexpediente +
			" AND " + ExpAlertaBean.C_NUMEROEXPEDIENTE + "="+ _numeroexpediente;
		int valor=1; // Si no hay registros, es el valor que tomará
        if(rows.find(sql)){
            Hashtable htRow=((Row)rows.get(0)).getRow();
            // El valor devuelto será "" Si no hay registros
            if(!((String)htRow.get("MAXVALOR")).equals("")) {
                Integer valorInt=Integer.valueOf((String)htRow.get("MAXVALOR"));
                valor=valorInt.intValue();
                valor++;
            }            
        }
        return new Integer(valor);        
    }
    
    /**
     * Consulta que devuelve las alertas para paginar
     * */
    public Paginador getPaginadorAlertas(BusquedaAlertaForm form,UsrBean userBean) throws ClsExceptions 
    {
		//int indiceCodigo = 0;
		//Map codigos = new Hashtable();
	
		String A_IDINSTITUCION="A." + ExpAlertaBean.C_IDINSTITUCION;
		String A_IDINSTITUCIONTIPOEXPEDIENTE="A." + ExpAlertaBean.C_IDINSTITUCIONTIPOEXPEDIENTE;
		String A_IDTIPOEXPEDIENTE="A." + ExpAlertaBean.C_IDTIPOEXPEDIENTE;
		String A_IDFASE="A." + ExpAlertaBean.C_IDFASE;
		String A_IDESTADO="A." + ExpAlertaBean.C_IDESTADO;
		String A_NUMEROEXPEDIENTE="A." + ExpAlertaBean.C_NUMEROEXPEDIENTE;
		String A_ANIOEXPEDIENTE="A." + ExpAlertaBean.C_ANIOEXPEDIENTE;
		String A_BORRADO="A." + ExpAlertaBean.C_BORRADO;
	
		//Tabla exp_tipoexpediente
		String T_IDINSTITUCION="T." + ExpTipoExpedienteBean.C_IDINSTITUCION;
		String T_IDTIPOEXPEDIENTE="T." + ExpTipoExpedienteBean.C_IDTIPOEXPEDIENTE;
	
		//LMS 09/08/2006
		//Se añade la relación con la tabla de permisos de expedientes.
		//Tabla EXP_PERMISOSTIPOSEXPEDIENTES
		//String E_IDINSTITUCION="X."+ExpPermisosTiposExpedientesBean.C_IDINSTITUCION;
		String E_IDPERFIL="X."+ExpPermisosTiposExpedientesBean.C_IDPERFIL;
		String E_IDINSTITUCIONTIPOEXPEDIENTE="X."+ExpPermisosTiposExpedientesBean.C_IDINSTITUCIONTIPOEXPEDIENTE;
		String E_IDTIPOEXPEDIENTE="X."+ExpPermisosTiposExpedientesBean.C_IDTIPOEXPEDIENTE;
	
		String A_FECHAALERTA = "A." + ExpAlertaBean.C_FECHAALERTA;
		String T_TIPOEXPEDIENTE = "T." + ExpTipoExpedienteBean.C_NOMBRE + " AS NOMBRETIPOEXPEDIENTE ";
		String A_TEXTO = "A." + ExpAlertaBean.C_TEXTO;
		String A_IDALERTA = "A." + ExpAlertaBean.C_IDALERTA;
	
		//NOMBRES TABLAS PARA LA JOIN
		String T_EXP_ALERTA = ExpAlertaBean.T_NOMBRETABLA + " A";
		String T_EXP_TIPOEXPEDIENTE = ExpTipoExpedienteBean.T_NOMBRETABLA + " T";
	
		//LMS 09/08/2006
		//Se añade el control de permisos sobre el tipo de expediente.
		String T_EXP_PERMISOSTIPOSEXPEDIENTES = ExpPermisosTiposExpedientesBean.T_NOMBRETABLA + " X";
	
	
		//Valores recogidos del formulario para la búsqueda
		String comboTipoExp = form.getComboTipoExpediente();
	
		//getComboTipoExpediente nos está devolviendo (idinstitucion,idtipoexpediente)
		String idinstitucion_tipoexpediente = "";
		if (comboTipoExp!=null && !comboTipoExp.equals("")){
			StringTokenizer st = new StringTokenizer(comboTipoExp,",");
			idinstitucion_tipoexpediente=st.nextToken();
			form.setTipoExpediente(st.nextToken());        	
		}else{
			idinstitucion_tipoexpediente="";
			form.setTipoExpediente("");  
		}
	
		String tipoExpediente = form.getTipoExpediente();
		String numeroExpediente = form.getNumeroExpediente();
		String anioExpediente = form.getAnioExpediente();
	
		String where = " WHERE ";
	
		where += A_IDINSTITUCION + " = " + form.getIdInstitucion();
	
		where += " AND " + A_IDTIPOEXPEDIENTE + " = " + T_IDTIPOEXPEDIENTE;
		where += " AND " + A_IDINSTITUCIONTIPOEXPEDIENTE + " = " + T_IDINSTITUCION;
		where += " AND A.IDINSTITUCION_TIPOEXPEDIENTE =  A.IDINSTITUCION ";
		where += " AND A.IDINSTITUCION_TIPOEXPEDIENTE =  EST.IDINSTITUCION ";
		where += " AND A.IDFASE=EST.IDFASE AND A.IDESTADO=EST.IDESTADO ";
		where += " AND A.IDTIPOEXPEDIENTE=EST.IDTIPOEXPEDIENTE ";
		where += " AND EST.IDINSTITUCION = FAS.IDINSTITUCION  "; 
		where += " AND EST.IDTIPOEXPEDIENTE = FAS.IDTIPOEXPEDIENTE ";
		where += " AND EST.IDFASE = FAS.IDFASE ";
	
		//campos de búsqueda
	
		if(tipoExpediente!=null && !tipoExpediente.equals("")){
			where += " AND " + A_IDTIPOEXPEDIENTE + " = " + tipoExpediente ;
		}
		if(numeroExpediente!=null && !numeroExpediente.equals("")){
			where += " AND "+ComodinBusquedas.prepararSentenciaCompleta(numeroExpediente,A_NUMEROEXPEDIENTE ) ;
		}
	
		if(anioExpediente!=null && !anioExpediente.equals("")){
			where += " AND "+ComodinBusquedas.prepararSentenciaCompleta(anioExpediente,A_ANIOEXPEDIENTE ) ;
		}
		//registros no borrados lógicamente (campo Borrado='N')
		where += " AND " + A_BORRADO + " = 'N'";
	
		//LMS 09/08/2006
		//Se añade el control de permisos sobre el tipo de expediente.
		where += " AND " + E_IDINSTITUCIONTIPOEXPEDIENTE + " = " + A_IDINSTITUCION;
		where += " AND " + E_IDTIPOEXPEDIENTE + " = " + A_IDTIPOEXPEDIENTE;
		where += " AND " + E_IDPERFIL + " IN (";
	
		String[] aPerfiles = userBean.getProfile();
	
		for (int i=0; i<aPerfiles.length; i++, where+=",")
		{
			where += "'"+aPerfiles[i] +"'";
		}
	
		where = where.substring(0,where.length()-1);
		where += ")";
		if(form.getIdFase()!=null && !form.getIdFase().equals("")){
			where += " AND "+A_IDFASE+"="+form.getIdFase();
		}
		if(form.getIdEstado()!=null && !form.getIdEstado().equals("")){
			where += " AND "+A_IDESTADO+"="+form.getIdEstado();
		}
	
		if((form.getFechaDesde()!=null && !form.getFechaDesde().equals(""))||(form.getFechaHasta()!=null && !form.getFechaHasta().equals(""))){
			
			String fDesde = GstDate.getApplicationFormatDate("", form.getFechaDesde());
			String fHasta = GstDate.getApplicationFormatDate("", form.getFechaHasta());
			where += " AND "+GstDate.dateBetweenDesdeAndHasta(A_FECHAALERTA, fDesde, fHasta);
	
		}
	
		//select completa
		String sql = "SELECT ";
	
		sql += A_FECHAALERTA +"," + T_TIPOEXPEDIENTE +","+ A_NUMEROEXPEDIENTE +","+
		A_ANIOEXPEDIENTE +","+ A_TEXTO +","+ A_IDALERTA +","+ A_IDINSTITUCION +","+ 
		A_IDINSTITUCIONTIPOEXPEDIENTE +","+ A_IDTIPOEXPEDIENTE+
			",EST.NOMBRE EST_NOMBRE,    FAS.NOMBRE FAS_NOMBRE "
		
		
		+ " FROM ";	
		sql += T_EXP_ALERTA + "," + T_EXP_TIPOEXPEDIENTE + "," + T_EXP_PERMISOSTIPOSEXPEDIENTES;
		sql += " ,EXP_ESTADO EST, EXP_FASES FAS ";
		sql += " " + where;
		
		Paginador paginador = new Paginador(sql);				
		int totalRegistros = paginador.getNumeroTotalRegistros();
		
		if (totalRegistros==0){					
			paginador =null;
		}
		
		return paginador;

    }
}

/*
 * Created on Mar 28, 2005
 * @author jmgrau 
*/
package com.siga.beans;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.consultas.CriterioDinamico;
import com.siga.general.SIGAException;

public class EnvDestinatariosAdm extends MasterBeanAdministrador {

    public EnvDestinatariosAdm(UsrBean usuario)
	{
	    super(EnvDestinatariosBean.T_NOMBRETABLA, usuario);
	}
    
    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
     */
    protected String[] getCamposBean() {
        String[] campos = {
                EnvDestinatariosBean.C_IDENVIO,
            	EnvDestinatariosBean.C_IDPERSONA,
            	EnvDestinatariosBean.C_IDINSTITUCION,
            	EnvDestinatariosBean.C_DOMICILIO,
            	EnvDestinatariosBean.C_CODIGOPOSTAL,
            	EnvDestinatariosBean.C_FAX1,
            	EnvDestinatariosBean.C_FAX2,
            	EnvDestinatariosBean.C_CORREOELECTRONICO,
            	EnvDestinatariosBean.C_IDPAIS,
            	EnvDestinatariosBean.C_IDPROVINCIA,
            	EnvDestinatariosBean.C_IDPOBLACION,
            	EnvDestinatariosBean.C_POBLACIONEXTRANJERA,
            	EnvDestinatariosBean.C_NOMBRE,
            	EnvDestinatariosBean.C_APELLIDOS1,
            	EnvDestinatariosBean.C_APELLIDOS2,
            	EnvDestinatariosBean.C_NIFCIF,
            	EnvDestinatariosBean.C_MOVIL,
            	EnvDestinatariosBean.C_FECHAMODIFICACION,
            	EnvDestinatariosBean.C_USUMODIFICACION,
            	EnvDestinatariosBean.C_TIPODESTINATARIO,
            	EnvDestinatariosBean.C_ORIGENDESTINATARIO,
            	EnvDestinatariosBean.C_IDESTADO,
				};

		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
     */
    protected String[] getClavesBean() {
        
        String[] claves = {EnvDestinatariosBean.C_IDINSTITUCION, 
                		   EnvDestinatariosBean.C_IDENVIO,
                		   EnvDestinatariosBean.C_IDPERSONA};
		return claves;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
     */
    protected String[] getOrdenCampos() {
        
        String[] campos = {EnvDestinatariosBean.C_IDENVIO};
		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#hashTableToBean(java.util.Hashtable)
     */
    protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
        EnvDestinatariosBean bean = null;

		try
		{
			bean = new EnvDestinatariosBean();
			
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, EnvDestinatariosBean.C_IDINSTITUCION));
			bean.setIdEnvio(UtilidadesHash.getInteger(hash, EnvDestinatariosBean.C_IDENVIO));
			bean.setIdPersona(UtilidadesHash.getLong(hash, EnvDestinatariosBean.C_IDPERSONA));
			bean.setDomicilio(UtilidadesHash.getString(hash, EnvDestinatariosBean.C_DOMICILIO));
			bean.setCodigoPostal(UtilidadesHash.getString(hash, EnvDestinatariosBean.C_CODIGOPOSTAL));
			bean.setIdPais(UtilidadesHash.getString(hash, EnvDestinatariosBean.C_IDPAIS));
			bean.setIdProvincia(UtilidadesHash.getString(hash, EnvDestinatariosBean.C_IDPROVINCIA));
			bean.setIdPoblacion(UtilidadesHash.getString(hash, EnvDestinatariosBean.C_IDPOBLACION));
			bean.setPoblacionExtranjera(UtilidadesHash.getString(hash, EnvDestinatariosBean.C_POBLACIONEXTRANJERA));
			bean.setFax1(UtilidadesHash.getString(hash, EnvDestinatariosBean.C_FAX1));
			bean.setFax2(UtilidadesHash.getString(hash, EnvDestinatariosBean.C_FAX2));
			bean.setCorreoElectronico(UtilidadesHash.getString(hash, EnvDestinatariosBean.C_CORREOELECTRONICO));
			bean.setNombre(UtilidadesHash.getString(hash, EnvDestinatariosBean.C_NOMBRE));
			bean.setApellidos1(UtilidadesHash.getString(hash, EnvDestinatariosBean.C_APELLIDOS1));
			bean.setApellidos2(UtilidadesHash.getString(hash, EnvDestinatariosBean.C_APELLIDOS2));
			bean.setNifcif(UtilidadesHash.getString(hash, EnvDestinatariosBean.C_NIFCIF));
			bean.setMovil(UtilidadesHash.getString(hash, EnvDestinatariosBean.C_MOVIL));
			bean.setTipoDestinatario(UtilidadesHash.getString(hash, EnvDestinatariosBean.C_TIPODESTINATARIO));
			bean.setOrigenDestinatario(UtilidadesHash.getShort(hash, EnvDestinatariosBean.C_ORIGENDESTINATARIO));
			bean.setIdEstado(UtilidadesHash.getShort(hash, EnvDestinatariosBean.C_IDESTADO));
			
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

			EnvDestinatariosBean b = (EnvDestinatariosBean) bean;
			
			UtilidadesHash.set(htData, EnvDestinatariosBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, EnvDestinatariosBean.C_IDENVIO, b.getIdEnvio());
			UtilidadesHash.set(htData, EnvDestinatariosBean.C_IDPERSONA, b.getIdPersona());
			UtilidadesHash.set(htData, EnvDestinatariosBean.C_DOMICILIO, b.getDomicilio());
			UtilidadesHash.set(htData, EnvDestinatariosBean.C_CODIGOPOSTAL, b.getCodigoPostal());
			UtilidadesHash.set(htData, EnvDestinatariosBean.C_IDPAIS, b.getIdPais());
			UtilidadesHash.set(htData, EnvDestinatariosBean.C_IDPROVINCIA, b.getIdProvincia());
			UtilidadesHash.set(htData, EnvDestinatariosBean.C_IDPOBLACION, b.getIdPoblacion());
			UtilidadesHash.set(htData, EnvDestinatariosBean.C_POBLACIONEXTRANJERA, b.getPoblacionExtranjera());
			UtilidadesHash.set(htData, EnvDestinatariosBean.C_CORREOELECTRONICO, b.getCorreoElectronico());
			UtilidadesHash.set(htData, EnvDestinatariosBean.C_FAX1, b.getFax1());
			UtilidadesHash.set(htData, EnvDestinatariosBean.C_FAX2, b.getFax2());
			UtilidadesHash.set(htData, EnvDestinatariosBean.C_NOMBRE, b.getNombre());
			UtilidadesHash.set(htData, EnvDestinatariosBean.C_APELLIDOS1, b.getApellidos1());
			UtilidadesHash.set(htData, EnvDestinatariosBean.C_APELLIDOS2, b.getApellidos2());
			UtilidadesHash.set(htData, EnvDestinatariosBean.C_NIFCIF, b.getNifcif());
			UtilidadesHash.set(htData, EnvDestinatariosBean.C_MOVIL, b.getMovil());
			UtilidadesHash.set(htData, EnvDestinatariosBean.C_TIPODESTINATARIO, b.getTipoDestinatario());
			UtilidadesHash.set(htData, EnvDestinatariosBean.C_ORIGENDESTINATARIO, b.getOrigenDestinatario());
			UtilidadesHash.set(htData, EnvDestinatariosBean.C_IDESTADO, b.getIdEstado());
			
		}

		catch (Exception e)
		{
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}
    
   /**
    * @author juan.grau
    *
    * Permite la búsqueda de datos de destinatario.
    *
    * @param String idInstitucion
    * @param String idEnvio
    * @param String idPersona
    * 
    * @return Vector (Rows)
    * 
    * @throws ClsExceptions
    *  
    */    
       
   public Vector getDatosDestinatario(String idEnvio, 
           							  String idInstitucion, 
           							  String idPersona)
   	throws ClsExceptions{
       
       Vector datos = new Vector(); 
    
       RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			
			StringBuffer sb= new StringBuffer();
			sb.append(" SELECT DES.IDINSTITUCION,  DES.IDPERSONA,    DES.DOMICILIO,  DES.POBLACIONEXTRANJERA,   DES.CODIGOPOSTAL,   DES.FAX1,");
            sb.append(" DES.FAX2,  DES.MOVIL,  DES.CORREOELECTRONICO,  PER.NOMBRE,  PER.APELLIDOS1,  PER.APELLIDOS2,  PER.NIFCIF,  COL.NCOLEGIADO,");
            sb.append(" F_SIGA_GETRECURSO(PA.NOMBRE, 1) AS PAIS,  PR.NOMBRE AS PROVINCIA, PO.NOMBRE AS POBLACION");
            sb.append(" FROM ENV_DESTINATARIOS DES,   CEN_COLEGIADO COL, CEN_PERSONA PER, CEN_PAIS PA,CEN_PROVINCIAS PR,CEN_POBLACIONES   PO");
            sb.append(" WHERE DES.IDINSTITUCION ="+idInstitucion +" AND DES.IDENVIO ="+idEnvio+ " AND DES.IDPERSONA ="+idPersona+" AND DES.IDPERSONA = PER.IDPERSONA");
            sb.append(" AND DES.IDINSTITUCION = COL.IDINSTITUCION(+)  AND DES.IDPERSONA = COL.IDPERSONA(+) AND DES.IDPAIS = PA.IDPAIS(+) AND DES.IDPROVINCIA = PR.IDPROVINCIA(+)");
            sb.append(" AND DES.IDPOBLACION = PO.IDPOBLACION(+) and DES.TIPODESTINATARIO='CEN_PERSONA'");
            sb.append(" UNION "); 
            sb.append(" SELECT DES.IDINSTITUCION, DES.IDPERSONA,  DES.DOMICILIO, DES.POBLACIONEXTRANJERA, DES.CODIGOPOSTAL, DES.FAX1, DES.FAX2, DES.MOVIL,");
            sb.append(" DES.CORREOELECTRONICO, PER.NOMBRE,  PER.APELLIDO1,  PER.APELLIDO2, PER.nif, COL.NCOLEGIADO, F_SIGA_GETRECURSO(PA.NOMBRE, 1) AS PAIS,");
            sb.append(" PR.NOMBRE AS PROVINCIA, PO.NOMBRE AS POBLACION FROM ENV_DESTINATARIOS DES, CEN_COLEGIADO     COL, SCS_PERSONAJG     PER,");
            sb.append(" CEN_PAIS  PA, CEN_PROVINCIAS  PR, CEN_POBLACIONES   PO  WHERE DES.IDINSTITUCION ="+idInstitucion+"  AND DES.IDENVIO ="+idEnvio+ " AND DES.IDPERSONA ="+idPersona);
            sb.append(" AND DES.IDPERSONA = PER.IDPERSONA  AND DES.IDINSTITUCION = COL.IDINSTITUCION(+) AND DES.IDPERSONA = COL.IDPERSONA(+) AND DES.IDPAIS = PA.IDPAIS(+)");
            sb.append(" AND DES.IDPROVINCIA = PR.IDPROVINCIA(+) AND DES.IDPOBLACION = PO.IDPOBLACION(+) and DES.TIPODESTINATARIO='SCS_PERSONAJG'");
            sb.append(" AND PER.IDINSTITUCION="+idInstitucion);
            
            sb.append(" UNION "); 
            
            sb.append(" SELECT DES.IDINSTITUCION, DES.IDPERSONA,  DES.DOMICILIO, DES.POBLACIONEXTRANJERA, DES.CODIGOPOSTAL, DES.FAX1, DES.FAX2, DES.MOVIL,");
            sb.append(" DES.CORREOELECTRONICO, DES.NOMBRE,  DES.APELLIDOS1,  DES.APELLIDOS2, DES.NIFCIF, PRO.NCOLEGIADO, F_SIGA_GETRECURSO(PA.NOMBRE, 1) AS PAIS,");
            sb.append(" PR.NOMBRE AS PROVINCIA, PO.NOMBRE AS POBLACION FROM ENV_DESTINATARIOS DES, SCS_PROCURADOR  PRO,");
            sb.append(" CEN_PAIS  PA, CEN_PROVINCIAS  PR, CEN_POBLACIONES   PO  WHERE DES.IDINSTITUCION ="+idInstitucion+"  AND DES.IDENVIO ="+idEnvio+ " AND DES.IDPERSONA ="+idPersona);
            sb.append(" AND DES.IDPERSONA = PRO.IDPROCURADOR  AND DES.IDINSTITUCION = PRO.IDINSTITUCION AND DES.IDPAIS = PA.IDPAIS(+)");
            sb.append(" AND DES.IDPROVINCIA = PR.IDPROVINCIA(+) AND DES.IDPOBLACION = PO.IDPOBLACION(+) and DES.TIPODESTINATARIO='SCS_PROCURADOR'");
            
            sb.append(" UNION "); 
            
            sb.append(" SELECT DES.IDINSTITUCION, DES.IDPERSONA,  DES.DOMICILIO, DES.POBLACIONEXTRANJERA, DES.CODIGOPOSTAL, DES.FAX1, DES.FAX2, DES.MOVIL,");
            sb.append(" DES.CORREOELECTRONICO, DES.NOMBRE,  DES.APELLIDOS1,  DES.APELLIDOS2, DES.NIFCIF, '', F_SIGA_GETRECURSO(PA.NOMBRE, 1) AS PAIS,");
            sb.append(" PR.NOMBRE AS PROVINCIA, PO.NOMBRE AS POBLACION FROM ENV_DESTINATARIOS DES, SCS_JUZGADO  PRO,");
            sb.append(" CEN_PAIS  PA, CEN_PROVINCIAS  PR, CEN_POBLACIONES   PO  WHERE DES.IDINSTITUCION ="+idInstitucion+"  AND DES.IDENVIO ="+idEnvio+ " AND DES.IDPERSONA ="+idPersona);
            sb.append(" AND DES.IDPERSONA = PRO.IDJUZGADO  AND DES.IDINSTITUCION = PRO.IDINSTITUCION AND DES.IDPAIS = PA.IDPAIS(+)");
            sb.append(" AND DES.IDPROVINCIA = PR.IDPROVINCIA(+) AND DES.IDPOBLACION = PO.IDPOBLACION(+) and DES.TIPODESTINATARIO='SCS_JUZGADO'");

            
            
            
           
            
            
             

			if (rc.query(sb.toString())) {
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
   
	/** 
	 * Comprueba si el destinatario ya ha sido insertado en la tabla de destinatarios
	 * @param  idEnvio id del envio
	 * @param  idInstitucion id de la institucion
	 * @param  idPersona id de la persona
	 * @return  boolean con el resultado  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */		
	public boolean existeDestinatario(String idEnvio, 
										String idInstitucion, 
										String idPersona) 
		throws ClsExceptions, SIGAException{
		try {
			Vector v = this.select(" WHERE " + EnvDestinatariosBean.C_IDINSTITUCION + " = " + idInstitucion +
			        				 " AND " + EnvDestinatariosBean.C_IDENVIO + " = " + idEnvio +
			        				 " AND " + EnvDestinatariosBean.C_IDPERSONA + " = " + idPersona);
		    
			if ((v != null) && (v.size()>0)) {
				return true;
			} else {
				return false;
			}
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al recuperar los datos");
		}
	}
	public void insertarDestinatariosDinamicos(String idInstitucion, String idEnvio, String idTipoEnvio) throws SIGAException, ClsExceptions{
		TreeMap<Integer,EnvDestinatariosBean> detDinamicos =  getDestinatariosDinamicos(idInstitucion, idEnvio, idTipoEnvio);
		EnvDestinatariosAdm destinatariosAdm = new EnvDestinatariosAdm(usrbean);
		Iterator<Integer> iterator = detDinamicos.keySet().iterator();
		while (iterator.hasNext()) {
			Integer key = (Integer) iterator.next();
			EnvDestinatariosBean envDestinatariosBean = detDinamicos.get(key);
			envDestinatariosBean.setOrigenDestinatario(EnvDestinatariosBean.ORIGENDESTINATARIO_DINAMICO);
			destinatariosAdm.insert(envDestinatariosBean);
			
		}
		
	}
	/**
	 * Metodo que devuelve los destinatarios que provienen de una lista dinamica y que no estan incluidos como destinatarios individukles
	 * @param idInstitucion
	 * @param idEnvio
	 * @param idTipoEnvio
	 * @return
	 * @throws SIGAException
	 */
	public TreeMap<Integer,EnvDestinatariosBean> getDestinatariosDinamicos (String idInstitucion, String idEnvio, String idTipoEnvio) throws SIGAException{
		
		Hashtable<String,EnvDestinatariosBean> destinatarios = getDestinatariosManuales(idInstitucion, idEnvio, idTipoEnvio);
		
		TreeMap<Integer,EnvDestinatariosBean> destDinamicosMap = new TreeMap<Integer, EnvDestinatariosBean>();
		String sql = null;
		int orden = 0;
		//      Seleccion de destinatarios de lista no dinamica. Se obtiene tanto los destinatarios con direccion de envio como los que no la tienen.
		//		Estos ultimos quedaran anotados en el log como usuario sin direccion de correo configurada
		try {
			RowsContainer rcDes = new RowsContainer();
			sql = getQueryDestinatariosListasNoDinamicas(idInstitucion, idEnvio, idTipoEnvio);
			if (rcDes.query(sql)) {
				for (int i = 0; i < rcDes.size(); i++)	{
					Row fila = (Row) rcDes.get(i);
					orden++;
					if(!destinatarios.containsKey("CEN_PERSONA_"+fila.getString("IDPERSONA"))){
						EnvDestinatariosBean destDinamico = getDestinatario(fila,Integer.valueOf(idInstitucion),Integer.valueOf(idEnvio)); 
						destDinamicosMap.put(orden,destDinamico);
						destinatarios.put(destDinamico.getTipoDestinatario()+"_"+destDinamico.getIdPersona(),  destDinamico);
				
					}
				}
			}

		}catch(ClsExceptions e){
			ClsLogging.writeFileLogError("EnvEnviosAdm.getDestinatarios. Error obteniene los destinatarios de las listas no dinamicas",e,10);
			throw new SIGAException("process.database_error");

		}
		/************ Destinatarios de listas dinámicas ***************/
//	  Obtenemos todas las queries distintas para el envío y la institución en cuestión
		Vector vQueriesConsultasNoExpertas = new Vector();
		Vector vQueriesConsultasExpertas = new Vector();
		//Acceso a BBDD
		try {
			RowsContainer rcQueries = new RowsContainer();
			if (rcQueries.query(getQueryConsultasDinamicas(idInstitucion, idEnvio))) {
				for (int i = 0; i < rcQueries.size(); i++)	{
					Row fila = (Row) rcQueries.get(i);
					String sQuery = fila.getString(ConConsultaBean.C_SENTENCIA);
					//reemplazamos la etiqueta de tipo de envío en la query
					sQuery = sQuery.replaceFirst(EnvTipoEnviosAdm.CONS_TIPOENVIO,idTipoEnvio);
					//Anhadimos la query al vector resultante de queries
					if("1".equals(fila.getString(ConConsultaBean.C_ESEXPERTA))){
						vQueriesConsultasExpertas.add(fila);
					}
					else{
						vQueriesConsultasNoExpertas.add(sQuery);
					}
				}
			}
		} catch (ClsExceptions e1) {
			ClsLogging.writeFileLogError("EnvEnviosAdm.getDestinatarios. Error ald obtener las consultas dinamicas",e1,10);
			throw new SIGAException("process.database_error");

		}
		//		Montamos y ejecutamos la query que obtendrá el vector de destinatarios de las listas dinámicas no expertas
		Vector vDestDinamicasNoExpertas = new Vector();

		if (vQueriesConsultasNoExpertas.size()>0){
			sql = "SELECT * FROM ";
			for (int i = 0; i < vQueriesConsultasNoExpertas.size(); i++) {
				sql += "(" + vQueriesConsultasNoExpertas.elementAt(i) + ") UNION ";                    
			}
			//borramos el último UNION
			sql = sql.substring(0,sql.length()-7);
			try {
				RowsContainer rcDestDin = new RowsContainer();
				if (rcDestDin.query(getQueryDestinatarios(sql.toString()))) {
					for (int i = 0; i < rcDestDin.size(); i++)	{
						Row fila = (Row) rcDestDin.get(i);
						if(!destinatarios.containsKey("CEN_PERSONA_"+fila.getString("IDPERSONA")) ){
							orden++;
							EnvDestinatariosBean destDinamico = getDestinatario(fila,Integer.valueOf(idInstitucion),Integer.valueOf(idEnvio));
							destDinamicosMap.put(orden,destDinamico);
							destinatarios.put(destDinamico.getTipoDestinatario()+"_"+destDinamico.getIdPersona(),  destDinamico);
						}
					}
				}

			} catch (ClsExceptions e1) {
				ClsLogging.writeFileLogError("EnvEnviosAdm.getDestinatarios. Error al obtener los destinatarios de las listas dinamicas no expertas",e1,10);
				throw new SIGAException("process.database_error");


			}					
		}

		//2009-CGAE-119-INC-CAT-035
		//		Montamos y ejecutamos la query que obtendrá el vector de destinatarios de las listas dinámicas  expertas 
		if (vQueriesConsultasExpertas.size()>0){
			for (int i = 0; i < vQueriesConsultasExpertas.size(); i++) {
				//Hay que crear el objeto ConConsultaBean 
				//Las consultas expertas para envios no pueden tener criterios dinamicos, por eso
				//el último parametro de la llamada a procesarEjecutarConsulta es un vector vacío
				Row fila = (Row) vQueriesConsultasExpertas.get(i);
				//				Row fila = (Row) rcQueries.get(i);
				ConConsultaAdm conAdm = new ConConsultaAdm(usrbean);
				ConConsultaBean conBean = new ConConsultaBean();
				conBean.setEsExperta("1");
				conBean.setSentencia(fila.getString(ConConsultaBean.C_SENTENCIA));
				conBean.setTipoConsulta(ConConsultaAdm.TIPO_CONSULTA_ENV);
				conBean.setIdInstitucion(Integer.valueOf(idInstitucion));
				conBean.setIdConsulta(Long.valueOf(fila.getString(ConConsultaBean.C_IDCONSULTA)));
				Hashtable ht = null;
				try {
					ht = conAdm.procesarEjecutarConsulta(idTipoEnvio, conBean, new CriterioDinamico[0], true);
				}catch (ClsExceptions e) {
					ClsLogging.writeFileLogError("EnvEnviosAdm.getDestinatarios. Error al procesar la consulta experta",e,10);
					throw new SIGAException("process.database_error");

				}

				sql = (String) ht.get("sentencia");
				Hashtable codigosOrdenados = (Hashtable) ht.get("codigosOrdenados");

//				String sqlExperta = getQueryDestinatarios(sql);

				RowsContainer rcDestDin = new RowsContainer();
				try {
					
					if (rcDestDin.findBind(getQueryDestinatarios(sql.toString()),codigosOrdenados)) {
						for (int j = 0; j < rcDestDin.size(); j++)	{
							Row filaDestDin = (Row) rcDestDin.get(j);
							if(!destinatarios.containsKey("CEN_PERSONA_"+filaDestDin.getString("IDPERSONA"))){
								orden++;
								EnvDestinatariosBean destDinamico = getDestinatario(filaDestDin,Integer.valueOf(idInstitucion),Integer.valueOf(idEnvio)); 
								destDinamicosMap.put(orden, destDinamico );
								destinatarios.put(destDinamico.getTipoDestinatario()+"_"+destDinamico.getIdPersona(),  destDinamico);
							}
						}
					}
				}catch (ClsExceptions e) {
					ClsLogging.writeFileLogError("EnvEnviosAdm.getDestinatarios. Error al obtener los destinatarios de las listas dinamicas expertas",e,10);
					throw new SIGAException("process.database_error");

				}
			}
		}
		return destDinamicosMap;
	}
	public Vector getDestinatarios(String idInstitucion, String idEnvio, String idTipoEnvio)throws SIGAException, ClsExceptions{

		Vector<EnvDestinatariosBean> vDestManuales = null;
		
		int orden = 0; 
		
		TreeMap<Integer,String> destinatariosmMap = new TreeMap<Integer, String>();
		Hashtable<String,EnvDestinatariosBean> destinatariosHashtable = new Hashtable<String, EnvDestinatariosBean>();

		Hashtable htPk = new Hashtable();
		htPk.put(EnvDestinatariosBean.C_IDINSTITUCION,idInstitucion);
		htPk.put(EnvDestinatariosBean.C_IDENVIO,idEnvio);
		//	    Obtenemos los destinatarios manuales
		EnvDestinatariosAdm destAdm = new EnvDestinatariosAdm(this.usrbean);
		try {
			vDestManuales = destAdm.select(htPk);
			
		} catch (ClsExceptions e1) {
			ClsLogging.writeFileLogError("EnvEnviosAdm.getDestinatarios. Error obteniendo los destinatarios manuales del envio",e1,10);
			throw new SIGAException("process.database_error");
		}
		//Para el caso de correo ordinario no se procesan los envios por lo que no se generaran los destinatario cuyo origen son las listas dinamicas
		if(Integer.valueOf(idTipoEnvio)==EnvTipoEnviosAdm.K_CORREO_ORDINARIO ){
			TreeMap<Integer,EnvDestinatariosBean> detDinamicos =  getDestinatariosDinamicos(idInstitucion, idEnvio, idTipoEnvio);
			Iterator<Integer> iterator = detDinamicos.keySet().iterator();
			while (iterator.hasNext()) {
				Integer key = (Integer) iterator.next();
				EnvDestinatariosBean envDestinatariosBean = detDinamicos.get(key);
				vDestManuales.add(envDestinatariosBean);
				
			}
			
			
		}
		return vDestManuales;
	}
	public Vector getDestinatariosEtiquetas(String idInstitucion, String idEnvio, String idTipoEnvio)throws SIGAException, ClsExceptions{

		Vector<EnvDestinatariosBean> vDestManuales = null;
		
		int orden = 0; 
		
		TreeMap<Integer,String> destinatariosmMap = new TreeMap<Integer, String>();
		Hashtable<String,EnvDestinatariosBean> destinatariosHashtable = new Hashtable<String, EnvDestinatariosBean>();

		Hashtable htPk = new Hashtable();
		htPk.put(EnvDestinatariosBean.C_IDINSTITUCION,idInstitucion);
		htPk.put(EnvDestinatariosBean.C_IDENVIO,idEnvio);
		htPk.put(EnvDestinatariosBean.C_ORIGENDESTINATARIO,EnvDestinatariosBean.ORIGENDESTINATARIO_INDIVIDUAL);
		//	    Obtenemos los destinatarios manuales
		EnvDestinatariosAdm destAdm = new EnvDestinatariosAdm(this.usrbean);
		try {
			vDestManuales = destAdm.select(htPk);
			
		} catch (ClsExceptions e1) {
			ClsLogging.writeFileLogError("EnvEnviosAdm.getDestinatarios. Error obteniendo los destinatarios manuales del envio",e1,10);
			throw new SIGAException("process.database_error");
		}
		TreeMap<Integer,EnvDestinatariosBean> detDinamicos =  getDestinatariosDinamicos(idInstitucion, idEnvio, idTipoEnvio);
		Iterator<Integer> iterator = detDinamicos.keySet().iterator();
		while (iterator.hasNext()) {
			Integer key = (Integer) iterator.next();
			EnvDestinatariosBean envDestinatariosBean = detDinamicos.get(key);
			vDestManuales.add(envDestinatariosBean);
			
		}
			
		return vDestManuales;
	}
	
	
	
	private String getQueryDestinatariosListasNoDinamicasSinDireccion(String idInstitucion, String idEnvio, String idTipoEnvio){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT CL.IDPERSONA,NULL IDDIRECCION,'' CODIGOPOSTAL,'' CORREOELECTRONICO,'' DOMICILIO,'' FAX1,'' FAX2,'' IDPAIS,'' IDPROVINCIA,'' IDPOBLACION,'' POBLACIONEXTRANJERA,'' MOVIL ");
		sql.append(" FROM ENV_ENVIOS                 EN, ");
		sql.append(" ENV_LISTACORREOSENVIOS     LE, ");
		sql.append(" ENV_LISTACORREOS           LI, ");
		sql.append(" ENV_COMPONENTESLISTACORREO CL ");
		sql.append(" WHERE EN.IDINSTITUCION =  ");
		sql.append(idInstitucion);
		sql.append(" AND EN.IDENVIO =  ");
		sql.append(idEnvio);
		sql.append(" AND EN.IDINSTITUCION = LE.IDINSTITUCION ");
		sql.append(" AND EN.IDENVIO = LE.IDENVIO ");
		sql.append(" AND LE.IDINSTITUCION = LI.IDINSTITUCION ");
		sql.append(" AND LI.IDINSTITUCION = CL.IDINSTITUCION ");
		sql.append(" AND LE.IDLISTACORREO = LI.IDLISTACORREO ");
		sql.append(" AND LI.IDLISTACORREO = CL.IDLISTACORREO ");
		sql.append(" AND IDPERSONA NOT IN  ( ");
		sql.append(getQueryDestinatariosListasNoDinamicasConDireccion(idInstitucion, idEnvio, idTipoEnvio,false));
		sql.append(" )");
		
		
		
		return sql.toString();
	}
	
	private String getQueryDestinatariosListasNoDinamicasConDireccion(String idInstitucion, String idEnvio, String idTipoEnvio,boolean isIncluirCamposSalida){
		StringBuffer sql = new StringBuffer();
		if(isIncluirCamposSalida)
			sql.append(" SELECT CL.IDPERSONA,DI.IDDIRECCION, DI.CODIGOPOSTAL,DI.CORREOELECTRONICO,DI.DOMICILIO,DI.FAX1,DI.FAX2,DI.IDPAIS,DI.IDPROVINCIA,DI.IDPOBLACION,DI.POBLACIONEXTRANJERA,DI.MOVIL ");
		else
			sql.append(" SELECT CL.IDPERSONA ");
		sql.append(" FROM ENV_ENVIOS                 EN, ");
		sql.append(" ENV_LISTACORREOSENVIOS     LE, ");
		sql.append(" ENV_LISTACORREOS           LI, ");
		sql.append(" ENV_COMPONENTESLISTACORREO CL, ");
		sql.append(" CEN_DIRECCIONES            DI ");
		sql.append(" WHERE EN.IDINSTITUCION =  ");
		sql.append(idInstitucion);
		sql.append(" AND EN.IDENVIO =  ");
		sql.append(idEnvio);
		sql.append(" AND EN.IDINSTITUCION = LE.IDINSTITUCION ");
		sql.append(" AND EN.IDENVIO = LE.IDENVIO ");
		sql.append(" AND LE.IDINSTITUCION = LI.IDINSTITUCION ");
		sql.append(" AND LI.IDINSTITUCION = CL.IDINSTITUCION ");
		sql.append(" AND LE.IDLISTACORREO = LI.IDLISTACORREO ");
		sql.append(" AND LI.IDLISTACORREO = CL.IDLISTACORREO ");
		sql.append(" AND DI.IDINSTITUCION = CL.IDINSTITUCION ");
		sql.append(" AND CL.IDPERSONA = DI.IDPERSONA ");
		sql.append(" AND DI.FECHABAJA IS NULL ");
		sql.append(" AND DI.IDDIRECCION =f_siga_getdireccion(EN.IDINSTITUCION, CL.IDPERSONA, ");
		sql.append(idTipoEnvio);
		sql.append(") ");
		return sql.toString();
	}
	
	private String getQueryDestinatariosListasNoDinamicas(String idInstitucion, String idEnvio, String idTipoEnvio){
		StringBuffer sql = new StringBuffer();
		
		sql.append(" SELECT * FROM ( ");
		sql.append(getQueryDestinatariosListasNoDinamicasSinDireccion(idInstitucion, idEnvio, idTipoEnvio));
		sql.append(" UNION ");
		sql.append(getQueryDestinatariosListasNoDinamicasConDireccion(idInstitucion, idEnvio, idTipoEnvio,true));
		sql.append(" ) ");
		
		return getQueryDestinatarios(sql.toString());
		
	}
	
	private String getQueryDestinatarios(String query){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT PERSONASLISTA.*,P.NOMBRE,P.APELLIDOS1,P.APELLIDOS2,P.APELLIDOS2,P.NIFCIF FROM	( ");
		sql.append(query);
		sql.append(" ) PERSONASLISTA, CEN_PERSONA P ");
		sql.append(" WHERE P.IDPERSONA = PERSONASLISTA.IDPERSONA ");
		return sql.toString();
	}
	
	private String getQueryConsultasDinamicas(String idInstitucion, String idEnvio){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT C.SENTENCIA, C.ESEXPERTA, C.IDCONSULTA ");
		sql.append(" FROM CON_CONSULTA C ");
		sql.append(" WHERE EXISTS (SELECT * ");
		sql.append(" FROM ENV_LISTACORREOSENVIOS  LE, ");
		sql.append(" ENV_LISTACORREOS        LI, ");
		sql.append(" ENV_LISTACORREOCONSULTA LC ");
		sql.append(" WHERE LE.IDINSTITUCION = ");
		sql.append(idInstitucion);
		sql.append(" AND LE.IDENVIO = ");
		sql.append(idEnvio);
		sql.append(" AND LE.IDINSTITUCION = LI.IDINSTITUCION ");
		sql.append(" AND LE.IDLISTACORREO = LI.IDLISTACORREO ");
		sql.append(" AND LE.IDINSTITUCION = LI.IDINSTITUCION ");
		sql.append(" AND LE.IDLISTACORREO = LC.IDLISTACORREO ");
		sql.append(" AND LE.IDINSTITUCION = LC.IDINSTITUCION ");
		sql.append(" AND LC.IDINSTITUCION_CON = C.IDINSTITUCION ");
		sql.append(" AND LC.IDCONSULTA = C.IDCONSULTA ");
		sql.append(" AND LI.DINAMICA = 'S') ");
		return sql.toString();
		
		
		
	}
	private EnvDestinatariosBean getDestinatario(Row destinatarioRow,Integer idInstitucion,Integer idEnvio){
		EnvDestinatariosBean envDestinatario = new EnvDestinatariosBean();
		envDestinatario.setNombre(destinatarioRow.getString("NOMBRE")!=null?destinatarioRow.getString("NOMBRE"):"");
		envDestinatario.setApellidos1(destinatarioRow.getString("APELLIDOS1")!=null?destinatarioRow.getString("APELLIDOS1"):"");
		envDestinatario.setApellidos2(destinatarioRow.getString("APELLIDOS2")!=null?destinatarioRow.getString("APELLIDOS2"):"");
		envDestinatario.setIdInstitucion(idInstitucion);
		envDestinatario.setIdEnvio(idEnvio);
		envDestinatario.setIdPersona(Long.valueOf(destinatarioRow.getString("IDPERSONA")));
		envDestinatario.setCodigoPostal(destinatarioRow.getString("CODIGOPOSTAL")!=null?destinatarioRow.getString("CODIGOPOSTAL"):"");
		envDestinatario.setCorreoElectronico(destinatarioRow.getString("CORREOELECTRONICO")!=null?destinatarioRow.getString("CORREOELECTRONICO"):"");
		envDestinatario.setDomicilio(destinatarioRow.getString("DOMICILIO")!=null?destinatarioRow.getString("DOMICILIO"):"");
		envDestinatario.setFax1(destinatarioRow.getString("FAX1")!=null?destinatarioRow.getString("FAX1"):"");
		envDestinatario.setFax2(destinatarioRow.getString("FAX2")!=null?destinatarioRow.getString("FAX2"):"");
		envDestinatario.setIdPais(destinatarioRow.getString("IDPAIS")!=null?destinatarioRow.getString("IDPAIS"):"");
		envDestinatario.setIdProvincia(destinatarioRow.getString("IDPROVINCIA")!=null?destinatarioRow.getString("IDPROVINCIA"):"");
		envDestinatario.setIdPoblacion(destinatarioRow.getString("IDPOBLACION")!=null?destinatarioRow.getString("IDPOBLACION"):"");
		envDestinatario.setPoblacionExtranjera(destinatarioRow.getString("POBLACIONEXTRANJERA")!=null?destinatarioRow.getString("POBLACIONEXTRANJERA"):"");
		envDestinatario.setMovil(destinatarioRow.getString("MOVIL")!=null?destinatarioRow.getString("MOVIL"):"");
		envDestinatario.setNifcif(destinatarioRow.getString("NIFCIF")!=null?destinatarioRow.getString("NIFCIF"):"");
		
		
		
		
		return envDestinatario;
		
		
	}
	
	
	
	
	
	private Hashtable<String,EnvDestinatariosBean> getDestinatariosManuales(String idInstitucion, String idEnvio, String idTipoEnvio)throws SIGAException{

		Vector<EnvDestinatariosBean> vDestManuales = null;
		
		
//		int orden = 0; 
		
		TreeMap<String,Object[]> destinatariosMap = new TreeMap<String, Object[]>();

		Hashtable htPk = new Hashtable();
		htPk.put(EnvDestinatariosBean.C_IDINSTITUCION,idInstitucion);
		htPk.put(EnvDestinatariosBean.C_IDENVIO,idEnvio);
		//	    Obtenemos los destinatarios manuales
		EnvDestinatariosAdm destAdm = new EnvDestinatariosAdm(this.usrbean);
		try {
			vDestManuales = destAdm.select(htPk);
			
		} catch (ClsExceptions e1) {
			ClsLogging.writeFileLogError("EnvEnviosAdm.getDestinatarios. Error obteniendo los destinatarios manuales del envio",e1,10);
			throw new SIGAException("process.database_error");
		}
		Hashtable<String,EnvDestinatariosBean> destinatariosHashtable = new Hashtable<String, EnvDestinatariosBean>();
		for (EnvDestinatariosBean objectDestinatariosBean : vDestManuales) {
			destinatariosHashtable.put(objectDestinatariosBean.getTipoDestinatario()+"_"+objectDestinatariosBean.getIdPersona(), objectDestinatariosBean);
//			orden++;
			
		}
		return destinatariosHashtable;
	}
	
	

    
}

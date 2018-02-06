/*
 * VERSIONES:
 * yolanda.garcia - 16-11-2004 - Creación
 */

package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import javax.transaction.UserTransaction;

import org.redabogacia.sigaservices.app.exceptions.BusinessException;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.Paginador;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;


public class CenGruposClienteAdm extends MasterBeanAdministrador {
	
	public CenGruposClienteAdm (UsrBean usu) {
		super (CenGruposClienteBean.T_NOMBRETABLA, usu);
	}
	
	protected String[] getCamposBean() {
		String [] campos = {CenGruposClienteBean.C_IDGRUPO, 		
				    		CenGruposClienteBean.C_NOMBRE,
				    		CenGruposClienteBean.C_IDINSTITUCION,
							CenGruposClienteBean.C_FECHAMODIFICACION,
							CenGruposClienteBean.C_USUMODIFICACION};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {CenGruposClienteBean.C_IDGRUPO,
							CenGruposClienteBean.C_IDINSTITUCION};
		return claves;
	}
	
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		CenGruposClienteBean bean = null;
		
		try {
			bean = new CenGruposClienteBean();
			bean.setIdGrupo		  (UtilidadesHash.getInteger(hash, CenGruposClienteBean.C_IDGRUPO));
			bean.setIdInstitucion (UtilidadesHash.getInteger(hash, CenGruposClienteBean.C_IDINSTITUCION));
			bean.setNombre		  (UtilidadesHash.getString(hash, CenGruposClienteBean.C_NOMBRE));
			bean.setFechaMod	  (UtilidadesHash.getString(hash, CenGruposClienteBean.C_FECHAMODIFICACION));
			bean.setUsuMod		  (UtilidadesHash.getInteger(hash, CenGruposClienteBean.C_USUMODIFICACION));
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
			CenGruposClienteBean b = (CenGruposClienteBean) bean;
			UtilidadesHash.set(htData, CenGruposClienteBean.C_IDGRUPO, b.getIdGrupo());
			UtilidadesHash.set(htData, CenGruposClienteBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, CenGruposClienteBean.C_NOMBRE, b.getNombre());
			UtilidadesHash.set(htData, CenGruposClienteBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, CenGruposClienteBean.C_USUMODIFICACION, b.getUsuMod());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}
	
	/** Funcion select (String where). Devuelve los campos: IDPROCURADOR, IDINSTITUCION, NOMBRE, DIRECCION, <BR>
	 * CODIGOPOSTAL, POBLACION ,PROVINCIA, TELEFONO1, TELEFONO2, FAX1.
	 * @param criteros para filtrar el select, campo where 
	 *  @return vector con los registros encontrados. El objeto es de tipo administrador del bean 
	 * */
	public Paginador busquedaGruposFijos(String nombre, String idInstitucion,String lenguaje) throws ClsExceptions 
	{
		/*Vector datos = new Vector();*/
		String select = null;
		Paginador paginador=null;
		String orden="";
		
		try {
			select  =" SELECT i."+CenInstitucionBean.C_IDINSTITUCION;
			select += " , i."+CenInstitucionBean.C_ABREVIATURA+" INSTITUCION";
			select += " , F_SIGA_GETRECURSO(grupo."+CenGruposClienteBean.C_NOMBRE+", "+lenguaje+") "+CenGruposClienteBean.C_NOMBRE ;
			select += " , grupo."+CenGruposClienteBean.C_IDGRUPO;
			//FROM:
			select += " FROM "+CenGruposClienteBean.T_NOMBRETABLA+" grupo ,";
			select += CenInstitucionBean.T_NOMBRETABLA+" i ";
			
			//WHERE:
			if(idInstitucion.equals("2000")){
				//salen las de todas las instituciones
				select += " WHERE grupo."+CenGruposClienteBean.C_IDINSTITUCION+"= i."+CenInstitucionBean.C_IDINSTITUCION;
			}else{
				//salen las de la 2000 y las propias
				
				
				
				select += " WHERE grupo.IDINSTITUCION IN (SELECT IDINSTITUCION" +
						" FROM CEN_INSTITUCION START WITH IDINSTITUCION =" +idInstitucion;
				select += " CONNECT BY PRIOR  CEN_INST_IDINSTITUCION = IDINSTITUCION)";
				select += " AND grupo."+CenGruposClienteBean.C_IDINSTITUCION+"= i."+CenInstitucionBean.C_IDINSTITUCION;
			}
			
			//Filtro del nombre:
			if (nombre !=null && !nombre.equals(""))
				select += " AND "+ComodinBusquedas.prepararSentenciaCompleta(nombre.trim(),"F_SIGA_GETRECURSO(grupo."+CenGruposClienteBean.C_NOMBRE+", "+lenguaje+")" ) ;

			
			
			//ORDER BY:
			//aalg: se añade la ordenacion en español
			select += " ORDER BY grupo."+CenGruposClienteBean.C_IDINSTITUCION+", NLSSORT(grupo."+CenGruposClienteBean.C_NOMBRE+", 'NLS_SORT=SPANISH')";
			orden="order by idinstitucion,NLSSORT(nombre, 'NLS_SORT=SPANISH') desc";
			//Consulta:
			//datos = this.selectGenerico(select);
			/*** PAGINACION ***/ 
	        paginador = new Paginador(select,orden,true);				
			int totalRegistros = paginador.getNumeroTotalRegistros();
			
			if (totalRegistros==0){					
				paginador =null;
			}else{
				int registrosPorPagina = paginador.getNumeroRegistrosPorPagina();	    		
	    		Vector datos = paginador.obtenerPagina(1);
	    	
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return paginador;
	}	

	/** Funcion select (String where). Devuelve los campos: IDPROCURADOR, IDINSTITUCION, NOMBRE, DIRECCION, <BR>
	 * CODIGOPOSTAL, POBLACION ,PROVINCIA, TELEFONO1, TELEFONO2, FAX1.
	 * @param criteros para filtrar el select, campo where 
	 *  @return vector con los registros encontrados. El objeto es de tipo administrador del bean 
	 * */
	public Vector busquedaGrupofijo(String idInstitucion, String idGrupo,String lenguaje) throws ClsExceptions 
	{
		Vector datos = new Vector();
		String select = null;
		
		try {
			select  = " SELECT ";
			select += " F_SIGA_GETRECURSO(grupo."+CenGruposClienteBean.C_NOMBRE+", "+lenguaje+") "+CenGruposClienteBean.C_NOMBRE ;
			
			select += " , grupo."+CenGruposClienteBean.C_IDGRUPO;
			select += " , grupo."+CenGruposClienteBean.C_IDINSTITUCION;
			//FROM:
			select += " FROM "+CenGruposClienteBean.T_NOMBRETABLA+" grupo";

			//Filtro:
			select += " WHERE grupo."+CenGruposClienteBean.C_IDINSTITUCION+"="+idInstitucion;
			select += " AND grupo."+CenGruposClienteBean.C_IDGRUPO+"="+idGrupo;
			
			//Consulta:
			datos = this.selectGenerico(select);			
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {
		return this.getClavesBean();
	}
	/**
	 * Insertar en un vector cada fila como una tabla hash del resultado de ejecutar la query
	 * @param Hashtable miHash: tabla hash de datos necesarios para la consulta SQL.
	 * @param String consulta: consulta SQL del SELECT almacenada en un String.
	 * @return Vector con tablas hash. Cada tabla hash es una fila del resultado del select en la base de datos.
	 * @throws ClsExceptions
	 */
	public Vector selectGenerico(String consulta) throws ClsExceptions {
		Vector datos = new Vector();
		
		// Acceso a BBDD	
		try { 
			RowsContainer rc = new RowsContainer(); 	
			if (rc.queryNLS(consulta)) {
				for (int i = 0; i < rc.size(); i++)	{		
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow();
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en CenGruposClientesAdm.selectGenerico(). Consulta SQL:"+consulta);
		}
		return datos;	
	}
	
	/**
	 * Comprueba si ya existe un procurador con el mismo nombre y apellidos
	 * Si la institución es el CGAE comprueba la duplicidad para todas las instituciones
	 * Si es otra, la comprueba para esa institución y para el CGAE
	 * @param idInstitucion
	 * @param nombre
	 * @param apellido1
	 * @param apellido2
	 * @return true si no existe ya en el sistema
	 * @throws ClsExceptions
	 */	
	public static boolean comprobarDuplicidad(String idInstitucion, String nombre)
	throws ClsExceptions{
		boolean sinDuplicar=true;
		Vector existe=null;
	    Hashtable codigos = new Hashtable();
	    codigos.put(new Integer(1),idInstitucion);
	    codigos.put(new Integer(2),nombre);
		String sql=
			"select pkg_siga_valida_mto_3.FUN_SCS_GRUPOS(:1,:2) VALOR from DUAL";
			
		RowsContainer rc= new RowsContainer();
		if(rc.findBind(sql,codigos)){
		    Vector v = rc.getAll();
		    if (v!=null&&v.size()>0) { 
		        sinDuplicar=(ClsConstants.DB_TRUE.equals((String)((Row)v.get(0)).getString("VALOR")));
		    }
		}else{
			ClsLogging.writeFileLog(sql, 10);
			throw new ClsExceptions("error al acceder a la función de validación");
		}
		return sinDuplicar;
	}
	
	public Integer getNuevoIdGrupo(String idInstitucion) throws ClsExceptions {
		Vector datos = new Vector();
		String select = null;
		Integer nuevoId;
		
		try {
			select  = "SELECT MAX("+CenGruposClienteBean.C_IDGRUPO+")+1 AS ID FROM "+CenGruposClienteBean.T_NOMBRETABLA+
					  " WHERE "+CenGruposClienteBean.C_IDINSTITUCION+"="+idInstitucion;			
			
			datos = this.selectGenerico(select);
			String id = (String)((Hashtable)datos.get(0)).get("ID");
			
			if ( (datos == null) || (id!= null && id.equals("")) )
				nuevoId = new Integer("0");
			else
				nuevoId = new Integer(id);

		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return nuevoId;
	}
	private String getConsultaExperta(Integer idGrupoFijo){
		StringBuilder builder = new StringBuilder();
		builder.append(" <SELECT> ");
		builder.append(" SELECT CEN_CLIENTE.IDINSTITUCION         AS \"IDINSTITUCION\", ");
		builder.append(" CEN_CLIENTE.IDPERSONA             AS \"IDPERSONA\", ");
		builder.append(" CEN_DIRECCIONES.CODIGOPOSTAL      AS \"CODIGOPOSTAL\", ");
		builder.append(" CEN_DIRECCIONES.CORREOELECTRONICO AS \"CORREOELECTRONICO\", ");
		builder.append(" CEN_DIRECCIONES.DOMICILIO         AS \"DOMICILIO\", ");
		builder.append(" CEN_DIRECCIONES.MOVIL             AS \"MOVIL\", ");
		builder.append(" CEN_DIRECCIONES.FAX1              AS \"FAX1\", ");
		builder.append(" CEN_DIRECCIONES.FAX2              AS \"FAX2\", ");
		builder.append(" CEN_DIRECCIONES.IDPAIS            AS \"IDPAIS\", ");
		builder.append(" CEN_DIRECCIONES.IDPROVINCIA       AS \"IDPROVINCIA\", ");
		builder.append(" CEN_DIRECCIONES.IDPOBLACION       AS \"IDPOBLACION\" ");
		builder.append(" </SELECT> ");
		builder.append(" <FROM> ");
		builder.append(" FROM CEN_CLIENTE, CEN_DIRECCIONES, CEN_GRUPOSCLIENTE_CLIENTE ");
		builder.append(" </FROM> ");
		builder.append(" <WHERE> ");
		builder.append(" WHERE CEN_CLIENTE.IDPERSONA = CEN_DIRECCIONES.IDPERSONA ");
		builder.append(" AND CEN_CLIENTE.IDINSTITUCION = CEN_DIRECCIONES.IDINSTITUCION ");
		builder.append(" AND CEN_GRUPOSCLIENTE_CLIENTE.IDINSTITUCION = CEN_CLIENTE.IDINSTITUCION ");
		builder.append(" AND CEN_GRUPOSCLIENTE_CLIENTE.IDPERSONA = CEN_CLIENTE.IDPERSONA ");
		builder.append(" AND CEN_CLIENTE.IDINSTITUCION = %%IDINSTITUCION%% ");
		builder.append(" AND CEN_GRUPOSCLIENTE_CLIENTE.IDGRUPO =  ");
		builder.append(idGrupoFijo);
		builder.append(" </WHERE> ");
		return builder.toString();
	}
	public void insertar(CenGruposClienteBean beanGrupos,boolean isInsertarListaCorreo)throws BusinessException{
		 
		
		UserTransaction tx = null;
		
		try {
			tx = this.usrbean.getTransaction();
			String nombreTabla = CenGruposClienteBean.T_NOMBRETABLA;
			String nombreCampoDescripcion = CenGruposClienteBean.C_NOMBRE;
			String idInstitucion = beanGrupos.getIdInstitucion().toString();
			String nombre = beanGrupos.getNombre()+" ("+UtilidadesString.getMensajeIdioma(usrbean,"general.automatico")+")";
			Integer idGrupoFijo = this.getNuevoIdGrupo(idInstitucion);
			beanGrupos.setIdGrupo(idGrupoFijo);
			String idRecurso = GenRecursosCatalogosAdm.getNombreIdRecurso(nombreTabla, nombreCampoDescripcion, beanGrupos.getIdInstitucion(), idGrupoFijo.toString());
			if (idRecurso == null)
				throw new BusinessException("error.messages.sinConfiguracionMultiIdioma");
			

			Hashtable htPkTabl = new Hashtable();
			htPkTabl.put(CenGruposClienteBean.C_IDGRUPO, idGrupoFijo);
			Hashtable htSignos = new Hashtable();
			htSignos.put(CenGruposClienteBean.C_IDGRUPO, "<>");
			
			boolean isClaveUnicaMultiIdioma = UtilidadesBDAdm.isClaveUnicaMultiIdioma(idInstitucion, beanGrupos.getNombre(), nombreCampoDescripcion, htPkTabl, htSignos, nombreTabla, 4, usrbean.getLanguage());

			if (isClaveUnicaMultiIdioma) {
				tx.begin();

				String idRecursoAlias = GenRecursosCatalogosAdm.getNombreIdRecursoAlias(nombreTabla, nombreCampoDescripcion,beanGrupos.getIdInstitucion(), idGrupoFijo.toString());
				GenRecursosCatalogosAdm admRecCatalogos = new GenRecursosCatalogosAdm(this.usrbean);
				GenRecursosCatalogosBean recCatalogoBean = new GenRecursosCatalogosBean();
				recCatalogoBean.setCampoTabla(nombreCampoDescripcion);
				recCatalogoBean.setDescripcion(beanGrupos.getNombre());
				recCatalogoBean.setIdInstitucion(beanGrupos.getIdInstitucion());
				recCatalogoBean.setIdRecurso(idRecurso);
				recCatalogoBean.setIdRecursoAlias(idRecursoAlias);
				recCatalogoBean.setNombreTabla(nombreTabla);
				//Insertmos los catalogos multidioma
				if (!admRecCatalogos.insert(recCatalogoBean, usrbean.getLanguageInstitucion()))
					throw new BusinessException("messages.inserted.error");
				//Insertmos el grupo
				beanGrupos.setNombre(idRecurso.toString());
				insert(beanGrupos);
				if(isInsertarListaCorreo){
					//Insertamos la lista de correo
					EnvListaCorreosAdm envListaCorreosAdm = new EnvListaCorreosAdm(usrbean);
				    EnvListaCorreosBean envListaCorreosBean = new EnvListaCorreosBean();	    
				    Integer idListaCorreo =  envListaCorreosAdm.getNewIdListaCorreos(usrbean);	    
				    envListaCorreosBean.setIdInstitucion(beanGrupos.getIdInstitucion());
				    envListaCorreosBean.setIdListaCorreo(idListaCorreo);
				    envListaCorreosBean.setNombre(nombre);
				    envListaCorreosBean.setDescripcion(UtilidadesString.getMensajeIdioma(usrbean,"censo.gestion.listacorreo.descripcion.automatica"));
				    envListaCorreosBean.setDinamica("S");		    
				    envListaCorreosAdm.insert(envListaCorreosBean);
					
			        //Insertamos la consulta experta
			        ConConsultaAdm conConsultaAdm = new ConConsultaAdm(usrbean);
					ConConsultaBean conConsultaBean = new ConConsultaBean();
					String idConsulta = String.valueOf(conConsultaAdm.getNewIdConsulta(idInstitucion));
					String sentencia = getConsultaExperta(idGrupoFijo);
				    conConsultaBean.setDescripcion(nombre);
					conConsultaBean.setGeneral(ConConsultaAdm.CONS_GENERAL_NO);
			    	conConsultaBean.setIdModulo(new Integer(4));//Envios a grupos
			    	conConsultaBean.setIdTabla(null);
				    conConsultaBean.setIdInstitucion(beanGrupos.getIdInstitucion());
				    conConsultaBean.setIdConsulta(Long.valueOf(idConsulta));
				    conConsultaBean.setTipoConsulta("E");
				    conConsultaBean.setSentencia(sentencia);
				    conConsultaBean.setEsExperta(ClsConstants.DB_TRUE);
				    conConsultaAdm.insert(conConsultaBean);
			        
				    //Asociamos la consulta experta a la lista de correo
				    EnvListaCorreoConsultaAdm envListaCorreoConsultaAdm = new EnvListaCorreoConsultaAdm(usrbean);
				    //Rellenamos el nuevo Bean de componente lista de correos
				    EnvListaCorreoConsultaBean envListaCorreoConsultaBean = new EnvListaCorreoConsultaBean();	    
				    envListaCorreoConsultaBean.setIdInstitucion(beanGrupos.getIdInstitucion());
				    envListaCorreoConsultaBean.setIdListaCorreo(idListaCorreo);
				    envListaCorreoConsultaBean.setIdConsulta(Long.valueOf(idConsulta));
				    envListaCorreoConsultaBean.setIdInstitucionCon(beanGrupos.getIdInstitucion());
			    	envListaCorreoConsultaAdm.insert(envListaCorreoConsultaBean);
					
			    	
			    	ConConsultaPerfilAdm cpAdm = new ConConsultaPerfilAdm (usrbean);
			    	ConConsultaPerfilBean cpBean = new ConConsultaPerfilBean();
			    	cpBean.setIdConsulta(Long.valueOf(idConsulta));
			    	cpBean.setIdInstitucion(beanGrupos.getIdInstitucion());
			    	cpBean.setIdInstitucion_Consulta(beanGrupos.getIdInstitucion());
			    	cpBean.setIdPerfil("ADG");
     	            cpAdm.insert(cpBean);
			    	
				}
				
				
				tx.commit();

			} else {
				throw new BusinessException("gratuita.mantenimientoTablasMaestra.mensaje.grupoFijoDuplicado");
			}
		}catch (BusinessException e) {
			try {tx.rollback();} catch (Exception e1) {}
			throw e;
		} catch (Exception e) {
			try {tx.rollback();} catch (Exception e1) {}
			throw new BusinessException("messages.inserted.error");
		}
		
		
	}
}

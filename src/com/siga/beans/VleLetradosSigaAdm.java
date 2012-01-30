
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.Utilidades.paginadores.PaginadorBind;
import com.siga.censo.form.BusquedaCensoForm;
import com.siga.censo.form.DatosGeneralesForm;
import com.siga.general.SIGAException;


/**
 * Administrador de Cliente
 * 
 * @author daniel.campos
 * @since 22-11-2004
 * @version miguel.villegas - 10-01-2005 - incorporacion 
 *  getNumColegiosEsCliente () y eliminarNoColegiado ()
 * @version raul.ggonzalez - 11-01-2005 - incorporacion 
 *  getDatosPersonalesCliente () y getGrupos ()
 * @version luismiguel.sanchezp - 22-03-2005 - Se anhade el método 
 *  getDatosCertificado ()
 * @version david.sanchezp - 27-12-2005 - incluir el campo sexo
 * @version RGG - CAMBIO VISIBILIDAD 
 *  public class CenClienteAdm extends MasterBeanAdministrador {
 * @version adrian.ayala - 05-06-2008 - Limpieza de altaColegial ()
 */
public class VleLetradosSigaAdm extends MasterBeanAdmVisible
{
	//////////////////// CONSTRUCTORES ////////////////////
	public VleLetradosSigaAdm (UsrBean usuario) {
		super (VleLetradosSigaBean.T_NOMBRETABLA, usuario);
	}
	
	public VleLetradosSigaAdm (Integer usuario, UsrBean usrbean, 
			  int idInstitucionCliente, long idPersonaCliente)
	{
		super (VleLetradosSigaBean.T_NOMBRETABLA, usuario, usrbean, 
			idInstitucionCliente, idPersonaCliente);
	}
	
	
	
	//////////////////// METODOS DE ADMINISTRADOR ////////////////////
	protected String[] getCamposBean ()
	{
		String [] campos = 
		{
				VleLetradosSigaBean.C_APELLIDOS,
				VleLetradosSigaBean.C_CODPOSTAL,
				VleLetradosSigaBean.C_DIRPROFESIONAL,
				VleLetradosSigaBean.C_IDPERSONA,
				VleLetradosSigaBean.C_MAIL,
				VleLetradosSigaBean.C_NOMBRE,
				VleLetradosSigaBean.C_NUMDOC,
				VleLetradosSigaBean.C_POBLACION,
				VleLetradosSigaBean.C_PROVINCIA,
				VleLetradosSigaBean.C_TELEFONO,
				VleLetradosSigaBean.C_FECHAMODIFICACION,
				VleLetradosSigaBean.C_USUMODIFICACION

		};
		return campos;
	} //getCamposBean ()
	
	protected String[] getClavesBean ()
	{
		String [] claves = 
		{
				VleLetradosSigaBean.C_IDPERSONA
		};
		return claves;
	} //getClavesBean ()

	protected String[] getOrdenCampos () {
		return getClavesBean ();
	} //getOrdenCampos ()

	protected MasterBean hashTableToBean (Hashtable hash)
			throws ClsExceptions
	{
		VleLetradosSigaBean bean = null;
		
		try
		{
			bean = new VleLetradosSigaBean ();

			bean.setId_letrado (UtilidadesHash.getString (hash, VleLetradosSigaBean.C_IDPERSONA));
			bean.setNombre (UtilidadesHash.getString (hash, VleLetradosSigaBean.C_NOMBRE));
			bean.setApellidos (UtilidadesHash.getString (hash, VleLetradosSigaBean.C_APELLIDOS));
			bean.setNum_doc (UtilidadesHash.getString (hash, VleLetradosSigaBean.C_NUMDOC));
			bean.setDir_profesional (UtilidadesHash.getString (hash, VleLetradosSigaBean.C_DIRPROFESIONAL));
			bean.setPoblacion (UtilidadesHash.getString (hash, VleLetradosSigaBean.C_POBLACION));
		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	} //hashTableToBean ()

	protected Hashtable beanToHashTable (MasterBean bean)
			throws ClsExceptions
	{
		Hashtable htData = null;
		
		try
		{
			htData = new Hashtable ();
			VleLetradosSigaBean b = (VleLetradosSigaBean) bean;
			UtilidadesHash.set (htData, VleLetradosSigaBean.C_IDPERSONA,			b.getId_letrado());
			UtilidadesHash.set (htData, VleLetradosSigaBean.C_NOMBRE,		b.getNombre());
			UtilidadesHash.set (htData, VleLetradosSigaBean.C_APELLIDOS,			b.getApellidos());
			UtilidadesHash.set (htData, VleLetradosSigaBean.C_NUMDOC,		b.getNum_doc());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	} //beanToHashTable ()

	
		/**
	 * Devuelve un vector con las direcciones del cliente pasado como parámetro 
	 * @author nuria.rgonzalez 18-01-05
	 * @version 3	 
	 * @param idPersona, es el identificador de la persona de al que vamos a obtener los datos. 
	 * @param idInstitucion, es el identificador de la institucion de la persona de al que vamos a obtener los datos. 
	 */	
	public Vector getDirecciones(Long idPersona, Integer idInstitucion, boolean bIncluirRegistrosConBajaLogica) throws ClsExceptions, SIGAException
	{
		Vector vector=null;
		try{
			CenDireccionesAdm direccionesAdm = null; 
			if (this.usrbean!=null) {
				direccionesAdm = new CenDireccionesAdm(this.usuModificacion,this.usrbean,this.idInstitucionCliente, this.idPersonaCliente);
			} else {
				direccionesAdm = new CenDireccionesAdm(this.usrbean);
			}
			vector=direccionesAdm.selectDirecciones(idPersona,idInstitucion, bIncluirRegistrosConBajaLogica);
		}
		catch (SIGAException e) { 
			throw e; 	
		}
		catch(Exception e) {
			throw new ClsExceptions (e, "Error al obtener Direcciones");
		}
		return vector;
	}
	
	/**
	 * Devuelve un Hastable con la direccion del cliente pasado como parámetro 
	 * @author nuria.rgonzalez 18-1-05
	 * @version 3	 
	 * @param idPersona, es el identificador de la persona de al que vamos a obtener los datos. 
	 * @param idInstitucion, es el identificador de la institucion de la persona de al que vamos a obtener los datos. 
	 * @param idDireccion, es el identificador de la direccion de la que queremos obtener los datos. 
	 */
	public Hashtable getDirecciones(Long idPersona, Integer idInstitucion, Long idDireccion) throws ClsExceptions, SIGAException{
		return getDirecciones(idPersona, idInstitucion, idDireccion, false);
	}

	public Hashtable getDirecciones(Long idPersona, Integer idInstitucion, Long idDireccion, boolean permitirNulos) throws ClsExceptions, SIGAException{
		//Hashtable hash=new Hashtable();
		Hashtable hash=null;
		try{
			CenDireccionesAdm direccionesAdm = null; 
			if (this.usrbean!=null) {
				direccionesAdm = new CenDireccionesAdm(this.usuModificacion,this.usrbean,this.idInstitucionCliente, this.idPersonaCliente);
			} else {
				direccionesAdm = new CenDireccionesAdm(this.usrbean);
			}
			hash=direccionesAdm.selectDirecciones(idPersona,idInstitucion,idDireccion, permitirNulos);
		}
		catch (SIGAException e) { 
			throw e; 	
		}
		catch(Exception e) {
			throw new ClsExceptions (e, "Error al obtener Direcciones");
		}
		return hash;
	}
	
	/**
	 * Devuelve un vector con las cuentas bancarias del cliente pasado como parámetro 
	 * @author nuria.rgonzalez 18-01-05
	 * @version 3	 
	 * @param idPersona, es el identificador de la persona de al que vamos a obtener los datos. 
	 * @param idInstitucion, es el identificador de la institucion de la persona de al que vamos a obtener los datos. 
	 */	

	
	public PaginadorBind getClientesCenso (String idInstitucionBuscar, String idInstitucionActual, BusquedaCensoForm formulario, String idioma) throws ClsExceptions, SIGAException {
		Vector clientes = new Vector();
		String sqlClientes = "";
		int contador = 0;
		Hashtable codigos = new Hashtable();

		try {
			sqlClientes = "SELECT C.id_letrado, C.id_colegio, C.descripcion, C.num_colegiado, " +
					"C.residencia," +
					"C.ejerciente," +
					//"decode(C.residencia, 'n', f_siga_getrecurso_etiqueta('general.no', "+idioma+"), f_siga_getrecurso_etiqueta('general.yes', "+idioma+"))   as residencia," +
					//"decode(C.ejerciente, 'n', f_siga_getrecurso_etiqueta('general.no', "+idioma+"), f_siga_getrecurso_etiqueta('general.yes', "+idioma+"))   as ejerciente," +
					" L.nombre, L.fax as fax1, " +
					"L.apellido1,L.apellido2,L.dir_profesional,L.num_doc, L.poblacion, L.idpoblacion, L.idprovincia as provincia, L.IDPAIS as pais, L.cod_postal,L.telefono,L.mail,  TO_CHAR(C.fecha_alta, 'dd/MM/yyyy') AS fecha_alta, C.tratamiento, L.sexo  "+
			"from  V_CENSO_COLEGIACIONES C, V_CENSO_LETRADOS L where C.id_letrado=L.id_letrado  and  ";
		       if (idInstitucionBuscar!=null && !idInstitucionBuscar.trim().equals("")) {
			       	contador++;
					codigos.put(new Integer(contador),idInstitucionBuscar.trim());
					sqlClientes += "  C.id_colegio = :"+contador;
			   } else {
				   	contador++;
				   	codigos.put(new Integer(contador),idInstitucionActual.trim());
					sqlClientes += "  C.id_colegio <> :"+contador;
			   }
		       
				boolean bBusqueda  = UtilidadesString.stringToBoolean(formulario.getChkBusqueda());	
//				 2     
							
	          if (formulario.getNumeroColegiado()!=null && !formulario.getNumeroColegiado().trim().equals("")) {
	          	contador++;
	          	codigos.put(new Integer(contador),formulario.getNumeroColegiado().trim());
	          	sqlClientes += " AND LTRIM(C.num_colegiado,'0') = LTRIM(:"+contador+",'0') " ;	

	          } 

//				  	 3
	  	       if (formulario.getNombre() != null && !formulario.getNombre().trim().equals("")) {
	  		       	if (bBusqueda) {
	  		       		contador++;
	  		       	sqlClientes += " AND (UPPER(L.nombre) "+ComodinBusquedas.prepararSentenciaExactaBind(formulario.getNombre().trim(),contador,codigos)+") ";
	  					  
	  		       	}else{
	  		       	contador++;
	  		       	sqlClientes += " AND ("+ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getNombre().trim(),"L.nombre",contador,codigos )+") ";
	  		       	}
	  	       }
	  	       
//				  	     4 y 5 Criterio de busqueda especial para los campos Apellido1 y Apellido2
	  	       if ((formulario.getApellido1()!= null && !formulario.getApellido1().trim().equals(""))&&(formulario.getApellido2()!= null && !formulario.getApellido2().trim().equals(""))) {// Los dos campos rellenos
		  	       	if (bBusqueda)  {
		  	       		contador++;
		  	       		sqlClientes += " AND (((L.apellido1 "+ComodinBusquedas.prepararSentenciaExactaBind(formulario.getApellido1().trim(),contador,codigos )+")";
		  	       	    contador++;
	  	       		    sqlClientes += " AND (L.apellido2 "+ComodinBusquedas.prepararSentenciaExactaBind(formulario.getApellido2().trim(),contador,codigos )+"))";
	  	       		    contador++;
		       		    sqlClientes += " OR (L.apellido1 "+ComodinBusquedas.prepararSentenciaExactaBind(formulario.getApellido1().trim()+" "+formulario.getApellido2().trim(),contador,codigos )+"))";
		  	       	}else{
		  	       		contador++;
		  	       		sqlClientes += " AND ((("+ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getApellido1().trim(),"L.apellido1",contador,codigos )+")";
			  	       	contador++;
		  	       		sqlClientes += " AND ("+ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getApellido2().trim(),"L.apellido2",contador,codigos)+"))";
		  	       	    contador++;
			       		sqlClientes += " OR ("+ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getApellido1().trim()+"% %"+formulario.getApellido2().trim() ,"L.apellido1",contador,codigos )+"))";
		  	       	}
		  	       	
	  	       }else if (formulario.getApellido1()!= null && !formulario.getApellido1().trim().equals("")) {//Apellido1 relleno
		  	       	if (bBusqueda) {
		  	       		contador++;
		  	       		sqlClientes += " AND ((L.apellido1 "+ComodinBusquedas.prepararSentenciaExactaBind(formulario.getApellido1(),contador,codigos)+") OR (L.apellido1 LIKE '"+UtilidadesBDAdm.validateChars(formulario.getApellido1().trim())+" %'))";
		  	       	}else{
		  	       		contador++;	
		  	       		sqlClientes += " AND ("+ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getApellido1().trim(),"L.apellido1",contador,codigos )+" OR (L.apellido1||' '||L.apellido2 LIKE '%"+UtilidadesBDAdm.validateChars(formulario.getApellido1().trim())+"%')) ";
		  	       	}
		  	       	
	  	       }else if (formulario.getApellido2()!= null && !formulario.getApellido2().trim().equals("")) {//Apellido2 relleno
		  	       	if (bBusqueda ) {
		  	       		contador++;
		  	       		sqlClientes += " AND ((L.apellido2 "+ComodinBusquedas.prepararSentenciaExactaBind(formulario.getApellido2().trim(),contador,codigos )+")"+
		  				               " OR (L.apellido1 LIKE '% "+UtilidadesBDAdm.validateChars(formulario.getApellido2().trim()) +"' )) ";
		  	       	}else{
		  	       		contador++;
		  	       		sqlClientes += " AND (("+ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getApellido2().trim(),"L.apellido2",contador,codigos)+") ";
		  	       		contador++;	
		  	       	    sqlClientes += " OR ("+ComodinBusquedas.prepararSentenciaCompletaBind(" "+formulario.getApellido2().trim(),"L.apellido1",contador,codigos)+ ")  )";
		  	       	}
	  	       }      
				  	   				       

		    sqlClientes+= " ORDER BY L.apellido1||' '||L.apellido2, L.nombre, C.descripcion";	       
			PaginadorBind paginado = new PaginadorBind(sqlClientes, codigos);

			int totalRegistros = paginado.getNumeroTotalRegistros();
			if (totalRegistros == 0) {
				paginado = null;
			}

			return paginado;
		}
		catch (Exception e) {
			throw new ClsExceptions(e, "Error obteniendo clientes ");
		}
		
	}
	public CenClienteBean existeClienteOtraInstitucionCenso (Long idPersona, Integer idInstitucion) throws ClsExceptions, SIGAException 
	{
		try {
			CenClienteBean salida = null;
			Hashtable codigos = new Hashtable();
		    codigos.put(new Integer(1),idPersona.toString());
    		codigos.put(new Integer(2),idInstitucion.toString());
		    Vector v = this.selectBind(" WHERE IDPERSONA=:1" + " AND IDINSTITUCION=:2",codigos);
			if (v!=null && v.size()>0) {
				salida = (CenClienteBean) v.get(0);
			}
			return salida;
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al consultar datos en B.D.");
		}
	}
	
	public PaginadorBind getClientesCensoArticulo27 (String idInstitucionBuscar, String idInstitucionActual, BusquedaCensoForm formulario, String idioma) throws ClsExceptions, SIGAException {
		Vector clientes = new Vector();
		String sqlClientes = "";
		int contador = 0;
		Hashtable codigos = new Hashtable();

		try {
			sqlClientes = "SELECT C.id_letrado, C.id_colegio, C.num_colegiado, " +
							"C.residencia, C.ejerciente,c.descripcion, C.tratamiento, " +
							" L.nombre, L.apellido1,L.apellido2,L.num_doc,L.sexo, TO_CHAR(C.fecha_alta, 'dd/MM/yyyy') AS fecha_alta, "+
							" L.DIR_PROFESIONAL, L.COD_POSTAL, L.IDPAIS pais, L.IDPROVINCIA provincia, L.IDPOBLACION idpoblacion, L.poblacion POBLACION, L.TELEFONO, L.FAX, L.MAIL "+
						  "FROM  V_CENSO_COLEGIACIONES C, V_CENSO_LETRADOS L where C.id_letrado=L.id_letrado ";
			
		       if (idInstitucionBuscar!= null && !idInstitucionBuscar.trim().equals("")) {
		    	   if(!idInstitucionBuscar.trim().equals(idInstitucionActual)){
		    		   //Se buscan los colegiados del colegio seleccionado
			       		contador++;
			       		codigos.put(new Integer(contador),idInstitucionBuscar.trim());
			       		sqlClientes += "    AND C.id_colegio = :"+contador;
		    	   }else{
		    		   //Se buscan los colegiados no ejercientes del colegio actual
		    		   	sqlClientes += "    AND C.id_colegio = "+idInstitucionActual+" AND C.ejerciente = 'n' ";
		    	   }
			   } else {
				   		//Se busca colegiados de otros colegios y colegiados no ejercientes del actual
		    			sqlClientes += "    AND ((C.id_colegio <> "+idInstitucionActual+") OR "+
					    " (C.id_colegio = "+idInstitucionActual+" AND C.ejerciente = 'n')) ";  
			   }
		       
				boolean bBusqueda  = false; //UtilidadesString.stringToBoolean(formulario.getChkBusqueda());	
				
	          if (formulario.getNumeroColegiado()!=null && !formulario.getNumeroColegiado().trim().equals("")) {
		          	contador++;
		          	codigos.put(new Integer(contador),formulario.getNumeroColegiado().trim());
		          	sqlClientes += " AND LTRIM(C.num_colegiado,'0') = LTRIM(:"+contador+",'0') " ;	
	          } 

	  	       if (formulario.getNombre() != null && !formulario.getNombre().trim().equals("")) {
	  		       	if (bBusqueda) {
	  		       		contador++;
	  		       	sqlClientes += " AND (UPPER(L.nombre) "+ComodinBusquedas.prepararSentenciaExactaBind(formulario.getNombre().trim(),contador,codigos)+") ";
	  					  
	  		       	}else{
	  		       	contador++;
	  		       	sqlClientes += " AND ("+ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getNombre().trim(),"L.nombre",contador,codigos )+") ";
	  		       	}
	  	       }
	  	       
	  	       if ((formulario.getApellido1()!= null && !formulario.getApellido1().trim().equals("")) && (formulario.getApellido2()!= null && !formulario.getApellido2().trim().equals(""))) {// Los dos campos rellenos
		  	       	if (bBusqueda)  {
		  	       		contador++;
		  	       		sqlClientes += " AND (((L.apellido1 "+ComodinBusquedas.prepararSentenciaExactaBind(formulario.getApellido1().trim(),contador,codigos )+")";
		  	       	    contador++;
	  	       		    sqlClientes += " AND (L.apellido2 "+ComodinBusquedas.prepararSentenciaExactaBind(formulario.getApellido2().trim(),contador,codigos )+"))";
	  	       		    contador++;
		       		    sqlClientes += " OR (L.apellido1 "+ComodinBusquedas.prepararSentenciaExactaBind(formulario.getApellido1().trim()+" "+formulario.getApellido2().trim(),contador,codigos )+"))";
			  	     }else{
		  	    	  	contador++;
		  	       		sqlClientes += " AND ((("+ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getApellido1().trim(),"L.apellido1",contador,codigos )+")";
		  	       		
			  	       	contador++;
		  	       		sqlClientes += " AND ("+ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getApellido2().trim(),"L.apellido2",contador,codigos)+"))";
		  	       	    contador++;
			       		sqlClientes += " OR ("+ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getApellido1().trim()+"% %"+formulario.getApellido2().trim() ,"L.apellido1",contador,codigos )+"))";
		  	       	}

	  	       }else if (formulario.getApellido1()!= null && !formulario.getApellido1().trim().equals("")) {//Apellido1 relleno
		  	       	if (bBusqueda  ) {
		  	       		contador++;
		  	       		sqlClientes += " AND ((L.apellido1 "+ComodinBusquedas.prepararSentenciaExactaBind(formulario.getApellido1(),contador,codigos)+") OR (L.apellido1 LIKE '"+UtilidadesBDAdm.validateChars(formulario.getApellido1().trim())+" %'))";
		  	       	}else{
		  	       		contador++;	
		  	       		sqlClientes += " AND ("+ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getApellido1().trim(),"L.apellido1",contador,codigos )+" OR (L.apellido1||' '||L.apellido2 LIKE '%"+UtilidadesBDAdm.validateChars(formulario.getApellido1().trim())+"%')) ";
		  	       		
		  	       	}
		  	       	
	  	       }else if (formulario.getApellido2()!= null && !formulario.getApellido2().trim().equals("")) {//Apellido2 relleno
	  	    	   if (bBusqueda ) {
	  	    		   contador++;
	  	    		   sqlClientes += " AND ((L.apellido2 "+ComodinBusquedas.prepararSentenciaExactaBind(formulario.getApellido2().trim(),contador,codigos )+")"+
	  	       			" OR (L.apellido1 LIKE '% "+UtilidadesBDAdm.validateChars(formulario.getApellido2().trim()) +"' )) ";
	  	       		 
		  	       	}else{
		  	       		contador++;
		  	       		sqlClientes += " AND (("+ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getApellido2().trim(),"L.apellido2",contador,codigos)+") ";
		  	       		contador++;	
		  	       	    sqlClientes += " OR ("+ComodinBusquedas.prepararSentenciaCompletaBind(" "+formulario.getApellido2().trim(),"L.apellido1",contador,codigos)+ ")  )";
		  	       	}
	  	       }
		  	          	  	       			       
		  	   if (formulario.getNumeroColegiado() == null || formulario.getNumeroColegiado().equals("")) {
				 if (idInstitucionBuscar == null || idInstitucionBuscar.trim().equals("") || idInstitucionBuscar.trim().equals(idInstitucionActual)) {
					sqlClientes += " UNION " +

					" SELECT nc.id_letrado, nc.id_colegio, 'No Colegiado', 'No Colegiado', 'No Colegiado',nc.descripcion,nc.idtratamiento, "
					+"		 nc.nombre, nc.apellido1, nc.apellido2, nc.num_doc,nc.sexo,'-', "
					+" 		 '-', '-', '-', '-', '-','-', '-', '-', '-' "
					+" FROM V_CENSO_NOCOLEGIADOS nc " + " WHERE 1 = 1 ";
					if (idInstitucionActual != null && !idInstitucionActual.trim().equals("")) {
						contador++;
						codigos.put(new Integer(contador), idInstitucionActual.trim());
						sqlClientes += "    AND nc.id_colegio = :" + contador;
					}
					if (formulario.getNombre() != null && !formulario.getNombre().trim().equals("")) {
						if (bBusqueda) {
							contador++;
							sqlClientes += " AND (UPPER(nc.nombre) "
									+ ComodinBusquedas.prepararSentenciaExactaBind(formulario.getNombre().trim(), contador, codigos) + ") ";

						} else {
							contador++;
							sqlClientes += " AND ("
									+ ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getNombre().trim(), "nc.nombre", contador, codigos)
									+ ") ";
						}
					}
					if ((formulario.getApellido1() != null && !formulario.getApellido1().trim().equals(""))
							&& (formulario.getApellido2() != null && !formulario.getApellido2().trim().equals(""))) {// Los dos campos rellenos
						if (bBusqueda) {
							contador++;
							sqlClientes += " AND (((nc.apellido1 "
									+ ComodinBusquedas.prepararSentenciaExactaBind(formulario.getApellido1().trim(), contador, codigos) + ")";
							contador++;
							sqlClientes += " AND (nc.apellido2 "
									+ ComodinBusquedas.prepararSentenciaExactaBind(formulario.getApellido2().trim(), contador, codigos) + "))";
							contador++;
							sqlClientes += " OR (nc.apellido1 "
									+ ComodinBusquedas.prepararSentenciaExactaBind(formulario.getApellido1().trim() + " "
											+ formulario.getApellido2().trim(), contador, codigos) + "))";
						} else {
							contador++;
							sqlClientes += " AND ((("
									+ ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getApellido1().trim(), "nc.apellido1", contador,
											codigos) + ")";

							contador++;
							sqlClientes += " AND ("
									+ ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getApellido2().trim(), "nc.apellido2", contador,
											codigos) + "))";
							contador++;
							sqlClientes += " OR ("
									+ ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getApellido1().trim() + "% %"
											+ formulario.getApellido2().trim(), "nc.apellido1", contador, codigos) + "))";
						}

					} else if (formulario.getApellido1() != null && !formulario.getApellido1().trim().equals("")) {//Apellido1 relleno
						if (bBusqueda) {
							contador++;
							sqlClientes += " AND ((nc.apellido1 "
									+ ComodinBusquedas.prepararSentenciaExactaBind(formulario.getApellido1(), contador, codigos)
									+ ") OR (nc.apellido1 LIKE '" + UtilidadesBDAdm.validateChars(formulario.getApellido1().trim()) + " %'))";
						} else {
							contador++;
							sqlClientes += " AND ("
									+ ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getApellido1().trim(), "nc.apellido1", contador,
											codigos) + " OR (nc.apellido1||' '||nc.apellido2 LIKE '%"
									+ UtilidadesBDAdm.validateChars(formulario.getApellido1().trim()) + "%')) ";

						}

					} else if (formulario.getApellido2() != null && !formulario.getApellido2().trim().equals("")) {//Apellido2 relleno
						if (bBusqueda) {
							contador++;
							sqlClientes += " AND ((nc.apellido2 "
									+ ComodinBusquedas.prepararSentenciaExactaBind(formulario.getApellido2().trim(), contador, codigos) + ")"
									+ " OR (nc.apellido1 LIKE '% " + UtilidadesBDAdm.validateChars(formulario.getApellido2().trim()) + "' )) ";

						} else {
							contador++;
							sqlClientes += " AND (("
									+ ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getApellido2().trim(), "nc.apellido2", contador,
											codigos) + ") ";
							contador++;
							sqlClientes += " OR ("
									+ ComodinBusquedas.prepararSentenciaCompletaBind(" " + formulario.getApellido2().trim(), "nc.apellido1",
											contador, codigos) + ")  )";
						}
					}
				}
			}
			sqlClientes+= "  ORDER BY apellido1 , apellido2, nombre";	       
			PaginadorBind paginado = new PaginadorBind(sqlClientes, codigos);

			int totalRegistros = paginado.getNumeroTotalRegistros();
			if (totalRegistros == 0) {
				paginado = null;
			}

			return paginado;
		}
		catch (Exception e) {
			throw new ClsExceptions(e, "Error obteniendo clientes ");
		}
		
	}
	
	public Hashtable getBusquedPorNIF(String NIF, String idInstitucionActual) throws ClsExceptions, SIGAException {
		String sqlClientes = "";
		Hashtable registro = null;
		RowsContainer rc = null;

		try {
			sqlClientes = "SELECT C.id_letrado, C.id_colegio, C.num_colegiado, " +
							"C.residencia, C.ejerciente,c.descripcion,C.tratamiento, " +
							" L.nombre, L.apellido1,L.apellido2,L.num_doc, L.sexo, TO_CHAR(C.fecha_alta, 'dd/MM/yyyy') AS fecha_alta, "+
							" L.DIR_PROFESIONAL, L.COD_POSTAL, L.IDPAIS, L.IDPROVINCIA, L.IDPOBLACION,L.POBLACION, L.TELEFONO, L.FAX, L.MAIL "+
						  "FROM  V_CENSO_COLEGIACIONES C, V_CENSO_LETRADOS L where C.id_letrado=L.id_letrado ";
			
			if (idInstitucionActual!= null && !idInstitucionActual.trim().equals("")) {
				sqlClientes += "    AND ((C.id_colegio <> "+idInstitucionActual+") OR "+
							   " 		 (C.id_colegio = "+idInstitucionActual+" AND C.ejerciente = 'n')) ";
			}
			  
			if (NIF != null && !NIF.trim().equals("")) {
				sqlClientes += " AND UPPER(L.num_doc)= '" + NIF +"'";
			}

			sqlClientes += " UNION " +

			"  SELECT nc.id_letrado, nc.id_colegio, 'No Colegiado', '-', 'No Colegiado',nc.descripcion,nc.idtratamiento, nc.nombre, "
			+"		nc.apellido1, nc.apellido2, nc.num_doc,nc.sexo,'-', "
			+" 		'-', '-', '-', '-', '-', '-', '-','-', '-' "
			+" FROM V_CENSO_NOCOLEGIADOS nc  WHERE 1 = 1 ";
			
			if (idInstitucionActual!= null && !idInstitucionActual.trim().equals("")) {
				sqlClientes += "    AND nc.id_colegio = "+idInstitucionActual;
			}

			if (NIF != null && !NIF.trim().equals("")) {
				sqlClientes += " AND UPPER(nc.num_doc)= '" + NIF +"'";
			}

			sqlClientes += "  ORDER BY apellido1 , apellido2, nombre";
			// RGG cambio visibilidad
			rc = this.find(sqlClientes);
			if (rc!=null) {
				if(rc.size()>0){
					for(int i = 0; i<rc.size();i++){
						Row fila = (Row) rc.get(i);
						registro = (Hashtable)fila.getRow();
						if(((String)registro.get("ID_COLEGIO")).equals(idInstitucionActual)){
							break;
						}
					}
				}
			}

		} catch (Exception e) {
			throw new ClsExceptions(e, "Error obteniendo clientes ");
		}			
		    
		return registro;
	}
}
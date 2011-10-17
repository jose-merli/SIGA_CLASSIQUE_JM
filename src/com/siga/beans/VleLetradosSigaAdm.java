
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.Utilidades.paginadores.PaginadorBind;
import com.siga.censo.form.BusquedaCensoForm;
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
		       if (!idInstitucionBuscar.trim().equals("")) {
			       	contador++;
					codigos.put(new Integer(contador),idInstitucionBuscar.trim());
					sqlClientes += "  C.id_colegio = :"+contador;
			   } else {
			   	//String institucionesVisibles = CenVisibilidad.getVisibilidadCenso(idInstitucionActual);
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
				   			//sqlClientes += " AND LTRIM(DECODE(CEN_COLEGIADO.COMUNITARIO,'1',CEN_COLEGIADO.NCOMUNITARIO, CEN_COLEGIADO.NCOLEGIADO),'0') = LTRIM(:"+contador+",'0') " ;

				          } 

//				  	 3
				  	       if (!formulario.getNombre().trim().equals("")) {
				  		       	if (bBusqueda) {
				  		       		contador++;
				  		       	sqlClientes += " AND (UPPER(L.nombre) "+ComodinBusquedas.prepararSentenciaExactaBind(formulario.getNombre().trim(),contador,codigos)+") ";
				  					  
				  		       	}else{
				  		       	contador++;
				  		       	sqlClientes += " AND ("+ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getNombre().trim(),"L.nombre",contador,codigos )+") ";
				  		       	}
				  	       }
				  	       
//				  	     4 y 5 Criterio de busqueda especial para los campos Apellido1 y Apellido2
				  	       if (!formulario.getApellido1().trim().equals("")&&(!formulario.getApellido2().trim().equals(""))) {// Los dos campos rellenos
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
				  	       }else if (!formulario.getApellido1().trim().equals("")&&(formulario.getApellido2().trim().equals(""))) {//Apellido1 relleno
				  	       	if (bBusqueda  ) {
				  	       	contador++;
				  	       		sqlClientes += " AND ((L.apellido1 "+ComodinBusquedas.prepararSentenciaExactaBind(formulario.getApellido1(),contador,codigos)+") OR (L.apellido1 LIKE '"+UtilidadesBDAdm.validateChars(formulario.getApellido1().trim())+" %'))";
				  	       	}else{
				  	       	contador++;	
				  	       		sqlClientes += " AND ("+ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getApellido1().trim(),"L.apellido1",contador,codigos )+" OR (L.apellido1||' '||L.apellido2 LIKE '%"+UtilidadesBDAdm.validateChars(formulario.getApellido1().trim())+"%')) ";
				  	       		
				  	       	}
				  	       }else if (formulario.getApellido1().trim().equals("")&&(!formulario.getApellido2().trim().equals(""))) {//Apellido2 relleno
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

				  	   				       
//				 6  //Consulta sobre el campo NIF/CIF, si el usuario mete comodines la búsqueda es como se hacía siempre, en el caso
					// de no meter comodines se ha creado un nuevo metodo ComodinBusquedas.preparaCadenaNIFSinComodin para que monte 
					// la consulta adecuada. 
				       
				       if (!formulario.getNif().trim().equals("")) {
				    	   if ((bBusqueda ) ) {
				    		   contador++;
				    		   codigos.put(new Integer(contador), formulario.getNif().trim().toUpperCase());
				    		   sqlClientes +=" AND UPPER(L.num_doc)= :"+contador;
				       		}else{
				     
				       	if (ComodinBusquedas.hasComodin(formulario.getNif())){
				       		contador++;
				       		sqlClientes += " AND ("+ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getNif().trim(),"L.num_doc",contador,codigos)+") ";
				       	}else{
				       		
				       			contador++;
				       		   sqlClientes +="AND "+ComodinBusquedas.prepararSentenciaNIFBind(formulario.getNif(),"L.num_doc",contador,codigos);
				       		}
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
}
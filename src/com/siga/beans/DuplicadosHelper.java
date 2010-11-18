/*
 * Created on 14-09-2010 by jbd
 * 
 */
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.siga.Utilidades.paginadores.Paginador;
import com.siga.Utilidades.paginadores.PaginadorBind;
import com.siga.censo.form.MantenimientoDuplicadosForm;
import com.siga.general.SIGAException;

/**
 * @author daniel.campos
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
// RGG cambio visibilidad public class CenPersonaAdm extends MasterBeanAdministrador {
public class DuplicadosHelper{

	/**
	 * Obtiene clientes repetidos segun los criterios de entrada
	 * 
	 * @param idInstitucion 
	 * @param formulario Formulario de busqueda de clientes con los datos de busqueda 
	 * @return java.util.Vector Vector de tablas hash  
	 */
	public Vector getPersonasSimilares(MantenimientoDuplicadosForm formulario) throws ClsExceptions, SIGAException {

		Vector salida = null;
		String sqlClientes = "";
	  	
	  	// Acceso a BBDD
		RowsContainer rcClientes = null;
		try { 
		    
			String nombre = formulario.getNombre();
			String apellido1 = formulario.getApellido1();
			String apellido2 = formulario.getApellido2();
			String nif = formulario.getNifcif();
			String nColegiado = formulario.getNumeroColegiado();
			
			boolean buscar = false;
			boolean checkIdentificador = formulario.getChkIdentificador();
			boolean checkApellidos = formulario.getChkApellidos();
			boolean checkNombreApellidos = formulario.getChkNombreApellidos();
			boolean checkNumColegiado = formulario.getChkNumColegiado();
			
			String sqlFinal = "";
			// Parte de la query comun a todas las subconsultas
			StringBuffer sqlGenerico = new StringBuffer();
			sqlGenerico.append(" select p.idpersona, ");
			sqlGenerico.append("        p.nifcif, ");
			sqlGenerico.append("        p.nombre, ");
			sqlGenerico.append("        p.apellidos1, ");
			sqlGenerico.append("        p.apellidos2, ");
			sqlGenerico.append("        c.idinstitucion, ");
			sqlGenerico.append("        c.ncolegiado ");
			sqlGenerico.append("   from cen_persona p, cen_persona p2, ");
			sqlGenerico.append("        cen_colegiado c, cen_colegiado c2, ");
			sqlGenerico.append("        cen_cliente cli, cen_cliente cli2 ");
			sqlGenerico.append("  where p.idpersona > 100 ");
			sqlGenerico.append("    and p2.idpersona > 100 ");
			sqlGenerico.append("    and cli.idpersona=p.idpersona ");
			sqlGenerico.append("    and cli.idinstitucion=2000 ");
			sqlGenerico.append("    and cli2.idpersona=p2.idpersona ");
			sqlGenerico.append("    and cli2.idinstitucion=2000 ");
			sqlGenerico.append("    and p.idpersona = c.idpersona(+) ");
			sqlGenerico.append("    and p2.idpersona = c2.idpersona(+) ");
			sqlGenerico.append("    and p.idpersona <> p2.idpersona ");

			// Añadimos los patrones al sql generico para que se apliquen en cada subquery
			// Ponemos el flag buscar a true para forzar que busque con el patron aunque no haya ningun criterio 
			if(nombre!=null && !nombre.equalsIgnoreCase("")){
				sqlGenerico.append("    and upper(p.nombre) like upper('%"+nombre+"%') ");
				sqlGenerico.append("    and upper(p2.nombre) like upper('%"+nombre+"%') ");
				buscar=true;
			}
			if(apellido1!=null && !apellido1.equalsIgnoreCase("")){
				sqlGenerico.append("    and upper(p.apellidos1) like upper('%"+apellido1+"%') ");
				sqlGenerico.append("    and upper(p2.apellidos1) like upper('%"+apellido1+"%') ");
				buscar=true;
			}
			if(apellido2!=null && !apellido2.equalsIgnoreCase("")){
				sqlGenerico.append("    and upper(p.apellidos2) like upper('%" + apellido2 + "%') ");
				sqlGenerico.append("    and upper(p2.apellidos2) like upper('%" + apellido2 + "%') ");
				buscar=true;
			}
			if(nif!=null && !nif.equalsIgnoreCase("")){
				sqlGenerico.append("    and to_number(regexp_replace(p.nifcif, '[^[:digit:]]', '')) = to_number(regexp_replace("+nif+", '[^[:digit:]]', '')) ");
				sqlGenerico.append("    and to_number(regexp_replace(p2.nifcif, '[^[:digit:]]', '')) = to_number(regexp_replace("+nif+", '[^[:digit:]]', '')) ");
				buscar=true;
			}
			if(nColegiado!=null && !nColegiado.equalsIgnoreCase("")){
				sqlGenerico.append("    and c.ncolegiado = '"+nColegiado+"' ");
				sqlGenerico.append("    and c2.ncolegiado = '"+nColegiado+"' ");
				buscar=true;
			}

			
			// Creamos las querys para cada paremetro de busqueda
			StringBuffer sqlMismoNif = new StringBuffer();
			sqlMismoNif.append("    and to_number(regexp_replace(p.nifcif, '[^[:digit:]]', '')) = to_number(regexp_replace(p2.nifcif, '[^[:digit:]]', '')) ");

			StringBuffer sqlNumeroColegiado = new StringBuffer();
			sqlNumeroColegiado.append("    and c.idinstitucion = c2.idinstitucion ");
			sqlNumeroColegiado.append("    and c.ncolegiado = c2.ncolegiado       ");

			StringBuffer sqlMismosApellidos = new StringBuffer();
			sqlMismosApellidos.append("   and (upper(p.apellidos1 || decode(p.apellidos2, null, '', ' ' || p.apellidos2)) =     ");
			sqlMismosApellidos.append("        upper(p2.apellidos1 || decode(p2.apellidos2, null, '', ' ' || p2.apellidos2)) or ");
			sqlMismosApellidos.append("        upper(p2.apellidos1 || decode(p2.apellidos2, null, '', ' ' || p2.apellidos2)) =  ");
			sqlMismosApellidos.append("        upper(p.apellidos1 || decode(p.apellidos2, null, '', ' ' || p.apellidos2)))      ");

			StringBuffer sqlMismoNombreApellidos = new StringBuffer();
			sqlMismoNombreApellidos.append("   and p.nombre<>'-'   ");
			sqlMismoNombreApellidos.append("   and (upper(regexp_replace(p.nombre || ' ' || p.apellidos1 || decode(p.apellidos2, null, '', ' ' || p.apellidos2), '[^[:alpha:]]', '')) =     ");
			sqlMismoNombreApellidos.append("       upper(regexp_replace(p2.nombre || ' ' || p2.apellidos1 || decode(p2.apellidos2, null, '', ' ' || p2.apellidos2), '[^[:alpha:]]', '')) or ");
			sqlMismoNombreApellidos.append("       upper(regexp_replace(p2.nombre || ' ' || p2.apellidos1 || decode(p2.apellidos2, null, '', ' ' || p2.apellidos2), '[^[:alpha:]]', '')) =  ");
			sqlMismoNombreApellidos.append("       upper(regexp_replace(p.nombre || ' ' || p.apellidos1 || decode(p.apellidos2, null, '', ' ' || p.apellidos2), '[^[:alpha:]]', '')))       ");

			// Creamos el order by dependiendo de la eleccion del usuario 
			StringBuffer sqlOrden = new StringBuffer();
	       	String campoOrden = formulario.getCampoOrdenacion();
	       	if (campoOrden.equalsIgnoreCase("nif"))
	       		sqlOrden.append(" order by to_number(regexp_replace(nifcif, '[^[:digit:]]', '')) ");
	       	if (campoOrden.equalsIgnoreCase("numeroColegiado"))
	       		sqlOrden.append(" order by to_number(ncolegiado) ");
	       	if (campoOrden.equalsIgnoreCase("apellidos"))
	       		sqlOrden.append(" order by apellidos1, apellidos2 ");
			sqlOrden.append(formulario.getSentidoOrdenacion());      	
	       	
			if(formulario.getTipoConexion()!=null && formulario.getTipoConexion().equalsIgnoreCase("union")){
				
				// Añadimos las querys especificas que han sido seleccionadas
		       	if(checkIdentificador){
					sqlFinal += sqlGenerico.toString()+sqlMismoNif.toString();
				}
		       	if(checkNumColegiado){
		       		if(!sqlFinal.equalsIgnoreCase(""))
		       			sqlFinal+=" union ";
		       		sqlFinal += sqlGenerico.toString()+sqlNumeroColegiado.toString();
				}
		       	if(checkApellidos){
		       		if(!sqlFinal.equalsIgnoreCase(""))
		       			sqlFinal+=" union ";
		       		sqlFinal += sqlGenerico.toString()+sqlMismosApellidos.toString();
				}
		       	if(checkNombreApellidos){
		       		if(!sqlFinal.equalsIgnoreCase(""))
		       			sqlFinal+=" union ";
		       		sqlFinal += sqlGenerico.toString()+sqlMismoNombreApellidos.toString();
				}
			}else{
				if(checkIdentificador){
					sqlFinal += sqlGenerico.toString()+sqlMismoNif.toString();
				}
		       	if(checkNumColegiado){
		       		if(!sqlFinal.equalsIgnoreCase(""))
		       			sqlFinal += sqlNumeroColegiado.toString();
		       		else
		       			sqlFinal += sqlGenerico.toString()+sqlNumeroColegiado.toString();
				}
		       	if(checkApellidos){
		       		if(!sqlFinal.equalsIgnoreCase(""))
		       			sqlFinal += sqlMismosApellidos.toString();
		       		else
		       			sqlFinal += sqlGenerico.toString()+sqlMismosApellidos.toString();
				}
		       	if(checkNombreApellidos){
		       		if(!sqlFinal.equalsIgnoreCase(""))
		       			sqlFinal += sqlMismoNombreApellidos.toString();
		       		else
		       			sqlFinal += sqlGenerico.toString()+sqlMismoNombreApellidos.toString();
				}
			}
	       	if(sqlFinal.equalsIgnoreCase("") && buscar)
	       		sqlFinal += sqlGenerico.toString();
	       	
	       	Vector similares = new Vector();
	       	// Completamos la query que se va a ejecutar
	       	String sql=" select distinct * from (" + sqlFinal.toString() + ")" + sqlOrden.toString();
	       	// Si se ha seleccionado algun criterio o patron realizamos la busqueda, si no no
	       	if(!sqlFinal.equalsIgnoreCase("")){
	       		similares=this.selectGenerico(sql);
	       	}
	       	
			return similares;
		}catch (Exception e) { 	
			throw new ClsExceptions(e,"Error obteniendo clientes "); 
		}
	}

	
	/**
	 * Obtiene clientes repetidos segun los criterios de entrada
	 * 
	 * @param idInstitucion 
	 * @param formulario Formulario de busqueda de clientes con los datos de busqueda 
	 * @return java.util.Vector Vector de tablas hash  
	 */
	public Paginador getDuplicados(MantenimientoDuplicadosForm formulario) throws ClsExceptions, SIGAException {

		Vector salida = null;
		String sqlClientes = "";
	  	
	  	// Acceso a BBDD
		RowsContainer rcClientes = null;
		try { 
		    
			String nombre = formulario.getNombre();
			String apellido1 = formulario.getApellido1();
			String apellido2 = formulario.getApellido2();
			String nif = formulario.getNifcif();
			String nColegiado = formulario.getNumeroColegiado();
			String idInstitucion = formulario.getIdInstitucion();
			
			boolean buscar = false;
			boolean checkIdentificador = formulario.getChkIdentificador();
			boolean checkApellidos = formulario.getChkApellidos();
			boolean checkNombreApellidos = formulario.getChkNombreApellidos();
			boolean checkNumColegiado = formulario.getChkNumColegiado();
			
			Hashtable codigos = new Hashtable();
			int contador = 0;
			
			String sqlFinal = "";
			// Parte de la query comun a todas las subconsultas
			StringBuffer sqlGenerico = new StringBuffer();
			sqlGenerico.append(" select p.idpersona, ");
			sqlGenerico.append("        p.nifcif, ");
			sqlGenerico.append("        p.nombre, ");
			sqlGenerico.append("        p.apellidos1, ");
			sqlGenerico.append("        p.apellidos2, ");
			sqlGenerico.append("        c.idinstitucion, ");
			sqlGenerico.append("        decode(c.comunitario,'1',c.ncomunitario,c.ncolegiado) ncolegiado, ");
			sqlGenerico.append("        c.ncomunitario, ");
			sqlGenerico.append("        i.abreviatura ");
			sqlGenerico.append("   from cen_persona p, cen_persona p2, ");
			sqlGenerico.append("        cen_colegiado c, cen_colegiado c2, ");
			sqlGenerico.append("        cen_cliente cli, cen_cliente cli2, ");
			sqlGenerico.append("        cen_institucion i");
			sqlGenerico.append("  where p.idpersona > 100 ");
			sqlGenerico.append("    and p2.idpersona > 100 ");
			sqlGenerico.append("    and cli.idpersona=p.idpersona ");
			sqlGenerico.append("    and cli.idinstitucion=2000 ");
			sqlGenerico.append("    and cli2.idpersona=p2.idpersona ");
			sqlGenerico.append("    and cli2.idinstitucion=2000 ");
			sqlGenerico.append("    and p.idpersona = c.idpersona(+) ");
			sqlGenerico.append("    and p2.idpersona = c2.idpersona(+) ");
			sqlGenerico.append("    and p.idpersona <> p2.idpersona ");
			sqlGenerico.append("    and i.idinstitucion(+)= c.idinstitucion ");

			// Añadimos los patrones al sql generico para que se apliquen en cada subquery
			// Ponemos el flag buscar a true para forzar que busque con el patron aunque no haya ningun criterio 
			if(nombre!=null && !nombre.equalsIgnoreCase("")){
				contador++;
				sqlGenerico.append("    and upper(p.nombre) like upper('%"+nombre+"%') ");
				sqlGenerico.append("    and upper(p2.nombre) like upper('%"+nombre+"%') ");
				buscar=true;
			}
			if(apellido1!=null && !apellido1.equalsIgnoreCase("")){
				sqlGenerico.append("    and upper(p.apellidos1) like upper('%"+apellido1+"%') ");
				sqlGenerico.append("    and upper(p2.apellidos1) like upper('%"+apellido1+"%') ");
				buscar=true;
			}
			if(apellido2!=null && !apellido2.equalsIgnoreCase("")){
				sqlGenerico.append("    and upper(p.apellidos2) like upper('%" + apellido2 + "%') ");
				sqlGenerico.append("    and upper(p2.apellidos2) like upper('%" + apellido2 + "%') ");
				buscar=true;
			}
			if(nif!=null && !nif.equalsIgnoreCase("")){
				sqlGenerico.append("    and to_number(regexp_replace(p.nifcif, '[^[:digit:]]', '')) = to_number(regexp_replace('"+nif+"', '[^[:digit:]]', '')) ");
				sqlGenerico.append("    and to_number(regexp_replace(p2.nifcif, '[^[:digit:]]', '')) = to_number(regexp_replace('"+nif+"', '[^[:digit:]]', '')) ");
				buscar=true;
			}
			if(nColegiado!=null && !nColegiado.equalsIgnoreCase("")){
				sqlGenerico.append("    and c.ncolegiado = '"+nColegiado+"' ");
				sqlGenerico.append("    and c2.ncolegiado = '"+nColegiado+"' ");
				buscar=true;
			}
			if(idInstitucion!=null && !idInstitucion.equalsIgnoreCase("")){
				sqlGenerico.append("    and c.idInstitucion = '"+idInstitucion+"' ");
				sqlGenerico.append("    and c2.idInstitucion = '"+idInstitucion+"' ");
				buscar=true;
			}

			
			// Creamos las querys para cada paremetro de busqueda
			StringBuffer sqlMismoNif = new StringBuffer();
			sqlMismoNif.append("    and to_number(regexp_replace(p.nifcif, '[^[:digit:]]', '')) = to_number(regexp_replace(p2.nifcif, '[^[:digit:]]', '')) ");

			StringBuffer sqlNumeroColegiado = new StringBuffer();
			sqlNumeroColegiado.append("    and c.idinstitucion = c2.idinstitucion ");
			sqlNumeroColegiado.append("    and c.ncolegiado = c2.ncolegiado       ");

			StringBuffer sqlMismosApellidos = new StringBuffer();
			sqlMismosApellidos.append("   and (upper(p.apellidos1 || decode(p.apellidos2, null, '', ' ' || p.apellidos2)) =     ");
			sqlMismosApellidos.append("        upper(p2.apellidos1 || decode(p2.apellidos2, null, '', ' ' || p2.apellidos2)) or ");
			sqlMismosApellidos.append("        upper(p2.apellidos1 || decode(p2.apellidos2, null, '', ' ' || p2.apellidos2)) =  ");
			sqlMismosApellidos.append("        upper(p.apellidos1 || decode(p.apellidos2, null, '', ' ' || p.apellidos2)))      ");

			StringBuffer sqlMismoNombreApellidos = new StringBuffer();
			sqlMismoNombreApellidos.append("   and p.nombre<>'-'   ");
			sqlMismoNombreApellidos.append("   and (upper(regexp_replace(p.nombre || ' ' || p.apellidos1 || decode(p.apellidos2, null, '', ' ' || p.apellidos2), '[^[:alpha:]]', '')) =     ");
			sqlMismoNombreApellidos.append("       upper(regexp_replace(p2.nombre || ' ' || p2.apellidos1 || decode(p2.apellidos2, null, '', ' ' || p2.apellidos2), '[^[:alpha:]]', '')) or ");
			sqlMismoNombreApellidos.append("       upper(regexp_replace(p2.nombre || ' ' || p2.apellidos1 || decode(p2.apellidos2, null, '', ' ' || p2.apellidos2), '[^[:alpha:]]', '')) =  ");
			sqlMismoNombreApellidos.append("       upper(regexp_replace(p.nombre || ' ' || p.apellidos1 || decode(p.apellidos2, null, '', ' ' || p.apellidos2), '[^[:alpha:]]', '')))       ");

			// Creamos el order by dependiendo de la eleccion del usuario 
			StringBuffer sqlOrden = new StringBuffer();
	       	String campoOrden = formulario.getCampoOrdenacion();
	       	if (campoOrden.equalsIgnoreCase("nif"))
	       		sqlOrden.append(" order by to_number(regexp_replace(nifcif, '[^[:digit:]]', '')) ");
	       	if (campoOrden.equalsIgnoreCase("numeroColegiado"))
	       		sqlOrden.append(" order by abreviatura, to_number(ncolegiado) ");
	       	if (campoOrden.equalsIgnoreCase("apellidos"))
	       		sqlOrden.append(" order by apellidos1, apellidos2 ");
			sqlOrden.append(formulario.getSentidoOrdenacion());      	
	       	
			
			if(formulario.getTipoConexion()!=null && formulario.getTipoConexion().equalsIgnoreCase("union")){
				
				// Añadimos las querys especificas que han sido seleccionadas
		       	if(checkIdentificador){
					sqlFinal += sqlGenerico.toString()+sqlMismoNif.toString();
				}
		       	if(checkNumColegiado){
		       		if(!sqlFinal.equalsIgnoreCase(""))
		       			sqlFinal+=" union ";
		       		sqlFinal += sqlGenerico.toString()+sqlNumeroColegiado.toString();
				}
		       	if(checkApellidos){
		       		if(!sqlFinal.equalsIgnoreCase(""))
		       			sqlFinal+=" union ";
		       		sqlFinal += sqlGenerico.toString()+sqlMismosApellidos.toString();
				}
		       	if(checkNombreApellidos){
		       		if(!sqlFinal.equalsIgnoreCase(""))
		       			sqlFinal+=" union ";
		       		sqlFinal += sqlGenerico.toString()+sqlMismoNombreApellidos.toString();
				}
			}else{
				if(checkIdentificador){
					sqlFinal += sqlGenerico.toString()+sqlMismoNif.toString();
				}
		       	if(checkNumColegiado){
		       		if(!sqlFinal.equalsIgnoreCase(""))
		       			sqlFinal += sqlNumeroColegiado.toString();
		       		else
		       			sqlFinal += sqlGenerico.toString()+sqlNumeroColegiado.toString();
				}
		       	if(checkApellidos){
		       		if(!sqlFinal.equalsIgnoreCase(""))
		       			sqlFinal += sqlMismosApellidos.toString();
		       		else
		       			sqlFinal += sqlGenerico.toString()+sqlMismosApellidos.toString();
				}
		       	if(checkNombreApellidos){
		       		if(!sqlFinal.equalsIgnoreCase(""))
		       			sqlFinal += sqlMismoNombreApellidos.toString();
		       		else
		       			sqlFinal += sqlGenerico.toString()+sqlMismoNombreApellidos.toString();
				}
			}
	       	if(sqlFinal.equalsIgnoreCase("") && buscar)
	       		sqlFinal += sqlGenerico.toString();
	       	
	       	Vector similares = new Vector();
	       	// Completamos la query que se va a ejecutar
	       	String sql=" select distinct * from (" + sqlFinal.toString() + ")" + sqlOrden.toString();
	       	// Si se ha seleccionado algun criterio o patron realizamos la busqueda, si no no
	       	if(!sqlFinal.equalsIgnoreCase("")){
	       		similares=this.selectGenerico(sql);
		       	Paginador paginador = new Paginador(sql);				
		       	int totalRegistros = paginador.getNumeroTotalRegistros();
	       	
				if (totalRegistros==0){					
					paginador =null;
				}
				return paginador;
	       	}else{
	       		return null;
	       	}
			
		}catch (Exception e) { 	
			throw new ClsExceptions(e,"Error obteniendo duplicados "); 
		}
	}
	

	////////////////////////// Metodos genericos de MasterBean ////////////////////////// 
	/**
	 * 
	 * @param select
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public Vector selectGenerico(String select) throws ClsExceptions, SIGAException 
	{
		Vector datos = new Vector();
		
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			if (rc.query(select)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable) fila.getRow(); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) {
			throw new ClsExceptions (e,  e.getMessage() + "Consulta SQL:"+select);
		}
		return datos;	
	}
	
	
	
	
}
	

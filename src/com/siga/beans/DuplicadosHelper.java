/*
 * Created on 14-09-2010 by jbd
 * 
 */
package com.siga.beans;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;
import com.siga.Utilidades.paginadores.Paginador;
import com.siga.Utilidades.paginadores.PaginadorBind;
import com.siga.censo.form.MantenimientoDuplicadosForm;
import com.siga.general.SIGAException;

public class DuplicadosHelper{

	public static final int SIMIL_NIF = 0;
	public static final int SIMIL_NOMAPEL = 1;
	public static final int SIMIL_NCOL = 2;
	public static final String[] similaritudes = {"nif", "nomapel", "ncol"};

	
	/**
	 * Busca y devuelve un listado de clientes que contengan algo parecido al NIF, Nombre y Apellidos o Colegio y Numero de colegiado introducidos por formulario.
	 * En caso de encontrar solo una coincidencia, tambien la devuelve.
	 * 
	 * @param formulario Formulario de busqueda de clientes con los datos de busqueda 
	 * @return java.util.Vector Vector de tablas hash  
	 */
	public Vector<Hashtable<String, String>> getPersonasSimilares(MantenimientoDuplicadosForm formulario) throws ClsExceptions, SIGAException
	{
		StringBuffer sqlSelect, sqlFrom, sqlWhere, sqlFinal = null;
		
		// construyendo base de la consulta: personas
		sqlSelect = new StringBuffer();
		sqlSelect.append("select per.idpersona, per.nifcif, per.nombre, per.apellidos1, per.apellidos2,");
		sqlSelect.append("       (select count(1) from cen_colegiado coleg where coleg.idpersona=Per.idpersona) as colegiaciones, ");
		sqlSelect.append("       (select count(1) from cen_nocolegiado conse where conse.idinstitucion=" + ClsConstants.INSTITUCION_CGAE + " and conse.idpersona=Per.idpersona) nocolegiadoCGAE ");
		sqlFrom = new StringBuffer();
		sqlFrom.append(" from cen_persona per ");
		sqlWhere = new StringBuffer();
		sqlWhere.append(" where per.idpersona > 100 ");

		// anyadiendo la busqueda por NIF, si se introdujo en el formulario
		String nif = formulario.getNifcif();
		nif = (nif == null || nif.trim().equalsIgnoreCase("")) ? "" : nif;
		if (! nif.equalsIgnoreCase("")) {
			sqlFinal = new StringBuffer();
			sqlFinal.append(sqlSelect);
			sqlFinal.append(sqlFrom);
			sqlFinal.append(sqlWhere);
			sqlFinal.append(" and regexp_like(regexp_replace(per.nifcif, '[^[:digit:]]', ''), regexp_replace('" +nif+ "', '[^[:digit:]]', '')) ");
			sqlFinal.append(" order by to_number(regexp_replace(Per.nifcif, '[^[:digit:]]', '')) ");
		}
		
		if (sqlFinal == null) {
			// anyadiendo la busqueda por nombre y/o apellidos, si se introdujo en el formulario
			String nombre = formulario.getNombre();
			nombre = (nombre == null || nombre.trim().equalsIgnoreCase("")) ? "" : nombre;
			String apellidos = formulario.getApellidos();
			apellidos = (apellidos == null || apellidos.trim().equalsIgnoreCase("")) ? "" : apellidos;
			
			if (! nombre.equalsIgnoreCase("") || ! apellidos.equalsIgnoreCase("")) {
				sqlFinal = new StringBuffer();
				sqlFinal.append(sqlSelect);
				sqlFinal.append(", per.apellidos1 || ' ' || per.apellidos2 as Apellidos "); // devolviendo este campo, podremos diferenciar en los resultados si se ha buscado por NIF o por Apellidos
				sqlFinal.append(sqlFrom);
				sqlFinal.append(sqlWhere);
				if (! nombre.equalsIgnoreCase("")) {
					sqlFinal.append(" and regexp_like(regexp_replace(upper(translate(Per.nombre, ");
					sqlFinal.append("                     '·ÈÌÛ˙‡ËÏÚ˘„ı‚ÍÓÙÙ‰ÎÔˆ¸Á¡…Õ”⁄¿»Ã“Ÿ√’¬ Œ‘€ƒÀœ÷‹«', ");
					sqlFinal.append("                     'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC')), 'DE |LA |Y |DEL |LOS |EL |I |[^[:alpha:]]| ', ''), ");
					sqlFinal.append("      regexp_replace(upper(translate('"+nombre+"', ");
					sqlFinal.append("                     '·ÈÌÛ˙‡ËÏÚ˘„ı‚ÍÓÙÙ‰ÎÔˆ¸Á¡…Õ”⁄¿»Ã“Ÿ√’¬ Œ‘€ƒÀœ÷‹«', ");
					sqlFinal.append("                     'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC')), 'DE |LA |Y |DEL |LOS |EL |I |[^[:alpha:]]| ', ''))");
				}
				if (! apellidos.equalsIgnoreCase("")) {
					sqlFinal.append(" and regexp_like(regexp_replace(upper(translate(Per.apellidos1 || decode(Per.apellidos2, null, '', ' ' || Per.apellidos2), ");
					sqlFinal.append("                     '·ÈÌÛ˙‡ËÏÚ˘„ı‚ÍÓÙÙ‰ÎÔˆ¸Á¡…Õ”⁄¿»Ã“Ÿ√’¬ Œ‘€ƒÀœ÷‹«', ");
					sqlFinal.append("                     'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC')), 'DE |LA |Y |DEL |LOS |EL |I |[^[:alpha:]]| ', ''), ");
					sqlFinal.append("      regexp_replace(upper(translate('"+apellidos+"', ");
					sqlFinal.append("                     '·ÈÌÛ˙‡ËÏÚ˘„ı‚ÍÓÙÙ‰ÎÔˆ¸Á¡…Õ”⁄¿»Ã“Ÿ√’¬ Œ‘€ƒÀœ÷‹«', ");
					sqlFinal.append("                     'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC')), 'DE |LA |Y |DEL |LOS |EL |I |[^[:alpha:]]| ', ''))");
				}
				sqlFinal.append(" order by Per.apellidos1, Per.apellidos2, Per.nombre ");
			}
		}

		if (sqlFinal == null) {
			// anyadiendo la busqueda por colegio y/o numero de colegiado, si se introdujo en el formulario
			String institucion = formulario.getIdInstitucion();
			institucion = (institucion == null || institucion.trim().equalsIgnoreCase("")) ? "" : institucion;
			String nColegiado = formulario.getNumeroColegiado();
			nColegiado = (nColegiado == null || nColegiado.trim().equalsIgnoreCase("")) ? "" : nColegiado;
			
			if (! institucion.equalsIgnoreCase("") || ! nColegiado.equalsIgnoreCase("")) {
				sqlFinal = new StringBuffer();
				sqlFinal.append(sqlSelect);
				sqlFinal.append(", col.ncolegiado, ins.ABREVIATURA, ins.idinstitucion ");
				sqlFinal.append(sqlFrom);
				sqlFinal.append(", cen_colegiado col, cen_institucion ins ");
				sqlFinal.append(sqlWhere);
				sqlFinal.append(" and per.Idpersona = col.Idpersona ");
				sqlFinal.append(" and col.Idinstitucion = ins.Idinstitucion ");
				if (! institucion.equalsIgnoreCase("")) {
					sqlFinal.append(" and col.idinstitucion = "+institucion+" ");
				}
				if (! nColegiado.equalsIgnoreCase("")) { 
					sqlFinal.append(" and Decode(Col.Comunitario, '1', Col.Ncomunitario, Col.Ncolegiado) = '"+nColegiado+"' ");
				}
				sqlFinal.append(" order by col.idinstitucion, To_Number(Decode(Col.Comunitario, '1', Col.Ncomunitario, Col.Ncolegiado)) ");
			}
		}

		// ejecutando la consulta
		try {
			Vector<Hashtable<String, String>> similares = new Vector<Hashtable<String, String>>();
			if (sqlFinal != null && !sqlFinal.toString().equalsIgnoreCase("")) {
				similares = this.selectGenerico(sqlFinal.toString());
			}

			return similares;
		} catch (Exception e) {
			throw new ClsExceptions(e, "Error obteniendo clientes ");
		}
	} //getPersonasSimilares()

	/**
	 * Busca y devuelve un listado de personas duplicadas en funcion del parametro. Ver los valores posibles en el array 'similaritudes'
	 * 
	 * @param similaritud
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public Vector<Hashtable<String, String>> getPersonasSimilares(int similaritud) throws ClsExceptions, SIGAException
	{
		StringBuffer sqlPersona, sqlGenerico, sqlMismoNif, sqlMismoNombreApellidos, sqlMismoNumeroColegiado;
		String sqlOrden, sqlFinal;
		try {
			// Subquery que se utiliza dos veces para buscar duplicados
			sqlPersona = new StringBuffer();
			sqlPersona.append("select per.idpersona, per.nifcif, per.nombre, per.apellidos1, per.apellidos2,");
			sqlPersona.append("       Col.Idinstitucion, Decode(Col.Comunitario, '1', Col.Ncomunitario, Col.Ncolegiado) Numcol ");
			sqlPersona.append("  from cen_persona per, cen_cliente cli, cen_colegiado col");
			sqlPersona.append(" where per.idpersona = cli.idpersona ");
			sqlPersona.append("   and per.idpersona > 100 ");
			sqlPersona.append("   and cli.idinstitucion = " + ClsConstants.INSTITUCION_CGAE);
			sqlPersona.append("   And Per.Idpersona = Col.Idpersona(+) ");

			// Parte de la query comun a todas las subconsultas
			sqlGenerico = new StringBuffer();
			sqlGenerico.append("select distinct p1."+CenPersonaBean.C_IDPERSONA+", p1.nifcif, p1.nombre, p1.apellidos1, p1.apellidos2, ");
			sqlGenerico.append("       (select count(1) from cen_colegiado where idpersona=p1.idpersona) as colegiaciones, ");
			sqlGenerico.append("       (select count(1) from cen_nocolegiado n where n.idinstitucion=" + ClsConstants.INSTITUCION_CGAE + " and n.idpersona=p1.idpersona) nocolegiadoCGAE, ");
			sqlGenerico.append("       (select ins.abreviatura from cen_institucion ins where ins.idinstitucion = p1.idinstitucion) as colegio, ");
			sqlGenerico.append("       p1.Numcol ");
			sqlGenerico.append("  from ( ");
			sqlGenerico.append(sqlPersona);
			sqlGenerico.append("       ) p1, ( ");
			sqlGenerico.append(sqlPersona);
			sqlGenerico.append("       ) p2 ");
			sqlGenerico.append(" where p1.idpersona <> p2.idpersona ");
			
			// creando las querys para cada parametro de busqueda
			sqlMismoNif = new StringBuffer();
			sqlMismoNif.append(" and to_number(regexp_replace(p1.nifcif, '[^[:digit:]]', '')) = to_number(regexp_replace(p2.nifcif, '[^[:digit:]]', '')) ");
			
			sqlMismoNombreApellidos = new StringBuffer();
			sqlMismoNombreApellidos.append(" and (regexp_replace(upper(translate(p1.nombre || ' ' || p1.apellidos1 || ' ' || p1.apellidos2, ");
			sqlMismoNombreApellidos.append("                     '·ÈÌÛ˙‡ËÏÚ˘„ı‚ÍÓÙÙ‰ÎÔˆ¸Á¡…Õ”⁄¿»Ã“Ÿ√’¬ Œ‘€ƒÀœ÷‹«', ");
			sqlMismoNombreApellidos.append("                     'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC')), 'DE |LA |Y |DEL |LOS |EL |I |[^[:alpha:]]| ', '') = ");
			sqlMismoNombreApellidos.append("      regexp_replace(upper(translate(p2.nombre || ' ' || p2.apellidos1 || ' ' || p2.apellidos2, ");
			sqlMismoNombreApellidos.append("                     '·ÈÌÛ˙‡ËÏÚ˘„ı‚ÍÓÙÙ‰ÎÔˆ¸Á¡…Õ”⁄¿»Ã“Ÿ√’¬ Œ‘€ƒÀœ÷‹«', ");
			sqlMismoNombreApellidos.append("                     'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC')), 'DE |LA |Y |DEL |LOS |EL |I |[^[:alpha:]]| ', ''))");
			
			sqlMismoNumeroColegiado = new StringBuffer();
			sqlMismoNumeroColegiado.append(" and P1.Idinstitucion = P2.Idinstitucion ");
			sqlMismoNumeroColegiado.append(" and P1.Numcol = P2.Numcol ");
			sqlMismoNumeroColegiado.append(" and P1.Numcol Is Not Null ");

			// construyendo consulta final
			sqlFinal = sqlGenerico.toString();
			switch (similaritud) {
				case SIMIL_NIF:
					sqlOrden = " order by to_number(regexp_replace(nifcif, '[^[:digit:]]', '')) ";
					sqlFinal += sqlMismoNif.toString() + " " + sqlOrden.toString();
					break;
				case SIMIL_NOMAPEL:
					sqlOrden = " order by upper(apellidos1||' '||apellidos2||' '||nombre) ";
					sqlFinal += sqlMismoNombreApellidos.toString() + sqlOrden.toString();
					break;
				case SIMIL_NCOL:
					sqlOrden = " order by Colegio, To_Number(Numcol) ";
					sqlFinal += sqlMismoNumeroColegiado.toString() + sqlOrden.toString();
					break;
			}
			
			Vector<Hashtable<String, String>> similares = this.selectGenerico(sqlFinal);
			return similares;
			
		} catch (Exception e) {
			throw new ClsExceptions(e, "Error obteniendo clientes ");
		}
	} // getPersonasSimilares()
	
	/**
	 * Dada una persona, devuelve todos los datos que luego se imprimiran en un listado
	 * 
	 * @param persona
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public ArrayList<ArrayList<String>> getDatosPersonaSimilar(Hashtable<String, String> persona, UsrBean user) throws Exception
	{
		// Datos
		String idPersona;
		CenPersonaBean beanPersona;
		ArrayList<String> datosPersona, datosColegiacion;

		Vector<Integer> colegiacionesPersona;
		String idInstitucionCol;
		CenColegiadoBean colegiacion;
		Vector<Row> estadosColegio;
		Hashtable<String, String> estado;
		Hashtable<String, String> direccionCenso;

		ArrayList<ArrayList<String>> todoslosdatos = new ArrayList<ArrayList<String>>();

		if (persona == null) {
			// rellenando la cabecera
			datosPersona = new ArrayList<String>();
			datosPersona.add("IDENTIFICADOR");
			datosPersona.add("NOMBRE");
			datosPersona.add("APELLIDO1");
			datosPersona.add("APELLIDO2");
			datosPersona.add("NIF_CIF");
			datosPersona.add("LUGAR_NACIMIENTO");
			datosPersona.add("FECHA_NACIMIENTO");
			datosPersona.add("ULTIMAMODIFICACION");
			datosPersona.add("SANCIONES");
			datosPersona.add("CERTIFICADOS");
			datosPersona.add("COLEGIO");
			datosPersona.add("INSCRIPCION");
			datosPersona.add("NCOLEGIADO");
			datosPersona.add("FECHAINCORPORACION");
			datosPersona.add("RESIDENCIA");
			datosPersona.add("ESTADOCOLEGIAL");
			datosPersona.add("FECHAESTADO");
	
			datosPersona.add("DIRECCION");
			datosPersona.add("EMAIL");
			datosPersona.add("TELEFONO");
			datosPersona.add("FAX");
			datosPersona.add("MOVIL");
			
			todoslosdatos.add(datosPersona);
			return (todoslosdatos);
		}
		
		// Controles
		CenPersonaAdm admPersona = new CenPersonaAdm(user);
		CenSancionAdm admSancion = new CenSancionAdm(user);
		CerSolicitudCertificadosAdm admCertificados = new CerSolicitudCertificadosAdm(user);
		CenColegiadoAdm admColeg = new CenColegiadoAdm(user);
		CenInstitucionAdm admInstitucion = new CenInstitucionAdm(user);
		CenDireccionesAdm admDireccion = new CenDireccionesAdm(user);

		// iniciando la recogida de datos de una persona
		idPersona = persona.get(CenPersonaBean.C_IDPERSONA);
		datosPersona = new ArrayList<String>();

		// obteniendo datos personales
		beanPersona = admPersona.getPersonaPorId(idPersona);
		if (beanPersona.getFechaNacimiento() != null && !beanPersona.getFechaNacimiento().equalsIgnoreCase("")) {
			beanPersona.setFechaNacimiento(UtilidadesString.formatoFecha(beanPersona.getFechaNacimiento(), ClsConstants.DATE_FORMAT_JAVA,
					ClsConstants.DATE_FORMAT_SHORT_SPANISH));
		}
		beanPersona.setFechaMod(UtilidadesString.formatoFecha(beanPersona.getFechaMod(), ClsConstants.DATE_FORMAT_JAVA,
				ClsConstants.DATE_FORMAT_SHORT_SPANISH));
		datosPersona.add(beanPersona.getIdPersona().toString());
		datosPersona.add(beanPersona.getNombre());
		datosPersona.add(beanPersona.getApellido1());
		datosPersona.add(beanPersona.getApellido2());
		datosPersona.add(beanPersona.getNIFCIF());
		datosPersona.add(beanPersona.getNaturalDe());
		datosPersona.add(beanPersona.getFechaNacimiento());
		datosPersona.add(beanPersona.getFechaMod());
		datosPersona.add(String.valueOf(admSancion.getSancionesLetrado(idPersona, String.valueOf(ClsConstants.INSTITUCION_CGAE)).size()));
		datosPersona.add(String.valueOf(admCertificados.getNumeroCertificados(String.valueOf(ClsConstants.INSTITUCION_CGAE), idPersona)));

		// obteniendo datos de cada colegiacion
		colegiacionesPersona = admColeg.getColegiaciones(idPersona);
		for (Iterator<Integer> iterColegiaciones = colegiacionesPersona.iterator(); iterColegiaciones.hasNext();) {
			idInstitucionCol = iterColegiaciones.next().toString();
			colegiacion = admColeg.getDatosColegiales(Long.valueOf(idPersona), Integer.valueOf(idInstitucionCol));

			// por cada colegiacion se anyadira un registro, duplicando la persona si hay mas de una colegiacion
			datosColegiacion = new ArrayList<String>(datosPersona);

			// anyadiendo datos de colegiacion
			datosColegiacion.add(admInstitucion.getAbreviaturaInstitucion(idInstitucionCol));
			if (colegiacion.getComunitario().equalsIgnoreCase("1")) {
				datosColegiacion.add("Inscrito");
				datosColegiacion.add(colegiacion.getNComunitario());
			} else {
				datosColegiacion.add(" ");
				datosColegiacion.add(colegiacion.getNColegiado());
			}
			if (colegiacion.getFechaIncorporacion() != null && !colegiacion.getFechaIncorporacion().equalsIgnoreCase("")) {
				colegiacion.setFechaIncorporacion((UtilidadesString.formatoFecha(colegiacion.getFechaIncorporacion(), ClsConstants.DATE_FORMAT_JAVA,
						ClsConstants.DATE_FORMAT_SHORT_SPANISH)));
			}
			datosColegiacion.add(colegiacion.getFechaIncorporacion());
			datosColegiacion.add(colegiacion.getSituacionResidente().equalsIgnoreCase("1") ? "Residente" : " ");

			// anyadiendo estado colegial
			estadosColegio = admColeg.getEstadosColegiales(Long.valueOf(idPersona), Integer.valueOf(idInstitucionCol), "1");
			if (estadosColegio != null && estadosColegio.size() > 0) {
				estado = estadosColegio.get(0).getRow();
				datosColegiacion.add(UtilidadesString.formatoFecha(estado.get(CenDatosColegialesEstadoBean.C_FECHAESTADO).toString(),
						ClsConstants.DATE_FORMAT_JAVA, ClsConstants.DATE_FORMAT_SHORT_SPANISH));
				datosColegiacion.add(estado.get(CenEstadoColegialBean.C_DESCRIPCION).toString());
			} else {
				datosColegiacion.add(" ");
				datosColegiacion.add(" ");
			}

			// anyadiendo direccion
			direccionCenso = admDireccion.getDireccionTipo(idPersona, idInstitucionCol, String.valueOf(ClsConstants.TIPO_DIRECCION_CENSOWEB));
			datosColegiacion.add(direccionCenso.get(CenDireccionesBean.C_DOMICILIO) + ", " + direccionCenso.get("POBLACION") + ", "
					+ direccionCenso.get(CenDireccionesBean.C_CODIGOPOSTAL) + " " + direccionCenso.get("PROVINCIA"));
			datosColegiacion.add(direccionCenso.get(CenDireccionesBean.C_CORREOELECTRONICO));
			datosColegiacion.add(direccionCenso.get(CenDireccionesBean.C_TELEFONO1));
			datosColegiacion.add(direccionCenso.get(CenDireccionesBean.C_FAX1));
			datosColegiacion.add(direccionCenso.get(CenDireccionesBean.C_MOVIL));

			todoslosdatos.add(datosColegiacion);
		}

		if (colegiacionesPersona.size() == 0) {
			todoslosdatos.add(datosPersona);
		}

		return todoslosdatos;
	} // getDatosPersonaSimilar()
	

	////////////////////////// Metodos genericos de MasterBean ////////////////////////// 
	/**
	 * 
	 * @param select
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public Vector<Hashtable<String, String>> selectGenerico(String select) throws ClsExceptions, SIGAException 
	{
		Vector<Hashtable<String, String>> datos = new Vector<Hashtable<String, String>>();
		
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			if (rc.query(select)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable<String, String> registro = (Hashtable<String, String>) fila.getRow(); 
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
	

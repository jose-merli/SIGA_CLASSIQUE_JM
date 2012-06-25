/*
 * Created on 25-ene-2005
 *
 */
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.general.SIGAException;

/**
 * @author daniel.campos
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CenNoColegiadoAdm extends MasterBeanAdministrador {

	/**
	 * @param tabla
	 * @param usuario
	 */
	public CenNoColegiadoAdm(UsrBean usuario) {
		super(CenNoColegiadoBean.T_NOMBRETABLA, usuario);
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
	 */
	protected String[] getCamposBean() {
		String [] campos = {CenNoColegiadoBean.C_FECHAMODIFICACION, CenNoColegiadoBean.C_USUMODIFICACION, 
							CenNoColegiadoBean.C_IDPERSONA,			
							CenNoColegiadoBean.C_IDINSTITUCION,		CenNoColegiadoBean.C_ANOTACIONES,
							CenNoColegiadoBean.C_SOCIEDADSJ,		CenNoColegiadoBean.C_TIPO,
							CenNoColegiadoBean.C_SUFIJO_NUMREG,     CenNoColegiadoBean.C_CONTADOR_NUMREG,
							CenNoColegiadoBean.C_PREFIJO_NUMREG,	CenNoColegiadoBean.C_FECHAFIN,
							CenNoColegiadoBean.C_RESENA, 			CenNoColegiadoBean.C_OBJETOSOCIAL,
							CenNoColegiadoBean.C_IDPERSONANOTARIO,	CenNoColegiadoBean.C_SOCIEDADSP,
							CenNoColegiadoBean.C_SUFIJO_NUMREGSP,   CenNoColegiadoBean.C_CONTADOR_NUMREGSP,
							CenNoColegiadoBean.C_PREFIJO_NUMREGSP,	CenNoColegiadoBean.C_NOPOLIZA,
							CenNoColegiadoBean.C_COMPANIASEG};
		return campos;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
	 */
	protected String[] getClavesBean() {
		String [] claves = {CenNoColegiadoBean.C_IDINSTITUCION,		CenNoColegiadoBean.C_IDPERSONA};
		return claves;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {
		return getClavesBean();
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#hashTableToBean(java.util.Hashtable)
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		CenNoColegiadoBean bean = null;
		try{
			bean = new CenNoColegiadoBean();
			bean.setFechaMod (UtilidadesHash.getString(hash, CenNoColegiadoBean.C_FECHAMODIFICACION));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, CenNoColegiadoBean.C_IDINSTITUCION));
			bean.setIdPersona(UtilidadesHash.getLong(hash, CenNoColegiadoBean.C_IDPERSONA));
			
			bean.setAnotaciones(UtilidadesHash.getString(hash, CenNoColegiadoBean.C_ANOTACIONES));
			bean.setSociedadSJ(UtilidadesHash.getString(hash, CenNoColegiadoBean.C_SOCIEDADSJ));
			bean.setTipo(UtilidadesHash.getString(hash, CenNoColegiadoBean.C_TIPO));
			bean.setUsuMod(UtilidadesHash.getInteger(hash, CenNoColegiadoBean.C_USUMODIFICACION));
			bean.setPrefijoNumReg(UtilidadesHash.getString(hash, CenNoColegiadoBean.C_PREFIJO_NUMREG));
			bean.setSufijoNumReg(UtilidadesHash.getString(hash, CenNoColegiadoBean.C_SUFIJO_NUMREG));
			bean.setContadorNumReg(UtilidadesHash.getInteger(hash, CenNoColegiadoBean.C_CONTADOR_NUMREG));
			bean.setFechaFin(UtilidadesHash.getString(hash, CenNoColegiadoBean.C_FECHAFIN));
			bean.setResena(UtilidadesHash.getString(hash, CenNoColegiadoBean.C_RESENA));
			bean.setObjetoSocial(UtilidadesHash.getString(hash, CenNoColegiadoBean.C_OBJETOSOCIAL));
			bean.setIdPersonaNotario(UtilidadesHash.getLong(hash, CenNoColegiadoBean.C_IDPERSONANOTARIO));
			bean.setSociedadSP(UtilidadesHash.getString(hash, CenNoColegiadoBean.C_SOCIEDADSP));
			bean.setPrefijoNumRegSP(UtilidadesHash.getString(hash, CenNoColegiadoBean.C_PREFIJO_NUMREGSP));
			bean.setSufijoNumRegSP(UtilidadesHash.getString(hash, CenNoColegiadoBean.C_SUFIJO_NUMREGSP));
			bean.setContadorNumRegSP(UtilidadesHash.getInteger(hash, CenNoColegiadoBean.C_CONTADOR_NUMREGSP));
			bean.setNoPoliza(UtilidadesHash.getString(hash, CenNoColegiadoBean.C_NOPOLIZA));
			bean.setCompaniaSeg(UtilidadesHash.getString(hash, CenNoColegiadoBean.C_COMPANIASEG));
		}
		catch(Exception e){
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#beanToHashTable(com.siga.beans.MasterBean)
	 */
	public Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try{
			hash = new Hashtable();
			CenNoColegiadoBean b = (CenNoColegiadoBean) bean;
			UtilidadesHash.set(hash, CenNoColegiadoBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(hash, CenNoColegiadoBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(hash, CenNoColegiadoBean.C_IDPERSONA, b.getIdPersona());
			UtilidadesHash.set(hash, CenNoColegiadoBean.C_ANOTACIONES, b.getAnotaciones());
			UtilidadesHash.set(hash, CenNoColegiadoBean.C_SOCIEDADSJ, b.getSociedadSJ());
			UtilidadesHash.set(hash, CenNoColegiadoBean.C_TIPO, b.getTipo());
			UtilidadesHash.set(hash, CenNoColegiadoBean.C_USUMODIFICACION, b.getUsuMod());	
			UtilidadesHash.set(hash, CenNoColegiadoBean.C_CONTADOR_NUMREG, b.getContadorNumReg());
			UtilidadesHash.set(hash, CenNoColegiadoBean.C_PREFIJO_NUMREG, b.getPrefijoNumReg());
			UtilidadesHash.set(hash, CenNoColegiadoBean.C_SUFIJO_NUMREG, b.getSufijoNumReg());
			UtilidadesHash.set(hash, CenNoColegiadoBean.C_FECHAFIN, b.getFechaFin());
			UtilidadesHash.set(hash, CenNoColegiadoBean.C_RESENA, b.getResena());
			UtilidadesHash.set(hash, CenNoColegiadoBean.C_OBJETOSOCIAL, b.getObjetoSocial());
			UtilidadesHash.set(hash, CenNoColegiadoBean.C_IDPERSONANOTARIO, b.getIdPersonaNotario());
			UtilidadesHash.set(hash, CenNoColegiadoBean.C_SOCIEDADSP, b.getSociedadSP());
			UtilidadesHash.set(hash, CenNoColegiadoBean.C_CONTADOR_NUMREGSP, b.getContadorNumRegSP());
			UtilidadesHash.set(hash, CenNoColegiadoBean.C_PREFIJO_NUMREGSP, b.getPrefijoNumRegSP());
			UtilidadesHash.set(hash, CenNoColegiadoBean.C_SUFIJO_NUMREGSP, b.getSufijoNumRegSP());
			UtilidadesHash.set(hash, CenNoColegiadoBean.C_NOPOLIZA, b.getNoPoliza());
			UtilidadesHash.set(hash, CenNoColegiadoBean.C_COMPANIASEG, b.getCompaniaSeg());
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	}
	private Vector getDatosInformeNoColegiado (String idInstitucion, String idPersona,String idioma, boolean isInforme
	) throws ClsExceptions  
	{
		Vector datos = null;
		try {
			Hashtable htCodigos = new Hashtable();
			int keyContador = 0;


			StringBuffer sql = new StringBuffer();
			sql.append(" ");
			sql.append(" SELECT PER.NOMBRE, PER.APELLIDOS1, PER.APELLIDOS2, ");
			sql.append(" PER.NIFCIF, PER.IDTIPOIDENTIFICACION, TO_CHAR(PER.FECHANACIMIENTO, 'dd-mm-yyyy') FECHANACIMIENTO, ");
			sql.append(" PER.IDESTADOCIVIL,PER.NATURALDE, PER.FALLECIDO, PER.SEXO, ");  

			keyContador++;
			htCodigos.put(new Integer(keyContador), idioma);
			sql.append(" F_SIGA_GETRECURSO(EC.DESCRIPCION, :");
			sql.append(keyContador);
			sql.append(") DESC_ESTADOCIVIL, ");
			keyContador++;
			htCodigos.put(new Integer(keyContador), idioma);

			sql.append(" F_SIGA_GETRECURSO(TI.DESCRIPCION, :");
			sql.append(keyContador);
			sql.append(") DESC_TIPOIDENTIFICACION, ");
			
			sql.append(" TO_CHAR(CLI.FECHAALTA, 'dd-mm-yyyy') FECHAALTA, CLI.CARACTER,      CLI.PUBLICIDAD, ");
			sql.append(" CLI.GUIAJUDICIAL, CLI.ABONOSBANCO, CLI.CARGOSBANCO, ");
			sql.append(" CLI.COMISIONES, CLI.IDTRATAMIENTO, CLI.IDLENGUAJE, CLI.FOTOGRAFIA, ");
			sql.append(" CLI.ASIENTOCONTABLE, TO_CHAR(CLI.FECHACARGA, 'dd-mm-yyyy') FECHACARGA, CLI.LETRADO, TO_CHAR(CLI.FECHAACTUALIZACION, 'dd-mm-yyyy') FECHAACTUALIZACION, ");
			sql.append(" TO_CHAR(CLI.FECHAEXPORTCENSO, 'dd-mm-yyyy') FECHAEXPORTCENSO, CLI.NOENVIARREVISTA, CLI.NOAPARECERREDABOGACIA, ");

			keyContador++;
			htCodigos.put(new Integer(keyContador), idioma);
			sql.append(" F_SIGA_GETRECURSO(TRA.DESCRIPCION, :");
			sql.append(keyContador);
			sql.append(") DESC_TRATAMIENTO, ");
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), idioma);
			sql.append(" F_SIGA_GETRECURSO(LEN.DESCRIPCION, :");
			sql.append(keyContador);
			sql.append(") DESC_LENGUAJE, ");
			
			
			sql.append(" NCOL.SERIE, NCOL.NUMEROREF, NCOL.SOCIEDADSJ, NCOL.TIPO, "); 
			sql.append(" NCOL.ANOTACIONES, NCOL.PREFIJO_NUMREG, NCOL.CONTADOR_NUMREG, ");
			sql.append(" NCOL.SUFIJO_NUMREG, TO_CHAR(NCOL.FECHAFIN, 'dd-mm-yyyy') FECHAFIN, NCOL.IDPERSONANOTARIO, ");
			sql.append(" NCOL.RESENA, NCOL.OBJETOSOCIAL, NCOL.SOCIEDADPROFESIONAL, ");
			sql.append(" NCOL.PREFIJO_NUMSSPP, NCOL.CONTADOR_NUMSSPP, ");
			sql.append(" NCOL.SUFIJO_NUMSSPP, NCOL.NOPOLIZA, NCOL.COMPANIASEG, ");
		    
			sql.append(" CLINOT.IDINSTITUCION IDINSTITUCION_NOTARIO, ");
			sql.append(" TO_CHAR(CLINOT.FECHAALTA, 'dd-mm-yyyy') FECHAALTA_NOTARIO, CLINOT.CARACTER CARACTER_NOTARIO, ");
			sql.append(" CLINOT.PUBLICIDAD PUBLICIDAD_NOTARIO, CLINOT.GUIAJUDICIAL GUIAJUDICIAL_NOTARIO, ");
			sql.append(" CLINOT.ABONOSBANCO ABONOSBANCO_NOTARIO, CLINOT.CARGOSBANCO CARGOSBANCO_NOTARIO, ");
			sql.append(" CLINOT.COMISIONES COMISIONES_NOTARIO, CLINOT.IDTRATAMIENTO IDTRATAMIENTO_NOTARIO, ");
			sql.append(" CLINOT.IDLENGUAJE IDLENGUAJE_NOTARIO, CLINOT.FOTOGRAFIA FOTOGRAFIA_NOTARIO, ");
			sql.append(" CLINOT.ASIENTOCONTABLE ASIENTOCONTABLE_NOTARIO,TO_CHAR(CLINOT.FECHACARGA, 'dd-mm-yyyy')  FECHACARGA_NOTARIO, ");
			sql.append(" CLINOT.LETRADO LETRADO_NOTARIO, TO_CHAR(CLINOT.FECHAACTUALIZACION, 'dd-mm-yyyy') FECHAACTUALIZACION_NOTARIO, ");
			sql.append(" TO_CHAR(CLINOT.FECHAEXPORTCENSO, 'dd-mm-yyyy') FECHAEXPORTCENSO_NOTARIO, CLINOT.NOENVIARREVISTA NOENVIARREVISTA_NOTARIO, ");
			sql.append(" CLINOT.NOAPARECERREDABOGACIA NOAPARECERREDABOGACIA, ");
		 
			sql.append(" PERNOT.NOMBRE NOMBRE_NOTARIO, PERNOT.APELLIDOS1 APELLIDOS1_NOTARIO, ");
			sql.append(" PERNOT.APELLIDOS2 APELLIDOS2_NOTARIO, PERNOT.NIFCIF NIFCIF_NOTARIO, ");
			sql.append(" PERNOT.IDTIPOIDENTIFICACION IDTIPOIDENTIFICACION_NOTARIO, ");
			sql.append(" TO_CHAR(PERNOT.FECHANACIMIENTO, 'dd-mm-yyyy') FECHANACIMIENTO_NOTARIO, ");
			sql.append(" PERNOT.IDESTADOCIVIL IDESTADOCIVIL_NOTARIO, ");
			sql.append(" PERNOT.NATURALDE NATURALDE_NOTARIO, PERNOT.FALLECIDO FALLECIDO_NOTARIO, ");
			sql.append(" PERNOT.SEXO SEXO_NOTARIO  ");
		       
			sql.append(" FROM CEN_NOCOLEGIADO          NCOL, ");
			sql.append(" CEN_CLIENTE            CLI, ");
			sql.append(" CEN_PERSONA            PER, ");
			sql.append(" CEN_ESTADOCIVIL        EC, ");
			sql.append(" CEN_TIPOIDENTIFICACION TI, ");
			sql.append(" CEN_TRATAMIENTO        TRA, ");
			sql.append(" ADM_LENGUAJES          LEN ");
			sql.append(" , CEN_CLIENTE            CLINOT, ");
			sql.append(" CEN_PERSONA            PERNOT ");

			sql.append(" WHERE NCOL.IDINSTITUCION(+) = CLI.IDINSTITUCION ");
			sql.append(" AND NCOL.IDPERSONA(+) = CLI.IDPERSONA ");
			//sql.append(" AND NCOL.IDPERSONA = PER.IDPERSONA ");
			sql.append(" AND CLI.IDPERSONA = PER.IDPERSONA ");
			sql.append(" AND PER.IDTIPOIDENTIFICACION = TI.IDTIPOIDENTIFICACION ");
			sql.append(" AND PER.IDESTADOCIVIL = EC.IDESTADOCIVIL(+) ");
			sql.append(" AND CLI.IDTRATAMIENTO = TRA.IDTRATAMIENTO ");
			sql.append(" AND CLI.IDLENGUAJE = LEN.IDLENGUAJE ");
			sql.append(" AND CLINOT.IDINSTITUCION(+) =  NCOL.IDINSTITUCION ");
			sql.append(" AND CLINOT.IDPERSONA(+) = NCOL.IDPERSONANOTARIO ");
			sql.append(" AND CLINOT.IDPERSONA = PERNOT.IDPERSONA(+) ");
			
			
			
			
			
			 
			keyContador++;
			htCodigos.put(new Integer(keyContador), idPersona);
			sql.append(" AND CLI.IDPERSONA = :");
			sql.append(keyContador);

			keyContador++;
			htCodigos.put(new Integer(keyContador), idInstitucion);
			sql.append(" AND CLI.IDINSTITUCION = :");
			sql.append(keyContador);



			if(isInforme){
				HelperInformesAdm helperInformes = new HelperInformesAdm();	
				datos = helperInformes.ejecutaConsultaBind(sql.toString(), htCodigos); 
				
			}else{
				RowsContainer rc = new RowsContainer();
				
				if (rc.queryBind(sql.toString(), htCodigos)) {
					datos = rc.getAll();
					
				}
			}

		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error ScsEJGAdm.getDatosInformeNoColegiado.");
		}
		return datos;
	}
	public Vector getInformeNoColegiado (String idInstitucion, String idPersona,String idioma,boolean isInforme)throws ClsExceptions {
		Vector vInforme = null;
		HelperInformesAdm helperInformes = new HelperInformesAdm();
		CenDireccionesAdm admDirecciones = null;
		try {
			
			vInforme = getDatosInformeNoColegiado(idInstitucion, idPersona, idioma, isInforme); 

			Hashtable registro = null;
			if(isInforme){
				registro = (Hashtable) vInforme.get(0);	
			}else{
				registro = ((Row) vInforme.get(0)).getRow();
			}
			

			
			//Añadimos los campos de la direccion preferente para el tipo de envio
			//AQUI TENGO DUDAS, NO SE SI ES ESTO LO QUE PIDEN. EN TAL CASO ME TENDRE QUE TRAER EL IDTIPOENVIO.
			//AHORA SE LO METO A PELO
			admDirecciones = new CenDireccionesAdm(usrbean);
			helperInformes.completarHashSalida(registro,admDirecciones.getDireccionPreferente(idInstitucion, idPersona, "1"));
			
		}catch (Exception e) {
			throw new ClsExceptions (e, "Error ScsEJGAdm.getInformeNoColegiado.");
		}
	return vInforme;

}
	public Hashtable getInformeNoColegiadoInforme (String idInstitucion, String idPersona,String idioma,boolean isInforme)throws ClsExceptions {
		Vector vInforme = null;
		Vector v= null;
		Vector vCuentabancaria= null;
		Hashtable total=new Hashtable();
		HelperInformesAdm helperInformes = new HelperInformesAdm();
		CenDireccionesAdm admDirecciones = null;
		CenCuentasBancariasAdm admCuentasBancarias = new CenCuentasBancariasAdm(usrbean);
		try {
			//Se recogen los componentes si los hubiera de BBDD
			//if(usrBean!=null){
				CenComponentesAdm componentesAdm = new CenComponentesAdm(usrbean);
				v= componentesAdm.selectComponentes(new Long(idPersona), new Integer(idInstitucion));									
			//}
												
			//Se recogen los datos paa el informe del No Colegiado
			vInforme = getDatosInformeNoColegiado(idInstitucion, idPersona, idioma, isInforme); 
			
			
			Hashtable registro = null;
			Hashtable registroContenido = null;
			Vector vInformeComp =new Vector();
			if(isInforme){
				//registro = (Hashtable) vInforme.get(0);

				if(v!=null && v.size()!=0)
				{
					for(int i=0;i<v.size();i++)
					{

						registroContenido = (Hashtable) v.get(i);
						registro = new Hashtable();
						registro.put("CARGO", registroContenido.get("CARGO"));
						registro.put("FECHACARGO", registroContenido.get("FECHACARGOINFORME"));
						registro.put("NIF_COMPONENTE", registroContenido.get("NIFCIF"));
						String nombreCompleto="";
						if(registroContenido.get("NOMBRE")!=null)
							nombreCompleto =(String)registroContenido.get("NOMBRE");
						if(registroContenido.get("APELLIDOS1")!=null)
							nombreCompleto =nombreCompleto +" "+(String)registroContenido.get("APELLIDOS1");
						if(registroContenido.get("APELLIDOS2")!=null)
							nombreCompleto =nombreCompleto +" "+(String)registroContenido.get("APELLIDOS2");
						registro.put("NOMBRE_COMPONENTE", nombreCompleto);
						if(registroContenido.get("EJERCIENTE")!=null && registroContenido.get("EJERCIENTE").equals("1"))
							registro.put("EJERCIENTE", UtilidadesString.getMensajeIdioma(usrbean,"censo.consultaDatosGenerales.literal.ejerciente"));
						else
							registro.put("EJERCIENTE", "");
				
						if(((String)registroContenido.get(CenComponentesBean.C_SOCIEDAD)).equals(ClsConstants.DB_FALSE)){
							registro.put("PARTICIPACION_SOCIEDAD_%",UtilidadesString.getMensajeIdioma(usrbean,"general.no"));
						}else{
							registro.put("PARTICIPACION_SOCIEDAD_%",UtilidadesString.getMensajeIdioma(usrbean,"general.yes"));							   	 				
						}
						vInformeComp.add(registro);
					}//fin for
				}
				else
					registro = (Hashtable) vInforme.get(0);	
								
			}else{
				registro = ((Row) vInforme.get(0)).getRow();
			}
						
			
			//Añadimos los campos de la direccion preferente para el tipo de envio
			//AQUI TENGO DUDAS, NO SE SI ES ESTO LO QUE PIDEN. EN TAL CASO ME TENDRE QUE TRAER EL IDTIPOENVIO.
			//AHORA SE LO METO A PELO
			admDirecciones = new CenDireccionesAdm(usrbean);
			helperInformes.completarHashSalida(registro,admDirecciones.getDireccionPreferente(idInstitucion, idPersona, "1"));
												
			helperInformes.completarHashSalida(registro,admCuentasBancarias.getCuentaCorrienteAbono(idInstitucion, idPersona));
			if(registro.get("CUENTABANCARIA_ABONO")==null)
				registro.put("CUENTABANCARIA_ABONO", "");
			helperInformes.completarHashSalida(registro,admCuentasBancarias.getCuentaCorrienteCargo(idInstitucion, idPersona));
			if(registro.get("CUENTABANCARIA_CARGO")==null)
				registro.put("CUENTABANCARIA_CARGO", "");
			helperInformes.completarHashSalida(registro,admCuentasBancarias.getCuentaCorrienteSJCS(idInstitucion, idPersona));
			if(registro.get("CUENTABANCARIA_SJCS")==null)
				registro.put("CUENTABANCARIA_SJCS", "");
												
			
			if(vInformeComp.size()!=0)
			{
				total.put("vInformeComp", vInformeComp);
			}
			
			if(registro.size()!=0)
			{
				total.put("vInforme", registro);
			}
		}catch (Exception e) {
			throw new ClsExceptions (e, "Error ScsEJGAdm.getInformeNoColegiado.");
		}
		return total;
					
}
	
		/**
	 * Comprueba si existe un colegiado
	 * 
	 * @param idPersona Long
	 * @param idInstitucion Integer
	 * @return CenColegiadoBean o null
	 */
	public CenNoColegiadoBean existeNoColegiado (Long idPersona) throws ClsExceptions, SIGAException {
		try	{
			
			CenNoColegiadoBean salida = null;
			Hashtable codigos = new Hashtable();
            codigos.put(new Integer(1),idPersona.toString());
            Vector v = this.selectBind(" WHERE IDPERSONA = :1 ",codigos);
			if (v != null && v.size () > 0)
				salida = (CenNoColegiadoBean) v.get(0);
			return salida;
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al consultar datos en B.D.");
		}
	} //existeNoColegiado ()
	
}
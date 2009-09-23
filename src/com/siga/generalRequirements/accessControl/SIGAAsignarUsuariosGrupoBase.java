package com.siga.generalRequirements.accessControl;

import java.util.*;
import javax.servlet.http.*;

import com.siga.Utilidades.UtilidadesString;
import com.siga.gui.processTree.*;
import com.atos.utils.*;

public class SIGAAsignarUsuariosGrupoBase
{
  protected HttpServletRequest req;

  public SIGAAsignarUsuariosGrupoBase()  {
  }

  public Hashtable getLiterals(HttpServletRequest _req) throws ClsExceptions {
    req=_req;
    Hashtable lits=new Hashtable();
    String[] keys =new String[] {
        "GUARDAR",
        "CANCELAR",
        "LITPROCESS",
        "LITACCESS",
        "LITASIGNARUSUARIOSGRUPO",
        SIGAPTConstants.ACCESS_DENY,
        SIGAPTConstants.ACCESS_FULL,
        SIGAPTConstants.ACCESS_READ,
        SIGAPTConstants.ACCESS_NONE,
        SIGAPTConstants.STRUC_CHANGED_reload,
        SIGAPTConstants.warning,
        SIGAPTConstants.STRUC_CHANGED,
        SIGAPTConstants.REG_CHANGED,
        SIGAPTConstants.PL_ERROR,
        SIGAPTConstants.REG_DELETED,
        SIGAPTConstants.PATER_RESTRIC,
        SIGAPTConstants.PARTIAL_PATER_RESTRIC,
        SIGAPTConstants.CONFIRM_SAVE,
        SIGAPTConstants.USUARIOS,
        SIGAPTConstants.DESASIGNAR_GRUPO,
        SIGAPTConstants.USUARIO_YA_ASIGNADO,
        SIGAPTConstants.MOVIMIENTO_NO_VALIDO,
        SIGAPTConstants.ASIGNAR_USUARIO_GRUPO
    };

    String[] idResource =new String[] {
        //"button.apply",
        "general.boton.guardar",
        //"button.cancel",
        "general.boton.cancel",
        "menu.tools.accesstype.process",
        "menu.tools.accesstype.accessright",
        "menu.tools.accesstype.asignarUsuariosGrupo",
        "process.access_deny",
        "process.access_full",
        "process.access_read",
        "process.access_none",
        "process.strct_change_reload",
        "process.warning",
        "process.strct_change",
        "error.messages.noupdated",
        "process.database_error",
        "error.messages.deleted",
        "process.pater_restric",
        "process.partial_pater_restric",
        "messages.confirm.updateData",
        "process.usuarios",
        "process.desasignar.grupo",
        "process.usuario.ya.asignado",
        "process.movimiento.no.valido",
        "process.asignar.grupo"
    };

//    String select="select " + ColumnConstants.SIGA_M
    HttpSession ses=req.getSession();
    UsrBean usrbean = (UsrBean)ses.getAttribute("USRBEAN");
    if (usrbean==null)
      throw new ClsExceptions("usrbean is null");

/*    String select="select DESC_RESOURCE from SIGA_MRESOURCES where ID_LANGUAGE='"+
        usrbean.getLanguage().toUpperCase()+"' and ID_RESOURCE='";

    Table gtTable = new Table(req, "SIGA_MRESOURCES",
                              "com.siga.generalRequirements.accessControl.SIGAGrDDBBObject");
*/
    lits.put("GUARDAR","Guardar");
    lits.put("CANCELAR","Cancelar");
    lits.put("LITPROCESS","Process Manager");
    lits.put("LITACCESS","Access Rights");
    lits.put("LITASIGNARUSUARIOSGRUPO","Asignación de Usuarios al Grupo");
    lits.put("ACCESS_DENY","Denegado");
    lits.put("ACCESS_FULL","Total");
    lits.put("ACCESS_READ","Lectura");
    lits.put("ACCESS_NONE","Ninguno");
    lits.put(SIGAPTConstants.STRUC_CHANGED_reload,"Estructura cambiada, ¿Quiere recargarla?");
    lits.put(SIGAPTConstants.warning,"Aviso");
    lits.put(SIGAPTConstants.STRUC_CHANGED,"Estructura cambiada");
    lits.put(SIGAPTConstants.REG_CHANGED,"Registro modificado");
    lits.put(SIGAPTConstants.PL_ERROR,"Database Error");
    lits.put(SIGAPTConstants.REG_DELETED,"Registro borrado");
    lits.put(SIGAPTConstants.PATER_RESTRIC,"El padre es más restrictivo que el hijo");
    lits.put(SIGAPTConstants.PARTIAL_PATER_RESTRIC,"Algún padre es más restrictivo que el hijo");
    lits.put(SIGAPTConstants.CONFIRM_SAVE,"¿Desea guardar los cambios?");
    lits.put(SIGAPTConstants.USUARIOS,"USUARIOS");
    lits.put(SIGAPTConstants.DESASIGNAR_GRUPO,"DESASIGNAR GRUPO/S");
    lits.put(SIGAPTConstants.USUARIO_YA_ASIGNADO,"El Usuario ya está asignado al Grupo");
    lits.put(SIGAPTConstants.MOVIMIENTO_NO_VALIDO,"Movimiento no válido");
    lits.put(SIGAPTConstants.ASIGNAR_USUARIO_GRUPO,"Debe asignar Usuarios a un Grupo o un Grupo a un Usuario");

    for (int h=0;h<keys.length;h++) {
      String text =  UtilidadesString.getMensajeIdioma(usrbean, idResource[h]);
      if (text!=null)
        lits.put(keys[h],text);
    }


/*    for (int h=0;h<keys.length;h++) {
      String select2=select+idResource[h]+"'";

      Vector vec=gtTable.search(select2);
      if (vec!=null && vec.size()>0) {
        lits.put(keys[h],((SIGAGrDDBBObject)vec.elementAt(0)).getString("DESC_RESOURCE"));
      }
    }  */
    return lits;
  }

  	protected SIGAUsuariosGruposHier getHier(Hashtable has, String padre, Hashtable hasorder) throws ClsExceptions 
  	{
  	    Enumeration e = has.keys();
  	    SIGAUsuariosGruposHier hier=null;
  	    
  	    while (e.hasMoreElements())
  	    {
  	        String id=(String)e.nextElement();
  	        SIGAUsuariosGruposHier hie=(SIGAUsuariosGruposHier)has.get(id);
  	        
  	        if (hie.getParent()!=null && !hie.getParent().equals(padre))
  	        {
	  	        SIGAUsuariosGruposHier myActHier=(SIGAUsuariosGruposHier)has.get(hie.getParent());
	      
	  	        myActHier.addChild((SIGAUsuariosGruposHier)has.get(hie.getId()));
  	        }
  	        
  	        else
  	        {
  	            hier=hie;
  	        }
  	    }
    
  	    if (hasorder!=null)
  	    {
  	        hier.sort(hasorder);
  	    }
    
  	    return hier;
  	}
}
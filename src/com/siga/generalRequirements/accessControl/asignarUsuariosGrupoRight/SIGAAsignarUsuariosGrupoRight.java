package com.siga.generalRequirements.accessControl.asignarUsuariosGrupoRight;

import java.util.*;

import com.atos.utils.*;
import com.siga.beans.*;

import javax.servlet.http.*;
import com.siga.gui.processTree.*;
import javax.transaction.UserTransaction;
import com.siga.generalRequirements.accessControl.*;

public class SIGAAsignarUsuariosGrupoRight extends SIGAAsignarUsuariosGrupoBase
{
    protected HttpServletRequest req;

    public SIGAAsignarUsuariosGrupoRight()
    { }

    public Vector loadGrupos(HttpServletRequest _req, String profile, String institucion, String nivel, Hashtable literales) throws ClsExceptions
    {
        Vector toRet = new Vector();
        
        Hashtable htAux = new Hashtable();
        
        String sDesasignar=(String)literales.get(SIGAPTConstants.DESASIGNAR_GRUPO);

        htAux.put(AdmPerfilBean.C_IDPERFIL, "");
        //htAux.put(AdmPerfilBean.C_DESCRIPCION, "DESASIGNAR GRUPO/S");
        htAux.put(AdmPerfilBean.C_DESCRIPCION, sDesasignar);
        htAux.put(AdmPerfilBean.C_IDINSTITUCION, "");

        toRet.add(htAux);
        
        Vector vAux = null;
        
        RowsContainer rc = new RowsContainer();
        
        String sql = " SELECT " + AdmPerfilBean.C_IDPERFIL + ", " +
        		     			  AdmPerfilBean.C_DESCRIPCION + ", " +
        		     			  AdmPerfilBean.C_IDINSTITUCION +
        		     " FROM " + AdmPerfilBean.T_NOMBRETABLA +
        		     " WHERE " + AdmPerfilBean.C_IDINSTITUCION + " = " + institucion +
        		     " ORDER BY " + AdmPerfilBean.C_DESCRIPCION;
        
        
        if (rc.findForUpdate(sql))
        {
            vAux = rc.getAll();
            
            for (int i=0; i<vAux.size(); i++)
            {
	            toRet.add(((Row)vAux.elementAt(i)).getRow());
            }
        }        

        return toRet;
    }
    
    public Vector loadUsuariosGrupos(HttpServletRequest _req, String profile, String institucion, String nivel, String nombreBusqueda, String idRolBusqueda, String idGrupoBusqueda, Hashtable literales) throws ClsExceptions 
    {
        req=_req;
        HttpSession ses = req.getSession();
        Table gtTable=null;
        gtTable = new Table(req, TableConstants.TABLE_ACCESS_RIGHT, "com.siga.generalRequirements.accessControl.SIGAGrDDBBObject");
        
        String query_usuarios = " SELECT U." + AdmUsuariosBean.C_IDUSUARIO + ", " +
        		                        "U." + AdmUsuariosBean.C_DESCRIPCION + " AS NOMBRE, " +
                                        "R." + AdmRolBean.C_IDROL + ", " +
        		                        "R." + AdmRolBean.C_DESCRIPCION + " AS ROL, " +
        		                        "P." + AdmPerfilBean.C_IDPERFIL + ", " +
                                        "P." + AdmPerfilBean.C_DESCRIPCION + " AS PERFIL" +
                                " FROM " + AdmUsuariosBean.T_NOMBRETABLA + " U, " +
                                           AdmPerfilBean.T_NOMBRETABLA + " P, " +
                                           AdmUsuarioEfectivoBean.T_NOMBRETABLA + " UE, " +
                                           AdmUsuariosEfectivosPerfilBean.T_NOMBRETABLA + " UEP, " +
                                           AdmRolBean.T_NOMBRETABLA + " R " +
                                " WHERE UE." + AdmUsuarioEfectivoBean.C_IDINSTITUCION + " = " + institucion + " AND " +
                                       "UE." + AdmUsuarioEfectivoBean.C_IDINSTITUCION + " = U." + AdmUsuariosBean.C_IDINSTITUCION + " AND " +
                                       "UE." + AdmUsuarioEfectivoBean.C_IDUSUARIO + " = U." + AdmUsuariosBean.C_IDUSUARIO + " AND " +
                                       "UE." + AdmUsuarioEfectivoBean.C_IDROL + " = R." + AdmRolBean.C_IDROL + " AND " + 
                                       "UE." + AdmUsuarioEfectivoBean.C_IDINSTITUCION + " = UEP." + AdmUsuariosEfectivosPerfilBean.C_IDINSTITUCION + "(+) AND " +
                                       "UE." + AdmUsuarioEfectivoBean.C_IDUSUARIO + " = UEP." + AdmUsuariosEfectivosPerfilBean.C_IDUSUARIO + "(+) AND " +
                                       "UE." + AdmRolBean.C_IDROL + " = UEP." + AdmUsuariosEfectivosPerfilBean.C_IDROL + "(+) AND " +
                                       "UEP." + AdmUsuariosEfectivosPerfilBean.C_IDINSTITUCION + " = P." + AdmPerfilBean.C_IDINSTITUCION + "(+) AND " +
                                       "UEP." + AdmUsuariosEfectivosPerfilBean.C_IDPERFIL + " = P." + AdmPerfilBean.C_IDPERFIL + "(+) AND " +
        							   "U." + AdmUsuariosBean.C_ACTIVO + " = 'S'";
        
        
        if (nombreBusqueda != null && !nombreBusqueda.trim().equals(""))
        {
            query_usuarios += " AND "+ComodinBusquedas.prepararSentenciaCompleta(nombreBusqueda.trim(),"U." + AdmUsuariosBean.C_DESCRIPCION);
        }
        
        if (idRolBusqueda!=null && !idRolBusqueda.trim().equals(""))
        {
            query_usuarios += " AND UE." + AdmUsuarioEfectivoBean.C_IDROL + " ='" + idRolBusqueda + "' ";
        } 
         
        if (idGrupoBusqueda!=null && !idGrupoBusqueda.trim().equals(""))
        {
            query_usuarios += " AND '" + idGrupoBusqueda + "' IN (" +
                              " SELECT N." + AdmUsuariosEfectivosPerfilBean.C_IDPERFIL + 
                              " FROM " + AdmUsuariosEfectivosPerfilBean.T_NOMBRETABLA + " N " +
                              " WHERE N." + AdmUsuariosEfectivosPerfilBean.C_IDINSTITUCION + " = UE." + AdmUsuarioEfectivoBean.C_IDINSTITUCION + " AND " +
                              " N." + AdmUsuariosEfectivosPerfilBean.C_IDUSUARIO + " = UE." + AdmUsuarioEfectivoBean.C_IDUSUARIO + " AND " +
                              " N." +AdmUsuariosEfectivosPerfilBean.C_IDROL + " = UE." + AdmUsuarioEfectivoBean.C_IDROL + ")";
        }
        
        query_usuarios += " ORDER BY U." + AdmUsuariosBean.C_DESCRIPCION;
        
        Hashtable hashUsuarios = new Hashtable();

        Vector vUsuarios = gtTable.searchForUpdateNLS(query_usuarios);
        
        int iUsuarios = 0;
        
        if (vUsuarios!=null) 
        {
            iUsuarios = vUsuarios.size();
            
            for (int i=0;i<iUsuarios;i++) 
            {
                SIGAGrDDBBObject obj=(SIGAGrDDBBObject)vUsuarios.elementAt(i);
                hashUsuarios.put(obj.getString(AdmUsuariosBean.C_IDUSUARIO) + "-" + obj.getString(AdmRolBean.C_IDROL), obj);
            }
        }

        ses.setAttribute("vtrbackup", hashUsuarios);

        Vector toRet=new Vector();
        
        Vector hiers=new Vector();
        Hashtable has=new Hashtable();
        Hashtable has2=new Hashtable();
        
        //Nodo raíz
        SIGAUsuariosGruposHier hierAct = new SIGAUsuariosGruposHier("-1", "");

        hiers.add(hierAct);
        has.put("-1", hierAct);

        SIGAUsuarioRolObj object=new SIGAUsuarioRolObj();
        //object.put(SIGABaseNode.NAME, "USUARIOS");
        String lit=(String)literales.get("USUARIOS");
        object.put(SIGABaseNode.NAME, "USUARIOS");
        object.modified = false;
        object.setActionMove(SIGAPTConstants.SAVE_USUARIOS_GRUPO);
        
        has2.put("-1", object);

        for (int h=0;h<iUsuarios;h++)
        {
            //Usuario + Rol
            SIGAGrDDBBObject obj=(SIGAGrDDBBObject)vUsuarios.elementAt(h);
            
            String idUsuario = (String)obj.getString(AdmUsuariosBean.C_IDUSUARIO);
            String descUsuario = (String)obj.getString("NOMBRE");
            String idRol = (String)obj.getString(AdmRolBean.C_IDROL);
            String descRol = (String)obj.getString("ROL");
            String idPerfil = (String)obj.getString(AdmPerfilBean.C_IDPERFIL);
            String descPerfil = (String)obj.getString("PERFIL");
            
            String idUsuarioMasidRol = idUsuario + "-" + idRol;
            
            hierAct = new SIGAUsuariosGruposHier(idUsuarioMasidRol, "-1");

            hiers.add(hierAct);
            has.put(idUsuarioMasidRol, hierAct);

            object=new SIGAUsuarioRolObj();
            
            object.put(SIGABaseNode.NAME, descUsuario + " / " + descRol);
            object.setOldState((String)object.get(SIGABaseNode.ACCESS));
            object.setActionMove(SIGAPTConstants.SAVE_USUARIOS_GRUPO);
            object.setIdUsuario(idUsuario);
            object.setIdRol(idRol);

            has2.put(idUsuarioMasidRol, object);

            if (idPerfil!=null && !idPerfil.trim().equals(""))
            {
	            //Grupo
                hierAct = new SIGAUsuariosGruposHier(idUsuarioMasidRol + "-" + idPerfil, idUsuarioMasidRol);
	
	            hiers.add(hierAct);
	            has.put(idUsuarioMasidRol + "-" + idPerfil, hierAct);
	            
	            SIGAGrupoObj object2=new SIGAGrupoObj();
	            object2.put(SIGABaseNode.NAME, descPerfil);
	            object2.setIdPerfil(idPerfil);
	            object2.setIdInstitucion(institucion);
	            
	            has2.put(idUsuarioMasidRol + "-" + idPerfil, object2);
            }
        }

        SIGAUsuariosGruposHier hier=getHier(has, "", has2);
        toRet.add(hier);
        toRet.add(has2);

        ses.setAttribute("hierbckp", hier);

        return toRet;
    }

    //public Vector modify(HttpServletRequest _req, String profile, String institucion, String nivel, String[] changes, String nombreBusqueda, String idRolBusqueda, String idGrupoBusqueda) throws Exception 
    public Vector modify(HttpServletRequest _req, String profile, String institucion, String nivel, String[] changes) throws Exception
    {
        req=_req;
        Vector vec=new Vector();
        UserTransaction tx =null;
        
        try 
        {
            HttpSession ses = req.getSession();
            com.atos.utils.UsrBean usrbean = (com.atos.utils.UsrBean)ses.getAttribute("USRBEAN");
            
            if (usrbean==null)
            {
                throw new ClsExceptions("Sesión no válida.");
            }

            tx = usrbean.getTransaction();
            tx.begin();

            for (int h=0;h<changes.length;h++) 
            {
                StringTokenizer tok=new StringTokenizer(changes[h],"-");
                modify(_req,tok.nextToken(), tok.nextToken(), profile, institucion);
            }

            tx.commit();
            vec.add("OK");
            //vec.add(loadUsuariosGrupos(req, profile, institucion, nivel, nombreBusqueda, idRolBusqueda, idGrupoBusqueda));
        } 
        
        catch (Exception e) 
        {
            e.printStackTrace();
            
            if (tx!=null)
            {
                tx.rollback();
            }
      
            vec.add("Exception");
            vec.add(e.getMessage());
      
            return vec;
        }
    
        return vec;
    }

    public void modify(HttpServletRequest _req, String oidTarget, String oidMoved, String profile, String institucion) throws Exception
    {
        req=_req;

        String access="0";
    
        if (SIGABaseNode.ACCESS_DENY.equals(oidMoved)) 
        {
            access="1";
        } 
        
        else if (SIGABaseNode.ACCESS_READ.equals(oidMoved)) 
        {
            access="2";
        } 
        
        else if (SIGABaseNode.ACCESS_FULL.equals(oidMoved)) 
        {
            access="3";
        }

        Vector vec=new Vector();

    /*String selectProcessPater="select a." + ColumnConstants.FN_PROCESS_ID +
                 " from (select " +
                 ColumnConstants.FN_PROCESS_ID_PARENT +" from " + TableConstants.TABLE_PROCESS +
                 " where " +
                 ColumnConstants.FN_PROCESS_ID +"='"+oidTarget+"') l,  "+
                 TableConstants.TABLE_PROCESS + " a  " +
                 " where a."+ColumnConstants.FN_PROCESS_ID+"=l."+
                 ColumnConstants.FN_PROCESS_ID_PARENT;*/
 
        String padre="select " + ColumnConstants.FN_ACCESS_RIGHT_VALUE +
        			 " from (select " +
        			 ColumnConstants.FN_PROCESS_ID_PARENT +" from " + TableConstants.TABLE_PROCESS +
        			 " where " +
        			 ColumnConstants.FN_PROCESS_ID +"='"+oidTarget+"') l,  "+
        			 TableConstants.TABLE_PROCESS + " a, " + TableConstants.TABLE_ACCESS_RIGHT +
        			 " b where a."+ColumnConstants.FN_PROCESS_ID+"=l."+
        			 ColumnConstants.FN_PROCESS_ID_PARENT +
        			 " and a."+ColumnConstants.FN_PROCESS_ID+"=b."+
        			 ColumnConstants.FN_ACCESS_RIGHT_PROCESS +
        			 " and b."+ ColumnConstants.FN_ACCESS_RIGHT_PROFILE+"='"+profile+"'" +
        			 " and b."+ ColumnConstants.FN_ACCESS_RIGHT_INSTITUCION+"="+institucion;

        String query_pro="select " +
                     	ColumnConstants.FN_PROCESS_ID +
                     	", " + ColumnConstants.FN_PROCESS_DESC +
                     	", " + ColumnConstants.FN_PROCESS_ID_PARENT + " from " +
                     	TableConstants.TABLE_PROCESS;

        Table gtTable=null;
        gtTable = new Table(req, TableConstants.TABLE_ACCESS_RIGHT, "com.siga.generalRequirements.accessControl.SIGAGrDDBBObject");

    // primero se testea que el padre tiene permisos superiores, aunque esto se va a hacer en local
    // Existe pade?
    /*Vector pater=gtTable.search(selectProcessPater);
    if (pater!=null && pater.size()>0) {
      SIGAGrDDBBObject pat=(SIGAGrDDBBObject)pater.elementAt(0);
      pater=gtTable.search(padre);
      if (pater== null || pater.size()<1) {
        throw new Exception(SIGAPTConstants.PATER_RESTRIC);
      }

      String paterAccess=((SIGAGrDDBBObject)pater.elementAt(0)).getString("ACCESS_RIGHT");
      if (paterAccess==null || access.compareTo(paterAccess)>0) {
        throw new Exception(SIGAPTConstants.PATER_RESTRIC);
      }
    }*/

        // Luego se testea que no ha cambiado la estructura
        HttpSession ses = req.getSession();
        //SIGAProcessHier hier=(SIGAProcessHier)ses.getAttribute("hierbckp");
        SIGAUsuariosGruposHier hier=(SIGAUsuariosGruposHier)ses.getAttribute("hierbckp");
        Vector processHier=gtTable.searchForUpdate(query_pro);
        Hashtable hiers=new Hashtable();
        Hashtable names=new Hashtable();
    
        for (int h=0;h<processHier.size();h++) 
        {
            SIGAGrDDBBObject obj=(SIGAGrDDBBObject)processHier.elementAt(h);
            SIGAProcessHier hierAct=new SIGAProcessHier((String)obj.getString(ColumnConstants.FN_PROCESS_ID), (String)obj.getString(ColumnConstants.FN_PROCESS_ID_PARENT));
            hiers.put(hierAct.getId(),hierAct);

            SIGAGrupoObj object=new SIGAGrupoObj();
            object.put(SIGABaseNode.NAME,obj.getString(ColumnConstants.FN_PROCESS_DESC));
            object.put(SIGABaseNode.OID,obj.getString(ColumnConstants.FN_PROCESS_ID));

            names.put((String)obj.getString(ColumnConstants.FN_PROCESS_ID), object);
        }
    
        //SIGAProcessHier actHier=getHier(hiers,"",names);
        SIGAUsuariosGruposHier actHier=getHier(hiers,"",names);
    
        if (!actHier.comparePath(hier,oidTarget)) 
        {
            throw new Exception(SIGAPTConstants.STRUC_CHANGED);
        }

        // Y al final se modifica el registro que sus hijos para cambiar los permisos a iguales o inferiores
        Hashtable hashBackup=(Hashtable)ses.getAttribute("vtrbackup");

        Vector ret=updateAccess(oidTarget, profile, institucion, access, ses, hashBackup, true);
    
        if (ret.size()!=0) 
        {
            throw new Exception(SIGAPTConstants.REG_CHANGED);
        }

    /*Object paramsPL[]={oidTarget,
                         profile,
                         access};
    String namePL = "{call ?:=PKG_SIGA_PROCESS.accessRight(?,?,?)}";
    executePL(namePL,paramsPL);*/
    }

    protected Vector updateAccess(String process, String profile, String institucion, String accessRight,HttpSession ses, Hashtable hashBackup, boolean pater) throws ClsExceptions 
    {
	    String rec="";
	    Vector vec=new Vector();
	    Table gtTable=null;
	    gtTable = new Table(req, TableConstants.TABLE_ACCESS_RIGHT, "com.siga.generalRequirements.accessControl.SIGAGrDDBBObject");
	    gtTable.addFilter(ColumnConstants.FN_ACCESS_RIGHT_PROCESS,process);
	    gtTable.addFilter(ColumnConstants.FN_ACCESS_RIGHT_PROFILE,profile);
	    gtTable.addFilter(ColumnConstants.FN_ACCESS_RIGHT_INSTITUCION,institucion);
	    Vector vecRows=gtTable.search(true);
	    Hashtable hasNewUpd=new Hashtable();
	    hasNewUpd.put(ColumnConstants.FN_ACCESS_RIGHT_PROCESS,process);
	    hasNewUpd.put(ColumnConstants.FN_ACCESS_RIGHT_PROFILE,profile);
	    hasNewUpd.put(ColumnConstants.FN_ACCESS_RIGHT_INSTITUCION,institucion);
	    hasNewUpd.put(ColumnConstants.FN_ACCESS_RIGHT_VALUE,accessRight);
	    
	    hasNewUpd.put(ColumnConstants.FN_ACCESS_RIGHT_DATEMOD ,"SYSDATE");
	    hasNewUpd.put(ColumnConstants.FN_ACCESS_RIGHT_USERMOD ,((UsrBean)ses.getAttribute("USRBEAN")).getUserName());

	    if (vecRows==null || vecRows.size()==0) 
	    {
	        SIGAGrDDBBObject processRight=new SIGAGrDDBBObject(req);
	        processRight.setData(hasNewUpd);
	        processRight.add(TableConstants.TABLE_ACCESS_RIGHT);
	    } 
	    
	    else  
	    {
	        if (!pater) 
	        {
	            SIGAGrDDBBObject obj=(SIGAGrDDBBObject)vecRows.elementAt(0);
	            
	            if (accessRight.compareTo(obj.getString(ColumnConstants.FN_ACCESS_RIGHT_VALUE))>=0) 
	            {
	                return vec;
	            }
	        }
      
	        SIGAGrDDBBObject hasbk=(SIGAGrDDBBObject)hashBackup.get(process);
	        SIGAGrDDBBObject processRight = (SIGAGrDDBBObject)vecRows.elementAt(0);
	        processRight.setDataBackup(hasbk.getData());
	        processRight.setData(hasNewUpd);

	        processRight.update(TableConstants.TABLE_ACCESS_RIGHT, new String[]{ColumnConstants.FN_ACCESS_RIGHT_PROCESS, ColumnConstants.FN_ACCESS_RIGHT_PROFILE, ColumnConstants.FN_ACCESS_RIGHT_INSTITUCION}, new String[]{ColumnConstants.FN_ACCESS_RIGHT_VALUE});

	        Vector ve = (Vector)ses.getAttribute("vectRecords");
      
	        if (ve!=null)
	        {
	            ClsLogging.writeFileLog("ve = "+ve,req,3);
	            
	            if (ve.size()>0)
	            {
	                ClsLogging.writeFileLog("ve = "+ve.size(),req,3);
	                java.util.Enumeration en=ve.elements();
          
	                while (en.hasMoreElements())
	                {
	                    com.atos.utils.PairsKeys obj = (com.atos.utils.PairsKeys)en.nextElement();
	                    rec=rec+", "+obj.getIdObj();
	                }
	            }
	            
	            vec.add("exception");
	            vec.add("modified");
	            vec.add(rec);
	        }
	    }
    
	    return vec;
    }

    public void executePL(String namePL,Object params[]) throws Exception 
    {
        String res=ClsMngBBDD.callPLFunction(namePL,params);
    
        if(!res.equals("0"))
        {
            throw new Exception(SIGAPTConstants.PL_ERROR + " " + res);
        }
    }	
    
    public static void crear (HttpServletRequest request, String[] aCrear) throws Exception
    {
        UsrBean usrbean = (UsrBean)request.getSession().getAttribute("USRBEAN");
        String idUsuario = usrbean.getUserName();
        
        if (aCrear!=null)
        {
	        for (int i=0; i<aCrear.length; i++)
	        {
	            String sElemento = aCrear[i];
	            
	            StringTokenizer st = new StringTokenizer(sElemento, "*");
	            
	            String sIdUsuario = st.nextToken();
	            String sIdInstitucion = st.nextToken();
	            String sIdRol = st.nextToken();
	            String sIdPerfil = st.nextToken();
	            
	            AdmUsuariosEfectivosPerfilBean beanNuevo = new AdmUsuariosEfectivosPerfilBean();
	            
	            beanNuevo.setIdUsuario(sIdUsuario);
	            beanNuevo.setIdInstitucion(sIdInstitucion);
	            beanNuevo.setIdRol(sIdRol);
	            beanNuevo.setIdPerfil(sIdPerfil);
	            
	            AdmUsuariosEfectivosPerfilAdm admBean = new AdmUsuariosEfectivosPerfilAdm(usrbean);
	            if (!admBean.insert(beanNuevo)) {
	                throw new ClsExceptions("Error al insertar usuario en grupo: "+admBean.getError());
	            }
	        }
        }
    }

    public static void borrar (HttpServletRequest request, String[] aBorrar) throws Exception
    {
        UsrBean usrbean = (UsrBean)request.getSession().getAttribute("USRBEAN");
        String idUsuario = usrbean.getUserName();
        
        if (aBorrar!=null)
        {
	        for (int i=0; i<aBorrar.length; i++)
	        {
	            String sElemento = aBorrar[i];
	            
	            StringTokenizer st = new StringTokenizer(sElemento, "*");
	            
	            String sIdUsuario = st.nextToken();
	            String sIdInstitucion = st.nextToken();
	            String sIdRol = st.nextToken();
	            String sIdPerfil = st.nextToken();
	            
	            AdmUsuariosEfectivosPerfilBean beanViejo = new AdmUsuariosEfectivosPerfilBean();
	            
	            beanViejo.setIdUsuario(sIdUsuario);
	            beanViejo.setIdInstitucion(sIdInstitucion);
	            beanViejo.setIdRol(sIdRol);
	            beanViejo.setIdPerfil(sIdPerfil);
	            
	            AdmUsuariosEfectivosPerfilAdm admBean = new AdmUsuariosEfectivosPerfilAdm(usrbean);
	            if (!admBean.delete(beanViejo)) {
	                throw new ClsExceptions("Error al borrar usuario en grupo: "+admBean.getError());
	            }
	        }
	    }
    }
}
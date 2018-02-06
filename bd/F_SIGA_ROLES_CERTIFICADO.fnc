CREATE OR REPLACE FUNCTION F_SIGA_ROLES_CERTIFICADO(
       NUMSERIE IN VARCHAR2,IDINSTITUCION IN NUMBER,IDUSUARIO IN NUMBER)
return varchar2 is
/****************************************************************************************************************/
/* Nombre:        F_SIGA_PERFILES_USUARIO                                                                   */
/* Descripcion:   Funcion para la optencion de los codigos de los perfiles a los que pertenece un usuario,      */
/*                separados por ;                                                                               */
/*                                                                                                          */
/* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
/* -------------------   ------   ------------------------------------------------------------   -------------  */
/* CODIGO_USUAIRO        IN       Codigo de usuario                                             NUMBER(5)      */
/*                                                                                                  */
/* Version:        1.0                                                                              */
/* Fecha Creacion: 14/12/2004                                                                                   */
/* Autor:         Jose Zulueta                                                                        */
/* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
/* ------------------   ---------------------------------   --------------------------------------------------- */
/****************************************************************************************************************/


 V_RESULT          VARCHAR2(2000);
 V_ROLES           VARCHAR2 (50);
 TYPE CTIPO        IS REF CURSOR ;
 CL                CTIPO;
 BEGIN
 
 OPEN CL for 'SELECT R.DESCRIPCION FROM ADM_ROL R, ADM_USUARIO_EFECTIVO U WHERE U.IDINSTITUCION='||IDINSTITUCION||' AND U.IDUSUARIO='||IDUSUARIO||' AND U.NUMSERIE='||CHR(39)||NUMSERIE||CHR(39)||' AND U.IDROL=R.IDROL';
  LOOP
    FETCH CL INTO V_ROLES;
    EXIT WHEN CL%NOTFOUND;
     V_RESULT := V_RESULT || V_ROLES || '; ';
    END LOOP;
  CLOSE CL;
  V_RESULT := RTRIM((V_RESULT));
  V_RESULT :=(substr(V_RESULT,1,(length(V_RESULT)-1)));
  RETURN V_RESULT;

END F_SIGA_ROLES_CERTIFICADO;
 
/

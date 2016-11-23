Pkg_Siga_Censo;

Declare
  Cursor c_direccionesduplicadas Is
  Select dir.*
    From Cen_Direcciones Dir, Cen_Direccion_Tipodireccion Tip
   Where Dir.Idinstitucion = Tip.Idinstitucion
     And Dir.Idpersona = Tip.Idpersona
     And Dir.Iddireccion = Tip.Iddireccion
     And Dir.Fechabaja Is Null
     And Dir.Idinstitucion = 2000
     And Tip.Idtipodireccion In (3, 9)
     And Exists (Select 1
            From Cen_Direcciones Dir2, Cen_Direccion_Tipodireccion Tip2
           Where Dir2.Idinstitucion = Tip2.Idinstitucion
             And Dir2.Idpersona = Tip2.Idpersona
             And Dir2.Iddireccion = Tip2.Iddireccion
             And Dir2.Fechabaja Is Null
             
             And Tip.Idinstitucion = Tip2.Idinstitucion
             And Tip.Idpersona = Tip2.Idpersona
             And Tip.Idtipodireccion = Tip2.Idtipodireccion
             And Tip.Iddireccion > Tip2.Iddireccion);
  p_Codretorno Varchar2(4000);
  p_Datoserror Varchar2(4000);
Begin
  For r_dir In c_direccionesduplicadas Loop
    Pkg_Siga_Censo.Actualizardatosletrado(r_dir.Idpersona, r_dir.Idinstitucion, 30, r_dir.Iddireccion, 0, p_Codretorno, p_Datoserror);
  End Loop;
End;
/

-- Entregado a version por AAG el 14/11 a las 11:25

-- 124_004

UPDATE GEN_PARAMETROS P SET P.VALOR = 0, P.FECHAMODIFICACION = SYSDATE, P.USUMODIFICACION = 0
WHERE P.PARAMETRO = 'PCAJG_RESP_RESOL_FTP_ACTIVO' AND P.IDINSTITUCION IN (2026,2030,2041,2047,2048,2057,2059,2061,2072,2075,2079);

modificadas vistas: V_PCAJG_EJG.sql, V_PCAJG_FAMILIARES.sql, V_PCAJG_MARCASEXPEDIENTES.sql

-- Ejecutado en Integracion por AAG el 21/11 a las 13:18

-- 124_005

insert into gen_recursos values ('censo.regtel.literal.name', 'Nombre', 0, 1, sysdate, 0, 19);
insert into gen_recursos values ('censo.regtel.literal.name', 'Nombre#CA', 0, 2, sysdate, 0, 19);
insert into gen_recursos values ('censo.regtel.literal.name', 'Nombre#EU', 0, 3, sysdate, 0, 19);
insert into gen_recursos values ('censo.regtel.literal.name', 'Nombre#GL', 0, 4, sysdate, 0, 19);
 -- Add/modify columns 
alter table CEN_NOCOLEGIADO add IDENTIFICADORDS VARCHAR2(20);

INSERT INTO gen_procesos
  (idproceso, idmodulo, traza, target, fechamodificacion, usumodificacion, descripcion, transaccion, idparent, nivel)
VALUES
  ('227', 'CEN', 1, 'Y', SYSDATE, 0, 'RegTel_NoColegiado', 'CEN_NoColegiado_DocumentacionRegTel', 500, 10);

INSERT INTO gen_pestanas
  (idproceso, idlenguaje, idrecurso, posicion, idgrupo, tipoacceso)
VALUES
  ('227', 1, 'pestana.auditoriaexp.RegTel', 18, 'FICHACLIEN', NULL);
  
INSERT INTO gen_parametros
    (modulo, parametro, valor, fechamodificacion, usumodificacion, idinstitucion, idrecurso)
  VALUES
    ('GEN',
     'PATH_DOCUSHARE_NOCOLEGIADO',
     'SIN CONFIGURAR',
     SYSDATE,
     0,
     0,
     'administracion.parametro.path_docushare_censo');  
     
INSERT INTO gen_parametros
    (modulo, parametro, valor, fechamodificacion, usumodificacion, idinstitucion, idrecurso)
  VALUES
    ('GEN',
     'ID_DOCUSHARE_NOCOLEGIADO',
     'SIN CONFIGURAR',
     SYSDATE,
     0,
     0,
     'administracion.parametro.id_docushare_censo');

-- Ejecutado en Integracion por AAG el 23/11 a las 13:05


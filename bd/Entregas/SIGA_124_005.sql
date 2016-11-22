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
     '',
     SYSDATE,
     0,
     0,
     'administracion.parametro.path_docushare_censo');  
     
INSERT INTO gen_parametros
    (modulo, parametro, valor, fechamodificacion, usumodificacion, idinstitucion, idrecurso)
  VALUES
    ('GEN',
     'ID_DOCUSHARE_NOCOLEGIADO',
     '',
     SYSDATE,
     0,
     0,
     'administracion.parametro.id_docushare_censo');
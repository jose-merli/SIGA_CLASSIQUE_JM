
coger createTablesJustificacion.sql

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.ecom.justificacion', 'Justificaci�n', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.ecom.justificacion', 'Justificaci�n#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.ecom.justificacion', 'Justificaci�n#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.ecom.justificacion', 'Justificaci�n#GL', 0, '4', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.ecom.certificacion', 'Certificaci�n', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.ecom.certificacion', 'Certificaci�n#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.ecom.certificacion', 'Certificaci�n#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.ecom.certificacion', 'Certificaci�n#GL', 0, '4', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.ecom.devolucion', 'Devoluci�n', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.ecom.devolucion', 'Devoluci�n#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.ecom.devolucion', 'Devoluci�n#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.ecom.devolucion', 'Devoluci�n#GL', 0, '4', sysdate, 0, '19');

insert into GEN_PROCESOS
  (IDPROCESO,   IDMODULO,   TRAZA,   TARGET,   FECHAMODIFICACION,   USUMODIFICACION,   DESCRIPCION,   TRANSACCION,   IDPARENT,   NIVEL)
values
  ('710',   'JGR',   1,   'Y',   sysdate,   0,   'Justificaci�n econ�mica',   'JGR_E-Comunicaciones_Justificacion',   '007',   10);
    insert into GEN_PROCESOS
  (IDPROCESO,   IDMODULO,   TRAZA,   TARGET,   FECHAMODIFICACION,   USUMODIFICACION,   DESCRIPCION,   TRANSACCION,   IDPARENT,   NIVEL)
values
  ('711',   'JGR',   1,   'Y',   sysdate,   0,   'Certificaci�n econ�mica',   'JGR_E-Comunicaciones_Certificacion',   '007',   10);
  
  insert into GEN_PROCESOS
  (IDPROCESO,   IDMODULO,   TRAZA,   TARGET,   FECHAMODIFICACION,   USUMODIFICACION,   DESCRIPCION,   TRANSACCION,   IDPARENT,   NIVEL)
values
  ('712',   'JGR',   1,   'Y',   sysdate,   0,   'Devoluci�n econ�mica',   'JGR_E-Comunicaciones_Devolucion',   '007',   10);

insert into GEN_MENU (IDMENU, ORDEN, TAGWIDTH, IDPARENT, FECHAMODIFICACION, USUMODIFICACION, URI_IMAGEN, IDRECURSO, GEN_MENU_IDMENU, IDPROCESO, IDLENGUAJE)
values ('710', 32210, 160, '606', sysdate, 0, null, 'menu.sjcs.ecom.justificacion', null, '710', '1');

insert into GEN_MENU (IDMENU, ORDEN, TAGWIDTH, IDPARENT, FECHAMODIFICACION, USUMODIFICACION, URI_IMAGEN, IDRECURSO, GEN_MENU_IDMENU, IDPROCESO, IDLENGUAJE)
values ('711', 32020, 160, '606', sysdate, 0, null, 'menu.sjcs.ecom.certificacion', null, '711', '1');

insert into GEN_MENU (IDMENU, ORDEN, TAGWIDTH, IDPARENT, FECHAMODIFICACION, USUMODIFICACION, URI_IMAGEN, IDRECURSO, GEN_MENU_IDMENU, IDPROCESO, IDLENGUAJE)
values ('712', 32030, 160, '606', sysdate, 0, null, 'menu.sjcs.ecom.devolucion', null, '712', '1');



insert into fcs_je_maestroestados (idestado, descripcion, codigoext, fechamodificacion, usumodificacion) values(10,'INICIAL','1',sysdate,-1,0);
insert into fcs_je_maestroestados (idestado, descripcion, codigoext, fechamodificacion, usumodificacion,propietario) values(12,'VALIDANDO','1',sysdate,-1,0);
insert into fcs_je_maestroestados (idestado, descripcion, codigoext, fechamodificacion, usumodificacion,propietario)values(14,'VALIDADO CORRECTO','1',sysdate,-1,0);
insert into fcs_je_maestroestados (idestado, descripcion, codigoext, fechamodificacion, usumodificacion,propietario)values(16,'NO V�LIDO','1',sysdate,-1,0);
insert into fcs_je_maestroestados (idestado, descripcion, codigoext, fechamodificacion, usumodificacion,propietario)values(20,'ENVIANDO CICAC','1',sysdate,-1,0);

insert into fcs_je_maestroestados (idestado, descripcion, codigoext, fechamodificacion, usumodificacion,propietario)values(30,'ENVIADO CICAC','1',sysdate,-1,1);
insert into fcs_je_maestroestados (idestado, descripcion, codigoext, fechamodificacion, usumodificacion,propietario)values(40,'ERROR CICAC','1',sysdate,-1,1);
insert into fcs_je_maestroestados (idestado, descripcion, codigoext, fechamodificacion, usumodificacion,propietario)values(50,'FIN CICAC','1',sysdate,-1,1);
insert into fcs_je_maestroestados (idestado, descripcion, codigoext, fechamodificacion, usumodificacion,propietario)values(52,'RESPONDIENDO CA','1',sysdate,-1,1);
insert into fcs_je_maestroestados (idestado, descripcion, codigoext, fechamodificacion, usumodificacion,propietario)values(55,'DEVUELTO CA KO','1',sysdate,-1,1);
insert into fcs_je_maestroestados (idestado, descripcion, codigoext, fechamodificacion, usumodificacion,propietario)values(62,'ENVIANDO AJG. Genera XML','1',sysdate,-1,1);
insert into fcs_je_maestroestados (idestado, descripcion, codigoext, fechamodificacion, usumodificacion,propietario)values(65,'ENVIANDO AJG. Mueve XML','1',sysdate,-1,1);
insert into fcs_je_maestroestados (idestado, descripcion, codigoext, fechamodificacion, usumodificacion,propietario)values(70,'ENVIADO AJG','1',sysdate,-1,1);
insert into fcs_je_maestroestados (idestado, descripcion, codigoext, fechamodificacion, usumodificacion,propietario)values(75,'Procesando AJG','1',sysdate,-1,1);

insert into fcs_je_maestroestados (idestado, descripcion, codigoext, fechamodificacion, usumodificacion,propietario)values(85,'Error AJG','1',sysdate,-1,1);
insert into fcs_je_maestroestados (idestado, descripcion, codigoext, fechamodificacion, usumodificacion,propietario)values(87,'DEVUELTO CA KO AJG','1',sysdate,-1,1);
insert into fcs_je_maestroestados (idestado, descripcion, codigoext, fechamodificacion, usumodificacion,propietario)values(90,'Verificado AJG','1',sysdate,-1,1);
insert into fcs_je_maestroestados (idestado, descripcion, codigoext, fechamodificacion, usumodificacion,propietario)values(95,'DEVUELTO CA OK','1',sysdate,-1,1);



insert into GEN_PROPERTIES (FICHERO, PARAMETRO, VALOR)
values ('SIGA', 'ecom.operaciones.cat_proc_justificacion_proc.ciclo', '120');
insert into GEN_PROPERTIES (FICHERO, PARAMETRO, VALOR)
values ('SIGA', 'ecom.operaciones.cat_recibe_justificacion_ftp_ica.ciclo', '120');




---------
insert into ECOM_OPERACION (IDOPERACION, IDSERVICIO, NOMBRE, MAXREINTENTOS, ACTIVO, FECHAMODIFICACION, USUMODIFICACION)
values (69, 2, 'Validaci�n justificaci�n econ�mica', 1, '1', sysdate, 0);
insert into ECOM_OPERACION (IDOPERACION, IDSERVICIO, NOMBRE, MAXREINTENTOS, ACTIVO, FECHAMODIFICACION, USUMODIFICACION)
values (70, 2, 'Envio justificaci�n econ�mica CICAC', 1, '1', sysdate, 0);
insert into ECOM_OPERACION (IDOPERACION, IDSERVICIO, NOMBRE, MAXREINTENTOS, ACTIVO, FECHAMODIFICACION, USUMODIFICACION)
values (71, 2, 'Envio justificaci�n econ�mica AJR', 1, '1', sysdate, 0);
--Pasan a estado Error AJG antivirus--mira carpeta \rebutjades
insert into ECOM_OPERACION (IDOPERACION, IDSERVICIO, NOMBRE, MAXREINTENTOS, ACTIVO, FECHAMODIFICACION, USUMODIFICACION)
values (73, 2, 'Procesa justificaciones procesadas', 1, '1', sysdate, 0);
---Pasan a estado Error AJG mira carpeta \sortides. Al final las mueve a \sortides\HIST

insert into ECOM_OPERACION (IDOPERACION, IDSERVICIO, NOMBRE, MAXREINTENTOS, ACTIVO, FECHAMODIFICACION, USUMODIFICACION)
values (74, 2, 'Envia acuses justificacion', 1, '1', sysdate, 0);

insert into ECOM_OPERACION (IDOPERACION, IDSERVICIO, NOMBRE, MAXREINTENTOS, ACTIVO, FECHAMODIFICACION, USUMODIFICACION)
values (75, 2, 'Recibe justificaciones FTP', 1, '1', sysdate, 0);

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('scs.parametro.pcajg.directorioJustOutFtp', 'Directorio FTP donde AJR situa el fichero con la informacion del procesado del xml de actuaciones economicas', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('scs.parametro.pcajg.directorioJustOutFtp', 'Directorio FTP donde AJR situa el fichero con la informacion del procesado del xml de actuaciones economicas#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('scs.parametro.pcajg.directorioJustOutFtp', 'Directorio FTP donde AJR situa el fichero con la informacion del procesado del xml de actuaciones economicas#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('scs.parametro.pcajg.directorioJustOutFtp', 'Directorio FTP donde AJR situa el fichero con la informacion del procesado del xml de actuaciones economicas#GL', 0, '4', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('scs.parametro.pcajg.directorioJustInFtp', 'Directorio FTP donde SIGA situa el fichero xml de actuaciones economicas', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('scs.parametro.pcajg.directorioJustInFtp', 'Directorio FTP donde SIGA situa el fichero xml de actuaciones economicas#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('scs.parametro.pcajg.directorioJustInFtp', 'Directorio FTP donde SIGA situa el fichero xml de actuaciones economicas#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('scs.parametro.pcajg.directorioJustInFtp', 'Directorio FTP donde SIGA situa el fichero xml de actuaciones economicas#GL', 0, '4', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('scs.parametro.pcajg.directorioJusrHcoFtp', 'Directorio FTP donde SIGA situa el fichero xml procesado por AJR despues de procesarlo', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('scs.parametro.pcajg.directorioJusrHcoFtp', 'Directorio FTP donde SIGA situa el fichero xml procesado por AJR despues de procesarlo#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('scs.parametro.pcajg.directorioJusrHcoFtp', 'Directorio FTP donde SIGA situa el fichero xml procesado por AJR despues de procesarlo#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('scs.parametro.pcajg.directorioJusrHcoFtp', 'Directorio FTP donde SIGA situa el fichero xml procesado por AJR despues de procesarlo#GL', 0, '4', sysdate, 0, '19');



insert into GEN_PARAMETROS (MODULO, PARAMETRO, VALOR, FECHAMODIFICACION, USUMODIFICACION, IDINSTITUCION, IDRECURSO, FECHA_BAJA)
values ('SCS', 'PCAJG_FTP_DIRECTORIO_JUST_IN', '/entrades', SYSDATE, 1, 0, 'scs.parametro.pcajg.directorioJustInFtp', null);
insert into GEN_PARAMETROS (MODULO, PARAMETRO, VALOR, FECHAMODIFICACION, USUMODIFICACION, IDINSTITUCION, IDRECURSO, FECHA_BAJA)
values ('SCS', 'PCAJG_FTP_DIRECTORIO_JUST_OUT', '/sortides', SYSDATE, 1, 0, 'scs.parametro.pcajg.directorioJustOutFtp', null);
insert into GEN_PARAMETROS (MODULO, PARAMETRO, VALOR, FECHAMODIFICACION, USUMODIFICACION, IDINSTITUCION, IDRECURSO, FECHA_BAJA)
values ('SCS', 'PCAJG_FTP_DIRECTORIO_JUST_OUT_HIST', '/sortides/HIST', SYSDATE, 1, 0, 'scs.parametro.pcajg.directorioJusrHcoFtp', null);

coge V_SIGA_cat_justificacion.sql
coge V_SIGA_CAT_DEVOLUCION.sql



insert into GEN_PARAMETROS (MODULO, PARAMETRO, VALOR, FECHAMODIFICACION, USUMODIFICACION, IDINSTITUCION, IDRECURSO, FECHA_BAJA)
values ('ECOM', 'IPS_CLIENTE_WS_GEST_ECON_CATALUNYA', '127.0.0.1', SYSDATE, 1, 0, null, null);

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('general.literal.errorGlobal', 'Error global', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('general.literal.errorGlobal', 'Error global#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('general.literal.errorGlobal', 'Error global#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('general.literal.errorGlobal', 'Error global#GL', 0, '4', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('general.boton.upload', 'Subir', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('general.boton.upload', 'Subir#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('general.boton.upload', 'Subir#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('general.boton.upload', 'Subir#GL', 0, '4', sysdate, 0, '19');



              


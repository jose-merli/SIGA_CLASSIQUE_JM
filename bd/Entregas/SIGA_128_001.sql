
coger createTablesJustificacion.sql
--
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.ecom.justificacion', 'Justificación', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.ecom.justificacion', 'Justificación#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.ecom.justificacion', 'Justificación#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.ecom.justificacion', 'Justificación#GL', 0, '4', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.ecom.certificacion', 'Certificación', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.ecom.certificacion', 'Certificación#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.ecom.certificacion', 'Certificación#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.ecom.certificacion', 'Certificación#GL', 0, '4', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.ecom.devolucion', 'Devolución', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.ecom.devolucion', 'Devolución#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.ecom.devolucion', 'Devolución#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.ecom.devolucion', 'Devolución#GL', 0, '4', sysdate, 0, '19');

insert into GEN_PROCESOS
  (IDPROCESO,   IDMODULO,   TRAZA,   TARGET,   FECHAMODIFICACION,   USUMODIFICACION,   DESCRIPCION,   TRANSACCION,   IDPARENT,   NIVEL)
values
  ('710',   'JGR',   1,   'Y',   sysdate,   0,   'Justificación económica',   'JGR_E-Comunicaciones_Justificacion',   '007',   10);
    insert into GEN_PROCESOS
  (IDPROCESO,   IDMODULO,   TRAZA,   TARGET,   FECHAMODIFICACION,   USUMODIFICACION,   DESCRIPCION,   TRANSACCION,   IDPARENT,   NIVEL)
values
  ('711',   'JGR',   1,   'Y',   sysdate,   0,   'Certificación económica',   'JGR_E-Comunicaciones_Certificacion',   '007',   10);
  
  insert into GEN_PROCESOS
  (IDPROCESO,   IDMODULO,   TRAZA,   TARGET,   FECHAMODIFICACION,   USUMODIFICACION,   DESCRIPCION,   TRANSACCION,   IDPARENT,   NIVEL)
values
  ('712',   'JGR',   1,   'Y',   sysdate,   0,   'Devolución económica',   'JGR_E-Comunicaciones_Devolucion',   '007',   10);

insert into GEN_MENU (IDMENU, ORDEN, TAGWIDTH, IDPARENT, FECHAMODIFICACION, USUMODIFICACION, URI_IMAGEN, IDRECURSO, GEN_MENU_IDMENU, IDPROCESO, IDLENGUAJE)
values ('710', 32210, 160, '606', sysdate, 0, null, 'menu.sjcs.ecom.justificacion', null, '710', '1');

insert into GEN_MENU (IDMENU, ORDEN, TAGWIDTH, IDPARENT, FECHAMODIFICACION, USUMODIFICACION, URI_IMAGEN, IDRECURSO, GEN_MENU_IDMENU, IDPROCESO, IDLENGUAJE)
values ('711', 32020, 160, '606', sysdate, 0, null, 'menu.sjcs.ecom.certificacion', null, '711', '1');

insert into GEN_MENU (IDMENU, ORDEN, TAGWIDTH, IDPARENT, FECHAMODIFICACION, USUMODIFICACION, URI_IMAGEN, IDRECURSO, GEN_MENU_IDMENU, IDPROCESO, IDLENGUAJE)
values ('712', 32030, 160, '606', sysdate, 0, null, 'menu.sjcs.ecom.devolucion', null, '712', '1');



insert into FCS_JE_MAESTROESTADOS (IDESTADO, DESCRIPCION, CODIGOEXT, FECHAMODIFICACION, USUMODIFICACION, PROPIETARIO)
values (10, 'Inicial', '1', sysdate, -1, 0);
insert into FCS_JE_MAESTROESTADOS (IDESTADO, DESCRIPCION, CODIGOEXT, FECHAMODIFICACION, USUMODIFICACION, PROPIETARIO)
values (12, 'Validando...', '1', sysdate, -1, 0);
insert into FCS_JE_MAESTROESTADOS (IDESTADO, DESCRIPCION, CODIGOEXT, FECHAMODIFICACION, USUMODIFICACION, PROPIETARIO)
values (14, 'Validado correctamente', '1', sysdate, -1, 0);
insert into FCS_JE_MAESTROESTADOS (IDESTADO, DESCRIPCION, CODIGOEXT, FECHAMODIFICACION, USUMODIFICACION, PROPIETARIO)
values (16, 'Validado erróneo', '1', sysdate, -1, 0);
insert into FCS_JE_MAESTROESTADOS (IDESTADO, DESCRIPCION, CODIGOEXT, FECHAMODIFICACION, USUMODIFICACION, PROPIETARIO)
values (20, 'Enviando Consell...', '1', sysdate, -1, 0);
insert into FCS_JE_MAESTROESTADOS (IDESTADO, DESCRIPCION, CODIGOEXT, FECHAMODIFICACION, USUMODIFICACION, PROPIETARIO)
values (30, 'Enviado Consell', '1', sysdate, -1, 1);
insert into FCS_JE_MAESTROESTADOS (IDESTADO, DESCRIPCION, CODIGOEXT, FECHAMODIFICACION, USUMODIFICACION, PROPIETARIO)
values (40, 'Error Consell', '1', sysdate, -1, 1);
insert into FCS_JE_MAESTROESTADOS (IDESTADO, DESCRIPCION, CODIGOEXT, FECHAMODIFICACION, USUMODIFICACION, PROPIETARIO)
values (50, 'Finalizado Consell', '1', sysdate, -1, 1);
insert into FCS_JE_MAESTROESTADOS (IDESTADO, DESCRIPCION, CODIGOEXT, FECHAMODIFICACION, USUMODIFICACION, PROPIETARIO)
values (60, 'Devuelto error Consell al ICA.', '1', sysdate, -1, 1);
insert into FCS_JE_MAESTROESTADOS (IDESTADO, DESCRIPCION, CODIGOEXT, FECHAMODIFICACION, USUMODIFICACION, PROPIETARIO)
values (62, 'Enviando Generalitat. Generando XML....', '1', sysdate, -1, 1);
insert into FCS_JE_MAESTROESTADOS (IDESTADO, DESCRIPCION, CODIGOEXT, FECHAMODIFICACION, USUMODIFICACION, PROPIETARIO)
values (65, 'Enviando Generalitat. Moviendo XML...', '1', sysdate, -1, 1);
insert into FCS_JE_MAESTROESTADOS (IDESTADO, DESCRIPCION, CODIGOEXT, FECHAMODIFICACION, USUMODIFICACION, PROPIETARIO)
values (70, 'Enviado Generalitat.', '1', sysdate, -1, 1);
insert into FCS_JE_MAESTROESTADOS (IDESTADO, DESCRIPCION, CODIGOEXT, FECHAMODIFICACION, USUMODIFICACION, PROPIETARIO)
values (75, 'Procesando XML de Generalitat...', '1', sysdate, -1, 1);
insert into FCS_JE_MAESTROESTADOS (IDESTADO, DESCRIPCION, CODIGOEXT, FECHAMODIFICACION, USUMODIFICACION, PROPIETARIO)
values (85, 'Error Generalitat', '1', sysdate, -1, 1);
insert into FCS_JE_MAESTROESTADOS (IDESTADO, DESCRIPCION, CODIGOEXT, FECHAMODIFICACION, USUMODIFICACION, PROPIETARIO)
values (87, 'Devuelto error Generalitat al Consell.', '1', sysdate, -1, 1);
insert into FCS_JE_MAESTROESTADOS (IDESTADO, DESCRIPCION, CODIGOEXT, FECHAMODIFICACION, USUMODIFICACION, PROPIETARIO)
values (90, 'Verificado correctamente Generalitat', '1', sysdate, -1, 1);
insert into FCS_JE_MAESTROESTADOS (IDESTADO, DESCRIPCION, CODIGOEXT, FECHAMODIFICACION, USUMODIFICACION, PROPIETARIO)
values (95, 'Respondido correctamente al ICA. Fin de proceso', '1', sysdate, -1, 1);
insert into FCS_JE_MAESTROESTADOS (IDESTADO, DESCRIPCION, CODIGOEXT, FECHAMODIFICACION, USUMODIFICACION, PROPIETARIO)
values (92, 'Respondiendo ICA...', '1', sysdate, -1, 1);



insert into GEN_PROPERTIES (FICHERO, PARAMETRO, VALOR)
values ('SIGA', 'ecom.operaciones.cat_proc_justificacion_proc.ciclo', '120');
insert into GEN_PROPERTIES (FICHERO, PARAMETRO, VALOR)
values ('SIGA', 'ecom.operaciones.cat_recibe_justificacion_ftp_ica.ciclo', '120');




---------
insert into ECOM_OPERACION (IDOPERACION, IDSERVICIO, NOMBRE, MAXREINTENTOS, ACTIVO, FECHAMODIFICACION, USUMODIFICACION)
values (69, 2, 'Validación justificación económica', 1, '1', sysdate, 0);
insert into ECOM_OPERACION (IDOPERACION, IDSERVICIO, NOMBRE, MAXREINTENTOS, ACTIVO, FECHAMODIFICACION, USUMODIFICACION)
values (70, 2, 'Envio justificación económica CICAC', 1, '1', sysdate, 0);
insert into ECOM_OPERACION (IDOPERACION, IDSERVICIO, NOMBRE, MAXREINTENTOS, ACTIVO, FECHAMODIFICACION, USUMODIFICACION)
values (71, 2, 'Envio justificación económica AJR', 1, '1', sysdate, 0);
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

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('general.literal.errorGlobal', 'Error', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('general.literal.errorGlobal', 'Error#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('general.literal.errorGlobal', 'Error#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('general.literal.errorGlobal', 'Error#GL', 0, '4', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gen.parametro.pathFicheros', 'Subir', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gen.parametro.pathFicheros', 'Subir#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gen.parametro.pathFicheros', 'Subir#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gen.parametro.pathFicheros', 'Subir#GL', 0, '4', sysdate, 0, '19');



insert into GEN_PARAMETROS (MODULO, PARAMETRO, VALOR, FECHAMODIFICACION, USUMODIFICACION, IDINSTITUCION, IDRECURSO, FECHA_BAJA)
values ('GEN', 'PATH_FICHEROS', '/Datos/SIGAINT/ficheros/archivo/', SYSDATE, 0, 0, 'gen.parametro.pathFicheros', null);

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gen.parametro.pathFicheros', 'Directorio donde se almacenan ficheros', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gen.parametro.pathFicheros', 'Directorio donde se almacenan ficheros#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gen.parametro.pathFicheros', 'Directorio donde se almacenan ficheros#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gen.parametro.pathFicheros', 'Directorio donde se almacenan ficheros#GL', 0, '4', sysdate, 0, '19');

--ejecutado en integracion 


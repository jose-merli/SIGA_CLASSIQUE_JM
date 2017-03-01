prompt PL/SQL Developer import file
prompt Created on martes, 28 de febrero de 2017 by jorgeta
set feedback off
set define off
prompt Loading PCAJG_ALC_TIPOERRORINTERCAMBIO...
insert into PCAJG_ALC_TIPOERRORINTERCAMBIO (ERROR_CODIGO, ERROR_DESCRIPCION)
values ('001', 'Intercambio duplicado');
insert into PCAJG_ALC_TIPOERRORINTERCAMBIO (ERROR_CODIGO, ERROR_DESCRIPCION)
values ('002', 'Expediente duplicado (Traslado exp.)');
insert into PCAJG_ALC_TIPOERRORINTERCAMBIO (ERROR_CODIGO, ERROR_DESCRIPCION)
values ('003', 'Expediente no existente (Actualización exp)');
insert into PCAJG_ALC_TIPOERRORINTERCAMBIO (ERROR_CODIGO, ERROR_DESCRIPCION)
values ('004', 'Código no existente');
insert into PCAJG_ALC_TIPOERRORINTERCAMBIO (ERROR_CODIGO, ERROR_DESCRIPCION)
values ('005', 'Campo obligatorio no informado');
insert into PCAJG_ALC_TIPOERRORINTERCAMBIO (ERROR_CODIGO, ERROR_DESCRIPCION)
values ('006', 'Formato del campo erróneo');
insert into PCAJG_ALC_TIPOERRORINTERCAMBIO (ERROR_CODIGO, ERROR_DESCRIPCION)
values ('007', 'Documentación obligatoria no informada');
insert into PCAJG_ALC_TIPOERRORINTERCAMBIO (ERROR_CODIGO, ERROR_DESCRIPCION)
values ('008', 'Solicitante no existente (Actualización exp)');
insert into PCAJG_ALC_TIPOERRORINTERCAMBIO (ERROR_CODIGO, ERROR_DESCRIPCION)
values ('009', 'Error indefinido');
insert into PCAJG_ALC_TIPOERRORINTERCAMBIO (ERROR_CODIGO, ERROR_DESCRIPCION)
values ('010', 'Intercambio mal formado');
insert into PCAJG_ALC_TIPOERRORINTERCAMBIO (ERROR_CODIGO, ERROR_DESCRIPCION)
values ('011', 'Designación no existente');
insert into PCAJG_ALC_TIPOERRORINTERCAMBIO (ERROR_CODIGO, ERROR_DESCRIPCION)
values ('012', 'Procedimiento judicial no existente');
insert into PCAJG_ALC_TIPOERRORINTERCAMBIO (ERROR_CODIGO, ERROR_DESCRIPCION)
values ('013', 'Designación ya existente');
insert into PCAJG_ALC_TIPOERRORINTERCAMBIO (ERROR_CODIGO, ERROR_DESCRIPCION)
values ('014', 'Procedimiento judicial ya existente');
insert into PCAJG_ALC_TIPOERRORINTERCAMBIO (ERROR_CODIGO, ERROR_DESCRIPCION)
values ('015', 'Documentación ya existente');
insert into PCAJG_ALC_TIPOERRORINTERCAMBIO (ERROR_CODIGO, ERROR_DESCRIPCION)
values ('016', 'Error en el formato del nombre del documento');
insert into PCAJG_ALC_TIPOERRORINTERCAMBIO (ERROR_CODIGO, ERROR_DESCRIPCION)
values ('017', 'Número/año de expediente del colegio no encontrado');
insert into PCAJG_ALC_TIPOERRORINTERCAMBIO (ERROR_CODIGO, ERROR_DESCRIPCION)
values ('018', 'Error interno. (Fichero pendiente de procesar)');
insert into PCAJG_ALC_TIPOERRORINTERCAMBIO (ERROR_CODIGO, ERROR_DESCRIPCION)
values ('019', 'Fichero comprimido mal formado');
insert into PCAJG_ALC_TIPOERRORINTERCAMBIO (ERROR_CODIGO, ERROR_DESCRIPCION)
values ('020', 'Motivo de cambio de procedimiento dado de baja');
insert into PCAJG_ALC_TIPOERRORINTERCAMBIO (ERROR_CODIGO, ERROR_DESCRIPCION)
values ('021', 'Ya existe, para la misma carga, un documento del mismo expediente e interviniente');
insert into PCAJG_ALC_TIPOERRORINTERCAMBIO (ERROR_CODIGO, ERROR_DESCRIPCION)
values ('022', 'El expediente es anterior al año 2012');
insert into PCAJG_ALC_TIPOERRORINTERCAMBIO (ERROR_CODIGO, ERROR_DESCRIPCION)
values ('024', 'Expediente no cargado');
insert into PCAJG_ALC_TIPOERRORINTERCAMBIO (ERROR_CODIGO, ERROR_DESCRIPCION)
values ('023', 'La fecha de actuación es anterior al año 2012');
commit;
prompt 24 records loaded
set feedback on
set define on
prompt Done.

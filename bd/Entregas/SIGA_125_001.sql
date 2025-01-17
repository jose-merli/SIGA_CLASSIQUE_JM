insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('cajg.opcion.informeEconomicoOk', 'Expedientes cuya informaci�n econ�mica ha sido enviada sin incidencias', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('cajg.opcion.informeEconomicoOk', 'Expedientes cuya informaci�n econ�mica ha sido enviada sin incidencias#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('cajg.opcion.informeEconomicoOk', 'Expedientes cuya informaci�n econ�mica ha sido enviada sin incidencias#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('cajg.opcion.informeEconomicoOk', 'Expedientes cuya informaci�n econ�mica ha sido enviada sin incidencias#GL', 0, '4', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('cajg.opcion.informeEconomicoSol', 'Expedientes a los que se ha solicidado el envio del informe económico', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('cajg.opcion.informeEconomicoSol', 'Expedientes a los que se ha solicidado el envio del informe econ�mico#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('cajg.opcion.informeEconomicoSol', 'Expedientes a los que se ha solicidado el envio del informe econ�mico#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('cajg.opcion.informeEconomicoSol', 'Expedientes a los que se ha solicidado el envio del informe econ�mico#GL', 0, '4', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('cajg.opcion.informeEconomicoKo', 'Expedientes cuya informaci�n econ�mica ha sido enviada con incidencias', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('cajg.opcion.informeEconomicoKo', 'Expedientes cuya informaci�n econ�mica ha sido enviada con incidencias#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('cajg.opcion.informeEconomicoKo', 'Expedientes cuya informaci�n econ�mica ha sido enviada con incidencias#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('cajg.opcion.informeEconomicoKo', 'Expedientes cuya informaci�n econ�mica ha sido enviada con incidencias#GL', 0, '4', sysdate, 0, '19');


insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.busquedaDuplicados.titulo.fusion', 'Censo > Mantenimiento de duplicados > Fusionar', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.busquedaDuplicados.titulo.fusion', 'Censo > Mantenimiento de duplicados > Fusionar#GL', 0, '4', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.busquedaDuplicados.titulo.fusion', 'Censo > Mantenimiento de duplicados > Fusionar#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.busquedaDuplicados.titulo.fusion', 'Censo > Mantenimiento de duplicados > Fusionar#EU', 0, '3', sysdate, 0, '19');

-- INI Agrupacion maestros Censo
Insert Into gen_recursos
  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values
  ('menu.censo.maestrosYMantenimientos', 'Maestros y Mantenimientos', 0, 1, Sysdate, 0, 19);
Insert Into gen_recursos
  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values
  ('menu.censo.maestrosYMantenimientos', 'Mestres i Manteniments', 0, 2, Sysdate, 0, 19);
Insert Into gen_recursos
  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values
  ('menu.censo.maestrosYMantenimientos', 'Maestros y Mantenimientos#EU', 0, 3, Sysdate, 0, 19);
Insert Into gen_recursos
  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values
  ('menu.censo.maestrosYMantenimientos', 'Maestros y Mantenimientos#GL', 0, 4, Sysdate, 0, 19);

Insert Into gen_procesos
  (idproceso, idmodulo, traza, target, fechamodificacion, usumodificacion, descripcion, transaccion, idparent, nivel)
Values
  ('180', 'CEN', 1, 'Y', Sysdate, 0, 'Maestros y Mantenimientos', 'CEN_MaestrosYMantenimientos', '00', '10');
Insert Into gen_menu
  (idmenu, orden, tagwidth, idparent, fechamodificacion, usumodificacion, uri_imagen, idrecurso, gen_menu_idmenu, idproceso, idlenguaje, migrado)
Values
  (162, 17000, 180, '0', Sysdate, 0, Null, 'menu.censo.maestrosYMantenimientos', Null, '180', 1, 0);

Update Gen_Menu Set idparent = '162' Where idmenu In ('7', '17', '19', '15E', '15F', '99E');
Update Gen_Menu Set orden = '17010' Where idmenu = '17';
Update Gen_Menu Set orden = '17020' Where idmenu = '7';
Update Gen_Menu Set orden = '17030' Where idmenu = '99E';
Update Gen_Menu Set orden = '17040' Where idmenu = '15E';
Update Gen_Menu Set orden = '17050' Where idmenu = '15F';
Update Gen_Menu Set orden = '17060' Where idmenu = '19';

Update Gen_Procesos Set idparent = '180' Where idproceso In 
(Select men.idproceso From gen_menu men Where idmenu In ('7', '17', '19', '15E', '15F', '99E'));

declare
  cursor c_aux is
  Select distinct Idinstitucion, Idperfil
    From Adm_Tiposacceso
   Where Idproceso In ('7', '17', '19', '15E', '15F', '99E')
     And Derechoacceso > 1;

Begin
  For Rec In c_Aux Loop
    Begin
      Insert Into Adm_Tiposacceso
        (Idproceso, Idperfil, Fechamodificacion, Usumodificacion, Derechoacceso, Idinstitucion)
      Values
        ('180', Rec.Idperfil, Sysdate, 0, 3, Rec.Idinstitucion);
      Dbms_Output.Put_Line('Insertado: ' || Rec.Idperfil || ', ' || Rec.Idinstitucion);
    
    Exception
      When Others Then
        Dbms_Output.Put_Line('Error=' || Sqlerrm);
        Rollback;
    End;
    Commit;
  End Loop;
End;
-- FIN Agrupacion maestros Censo

-- INI Cambio FKs para nuevo mantenimiento duplicados
alter table 	SCS_ZONA	 drop constraint 	FK_9ZO_1IN	 ; 
alter table 	SCS_ZONA	 add constraint 	FK_9ZO_1IN	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_UNIDADFAMILIAREJG	 drop constraint 	FK_UNIDADFAMILIAR_TIPOINGRESO	 ; 
alter table 	SCS_UNIDADFAMILIAREJG	 drop constraint 	FK_UNIDADFAMILIAR_PERSONAEJG	 ; 
alter table 	SCS_UNIDADFAMILIAREJG	 drop constraint 	FK_UNIDADFAMILIAR_PARENTESCO	 ; 
alter table 	SCS_UNIDADFAMILIAREJG	 drop constraint 	FK_UNIDADFAMILIAR_GRUPOLAB	 ; 
alter table 	SCS_UNIDADFAMILIAREJG	 drop constraint 	FK_UNIDADFAMILIAR_EJG	 ; 
alter table 	SCS_UNIDADFAMILIAREJG	 add constraint 	FK_UNIDADFAMILIAR_TIPOINGRESO	 foreign key (	IDTIPOINGRESO	)	 references 	SCS_TIPOINGRESO	 (	IDTIPOINGRESO	) 		 Deferrable;
alter table 	SCS_UNIDADFAMILIAREJG	 add constraint 	FK_UNIDADFAMILIAR_PERSONAEJG	 foreign key (	IDPERSONA,IDINSTITUCION	)	 references 	SCS_PERSONAJG	 (	IDPERSONA,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_UNIDADFAMILIAREJG	 add constraint 	FK_UNIDADFAMILIAR_PARENTESCO	 foreign key (	IDPARENTESCO,IDINSTITUCION	)	 references 	SCS_PARENTESCO	 (	IDPARENTESCO,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_UNIDADFAMILIAREJG	 add constraint 	FK_UNIDADFAMILIAR_GRUPOLAB	 foreign key (	IDTIPOGRUPOLAB,IDINSTITUCION	)	 references 	SCS_TIPOGRUPOLABORAL	 (	IDTIPOGRUPOLAB,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_UNIDADFAMILIAREJG	 add constraint 	FK_UNIDADFAMILIAR_EJG	 foreign key (	NUMERO,IDINSTITUCION,IDTIPOEJG,ANIO	)	 references 	SCS_EJG	 (	NUMERO,IDINSTITUCION,IDTIPOEJG,ANIO	) 	 on delete cascade	 Deferrable;
alter table 	SCS_TURNO	 drop constraint 	FK_SCS_TIPOTURNO	 ; 
alter table 	SCS_TURNO	 drop constraint 	FK_SCS_INSCRIPCION_ULTIMO_T	 ; 
alter table 	SCS_TURNO	 drop constraint 	FK_9TU_9SZ	 ; 
alter table 	SCS_TURNO	 drop constraint 	FK_9TU_9PP	 ; 
alter table 	SCS_TURNO	 drop constraint 	FK_9TU_9OC	 ; 
alter table 	SCS_TURNO	 drop constraint 	FK_9TU_9MA	 ; 
alter table 	SCS_TURNO	 drop constraint 	FK_9TU_9GF	 ; 
alter table 	SCS_TURNO	 drop constraint 	FK_9TU_1IN	 ; 
alter table 	SCS_TURNO	 add constraint 	FK_SCS_TIPOTURNO	 foreign key (	IDTIPOTURNO	)	 references 	SCS_TIPOTURNO	 (	IDTIPOTURNO	) 		 Deferrable;
alter table 	SCS_TURNO	 add constraint 	FK_SCS_INSCRIPCION_ULTIMO_T	 foreign key (	FECHASOLICITUD_ULTIMO,IDINSTITUCION,IDTURNO,IDPERSONA_ULTIMO	)	 references 	SCS_INSCRIPCIONTURNO	 (	FECHASOLICITUD,IDINSTITUCION,IDTURNO,IDPERSONA	) 		 Deferrable;
alter table 	SCS_TURNO	 add constraint 	FK_9TU_9SZ	 foreign key (	IDSUBZONA,IDINSTITUCION,IDZONA	)	 references 	SCS_SUBZONA	 (	IDSUBZONA,IDINSTITUCION,IDZONA	) 		 Deferrable;
alter table 	SCS_TURNO	 add constraint 	FK_9TU_9PP	 foreign key (	IDPARTIDAPRESUPUESTARIA,IDINSTITUCION	)	 references 	SCS_PARTIDAPRESUPUESTARIA	 (	IDPARTIDAPRESUPUESTARIA,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_TURNO	 add constraint 	FK_9TU_9OC	 foreign key (	IDORDENACIONCOLAS	)	 references 	SCS_ORDENACIONCOLAS	 (	IDORDENACIONCOLAS	) 		 Deferrable;
alter table 	SCS_TURNO	 add constraint 	FK_9TU_9MA	 foreign key (	IDMATERIA,IDINSTITUCION,IDAREA	)	 references 	SCS_MATERIA	 (	IDMATERIA,IDINSTITUCION,IDAREA	) 		 Deferrable;
alter table 	SCS_TURNO	 add constraint 	FK_9TU_9GF	 foreign key (	IDGRUPOFACTURACION,IDINSTITUCION	)	 references 	SCS_GRUPOFACTURACION	 (	IDGRUPOFACTURACION,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_TURNO	 add constraint 	FK_9TU_1IN	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_TIPOSOJCOLEGIO	 drop constraint 	STJ_CIN_FK	 ; 
alter table 	SCS_TIPOSOJCOLEGIO	 add constraint 	STJ_CIN_FK	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_TIPORESPUESTA	 drop constraint 	FK_TIPORESPUESTA_IDINSTI	 ; 
alter table 	SCS_TIPORESPUESTA	 add constraint 	FK_TIPORESPUESTA_IDINSTI	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_TIPOGRUPOLABORAL	 drop constraint 	FK_TIPOGRUPOLAB_IDINSTI	 ; 
alter table 	SCS_TIPOGRUPOLABORAL	 add constraint 	FK_TIPOGRUPOLAB_IDINSTI	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_TIPOFUNDAMENTOS	 drop constraint 	FK_SCS_TIPOFUN_TIPORES	 ; 
alter table 	SCS_TIPOFUNDAMENTOS	 drop constraint 	FK_SCS_TIPOFUND_INSTITUCION	 ; 
alter table 	SCS_TIPOFUNDAMENTOS	 add constraint 	FK_SCS_TIPOFUN_TIPORES	 foreign key (	IDTIPORESOLUCION	)	 references 	SCS_TIPORESOLUCION	 (	IDTIPORESOLUCION	) 		 Deferrable;
alter table 	SCS_TIPOFUNDAMENTOS	 add constraint 	FK_SCS_TIPOFUND_INSTITUCION	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_TIPOFUNDAMENTOCALIF	 drop constraint 	FK_SCS_TIPOFUNCAL_TIPODICTAMEN	 ; 
alter table 	SCS_TIPOFUNDAMENTOCALIF	 drop constraint 	FK_SCS_TIPOFUNCAL_INSTITU	 ; 
alter table 	SCS_TIPOFUNDAMENTOCALIF	 add constraint 	FK_SCS_TIPOFUNCAL_TIPODICTAMEN	 foreign key (	IDTIPODICTAMENEJG,IDINSTITUCION	)	 references 	SCS_TIPODICTAMENEJG	 (	IDTIPODICTAMENEJG,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_TIPOFUNDAMENTOCALIF	 add constraint 	FK_SCS_TIPOFUNCAL_INSTITU	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_TIPOEJGCOLEGIO	 drop constraint 	FK_SCS_SERIEEJG_INSTITUCION	 ; 
alter table 	SCS_TIPOEJGCOLEGIO	 add constraint 	FK_SCS_SERIEEJG_INSTITUCION	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_TIPODOCUMENTOEJG	 drop constraint 	FK_TIPODOCEJG_CEN_INSTITUCION	 ; 
alter table 	SCS_TIPODOCUMENTOEJG	 add constraint 	FK_TIPODOCEJG_CEN_INSTITUCION	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_TIPODICTAMENEJG	 drop constraint 	FK_SCS_TIPODICTAMENEJG_INSTITU	 ; 
alter table 	SCS_TIPODICTAMENEJG	 add constraint 	FK_SCS_TIPODICTAMENEJG_INSTITU	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_TIPODESIGNACOLEGIO	 drop constraint 	FK_SCS_ORIGENOFICIO_INSTITUCIO	 ; 
alter table 	SCS_TIPODESIGNACOLEGIO	 add constraint 	FK_SCS_ORIGENOFICIO_INSTITUCIO	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_TIPOCONSULTA	 drop constraint 	FK_TIPOCONSULTA_IDINSTI	 ; 
alter table 	SCS_TIPOCONSULTA	 add constraint 	FK_TIPOCONSULTA_IDINSTI	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_TIPOCONOCE	 drop constraint 	FK_TIPOCONOCE_IDINSTI	 ; 
alter table 	SCS_TIPOCONOCE	 add constraint 	FK_TIPOCONOCE_IDINSTI	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_TIPOASISTENCIACOLEGIO	 drop constraint 	FK_SCS_ORIGENASISTENCIA_INSTIT	 ; 
alter table 	SCS_TIPOASISTENCIACOLEGIO	 add constraint 	FK_SCS_ORIGENASISTENCIA_INSTIT	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_TIPOACTUACIONCOSTEFIJO	 drop constraint 	FK_SCS_ACTCOSTEFIJO_TIPOACT	 ; 
alter table 	SCS_TIPOACTUACIONCOSTEFIJO	 drop constraint 	FK_SCS_ACTCOSTEFIJO_COSTEFIJO	 ; 
alter table 	SCS_TIPOACTUACIONCOSTEFIJO	 add constraint 	FK_SCS_ACTCOSTEFIJO_TIPOACT	 foreign key (	IDTIPOACTUACION,IDINSTITUCION,IDTIPOASISTENCIA	)	 references 	SCS_TIPOACTUACION	 (	IDTIPOACTUACION,IDINSTITUCION,IDTIPOASISTENCIA	) 	 on delete cascade	 Deferrable;
alter table 	SCS_TIPOACTUACIONCOSTEFIJO	 add constraint 	FK_SCS_ACTCOSTEFIJO_COSTEFIJO	 foreign key (	IDCOSTEFIJO,IDINSTITUCION	)	 references 	SCS_COSTEFIJO	 (	IDCOSTEFIJO,IDINSTITUCION	) 	 on delete cascade	 Deferrable;
alter table 	SCS_TELEFONOSPERSONA	 drop constraint 	FK_SCS_TELEFONOSPERSONA_PERSJG	 ; 
alter table 	SCS_TELEFONOSPERSONA	 add constraint 	FK_SCS_TELEFONOSPERSONA_PERSJG	 foreign key (	IDINSTITUCION,IDPERSONA	)	 references 	SCS_PERSONAJG	 (	IDINSTITUCION,IDPERSONA	) 		 Deferrable;
alter table 	SCS_SUBZONAPARTIDO	 drop constraint 	FK_SCS_SUBZONAPARTIDO_SUBZONA	 ; 
alter table 	SCS_SUBZONAPARTIDO	 drop constraint 	FK_SCS_SUBZONAPARTIDO_PARTIDO	 ; 
alter table 	SCS_SUBZONAPARTIDO	 add constraint 	FK_SCS_SUBZONAPARTIDO_SUBZONA	 foreign key (	IDZONA,IDINSTITUCION,IDSUBZONA	)	 references 	SCS_SUBZONA	 (	IDZONA,IDINSTITUCION,IDSUBZONA	) 	 on delete cascade	 Deferrable;
alter table 	SCS_SUBZONAPARTIDO	 add constraint 	FK_SCS_SUBZONAPARTIDO_PARTIDO	 foreign key (	IDPARTIDO	)	 references 	CEN_PARTIDOJUDICIAL	 (	IDPARTIDO	) 		 Deferrable;
alter table 	SCS_SUBZONA	 drop constraint 	FK_9SQ_9ZO	 ; 
alter table 	SCS_SUBZONA	 add constraint 	FK_9SQ_9ZO	 foreign key (	IDINSTITUCION,IDZONA	)	 references 	SCS_ZONA	 (	IDINSTITUCION,IDZONA	) 		 Deferrable;
alter table 	SCS_SOJ	 drop constraint 	SSJ_SGO_FK	 ; 
alter table 	SCS_SOJ	 drop constraint 	FK_SOJ_TIPORESPUESTA	 ; 
alter table 	SCS_SOJ	 drop constraint 	FK_SOJ_TIPOCONSULTA	 ; 
alter table 	SCS_SOJ	 drop constraint 	FK_SOJ_EJG	 ; 
alter table 	SCS_SOJ	 drop constraint 	FK_SCS_SOJ_TIPOSOJCOLEGIO	 ; 
alter table 	SCS_SOJ	 drop constraint 	FK_SCS_SOJ_TIPOSOJ	 ; 
alter table 	SCS_SOJ	 drop constraint 	FK_SCS_SOJ_PERSONAJG	 ; 
alter table 	SCS_SOJ	 drop constraint 	FK_SCS_SOJ_FACTURACIONJG	 ; 
alter table 	SCS_SOJ	 drop constraint 	FK_SCS_SOJ_COLEGIADO	 ; 
alter table 	SCS_SOJ	 add constraint 	SSJ_SGO_FK	 foreign key (	IDGUARDIA,IDINSTITUCION,IDTURNO	)	 references 	SCS_GUARDIASTURNO	 (	IDGUARDIA,IDINSTITUCION,IDTURNO	) 		 Deferrable;
alter table 	SCS_SOJ	 add constraint 	FK_SOJ_TIPORESPUESTA	 foreign key (	IDTIPORESPUESTA,IDINSTITUCION	)	 references 	SCS_TIPORESPUESTA	 (	IDTIPORESPUESTA,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_SOJ	 add constraint 	FK_SOJ_TIPOCONSULTA	 foreign key (	IDTIPOCONSULTA,IDINSTITUCION	)	 references 	SCS_TIPOCONSULTA	 (	IDTIPOCONSULTA,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_SOJ	 add constraint 	FK_SOJ_EJG	 foreign key (	EJGNUMERO,IDINSTITUCION,EJGIDTIPOEJG,EJGANIO	)	 references 	SCS_EJG	 (	NUMERO,IDINSTITUCION,IDTIPOEJG,ANIO	) 		 Deferrable;
alter table 	SCS_SOJ	 add constraint 	FK_SCS_SOJ_TIPOSOJCOLEGIO	 foreign key (	IDTIPOSOJCOLEGIO,IDINSTITUCION	)	 references 	SCS_TIPOSOJCOLEGIO	 (	IDTIPOSOJCOLEGIO,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_SOJ	 add constraint 	FK_SCS_SOJ_TIPOSOJ	 foreign key (	IDTIPOSOJ	)	 references 	SCS_TIPOSOJ	 (	IDTIPOSOJ	) 		 Deferrable;
alter table 	SCS_SOJ	 add constraint 	FK_SCS_SOJ_PERSONAJG	 foreign key (	IDPERSONAJG,IDINSTITUCION	)	 references 	SCS_PERSONAJG	 (	IDPERSONA,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_SOJ	 add constraint 	FK_SCS_SOJ_FACTURACIONJG	 foreign key (	IDFACTURACION,IDINSTITUCION	)	 references 	FCS_FACTURACIONJG	 (	IDFACTURACION,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_SOJ	 add constraint 	FK_SCS_SOJ_COLEGIADO	 foreign key (	IDINSTITUCION,IDPERSONA	)	 references 	CEN_COLEGIADO	 (	IDINSTITUCION,IDPERSONA	) 		 Deferrable;
alter table 	SCS_SALTOSCOMPENSACIONES	 drop constraint 	FK_SCS_SALTOSTURNO_TURNO	 ; 
alter table 	SCS_SALTOSCOMPENSACIONES	 drop constraint 	FK_SCS_SALTOSTURNO_GUARDIASTUR	 ; 
alter table 	SCS_SALTOSCOMPENSACIONES	 drop constraint 	FK_SCS_SALTOSTURNO_COLEGIADO	 ; 
alter table 	SCS_SALTOSCOMPENSACIONES	 drop constraint 	FK_SALTOSCOMPENSACIONES_CALEND	 ; 
alter table 	SCS_SALTOSCOMPENSACIONES	 add constraint 	FK_SCS_SALTOSTURNO_TURNO	 foreign key (	IDTURNO,IDINSTITUCION	)	 references 	SCS_TURNO	 (	IDTURNO,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_SALTOSCOMPENSACIONES	 add constraint 	FK_SCS_SALTOSTURNO_GUARDIASTUR	 foreign key (	IDGUARDIA,IDINSTITUCION,IDTURNO	)	 references 	SCS_GUARDIASTURNO	 (	IDGUARDIA,IDINSTITUCION,IDTURNO	) 		 Deferrable;
alter table 	SCS_SALTOSCOMPENSACIONES	 add constraint 	FK_SCS_SALTOSTURNO_COLEGIADO	 foreign key (	IDPERSONA,IDINSTITUCION	)	 references 	CEN_COLEGIADO	 (	IDPERSONA,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_SALTOSCOMPENSACIONES	 add constraint 	FK_SALTOSCOMPENSACIONES_CALEND	 foreign key (	IDCALENDARIOGUARDIAS,IDINSTITUCION,IDTURNO,IDGUARDIA	)	 references 	SCS_CALENDARIOGUARDIAS	 (	IDCALENDARIOGUARDIAS,IDINSTITUCION,IDTURNO,IDGUARDIA	) 		 Deferrable;
alter table 	SCS_SALTOCOMPENSACIONGRUPO	 drop constraint 	FK_SCS_SALTOCOMP_GRUPOGUARDIA	 ; 
alter table 	SCS_SALTOCOMPENSACIONGRUPO	 drop constraint 	FK_SCS_SALTOCOMP_CALEN_CUMPLI	 ; 
alter table 	SCS_SALTOCOMPENSACIONGRUPO	 drop constraint 	FK_SCS_SALTOCOMP_CALEN	 ; 
alter table 	SCS_SALTOCOMPENSACIONGRUPO	 add constraint 	FK_SCS_SALTOCOMP_GRUPOGUARDIA	 foreign key (	IDGRUPOGUARDIA	)	 references 	SCS_GRUPOGUARDIA	 (	IDGRUPOGUARDIA	) 		 Deferrable;
alter table 	SCS_SALTOCOMPENSACIONGRUPO	 add constraint 	FK_SCS_SALTOCOMP_CALEN_CUMPLI	 foreign key (	IDCALENDARIOGUARDIAS_CUMPLI,IDINSTITUCION_CUMPLI,IDTURNO_CUMPLI,IDGUARDIA_CUMPLI	)	 references 	SCS_CALENDARIOGUARDIAS	 (	IDCALENDARIOGUARDIAS,IDINSTITUCION,IDTURNO,IDGUARDIA	) 		 Deferrable;
alter table 	SCS_SALTOCOMPENSACIONGRUPO	 add constraint 	FK_SCS_SALTOCOMP_CALEN	 foreign key (	IDCALENDARIOGUARDIAS,IDINSTITUCION,IDTURNO,IDGUARDIA	)	 references 	SCS_CALENDARIOGUARDIAS	 (	IDCALENDARIOGUARDIAS,IDINSTITUCION,IDTURNO,IDGUARDIA	) 		 Deferrable;
alter table 	SCS_RETENCIONESIRPF	 drop constraint 	SRF_SME_FK	 ; 
alter table 	SCS_RETENCIONESIRPF	 drop constraint 	SRF_CLIENTE_FK	 ; 
alter table 	SCS_RETENCIONESIRPF	 add constraint 	SRF_SME_FK	 foreign key (	IDRETENCION	)	 references 	SCS_MAESTRORETENCIONES	 (	IDRETENCION	) 		 Deferrable;
alter table 	SCS_RETENCIONESIRPF	 add constraint 	SRF_CLIENTE_FK	 foreign key (	IDPERSONA,IDINSTITUCION	)	 references 	CEN_CLIENTE	 (	IDPERSONA,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_PROG_CALENDARIOS	 drop constraint 	FK_SCS_CONJUNTOGUARDIAS	 ; 
alter table 	SCS_PROG_CALENDARIOS	 drop constraint 	FK_PROGCAL_GENFICHERO	 ; 
alter table 	SCS_PROG_CALENDARIOS	 add constraint 	FK_SCS_CONJUNTOGUARDIAS	 foreign key (	IDINSTITUCION,IDCONJUNTOGUARDIA	)	 references 	SCS_CONJUNTOGUARDIAS	 (	IDINSTITUCION,IDCONJUNTOGUARDIA	) 		 Deferrable;
alter table 	SCS_PROG_CALENDARIOS	 add constraint 	FK_PROGCAL_GENFICHERO	 foreign key (	IDFICHEROCALENDARIO,IDINSTITUCION	)	 references 	GEN_FICHERO	 (	IDFICHERO,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_PROCURADOR	 drop constraint 	FK_SCS_PROCURADOR_PROVINCIA	 ; 
alter table 	SCS_PROCURADOR	 drop constraint 	FK_SCS_PROCURADOR_POBLACION	 ; 
alter table 	SCS_PROCURADOR	 drop constraint 	FK_SCS_PROCURADOR_INSTITUCION	 ; 
alter table 	SCS_PROCURADOR	 drop constraint 	FK_SCS_PROCURADOR_CENCOLPROC	 ; 
alter table 	SCS_PROCURADOR	 add constraint 	FK_SCS_PROCURADOR_PROVINCIA	 foreign key (	IDPROVINCIA	)	 references 	CEN_PROVINCIAS	 (	IDPROVINCIA	) 		 Deferrable;
alter table 	SCS_PROCURADOR	 add constraint 	FK_SCS_PROCURADOR_POBLACION	 foreign key (	IDPOBLACION	)	 references 	CEN_POBLACIONES	 (	IDPOBLACION	) 		 Deferrable;
alter table 	SCS_PROCURADOR	 add constraint 	FK_SCS_PROCURADOR_INSTITUCION	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_PROCURADOR	 add constraint 	FK_SCS_PROCURADOR_CENCOLPROC	 foreign key (	IDCOLPROCURADOR	)	 references 	CEN_COLEGIOPROCURADOR	 (	IDCOLPROCURADOR	) 		 Deferrable;
alter table 	SCS_PROCEDIMIENTOSPROCURADORES	 drop constraint 	FK_SCS_PROCEDIMI_SCS_JURISDICC	 ; 
alter table 	SCS_PROCEDIMIENTOSPROCURADORES	 add constraint 	FK_SCS_PROCEDIMI_SCS_JURISDICC	 foreign key (	IDJURISDICCION	)	 references 	SCS_JURISDICCION	 (	IDJURISDICCION	) 		 Deferrable;
alter table 	SCS_PROCEDIMIENTOS	 drop constraint 	FK_SCS_PROCEDIMIENTO_JURISDICC	 ; 
alter table 	SCS_PROCEDIMIENTOS	 add constraint 	FK_SCS_PROCEDIMIENTO_JURISDICC	 foreign key (	IDJURISDICCION	)	 references 	SCS_JURISDICCION	 (	IDJURISDICCION	) 		 Deferrable;
alter table 	SCS_PRISION	 drop constraint 	FK_SCS_PRISION_PROVINCIA	 ; 
alter table 	SCS_PRISION	 drop constraint 	FK_SCS_PRISION_POBLACION	 ; 
alter table 	SCS_PRISION	 drop constraint 	FK_SCS_PRISION_INSTITUCION	 ; 
alter table 	SCS_PRISION	 add constraint 	FK_SCS_PRISION_PROVINCIA	 foreign key (	IDPROVINCIA	)	 references 	CEN_PROVINCIAS	 (	IDPROVINCIA	) 		 Deferrable;
alter table 	SCS_PRISION	 add constraint 	FK_SCS_PRISION_POBLACION	 foreign key (	IDPOBLACION	)	 references 	CEN_POBLACIONES	 (	IDPOBLACION	) 		 Deferrable;
alter table 	SCS_PRISION	 add constraint 	FK_SCS_PRISION_INSTITUCION	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_PRETENSION	 drop constraint 	FK_PRETENSION_JURISDICCION	 ; 
alter table 	SCS_PRETENSION	 drop constraint 	FK_PRETENSION_IDINSTI	 ; 
alter table 	SCS_PRETENSION	 add constraint 	FK_PRETENSION_JURISDICCION	 foreign key (	IDJURISDICCION	)	 references 	SCS_JURISDICCION	 (	IDJURISDICCION	) 		 Deferrable;
alter table 	SCS_PRETENSION	 add constraint 	FK_PRETENSION_IDINSTI	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_PRECIOHITO	 drop constraint 	FK_PRECIOHIGO_HITOFACTURABLE	 ; 
alter table 	SCS_PRECIOHITO	 add constraint 	FK_PRECIOHIGO_HITOFACTURABLE	 foreign key (	IDHITOFACTURABLE	)	 references 	SCS_HITOFACTURABLE	 (	IDHITO	) 		 Deferrable;
alter table 	SCS_PERSONAJG	 drop constraint 	SPG_CIN_FK	 ; 
alter table 	SCS_PERSONAJG	 drop constraint 	FK_SCS_PERSONAJG_TIPOIDENTIFIC	 ; 
alter table 	SCS_PERSONAJG	 drop constraint 	FK_SCS_PERSONAJG_PROFESION	 ; 
alter table 	SCS_PERSONAJG	 drop constraint 	FK_SCS_PERSONAJG_POBLACIONES	 ; 
alter table 	SCS_PERSONAJG	 drop constraint 	FK_SCS_PERSONAJG_PERSONAJG	 ; 
alter table 	SCS_PERSONAJG	 drop constraint 	FK_SCS_PERSONAJG_PAIS	 ; 
alter table 	SCS_PERSONAJG	 drop constraint 	FK_SCS_PERSONAJG_IDLENGUAJE	 ; 
alter table 	SCS_PERSONAJG	 drop constraint 	FK_SCS_PERSONAJG_ESTADOCIVIL	 ; 
alter table 	SCS_PERSONAJG	 add constraint 	SPG_CIN_FK	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_PERSONAJG	 add constraint 	FK_SCS_PERSONAJG_TIPOIDENTIFIC	 foreign key (	IDTIPOIDENTIFICACION	)	 references 	CEN_TIPOIDENTIFICACION	 (	IDTIPOIDENTIFICACION	) 		 Deferrable;
alter table 	SCS_PERSONAJG	 add constraint 	FK_SCS_PERSONAJG_PROFESION	 foreign key (	IDPROFESION	)	 references 	SCS_PROFESION	 (	IDPROFESION	) 		 Deferrable;
alter table 	SCS_PERSONAJG	 add constraint 	FK_SCS_PERSONAJG_POBLACIONES	 foreign key (	IDPOBLACION	)	 references 	CEN_POBLACIONES	 (	IDPOBLACION	) 		 Deferrable;
alter table 	SCS_PERSONAJG	 add constraint 	FK_SCS_PERSONAJG_PERSONAJG	 foreign key (	IDREPRESENTANTEJG,IDINSTITUCION	)	 references 	SCS_PERSONAJG	 (	IDPERSONA,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_PERSONAJG	 add constraint 	FK_SCS_PERSONAJG_PAIS	 foreign key (	IDPAIS	)	 references 	CEN_PAIS	 (	IDPAIS	) 		 Deferrable;
alter table 	SCS_PERSONAJG	 add constraint 	FK_SCS_PERSONAJG_IDLENGUAJE	 foreign key (	IDLENGUAJE	)	 references 	ADM_LENGUAJES	 (	IDLENGUAJE	) 		 Deferrable;
alter table 	SCS_PERSONAJG	 add constraint 	FK_SCS_PERSONAJG_ESTADOCIVIL	 foreign key (	IDESTADOCIVIL	)	 references 	CEN_ESTADOCIVIL	 (	IDESTADOCIVIL	) 		 Deferrable;
alter table 	SCS_PERMUTA_CABECERA	 drop constraint 	FK_SCS_PERMUTA_CABECERA	 ; 
alter table 	SCS_PERMUTA_CABECERA	 drop constraint 	FK_SCS_PERMUTACAB_SCS_CALGUARD	 ; 
alter table 	SCS_PERMUTA_CABECERA	 add constraint 	FK_SCS_PERMUTA_CABECERA	 foreign key (	FECHA,IDINSTITUCION,IDTURNO,IDGUARDIA,IDPERSONA	)	 references 	SCS_CABECERAGUARDIAS	 (	FECHAINICIO,IDINSTITUCION,IDTURNO,IDGUARDIA,IDPERSONA	) 		 Deferrable;
alter table 	SCS_PERMUTA_CABECERA	 add constraint 	FK_SCS_PERMUTACAB_SCS_CALGUARD	 foreign key (	IDCALENDARIOGUARDIAS,IDINSTITUCION,IDTURNO,IDGUARDIA	)	 references 	SCS_CALENDARIOGUARDIAS	 (	IDCALENDARIOGUARDIAS,IDINSTITUCION,IDTURNO,IDGUARDIA	) 		 Deferrable;
alter table 	SCS_PERMUTAGUARDIAS	 drop constraint 	FK_SCS_PERMUTASOL_SCS_CALGUARD	 ; 
alter table 	SCS_PERMUTAGUARDIAS	 drop constraint 	FK_SCS_PERMUTACON_SCS_CALGUARD	 ; 
alter table 	SCS_PERMUTAGUARDIAS	 drop constraint 	FK_PER_SOLICITANTE	 ; 
alter table 	SCS_PERMUTAGUARDIAS	 drop constraint 	FK_PER_CONFIRMADOR	 ; 
alter table 	SCS_PERMUTAGUARDIAS	 add constraint 	FK_SCS_PERMUTASOL_SCS_CALGUARD	 foreign key (	IDINSTITUCION,IDTURNO_SOLICITANTE,IDCALENDARIOGUARDIAS_SOLICITAN,IDGUARDIA_SOLICITANTE	)	 references 	SCS_CALENDARIOGUARDIAS	 (	IDINSTITUCION,IDTURNO,IDCALENDARIOGUARDIAS,IDGUARDIA	) 		 Deferrable;
alter table 	SCS_PERMUTAGUARDIAS	 add constraint 	FK_SCS_PERMUTACON_SCS_CALGUARD	 foreign key (	IDINSTITUCION,IDTURNO_CONFIRMADOR,IDCALENDARIOGUARDIAS_CONFIRMAD,IDGUARDIA_CONFIRMADOR	)	 references 	SCS_CALENDARIOGUARDIAS	 (	IDINSTITUCION,IDTURNO,IDCALENDARIOGUARDIAS,IDGUARDIA	) 		 Deferrable;
alter table 	SCS_PERMUTAGUARDIAS	 add constraint 	FK_PER_SOLICITANTE	 foreign key (	ID_PER_CAB_SOLICITANTE,IDINSTITUCION	)	 references 	SCS_PERMUTA_CABECERA	 (	ID_PERMUTA_CABECERA,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_PERMUTAGUARDIAS	 add constraint 	FK_PER_CONFIRMADOR	 foreign key (	ID_PER_CAB_CONFIRMADOR,IDINSTITUCION	)	 references 	SCS_PERMUTA_CABECERA	 (	ID_PERMUTA_CABECERA,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_MATERIAJURISDICCION	 drop constraint 	FK_SCS_MATERIA_MATERIAJURIS	 ; 
alter table 	SCS_MATERIAJURISDICCION	 drop constraint 	FK_SCS_JURIS_MATERIAJURIS	 ; 
alter table 	SCS_MATERIAJURISDICCION	 add constraint 	FK_SCS_MATERIA_MATERIAJURIS	 foreign key (	IDMATERIA,IDINSTITUCION,IDAREA	)	 references 	SCS_MATERIA	 (	IDMATERIA,IDINSTITUCION,IDAREA	) 		 Deferrable;
alter table 	SCS_MATERIAJURISDICCION	 add constraint 	FK_SCS_JURIS_MATERIAJURIS	 foreign key (	IDJURISDICCION	)	 references 	SCS_JURISDICCION	 (	IDJURISDICCION	) 		 Deferrable;
alter table 	SCS_MATERIA	 drop constraint 	FK_9MA_9AR	 ; 
alter table 	SCS_MATERIA	 add constraint 	FK_9MA_9AR	 foreign key (	IDINSTITUCION,IDAREA	)	 references 	SCS_AREA	 (	IDINSTITUCION,IDAREA	) 		 Deferrable;
alter table 	SCS_LISTAGUARDIAS	 drop constraint 	SLA_CIN_FK	 ; 
alter table 	SCS_LISTAGUARDIAS	 add constraint 	SLA_CIN_FK	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_JUZGADOPROCEDIMIENTO	 drop constraint 	FK_SCS_JUZGADOPROCEDIMIENTO_JU	 ; 
alter table 	SCS_JUZGADOPROCEDIMIENTO	 drop constraint 	FK_JUZGAGOPROCEDIMIENTO_PROCE	 ; 
alter table 	SCS_JUZGADOPROCEDIMIENTO	 add constraint 	FK_SCS_JUZGADOPROCEDIMIENTO_JU	 foreign key (	IDJUZGADO,IDINSTITUCION_JUZG	)	 references 	SCS_JUZGADO	 (	IDJUZGADO,IDINSTITUCION	) 	 on delete cascade	 Deferrable;
alter table 	SCS_JUZGADOPROCEDIMIENTO	 add constraint 	FK_JUZGAGOPROCEDIMIENTO_PROCE	 foreign key (	IDPROCEDIMIENTO,IDINSTITUCION	)	 references 	SCS_PROCEDIMIENTOS	 (	IDPROCEDIMIENTO,IDINSTITUCION	) 	 on delete cascade	 Deferrable;
alter table 	SCS_JUZGADO	 drop constraint 	FK_SCS_JUZGADO_PROVINCIA	 ; 
alter table 	SCS_JUZGADO	 drop constraint 	FK_SCS_JUZGADO_POBLACION	 ; 
alter table 	SCS_JUZGADO	 drop constraint 	FK_SCS_JUZGADO_INSTITUCION	 ; 
alter table 	SCS_JUZGADO	 add constraint 	FK_SCS_JUZGADO_PROVINCIA	 foreign key (	IDPROVINCIA	)	 references 	CEN_PROVINCIAS	 (	IDPROVINCIA	) 		 Deferrable;
alter table 	SCS_JUZGADO	 add constraint 	FK_SCS_JUZGADO_POBLACION	 foreign key (	IDPOBLACION	)	 references 	CEN_POBLACIONES	 (	IDPOBLACION	) 		 Deferrable;
alter table 	SCS_JUZGADO	 add constraint 	FK_SCS_JUZGADO_INSTITUCION	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_INSCRIPCIONTURNO	 drop constraint 	SIO_9IT_1CO	 ; 
alter table 	SCS_INSCRIPCIONTURNO	 drop constraint 	FK_9IT_9TU	 ; 
alter table 	SCS_INSCRIPCIONTURNO	 add constraint 	SIO_9IT_1CO	 foreign key (	IDINSTITUCION,IDPERSONA	)	 references 	CEN_COLEGIADO	 (	IDINSTITUCION,IDPERSONA	) 		 Deferrable;
alter table 	SCS_INSCRIPCIONTURNO	 add constraint 	FK_9IT_9TU	 foreign key (	IDTURNO,IDINSTITUCION	)	 references 	SCS_TURNO	 (	IDTURNO,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_INSCRIPCIONGUARDIA	 drop constraint 	SIA_CCO_FK	 ; 
alter table 	SCS_INSCRIPCIONGUARDIA	 drop constraint 	FK_SCS_INSCRIPCIONGUARDIA	 ; 
alter table 	SCS_INSCRIPCIONGUARDIA	 add constraint 	SIA_CCO_FK	 foreign key (	IDINSTITUCION,IDPERSONA	)	 references 	CEN_COLEGIADO	 (	IDINSTITUCION,IDPERSONA	) 		 Deferrable;
alter table 	SCS_INSCRIPCIONGUARDIA	 add constraint 	FK_SCS_INSCRIPCIONGUARDIA	 foreign key (	IDGUARDIA,IDINSTITUCION,IDTURNO	)	 references 	SCS_GUARDIASTURNO	 (	IDGUARDIA,IDINSTITUCION,IDTURNO	) 	 on delete cascade	 Deferrable;
alter table 	SCS_INCOMPATIBILIDADGUARDIAS	 drop constraint 	FK_SCS_INCOMPATIBIUARDIA2	 ; 
alter table 	SCS_INCOMPATIBILIDADGUARDIAS	 drop constraint 	FK_SCS_INCOMPATIBILIDADGUARDIA	 ; 
alter table 	SCS_INCOMPATIBILIDADGUARDIAS	 add constraint 	FK_SCS_INCOMPATIBIUARDIA2	 foreign key (	IDGUARDIA_INCOMPATIBLE,IDINSTITUCION,IDTURNO_INCOMPATIBLE	)	 references 	SCS_GUARDIASTURNO	 (	IDGUARDIA,IDINSTITUCION,IDTURNO	) 	 on delete cascade	 Deferrable;
alter table 	SCS_INCOMPATIBILIDADGUARDIAS	 add constraint 	FK_SCS_INCOMPATIBILIDADGUARDIA	 foreign key (	IDGUARDIA,IDINSTITUCION,IDTURNO	)	 references 	SCS_GUARDIASTURNO	 (	IDGUARDIA,IDINSTITUCION,IDTURNO	) 	 on delete cascade	 Deferrable;
alter table 	SCS_INCLUSIONGUARDIASENLISTAS	 drop constraint 	FK_INCLUSIONGUARDIAS_LISTAS	 ; 
alter table 	SCS_INCLUSIONGUARDIASENLISTAS	 drop constraint 	FK_GUARDIATURNO_INCLUSION	 ; 
alter table 	SCS_INCLUSIONGUARDIASENLISTAS	 add constraint 	FK_INCLUSIONGUARDIAS_LISTAS	 foreign key (	IDLISTA,IDINSTITUCION	)	 references 	SCS_LISTAGUARDIAS	 (	IDLISTA,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_INCLUSIONGUARDIASENLISTAS	 add constraint 	FK_GUARDIATURNO_INCLUSION	 foreign key (	IDGUARDIA,IDINSTITUCION,IDTURNO	)	 references 	SCS_GUARDIASTURNO	 (	IDGUARDIA,IDINSTITUCION,IDTURNO	) 	 on delete cascade	 Deferrable;
alter table 	SCS_HITOFACTURABLEGUARDIA	 drop constraint 	PK_SCS_HITOFACTURABLEGUAR_HITO	 ; 
alter table 	SCS_HITOFACTURABLEGUARDIA	 drop constraint 	FK_SCS_HITOFACTURABLEGUARDIA	 ; 
alter table 	SCS_HITOFACTURABLEGUARDIA	 add constraint 	PK_SCS_HITOFACTURABLEGUAR_HITO	 foreign key (	IDHITO	)	 references 	SCS_HITOFACTURABLE	 (	IDHITO	) 		 Deferrable;
alter table 	SCS_HITOFACTURABLEGUARDIA	 add constraint 	FK_SCS_HITOFACTURABLEGUARDIA	 foreign key (	IDGUARDIA,IDINSTITUCION,IDTURNO	)	 references 	SCS_GUARDIASTURNO	 (	IDGUARDIA,IDINSTITUCION,IDTURNO	) 	 on delete cascade	 Deferrable;
alter table 	SCS_HITOFACTURABLE	 drop constraint 	FK_SCS_HITOFACT_HITOFACT	 ; 
alter table 	SCS_HITOFACTURABLE	 add constraint 	FK_SCS_HITOFACT_HITOFACT	 foreign key (	IDHITOCONFIGURACION	)	 references 	SCS_HITOFACTURABLE	 (	IDHITO	) 		 Deferrable;
alter table 	SCS_HCO_CONF_PROG_CALENDARIOS	 drop constraint 	FK_SCS_PROG_CALENDARIOS	 ; 
alter table 	SCS_HCO_CONF_PROG_CALENDARIOS	 drop constraint 	FK_SCS_GUARDIASTURNO_HCOPR	 ; 
alter table 	SCS_HCO_CONF_PROG_CALENDARIOS	 drop constraint 	FK_SCS_CONJUNTOGUARDIAS_HCOPR	 ; 
alter table 	SCS_HCO_CONF_PROG_CALENDARIOS	 add constraint 	FK_SCS_PROG_CALENDARIOS	 foreign key (	IDPROGCALENDARIO	)	 references 	SCS_PROG_CALENDARIOS	 (	IDPROGCALENDARIO	) 		 Deferrable;
alter table 	SCS_HCO_CONF_PROG_CALENDARIOS	 add constraint 	FK_SCS_GUARDIASTURNO_HCOPR	 foreign key (	IDGUARDIA,IDINSTITUCION,IDTURNO	)	 references 	SCS_GUARDIASTURNO	 (	IDGUARDIA,IDINSTITUCION,IDTURNO	) 		 Deferrable;
alter table 	SCS_HCO_CONF_PROG_CALENDARIOS	 add constraint 	FK_SCS_CONJUNTOGUARDIAS_HCOPR	 foreign key (	IDINSTITUCION,IDCONJUNTOGUARDIA	)	 references 	SCS_CONJUNTOGUARDIAS	 (	IDINSTITUCION,IDCONJUNTOGUARDIA	) 		 Deferrable;
alter table 	SCS_GUARDIASTURNO	 drop constraint 	FK_SCS_TIPOSGUARDIAS	 ; 
alter table 	SCS_GUARDIASTURNO	 drop constraint 	FK_SCS_INSCRIPCION_ULTIMO	 ; 
alter table 	SCS_GUARDIASTURNO	 drop constraint 	FK_SCS_GUARDIATURNO_PPAL	 ; 
alter table 	SCS_GUARDIASTURNO	 drop constraint 	FK_SCS_GUARDIASTURNO_TURNO	 ; 
alter table 	SCS_GUARDIASTURNO	 drop constraint 	FK_SCS_GUARDIASTURNO_SUSTIT	 ; 
alter table 	SCS_GUARDIASTURNO	 drop constraint 	FK_SCS_GUARDIASTURNO_PARTIDAPR	 ; 
alter table 	SCS_GUARDIASTURNO	 drop constraint 	FK_SCS_GUARDIASTURNO_ORDENACIO	 ; 
alter table 	SCS_GUARDIASTURNO	 add constraint 	FK_SCS_TIPOSGUARDIAS	 foreign key (	IDTIPOGUARDIA	)	 references 	SCS_TIPOSGUARDIAS	 (	IDTIPOGUARDIA	) 		 Deferrable;
alter table 	SCS_GUARDIASTURNO	 add constraint 	FK_SCS_INSCRIPCION_ULTIMO	 foreign key (	FECHASUSCRIPCION_ULTIMO,IDINSTITUCION,IDTURNO,IDGUARDIA,IDPERSONA_ULTIMO	)	 references 	SCS_INSCRIPCIONGUARDIA	 (	FECHASUSCRIPCION,IDINSTITUCION,IDTURNO,IDGUARDIA,IDPERSONA	) 		 Deferrable;
alter table 	SCS_GUARDIASTURNO	 add constraint 	FK_SCS_GUARDIATURNO_PPAL	 foreign key (	IDGUARDIAPRINCIPAL,IDINSTITUCIONPRINCIPAL,IDTURNOPRINCIPAL	)	 references 	SCS_GUARDIASTURNO	 (	IDGUARDIA,IDINSTITUCION,IDTURNO	) 		 Deferrable;
alter table 	SCS_GUARDIASTURNO	 add constraint 	FK_SCS_GUARDIASTURNO_TURNO	 foreign key (	IDTURNO,IDINSTITUCION	)	 references 	SCS_TURNO	 (	IDTURNO,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_GUARDIASTURNO	 add constraint 	FK_SCS_GUARDIASTURNO_SUSTIT	 foreign key (	IDGUARDIASUSTITUCION,IDINSTITUCION,IDTURNOSUSTITUCION	)	 references 	SCS_GUARDIASTURNO	 (	IDGUARDIA,IDINSTITUCION,IDTURNO	) 		 Deferrable;
alter table 	SCS_GUARDIASTURNO	 add constraint 	FK_SCS_GUARDIASTURNO_PARTIDAPR	 foreign key (	IDPARTIDAPRESUPUESTARIA,IDINSTITUCION	)	 references 	SCS_PARTIDAPRESUPUESTARIA	 (	IDPARTIDAPRESUPUESTARIA,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_GUARDIASTURNO	 add constraint 	FK_SCS_GUARDIASTURNO_ORDENACIO	 foreign key (	IDORDENACIONCOLAS	)	 references 	SCS_ORDENACIONCOLAS	 (	IDORDENACIONCOLAS	) 		 Deferrable;
alter table 	SCS_GUARDIASCOLEGIADO	 drop constraint 	FK_SCS_GUARDIASCOLEGIADO_FACT	 ; 
alter table 	SCS_GUARDIASCOLEGIADO	 drop constraint 	FK_SCS_GUARDIASCOLEGIADO_COLEG	 ; 
alter table 	SCS_GUARDIASCOLEGIADO	 drop constraint 	FK_SCS_GUARDIASCOLEGIADO_CABE	 ; 
alter table 	SCS_GUARDIASCOLEGIADO	 add constraint 	FK_SCS_GUARDIASCOLEGIADO_FACT	 foreign key (	IDFACTURACION,IDINSTITUCION	)	 references 	FCS_FACTURACIONJG	 (	IDFACTURACION,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_GUARDIASCOLEGIADO	 add constraint 	FK_SCS_GUARDIASCOLEGIADO_COLEG	 foreign key (	IDPERSONA,IDINSTITUCION	)	 references 	CEN_COLEGIADO	 (	IDPERSONA,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_GUARDIASCOLEGIADO	 add constraint 	FK_SCS_GUARDIASCOLEGIADO_CABE	 foreign key (	FECHAINICIO,IDINSTITUCION,IDTURNO,IDGUARDIA,IDPERSONA	)	 references 	SCS_CABECERAGUARDIAS	 (	FECHAINICIO,IDINSTITUCION,IDTURNO,IDGUARDIA,IDPERSONA	) 		 Deferrable;
alter table 	SCS_GRUPOGUARDIACOLEGIADO_HIST	 drop constraint 	FK_SCS_GRUPOGUARDIACOLEGIADO	 ; 
alter table 	SCS_GRUPOGUARDIACOLEGIADO_HIST	 drop constraint 	FK_SCS_GRUPOGUARDIA	 ; 
alter table 	SCS_GRUPOGUARDIACOLEGIADO_HIST	 drop constraint 	FK_SCS_CALENDARIOGUARDIAS	 ; 
alter table 	SCS_GRUPOGUARDIACOLEGIADO_HIST	 add constraint 	FK_SCS_GRUPOGUARDIACOLEGIADO	 foreign key (	IDGRUPOGUARDIACOLEGIADO	)	 references 	SCS_GRUPOGUARDIACOLEGIADO	 (	IDGRUPOGUARDIACOLEGIADO	) 		 Deferrable;
alter table 	SCS_GRUPOGUARDIACOLEGIADO_HIST	 add constraint 	FK_SCS_GRUPOGUARDIA	 foreign key (	IDGRUPOGUARDIA	)	 references 	SCS_GRUPOGUARDIA	 (	IDGRUPOGUARDIA	) 		 Deferrable;
alter table 	SCS_GRUPOGUARDIACOLEGIADO_HIST	 add constraint 	FK_SCS_CALENDARIOGUARDIAS	 foreign key (	IDCALENDARIOGUARDIAS,IDINSTITUCION,IDTURNO,IDGUARDIA	)	 references 	SCS_CALENDARIOGUARDIAS	 (	IDCALENDARIOGUARDIAS,IDINSTITUCION,IDTURNO,IDGUARDIA	) 		 Deferrable;
alter table 	SCS_GRUPOGUARDIACOLEGIADO	 drop constraint 	FK_SCS_GRUPOGUA_INSCRIPCIONGUA	 ; 
alter table 	SCS_GRUPOGUARDIACOLEGIADO	 drop constraint 	FK_SCS_GRUPOGUARDIA_GRUPO	 ; 
alter table 	SCS_GRUPOGUARDIACOLEGIADO	 add constraint 	FK_SCS_GRUPOGUA_INSCRIPCIONGUA	 foreign key (	FECHASUSCRIPCION,IDINSTITUCION,IDTURNO,IDGUARDIA,IDPERSONA	)	 references 	SCS_INSCRIPCIONGUARDIA	 (	FECHASUSCRIPCION,IDINSTITUCION,IDTURNO,IDGUARDIA,IDPERSONA	) 		 Deferrable;
alter table 	SCS_GRUPOGUARDIACOLEGIADO	 add constraint 	FK_SCS_GRUPOGUARDIA_GRUPO	 foreign key (	IDGRUPOGUARDIA	)	 references 	SCS_GRUPOGUARDIA	 (	IDGRUPOGUARDIA	) 		 Deferrable;
alter table 	SCS_GRUPOGUARDIA	 drop constraint 	FK_SCS_GRUPOGUARDIA_GUARDIA	 ; 
alter table 	SCS_GRUPOGUARDIA	 add constraint 	FK_SCS_GRUPOGUARDIA_GUARDIA	 foreign key (	IDGUARDIA,IDINSTITUCION,IDTURNO	)	 references 	SCS_GUARDIASTURNO	 (	IDGUARDIA,IDINSTITUCION,IDTURNO	) 		 Deferrable;
alter table 	SCS_GRUPOFACTURACION	 drop constraint 	FK_GRUPOFACTURACION_INSTITUCIO	 ; 
alter table 	SCS_GRUPOFACTURACION	 add constraint 	FK_GRUPOFACTURACION_INSTITUCIO	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_ESTADOEJG	 drop constraint 	FK_SCS_ESTADOEJGESTADO_ESTADO	 ; 
alter table 	SCS_ESTADOEJG	 drop constraint 	FK_SCS_ESTADOEJGESTADO_EJG	 ; 
alter table 	SCS_ESTADOEJG	 add constraint 	FK_SCS_ESTADOEJGESTADO_ESTADO	 foreign key (	IDESTADOEJG	)	 references 	SCS_MAESTROESTADOSEJG	 (	IDESTADOEJG	) 		 Deferrable;
alter table 	SCS_ESTADOEJG	 add constraint 	FK_SCS_ESTADOEJGESTADO_EJG	 foreign key (	NUMERO,IDINSTITUCION,IDTIPOEJG,ANIO	)	 references 	SCS_EJG	 (	NUMERO,IDINSTITUCION,IDTIPOEJG,ANIO	) 	 on delete cascade	 Deferrable;
alter table 	SCS_EJG_RESOLUCION	 drop constraint 	FK_EJG_RESOLUCION_EJG	 ; 
alter table 	SCS_EJG_RESOLUCION	 add constraint 	FK_EJG_RESOLUCION_EJG	 foreign key (	IDTIPOEJG,ANIO,NUMERO,IDINSTITUCION	)	 references 	SCS_EJG	 (	IDTIPOEJG,ANIO,NUMERO,IDINSTITUCION	) 	 on delete cascade	 Deferrable;
alter table 	SCS_EJG_PRESTACION_RECHAZADA	 drop constraint 	FK_SCS_EJG_PREST_SCS_PRESTACIO	 ; 
alter table 	SCS_EJG_PRESTACION_RECHAZADA	 drop constraint 	FK_SCS_EJG_PRESTACION_SCS_EJG	 ; 
alter table 	SCS_EJG_PRESTACION_RECHAZADA	 add constraint 	FK_SCS_EJG_PREST_SCS_PRESTACIO	 foreign key (	IDINSTITUCION,IDPRESTACION	)	 references 	SCS_PRESTACION	 (	IDINSTITUCION,IDPRESTACION	) 		 Deferrable;
alter table 	SCS_EJG_PRESTACION_RECHAZADA	 add constraint 	FK_SCS_EJG_PRESTACION_SCS_EJG	 foreign key (	IDTIPOEJG,IDINSTITUCION,ANIO,NUMERO	)	 references 	SCS_EJG	 (	IDTIPOEJG,IDINSTITUCION,ANIO,NUMERO	) 		 Deferrable;
alter table 	SCS_EJG_ACTA	 drop constraint 	FK_EJGACTA_TIPORESOLUCION	 ; 
alter table 	SCS_EJG_ACTA	 drop constraint 	FK_EJGACTA_TIPOFUNDAMENTOS	 ; 
alter table 	SCS_EJG_ACTA	 drop constraint 	FK_EJGACTA_EJG	 ; 
alter table 	SCS_EJG_ACTA	 drop constraint 	FK_EJGACTA_ACTA	 ; 
alter table 	SCS_EJG_ACTA	 add constraint 	FK_EJGACTA_TIPORESOLUCION	 foreign key (	IDTIPORATIFICACIONEJG	)	 references 	SCS_TIPORESOLUCION	 (	IDTIPORESOLUCION	) 		 Deferrable;
alter table 	SCS_EJG_ACTA	 add constraint 	FK_EJGACTA_TIPOFUNDAMENTOS	 foreign key (	IDFUNDAMENTOJURIDICO,IDINSTITUCIONEJG	)	 references 	SCS_TIPOFUNDAMENTOS	 (	IDFUNDAMENTO,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_EJG_ACTA	 add constraint 	FK_EJGACTA_EJG	 foreign key (	NUMEROEJG,IDINSTITUCIONEJG,IDTIPOEJG,ANIOEJG	)	 references 	SCS_EJG	 (	NUMERO,IDINSTITUCION,IDTIPOEJG,ANIO	) 		 Deferrable;
alter table 	SCS_EJG_ACTA	 add constraint 	FK_EJGACTA_ACTA	 foreign key (	ANIOACTA,IDACTA,IDINSTITUCIONACTA	)	 references 	SCS_ACTACOMISION	 (	ANIOACTA,IDACTA,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_EJGDESIGNA	 drop constraint 	FK_EJGDESIGNA_EJG	 ; 
alter table 	SCS_EJGDESIGNA	 drop constraint 	FK_EJGDESIGNA_DESIGNA	 ; 
alter table 	SCS_EJGDESIGNA	 add constraint 	FK_EJGDESIGNA_EJG	 foreign key (	NUMEROEJG,IDINSTITUCION,IDTIPOEJG,ANIOEJG	)	 references 	SCS_EJG	 (	NUMERO,IDINSTITUCION,IDTIPOEJG,ANIO	) 		 Deferrable;
alter table 	SCS_EJGDESIGNA	 add constraint 	FK_EJGDESIGNA_DESIGNA	 foreign key (	NUMERODESIGNA,IDINSTITUCION,IDTURNO,ANIODESIGNA	)	 references 	SCS_DESIGNA	 (	NUMERO,IDINSTITUCION,IDTURNO,ANIO	) 		 Deferrable;
alter table 	SCS_EJG	 drop constraint 	SEG_SPG_FK	 ; 
alter table 	SCS_EJG	 drop constraint 	SEG_GUARDIASTURNO_FK	 ; 
alter table 	SCS_EJG	 drop constraint 	SEG_COLEGIADO_FK	 ; 
alter table 	SCS_EJG	 drop constraint 	FK_SCS_EJG_TIPOSENTIDOAUTO	 ; 
alter table 	SCS_EJG	 drop constraint 	FK_SCS_EJG_TIPORESOLUCION	 ; 
alter table 	SCS_EJG	 drop constraint 	FK_SCS_EJG_TIPORESOLAUTO	 ; 
alter table 	SCS_EJG	 drop constraint 	FK_SCS_EJG_TIPORENUNCIA	 ; 
alter table 	SCS_EJG	 drop constraint 	FK_SCS_EJG_TIPOFUNDAMENTOSCALI	 ; 
alter table 	SCS_EJG	 drop constraint 	FK_SCS_EJG_TIPOFUNDAMENTOS	 ; 
alter table 	SCS_EJG	 drop constraint 	FK_SCS_EJG_TIPOEJGCOLEGIO	 ; 
alter table 	SCS_EJG	 drop constraint 	FK_SCS_EJG_TIPOEJG	 ; 
alter table 	SCS_EJG	 drop constraint 	FK_SCS_EJG_TIPODICTAMENEJG	 ; 
alter table 	SCS_EJG	 drop constraint 	FK_SCS_EJG_PROCURADOR	 ; 
alter table 	SCS_EJG	 drop constraint 	FK_SCS_EJG_PRETENSION	 ; 
alter table 	SCS_EJG	 drop constraint 	FK_SCS_EJG_PRECEPTIVO	 ; 
alter table 	SCS_EJG	 drop constraint 	FK_SCS_EJG_PONENTE	 ; 
alter table 	SCS_EJG	 drop constraint 	FK_SCS_EJG_ORIGEN_CAJG	 ; 
alter table 	SCS_EJG	 drop constraint 	FK_SCS_EJG_JUZGADO	 ; 
alter table 	SCS_EJG	 drop constraint 	FK_SCS_EJG_FACTURACIONJG	 ; 
alter table 	SCS_EJG	 drop constraint 	FK_SCS_EJG_DICTAMENEJG	 ; 
alter table 	SCS_EJG	 drop constraint 	FK_SCS_EJG_COMISARIA	 ; 
alter table 	SCS_EJG	 drop constraint 	FK_SCS_EJG_CALIDAD	 ; 
alter table 	SCS_EJG	 drop constraint 	FK_SCS_EJG_ACTA	 ; 
alter table 	SCS_EJG	 drop constraint 	FK_SCSEJG_ECOMCOLA	 ; 
alter table 	SCS_EJG	 add constraint 	SEG_SPG_FK	 foreign key (	IDPERSONAJG,IDINSTITUCION	)	 references 	SCS_PERSONAJG	 (	IDPERSONA,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_EJG	 add constraint 	SEG_GUARDIASTURNO_FK	 foreign key (	GUARDIATURNO_IDGUARDIA,IDINSTITUCION,GUARDIATURNO_IDTURNO	)	 references 	SCS_GUARDIASTURNO	 (	IDGUARDIA,IDINSTITUCION,IDTURNO	) 		 Deferrable;
alter table 	SCS_EJG	 add constraint 	SEG_COLEGIADO_FK	 foreign key (	IDINSTITUCION,IDPERSONA	)	 references 	CEN_COLEGIADO	 (	IDINSTITUCION,IDPERSONA	) 		 Deferrable;
alter table 	SCS_EJG	 add constraint 	FK_SCS_EJG_TIPOSENTIDOAUTO	 foreign key (	IDTIPOSENTIDOAUTO	)	 references 	SCS_TIPOSENTIDOAUTO	 (	IDTIPOSENTIDOAUTO	) 		 Deferrable;
alter table 	SCS_EJG	 add constraint 	FK_SCS_EJG_TIPORESOLUCION	 foreign key (	IDTIPORATIFICACIONEJG	)	 references 	SCS_TIPORESOLUCION	 (	IDTIPORESOLUCION	) 		 Deferrable;
alter table 	SCS_EJG	 add constraint 	FK_SCS_EJG_TIPORESOLAUTO	 foreign key (	IDTIPORESOLAUTO	)	 references 	SCS_TIPORESOLAUTO	 (	IDTIPORESOLAUTO	) 		 Deferrable;
alter table 	SCS_EJG	 add constraint 	FK_SCS_EJG_TIPORENUNCIA	 foreign key (	IDRENUNCIA	)	 references 	SCS_RENUNCIA	 (	IDRENUNCIA	) 		 Deferrable;
alter table 	SCS_EJG	 add constraint 	FK_SCS_EJG_TIPOFUNDAMENTOSCALI	 foreign key (	IDFUNDAMENTOCALIF,IDINSTITUCION	)	 references 	SCS_TIPOFUNDAMENTOCALIF	 (	IDFUNDAMENTOCALIF,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_EJG	 add constraint 	FK_SCS_EJG_TIPOFUNDAMENTOS	 foreign key (	IDFUNDAMENTOJURIDICO,IDINSTITUCION	)	 references 	SCS_TIPOFUNDAMENTOS	 (	IDFUNDAMENTO,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_EJG	 add constraint 	FK_SCS_EJG_TIPOEJGCOLEGIO	 foreign key (	IDTIPOEJGCOLEGIO,IDINSTITUCION	)	 references 	SCS_TIPOEJGCOLEGIO	 (	IDTIPOEJGCOLEGIO,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_EJG	 add constraint 	FK_SCS_EJG_TIPOEJG	 foreign key (	IDTIPOEJG	)	 references 	SCS_TIPOEJG	 (	IDTIPOEJG	) 		 Deferrable;
alter table 	SCS_EJG	 add constraint 	FK_SCS_EJG_TIPODICTAMENEJG	 foreign key (	IDTIPODICTAMENEJG,IDINSTITUCION	)	 references 	SCS_TIPODICTAMENEJG	 (	IDTIPODICTAMENEJG,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_EJG	 add constraint 	FK_SCS_EJG_PROCURADOR	 foreign key (	IDPROCURADOR,IDINSTITUCION_PROC	)	 references 	SCS_PROCURADOR	 (	IDPROCURADOR,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_EJG	 add constraint 	FK_SCS_EJG_PRETENSION	 foreign key (	IDPRETENSION,IDINSTITUCION	)	 references 	SCS_PRETENSION	 (	IDPRETENSION,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_EJG	 add constraint 	FK_SCS_EJG_PRECEPTIVO	 foreign key (	IDPRECEPTIVO	)	 references 	SCS_PRECEPTIVO	 (	IDPRECEPTIVO	) 		 Deferrable;
alter table 	SCS_EJG	 add constraint 	FK_SCS_EJG_PONENTE	 foreign key (	IDINSTITUCIONPONENTE,IDPONENTE	)	 references 	SCS_PONENTE	 (	IDINSTITUCION,IDPONENTE	) 		 Deferrable;
alter table 	SCS_EJG	 add constraint 	FK_SCS_EJG_ORIGEN_CAJG	 foreign key (	IDORIGENCAJG	)	 references 	SCS_ORIGENCAJG	 (	IDORIGENCAJG	) 		 Deferrable;
alter table 	SCS_EJG	 add constraint 	FK_SCS_EJG_JUZGADO	 foreign key (	JUZGADOIDINSTITUCION,JUZGADO	)	 references 	SCS_JUZGADO	 (	IDINSTITUCION,IDJUZGADO	) 		 Deferrable;
alter table 	SCS_EJG	 add constraint 	FK_SCS_EJG_FACTURACIONJG	 foreign key (	IDFACTURACION,IDINSTITUCION	)	 references 	FCS_FACTURACIONJG	 (	IDFACTURACION,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_EJG	 add constraint 	FK_SCS_EJG_DICTAMENEJG	 foreign key (	IDDICTAMEN,IDINSTITUCION	)	 references 	SCS_DICTAMENEJG	 (	IDDICTAMEN,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_EJG	 add constraint 	FK_SCS_EJG_COMISARIA	 foreign key (	COMISARIAIDINSTITUCION,COMISARIA	)	 references 	SCS_COMISARIA	 (	IDINSTITUCION,IDCOMISARIA	) 		 Deferrable;
alter table 	SCS_EJG	 add constraint 	FK_SCS_EJG_CALIDAD	 foreign key (	CALIDADIDINSTITUCION,IDTIPOENCALIDAD	)	 references 	SCS_TIPOENCALIDAD	 (	IDINSTITUCION,IDTIPOENCALIDAD	) 		 Deferrable;
alter table 	SCS_EJG	 add constraint 	FK_SCS_EJG_ACTA	 foreign key (	ANIOACTA,IDACTA,IDINSTITUCIONACTA	)	 references 	SCS_ACTACOMISION	 (	ANIOACTA,IDACTA,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_EJG	 add constraint 	FK_SCSEJG_ECOMCOLA	 foreign key (	IDECOMCOLA	)	 references 	ECOM_COLA	 (	IDECOMCOLA	) 		 Deferrable;
alter table 	SCS_EEJG_XML	 drop constraint 	FK_EEJG_XML_PETICION	 ; 
alter table 	SCS_EEJG_XML	 add constraint 	FK_EEJG_XML_PETICION	 foreign key (	IDPETICION	)	 references 	SCS_EEJG_PETICIONES	 (	IDPETICION	) 		 Deferrable;
alter table 	SCS_EEJG_PETICIONES	 drop constraint 	FK_SCS_EEJG_EJG	 ; 
alter table 	SCS_EEJG_PETICIONES	 drop constraint 	FK_SCSEEJGPETI_ECOMCOLA	 ; 
alter table 	SCS_EEJG_PETICIONES	 add constraint 	FK_SCS_EEJG_EJG	 foreign key (	NUMERO,IDINSTITUCION,IDTIPOEJG,ANIO	)	 references 	SCS_EJG	 (	NUMERO,IDINSTITUCION,IDTIPOEJG,ANIO	) 		 Deferrable;
alter table 	SCS_EEJG_PETICIONES	 add constraint 	FK_SCSEEJGPETI_ECOMCOLA	 foreign key (	IDECOMCOLA	)	 references 	ECOM_COLA	 (	IDECOMCOLA	) 		 Deferrable;
alter table 	SCS_DOCUMENTOEJG	 drop constraint 	FK_SCS_TIPODOCUMENTACION	 ; 
alter table 	SCS_DOCUMENTOEJG	 drop constraint 	FK_CEN_INSTITUCION	 ; 
alter table 	SCS_DOCUMENTOEJG	 add constraint 	FK_SCS_TIPODOCUMENTACION	 foreign key (	IDTIPODOCUMENTOEJG,IDINSTITUCION	)	 references 	SCS_TIPODOCUMENTOEJG	 (	IDTIPODOCUMENTOEJG,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_DOCUMENTOEJG	 add constraint 	FK_CEN_INSTITUCION	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_DOCUMENTACIONSOJ	 drop constraint 	SDJ_SSJ_FK	 ; 
alter table 	SCS_DOCUMENTACIONSOJ	 add constraint 	SDJ_SSJ_FK	 foreign key (	NUMERO,IDINSTITUCION,IDTIPOSOJ,ANIO	)	 references 	SCS_SOJ	 (	NUMERO,IDINSTITUCION,IDTIPOSOJ,ANIO	) 	 on delete cascade	 Deferrable;
alter table 	SCS_DOCUMENTACIONEJG	 drop constraint 	FK_SCS_UNIDADFAMILIAREJG	 ; 
alter table 	SCS_DOCUMENTACIONEJG	 drop constraint 	FK_SCS_DOCEJG_DOCUMENTACIONEJG	 ; 
alter table 	SCS_DOCUMENTACIONEJG	 drop constraint 	FK_SCSPRESENTADOR	 ; 
alter table 	SCS_DOCUMENTACIONEJG	 drop constraint 	FK_GEN_FICHERO	 ; 
alter table 	SCS_DOCUMENTACIONEJG	 drop constraint 	FK_9DE_9EJ	 ; 
alter table 	SCS_DOCUMENTACIONEJG	 add constraint 	FK_SCS_UNIDADFAMILIAREJG	 foreign key (	PRESENTADOR,IDINSTITUCION,IDTIPOEJG,ANIO,NUMERO	)	 references 	SCS_UNIDADFAMILIAREJG	 (	IDPERSONA,IDINSTITUCION,IDTIPOEJG,ANIO,NUMERO	) 		 Deferrable;
alter table 	SCS_DOCUMENTACIONEJG	 add constraint 	FK_SCS_DOCEJG_DOCUMENTACIONEJG	 foreign key (	IDTIPODOCUMENTO,IDINSTITUCION,IDDOCUMENTO	)	 references 	SCS_DOCUMENTOEJG	 (	IDTIPODOCUMENTOEJG,IDINSTITUCION,IDDOCUMENTOEJG	) 		 Deferrable;
alter table 	SCS_DOCUMENTACIONEJG	 add constraint 	FK_SCSPRESENTADOR	 foreign key (	IDMAESTROPRESENTADOR,IDINSTITUCION	)	 references 	SCS_PRESENTADOR	 (	IDPRESENTADOR,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_DOCUMENTACIONEJG	 add constraint 	FK_GEN_FICHERO	 foreign key (	IDFICHERO,IDINSTITUCION	)	 references 	GEN_FICHERO	 (	IDFICHERO,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_DOCUMENTACIONEJG	 add constraint 	FK_9DE_9EJ	 foreign key (	NUMERO,IDINSTITUCION,IDTIPOEJG,ANIO	)	 references 	SCS_EJG	 (	NUMERO,IDINSTITUCION,IDTIPOEJG,ANIO	) 		 Deferrable;
alter table 	SCS_DOCUMENTACIONDESIGNA	 drop constraint 	FK_SCS_TIPODOCDESIGNA	 ; 
alter table 	SCS_DOCUMENTACIONDESIGNA	 drop constraint 	FK_SCS_DESIGNA	 ; 
alter table 	SCS_DOCUMENTACIONDESIGNA	 drop constraint 	FK_SCS_ACTDESIGNA	 ; 
alter table 	SCS_DOCUMENTACIONDESIGNA	 drop constraint 	FK_DOCDES_GENFICHERO	 ; 
alter table 	SCS_DOCUMENTACIONDESIGNA	 add constraint 	FK_SCS_TIPODOCDESIGNA	 foreign key (	IDTIPODOCUMENTO	)	 references 	SCS_TIPODOCUMENTODES	 (	IDTIPODOCUMENTODES	) 		 Deferrable;
alter table 	SCS_DOCUMENTACIONDESIGNA	 add constraint 	FK_SCS_DESIGNA	 foreign key (	NUMERO,IDINSTITUCION,IDTURNO,ANIO	)	 references 	SCS_DESIGNA	 (	NUMERO,IDINSTITUCION,IDTURNO,ANIO	) 		 Deferrable;
alter table 	SCS_DOCUMENTACIONDESIGNA	 add constraint 	FK_SCS_ACTDESIGNA	 foreign key (	IDACTUACION,IDINSTITUCION,IDTURNO,ANIO,NUMERO	)	 references 	SCS_ACTUACIONDESIGNA	 (	NUMEROASUNTO,IDINSTITUCION,IDTURNO,ANIO,NUMERO	) 		 Deferrable;
alter table 	SCS_DOCUMENTACIONDESIGNA	 add constraint 	FK_DOCDES_GENFICHERO	 foreign key (	IDINSTITUCION,IDFICHERO	)	 references 	GEN_FICHERO	 (	IDINSTITUCION,IDFICHERO	) 		 Deferrable;
alter table 	SCS_DOCUMENTACIONASI	 drop constraint 	FK_SCS_ASISTENCIA	 ; 
alter table 	SCS_DOCUMENTACIONASI	 drop constraint 	FK_SCS_ACTUACIONASISTENCIA	 ; 
alter table 	SCS_DOCUMENTACIONASI	 drop constraint 	FK_DOCASI_GENFICHERO	 ; 
alter table 	SCS_DOCUMENTACIONASI	 add constraint 	FK_SCS_ASISTENCIA	 foreign key (	NUMERO,IDINSTITUCION,ANIO	)	 references 	SCS_ASISTENCIA	 (	NUMERO,IDINSTITUCION,ANIO	) 		 Deferrable;
alter table 	SCS_DOCUMENTACIONASI	 add constraint 	FK_SCS_ACTUACIONASISTENCIA	 foreign key (	IDACTUACION,IDINSTITUCION,ANIO,NUMERO	)	 references 	SCS_ACTUACIONASISTENCIA	 (	IDACTUACION,IDINSTITUCION,ANIO,NUMERO	) 		 Deferrable;
alter table 	SCS_DOCUMENTACIONASI	 add constraint 	FK_DOCASI_GENFICHERO	 foreign key (	IDINSTITUCION,IDFICHERO	)	 references 	GEN_FICHERO	 (	IDINSTITUCION,IDFICHERO	) 		 Deferrable;
alter table 	SCS_DE_TIPOINMUEBLE	 drop constraint 	FK_SCS_DE_TIPOIN_SCS_DE_TIPOVI	 ; 
alter table 	SCS_DE_TIPOINMUEBLE	 add constraint 	FK_SCS_DE_TIPOIN_SCS_DE_TIPOVI	 foreign key (	IDTIPOVIVIENDA,IDINSTITUCION	)	 references 	SCS_DE_TIPOVIVIENDA	 (	IDTIPOVIVIENDA,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_DE_IRPF20	 drop constraint 	FK_SCS_DE_IRPF_SCS_UNIDADFAM	 ; 
alter table 	SCS_DE_IRPF20	 drop constraint 	FK_SCS_DE_IRPF_SCS_DE_TIPOREN	 ; 
alter table 	SCS_DE_IRPF20	 drop constraint 	FK_SCS_DE_IRPF_SCS_DE_EJERC	 ; 
alter table 	SCS_DE_IRPF20	 add constraint 	FK_SCS_DE_IRPF_SCS_UNIDADFAM	 foreign key (	IDPERSONA,ANIO,NUMERO,IDINSTITUCION,IDTIPOEJG	)	 references 	SCS_UNIDADFAMILIAREJG	 (	IDPERSONA,ANIO,NUMERO,IDINSTITUCION,IDTIPOEJG	) 		 Deferrable;
alter table 	SCS_DE_IRPF20	 add constraint 	FK_SCS_DE_IRPF_SCS_DE_TIPOREN	 foreign key (	IDTIPORENDIMIENTO,IDINSTITUCION	)	 references 	SCS_DE_TIPORENDIMIENTO	 (	IDTIPORENDIMIENTO,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_DE_IRPF20	 add constraint 	FK_SCS_DE_IRPF_SCS_DE_EJERC	 foreign key (	IDEJERCICIO,IDINSTITUCION	)	 references 	SCS_DE_EJERCICIO	 (	IDEJERCICIO,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_DE_INGRESOS	 drop constraint 	FK_SCS_DE_INGRES_SCS_UNIDADFAM	 ; 
alter table 	SCS_DE_INGRESOS	 drop constraint 	FK_SCS_DE_INGRES_SCS_DE_TIPOIN	 ; 
alter table 	SCS_DE_INGRESOS	 drop constraint 	FK_SCS_DE_INGRES_SCS_DE_PERIOD	 ; 
alter table 	SCS_DE_INGRESOS	 add constraint 	FK_SCS_DE_INGRES_SCS_UNIDADFAM	 foreign key (	IDPERSONA,ANIO,NUMERO,IDINSTITUCION,IDTIPOEJG	)	 references 	SCS_UNIDADFAMILIAREJG	 (	IDPERSONA,ANIO,NUMERO,IDINSTITUCION,IDTIPOEJG	) 		 Deferrable;
alter table 	SCS_DE_INGRESOS	 add constraint 	FK_SCS_DE_INGRES_SCS_DE_TIPOIN	 foreign key (	IDTIPOINGRESO,IDINSTITUCION	)	 references 	SCS_DE_TIPOINGRESO	 (	IDTIPOINGRESO,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_DE_INGRESOS	 add constraint 	FK_SCS_DE_INGRES_SCS_DE_PERIOD	 foreign key (	IDPERIODICIDAD,IDINSTITUCION	)	 references 	SCS_DE_PERIODICIDAD	 (	IDPERIODICIDAD,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_DE_CARGAECONOMICA	 drop constraint 	FK_SCS_DE_CARGAE_SCS_UNIDADFAM	 ; 
alter table 	SCS_DE_CARGAECONOMICA	 drop constraint 	FK_SCS_DE_CARGAE_SCS_DE_TIPOCA	 ; 
alter table 	SCS_DE_CARGAECONOMICA	 drop constraint 	FK_SCS_DE_CARGAE_SCS_DE_PERIOD	 ; 
alter table 	SCS_DE_CARGAECONOMICA	 add constraint 	FK_SCS_DE_CARGAE_SCS_UNIDADFAM	 foreign key (	IDPERSONA,ANIO,NUMERO,IDINSTITUCION,IDTIPOEJG	)	 references 	SCS_UNIDADFAMILIAREJG	 (	IDPERSONA,ANIO,NUMERO,IDINSTITUCION,IDTIPOEJG	) 		 Deferrable;
alter table 	SCS_DE_CARGAECONOMICA	 add constraint 	FK_SCS_DE_CARGAE_SCS_DE_TIPOCA	 foreign key (	IDTIPOCARGAECONOMICA,IDINSTITUCION	)	 references 	SCS_DE_TIPOCARGAECONOMICA	 (	IDTIPOCARGAECONOMICA,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_DE_CARGAECONOMICA	 add constraint 	FK_SCS_DE_CARGAE_SCS_DE_PERIOD	 foreign key (	IDPERIODICIDAD,IDINSTITUCION	)	 references 	SCS_DE_PERIODICIDAD	 (	IDPERIODICIDAD,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_DE_BIENMUEBLE	 drop constraint 	FK_SCS_DE_BIENMU_SCS_UNIDADFAM	 ; 
alter table 	SCS_DE_BIENMUEBLE	 drop constraint 	FK_SCS_DE_BIENMU_SCS_DE_TIPOMU	 ; 
alter table 	SCS_DE_BIENMUEBLE	 drop constraint 	FK_SCS_DE_BIENMU_SCS_DE_ORIGEN	 ; 
alter table 	SCS_DE_BIENMUEBLE	 add constraint 	FK_SCS_DE_BIENMU_SCS_UNIDADFAM	 foreign key (	IDPERSONA,ANIO,NUMERO,IDINSTITUCION,IDTIPOEJG	)	 references 	SCS_UNIDADFAMILIAREJG	 (	IDPERSONA,ANIO,NUMERO,IDINSTITUCION,IDTIPOEJG	) 		 Deferrable;
alter table 	SCS_DE_BIENMUEBLE	 add constraint 	FK_SCS_DE_BIENMU_SCS_DE_TIPOMU	 foreign key (	IDTIPOMUEBLE,IDINSTITUCION	)	 references 	SCS_DE_TIPOMUEBLE	 (	IDTIPOMUEBLE,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_DE_BIENMUEBLE	 add constraint 	FK_SCS_DE_BIENMU_SCS_DE_ORIGEN	 foreign key (	IDORIGENVALBM,IDINSTITUCION	)	 references 	SCS_DE_ORIGENVAL_BM	 (	IDORIGENVALBM,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_DE_BIENINMUEBLE	 drop constraint 	FK_SCS_DE_BIENIN_SCS_UNIDADFAM	 ; 
alter table 	SCS_DE_BIENINMUEBLE	 drop constraint 	FK_SCS_DE_BIENIN_SCS_DE_TIPOVI	 ; 
alter table 	SCS_DE_BIENINMUEBLE	 drop constraint 	FK_SCS_DE_BIENIN_SCS_DE_TIPOIN	 ; 
alter table 	SCS_DE_BIENINMUEBLE	 drop constraint 	FK_SCS_DE_BIENIN_SCS_DE_ORIGEN	 ; 
alter table 	SCS_DE_BIENINMUEBLE	 add constraint 	FK_SCS_DE_BIENIN_SCS_UNIDADFAM	 foreign key (	IDPERSONA,ANIO,NUMERO,IDINSTITUCION,IDTIPOEJG	)	 references 	SCS_UNIDADFAMILIAREJG	 (	IDPERSONA,ANIO,NUMERO,IDINSTITUCION,IDTIPOEJG	) 		 Deferrable;
alter table 	SCS_DE_BIENINMUEBLE	 add constraint 	FK_SCS_DE_BIENIN_SCS_DE_TIPOVI	 foreign key (	IDTIPOVIVIENDA,IDINSTITUCION	)	 references 	SCS_DE_TIPOVIVIENDA	 (	IDTIPOVIVIENDA,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_DE_BIENINMUEBLE	 add constraint 	FK_SCS_DE_BIENIN_SCS_DE_TIPOIN	 foreign key (	IDTIPOINMUEBLE,IDINSTITUCION	)	 references 	SCS_DE_TIPOINMUEBLE	 (	IDTIPOINMUEBLE,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_DE_BIENINMUEBLE	 add constraint 	FK_SCS_DE_BIENIN_SCS_DE_ORIGEN	 foreign key (	IDORIGENVALBI,IDINSTITUCION	)	 references 	SCS_DE_ORIGENVAL_BI	 (	IDORIGENVALBI,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_DESIGNASLETRADO	 drop constraint 	FK_SCS_LETRADODESIGNA_DESIGNA	 ; 
alter table 	SCS_DESIGNASLETRADO	 drop constraint 	FK_SCS_LETRADODESIGNA_COLEGIAD	 ; 
alter table 	SCS_DESIGNASLETRADO	 drop constraint 	FK_SCS_DESIGNALETRADO_MOTIVO	 ; 
alter table 	SCS_DESIGNASLETRADO	 add constraint 	FK_SCS_LETRADODESIGNA_DESIGNA	 foreign key (	NUMERO,IDINSTITUCION,IDTURNO,ANIO	)	 references 	SCS_DESIGNA	 (	NUMERO,IDINSTITUCION,IDTURNO,ANIO	) 		 Deferrable;
alter table 	SCS_DESIGNASLETRADO	 add constraint 	FK_SCS_LETRADODESIGNA_COLEGIAD	 foreign key (	IDPERSONA,IDINSTITUCION	)	 references 	CEN_CLIENTE	 (	IDPERSONA,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_DESIGNASLETRADO	 add constraint 	FK_SCS_DESIGNALETRADO_MOTIVO	 foreign key (	IDTIPOMOTIVO	)	 references 	SCS_TIPOMOTIVO	 (	IDTIPOMOTIVO	) 		 Deferrable;
alter table 	SCS_DESIGNAPROCURADOR	 drop constraint 	FK_SCS_DESIGNAPROCURADOR_PROCU	 ; 
alter table 	SCS_DESIGNAPROCURADOR	 drop constraint 	FK_SCS_DESIGNAPROCURADOR_MOTIV	 ; 
alter table 	SCS_DESIGNAPROCURADOR	 drop constraint 	FK_SCS_DESIGNAPROCURADOR_DESIG	 ; 
alter table 	SCS_DESIGNAPROCURADOR	 add constraint 	FK_SCS_DESIGNAPROCURADOR_PROCU	 foreign key (	IDPROCURADOR,IDINSTITUCION_PROC	)	 references 	SCS_PROCURADOR	 (	IDPROCURADOR,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_DESIGNAPROCURADOR	 add constraint 	FK_SCS_DESIGNAPROCURADOR_MOTIV	 foreign key (	IDTIPOMOTIVO	)	 references 	SCS_TIPOMOTIVO	 (	IDTIPOMOTIVO	) 		 Deferrable;
alter table 	SCS_DESIGNAPROCURADOR	 add constraint 	FK_SCS_DESIGNAPROCURADOR_DESIG	 foreign key (	ANIO,IDINSTITUCION,IDTURNO,NUMERO	)	 references 	SCS_DESIGNA	 (	ANIO,IDINSTITUCION,IDTURNO,NUMERO	) 	 on delete cascade	 Deferrable;
alter table 	SCS_DESIGNA	 drop constraint 	FK_SCS_DESIGNA_PROCURADOR	 ; 
alter table 	SCS_DESIGNA	 drop constraint 	FK_SCS_DESIGNA_PRET	 ; 
alter table 	SCS_DESIGNA	 drop constraint 	FK_SCS_DESIGNA_JUZGADO	 ; 
alter table 	SCS_DESIGNA	 drop constraint 	FK_DESIGNA_TURNO	 ; 
alter table 	SCS_DESIGNA	 drop constraint 	FK_DESIGNA_TIPODESIGNACOLEGIO	 ; 
alter table 	SCS_DESIGNA	 drop constraint 	FK_DESIGNA_PROCEDIMIENTO	 ; 
alter table 	SCS_DESIGNA	 add constraint 	FK_SCS_DESIGNA_PROCURADOR	 foreign key (	IDINSTITUCION_PROCUR,IDPROCURADOR	)	 references 	SCS_PROCURADOR	 (	IDINSTITUCION,IDPROCURADOR	) 		 Deferrable;
alter table 	SCS_DESIGNA	 add constraint 	FK_SCS_DESIGNA_PRET	 foreign key (	IDPRETENSION,IDINSTITUCION	)	 references 	SCS_PRETENSION	 (	IDPRETENSION,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_DESIGNA	 add constraint 	FK_SCS_DESIGNA_JUZGADO	 foreign key (	IDINSTITUCION_JUZG,IDJUZGADO	)	 references 	SCS_JUZGADO	 (	IDINSTITUCION,IDJUZGADO	) 		 Deferrable;
alter table 	SCS_DESIGNA	 add constraint 	FK_DESIGNA_TURNO	 foreign key (	IDTURNO,IDINSTITUCION	)	 references 	SCS_TURNO	 (	IDTURNO,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_DESIGNA	 add constraint 	FK_DESIGNA_TIPODESIGNACOLEGIO	 foreign key (	IDTIPODESIGNACOLEGIO,IDINSTITUCION	)	 references 	SCS_TIPODESIGNACOLEGIO	 (	IDTIPODESIGNACOLEGIO,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_DESIGNA	 add constraint 	FK_DESIGNA_PROCEDIMIENTO	 foreign key (	IDPROCEDIMIENTO,IDINSTITUCION	)	 references 	SCS_PROCEDIMIENTOS	 (	IDPROCEDIMIENTO,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_DELITOSEJG	 drop constraint 	FK_SCS_DELITOSEJG_EJG	 ; 
alter table 	SCS_DELITOSEJG	 drop constraint 	FK_SCS_DELITOSEJG_DELITOS	 ; 
alter table 	SCS_DELITOSEJG	 add constraint 	FK_SCS_DELITOSEJG_EJG	 foreign key (	IDTIPOEJG,IDINSTITUCION,NUMERO,ANIO	)	 references 	SCS_EJG	 (	IDTIPOEJG,IDINSTITUCION,NUMERO,ANIO	) 	 on delete cascade	 Deferrable;
alter table 	SCS_DELITOSEJG	 add constraint 	FK_SCS_DELITOSEJG_DELITOS	 foreign key (	IDDELITO,IDINSTITUCION	)	 references 	SCS_DELITO	 (	IDDELITO,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_DELITOSDESIGNA	 drop constraint 	FK_SCS_DELITOSDESIGNA_DESIGNA	 ; 
alter table 	SCS_DELITOSDESIGNA	 drop constraint 	FK_DELITOSDESIGNA_DELITO	 ; 
alter table 	SCS_DELITOSDESIGNA	 add constraint 	FK_SCS_DELITOSDESIGNA_DESIGNA	 foreign key (	ANIO,IDINSTITUCION,NUMERO,IDTURNO	)	 references 	SCS_DESIGNA	 (	ANIO,IDINSTITUCION,NUMERO,IDTURNO	) 	 on delete cascade	 Deferrable;
alter table 	SCS_DELITOSDESIGNA	 add constraint 	FK_DELITOSDESIGNA_DELITO	 foreign key (	IDDELITO,IDINSTITUCION	)	 references 	SCS_DELITO	 (	IDDELITO,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_DELITOSASISTENCIA	 drop constraint 	FK_SCS_DELITOSASISTENCIA_DELIT	 ; 
alter table 	SCS_DELITOSASISTENCIA	 drop constraint 	FK_SCS_DELITOSASISTENCIA_ASIST	 ; 
alter table 	SCS_DELITOSASISTENCIA	 add constraint 	FK_SCS_DELITOSASISTENCIA_DELIT	 foreign key (	IDDELITO,IDINSTITUCION	)	 references 	SCS_DELITO	 (	IDDELITO,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_DELITOSASISTENCIA	 add constraint 	FK_SCS_DELITOSASISTENCIA_ASIST	 foreign key (	ANIO,IDINSTITUCION,NUMERO	)	 references 	SCS_ASISTENCIA	 (	ANIO,IDINSTITUCION,NUMERO	) 	 on delete cascade	 Deferrable;
alter table 	SCS_DELITO	 drop constraint 	FK_SCS_DELITO_INSTITUCION	 ; 
alter table 	SCS_DELITO	 add constraint 	FK_SCS_DELITO_INSTITUCION	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_DEFENDIDOSDESIGNA	 drop constraint 	FK_SCS_DEFENDIDOSTUROFICIO_PER	 ; 
alter table 	SCS_DEFENDIDOSDESIGNA	 drop constraint 	FK_SCS_DEFENDIDOSTUROFICIO_DES	 ; 
alter table 	SCS_DEFENDIDOSDESIGNA	 drop constraint 	FK_SCS_DEFENDESIG_TCALIDAD	 ; 
alter table 	SCS_DEFENDIDOSDESIGNA	 add constraint 	FK_SCS_DEFENDIDOSTUROFICIO_PER	 foreign key (	IDPERSONA,IDINSTITUCION	)	 references 	SCS_PERSONAJG	 (	IDPERSONA,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_DEFENDIDOSDESIGNA	 add constraint 	FK_SCS_DEFENDIDOSTUROFICIO_DES	 foreign key (	NUMERO,IDINSTITUCION,IDTURNO,ANIO	)	 references 	SCS_DESIGNA	 (	NUMERO,IDINSTITUCION,IDTURNO,ANIO	) 		 Deferrable;
alter table 	SCS_DEFENDIDOSDESIGNA	 add constraint 	FK_SCS_DEFENDESIG_TCALIDAD	 foreign key (	CALIDADIDINSTITUCION,IDTIPOENCALIDAD	)	 references 	SCS_TIPOENCALIDAD	 (	IDINSTITUCION,IDTIPOENCALIDAD	) 		 Deferrable;
alter table 	SCS_CONTRARIOSEJG	 drop constraint 	FK_PROCURADOR_CONTR	 ; 
alter table 	SCS_CONTRARIOSEJG	 drop constraint 	FK_9CE_9PE	 ; 
alter table 	SCS_CONTRARIOSEJG	 drop constraint 	FK_9CE_9EJ	 ; 
alter table 	SCS_CONTRARIOSEJG	 add constraint 	FK_PROCURADOR_CONTR	 foreign key (	IDPROCURADOR,IDINSTITUCION_PROCU	)	 references 	SCS_PROCURADOR	 (	IDPROCURADOR,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_CONTRARIOSEJG	 add constraint 	FK_9CE_9PE	 foreign key (	IDPERSONA,IDINSTITUCION	)	 references 	SCS_PERSONAJG	 (	IDPERSONA,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_CONTRARIOSEJG	 add constraint 	FK_9CE_9EJ	 foreign key (	NUMERO,IDINSTITUCION,IDTIPOEJG,ANIO	)	 references 	SCS_EJG	 (	NUMERO,IDINSTITUCION,IDTIPOEJG,ANIO	) 	 on delete cascade	 Deferrable;
alter table 	SCS_CONTRARIOSDESIGNA	 drop constraint 	FK_SCS_CONTRARIOSTUROFICIO_PER	 ; 
alter table 	SCS_CONTRARIOSDESIGNA	 drop constraint 	FK_SCS_CONTRARIOSTUROFICIO_DES	 ; 
alter table 	SCS_CONTRARIOSDESIGNA	 drop constraint 	FK_SCS_CONTRARIOSDESIGNA_REP	 ; 
alter table 	SCS_CONTRARIOSDESIGNA	 drop constraint 	FK_SCS_CONTRARIOSDESIGNA	 ; 
alter table 	SCS_CONTRARIOSDESIGNA	 add constraint 	FK_SCS_CONTRARIOSTUROFICIO_PER	 foreign key (	IDPERSONA,IDINSTITUCION	)	 references 	SCS_PERSONAJG	 (	IDPERSONA,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_CONTRARIOSDESIGNA	 add constraint 	FK_SCS_CONTRARIOSTUROFICIO_DES	 foreign key (	NUMERO,IDINSTITUCION,IDTURNO,ANIO	)	 references 	SCS_DESIGNA	 (	NUMERO,IDINSTITUCION,IDTURNO,ANIO	) 		 Deferrable;
alter table 	SCS_CONTRARIOSDESIGNA	 add constraint 	FK_SCS_CONTRARIOSDESIGNA_REP	 foreign key (	IDREPRESENTANTELEGAL,IDINSTITUCION	)	 references 	SCS_PERSONAJG	 (	IDPERSONA,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_CONTRARIOSDESIGNA	 add constraint 	FK_SCS_CONTRARIOSDESIGNA	 foreign key (	IDPROCURADOR,IDINSTITUCION_PROCU	)	 references 	SCS_PROCURADOR	 (	IDPROCURADOR,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_CONTRARIOSASISTENCIA	 drop constraint 	FK_SCSCONTRSASIST_SCSPERSOJG	 ; 
alter table 	SCS_CONTRARIOSASISTENCIA	 drop constraint 	FK_SCSCONTRSASIST_SCSASIST	 ; 
alter table 	SCS_CONTRARIOSASISTENCIA	 add constraint 	FK_SCSCONTRSASIST_SCSPERSOJG	 foreign key (	IDPERSONA,IDINSTITUCION	)	 references 	SCS_PERSONAJG	 (	IDPERSONA,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_CONTRARIOSASISTENCIA	 add constraint 	FK_SCSCONTRSASIST_SCSASIST	 foreign key (	ANIO,IDINSTITUCION,NUMERO	)	 references 	SCS_ASISTENCIA	 (	ANIO,IDINSTITUCION,NUMERO	) 		 Deferrable;
alter table 	SCS_CONF_CONJUNTO_GUARDIAS	 drop constraint 	FK_CONJUNTOGUARDIAS	 ; 
alter table 	SCS_CONF_CONJUNTO_GUARDIAS	 drop constraint 	FK2_SCS_CONF_CONJUNTO_GUARDIAS	 ; 
alter table 	SCS_CONF_CONJUNTO_GUARDIAS	 drop constraint 	FK1_SCS_CONF_CONJUNTO_GUARDIAS	 ; 
alter table 	SCS_CONF_CONJUNTO_GUARDIAS	 add constraint 	FK_CONJUNTOGUARDIAS	 foreign key (	IDINSTITUCION,IDCONJUNTOGUARDIA	)	 references 	SCS_CONJUNTOGUARDIAS	 (	IDINSTITUCION,IDCONJUNTOGUARDIA	) 		 Deferrable;
alter table 	SCS_CONF_CONJUNTO_GUARDIAS	 add constraint 	FK2_SCS_CONF_CONJUNTO_GUARDIAS	 foreign key (	IDGUARDIA,IDINSTITUCION,IDTURNO	)	 references 	SCS_GUARDIASTURNO	 (	IDGUARDIA,IDINSTITUCION,IDTURNO	) 		 Deferrable;
alter table 	SCS_CONF_CONJUNTO_GUARDIAS	 add constraint 	FK1_SCS_CONF_CONJUNTO_GUARDIAS	 foreign key (	IDTURNO,IDINSTITUCION	)	 references 	SCS_TURNO	 (	IDTURNO,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_COMUNICACIONES	 drop constraint 	FK_COMUNICACION_ENVSALIDA	 ; 
alter table 	SCS_COMUNICACIONES	 drop constraint 	FK_COMUNICACION_ENVENTRADA	 ; 
alter table 	SCS_COMUNICACIONES	 drop constraint 	FK_COMUNICACION_EJG	 ; 
alter table 	SCS_COMUNICACIONES	 drop constraint 	FK_COMUNICACION_DESIGNA	 ; 
alter table 	SCS_COMUNICACIONES	 add constraint 	FK_COMUNICACION_ENVSALIDA	 foreign key (	IDENVIOSALIDA,IDINSTITUCION	)	 references 	ENV_ENVIOS	 (	IDENVIO,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_COMUNICACIONES	 add constraint 	FK_COMUNICACION_ENVENTRADA	 foreign key (	IDENVIOENTRADA,IDINSTITUCION	)	 references 	ENV_ENTRADA_ENVIOS	 (	IDENVIO,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_COMUNICACIONES	 add constraint 	FK_COMUNICACION_EJG	 foreign key (	EJGIDTIPO,IDINSTITUCION,EJGANIO,EJGNUMERO	)	 references 	SCS_EJG	 (	IDTIPOEJG,IDINSTITUCION,ANIO,NUMERO	) 		 Deferrable;
alter table 	SCS_COMUNICACIONES	 add constraint 	FK_COMUNICACION_DESIGNA	 foreign key (	DESIGNAIDTURNO,IDINSTITUCION,DESIGNAANIO,DESIGNANUMERO	)	 references 	SCS_DESIGNA	 (	IDTURNO,IDINSTITUCION,ANIO,NUMERO	) 		 Deferrable;
alter table 	SCS_COMISARIA	 drop constraint 	FK_SCS_COMISARIA_PROVINCIA	 ; 
alter table 	SCS_COMISARIA	 drop constraint 	FK_SCS_COMISARIA_POBLACION	 ; 
alter table 	SCS_COMISARIA	 drop constraint 	FK_SCS_COMISARIA_INSTITUCION	 ; 
alter table 	SCS_COMISARIA	 add constraint 	FK_SCS_COMISARIA_PROVINCIA	 foreign key (	IDPROVINCIA	)	 references 	CEN_PROVINCIAS	 (	IDPROVINCIA	) 		 Deferrable;
alter table 	SCS_COMISARIA	 add constraint 	FK_SCS_COMISARIA_POBLACION	 foreign key (	IDPOBLACION	)	 references 	CEN_POBLACIONES	 (	IDPOBLACION	) 		 Deferrable;
alter table 	SCS_COMISARIA	 add constraint 	FK_SCS_COMISARIA_INSTITUCION	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_CLASIFICACIONEJGCOLEGIO	 drop constraint 	FK_INSTITUCIONCLASI	 ; 
alter table 	SCS_CLASIFICACIONEJGCOLEGIO	 add constraint 	FK_INSTITUCIONCLASI	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_CALENDARIOLABORAL	 drop constraint 	FK_9CL_9PJ	 ; 
alter table 	SCS_CALENDARIOLABORAL	 drop constraint 	FK_9CL_1IN	 ; 
alter table 	SCS_CALENDARIOLABORAL	 add constraint 	FK_9CL_9PJ	 foreign key (	IDPARTIDO	)	 references 	CEN_PARTIDOJUDICIAL	 (	IDPARTIDO	) 		 Deferrable;
alter table 	SCS_CALENDARIOLABORAL	 add constraint 	FK_9CL_1IN	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_CALENDARIOGUARDIAS	 drop constraint 	FK_SCS_CALENDARIOGUARDIAS_GUAR	 ; 
alter table 	SCS_CALENDARIOGUARDIAS	 drop constraint 	FK_CALENDARIO_COLEGIADO_ULTIMO	 ; 
alter table 	SCS_CALENDARIOGUARDIAS	 drop constraint 	FK_CALENDARIOGUARDIAS_VP	 ; 
alter table 	SCS_CALENDARIOGUARDIAS	 add constraint 	FK_SCS_CALENDARIOGUARDIAS_GUAR	 foreign key (	IDGUARDIA,IDINSTITUCION,IDTURNO	)	 references 	SCS_GUARDIASTURNO	 (	IDGUARDIA,IDINSTITUCION,IDTURNO	) 		 Deferrable;
alter table 	SCS_CALENDARIOGUARDIAS	 add constraint 	FK_CALENDARIO_COLEGIADO_ULTIMO	 foreign key (	FECHASUSC_ULTIMOANTERIOR,IDINSTITUCION,IDTURNO,IDGUARDIA,IDPERSONA_ULTIMOANTERIOR	)	 references 	SCS_INSCRIPCIONGUARDIA	 (	FECHASUSCRIPCION,IDINSTITUCION,IDTURNO,IDGUARDIA,IDPERSONA	) 		 Deferrable;
alter table 	SCS_CALENDARIOGUARDIAS	 add constraint 	FK_CALENDARIOGUARDIAS_VP	 foreign key (	IDCALENDARIOGUARDIASPRINCIPAL,IDINSTITUCION,IDTURNOPRINCIPAL,IDGUARDIAPRINCIPAL	)	 references 	SCS_CALENDARIOGUARDIAS	 (	IDCALENDARIOGUARDIAS,IDINSTITUCION,IDTURNO,IDGUARDIA	) 		 Deferrable;
alter table 	SCS_CABECERAGUARDIAS	 drop constraint 	FK_SCS_CABGUARD_SCS_CALGUARD	 ; 
alter table 	SCS_CABECERAGUARDIAS	 drop constraint 	FK_SCS_CABGUARD_CEN_COLEG	 ; 
alter table 	SCS_CABECERAGUARDIAS	 add constraint 	FK_SCS_CABGUARD_SCS_CALGUARD	 foreign key (	IDCALENDARIOGUARDIAS,IDINSTITUCION,IDTURNO,IDGUARDIA	)	 references 	SCS_CALENDARIOGUARDIAS	 (	IDCALENDARIOGUARDIAS,IDINSTITUCION,IDTURNO,IDGUARDIA	) 		 Deferrable;
alter table 	SCS_CABECERAGUARDIAS	 add constraint 	FK_SCS_CABGUARD_CEN_COLEG	 foreign key (	IDPERSONA,IDINSTITUCION	)	 references 	CEN_COLEGIADO	 (	IDPERSONA,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_BENEFICIARIOSOJ	 drop constraint 	FK_SCS_BENEFICIARIO_TIPOCONOCE	 ; 
alter table 	SCS_BENEFICIARIOSOJ	 drop constraint 	FK_SCS_BENEFICIARIO_TIPGRUPLAB	 ; 
alter table 	SCS_BENEFICIARIOSOJ	 drop constraint 	FK_SCS_BENEFICIARIO_PERSONAJG	 ; 
alter table 	SCS_BENEFICIARIOSOJ	 add constraint 	FK_SCS_BENEFICIARIO_TIPOCONOCE	 foreign key (	IDTIPOCONOCE,IDINSTITUCION	)	 references 	SCS_TIPOCONOCE	 (	IDTIPOCONOCE,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_BENEFICIARIOSOJ	 add constraint 	FK_SCS_BENEFICIARIO_TIPGRUPLAB	 foreign key (	IDTIPOGRUPOLAB,IDINSTITUCION	)	 references 	SCS_TIPOGRUPOLABORAL	 (	IDTIPOGRUPOLAB,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_BENEFICIARIOSOJ	 add constraint 	FK_SCS_BENEFICIARIO_PERSONAJG	 foreign key (	IDPERSONA,IDINSTITUCION	)	 references 	SCS_PERSONAJG	 (	IDPERSONA,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_ASISTENCIA	 drop constraint 	FK_SOL_CV	 ; 
alter table 	SCS_ASISTENCIA	 drop constraint 	FK_SCS_ASISTENCIA_TIPOASISTENC	 ; 
alter table 	SCS_ASISTENCIA	 drop constraint 	FK_SCS_ASISTENCIA_TIPOASISCOLE	 ; 
alter table 	SCS_ASISTENCIA	 drop constraint 	FK_SCS_ASISTENCIA_FACTURACIONJ	 ; 
alter table 	SCS_ASISTENCIA	 drop constraint 	FK_ASISTENCIA_PERSONAJG	 ; 
alter table 	SCS_ASISTENCIA	 drop constraint 	FK_ASISTENCIA_JUZGADO	 ; 
alter table 	SCS_ASISTENCIA	 drop constraint 	FK_ASISTENCIA_GUARDIASTURNO	 ; 
alter table 	SCS_ASISTENCIA	 drop constraint 	FK_ASISTENCIA_ESTADOASISTENCIA	 ; 
alter table 	SCS_ASISTENCIA	 drop constraint 	FK_ASISTENCIA_EJG	 ; 
alter table 	SCS_ASISTENCIA	 drop constraint 	FK_ASISTENCIA_DESIGNA	 ; 
alter table 	SCS_ASISTENCIA	 drop constraint 	FK_ASISTENCIA_COMISARIA	 ; 
alter table 	SCS_ASISTENCIA	 drop constraint 	FK_9AA_1CO	 ; 
alter table 	SCS_ASISTENCIA	 add constraint 	FK_SOL_CV	 foreign key (	IDSOLICITUDCENTRALITA	)	 references 	SCS_SOLICITUD_ACEPTADA	 (	IDSOLICITUD	) 		 Deferrable;
alter table 	SCS_ASISTENCIA	 add constraint 	FK_SCS_ASISTENCIA_TIPOASISTENC	 foreign key (	IDTIPOASISTENCIA	)	 references 	SCS_TIPOASISTENCIA	 (	IDTIPOASISTENCIA	) 		 Deferrable;
alter table 	SCS_ASISTENCIA	 add constraint 	FK_SCS_ASISTENCIA_TIPOASISCOLE	 foreign key (	IDTIPOASISTENCIACOLEGIO,IDINSTITUCION	)	 references 	SCS_TIPOASISTENCIACOLEGIO	 (	IDTIPOASISTENCIACOLEGIO,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_ASISTENCIA	 add constraint 	FK_SCS_ASISTENCIA_FACTURACIONJ	 foreign key (	IDFACTURACION,IDINSTITUCION	)	 references 	FCS_FACTURACIONJG	 (	IDFACTURACION,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_ASISTENCIA	 add constraint 	FK_ASISTENCIA_PERSONAJG	 foreign key (	IDPERSONAJG,IDINSTITUCION	)	 references 	SCS_PERSONAJG	 (	IDPERSONA,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_ASISTENCIA	 add constraint 	FK_ASISTENCIA_JUZGADO	 foreign key (	JUZGADOIDINSTITUCION,JUZGADO	)	 references 	SCS_JUZGADO	 (	IDINSTITUCION,IDJUZGADO	) 		 Deferrable;
alter table 	SCS_ASISTENCIA	 add constraint 	FK_ASISTENCIA_GUARDIASTURNO	 foreign key (	IDGUARDIA,IDINSTITUCION,IDTURNO	)	 references 	SCS_GUARDIASTURNO	 (	IDGUARDIA,IDINSTITUCION,IDTURNO	) 		 Deferrable;
alter table 	SCS_ASISTENCIA	 add constraint 	FK_ASISTENCIA_ESTADOASISTENCIA	 foreign key (	IDESTADOASISTENCIA	)	 references 	SCS_ESTADOASISTENCIA	 (	IDESTADOASISTENCIA	) 		 Deferrable;
alter table 	SCS_ASISTENCIA	 add constraint 	FK_ASISTENCIA_EJG	 foreign key (	EJGNUMERO,IDINSTITUCION,EJGIDTIPOEJG,EJGANIO	)	 references 	SCS_EJG	 (	NUMERO,IDINSTITUCION,IDTIPOEJG,ANIO	) 		 Deferrable;
alter table 	SCS_ASISTENCIA	 add constraint 	FK_ASISTENCIA_DESIGNA	 foreign key (	DESIGNA_TURNO,IDINSTITUCION,DESIGNA_ANIO,DESIGNA_NUMERO	)	 references 	SCS_DESIGNA	 (	IDTURNO,IDINSTITUCION,ANIO,NUMERO	) 		 Deferrable;
alter table 	SCS_ASISTENCIA	 add constraint 	FK_ASISTENCIA_COMISARIA	 foreign key (	COMISARIAIDINSTITUCION,COMISARIA	)	 references 	SCS_COMISARIA	 (	IDINSTITUCION,IDCOMISARIA	) 		 Deferrable;
alter table 	SCS_ASISTENCIA	 add constraint 	FK_9AA_1CO	 foreign key (	IDPERSONACOLEGIADO,IDINSTITUCION	)	 references 	CEN_COLEGIADO	 (	IDPERSONA,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_AREA	 drop constraint 	FK_9AR_1IN	 ; 
alter table 	SCS_AREA	 add constraint 	FK_9AR_1IN	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_ACTUACIONDESIGNAPROCURADOR	 drop constraint 	FK_SCS_ACTUACION_SCS_DESIGNAPR	 ; 
alter table 	SCS_ACTUACIONDESIGNAPROCURADOR	 drop constraint 	FK_SCS_ACTUACION_SCS_ACREDIT_P	 ; 
alter table 	SCS_ACTUACIONDESIGNAPROCURADOR	 drop constraint 	FK_SCS_ACTUACIONDE_SCS_JUZGADO	 ; 
alter table 	SCS_ACTUACIONDESIGNAPROCURADOR	 add constraint 	FK_SCS_ACTUACION_SCS_DESIGNAPR	 foreign key (	FECHADESIGNA,IDINSTITUCION,IDTURNO,NUMERO,ANIO,IDINSTITUCION_PROC,IDPROCURADOR	)	 references 	SCS_DESIGNAPROCURADOR	 (	FECHADESIGNA,IDINSTITUCION,IDTURNO,NUMERO,ANIO,IDINSTITUCION_PROC,IDPROCURADOR	) 		 Deferrable;
alter table 	SCS_ACTUACIONDESIGNAPROCURADOR	 add constraint 	FK_SCS_ACTUACION_SCS_ACREDIT_P	 foreign key (	IDACREDITACION,IDINSTITUCION,IDPROCEDIMIENTOPROCURADOR	)	 references 	SCS_ACREDIT_PROCPROCURADOR	 (	IDACREDITACION,IDINSTITUCION,IDPROCEDIMIENTOPROCURADOR	) 		 Deferrable;
alter table 	SCS_ACTUACIONDESIGNAPROCURADOR	 add constraint 	FK_SCS_ACTUACIONDE_SCS_JUZGADO	 foreign key (	IDJUZGADO,IDINSTITUCION	)	 references 	SCS_JUZGADO	 (	IDJUZGADO,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_ACTUACIONDESIGNA	 drop constraint 	FK_SCS_ACTUACIONTURNO_DESIGNA	 ; 
alter table 	SCS_ACTUACIONDESIGNA	 drop constraint 	FK_SCS_ACTUACIONDESIGNA_PRISIO	 ; 
alter table 	SCS_ACTUACIONDESIGNA	 drop constraint 	FK_SCS_ACTUACIONDESIGNA_PRET	 ; 
alter table 	SCS_ACTUACIONDESIGNA	 drop constraint 	FK_SCS_ACTUACIONDESIGNA_JUZGAD	 ; 
alter table 	SCS_ACTUACIONDESIGNA	 drop constraint 	FK_SCS_ACTUACIONDESIGNA_FACT	 ; 
alter table 	SCS_ACTUACIONDESIGNA	 drop constraint 	FK_SCS_ACTUACIONDESIGNA_COMISA	 ; 
alter table 	SCS_ACTUACIONDESIGNA	 drop constraint 	FK_SCS_ACTUACIONDESIGNA_COLEGI	 ; 
alter table 	SCS_ACTUACIONDESIGNA	 drop constraint 	FK_SCS_ACTUACIONDESIGNA_ACREDI	 ; 
alter table 	SCS_ACTUACIONDESIGNA	 drop constraint 	FK_SCS_ACTDES_PROC_AC	 ; 
alter table 	SCS_ACTUACIONDESIGNA	 drop constraint 	FK_SCS_ACTDESIG_MOTCAMBIO	 ; 
alter table 	SCS_ACTUACIONDESIGNA	 drop constraint 	FK_ACTUACIONDESIGNA_PROCEDIMIE	 ; 
alter table 	SCS_ACTUACIONDESIGNA	 add constraint 	FK_SCS_ACTUACIONTURNO_DESIGNA	 foreign key (	NUMERO,IDINSTITUCION,IDTURNO,ANIO	)	 references 	SCS_DESIGNA	 (	NUMERO,IDINSTITUCION,IDTURNO,ANIO	) 		 Deferrable;
alter table 	SCS_ACTUACIONDESIGNA	 add constraint 	FK_SCS_ACTUACIONDESIGNA_PRISIO	 foreign key (	IDPRISION,IDINSTITUCION_PRIS	)	 references 	SCS_PRISION	 (	IDPRISION,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_ACTUACIONDESIGNA	 add constraint 	FK_SCS_ACTUACIONDESIGNA_PRET	 foreign key (	IDPRETENSION,IDINSTITUCION	)	 references 	SCS_PRETENSION	 (	IDPRETENSION,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_ACTUACIONDESIGNA	 add constraint 	FK_SCS_ACTUACIONDESIGNA_JUZGAD	 foreign key (	IDJUZGADO,IDINSTITUCION_JUZG	)	 references 	SCS_JUZGADO	 (	IDJUZGADO,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_ACTUACIONDESIGNA	 add constraint 	FK_SCS_ACTUACIONDESIGNA_FACT	 foreign key (	IDFACTURACION,IDINSTITUCION	)	 references 	FCS_FACTURACIONJG	 (	IDFACTURACION,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_ACTUACIONDESIGNA	 add constraint 	FK_SCS_ACTUACIONDESIGNA_COMISA	 foreign key (	IDCOMISARIA,IDINSTITUCION_COMIS	)	 references 	SCS_COMISARIA	 (	IDCOMISARIA,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_ACTUACIONDESIGNA	 add constraint 	FK_SCS_ACTUACIONDESIGNA_COLEGI	 foreign key (	IDPERSONACOLEGIADO,IDINSTITUCION	)	 references 	CEN_COLEGIADO	 (	IDPERSONA,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_ACTUACIONDESIGNA	 add constraint 	FK_SCS_ACTUACIONDESIGNA_ACREDI	 foreign key (	IDACREDITACION	)	 references 	SCS_ACREDITACION	 (	IDACREDITACION	) 		 Deferrable;
alter table 	SCS_ACTUACIONDESIGNA	 add constraint 	FK_SCS_ACTDES_PROC_AC	 foreign key (	IDINSTITUCION_PROC,IDPROCEDIMIENTO,IDACREDITACION	)	 references 	SCS_ACREDITACIONPROCEDIMIENTO	 (	IDINSTITUCION,IDPROCEDIMIENTO,IDACREDITACION	) 		 Deferrable;
alter table 	SCS_ACTUACIONDESIGNA	 add constraint 	FK_SCS_ACTDESIG_MOTCAMBIO	 foreign key (	ID_MOTIVO_CAMBIO,IDINSTITUCION	)	 references 	SCS_ACTUADESIG_MOTCAMBIO	 (	IDACTDESMOTCAMBIO,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_ACTUACIONDESIGNA	 add constraint 	FK_ACTUACIONDESIGNA_PROCEDIMIE	 foreign key (	IDINSTITUCION_PROC,IDPROCEDIMIENTO	)	 references 	SCS_PROCEDIMIENTOS	 (	IDINSTITUCION,IDPROCEDIMIENTO	) 		 Deferrable;
alter table 	SCS_ACTUACIONASISTENCIA	 drop constraint 	FK_SCS_ACTUACION_ASISTENCIA	 ; 
alter table 	SCS_ACTUACIONASISTENCIA	 drop constraint 	FK_SCS_ACTUACIONASISTENCIA_PRI	 ; 
alter table 	SCS_ACTUACIONASISTENCIA	 drop constraint 	FK_SCS_ACTUACIONASISTENCIA_JUZ	 ; 
alter table 	SCS_ACTUACIONASISTENCIA	 drop constraint 	FK_SCS_ACTUACIONASISTENCIA_FAC	 ; 
alter table 	SCS_ACTUACIONASISTENCIA	 drop constraint 	FK_SCS_ACTUACIONASISTENCIA_COM	 ; 
alter table 	SCS_ACTUACIONASISTENCIA	 drop constraint 	FK_SCS_ACTASISTENCIA_TIPOACT	 ; 
alter table 	SCS_ACTUACIONASISTENCIA	 add constraint 	FK_SCS_ACTUACION_ASISTENCIA	 foreign key (	NUMERO,IDINSTITUCION,ANIO	)	 references 	SCS_ASISTENCIA	 (	NUMERO,IDINSTITUCION,ANIO	) 		 Deferrable;
alter table 	SCS_ACTUACIONASISTENCIA	 add constraint 	FK_SCS_ACTUACIONASISTENCIA_PRI	 foreign key (	IDPRISION,IDINSTITUCION_PRIS	)	 references 	SCS_PRISION	 (	IDPRISION,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_ACTUACIONASISTENCIA	 add constraint 	FK_SCS_ACTUACIONASISTENCIA_JUZ	 foreign key (	IDJUZGADO,IDINSTITUCION_JUZG	)	 references 	SCS_JUZGADO	 (	IDJUZGADO,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_ACTUACIONASISTENCIA	 add constraint 	FK_SCS_ACTUACIONASISTENCIA_FAC	 foreign key (	IDFACTURACION,IDINSTITUCION	)	 references 	FCS_FACTURACIONJG	 (	IDFACTURACION,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_ACTUACIONASISTENCIA	 add constraint 	FK_SCS_ACTUACIONASISTENCIA_COM	 foreign key (	IDCOMISARIA,IDINSTITUCION_COMIS	)	 references 	SCS_COMISARIA	 (	IDCOMISARIA,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_ACTUACIONASISTENCIA	 add constraint 	FK_SCS_ACTASISTENCIA_TIPOACT	 foreign key (	IDTIPOACTUACION,IDINSTITUCION,IDTIPOASISTENCIA	)	 references 	SCS_TIPOACTUACION	 (	IDTIPOACTUACION,IDINSTITUCION,IDTIPOASISTENCIA	) 		 Deferrable;
alter table 	SCS_ACTUACIONASISTCOSTEFIJO	 drop constraint 	FK_ACTASISTCOSTE_TIPOACTCOSTE	 ; 
alter table 	SCS_ACTUACIONASISTCOSTEFIJO	 drop constraint 	FK_ACTASISTCOSTE_ACTASIST	 ; 
alter table 	SCS_ACTUACIONASISTCOSTEFIJO	 add constraint 	FK_ACTASISTCOSTE_TIPOACTCOSTE	 foreign key (	IDCOSTEFIJO,IDINSTITUCION,IDTIPOASISTENCIA,IDTIPOACTUACION	)	 references 	SCS_TIPOACTUACIONCOSTEFIJO	 (	IDCOSTEFIJO,IDINSTITUCION,IDTIPOASISTENCIA,IDTIPOACTUACION	) 		 Deferrable;
alter table 	SCS_ACTUACIONASISTCOSTEFIJO	 add constraint 	FK_ACTASISTCOSTE_ACTASIST	 foreign key (	IDACTUACION,IDINSTITUCION,ANIO,NUMERO	)	 references 	SCS_ACTUACIONASISTENCIA	 (	IDACTUACION,IDINSTITUCION,ANIO,NUMERO	) 		 Deferrable;
alter table 	SCS_ACREDIT_PROCPROCURADOR	 drop constraint 	FK_SCS_ACREDITAC_SCS_PROCEDIMI	 ; 
alter table 	SCS_ACREDIT_PROCPROCURADOR	 drop constraint 	FK_SCS_ACREDITAC_SCS_ACREDITAC	 ; 
alter table 	SCS_ACREDIT_PROCPROCURADOR	 add constraint 	FK_SCS_ACREDITAC_SCS_PROCEDIMI	 foreign key (	IDPROCEDIMIENTOPROCURADOR,IDINSTITUCION	)	 references 	SCS_PROCEDIMIENTOSPROCURADORES	 (	IDPROCEDIMIENTOPROCURADOR,IDINSTITUCION	) 		 Deferrable;
alter table 	SCS_ACREDIT_PROCPROCURADOR	 add constraint 	FK_SCS_ACREDITAC_SCS_ACREDITAC	 foreign key (	IDACREDITACION	)	 references 	SCS_ACREDITACION	 (	IDACREDITACION	) 		 Deferrable;
alter table 	SCS_ACREDITACIONPROCEDIMIENTO	 drop constraint 	FK_SCS_ACREDITACIONPROC_PROCED	 ; 
alter table 	SCS_ACREDITACIONPROCEDIMIENTO	 drop constraint 	FK_ACREDITACIONPROC_ACREDITACI	 ; 
alter table 	SCS_ACREDITACIONPROCEDIMIENTO	 add constraint 	FK_SCS_ACREDITACIONPROC_PROCED	 foreign key (	IDPROCEDIMIENTO,IDINSTITUCION	)	 references 	SCS_PROCEDIMIENTOS	 (	IDPROCEDIMIENTO,IDINSTITUCION	) 	 on delete cascade	 Deferrable;
alter table 	SCS_ACREDITACIONPROCEDIMIENTO	 add constraint 	FK_ACREDITACIONPROC_ACREDITACI	 foreign key (	IDACREDITACION	)	 references 	SCS_ACREDITACION	 (	IDACREDITACION	) 		 Deferrable;
alter table 	SCS_ACREDITACION	 drop constraint 	FK_SCS_ACREDITACION_TIPO	 ; 
alter table 	SCS_ACREDITACION	 add constraint 	FK_SCS_ACREDITACION_TIPO	 foreign key (	IDTIPOACREDITACION	)	 references 	SCS_TIPOACREDITACION	 (	IDTIPOACREDITACION	) 		 Deferrable;
alter table 	REG_HISTORICOCONTACTO	 drop constraint 	FK_REG_HISTORICOCONTACTO_USUAR	 ; 
alter table 	REG_HISTORICOCONTACTO	 drop constraint 	FK_REG_HISTORICOCONTACTO_CONTA	 ; 
alter table 	REG_HISTORICOCONTACTO	 add constraint 	FK_REG_HISTORICOCONTACTO_USUAR	 foreign key (	IDINSTITUCION,IDUSUARIO	)	 references 	ADM_USUARIOS	 (	IDINSTITUCION,IDUSUARIO	) 		 Deferrable;
alter table 	REG_HISTORICOCONTACTO	 add constraint 	FK_REG_HISTORICOCONTACTO_CONTA	 foreign key (	IDCONTACTO	)	 references 	REG_CONTACTO	 (	IDCONTACTO	) 		 Deferrable;
alter table 	REG_CONTACTO	 drop constraint 	FK_REG_CONTACTO_USUARIOS	 ; 
alter table 	REG_CONTACTO	 drop constraint 	FK_REG_CONTACTO_TIPOCONTACTO	 ; 
alter table 	REG_CONTACTO	 drop constraint 	FK_REG_CONTACTO_MOTIVO	 ; 
alter table 	REG_CONTACTO	 drop constraint 	FK_REG_CONTACTO_ESTADO	 ; 
alter table 	REG_CONTACTO	 drop constraint 	FK_REG_CONTACTO_CATEGORIA	 ; 
alter table 	REG_CONTACTO	 add constraint 	FK_REG_CONTACTO_USUARIOS	 foreign key (	IDINSTITUCION,IDUSUARIO	)	 references 	ADM_USUARIOS	 (	IDINSTITUCION,IDUSUARIO	) 		 Deferrable;
alter table 	REG_CONTACTO	 add constraint 	FK_REG_CONTACTO_TIPOCONTACTO	 foreign key (	IDTIPO	)	 references 	REG_TIPOCONTACTO	 (	IDTIPO	) 		 Deferrable;
alter table 	REG_CONTACTO	 add constraint 	FK_REG_CONTACTO_MOTIVO	 foreign key (	IDMOTIVO	)	 references 	REG_MOTIVOCONTACTO	 (	IDMOTIVO	) 		 Deferrable;
alter table 	REG_CONTACTO	 add constraint 	FK_REG_CONTACTO_ESTADO	 foreign key (	IDESTADO	)	 references 	REG_ESTADOCONTACTO	 (	IDESTADO	) 		 Deferrable;
alter table 	REG_CONTACTO	 add constraint 	FK_REG_CONTACTO_CATEGORIA	 foreign key (	IDCATEGORIA	)	 references 	REG_CATEGORIACONTACTO	 (	IDCATEGORIA	) 		 Deferrable;
alter table 	REG_ARCHIVOCONTACTO	 drop constraint 	FK_REG_ARCHIVOCONTACTO_CONTACT	 ; 
alter table 	REG_ARCHIVOCONTACTO	 add constraint 	FK_REG_ARCHIVOCONTACTO_CONTACT	 foreign key (	IDCONTACTO	)	 references 	REG_CONTACTO	 (	IDCONTACTO	) 		 Deferrable;
alter table 	PYS_SUSCRIPCION	 drop constraint 	FK_SUSCRIPCION_SERVINSTITUCION	 ; 
alter table 	PYS_SUSCRIPCION	 drop constraint 	FK_SUSCRIPCION_PETICION	 ; 
alter table 	PYS_SUSCRIPCION	 drop constraint 	FK_SUSCRIPCION_FORMAPAGO	 ; 
alter table 	PYS_SUSCRIPCION	 drop constraint 	FK_SUSCRIPCION_CUENTASBANCARIA	 ; 
alter table 	PYS_SUSCRIPCION	 drop constraint 	FK_SUSCRIPCION_CLIENTE	 ; 
alter table 	PYS_SUSCRIPCION	 add constraint 	FK_SUSCRIPCION_SERVINSTITUCION	 foreign key (	IDSERVICIOSINSTITUCION,IDINSTITUCION,IDTIPOSERVICIOS,IDSERVICIO	)	 references 	PYS_SERVICIOSINSTITUCION	 (	IDSERVICIOSINSTITUCION,IDINSTITUCION,IDTIPOSERVICIOS,IDSERVICIO	) 		 Deferrable;
alter table 	PYS_SUSCRIPCION	 add constraint 	FK_SUSCRIPCION_PETICION	 foreign key (	IDPETICION,IDINSTITUCION	)	 references 	PYS_PETICIONCOMPRASUSCRIPCION	 (	IDPETICION,IDINSTITUCION	) 	 on delete cascade	 Deferrable;
alter table 	PYS_SUSCRIPCION	 add constraint 	FK_SUSCRIPCION_FORMAPAGO	 foreign key (	IDFORMAPAGO	)	 references 	PYS_FORMAPAGO	 (	IDFORMAPAGO	) 		 Deferrable;
alter table 	PYS_SUSCRIPCION	 add constraint 	FK_SUSCRIPCION_CUENTASBANCARIA	 foreign key (	IDCUENTA,IDINSTITUCION,IDPERSONA	)	 references 	CEN_CUENTASBANCARIAS	 (	IDCUENTA,IDINSTITUCION,IDPERSONA	) 		 Deferrable;
alter table 	PYS_SUSCRIPCION	 add constraint 	FK_SUSCRIPCION_CLIENTE	 foreign key (	IDPERSONA,IDINSTITUCION	)	 references 	CEN_CLIENTE	 (	IDPERSONA,IDINSTITUCION	) 		 Deferrable;
alter table 	PYS_SERVICIOSSOLICITADOS	 drop constraint 	PK_SERVICIOSSOLICITADOS_SERVIC	 ; 
alter table 	PYS_SERVICIOSSOLICITADOS	 drop constraint 	FK_SERVICIOSSOLICITADOS_PRECIO	 ; 
alter table 	PYS_SERVICIOSSOLICITADOS	 drop constraint 	FK_SERVICIOSSOLICITADOS_PETICI	 ; 
alter table 	PYS_SERVICIOSSOLICITADOS	 drop constraint 	FK_SERVICIOSSOLICITADOS_FORMAP	 ; 
alter table 	PYS_SERVICIOSSOLICITADOS	 drop constraint 	FK_SERVICIOSSOLICITADOS_CUENTA	 ; 
alter table 	PYS_SERVICIOSSOLICITADOS	 add constraint 	PK_SERVICIOSSOLICITADOS_SERVIC	 foreign key (	IDSERVICIOSINSTITUCION,IDINSTITUCION,IDTIPOSERVICIOS,IDSERVICIO	)	 references 	PYS_SERVICIOSINSTITUCION	 (	IDSERVICIOSINSTITUCION,IDINSTITUCION,IDTIPOSERVICIOS,IDSERVICIO	) 		 Deferrable;
alter table 	PYS_SERVICIOSSOLICITADOS	 add constraint 	FK_SERVICIOSSOLICITADOS_PRECIO	 foreign key (	IDPRECIOSSERVICIOS,IDINSTITUCION,IDTIPOSERVICIOS,IDSERVICIO,IDSERVICIOSINSTITUCION,IDPERIODICIDAD	)	 references 	PYS_PRECIOSSERVICIOS	 (	IDPRECIOSSERVICIOS,IDINSTITUCION,IDTIPOSERVICIOS,IDSERVICIO,IDSERVICIOSINSTITUCION,IDPERIODICIDAD	) 		 Deferrable;
alter table 	PYS_SERVICIOSSOLICITADOS	 add constraint 	FK_SERVICIOSSOLICITADOS_PETICI	 foreign key (	IDPETICION,IDINSTITUCION	)	 references 	PYS_PETICIONCOMPRASUSCRIPCION	 (	IDPETICION,IDINSTITUCION	) 	 on delete cascade	 Deferrable;
alter table 	PYS_SERVICIOSSOLICITADOS	 add constraint 	FK_SERVICIOSSOLICITADOS_FORMAP	 foreign key (	IDFORMAPAGO	)	 references 	PYS_FORMAPAGO	 (	IDFORMAPAGO	) 		 Deferrable;
alter table 	PYS_SERVICIOSSOLICITADOS	 add constraint 	FK_SERVICIOSSOLICITADOS_CUENTA	 foreign key (	IDCUENTA,IDINSTITUCION,IDPERSONA	)	 references 	CEN_CUENTASBANCARIAS	 (	IDCUENTA,IDINSTITUCION,IDPERSONA	) 		 Deferrable;
alter table 	PYS_SERVICIOSINSTITUCION	 drop constraint 	FK_SERVICIOS_TIPOIVA	 ; 
alter table 	PYS_SERVICIOSINSTITUCION	 drop constraint 	FK_SERVICIOSINSTITUCION_SERV	 ; 
alter table 	PYS_SERVICIOSINSTITUCION	 add constraint 	FK_SERVICIOS_TIPOIVA	 foreign key (	IDTIPOIVA	)	 references 	PYS_TIPOIVA	 (	IDTIPOIVA	) 		 Deferrable;
alter table 	PYS_SERVICIOSINSTITUCION	 add constraint 	FK_SERVICIOSINSTITUCION_SERV	 foreign key (	IDSERVICIO,IDINSTITUCION,IDTIPOSERVICIOS	)	 references 	PYS_SERVICIOS	 (	IDSERVICIO,IDINSTITUCION,IDTIPOSERVICIOS	) 		 Deferrable;
alter table 	PYS_SERVICIOS	 drop constraint 	FK_SERVICIOS_INSTITUCION	 ; 
alter table 	PYS_SERVICIOS	 drop constraint 	FK_PYS_SERVICIOS_TIPOSERVICIOS	 ; 
alter table 	PYS_SERVICIOS	 add constraint 	FK_SERVICIOS_INSTITUCION	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	PYS_SERVICIOS	 add constraint 	FK_PYS_SERVICIOS_TIPOSERVICIOS	 foreign key (	IDTIPOSERVICIOS	)	 references 	PYS_TIPOSERVICIOS	 (	IDTIPOSERVICIOS	) 		 Deferrable;
alter table 	PYS_SERVICIOANTICIPO	 drop constraint 	FK_PYS_SERVANT_PYS_SERVICIOINS	 ; 
alter table 	PYS_SERVICIOANTICIPO	 drop constraint 	FK_PYS_SERVANT_PYS_ANTICIPO	 ; 
alter table 	PYS_SERVICIOANTICIPO	 add constraint 	FK_PYS_SERVANT_PYS_SERVICIOINS	 foreign key (	IDSERVICIOSINSTITUCION,IDINSTITUCION,IDTIPOSERVICIOS,IDSERVICIO	)	 references 	PYS_SERVICIOSINSTITUCION	 (	IDSERVICIOSINSTITUCION,IDINSTITUCION,IDTIPOSERVICIOS,IDSERVICIO	) 		 Deferrable;
alter table 	PYS_SERVICIOANTICIPO	 add constraint 	FK_PYS_SERVANT_PYS_ANTICIPO	 foreign key (	IDANTICIPO,IDINSTITUCION,IDPERSONA	)	 references 	PYS_ANTICIPOLETRADO	 (	IDANTICIPO,IDINSTITUCION,IDPERSONA	) 		 Deferrable;
alter table 	PYS_PRODUCTOSSOLICITADOS	 drop constraint 	PK_PRODUCTOSSOLICITADOS_CUENTA	 ; 
alter table 	PYS_PRODUCTOSSOLICITADOS	 drop constraint 	FK_PYS_PSOLICITADOS_ENV_TIPOEN	 ; 
alter table 	PYS_PRODUCTOSSOLICITADOS	 drop constraint 	FK_PYS_PSOLICITADOS_CEN_DIRECC	 ; 
alter table 	PYS_PRODUCTOSSOLICITADOS	 drop constraint 	FK_PRODUCTOS_SOLIC_TIPOIVA	 ; 
alter table 	PYS_PRODUCTOSSOLICITADOS	 drop constraint 	FK_PRODUCTOSSOLITADOS_PETICION	 ; 
alter table 	PYS_PRODUCTOSSOLICITADOS	 drop constraint 	FK_PRODUCTOSSOLICITADOS_PRODUC	 ; 
alter table 	PYS_PRODUCTOSSOLICITADOS	 drop constraint 	FK_PRODUCTOSSOLICITADOS_FORMAP	 ; 
alter table 	PYS_PRODUCTOSSOLICITADOS	 add constraint 	PK_PRODUCTOSSOLICITADOS_CUENTA	 foreign key (	IDCUENTA,IDINSTITUCION,IDPERSONA	)	 references 	CEN_CUENTASBANCARIAS	 (	IDCUENTA,IDINSTITUCION,IDPERSONA	) 		 Deferrable;
alter table 	PYS_PRODUCTOSSOLICITADOS	 add constraint 	FK_PYS_PSOLICITADOS_ENV_TIPOEN	 foreign key (	IDTIPOENVIOS	)	 references 	ENV_TIPOENVIOS	 (	IDTIPOENVIOS	) 		 Deferrable;
alter table 	PYS_PRODUCTOSSOLICITADOS	 add constraint 	FK_PYS_PSOLICITADOS_CEN_DIRECC	 foreign key (	IDDIRECCION,IDINSTITUCION,IDPERSONA	)	 references 	CEN_DIRECCIONES	 (	IDDIRECCION,IDINSTITUCION,IDPERSONA	) 		 Deferrable;
alter table 	PYS_PRODUCTOSSOLICITADOS	 add constraint 	FK_PRODUCTOS_SOLIC_TIPOIVA	 foreign key (	IDTIPOIVA	)	 references 	PYS_TIPOIVA	 (	IDTIPOIVA	) 		 Deferrable;
alter table 	PYS_PRODUCTOSSOLICITADOS	 add constraint 	FK_PRODUCTOSSOLITADOS_PETICION	 foreign key (	IDPETICION,IDINSTITUCION	)	 references 	PYS_PETICIONCOMPRASUSCRIPCION	 (	IDPETICION,IDINSTITUCION	) 	 on delete cascade	 Deferrable;
alter table 	PYS_PRODUCTOSSOLICITADOS	 add constraint 	FK_PRODUCTOSSOLICITADOS_PRODUC	 foreign key (	IDPRODUCTOINSTITUCION,IDINSTITUCION,IDTIPOPRODUCTO,IDPRODUCTO	)	 references 	PYS_PRODUCTOSINSTITUCION	 (	IDPRODUCTOINSTITUCION,IDINSTITUCION,IDTIPOPRODUCTO,IDPRODUCTO	) 		 Deferrable;
alter table 	PYS_PRODUCTOSSOLICITADOS	 add constraint 	FK_PRODUCTOSSOLICITADOS_FORMAP	 foreign key (	IDFORMAPAGO	)	 references 	PYS_FORMAPAGO	 (	IDFORMAPAGO	) 		 Deferrable;
alter table 	PYS_PRODUCTOSINSTITUCION	 drop constraint 	FK_PYS_PRODINSTI_ADM_CONTADOR	 ; 
alter table 	PYS_PRODUCTOSINSTITUCION	 drop constraint 	FK_PRODUCTOS_TIPOIVA	 ; 
alter table 	PYS_PRODUCTOSINSTITUCION	 drop constraint 	FK_PRODUCTOSINSTITUCION_PRODUC	 ; 
alter table 	PYS_PRODUCTOSINSTITUCION	 add constraint 	FK_PYS_PRODINSTI_ADM_CONTADOR	 foreign key (	IDCONTADOR,IDINSTITUCION	)	 references 	ADM_CONTADOR	 (	IDCONTADOR,IDINSTITUCION	) 		 Deferrable;
alter table 	PYS_PRODUCTOSINSTITUCION	 add constraint 	FK_PRODUCTOS_TIPOIVA	 foreign key (	IDTIPOIVA	)	 references 	PYS_TIPOIVA	 (	IDTIPOIVA	) 		 Deferrable;
alter table 	PYS_PRODUCTOSINSTITUCION	 add constraint 	FK_PRODUCTOSINSTITUCION_PRODUC	 foreign key (	IDPRODUCTO,IDINSTITUCION,IDTIPOPRODUCTO	)	 references 	PYS_PRODUCTOS	 (	IDPRODUCTO,IDINSTITUCION,IDTIPOPRODUCTO	) 		 Deferrable;
alter table 	PYS_PRODUCTOS	 drop constraint 	FK_PYS_PRODUCTOS_TIPOSPRODUCTO	 ; 
alter table 	PYS_PRODUCTOS	 drop constraint 	FK_PRODUCTOS_INSTITUCION	 ; 
alter table 	PYS_PRODUCTOS	 add constraint 	FK_PYS_PRODUCTOS_TIPOSPRODUCTO	 foreign key (	IDTIPOPRODUCTO	)	 references 	PYS_TIPOSPRODUCTOS	 (	IDTIPOPRODUCTO	) 		 Deferrable;
alter table 	PYS_PRODUCTOS	 add constraint 	FK_PRODUCTOS_INSTITUCION	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	PYS_PRECIOSSERVICIOS	 drop constraint 	FK_PRECIOSSERVICIOS_SERVINSTIT	 ; 
alter table 	PYS_PRECIOSSERVICIOS	 drop constraint 	FK_PRECIOSSERVICIOS_PERIODICID	 ; 
alter table 	PYS_PRECIOSSERVICIOS	 add constraint 	FK_PRECIOSSERVICIOS_SERVINSTIT	 foreign key (	IDSERVICIOSINSTITUCION,IDINSTITUCION,IDTIPOSERVICIOS,IDSERVICIO	)	 references 	PYS_SERVICIOSINSTITUCION	 (	IDSERVICIOSINSTITUCION,IDINSTITUCION,IDTIPOSERVICIOS,IDSERVICIO	) 	 on delete cascade	 Deferrable;
alter table 	PYS_PRECIOSSERVICIOS	 add constraint 	FK_PRECIOSSERVICIOS_PERIODICID	 foreign key (	IDPERIODICIDAD	)	 references 	PYS_PERIODICIDAD	 (	IDPERIODICIDAD	) 		 Deferrable;
alter table 	PYS_PETICIONCOMPRASUSCRIPCION	 drop constraint 	FK_PETICION_ESTADOPETICION	 ; 
alter table 	PYS_PETICIONCOMPRASUSCRIPCION	 drop constraint 	FK_PETICION_CLIENTE	 ; 
alter table 	PYS_PETICIONCOMPRASUSCRIPCION	 add constraint 	FK_PETICION_ESTADOPETICION	 foreign key (	IDESTADOPETICION	)	 references 	PYS_ESTADOPETICION	 (	IDESTADOPETICION	) 		 Deferrable;
alter table 	PYS_PETICIONCOMPRASUSCRIPCION	 add constraint 	FK_PETICION_CLIENTE	 foreign key (	IDPERSONA,IDINSTITUCION	)	 references 	CEN_CLIENTE	 (	IDPERSONA,IDINSTITUCION	) 		 Deferrable;
alter table 	PYS_LINEAANTICIPO	 drop constraint 	FK_PYS_LINEAANT_CEN_ANTICIPO	 ; 
alter table 	PYS_LINEAANTICIPO	 drop constraint 	FK_LINEAANT_LINEAFAC	 ; 
alter table 	PYS_LINEAANTICIPO	 add constraint 	FK_PYS_LINEAANT_CEN_ANTICIPO	 foreign key (	IDANTICIPO,IDINSTITUCION,IDPERSONA	)	 references 	PYS_ANTICIPOLETRADO	 (	IDANTICIPO,IDINSTITUCION,IDPERSONA	) 		 Deferrable;
alter table 	PYS_LINEAANTICIPO	 add constraint 	FK_LINEAANT_LINEAFAC	 foreign key (	NUMEROLINEA,IDINSTITUCION,IDFACTURA	)	 references 	FAC_LINEAFACTURA	 (	NUMEROLINEA,IDINSTITUCION,IDFACTURA	) 		 Deferrable;
alter table 	PYS_FORMAPAGOSERVICIOS	 drop constraint 	FK_FORMAPAGOSERVICIO_SERVINSTI	 ; 
alter table 	PYS_FORMAPAGOSERVICIOS	 drop constraint 	FK_FORMAPAGOSERVICIO_FORMAPAGO	 ; 
alter table 	PYS_FORMAPAGOSERVICIOS	 add constraint 	FK_FORMAPAGOSERVICIO_SERVINSTI	 foreign key (	IDSERVICIOSINSTITUCION,IDINSTITUCION,IDTIPOSERVICIOS,IDSERVICIO	)	 references 	PYS_SERVICIOSINSTITUCION	 (	IDSERVICIOSINSTITUCION,IDINSTITUCION,IDTIPOSERVICIOS,IDSERVICIO	) 	 on delete cascade	 Deferrable;
alter table 	PYS_FORMAPAGOSERVICIOS	 add constraint 	FK_FORMAPAGOSERVICIO_FORMAPAGO	 foreign key (	IDFORMAPAGO	)	 references 	PYS_FORMAPAGO	 (	IDFORMAPAGO	) 		 Deferrable;
alter table 	PYS_FORMAPAGOPRODUCTO	 drop constraint 	FK_FORMAPAGOPRODUCTO_PRODINSTI	 ; 
alter table 	PYS_FORMAPAGOPRODUCTO	 drop constraint 	FK_FORMAPAGOPRODUCTO_FORMAPAGO	 ; 
alter table 	PYS_FORMAPAGOPRODUCTO	 add constraint 	FK_FORMAPAGOPRODUCTO_PRODINSTI	 foreign key (	IDPRODUCTOINSTITUCION,IDINSTITUCION,IDTIPOPRODUCTO,IDPRODUCTO	)	 references 	PYS_PRODUCTOSINSTITUCION	 (	IDPRODUCTOINSTITUCION,IDINSTITUCION,IDTIPOPRODUCTO,IDPRODUCTO	) 	 on delete cascade	 Deferrable;
alter table 	PYS_FORMAPAGOPRODUCTO	 add constraint 	FK_FORMAPAGOPRODUCTO_FORMAPAGO	 foreign key (	IDFORMAPAGO	)	 references 	PYS_FORMAPAGO	 (	IDFORMAPAGO	) 		 Deferrable;
alter table 	PYS_COMPRA	 drop constraint 	FK_PYS_COMPRA_PRODUCTOSINSTITU	 ; 
alter table 	PYS_COMPRA	 drop constraint 	FK_PYS_COMPRA_PETICIONCOMPRA	 ; 
alter table 	PYS_COMPRA	 drop constraint 	FK_PYS_COMPRA_FORMAPAGO	 ; 
alter table 	PYS_COMPRA	 drop constraint 	FK_PYS_COMPRA_FACTURA	 ; 
alter table 	PYS_COMPRA	 drop constraint 	FK_COMPRA_TIPOIVA	 ; 
alter table 	PYS_COMPRA	 drop constraint 	FK_COMPRA_CUENTASBANCARIAS	 ; 
alter table 	PYS_COMPRA	 drop constraint 	FK_COMPRA_CLIENTE	 ; 
alter table 	PYS_COMPRA	 add constraint 	FK_PYS_COMPRA_PRODUCTOSINSTITU	 foreign key (	IDPRODUCTOINSTITUCION,IDINSTITUCION,IDTIPOPRODUCTO,IDPRODUCTO	)	 references 	PYS_PRODUCTOSINSTITUCION	 (	IDPRODUCTOINSTITUCION,IDINSTITUCION,IDTIPOPRODUCTO,IDPRODUCTO	) 		 Deferrable;
alter table 	PYS_COMPRA	 add constraint 	FK_PYS_COMPRA_PETICIONCOMPRA	 foreign key (	IDPETICION,IDINSTITUCION	)	 references 	PYS_PETICIONCOMPRASUSCRIPCION	 (	IDPETICION,IDINSTITUCION	) 	 on delete cascade	 Deferrable;
alter table 	PYS_COMPRA	 add constraint 	FK_PYS_COMPRA_FORMAPAGO	 foreign key (	IDFORMAPAGO	)	 references 	PYS_FORMAPAGO	 (	IDFORMAPAGO	) 		 Deferrable;
alter table 	PYS_COMPRA	 add constraint 	FK_PYS_COMPRA_FACTURA	 foreign key (	IDFACTURA,IDINSTITUCION,NUMEROLINEA	)	 references 	FAC_LINEAFACTURA	 (	IDFACTURA,IDINSTITUCION,NUMEROLINEA	) 		 Deferrable;
alter table 	PYS_COMPRA	 add constraint 	FK_COMPRA_TIPOIVA	 foreign key (	IDTIPOIVA	)	 references 	PYS_TIPOIVA	 (	IDTIPOIVA	) 		 Deferrable;
alter table 	PYS_COMPRA	 add constraint 	FK_COMPRA_CUENTASBANCARIAS	 foreign key (	IDCUENTA,IDINSTITUCION,IDPERSONA	)	 references 	CEN_CUENTASBANCARIAS	 (	IDCUENTA,IDINSTITUCION,IDPERSONA	) 		 Deferrable;
alter table 	PYS_COMPRA	 add constraint 	FK_COMPRA_CLIENTE	 foreign key (	IDPERSONA,IDINSTITUCION	)	 references 	CEN_CLIENTE	 (	IDPERSONA,IDINSTITUCION	) 		 Deferrable;
alter table 	PCAJG_TURNO_SCSTURNO	 drop constraint 	FK_PCAJGTURNO_SCSTURNO	 ; 
alter table 	PCAJG_TURNO_SCSTURNO	 drop constraint 	FK_PCAJGTURNO	 ; 
alter table 	PCAJG_TURNO_SCSTURNO	 add constraint 	FK_PCAJGTURNO_SCSTURNO	 foreign key (	IDTURNO,IDINSTITUCION	)	 references 	SCS_TURNO	 (	IDTURNO,IDINSTITUCION	) 		 Deferrable;
alter table 	PCAJG_TURNO_SCSTURNO	 add constraint 	FK_PCAJGTURNO	 foreign key (	IDINSTITUCION,IDENTIFICADOR	)	 references 	PCAJG_TURNO	 (	IDINSTITUCION,IDENTIFICADOR	) 		 Deferrable;
alter table 	PCAJG_TIPO_RESOLUCION_SCSTIPOR	 drop constraint 	FK_PCAJGTIPORESO_SCSTIPORESOL	 ; 
alter table 	PCAJG_TIPO_RESOLUCION_SCSTIPOR	 drop constraint 	FK_PCAJGTIPORESOLUCION	 ; 
alter table 	PCAJG_TIPO_RESOLUCION_SCSTIPOR	 add constraint 	FK_PCAJGTIPORESO_SCSTIPORESOL	 foreign key (	IDTIPORESOLUCION	)	 references 	SCS_TIPORESOLUCION	 (	IDTIPORESOLUCION	) 		 Deferrable;
alter table 	PCAJG_TIPO_RESOLUCION_SCSTIPOR	 add constraint 	FK_PCAJGTIPORESOLUCION	 foreign key (	IDINSTITUCION,IDENTIFICADOR	)	 references 	PCAJG_TIPO_RESOLUCION	 (	IDINSTITUCION,IDENTIFICADOR	) 		 Deferrable;
alter table 	PCAJG_TIPO_PERSONA_TIPOPER	 drop constraint 	FK_PCAJGTIPOPERSONA	 ; 
alter table 	PCAJG_TIPO_PERSONA_TIPOPER	 add constraint 	FK_PCAJGTIPOPERSONA	 foreign key (	IDINSTITUCION,IDENTIFICADOR	)	 references 	PCAJG_TIPO_PERSONA	 (	IDINSTITUCION,IDENTIFICADOR	) 		 Deferrable;
alter table 	PCAJG_TIPO_IDENTIFICACION_CENT	 drop constraint 	FK_PCAJGTIPOIDEN_CENTIPOIDENT	 ; 
alter table 	PCAJG_TIPO_IDENTIFICACION_CENT	 drop constraint 	FK_PCAJGTIPOIDENTIFICACION	 ; 
alter table 	PCAJG_TIPO_IDENTIFICACION_CENT	 add constraint 	FK_PCAJGTIPOIDEN_CENTIPOIDENT	 foreign key (	IDTIPOIDENTIFICACION	)	 references 	CEN_TIPOIDENTIFICACION	 (	IDTIPOIDENTIFICACION	) 		 Deferrable;
alter table 	PCAJG_TIPO_IDENTIFICACION_CENT	 add constraint 	FK_PCAJGTIPOIDENTIFICACION	 foreign key (	IDINSTITUCION,IDENTIFICADOR	)	 references 	PCAJG_TIPO_IDENTIFICACION	 (	IDINSTITUCION,IDENTIFICADOR	) 		 Deferrable;
alter table 	PCAJG_TIPO_EJG_SCSTIPOEJG	 drop constraint 	FK_PCAJGTIPOEJG_SCSTIPOEJG	 ; 
alter table 	PCAJG_TIPO_EJG_SCSTIPOEJG	 drop constraint 	FK_PCAJGTIPOEJG	 ; 
alter table 	PCAJG_TIPO_EJG_SCSTIPOEJG	 add constraint 	FK_PCAJGTIPOEJG_SCSTIPOEJG	 foreign key (	IDTIPOEJG	)	 references 	SCS_TIPOEJG	 (	IDTIPOEJG	) 		 Deferrable;
alter table 	PCAJG_TIPO_EJG_SCSTIPOEJG	 add constraint 	FK_PCAJGTIPOEJG	 foreign key (	IDINSTITUCION,IDENTIFICADOR	)	 references 	PCAJG_TIPO_EJG	 (	IDINSTITUCION,IDENTIFICADOR	) 		 Deferrable;
alter table 	PCAJG_TIPO_DICTAMEN_SCSTIPODIC	 drop constraint 	FK_PCAJGTIPODICT_SCSTIPODICTA	 ; 
alter table 	PCAJG_TIPO_DICTAMEN_SCSTIPODIC	 drop constraint 	FK_PCAJGTIPODICTAMEN	 ; 
alter table 	PCAJG_TIPO_DICTAMEN_SCSTIPODIC	 add constraint 	FK_PCAJGTIPODICT_SCSTIPODICTA	 foreign key (	IDTIPODICTAMENEJG,IDINSTITUCION	)	 references 	SCS_TIPODICTAMENEJG	 (	IDTIPODICTAMENEJG,IDINSTITUCION	) 		 Deferrable;
alter table 	PCAJG_TIPO_DICTAMEN_SCSTIPODIC	 add constraint 	FK_PCAJGTIPODICTAMEN	 foreign key (	IDINSTITUCION,IDENTIFICADOR	)	 references 	PCAJG_TIPO_DICTAMEN	 (	IDINSTITUCION,IDENTIFICADOR	) 		 Deferrable;
alter table 	PCAJG_SEXO_S_SEXO	 drop constraint 	FK_PCAJGSEXO	 ; 
alter table 	PCAJG_SEXO_S_SEXO	 add constraint 	FK_PCAJGSEXO	 foreign key (	IDINSTITUCION,IDENTIFICADOR	)	 references 	PCAJG_SEXO	 (	IDINSTITUCION,IDENTIFICADOR	) 		 Deferrable;
alter table 	PCAJG_REGIMEN_CONY_S_REGCONY	 drop constraint 	FK_PCAJGREGIMENCONYUGAL	 ; 
alter table 	PCAJG_REGIMEN_CONY_S_REGCONY	 add constraint 	FK_PCAJGREGIMENCONYUGAL	 foreign key (	IDINSTITUCION,IDENTIFICADOR	)	 references 	PCAJG_REGIMEN_CONYUGAL	 (	IDINSTITUCION,IDENTIFICADOR	) 		 Deferrable;
alter table 	PCAJG_PROCED_SCSPROCED	 drop constraint 	FK_PCAJGPROCED_SCSPROCED	 ; 
alter table 	PCAJG_PROCED_SCSPROCED	 drop constraint 	FK_PCAJGPROCED	 ; 
alter table 	PCAJG_PROCED_SCSPROCED	 add constraint 	FK_PCAJGPROCED_SCSPROCED	 foreign key (	IDPROCEDIMIENTO,IDINSTITUCION	)	 references 	SCS_PROCEDIMIENTOS	 (	IDPROCEDIMIENTO,IDINSTITUCION	) 		 Deferrable;
alter table 	PCAJG_PROCED_SCSPROCED	 add constraint 	FK_PCAJGPROCED	 foreign key (	IDINSTITUCION,IDENTIFICADOR	)	 references 	PCAJG_PROCEDIMIENTOS	 (	IDINSTITUCION,IDENTIFICADOR	) 		 Deferrable;
alter table 	PCAJG_PAIS_CENPAIS	 drop constraint 	FK_PCAJGPAIS_CENPAIS	 ; 
alter table 	PCAJG_PAIS_CENPAIS	 drop constraint 	FK_PCAJGPAIS	 ; 
alter table 	PCAJG_PAIS_CENPAIS	 add constraint 	FK_PCAJGPAIS_CENPAIS	 foreign key (	IDPAIS	)	 references 	CEN_PAIS	 (	IDPAIS	) 		 Deferrable;
alter table 	PCAJG_PAIS_CENPAIS	 add constraint 	FK_PCAJGPAIS	 foreign key (	IDINSTITUCION,IDENTIFICADOR	)	 references 	PCAJG_PAIS	 (	IDINSTITUCION,IDENTIFICADOR	) 		 Deferrable;
alter table 	PCAJG_MOTIVO_RESOLUCION_SCSTIP	 drop constraint 	FK_PCAJGMOTIVORE_SCSTIPOFUNDA	 ; 
alter table 	PCAJG_MOTIVO_RESOLUCION_SCSTIP	 drop constraint 	FK_PCAJGMOTIVORESOLUCION	 ; 
alter table 	PCAJG_MOTIVO_RESOLUCION_SCSTIP	 add constraint 	FK_PCAJGMOTIVORE_SCSTIPOFUNDA	 foreign key (	IDFUNDAMENTO,IDINSTITUCION	)	 references 	SCS_TIPOFUNDAMENTOS	 (	IDFUNDAMENTO,IDINSTITUCION	) 		 Deferrable;
alter table 	PCAJG_MOTIVO_RESOLUCION_SCSTIP	 add constraint 	FK_PCAJGMOTIVORESOLUCION	 foreign key (	IDINSTITUCION,IDENTIFICADOR	)	 references 	PCAJG_MOTIVO_RESOLUCION	 (	IDINSTITUCION,IDENTIFICADOR	) 		 Deferrable;
alter table 	PCAJG_MOTIVO_DICTAMEN_SCSTIPOF	 drop constraint 	FK_PCAJGMOTIVODI_SCSTIPOFUNDA	 ; 
alter table 	PCAJG_MOTIVO_DICTAMEN_SCSTIPOF	 drop constraint 	FK_PCAJGMOTIVODICTAMEN	 ; 
alter table 	PCAJG_MOTIVO_DICTAMEN_SCSTIPOF	 add constraint 	FK_PCAJGMOTIVODI_SCSTIPOFUNDA	 foreign key (	IDFUNDAMENTOCALIF,IDINSTITUCION	)	 references 	SCS_TIPOFUNDAMENTOCALIF	 (	IDFUNDAMENTOCALIF,IDINSTITUCION	) 		 Deferrable;
alter table 	PCAJG_MOTIVO_DICTAMEN_SCSTIPOF	 add constraint 	FK_PCAJGMOTIVODICTAMEN	 foreign key (	IDINSTITUCION,IDENTIFICADOR	)	 references 	PCAJG_MOTIVO_DICTAMEN	 (	IDINSTITUCION,IDENTIFICADOR	) 		 Deferrable;
alter table 	PCAJG_JURIS_SCS_JURIS	 drop constraint 	FK_PCAJGJURIS_SCSJURIS	 ; 
alter table 	PCAJG_JURIS_SCS_JURIS	 drop constraint 	FK_PCAJGJURIS	 ; 
alter table 	PCAJG_JURIS_SCS_JURIS	 add constraint 	FK_PCAJGJURIS_SCSJURIS	 foreign key (	IDJURISDICCION	)	 references 	SCS_JURISDICCION	 (	IDJURISDICCION	) 		 Deferrable;
alter table 	PCAJG_JURIS_SCS_JURIS	 add constraint 	FK_PCAJGJURIS	 foreign key (	IDINSTITUCION,IDENTIFICADOR	)	 references 	PCAJG_JURISDICCION	 (	IDINSTITUCION,IDENTIFICADOR	) 		 Deferrable;
alter table 	PCAJG_ICAS_CENINSTITUCION	 drop constraint 	FK_PCAJGICAS_CENINSTITUCION	 ; 
alter table 	PCAJG_ICAS_CENINSTITUCION	 drop constraint 	FK_PCAJGICAS	 ; 
alter table 	PCAJG_ICAS_CENINSTITUCION	 add constraint 	FK_PCAJGICAS_CENINSTITUCION	 foreign key (	IDINSTITUCION_SIGA	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	PCAJG_ICAS_CENINSTITUCION	 add constraint 	FK_PCAJGICAS	 foreign key (	IDINSTITUCION_PCAJG,IDENTIFICADOR	)	 references 	PCAJG_ICAS	 (	IDINSTITUCION,IDENTIFICADOR	) 		 Deferrable;
alter table 	PCAJG_ICAPROC_CENINSTI	 drop constraint 	FK_PCAJGICASPROC	 ; 
alter table 	PCAJG_ICAPROC_CENINSTI	 drop constraint 	FK_PCAJGICAPROC_CENINSTI	 ; 
alter table 	PCAJG_ICAPROC_CENINSTI	 add constraint 	FK_PCAJGICASPROC	 foreign key (	IDINSTITUCION_PCAJG,IDENTIFICADOR	)	 references 	PCAJG_ICAPROCURADOR	 (	IDINSTITUCION,IDENTIFICADOR	) 		 Deferrable;
alter table 	PCAJG_ICAPROC_CENINSTI	 add constraint 	FK_PCAJGICAPROC_CENINSTI	 foreign key (	IDINSTITUCION_SIGA	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	PCAJG_ESTADO_CIVIL_CENESTADOCI	 drop constraint 	FK_PCAJGESTADOCI_CENESTADOCIV	 ; 
alter table 	PCAJG_ESTADO_CIVIL_CENESTADOCI	 drop constraint 	FK_PCAJGESTADOCIVIL	 ; 
alter table 	PCAJG_ESTADO_CIVIL_CENESTADOCI	 add constraint 	FK_PCAJGESTADOCI_CENESTADOCIV	 foreign key (	IDESTADOCIVIL	)	 references 	CEN_ESTADOCIVIL	 (	IDESTADOCIVIL	) 		 Deferrable;
alter table 	PCAJG_ESTADO_CIVIL_CENESTADOCI	 add constraint 	FK_PCAJGESTADOCIVIL	 foreign key (	IDINSTITUCION,IDENTIFICADOR	)	 references 	PCAJG_ESTADO_CIVIL	 (	IDINSTITUCION,IDENTIFICADOR	) 		 Deferrable;
alter table 	MJU_REPORT_CERTIFICADO	 drop constraint 	FK_MJUREPCERT_MJUREPORT	 ; 
alter table 	MJU_REPORT_CERTIFICADO	 add constraint 	FK_MJUREPCERT_MJUREPORT	 foreign key (	IDMJUREPORT	)	 references 	MJU_REPORT	 (	IDMJUREPORT	) 		 Deferrable;
alter table 	MJU_COLEGIADO_ERROR	 drop constraint 	FK_MJUCOLEG_MJUREPORT	 ; 
alter table 	MJU_COLEGIADO_ERROR	 add constraint 	FK_MJUCOLEG_MJUREPORT	 foreign key (	IDMJUREPORT	)	 references 	MJU_REPORT	 (	IDMJUREPORT	) 		 Deferrable;
alter table 	GEN_TAREASPENDIENTES	 drop constraint 	FK_GEN_TAREASPENDIENTES_TAREA	 ; 
alter table 	GEN_TAREASPENDIENTES	 drop constraint 	FK_GEN_TAREASPENDIENTES_PROCES	 ; 
alter table 	GEN_TAREASPENDIENTES	 add constraint 	FK_GEN_TAREASPENDIENTES_TAREA	 foreign key (	IDTAREA	)	 references 	GEN_TAREAS	 (	IDTAREA	) 		 Deferrable;
alter table 	GEN_TAREASPENDIENTES	 add constraint 	FK_GEN_TAREASPENDIENTES_PROCES	 foreign key (	IDPROCESO	)	 references 	GEN_PROCESOS	 (	IDPROCESO	) 		 Deferrable;
alter table 	GEN_TABLAS_MAESTRAS	 drop constraint 	FK_GEN_TABLAS_MAESTRAS_RECURSO	 ; 
alter table 	GEN_TABLAS_MAESTRAS	 add constraint 	FK_GEN_TABLAS_MAESTRAS_RECURSO	 foreign key (	IDLENGUAJE,IDRECURSO	)	 references 	GEN_RECURSOS	 (	IDLENGUAJE,IDRECURSO	) 		 Deferrable;
alter table 	GEN_RECURSOS_CATALOGOS	 drop constraint 	FK_GEN_RECURSOS_CATAL_LENGUAJE	 ; 
alter table 	GEN_RECURSOS_CATALOGOS	 drop constraint 	FK_GEN_RECURSOS_CATAL_INSTIT	 ; 
alter table 	GEN_RECURSOS_CATALOGOS	 add constraint 	FK_GEN_RECURSOS_CATAL_LENGUAJE	 foreign key (	IDLENGUAJE	)	 references 	ADM_LENGUAJES	 (	IDLENGUAJE	) 		 Deferrable;
alter table 	GEN_RECURSOS_CATALOGOS	 add constraint 	FK_GEN_RECURSOS_CATAL_INSTIT	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	GEN_RECURSOS	 drop constraint 	FK_GEN_RECURSOS_PROPIEDAD	 ; 
alter table 	GEN_RECURSOS	 drop constraint 	FK_GEN_RECURSOS_LENGUAJE	 ; 
alter table 	GEN_RECURSOS	 add constraint 	FK_GEN_RECURSOS_PROPIEDAD	 foreign key (	IDPROPIEDAD	)	 references 	GEN_RECURSPROPS	 (	IDPROPIEDAD	) 		 Deferrable;
alter table 	GEN_RECURSOS	 add constraint 	FK_GEN_RECURSOS_LENGUAJE	 foreign key (	IDLENGUAJE	)	 references 	ADM_LENGUAJES	 (	IDLENGUAJE	) 		 Deferrable;
alter table 	GEN_PESTANAS	 drop constraint 	FK_GEN_PESTANAS_RECURSOS	 ; 
alter table 	GEN_PESTANAS	 drop constraint 	FK_GEN_PESTANAS_PROCESOS	 ; 
alter table 	GEN_PESTANAS	 add constraint 	FK_GEN_PESTANAS_RECURSOS	 foreign key (	IDRECURSO,IDLENGUAJE	)	 references 	GEN_RECURSOS	 (	IDRECURSO,IDLENGUAJE	) 		 Deferrable;
alter table 	GEN_PESTANAS	 add constraint 	FK_GEN_PESTANAS_PROCESOS	 foreign key (	IDPROCESO	)	 references 	GEN_PROCESOS	 (	IDPROCESO	) 		 Deferrable;
alter table 	GEN_MTIPOS_ERROR	 drop constraint 	FK_GEN_MTIPOS_ERROR_TIPOERROR	 ; 
alter table 	GEN_MTIPOS_ERROR	 drop constraint 	FK_GEN_MTIPOS_ERROR_CATEGORIAS	 ; 
alter table 	GEN_MTIPOS_ERROR	 add constraint 	FK_GEN_MTIPOS_ERROR_TIPOERROR	 foreign key (	IDTIPOERROR	)	 references 	GEN_DTIPOS_ERROR	 (	IDTIPOERROR	) 		 Deferrable;
alter table 	GEN_MTIPOS_ERROR	 add constraint 	FK_GEN_MTIPOS_ERROR_CATEGORIAS	 foreign key (	IDCATEGORIAERROR	)	 references 	GEN_CATEGORIAS_ERROR	 (	IDCATEGORIAERROR	) 		 Deferrable;
alter table 	GEN_MENU	 drop constraint 	FK_GEN_MENU_RECURSOS	 ; 
alter table 	GEN_MENU	 drop constraint 	FK_GEN_MENU_PROCESOS	 ; 
alter table 	GEN_MENU	 drop constraint 	FK_GEN_MENU_MENU	 ; 
alter table 	GEN_MENU	 add constraint 	FK_GEN_MENU_RECURSOS	 foreign key (	IDLENGUAJE,IDRECURSO	)	 references 	GEN_RECURSOS	 (	IDLENGUAJE,IDRECURSO	) 		 Deferrable;
alter table 	GEN_MENU	 add constraint 	FK_GEN_MENU_PROCESOS	 foreign key (	IDPROCESO	)	 references 	GEN_PROCESOS	 (	IDPROCESO	) 		 Deferrable;
alter table 	GEN_MENU	 add constraint 	FK_GEN_MENU_MENU	 foreign key (	GEN_MENU_IDMENU	)	 references 	GEN_MENU	 (	IDMENU	) 		 Deferrable;
alter table 	GEN_ERRORES	 drop constraint 	FK_GEN_ERRORES_USUARIOS	 ; 
alter table 	GEN_ERRORES	 drop constraint 	FK_GEN_ERRORES_MTIPOS_ERROR	 ; 
alter table 	GEN_ERRORES	 add constraint 	FK_GEN_ERRORES_USUARIOS	 foreign key (	IDUSUARIO,IDINSTITUCION	)	 references 	ADM_USUARIOS	 (	IDUSUARIO,IDINSTITUCION	) 	 on delete cascade	 Deferrable;
alter table 	GEN_ERRORES	 add constraint 	FK_GEN_ERRORES_MTIPOS_ERROR	 foreign key (	IDTIPOERROR,IDCATEGORIAERROR	)	 references 	GEN_MTIPOS_ERROR	 (	IDTIPOERROR,IDCATEGORIAERROR	) 		 Deferrable;
alter table 	GEN_DTIPOS_ERROR	 drop constraint 	FK_GEN_DTIPOS_ERROR_LENGUAJES	 ; 
alter table 	GEN_DTIPOS_ERROR	 add constraint 	FK_GEN_DTIPOS_ERROR_LENGUAJES	 foreign key (	IDLENGUAJE	)	 references 	ADM_LENGUAJES	 (	IDLENGUAJE	) 		 Deferrable;
alter table 	GEN_CATEGORIAS_ERROR	 drop constraint 	FK_GEN_CATECORIAS_ERROR_PROCES	 ; 
alter table 	GEN_CATEGORIAS_ERROR	 add constraint 	FK_GEN_CATECORIAS_ERROR_PROCES	 foreign key (	IDPROCESO	)	 references 	GEN_PROCESOS	 (	IDPROCESO	) 		 Deferrable;
alter table 	FICHA_MENU	 drop constraint 	MENU_PARENT	 ; 
alter table 	FICHA_MENU	 add constraint 	MENU_PARENT	 foreign key (	IDMENU_PARENT	)	 references 	FICHA_MENU	 (	IDMENU	) 		 Deferrable;
alter table 	FCS_RETENCIONES_JUDICIALES	 drop constraint 	FK_RETENCIONES_JUDICIALES_DEST	 ; 
alter table 	FCS_RETENCIONES_JUDICIALES	 drop constraint 	FK_RETENCIONES_JUDICIALES_COLE	 ; 
alter table 	FCS_RETENCIONES_JUDICIALES	 add constraint 	FK_RETENCIONES_JUDICIALES_DEST	 foreign key (	IDDESTINATARIO,IDINSTITUCION	)	 references 	FCS_DESTINATARIOS_RETENCIONES	 (	IDDESTINATARIO,IDINSTITUCION	) 		 Deferrable;
alter table 	FCS_RETENCIONES_JUDICIALES	 add constraint 	FK_RETENCIONES_JUDICIALES_COLE	 foreign key (	IDPERSONA,IDINSTITUCION	)	 references 	CEN_COLEGIADO	 (	IDPERSONA,IDINSTITUCION	) 		 Deferrable;
alter table 	FCS_PAGO_GRUPOFACT_HITO	 drop constraint 	FK_PAGO_GRUPOFACT_HITO_PAGO	 ; 
alter table 	FCS_PAGO_GRUPOFACT_HITO	 drop constraint 	FK_PAGO_GRUPOFACT_HITO_HITOGEN	 ; 
alter table 	FCS_PAGO_GRUPOFACT_HITO	 drop constraint 	FK_PAGO_GRUPOFACT_HITO_GRUPOFA	 ; 
alter table 	FCS_PAGO_GRUPOFACT_HITO	 add constraint 	FK_PAGO_GRUPOFACT_HITO_PAGO	 foreign key (	IDPAGOSJG,IDINSTITUCION	)	 references 	FCS_PAGOSJG	 (	IDPAGOSJG,IDINSTITUCION	) 	 on delete cascade	 Deferrable;
alter table 	FCS_PAGO_GRUPOFACT_HITO	 add constraint 	FK_PAGO_GRUPOFACT_HITO_HITOGEN	 foreign key (	IDHITOGENERAL	)	 references 	FCS_HITOGENERAL	 (	IDHITOGENERAL	) 		 Deferrable;
alter table 	FCS_PAGO_GRUPOFACT_HITO	 add constraint 	FK_PAGO_GRUPOFACT_HITO_GRUPOFA	 foreign key (	IDGRUPOFACTURACION,IDINSTITUCION	)	 references 	SCS_GRUPOFACTURACION	 (	IDGRUPOFACTURACION,IDINSTITUCION	) 		 Deferrable;
alter table 	FCS_PAGO_COLEGIADO	 drop constraint 	FK_SCS_MAESTRORETENCIONES	 ; 
alter table 	FCS_PAGO_COLEGIADO	 drop constraint 	FK_PER_DESTINO_CLIENTE	 ; 
alter table 	FCS_PAGO_COLEGIADO	 drop constraint 	FK_PAGOSJG_COLEGIADO_PAGO	 ; 
alter table 	FCS_PAGO_COLEGIADO	 drop constraint 	FK_PAGOSJG_COLEGIADO_CLIENTE	 ; 
alter table 	FCS_PAGO_COLEGIADO	 drop constraint 	FK_FCS_PER_DEST_CUENTASBANCA	 ; 
alter table 	FCS_PAGO_COLEGIADO	 add constraint 	FK_SCS_MAESTRORETENCIONES	 foreign key (	IDRETENCION	)	 references 	SCS_MAESTRORETENCIONES	 (	IDRETENCION	) 		 Deferrable;
alter table 	FCS_PAGO_COLEGIADO	 add constraint 	FK_PER_DESTINO_CLIENTE	 foreign key (	IDPERDESTINO,IDINSTITUCION	)	 references 	CEN_CLIENTE	 (	IDPERSONA,IDINSTITUCION	) 		 Deferrable;
alter table 	FCS_PAGO_COLEGIADO	 add constraint 	FK_PAGOSJG_COLEGIADO_PAGO	 foreign key (	IDPAGOSJG,IDINSTITUCION	)	 references 	FCS_PAGOSJG	 (	IDPAGOSJG,IDINSTITUCION	) 		 Deferrable;
alter table 	FCS_PAGO_COLEGIADO	 add constraint 	FK_PAGOSJG_COLEGIADO_CLIENTE	 foreign key (	IDPERORIGEN,IDINSTITUCION	)	 references 	CEN_CLIENTE	 (	IDPERSONA,IDINSTITUCION	) 		 Deferrable;
alter table 	FCS_PAGO_COLEGIADO	 add constraint 	FK_FCS_PER_DEST_CUENTASBANCA	 foreign key (	IDCUENTA,IDINSTITUCION,IDPERDESTINO	)	 references 	CEN_CUENTASBANCARIAS	 (	IDCUENTA,IDINSTITUCION,IDPERSONA	) 		 Deferrable;
alter table 	FCS_PAGOS_ESTADOSPAGOS	 drop constraint 	FK_PAGOS_ESTADOSPAGO_ESTADOSPA	 ; 
alter table 	FCS_PAGOS_ESTADOSPAGOS	 drop constraint 	FK_PAGOS_ESTADOSPAGOS_PAGOS	 ; 
alter table 	FCS_PAGOS_ESTADOSPAGOS	 add constraint 	FK_PAGOS_ESTADOSPAGO_ESTADOSPA	 foreign key (	IDESTADOPAGOSJG	)	 references 	FCS_ESTADOSPAGOS	 (	IDESTADOPAGOSJG	) 		 Deferrable;
alter table 	FCS_PAGOS_ESTADOSPAGOS	 add constraint 	FK_PAGOS_ESTADOSPAGOS_PAGOS	 foreign key (	IDPAGOSJG,IDINSTITUCION	)	 references 	FCS_PAGOSJG	 (	IDPAGOSJG,IDINSTITUCION	) 	 on delete cascade	 Deferrable;
alter table 	FCS_PAGOSJG	 drop constraint 	FK_PAGOSJG_FACTURACION	 ; 
alter table 	FCS_PAGOSJG	 drop constraint 	FK_FCS_PAGOSJG_FAC_SUFIJO	 ; 
alter table 	FCS_PAGOSJG	 add constraint 	FK_PAGOSJG_FACTURACION	 foreign key (	IDFACTURACION,IDINSTITUCION	)	 references 	FCS_FACTURACIONJG	 (	IDFACTURACION,IDINSTITUCION	) 	 on delete cascade	 Deferrable;
alter table 	FCS_PAGOSJG	 add constraint 	FK_FCS_PAGOSJG_FAC_SUFIJO	 foreign key (	IDSUFIJO,IDINSTITUCION	)	 references 	FAC_SUFIJO	 (	IDSUFIJO,IDINSTITUCION	) 		 Deferrable;
alter table 	FCS_MOVIMIENTOSVARIOS	 drop constraint 	FK_MOVIMIENTOSVARIOS_GRFAC	 ; 
alter table 	FCS_MOVIMIENTOSVARIOS	 drop constraint 	FK_MOVIMIENTOSVARIOS_FAC	 ; 
alter table 	FCS_MOVIMIENTOSVARIOS	 drop constraint 	FK_MOVIMIENTOSVARIOS_COLEG	 ; 
alter table 	FCS_MOVIMIENTOSVARIOS	 add constraint 	FK_MOVIMIENTOSVARIOS_GRFAC	 foreign key (	IDGRUPOFACTURACION,IDINSTITUCION	)	 references 	SCS_GRUPOFACTURACION	 (	IDGRUPOFACTURACION,IDINSTITUCION	) 		 Deferrable;
alter table 	FCS_MOVIMIENTOSVARIOS	 add constraint 	FK_MOVIMIENTOSVARIOS_FAC	 foreign key (	IDFACTURACION,IDINSTITUCION	)	 references 	FCS_FACTURACIONJG	 (	IDFACTURACION,IDINSTITUCION	) 		 Deferrable;
alter table 	FCS_MOVIMIENTOSVARIOS	 add constraint 	FK_MOVIMIENTOSVARIOS_COLEG	 foreign key (	IDPERSONA,IDINSTITUCION	)	 references 	CEN_COLEGIADO	 (	IDPERSONA,IDINSTITUCION	) 		 Deferrable;
alter table 	FCS_HISTO_TIPOACTCOSTEFIJO	 drop constraint 	FK_FCSHISTOTACTCOSTEFIJO_FACT	 ; 
alter table 	FCS_HISTO_TIPOACTCOSTEFIJO	 add constraint 	FK_FCSHISTOTACTCOSTEFIJO_FACT	 foreign key (	IDFACTURACION,IDINSTITUCION	)	 references 	FCS_FACTURACIONJG	 (	IDFACTURACION,IDINSTITUCION	) 		 Deferrable;
alter table 	FCS_HISTO_ACREDITACIONPROC	 drop constraint 	FK_HISTO_ACREDITACIONPROC_FACT	 ; 
alter table 	FCS_HISTO_ACREDITACIONPROC	 drop constraint 	FK_FCS_HISTOACR_PROC	 ; 
alter table 	FCS_HISTO_ACREDITACIONPROC	 drop constraint 	FK_FCS_HISTOACR_FACT	 ; 
alter table 	FCS_HISTO_ACREDITACIONPROC	 add constraint 	FK_HISTO_ACREDITACIONPROC_FACT	 foreign key (	IDFACTURACION,IDINSTITUCION	)	 references 	FCS_FACTURACIONJG	 (	IDFACTURACION,IDINSTITUCION	) 		 Deferrable;
alter table 	FCS_HISTO_ACREDITACIONPROC	 add constraint 	FK_FCS_HISTOACR_PROC	 foreign key (	IDFACTURACION,IDINSTITUCION,IDPROCEDIMIENTO	)	 references 	FCS_HISTORICO_PROCEDIMIENTOS	 (	IDFACTURACION,IDINSTITUCION,IDPROCEDIMIENTO	) 		 Deferrable;
alter table 	FCS_HISTO_ACREDITACIONPROC	 add constraint 	FK_FCS_HISTOACR_FACT	 foreign key (	IDFACTURACION,IDINSTITUCION,IDACREDITACION	)	 references 	FCS_HISTORICO_ACREDITACION	 (	IDFACTURACION,IDINSTITUCION,IDACREDITACION	) 		 Deferrable;
alter table 	FCS_HISTORICO_TIPOASISTCOLEGIO	 drop constraint 	FK_FCSHISTOTASISTCOLE_FACT	 ; 
alter table 	FCS_HISTORICO_TIPOASISTCOLEGIO	 add constraint 	FK_FCSHISTOTASISTCOLE_FACT	 foreign key (	IDFACTURACION,IDINSTITUCION	)	 references 	FCS_FACTURACIONJG	 (	IDFACTURACION,IDINSTITUCION	) 		 Deferrable;
alter table 	FCS_HISTORICO_TIPOACTUACION	 drop constraint 	FK_FCSHISTOTIPOACTUACION_FACT	 ; 
alter table 	FCS_HISTORICO_TIPOACTUACION	 add constraint 	FK_FCSHISTOTIPOACTUACION_FACT	 foreign key (	IDFACTURACION,IDINSTITUCION	)	 references 	FCS_FACTURACIONJG	 (	IDFACTURACION,IDINSTITUCION	) 		 Deferrable;
alter table 	FCS_HISTORICO_PROCEDIMIENTOS	 drop constraint 	FK_FCS_HISTORICO_PROC_FAC	 ; 
alter table 	FCS_HISTORICO_PROCEDIMIENTOS	 add constraint 	FK_FCS_HISTORICO_PROC_FAC	 foreign key (	IDFACTURACION,IDINSTITUCION	)	 references 	FCS_FACTURACIONJG	 (	IDFACTURACION,IDINSTITUCION	) 		 Deferrable;
alter table 	FCS_HISTORICO_HITOFACT	 drop constraint 	FK_FCSHISTHITOFACT_FACTURACION	 ; 
alter table 	FCS_HISTORICO_HITOFACT	 add constraint 	FK_FCSHISTHITOFACT_FACTURACION	 foreign key (	IDINSTITUCION,IDFACTURACION	)	 references 	FCS_FACTURACIONJG	 (	IDINSTITUCION,IDFACTURACION	) 		 Deferrable;
alter table 	FCS_HISTORICO_ACREDITACION	 drop constraint 	FK_FCS_HISTO_ACREDIT_FACT	 ; 
alter table 	FCS_HISTORICO_ACREDITACION	 add constraint 	FK_FCS_HISTO_ACREDIT_FACT	 foreign key (	IDINSTITUCION,IDFACTURACION	)	 references 	FCS_FACTURACIONJG	 (	IDINSTITUCION,IDFACTURACION	) 		 Deferrable;
alter table 	FCS_FACT_SOJ	 drop constraint 	FK_FACT_SOJ_SOJ	 ; 
alter table 	FCS_FACT_SOJ	 drop constraint 	FK_FACT_SOJ_FACTURACIONJG	 ; 
alter table 	FCS_FACT_SOJ	 add constraint 	FK_FACT_SOJ_SOJ	 foreign key (	NUMERO,IDINSTITUCION,IDTIPOSOJ,ANIO	)	 references 	SCS_SOJ	 (	NUMERO,IDINSTITUCION,IDTIPOSOJ,ANIO	) 		 Deferrable;
alter table 	FCS_FACT_SOJ	 add constraint 	FK_FACT_SOJ_FACTURACIONJG	 foreign key (	IDFACTURACION,IDINSTITUCION	)	 references 	FCS_FACTURACIONJG	 (	IDFACTURACION,IDINSTITUCION	) 	 on delete cascade	 Deferrable;
alter table 	FCS_FACT_GUARDIASCOLEGIADO	 drop constraint 	FK_FACT_GUARDIAS_APUNTE	 ; 
alter table 	FCS_FACT_GUARDIASCOLEGIADO	 drop constraint 	FK_FACT_GUARDIASCOLEGIADO_HITO	 ; 
alter table 	FCS_FACT_GUARDIASCOLEGIADO	 drop constraint 	FK_FACT_GUARDIASCOLEGIADO_GUAR	 ; 
alter table 	FCS_FACT_GUARDIASCOLEGIADO	 drop constraint 	FK_FACT_GUARDIASCOLEGIADO_FACT	 ; 
alter table 	FCS_FACT_GUARDIASCOLEGIADO	 add constraint 	FK_FACT_GUARDIAS_APUNTE	 foreign key (	IDAPUNTE,IDINSTITUCION,IDFACTURACION	)	 references 	FCS_FACT_APUNTE	 (	IDAPUNTE,IDINSTITUCION,IDFACTURACION	) 	 on delete cascade	 Deferrable;
alter table 	FCS_FACT_GUARDIASCOLEGIADO	 add constraint 	FK_FACT_GUARDIASCOLEGIADO_HITO	 foreign key (	IDHITO	)	 references 	SCS_HITOFACTURABLE	 (	IDHITO	) 		 Deferrable;
alter table 	FCS_FACT_GUARDIASCOLEGIADO	 add constraint 	FK_FACT_GUARDIASCOLEGIADO_GUAR	 foreign key (	IDPERSONA,IDINSTITUCION,IDTURNO,IDGUARDIA,FECHAINICIO,FECHAFIN	)	 references 	SCS_GUARDIASCOLEGIADO	 (	IDPERSONA,IDINSTITUCION,IDTURNO,IDGUARDIA,FECHAINICIO,FECHAFIN	) 		 Deferrable;
alter table 	FCS_FACT_GUARDIASCOLEGIADO	 add constraint 	FK_FACT_GUARDIASCOLEGIADO_FACT	 foreign key (	IDFACTURACION,IDINSTITUCION	)	 references 	FCS_FACTURACIONJG	 (	IDFACTURACION,IDINSTITUCION	) 	 on delete cascade	 Deferrable;
alter table 	FCS_FACT_GRUPOFACT_HITO	 drop constraint 	FK_FACT_GRUPOFACT_HITO_HITOGEN	 ; 
alter table 	FCS_FACT_GRUPOFACT_HITO	 drop constraint 	FK_FACT_GRUPOFACT_HITO_GRUPO	 ; 
alter table 	FCS_FACT_GRUPOFACT_HITO	 drop constraint 	FK_FACT_GRUPOFACT_HITO_FACT	 ; 
alter table 	FCS_FACT_GRUPOFACT_HITO	 add constraint 	FK_FACT_GRUPOFACT_HITO_HITOGEN	 foreign key (	IDHITOGENERAL	)	 references 	FCS_HITOGENERAL	 (	IDHITOGENERAL	) 		 Deferrable;
alter table 	FCS_FACT_GRUPOFACT_HITO	 add constraint 	FK_FACT_GRUPOFACT_HITO_GRUPO	 foreign key (	IDGRUPOFACTURACION,IDINSTITUCION	)	 references 	SCS_GRUPOFACTURACION	 (	IDGRUPOFACTURACION,IDINSTITUCION	) 		 Deferrable;
alter table 	FCS_FACT_GRUPOFACT_HITO	 add constraint 	FK_FACT_GRUPOFACT_HITO_FACT	 foreign key (	IDFACTURACION,IDINSTITUCION	)	 references 	FCS_FACTURACIONJG	 (	IDFACTURACION,IDINSTITUCION	) 	 on delete cascade	 Deferrable;
alter table 	FCS_FACT_ESTADOSFACTURACION	 drop constraint 	FK_FACT_ESTADOSFACT_FACTURAC	 ; 
alter table 	FCS_FACT_ESTADOSFACTURACION	 drop constraint 	FK_FACT_ESTADOSFACT_ESTADOS	 ; 
alter table 	FCS_FACT_ESTADOSFACTURACION	 add constraint 	FK_FACT_ESTADOSFACT_FACTURAC	 foreign key (	IDFACTURACION,IDINSTITUCION	)	 references 	FCS_FACTURACIONJG	 (	IDFACTURACION,IDINSTITUCION	) 	 on delete cascade	 Deferrable;
alter table 	FCS_FACT_ESTADOSFACTURACION	 add constraint 	FK_FACT_ESTADOSFACT_ESTADOS	 foreign key (	IDESTADOFACTURACION	)	 references 	FCS_ESTADOSFACTURACION	 (	IDESTADOFACTURACION	) 		 Deferrable;
alter table 	FCS_FACT_EJG	 drop constraint 	FK_FACT_EJG_FACTURACIONJG	 ; 
alter table 	FCS_FACT_EJG	 drop constraint 	FK_FACT_EJG_EJG	 ; 
alter table 	FCS_FACT_EJG	 add constraint 	FK_FACT_EJG_FACTURACIONJG	 foreign key (	IDFACTURACION,IDINSTITUCION	)	 references 	FCS_FACTURACIONJG	 (	IDFACTURACION,IDINSTITUCION	) 	 on delete cascade	 Deferrable;
alter table 	FCS_FACT_EJG	 add constraint 	FK_FACT_EJG_EJG	 foreign key (	NUMERO,IDINSTITUCION,IDTIPOEJG,ANIO	)	 references 	SCS_EJG	 (	NUMERO,IDINSTITUCION,IDTIPOEJG,ANIO	) 		 Deferrable;
alter table 	FCS_FACT_ASISTENCIA	 drop constraint 	FK_FCS_FACT_ASISTENCIA_HITO	 ; 
alter table 	FCS_FACT_ASISTENCIA	 drop constraint 	FK_FCS_FACT_ASISTENCIA_APUNTE	 ; 
alter table 	FCS_FACT_ASISTENCIA	 drop constraint 	FK_FACT_ASISTENCIA_ASISTENCIA	 ; 
alter table 	FCS_FACT_ASISTENCIA	 add constraint 	FK_FCS_FACT_ASISTENCIA_HITO	 foreign key (	IDHITO	)	 references 	SCS_HITOFACTURABLE	 (	IDHITO	) 		 Deferrable;
alter table 	FCS_FACT_ASISTENCIA	 add constraint 	FK_FCS_FACT_ASISTENCIA_APUNTE	 foreign key (	IDAPUNTE,IDINSTITUCION,IDFACTURACION	)	 references 	FCS_FACT_APUNTE	 (	IDAPUNTE,IDINSTITUCION,IDFACTURACION	) 	 on delete cascade	 Deferrable;
alter table 	FCS_FACT_ASISTENCIA	 add constraint 	FK_FACT_ASISTENCIA_ASISTENCIA	 foreign key (	NUMERO,IDINSTITUCION,ANIO	)	 references 	SCS_ASISTENCIA	 (	NUMERO,IDINSTITUCION,ANIO	) 		 Deferrable;
alter table 	FCS_FACT_APUNTE	 drop constraint 	FK_FCS_FACT_APUNTE_TIPOAPUNTE	 ; 
alter table 	FCS_FACT_APUNTE	 drop constraint 	FK_FCS_FACT_APUNTE_HITOFACTURA	 ; 
alter table 	FCS_FACT_APUNTE	 drop constraint 	FK_FCS_FACT_APUNTE_FACTURACION	 ; 
alter table 	FCS_FACT_APUNTE	 drop constraint 	FK_FCS_FACT_APUNTE_CABECERA	 ; 
alter table 	FCS_FACT_APUNTE	 add constraint 	FK_FCS_FACT_APUNTE_TIPOAPUNTE	 foreign key (	IDTIPOAPUNTE	)	 references 	FCS_TIPOAPUNTES	 (	IDTIPOAPUNTE	) 		 Deferrable;
alter table 	FCS_FACT_APUNTE	 add constraint 	FK_FCS_FACT_APUNTE_HITOFACTURA	 foreign key (	IDHITO	)	 references 	SCS_HITOFACTURABLE	 (	IDHITO	) 		 Deferrable;
alter table 	FCS_FACT_APUNTE	 add constraint 	FK_FCS_FACT_APUNTE_FACTURACION	 foreign key (	IDFACTURACION,IDINSTITUCION	)	 references 	FCS_FACTURACIONJG	 (	IDFACTURACION,IDINSTITUCION	) 	 on delete cascade	 Deferrable;
alter table 	FCS_FACT_APUNTE	 add constraint 	FK_FCS_FACT_APUNTE_CABECERA	 foreign key (	FECHAINICIO,IDINSTITUCION,IDPERSONA,IDTURNO,IDGUARDIA	)	 references 	SCS_CABECERAGUARDIAS	 (	FECHAINICIO,IDINSTITUCION,IDPERSONA,IDTURNO,IDGUARDIA	) 		 Deferrable;
alter table 	FCS_FACT_ACTUACIONDESIGNA	 drop constraint 	FK_FACT_ACTUACIONDESIGNA_FACT	 ; 
alter table 	FCS_FACT_ACTUACIONDESIGNA	 drop constraint 	FK_FACT_ACTUACIONDESIGNA_COLEG	 ; 
alter table 	FCS_FACT_ACTUACIONDESIGNA	 drop constraint 	FK_FACT_ACTUACIONDESIGNA_ACTUA	 ; 
alter table 	FCS_FACT_ACTUACIONDESIGNA	 add constraint 	FK_FACT_ACTUACIONDESIGNA_FACT	 foreign key (	IDFACTURACION,IDINSTITUCION	)	 references 	FCS_FACTURACIONJG	 (	IDFACTURACION,IDINSTITUCION	) 	 on delete cascade	 Deferrable;
alter table 	FCS_FACT_ACTUACIONDESIGNA	 add constraint 	FK_FACT_ACTUACIONDESIGNA_COLEG	 foreign key (	IDPERSONA,IDINSTITUCION	)	 references 	CEN_COLEGIADO	 (	IDPERSONA,IDINSTITUCION	) 		 Deferrable;
alter table 	FCS_FACT_ACTUACIONDESIGNA	 add constraint 	FK_FACT_ACTUACIONDESIGNA_ACTUA	 foreign key (	IDTURNO,IDINSTITUCION,NUMEROASUNTO,NUMERO,ANIO	)	 references 	SCS_ACTUACIONDESIGNA	 (	IDTURNO,IDINSTITUCION,NUMEROASUNTO,NUMERO,ANIO	) 		 Deferrable;
alter table 	FCS_FACT_ACTUACIONASISTENCIA	 drop constraint 	FK_FCS_FACT_ACTUACIONASIST_APU	 ; 
alter table 	FCS_FACT_ACTUACIONASISTENCIA	 drop constraint 	FK_FACT_ACTUACIONASISTENCIA_HI	 ; 
alter table 	FCS_FACT_ACTUACIONASISTENCIA	 drop constraint 	FK_FACT_ACTUACIONASISTENCIA_AC	 ; 
alter table 	FCS_FACT_ACTUACIONASISTENCIA	 add constraint 	FK_FCS_FACT_ACTUACIONASIST_APU	 foreign key (	IDAPUNTE,IDINSTITUCION,IDFACTURACION	)	 references 	FCS_FACT_APUNTE	 (	IDAPUNTE,IDINSTITUCION,IDFACTURACION	) 	 on delete cascade	 Deferrable;
alter table 	FCS_FACT_ACTUACIONASISTENCIA	 add constraint 	FK_FACT_ACTUACIONASISTENCIA_HI	 foreign key (	IDHITO	)	 references 	SCS_HITOFACTURABLE	 (	IDHITO	) 		 Deferrable;
alter table 	FCS_FACT_ACTUACIONASISTENCIA	 add constraint 	FK_FACT_ACTUACIONASISTENCIA_AC	 foreign key (	NUMERO,IDINSTITUCION,IDACTUACION,ANIO	)	 references 	SCS_ACTUACIONASISTENCIA	 (	NUMERO,IDINSTITUCION,IDACTUACION,ANIO	) 		 Deferrable;
alter table 	FCS_FACTURACION_ESTADO_ENVIO	 drop constraint 	FK_FCS_FACTURACI_FCS_MAESTRO_E	 ; 
alter table 	FCS_FACTURACION_ESTADO_ENVIO	 drop constraint 	FK_FCS_FACTURACI_FCS_FACTURACI	 ; 
alter table 	FCS_FACTURACION_ESTADO_ENVIO	 add constraint 	FK_FCS_FACTURACI_FCS_MAESTRO_E	 foreign key (	IDESTADOENVIOFACTURACION	)	 references 	FCS_MAESTROESTADOS_ENVIO_FACT	 (	IDESTADOENVIOFACTURACION	) 		 Deferrable;
alter table 	FCS_FACTURACION_ESTADO_ENVIO	 add constraint 	FK_FCS_FACTURACI_FCS_FACTURACI	 foreign key (	IDINSTITUCION,IDFACTURACION	)	 references 	FCS_FACTURACIONJG	 (	IDINSTITUCION,IDFACTURACION	) 		 Deferrable;
alter table 	FCS_FACTURACIONJG	 drop constraint 	FK_FCS_FACTURACIONJG_REGULARIZ	 ; 
alter table 	FCS_FACTURACIONJG	 add constraint 	FK_FCS_FACTURACIONJG_REGULARIZ	 foreign key (	IDFACTURACION_REGULARIZA,IDINSTITUCION	)	 references 	FCS_FACTURACIONJG	 (	IDFACTURACION,IDINSTITUCION	) 		 Deferrable;
alter table 	FCS_CONF_IMPRESO190	 drop constraint 	FK_FCS_IMPRESO190_PROVINCIA	 ; 
alter table 	FCS_CONF_IMPRESO190	 add constraint 	FK_FCS_IMPRESO190_PROVINCIA	 foreign key (	IDPROVINCIA	)	 references 	CEN_PROVINCIAS	 (	IDPROVINCIA	) 		 Deferrable;
alter table 	FCS_COBROS_RETENCIONJUDICIAL	 drop constraint 	FK_COBROS_RET_PAGOCOLEGIADO	 ; 
alter table 	FCS_COBROS_RETENCIONJUDICIAL	 drop constraint 	FK_COBROS_RETENCIONES_RETENCIO	 ; 
alter table 	FCS_COBROS_RETENCIONJUDICIAL	 add constraint 	FK_COBROS_RET_PAGOCOLEGIADO	 foreign key (	IDPAGOSJG,IDINSTITUCION,IDPERSONA	)	 references 	FCS_PAGO_COLEGIADO	 (	IDPAGOSJG,IDINSTITUCION,IDPERORIGEN	) 		 Deferrable;
alter table 	FCS_COBROS_RETENCIONJUDICIAL	 add constraint 	FK_COBROS_RETENCIONES_RETENCIO	 foreign key (	IDRETENCION,IDINSTITUCION	)	 references 	FCS_RETENCIONES_JUDICIALES	 (	IDRETENCION,IDINSTITUCION	) 		 Deferrable;
alter table 	FCS_APLICA_MOVIMIENTOSVARIOS	 drop constraint 	FK_APLICA_MOVIMIENTOS_PAGOS	 ; 
alter table 	FCS_APLICA_MOVIMIENTOSVARIOS	 drop constraint 	FK_APLICA_MOVIMIENTOS_MOVIM	 ; 
alter table 	FCS_APLICA_MOVIMIENTOSVARIOS	 add constraint 	FK_APLICA_MOVIMIENTOS_PAGOS	 foreign key (	IDPAGOSJG,IDINSTITUCION,IDPERSONA	)	 references 	FCS_PAGO_COLEGIADO	 (	IDPAGOSJG,IDINSTITUCION,IDPERORIGEN	) 		 Deferrable;
alter table 	FCS_APLICA_MOVIMIENTOSVARIOS	 add constraint 	FK_APLICA_MOVIMIENTOS_MOVIM	 foreign key (	IDMOVIMIENTO,IDINSTITUCION	)	 references 	FCS_MOVIMIENTOSVARIOS	 (	IDMOVIMIENTO,IDINSTITUCION	) 		 Deferrable;
alter table 	FAC_TIPOSSERVINCLSENFACT	 drop constraint 	FK_TIPOSSERVINCLSENFACT_SERVI	 ; 
alter table 	FAC_TIPOSSERVINCLSENFACT	 drop constraint 	FK_FAC_TIPOSSERVINCLSENFACT_SE	 ; 
alter table 	FAC_TIPOSSERVINCLSENFACT	 add constraint 	FK_TIPOSSERVINCLSENFACT_SERVI	 foreign key (	IDSERVICIO,IDINSTITUCION,IDTIPOSERVICIOS	)	 references 	PYS_SERVICIOS	 (	IDSERVICIO,IDINSTITUCION,IDTIPOSERVICIOS	) 		 Deferrable;
alter table 	FAC_TIPOSSERVINCLSENFACT	 add constraint 	FK_FAC_TIPOSSERVINCLSENFACT_SE	 foreign key (	IDSERIEFACTURACION,IDINSTITUCION	)	 references 	FAC_SERIEFACTURACION	 (	IDSERIEFACTURACION,IDINSTITUCION	) 		 Deferrable;
alter table 	FAC_TIPOSPRODUINCLUENFACTU	 drop constraint 	FK_TIPOSPRODUINCLUENFAC_PRODUC	 ; 
alter table 	FAC_TIPOSPRODUINCLUENFACTU	 drop constraint 	FK_FAC_TIPOSPRODUINCLUFACT_SER	 ; 
alter table 	FAC_TIPOSPRODUINCLUENFACTU	 add constraint 	FK_TIPOSPRODUINCLUENFAC_PRODUC	 foreign key (	IDPRODUCTO,IDINSTITUCION,IDTIPOPRODUCTO	)	 references 	PYS_PRODUCTOS	 (	IDPRODUCTO,IDINSTITUCION,IDTIPOPRODUCTO	) 		 Deferrable;
alter table 	FAC_TIPOSPRODUINCLUENFACTU	 add constraint 	FK_FAC_TIPOSPRODUINCLUFACT_SER	 foreign key (	IDSERIEFACTURACION,IDINSTITUCION	)	 references 	FAC_SERIEFACTURACION	 (	IDSERIEFACTURACION,IDINSTITUCION	) 		 Deferrable;
alter table 	FAC_TIPOCLIINCLUIDOENSERIEFAC	 drop constraint 	FK_FAC_TIPOCLIINCLUERIEF_SERIE	 ; 
alter table 	FAC_TIPOCLIINCLUIDOENSERIEFAC	 drop constraint 	FK_FAC_TIPOCLIINCLUERIEF_GRUPO	 ; 
alter table 	FAC_TIPOCLIINCLUIDOENSERIEFAC	 add constraint 	FK_FAC_TIPOCLIINCLUERIEF_SERIE	 foreign key (	IDSERIEFACTURACION,IDINSTITUCION	)	 references 	FAC_SERIEFACTURACION	 (	IDSERIEFACTURACION,IDINSTITUCION	) 		 Deferrable;
alter table 	FAC_TIPOCLIINCLUIDOENSERIEFAC	 add constraint 	FK_FAC_TIPOCLIINCLUERIEF_GRUPO	 foreign key (	IDINSTITUCION_GRUPO,IDGRUPO	)	 references 	CEN_GRUPOSCLIENTE	 (	IDINSTITUCION,IDGRUPO	) 		 Deferrable;
alter table 	FAC_SUFIJO	 drop constraint 	FK_FAC_SUFIJO_INSTITUCION	 ; 
alter table 	FAC_SUFIJO	 add constraint 	FK_FAC_SUFIJO_INSTITUCION	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 	 on delete cascade	 Deferrable;
alter table 	FAC_SERIEFACTURACION_BANCO	 drop constraint 	FK_FAC_SERIEFACTURACION	 ; 
alter table 	FAC_SERIEFACTURACION_BANCO	 drop constraint 	FK_FAC_SERIEBANCO_FAC_SUFIJO	 ; 
alter table 	FAC_SERIEFACTURACION_BANCO	 drop constraint 	FK_FAC_BANCOINSTITUCION	 ; 
alter table 	FAC_SERIEFACTURACION_BANCO	 add constraint 	FK_FAC_SERIEFACTURACION	 foreign key (	IDSERIEFACTURACION,IDINSTITUCION	)	 references 	FAC_SERIEFACTURACION	 (	IDSERIEFACTURACION,IDINSTITUCION	) 	 on delete cascade	 Deferrable;
alter table 	FAC_SERIEFACTURACION_BANCO	 add constraint 	FK_FAC_SERIEBANCO_FAC_SUFIJO	 foreign key (	IDSUFIJO,IDINSTITUCION	)	 references 	FAC_SUFIJO	 (	IDSUFIJO,IDINSTITUCION	) 		 Deferrable;
alter table 	FAC_SERIEFACTURACION_BANCO	 add constraint 	FK_FAC_BANCOINSTITUCION	 foreign key (	BANCOS_CODIGO,IDINSTITUCION	)	 references 	FAC_BANCOINSTITUCION	 (	BANCOS_CODIGO,IDINSTITUCION	) 		 Deferrable;
alter table 	FAC_SERIEFACTURACION	 drop constraint 	FK_SERIEFACTURACION_PLANTILLAF	 ; 
alter table 	FAC_SERIEFACTURACION	 drop constraint 	FK_SERIEFACTURACION_INSTITUCIO	 ; 
alter table 	FAC_SERIEFACTURACION	 drop constraint 	FK_SERIEFACTURACION_ENVPLANT	 ; 
alter table 	FAC_SERIEFACTURACION	 drop constraint 	FK_ADM_CONTADOR	 ; 
alter table 	FAC_SERIEFACTURACION	 add constraint 	FK_SERIEFACTURACION_PLANTILLAF	 foreign key (	IDPLANTILLA,IDINSTITUCION	)	 references 	FAC_PLANTILLAFACTURACION	 (	IDPLANTILLA,IDINSTITUCION	) 		 Deferrable;
alter table 	FAC_SERIEFACTURACION	 add constraint 	FK_SERIEFACTURACION_INSTITUCIO	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	FAC_SERIEFACTURACION	 add constraint 	FK_SERIEFACTURACION_ENVPLANT	 foreign key (	IDTIPOENVIOS,IDINSTITUCION,IDTIPOPLANTILLAMAIL	)	 references 	ENV_PLANTILLASENVIOS	 (	IDTIPOENVIOS,IDINSTITUCION,IDPLANTILLAENVIOS	) 		 Deferrable;
alter table 	FAC_SERIEFACTURACION	 add constraint 	FK_ADM_CONTADOR	 foreign key (	IDCONTADOR,IDINSTITUCION	)	 references 	ADM_CONTADOR	 (	IDCONTADOR,IDINSTITUCION	) 		 Deferrable;
alter table 	FAC_RENEGOCIACION	 drop constraint 	FK_FAC_RENEGOCIACION_FACTURA	 ; 
alter table 	FAC_RENEGOCIACION	 drop constraint 	FK_FAC_RENEGOCIACION_CUENTASBA	 ; 
alter table 	FAC_RENEGOCIACION	 add constraint 	FK_FAC_RENEGOCIACION_FACTURA	 foreign key (	IDFACTURA,IDINSTITUCION	)	 references 	FAC_FACTURA	 (	IDFACTURA,IDINSTITUCION	) 		 Deferrable;
alter table 	FAC_RENEGOCIACION	 add constraint 	FK_FAC_RENEGOCIACION_CUENTASBA	 foreign key (	IDCUENTA,IDINSTITUCION,IDPERSONA	)	 references 	CEN_CUENTASBANCARIAS	 (	IDCUENTA,IDINSTITUCION,IDPERSONA	) 		 Deferrable;
alter table 	FAC_REGISTROFICHCONTA	 drop constraint 	FK_REGISTROFICHCONTA_ESTADO	 ; 
alter table 	FAC_REGISTROFICHCONTA	 add constraint 	FK_REGISTROFICHCONTA_ESTADO	 foreign key (	ESTADO	)	 references 	FAC_ESTADOCONTAB	 (	IDESTADO	) 		 Deferrable;
alter table 	FAC_PREVISIONFACTURACION	 drop constraint 	FK_FAC_PREVISIONFACTURACION_SE	 ; 
alter table 	FAC_PREVISIONFACTURACION	 add constraint 	FK_FAC_PREVISIONFACTURACION_SE	 foreign key (	IDSERIEFACTURACION,IDINSTITUCION	)	 references 	FAC_SERIEFACTURACION	 (	IDSERIEFACTURACION,IDINSTITUCION	) 		 Deferrable;
alter table 	FAC_PLANTILLAFACTURACION	 drop constraint 	FPN_CIN_FK	 ; 
alter table 	FAC_PLANTILLAFACTURACION	 add constraint 	FPN_CIN_FK	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	FAC_PAGOSPORCAJA	 drop constraint 	FK_FAC_PAGOSPORCAJA_FACTURA	 ; 
alter table 	FAC_PAGOSPORCAJA	 drop constraint 	FK_COMPENSACION_FACTURAS_ABONO	 ; 
alter table 	FAC_PAGOSPORCAJA	 add constraint 	FK_FAC_PAGOSPORCAJA_FACTURA	 foreign key (	IDFACTURA,IDINSTITUCION	)	 references 	FAC_FACTURA	 (	IDFACTURA,IDINSTITUCION	) 		 Deferrable;
alter table 	FAC_PAGOSPORCAJA	 add constraint 	FK_COMPENSACION_FACTURAS_ABONO	 foreign key (	IDPAGOABONO,IDINSTITUCION,IDABONO	)	 references 	FAC_PAGOABONOEFECTIVO	 (	IDPAGOABONO,IDINSTITUCION,IDABONO	) 		 Deferrable;
alter table 	FAC_PAGOABONOEFECTIVO	 drop constraint 	FK_FAC_PAGOABONOEFECTIVO_ABONO	 ; 
alter table 	FAC_PAGOABONOEFECTIVO	 add constraint 	FK_FAC_PAGOABONOEFECTIVO_ABONO	 foreign key (	IDABONO,IDINSTITUCION	)	 references 	FAC_ABONO	 (	IDABONO,IDINSTITUCION	) 		 Deferrable;
alter table 	FAC_LINEAFACTURA	 drop constraint 	FK_LINEAFACTURA_TIPOIVA	 ; 
alter table 	FAC_LINEAFACTURA	 drop constraint 	FK_LINEAFACTURA_FORMAPAGO	 ; 
alter table 	FAC_LINEAFACTURA	 drop constraint 	FK_FAC_LINEAFACTURA_FACTURA	 ; 
alter table 	FAC_LINEAFACTURA	 add constraint 	FK_LINEAFACTURA_TIPOIVA	 foreign key (	IDTIPOIVA	)	 references 	PYS_TIPOIVA	 (	IDTIPOIVA	) 		 Deferrable;
alter table 	FAC_LINEAFACTURA	 add constraint 	FK_LINEAFACTURA_FORMAPAGO	 foreign key (	IDFORMAPAGO	)	 references 	PYS_FORMAPAGO	 (	IDFORMAPAGO	) 		 Deferrable;
alter table 	FAC_LINEAFACTURA	 add constraint 	FK_FAC_LINEAFACTURA_FACTURA	 foreign key (	IDFACTURA,IDINSTITUCION	)	 references 	FAC_FACTURA	 (	IDFACTURA,IDINSTITUCION	) 		 Deferrable;
alter table 	FAC_LINEADEVOLUDISQBANCO	 drop constraint 	FK_FAC_LINEADEVOLDISQBANCO_FAC	 ; 
alter table 	FAC_LINEADEVOLUDISQBANCO	 drop constraint 	FK_FAC_LINEADEVOLDISQBANCO_DIS	 ; 
alter table 	FAC_LINEADEVOLUDISQBANCO	 add constraint 	FK_FAC_LINEADEVOLDISQBANCO_FAC	 foreign key (	IDFACTURAINCLUIDAENDISQUETE,IDINSTITUCION,IDDISQUETECARGOS	)	 references 	FAC_FACTURAINCLUIDAENDISQUETE	 (	IDFACTURAINCLUIDAENDISQUETE,IDINSTITUCION,IDDISQUETECARGOS	) 		 Deferrable;
alter table 	FAC_LINEADEVOLUDISQBANCO	 add constraint 	FK_FAC_LINEADEVOLDISQBANCO_DIS	 foreign key (	IDDISQUETEDEVOLUCIONES,IDINSTITUCION	)	 references 	FAC_DISQUETEDEVOLUCIONES	 (	IDDISQUETEDEVOLUCIONES,IDINSTITUCION	) 		 Deferrable;
alter table 	FAC_LINEAABONO	 drop constraint 	FK_FAC_LINEAFACTURA	 ; 
alter table 	FAC_LINEAABONO	 drop constraint 	FK_ABONO_LINEAS	 ; 
alter table 	FAC_LINEAABONO	 add constraint 	FK_FAC_LINEAFACTURA	 foreign key (	LINEAFACTURA,IDINSTITUCION,IDFACTURA	)	 references 	FAC_LINEAFACTURA	 (	NUMEROLINEA,IDINSTITUCION,IDFACTURA	) 		 Deferrable;
alter table 	FAC_LINEAABONO	 add constraint 	FK_ABONO_LINEAS	 foreign key (	IDABONO,IDINSTITUCION	)	 references 	FAC_ABONO	 (	IDABONO,IDINSTITUCION	) 		 Deferrable;
alter table 	FAC_GRUPCRITINCLUIDOSENSERIE	 drop constraint 	FK_FAC_GRUCRITINCLUIDENSE_SERI	 ; 
alter table 	FAC_GRUPCRITINCLUIDOSENSERIE	 drop constraint 	FK_FAC_GRUCRITINCLUIDENSE_GRUP	 ; 
alter table 	FAC_GRUPCRITINCLUIDOSENSERIE	 add constraint 	FK_FAC_GRUCRITINCLUIDENSE_SERI	 foreign key (	IDSERIEFACTURACION,IDINSTITUCION	)	 references 	FAC_SERIEFACTURACION	 (	IDSERIEFACTURACION,IDINSTITUCION	) 		 Deferrable;
alter table 	FAC_GRUPCRITINCLUIDOSENSERIE	 add constraint 	FK_FAC_GRUCRITINCLUIDENSE_GRUP	 foreign key (	IDINSTITUCION_GRUP,IDGRUPOSCRITERIOS	)	 references 	CEN_GRUPOSCRITERIOS	 (	IDINSTITUCION,IDGRUPOSCRITERIOS	) 		 Deferrable;
alter table 	FAC_FORMAPAGOSERIE	 drop constraint 	FK_FORMAPAGOSERIE_SERIE	 ; 
alter table 	FAC_FORMAPAGOSERIE	 drop constraint 	FK_FORMAPAGOSERIE_FORMAPAGO	 ; 
alter table 	FAC_FORMAPAGOSERIE	 add constraint 	FK_FORMAPAGOSERIE_SERIE	 foreign key (	IDSERIEFACTURACION,IDINSTITUCION	)	 references 	FAC_SERIEFACTURACION	 (	IDSERIEFACTURACION,IDINSTITUCION	) 	 on delete cascade	 Deferrable;
alter table 	FAC_FORMAPAGOSERIE	 add constraint 	FK_FORMAPAGOSERIE_FORMAPAGO	 foreign key (	IDFORMAPAGO	)	 references 	PYS_FORMAPAGO	 (	IDFORMAPAGO	) 		 Deferrable;
alter table 	FAC_FACTURAINCLUIDAENDISQUETE	 drop constraint 	FK_FAC_FACTINCLUIDNDISQUE_FACT	 ; 
alter table 	FAC_FACTURAINCLUIDAENDISQUETE	 drop constraint 	FK_FAC_FACTINCLUIDNDISQUE_DISQ	 ; 
alter table 	FAC_FACTURAINCLUIDAENDISQUETE	 drop constraint 	FK_FACINCLUIDADISQUETE_CUENTAS	 ; 
alter table 	FAC_FACTURAINCLUIDAENDISQUETE	 drop constraint 	FFE_FRN_FK	 ; 
alter table 	FAC_FACTURAINCLUIDAENDISQUETE	 add constraint 	FK_FAC_FACTINCLUIDNDISQUE_FACT	 foreign key (	IDFACTURA,IDINSTITUCION	)	 references 	FAC_FACTURA	 (	IDFACTURA,IDINSTITUCION	) 		 Deferrable;
alter table 	FAC_FACTURAINCLUIDAENDISQUETE	 add constraint 	FK_FAC_FACTINCLUIDNDISQUE_DISQ	 foreign key (	IDDISQUETECARGOS,IDINSTITUCION	)	 references 	FAC_DISQUETECARGOS	 (	IDDISQUETECARGOS,IDINSTITUCION	) 		 Deferrable;
alter table 	FAC_FACTURAINCLUIDAENDISQUETE	 add constraint 	FK_FACINCLUIDADISQUETE_CUENTAS	 foreign key (	IDCUENTA,IDINSTITUCION,IDPERSONA	)	 references 	CEN_CUENTASBANCARIAS	 (	IDCUENTA,IDINSTITUCION,IDPERSONA	) 		 Deferrable;
alter table 	FAC_FACTURAINCLUIDAENDISQUETE	 add constraint 	FFE_FRN_FK	 foreign key (	IDRENEGOCIACION,IDINSTITUCION,IDFACTURA	)	 references 	FAC_RENEGOCIACION	 (	IDRENEGOCIACION,IDINSTITUCION,IDFACTURA	) 		 Deferrable;
alter table 	FAC_FACTURACIONSUSCRIPCION	 drop constraint 	FACSUSCR_PSN_FK	 ; 
alter table 	FAC_FACTURACIONSUSCRIPCION	 drop constraint 	FACSUSCR_FLA_FK	 ; 
alter table 	FAC_FACTURACIONSUSCRIPCION	 add constraint 	FACSUSCR_PSN_FK	 foreign key (	IDSUSCRIPCION,IDINSTITUCION,IDTIPOSERVICIOS,IDSERVICIO,IDSERVICIOSINSTITUCION	)	 references 	PYS_SUSCRIPCION	 (	IDSUSCRIPCION,IDINSTITUCION,IDTIPOSERVICIOS,IDSERVICIO,IDSERVICIOSINSTITUCION	) 		 Deferrable;
alter table 	FAC_FACTURACIONSUSCRIPCION	 add constraint 	FACSUSCR_FLA_FK	 foreign key (	NUMEROLINEA,IDINSTITUCION,IDFACTURA	)	 references 	FAC_LINEAFACTURA	 (	NUMEROLINEA,IDINSTITUCION,IDFACTURA	) 		 Deferrable;
alter table 	FAC_FACTURACIONPROGRAMADA	 drop constraint 	FK_FAC_FACTURACIONPROGR_SERIE	 ; 
alter table 	FAC_FACTURACIONPROGRAMADA	 drop constraint 	FK_FAC_FACTURACIONPROGR_PREV	 ; 
alter table 	FAC_FACTURACIONPROGRAMADA	 drop constraint 	FK_FAC_FACTURACIONPROGR_INSTIT	 ; 
alter table 	FAC_FACTURACIONPROGRAMADA	 drop constraint 	FK_FAC_FACTPROGR_ENVPLANT	 ; 
alter table 	FAC_FACTURACIONPROGRAMADA	 drop constraint 	FK_FAC_ESTADOCONFIRM_PDF	 ; 
alter table 	FAC_FACTURACIONPROGRAMADA	 drop constraint 	FK_FAC_ESTADOCONFIRM_ENV	 ; 
alter table 	FAC_FACTURACIONPROGRAMADA	 drop constraint 	FK_FAC_ESTADOCONFIRM_CON	 ; 
alter table 	FAC_FACTURACIONPROGRAMADA	 add constraint 	FK_FAC_FACTURACIONPROGR_SERIE	 foreign key (	IDSERIEFACTURACION,IDINSTITUCION	)	 references 	FAC_SERIEFACTURACION	 (	IDSERIEFACTURACION,IDINSTITUCION	) 		 Deferrable;
alter table 	FAC_FACTURACIONPROGRAMADA	 add constraint 	FK_FAC_FACTURACIONPROGR_PREV	 foreign key (	IDPREVISION,IDINSTITUCION,IDSERIEFACTURACION	)	 references 	FAC_PREVISIONFACTURACION	 (	IDPREVISION,IDINSTITUCION,IDSERIEFACTURACION	) 		 Deferrable;
alter table 	FAC_FACTURACIONPROGRAMADA	 add constraint 	FK_FAC_FACTURACIONPROGR_INSTIT	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	FAC_FACTURACIONPROGRAMADA	 add constraint 	FK_FAC_FACTPROGR_ENVPLANT	 foreign key (	IDTIPOENVIOS,IDINSTITUCION,IDTIPOPLANTILLAMAIL	)	 references 	ENV_PLANTILLASENVIOS	 (	IDTIPOENVIOS,IDINSTITUCION,IDPLANTILLAENVIOS	) 		 Deferrable;
alter table 	FAC_FACTURACIONPROGRAMADA	 add constraint 	FK_FAC_ESTADOCONFIRM_PDF	 foreign key (	IDESTADOPDF	)	 references 	FAC_ESTADOCONFIRMFACT	 (	IDESTADO	) 		 Deferrable;
alter table 	FAC_FACTURACIONPROGRAMADA	 add constraint 	FK_FAC_ESTADOCONFIRM_ENV	 foreign key (	IDESTADOENVIO	)	 references 	FAC_ESTADOCONFIRMFACT	 (	IDESTADO	) 		 Deferrable;
alter table 	FAC_FACTURACIONPROGRAMADA	 add constraint 	FK_FAC_ESTADOCONFIRM_CON	 foreign key (	IDESTADOCONFIRMACION	)	 references 	FAC_ESTADOCONFIRMFACT	 (	IDESTADO	) 		 Deferrable;
alter table 	FAC_FACTURA	 drop constraint 	FK_FAC_FACTURA_INSTITUCION	 ; 
alter table 	FAC_FACTURA	 drop constraint 	FK_FAC_FACTURA_FORMAPAGO	 ; 
alter table 	FAC_FACTURA	 drop constraint 	FK_FAC_FACTURA_FACTURACION	 ; 
alter table 	FAC_FACTURA	 drop constraint 	FK_FAC_FACTURA_CUENTASBANCARIA	 ; 
alter table 	FAC_FACTURA	 drop constraint 	FK_FAC_FACTURA_CLIENTE	 ; 
alter table 	FAC_FACTURA	 drop constraint 	FK_FAC_ESTADOFACTURA	 ; 
alter table 	FAC_FACTURA	 drop constraint 	FK_FACTURA_MANDATO_DEUDOR	 ; 
alter table 	FAC_FACTURA	 drop constraint 	FK_FACTURA_MANDATO	 ; 
alter table 	FAC_FACTURA	 drop constraint 	FK_FACTURA_COMISION	 ; 
alter table 	FAC_FACTURA	 drop constraint 	FK_CEN_PERSONA_FAC_FACTURA	 ; 
alter table 	FAC_FACTURA	 add constraint 	FK_FAC_FACTURA_INSTITUCION	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	FAC_FACTURA	 add constraint 	FK_FAC_FACTURA_FORMAPAGO	 foreign key (	IDFORMAPAGO	)	 references 	PYS_FORMAPAGO	 (	IDFORMAPAGO	) 		 Deferrable;
alter table 	FAC_FACTURA	 add constraint 	FK_FAC_FACTURA_FACTURACION	 foreign key (	IDPROGRAMACION,IDINSTITUCION,IDSERIEFACTURACION	)	 references 	FAC_FACTURACIONPROGRAMADA	 (	IDPROGRAMACION,IDINSTITUCION,IDSERIEFACTURACION	) 		 Deferrable;
alter table 	FAC_FACTURA	 add constraint 	FK_FAC_FACTURA_CUENTASBANCARIA	 foreign key (	IDCUENTA,IDINSTITUCION,IDPERSONA	)	 references 	CEN_CUENTASBANCARIAS	 (	IDCUENTA,IDINSTITUCION,IDPERSONA	) 		 Deferrable;
alter table 	FAC_FACTURA	 add constraint 	FK_FAC_FACTURA_CLIENTE	 foreign key (	IDPERSONA,IDINSTITUCION	)	 references 	CEN_CLIENTE	 (	IDPERSONA,IDINSTITUCION	) 		 Deferrable;
alter table 	FAC_FACTURA	 add constraint 	FK_FAC_ESTADOFACTURA	 foreign key (	ESTADO	)	 references 	FAC_ESTADOFACTURA	 (	IDESTADO	) 		 Deferrable;
alter table 	FAC_FACTURA	 add constraint 	FK_FACTURA_MANDATO_DEUDOR	 foreign key (	IDMANDATO,IDINSTITUCION,IDPERSONADEUDOR,IDCUENTADEUDOR	)	 references 	CEN_MANDATOS_CUENTASBANCARIAS	 (	IDMANDATO,IDINSTITUCION,IDPERSONA,IDCUENTA	) 		 Deferrable;
alter table 	FAC_FACTURA	 add constraint 	FK_FACTURA_MANDATO	 foreign key (	IDMANDATO,IDINSTITUCION,IDPERSONA,IDCUENTA	)	 references 	CEN_MANDATOS_CUENTASBANCARIAS	 (	IDMANDATO,IDINSTITUCION,IDPERSONA,IDCUENTA	) 		 Deferrable;
alter table 	FAC_FACTURA	 add constraint 	FK_FACTURA_COMISION	 foreign key (	COMISIONIDFACTURA,IDINSTITUCION	)	 references 	FAC_FACTURA	 (	IDFACTURA,IDINSTITUCION	) 		 Deferrable;
alter table 	FAC_FACTURA	 add constraint 	FK_CEN_PERSONA_FAC_FACTURA	 foreign key (	IDPERSONA	)	 references 	CEN_PERSONA	 (	IDPERSONA	) 		 Deferrable;
alter table 	FAC_ESTADOFACTURA	 drop constraint 	FK_GEN_RECURSOS_ESTADOFAC	 ; 
alter table 	FAC_ESTADOFACTURA	 add constraint 	FK_GEN_RECURSOS_ESTADOFAC	 foreign key (	LENGUAJE,DESCRIPCION	)	 references 	GEN_RECURSOS	 (	IDLENGUAJE,IDRECURSO	) 		 Deferrable;
alter table 	FAC_ESTADOCONTAB	 drop constraint 	FK_GEN_RECURSOS_ESTADOCONTAB	 ; 
alter table 	FAC_ESTADOCONTAB	 add constraint 	FK_GEN_RECURSOS_ESTADOCONTAB	 foreign key (	LENGUAJE,DESCRIPCION	)	 references 	GEN_RECURSOS	 (	IDLENGUAJE,IDRECURSO	) 		 Deferrable;
alter table 	FAC_ESTADOCONFIRMFACT	 drop constraint 	FK_GEN_RECURSOS_ESTADO	 ; 
alter table 	FAC_ESTADOCONFIRMFACT	 add constraint 	FK_GEN_RECURSOS_ESTADO	 foreign key (	LENGUAJE,DESCRIPCION	)	 references 	GEN_RECURSOS	 (	IDLENGUAJE,IDRECURSO	) 		 Deferrable;
alter table 	FAC_ESTADOABONO	 drop constraint 	FK_GEN_RECURSOS_ESTADOABO	 ; 
alter table 	FAC_ESTADOABONO	 add constraint 	FK_GEN_RECURSOS_ESTADOABO	 foreign key (	LENGUAJE,DESCRIPCION	)	 references 	GEN_RECURSOS	 (	IDLENGUAJE,IDRECURSO	) 		 Deferrable;
alter table 	FAC_DISQUETEDEVOLUCIONES	 drop constraint 	FK_FAC_DISDEVOLUCIONES_INSTITU	 ; 
alter table 	FAC_DISQUETEDEVOLUCIONES	 drop constraint 	FDE_FBN_FK	 ; 
alter table 	FAC_DISQUETEDEVOLUCIONES	 add constraint 	FK_FAC_DISDEVOLUCIONES_INSTITU	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	FAC_DISQUETEDEVOLUCIONES	 add constraint 	FDE_FBN_FK	 foreign key (	BANCOS_CODIGO,IDINSTITUCION	)	 references 	FAC_BANCOINSTITUCION	 (	BANCOS_CODIGO,IDINSTITUCION	) 		 Deferrable;
alter table 	FAC_DISQUETECARGOS	 drop constraint 	FK_FAC_DISQUETECARGOS_FACTURAC	 ; 
alter table 	FAC_DISQUETECARGOS	 drop constraint 	FK_DISQUETECARGOS_SUFIJO	 ; 
alter table 	FAC_DISQUETECARGOS	 drop constraint 	FDO_FBN_FK	 ; 
alter table 	FAC_DISQUETECARGOS	 add constraint 	FK_FAC_DISQUETECARGOS_FACTURAC	 foreign key (	IDPROGRAMACION,IDINSTITUCION,IDSERIEFACTURACION	)	 references 	FAC_FACTURACIONPROGRAMADA	 (	IDPROGRAMACION,IDINSTITUCION,IDSERIEFACTURACION	) 		 Deferrable;
alter table 	FAC_DISQUETECARGOS	 add constraint 	FK_DISQUETECARGOS_SUFIJO	 foreign key (	IDSUFIJO,IDINSTITUCION	)	 references 	FAC_SUFIJO	 (	IDSUFIJO,IDINSTITUCION	) 		 Deferrable;
alter table 	FAC_DISQUETECARGOS	 add constraint 	FDO_FBN_FK	 foreign key (	BANCOS_CODIGO,IDINSTITUCION	)	 references 	FAC_BANCOINSTITUCION	 (	BANCOS_CODIGO,IDINSTITUCION	) 		 Deferrable;
alter table 	FAC_DISQUETEABONOS	 drop constraint 	FDO_FBN_FK2	 ; 
alter table 	FAC_DISQUETEABONOS	 add constraint 	FDO_FBN_FK2	 foreign key (	BANCOS_CODIGO,IDINSTITUCION	)	 references 	FAC_BANCOINSTITUCION	 (	BANCOS_CODIGO,IDINSTITUCION	) 		 Deferrable;
alter table 	FAC_CLIENINCLUIDOENSERIEFACTUR	 drop constraint 	FK_FAC_CLIENINCLUENSERIE_SEFAC	 ; 
alter table 	FAC_CLIENINCLUIDOENSERIEFACTUR	 drop constraint 	FK_FAC_CLIENINCLUENSERIE_CLIEN	 ; 
alter table 	FAC_CLIENINCLUIDOENSERIEFACTUR	 add constraint 	FK_FAC_CLIENINCLUENSERIE_SEFAC	 foreign key (	IDSERIEFACTURACION,IDINSTITUCION	)	 references 	FAC_SERIEFACTURACION	 (	IDSERIEFACTURACION,IDINSTITUCION	) 		 Deferrable;
alter table 	FAC_CLIENINCLUIDOENSERIEFACTUR	 add constraint 	FK_FAC_CLIENINCLUENSERIE_CLIEN	 foreign key (	IDPERSONA,IDINSTITUCION	)	 references 	CEN_CLIENTE	 (	IDPERSONA,IDINSTITUCION	) 		 Deferrable;
alter table 	FAC_BANCOINSTITUCION	 drop constraint 	FK_FAC_BANCINSTI_FAC_SUFSJCS	 ; 
alter table 	FAC_BANCOINSTITUCION	 drop constraint 	FK_BANCOINSTITUCION_TIPOIVA	 ; 
alter table 	FAC_BANCOINSTITUCION	 drop constraint 	FK_BANCOINSTITUCION_INSTITUCIO	 ; 
alter table 	FAC_BANCOINSTITUCION	 drop constraint 	FK_BANCOINSTITUCION_BANCO	 ; 
alter table 	FAC_BANCOINSTITUCION	 add constraint 	FK_FAC_BANCINSTI_FAC_SUFSJCS	 foreign key (	IDSUFIJOSJCS,IDINSTITUCION	)	 references 	FAC_SUFIJO	 (	IDSUFIJO,IDINSTITUCION	) 		 Deferrable;
alter table 	FAC_BANCOINSTITUCION	 add constraint 	FK_BANCOINSTITUCION_TIPOIVA	 foreign key (	IDTIPOIVA	)	 references 	PYS_TIPOIVA	 (	IDTIPOIVA	) 		 Deferrable;
alter table 	FAC_BANCOINSTITUCION	 add constraint 	FK_BANCOINSTITUCION_INSTITUCIO	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	FAC_BANCOINSTITUCION	 add constraint 	FK_BANCOINSTITUCION_BANCO	 foreign key (	COD_BANCO	)	 references 	CEN_BANCOS	 (	CODIGO	) 		 Deferrable;
alter table 	FAC_ABONOINCLUIDOENDISQUETE	 drop constraint 	FK_FAC_ABONOINCLUIDOENDISQ_DIS	 ; 
alter table 	FAC_ABONOINCLUIDOENDISQUETE	 drop constraint 	FK_FAC_ABONOINCLUIDOENDISQ_ABO	 ; 
alter table 	FAC_ABONOINCLUIDOENDISQUETE	 add constraint 	FK_FAC_ABONOINCLUIDOENDISQ_DIS	 foreign key (	IDDISQUETEABONO,IDINSTITUCION	)	 references 	FAC_DISQUETEABONOS	 (	IDDISQUETEABONO,IDINSTITUCION	) 		 Deferrable;
alter table 	FAC_ABONOINCLUIDOENDISQUETE	 add constraint 	FK_FAC_ABONOINCLUIDOENDISQ_ABO	 foreign key (	IDABONO,IDINSTITUCION	)	 references 	FAC_ABONO	 (	IDABONO,IDINSTITUCION	) 		 Deferrable;
alter table 	FAC_ABONO	 drop constraint 	FK_FAC_ESTADOABONO	 ; 
alter table 	FAC_ABONO	 drop constraint 	FK_FAC_ABONO_PAGOSJ	 ; 
alter table 	FAC_ABONO	 drop constraint 	FK_FAC_ABONO_PAGOSCOLEGIADO	 ; 
alter table 	FAC_ABONO	 drop constraint 	FK_FAC_ABONO_FACTURA	 ; 
alter table 	FAC_ABONO	 drop constraint 	FK_FAC_ABONO_CUENTASBANCARIAS	 ; 
alter table 	FAC_ABONO	 drop constraint 	FK_FAC_ABONO_CLIENTE	 ; 
alter table 	FAC_ABONO	 add constraint 	FK_FAC_ESTADOABONO	 foreign key (	ESTADO	)	 references 	FAC_ESTADOABONO	 (	IDESTADO	) 		 Deferrable;
alter table 	FAC_ABONO	 add constraint 	FK_FAC_ABONO_PAGOSJ	 foreign key (	IDPAGOSJG,IDINSTITUCION	)	 references 	FCS_PAGOSJG	 (	IDPAGOSJG,IDINSTITUCION	) 		 Deferrable;
alter table 	FAC_ABONO	 add constraint 	FK_FAC_ABONO_PAGOSCOLEGIADO	 foreign key (	IDPERORIGEN,IDINSTITUCION,IDPAGOSJG	)	 references 	FCS_PAGO_COLEGIADO	 (	IDPERORIGEN,IDINSTITUCION,IDPAGOSJG	) 		 Deferrable;
alter table 	FAC_ABONO	 add constraint 	FK_FAC_ABONO_FACTURA	 foreign key (	IDFACTURA,IDINSTITUCION	)	 references 	FAC_FACTURA	 (	IDFACTURA,IDINSTITUCION	) 		 Deferrable;
alter table 	FAC_ABONO	 add constraint 	FK_FAC_ABONO_CUENTASBANCARIAS	 foreign key (	IDCUENTA,IDINSTITUCION,IDPERSONA	)	 references 	CEN_CUENTASBANCARIAS	 (	IDCUENTA,IDINSTITUCION,IDPERSONA	) 		 Deferrable;
alter table 	FAC_ABONO	 add constraint 	FK_FAC_ABONO_CLIENTE	 foreign key (	IDPERSONA,IDINSTITUCION	)	 references 	CEN_CLIENTE	 (	IDPERSONA,IDINSTITUCION	) 		 Deferrable;
alter table 	EXP_TIPOEXPEDIENTE	 drop constraint 	FK_ENV_PALNTILLAENVIOS	 ; 
alter table 	EXP_TIPOEXPEDIENTE	 add constraint 	FK_ENV_PALNTILLAENVIOS	 foreign key (	IDPLANTILLAENVIOS,IDINSTITUCION,IDTIPOENVIOS	)	 references 	ENV_PLANTILLASENVIOS	 (	IDPLANTILLAENVIOS,IDINSTITUCION,IDTIPOENVIOS	) 		 Deferrable;
alter table 	EXP_TIPOANOTACION	 drop constraint 	FK_EXP_TIPOANOTACION_TIPOEXPED	 ; 
alter table 	EXP_TIPOANOTACION	 drop constraint 	FK_EXP_TIPOANOTACION_FASES	 ; 
alter table 	EXP_TIPOANOTACION	 drop constraint 	FK_EXP_TIPOANOTACION_ESTADO	 ; 
alter table 	EXP_TIPOANOTACION	 add constraint 	FK_EXP_TIPOANOTACION_TIPOEXPED	 foreign key (	IDTIPOEXPEDIENTE,IDINSTITUCION	)	 references 	EXP_TIPOEXPEDIENTE	 (	IDTIPOEXPEDIENTE,IDINSTITUCION	) 		 Deferrable;
alter table 	EXP_TIPOANOTACION	 add constraint 	FK_EXP_TIPOANOTACION_FASES	 foreign key (	IDFASE,IDINSTITUCION,IDTIPOEXPEDIENTE	)	 references 	EXP_FASES	 (	IDFASE,IDINSTITUCION,IDTIPOEXPEDIENTE	) 		 Deferrable;
alter table 	EXP_TIPOANOTACION	 add constraint 	FK_EXP_TIPOANOTACION_ESTADO	 foreign key (	IDFASE,IDINSTITUCION,IDTIPOEXPEDIENTE,IDESTADO	)	 references 	EXP_ESTADO	 (	IDFASE,IDINSTITUCION,IDTIPOEXPEDIENTE,IDESTADO	) 		 Deferrable;
alter table 	EXP_SOLICITUDBORRADO	 drop constraint 	FK_EXP_SOLICITUDBORR_CEN_PERSO	 ; 
alter table 	EXP_SOLICITUDBORRADO	 add constraint 	FK_EXP_SOLICITUDBORR_CEN_PERSO	 foreign key (	IDPERSONA	)	 references 	CEN_PERSONA	 (	IDPERSONA	) 		 Deferrable;
alter table 	EXP_ROLPARTE	 drop constraint 	FK_EXP_ROLPARTE_TIPOEXPEDIENTE	 ; 
alter table 	EXP_ROLPARTE	 add constraint 	FK_EXP_ROLPARTE_TIPOEXPEDIENTE	 foreign key (	IDTIPOEXPEDIENTE,IDINSTITUCION	)	 references 	EXP_TIPOEXPEDIENTE	 (	IDTIPOEXPEDIENTE,IDINSTITUCION	) 		 Deferrable;
alter table 	EXP_PLAZOESTADOCLASIFICACION	 drop constraint 	FK_EXP_PLAZOESTCLASIFI_ESTADO	 ; 
alter table 	EXP_PLAZOESTADOCLASIFICACION	 drop constraint 	FK_EXP_PLAZOESTCLASIFI_CLASIF	 ; 
alter table 	EXP_PLAZOESTADOCLASIFICACION	 add constraint 	FK_EXP_PLAZOESTCLASIFI_ESTADO	 foreign key (	IDTIPOEXPEDIENTE,IDESTADO,IDFASE,IDINSTITUCION	)	 references 	EXP_ESTADO	 (	IDTIPOEXPEDIENTE,IDESTADO,IDFASE,IDINSTITUCION	) 		 Deferrable;
alter table 	EXP_PLAZOESTADOCLASIFICACION	 add constraint 	FK_EXP_PLAZOESTCLASIFI_CLASIF	 foreign key (	IDTIPOEXPEDIENTE,IDCLASIFICACION,IDINSTITUCION	)	 references 	EXP_CLASIFICACION	 (	IDTIPOEXPEDIENTE,IDCLASIFICACION,IDINSTITUCION	) 		 Deferrable;
alter table 	EXP_PESTANACONF	 drop constraint 	FK_EXP_PESTCONF_CAMPOTIPOEXP	 ; 
alter table 	EXP_PESTANACONF	 add constraint 	FK_EXP_PESTCONF_CAMPOTIPOEXP	 foreign key (	IDCAMPO,IDINSTITUCION,IDTIPOEXPEDIENTE	)	 references 	EXP_CAMPOTIPOEXPEDIENTE	 (	IDCAMPO,IDINSTITUCION,IDTIPOEXPEDIENTE	) 		 Deferrable;
alter table 	EXP_PERMISOSTIPOSEXPEDIENTES	 drop constraint 	FK_EXP_PERMISOSTIPO_TIPOEXPEDI	 ; 
alter table 	EXP_PERMISOSTIPOSEXPEDIENTES	 drop constraint 	FK_EXP_PERMISOSTIPO_ADM_PERFIL	 ; 
alter table 	EXP_PERMISOSTIPOSEXPEDIENTES	 add constraint 	FK_EXP_PERMISOSTIPO_TIPOEXPEDI	 foreign key (	IDTIPOEXPEDIENTE,IDINSTITUCION_TIPOEXPEDIENTE	)	 references 	EXP_TIPOEXPEDIENTE	 (	IDTIPOEXPEDIENTE,IDINSTITUCION	) 		 Deferrable;
alter table 	EXP_PERMISOSTIPOSEXPEDIENTES	 add constraint 	FK_EXP_PERMISOSTIPO_ADM_PERFIL	 foreign key (	IDPERFIL,IDINSTITUCION	)	 references 	ADM_PERFIL	 (	IDPERFIL,IDINSTITUCION	) 		 Deferrable;
alter table 	EXP_PARTE	 drop constraint 	FK_EXP_PARTE_ROLPARTE	 ; 
alter table 	EXP_PARTE	 drop constraint 	FK_EXP_PARTE_EXPEDIENTE	 ; 
alter table 	EXP_PARTE	 drop constraint 	FK_EXP_PARTE_DIRECCION	 ; 
alter table 	EXP_PARTE	 drop constraint 	FK_EXP_PARTE_CLIENTE	 ; 
alter table 	EXP_PARTE	 add constraint 	FK_EXP_PARTE_ROLPARTE	 foreign key (	IDROL,IDINSTITUCION,IDTIPOEXPEDIENTE	)	 references 	EXP_ROLPARTE	 (	IDROL,IDINSTITUCION,IDTIPOEXPEDIENTE	) 		 Deferrable;
alter table 	EXP_PARTE	 add constraint 	FK_EXP_PARTE_EXPEDIENTE	 foreign key (	IDINSTITUCION_TIPOEXPEDIENTE,IDINSTITUCION,IDTIPOEXPEDIENTE,NUMEROEXPEDIENTE,ANIOEXPEDIENTE	)	 references 	EXP_EXPEDIENTE	 (	IDINSTITUCION_TIPOEXPEDIENTE,IDINSTITUCION,IDTIPOEXPEDIENTE,NUMEROEXPEDIENTE,ANIOEXPEDIENTE	) 		 Deferrable;
alter table 	EXP_PARTE	 add constraint 	FK_EXP_PARTE_DIRECCION	 foreign key (	IDDIRECCION,IDINSTITUCION,IDPERSONA	)	 references 	CEN_DIRECCIONES	 (	IDDIRECCION,IDINSTITUCION,IDPERSONA	) 		 Deferrable;
alter table 	EXP_PARTE	 add constraint 	FK_EXP_PARTE_CLIENTE	 foreign key (	IDPERSONA,IDINSTITUCION	)	 references 	CEN_CLIENTE	 (	IDPERSONA,IDINSTITUCION	) 		 Deferrable;
alter table 	EXP_LOGESTADO	 drop constraint 	FK_EXP_LOGESTADO_EXPEDIENTE	 ; 
alter table 	EXP_LOGESTADO	 drop constraint 	FK_EXP_LOGESTADO_ESTADO	 ; 
alter table 	EXP_LOGESTADO	 add constraint 	FK_EXP_LOGESTADO_EXPEDIENTE	 foreign key (	ANIOEXPEDIENTE,IDINSTITUCION,IDINSTITUCION_TIPOEXPEDIENTE,IDTIPOEXPEDIENTE,NUMEROEXPEDIENTE	)	 references 	EXP_EXPEDIENTE	 (	ANIOEXPEDIENTE,IDINSTITUCION,IDINSTITUCION_TIPOEXPEDIENTE,IDTIPOEXPEDIENTE,NUMEROEXPEDIENTE	) 		 Deferrable;
alter table 	EXP_LOGESTADO	 add constraint 	FK_EXP_LOGESTADO_ESTADO	 foreign key (	IDESTADO,IDINSTITUCION_TIPOEXPEDIENTE,IDTIPOEXPEDIENTE,IDFASE	)	 references 	EXP_ESTADO	 (	IDESTADO,IDINSTITUCION,IDTIPOEXPEDIENTE,IDFASE	) 		 Deferrable;
alter table 	EXP_FASES	 drop constraint 	FK_FASES_TIPOEXPEDIENTE	 ; 
alter table 	EXP_FASES	 drop constraint 	FK_EXP_FASES_INSTITUCION	 ; 
alter table 	EXP_FASES	 add constraint 	FK_FASES_TIPOEXPEDIENTE	 foreign key (	IDTIPOEXPEDIENTE,IDINSTITUCION	)	 references 	EXP_TIPOEXPEDIENTE	 (	IDTIPOEXPEDIENTE,IDINSTITUCION	) 		 Deferrable;
alter table 	EXP_FASES	 add constraint 	FK_EXP_FASES_INSTITUCION	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	EXP_EXPEDIENTE	 drop constraint 	FK_SCS_PROCEDIMIEN_EXPEDIENTE	 ; 
alter table 	EXP_EXPEDIENTE	 drop constraint 	FK_SCS_JUZGADO_EXPEDIENTE	 ; 
alter table 	EXP_EXPEDIENTE	 drop constraint 	FK_SCS_EXPEDIEN_PRETENSION	 ; 
alter table 	EXP_EXPEDIENTE	 drop constraint 	FK_EXP_EXPED_TIPORESULTADO	 ; 
alter table 	EXP_EXPEDIENTE	 drop constraint 	FK_EXP_EXPEDIEN_SCS_MATERIA	 ; 
alter table 	EXP_EXPEDIENTE	 drop constraint 	FK_EXP_EXPEDIENTE_TIPOEXPEDIEN	 ; 
alter table 	EXP_EXPEDIENTE	 drop constraint 	FK_EXP_EXPEDIENTE_DIRECCION	 ; 
alter table 	EXP_EXPEDIENTE	 drop constraint 	FK_EXP_EXPEDIENTE_CEN_CLIENTE	 ; 
alter table 	EXP_EXPEDIENTE	 drop constraint 	FK_EXP_EXPEDIENTES_IVA	 ; 
alter table 	EXP_EXPEDIENTE	 drop constraint 	FK_EXP_EXPEDIENTES_INSTITUCION	 ; 
alter table 	EXP_EXPEDIENTE	 drop constraint 	FK_EXP_EXPEDIENTES_ESTADO	 ; 
alter table 	EXP_EXPEDIENTE	 drop constraint 	FK_EXP_EXPEDIENTES_CLASIFICACI	 ; 
alter table 	EXP_EXPEDIENTE	 add constraint 	FK_SCS_PROCEDIMIEN_EXPEDIENTE	 foreign key (	IDINSTITUCION_PROC,PROCEDIMIENTO	)	 references 	SCS_PROCEDIMIENTOS	 (	IDINSTITUCION,IDPROCEDIMIENTO	) 		 Deferrable;
alter table 	EXP_EXPEDIENTE	 add constraint 	FK_SCS_JUZGADO_EXPEDIENTE	 foreign key (	IDINSTITUCION_JUZ,JUZGADO	)	 references 	SCS_JUZGADO	 (	IDINSTITUCION,IDJUZGADO	) 		 Deferrable;
alter table 	EXP_EXPEDIENTE	 add constraint 	FK_SCS_EXPEDIEN_PRETENSION	 foreign key (	IDPRETENSION,IDINSTITUCION	)	 references 	SCS_PRETENSION	 (	IDPRETENSION,IDINSTITUCION	) 		 Deferrable;
alter table 	EXP_EXPEDIENTE	 add constraint 	FK_EXP_EXPED_TIPORESULTADO	 foreign key (	IDRESULTADOJUNTAGOBIERNO,IDINSTITUCION	)	 references 	EXP_TIPORESULTADORESOLUCION	 (	IDTIPORESULTADO,IDINSTITUCION	) 		 Deferrable;
alter table 	EXP_EXPEDIENTE	 add constraint 	FK_EXP_EXPEDIEN_SCS_MATERIA	 foreign key (	IDMATERIA,IDINSTITUCION,IDAREA	)	 references 	SCS_MATERIA	 (	IDMATERIA,IDINSTITUCION,IDAREA	) 		 Deferrable;
alter table 	EXP_EXPEDIENTE	 add constraint 	FK_EXP_EXPEDIENTE_TIPOEXPEDIEN	 foreign key (	IDINSTITUCION_TIPOEXPEDIENTE,IDTIPOEXPEDIENTE	)	 references 	EXP_TIPOEXPEDIENTE	 (	IDINSTITUCION,IDTIPOEXPEDIENTE	) 		 Deferrable;
alter table 	EXP_EXPEDIENTE	 add constraint 	FK_EXP_EXPEDIENTE_DIRECCION	 foreign key (	IDDIRECCION,IDINSTITUCION,IDPERSONA	)	 references 	CEN_DIRECCIONES	 (	IDDIRECCION,IDINSTITUCION,IDPERSONA	) 		 Deferrable;
alter table 	EXP_EXPEDIENTE	 add constraint 	FK_EXP_EXPEDIENTE_CEN_CLIENTE	 foreign key (	IDPERSONA,IDINSTITUCION	)	 references 	CEN_CLIENTE	 (	IDPERSONA,IDINSTITUCION	) 		 Deferrable;
alter table 	EXP_EXPEDIENTE	 add constraint 	FK_EXP_EXPEDIENTES_IVA	 foreign key (	IDTIPOIVA	)	 references 	PYS_TIPOIVA	 (	IDTIPOIVA	) 		 Deferrable;
alter table 	EXP_EXPEDIENTE	 add constraint 	FK_EXP_EXPEDIENTES_INSTITUCION	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	EXP_EXPEDIENTE	 add constraint 	FK_EXP_EXPEDIENTES_ESTADO	 foreign key (	IDESTADO,IDTIPOEXPEDIENTE,IDINSTITUCION_TIPOEXPEDIENTE,IDFASE	)	 references 	EXP_ESTADO	 (	IDESTADO,IDTIPOEXPEDIENTE,IDINSTITUCION,IDFASE	) 		 Deferrable;
alter table 	EXP_EXPEDIENTE	 add constraint 	FK_EXP_EXPEDIENTES_CLASIFICACI	 foreign key (	IDCLASIFICACION,IDTIPOEXPEDIENTE,IDINSTITUCION_TIPOEXPEDIENTE	)	 references 	EXP_CLASIFICACION	 (	IDCLASIFICACION,IDTIPOEXPEDIENTE,IDINSTITUCION	) 		 Deferrable;
alter table 	EXP_ESTADO	 drop constraint 	FK_EXP_ESTADO_FASES	 ; 
alter table 	EXP_ESTADO	 drop constraint 	FK_EXP_ESTADO_ESTADO	 ; 
alter table 	EXP_ESTADO	 add constraint 	FK_EXP_ESTADO_FASES	 foreign key (	IDTIPOEXPEDIENTE,IDFASE,IDINSTITUCION	)	 references 	EXP_FASES	 (	IDTIPOEXPEDIENTE,IDFASE,IDINSTITUCION	) 		 Deferrable;
alter table 	EXP_ESTADO	 add constraint 	FK_EXP_ESTADO_ESTADO	 foreign key (	IDESTADO_SIGUIENTE,IDINSTITUCION,IDTIPOEXPEDIENTE,IDFASE_SIGUIENTE	)	 references 	EXP_ESTADO	 (	IDESTADO,IDINSTITUCION,IDTIPOEXPEDIENTE,IDFASE	) 		 Deferrable;
alter table 	EXP_DOCUMENTO	 drop constraint 	FK_EXP_DOCUMENTO_EXPEDIENTE	 ; 
alter table 	EXP_DOCUMENTO	 drop constraint 	FK_EXP_DOCUMENTO_ESTADO	 ; 
alter table 	EXP_DOCUMENTO	 add constraint 	FK_EXP_DOCUMENTO_EXPEDIENTE	 foreign key (	IDINSTITUCION_TIPOEXPEDIENTE,IDINSTITUCION,IDTIPOEXPEDIENTE,ANIOEXPEDIENTE,NUMEROEXPEDIENTE	)	 references 	EXP_EXPEDIENTE	 (	IDINSTITUCION_TIPOEXPEDIENTE,IDINSTITUCION,IDTIPOEXPEDIENTE,ANIOEXPEDIENTE,NUMEROEXPEDIENTE	) 		 Deferrable;
alter table 	EXP_DOCUMENTO	 add constraint 	FK_EXP_DOCUMENTO_ESTADO	 foreign key (	IDINSTITUCION_TIPOEXPEDIENTE,IDTIPOEXPEDIENTE,IDFASE,IDESTADO	)	 references 	EXP_ESTADO	 (	IDINSTITUCION,IDTIPOEXPEDIENTE,IDFASE,IDESTADO	) 		 Deferrable;
alter table 	EXP_DENUNCIANTE	 drop constraint 	FK_EXP_DENUNCIANTE_EXPEDIENTE	 ; 
alter table 	EXP_DENUNCIANTE	 drop constraint 	FK_EXP_DENUNCIANTE_DIRECCION	 ; 
alter table 	EXP_DENUNCIANTE	 drop constraint 	FK_EXP_DENUNCIANTE_CLIENTE	 ; 
alter table 	EXP_DENUNCIANTE	 add constraint 	FK_EXP_DENUNCIANTE_EXPEDIENTE	 foreign key (	IDINSTITUCION_TIPOEXPEDIENTE,IDINSTITUCION,IDTIPOEXPEDIENTE,NUMEROEXPEDIENTE,ANIOEXPEDIENTE	)	 references 	EXP_EXPEDIENTE	 (	IDINSTITUCION_TIPOEXPEDIENTE,IDINSTITUCION,IDTIPOEXPEDIENTE,NUMEROEXPEDIENTE,ANIOEXPEDIENTE	) 		 Deferrable;
alter table 	EXP_DENUNCIANTE	 add constraint 	FK_EXP_DENUNCIANTE_DIRECCION	 foreign key (	IDDIRECCION,IDINSTITUCION,IDPERSONA	)	 references 	CEN_DIRECCIONES	 (	IDDIRECCION,IDINSTITUCION,IDPERSONA	) 		 Deferrable;
alter table 	EXP_DENUNCIANTE	 add constraint 	FK_EXP_DENUNCIANTE_CLIENTE	 foreign key (	IDPERSONA,IDINSTITUCION	)	 references 	CEN_CLIENTE	 (	IDPERSONA,IDINSTITUCION	) 		 Deferrable;
alter table 	EXP_DENUNCIADO	 drop constraint 	FK_EXP_DENUNCIADO_EXPEDIENTE	 ; 
alter table 	EXP_DENUNCIADO	 drop constraint 	FK_EXP_DENUNCIADO_DIRECCION	 ; 
alter table 	EXP_DENUNCIADO	 drop constraint 	FK_EXP_DENUNCIADO_CLIENTE	 ; 
alter table 	EXP_DENUNCIADO	 add constraint 	FK_EXP_DENUNCIADO_EXPEDIENTE	 foreign key (	IDINSTITUCION_TIPOEXPEDIENTE,IDINSTITUCION,IDTIPOEXPEDIENTE,NUMEROEXPEDIENTE,ANIOEXPEDIENTE	)	 references 	EXP_EXPEDIENTE	 (	IDINSTITUCION_TIPOEXPEDIENTE,IDINSTITUCION,IDTIPOEXPEDIENTE,NUMEROEXPEDIENTE,ANIOEXPEDIENTE	) 		 Deferrable;
alter table 	EXP_DENUNCIADO	 add constraint 	FK_EXP_DENUNCIADO_DIRECCION	 foreign key (	IDDIRECCION,IDINSTITUCION,IDPERSONA	)	 references 	CEN_DIRECCIONES	 (	IDDIRECCION,IDINSTITUCION,IDPERSONA	) 		 Deferrable;
alter table 	EXP_DENUNCIADO	 add constraint 	FK_EXP_DENUNCIADO_CLIENTE	 foreign key (	IDPERSONA,IDINSTITUCION	)	 references 	CEN_CLIENTE	 (	IDPERSONA,IDINSTITUCION	) 		 Deferrable;
alter table 	EXP_CLASIFICACION	 drop constraint 	FK_EXP_CLASIFICACION_TIPOEXPE	 ; 
alter table 	EXP_CLASIFICACION	 drop constraint 	FK_EXP_CLASIFICACION_INSTITUCI	 ; 
alter table 	EXP_CLASIFICACION	 add constraint 	FK_EXP_CLASIFICACION_TIPOEXPE	 foreign key (	IDTIPOEXPEDIENTE,IDINSTITUCION	)	 references 	EXP_TIPOEXPEDIENTE	 (	IDTIPOEXPEDIENTE,IDINSTITUCION	) 		 Deferrable;
alter table 	EXP_CLASIFICACION	 add constraint 	FK_EXP_CLASIFICACION_INSTITUCI	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	EXP_CAMPOTIPOEXPEDIENTE	 drop constraint 	FK_CAMPOTIPOEXPEDIENTE_TIPOEXP	 ; 
alter table 	EXP_CAMPOTIPOEXPEDIENTE	 drop constraint 	FK_CAMPOTIPOEXPEDIENTE_CAMPOEX	 ; 
alter table 	EXP_CAMPOTIPOEXPEDIENTE	 add constraint 	FK_CAMPOTIPOEXPEDIENTE_TIPOEXP	 foreign key (	IDTIPOEXPEDIENTE,IDINSTITUCION	)	 references 	EXP_TIPOEXPEDIENTE	 (	IDTIPOEXPEDIENTE,IDINSTITUCION	) 		 Deferrable;
alter table 	EXP_CAMPOTIPOEXPEDIENTE	 add constraint 	FK_CAMPOTIPOEXPEDIENTE_CAMPOEX	 foreign key (	IDCAMPO	)	 references 	EXP_CAMPOSEXPEDIENTES	 (	IDCAMPO	) 		 Deferrable;
alter table 	EXP_CAMPOSVALOR	 drop constraint 	FK_EXP_CAMPOSVALOR_EXPEDIENTE	 ; 
alter table 	EXP_CAMPOSVALOR	 drop constraint 	FK_EXP_CAMPOSVALOR_CAMPOCONF	 ; 
alter table 	EXP_CAMPOSVALOR	 add constraint 	FK_EXP_CAMPOSVALOR_EXPEDIENTE	 foreign key (	ANIOEXPEDIENTE,IDINSTITUCION,IDINSTITUCION_TIPOEXPEDIENTE,IDTIPOEXPEDIENTE,NUMEROEXPEDIENTE	)	 references 	EXP_EXPEDIENTE	 (	ANIOEXPEDIENTE,IDINSTITUCION,IDINSTITUCION_TIPOEXPEDIENTE,IDTIPOEXPEDIENTE,NUMEROEXPEDIENTE	) 		 Deferrable;
alter table 	EXP_CAMPOSVALOR	 add constraint 	FK_EXP_CAMPOSVALOR_CAMPOCONF	 foreign key (	IDCAMPOCONF,IDINSTITUCION_TIPOEXPEDIENTE,IDTIPOEXPEDIENTE,IDCAMPO,IDPESTANACONF	)	 references 	EXP_CAMPOCONF	 (	IDCAMPOCONF,IDINSTITUCION,IDTIPOEXPEDIENTE,IDCAMPO,IDPESTANACONF	) 		 Deferrable;
alter table 	EXP_CAMPOCONF	 drop constraint 	FK_EXP_CAMPOSCONF_TIPOCAMPO	 ; 
alter table 	EXP_CAMPOCONF	 drop constraint 	FK_EXP_CAMPOSCONF_PESTANACONF	 ; 
alter table 	EXP_CAMPOCONF	 add constraint 	FK_EXP_CAMPOSCONF_TIPOCAMPO	 foreign key (	TIPO	)	 references 	GEN_TIPOCAMPO	 (	IDTIPOCAMPO	) 		 Deferrable;
alter table 	EXP_CAMPOCONF	 add constraint 	FK_EXP_CAMPOSCONF_PESTANACONF	 foreign key (	IDCAMPO,IDINSTITUCION,IDTIPOEXPEDIENTE,IDPESTANACONF	)	 references 	EXP_PESTANACONF	 (	IDCAMPO,IDINSTITUCION,IDTIPOEXPEDIENTE,IDPESTANACONF	) 		 Deferrable;
alter table 	EXP_ANOTACION	 drop constraint 	FK_EXP_ANOTACION_TIPOANOTACION	 ; 
alter table 	EXP_ANOTACION	 drop constraint 	FK_EXP_ANOTACION_EXPEDIENTE	 ; 
alter table 	EXP_ANOTACION	 drop constraint 	FK_EXP_ANOTACION_ESTADO	 ; 
alter table 	EXP_ANOTACION	 add constraint 	FK_EXP_ANOTACION_TIPOANOTACION	 foreign key (	IDINSTITUCION_TIPOEXPEDIENTE,IDTIPOEXPEDIENTE,IDTIPOANOTACION	)	 references 	EXP_TIPOANOTACION	 (	IDINSTITUCION,IDTIPOEXPEDIENTE,IDTIPOANOTACION	) 		 Deferrable;
alter table 	EXP_ANOTACION	 add constraint 	FK_EXP_ANOTACION_EXPEDIENTE	 foreign key (	ANIOEXPEDIENTE,IDINSTITUCION,IDTIPOEXPEDIENTE,IDINSTITUCION_TIPOEXPEDIENTE,NUMEROEXPEDIENTE	)	 references 	EXP_EXPEDIENTE	 (	ANIOEXPEDIENTE,IDINSTITUCION,IDTIPOEXPEDIENTE,IDINSTITUCION_TIPOEXPEDIENTE,NUMEROEXPEDIENTE	) 		 Deferrable;
alter table 	EXP_ANOTACION	 add constraint 	FK_EXP_ANOTACION_ESTADO	 foreign key (	IDESTADO,IDTIPOEXPEDIENTE,IDINSTITUCION_TIPOEXPEDIENTE,IDFASE	)	 references 	EXP_ESTADO	 (	IDESTADO,IDTIPOEXPEDIENTE,IDINSTITUCION,IDFASE	) 		 Deferrable;
alter table 	EXP_ALERTA	 drop constraint 	FK_EXP_ALERTA_EXPEDIENTE	 ; 
alter table 	EXP_ALERTA	 drop constraint 	FK_EXP_ALERTA_ESTADO	 ; 
alter table 	EXP_ALERTA	 add constraint 	FK_EXP_ALERTA_EXPEDIENTE	 foreign key (	IDINSTITUCION_TIPOEXPEDIENTE,IDINSTITUCION,IDTIPOEXPEDIENTE,NUMEROEXPEDIENTE,ANIOEXPEDIENTE	)	 references 	EXP_EXPEDIENTE	 (	IDINSTITUCION_TIPOEXPEDIENTE,IDINSTITUCION,IDTIPOEXPEDIENTE,NUMEROEXPEDIENTE,ANIOEXPEDIENTE	) 		 Deferrable;
alter table 	EXP_ALERTA	 add constraint 	FK_EXP_ALERTA_ESTADO	 foreign key (	IDESTADO,IDTIPOEXPEDIENTE,IDINSTITUCION_TIPOEXPEDIENTE,IDFASE	)	 references 	EXP_ESTADO	 (	IDESTADO,IDTIPOEXPEDIENTE,IDINSTITUCION,IDFASE	) 		 Deferrable;
alter table 	ENV_VALORCAMPOCLAVE	 drop constraint 	FK_VALOR_DESTPROGINF	 ; 
alter table 	ENV_VALORCAMPOCLAVE	 drop constraint 	FK_VALOR_CAMPOCLAVEPROG	 ; 
alter table 	ENV_VALORCAMPOCLAVE	 add constraint 	FK_VALOR_DESTPROGINF	 foreign key (	IDINSTITUCION_PERSONA,IDPROGRAM,IDENVIO,IDINSTITUCION,IDPERSONA	)	 references 	ENV_DESTPROGRAMINFORMES	 (	IDINSTITUCION_PERSONA,IDPROGRAM,IDENVIO,IDINSTITUCION,IDPERSONA	) 		 Deferrable;
alter table 	ENV_VALORCAMPOCLAVE	 add constraint 	FK_VALOR_CAMPOCLAVEPROG	 foreign key (	CAMPO,IDTIPOINFORME,CLAVE	)	 references 	ENV_CAMPOCLAVEPROG	 (	CAMPO,IDTIPOINFORME,CLAVE	) 		 Deferrable;
alter table 	ENV_TEMP_DESTINATARIOS	 drop constraint 	FK_ENV_TEMP_DESTINATARI_ENVIOS	 ; 
alter table 	ENV_TEMP_DESTINATARIOS	 add constraint 	FK_ENV_TEMP_DESTINATARI_ENVIOS	 foreign key (	IDENVIO,IDINSTITUCION	)	 references 	ENV_ENVIOS	 (	IDENVIO,IDINSTITUCION	) 		 Deferrable;
alter table 	ENV_REMITENTES	 drop constraint 	FK_ENV_REMITENTES_ENVIO	 ; 
alter table 	ENV_REMITENTES	 drop constraint 	FK_ENV_REMITENTES_CLIENTE	 ; 
alter table 	ENV_REMITENTES	 drop constraint 	FK_ENV_REMITENTES_CEN_PROVINCI	 ; 
alter table 	ENV_REMITENTES	 drop constraint 	FK_ENV_REMITENTES_CEN_POBLACIO	 ; 
alter table 	ENV_REMITENTES	 drop constraint 	FK_ENV_REMITENTES_CEN_PAIS	 ; 
alter table 	ENV_REMITENTES	 add constraint 	FK_ENV_REMITENTES_ENVIO	 foreign key (	IDENVIO,IDINSTITUCION	)	 references 	ENV_ENVIOS	 (	IDENVIO,IDINSTITUCION	) 		 Deferrable;
alter table 	ENV_REMITENTES	 add constraint 	FK_ENV_REMITENTES_CLIENTE	 foreign key (	IDPERSONA,IDINSTITUCION	)	 references 	CEN_CLIENTE	 (	IDPERSONA,IDINSTITUCION	) 		 Deferrable;
alter table 	ENV_REMITENTES	 add constraint 	FK_ENV_REMITENTES_CEN_PROVINCI	 foreign key (	IDPROVINCIA	)	 references 	CEN_PROVINCIAS	 (	IDPROVINCIA	) 		 Deferrable;
alter table 	ENV_REMITENTES	 add constraint 	FK_ENV_REMITENTES_CEN_POBLACIO	 foreign key (	IDPOBLACION	)	 references 	CEN_POBLACIONES	 (	IDPOBLACION	) 		 Deferrable;
alter table 	ENV_REMITENTES	 add constraint 	FK_ENV_REMITENTES_CEN_PAIS	 foreign key (	IDPAIS	)	 references 	CEN_PAIS	 (	IDPAIS	) 		 Deferrable;
alter table 	ENV_PROGRAMPAGOS	 drop constraint 	FK_PROGRAMPAGOS_PERSONA	 ; 
alter table 	ENV_PROGRAMPAGOS	 drop constraint 	FK_PROGRAMPAGOS_PAGOSJG	 ; 
alter table 	ENV_PROGRAMPAGOS	 drop constraint 	FK_PROGRAMPAGOS_ENVIOPROG	 ; 
alter table 	ENV_PROGRAMPAGOS	 add constraint 	FK_PROGRAMPAGOS_PERSONA	 foreign key (	IDPERSONA	)	 references 	CEN_PERSONA	 (	IDPERSONA	) 		 Deferrable;
alter table 	ENV_PROGRAMPAGOS	 add constraint 	FK_PROGRAMPAGOS_PAGOSJG	 foreign key (	IDPAGO,IDINSTITUCION	)	 references 	FCS_PAGOSJG	 (	IDPAGOSJG,IDINSTITUCION	) 		 Deferrable;
alter table 	ENV_PROGRAMPAGOS	 add constraint 	FK_PROGRAMPAGOS_ENVIOPROG	 foreign key (	IDINSTITUCION,IDENVIO	)	 references 	ENV_ENVIOPROGRAMADO	 (	IDINSTITUCION,IDENVIO	) 		 Deferrable;
alter table 	ENV_PROGRAMIRPF	 drop constraint 	FK_PROGRAMIRPF_PERSONA	 ; 
alter table 	ENV_PROGRAMIRPF	 drop constraint 	FK_PROGRAMIRPF_ENVIOPROG	 ; 
alter table 	ENV_PROGRAMIRPF	 add constraint 	FK_PROGRAMIRPF_PERSONA	 foreign key (	IDPERSONA	)	 references 	CEN_PERSONA	 (	IDPERSONA	) 		 Deferrable;
alter table 	ENV_PROGRAMIRPF	 add constraint 	FK_PROGRAMIRPF_ENVIOPROG	 foreign key (	IDINSTITUCION,IDENVIO	)	 references 	ENV_ENVIOPROGRAMADO	 (	IDINSTITUCION,IDENVIO	) 		 Deferrable;
alter table 	ENV_PROGRAMINFORMES	 drop constraint 	FK_PROGRAMINFORMES_TIPO	 ; 
alter table 	ENV_PROGRAMINFORMES	 drop constraint 	FK_PROGRAMINFORMES_ENVIOPROG	 ; 
alter table 	ENV_PROGRAMINFORMES	 add constraint 	FK_PROGRAMINFORMES_TIPO	 foreign key (	IDTIPOINFORME	)	 references 	ADM_TIPOINFORME	 (	IDTIPOINFORME	) 		 Deferrable;
alter table 	ENV_PROGRAMINFORMES	 add constraint 	FK_PROGRAMINFORMES_ENVIOPROG	 foreign key (	IDINSTITUCION,IDENVIO	)	 references 	ENV_ENVIOPROGRAMADO	 (	IDINSTITUCION,IDENVIO	) 		 Deferrable;
alter table 	ENV_PLANTILLASENVIOS	 drop constraint 	FK_ENV_PLANTILLASENVIOS_TIPOEN	 ; 
alter table 	ENV_PLANTILLASENVIOS	 drop constraint 	FK_ENV_PLANTILLASENVIOS_INSTIT	 ; 
alter table 	ENV_PLANTILLASENVIOS	 add constraint 	FK_ENV_PLANTILLASENVIOS_TIPOEN	 foreign key (	IDTIPOENVIOS	)	 references 	ENV_TIPOENVIOS	 (	IDTIPOENVIOS	) 		 Deferrable;
alter table 	ENV_PLANTILLASENVIOS	 add constraint 	FK_ENV_PLANTILLASENVIOS_INSTIT	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	ENV_PLANTILLAREMITENTES	 drop constraint 	FK_ENV_PLANTREMIT_PLANTI	 ; 
alter table 	ENV_PLANTILLAREMITENTES	 drop constraint 	FK_ENV_PLANTREMIT_CLIENTE	 ; 
alter table 	ENV_PLANTILLAREMITENTES	 drop constraint 	FK_ENV_PLANTREMIT_CEN_PROVINCI	 ; 
alter table 	ENV_PLANTILLAREMITENTES	 drop constraint 	FK_ENV_PLANTREMIT_CEN_POBLACIO	 ; 
alter table 	ENV_PLANTILLAREMITENTES	 drop constraint 	FK_ENV_PLANTREMIT_CEN_PAIS	 ; 
alter table 	ENV_PLANTILLAREMITENTES	 add constraint 	FK_ENV_PLANTREMIT_PLANTI	 foreign key (	IDPLANTILLAENVIOS,IDINSTITUCION,IDTIPOENVIOS	)	 references 	ENV_PLANTILLASENVIOS	 (	IDPLANTILLAENVIOS,IDINSTITUCION,IDTIPOENVIOS	) 		 Deferrable;
alter table 	ENV_PLANTILLAREMITENTES	 add constraint 	FK_ENV_PLANTREMIT_CLIENTE	 foreign key (	IDPERSONA,IDINSTITUCION	)	 references 	CEN_CLIENTE	 (	IDPERSONA,IDINSTITUCION	) 		 Deferrable;
alter table 	ENV_PLANTILLAREMITENTES	 add constraint 	FK_ENV_PLANTREMIT_CEN_PROVINCI	 foreign key (	IDPROVINCIA	)	 references 	CEN_PROVINCIAS	 (	IDPROVINCIA	) 		 Deferrable;
alter table 	ENV_PLANTILLAREMITENTES	 add constraint 	FK_ENV_PLANTREMIT_CEN_POBLACIO	 foreign key (	IDPOBLACION	)	 references 	CEN_POBLACIONES	 (	IDPOBLACION	) 		 Deferrable;
alter table 	ENV_PLANTILLAREMITENTES	 add constraint 	FK_ENV_PLANTREMIT_CEN_PAIS	 foreign key (	IDPAIS	)	 references 	CEN_PAIS	 (	IDPAIS	) 		 Deferrable;
alter table 	ENV_PLANTILLAGENERACION	 drop constraint 	FK_ENV_PGENERACION_PLANTILLAEN	 ; 
alter table 	ENV_PLANTILLAGENERACION	 add constraint 	FK_ENV_PGENERACION_PLANTILLAEN	 foreign key (	IDPLANTILLAENVIOS,IDINSTITUCION,IDTIPOENVIOS	)	 references 	ENV_PLANTILLASENVIOS	 (	IDPLANTILLAENVIOS,IDINSTITUCION,IDTIPOENVIOS	) 		 Deferrable;
alter table 	ENV_LISTACORREOSENVIOS	 drop constraint 	FK_ENV_LISTACORREOSENVIOS_LIST	 ; 
alter table 	ENV_LISTACORREOSENVIOS	 drop constraint 	FK_ENV_LISTACORREOSENVIOS_ENVI	 ; 
alter table 	ENV_LISTACORREOSENVIOS	 add constraint 	FK_ENV_LISTACORREOSENVIOS_LIST	 foreign key (	IDLISTACORREO,IDINSTITUCION	)	 references 	ENV_LISTACORREOS	 (	IDLISTACORREO,IDINSTITUCION	) 		 Deferrable;
alter table 	ENV_LISTACORREOSENVIOS	 add constraint 	FK_ENV_LISTACORREOSENVIOS_ENVI	 foreign key (	IDENVIO,IDINSTITUCION	)	 references 	ENV_ENVIOS	 (	IDENVIO,IDINSTITUCION	) 		 Deferrable;
alter table 	ENV_LISTACORREOS	 drop constraint 	FK_ENV_LISTACORREOS_CEN_INSTIT	 ; 
alter table 	ENV_LISTACORREOS	 add constraint 	FK_ENV_LISTACORREOS_CEN_INSTIT	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	ENV_LISTACORREOCONSULTA	 drop constraint 	FK_ENV_LISTACORREOCON_CON_CON	 ; 
alter table 	ENV_LISTACORREOCONSULTA	 add constraint 	FK_ENV_LISTACORREOCON_CON_CON	 foreign key (	IDINSTITUCION_CON,IDCONSULTA	)	 references 	CON_CONSULTA	 (	IDINSTITUCION,IDCONSULTA	) 		 Deferrable;
alter table 	ENV_IMAGENPLANTILLA	 drop constraint 	FK_ENV_IMAGENPLANTILLA	 ; 
alter table 	ENV_IMAGENPLANTILLA	 add constraint 	FK_ENV_IMAGENPLANTILLA	 foreign key (	IDPLANTILLAENVIOS,IDINSTITUCION,IDTIPOENVIOS	)	 references 	ENV_PLANTILLASENVIOS	 (	IDPLANTILLAENVIOS,IDINSTITUCION,IDTIPOENVIOS	) 		 Deferrable;
alter table 	ENV_HISTORICOESTADOENVIO	 drop constraint 	FK_ENV_HISTORICO_ESTADO	 ; 
alter table 	ENV_HISTORICOESTADOENVIO	 drop constraint 	FK_ENV_HISTORICO_ENVIO	 ; 
alter table 	ENV_HISTORICOESTADOENVIO	 add constraint 	FK_ENV_HISTORICO_ESTADO	 foreign key (	IDESTADO	)	 references 	ENV_ESTADOENVIO	 (	IDESTADO	) 		 Deferrable;
alter table 	ENV_HISTORICOESTADOENVIO	 add constraint 	FK_ENV_HISTORICO_ENVIO	 foreign key (	IDENVIO,IDINSTITUCION	)	 references 	ENV_ENVIOS	 (	IDENVIO,IDINSTITUCION	) 		 Deferrable;
alter table 	ENV_ESTAT_ENVIO	 drop constraint 	FK_ENV_ESTAT_ENVIO_ENVIOS	 ; 
alter table 	ENV_ESTAT_ENVIO	 add constraint 	FK_ENV_ESTAT_ENVIO_ENVIOS	 foreign key (	IDENVIO,IDINSTITUCION	)	 references 	ENV_ENVIOS	 (	IDENVIO,IDINSTITUCION	) 	 on delete cascade	 Deferrable;
alter table 	ENV_ENVIOS	 drop constraint 	FK_ENV_TIPOENVIO_ENVIOS	 ; 
alter table 	ENV_ENVIOS	 drop constraint 	FK_ENV_ENVIOS_IMPRESORA	 ; 
alter table 	ENV_ENVIOS	 drop constraint 	FK_ENV_ENVIOS_ESTADO	 ; 
alter table 	ENV_ENVIOS	 add constraint 	FK_ENV_TIPOENVIO_ENVIOS	 foreign key (	IDTIPOENVIOS	)	 references 	ENV_TIPOENVIOS	 (	IDTIPOENVIOS	) 		 Deferrable;
alter table 	ENV_ENVIOS	 add constraint 	FK_ENV_ENVIOS_IMPRESORA	 foreign key (	IDIMPRESORA,IDINSTITUCION	)	 references 	CON_IMPRESORA	 (	IDIMPRESORA,IDINSTITUCION	) 		 Deferrable;
alter table 	ENV_ENVIOS	 add constraint 	FK_ENV_ENVIOS_ESTADO	 foreign key (	IDESTADO	)	 references 	ENV_ESTADOENVIO	 (	IDESTADO	) 		 Deferrable;
alter table 	ENV_ENVIOPROGRAMADO	 drop constraint 	FK_ENVIOPROG_PLANTILLAENVIOS	 ; 
alter table 	ENV_ENVIOPROGRAMADO	 add constraint 	FK_ENVIOPROG_PLANTILLAENVIOS	 foreign key (	IDPLANTILLAENVIOS,IDINSTITUCION,IDTIPOENVIOS	)	 references 	ENV_PLANTILLASENVIOS	 (	IDPLANTILLAENVIOS,IDINSTITUCION,IDTIPOENVIOS	) 		 Deferrable;
alter table 	ENV_DOCUMENTOSDESTINATARIOS	 drop constraint 	FK_ENV_DOCUMENTOSENVIOS_DESTIN	 ; 
alter table 	ENV_DOCUMENTOSDESTINATARIOS	 add constraint 	FK_ENV_DOCUMENTOSENVIOS_DESTIN	 foreign key (	IDINSTITUCION,IDENVIO,IDPERSONA	)	 references 	ENV_DESTINATARIOS	 (	IDINSTITUCION,IDENVIO,IDPERSONA	) 		 Deferrable;
alter table 	ENV_DOCUMENTOS	 drop constraint 	FK_ENV_DOCUMENTOS_ENV_ENVIOS	 ; 
alter table 	ENV_DOCUMENTOS	 add constraint 	FK_ENV_DOCUMENTOS_ENV_ENVIOS	 foreign key (	IDINSTITUCION,IDENVIO	)	 references 	ENV_ENVIOS	 (	IDINSTITUCION,IDENVIO	) 	 on delete cascade	 Deferrable;
alter table 	ENV_DESTPROGRAMINFORMES	 drop constraint 	FK_DESTPROGIN_PROGRAMINFORMES	 ; 
alter table 	ENV_DESTPROGRAMINFORMES	 drop constraint 	FK_DESTPROGINF_ENVIOPROG	 ; 
alter table 	ENV_DESTPROGRAMINFORMES	 add constraint 	FK_DESTPROGIN_PROGRAMINFORMES	 foreign key (	IDINSTITUCION,IDPROGRAM,IDENVIO	)	 references 	ENV_PROGRAMINFORMES	 (	IDINSTITUCION,IDPROGRAM,IDENVIO	) 		 Deferrable;
alter table 	ENV_DESTPROGRAMINFORMES	 add constraint 	FK_DESTPROGINF_ENVIOPROG	 foreign key (	IDINSTITUCION,IDENVIO	)	 references 	ENV_ENVIOPROGRAMADO	 (	IDINSTITUCION,IDENVIO	) 		 Deferrable;
alter table 	ENV_DESTINATARIOS	 drop constraint 	FK_ENV_DESTINAT_CEN_INSTITUCIO	 ; 
alter table 	ENV_DESTINATARIOS	 drop constraint 	FK_ENV_DESTINATARIOS_ENVIOS	 ; 
alter table 	ENV_DESTINATARIOS	 drop constraint 	FK_ENV_DESTINATARIOS_CEN_PROVI	 ; 
alter table 	ENV_DESTINATARIOS	 drop constraint 	FK_ENV_DESTINATARIOS_CEN_POBLA	 ; 
alter table 	ENV_DESTINATARIOS	 drop constraint 	FK_ENV_DESTINATARIOS_CEN_PAIS	 ; 
alter table 	ENV_DESTINATARIOS	 add constraint 	FK_ENV_DESTINAT_CEN_INSTITUCIO	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	ENV_DESTINATARIOS	 add constraint 	FK_ENV_DESTINATARIOS_ENVIOS	 foreign key (	IDINSTITUCION,IDENVIO	)	 references 	ENV_ENVIOS	 (	IDINSTITUCION,IDENVIO	) 		 Deferrable;
alter table 	ENV_DESTINATARIOS	 add constraint 	FK_ENV_DESTINATARIOS_CEN_PROVI	 foreign key (	IDPROVINCIA	)	 references 	CEN_PROVINCIAS	 (	IDPROVINCIA	) 		 Deferrable;
alter table 	ENV_DESTINATARIOS	 add constraint 	FK_ENV_DESTINATARIOS_CEN_POBLA	 foreign key (	IDPOBLACION	)	 references 	CEN_POBLACIONES	 (	IDPOBLACION	) 		 Deferrable;
alter table 	ENV_DESTINATARIOS	 add constraint 	FK_ENV_DESTINATARIOS_CEN_PAIS	 foreign key (	IDPAIS	)	 references 	CEN_PAIS	 (	IDPAIS	) 		 Deferrable;
alter table 	ENV_COMUNICACIONMOROSOS	 drop constraint 	FK_ENV_COMUNICACIONMOROSOS_FAC	 ; 
alter table 	ENV_COMUNICACIONMOROSOS	 drop constraint 	FK_ENV_COMUNICACIONMOROSOS_ENV	 ; 
alter table 	ENV_COMUNICACIONMOROSOS	 drop constraint 	FK_ENV_COMUNICACIONMOROSOS_CLI	 ; 
alter table 	ENV_COMUNICACIONMOROSOS	 add constraint 	FK_ENV_COMUNICACIONMOROSOS_FAC	 foreign key (	IDFACTURA,IDINSTITUCION	)	 references 	FAC_FACTURA	 (	IDFACTURA,IDINSTITUCION	) 		 Deferrable;
alter table 	ENV_COMUNICACIONMOROSOS	 add constraint 	FK_ENV_COMUNICACIONMOROSOS_ENV	 foreign key (	IDENVIODOC,IDINSTITUCION	)	 references 	ENV_ENVIOS	 (	IDENVIO,IDINSTITUCION	) 		 Deferrable;
alter table 	ENV_COMUNICACIONMOROSOS	 add constraint 	FK_ENV_COMUNICACIONMOROSOS_CLI	 foreign key (	IDPERSONA,IDINSTITUCION	)	 references 	CEN_CLIENTE	 (	IDPERSONA,IDINSTITUCION	) 		 Deferrable;
alter table 	ENV_COMPONENTESLISTACORREO	 drop constraint 	FK_ENV_COMPONENTESL_CEN_CLIENT	 ; 
alter table 	ENV_COMPONENTESLISTACORREO	 drop constraint 	FK_ENV_COMPONENTESLISTA_LISTA	 ; 
alter table 	ENV_COMPONENTESLISTACORREO	 add constraint 	FK_ENV_COMPONENTESL_CEN_CLIENT	 foreign key (	IDPERSONA,IDINSTITUCION	)	 references 	CEN_CLIENTE	 (	IDPERSONA,IDINSTITUCION	) 		 Deferrable;
alter table 	ENV_COMPONENTESLISTACORREO	 add constraint 	FK_ENV_COMPONENTESLISTA_LISTA	 foreign key (	IDLISTACORREO,IDINSTITUCION	)	 references 	ENV_LISTACORREOS	 (	IDLISTACORREO,IDINSTITUCION	) 		 Deferrable;
alter table 	ENV_CLAVEPROG	 drop constraint 	FK_ENV_CLAVEPROG_TIPO	 ; 
alter table 	ENV_CLAVEPROG	 add constraint 	FK_ENV_CLAVEPROG_TIPO	 foreign key (	IDTIPOINFORME	)	 references 	ADM_TIPOINFORME	 (	IDTIPOINFORME	) 		 Deferrable;
alter table 	ENV_CAMPOSPLANTILLA	 drop constraint 	FK_ENV_CAMPOSTIPOS_ENV_FORMATO	 ; 
alter table 	ENV_CAMPOSPLANTILLA	 drop constraint 	FK_ENV_CAMPOSTIPOSENVIOS_CAMPO	 ; 
alter table 	ENV_CAMPOSPLANTILLA	 drop constraint 	FK_ENV_CAMPOSPLAN_PLANTILLAENV	 ; 
alter table 	ENV_CAMPOSPLANTILLA	 add constraint 	FK_ENV_CAMPOSTIPOS_ENV_FORMATO	 foreign key (	IDFORMATO,TIPOCAMPO	)	 references 	CER_FORMATOS	 (	IDFORMATO,TIPOCAMPO	) 		 Deferrable;
alter table 	ENV_CAMPOSPLANTILLA	 add constraint 	FK_ENV_CAMPOSTIPOSENVIOS_CAMPO	 foreign key (	TIPOCAMPO,IDCAMPO	)	 references 	ENV_CAMPOS	 (	TIPOCAMPO,IDCAMPO	) 		 Deferrable;
alter table 	ENV_CAMPOSPLANTILLA	 add constraint 	FK_ENV_CAMPOSPLAN_PLANTILLAENV	 foreign key (	IDPLANTILLAENVIOS,IDINSTITUCION,IDTIPOENVIOS	)	 references 	ENV_PLANTILLASENVIOS	 (	IDPLANTILLAENVIOS,IDINSTITUCION,IDTIPOENVIOS	) 		 Deferrable;
alter table 	ENV_CAMPOSENVIOS	 drop constraint 	FK_ENV_CAMPOSENVIOS_ENV_CAMPOS	 ; 
alter table 	ENV_CAMPOSENVIOS	 drop constraint 	FK_ENV_CAMPOSENVIOS_ENVIOS	 ; 
alter table 	ENV_CAMPOSENVIOS	 drop constraint 	FK_ENV_CAMPOSENVIOS_CER_FORMAT	 ; 
alter table 	ENV_CAMPOSENVIOS	 add constraint 	FK_ENV_CAMPOSENVIOS_ENV_CAMPOS	 foreign key (	TIPOCAMPO,IDCAMPO	)	 references 	ENV_CAMPOS	 (	TIPOCAMPO,IDCAMPO	) 		 Deferrable;
alter table 	ENV_CAMPOSENVIOS	 add constraint 	FK_ENV_CAMPOSENVIOS_ENVIOS	 foreign key (	IDENVIO,IDINSTITUCION	)	 references 	ENV_ENVIOS	 (	IDENVIO,IDINSTITUCION	) 		 Deferrable;
alter table 	ENV_CAMPOSENVIOS	 add constraint 	FK_ENV_CAMPOSENVIOS_CER_FORMAT	 foreign key (	IDFORMATO,TIPOCAMPO	)	 references 	CER_FORMATOS	 (	IDFORMATO,TIPOCAMPO	) 		 Deferrable;
alter table 	ENV_CAMPOCLAVEPROG	 drop constraint 	FK_ENV_CAMPOCLAVEPROG	 ; 
alter table 	ENV_CAMPOCLAVEPROG	 add constraint 	FK_ENV_CAMPOCLAVEPROG	 foreign key (	CLAVE,IDTIPOINFORME	)	 references 	ENV_CLAVEPROG	 (	CLAVE,IDTIPOINFORME	) 		 Deferrable;
alter table 	ECOM_SOLSUSPROCEDIMIENTO	 drop constraint 	FK_ECOM_SOLSUSPROCEDIMIENTO_2	 ; 
alter table 	ECOM_SOLSUSPROCEDIMIENTO	 drop constraint 	FK_ECOM_SOLSUSPROCEDIMIENTO_1	 ; 
alter table 	ECOM_SOLSUSPROCEDIMIENTO	 add constraint 	FK_ECOM_SOLSUSPROCEDIMIENTO_2	 foreign key (	IDENVIO,IDINSTITUCION	)	 references 	ENV_ENVIOS	 (	IDENVIO,IDINSTITUCION	) 		 Deferrable;
alter table 	ECOM_SOLSUSPROCEDIMIENTO	 add constraint 	FK_ECOM_SOLSUSPROCEDIMIENTO_1	 foreign key (	NUMEROEJG,IDINSTITUCION,IDTIPOEJG,ANIOEJG	)	 references 	SCS_EJG	 (	NUMERO,IDINSTITUCION,IDTIPOEJG,ANIO	) 		 Deferrable;
alter table 	ECOM_SOLIMPUGRESOLUCIONAJG	 drop constraint 	FK_ECOM_SOLIMPUGRESOLAJG_2	 ; 
alter table 	ECOM_SOLIMPUGRESOLUCIONAJG	 drop constraint 	FK_ECOM_SOLIMPUGRESOLAJG_1	 ; 
alter table 	ECOM_SOLIMPUGRESOLUCIONAJG	 add constraint 	FK_ECOM_SOLIMPUGRESOLAJG_2	 foreign key (	IDENVIO,IDINSTITUCION	)	 references 	ENV_ENVIOS	 (	IDENVIO,IDINSTITUCION	) 		 Deferrable;
alter table 	ECOM_SOLIMPUGRESOLUCIONAJG	 add constraint 	FK_ECOM_SOLIMPUGRESOLAJG_1	 foreign key (	NUMEROEJG,IDINSTITUCION,IDTIPOEJG,ANIOEJG	)	 references 	SCS_EJG	 (	NUMERO,IDINSTITUCION,IDTIPOEJG,ANIO	) 		 Deferrable;
alter table 	ECOM_SOLDESIGNAPROVISIONAL	 drop constraint 	FK_SOLDESIGNAPROVISIONAL_6	 ; 
alter table 	ECOM_SOLDESIGNAPROVISIONAL	 drop constraint 	FK_SOLDESIGNAPROVISIONAL_5	 ; 
alter table 	ECOM_SOLDESIGNAPROVISIONAL	 drop constraint 	FK_SOLDESIGNAPROVISIONAL_4	 ; 
alter table 	ECOM_SOLDESIGNAPROVISIONAL	 drop constraint 	FK_SOLDESIGNAPROVISIONAL_3	 ; 
alter table 	ECOM_SOLDESIGNAPROVISIONAL	 drop constraint 	FK_SOLDESIGNAPROVISIONAL_2	 ; 
alter table 	ECOM_SOLDESIGNAPROVISIONAL	 drop constraint 	FK_SOLDESIGNAPROVISIONAL_1	 ; 
alter table 	ECOM_SOLDESIGNAPROVISIONAL	 add constraint 	FK_SOLDESIGNAPROVISIONAL_6	 foreign key (	IDECOMCOLA	)	 references 	ECOM_COLA	 (	IDECOMCOLA	) 		 Deferrable;
alter table 	ECOM_SOLDESIGNAPROVISIONAL	 add constraint 	FK_SOLDESIGNAPROVISIONAL_5	 foreign key (	DESIGNANEWIDTURNO,IDINSTITUCION	)	 references 	SCS_TURNO	 (	IDTURNO,IDINSTITUCION	) 		 Deferrable;
alter table 	ECOM_SOLDESIGNAPROVISIONAL	 add constraint 	FK_SOLDESIGNAPROVISIONAL_4	 foreign key (	IDINSTITUCION,IDENVIO	)	 references 	ENV_ENTRADA_ENVIOS	 (	IDINSTITUCION,IDENVIO	) 		 Deferrable;
alter table 	ECOM_SOLDESIGNAPROVISIONAL	 add constraint 	FK_SOLDESIGNAPROVISIONAL_3	 foreign key (	DESIGNASELNUMERO,IDINSTITUCION,DESIGNASELIDTURNO,DESIGNASELANIO	)	 references 	SCS_DESIGNA	 (	NUMERO,IDINSTITUCION,IDTURNO,ANIO	) 		 Deferrable;
alter table 	ECOM_SOLDESIGNAPROVISIONAL	 add constraint 	FK_SOLDESIGNAPROVISIONAL_2	 foreign key (	EJGNEWNUMERO,IDINSTITUCION,EJGNEWIDTIPO,EJGNEWANIO	)	 references 	SCS_EJG	 (	NUMERO,IDINSTITUCION,IDTIPOEJG,ANIO	) 		 Deferrable;
alter table 	ECOM_SOLDESIGNAPROVISIONAL	 add constraint 	FK_SOLDESIGNAPROVISIONAL_1	 foreign key (	EJGSELNUMERO,IDINSTITUCION,EJGSELIDTIPO,EJGSELANIO	)	 references 	SCS_EJG	 (	NUMERO,IDINSTITUCION,IDTIPOEJG,ANIO	) 		 Deferrable;
alter table 	ECOM_RESPSOLSUSPROCEDIMIENTO	 drop constraint 	FK_RESPSOLSUSPROCEDIMIENTO_6	 ; 
alter table 	ECOM_RESPSOLSUSPROCEDIMIENTO	 drop constraint 	FK_RESPSOLSUSPROCEDIMIENTO_4	 ; 
alter table 	ECOM_RESPSOLSUSPROCEDIMIENTO	 drop constraint 	FK_RESPSOLSUSPROCEDIMIENTO_2	 ; 
alter table 	ECOM_RESPSOLSUSPROCEDIMIENTO	 drop constraint 	FK_RESPSOLSUSPROCEDIMIENTO_1	 ; 
alter table 	ECOM_RESPSOLSUSPROCEDIMIENTO	 add constraint 	FK_RESPSOLSUSPROCEDIMIENTO_6	 foreign key (	IDECOMCOLA	)	 references 	ECOM_COLA	 (	IDECOMCOLA	) 		 Deferrable;
alter table 	ECOM_RESPSOLSUSPROCEDIMIENTO	 add constraint 	FK_RESPSOLSUSPROCEDIMIENTO_4	 foreign key (	IDINSTITUCION,IDENVIO	)	 references 	ENV_ENTRADA_ENVIOS	 (	IDINSTITUCION,IDENVIO	) 		 Deferrable;
alter table 	ECOM_RESPSOLSUSPROCEDIMIENTO	 add constraint 	FK_RESPSOLSUSPROCEDIMIENTO_2	 foreign key (	EJGNEWNUMERO,IDINSTITUCION,EJGNEWIDTIPO,EJGNEWANIO	)	 references 	SCS_EJG	 (	NUMERO,IDINSTITUCION,IDTIPOEJG,ANIO	) 		 Deferrable;
alter table 	ECOM_RESPSOLSUSPROCEDIMIENTO	 add constraint 	FK_RESPSOLSUSPROCEDIMIENTO_1	 foreign key (	EJGSELNUMERO,IDINSTITUCION,EJGSELIDTIPO,EJGSELANIO	)	 references 	SCS_EJG	 (	NUMERO,IDINSTITUCION,IDTIPOEJG,ANIO	) 		 Deferrable;
alter table 	ECOM_RESOLUCIONIMPUGNACION	 drop constraint 	FK_RESOLUCIONIMPUGNACION_3	 ; 
alter table 	ECOM_RESOLUCIONIMPUGNACION	 drop constraint 	FK_RESOLUCIONIMPUGNACION_2	 ; 
alter table 	ECOM_RESOLUCIONIMPUGNACION	 drop constraint 	FK_ECOM_RESOLIMPUGNACION_1	 ; 
alter table 	ECOM_RESOLUCIONIMPUGNACION	 add constraint 	FK_RESOLUCIONIMPUGNACION_3	 foreign key (	IDECOMCOLA	)	 references 	ECOM_COLA	 (	IDECOMCOLA	) 		 Deferrable;
alter table 	ECOM_RESOLUCIONIMPUGNACION	 add constraint 	FK_RESOLUCIONIMPUGNACION_2	 foreign key (	IDINSTITUCION,IDENVIO	)	 references 	ENV_ENTRADA_ENVIOS	 (	IDINSTITUCION,IDENVIO	) 		 Deferrable;
alter table 	ECOM_RESOLUCIONIMPUGNACION	 add constraint 	FK_ECOM_RESOLIMPUGNACION_1	 foreign key (	NUMEROEJG,IDINSTITUCION,IDTIPOEJG,ANIOEJG	)	 references 	SCS_EJG	 (	NUMERO,IDINSTITUCION,IDTIPOEJG,ANIO	) 		 Deferrable;
alter table 	ECOM_PROCESARESPUESTAS	 drop constraint 	FK_ECOM_PROCESARESPUESTAS_1	 ; 
alter table 	ECOM_PROCESARESPUESTAS	 add constraint 	FK_ECOM_PROCESARESPUESTAS_1	 foreign key (	IDINSTITUCION,IDENVIO	)	 references 	ENV_ENTRADA_ENVIOS	 (	IDINSTITUCION,IDENVIO	) 		 Deferrable;
alter table 	ECOM_OPERACION	 drop constraint 	FK_ECOM_OPERACIO_ECOM_SERVICIO	 ; 
alter table 	ECOM_OPERACION	 add constraint 	FK_ECOM_OPERACIO_ECOM_SERVICIO	 foreign key (	IDSERVICIO	)	 references 	ECOM_SERVICIO	 (	IDSERVICIO	) 		 Deferrable;
alter table 	ECOM_NOTIFICACIONERROR	 drop constraint 	FK_ECOM_NOTIFICACIONERROR_2	 ; 
alter table 	ECOM_NOTIFICACIONERROR	 drop constraint 	FK_ECOM_NOTIFICACIONERROR_1	 ; 
alter table 	ECOM_NOTIFICACIONERROR	 add constraint 	FK_ECOM_NOTIFICACIONERROR_2	 foreign key (	IDECOMCOLA	)	 references 	ECOM_COLA	 (	IDECOMCOLA	) 		 Deferrable;
alter table 	ECOM_NOTIFICACIONERROR	 add constraint 	FK_ECOM_NOTIFICACIONERROR_1	 foreign key (	IDINSTITUCION,IDENVIO	)	 references 	ENV_ENTRADA_ENVIOS	 (	IDINSTITUCION,IDENVIO	) 		 Deferrable;
alter table 	ECOM_DESIGNAPROVISIONAL	 drop constraint 	FK_ECOM_DESIGNAPROVISIONAL_6	 ; 
alter table 	ECOM_DESIGNAPROVISIONAL	 drop constraint 	FK_ECOM_DESIGNAPROVISIONAL_5	 ; 
alter table 	ECOM_DESIGNAPROVISIONAL	 drop constraint 	FK_ECOM_DESIGNAPROVISIONAL_4	 ; 
alter table 	ECOM_DESIGNAPROVISIONAL	 drop constraint 	FK_ECOM_DESIGNAPROVISIONAL_3	 ; 
alter table 	ECOM_DESIGNAPROVISIONAL	 drop constraint 	FK_ECOM_DESIGNAPROVISIONAL_2	 ; 
alter table 	ECOM_DESIGNAPROVISIONAL	 drop constraint 	FK_ECOM_DESIGNAPROVISIONAL_1	 ; 
alter table 	ECOM_DESIGNAPROVISIONAL	 add constraint 	FK_ECOM_DESIGNAPROVISIONAL_6	 foreign key (	IDECOMCOLA	)	 references 	ECOM_COLA	 (	IDECOMCOLA	) 		 Deferrable;
alter table 	ECOM_DESIGNAPROVISIONAL	 add constraint 	FK_ECOM_DESIGNAPROVISIONAL_5	 foreign key (	IDPROCURADOR,IDINSTITUCION	)	 references 	SCS_PROCURADOR	 (	IDPROCURADOR,IDINSTITUCION	) 		 Deferrable;
alter table 	ECOM_DESIGNAPROVISIONAL	 add constraint 	FK_ECOM_DESIGNAPROVISIONAL_4	 foreign key (	IDPERSONA,IDINSTITUCION_LETRADO	)	 references 	CEN_CLIENTE	 (	IDPERSONA,IDINSTITUCION	) 		 Deferrable;
alter table 	ECOM_DESIGNAPROVISIONAL	 add constraint 	FK_ECOM_DESIGNAPROVISIONAL_3	 foreign key (	IDENVIO,IDINSTITUCION	)	 references 	ENV_ENVIOS	 (	IDENVIO,IDINSTITUCION	) 		 Deferrable;
alter table 	ECOM_DESIGNAPROVISIONAL	 add constraint 	FK_ECOM_DESIGNAPROVISIONAL_2	 foreign key (	NUMEROEJG,IDINSTITUCION,IDTIPOEJG,ANIOEJG	)	 references 	SCS_EJG	 (	NUMERO,IDINSTITUCION,IDTIPOEJG,ANIO	) 		 Deferrable;
alter table 	ECOM_DESIGNAPROVISIONAL	 add constraint 	FK_ECOM_DESIGNAPROVISIONAL_1	 foreign key (	NUMERO,IDINSTITUCION,IDTURNO,ANIO	)	 references 	SCS_DESIGNA	 (	NUMERO,IDINSTITUCION,IDTURNO,ANIO	) 		 Deferrable;
alter table 	ECOM_COMUNICACIONRESOLUCIONAJG	 drop constraint 	FK_ECOM_COMNRESOLUCIONAJG_2	 ; 
alter table 	ECOM_COMUNICACIONRESOLUCIONAJG	 drop constraint 	FK_ECOM_COMNRESOLUCIONAJG_1	 ; 
alter table 	ECOM_COMUNICACIONRESOLUCIONAJG	 add constraint 	FK_ECOM_COMNRESOLUCIONAJG_2	 foreign key (	IDENVIO,IDINSTITUCION	)	 references 	ENV_ENVIOS	 (	IDENVIO,IDINSTITUCION	) 		 Deferrable;
alter table 	ECOM_COMUNICACIONRESOLUCIONAJG	 add constraint 	FK_ECOM_COMNRESOLUCIONAJG_1	 foreign key (	NUMEROEJG,IDINSTITUCION,IDTIPOEJG,ANIOEJG	)	 references 	SCS_EJG	 (	NUMERO,IDINSTITUCION,IDTIPOEJG,ANIO	) 		 Deferrable;
alter table 	ECOM_COLA_PARAMETROS	 drop constraint 	FK_ECOM_COLA_PARAM_ECOMCOLA	 ; 
alter table 	ECOM_COLA_PARAMETROS	 add constraint 	FK_ECOM_COLA_PARAM_ECOMCOLA	 foreign key (	IDECOMCOLA	)	 references 	ECOM_COLA	 (	IDECOMCOLA	) 		 Deferrable;
alter table 	ECOM_COLA	 drop constraint 	FK_ECOM_COLA_ECOM_OPERACION	 ; 
alter table 	ECOM_COLA	 drop constraint 	FK_ECOM_COLA_ECOM_ESTADOSCOLA	 ; 
alter table 	ECOM_COLA	 add constraint 	FK_ECOM_COLA_ECOM_OPERACION	 foreign key (	IDOPERACION	)	 references 	ECOM_OPERACION	 (	IDOPERACION	) 		 Deferrable;
alter table 	ECOM_COLA	 add constraint 	FK_ECOM_COLA_ECOM_ESTADOSCOLA	 foreign key (	IDESTADOCOLA	)	 references 	ECOM_ESTADOSCOLA	 (	IDESTADOCOLA	) 		 Deferrable;
alter table 	ECOM_CEN_WS_PAGINA	 drop constraint 	FK_ECOM_CEN_WS_P_ECOM_CEN_WS_E	 ; 
alter table 	ECOM_CEN_WS_PAGINA	 drop constraint 	FK_ECOM_CEN_PAGINA_XMLRESP	 ; 
alter table 	ECOM_CEN_WS_PAGINA	 drop constraint 	FK_ECOM_CEN_PAGINA_XMLENVIO	 ; 
alter table 	ECOM_CEN_WS_PAGINA	 drop constraint 	FK_ECOM_CEN_PAGINA_ERRVAL	 ; 
alter table 	ECOM_CEN_WS_PAGINA	 add constraint 	FK_ECOM_CEN_WS_P_ECOM_CEN_WS_E	 foreign key (	IDCENWSENVIO	)	 references 	ECOM_CEN_WS_ENVIO	 (	IDCENWSENVIO	) 		 Deferrable;
alter table 	ECOM_CEN_WS_PAGINA	 add constraint 	FK_ECOM_CEN_PAGINA_XMLRESP	 foreign key (	IDCENXMLRESPUESTA	)	 references 	ECOM_CEN_XML_RESPUESTA	 (	IDCENXMLRESPUESTA	) 		 Deferrable;
alter table 	ECOM_CEN_WS_PAGINA	 add constraint 	FK_ECOM_CEN_PAGINA_XMLENVIO	 foreign key (	IDCENXMLENVIO	)	 references 	ECOM_CEN_XML_ENVIO	 (	IDCENXMLENVIO	) 		 Deferrable;
alter table 	ECOM_CEN_WS_PAGINA	 add constraint 	FK_ECOM_CEN_PAGINA_ERRVAL	 foreign key (	IDCENERRVALIDA	)	 references 	ECOM_CEN_ERROR_VALIDA	 (	IDCENERRVALIDA	) 		 Deferrable;
alter table 	ECOM_CEN_WS_ENVIO	 drop constraint 	FK_ECOM_WSENVIO_TIPOENVIO	 ; 
alter table 	ECOM_CEN_WS_ENVIO	 drop constraint 	FK_ECOM_WSENVIO_ESTADO	 ; 
alter table 	ECOM_CEN_WS_ENVIO	 add constraint 	FK_ECOM_WSENVIO_TIPOENVIO	 foreign key (	IDTIPOENVIO	)	 references 	ECOM_CEN_TIPOENVIO	 (	IDTIPOENVIO	) 		 Deferrable;
alter table 	ECOM_CEN_WS_ENVIO	 add constraint 	FK_ECOM_WSENVIO_ESTADO	 foreign key (	IDESTADOENVIO	)	 references 	ECOM_CEN_MAESTRO_ESTAD_ENVIO	 (	IDESTADOENVIO	) 		 Deferrable;
alter table 	ECOM_CEN_EX_PERFIL	 drop constraint 	FK_ECOM_CEN_EX_COLUMN	 ; 
alter table 	ECOM_CEN_EX_PERFIL	 add constraint 	FK_ECOM_CEN_EX_COLUMN	 foreign key (	ID_COL	)	 references 	ECOM_CEN_EX_COLUMN	 (	ID_COL	) 		 Deferrable;
alter table 	ECOM_CEN_DATOS_INCIDENCIAS	 drop constraint 	FK_ECOM_CENSO_DA_ECOM_CENSO_MA	 ; 
alter table 	ECOM_CEN_DATOS_INCIDENCIAS	 drop constraint 	FK_ECOM_CENSO_DA_ECOM_CENSO_DA	 ; 
alter table 	ECOM_CEN_DATOS_INCIDENCIAS	 add constraint 	FK_ECOM_CENSO_DA_ECOM_CENSO_MA	 foreign key (	IDCENSOMAESTROINCIDENCIAS	)	 references 	ECOM_CEN_MAESTRO_INCIDENC	 (	IDCENSOMAESTROINCIDENCIAS	) 		 Deferrable;
alter table 	ECOM_CEN_DATOS_INCIDENCIAS	 add constraint 	FK_ECOM_CENSO_DA_ECOM_CENSO_DA	 foreign key (	IDCENSODATOS	)	 references 	ECOM_CEN_DATOS	 (	IDCENSODATOS	) 		 Deferrable;
alter table 	ECOM_CEN_DATOS	 drop constraint 	FK_ECOM_CENSO_DA_ECOM_CENSO_TI	 ; 
alter table 	ECOM_CEN_DATOS	 drop constraint 	FK_ECOM_CENSO_DA_ECOM_CENSO_SI	 ; 
alter table 	ECOM_CEN_DATOS	 drop constraint 	FK_ECOM_CENSO_CO_ECOM_CENSO_DI	 ; 
alter table 	ECOM_CEN_DATOS	 drop constraint 	FK_ECOMCEN_DATOS_MAEST_ESTADOS	 ; 
alter table 	ECOM_CEN_DATOS	 add constraint 	FK_ECOM_CENSO_DA_ECOM_CENSO_TI	 foreign key (	IDCENSOTIPOIDENTIFICACION	)	 references 	ECOM_CEN_TIPOIDENTIFICACION	 (	IDCENSOTIPOIDENTIFICACION	) 		 Deferrable;
alter table 	ECOM_CEN_DATOS	 add constraint 	FK_ECOM_CENSO_DA_ECOM_CENSO_SI	 foreign key (	IDECOMCENSOSITUACIONEJER	)	 references 	ECOM_CEN_SITUACIONEJERCIENTE	 (	IDECOMCENSOSITUACIONEJER	) 		 Deferrable;
alter table 	ECOM_CEN_DATOS	 add constraint 	FK_ECOM_CENSO_CO_ECOM_CENSO_DI	 foreign key (	IDCENSODIRECCION	)	 references 	ECOM_CEN_DIRECCION	 (	IDCENSODIRECCION	) 		 Deferrable;
alter table 	ECOM_CEN_DATOS	 add constraint 	FK_ECOMCEN_DATOS_MAEST_ESTADOS	 foreign key (	IDESTADOCOLEGIADO	)	 references 	ECOM_CEN_MAESTRO_ESTAD_COLEG	 (	IDESTADOCOLEGIADO	) 		 Deferrable;
alter table 	ECOM_CEN_COLEGIADO_HIST	 drop constraint 	FK_ECOM_CENSO_CO_ECOM_CENSO_CO	 ; 
alter table 	ECOM_CEN_COLEGIADO_HIST	 drop constraint 	FK_ECOMCENCOLHIST_ECOMCEN_DAT	 ; 
alter table 	ECOM_CEN_COLEGIADO_HIST	 add constraint 	FK_ECOM_CENSO_CO_ECOM_CENSO_CO	 foreign key (	IDCENSOCOLEGIADO	)	 references 	ECOM_CEN_COLEGIADO	 (	IDCENSOCOLEGIADO	) 		 Deferrable;
alter table 	ECOM_CEN_COLEGIADO_HIST	 add constraint 	FK_ECOMCENCOLHIST_ECOMCEN_DAT	 foreign key (	IDCENSODATOS	)	 references 	ECOM_CEN_DATOS	 (	IDCENSODATOS	) 		 Deferrable;
alter table 	ECOM_CEN_COLEGIADO	 drop constraint 	FK_ECOM_CENSO_CO_ECOM_CENSO_DA	 ; 
alter table 	ECOM_CEN_COLEGIADO	 drop constraint 	FK_ECOM_CENSO_CO_CEN_COLEGIADO	 ; 
alter table 	ECOM_CEN_COLEGIADO	 drop constraint 	FK_ECOM_CENCOL_ECOM_CEN_WSENV	 ; 
alter table 	ECOM_CEN_COLEGIADO	 add constraint 	FK_ECOM_CENSO_CO_ECOM_CENSO_DA	 foreign key (	IDCENSODATOS	)	 references 	ECOM_CEN_DATOS	 (	IDCENSODATOS	) 		 Deferrable;
alter table 	ECOM_CEN_COLEGIADO	 add constraint 	FK_ECOM_CENSO_CO_CEN_COLEGIADO	 foreign key (	IDINSTITUCION,IDPERSONA	)	 references 	CEN_COLEGIADO	 (	IDINSTITUCION,IDPERSONA	) 		 Deferrable;
alter table 	ECOM_CEN_COLEGIADO	 add constraint 	FK_ECOM_CENCOL_ECOM_CEN_WSENV	 foreign key (	IDCENSOWSPAGINA	)	 references 	ECOM_CEN_WS_PAGINA	 (	IDCENSOWSPAGINA	) 		 Deferrable;
alter table 	ECOM_CENSO_SINONIMOS	 drop constraint 	FK_ECOM_CENSO_SI_CEN_POBLACION	 ; 
alter table 	ECOM_CENSO_SINONIMOS	 add constraint 	FK_ECOM_CENSO_SI_CEN_POBLACION	 foreign key (	IDPOBLACION	)	 references 	CEN_POBLACIONES	 (	IDPOBLACION	) 		 Deferrable;
alter table 	CV_LLAMADA	 drop constraint 	FK_CV_LLAMADA_COLEGIO	 ; 
alter table 	CV_LLAMADA	 add constraint 	FK_CV_LLAMADA_COLEGIO	 foreign key (	IDINSTITUCION	)	 references 	CV_COLEGIOABOGADOS	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	CV_GUARDIACOLEGIADO	 drop constraint 	FK_CV_GUARDIACOLEGIADO_COLEGIO	 ; 
alter table 	CV_GUARDIACOLEGIADO	 add constraint 	FK_CV_GUARDIACOLEGIADO_COLEGIO	 foreign key (	IDINSTITUCION	)	 references 	CV_COLEGIOABOGADOS	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	CON_TIPOCAMPOCONSULTA	 drop constraint 	FK_CON_TIPOCAMPOCONSULTA_BASE	 ; 
alter table 	CON_TIPOCAMPOCONSULTA	 add constraint 	FK_CON_TIPOCAMPOCONSULTA_BASE	 foreign key (	IDBASE	)	 references 	CON_BASECONSULTA	 (	IDBASE	) 		 Deferrable;
alter table 	CON_TABLACONSULTA	 drop constraint 	FK_CON_TABLACONSULTA_BASE	 ; 
alter table 	CON_TABLACONSULTA	 add constraint 	FK_CON_TABLACONSULTA_BASE	 foreign key (	IDBASE	)	 references 	CON_BASECONSULTA	 (	IDBASE	) 		 Deferrable;
alter table 	CON_PLANTILLAIMPRESION	 drop constraint 	FK_CON_PLANTILLAIMPRESION_MODU	 ; 
alter table 	CON_PLANTILLAIMPRESION	 add constraint 	FK_CON_PLANTILLAIMPRESION_MODU	 foreign key (	IDMODULO	)	 references 	CON_MODULO	 (	IDMODULO	) 		 Deferrable;
alter table 	CON_JOINTABLASCONSULTA	 drop constraint 	FK_CON_JOINTABLASCONSULTA_TABL	 ; 
alter table 	CON_JOINTABLASCONSULTA	 drop constraint 	FK_CON_JOINTABLASCONSULTA_CONS	 ; 
alter table 	CON_JOINTABLASCONSULTA	 add constraint 	FK_CON_JOINTABLASCONSULTA_TABL	 foreign key (	IDTABLA1	)	 references 	CON_TABLACONSULTA	 (	IDTABLA	) 		 Deferrable;
alter table 	CON_JOINTABLASCONSULTA	 add constraint 	FK_CON_JOINTABLASCONSULTA_CONS	 foreign key (	IDTABLA2	)	 references 	CON_TABLACONSULTA	 (	IDTABLA	) 		 Deferrable;
alter table 	CON_CRITERIOSDINAMICOS	 drop constraint 	FK_CON_CDINAMICOS_CON_CONSULTA	 ; 
alter table 	CON_CRITERIOSDINAMICOS	 drop constraint 	FK_CON_CDINAMICOS_CON_CAMPOCON	 ; 
alter table 	CON_CRITERIOSDINAMICOS	 add constraint 	FK_CON_CDINAMICOS_CON_CONSULTA	 foreign key (	IDCONSULTA,IDINSTITUCION	)	 references 	CON_CONSULTA	 (	IDCONSULTA,IDINSTITUCION	) 		 Deferrable;
alter table 	CON_CRITERIOSDINAMICOS	 add constraint 	FK_CON_CDINAMICOS_CON_CAMPOCON	 foreign key (	IDCAMPO	)	 references 	CON_CAMPOCONSULTA	 (	IDCAMPO	) 		 Deferrable;
alter table 	CON_CRITERIOCONSULTA	 drop constraint 	FK_CON_CRITERIOCONSULTA_OPERAC	 ; 
alter table 	CON_CRITERIOCONSULTA	 drop constraint 	FK_CON_CRITERIOCONSULTA_CONSUL	 ; 
alter table 	CON_CRITERIOCONSULTA	 drop constraint 	FK_CON_CRITERIOCONSULTA_CAMPO	 ; 
alter table 	CON_CRITERIOCONSULTA	 add constraint 	FK_CON_CRITERIOCONSULTA_OPERAC	 foreign key (	IDOPERACION	)	 references 	CON_OPERACIONCONSULTA	 (	IDOPERACION	) 		 Deferrable;
alter table 	CON_CRITERIOCONSULTA	 add constraint 	FK_CON_CRITERIOCONSULTA_CONSUL	 foreign key (	IDCONSULTA,IDINSTITUCION	)	 references 	CON_CONSULTA	 (	IDCONSULTA,IDINSTITUCION	) 		 Deferrable;
alter table 	CON_CRITERIOCONSULTA	 add constraint 	FK_CON_CRITERIOCONSULTA_CAMPO	 foreign key (	IDCAMPO	)	 references 	CON_CAMPOCONSULTA	 (	IDCAMPO	) 		 Deferrable;
alter table 	CON_CONSULTAPERFIL	 drop constraint 	FK_CON_CONSULTAPERFIL_PERFIL	 ; 
alter table 	CON_CONSULTAPERFIL	 drop constraint 	FK_CON_CONSULTAPERFIL_CONSULTA	 ; 
alter table 	CON_CONSULTAPERFIL	 add constraint 	FK_CON_CONSULTAPERFIL_PERFIL	 foreign key (	IDPERFIL,IDINSTITUCION	)	 references 	ADM_PERFIL	 (	IDPERFIL,IDINSTITUCION	) 		 Deferrable;
alter table 	CON_CONSULTAPERFIL	 add constraint 	FK_CON_CONSULTAPERFIL_CONSULTA	 foreign key (	IDCONSULTA,IDINSTITUCION_CONSULTA	)	 references 	CON_CONSULTA	 (	IDCONSULTA,IDINSTITUCION	) 		 Deferrable;
alter table 	CON_CONSULTA	 drop constraint 	FK_CON_CONSULTA_TABLA	 ; 
alter table 	CON_CONSULTA	 drop constraint 	FK_CON_CONSULTA_MODULO	 ; 
alter table 	CON_CONSULTA	 add constraint 	FK_CON_CONSULTA_TABLA	 foreign key (	IDTABLA	)	 references 	CON_TABLACONSULTA	 (	IDTABLA	) 		 Deferrable;
alter table 	CON_CONSULTA	 add constraint 	FK_CON_CONSULTA_MODULO	 foreign key (	IDMODULO	)	 references 	CON_MODULO	 (	IDMODULO	) 		 Deferrable;
alter table 	CON_CAMPOSSALIDA	 drop constraint 	FK_CON_CAMPOSSALIDA_CONSULTA	 ; 
alter table 	CON_CAMPOSSALIDA	 drop constraint 	FK_CON_CAMPOSSALIDA_CAMPO	 ; 
alter table 	CON_CAMPOSSALIDA	 add constraint 	FK_CON_CAMPOSSALIDA_CONSULTA	 foreign key (	IDCONSULTA,IDINSTITUCION	)	 references 	CON_CONSULTA	 (	IDCONSULTA,IDINSTITUCION	) 		 Deferrable;
alter table 	CON_CAMPOSSALIDA	 add constraint 	FK_CON_CAMPOSSALIDA_CAMPO	 foreign key (	IDCAMPO	)	 references 	CON_CAMPOCONSULTA	 (	IDCAMPO	) 		 Deferrable;
alter table 	CON_CAMPOSORDENACION	 drop constraint 	FK_CON_CAMPOSORDENACION_CONSUL	 ; 
alter table 	CON_CAMPOSORDENACION	 drop constraint 	FK_CON_CAMPOSORDENACION_CAMPO	 ; 
alter table 	CON_CAMPOSORDENACION	 add constraint 	FK_CON_CAMPOSORDENACION_CONSUL	 foreign key (	IDCONSULTA,IDINSTITUCION	)	 references 	CON_CONSULTA	 (	IDCONSULTA,IDINSTITUCION	) 		 Deferrable;
alter table 	CON_CAMPOSORDENACION	 add constraint 	FK_CON_CAMPOSORDENACION_CAMPO	 foreign key (	IDCAMPO	)	 references 	CON_CAMPOCONSULTA	 (	IDCAMPO	) 		 Deferrable;
alter table 	CON_CAMPOSAGREGACION	 drop constraint 	FK_CON_CAMPOSAGREGACION_CONSUL	 ; 
alter table 	CON_CAMPOSAGREGACION	 drop constraint 	FK_CON_CAMPOSAGREGACION_CAMPOC	 ; 
alter table 	CON_CAMPOSAGREGACION	 add constraint 	FK_CON_CAMPOSAGREGACION_CONSUL	 foreign key (	IDCONSULTA,IDINSTITUCION	)	 references 	CON_CONSULTA	 (	IDCONSULTA,IDINSTITUCION	) 		 Deferrable;
alter table 	CON_CAMPOSAGREGACION	 add constraint 	FK_CON_CAMPOSAGREGACION_CAMPOC	 foreign key (	IDCAMPO	)	 references 	CON_CAMPOCONSULTA	 (	IDCAMPO	) 		 Deferrable;
alter table 	CON_CAMPOCONSULTA	 drop constraint 	FK_CON_CAMPOCONSULTA_TIPOCAMPO	 ; 
alter table 	CON_CAMPOCONSULTA	 drop constraint 	FK_CON_CAMPOCONSULTA_TABLACONS	 ; 
alter table 	CON_CAMPOCONSULTA	 add constraint 	FK_CON_CAMPOCONSULTA_TIPOCAMPO	 foreign key (	IDTIPOCAMPO	)	 references 	CON_TIPOCAMPOCONSULTA	 (	IDTIPOCAMPO	) 		 Deferrable;
alter table 	CON_CAMPOCONSULTA	 add constraint 	FK_CON_CAMPOCONSULTA_TABLACONS	 foreign key (	IDTABLA	)	 references 	CON_TABLACONSULTA	 (	IDTABLA	) 		 Deferrable;
alter table 	CER_SOLICITUDES_ACCION	 drop constraint 	FK_CER_SOLICITUDES_ACCION	 ; 
alter table 	CER_SOLICITUDES_ACCION	 add constraint 	FK_CER_SOLICITUDES_ACCION	 foreign key (	IDSOLICITUD,IDINSTITUCION	)	 references 	CER_SOLICITUDCERTIFICADOS	 (	IDSOLICITUD,IDINSTITUCION	) 		 Deferrable;
alter table 	CER_SOLICITUDCERTIFICADOSTEXTO	 drop constraint 	FK_CER_SOLCERTIRFTEXTO_SOL	 ; 
alter table 	CER_SOLICITUDCERTIFICADOSTEXTO	 add constraint 	FK_CER_SOLCERTIRFTEXTO_SOL	 foreign key (	IDSOLICITUD,IDINSTITUCION	)	 references 	CER_SOLICITUDCERTIFICADOS	 (	IDSOLICITUD,IDINSTITUCION	) 		 Deferrable;
alter table 	CER_SOLICITUDCERTIFICADOS	 drop constraint 	FK_CER_SOLICERTIF_PYS_PRODINST	 ; 
alter table 	CER_SOLICITUDCERTIFICADOS	 drop constraint 	FK_CER_SOLCERTIFIC_ENV_TIPOENV	 ; 
alter table 	CER_SOLICITUDCERTIFICADOS	 drop constraint 	FK_CER_SOLCERTIFIC_CEN_PERSONA	 ; 
alter table 	CER_SOLICITUDCERTIFICADOS	 drop constraint 	FK_BANCOS_CERCODIGOBANCO	 ; 
alter table 	CER_SOLICITUDCERTIFICADOS	 add constraint 	FK_CER_SOLICERTIF_PYS_PRODINST	 foreign key (	PPN_IDPRODUCTOINSTITUCION,IDINSTITUCION,PPN_IDTIPOPRODUCTO,PPN_IDPRODUCTO	)	 references 	PYS_PRODUCTOSINSTITUCION	 (	IDPRODUCTOINSTITUCION,IDINSTITUCION,IDTIPOPRODUCTO,IDPRODUCTO	) 		 Deferrable;
alter table 	CER_SOLICITUDCERTIFICADOS	 add constraint 	FK_CER_SOLCERTIFIC_ENV_TIPOENV	 foreign key (	IDTIPOENVIOS	)	 references 	ENV_TIPOENVIOS	 (	IDTIPOENVIOS	) 		 Deferrable;
alter table 	CER_SOLICITUDCERTIFICADOS	 add constraint 	FK_CER_SOLCERTIFIC_CEN_PERSONA	 foreign key (	IDPERSONA_DES	)	 references 	CEN_PERSONA	 (	IDPERSONA	) 		 Deferrable;
alter table 	CER_SOLICITUDCERTIFICADOS	 add constraint 	FK_BANCOS_CERCODIGOBANCO	 foreign key (	CBO_CODIGO	)	 references 	CEN_BANCOS	 (	CODIGO	) 		 Deferrable;
alter table 	CER_RELACION_PLANTILLAS	 drop constraint 	FK_CER_RELACION_PLAN_PADRE	 ; 
alter table 	CER_RELACION_PLANTILLAS	 drop constraint 	FK_CER_RELACION_PLANTILLA	 ; 
alter table 	CER_RELACION_PLANTILLAS	 add constraint 	FK_CER_RELACION_PLAN_PADRE	 foreign key (	IDPRODUCTOINSTITUCION,IDPLANTILLAPADRE,IDINSTITUCION,IDTIPOPRODUCTO,IDPRODUCTO	)	 references 	CER_PLANTILLAS	 (	IDPRODUCTOINSTITUCION,IDPLANTILLA,IDINSTITUCION,IDTIPOPRODUCTO,IDPRODUCTO	) 		 Deferrable;
alter table 	CER_RELACION_PLANTILLAS	 add constraint 	FK_CER_RELACION_PLANTILLA	 foreign key (	IDPRODUCTOINSTITUCION,IDPLANTILLA,IDINSTITUCION,IDTIPOPRODUCTO,IDPRODUCTO	)	 references 	CER_PLANTILLAS	 (	IDPRODUCTOINSTITUCION,IDPLANTILLA,IDINSTITUCION,IDTIPOPRODUCTO,IDPRODUCTO	) 		 Deferrable;
alter table 	CER_PRODUCINSTICAMPCERTIF	 drop constraint 	FK_PRODUCINSTICAMPCERTIF_CAMPO	 ; 
alter table 	CER_PRODUCINSTICAMPCERTIF	 drop constraint 	FK_CER_PRODUCINT_PRODUC	 ; 
alter table 	CER_PRODUCINSTICAMPCERTIF	 drop constraint 	FK_CER_PRODUCINT_FORMATOS	 ; 
alter table 	CER_PRODUCINSTICAMPCERTIF	 add constraint 	FK_PRODUCINSTICAMPCERTIF_CAMPO	 foreign key (	TIPOCAMPO,IDCAMPOCERTIFICADO	)	 references 	CER_CAMPOSCERTIFICADOS	 (	TIPOCAMPO,IDCAMPOCERTIFICADO	) 		 Deferrable;
alter table 	CER_PRODUCINSTICAMPCERTIF	 add constraint 	FK_CER_PRODUCINT_PRODUC	 foreign key (	IDPRODUCTOINSTITUCION,IDINSTITUCION,IDTIPOPRODUCTO,IDPRODUCTO	)	 references 	PYS_PRODUCTOSINSTITUCION	 (	IDPRODUCTOINSTITUCION,IDINSTITUCION,IDTIPOPRODUCTO,IDPRODUCTO	) 		 Deferrable;
alter table 	CER_PRODUCINSTICAMPCERTIF	 add constraint 	FK_CER_PRODUCINT_FORMATOS	 foreign key (	IDFORMATO,TIPOCAMPO	)	 references 	CER_FORMATOS	 (	IDFORMATO,TIPOCAMPO	) 		 Deferrable;
alter table 	CER_PLANTILLAS	 drop constraint 	FK_ENV_PLANTILLAS_PYS_PRODINST	 ; 
alter table 	CER_PLANTILLAS	 add constraint 	FK_ENV_PLANTILLAS_PYS_PRODINST	 foreign key (	IDPRODUCTOINSTITUCION,IDINSTITUCION,IDTIPOPRODUCTO,IDPRODUCTO	)	 references 	PYS_PRODUCTOSINSTITUCION	 (	IDPRODUCTOINSTITUCION,IDINSTITUCION,IDTIPOPRODUCTO,IDPRODUCTO	) 		 Deferrable;
alter table 	CER_ENVIOS	 drop constraint 	FK_CER_ENVIOS_PLANT_ENV	 ; 
alter table 	CER_ENVIOS	 drop constraint 	FK_CER_ENVIOS_PLANTILLA	 ; 
alter table 	CER_ENVIOS	 drop constraint 	FK_CER_ENVIOS	 ; 
alter table 	CER_ENVIOS	 add constraint 	FK_CER_ENVIOS_PLANT_ENV	 foreign key (	TIPOPLANTILLA,IDINSTITUCION,TIPOENVIO	)	 references 	ENV_PLANTILLASENVIOS	 (	IDPLANTILLAENVIOS,IDINSTITUCION,IDTIPOENVIOS	) 		 Deferrable;
alter table 	CER_ENVIOS	 add constraint 	FK_CER_ENVIOS_PLANTILLA	 foreign key (	PLANTILLA,IDINSTITUCION,TIPOENVIO,TIPOPLANTILLA	)	 references 	ENV_PLANTILLAGENERACION	 (	IDPLANTILLA,IDINSTITUCION,IDTIPOENVIOS,IDPLANTILLAENVIOS	) 		 Deferrable;
alter table 	CER_ENVIOS	 add constraint 	FK_CER_ENVIOS	 foreign key (	IDSOLICITUD,IDINSTITUCION	)	 references 	CER_SOLICITUDCERTIFICADOS	 (	IDSOLICITUD,IDINSTITUCION	) 		 Deferrable;
alter table 	CEN_TIPOSCVSUBTIPO2	 drop constraint 	FK_CEN_TIPOSCVSUBTIPO2_INST	 ; 
alter table 	CEN_TIPOSCVSUBTIPO2	 drop constraint 	FK_CEN_TIPOSCVSUBTIPO2_CV	 ; 
alter table 	CEN_TIPOSCVSUBTIPO2	 add constraint 	FK_CEN_TIPOSCVSUBTIPO2_INST	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	CEN_TIPOSCVSUBTIPO2	 add constraint 	FK_CEN_TIPOSCVSUBTIPO2_CV	 foreign key (	IDTIPOCV	)	 references 	CEN_TIPOSCV	 (	IDTIPOCV	) 		 Deferrable;
alter table 	CEN_TIPOSCVSUBTIPO1	 drop constraint 	FK_CEN_TIPOSCVSUBTIPO1_INST	 ; 
alter table 	CEN_TIPOSCVSUBTIPO1	 drop constraint 	FK_CEN_TIPOSCVSUBTIPO1_CV	 ; 
alter table 	CEN_TIPOSCVSUBTIPO1	 add constraint 	FK_CEN_TIPOSCVSUBTIPO1_INST	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	CEN_TIPOSCVSUBTIPO1	 add constraint 	FK_CEN_TIPOSCVSUBTIPO1_CV	 foreign key (	IDTIPOCV	)	 references 	CEN_TIPOSCV	 (	IDTIPOCV	) 		 Deferrable;
alter table 	CEN_TIPOCLIENTE_ACCESODATOS	 drop constraint 	FK_CEN_TIPOCLIENTE_TIPO	 ; 
alter table 	CEN_TIPOCLIENTE_ACCESODATOS	 drop constraint 	FK_CEN_TIPOCLIENTE_ACCESO	 ; 
alter table 	CEN_TIPOCLIENTE_ACCESODATOS	 add constraint 	FK_CEN_TIPOCLIENTE_TIPO	 foreign key (	IDTIPOCLIENTE	)	 references 	CEN_TIPOCLIENTE	 (	IDTIPOCLIENTE	) 		 Deferrable;
alter table 	CEN_TIPOCLIENTE_ACCESODATOS	 add constraint 	FK_CEN_TIPOCLIENTE_ACCESO	 foreign key (	CARACTER	)	 references 	CEN_ACCESODATOS	 (	CARACTER	) 		 Deferrable;
alter table 	CEN_SUCURSALES	 drop constraint 	FK_SUCURSALES_PLAZA	 ; 
alter table 	CEN_SUCURSALES	 add constraint 	FK_SUCURSALES_PLAZA	 foreign key (	COD_PLAZA	)	 references 	CEN_PLAZAS	 (	COD_PLAZA	) 		 Deferrable;
alter table 	CEN_SOLMODIFACTURACIONSERVICIO	 drop constraint 	FK_SERVICIOSSOLICITADOS_SOLMOD	 ; 
alter table 	CEN_SOLMODIFACTURACIONSERVICIO	 add constraint 	FK_SERVICIOSSOLICITADOS_SOLMOD	 foreign key (	IDPETICION,IDINSTITUCION,IDTIPOSERVICIOS,IDSERVICIO,IDSERVICIOSINSTITUCION	)	 references 	PYS_SERVICIOSSOLICITADOS	 (	IDPETICION,IDINSTITUCION,IDTIPOSERVICIOS,IDSERVICIO,IDSERVICIOSINSTITUCION	) 		 Deferrable;
alter table 	CEN_SOLIMODIDIRECCIONES	 drop constraint 	FK_PROVINCIAS_SOLIMODDIRECCIO	 ; 
alter table 	CEN_SOLIMODIDIRECCIONES	 drop constraint 	FK_POBLACIONES_SOLIMODDIRECCIO	 ; 
alter table 	CEN_SOLIMODIDIRECCIONES	 drop constraint 	FK_PAIS_SOLIMODIDIRECCIONES	 ; 
alter table 	CEN_SOLIMODIDIRECCIONES	 drop constraint 	FK_DIRECCIONES_SOLICITUDMODIFI	 ; 
alter table 	CEN_SOLIMODIDIRECCIONES	 drop constraint 	FK_CLIENTE_SOLICITUDMODIFICACI	 ; 
alter table 	CEN_SOLIMODIDIRECCIONES	 drop constraint 	FK_CEN_SOLIMODIDIRECCION_ESTAD	 ; 
alter table 	CEN_SOLIMODIDIRECCIONES	 add constraint 	FK_PROVINCIAS_SOLIMODDIRECCIO	 foreign key (	IDPROVINCIA	)	 references 	CEN_PROVINCIAS	 (	IDPROVINCIA	) 		 Deferrable;
alter table 	CEN_SOLIMODIDIRECCIONES	 add constraint 	FK_POBLACIONES_SOLIMODDIRECCIO	 foreign key (	IDPOBLACION	)	 references 	CEN_POBLACIONES	 (	IDPOBLACION	) 		 Deferrable;
alter table 	CEN_SOLIMODIDIRECCIONES	 add constraint 	FK_PAIS_SOLIMODIDIRECCIONES	 foreign key (	IDPAIS	)	 references 	CEN_PAIS	 (	IDPAIS	) 		 Deferrable;
alter table 	CEN_SOLIMODIDIRECCIONES	 add constraint 	FK_DIRECCIONES_SOLICITUDMODIFI	 foreign key (	IDPERSONA,IDDIRECCION,IDINSTITUCION	)	 references 	CEN_DIRECCIONES	 (	IDPERSONA,IDDIRECCION,IDINSTITUCION	) 		 Deferrable;
alter table 	CEN_SOLIMODIDIRECCIONES	 add constraint 	FK_CLIENTE_SOLICITUDMODIFICACI	 foreign key (	IDPERSONA,IDINSTITUCION	)	 references 	CEN_CLIENTE	 (	IDPERSONA,IDINSTITUCION	) 		 Deferrable;
alter table 	CEN_SOLIMODIDIRECCIONES	 add constraint 	FK_CEN_SOLIMODIDIRECCION_ESTAD	 foreign key (	IDESTADOSOLIC	)	 references 	CEN_ESTADOSOLICITUDMODIF	 (	IDESTADOSOLIC	) 		 Deferrable;
alter table 	CEN_SOLICMODIFEXPORTARFOTO	 drop constraint 	FK_SOLICITMODIFEXPFOTO_ESTADO	 ; 
alter table 	CEN_SOLICMODIFEXPORTARFOTO	 drop constraint 	FK_SOLICITMODIFEXPFOTO_CLIENTE	 ; 
alter table 	CEN_SOLICMODIFEXPORTARFOTO	 add constraint 	FK_SOLICITMODIFEXPFOTO_ESTADO	 foreign key (	IDESTADOSOLIC	)	 references 	CEN_ESTADOSOLICITUDMODIF	 (	IDESTADOSOLIC	) 		 Deferrable;
alter table 	CEN_SOLICMODIFEXPORTARFOTO	 add constraint 	FK_SOLICITMODIFEXPFOTO_CLIENTE	 foreign key (	IDPERSONA,IDINSTITUCION	)	 references 	CEN_CLIENTE	 (	IDPERSONA,IDINSTITUCION	) 		 Deferrable;
alter table 	CEN_SOLICMODICUENTAS	 drop constraint 	FK_SOLICMODICUENTAS_BANCOS	 ; 
alter table 	CEN_SOLICMODICUENTAS	 drop constraint 	FK_CUENTASBANCARIASSOLICITUDMO	 ; 
alter table 	CEN_SOLICMODICUENTAS	 drop constraint 	FK_CLIENTES_SOLICITUDMODIFICAC	 ; 
alter table 	CEN_SOLICMODICUENTAS	 drop constraint 	FK_CEN_SOLICMODICUENTAS_ESTADO	 ; 
alter table 	CEN_SOLICMODICUENTAS	 add constraint 	FK_SOLICMODICUENTAS_BANCOS	 foreign key (	CBO_CODIGO	)	 references 	CEN_BANCOS	 (	CODIGO	) 		 Deferrable;
alter table 	CEN_SOLICMODICUENTAS	 add constraint 	FK_CUENTASBANCARIASSOLICITUDMO	 foreign key (	IDCUENTA,IDPERSONA,IDINSTITUCION	)	 references 	CEN_CUENTASBANCARIAS	 (	IDCUENTA,IDPERSONA,IDINSTITUCION	) 		 Deferrable;
alter table 	CEN_SOLICMODICUENTAS	 add constraint 	FK_CLIENTES_SOLICITUDMODIFICAC	 foreign key (	IDINSTITUCION,IDPERSONA	)	 references 	CEN_CLIENTE	 (	IDINSTITUCION,IDPERSONA	) 		 Deferrable;
alter table 	CEN_SOLICMODICUENTAS	 add constraint 	FK_CEN_SOLICMODICUENTAS_ESTADO	 foreign key (	IDESTADOSOLIC	)	 references 	CEN_ESTADOSOLICITUDMODIF	 (	IDESTADOSOLIC	) 		 Deferrable;
alter table 	CEN_SOLICITUDMUTUALIDAD	 drop constraint 	FK_TIPOIDENTIFICACION_SOLIMUT	 ; 
alter table 	CEN_SOLICITUDMUTUALIDAD	 drop constraint 	FK_POBLACIONES_SOLICITUDMUT	 ; 
alter table 	CEN_SOLICITUDMUTUALIDAD	 drop constraint 	FK_PAIS_SOLICITUDMUT	 ; 
alter table 	CEN_SOLICITUDMUTUALIDAD	 drop constraint 	FK_INSTITUCION_SOLICITUDMUT	 ; 
alter table 	CEN_SOLICITUDMUTUALIDAD	 drop constraint 	FK_ESTADOCIVIL_SOLICITUDMUT	 ; 
alter table 	CEN_SOLICITUDMUTUALIDAD	 add constraint 	FK_TIPOIDENTIFICACION_SOLIMUT	 foreign key (	IDTIPOIDENTIFICACION	)	 references 	CEN_TIPOIDENTIFICACION	 (	IDTIPOIDENTIFICACION	) 		 Deferrable;
alter table 	CEN_SOLICITUDMUTUALIDAD	 add constraint 	FK_POBLACIONES_SOLICITUDMUT	 foreign key (	IDPOBLACION	)	 references 	CEN_POBLACIONES	 (	IDPOBLACION	) 		 Deferrable;
alter table 	CEN_SOLICITUDMUTUALIDAD	 add constraint 	FK_PAIS_SOLICITUDMUT	 foreign key (	IDPAIS	)	 references 	CEN_PAIS	 (	IDPAIS	) 		 Deferrable;
alter table 	CEN_SOLICITUDMUTUALIDAD	 add constraint 	FK_INSTITUCION_SOLICITUDMUT	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	CEN_SOLICITUDMUTUALIDAD	 add constraint 	FK_ESTADOCIVIL_SOLICITUDMUT	 foreign key (	IDESTADOCIVIL	)	 references 	CEN_ESTADOCIVIL	 (	IDESTADOCIVIL	) 		 Deferrable;
alter table 	CEN_SOLICITUDMODIFICACIONCV	 drop constraint 	FK_TIPOCV_SOLICITUDMODIFICACIO	 ; 
alter table 	CEN_SOLICITUDMODIFICACIONCV	 drop constraint 	FK_SOLMODIFCV_TIPOSCV_SUBT2	 ; 
alter table 	CEN_SOLICITUDMODIFICACIONCV	 drop constraint 	FK_SOLMODIFCV_TIPOSCV_SUBT1	 ; 
alter table 	CEN_SOLICITUDMODIFICACIONCV	 drop constraint 	FK_DATOSCV_SOLICITUDMODIFICACI	 ; 
alter table 	CEN_SOLICITUDMODIFICACIONCV	 drop constraint 	FK_CEN_SOLICITUDMODIFICACION	 ; 
alter table 	CEN_SOLICITUDMODIFICACIONCV	 drop constraint 	FK_CEN_SOLICITUDMODIFCV_ESTADO	 ; 
alter table 	CEN_SOLICITUDMODIFICACIONCV	 add constraint 	FK_TIPOCV_SOLICITUDMODIFICACIO	 foreign key (	IDTIPOCV	)	 references 	CEN_TIPOSCV	 (	IDTIPOCV	) 		 Deferrable;
alter table 	CEN_SOLICITUDMODIFICACIONCV	 add constraint 	FK_SOLMODIFCV_TIPOSCV_SUBT2	 foreign key (	IDINSTITUCION_SUBT2,IDTIPOCV,IDTIPOCVSUBTIPO2	)	 references 	CEN_TIPOSCVSUBTIPO2	 (	IDINSTITUCION,IDTIPOCV,IDTIPOCVSUBTIPO2	) 		 Deferrable;
alter table 	CEN_SOLICITUDMODIFICACIONCV	 add constraint 	FK_SOLMODIFCV_TIPOSCV_SUBT1	 foreign key (	IDINSTITUCION_SUBT1,IDTIPOCV,IDTIPOCVSUBTIPO1	)	 references 	CEN_TIPOSCVSUBTIPO1	 (	IDINSTITUCION,IDTIPOCV,IDTIPOCVSUBTIPO1	) 		 Deferrable;
alter table 	CEN_SOLICITUDMODIFICACIONCV	 add constraint 	FK_DATOSCV_SOLICITUDMODIFICACI	 foreign key (	IDCV,IDPERSONA,IDINSTITUCION	)	 references 	CEN_DATOSCV	 (	IDCV,IDPERSONA,IDINSTITUCION	) 		 Deferrable;
alter table 	CEN_SOLICITUDMODIFICACIONCV	 add constraint 	FK_CEN_SOLICITUDMODIFICACION	 foreign key (	IDINSTITUCION,IDPERSONA	)	 references 	CEN_CLIENTE	 (	IDINSTITUCION,IDPERSONA	) 		 Deferrable;
alter table 	CEN_SOLICITUDMODIFICACIONCV	 add constraint 	FK_CEN_SOLICITUDMODIFCV_ESTADO	 foreign key (	IDESTADOSOLIC	)	 references 	CEN_ESTADOSOLICITUDMODIF	 (	IDESTADOSOLIC	) 		 Deferrable;
alter table 	CEN_SOLICITUDINCORPORACION	 drop constraint 	FK_TIPOSOLICITUD_SOLICITUDINCO	 ; 
alter table 	CEN_SOLICITUDINCORPORACION	 drop constraint 	FK_TIPOIDENTIFICACION_SOLICITU	 ; 
alter table 	CEN_SOLICITUDINCORPORACION	 drop constraint 	FK_TIPOCOLEGIACION_SOLICITUDIN	 ; 
alter table 	CEN_SOLICITUDINCORPORACION	 drop constraint 	FK_POBLACIONES_SOLICITUDINCORP	 ; 
alter table 	CEN_SOLICITUDINCORPORACION	 drop constraint 	FK_PAIS_SOLICITUDINCORPORACION	 ; 
alter table 	CEN_SOLICITUDINCORPORACION	 drop constraint 	FK_MODALIDADDOC_DOCMODALIDAD	 ; 
alter table 	CEN_SOLICITUDINCORPORACION	 drop constraint 	FK_INSTITUCION_SOLICITUDINCORP	 ; 
alter table 	CEN_SOLICITUDINCORPORACION	 drop constraint 	FK_ESTADOSOLICITUD_SOLICITINCO	 ; 
alter table 	CEN_SOLICITUDINCORPORACION	 drop constraint 	FK_ESTADOCIVIL_SOLICITUDINCORP	 ; 
alter table 	CEN_SOLICITUDINCORPORACION	 drop constraint 	FK_DIRECCIONES	 ; 
alter table 	CEN_SOLICITUDINCORPORACION	 drop constraint 	FK_BANC_SOLICITUDINCORPORACION	 ; 
alter table 	CEN_SOLICITUDINCORPORACION	 add constraint 	FK_TIPOSOLICITUD_SOLICITUDINCO	 foreign key (	IDTIPOSOLICITUD	)	 references 	CEN_TIPOSOLICITUD	 (	IDTIPOSOLICITUD	) 		 Deferrable;
alter table 	CEN_SOLICITUDINCORPORACION	 add constraint 	FK_TIPOIDENTIFICACION_SOLICITU	 foreign key (	IDTIPOIDENTIFICACION	)	 references 	CEN_TIPOIDENTIFICACION	 (	IDTIPOIDENTIFICACION	) 		 Deferrable;
alter table 	CEN_SOLICITUDINCORPORACION	 add constraint 	FK_TIPOCOLEGIACION_SOLICITUDIN	 foreign key (	IDTIPOCOLEGIACION	)	 references 	CEN_TIPOCOLEGIACION	 (	IDTIPOCOLEGIACION	) 		 Deferrable;
alter table 	CEN_SOLICITUDINCORPORACION	 add constraint 	FK_POBLACIONES_SOLICITUDINCORP	 foreign key (	IDPOBLACION	)	 references 	CEN_POBLACIONES	 (	IDPOBLACION	) 		 Deferrable;
alter table 	CEN_SOLICITUDINCORPORACION	 add constraint 	FK_PAIS_SOLICITUDINCORPORACION	 foreign key (	IDPAIS	)	 references 	CEN_PAIS	 (	IDPAIS	) 		 Deferrable;
alter table 	CEN_SOLICITUDINCORPORACION	 add constraint 	FK_MODALIDADDOC_DOCMODALIDAD	 foreign key (	IDMODALIDADDOCUMENTACION,IDINSTITUCION	)	 references 	CEN_DOCUMENTACIONMODALIDAD	 (	IDMODALIDAD,IDINSTITUCION	) 		 Deferrable;
alter table 	CEN_SOLICITUDINCORPORACION	 add constraint 	FK_INSTITUCION_SOLICITUDINCORP	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	CEN_SOLICITUDINCORPORACION	 add constraint 	FK_ESTADOSOLICITUD_SOLICITINCO	 foreign key (	IDESTADO	)	 references 	CEN_ESTADOSOLICITUD	 (	IDESTADO	) 		 Deferrable;
alter table 	CEN_SOLICITUDINCORPORACION	 add constraint 	FK_ESTADOCIVIL_SOLICITUDINCORP	 foreign key (	IDESTADOCIVIL	)	 references 	CEN_ESTADOCIVIL	 (	IDESTADOCIVIL	) 		 Deferrable;
alter table 	CEN_SOLICITUDINCORPORACION	 add constraint 	FK_DIRECCIONES	 foreign key (	IDDIRECCIONTEMP,IDINSTITUCION,IDPERSONATEMP	)	 references 	CEN_DIRECCIONES	 (	IDDIRECCION,IDINSTITUCION,IDPERSONA	) 		 Deferrable;
alter table 	CEN_SOLICITUDINCORPORACION	 add constraint 	FK_BANC_SOLICITUDINCORPORACION	 foreign key (	CBO_CODIGO	)	 references 	CEN_BANCOS	 (	CODIGO	) 		 Deferrable;
alter table 	CEN_SOLICITUDESMODIFICACION	 drop constraint 	FK_SOLICIT_TIPOSMODIFICACIONES	 ; 
alter table 	CEN_SOLICITUDESMODIFICACION	 drop constraint 	FK_CEN_SOLICITUDESMODIFIC_ESTA	 ; 
alter table 	CEN_SOLICITUDESMODIFICACION	 drop constraint 	FK_CEN_SOLICITUDESMODIFICAC_CL	 ; 
alter table 	CEN_SOLICITUDESMODIFICACION	 add constraint 	FK_SOLICIT_TIPOSMODIFICACIONES	 foreign key (	IDTIPOMODIFICACION	)	 references 	CEN_TIPOSMODIFICACIONES	 (	IDTIPOMODIFICACION	) 		 Deferrable;
alter table 	CEN_SOLICITUDESMODIFICACION	 add constraint 	FK_CEN_SOLICITUDESMODIFIC_ESTA	 foreign key (	IDESTADOSOLIC	)	 references 	CEN_ESTADOSOLICITUDMODIF	 (	IDESTADOSOLIC	) 		 Deferrable;
alter table 	CEN_SOLICITUDESMODIFICACION	 add constraint 	FK_CEN_SOLICITUDESMODIFICAC_CL	 foreign key (	IDPERSONA,IDINSTITUCION	)	 references 	CEN_CLIENTE	 (	IDPERSONA,IDINSTITUCION	) 		 Deferrable;
alter table 	CEN_SOLICITMODIFDATOSBASICOS	 drop constraint 	FK_SOLICITMODIFDATOS_LENGUAJES	 ; 
alter table 	CEN_SOLICITMODIFDATOSBASICOS	 drop constraint 	FK_SOLICITMODIFDATOS_CLIENTE	 ; 
alter table 	CEN_SOLICITMODIFDATOSBASICOS	 drop constraint 	FK_SOLICITMODIFDATBASICOS_ESTA	 ; 
alter table 	CEN_SOLICITMODIFDATOSBASICOS	 add constraint 	FK_SOLICITMODIFDATOS_LENGUAJES	 foreign key (	IDLENGUAJE	)	 references 	ADM_LENGUAJES	 (	IDLENGUAJE	) 		 Deferrable;
alter table 	CEN_SOLICITMODIFDATOSBASICOS	 add constraint 	FK_SOLICITMODIFDATOS_CLIENTE	 foreign key (	IDPERSONA,IDINSTITUCION	)	 references 	CEN_CLIENTE	 (	IDPERSONA,IDINSTITUCION	) 		 Deferrable;
alter table 	CEN_SOLICITMODIFDATOSBASICOS	 add constraint 	FK_SOLICITMODIFDATBASICOS_ESTA	 foreign key (	IDESTADOSOLIC	)	 references 	CEN_ESTADOSOLICITUDMODIF	 (	IDESTADOSOLIC	) 		 Deferrable;
alter table 	CEN_SANCION	 drop constraint 	FK_CEN_SANCION_TIPO	 ; 
alter table 	CEN_SANCION	 drop constraint 	FK_CEN_SANCION_PERSON	 ; 
alter table 	CEN_SANCION	 drop constraint 	FK_CEN_SANCION_INSTITUCIONSAN	 ; 
alter table 	CEN_SANCION	 drop constraint 	FK_CEN_SANCION_INSTITUCION	 ; 
alter table 	CEN_SANCION	 add constraint 	FK_CEN_SANCION_TIPO	 foreign key (	IDTIPOSANCION	)	 references 	CEN_TIPOSANCION	 (	IDTIPOSANCION	) 		 Deferrable;
alter table 	CEN_SANCION	 add constraint 	FK_CEN_SANCION_PERSON	 foreign key (	IDPERSONA	)	 references 	CEN_PERSONA	 (	IDPERSONA	) 		 Deferrable;
alter table 	CEN_SANCION	 add constraint 	FK_CEN_SANCION_INSTITUCIONSAN	 foreign key (	IDINSTITUCIONSANCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	CEN_SANCION	 add constraint 	FK_CEN_SANCION_INSTITUCION	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	CEN_PROVINCIAS	 drop constraint 	FK_COMUNIDADESAUTONOMAS_PROVIN	 ; 
alter table 	CEN_PROVINCIAS	 drop constraint 	FK_CEN_PAIS_PROVINCIAS	 ; 
alter table 	CEN_PROVINCIAS	 add constraint 	FK_COMUNIDADESAUTONOMAS_PROVIN	 foreign key (	IDCOMUNIDADAUTONOMA	)	 references 	CEN_COMUNIDADESAUTONOMAS	 (	IDCOMUNIDADAUTONOMA	) 		 Deferrable;
alter table 	CEN_PROVINCIAS	 add constraint 	FK_CEN_PAIS_PROVINCIAS	 foreign key (	IDPAIS	)	 references 	CEN_PAIS	 (	IDPAIS	) 		 Deferrable;
alter table 	CEN_POBLACIONES	 drop constraint 	FK_CEN_POBLACIONES_PROVINCIA	 ; 
alter table 	CEN_POBLACIONES	 drop constraint 	FK_CEN_POBLACIONES_PARTIDO	 ; 
alter table 	CEN_POBLACIONES	 drop constraint 	FK_CEN_POBLACIONES_MUNICIPIOS	 ; 
alter table 	CEN_POBLACIONES	 add constraint 	FK_CEN_POBLACIONES_PROVINCIA	 foreign key (	IDPROVINCIA	)	 references 	CEN_PROVINCIAS	 (	IDPROVINCIA	) 		 Deferrable;
alter table 	CEN_POBLACIONES	 add constraint 	FK_CEN_POBLACIONES_PARTIDO	 foreign key (	IDPARTIDO	)	 references 	CEN_PARTIDOJUDICIAL	 (	IDPARTIDO	) 		 Deferrable;
alter table 	CEN_POBLACIONES	 add constraint 	FK_CEN_POBLACIONES_MUNICIPIOS	 foreign key (	IDPOBLACIONMUNICIPIO	)	 references 	CEN_POBLACIONES	 (	IDPOBLACION	) 		 Deferrable;
alter table 	CEN_PERSONA	 drop constraint 	FK_CEN_PERSONA_TIPOIDENTIFICAC	 ; 
alter table 	CEN_PERSONA	 drop constraint 	FK_CEN_PERSONA_ESTADOCIVIL	 ; 
alter table 	CEN_PERSONA	 add constraint 	FK_CEN_PERSONA_TIPOIDENTIFICAC	 foreign key (	IDTIPOIDENTIFICACION	)	 references 	CEN_TIPOIDENTIFICACION	 (	IDTIPOIDENTIFICACION	) 		 Deferrable;
alter table 	CEN_PERSONA	 add constraint 	FK_CEN_PERSONA_ESTADOCIVIL	 foreign key (	IDESTADOCIVIL	)	 references 	CEN_ESTADOCIVIL	 (	IDESTADOCIVIL	) 		 Deferrable;
alter table 	CEN_PARTIDOJUDICIAL	 drop constraint 	SYS_C0019283	 ; 
alter table 	CEN_PARTIDOJUDICIAL	 add constraint 	SYS_C0019283	 foreign key (	IDINSTITUCIONPROPIETARIO	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	CEN_PAISES_ORGINT	 drop constraint 	FK_CEN_PAIS_ORGINT	 ; 
alter table 	CEN_PAISES_ORGINT	 drop constraint 	FK_CEN_ORGANISMOS_INT	 ; 
alter table 	CEN_PAISES_ORGINT	 add constraint 	FK_CEN_PAIS_ORGINT	 foreign key (	IDPAIS	)	 references 	CEN_PAIS	 (	IDPAIS	) 		 Deferrable;
alter table 	CEN_PAISES_ORGINT	 add constraint 	FK_CEN_ORGANISMOS_INT	 foreign key (	IDORGANIZACION	)	 references 	CEN_ORGANISMOS_INT	 (	IDORGANIZACION	) 		 Deferrable;
alter table 	CEN_NOCOLEGIADO_ACTIVIDAD	 drop constraint 	FK_CEN_NOCOL_CEN_NOCOL_ACT	 ; 
alter table 	CEN_NOCOLEGIADO_ACTIVIDAD	 drop constraint 	FK_CEN_ACT_PROF_CEN_NOCOL_ACT	 ; 
alter table 	CEN_NOCOLEGIADO_ACTIVIDAD	 add constraint 	FK_CEN_NOCOL_CEN_NOCOL_ACT	 foreign key (	IDPERSONA,IDINSTITUCION	)	 references 	CEN_NOCOLEGIADO	 (	IDPERSONA,IDINSTITUCION	) 		 Deferrable;
alter table 	CEN_NOCOLEGIADO_ACTIVIDAD	 add constraint 	FK_CEN_ACT_PROF_CEN_NOCOL_ACT	 foreign key (	IDACTIVIDADPROFESIONAL	)	 references 	CEN_ACTIVIDADPROFESIONAL	 (	IDACTIVIDADPROFESIONAL	) 		 Deferrable;
alter table 	CEN_NOCOLEGIADO	 drop constraint 	FK_CEN_NOCOLEGIADO_CLIENTE	 ; 
alter table 	CEN_NOCOLEGIADO	 drop constraint 	FK_CEN_CLIENTE_CEN_NOCOL	 ; 
alter table 	CEN_NOCOLEGIADO	 add constraint 	FK_CEN_NOCOLEGIADO_CLIENTE	 foreign key (	IDINSTITUCION,IDPERSONA	)	 references 	CEN_CLIENTE	 (	IDINSTITUCION,IDPERSONA	) 	 on delete cascade	 Deferrable;
alter table 	CEN_NOCOLEGIADO	 add constraint 	FK_CEN_CLIENTE_CEN_NOCOL	 foreign key (	IDPERSONANOTARIO,IDINSTITUCION	)	 references 	CEN_CLIENTE	 (	IDPERSONA,IDINSTITUCION	) 		 Deferrable;
alter table 	CEN_MEDIADOR_XML	 drop constraint 	FK_CEN_MED__CEN_MED_TIPODOC	 ; 
alter table 	CEN_MEDIADOR_XML	 drop constraint 	FK_CEN_MEDIXML__CEN_MED_FICH	 ; 
alter table 	CEN_MEDIADOR_XML	 drop constraint 	FK_CEN_MEDIADOR__CEN_MED_TIPOM	 ; 
alter table 	CEN_MEDIADOR_XML	 add constraint 	FK_CEN_MED__CEN_MED_TIPODOC	 foreign key (	IDTIPODOCUMENTO	)	 references 	CEN_MED_TIPODOCUMENTO	 (	IDTIPODOCUMENTO	) 		 Deferrable;
alter table 	CEN_MEDIADOR_XML	 add constraint 	FK_CEN_MEDIXML__CEN_MED_FICH	 foreign key (	IDMEDIADOREXPORTFICHERO	)	 references 	CEN_MEDIADOR_EXPORTFICHERO	 (	IDMEDIADOREXPORTFICHERO	) 		 Deferrable;
alter table 	CEN_MEDIADOR_XML	 add constraint 	FK_CEN_MEDIADOR__CEN_MED_TIPOM	 foreign key (	IDTIPOMOVIMIENTO	)	 references 	CEN_MED_TIPOMOVIM	 (	IDTIPOMOVIMIENTO	) 		 Deferrable;
alter table 	CEN_MEDIADOR_EXPORTFICHERO	 drop constraint 	FK_CEN_MEDFICH__CEN_MEDTIPOEXP	 ; 
alter table 	CEN_MEDIADOR_EXPORTFICHERO	 add constraint 	FK_CEN_MEDFICH__CEN_MEDTIPOEXP	 foreign key (	IDTIPOEXPORT	)	 references 	CEN_MEDIADOR_TIPOEXPORT	 (	IDMEDIADORTIPOEXPORT	) 		 Deferrable;
alter table 	CEN_MEDIADOR_CSV	 drop constraint 	FK_CEN_MEDIADOR__CEN_MEDIADOR_	 ; 
alter table 	CEN_MEDIADOR_CSV	 add constraint 	FK_CEN_MEDIADOR__CEN_MEDIADOR_	 foreign key (	IDMEDIADORFICHEROCSV	)	 references 	CEN_MEDIADOR_FICHEROCSV	 (	IDMEDIADORFICHEROCSV	) 		 Deferrable;
alter table 	CEN_MANDATOS_CUENTASBANCARIAS	 drop constraint 	FK_TIPOID_DEUDOR_MANDATOS	 ; 
alter table 	CEN_MANDATOS_CUENTASBANCARIAS	 drop constraint 	FK_TIPOID_ACREEDOR_MANDATOS	 ; 
alter table 	CEN_MANDATOS_CUENTASBANCARIAS	 drop constraint 	FK_PROV_DEUDOR_MANDATOS	 ; 
alter table 	CEN_MANDATOS_CUENTASBANCARIAS	 drop constraint 	FK_PROV_ACREEDOR_MANDATOS	 ; 
alter table 	CEN_MANDATOS_CUENTASBANCARIAS	 drop constraint 	FK_POBL_DEUDOR_MANDATOS	 ; 
alter table 	CEN_MANDATOS_CUENTASBANCARIAS	 drop constraint 	FK_POBL_ACREEDOR_MANDATOS	 ; 
alter table 	CEN_MANDATOS_CUENTASBANCARIAS	 drop constraint 	FK_PAIS_DEUDOR_MANDATOS	 ; 
alter table 	CEN_MANDATOS_CUENTASBANCARIAS	 drop constraint 	FK_PAIS_ACREEDOR_MANDATOS	 ; 
alter table 	CEN_MANDATOS_CUENTASBANCARIAS	 drop constraint 	FK_MAN_GEN_FICHERO	 ; 
alter table 	CEN_MANDATOS_CUENTASBANCARIAS	 drop constraint 	FK_MANDATOS_INSTITUCIONES	 ; 
alter table 	CEN_MANDATOS_CUENTASBANCARIAS	 drop constraint 	FK_MANDATOS_CUENTASBANCARIAS	 ; 
alter table 	CEN_MANDATOS_CUENTASBANCARIAS	 add constraint 	FK_TIPOID_DEUDOR_MANDATOS	 foreign key (	DEUDOR_TIPOID	)	 references 	CEN_TIPOIDENTIFICACION	 (	IDTIPOIDENTIFICACION	) 		 Deferrable;
alter table 	CEN_MANDATOS_CUENTASBANCARIAS	 add constraint 	FK_TIPOID_ACREEDOR_MANDATOS	 foreign key (	ACREEDOR_TIPOID	)	 references 	CEN_TIPOIDENTIFICACION	 (	IDTIPOIDENTIFICACION	) 		 Deferrable;
alter table 	CEN_MANDATOS_CUENTASBANCARIAS	 add constraint 	FK_PROV_DEUDOR_MANDATOS	 foreign key (	DEUDOR_IDPROVINCIA	)	 references 	CEN_PROVINCIAS	 (	IDPROVINCIA	) 		 Deferrable;
alter table 	CEN_MANDATOS_CUENTASBANCARIAS	 add constraint 	FK_PROV_ACREEDOR_MANDATOS	 foreign key (	ACREEDOR_IDPROVINCIA	)	 references 	CEN_PROVINCIAS	 (	IDPROVINCIA	) 		 Deferrable;
alter table 	CEN_MANDATOS_CUENTASBANCARIAS	 add constraint 	FK_POBL_DEUDOR_MANDATOS	 foreign key (	DEUDOR_IDPOBLACION	)	 references 	CEN_POBLACIONES	 (	IDPOBLACION	) 		 Deferrable;
alter table 	CEN_MANDATOS_CUENTASBANCARIAS	 add constraint 	FK_POBL_ACREEDOR_MANDATOS	 foreign key (	ACREEDOR_IDPOBLACION	)	 references 	CEN_POBLACIONES	 (	IDPOBLACION	) 		 Deferrable;
alter table 	CEN_MANDATOS_CUENTASBANCARIAS	 add constraint 	FK_PAIS_DEUDOR_MANDATOS	 foreign key (	DEUDOR_IDPAIS	)	 references 	CEN_PAIS	 (	IDPAIS	) 		 Deferrable;
alter table 	CEN_MANDATOS_CUENTASBANCARIAS	 add constraint 	FK_PAIS_ACREEDOR_MANDATOS	 foreign key (	ACREEDOR_IDPAIS	)	 references 	CEN_PAIS	 (	IDPAIS	) 		 Deferrable;
alter table 	CEN_MANDATOS_CUENTASBANCARIAS	 add constraint 	FK_MAN_GEN_FICHERO	 foreign key (	IDFICHEROFIRMA,IDINSTITUCION	)	 references 	GEN_FICHERO	 (	IDFICHERO,IDINSTITUCION	) 		 Deferrable;
alter table 	CEN_MANDATOS_CUENTASBANCARIAS	 add constraint 	FK_MANDATOS_INSTITUCIONES	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	CEN_MANDATOS_CUENTASBANCARIAS	 add constraint 	FK_MANDATOS_CUENTASBANCARIAS	 foreign key (	IDCUENTA,IDINSTITUCION,IDPERSONA	)	 references 	CEN_CUENTASBANCARIAS	 (	IDCUENTA,IDINSTITUCION,IDPERSONA	) 		 Deferrable;
alter table 	CEN_INSTITUCION_POBLACION	 drop constraint 	FK_INSTITUCION_POBLACION_POBLA	 ; 
alter table 	CEN_INSTITUCION_POBLACION	 drop constraint 	FK_INSTITUCION_POBLACION_INSTI	 ; 
alter table 	CEN_INSTITUCION_POBLACION	 add constraint 	FK_INSTITUCION_POBLACION_POBLA	 foreign key (	IDPOBLACION	)	 references 	CEN_POBLACIONES	 (	IDPOBLACION	) 		 Deferrable;
alter table 	CEN_INSTITUCION_POBLACION	 add constraint 	FK_INSTITUCION_POBLACION_INSTI	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	CEN_INSTITUCION_LENGUAJES	 drop constraint 	FK_CEN_INSTITUCION_LENG_LENG	 ; 
alter table 	CEN_INSTITUCION_LENGUAJES	 drop constraint 	FK_CEN_INSTITUCION_LENG_INSTI	 ; 
alter table 	CEN_INSTITUCION_LENGUAJES	 add constraint 	FK_CEN_INSTITUCION_LENG_LENG	 foreign key (	IDLENGUAJE	)	 references 	ADM_LENGUAJES	 (	IDLENGUAJE	) 		 Deferrable;
alter table 	CEN_INSTITUCION_LENGUAJES	 add constraint 	FK_CEN_INSTITUCION_LENG_INSTI	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	CEN_INSTITUCION	 drop constraint 	FK_CEN_INSTITUCION_LENGUAJE	 ; 
alter table 	CEN_INSTITUCION	 drop constraint 	FK_CEN_INSTITUCION_INSTITUCION	 ; 
alter table 	CEN_INSTITUCION	 add constraint 	FK_CEN_INSTITUCION_LENGUAJE	 foreign key (	IDLENGUAJE	)	 references 	ADM_LENGUAJES	 (	IDLENGUAJE	) 		 Deferrable;
alter table 	CEN_INSTITUCION	 add constraint 	FK_CEN_INSTITUCION_INSTITUCION	 foreign key (	CEN_INST_IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	CEN_INFLUENCIA	 drop constraint 	FK_CEN_INFLUENCIA_PARTIDOJUDIC	 ; 
alter table 	CEN_INFLUENCIA	 drop constraint 	FK_CEN_INFLUENCIA_INSTITUCION	 ; 
alter table 	CEN_INFLUENCIA	 add constraint 	FK_CEN_INFLUENCIA_PARTIDOJUDIC	 foreign key (	IDPARTIDO	)	 references 	CEN_PARTIDOJUDICIAL	 (	IDPARTIDO	) 		 Deferrable;
alter table 	CEN_INFLUENCIA	 add constraint 	FK_CEN_INFLUENCIA_INSTITUCION	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	CEN_HISTORICO	 drop constraint 	FK_CEN_HISTORICO_TIPOCAMBIO	 ; 
alter table 	CEN_HISTORICO	 drop constraint 	FK_CEN_HISTORICO_CLIENTE	 ; 
alter table 	CEN_HISTORICO	 add constraint 	FK_CEN_HISTORICO_TIPOCAMBIO	 foreign key (	IDTIPOCAMBIO	)	 references 	CEN_TIPOCAMBIO	 (	IDTIPOCAMBIO	) 		 Deferrable;
alter table 	CEN_HISTORICO	 add constraint 	FK_CEN_HISTORICO_CLIENTE	 foreign key (	IDINSTITUCION,IDPERSONA	)	 references 	CEN_CLIENTE	 (	IDINSTITUCION,IDPERSONA	) 		 Deferrable;
alter table 	CEN_GRUPOS_FICHEROS	 drop constraint 	FK_FICH_CEN_GRUPOSCLIENTE	 ; 
alter table 	CEN_GRUPOS_FICHEROS	 add constraint 	FK_FICH_CEN_GRUPOSCLIENTE	 foreign key (	IDINSTITUCION_GRUPO,IDGRUPO	)	 references 	CEN_GRUPOSCLIENTE	 (	IDINSTITUCION,IDGRUPO	) 		 Deferrable;
alter table 	CEN_GRUPOSCRITERIOS	 drop constraint 	FK_GRUPOSCRITERIOS_INSTITUCION	 ; 
alter table 	CEN_GRUPOSCRITERIOS	 drop constraint 	FK_GRUPOSCRITERIOS_CONSULTA	 ; 
alter table 	CEN_GRUPOSCRITERIOS	 add constraint 	FK_GRUPOSCRITERIOS_INSTITUCION	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	CEN_GRUPOSCRITERIOS	 add constraint 	FK_GRUPOSCRITERIOS_CONSULTA	 foreign key (	IDCONSULTA,IDINSTITUCION	)	 references 	CON_CONSULTA	 (	IDCONSULTA,IDINSTITUCION	) 		 Deferrable;
alter table 	CEN_GRUPOSCLIENTE_CLIENTE	 drop constraint 	FK_GRUPOSCLIENTE_CLIENTE_GRUPO	 ; 
alter table 	CEN_GRUPOSCLIENTE_CLIENTE	 drop constraint 	FK_CEN_CLIENTE_GRUPOSCLIENTE_C	 ; 
alter table 	CEN_GRUPOSCLIENTE_CLIENTE	 add constraint 	FK_GRUPOSCLIENTE_CLIENTE_GRUPO	 foreign key (	IDINSTITUCION_GRUPO,IDGRUPO	)	 references 	CEN_GRUPOSCLIENTE	 (	IDINSTITUCION,IDGRUPO	) 		 Deferrable;
alter table 	CEN_GRUPOSCLIENTE_CLIENTE	 add constraint 	FK_CEN_CLIENTE_GRUPOSCLIENTE_C	 foreign key (	IDINSTITUCION,IDPERSONA	)	 references 	CEN_CLIENTE	 (	IDINSTITUCION,IDPERSONA	) 		 Deferrable;
alter table 	CEN_GRUPOSCLIENTE	 drop constraint 	FK_CEN_INSTITUCION_GRUPOSCLIEN	 ; 
alter table 	CEN_GRUPOSCLIENTE	 add constraint 	FK_CEN_INSTITUCION_GRUPOSCLIEN	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	CEN_ESTADOACTIVIDADPERSONA	 drop constraint 	FK_CEN_ESTADOACT_PERSON	 ; 
alter table 	CEN_ESTADOACTIVIDADPERSONA	 drop constraint 	FK_CEN_ESTADOACT_ESTADO	 ; 
alter table 	CEN_ESTADOACTIVIDADPERSONA	 add constraint 	FK_CEN_ESTADOACT_PERSON	 foreign key (	IDPERSONA	)	 references 	CEN_PERSONA	 (	IDPERSONA	) 		 Deferrable;
alter table 	CEN_ESTADOACTIVIDADPERSONA	 add constraint 	FK_CEN_ESTADOACT_ESTADO	 foreign key (	IDESTADO	)	 references 	CEN_ESTADOACTIVIDAD	 (	IDESTADO	) 		 Deferrable;
alter table 	CEN_DOCUMENTSOLICITUDINSTITU	 drop constraint 	FK_TIPOSOLICITUD_DOCU_SOLIC_IN	 ; 
alter table 	CEN_DOCUMENTSOLICITUDINSTITU	 drop constraint 	FK_TIPOCOLEGIACION_DOCU_SOL_IN	 ; 
alter table 	CEN_DOCUMENTSOLICITUDINSTITU	 drop constraint 	FK_INSTITUCION_DOCUMENTO_SOL_I	 ; 
alter table 	CEN_DOCUMENTSOLICITUDINSTITU	 drop constraint 	FK_DOC_MODALIDAD_DOCU_SOLIC_IN	 ; 
alter table 	CEN_DOCUMENTSOLICITUDINSTITU	 drop constraint 	FK_DOCUMENTACIONSOLIC_DOCUMENT	 ; 
alter table 	CEN_DOCUMENTSOLICITUDINSTITU	 add constraint 	FK_TIPOSOLICITUD_DOCU_SOLIC_IN	 foreign key (	IDTIPOSOLICITUD	)	 references 	CEN_TIPOSOLICITUD	 (	IDTIPOSOLICITUD	) 		 Deferrable;
alter table 	CEN_DOCUMENTSOLICITUDINSTITU	 add constraint 	FK_TIPOCOLEGIACION_DOCU_SOL_IN	 foreign key (	IDTIPOCOLEGIACION	)	 references 	CEN_TIPOCOLEGIACION	 (	IDTIPOCOLEGIACION	) 		 Deferrable;
alter table 	CEN_DOCUMENTSOLICITUDINSTITU	 add constraint 	FK_INSTITUCION_DOCUMENTO_SOL_I	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	CEN_DOCUMENTSOLICITUDINSTITU	 add constraint 	FK_DOC_MODALIDAD_DOCU_SOLIC_IN	 foreign key (	IDMODALIDAD,IDINSTITUCION	)	 references 	CEN_DOCUMENTACIONMODALIDAD	 (	IDMODALIDAD,IDINSTITUCION	) 		 Deferrable;
alter table 	CEN_DOCUMENTSOLICITUDINSTITU	 add constraint 	FK_DOCUMENTACIONSOLIC_DOCUMENT	 foreign key (	IDDOCUMSOLIINSTITU	)	 references 	CEN_DOCUMENTACIONSOLICITUD	 (	IDDOCUMENTACION	) 		 Deferrable;
alter table 	CEN_DOCUMENTACIONPRESENTADA	 drop constraint 	FK_SOLICITUDINCORPORACION_D_PR	 ; 
alter table 	CEN_DOCUMENTACIONPRESENTADA	 drop constraint 	FK_DOCUMENTACIONSOLICITUD_PRES	 ; 
alter table 	CEN_DOCUMENTACIONPRESENTADA	 add constraint 	FK_SOLICITUDINCORPORACION_D_PR	 foreign key (	IDSOLICITUD	)	 references 	CEN_SOLICITUDINCORPORACION	 (	IDSOLICITUD	) 		 Deferrable;
alter table 	CEN_DOCUMENTACIONPRESENTADA	 add constraint 	FK_DOCUMENTACIONSOLICITUD_PRES	 foreign key (	IDDOCUMENTACION	)	 references 	CEN_DOCUMENTACIONSOLICITUD	 (	IDDOCUMENTACION	) 		 Deferrable;
alter table 	CEN_DOCUMENTACIONMODALIDAD	 drop constraint 	FK_DOCMODALIDAD_INSTITUCION	 ; 
alter table 	CEN_DOCUMENTACIONMODALIDAD	 add constraint 	FK_DOCMODALIDAD_INSTITUCION	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	CEN_DIRECCION_TIPODIRECCION	 drop constraint 	FK_CEN_DIRECCION_TIPODIRECCION	 ; 
alter table 	CEN_DIRECCION_TIPODIRECCION	 drop constraint 	FK_CEN_DIRECCION_DIRECCION	 ; 
alter table 	CEN_DIRECCION_TIPODIRECCION	 add constraint 	FK_CEN_DIRECCION_TIPODIRECCION	 foreign key (	IDTIPODIRECCION	)	 references 	CEN_TIPODIRECCION	 (	IDTIPODIRECCION	) 		 Deferrable;
alter table 	CEN_DIRECCION_TIPODIRECCION	 add constraint 	FK_CEN_DIRECCION_DIRECCION	 foreign key (	IDDIRECCION,IDINSTITUCION,IDPERSONA	)	 references 	CEN_DIRECCIONES	 (	IDDIRECCION,IDINSTITUCION,IDPERSONA	) 	 on delete cascade	 Deferrable;
alter table 	CEN_DIRECCIONES	 drop constraint 	FK_CEN_DIRECCIONES_POBLACIONES	 ; 
alter table 	CEN_DIRECCIONES	 drop constraint 	FK_CEN_DIRECCIONES_PAIS	 ; 
alter table 	CEN_DIRECCIONES	 drop constraint 	FK_CEN_DIRECCIONES_INSTIALTA	 ; 
alter table 	CEN_DIRECCIONES	 drop constraint 	FK_CEN_DIRECCIONES_CEN_PROVINC	 ; 
alter table 	CEN_DIRECCIONES	 drop constraint 	FK_CEN_DIRECCIONES_CEN_CLIENTE	 ; 
alter table 	CEN_DIRECCIONES	 add constraint 	FK_CEN_DIRECCIONES_POBLACIONES	 foreign key (	IDPOBLACION	)	 references 	CEN_POBLACIONES	 (	IDPOBLACION	) 		 Deferrable;
alter table 	CEN_DIRECCIONES	 add constraint 	FK_CEN_DIRECCIONES_PAIS	 foreign key (	IDPAIS	)	 references 	CEN_PAIS	 (	IDPAIS	) 		 Deferrable;
alter table 	CEN_DIRECCIONES	 add constraint 	FK_CEN_DIRECCIONES_INSTIALTA	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	CEN_DIRECCIONES	 add constraint 	FK_CEN_DIRECCIONES_CEN_PROVINC	 foreign key (	IDPROVINCIA	)	 references 	CEN_PROVINCIAS	 (	IDPROVINCIA	) 		 Deferrable;
alter table 	CEN_DIRECCIONES	 add constraint 	FK_CEN_DIRECCIONES_CEN_CLIENTE	 foreign key (	IDPERSONA,IDINSTITUCION	)	 references 	CEN_CLIENTE	 (	IDPERSONA,IDINSTITUCION	) 		 Deferrable;
alter table 	CEN_DATOSCV	 drop constraint 	FK_CEN_TIPOSCV_SUBT2	 ; 
alter table 	CEN_DATOSCV	 drop constraint 	FK_CEN_TIPOSCV_SUBT1	 ; 
alter table 	CEN_DATOSCV	 drop constraint 	FK_CEN_TIPOSCV_CEN_DATOSCV	 ; 
alter table 	CEN_DATOSCV	 drop constraint 	FK_CEN_CLIENTE_CEN_DATOSCV	 ; 
alter table 	CEN_DATOSCV	 add constraint 	FK_CEN_TIPOSCV_SUBT2	 foreign key (	IDINSTITUCION_SUBT2,IDTIPOCV,IDTIPOCVSUBTIPO2	)	 references 	CEN_TIPOSCVSUBTIPO2	 (	IDINSTITUCION,IDTIPOCV,IDTIPOCVSUBTIPO2	) 		 Deferrable;
alter table 	CEN_DATOSCV	 add constraint 	FK_CEN_TIPOSCV_SUBT1	 foreign key (	IDINSTITUCION_SUBT1,IDTIPOCV,IDTIPOCVSUBTIPO1	)	 references 	CEN_TIPOSCVSUBTIPO1	 (	IDINSTITUCION,IDTIPOCV,IDTIPOCVSUBTIPO1	) 		 Deferrable;
alter table 	CEN_DATOSCV	 add constraint 	FK_CEN_TIPOSCV_CEN_DATOSCV	 foreign key (	IDTIPOCV	)	 references 	CEN_TIPOSCV	 (	IDTIPOCV	) 		 Deferrable;
alter table 	CEN_DATOSCV	 add constraint 	FK_CEN_CLIENTE_CEN_DATOSCV	 foreign key (	IDINSTITUCION,IDPERSONA	)	 references 	CEN_CLIENTE	 (	IDINSTITUCION,IDPERSONA	) 		 Deferrable;
alter table 	CEN_DATOSCOLEGIALESESTADO	 drop constraint 	FK_CEN_ESTADO_CEN_DATOSCOLEGIA	 ; 
alter table 	CEN_DATOSCOLEGIALESESTADO	 drop constraint 	FK_CEN_COLEGIADO_CEN_DATOSCOLE	 ; 
alter table 	CEN_DATOSCOLEGIALESESTADO	 add constraint 	FK_CEN_ESTADO_CEN_DATOSCOLEGIA	 foreign key (	IDESTADO	)	 references 	CEN_ESTADOCOLEGIAL	 (	IDESTADO	) 		 Deferrable;
alter table 	CEN_DATOSCOLEGIALESESTADO	 add constraint 	FK_CEN_COLEGIADO_CEN_DATOSCOLE	 foreign key (	IDINSTITUCION,IDPERSONA	)	 references 	CEN_COLEGIADO	 (	IDINSTITUCION,IDPERSONA	) 		 Deferrable;
alter table 	CEN_CUENTASBANCARIAS	 drop constraint 	FK_CEN_CLIENTE_CEN_CUENTASBANC	 ; 
alter table 	CEN_CUENTASBANCARIAS	 drop constraint 	FK_BANCOS_CUENTASBANCA	 ; 
alter table 	CEN_CUENTASBANCARIAS	 add constraint 	FK_CEN_CLIENTE_CEN_CUENTASBANC	 foreign key (	IDPERSONA,IDINSTITUCION	)	 references 	CEN_CLIENTE	 (	IDPERSONA,IDINSTITUCION	) 		 Deferrable;
alter table 	CEN_CUENTASBANCARIAS	 add constraint 	FK_BANCOS_CUENTASBANCA	 foreign key (	CBO_CODIGO	)	 references 	CEN_BANCOS	 (	CODIGO	) 		 Deferrable;
alter table 	CEN_CONTACTO	 drop constraint 	FK_CEN_INSTITUCION_CEN_CONTACT	 ; 
alter table 	CEN_CONTACTO	 drop constraint 	FK_CEN_CLIENTE_CEN_CONTACTO	 ; 
alter table 	CEN_CONTACTO	 add constraint 	FK_CEN_INSTITUCION_CEN_CONTACT	 foreign key (	CEN_INSTITUCION_ID	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	CEN_CONTACTO	 add constraint 	FK_CEN_CLIENTE_CEN_CONTACTO	 foreign key (	IDPERSONA,CEN_CLIENTE_CEN_INSTITUCION_ID	)	 references 	CEN_CLIENTE	 (	IDPERSONA,IDINSTITUCION	) 		 Deferrable;
alter table 	CEN_CONSEJO	 drop constraint 	FK_CEN_CLIENTE_CEN_CONSEJO	 ; 
alter table 	CEN_CONSEJO	 add constraint 	FK_CEN_CLIENTE_CEN_CONSEJO	 foreign key (	IDINSTITUCION,IDPERSONA	)	 references 	CEN_CLIENTE	 (	IDINSTITUCION,IDPERSONA	) 		 Deferrable;
alter table 	CEN_COMPONENTES	 drop constraint 	FK_COMPONENTES_CUENTASBANCARIA	 ; 
alter table 	CEN_COMPONENTES	 drop constraint 	FK_CEN_TIPOCOLEGIO_CEN_COMPO	 ; 
alter table 	CEN_COMPONENTES	 drop constraint 	FK_CEN_PROVINCIA_CEN_COMPO	 ; 
alter table 	CEN_COMPONENTES	 drop constraint 	FK_CEN_NOCOLEGIADO_CEN_COMPONE	 ; 
alter table 	CEN_COMPONENTES	 drop constraint 	FK_CEN_CLIENTE_CEN_COMPONENTE	 ; 
alter table 	CEN_COMPONENTES	 drop constraint 	FK_CEN_CARGO_CEN_COMPONENTE	 ; 
alter table 	CEN_COMPONENTES	 add constraint 	FK_COMPONENTES_CUENTASBANCARIA	 foreign key (	IDCUENTA,IDINSTITUCION,IDPERSONA	)	 references 	CEN_CUENTASBANCARIAS	 (	IDCUENTA,IDINSTITUCION,IDPERSONA	) 		 Deferrable;
alter table 	CEN_COMPONENTES	 add constraint 	FK_CEN_TIPOCOLEGIO_CEN_COMPO	 foreign key (	IDTIPOCOLEGIO	)	 references 	CEN_ACTIVIDADPROFESIONAL	 (	IDACTIVIDADPROFESIONAL	) 		 Deferrable;
alter table 	CEN_COMPONENTES	 add constraint 	FK_CEN_PROVINCIA_CEN_COMPO	 foreign key (	IDPROVINCIA	)	 references 	CEN_PROVINCIAS	 (	IDPROVINCIA	) 		 Deferrable;
alter table 	CEN_COMPONENTES	 add constraint 	FK_CEN_NOCOLEGIADO_CEN_COMPONE	 foreign key (	IDPERSONA,IDINSTITUCION	)	 references 	CEN_NOCOLEGIADO	 (	IDPERSONA,IDINSTITUCION	) 		 Deferrable;
alter table 	CEN_COMPONENTES	 add constraint 	FK_CEN_CLIENTE_CEN_COMPONENTE	 foreign key (	CEN_CLIENTE_IDINSTITUCION,CEN_CLIENTE_IDPERSONA	)	 references 	CEN_CLIENTE	 (	IDINSTITUCION,IDPERSONA	) 		 Deferrable;
alter table 	CEN_COMPONENTES	 add constraint 	FK_CEN_CARGO_CEN_COMPONENTE	 foreign key (	IDCARGO	)	 references 	CEN_CARGO	 (	IDCARGO	) 		 Deferrable;
alter table 	CEN_COLEGIOPROCURADOR	 drop constraint 	FK_CEN_TIPOVIAPROCURADOR	 ; 
alter table 	CEN_COLEGIOPROCURADOR	 drop constraint 	FK_CEN_PROVINCIAPROCURADOR	 ; 
alter table 	CEN_COLEGIOPROCURADOR	 drop constraint 	FK_CEN_POBLACIONPROCURADOR	 ; 
alter table 	CEN_COLEGIOPROCURADOR	 drop constraint 	FK_CEN_PAISPROCURADOR	 ; 
alter table 	CEN_COLEGIOPROCURADOR	 add constraint 	FK_CEN_TIPOVIAPROCURADOR	 foreign key (	IDINSTITUCION,IDTIPOVIA	)	 references 	CEN_TIPOVIA	 (	IDINSTITUCION,IDTIPOVIA	) 		 Deferrable;
alter table 	CEN_COLEGIOPROCURADOR	 add constraint 	FK_CEN_PROVINCIAPROCURADOR	 foreign key (	IDPROVINCIA	)	 references 	CEN_PROVINCIAS	 (	IDPROVINCIA	) 		 Deferrable;
alter table 	CEN_COLEGIOPROCURADOR	 add constraint 	FK_CEN_POBLACIONPROCURADOR	 foreign key (	IDPOBLACION	)	 references 	CEN_POBLACIONES	 (	IDPOBLACION	) 		 Deferrable;
alter table 	CEN_COLEGIOPROCURADOR	 add constraint 	FK_CEN_PAISPROCURADOR	 foreign key (	IDPAIS	)	 references 	CEN_PAIS	 (	IDPAIS	) 		 Deferrable;
alter table 	CEN_COLEGIO	 drop constraint 	FK_CEN_CLIENTE_CEN_COLEGIO	 ; 
alter table 	CEN_COLEGIO	 add constraint 	FK_CEN_CLIENTE_CEN_COLEGIO	 foreign key (	IDINSTITUCION,IDPERSONA	)	 references 	CEN_CLIENTE	 (	IDINSTITUCION,IDPERSONA	) 		 Deferrable;
alter table 	CEN_COLEGIADO	 drop constraint 	FK_CEN_TIPOSSEGURO_CEN_COLEGIA	 ; 
alter table 	CEN_COLEGIADO	 drop constraint 	FK_CEN_PERSONA_CEN_COLEGIADO	 ; 
alter table 	CEN_COLEGIADO	 drop constraint 	FK_CEN_CLIENTE_CEN_COLEGIADO	 ; 
alter table 	CEN_COLEGIADO	 add constraint 	FK_CEN_TIPOSSEGURO_CEN_COLEGIA	 foreign key (	IDTIPOSSEGURO	)	 references 	CEN_TIPOSSEGURO	 (	IDTIPOSSEGURO	) 		 Deferrable;
alter table 	CEN_COLEGIADO	 add constraint 	FK_CEN_PERSONA_CEN_COLEGIADO	 foreign key (	IDPERSONA	)	 references 	CEN_PERSONA	 (	IDPERSONA	) 		 Deferrable;
alter table 	CEN_COLEGIADO	 add constraint 	FK_CEN_CLIENTE_CEN_COLEGIADO	 foreign key (	IDINSTITUCION,IDPERSONA	)	 references 	CEN_CLIENTE	 (	IDINSTITUCION,IDPERSONA	) 		 Deferrable;
alter table 	CEN_COLACAMBIOLETRADO	 drop constraint 	FK_CEN_COLACAMBIO_TIPO	 ; 
alter table 	CEN_COLACAMBIOLETRADO	 drop constraint 	FK_CEN_COLACAMBIOLETRADO_CLI	 ; 
alter table 	CEN_COLACAMBIOLETRADO	 add constraint 	FK_CEN_COLACAMBIO_TIPO	 foreign key (	IDTIPOCAMBIO	)	 references 	CEN_TIPOCAMBIOCOLA	 (	IDTIPOCAMBIO	) 		 Deferrable;
alter table 	CEN_COLACAMBIOLETRADO	 add constraint 	FK_CEN_COLACAMBIOLETRADO_CLI	 foreign key (	IDINSTITUCION,IDPERSONA	)	 references 	CEN_CLIENTE	 (	IDINSTITUCION,IDPERSONA	) 		 Deferrable;
alter table 	CEN_CLIENTE	 drop constraint 	FK_CEN_TRATAMIENTO_CEN_CLIENTE	 ; 
alter table 	CEN_CLIENTE	 drop constraint 	FK_CEN_PERSONA_CEN_CLIENTE	 ; 
alter table 	CEN_CLIENTE	 drop constraint 	FK_CEN_INSTITUCION_CEN_CLIENTE	 ; 
alter table 	CEN_CLIENTE	 drop constraint 	FK_ADM_LENGUAJES_CEN_CLIENTE	 ; 
alter table 	CEN_CLIENTE	 add constraint 	FK_CEN_TRATAMIENTO_CEN_CLIENTE	 foreign key (	IDTRATAMIENTO	)	 references 	CEN_TRATAMIENTO	 (	IDTRATAMIENTO	) 		 Deferrable;
alter table 	CEN_CLIENTE	 add constraint 	FK_CEN_PERSONA_CEN_CLIENTE	 foreign key (	IDPERSONA	)	 references 	CEN_PERSONA	 (	IDPERSONA	) 		 Deferrable;
alter table 	CEN_CLIENTE	 add constraint 	FK_CEN_INSTITUCION_CEN_CLIENTE	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	CEN_CLIENTE	 add constraint 	FK_ADM_LENGUAJES_CEN_CLIENTE	 foreign key (	IDLENGUAJE	)	 references 	ADM_LENGUAJES	 (	IDLENGUAJE	) 		 Deferrable;
alter table 	CEN_CGAE	 drop constraint 	FK_ID_PERSONA_CEN_CGAE	 ; 
alter table 	CEN_CGAE	 add constraint 	FK_ID_PERSONA_CEN_CGAE	 foreign key (	IDINSTITUCION,IDPERSONA	)	 references 	CEN_CLIENTE	 (	IDINSTITUCION,IDPERSONA	) 		 Deferrable;
alter table 	CEN_CARGAMASIVA	 drop constraint 	FK_GEN_FICHEROGFLOG	 ; 
alter table 	CEN_CARGAMASIVA	 drop constraint 	FK_GEN_FICHEROGF	 ; 
alter table 	CEN_CARGAMASIVA	 add constraint 	FK_GEN_FICHEROGFLOG	 foreign key (	IDFICHEROLOG,IDINSTITUCION	)	 references 	GEN_FICHERO	 (	IDFICHERO,IDINSTITUCION	) 		 Deferrable;
alter table 	CEN_CARGAMASIVA	 add constraint 	FK_GEN_FICHEROGF	 foreign key (	IDFICHERO,IDINSTITUCION	)	 references 	GEN_FICHERO	 (	IDFICHERO,IDINSTITUCION	) 		 Deferrable;
alter table 	CEN_BANCOS	 drop constraint 	FK_CEN_BANCO_PAIS	 ; 
alter table 	CEN_BANCOS	 add constraint 	FK_CEN_BANCO_PAIS	 foreign key (	IDPAIS	)	 references 	CEN_PAIS	 (	IDPAIS	) 		 Deferrable;
alter table 	CEN_BAJASTEMPORALES	 drop constraint 	FK_CENBAJASTEMP_CEN_INSTIT	 ; 
alter table 	CEN_BAJASTEMPORALES	 drop constraint 	FK_CENBAJASTEMP_CEN_COLEGIADO	 ; 
alter table 	CEN_BAJASTEMPORALES	 add constraint 	FK_CENBAJASTEMP_CEN_INSTIT	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	CEN_BAJASTEMPORALES	 add constraint 	FK_CENBAJASTEMP_CEN_COLEGIADO	 foreign key (	IDPERSONA,IDINSTITUCION	)	 references 	CEN_COLEGIADO	 (	IDPERSONA,IDINSTITUCION	) 		 Deferrable;
alter table 	CEN_ANEXOS_CUENTASBANCARIAS	 drop constraint 	FK_MANDATOS_ANEXOS	 ; 
alter table 	CEN_ANEXOS_CUENTASBANCARIAS	 drop constraint 	FK_ANX_GEN_FICHERO	 ; 
alter table 	CEN_ANEXOS_CUENTASBANCARIAS	 add constraint 	FK_MANDATOS_ANEXOS	 foreign key (	IDMANDATO,IDINSTITUCION,IDPERSONA,IDCUENTA	)	 references 	CEN_MANDATOS_CUENTASBANCARIAS	 (	IDMANDATO,IDINSTITUCION,IDPERSONA,IDCUENTA	) 		 Deferrable;
alter table 	CEN_ANEXOS_CUENTASBANCARIAS	 add constraint 	FK_ANX_GEN_FICHERO	 foreign key (	IDFICHEROFIRMA,IDINSTITUCION	)	 references 	GEN_FICHERO	 (	IDFICHERO,IDINSTITUCION	) 		 Deferrable;
alter table 	CEN_ACCESODATOS_CAMPOS	 drop constraint 	FK_CEN_ACCESODATOS_CAMPOS	 ; 
alter table 	CEN_ACCESODATOS_CAMPOS	 drop constraint 	FK_CEN_ACCESODATOS_ACCESODATOS	 ; 
alter table 	CEN_ACCESODATOS_CAMPOS	 add constraint 	FK_CEN_ACCESODATOS_CAMPOS	 foreign key (	IDCAMPOS	)	 references 	CEN_CAMPOS	 (	IDCAMPOS	) 		 Deferrable;
alter table 	CEN_ACCESODATOS_CAMPOS	 add constraint 	FK_CEN_ACCESODATOS_ACCESODATOS	 foreign key (	CARACTER	)	 references 	CEN_ACCESODATOS	 (	CARACTER	) 		 Deferrable;
alter table 	CAJG_TIPOREMESA	 drop constraint 	FK_CAJGTIPOREME_ADMCONTADOR	 ; 
alter table 	CAJG_TIPOREMESA	 add constraint 	FK_CAJGTIPOREME_ADMCONTADOR	 foreign key (	IDCONTADOR,IDINSTITUCION	)	 references 	ADM_CONTADOR	 (	IDCONTADOR,IDINSTITUCION	) 		 Deferrable;
alter table 	CAJG_RESPUESTA_EJGREMESA	 drop constraint 	FK_CAJGRESP_ER_TIPORES	 ; 
alter table 	CAJG_RESPUESTA_EJGREMESA	 drop constraint 	FK_CAJGRESP_EJGREMESA	 ; 
alter table 	CAJG_RESPUESTA_EJGREMESA	 add constraint 	FK_CAJGRESP_ER_TIPORES	 foreign key (	IDTIPORESPUESTA	)	 references 	CAJG_TIPO_RESPUESTA	 (	IDTIPORESPUESTA	) 		 Deferrable;
alter table 	CAJG_RESPUESTA_EJGREMESA	 add constraint 	FK_CAJGRESP_EJGREMESA	 foreign key (	IDEJGREMESA	)	 references 	CAJG_EJGREMESA	 (	IDEJGREMESA	) 		 Deferrable;
alter table 	CAJG_REMESARESOLUCIONFICHERO	 drop constraint 	FK_CAJG_REMERESFICH_REMERESOL	 ; 
alter table 	CAJG_REMESARESOLUCIONFICHERO	 drop constraint 	FK_CAJG_REMERESFICH_ERRORESRES	 ; 
alter table 	CAJG_REMESARESOLUCIONFICHERO	 add constraint 	FK_CAJG_REMERESFICH_REMERESOL	 foreign key (	IDINSTITUCION,IDREMESARESOLUCION	)	 references 	CAJG_REMESARESOLUCION	 (	IDINSTITUCION,IDREMESARESOLUCION	) 		 Deferrable;
alter table 	CAJG_REMESARESOLUCIONFICHERO	 add constraint 	FK_CAJG_REMERESFICH_ERRORESRES	 foreign key (	IDERRORESREMESARESOL,IDINSTITUCION	)	 references 	CAJG_ERRORESREMESARESOL	 (	IDERRORESREMESARESOL,IDINSTITUCION	) 		 Deferrable;
alter table 	CAJG_REMESAESTADOS	 drop constraint 	FK_CAJG_TIPOESTREMESA_REME	 ; 
alter table 	CAJG_REMESAESTADOS	 drop constraint 	FK_CAJG_REMESA_REMEESTADOS	 ; 
alter table 	CAJG_REMESAESTADOS	 add constraint 	FK_CAJG_TIPOESTREMESA_REME	 foreign key (	IDESTADO	)	 references 	CAJG_TIPOESTADOREMESA	 (	IDESTADO	) 		 Deferrable;
alter table 	CAJG_REMESAESTADOS	 add constraint 	FK_CAJG_REMESA_REMEESTADOS	 foreign key (	IDREMESA,IDINSTITUCION	)	 references 	CAJG_REMESA	 (	IDREMESA,IDINSTITUCION	) 		 Deferrable;
alter table 	CAJG_PROCEDIMIENTOREMESARESOL	 drop constraint 	FK_CAJG_TIPOREMESA	 ; 
alter table 	CAJG_PROCEDIMIENTOREMESARESOL	 add constraint 	FK_CAJG_TIPOREMESA	 foreign key (	IDTIPOREMESA,IDINSTITUCION	)	 references 	CAJG_TIPOREMESA	 (	IDTIPOREMESA,IDINSTITUCION	) 		 Deferrable;
alter table 	CAJG_EJGREMESA	 drop constraint 	FK_SCS_EJG_CAJG_EJGREMESA	 ; 
alter table 	CAJG_EJGREMESA	 drop constraint 	FK_CAJG_REMESA_CAJG_EJGREMESA	 ; 
alter table 	CAJG_EJGREMESA	 add constraint 	FK_SCS_EJG_CAJG_EJGREMESA	 foreign key (	IDTIPOEJG,IDINSTITUCION,ANIO,NUMERO	)	 references 	SCS_EJG	 (	IDTIPOEJG,IDINSTITUCION,ANIO,NUMERO	) 		 Deferrable;
alter table 	CAJG_EJGREMESA	 add constraint 	FK_CAJG_REMESA_CAJG_EJGREMESA	 foreign key (	IDREMESA,IDINSTITUCION	)	 references 	CAJG_REMESA	 (	IDREMESA,IDINSTITUCION	) 		 Deferrable;
alter table 	ADM_VALORPREFERENTE	 drop constraint 	FK_ADM_VALORPREFERENTE_BOTON	 ; 
alter table 	ADM_VALORPREFERENTE	 add constraint 	FK_ADM_VALORPREFERENTE_BOTON	 foreign key (	IDINSTITUCION,IDBOTON	)	 references 	ADM_BOTONACCION	 (	IDINSTITUCION,IDBOTON	) 		 Deferrable;
alter table 	ADM_USUARIO_EFECTIVO	 drop constraint 	FK_ADM_USUARIO_EFECTIVO_USUARI	 ; 
alter table 	ADM_USUARIO_EFECTIVO	 drop constraint 	FK_ADM_USUARIO_EFECTIVO_ROL	 ; 
alter table 	ADM_USUARIO_EFECTIVO	 drop constraint 	FK_ADM_USUARIO_EFECTIVO_CERTIF	 ; 
alter table 	ADM_USUARIO_EFECTIVO	 add constraint 	FK_ADM_USUARIO_EFECTIVO_USUARI	 foreign key (	IDINSTITUCION,IDUSUARIO	)	 references 	ADM_USUARIOS	 (	IDINSTITUCION,IDUSUARIO	) 		 Deferrable;
alter table 	ADM_USUARIO_EFECTIVO	 add constraint 	FK_ADM_USUARIO_EFECTIVO_ROL	 foreign key (	IDROL	)	 references 	ADM_ROL	 (	IDROL	) 		 Deferrable;
alter table 	ADM_USUARIO_EFECTIVO	 add constraint 	FK_ADM_USUARIO_EFECTIVO_CERTIF	 foreign key (	NUMSERIE,IDINSTITUCION	)	 references 	ADM_CERTIFICADOS	 (	NUMSERIE,IDINSTITUCION	) 		 Deferrable;
alter table 	ADM_USUARIOS_EFECTIVOS_PERFIL	 drop constraint 	FK_ADM_USU_EFECTIVO_USU_EFECTI	 ; 
alter table 	ADM_USUARIOS_EFECTIVOS_PERFIL	 drop constraint 	FK_ADM_USUARIOS_EFECTIVOS_PER	 ; 
alter table 	ADM_USUARIOS_EFECTIVOS_PERFIL	 add constraint 	FK_ADM_USU_EFECTIVO_USU_EFECTI	 foreign key (	IDROL,IDUSUARIO,IDINSTITUCION	)	 references 	ADM_USUARIO_EFECTIVO	 (	IDROL,IDUSUARIO,IDINSTITUCION	) 		 Deferrable;
alter table 	ADM_USUARIOS_EFECTIVOS_PERFIL	 add constraint 	FK_ADM_USUARIOS_EFECTIVOS_PER	 foreign key (	IDPERFIL,IDINSTITUCION	)	 references 	ADM_PERFIL	 (	IDPERFIL,IDINSTITUCION	) 		 Deferrable;
alter table 	ADM_USUARIOS	 drop constraint 	FK_ADM_LENGUAJES_ADM_USUARIOS	 ; 
alter table 	ADM_USUARIOS	 add constraint 	FK_ADM_LENGUAJES_ADM_USUARIOS	 foreign key (	IDLENGUAJE	)	 references 	ADM_LENGUAJES	 (	IDLENGUAJE	) 		 Deferrable;
alter table 	ADM_TIPOSACCESO	 drop constraint 	FK_GEN_PROCESO_ADM_TIPOSACCESO	 ; 
alter table 	ADM_TIPOSACCESO	 drop constraint 	FK_ADM_PERFIL_ADM_TIPOSACCESO	 ; 
alter table 	ADM_TIPOSACCESO	 add constraint 	FK_GEN_PROCESO_ADM_TIPOSACCESO	 foreign key (	IDPROCESO	)	 references 	GEN_PROCESOS	 (	IDPROCESO	) 		 Deferrable;
alter table 	ADM_TIPOSACCESO	 add constraint 	FK_ADM_PERFIL_ADM_TIPOSACCESO	 foreign key (	IDINSTITUCION,IDPERFIL	)	 references 	ADM_PERFIL	 (	IDINSTITUCION,IDPERFIL	) 		 Deferrable;
alter table 	ADM_TIPOINFORME	 drop constraint 	FK_ADM_TIPOINFORME_SUBTIPO	 ; 
alter table 	ADM_TIPOINFORME	 add constraint 	FK_ADM_TIPOINFORME_SUBTIPO	 foreign key (	IDTIPOINFORMEPADRE	)	 references 	ADM_TIPOINFORME	 (	IDTIPOINFORME	) 		 Deferrable;
alter table 	ADM_TIPOFILTROINFORME	 drop constraint 	FK_ADM_TIPOFILTROINFORME_TIPO	 ; 
alter table 	ADM_TIPOFILTROINFORME	 add constraint 	FK_ADM_TIPOFILTROINFORME_TIPO	 foreign key (	IDTIPOINFORME	)	 references 	ADM_TIPOINFORME	 (	IDTIPOINFORME	) 		 Deferrable;
alter table 	ADM_PERFIL_ROL	 drop constraint 	FK_ADM_ROL_PERFIL_ROL	 ; 
alter table 	ADM_PERFIL_ROL	 drop constraint 	FK_ADM_PERFIL_PERFIL_ROL	 ; 
alter table 	ADM_PERFIL_ROL	 add constraint 	FK_ADM_ROL_PERFIL_ROL	 foreign key (	IDROL	)	 references 	ADM_ROL	 (	IDROL	) 		 Deferrable;
alter table 	ADM_PERFIL_ROL	 add constraint 	FK_ADM_PERFIL_PERFIL_ROL	 foreign key (	IDPERFIL,IDINSTITUCION	)	 references 	ADM_PERFIL	 (	IDPERFIL,IDINSTITUCION	) 		 Deferrable;
alter table 	ADM_PERFIL	 drop constraint 	FK_CEN_INSTITUCION_PERFIL	 ; 
alter table 	ADM_PERFIL	 add constraint 	FK_CEN_INSTITUCION_PERFIL	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	ADM_INFORME	 drop constraint 	FK_ADM_INFORME_TIPO	 ; 
alter table 	ADM_INFORME	 add constraint 	FK_ADM_INFORME_TIPO	 foreign key (	IDTIPOINFORME	)	 references 	ADM_TIPOINFORME	 (	IDTIPOINFORME	) 		 Deferrable;
alter table 	ADM_ENVIOINFORME	 drop constraint 	FK_ADM_ENVIOINFORME_2	 ; 
alter table 	ADM_ENVIOINFORME	 drop constraint 	FK_ADM_ENVIOINFORME_1	 ; 
alter table 	ADM_ENVIOINFORME	 add constraint 	FK_ADM_ENVIOINFORME_2	 foreign key (	IDPLANTILLAENVIODEF,IDINSTITUCION,IDTIPOENVIOS	)	 references 	ENV_PLANTILLASENVIOS	 (	IDPLANTILLAENVIOS,IDINSTITUCION,IDTIPOENVIOS	) 		 Deferrable;
alter table 	ADM_ENVIOINFORME	 add constraint 	FK_ADM_ENVIOINFORME_1	 foreign key (	IDPLANTILLA,IDINSTITUCION	)	 references 	ADM_INFORME	 (	IDPLANTILLA,IDINSTITUCION	) 		 Deferrable;
alter table 	ADM_CONTADOR	 drop constraint 	FK_ADM_CONTADOR_MODO_CONTADOR	 ; 
alter table 	ADM_CONTADOR	 drop constraint 	FK_ADM_CONTADOR_INSTITUCION	 ; 
alter table 	ADM_CONTADOR	 drop constraint 	FK_ADM_CONTADOR_CON_MODULO	 ; 
alter table 	ADM_CONTADOR	 add constraint 	FK_ADM_CONTADOR_MODO_CONTADOR	 foreign key (	MODO	)	 references 	ADM_MODO_CONTADOR	 (	IDMODO	) 		 Deferrable;
alter table 	ADM_CONTADOR	 add constraint 	FK_ADM_CONTADOR_INSTITUCION	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	ADM_CONTADOR	 add constraint 	FK_ADM_CONTADOR_CON_MODULO	 foreign key (	IDMODULO	)	 references 	CON_MODULO	 (	IDMODULO	) 		 Deferrable;
alter table 	ADM_CONSULTAINFORME	 drop constraint 	FK_ADM_CONSULTAINFORME_INFORME	 ; 
alter table 	ADM_CONSULTAINFORME	 drop constraint 	FK_ADM_CONSULTAINFORME_CON	 ; 
alter table 	ADM_CONSULTAINFORME	 add constraint 	FK_ADM_CONSULTAINFORME_INFORME	 foreign key (	IDPLANTILLA,IDINSTITUCION	)	 references 	ADM_INFORME	 (	IDPLANTILLA,IDINSTITUCION	) 		 Deferrable;
alter table 	ADM_CONSULTAINFORME	 add constraint 	FK_ADM_CONSULTAINFORME_CON	 foreign key (	IDINSTITUCION_CONSULTA,IDCONSULTA	)	 references 	CON_CONSULTA	 (	IDINSTITUCION,IDCONSULTA	) 		 Deferrable;
alter table 	ADM_CERTIFICADOS	 drop constraint 	FK_ADM_CERTIFICADOS_USUARIOS	 ; 
alter table 	ADM_CERTIFICADOS	 drop constraint 	FK_ADM_CERTIFICADOS_INTITUCION	 ; 
alter table 	ADM_CERTIFICADOS	 add constraint 	FK_ADM_CERTIFICADOS_USUARIOS	 foreign key (	IDUSUARIO,IDINSTITUCION	)	 references 	ADM_USUARIOS	 (	IDUSUARIO,IDINSTITUCION	) 		 Deferrable;
alter table 	ADM_CERTIFICADOS	 add constraint 	FK_ADM_CERTIFICADOS_INTITUCION	 foreign key (	IDINSTITUCION	)	 references 	CEN_INSTITUCION	 (	IDINSTITUCION	) 		 Deferrable;
alter table 	ADM_BOTONACCION	 drop constraint 	FK_ADM_BOTONACCION_RECURSO	 ; 
alter table 	ADM_BOTONACCION	 add constraint 	FK_ADM_BOTONACCION_RECURSO	 foreign key (	LENGUAJE,NOMBREBOTON	)	 references 	GEN_RECURSOS	 (	IDLENGUAJE,IDRECURSO	) 		 Deferrable;
-- FIN Cambio FKs para nuevo mantenimiento duplicados

-- INI Mantenimiento duplicados
Nuevo pkg_siga_fusion_personas
drop package pkg_fusion_personas;

Insert Into Gen_Properties (fichero, parametro, valor) Values ('SIGA', 'censo.duplicados.directorio', 'duplicados');

Insert Into gen_procesos
  (idproceso, idmodulo, traza, target, fechamodificacion, usumodificacion, descripcion, transaccion, idparent, nivel)
  (Select '18A', idmodulo, traza, target, Sysdate, 0, 'Fusi�n de colegios en producci�n', 'CEN_FusionColegiosEnProduccion', '18', nivel
     From Gen_Procesos
    Where transaccion = 'CEN_MantenimientoDuplicados');

Update Gen_Procesos Set Descripcion = 'HIDDEN_' || Descripcion Where Idproceso In ('18');
Delete From Adm_Tiposacceso Where Idproceso = '18' And Idinstitucion <> 2000;
-- FIN Mantenimiento duplicados

-- Ejecutado en Integracion por AAG el 19/12 a las 13:00

Actualizado Pkg_Siga_Fusion_Personas

-- Ejecutado en Integracion por AAG el 22/12 a las 12:40

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.busquedaDuplicados.botonInforme', 'Informe general de duplicados', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.busquedaDuplicados.botonInforme', 'Informe general de duplicats', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.busquedaDuplicados.botonInforme', 'Informe general de duplicados#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.busquedaDuplicados.botonInforme', 'Informe general de duplicados#GL', 0, '4', sysdate, 0, '19');

alter table 	CEN_SANCION	 drop constraint 	FK_CEN_SANCION_SANCION_CGAE	 ; 
alter table 	CEN_SANCION	 add constraint 	FK_CEN_SANCION_SANCION_CGAE	 foreign key (	IDPERSONA, IDSANCIONORIGEN, IDINSTITUCIONORIGEN	)	 references 	CEN_SANCION	 (	IDPERSONA, IDSANCION, IDINSTITUCION	) 		 Deferrable;

alter table 	CEN_PARTIDOJUDICIAL	 drop constraint 	SYS_C0043299	 ; --Solo funciona en PRO
alter table 	CEN_PARTIDOJUDICIAL	 drop constraint 	SYS_C0016807	 ; --Solo funciona en PRE

-- Ejecutado en Integracion por AAG el 29/12 a las 15:00

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.funcionalidadNoDisponible', 'Esta funcionalidad no est� disponible. Consulte con el Administrador', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.funcionalidadNoDisponible', 'Esta funcionalidad no est� disponible. Consulte con el Administrador#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.funcionalidadNoDisponible', 'Esta funcionalidad no est� disponible. Consulte con el Administrador#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.funcionalidadNoDisponible', 'Esta funcionalidad no est� disponible. Consulte con el Administrador#EU', 0, '3', sysdate, 0, '19');

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.ningunaSeleccionFusionar', 'No se ha seleccionado nada. Seleccione dos personas para fusionar.', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.ningunaSeleccionFusionar', 'No se ha seleccionado nada. Seleccione dos personas para fusionar.#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.ningunaSeleccionFusionar', 'No se ha seleccionado nada. Seleccione dos personas para fusionar.#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.ningunaSeleccionFusionar', 'No se ha seleccionado nada. Seleccione dos personas para fusionar.#EU', 0, '3', sysdate, 0, '19');

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.incorrectaSeleccionFusionar', 'Selecci�n incorrecta. Seleccione dos personas para fusionar.', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.incorrectaSeleccionFusionar', 'Selecci�n incorrecta. Seleccione dos personas para fusionar.#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.incorrectaSeleccionFusionar', 'Selecci�n incorrecta. Seleccione dos personas para fusionar.#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.incorrectaSeleccionFusionar', 'Selecci�n incorrecta. Seleccione dos personas para fusionar.#EU', 0, '3', sysdate, 0, '19');
        
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.fusionEnCurso', 'Ya se ha solicitado la combinaci�n de alguna de estas personas. Espere unos minutos hasta que termine.', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.fusionEnCurso', 'Ya se ha solicitado la combinaci�n de alguna de estas personas. Espere unos minutos hasta que termine.#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.fusionEnCurso', 'Ya se ha solicitado la combinaci�n de alguna de estas personas. Espere unos minutos hasta que termine.#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.fusionEnCurso', 'Ya se ha solicitado la combinaci�n de alguna de estas personas. Espere unos minutos hasta que termine.#EU', 0, '3', sysdate, 0, '19');

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.sinPermisoFusionar1', 'No est� permitida la fusi�n por seguridad. El colegio de ', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.sinPermisoFusionar1', 'No est� permitida la fusi�n por seguridad. El colegio de #GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.sinPermisoFusionar1', 'No est� permitida la fusi�n por seguridad. El colegio de #CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.sinPermisoFusionar1', 'No est� permitida la fusi�n por seguridad. El colegio de #EU', 0, '3', sysdate, 0, '19');
           
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.sinPermisoFusionar2', ' usa SIGA y puede contener datos delicados. Por favor, consulte con el Administrador.', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.sinPermisoFusionar2', ' usa SIGA y puede contener datos delicados. Por favor, consulte con el Administrador.#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.sinPermisoFusionar2', ' usa SIGA y puede contener datos delicados. Por favor, consulte con el Administrador.#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.sinPermisoFusionar2', ' usa SIGA y puede contener datos delicados. Por favor, consulte con el Administrador.#EU', 0, '3', sysdate, 0, '19');

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.fusionEnCursoNuevaBusqueda', 'Ya se ha solicitado la combinaci�n de alguna de estas personas. Por favor, realice una nueva b�squeda.', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.fusionEnCursoNuevaBusqueda', 'Ya se ha solicitado la combinaci�n de alguna de estas personas. Por favor, realice una nueva b�squeda.#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.fusionEnCursoNuevaBusqueda', 'Ya se ha solicitado la combinaci�n de alguna de estas personas. Por favor, realice una nueva b�squeda.#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.fusionEnCursoNuevaBusqueda', 'Ya se ha solicitado la combinaci�n de alguna de estas personas. Por favor, realice una nueva b�squeda.#EU', 0, '3', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.registroMismoPagoSJCS', 'Ambas personas tienen registros en el mismo Pago SJCS. Por favor, consulte al Administrador.', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.registroMismoPagoSJCS', 'Ambas personas tienen registros en el mismo Pago SJCS. Por favor, consulte al Administrador.#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.registroMismoPagoSJCS', 'Ambas personas tienen registros en el mismo Pago SJCS. Por favor, consulte al Administrador.#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.registroMismoPagoSJCS', 'Ambas personas tienen registros en el mismo Pago SJCS. Por favor, consulte al Administrador.#EU', 0, '3', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.guardiaMismoDia', 'Ambas personas tienen guardia en el mismo d�a. Por favor, consulte al Administrador.', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.guardiaMismoDia', 'Ambas personas tienen guardia en el mismo d�a. Por favor, consulte al Administrador.#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.guardiaMismoDia', 'Ambas personas tienen guardia en el mismo d�a. Por favor, consulte al Administrador.#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.guardiaMismoDia', 'Ambas personas tienen guardia en el mismo d�a. Por favor, consulte al Administrador.#EU', 0, '3', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.solicitadaFusionEstadoEnCurso', 'Ya se solicit� la generaci�n del informe de duplicados. Espere unos minutos hasta que termine.', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.solicitadaFusionEstadoEnCurso', 'Ya se solicit� la generaci�n del informe de duplicados. Espere unos minutos hasta que termine.#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.solicitadaFusionEstadoEnCurso', 'Ya se solicit� la generaci�n del informe de duplicados. Espere unos minutos hasta que termine.#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.solicitadaFusionEstadoEnCurso', 'Ya se solicit� la generaci�n del informe de duplicados. Espere unos minutos hasta que termine.#EU', 0, '3', sysdate, 0, '19');

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.fusionarColegiosProduccion', 'Error al obtener el permiso para fusionar personas en colegios en producci�n.', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.fusionarColegiosProduccion', 'Error al obtener el permiso para fusionar personas en colegios en producci�n.#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.fusionarColegiosProduccion', 'Error al obtener el permiso para fusionar personas en colegios en producci�n.#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.fusionarColegiosProduccion', 'Error al obtener el permiso para fusionar personas en colegios en producci�n.#EU', 0, '3', sysdate, 0, '19');

        
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.generacionInforme', 'Se ha empezado a generar el informe. Si no se descarga ahora, vuelva a intentarlo m�s tarde.', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.generacionInforme', 'Se ha empezado a generar el informe. Si no se descarga ahora, vuelva a intentarlo m�s tarde.#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.generacionInforme', 'Se ha empezado a generar el informe. Si no se descarga ahora, vuelva a intentarlo m�s tarde.#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.generacionInforme', 'Se ha empezado a generar el informe. Si no se descarga ahora, vuelva a intentarlo m�s tarde.#EU', 0, '3', sysdate, 0, '19');

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.descargarInformeDeHoy', 'Descargando el informe del d�a de hoy. Si quiere actualizar los datos, vuelva a generar el informe ma�ana.', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.descargarInformeDeHoy', 'Descargando el informe del d�a de hoy. Si quiere actualizar los datos, vuelva a generar el informe ma�ana.#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.descargarInformeDeHoy', 'Descargando el informe del d�a de hoy. Si quiere actualizar los datos, vuelva a generar el informe ma�ana.#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.descargarInformeDeHoy', 'Descargando el informe del d�a de hoy. Si quiere actualizar los datos, vuelva a generar el informe ma�ana.#EU', 0, '3', sysdate, 0, '19');

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.reviseDatos', 'Revise todos los conjuntos de datos antes de aceptar la combinaci�n.', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.reviseDatos', 'Revise todos los conjuntos de datos antes de aceptar la combinaci�n.#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.reviseDatos', 'Revise todos los conjuntos de datos antes de aceptar la combinaci�n.#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.reviseDatos', 'Revise todos los conjuntos de datos antes de aceptar la combinaci�n.#EU', 0, '3', sysdate, 0, '19');

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.espera', 'Este proceso puede durar varios minutos. Por favor, espere...', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.espera', 'Este proceso puede durar varios minutos. Por favor, espere...#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.espera', 'Este proceso puede durar varios minutos. Por favor, espere...#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('messages.error.censo.mantenimientoDuplicados.espera', 'Este proceso puede durar varios minutos. Por favor, espere...#EU', 0, '3', sysdate, 0, '19');
Actualizacion Pkg_Siga_Fusion_Personas


-- Ejecutado en Integracion por AAG el 13/01 a las 13:30
              
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.busquedaDuplicados.patron.numeroIdentificacion', 'N�mero Identificaci�n', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.busquedaDuplicados.patron.numeroIdentificacion', 'N�mero Identificaci�n#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.busquedaDuplicados.patron.numeroIdentificacion', 'N�mero Identificaci�n#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.busquedaDuplicados.patron.numeroIdentificacion', 'N�mero Identificaci�n#EU', 0, '3', sysdate, 0, '19');
        
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.busquedaDuplicados.patron.ordenNumeroIdentificacion', 'Se ordena por N�mero Identificaci�n', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.busquedaDuplicados.patron.ordenNumeroIdentificacion', 'Se ordena por N�mero Identificaci�n#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.busquedaDuplicados.patron.ordenNumeroIdentificacion', 'Se ordena por N�mero Identificaci�n#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.busquedaDuplicados.patron.ordenNumeroIdentificacion', 'Se ordena por N�mero Identificaci�n#EU', 0, '3', sysdate, 0, '19');
        
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.busquedaDuplicados.patron.ordenInstitucionYNumeroIdentificacion', 'Se ordena por Instituci�n y N�mero de colegiado', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.busquedaDuplicados.patron.ordenInstitucionYNumeroIdentificacion', 'Se ordena por Instituci�n y N�mero de colegiado#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.busquedaDuplicados.patron.ordenInstitucionYNumeroIdentificacion', 'Se ordena por Instituci�n y N�mero de colegiado#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.busquedaDuplicados.patron.ordenInstitucionYNumeroIdentificacion', 'Se ordena por Instituci�n y N�mero de colegiado#EU', 0, '3', sysdate, 0, '19');
        
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.busquedaDuplicados.patron.apellidos', 'Apellidos', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.busquedaDuplicados.patron.apellidos', 'Apellidos#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.busquedaDuplicados.patron.apellidos', 'Apellidos#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.busquedaDuplicados.patron.apellidos', 'Apellidos#EU', 0, '3', sysdate, 0, '19');
    
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.busquedaDuplicados.patron.ordenApellidosYNombre', 'Se ordena por Apellidos y Nombre', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.busquedaDuplicados.patron.ordenApellidosYNombre', 'Se ordena por Apellidos y Nombre#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.busquedaDuplicados.patron.ordenApellidosYNombre', 'Se ordena por Apellidos y Nombre#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.busquedaDuplicados.patron.ordenApellidosYNombre', 'Se ordena por Apellidos y Nombre#EU', 0, '3', sysdate, 0, '19');  
        
        
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.gestionarDuplicado.patron.identificacion', 'Identificaci�n', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.gestionarDuplicado.patron.identificacion', 'Identificaci�n#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.gestionarDuplicado.patron.identificacion', 'Identificaci�n#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.gestionarDuplicado.patron.identificacion', 'Identificaci�n#EU', 0, '3', sysdate, 0, '19');
        
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.gestionarDuplicado.patron.ultimaModificacion', '�ltima Modificaci�n', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.gestionarDuplicado.patron.ultimaModificacion', '�ltima Modificaci�n#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.gestionarDuplicado.patron.ultimaModificacion', '�ltima Modificaci�n#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.gestionarDuplicado.patron.ultimaModificacion', '�ltima Modificaci�n#EU', 0, '3', sysdate, 0, '19');
        
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.gestionarDuplicado.patron.modificarDatos', '>> Modificar datos >>', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.gestionarDuplicado.patron.modificarDatos', '>> Modificar datos >>#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.gestionarDuplicado.patron.modificarDatos', '>> Modificar datos >>#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.gestionarDuplicado.patron.modificarDatos', '>> Modificar datos >>#EU', 0, '3', sysdate, 0, '19');
        
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.gestionarDuplicado.patron.seleccionDestino', 'Seleccione cu�l ser� el destino', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.gestionarDuplicado.patron.seleccionDestino', 'Seleccione cu�l ser� el destino#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.gestionarDuplicado.patron.seleccionDestino', 'Seleccione cu�l ser� el destino#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.gestionarDuplicado.patron.seleccionDestino', 'Seleccione cu�l ser� el destino#EU', 0, '3', sysdate, 0, '19');
        
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.gestionarDuplicado.patron.datosGenerales', '(datos generales que se conservar�n)', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.gestionarDuplicado.patron.datosGenerales', '(datos generales que se conservar�n)#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.gestionarDuplicado.patron.datosGenerales', '(datos generales que se conservar�n)#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.gestionarDuplicado.patron.datosGenerales', '(datos generales que se conservar�n)#EU', 0, '3', sysdate, 0, '19');
        
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.gestionarDuplicado.patron.noDisponible', 'No disponible', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.gestionarDuplicado.patron.noDisponible', 'No disponible#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.gestionarDuplicado.patron.noDisponible', 'No disponible#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.gestionarDuplicado.patron.noDisponible', 'No disponible#EU', 0, '3', sysdate, 0, '19');
      
  insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.gestionarDuplicado.patron.sexo', 'Sexo', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.gestionarDuplicado.patron.sexo', 'Sexo#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.gestionarDuplicado.patron.sexo', 'Sexo#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.gestionarDuplicado.patron.sexo', 'Sexo#EU', 0, '3', sysdate, 0, '19');
        
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.gestionarDuplicado.patron.estadoCivil', 'Estado civil', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.gestionarDuplicado.patron.estadoCivil', 'Estado civil#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.gestionarDuplicado.patron.estadoCivil', 'Estado civil#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.gestionarDuplicado.patron.estadoCivil', 'Estado civil#EU', 0, '3', sysdate, 0, '19');
        
        
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.gestionarDuplicado.patron.fechaNacimiento', 'fecha de nacimiento', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.gestionarDuplicado.patron.fechaNacimiento', 'fecha de nacimiento#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.gestionarDuplicado.patron.fechaNacimiento', 'fecha de nacimiento#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.gestionarDuplicado.patron.fechaNacimiento', 'fecha de nacimiento#EU', 0, '3', sysdate, 0, '19');
        
        
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.gestionarDuplicado.patron.lugarNacimiento', 'lugar de nacimiento', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.gestionarDuplicado.patron.lugarNacimiento', 'lugar de nacimiento#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.gestionarDuplicado.patron.lugarNacimiento', 'lugar de nacimiento#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.gestionarDuplicado.patron.lugarNacimiento', 'lugar de nacimiento#EU', 0, '3', sysdate, 0, '19');
        
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.gestionarDuplicado.patron.sancionesYCertificados', 'Sanciones y Certificados', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.gestionarDuplicado.patron.sancionesYCertificados', 'Sanciones y Certificados#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.gestionarDuplicado.patron.sancionesYCertificados', 'Sanciones y Certificados#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.gestionarDuplicado.patron.sancionesYCertificados', 'Sanciones y Certificados#EU', 0, '3', sysdate, 0, '19');
        
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.gestionarDuplicado.patron.historicoEstados', 'Hist�rico de estados', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.gestionarDuplicado.patron.historicoEstados', 'Hist�rico de estados#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.gestionarDuplicado.patron.historicoEstados', 'Hist�rico de estados#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.gestionarDuplicado.patron.historicoEstados', 'Hist�rico de estados#EU', 0, '3', sysdate, 0, '19');         

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.gestionarDuplicado.patron.ultimaActualizacionExterna', '�ltima actualizaci�n externa', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.gestionarDuplicado.patron.ultimaActualizacionExterna', '�ltima actualizaci�n externa#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.gestionarDuplicado.patron.ultimaActualizacionExterna', '�ltima actualizaci�n externa#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
        values ('censo.gestionarDuplicado.patron.ultimaActualizacionExterna', '�ltima actualizaci�n externa#EU', 0, '3', sysdate, 0, '19'); 

-- Ejecutado en Integracion por AAG el 16/01 a las 10:30

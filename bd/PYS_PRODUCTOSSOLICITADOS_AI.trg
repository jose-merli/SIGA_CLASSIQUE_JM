CREATE OR REPLACE TRIGGER PYS_PRODUCTOSSOLICITADOS_AI  
AFTER INSERT
ON pys_productossolicitados
REFERENCING NEW AS NEW OLD AS OLD
FOR EACH ROW
Declare
  v_Escertificado          Number;
  v_Instituciondestino     Pys_Productossolicitados.Idinstitucionorigen%Type;

Begin

  --Si la institucion es CGAE o Consejo metemos como Institucion Destino el mismo que el Origen
  -- Si la institucion es un colegio dejamos la institucion destino a nulo
  If (:New.Idinstitucion = 2000 Or Substr(:New.Idinstitucion, 1, 2) = 30) Then
    v_Instituciondestino := :New.Idinstitucionorigen;
  Else
    v_Instituciondestino := Null;
  End If;

  --Si el producto es de Tipo Certificado insertamos un registro en CER_SOLICITUDCERTIFICADOS:
  Select Count(*)
    Into v_Escertificado
    From Pys_Productosinstitucion Pi
   Where Pi.Idinstitucion = :New.Idinstitucion
     And Pi.Idtipoproducto = :New.Idtipoproducto
     And Pi.Idproducto = :New.Idproducto
     And Pi.Idproductoinstitucion = :New.Idproductoinstitucion
     And Pi.Tipocertificado = 'C';

  If (v_Escertificado > 0) Then

    For i In 1 .. :New.Cantidad Loop
      Insert Into Cer_Solicitudcertificados
        (Idinstitucion, Idsolicitud, Fechasolicitud, Idestadosolicitudcertificado,
         Idinstitucion_Sol, Idinstitucionorigen, Idinstituciondestino, Idinstitucioncolegiacion,
         Idpersona_Des, Idpersona_Dir, Iddireccion_Dir, Idtipoenvios, Ppn_Idtipoproducto,
         Ppn_Idproductoinstitucion, Ppn_Idproducto, Idestadocertificado, Fechaestado,
         Fechaemisioncertificado, Fechamodificacion, Usumodificacion, Idpeticionproducto,
         Idmetodosolicitud, Aceptacesionmutualidad, Fechacreacion, Usucreacion)
      Values
        (:New.Idinstitucion, Seq_Solicitudcertificados.Nextval, :New.Fecharecepcionsolicitud, 1,
         :New.Idinstitucion, :New.Idinstitucionorigen, v_Instituciondestino,
         :New.Idinstitucioncolegiacion, :New.Idpersona, :New.Idpersona, :New.Iddireccion,
         :New.Idtipoenvios, :New.Idtipoproducto, :New.Idproductoinstitucion, :New.Idproducto, 1,
         Sysdate, Null, :New.Fechamodificacion, :New.Usumodificacion, :New.Idpeticion,
         :New.Metodorecepcionsolicitud, :New.Aceptacesionmutualidad, Sysdate, :New.Usumodificacion);
    End Loop;

  End If;
End Pys_Productossolicitados_Ai;
/

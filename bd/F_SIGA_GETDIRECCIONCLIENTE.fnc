create or replace function F_SIGA_GETDIRECCIONCLIENTE(p_idinstitucion   in CEN_DIRECCIONES.IDINSTITUCION%type,
                                                      p_idpersona       in CEN_DIRECCIONES.IDPERSONA%type,
                                                      p_idtipodireccion in CEN_DIRECCION_TIPODIRECCION.IDTIPODIRECCION%type,
                                                      p_campodireccion  in number)
  return VARCHAR2 is

/*******************************************************************************************************************/
/* Nombre:        F_SIGA_GETDIRECCIONCLIENTE                                                                       */
/* Descripcion:   Funcion que devolverá el campo especificado de la Dirección activa del tipo especificado,        */
/*                 perteneciente a la Institución y Persona especificada. (La primera que encuentre de ese Tipo).  */
/*                Si dicha Persona no tuviera en esa Institución, una Dirección activa del tipo especificado,      */
/*                 entonces se toma la primera dirección activa que se encuentre para esa Persona e Institución,   */
/*                 sin importar el Tipo de Dirección que sea.                                                      */
/*                Si no existe ninguna dirección activa para esa Persona e Institución (de ningún tipo),           */
/*                 entonces devuelve ''.                                                                           */
/*                                                                                                                 */
/* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos     */
/* -------------------   ------   ------------------------------------------------------------   -------------     */
/* P_IDINSTITUCION       IN        Identificador de la Institucion.                                NUMBER(4)       */
/* P_IDTURNO             IN        Identificador del Turno.                                        NUMBER(5)       */
/* P_IDTIPODIRECCION     IN        Identificador del Tipo de Dirección.                            NUMBER(22)      */
/* P_CAMPODIRECCION      IN        Identificador del Campo de la Dirección deseado.                NUMBER          */
/*                                                                                                                 */
/* Version:        1.1                                                                                             */
/* Fecha Creacion:                                                                                                 */
/* Autor:                                                                                                          */
/* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                               */
/* ------------------   ---------------------------------   ---------------------------------------------------    */
/*                                                                                                                 */
/*******************************************************************************************************************/

  r_direccion         CEN_DIRECCIONES%rowtype;
  v_domicilio         CEN_DIRECCIONES.DOMICILIO%type;
  v_codigopostal      CEN_DIRECCIONES.CODIGOPOSTAL%type;
  v_provincia         CEN_PROVINCIAS.NOMBRE%type;
  v_poblacion         CEN_POBLACIONES.NOMBRE%type;
  v_telefono1         CEN_DIRECCIONES.TELEFONO1%type;
  v_telefono2         CEN_DIRECCIONES.TELEFONO2%type;
  v_movil             CEN_DIRECCIONES.MOVIL%type;
  v_fax1              CEN_DIRECCIONES.FAX1%type;
  v_fax2              CEN_DIRECCIONES.FAX2%type;
  v_correoelectronico CEN_DIRECCIONES.CORREOELECTRONICO%type;
  v_paginaweb         CEN_DIRECCIONES.PAGINAWEB%type;
  v_colegioorigen     Cen_Institucion.Abreviatura%Type;

BEGIN

  --realizando la consulta
  begin
    -- Devuelve la primera dirección activa, que tenga esa Persona del Tipo especificado.
    select *
      into r_direccion
      from CEN_DIRECCIONES DIR
     where dir.fechabaja is null
       and dir.idinstitucion = p_idinstitucion
       and dir.idpersona = p_idpersona
       and exists (select 1
              from CEN_DIRECCION_TIPODIRECCION TIP
             where dir.idinstitucion = tip.idinstitucion
               and dir.idpersona = tip.idpersona
               and dir.iddireccion = tip.iddireccion
               and tip.idtipodireccion = p_idtipodireccion)
       and rownum = 1;

    -- Si no tiene dirección del tipo especificado, toma la primera activa que encuentra para esta Persona.
    if r_direccion.iddireccion is null then
      select *
        into r_direccion
        from CEN_DIRECCIONES DIR
       where dir.fechabaja is null
         and dir.idinstitucion = p_idinstitucion
         and dir.idpersona = p_idpersona
         and rownum = 1;
    end if;

  exception
    when no_data_found then
      begin
        -- Si ocurre una excepción por no devolver datos, toma la primera activa que encuentra para esta Persona.
        select *
          into r_direccion
          from CEN_DIRECCIONES DIR
         where dir.fechabaja is null
           and dir.idinstitucion = p_idinstitucion
           and dir.idpersona = p_idpersona
           and rownum = 1;
      exception
        when no_data_found then
          -- Si no encuentra ninguna, devuelve ''.
          return '';
      end;
  end;

  --obteniendo los campos de direccion postal
  v_domicilio    := r_direccion.domicilio;
  v_codigopostal := r_direccion.codigopostal;

  if r_direccion.poblacionextranjera is null then
    -- La Dirección es española.
    if r_direccion.idpoblacion is null then
      v_poblacion := '';
    else
      select nombre
        into v_poblacion
        from CEN_POBLACIONES
       where idpoblacion = r_direccion.idpoblacion;
    end if;
    if r_direccion.idprovincia is null then
      v_provincia := '';
    else
      select nombre
        into v_provincia
        from CEN_PROVINCIAS
       where idprovincia = r_direccion.idprovincia;
    end if;
  else
    -- La Dirección es extranjera.
    select f_siga_getrecurso(nombre, 1)
      into v_poblacion
      from Cen_Pais
     where idpais = r_direccion.Idpais;
    v_provincia := '';
    v_poblacion := r_direccion.poblacionextranjera || ' (' || v_poblacion || ')';
  end if;
  
  Begin
    select abreviatura
      into v_colegioorigen
      from Cen_Institucion
     where IDINSTITUCION = r_direccion.Idinstitucionalta;
  Exception
    When Others Then
      v_colegioorigen := Null;
  End;

  --obteniendo otros campos de direccion
  v_telefono1         := r_direccion.telefono1;
  v_telefono2         := r_direccion.telefono2;
  v_movil             := r_direccion.movil;
  v_fax1              := r_direccion.fax1;
  v_fax2              := r_direccion.fax2;
  v_correoelectronico := r_direccion.correoelectronico;
  v_paginaweb         := r_direccion.paginaweb;

  --devolviendo el campo pedido
  if p_campodireccion is null then
    return v_domicilio || '; ' || v_codigopostal || ' ' || v_poblacion || ' (' || v_provincia || ')';
  elsif p_campodireccion = 1 then
    return v_domicilio;
  elsif p_campodireccion = 2 then
    return v_codigopostal;
  elsif p_campodireccion = 3 then
    return v_poblacion;
  elsif p_campodireccion = 4 then
    return v_provincia;
  elsif p_campodireccion = 11 then
    return v_telefono1;
  elsif p_campodireccion = 12 then
    return v_telefono2;
  elsif p_campodireccion = 13 then
    return v_movil;
  elsif p_campodireccion = 14 then
    return v_fax1;
  elsif p_campodireccion = 15 then
    return v_fax2;
  elsif p_campodireccion = 16 then
    return v_correoelectronico;
  elsif p_campodireccion = 17 then
    return v_paginaweb;
  elsif p_campodireccion = 20 Then
    Return v_colegioorigen;
  else
    return v_domicilio || '; ' || v_codigopostal || ' ' || v_poblacion || ' (' || v_provincia || ')';
  end if;
END;

 
/

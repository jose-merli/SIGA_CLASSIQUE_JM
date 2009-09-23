package com.atos.utils;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

public interface FileProvider
{
  public InputStream getFile(String[] params) throws Exception;
  public void setRequest(HttpServletRequest req) throws Exception;
}
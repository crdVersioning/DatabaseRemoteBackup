package com.dps.databaseremotebackup;

import static com.dps.databaseremotebackup.Dao.DBI;
import com.dps.dbi.DbResult;
import com.dps.dbi.JsonMode;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDateTime;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ApiServlet extends HttpServlet
{   
    static
    {
        DbResult.defaultJsonMode = JsonMode.LIST_OF_OBJECTS;
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        JsonObject jsonResponse = new JsonObject();
        
        String requestUrl = request.getRequestURL().toString();
        String requestUri = request.getRequestURI();
        String servletPath = request.getServletPath();
        String pathInfo = request.getPathInfo();
        String queryString = request.getQueryString();
        
        jsonResponse.addProperty("requestUrl",requestUrl);
        jsonResponse.addProperty("requestUri",requestUri);
        jsonResponse.addProperty("servletPath",servletPath);
        jsonResponse.addProperty("pathInfo",pathInfo);
        jsonResponse.addProperty("queryString",queryString);
        
        jsonResponse.addProperty("request_ts",LocalDateTime.now().toString());
        jsonResponse.add("response_ts",null);
        
        jsonResponse.addProperty("success",true);
        jsonResponse.add("data",null);
        
        try
        {
            if(pathInfo!=null) switch(pathInfo)
            {
                case "/info" -> processInfo(jsonResponse);
                case "/select" -> processSelect(request,jsonResponse);
            }
        } 
        catch(Exception ex)
        {
            jsonResponse.addProperty("success",false);
            jsonResponse.addProperty("error",ex.toString());
        }
        
        response.setContentType("text/json;charset=UTF-8");
        try(PrintWriter out = response.getWriter())
        {
            jsonResponse.addProperty("response_ts",LocalDateTime.now().toString());
            out.println(jsonResponse);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {processRequest(request, response);}
    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {processRequest(request, response);}
    @Override protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {processRequest(request, response);}
    @Override protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {processRequest(request, response);}
    @Override public String getServletInfo() {return "Short description";}
    // </editor-fold>

    private void processInfo(JsonObject jsonResponse)
    {
        jsonResponse.addProperty("data","DATABASE REMOTE BACKUP ONLINE");
    }

    private void processSelect(HttpServletRequest request, JsonObject jsonResponse) throws SQLException
    {
        String source = request.getParameter("source");
        String where = request.getParameter("where");
        String order = request.getParameter("order");
        
        var jsonData = DBI.read(source).where(where!=null,where).order(order!=null?order:null).go().toJson();
        
        jsonResponse.add("data",jsonData);
    }
}

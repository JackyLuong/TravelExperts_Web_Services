package com.example.travelexperts_web_services;

import com.example.travelexperts_web_services.util.DBConnectionMngr;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.*;

@Path("/province")
public class ProvinceResource {


    @GET
    @Produces(MediaType.APPLICATION_JSON) // returns Json Data
    public String getAllProvinces() {
        String responseString = null;
        try {
            DBConnectionMngr cm = DBConnectionMngr.getInstance();
            Connection conn = cm.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("Select * From Provinces");
            ResultSetMetaData rsmd = rs.getMetaData();
            JSONArray jsonArray = new JSONArray();
            while (rs.next()){
                JSONObject jsonObject = new JSONObject();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    jsonObject.put(rsmd.getColumnName(i), rs.getString(i));

                }
                jsonArray.add(jsonObject);
            }

            responseString = jsonArray.toJSONString();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return responseString;
    }

    //get all province/state by selected country

    @GET
    @Path("/getprovinces/{country}")
    @Produces(MediaType.APPLICATION_JSON) // returns Json Data
    public String getProvinces(@PathParam("country") String country) {
        String responseString = null;
        try {
            DBConnectionMngr cm = DBConnectionMngr.getInstance();
            Connection conn = cm.getConnection();
            String psql = "Select * From Provinces WHERE COUNTRYNAME=?";
            PreparedStatement pstmt = conn.prepareStatement(psql);
            pstmt.setString(1,country);
            ResultSet rs = pstmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            JSONArray jsonArray = new JSONArray();
            while (rs.next()){
                JSONObject jsonObject = new JSONObject();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    jsonObject.put(rsmd.getColumnName(i), rs.getString(i));

                }
                jsonArray.add(jsonObject);
            }

            responseString = jsonArray.toJSONString();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return responseString;
    }

    //get country by the selected

    @GET
    @Path("/getcountry/{stateId}")
    @Produces(MediaType.APPLICATION_JSON) // returns Json Data
    public String getCountry(@PathParam("stateId") String stateId) {
        String responseString = null;
        try {
            DBConnectionMngr cm = DBConnectionMngr.getInstance();
            Connection conn = cm.getConnection();
            String psql = "Select * From Provinces WHERE StateId=?";
            PreparedStatement pstmt = conn.prepareStatement(psql);
            pstmt.setString(1,stateId);
            ResultSet rs = pstmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            JSONArray jsonArray = new JSONArray();
            while (rs.next()){
                JSONObject jsonObject = new JSONObject();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    jsonObject.put(rsmd.getColumnName(i), rs.getString(i));

                }
                jsonArray.add(jsonObject);
            }

            responseString = jsonArray.toJSONString();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return responseString;
    }




}
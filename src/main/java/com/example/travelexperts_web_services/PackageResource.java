/*
 * Author: Jacky Luong
 */
package com.example.travelexperts_web_services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.TravelPackage;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.Type;
import java.util.List;

@Path("/packages")
public class PackageResource {
    public PackageResource()
    {
        try
        {
            //Mariadb driver to connect to database
            Class.forName("org.mariadb.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Get all packages
     * @return JSON string of all packages
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getPackages()
    {
        String response = ""; //returns JSON string
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();
        Query query = em.createQuery("SELECT a FROM TravelPackage a"); //select all Packages
        List<TravelPackage> packageList = query.getResultList(); //all packages

        Type type = new TypeToken<List<TravelPackage>>(){}.getType();
        Gson gson = new Gson();

        //convert packages to JSON String
        response = gson.toJson(packageList, type);
        em.close(); //close connection

        return response;
    }

    /**
     * Get selected package
     * @param packageId
     * @return
     */
    @Path("/get-package/{PackageId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getPackage(@PathParam("PackageId") int packageId)
    {
        String response = ""; //return JSON string
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();
        TravelPackage selectedPackage = em.find(TravelPackage.class, packageId); //get package based on given package ID.

        Gson gson = new Gson();
        response = gson.toJson(selectedPackage); //convert package to JSON String
        em.close();//close connection
        return response;
    }

    /**
     * Update Package information
     * @param JSONString
     * @return
     */
    @Path("/post-packages")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public String postPackages(String JSONString)
    {
        String response = ""; //returns JSON String
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();

        Gson gson = new Gson();
        //Create new package with information from JSONString
        TravelPackage newPackages = gson.fromJson(JSONString, TravelPackage.class);

        em.getTransaction().begin();
        //Update package
        TravelPackage mergedPackage = em.merge(newPackages);
        if(mergedPackage != null) //update succeeds
        {
            em.getTransaction().commit(); //commit update
            em.close();//close connection
            response = "{'message': 'Package was updated successfully'}";
        }
        else//update fails
        {
            em.getTransaction().rollback();//undo changes
            em.close();//close connection
            response = "{'message': 'Failed to update package'}";
        }
        return response;
    }

    /**
     * Add package to table
     * @param JSONString
     * @return
     */
    @Path("/add-package")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public String addPackage(String JSONString)
    {
        String response = ""; //return JSON String
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();

        Gson gson = new Gson();
        //create new package with information from JSONString
        TravelPackage newPackage = gson.fromJson(JSONString, TravelPackage.class);

        em.getTransaction().begin();
        em.persist(newPackage); //add new package
        if(em.contains(newPackage)) //added successfully
        {
            em.getTransaction().commit();//commit changes
            em.close();//close connection
            response = "{'message': 'Package was inserted successfully'}";
        }
        else
        {
            em.getTransaction().rollback(); //undo changes
            em.close();//close connection
            response = "{'message': 'Failed to insert Package'}";
        }
        return response;
    }

    /**
     * Delete selected package
     * @param packageId
     * @return
     */
    @Path("/delete-package/{PackageId}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public String deletePackage(@PathParam("PackageId") int packageId)
    {
        String response = ""; //returns JSON string
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();

        //get selected package
        TravelPackage selectedPackage = em.find(TravelPackage.class, packageId);
        if(selectedPackage != null) //packages exists
        {
            em.getTransaction().begin();
            em.remove(selectedPackage);//delete package

            if(!em.contains(selectedPackage))//deleted successfully
            {
                em.getTransaction().commit();//commit changes
                em.close();//close connection
                response = "{'message': 'Package was deleted successfully'}";
            }
            else
            {
                em.getTransaction().rollback();//undo changes
                em.close();//close connection
                response = "{'message': 'Failed to delete package'}";
            }
        }
        else //package doesn't exist
        {
            response = "{'message': 'package doesn't exist.'}";
        }
        return response;
    }
}
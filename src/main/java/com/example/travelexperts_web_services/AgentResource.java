package com.example.travelexperts_web_services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Agent;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.Type;
import java.util.List;

@Path("/agent")
public class AgentResource {
    public AgentResource() {
        try {
            //Mariadb driver to connect to database
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get selected agent
     *
     * @param AgtEmail
     * @return
     */
    @Path("/get-agent/{agentEmail}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getCustomer(@PathParam("agentEmail") String AgtEmail) {
        String response = ""; //return JSON string
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();
        Agent agent = (Agent) em.createQuery("Select a from Agent a where a.agtEmail = ?1").setParameter(1, AgtEmail).getSingleResult();
        Type type = new TypeToken<Agent>(){}.getType();
        Gson gson = new Gson();
        response = gson.toJson(agent, type); //convert customer to JSON String
        em.close();//close connection
        return response;


    }

}

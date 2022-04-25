package com.example.travelexperts_web_services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Customer;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.Type;
import java.util.List;

@Path("/customers")
public class CustomerResource {
    public CustomerResource() {
        try {
            //Mariadb driver to connect to database
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get all customer
     *
     * @return JSON string of all customers
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getCustomers() {
        String response = ""; //returns JSON string
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();
        Query query = em.createQuery("SELECT a FROM Customer a order by a.custLastName"); //select all Customers
        List<Customer> customersList = query.getResultList(); //all customer
        Type type = new TypeToken<List<Customer>>() {
        }.getType();
        Gson gson = new Gson();

        //convert customer to JSON String
        response = gson.toJson(customersList, type);
        em.close(); //close connection

        return response;
    }

    /**
     * Get selected customer
     *
     * @param customerId
     * @return
     */
    @Path("/get-customer/{CustomerId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getCustomer(@PathParam("CustomerId") int customerId) {
        String response = ""; //return JSON string
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();
        Customer selectedCustomer = em.find(Customer.class, customerId); //get customer based on given customer ID.

        Gson gson = new Gson();
        response = gson.toJson(selectedCustomer); //convert customer to JSON String
        em.close();//close connection
        return response;
    }

    /**
     * Update customer information
     *
     * @param JSONString
     * @return
     */
    @Path("/post-customer")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public String postCustomer(String JSONString) {
        String response = ""; //returns JSON String
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();

        Gson gson = new Gson();
        //Create new Customer with information from JSONString
        Customer newCustomer = gson.fromJson(JSONString, Customer.class);

        em.getTransaction().begin();
        //Update Customer
        Customer mergedCustomer = em.merge(newCustomer);
        if (mergedCustomer != null) //update succeeds
        {
            em.getTransaction().commit(); //commit update
            em.close();//close connection
            response = "{\"message\":\"Customer was updated successfully\"}";
        } else//update fails
        {
            em.getTransaction().rollback();//undo changes
            em.close();//close connection
            response = "{\"message\":\"Failed to update Customer\"}";
        }
        return response;
    }

    /**
     * Add Customer to table
     *
     * @param JSONString
     * @return
     */
    @Path("/add-customer")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public String addCustomer(String JSONString) {
        String response = ""; //return JSON String
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();

        Gson gson = new Gson();
        //create new Customer with information from JSONString
        Customer newCustomer = gson.fromJson(JSONString, Customer.class);
        em.getTransaction().begin();
        em.persist(newCustomer); //add new Customer
        if (em.contains(newCustomer)) //added successfully
        {
            em.getTransaction().commit();//commit changes
            em.close();//close connection
            response = "{\"message\":\"Customer was inserted successfully\"}";
        } else {
            em.getTransaction().rollback(); //undo changes
            em.close();//close connection
            response = "{\"message\":\"Failed to insert Customer\"}";
        }
        return response;
    }

    /**
     * Delete selected Customer
     *
     * @param CustomerId
     * @return
     */
    @Path("/delete-customer/{CustomerId}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteCustomer(@PathParam("CustomerId") int CustomerId) {
        String response = ""; //returns JSON string
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();

        //get selected Customer
        Customer selectedCustomer = em.find(Customer.class, CustomerId);
        if (selectedCustomer != null) //Customer exists
        {
            //try-catch. To handle failed customer delete due to fk constraint
            try{
                em.getTransaction().begin();
                em.remove(selectedCustomer);//delete Customer

                if (!em.contains(selectedCustomer))//deleted successfully
                {
                    em.getTransaction().commit();//commit changes
                    em.close();//close connection
                    response = "{\"message\": \"Customer was deleted successfully\"}";
                } else {
                    em.getTransaction().rollback();//undo changes
                    em.close();//close connection
                    response = "{\"message\": \"Failed to delete Customer\"}";
                }
            }catch (Exception e){
                response = "{\"message\": \"Failed to delete Customer. Check with Administrator\"}";
            }

        } else //Customer doesn't exist
        {
            response = "{\"message\": \"Customer doesn't exist.\"}";
        }
        return response;
    }
}

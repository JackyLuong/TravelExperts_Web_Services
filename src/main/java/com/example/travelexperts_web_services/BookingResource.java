/*
 * Author: Jacky Luong
 */
package com.example.travelexperts_web_services;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import model.Booking;
import model.Triptype;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.Type;
import java.util.List;

@Path("/bookings")
public class BookingResource
{
    public BookingResource()
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
     * Get all Booking
     * @return JSON string of all booking
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getBookings()
    {
        String response = ""; //returns JSON string
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();
        Query query = em.createQuery("SELECT a FROM Booking a order by  a.bookingDate desc"); //select all Bookings
        List<Booking> bookingsList = query.getResultList(); //all booking
        Type bookingType = new TypeToken<List<Booking>>(){}.getType();
        Gson gson = new Gson();
        //convert Booking to JSON String
        response = gson.toJson(bookingsList, bookingType);
        em.close(); //close connection

        return response;
    }

    /**
     * Get selected booking
     * @param bookingId
     * @return
     */
    @Path("/get-booking/{BookingId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getBooking(@PathParam("BookingId") int bookingId)
    {
        String response = ""; //return JSON string
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();
        Booking selectedBooking = em.find(Booking.class, bookingId); //get booking based on given Booking ID.

        Gson gson = new Gson();
        response = gson.toJson(selectedBooking); //convert Booking to JSON String
        em.close();//close connection
        return response;
    }

    /**
     * Update Booking information
     * @param JSONString
     * @return
     */
    @Path("/post-booking")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public String postBooking(String JSONString)
    {
        String response = ""; //returns JSON String
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();

        Gson gson = new Gson();
        //Create new booking with information from JSONString
        Booking newBooking = gson.fromJson(JSONString, Booking.class);

        em.getTransaction().begin();
        //Update booking
        Booking mergedBooking = em.merge(newBooking);
        if(mergedBooking != null) //update succeeds
        {
            em.getTransaction().commit(); //commit update
            em.close();//close connection
            response = "{'message': 'Booking was updated successfully'}";
        }
        else//update fails
        {
            em.getTransaction().rollback();//undo changes
            em.close();//close connection
            response = "{'message': 'Failed to update booking'}";
        }
        return response;
    }

    /**
     * Add Booking to table
     * @param JSONString
     * @return
     */
    @Path("/add-booking")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public String addBooking(String JSONString)
    {
        String response = ""; //return JSON String
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();

        Gson gson = new Gson();
        //create new booking with information from JSONString
        Booking newBooking = gson.fromJson(JSONString, Booking.class);

        em.getTransaction().begin();
        em.persist(newBooking); //add new booking
        if(em.contains(newBooking)) //added successfully
        {
            em.getTransaction().commit();//commit changes
            em.close();//close connection
            response = "{'message': 'Booking was inserted successfully'}";
        }
        else
        {
            em.getTransaction().rollback(); //undo changes
            em.close();//close connection
            response = "{'message': 'Failed to insert Booking'}";
        }
        return response;
    }

    /**
     * Delete selected Booking
     * @param bookingId
     * @return
     */
    @Path("/delete-booking/{BookingId}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteBooking(@PathParam("BookingId") int bookingId)
    {
        String response = ""; //returns JSON string
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();

        //get selected booking
        Booking selectedBooking = em.find(Booking.class, bookingId);
        if(selectedBooking != null) //Booking exists
        {
            em.getTransaction().begin();
            em.remove(selectedBooking);//delete Booking

            if(!em.contains(selectedBooking))//deleted successfully
            {
                em.getTransaction().commit();//commit changes
                em.close();//close connection
                response = "{'message': 'Booking was deleted successfully'}";
            }
            else
            {
                em.getTransaction().rollback();//undo changes
                em.close();//close connection
                response = "{'message': 'Failed to delete booking'}";
            }
        }
        else //Booking doesn't exist
        {
            response = "{'message': 'Booking doesn't exist.'}";
        }
        return response;
    }


    /**
     * Get selected booking tryiptype
     * @param
     * @return
     */
    @Path("/triptype")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getTripType()
    {

        String response = ""; //returns JSON string
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();
        Query query = em.createQuery("SELECT a FROM Triptype a "); //select all Bookings
        List<Triptype> triptypeList = query.getResultList(); //all booking
        Type tripType = new TypeToken<List<Booking>>(){}.getType();
        Gson gson = new Gson();
        //convert Booking to JSON String
        response = gson.toJson(triptypeList, tripType);
        em.close(); //close connection

        return response;

    }
}

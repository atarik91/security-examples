package org.jboss.security.examples.customer;

import java.util.List;

import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Path("/")
@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
public class CustomersResource {
	
    CustomerData data = new CustomerData();
    
    @GET
    @Path("/customerList")
    @Produces({ MediaType.APPLICATION_XML })
	public String  getCustomers() {
        return data.getCustomers();
    }
    
    @GET
    @Path("/getAll")
    public List<Customer>  getCustomerList() {
        return data.getCustomerList();
    }
    
    @GET
    @Path("/getByNumber/{customernumber}")
    public Customer getCustomerByNumber(@PathParam("customernumber") String customernumber) {
        return data.getCustomerByNumber(customernumber);
    }
    
    @GET
    @Path("/getByName/{customername}")
    public Customer  getCustomerByName(@PathParam("customername") String customername) {
        return data.getCustomerByName(customername);
    }
    
    @GET
    @Path("/getByCity/{city}")
    public Customer  getCustomerByCity(@PathParam("city") String city) {
        return data.getCustomerByCity(city);
    }
    
    @GET
    @Path("/getByCountry/{country}")
    public Customer  getCustomerByCountry(@PathParam("country") String country) {
        return data.getCustomerByCountry(country);
    }
}

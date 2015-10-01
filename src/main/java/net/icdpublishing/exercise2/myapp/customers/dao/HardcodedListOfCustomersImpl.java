package net.icdpublishing.exercise2.myapp.customers.dao;

import java.util.*;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import net.icdpublishing.exercise2.myapp.charging.services.ImaginaryChargingService;
import net.icdpublishing.exercise2.myapp.customers.domain.Customer;
import net.icdpublishing.exercise2.myapp.customers.domain.CustomerType;
import net.icdpublishing.exercise2.searchengine.domain.Record;
import net.icdpublishing.exercise2.searchengine.domain.SourceType;

import javax.annotation.Nullable;

public class HardcodedListOfCustomersImpl implements CustomerDao {

	private static Map<String,Customer> customers = new HashMap<String, Customer>();

	public HardcodedListOfCustomersImpl() {
    	customers.put("john.doe@192.com", createDummyCustomer("john.doe@192.com", "John", "Doe", CustomerType.PREMIUM));
		customers.put("sally.smith@192.com", createDummyCustomer("sally.smith@192.com", "Sally", "Smith", CustomerType.PREMIUM));
		customers.put("harry.lang@192.com", createDummyCustomer("harry.lang@192.com", "Harry", "Lang", CustomerType.NON_PAYING));
	}
	
	public Customer findCustomerByEmailAddress(String email) throws CustomerNotFoundException {
		Customer customer = customers.get(email);
		if(customer == null) {
			throw new CustomerNotFoundException("Invalid customer");
		}	
		return customer;
	}

    private Customer createDummyCustomer(String email, String forename, String surname, CustomerType type) {
		Customer c = new Customer();
		c.setEmailAddress(email);
		c.setForename(forename);
		c.setSurname(surname);
		c.setCustomType(type);
		return c;
	}


}
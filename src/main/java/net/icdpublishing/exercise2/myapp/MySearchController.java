package net.icdpublishing.exercise2.myapp;

import net.icdpublishing.exercise2.myapp.customers.dao.CustomerAuthenticationException;
import net.icdpublishing.exercise2.myapp.customers.dao.CustomerNotFoundException;
import net.icdpublishing.exercise2.myapp.customers.domain.Customer;
import net.icdpublishing.exercise2.myapp.customers.service.CustomerService;
import net.icdpublishing.exercise2.searchengine.domain.Record;
import net.icdpublishing.exercise2.searchengine.requests.SimpleSurnameAndPostcodeQuery;
import net.icdpublishing.exercise2.searchengine.services.SearchEngineRetrievalService;

import java.util.Collection;

public class MySearchController {
    private static final String CUSTOMER_NOT_REGISTERED = "The current customer is not registered in our system";
    private SearchEngineRetrievalService retrievalService;
    private CustomerService customerService;


    public MySearchController(SearchEngineRetrievalService retrievalService,
                              CustomerService customerService) {
		this.retrievalService = retrievalService;
        this.customerService = customerService;
	}
	
	public Collection<Record> handleRequest(SearchRequest request) {
        if(isCustomerAuthenticated(request.getCustomer())) {
            Collection<Record> resultSet = getResults(request.getQuery());
            return customerService.filterByCustomerType(resultSet, request.getCustomer());
        }
        throw new CustomerAuthenticationException(CUSTOMER_NOT_REGISTERED);
	}

    private boolean isCustomerAuthenticated(Customer customer) {
        try {
            customerService.findCustomerByEmailAddress(customer.getEmailAddress());
        }catch (CustomerNotFoundException ex){
            return false;
        }
        return true;
    }

    private Collection<Record> getResults(SimpleSurnameAndPostcodeQuery query) {
		return retrievalService.search(query);
	}

}
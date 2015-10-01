package net.icdpublishing.exercise2.myapp.customers.service;

import net.icdpublishing.exercise2.myapp.customers.dao.CustomerNotFoundException;
import net.icdpublishing.exercise2.myapp.customers.domain.Customer;
import net.icdpublishing.exercise2.searchengine.domain.Record;

import java.util.Collection;

/**
 * Created by ruben on 1/10/15.
 */
public interface CustomerService {

    Customer findCustomerByEmailAddress(String email) throws CustomerNotFoundException;

    Collection<Record> filterByCustomerType(Collection<Record> resultSet, Customer customer);
}

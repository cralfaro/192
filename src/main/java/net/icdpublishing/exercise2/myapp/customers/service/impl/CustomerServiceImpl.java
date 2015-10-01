package net.icdpublishing.exercise2.myapp.customers.service.impl;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import net.icdpublishing.exercise2.myapp.charging.services.ImaginaryChargingService;
import net.icdpublishing.exercise2.myapp.customers.dao.CustomerDao;
import net.icdpublishing.exercise2.myapp.customers.dao.CustomerNotFoundException;
import net.icdpublishing.exercise2.myapp.customers.domain.Customer;
import net.icdpublishing.exercise2.myapp.customers.domain.CustomerType;
import net.icdpublishing.exercise2.myapp.customers.service.CustomerService;
import net.icdpublishing.exercise2.searchengine.domain.Record;
import net.icdpublishing.exercise2.searchengine.domain.SourceType;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by ruben on 1/10/15.
 */
public class CustomerServiceImpl implements CustomerService{

    private static final int UNIQUE_RESULT = 1;

    private ImaginaryChargingService imaginaryChargingService;
    private CustomerDao customerDao;

    public CustomerServiceImpl(CustomerDao customerDao, ImaginaryChargingService imaginaryChargingService){
        this.imaginaryChargingService = imaginaryChargingService;
        this.customerDao = customerDao;
    }

    @Override
    public Customer findCustomerByEmailAddress(String email) throws CustomerNotFoundException {
        return customerDao.findCustomerByEmailAddress(email);
    }

    @Override
    public Collection<Record> filterByCustomerType(Collection<Record> resultSet, Customer customer) {
        List<Record> orderedRecords;
        if(CustomerType.NON_PAYING.equals(customer.getCustomType())){
            orderedRecords = Lists.newArrayList(filterRecordsForNonPayableCustomer(resultSet));
        }
        else{
            int creditsToPay = resultSet.size() - filterRecordsForNonPayableCustomer(resultSet).size();
            //TODO Ruben I didn't found not method to check how many credits have a particular customer, to verify that the credits has been correctly updated
            imaginaryChargingService.charge(customer.getEmailAddress(), creditsToPay);
            orderedRecords = Lists.newArrayList(resultSet);
        }
        Collections.sort(orderedRecords, new RecordComparator());
        return orderedRecords;
    }

    private Collection<Record> filterRecordsForNonPayableCustomer(Collection<Record> resultSet) {
        List<Record> results = Lists.newArrayList(FluentIterable.from(resultSet).filter(new Predicate<Record>() {
            @Override
            public boolean apply(@Nullable Record record) {
                return record.getSourceTypes().size() == UNIQUE_RESULT && record.getSourceTypes().contains(SourceType.BT);
            }
        }).toList());
        return results;
    }

    private class RecordComparator implements Comparator<Record> {
        @Override
        public int compare(Record record1, Record record2) {
            return record1.getPerson().getSurname().compareTo(record2.getPerson().getSurname());
        }
    }
}

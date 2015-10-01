package net.icdpublishing.exercise2.myapp.search.controller;

import com.google.common.collect.Lists;
import net.icdpublishing.exercise2.myapp.MySearchController;
import net.icdpublishing.exercise2.myapp.SearchRequest;
import net.icdpublishing.exercise2.myapp.charging.services.ImaginaryChargingService;
import net.icdpublishing.exercise2.myapp.customers.dao.CustomerAuthenticationException;
import net.icdpublishing.exercise2.myapp.customers.dao.HardcodedListOfCustomersImpl;
import net.icdpublishing.exercise2.myapp.customers.domain.Customer;
import net.icdpublishing.exercise2.myapp.customers.domain.CustomerType;
import net.icdpublishing.exercise2.myapp.customers.service.CustomerService;
import net.icdpublishing.exercise2.myapp.customers.service.impl.CustomerServiceImpl;
import net.icdpublishing.exercise2.searchengine.domain.Record;
import net.icdpublishing.exercise2.searchengine.loader.DataLoader;
import net.icdpublishing.exercise2.searchengine.requests.SimpleSurnameAndPostcodeQuery;
import net.icdpublishing.exercise2.searchengine.services.DummyRetrievalServiceImpl;
import net.icdpublishing.exercise2.searchengine.services.SearchEngineRetrievalService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

import static org.hamcrest.core.Is.is;

public class MySearchControllerTest {

    private static final String AUTHENTICATED_CUSTOMER_EMAIL = "john.doe@192.com";
    private static final String NOT_AUTHENTICATED_CUSTOMER_EMAIL = "fake.john.doe@192.com";


    private MySearchController controller;
    private SearchEngineRetrievalService retrievalService;
    private CustomerService customerService;
	
	@Before
	public void setUp() throws Exception {
        DataLoader dataLoader = new DataLoader();
        retrievalService = new DummyRetrievalServiceImpl(dataLoader);
        customerService = new CustomerServiceImpl(new HardcodedListOfCustomersImpl(), new ImaginaryChargingService());
        controller = new MySearchController(retrievalService, customerService);
	}

	@Test
	public void shouldReturnOnlyRecordsFromBTDataSource() {
        SimpleSurnameAndPostcodeQuery query = buildSearchRequestForNonPayableCustomer();
        SearchRequest searchRequest = new SearchRequest(query, buildNonPayableCustomer());
        Collection<Record> records = controller.handleRequest(searchRequest);
        Assert.assertThat(records.size(), is(1));
	}

    @Test
    public void shouldReturnNotRecordsFromBTDataSource() {
        SimpleSurnameAndPostcodeQuery query = buildSearchRequestForNonPayableCustomerWithNotResults();
        SearchRequest searchRequest = new SearchRequest(query, buildNonPayableCustomer());
        Collection<Record> records = controller.handleRequest(searchRequest);
        Assert.assertThat(records.size(), is(0));
    }

    @Test
    public void testAuthenticatedCustomerRequestAListOfRecords() {
        SimpleSurnameAndPostcodeQuery query = buildSearchRequestForPremiumCustomer();
        Customer premiumCustomer = buildPremiumCustomer();
        SearchRequest searchRequest = new SearchRequest(query, premiumCustomer);
        Collection<Record> records = controller.handleRequest(searchRequest);
        Assert.assertThat(records.size(), is(3));
    }


    @Test(expected=CustomerAuthenticationException.class)
    public void testNotAuthenticatedCustomerGetsException() {
        SimpleSurnameAndPostcodeQuery query = buildSearchRequestForPremiumCustomer();
        Customer notAuthenticatedUser = buildNotAuthenticatedCustomer();
        SearchRequest searchRequest = new SearchRequest(query, notAuthenticatedUser);
        controller.handleRequest(searchRequest);
    }

    @Test
    public void testListOfRecordsIsOrderedBySurnameAsc() {
        SimpleSurnameAndPostcodeQuery query = buildSearchRequestForPremiumCustomer();
        Customer premiumCustomer = buildPremiumCustomer();
        SearchRequest searchRequest = new SearchRequest(query, premiumCustomer);
        Collection<Record> records = controller.handleRequest(searchRequest);
        Assert.assertThat(records.size(), is(3));
        //TODO this test is not very usefull but as we only can order by surname, and all records have the same surname...
        List<Record> recordsList = Lists.newArrayList(records);
        Assert.assertThat("Smith", is(recordsList.get(0).getPerson().getSurname()));
        Assert.assertThat("Smith", is(recordsList.get(1).getPerson().getSurname()));
        Assert.assertThat("Smith", is(recordsList.get(2).getPerson().getSurname()));

    }

    private Customer buildPremiumCustomer() {
        Customer c = new Customer();
        c.setCustomType(CustomerType.PREMIUM);
        c.setEmailAddress(AUTHENTICATED_CUSTOMER_EMAIL);
        return c;
    }

    private Customer buildNotAuthenticatedCustomer() {
        Customer c = new Customer();
        c.setCustomType(CustomerType.PREMIUM);
        c.setEmailAddress(NOT_AUTHENTICATED_CUSTOMER_EMAIL);
        return c;
    }

    private Customer buildNonPayableCustomer() {
        Customer c = new Customer();
        c.setCustomType(CustomerType.NON_PAYING);
        c.setEmailAddress(AUTHENTICATED_CUSTOMER_EMAIL);
        return c;
    }

    private SimpleSurnameAndPostcodeQuery buildSearchRequestForNonPayableCustomer() {
        return new SimpleSurnameAndPostcodeQuery("Smith", "sw6 2bq");
    }

    private SimpleSurnameAndPostcodeQuery buildSearchRequestForNonPayableCustomerWithNotResults() {
        return new SimpleSurnameAndPostcodeQuery("Cole", "sw6 2bq");
    }

    private SimpleSurnameAndPostcodeQuery buildSearchRequestForPremiumCustomer() {
        return new SimpleSurnameAndPostcodeQuery("Smith", "sw6 2bq");
    }

}

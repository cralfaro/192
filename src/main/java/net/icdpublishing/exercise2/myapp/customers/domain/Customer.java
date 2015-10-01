package net.icdpublishing.exercise2.myapp.customers.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

//TODO Ruben in most of the cases we can save time using this annotations, just when our equals or hashCode need some particular conditions we can code it manually
//@ToString(of = {"forename", "surname"}) or ToString to use all parameters
//@EqualsAndHashCode() here we can too use properties as exclude, or of to decide which parameters will be part of the equals and hashCode
public class Customer {

    //TODO Ruben. I tried to use here lombok, but i cant run mvn clean install for some compilation failures (relative to 1.6 version i think)

//    @Getter @Setter
//	  private String forename;
//    @Getter @Setter
//    private String surname;
//    @Getter @Setter
//    private String emailAddress;
//    @Getter @Setter
//    private CustomerType customerType;

    private String forename;
    private String surname;
    private String emailAddress;
    private CustomerType customerType;

    public String getForename() {
        return forename;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public CustomerType getCustomType() {
        return customerType;
    }

    public void setCustomType(CustomerType customType) {
        this.customerType = customType;
    }


    //TODO Ruben with Lombok we can too create some annotation in the header class to build the equals, hashCode and toString
    //TODO The inconvenience is that we need to keep updated the parameters to take into account on those methods

    @Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
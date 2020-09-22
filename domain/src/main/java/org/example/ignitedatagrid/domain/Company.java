package org.example.ignitedatagrid.domain;

public class Company {
    private Long id;
    private String name;
    private String taxCode;
    private Address primaryAddress;
    private User responsiblePerson;

    public Company(Long id, String name, String taxCode, Address primaryAddress, User responsiblePerson) {
        this.id = id;
        this.name = name;
        this.taxCode = taxCode;
        this.primaryAddress = primaryAddress;
        this.responsiblePerson = responsiblePerson;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public Address getPrimaryAddress() {
        return primaryAddress;
    }

    public void setPrimaryAddress(Address primaryAddress) {
        this.primaryAddress = primaryAddress;
    }

    public User getResponsiblePerson() {
        return responsiblePerson;
    }

    public void setResponsiblePerson(User responsiblePerson) {
        this.responsiblePerson = responsiblePerson;
    }
}

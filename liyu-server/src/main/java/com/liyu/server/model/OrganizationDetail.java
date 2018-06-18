package com.liyu.server.model;

import com.liyu.server.tables.pojos.Contact;

import java.util.List;

public class OrganizationDetail extends OrganizationExtend {
    private List<String> contactIds;
    private List<Contact> contacts;
    private String contactNameTitle;

    public OrganizationDetail(OrganizationExtend organization) {
        super(organization);
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public List<String> getContactIds() {
        return contactIds;
    }

    public void setContactIds(List<String> contactIds) {
        this.contactIds = contactIds;
    }

    public String getContactNameTitle() {
        return contactNameTitle;
    }

    public void setContactNameTitle(String contactNameTitle) {
        this.contactNameTitle = contactNameTitle;
    }
}

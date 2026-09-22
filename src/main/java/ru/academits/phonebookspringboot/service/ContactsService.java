package ru.academits.phonebookspringboot.service;

import ru.academits.phonebookspringboot.data.Contact;

import java.util.List;

public interface ContactsService {
    List<Contact> getAll(String term);

    void create(Contact contact);

    void update(Contact contact);

    void delete(int contactId);

    boolean isPhoneExists(String phone, int contactId);
}

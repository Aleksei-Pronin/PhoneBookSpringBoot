package ru.academits.phonebookspringboot.service;

import ru.academits.phonebookspringboot.data.Contact;

import java.util.List;

public interface ContactsService {
    List<Contact> get(String term);

    void create(Contact contact);

    void update(int id, Contact contact);

    void delete(int contactId);

    void delete(List<Integer> contactIds);
}

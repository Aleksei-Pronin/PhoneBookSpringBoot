package ru.academits.phonebookspringboot.service;

import ru.academits.phonebookspringboot.data.BaseResponse;
import ru.academits.phonebookspringboot.data.Contact;

import java.util.List;

public interface ContactsService {
    List<Contact> get(String term);

    BaseResponse create(Contact contact);

    BaseResponse update(Contact contact, int contactId);

    BaseResponse delete(int contactId);

    BaseResponse delete(List<Integer> contactIds);
}

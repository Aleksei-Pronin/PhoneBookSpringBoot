package ru.academits.phonebookspringboot.repository;

import ru.academits.phonebookspringboot.data.BaseResponse;
import ru.academits.phonebookspringboot.data.Contact;

import java.util.List;

public interface ContactsRepository {
    List<Contact> getAll();

    List<Contact> search(String term);

    BaseResponse create(Contact contact);

    BaseResponse update(Contact contact, int contactId);

    BaseResponse delete(int contactId);

    BaseResponse delete(List<Integer> contactIds);

    boolean isPhoneExists(String phone, int contactId);
}
package com.gda.dbgenerator.sample;

import com.gda.dbgenerator.annotations.Field;
import com.gda.dbgenerator.annotations.Table;

/**
 * @author Gian Darren Azriel Aquino
 * @since 9/1/14
 */
  @Table
  public class Contact {

    @Field(type = Field.Type.INTEGER, unique = true, notNull = true)
    private int contactId;

    @Field(type = Field.Type.TEXT)
    private String firstName;

    @Field(type = Field.Type.TEXT)
    private String lastName;

    @Field(type = Field.Type.INTEGER)
    private int age;

    public int getContactId() {
      return contactId;
    }

    public void setContactId(int contactId) {
      this.contactId = contactId;
    }

    public String getFirstName() {
      return firstName;
    }

    public void setFirstName(String firstName) {
      this.firstName = firstName;
    }

    public String getLastName() {
      return lastName;
    }

    public void setLastName(String lastName) {
      this.lastName = lastName;
    }

    public int getAge() {
      return age;
    }

    public void setAge(int age) {
      this.age = age;
    }

    @Override public String toString() {
      return "Contact{" +
          "contactId=" + contactId +
          ", firstName='" + firstName + '\'' +
          ", lastName='" + lastName + '\'' +
          ", age=" + age +
          '}';
    }
  }

AndroidDatabaseGenerator
========================

How to use:

1. Add _@DbConfig(databaseName = "YourDatabaseName", databaseVersion = YourDatabaseVersion)_ into your Application.

```
@DbConfig(databaseName = "MyDatabase", databaseVersion = 1)
public class MyApplication extends Application {

  @Override public void onCreate() {
    super.onCreate();
  }
}
```

2. Annotate your Model with _@Table_ and _@Field_. Note that there should be a _Getter_ and _Setter_ for your Field.

```
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
}

```

3. Compile your Project and it will generate the ff. files.
  
  a.) DatabaseOpenHelper
  b.) DAO for each Model
  
  
Sample Usage based on the example above:

```
//Insert
Contact contact = new Contact(); //Your Model
contact.setContactId(1);
contact.setFirstName("Hello");
contact.setLastName("World");
contact.setAge("2014");
ContactDAO.getInstance(getApplicationContext()).insert(contact);

//Query
List<Contact> contactList = ContactDAO.getInstance(getApplicationContext()).queryAll(SORT_ORDER);
```

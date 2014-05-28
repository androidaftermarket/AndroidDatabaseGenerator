GDAndroidDBGenerator
====================

Android DB Generator

A DB Generator for android using annotations.

Generates SQLiteOpenHelper class which will be [DatabaseName]OpenHelper.

Generates Table Class for each Model which will be [ModelClassName]Table which extends BaseColumn.

Generates DAO for each Table.


HOW TO USE:
===========

Use @DbConfig for the configuration of the Database

```
@DbConfig
  databaseName
  databaseVersion 
```

NOTE: Must have ONE @DbConfig or the classes will not be generated

```
@DbConfig(databaseName = "MyDatabase", databaseVersion = 1)
public class Config {
}
```

Use @Table to Identify that the class is a table in the database

Use @Field to Identify the field in the table 

```
@Field 
  type    = TEXT/INTEGER (default TEXT)
  unique  = true/false   (default false)
  notNull = true/false   (default false)
```

NOTE: Must have Getter and Setter for each Field

```
@Table
public class User {

    @Field(unique = true)
    private String name;

    @Field(type = Field.Type.INTEGER)
    private int age;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```
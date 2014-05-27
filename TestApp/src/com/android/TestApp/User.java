package com.android.TestApp;

import com.android.gda.dbgenerator.annotations.Column;
import com.android.gda.dbgenerator.annotations.Table;

/**
 * Created by gdaAquino on 5/27/14.
 */
@Table
public class User {

    @Column
    private String name;

    @Column(type = Column.Type.INTEGER)
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("name='").append(name).append('\'');
        sb.append(", age='").append(age).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

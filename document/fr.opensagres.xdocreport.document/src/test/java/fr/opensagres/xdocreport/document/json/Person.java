package fr.opensagres.xdocreport.document.json;

import java.io.InputStream;
import java.util.Date;

public class Person
{

    private String name;

    private int age;

    private Date birthday;

    private InputStream photo;
    
    private boolean married;

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public int getAge()
    {
        return age;
    }

    public void setAge( int age )
    {
        this.age = age;
    }

    public Date getBirthday()
    {
        return birthday;
    }

    public void setBirthday( Date birthday )
    {
        this.birthday = birthday;
    }

    public InputStream getPhoto()
    {
        return photo;
    }

    public void setPhoto( InputStream photo )
    {
        this.photo = photo;
    }

    public boolean isMarried()
    {
        return married;
    }

    public void setMarried( boolean married )
    {
        this.married = married;
    }

    
}

package fr.opensagres.xdocreport.template.formatter.cli;

import java.util.Date;
import java.util.List;

public class Person
{

    private String firstname;

    private String lastName;

    private Date birthDate;

    private List<Skill> skills;

    public String getFirstname()
    {
        return firstname;
    }

    public void setFirstname( String firstname )
    {
        this.firstname = firstname;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName( String lastName )
    {
        this.lastName = lastName;
    }

    public Date getBirthDate()
    {
        return birthDate;
    }

    public void setBirthDate( Date birthDate )
    {
        this.birthDate = birthDate;
    }

    public List<Skill> getSkills()
    {
        return skills;
    }

    public void setSkills( List<Skill> skills )
    {
        this.skills = skills;
    }

}

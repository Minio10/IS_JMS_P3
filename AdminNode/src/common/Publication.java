package common;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
@Entity
public class Publication implements Serializable
{
private static final long serialVersionUID = 1L;
// we use this generation type to match that of SQLWriteStudents
@OnDelete(action = OnDeleteAction.CASCADE)
@Id
@GeneratedValue(strategy = GenerationType.AUTO)
private Long id;
private String name;
private String type;
private String pubdate;



public Publication(){}
public Publication(String name, String type, String pubdate)
{
	this.name = name;
	this.type = type;
	this.pubdate =pubdate;

}


public String getName()
{
	return name;
}

public void setName(String name)
{
	this.name = name;
}

public String getType()
{
	return type;
}

public void setType(String type)
{
	this.type = type;
}

public String getPubDate()
{
	return pubdate;
}

public void setPubDate(String pubdate)
{
	this.pubdate = pubdate;
}
@Override
public String toString()
{
return "Researcher " + name + "\n " + type;
}
}
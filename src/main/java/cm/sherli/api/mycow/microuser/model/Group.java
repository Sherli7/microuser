package cm.sherli.api.mycow.microuser.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "groups")
public class Group {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "name")
  private String name;

  @Column(name = "description")
  private String description;

  @ManyToMany(fetch = FetchType.LAZY,
      cascade = {
          CascadeType.PERSIST,
          CascadeType.MERGE
      })
  @JoinTable(name = "group_employees",
        joinColumns = { @JoinColumn(name = "group_id") },
        inverseJoinColumns = { @JoinColumn(name = "employee_id") })
  private Set<Employee> employees = new HashSet<>();
  
  public Group() {

  }
  
  public Group(String name, String description) {
    this.name = name;
    this.description = description;
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  public Set<Employee> getEmployee() {
    return employees;
  }

  public void setEmployee(Set<Employee> employees) {
    this.employees = employees;
  }
  
  public void addemployee(Employee tag) {
    this.employees.add(tag);
    tag.getGroups().add(this);
  }
  
  public void removeemployee(long employeeId) {
    Employee employees = this.employees.stream().filter(g -> g.getId() == employeeId).findFirst().orElse(null);
    if (employees != null) this.employees.remove(employees);
    employees.getGroups().remove(this);
  }

@Override
public String toString() {
	return "Group [id=" + id + ", name=" + name + ", description=" + description + ", employees=" + employees + "]";
}








}

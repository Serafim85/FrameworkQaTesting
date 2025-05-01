package x_client.helpersDb;

import base.BaseDbHelper;
import x_client.entity.Employee;
import x_client.entityDb.EmployeeEntity;
import org.instancio.Instancio;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeDbHelper extends BaseDbHelper {

    public EmployeeEntity getEmployeeFromDB(int id) {

        return findEntityById(EmployeeEntity.class, id);
    }

    public List<EmployeeEntity> getEmployeeFromDBByCompanyId(int companyId) {

        return getListOfEntityByParam("EmployeeEntity", EmployeeEntity.class,
                "companyId", companyId);
    }

    public EmployeeEntity createNewEmployee(int companyId) {
        EmployeeEntity employee = newEmployee(companyId);
        createNewEntity(employee);
        System.out.println("Создан сотрудник с id " + " " + employee.getId());

        return employee;
    }

    private EmployeeEntity newEmployee(int companyId) {
        Employee employee = Instancio.create(Employee.class);

        EmployeeEntity employeeEntity = new EmployeeEntity();

        employeeEntity.setCreateDateTime(employee.getCreateDateTime());
        employeeEntity.setLastChangedDateTime(employee.getLastChangedDateTime());
        employeeEntity.setFirstName(employee.getFirstName());
        employeeEntity.setLastName(employee.getLastName());
        employeeEntity.setMiddleName(employee.getMiddleName());

        employeeEntity.setCompanyId(companyId);
        employeeEntity.setEmail("charon85@mail.ru");
        employeeEntity.setAvatar_url(employee.getAvatar_url());

        employeeEntity.setPhone(employee.getPhone());
        employeeEntity.setBirthdate(employee.getBirthdate());
        employeeEntity.setIsActive(employee.getIsActive());

        return employeeEntity;
    }

    public void deleteNewEmployee(EmployeeEntity employee) {
        int id = employee.getId();

        assertNotNull(employee);
        removeEntity(employee);

        assertNull(getEmployeeFromDB(id));
        System.out.println("Удален сотрудник с id " + " " + employee.getId());
    }

    public void deleteNewEmployeeById(int id) {
        EmployeeEntity employee = findEntityById(EmployeeEntity.class, id);

        assertNotNull(employee);
        removeEntity(employee);

        assertNull(getEmployeeFromDB(id));
        System.out.println("Удален сотрудник с id " + " " + employee.getId());
    }
}

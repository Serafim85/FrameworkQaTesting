package x_client.helpersDb;

import base.BaseDbHelper;
import base.BaseProperties;
import x_client.entityDb.CompanyEntity;
import x_client.entityDb.EmployeeEntity;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CompanyDbHelper extends BaseDbHelper {

    private final BaseProperties propertiesHelper;
    String name;
    String description;

    public CompanyDbHelper() {
        this.propertiesHelper = getProperties();
        name = propertiesHelper.getName();
        description = propertiesHelper.getDescription();
    }

    public CompanyEntity getCompanyFromDB(int id) {
        return findEntityById(CompanyEntity.class, id);
    }

    public List<CompanyEntity> getCompanyFromDBByName() {
        return getListOfEntityByParam("CompanyEntity", CompanyEntity.class, "name", name);
    }

    public CompanyEntity createNewCompany() {
        CompanyEntity company = new CompanyEntity();
        company.setName(name);
        company.setDescription(description);
        company.setActive(true);
        createNewEntity(company);
        System.out.println("Создана компания с id " + " " + company.getId());

        return company;
    }

    public void deleteNewCompany(CompanyEntity company) {
        int id = company.getId();

        assertNotNull(company);
        List<EmployeeEntity> employees = getListOfEntityByParam("EmployeeEntity",
                EmployeeEntity.class, "companyId", id);
        for (EmployeeEntity employee : employees) {
            removeEntity(employee);
        }
        removeEntity(company);

        assertNull(getCompanyFromDB(id));
        System.out.println("Удалена компания с id " + " " + company.getId());
    }

    public void deleteAllCompanyByName() {
        List<CompanyEntity> companies = getCompanyFromDBByName();
        for (CompanyEntity company : companies) {
            deleteNewCompany(company);
        }
    }
}

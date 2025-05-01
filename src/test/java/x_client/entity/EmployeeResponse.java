package x_client.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeResponse {
    private int id;

    private String firstName;

    private String lastName;

    private String middleName;

    private String email;

    private String url;

    private String phone;

    private String birthdate;

    private boolean isActive;

    private int companyId;

    public EmployeeResponse() {
    }

    @JsonCreator
    public EmployeeResponse(@JsonProperty("id") int id,
                            @JsonProperty("isActive") boolean isActive,
                            @JsonProperty("firstName") String firstName,
                            @JsonProperty("companyId") int companyId,
                            @JsonProperty String lastName,
                            @JsonProperty String middleName,
                            @JsonProperty String email,
                            @JsonProperty String url,
                            @JsonProperty String phone,
                            @JsonProperty String birthdate) {
        this.id = id;
        this.isActive = isActive;
        this.firstName = firstName;
        this.companyId = companyId;
        this.lastName = lastName;
        this.middleName = middleName;
        this.email = email;
        this.url = url;
        this.phone = phone;
        this.birthdate = birthdate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    @Override
    public String toString() {
        return "EmployeeResponse{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", email='" + email + '\'' +
                ", url='" + url + '\'' +
                ", phone='" + phone + '\'' +
                ", birthdate='" + birthdate + '\'' +
                ", isActive=" + isActive +
                ", companyId=" + companyId +
                '}';
    }
}

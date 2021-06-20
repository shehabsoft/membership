package org.emu.membership.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;
import org.emu.membership.domain.enumeration.Gender;

/**
 * A DTO for the {@link org.emu.membership.domain.Member} entity.
 */
public class MemberDTO implements Serializable {

    private Long id;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String civilId;

    @NotNull
    private Instant birthDate;

    @NotNull
    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    private String email;

    @NotNull
    private String phone;

    @NotNull
    private String address1;

    private String address2;

    private String city;

    private String country;

    private Double salary;

    @NotNull
    private Gender gender;

    private MembershipStatusDTO membershipStatus;

    private MembershipCategoryDTO membershipCategory;

    private MembershipTypeDTO membershipType;

    private MembershipLevelDTO membershipLevel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getCivilId() {
        return civilId;
    }

    public void setCivilId(String civilId) {
        this.civilId = civilId;
    }

    public Instant getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Instant birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public MembershipStatusDTO getMembershipStatus() {
        return membershipStatus;
    }

    public void setMembershipStatus(MembershipStatusDTO membershipStatus) {
        this.membershipStatus = membershipStatus;
    }

    public MembershipCategoryDTO getMembershipCategory() {
        return membershipCategory;
    }

    public void setMembershipCategory(MembershipCategoryDTO membershipCategory) {
        this.membershipCategory = membershipCategory;
    }

    public MembershipTypeDTO getMembershipType() {
        return membershipType;
    }

    public void setMembershipType(MembershipTypeDTO membershipType) {
        this.membershipType = membershipType;
    }

    public MembershipLevelDTO getMembershipLevel() {
        return membershipLevel;
    }

    public void setMembershipLevel(MembershipLevelDTO membershipLevel) {
        this.membershipLevel = membershipLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MemberDTO)) {
            return false;
        }

        MemberDTO memberDTO = (MemberDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, memberDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MemberDTO{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", civilId='" + getCivilId() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            ", address1='" + getAddress1() + "'" +
            ", address2='" + getAddress2() + "'" +
            ", city='" + getCity() + "'" +
            ", country='" + getCountry() + "'" +
            ", salary=" + getSalary() +
            ", gender='" + getGender() + "'" +
            ", membershipStatus=" + getMembershipStatus() +
            ", membershipCategory=" + getMembershipCategory() +
            ", membershipType=" + getMembershipType() +
            ", membershipLevel=" + getMembershipLevel() +
            "}";
    }
}

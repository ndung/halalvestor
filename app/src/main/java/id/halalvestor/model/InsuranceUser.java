package id.halalvestor.model;

import java.io.Serializable;

public class InsuranceUser implements Serializable{

    private String name;
    private String phoneNumber;
    private String email;
    private String dateOfBirth;
    private String placeOfBirth;
    private String identityNumber;
    private String address;
    private String identityPicFilepath;
    private String bookPicFilepath;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIdentityPicFilepath() {
        return identityPicFilepath;
    }

    public void setIdentityPicFilepath(String identityPicFilepath) {
        this.identityPicFilepath = identityPicFilepath;
    }

    public String getBookPicFilepath() {
        return bookPicFilepath;
    }

    public void setBookPicFilepath(String bookPicFilepath) {
        this.bookPicFilepath = bookPicFilepath;
    }

    @Override
    public String toString() {
        return "InsuranceUser{" +
                "name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", placeOfBirth='" + placeOfBirth + '\'' +
                ", identityNumber='" + identityNumber + '\'' +
                ", address='" + address + '\'' +
                ", identityPicFilepath='" + identityPicFilepath + '\'' +
                ", bookPicFilepath='" + bookPicFilepath + '\'' +
                '}';
    }
}

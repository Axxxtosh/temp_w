package com.uriah.admin.worldvisioncable.Models;

/**
 * Created by Ashlesh on 08-12-2017.
 */

public class ComplaintsModel {

    String id;
    String name;
    String email;
    String phone;
    String nature_of_complaint;
    String description;
    String status;
    String created_on;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getNature_of_complaint() {
        return nature_of_complaint;
    }

    public void setNature_of_complaint(String nature_of_complaint) {
        this.nature_of_complaint = nature_of_complaint;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }
}

package com.example.waldo.models;

import java.io.Serializable;

public class PropertyData implements Serializable {

    public static class Forms implements Serializable{
        public String id="";
        public String form_name="";
        public String form_code="";
        public String file="";
        public String assigned_sites="";
        public String user_id="";
        public String position="";
        public String is_deleted="";
        public String created_at="";
        public boolean ischecked=false;
    }
    public static class Supervisor implements Serializable{
        public String id="";
        public String name="";
        public String email="";
        public String phone_no="";
        public String address="";
        public String password="";
        public String emergency_contact="";
        public String dob="";
        public String bank_account="";
        public String role_id="";
        public String profile_img="";
    }

    public static class Inspector implements Serializable{
        public String id="";
        public String name="";
        public String email="";
        public String business_no="";
        public String licence_exp="";
        public String licence_no="";
    }
}


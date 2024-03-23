package org.apache.camel.learn;

public class Person {
    private int codigo;
    private String identificacion;
    private String name;
    private int age;
    private String email;
    private int clientType;
    private double amount;

    public int getClientType() {
        return this.clientType;
    }

    public void setClientType(int clientType) {
        this.clientType = clientType;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    public Person() {
    }

    public Person(int codigo,String identificacion ,String name, int age, String email,int clientType,double amount) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.clientType = clientType;
        this.amount = amount;
        this.codigo = codigo;
        this.identificacion = identificacion;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public int getCodigo() {
        return this.codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getIdentificacion() {
        return this.identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }
}

package Confectionary;

public class Worker {

    private int id;
    private String name;
    private String surname;
    private int phone_number;
    private String occupation;
    private Addresses address;

    public Worker(){
    }
    public int getWorker_id() {
        return id;
    }

    public void setWorker_id(int worker_id) {
        this.id = worker_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(int phone_number) {
        this.phone_number = phone_number;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public Addresses getAddress() {
        return address;
    }

    public void setAddress(Addresses address) {
        this.address = address;
    }
}

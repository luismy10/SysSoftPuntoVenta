package model;

public class TicketTB {

    private int id;
    private String nombreTicket;
    private Object variable;

    public TicketTB() {
    }

    public TicketTB(String nombreTicket, Object variable) {
        this.nombreTicket = nombreTicket;
        this.variable = variable;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreTicket() {
        return nombreTicket;
    }

    public void setNombreTicket(String nombreTicket) {
        this.nombreTicket = nombreTicket;
    }

    public Object getVariable() {
        return variable;
    }

    public void setVariable(Object variable) {
        this.variable = variable;
    }

    @Override
    public String toString() {
        return nombreTicket;
    }

}

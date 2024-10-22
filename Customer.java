import java.time.LocalDate;




public class Customer {
    private String personalNumber;
    private String name;
    private LocalDate lastPaymentDate;

    public Customer(String personalNumber, String name, LocalDate lastPaymentDate) {
        this.personalNumber = personalNumber;
        this.name = name;
        this.lastPaymentDate = lastPaymentDate;
    }

    public String getPersonalNumber() {
        return personalNumber;
    }

    public void setPersonalNumber(String personalNumber) {
        this.personalNumber = personalNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getLastPaymentDate() {
        return lastPaymentDate;
    }

    public void setLastPaymentDate(LocalDate lastPaymentDate) {
        this.lastPaymentDate = lastPaymentDate;
    }

    @Override
    public String toString() {
        return name + " (" + personalNumber + ")";
    }
}

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;

class GymSystem {
    private static final String CUSTOMER_FILE = "src/customers.txt";
    private static final String LOG_FILE = "src/gym_log.txt"; // PT log file

    private List<Customer> customers = new ArrayList<>();

    public GymSystem() {
        loadCustomers();
    }
    private void loadCustomers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CUSTOMER_FILE))) {
            String line;

            // While loop to read file
            while ((line = reader.readLine()) != null) {
                // Split based on comma and trim the parts to remove extra spaces
                String[] parts = line.split(", ");
                if (parts.length == 2) {
                    String personalNumber = parts[0].trim(); // Ensure it's trimmed
                    String name = parts[1].trim(); // Trim name too
                    String paymentDateLine = reader.readLine(); // The next line contains the payment date

                    // Check if payment date line is valid
                    if (paymentDateLine != null) {
                        LocalDate lastPaymentDate = LocalDate.parse(paymentDateLine.trim()); // Ensure no extra spaces in date
                        Customer customer = new Customer(personalNumber, name, lastPaymentDate);

                        // Debug print to see exactly what was loaded
                        System.out.println("Loaded customer: [" + customer.getPersonalNumber() + "] " + customer.getName() + " (" + lastPaymentDate + ")");
                        customers.add(customer);
                    } else {
                        System.out.println("Warning: No payment date found for " + name);
                    }
                } else {
                    System.out.println("Warning: Invalid line format: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading customer data: " + e.getMessage());
        } catch (DateTimeParseException e) {
            System.out.println("Error parsing date: " + e.getMessage());
        }
    }



    public Customer findCustomer(String identifier) {
        identifier = identifier.trim();  // Trim the input to avoid leading/trailing spaces

        // Check if the identifier is numeric (personal number)
        if (identifier.matches("\\d+")) {
            // Search by personal number
            String finalIdentifier = identifier;
            return customers.stream()
                    .filter(c -> c.getPersonalNumber().equals(finalIdentifier))
                    .findFirst()
                    .orElse(null);
        } else {
            // Search by name (case-insensitive)
            String finalIdentifier1 = identifier;
            return customers.stream()
                    .filter(c -> c.getName().equalsIgnoreCase(finalIdentifier1))
                    .findFirst()
                    .orElse(null);
        }
    }



    // Determines the status of a customer
    public void checkCustomer(String identifier) {
        Optional<Customer> customer = Optional.ofNullable(findCustomer(identifier));
        if (customer.isPresent()) {
            Customer foundCustomer = customer.get();
            LocalDate lastPaymentDate = foundCustomer.getLastPaymentDate();
            LocalDate now = LocalDate.now();
            long daysBetween = ChronoUnit.DAYS.between(lastPaymentDate, now);

            if (daysBetween <= 365) {
                System.out.println(foundCustomer + " is a current member.");
                logVisit(foundCustomer);
            } else {
                System.out.println(foundCustomer + " is a former member.");
            }
        } else {
            System.out.println("No such customer. Unauthorized access.");
        }
    }

    // Logs the visit of a current member
    private void logVisit(Customer customer) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            writer.write(customer.getName() + ", " + customer.getPersonalNumber() + ", " + LocalDate.now());
            writer.newLine();
            System.out.println("Visit logged for " + customer.getName());
        } catch (IOException e) {
            System.out.println("Error logging visit: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        GymSystem gym = new GymSystem();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter personal number or name (or 'exit' to quit): ");
            String input = scanner.nextLine();
            if ("exit".equalsIgnoreCase(input)) {
                break;
            }
            gym.checkCustomer(input);
        }

        scanner.close();
    }
}

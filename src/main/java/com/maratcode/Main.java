package com.maratcode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@SpringBootApplication
//@EnableAutoConfiguration
//@Configuration
//@ComponentScan(basePackages = "com.maratcode")
@RestController
@RequestMapping("api/v1")
public class Main {

    private final CustomerRepository customerRepository;

    public Main(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        System.out.println("Hello");
    }
    @GetMapping("/greet")
    public GreetResponse greet() {
        return new GreetResponse(
                "Hello, I say!",
                List.of("Groovy","Java","JavaScript"),
                new Person("John", 23, 30000));
    }
    record Person(String name, int age, int savings) {}
    record GreetResponse(
            String greet,
            List<String> favProgrammingLanguages,
            Person person){}

    /*
    class GreetResponse {
        private final String greet;

        GreetResponse(String greet) {
            this.greet = greet;
        }

        public String getGreet() {
            return greet;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            GreetResponse that = (GreetResponse) o;

            return greet.equals(that.greet);
        }

        @Override
        public int hashCode() {
            return greet.hashCode();
        }

        @Override
        public String toString() {
            return "GreetResponse{" +
                    "greet='" + greet + '\'' +
                    '}';
        }
    }
*/

    @GetMapping("/customers")
    public List<Customer> getCustomers() {
        System.out.println("Customers list requested");
        return (List<Customer>) customerRepository.findAll();
    }

    record NewCustomerRequest(
            String name,
            String email,
            Integer age
    ){}

    @PostMapping("/customers")
    public void addCustomer(@RequestBody NewCustomerRequest newCustomerRequest) {
        System.out.println("Customer creation requested");
        Customer customer = new Customer();
        customer.setName(newCustomerRequest.name);
        customer.setEmail(newCustomerRequest.email);
        customer.setAge(newCustomerRequest.age);
        customerRepository.save(customer);
    }

    @DeleteMapping("/customers/{id}")
    public void deleteCustomer(@PathVariable("id") Long id) {
        System.out.println("Customer with id "+id+" deleted");
        customerRepository.deleteById(id);
    }

    record UpdateCustomerRequest(
            String name,
            String email,
            Integer age
    ){}

    @PatchMapping("/customers/{id}")
    public void patchCustomer(@PathVariable("id") Long id, @RequestBody UpdateCustomerRequest updateCustomerRequest) {
        System.out.println("Customer with id "+id+" update requested");
        Optional<Customer> customerById = customerRepository.findById(id);
        if (customerById.isPresent()) {
            customerById.get().setName(updateCustomerRequest.name);
            customerById.get().setEmail(updateCustomerRequest.email);
            customerById.get().setAge(updateCustomerRequest.age);
            customerRepository.save(customerById.get());
        }
        else System.out.println("Customer with id "+id+" not found");
    }

}

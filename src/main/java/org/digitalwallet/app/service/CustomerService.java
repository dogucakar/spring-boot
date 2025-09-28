package org.digitalwallet.app.service;

import lombok.AllArgsConstructor;
import org.digitalwallet.app.exception.CustomerNotFoundException;
import org.digitalwallet.app.repository.CustomerRepository;
import org.digitalwallet.app.repository.model.Customer;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public Customer findById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: ", customerId));
    }
}

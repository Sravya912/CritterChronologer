package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(this::getDTO)
                .collect(Collectors.toList());
    }

    public CustomerDTO getCustomerByPetId(Long petId) {
        Pet pet = petRepository.getOne(petId);
        Customer customer = pet.getCustomer();
        return getDTO(customer);
    }

    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setName(customerDTO.getName());
        customer.setNotes(customerDTO.getNotes());
        customer.setPhoneNumber(customerDTO.getPhoneNumber());

        List<Long> petIds = customerDTO.getPetIds();
        List<Pet> pets = petIds != null && !petIds.isEmpty() ?
                new java.util.ArrayList<>(petRepository.findAllById(petIds)) :
                new java.util.ArrayList<>();

        customer.setPets(pets);

        Customer savedCustomer = customerRepository.save(customer);
        return getDTO(savedCustomer);
    }

    private CustomerDTO getDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setName(customer.getName());
        customerDTO.setPhoneNumber(customer.getPhoneNumber());
        customerDTO.setNotes(customer.getNotes());

        List<Long> petIds = customer.getPets().stream()
                .map(Pet::getId)
                .collect(Collectors.toList());
        customerDTO.setPetIds(petIds);

        return customerDTO;
    }
}

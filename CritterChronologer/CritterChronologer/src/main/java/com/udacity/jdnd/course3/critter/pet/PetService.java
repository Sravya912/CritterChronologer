package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.user.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.udacity.jdnd.course3.critter.user.Customer;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PetService implements PetServiceInterface {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private PetDTO getDTO(Pet pet) {
        PetDTO petDTO = new PetDTO();
        petDTO.setType(pet.getType());
        petDTO.setName(pet.getName());
        petDTO.setOwnerId(pet.getCustomer().getId());
        petDTO.setBirthDate(pet.getBirthDate());
        petDTO.setNotes(pet.getNotes());
        petDTO.setId(pet.getId());
        return petDTO;
    }

    @Override
    public PetDTO storePet(PetDTO petDTO) {
        Long ownerId = petDTO.getOwnerId();
        Customer customer = customerRepository.findById(ownerId).orElse(null);

        if (customer == null) {
            throw new IllegalArgumentException("Customer with ID " + ownerId + " not found");
        }

        Pet pet = new Pet();
        pet.setType(petDTO.getType());
        pet.setBirthDate(petDTO.getBirthDate());
        pet.setName(petDTO.getName());
        pet.setNotes(petDTO.getNotes());
        pet.setCustomer(customer);
        customer.insertPet(pet);

        // Save the pet directly to ensure it's persisted
        Pet savedPet = petRepository.save(pet);
        return getDTO(savedPet);
    }

    @Override
    public PetDTO getPetById(Long petId) {
        Pet pet = petRepository.findById(petId).orElse(null);

        if (pet == null) {
            throw new IllegalArgumentException("Pet with ID " + petId + " not found");
        }
        return getDTO(pet);
    }

    @Override
    public List<PetDTO> getPetsByOwnerId(Long id) {
        List<Pet> pets = petRepository.getAllByCustomerId(id);
        return pets.stream()
                .map(this::getDTO)
                .collect(Collectors.toList());
    }

    public List<PetDTO> getAllPets() {
        return petRepository.findAll()
                .stream()
                .map(this::getDTO)
                .collect(Collectors.toList());
    }
}

package com.udacity.jdnd.course3.critter.pet;

import java.util.List;

public interface PetServiceInterface {
    PetDTO storePet(PetDTO petDTO);
    PetDTO getPetById(Long petId);
    List<PetDTO> getPetsByOwnerId(Long id);
}

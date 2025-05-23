package com.udacity.jdnd.course3.critter.pet;

import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PetRepository extends JpaRepository<Pet,Long> {

    List<Pet> getAllByCustomerId(Long customerId);
}

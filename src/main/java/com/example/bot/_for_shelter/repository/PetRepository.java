package com.example.bot._for_shelter.repository;

import com.example.bot._for_shelter.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    List<Pet> findAllByHaveOwner(boolean b);


}

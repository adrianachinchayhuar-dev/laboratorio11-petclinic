package com.tecsup.petclinic.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.tecsup.petclinic.dtos.OwnerDTO;
import com.tecsup.petclinic.entities.Owner;
import com.tecsup.petclinic.exceptions.OwnerNotFoundException;
import com.tecsup.petclinic.mappers.OwnerMapper;
import com.tecsup.petclinic.repositories.OwnerRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OwnerServiceImpl implements OwnerService {

    OwnerRepository ownerRepository;
    OwnerMapper ownerMapper;

    public OwnerServiceImpl(OwnerRepository ownerRepository, OwnerMapper ownerMapper) {
        this.ownerRepository = ownerRepository;
        this.ownerMapper = ownerMapper;
    }

    /**
     *
     * @param ownerDTO
     * @return
     */
    @Override
    public OwnerDTO create(OwnerDTO ownerDTO) {

        Owner newOwner = ownerRepository.save(ownerMapper.mapToEntity(ownerDTO));

        return ownerMapper.mapToDto(newOwner);
    }

    /**
     *
     * @param ownerDTO
     * @return
     */
    @Override
    public OwnerDTO update(OwnerDTO ownerDTO) {

        Owner newOwner = ownerRepository.save(ownerMapper.mapToEntity(ownerDTO));

        return ownerMapper.mapToDto(newOwner);
    }

    /**
     *
     * @param id
     * @throws OwnerNotFoundException
     */
    @Override
    public void delete(Integer id) throws OwnerNotFoundException {

        OwnerDTO owner = findById(id);

        ownerRepository.delete(this.ownerMapper.mapToEntity(owner));
    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    public OwnerDTO findById(Integer id) throws OwnerNotFoundException {

        Optional<Owner> owner = ownerRepository.findById(id);

        if (!owner.isPresent())
            throw new OwnerNotFoundException("Record not found...!");

        return this.ownerMapper.mapToDto(owner.get());
    }

    /**
     *
     * @param firstName
     * @return
     */
    @Override
    public List<OwnerDTO> findByFirstName(String firstName) {

        List<Owner> owners = ownerRepository.findByFirstName(firstName);

        owners.forEach(owner -> log.info("" + owner));

        return owners
                .stream()
                .map(this.ownerMapper::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     *
     * @param lastName
     * @return
     */
    @Override
    public List<OwnerDTO> findByLastName(String lastName) {

        List<Owner> owners = ownerRepository.findByLastName(lastName);

        owners.forEach(owner -> log.info("" + owner));

        return owners
                .stream()
                .map(this.ownerMapper::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     *
     * @param city
     * @return
     */
    @Override
    public List<Owner> findByCity(String city) {

        List<Owner> owners = ownerRepository.findByCity(city);

        owners.forEach(owner -> log.info("" + owner));

        return owners;
    }

    /**
     *
     * @return
     */
    @Override
    public List<Owner> findAll() {

        return ownerRepository.findAll();
    }
}
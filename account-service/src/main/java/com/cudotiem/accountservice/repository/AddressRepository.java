package com.cudotiem.accountservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cudotiem.accountservice.entity.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer>{

}

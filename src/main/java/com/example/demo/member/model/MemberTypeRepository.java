package com.example.demo.member.model;


import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberTypeRepository extends JpaRepository<MemberType, Long> {

	MemberType findByTypeName(String typeName);
	

}

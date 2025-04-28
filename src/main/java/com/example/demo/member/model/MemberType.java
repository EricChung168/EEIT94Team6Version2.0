package com.example.demo.member.model;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "MemberType")
public class MemberType {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer memberTypeId;

    @Column(name = "typeName", nullable = false, length = 50)
    private String typeName;

    @Column(name = "applicationFee", nullable = false, precision = 10, scale = 2)
    private BigDecimal applicationFee;

    @OneToMany(mappedBy = "memberType", fetch = FetchType.LAZY)
    private List<Member> members;
}

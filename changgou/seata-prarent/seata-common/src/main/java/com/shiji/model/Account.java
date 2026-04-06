package com.shiji.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Table(name = "account_tbl")
public class Account implements Serializable{

    @Id
    @Column(name = "id")
    public Long id;

    @Column(name = "money")
    private Integer money;

    @Column(name = "user_id")
    private String useId;
}

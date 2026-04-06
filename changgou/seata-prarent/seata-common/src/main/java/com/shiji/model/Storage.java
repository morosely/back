package com.shiji.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Table(name = "storage_tbl")
public class Storage implements Serializable {

    @Id
    @Column(name = "id")
    public Long id;

    @Column(name = "commodity_code")
    public String commodityCode;

    @Column(name = "count")
    public Integer count;
}

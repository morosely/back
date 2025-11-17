package com.efuture.omdmain.model;

import lombok.Data;

import java.util.Date;

@Data
public class AeonMoreBarNoModel {

    private Long mbid;

    private String goodsCode;

    private String barNo;

    private String groupCode;
    
    private Date createDate;
    
    private Date updateDate;

    private Short mainBarcodeFlag;
    
    private Short barNoStatus;

}
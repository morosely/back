package com.efuture.omdmain.service;

public interface BeanConstant
{
    interface Status
    {
        final String INVALID = "0";
        final String NORMAL = "1";
        final String PUBLISH = "2";
    }
    
    interface QueryField
    {
        final String PARAMKEY_FIELDS = "fields";
        final String PARAMKEY_ORDERFLD = "order_field";
        final String PARAMKEY_ORDERDIR = "order_direction";
        final String PARAMKEY_PAGENO = "page_no";
        final String PARAMKEY_PAGESIZE = "page_size";
    }
}

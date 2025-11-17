package com.efuture.omdmain.model;

import lombok.Data;

import java.util.List;

@Data
public class PackageAttCateTree extends PackageAttCate{
    private List<PackageAttCateTree> children;
}

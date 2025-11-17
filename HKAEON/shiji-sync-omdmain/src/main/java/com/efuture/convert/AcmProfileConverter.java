package com.efuture.convert;

import com.efuture.model.ExtAmcProfileModel;
import com.efuture.model.AmcProfileModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @Author: Liyj
 * @Description:
 * @Date: 2022/3/30 10:37
 */
@Mapper(componentModel = "AcmProfileConverter")
public interface AcmProfileConverter {

    AcmProfileConverter INSTANCE = Mappers.getMapper(AcmProfileConverter.class);

    @Mappings({
            @Mapping(source = "erpCode", target = "filler1"),
            @Mapping(source = "corporationCode", target = "filler3"),
            @Mapping(source = "memberId", target = "memberId"),
            @Mapping(source = "memberCard", target = "filler2"),
            @Mapping(target = "bonusPoint", expression = "java(extAmcProfileModel.getMemberSystem() == null ? null : new java.math.BigDecimal(extAmcProfileModel.getMemberSystem()))"),
            @Mapping(target = "bonusPointLastMonth", expression = "java(extAmcProfileModel.getMemberLevel() == null ? null : new java.math.BigDecimal(extAmcProfileModel.getMemberLevel()))"),
            @Mapping(source = "memberLevelName", target = "filler4"),
            @Mapping(source = "memberName", target = "filler8"),
            @Mapping(source = "memberNameEn", target = "filler5"),
            @Mapping(target = "bonusPointUsed", expression = "java(extAmcProfileModel.getMemberGender() == null ? null : new java.math.BigDecimal(extAmcProfileModel.getMemberGender()))"),
            //@Mapping(target = "filler6", expression = "java(com.product.util.DateTimeFormatUtil.toDateTime(extAmcProfileModel.getMemberBirthday()))"),
            @Mapping(target = "filler6", expression = "java(com.efuture.utils.BeanUtils.formatTime(extAmcProfileModel.getMemberBirthday()))"),
            @Mapping(source = "dealType", target = "estamp"),
            @Mapping(source = "effectiveTime", target = "filler7"),
            @Mapping(source = "expireTime", target = "membershipExpireDate", dateFormat = "yyyy-MM-dd HH:mm:ss"),
            @Mapping(target = "updateDate", expression = "java(new java.util.Date())"),
            @Mapping(source = "memberLevel", target = "memberLevel"),
            @Mapping(source = "status", target = "status")
    })
    AmcProfileModel extToFormal(ExtAmcProfileModel extAmcProfileModel);

    List<AmcProfileModel> extsToFormals(List<ExtAmcProfileModel> list);

}

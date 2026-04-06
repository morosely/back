package com.jd.service;

import com.jd.common.pojo.JDReturnResult;
import com.jd.pojo.TbItemParam;

public interface ItemParamService {

	JDReturnResult getItemParamByCid(long cid);

	JDReturnResult insertItemParam(TbItemParam itemParam);

}

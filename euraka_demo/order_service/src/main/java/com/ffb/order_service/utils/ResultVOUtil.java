package com.ffb.order_service.utils;



import com.ffb.order_service.VO.ResultVO;

import java.util.Map;

/**
 * Created by 廖师兄
 * 2017-12-10 18:03
 */
public class ResultVOUtil {

    public static ResultVO success(Object object) {
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg("成功");
        resultVO.setData(object);
        return resultVO;
    }
}

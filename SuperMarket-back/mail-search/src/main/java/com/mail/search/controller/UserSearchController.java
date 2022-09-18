package com.mail.search.controller;


import com.mail.common.util.R;
import com.mail.search.service.UserSearchService;
import com.mail.search.vo.request.SearchParamReqVO;
import com.mail.search.vo.response.SearchParamRespVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("search/user")
public class UserSearchController {

    @Resource
    private UserSearchService userSearchService;


    @GetMapping("sku/list")
    public R userSearchSkuInfoList(SearchParamReqVO searchParamReqVO){
        SearchParamRespVO searchParamRespVO = userSearchService.userSearchSkuInfoList(searchParamReqVO);
        return R.ok(searchParamRespVO);
    }

}

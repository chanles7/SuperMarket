package com.mail.search.service;

import com.mail.search.vo.request.SearchParamReqVO;
import com.mail.search.vo.response.SearchParamRespVO;

public interface UserSearchService {

    SearchParamRespVO userSearchSkuInfoList(SearchParamReqVO searchParamReqVO);
}

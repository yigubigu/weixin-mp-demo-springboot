package com.github.binarywang.demo.wechat.controller;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpUserList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/wechat/followers")
public class FollowerController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
	@Autowired
    private WxMpService wxService;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody() 
    public ResponseWrapper getUsers(){
		try {
			WxMpUserList wxMpUserList = this.wxService.getUserService().userList(null);
			return new ResponseWrapper(wxMpUserList);
		} catch (WxErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseWrapper();
	}
    
}

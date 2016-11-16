package com.github.binarywang.demo.wechat.controller;

import java.util.List;
import java.util.Map;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.result.WxMpUserList;
import me.chanjar.weixin.mp.bean.tag.WxUserTag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
			logger.error(e.getMessage());
		}
		return new ResponseWrapper();
	}
	
	@RequestMapping(value="batchAssignTag", method=RequestMethod.POST)
	@ResponseBody()
	public ResponseWrapper batchAssginTagToUsers(@RequestBody Map<String, Object> params) {
		String tag = (String)params.get("tags");
		List<String> userIds = (List<String>) params.get("userIds");
		try {
			WxUserTag  userTag = this.wxService.getUserTagService().tagCreate(tag);
			this.wxService.getUserTagService().batchTagging(userTag.getId(), userIds.toArray(new String[0]));
			
		} catch (WxErrorException e) {
			logger.error(e.getMessage());
		}
		return new ResponseWrapper();
	}
	
	@RequestMapping(value = "{follwerId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseWrapper getUserById(@PathVariable String follwerId ) {
		
		try {
			WxMpUser user = this.wxService.getUserService().userInfo(follwerId);
			return new ResponseWrapper(user);
		} catch (WxErrorException e) {
			logger.error(e.getMessage());
		}
		return new ResponseWrapper();
	}
	
	@RequestMapping(value = "{tags}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseWrapper getTags( ) {
		try {
			List<WxUserTag> userTag = this.wxService.getUserTagService().tagGet();
			return new ResponseWrapper(userTag);
		} catch (WxErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseWrapper();
	}
	
    
}

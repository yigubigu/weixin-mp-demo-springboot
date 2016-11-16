package com.github.binarywang.demo.wechat.controller;

import java.util.List;
import java.util.Map;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
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
@RequestMapping("/wechat/tags")
public class TagController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
    private WxMpService wxService;
	
	@RequestMapping( method = RequestMethod.GET)
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
	
	@RequestMapping( method = RequestMethod.POST)
    @ResponseBody
    public ResponseWrapper addTag(@RequestBody Map<String, Object> params ) {
		String tagName =  (String)params.get("tag");		
		try {
			WxUserTag tag = this.wxService.getUserTagService().tagCreate(tagName);
			return new ResponseWrapper(tag);
		} catch (WxErrorException e) {
			logger.error(e.getMessage());
		}
		return new ResponseWrapper();
	}
	
	

	@RequestMapping(value="assignToUsers", method=RequestMethod.POST)
	@ResponseBody()
	public ResponseWrapper batchAssginTagToUsers(@RequestBody Map<String, Object> params) {
		String tag = (String)params.get("tags");
		List<String> userIds = (List<String>) params.get("userIds");
		try {
			
			List<WxUserTag>  userTags = this.wxService.getUserTagService().tagGet();
			WxUserTag userTag = null;
			
			for(int i=0; i < userTags.size(); i++) {
				WxUserTag tmp = userTags.get(i);
				if (tmp.getName().equalsIgnoreCase(tag)) {
					userTag = tmp;
					break;
				}
			}
			
			if (userTag == null) {
				userTag = this.wxService.getUserTagService().tagCreate(tag);
			}			
			
			printArray( userIds.toArray(new String[0]));
			
			this.wxService.getUserTagService().batchTagging(userTag.getId(), userIds.toArray(new String[0]));
			
		} catch (WxErrorException e) {
			logger.error(e.getMessage());
		}
		return new ResponseWrapper();
	}

	private void printArray(String[] userIds) {
		
		for(int i=0; i< userIds.length; i++) {
			logger.debug(" userid " + userIds[i]);
		}
	}
	
	@RequestMapping(value="{tagId}/assignToUsers", method=RequestMethod.POST)
	@ResponseBody()
	public ResponseWrapper assginToUsers(@PathVariable long tagId, @RequestBody Map<String, Object> params) {
		
		List<String> userIds = (List<String>) params.get("userIds");
		try {										
			printArray( userIds.toArray(new String[0]));			
			this.wxService.getUserTagService().batchTagging(tagId, userIds.toArray(new String[0]));
			
		} catch (WxErrorException e) {
			logger.error(e.getMessage());
		}
		return new ResponseWrapper();
	}
}

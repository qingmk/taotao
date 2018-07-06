package com.taotao.manage.controller.api;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.bean.ItemCatResult;
import com.taotao.manage.service.impl.ItemCatService;

@Controller
@RequestMapping("api/item/cat")
public class ApiItemCatController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ApiItemCatController.class);
	@Autowired
	private ItemCatService itemCatService;
	private static final ObjectMapper MAPPER=new ObjectMapper();
	/**
	 * 对外提供接口，查询类目数据
	 * @return
	 */
	/*@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<ItemCatResult> queryItemCat(@RequestParam(value="callback",required=false) String callback) {
		try {
			if(LOGGER.isDebugEnabled()) {
				LOGGER.debug("开始查询类目数据");
			}
			ItemCatResult result = this.itemCatService.queryAllToTree();
			if(LOGGER.isDebugEnabled()) {
				LOGGER.debug("查询类目数据成功,数据="+result.getItemCats().toString());
			}
			return ResponseEntity.ok().body(result);
		} catch (Exception e) {
			
			LOGGER.error("查询类目数据错误", e);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
*/
	/**
	 * 对外提供接口，查询类目数据
	 * @param callback
	 * @return
	 */
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<String> queryItemCat(@RequestParam(value="callback") String callback) {
		try {
			if(LOGGER.isDebugEnabled()) {
				LOGGER.debug("开始查询类目数据");
			}
			ItemCatResult result = this.itemCatService.queryAllToTree();
			String json = MAPPER.writeValueAsString(result);
			if(LOGGER.isDebugEnabled()) {
				LOGGER.debug("查询类目数据成功,数据="+json);
			}
			if(StringUtils.isEmpty(callback)) {
				return  ResponseEntity.ok().body(json);
			}
			return ResponseEntity.ok().body(callback+"("+json+");");
		} catch (Exception e) {
			
			LOGGER.error("查询类目数据错误", e);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
}

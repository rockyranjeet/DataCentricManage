package com.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.model.Item;
import com.app.model.Item;
import com.app.service.IItemService;

@Controller
public class ItemController 
{
	@Autowired
	private IItemService service;
	//1.show Item registration page
	@RequestMapping("/regItem")
	public String showReg()
	{
		return "ItemRegister";	
	}

	//2.save Item data
	@RequestMapping(value="/saveItem",method=RequestMethod.POST)
	public String saveItem(@ModelAttribute("item")Item item,ModelMap map)
	{    
		int itemId=service.saveItem(item);
		String info="Save with : "+itemId;
		map.addAttribute("msg",info);
		return "ItemRegister";
	}
	//3.get Item Data
	@RequestMapping("/getAllItems")
	public String getData(ModelMap map)
	{
		List<Item> item=service.getAllItems();
		map.addAttribute("items",item);
		return "ItemData";
	}
	//4. delete Item Data
	@RequestMapping("/deleteItem")
	public String delItem(@RequestParam("itemId")int ItemId)
	{
		service.deleteItem(ItemId);
		return "redirect:getAllItems";
	}
	//5. edit Item data 
	@RequestMapping("/editItem")
	public String showEdit(@RequestParam("itemId")int ItemId,ModelMap map)
	{
		Item item=service.getItemById(ItemId);
		map.addAttribute("item",item);
		return "ItemDataEdit";
	}
	//6. update Item Data
	@RequestMapping(value="/updateItem",method=RequestMethod.POST)
	public String updateItem(@ModelAttribute("item")Item Item)
	{      
		service.updateItem(Item);
		return "redirect:getAllItems";
	}
	// 7. Item Excel Export
	   @RequestMapping("/itemExcel")
	   public String doExcelExport(ModelMap map)
	   {
		   
		   List<Item> itemList=service.getAllItems();
		   map.addAttribute("itemList",itemList);
		   return "ItemExcelView";
		   
	   }
	   
	   // 8. Item Pdf Export
	   @RequestMapping("/itemPdf")
	   public String doPdfExport(ModelMap map)
	   {
		   List<Item> itemList=service.getAllItems();
		   map.addAttribute("itemList",itemList);
		   return "ItemPdfView";
	   }


}

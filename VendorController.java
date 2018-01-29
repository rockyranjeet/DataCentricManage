package com.app.controller;

import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.app.model.Vendor;
import com.app.service.IVendorService;
import com.app.util.CommonUtil;
import com.app.util.VendorUtil;
@Controller
public class VendorController
{    
	@Autowired
	private IVendorService service;

	@Autowired
	private ServletContext sc;

	@Autowired
	private VendorUtil venUtil;

	@Autowired
	private CommonUtil commonUtil;

	//1.show Vendor registration page
	@RequestMapping("/regVen")
	public String showReg()
	{
		return "VendorRegister";	
	}

	//2.save Vendor data
	@RequestMapping(value="/saveVen",method=RequestMethod.POST)
	public String saveLoc(@ModelAttribute("vendor")Vendor v,ModelMap map,@RequestParam("fobj")CommonsMultipartFile file)
	{
		int venId=service.saveVendor(v);
		
		boolean flag=commonUtil.sendEmail(v.getVenEmail(),"Hello Vendor", "Your id is : "+venId, file);
		String info="Save with : "+venId;
		map.addAttribute("msg",info);
		return "VendorRegister";
	}
	//3.get Vendor Data
	@RequestMapping("/getAllVens")
	public String getData(ModelMap map)
	{
		List<Vendor> ven=service.getAllVendors();
		map.addAttribute("vens",ven);
		return "VendorData";
	}
	//4. delete Vendor Data
	@RequestMapping("/deleteVen")
	public String delVen(@RequestParam("venId")int venId)
	{
		service.deleteVendor(venId);
		return "redirect:getAllVens";
	}
	//5. edit Vendor data 
	@RequestMapping("/editVen")
	public String showEdit(@RequestParam("venId")int venId,ModelMap map)
	{
		Vendor ven=service.getVendorById(venId);
		map.addAttribute("ven",ven);
		return "VendorDataEdit";
	}
	//6. update Vendor Data
	@RequestMapping(value="/updateVen",method=RequestMethod.POST)
	public String updateVendor(@ModelAttribute("vendor")Vendor ven)
	{
		service.updateVendor(ven);
		return "redirect:getAllVens";
	}
	// 7. Vendor Excel Export
	@RequestMapping("/venExcel")
	public String doExcelExport(ModelMap map)
	{

		List<Vendor> venList=service.getAllVendors();
		map.addAttribute("venList",venList);
		return "VenExcelView";

	}

	// 8. Vendor Pdf Export
	@RequestMapping("/venPdf")
	public String doPdfExport(ModelMap map)
	{
		List<Vendor> venList=service.getAllVendors();
		map.addAttribute("venList",venList);
		return "VenPdfView";
	}
	// 9. Chart creation (Pie/Bar)
	@RequestMapping("/venReport")
	public String generateCharts()
	{
		//path to save image
		String path=sc.getRealPath("/");

		//data to create chart
		List<Object[]> data=service.getVenTypeAndCount();


		//call chart method
		venUtil.generatePieChart(path,data);
		venUtil.generateBarChart(path,data);

		//ui page to show charts
		return "VendorReports";   

	}
}

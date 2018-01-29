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

import com.app.model.Location;
import com.app.service.ILocationService;
import com.app.util.LocationUtil;

@Controller
public class LocationController {

	@Autowired
    private ILocationService service;
	
	@Autowired
	private ServletContext sc;
	
	@Autowired
	private LocationUtil locUtil;


   //1.show location registration page
   @RequestMapping("/regLoc")
   public String showReg()
   {
   return "LocationRegister";	
   }
   
   //2.save location data
   @RequestMapping(value="/saveLoc",method=RequestMethod.POST)
   public String saveLoc(@ModelAttribute("location")Location loc,ModelMap map)
   {
	  int locId=service.saveLocation(loc);
	  String info="Save with : "+locId;
	  map.addAttribute("msg",info);
	  return "LocationRegister";
   }
   //3.get Location Data
   @RequestMapping("/getAllLocs")
   public String getData(ModelMap map)
   {
	   List<Location> loc=service.getAllLocations();
	   map.addAttribute("locs",loc);
	   return "LocationData";
   }
   //4. delete Location Data
   @RequestMapping("/deleteLoc")
   public String delLoc(@RequestParam("locId")int locId)
   {
	   service.deleteLocation(locId);
	   return "redirect:getAllLocs";
   }
   //5. edit Location data 
   @RequestMapping("/editLoc")
   public String editLoc(@RequestParam("locId")int locId,ModelMap map)
   {
	   Location loc=service.getLocationById(locId);
	   map.addAttribute("loc",loc);
	   return "LocationDataEdit";
   }
   //6. update Location Data
   @RequestMapping(value="/updateLoc",method=RequestMethod.POST)
   public String updateLocation(@ModelAttribute("location")Location loc)
   {
	   service.updateLocation(loc);
	   return "redirect:getAllLocs";
   }
   // 7. Location Excel Export
   @RequestMapping("/locExcel")
   public String doExcelExport(ModelMap map)
   {
	   
	   List<Location> locList=service.getAllLocations();
	   map.addAttribute("locList",locList);
	   return "LocExcelView";
	   
   }
   
   // 8. Location Pdf Export
   @RequestMapping("/locPdf")
   public String doPdfExport(ModelMap map)
   {
	   List<Location> locList=service.getAllLocations();
	   map.addAttribute("locList",locList);
	   return "LocPdfView";
   }
   // 9. Chart creation (Pie/Bar)
   @RequestMapping("/locReport")
   public String generateCharts()
   {
	   //path to save image
	   String path=sc.getRealPath("/");
	   
	   //data to create chart
	   List<Object[]> data=service.getLocTypeAndCount();
	   
	   
	   //call chart method
	   locUtil.generatePieChart(path,data);
	   locUtil.generateBarChart(path,data);
	   
	   //ui page to show charts
	   return "LocationReports";
	  
   }
	   
  }

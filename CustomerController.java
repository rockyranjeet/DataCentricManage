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

import com.app.model.Customer;
import com.app.service.ICustomerService;
import com.app.util.CoDecUtil;
import com.app.util.CodeUtil;
import com.app.util.CommonUtil;
import com.app.util.CustomerUtil;
@Controller
public class CustomerController  {
	@Autowired
	private ICustomerService service;

	@Autowired
	private ServletContext sc;

	@Autowired
	private CustomerUtil custUtil;

	@Autowired
	private CodeUtil codeUtil;

	@Autowired
	private CoDecUtil codecUtil;

	@Autowired
	private CommonUtil commonUtil;

	//1.show Customer registration page
	@RequestMapping("/regCust")
	public String showReg()
	{
		return "CustomerRegister";	
	}

	//2.save Customer data
	@RequestMapping(value="/saveCust",method=RequestMethod.POST)
	public String saveCust(@ModelAttribute("customer")Customer cust,ModelMap map)
	{    
		//generate pwd,token
		String pwd=codeUtil.getPwd();
		String token=codeUtil.getToken();
		//do encode pwd,token
		String encPwd=codecUtil.doEncode(pwd);
		String encToken=codecUtil.doEncode(token);
		//set encoded value to cust Obj
		cust.setPassword(encPwd);
		cust.setAccToken(encToken);

		//save this cust obj
		service.saveCustomer(cust);

		//send email
		String text="Id is :"+cust.getCustId()+",User Name is :"+cust.getCustEmail()+",Password is :"+pwd +",Token is :"+token;
		boolean flag=commonUtil.sendEmail(cust.getCustEmail(),"Register as customer",text,null);
		String message=cust.getCustId()+"Customer registered";
		if(flag) message += "Email also sent";
		//int custId=service.saveCustomer(cust);
		//String info="Save with : "+custId;
		map.addAttribute("msg",message);
		return "CustomerRegister";
	}

	//3.get Customer Data
	@RequestMapping("/getAllCusts")
	public String getData(ModelMap map)
	{
		List<Customer> cust=service.getAllCustomers();
		map.addAttribute("custs",cust);
		return "CustomerData";
	}
	//4. delete Customer Data
	@RequestMapping("/deleteCust")
	public String delCust(@RequestParam("custId")int CustId)
	{
		service.deleteCustomer(CustId);
		return "redirect:getAllCusts";
	}
	//5. edit Customer data 
	@RequestMapping("/editCust")
	public String showEdit(@RequestParam("custId")int custId,ModelMap map)
	{
		Customer cust=service.getCustomerById(custId);
		map.addAttribute("cust",cust);
		return "CustomerDataEdit";
	}
	//6. update Customer Data
	@RequestMapping(value="/updateCust",method=RequestMethod.POST)
	public String updateCustomer(@ModelAttribute("customer")Customer cust)
	{      
		service.updateCustomer(cust);
		return "redirect:getAllCusts";
	}
	// 7. Vendor Excel Export
	@RequestMapping("/custExcel")
	public String doExcelExport(ModelMap map)
	{

		List<Customer> custList=service.getAllCustomers();
		map.addAttribute("custList",custList);
		return "CustExcelView";

	}

	// 8. Vendor Pdf Export
	@RequestMapping("/custPdf")
	public String doPdfExport(ModelMap map)
	{
		List<Customer> custList=service.getAllCustomers();
		map.addAttribute("custList",custList);
		return "CustPdfView";
	}
	// 9. Chart creation (Pie/Bar)
	@RequestMapping("/custReport")
	public String generateCharts()
	{
		//path to save image
		String path=sc.getRealPath("/");

		//data to create chart
		List<Object[]> data=service.getCustTypeAndCount();


		//call chart method
		custUtil.generatePieChart(path,data);
		custUtil.generateBarChart(path,data);

		//ui page to show charts
		return "CustomerReports";   

	}
	//10 . reset pwd 
	@RequestMapping(value="/resetCust"/*method=RequestMethod.POST*/)
	public String pwdreset(@RequestParam("email")String mail,ModelMap map)
	{
		Customer cust=service.getCustomerByMail(mail);
		String s1=cust.getPassword();
		//String s2=cust.getAccToken();
		//send email
				String text="Email is :"+cust.getCustId()+",User Name is :"+cust.getCustEmail()+",Password is :"+s1;
				boolean flag=commonUtil.sendEmail(cust.getCustEmail(),"Reset Password",text,null);
				String message=cust.getCustEmail()+"Password Reset";
				if(flag) message += "Password Sent to Email";
		        return "CustPwdReset";
	}

}

package com.app.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.app.model.Document;
import com.app.service.IDocumentService;

@Controller
public class DocumentController {

	@Autowired
	private IDocumentService service;
	
	//1.show upload/Download page with data
	@RequestMapping("/regDocs")
	public String showPage(ModelMap map)
	{
		System.out.println("done");
		List<Object[]> list=service.getFileIdAndNames();
		map.addAttribute("list",list);
         return "Document";	
	}
	//2.upload Document to DB
	@RequestMapping(value="/uploadDoc",method=RequestMethod.POST)
	public String uploadDoc(@RequestParam("fileId")int fid,@RequestParam("fileOb") CommonsMultipartFile cmf)
	{
		if(cmf!=null)
		{
			Document doc=new Document();
			doc.setFileId(fid);
			doc.setFileName(cmf.getOriginalFilename());
			doc.setFileData(cmf.getBytes());
			service.saveDocument(doc);
		}
		return "redirect:regDocs";
	}
	//3.download Document based on docId
	@RequestMapping("/downloadDoc")
	public void downloadDocument(@RequestParam("docId")int fileId, HttpServletResponse res)
	{
		Document doc=service.getDocumentById(fileId);
		String fname=doc.getFileName();
		byte[] fdata=doc.getFileData();
		res.addHeader("Content-Disposition", "attachment;filename="+fname);
		try{
			FileCopyUtils.copy(fdata, res.getOutputStream());
		}catch(IOException e)
		{
this is doc
			e.printStackTrace();
		}
	}
}

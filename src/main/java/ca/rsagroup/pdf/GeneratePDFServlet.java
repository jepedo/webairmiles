package ca.rsagroup.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestHandler;

import org.xhtmlrenderer.pdf.ITextRenderer;

import ca.rsagroup.commons.ConfigurationManager;
import ca.rsagroup.web.util.DatabaseDrivenMessageSource;

@Component("myServlet")
public class GeneratePDFServlet implements HttpRequestHandler {
	
	@Inject
	private ConfigurationManager configurationManager;
	
	protected DatabaseDrivenMessageSource configurationSource;
	
	@Override
	public void handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		 ServletOutputStream os = response.getOutputStream();
		  //set the response content type to PDF
		 String reqPath = request.getRequestURI();
//		 String reqPath2 = request.getRequestURL().toString();
		 String rurl = request.getRequestURL().toString();			
		 rurl = rurl.substring(rurl.indexOf(request.getContextPath()));
		 String reqPath2 = "http://localhost:8080"+rurl;
			
		 String lang = request.getParameter("lang");
		 String domainId = request.getParameter("domain");
		 Locale myLocale = new Locale(lang,"CA",domainId);
		 String reqId = reqPath.substring(reqPath.lastIndexOf("/")+1);        
		 URL pdfUrl = new URL(reqPath2.substring(0,reqPath2.lastIndexOf("pdf"))+"app/pdf?lang="+lang+"&id="+reqId+"&domain="+domainId);
		 
		 ClientHttpRequestFactory factory  = new SimpleClientHttpRequestFactory();
		 ClientHttpRequest hr;
		 String pdfContent= "abc";
		try {
			hr = factory.createRequest(pdfUrl.toURI(), HttpMethod.GET);
//			 InputStream io = hr.execute().getBody();
//			 pdfContent = convertStreamToString( hr.execute().getBody());
			  String HTML_TO_PDF =File.separator+System.currentTimeMillis()+"-ConvertedFile.html";//configurationManager.getTempDir()+
			  OutputStream out = new java.io.FileOutputStream(HTML_TO_PDF);  
			  IOUtils.copy(hr.execute().getBody(),out);
//			  response.setContentType("application/pdf");
			  response.setContentType("application/force-download");  
			  response.setHeader("Content-Disposition", "attachment; filename=" + configurationSource.getText("pdf.defaultFilename.label", myLocale));  			  
		        ITextRenderer renderer = new ITextRenderer();
		        renderer.setDocument(pdfUrl.toString());
//		        renderer.setDocument(new File(HTML_TO_PDF).toURI().toURL().toString());      
		        renderer.layout();
		        try {
					renderer.createPDF(os);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 			  
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		 
	 }

	public static String convertStreamToString(java.io.InputStream is) {
	    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
		 

}

package com.levent.fop.test.test1;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import com.levent.fop.template.SurrenderTemp;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.plutext.jaxb.xslfo.Root;

public class CreatePdf {

	
	public static void main(String[] args) throws Exception {
		FileInputStream ins = null;
		FileOutputStream out = null;
		File file = null;
		try{
			SurrenderTemp SurrenderTemp = new SurrenderTemp();
			file =createTempPdf(SurrenderTemp.createRoot()); 
			
			 ins = new FileInputStream(file);
			 out = new FileOutputStream("D:/����pdf/SurrenderTemp.pdf");
			byte[] b = new byte[1024];
			int n=0;
			while((n=ins.read(b))!=-1){
				out.write(b, 0, n);
			}
			System.out.println("�ɹ�");			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(ins !=null)
				ins.close();
			if(out !=null)
				out.close();
			if(file !=null && file.exists())	
				file.delete();
		}
        
	}
	

	
	 public static File createTempPdf(Root root) throws Exception{
			StringWriter sw=null;
			File file = File.createTempFile("tempPdf",".pdf");
			sw=new StringWriter();
			JAXBContext content=JAXBContext.newInstance(Root.class);
			Marshaller mar=content.createMarshaller();
			mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			mar.setProperty(Marshaller.JAXB_ENCODING, "GBK");
			mar.marshal(root, sw);
			String str=sw.toString();
			str=str.replace("\\", "/");
			str=str.replace("&amp;#8203;", "&#8203;");//jaxb������Marshaller ��������ַ�������&�������ַ��滻Ϊ&amp;��������Ҫʹ�ò�ռλ�ָ���&#8203;������ֵ�Ԫ�����������Ҫ�滻
			str=str.replace("&amp;#160;", "&#160;");//jaxb������Marshaller ��������ַ�������&�������ַ��滻Ϊ&amp;��������Ҫʹ�ÿո��ַ�&#160;������Ҫ�滻
			File xmlFile=	File.createTempFile("tempXml", ".xml");
			Writer writer=new FileWriter(xmlFile);
		    writer.write(str);
		    writer.flush();
		    writer.close();
		    
		    FopFactory factory = FopFactory.newInstance(new File("src/test1/conf/fop.xml"));
			OutputStream out = null;
			out = new FileOutputStream(file);
			out = new BufferedOutputStream(out);
			Fop fop = factory.newFop(MimeConstants.MIME_PDF, out);
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			Source source = new StreamSource(xmlFile);
			Result res = new SAXResult(fop.getDefaultHandler());
			transformer.transform(source, res);
			out.close();
			if(xmlFile.exists()) 
				xmlFile.delete();
		 return  file;
	 }
	
}

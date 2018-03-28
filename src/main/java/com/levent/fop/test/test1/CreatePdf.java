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
			SurrenderTemp temp = new SurrenderTemp();
			file = createTempPdf(temp.createRoot());

			ins = new FileInputStream(file);
			out = new FileOutputStream("src//main//resources//output//SurrenderTemp.pdf");
			byte[] b = new byte[1024];
			int n;
			while((n=ins.read(b))!=-1){
				out.write(b, 0, n);
			}
			System.out.println("SUCCESS");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(ins !=null)
				ins.close();
			if(out !=null)
				out.close();
			if(file !=null && file.exists()){
				boolean del = file.delete();
				String delResult = del?"Delete Success":"Delete Failure";
				System.out.println(delResult);
			}
		}
	}

	private static File createTempPdf(Root root) throws Exception{
		StringWriter sw = new StringWriter();
		File file = File.createTempFile("tempPdf",".pdf");

		JAXBContext content=JAXBContext.newInstance(Root.class);
		Marshaller mar=content.createMarshaller();
		mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		mar.setProperty(Marshaller.JAXB_ENCODING, "utf-8");
		mar.marshal(root, sw);
		String str=sw.toString();
		str=str.replace("\\", "/");
		str=str.replace("&amp;#8203;", "&#8203;");//jaxb下属的Marshaller 会把特殊字符，比如&等特殊字符替换为&amp;，由于需要使用不占位分隔符&#8203;避免出现单元格溢出，所以要替换
		str=str.replace("&amp;#160;", "&#160;");//jaxb下属的Marshaller 会把特殊字符，比如&等特殊字符替换为&amp;，由于需要使用空格字符&#160;，所以要替换
		File xmlFile = File.createTempFile("tempXml", ".xml");
		Writer writer=new FileWriter(xmlFile);
		writer.write(str);
		writer.flush();
		writer.close();

		FopFactory factory = FopFactory.newInstance(new File("src//main//resources//conf//fop.xml"));
		OutputStream out = new FileOutputStream(file);
		out = new BufferedOutputStream(out);
		Fop fop = factory.newFop(MimeConstants.MIME_PDF, out);

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		Source source = new StreamSource(xmlFile);
		Result res = new SAXResult(fop.getDefaultHandler());
		transformer.transform(source, res);
		out.close();
		if(xmlFile.exists()){
			boolean del = xmlFile.delete();
			String delResult = del?"Delete Success":"Delete Failure";
			System.out.println(delResult);
		}
		return  file;
	}
}

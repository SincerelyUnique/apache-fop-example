package com.levent.fop;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;

public class FOPPdfDemo {

	private static final String RESOURCES_DIR;
	private static final String OUTPUT_DIR;
	
	static {
		// 资源路径
		RESOURCES_DIR = "src//main//resources//";
		// 输出路径
		OUTPUT_DIR = "src//main//resources//output//";
	}

	/**
	 * 测试方法
	 */
	public static void main(String[] args) {
		FOPPdfDemo fOPPdfDemo = new FOPPdfDemo();
		try {
			fOPPdfDemo.convertToFO();
			fOPPdfDemo.convertToPDF();
		} catch (FOPException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将指定的XML文件转为PDF文件
	 *
	 * @throws IOException IOE
	 * @throws FOPException FOPE
	 * @throws TransformerException TE
	 */
	public void convertToPDF() throws IOException, FOPException, TransformerException {
		// the XSL FO file
		File xsltFile = new File(RESOURCES_DIR + "//template.xsl");
		// the XML file which provides the input
		StreamSource xmlSource = new StreamSource(new File(RESOURCES_DIR + "//Employees.xml"));
		// create an instance of fop factory
		FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
		// a user agent is needed for transformation
		FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
		// Setup output
		OutputStream out;
		out = new java.io.FileOutputStream(OUTPUT_DIR + "//employee.pdf");

		try {
			// Construct fop with desired output format
			Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);

			// Setup XSLT
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer(new StreamSource(xsltFile));

			// Resulting SAX events (the generated FO) must be piped through to
			// FOP
			Result res = new SAXResult(fop.getDefaultHandler());

			// Start XSLT transformation and FOP processing
			// That's where the XML is first transformed to XSL-FO and then
			// PDF is created
			transformer.transform(xmlSource, res);
		} finally {
			out.close();
		}
	}

	/**
	 * 将指定的XML文件转为XSL-FO文件
	 *
	 * @throws IOException	IOE
	 * @throws FOPException FOPE
	 * @throws TransformerException TE
	 */
	private void convertToFO() throws IOException, FOPException, TransformerException {
		// the XSL FO file
		File xsltFile = new File(RESOURCES_DIR + "//template.xsl");

		/*
		 * TransformerFactory factory = TransformerFactory.newInstance();
		 * Transformer transformer = factory.newTransformer(new
		 * StreamSource("F:\\Temp\\template.xsl"));
		 */

		// the XML file which provides the input
		StreamSource xmlSource = new StreamSource(new File(RESOURCES_DIR + "//Employees.xml"));

		// a user agent is needed for transformation
		/* FOUserAgent foUserAgent = fopFactory.newFOUserAgent(); */
		// Setup output
		OutputStream out;

		out = new java.io.FileOutputStream(OUTPUT_DIR + "temp.fo");

		try {
			// Setup XSLT
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer(new StreamSource(xsltFile));

			// Resulting SAX events (the generated FO) must be piped through to
			// FOP
			// Result res = new SAXResult(fop.getDefaultHandler());

			Result res = new StreamResult(out);

			// Start XSLT transformation and FOP processing
			transformer.transform(xmlSource, res);

			// Start XSLT transformation and FOP processing
			// That's where the XML is first transformed to XSL-FO and then
			// PDF is created
			transformer.transform(xmlSource, res);
		} finally {
			out.close();
		}
	}

}

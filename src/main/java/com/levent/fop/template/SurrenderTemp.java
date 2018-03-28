package com.levent.fop.template;

import org.plutext.jaxb.xslfo.BackgroundRepeatType;
import org.plutext.jaxb.xslfo.Block;
import org.plutext.jaxb.xslfo.Flow;
import org.plutext.jaxb.xslfo.LayoutMasterSet;
import org.plutext.jaxb.xslfo.Leader;
import org.plutext.jaxb.xslfo.LeaderPatternType;
import org.plutext.jaxb.xslfo.PageSequence;
import org.plutext.jaxb.xslfo.RegionAfter;
import org.plutext.jaxb.xslfo.RegionBefore;
import org.plutext.jaxb.xslfo.RegionBody;
import org.plutext.jaxb.xslfo.RegionEnd;
import org.plutext.jaxb.xslfo.RegionStart;
import org.plutext.jaxb.xslfo.Root;
import org.plutext.jaxb.xslfo.SimplePageMaster;
import org.plutext.jaxb.xslfo.StaticContent;

import java.io.*;

public class SurrenderTemp {

	/**
	 * 返回一个Root对象
	 */
	public Root createRoot() throws Exception{
		Root root = new Root();
		root.setFontFamily("SimSun"); //设置字体
		LayoutMasterSet layMas = new LayoutMasterSet();
		SimplePageMaster simplePageMaster = new SimplePageMaster();
		simplePageMaster = buildPageLayout(simplePageMaster);
		layMas.getSimplePageMasterOrPageSequenceMaster().add(simplePageMaster);
		root.setLayoutMasterSet(layMas);
		root.getPageSequence().add(createPageSequence(new PageSequence()));
		return root;
	}

	/**
	 * 构建页面布局
	 */
	private SimplePageMaster buildPageLayout( SimplePageMaster simplePageMaster) {
		
		simplePageMaster.setMasterName("healthy");
		simplePageMaster.setPageWidth("210mm");//页宽
		simplePageMaster.setPageHeight("300mm");//页高
		simplePageMaster.setMarginTop("4mm");//页上边距
		simplePageMaster.setMarginBottom("4mm");//页下边距
		simplePageMaster.setMarginLeft("8mm");//左边距
		simplePageMaster.setMarginRight("8mm");//右边距
		
		RegionBody  regionBody = new RegionBody();
		/*regionBody.setBackgroundImage("file:D:"+"\\image\\DRAFT.png");*/
		regionBody.setBackgroundRepeat(BackgroundRepeatType.NO_REPEAT); //设置不平铺
		regionBody.setBackgroundPositionHorizontal("50%");//水平居中
		regionBody.setBackgroundPositionVertical("50%");//竖直居中
		
		regionBody.setMarginTop("1.6cm");//上边距
		regionBody.setMarginBottom("2.5cm");//页底部边距
		regionBody.setMarginLeft("15pt");//左边距
		regionBody.setMarginRight("15pt");//右边距
		
		simplePageMaster.setRegionBody(regionBody);
		
		RegionBefore rbefore = new RegionBefore();
		rbefore.setExtent("1.6cm"); //页眉
		simplePageMaster.setRegionBefore(rbefore);
		
		RegionAfter rafter = new RegionAfter();
		rafter.setExtent("2.5cm");//页脚
		simplePageMaster.setRegionAfter(rafter);
		
		RegionStart rstart = new RegionStart();
		rstart.setExtent("15pt");//左侧栏
		simplePageMaster.setRegionStart(rstart);
		
		RegionEnd rend = new RegionEnd();
		rend.setExtent("15pt");
		simplePageMaster.setRegionEnd(rend);
		
		return simplePageMaster;
	}

	/**
	 * 组装page-sequence对象
	 */
	private PageSequence createPageSequence(PageSequence pageSequence) throws Exception{
		pageSequence.setMasterReference("healthy");
		for(int i=0 ;i<2 ;i++){
			StaticContent  staticContent = new StaticContent();
			if(i==0){
				pageSequence.getStaticContent().add(pageHeader(staticContent));
			}else {
				pageSequence.getStaticContent().add(pageFooter(staticContent));
			}
		}
			Flow flow = new Flow();
			pageSequence.setFlow(makeFirstFlow(flow));
		
		return pageSequence;
	}

	//页眉
	private StaticContent pageHeader(StaticContent content){
		content.setFlowName("xsl-region-before");
		Block block = new Block();
		content.getBlockOrBlockContainerOrTable().add(block);
		return content;
	}

	// 页脚
	private StaticContent pageFooter(StaticContent content){
		Block block;
		content.setFlowName("xsl-region-after");
		block = new Block();
		content.getBlockOrBlockContainerOrTable().add(block);
		return content;
	}

	/**
	 * 构建flow标签及其属性和子标签
	 */
	private Flow makeFirstFlow(Flow flow) throws Exception{
		Block block;
		flow.setFlowName("xsl-region-body");

		for(int i=0;i<1;i++){
			block = new Block();
			block.setFontSize("7pt");
			block.setLineHeight("12pt");
			block.setFontFamily("msyh");
			String content = readTxtFile("src//main//resources//test//test1//arti.txt");
			block.getContent().add(content);
			flow.getMarkerOrBlockOrBlockContainer().add(block);
		}

		Leader leader2 = new Leader();
		drawLine(leader2);
		block = new Block();
		block.getContent().add(leader2);
		flow.getMarkerOrBlockOrBlockContainer().add(block);

		return flow;
	}
	
	/**
	 * 划线
	 */
	private void drawLine(Leader leader2) {
		leader2.setLeaderPattern(LeaderPatternType.RULE);
		leader2.getLeaderLength().add("80%");
		leader2.setLeaderPatternWidth("0.001mm");
		leader2.setColor("black");
	}

	/**
	 * 读取txt
	 */
	private String readTxtFile( String filePath ) throws IOException{
		File file = new File(filePath);//定义一个file对象，用来初始化FileReader
		//FileReader reader = new FileReader(file);//定义一个fileReader对象，用来初始化BufferedReader
		InputStreamReader reader = new InputStreamReader(new FileInputStream(file), "gbk");
		BufferedReader bReader = new BufferedReader(reader);//new一个BufferedReader对象，将文件内容读取到缓存
		StringBuilder sb = new StringBuilder();//定义一个字符串缓存，将字符串存放缓存中
		String s = "";
		while ((s =bReader.readLine()) != null) {//逐行读取文件内容，不读取换行符和末尾的空格
			sb.append(s + "\n");//将读取的字符串添加换行符后累加存放在缓存中
			System.out.println(s);
		}
		bReader.close();
		String str = sb.toString();
		return str;
	}
}

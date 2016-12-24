import org.w3c.dom.*;
import java.io.*;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import static java.lang.System.out;

public class treefy
{
	public static void main(String args[])
	{
		String fName = "webcontent.xml";
		if(args.length > 0)
		{
			if(args[0].endsWith(".xml"))
			{
				fName = args[0];
			}
		}
		ArrayList<PageNode> pageArray = new ArrayList<PageNode>();
		ArrayList<PageNode> ded = new ArrayList<PageNode>();
		try
		{
			FileInputStream fis = new FileInputStream(fName);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			Document sitemap = dbf.newDocumentBuilder().parse(fis);
			Element root = sitemap.getDocumentElement();
			NodeList kids = root.getChildNodes();
			ArrayList<Node> nodeList = new ArrayList<Node>();

			for(int ind = 0; ind < kids.getLength(); ind++)
			{
				if(kids.item(ind).hasAttributes())
					nodeList.add(kids.item(ind));
			}

			for(int ind = 0; ind < nodeList.size(); ind++)
			{
				PageNode item;
				String title;
				String url_path;
				String content = null;
				int id;
				int pid;
				String tid;
				String tpid;

				Node cur = nodeList.get(ind);
				NamedNodeMap attrs = cur.getAttributes();
				tid = attrs.getNamedItem("id").getNodeValue();
				tpid = attrs.getNamedItem("pid").getNodeValue();
				url_path = attrs.getNamedItem("UrlPath").getNodeValue();
				title = attrs.getNamedItem("title").getNodeValue();
				id = Integer.parseInt(tid);
				pid = Integer.parseInt(tpid);

				if(nodeList.get(ind).hasChildNodes())
					content = cur.getNodeValue();

				item = new PageNode(title,url_path,content,id,pid);
				pageArray.add(item);
			}

			fis.close();
		}catch(Exception e)
		{
			e.printStackTrace();
		}

		PageNode root = pageArray.get(0);
		pageArray.remove(0);
		while(pageArray.size() > 0)
		{
			for(int ind = 0; ind < pageArray.size(); ind++)
			{
				PageNode cur = pageArray.get(ind);
				if(cur.getid() == root.getpid())
				{
					cur.addChild(root);
					root = cur;
					pageArray.remove(ind--);
				}else if( root.getid() == cur.getpid() )
				{
					root.addChild(cur);
					pageArray.remove(ind--);
				}else
				{
					PageNode parent = root.findID(cur.getpid(),true);
					if(parent != null)
					{
						parent.addChild(cur);
						pageArray.remove(ind--);
					}else
					{
						boolean exists = false;
						for(int ind0 = 0; ind0 < pageArray.size(); ind0++)
						{
							if(pageArray.get(ind0).getid() == cur.getpid())
							{
								exists = true;
								break;
							}
						}
						if(!exists)
						{
							ded.add(pageArray.get(ind));
							pageArray.remove(ind--);

						}
					}
				}
			}
		}

		root.setURLs();
		try
		{
			PrintWriter fos = new PrintWriter("SiteDead.txt");

			for(int ind = 0; ind < ded.size(); ind++)
			{
				fos.println(ded.get(ind).toString());
			}
			fos.close();
		}catch(Exception e)
		{
			e.printStackTrace();
		}

		output(root);
	}

	public static void output(PageNode root)
	{
		try
		{
			PrintWriter pw = new PrintWriter("Tree.csv");
			pw.println("Title;Template Name;ID;PID;URL");
			root.walk(pw);
			pw.close();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}

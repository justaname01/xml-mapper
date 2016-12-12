import java.util.ArrayList;
import static java.lang.System.out;

public class PageNode
{
	private String title;
	private String url_path;
	private String content;
	private String template_name;
	private int id;
	private int pid;
	private ArrayList<PageNode> children;
	private PageNode parent;
	private PageNode lastRes;

	public PageNode(String name, String url, String text, int nid, int npid)
	{
		title = name;
		url_path = url;
		content = text;
		template_name = "";
		id = nid;
		pid = npid;
		children = new ArrayList<PageNode>();
		lastRes = null;
	}

	public ArrayList<PageNode> getChildren()
	{
		return children;
	}

	public int getid()
	{
		return id;
	}

	public int getpid()
	{
		return pid;
	}

	public String getURL()
	{
		return url_path;
	}

	private void setURL(String path)
	{
		if(path != null)
			url_path = path;
	}

	public boolean hasContent()
	{
		return content != null;
	}

	public String getContent()
	{
		return content;
	}

	public String getTemplateName()
	{
		return template_name;
	}

	public void setTemplateName(String newName)
	{
		if(newName != null)
		{
			template_name = newName;
		}
	}

	public PageNode getParent()
	{
		return parent;
	}

	public PageNode getLastSearch()
	{
		return lastRes;
	}

	public void addChild(PageNode data)
	{
		if(data != null)
		{
			children.add(data);
			data.addParent(this);
		}
	}

	private void addParent(PageNode data)
	{
		if(data != null)
		{
			parent = data;
		}
	}

	public int getSize(boolean isTop)
	{
		int sum = 1;
		if(children.size() == 0)
			return 1;
		else
		{
			for(int ind = 0;ind < children.size(); ind++)
			{
				sum += children.get(ind).getSize(false);
			}
			return sum;
		}
	}

	public PageNode findID(int sid,boolean isTop)
	{
		if(id == sid)
			return this;
		else
		{
			PageNode return_value = null;
			for(int ind = 0; ind < children.size(); ind++)
			{
				return_value = children.get(ind).findID( sid, false );
				if(return_value != null)
					break;
			}
			return return_value;
		}
	}

	public void walk(int lvl)
	{
		for(int ind = 0; ind < lvl; ind++)
			out.print("\t");
		out.println(toString());
		for(int ind = 0; ind < children.size(); ind++)
			children.get(ind).walk(lvl+1);
	}

	public void walk(java.io.PrintWriter writer,int lvl)
	{
		for(int ind = 0; ind < lvl; ind++)
			writer.print("\t");
		writer.println(toString());
		for(int ind = 0; ind < children.size(); ind++)
			children.get(ind).walk(writer,lvl+1);
	}

	public void walk(java.io.PrintWriter writer)
	{
		writer.println( title + ";" + template_name +";"+id+";"+pid+";"+url_path);
		for(int ind = 0; ind < children.size(); ind++)
			children.get(ind).walk(writer);
	}

	public void setURLs()
	{
		if(children.size() != 0)
		{
			for(int ind = 0; ind < children.size(); ind++)
			{
				children.get(ind).setURL(getURL() + "/" + children.get(ind).getURL());
				children.get(ind).setURLs();
			}
			
			setURL(getURL()+"/index.html");
		}else
		{
			setURL(getURL()+".html");
		}
	}

	public String toString()
	{
		String ret = "id = " + id + "\t pid = " + pid + "\t title = " + title + "\t url = " + url_path + "\t text = " + content;
		return ret;
	}
}
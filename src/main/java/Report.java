import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import com.opencsv.CSVWriter;


public class Report 
{private static long startTime = System.currentTimeMillis();
	private static Class[] getClasses(String packageName) throws ClassNotFoundException, IOException 
	{
	    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	    assert classLoader != null;
	    String path = packageName.replace('.', '/');
	    Enumeration<URL> resources = classLoader.getResources(path);
	    List<File> dirs = new ArrayList<File>();
	    while (resources.hasMoreElements()) 
	    {
	        URL resource = resources.nextElement();
	        dirs.add(new File(resource.getFile()));
	    }
	    ArrayList<Class> classes = new ArrayList<Class>();
	    for (File directory : dirs) 
	    {
	        classes.addAll(findClasses(directory, packageName));
	    }
	    return classes.toArray(new Class[classes.size()]);
	}

	private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException 
	{
	    List<Class> classes = new ArrayList<Class>();
	    if (!directory.exists()) 
	    {
	        return classes;
	    }
	    File[] files = directory.listFiles();
	    for (File file : files) 
	    {
	        if (file.isDirectory()) 
	        {
	            assert !file.getName().contains(".");
	            classes.addAll(findClasses(file, packageName + "." + file.getName()));
	        } 
	        else if (file.getName().endsWith(".class")) 
	        {
	            classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
	        }
	    }
	    return classes;
	}
	
	public static void getAnnotationValue(String filePath) throws ClassNotFoundException, IOException
	{
		File file = new File(filePath);
		FileWriter outputfile = new FileWriter(file);
        CSVWriter writer = new CSVWriter(outputfile);
		Class[] classes = getClasses("com.citi.cpb");
		//System.out.println(classes.length);
		List<String[]> data = new ArrayList<String[]>(); 
		data.add(new String[] { "Class", "Field", "Annotation" }); 
		data.add(new String[] { " ", " ", " " }); 
	
		for(int i=0;i<classes.length;i++) 
		{
			//System.out.println(classes[i]);
			Field[] fields = classes[i].getDeclaredFields();
			 for(int k=0;k<fields.length;k++) 
				{
				    Field element = (Field) fields[k];
				    //System.out.print(element.getName()+"			");
				    Annotation[] annotations = element.getDeclaredAnnotations();
				    if(annotations.length==0)
				    {
				    	data.add(new String[] {classes[i].toString(),element.getName(),"@com.citi.cpb.annotations.PII(value=\"PII\")"});
				    }
				    else
				    {
				    	for(int j=0;j<annotations.length;j++)
					    {
					    	//System.out.println( annotations[j]);
					    	data.add(new String[] {classes[i].toString(),element.getName(), annotations[j].toString()});
					    }
				    }
				   // System.out.println("");
				}
			// System.out.println("\n");
		}
		writer.writeAll(data);
        writer.close();
	}
		
	public static void main(String[] args) throws Exception 
	{
		getAnnotationValue("C://Users//Public//data.csv");
		long endTime = System.currentTimeMillis();
        System.out.println("It took " + (endTime - startTime) + " milliseconds");
	}
}
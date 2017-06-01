
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 *
 */
@Path("")
public class App
{
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	@Path("/createproject")
	public Response createproject (Project project)
	{
	
		JSONObject obj = new JSONObject();
		obj.put("id", project.getId());
		obj.put("projectName", project.getProjectName());
		obj.put("creationDate", project.getCreationDate());
		obj.put("expiryDate", project.getExpiryDate());
		obj.put("enabled", project.isEnabled());
		obj.put("projectCost", project.getProjectCost());
		obj.put("projectUrl", project.getProjectUrl());
	
		JSONArray countries = new JSONArray();
		for(String country: project.getTargetCountries())
			countries.add(country);
		
		obj.put("targetCountries", countries);
	
		JSONArray targetKeys = new JSONArray();
		for(TargetKeys key: project.getTargetKeys()){
			JSONObject tmpObj = new JSONObject();
			tmpObj.put("number", key.getNumber());
			tmpObj.put("keyword", key.getKeyword());
			targetKeys.add(tmpObj);
		}
		obj.put("targetKeys", targetKeys);
		
		try (FileWriter file = new FileWriter("C:/Users/shach/workspace/ApiTesting/project.txt",true)) {
			BufferedWriter bw = new BufferedWriter(file);
		
			file.write(obj.toJSONString()+"\n");
			System.out.println("Write to to File...");
		} 
		catch (IOException e) {
			e.printStackTrace();
		}

	  return Response.ok().build();
	  }
    @GET
    @Produces("application/json")
    @Path("/requestProject")
    public ArrayList<ProjectResponse> requestProject (@QueryParam("projectid") Long projectid, @QueryParam("country") String country,@QueryParam("number") Long number,
    		@QueryParam("keyword") String keyword)
    {
        ArrayList<Project> projects = new ArrayList<Project>();
        Set<Long> matchedIds = new HashSet<Long>();
        ArrayList<ProjectResponse> projectResponses = new ArrayList<ProjectResponse>();

        JSONParser parser = new JSONParser();
        
        try {

        	
        	BufferedReader br = new BufferedReader(new FileReader("C:/Users/"
        			+ "/workspace/ApiTesting/project.txt"));
        	String line;
        	
	        	while( (line =br.readLine())!=null){
	        		int criteriaCnt = 0;
	        		int nonNullCriterias = 3;
		            Project project = new Project();
		            ProjectResponse projectResponse = new ProjectResponse();
	
		        	Object obj = JSONValue.parse(line);
		            JSONObject jsonObject = (JSONObject) obj;
		            long id = (Long)jsonObject.get("id");
		            project.setId(id);
		            
		            project.setCreationDate((String)jsonObject.get("creationDate"));
		            project.setEnabled((boolean)jsonObject.get("enabled"));
		
		            project.setExpiryDate((String)jsonObject.get("expiryDate"));
		            
		            project.setProjectCost((Double)jsonObject.get("projectCost"));
		            project.setProjectName((String)jsonObject.get("projectName"));
		            project.setProjectUrl(((String)jsonObject.get("projectUrl")));
		            
		            projectResponse.setProjectCost((Double)jsonObject.get("projectCost"));
		            projectResponse.setProjectName((String)jsonObject.get("projectName"));
		            projectResponse.setProjectUrl(((String)jsonObject.get("projectUrl")));
		
		            JSONArray jsonArray = (JSONArray)jsonObject.get("targetCountries");
		            String[] countries = new String[jsonArray.size()];
		            for(int i =0;i<jsonArray.size();i++){
		            	String cont = (String)jsonArray.get(i);
		            	if(country!=null && country.toLowerCase().equals(cont.toLowerCase())){
		            		criteriaCnt++;
		            	}
		            	else if(country == null && i == 0 ){
		            		nonNullCriterias--;
		            		System.out.println("Count null");
		            	}
		            	countries[i] = (String)jsonArray.get(i);
		            }
		             
		            project.setTargetCountries(countries);
		            JSONArray keysJsonArray = (JSONArray)jsonObject.get("targetKeys");
		            TargetKeys[] targetKeys = new TargetKeys[jsonArray.size()];
		            Iterator<TargetKeys> iterator = keysJsonArray.iterator();
		            int i =0;
		            for(Object ob : keysJsonArray){
		            	TargetKeys key = new TargetKeys();
		                JSONObject json = (JSONObject)ob;	
		                System.out.println(number+"number..other"+(Long)json.get("number"));	
		                if(number!= null && number <= (Long)json.get("number")){
		                	System.out.println("Increasing cr for number");
		                	criteriaCnt++;		                	
		                }
		                else if(number == null && i ==0){
		                	nonNullCriterias--;
		            		System.out.println("number null");

		                }
		                String keyW = (String)json.get("keyword");
		                if(keyword!=null && keyword.toLowerCase().equals(keyW.toLowerCase())){
		                	criteriaCnt++;
		                }
		                else if(keyword == null && i == 0 ){
		            		System.out.println("keyword null");
		                	nonNullCriterias--;
		                }
		                	
		            	key.setKeyword((String)json.get("keyword"));
		            	key.setNumber((Long)json.get("number"));
		            	targetKeys[i] = key;
		            	i++;
	            }
	        	project.setTargetKeys(targetKeys);
	        	projects.add(project);	
	        	projectResponses.add(projectResponse);
	        	
        		System.out.println(criteriaCnt+" match.."+nonNullCriterias);

	        	if(criteriaCnt >= nonNullCriterias){
	        		matchedIds.add(id);
	        		System.out.println("adding id.."+id);
	            }

	        	
	            }
        	if(projectid == null && country == null && number ==null && keyword == null)
                return projectResponses;
        	else
        	return filterResponses(projects,projectid, country, number, keyword, matchedIds);
        	

        } catch (IOException e) {
            e.printStackTrace();
        } 


        return null;
    }
    public ArrayList<ProjectResponse> filterResponses(ArrayList<Project> projects,Long projectid, String country,Long number,String keyword, Set<Long> matchedIds)
    {
    	ArrayList<ProjectResponse> projectResponses = new ArrayList<ProjectResponse>();
    	if(projectid !=null){
    		for(Project project: projects){
    			ProjectResponse projectResponse = new ProjectResponse();
		    	System.out.println(projectid+"add Filtering..."+project.getId());

    			if(project.getId() == projectid  && project.isEnabled()){

    				projectResponse.setProjectName(project.getProjectName());
    				projectResponse.setProjectUrl(project.getProjectUrl());
    				projectResponse.setProjectCost(project.getProjectCost());	
    				projectResponses.add(projectResponse);
    			}
    		}
    		return projectResponses;
    	}
    	else {
    		for(Project project: projects){
    			ProjectResponse projectResponse = new ProjectResponse();
    			Date today = new Date();
    			Date expiryDate = null;
    			
    			if(matchedIds.contains(project.getId()) && project.isEnabled()){
    				projectResponse.setProjectName(project.getProjectName());
    				projectResponse.setProjectUrl(project.getProjectUrl());
    				projectResponse.setProjectCost(project.getProjectCost());	
    				projectResponses.add(projectResponse);
    			}

    		}
    		return projectResponses;
    		
    		
    	}
    	
    }
}
